package org.allbluehu.mse.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.allbluehu.mse.auth.entity.OAuth2Client;
import org.allbluehu.mse.auth.repository.OAuth2ClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
    
    private final OAuth2ClientRepository oauth2ClientRepository;
    private final ObjectMapper objectMapper;
    
    @Override
    public void save(RegisteredClient registeredClient) {
        OAuth2Client entity = convertToEntity(registeredClient);
        oauth2ClientRepository.save(entity);
    }
    
    @Override
    public RegisteredClient findById(String id) {
        return oauth2ClientRepository.findById(id)
            .map(this::convertFromEntity)
            .orElse(null);
    }
    
    @Override
    public RegisteredClient findByClientId(String clientId) {
        return oauth2ClientRepository.findByClientId(clientId)
            .map(this::convertFromEntity)
            .orElse(null);
    }
    
    private OAuth2Client convertToEntity(RegisteredClient registeredClient) {
        OAuth2Client entity = new OAuth2Client();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientName(registeredClient.getClientName());
        
        registeredClient.getClientAuthenticationMethods().forEach(method ->
            entity.getClientAuthenticationMethods().add(method.getValue()));
        
        registeredClient.getAuthorizationGrantTypes().forEach(grantType ->
            entity.getAuthorizationGrantTypes().add(grantType.getValue()));
        
        entity.setRedirectUris(registeredClient.getRedirectUris());
        entity.setScopes(registeredClient.getScopes());
        entity.setPostLogoutRedirectUris(registeredClient.getPostLogoutRedirectUris());
        
        try {
            entity.setClientSettings(objectMapper.writeValueAsString(
                registeredClient.getClientSettings().getSettings()));
            entity.setTokenSettings(objectMapper.writeValueAsString(
                registeredClient.getTokenSettings().getSettings()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert settings to JSON", e);
        }
        
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        
        return entity;
    }
    
    private RegisteredClient convertFromEntity(OAuth2Client entity) {
        RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId())
            .clientId(entity.getClientId())
            .clientSecret(entity.getClientSecret())
            .clientName(entity.getClientName());
        
        entity.getClientAuthenticationMethods().forEach(method ->
            builder.clientAuthenticationMethod(new ClientAuthenticationMethod(method)));
        
        entity.getAuthorizationGrantTypes().forEach(grantType ->
            builder.authorizationGrantType(new AuthorizationGrantType(grantType)));
        
        entity.getRedirectUris().forEach(builder::redirectUri);
        entity.getScopes().forEach(builder::scope);
        entity.getPostLogoutRedirectUris().forEach(builder::postLogoutRedirectUri);
        
        try {
            if (StringUtils.hasText(entity.getClientSettings())) {
                Map<String, Object> clientSettingsMap = objectMapper.readValue(
                    entity.getClientSettings(), new TypeReference<Map<String, Object>>() {});
                builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());
            }
            
            if (StringUtils.hasText(entity.getTokenSettings())) {
                Map<String, Object> tokenSettingsMap = objectMapper.readValue(
                    entity.getTokenSettings(), new TypeReference<Map<String, Object>>() {});
                builder.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to settings", e);
        }
        
        builder.clientIdIssuedAt(entity.getClientIdIssuedAt());
        builder.clientSecretExpiresAt(entity.getClientSecretExpiresAt());
        
        return builder.build();
    }
}