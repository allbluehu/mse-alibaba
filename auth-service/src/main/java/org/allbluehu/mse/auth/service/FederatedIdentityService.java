package org.allbluehu.mse.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.allbluehu.mse.auth.entity.SocialConnection;
import org.allbluehu.mse.auth.entity.User;
import org.allbluehu.mse.auth.repository.SocialConnectionRepository;
import org.allbluehu.mse.auth.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FederatedIdentityService extends DefaultOAuth2UserService {
    
    private final UserRepository userRepository;
    private final SocialConnectionRepository socialConnectionRepository;
    private final UserService userService;
    
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 获取第三方用户信息
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        // 2. 提取信息
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oauth2User.getAttributes();
        String providerUserId = extractProviderUserId(registrationId, attributes);
        
        log.info("第三方登录: provider={}, providerUserId={}", registrationId, providerUserId);
        
        // 3. 查找现有关联
        SocialConnection connection = socialConnectionRepository
            .findByProviderAndProviderUserId(registrationId, providerUserId)
            .orElseGet(() -> createNewConnection(registrationId, providerUserId, attributes));
        
        // 4. 更新连接信息
        updateConnectionInfo(connection, userRequest, attributes);
        
        // 5. 获取或创建用户
        User user = connection.getUser();
        if (user == null) {
            user = userService.createUserFromSocial(registrationId, attributes);
            connection.setUser(user);
            socialConnectionRepository.save(connection);
        }
        
        // 6. 转换为Spring Security用户
        return userService.createOAuth2User(user, attributes);
    }
    
    private String extractProviderUserId(String provider, Map<String, Object> attributes) {
        switch (provider.toLowerCase()) {
            case "wechat":
                return (String) attributes.get("openid");
            case "douyin":
                return (String) attributes.get("open_id");
            case "github":
                return attributes.get("id").toString();
            case "google":
                return (String) attributes.get("sub");
            default:
                return attributes.get("sub") != null ? 
                    attributes.get("sub").toString() : 
                    attributes.get("id").toString();
        }
    }
    
    private SocialConnection createNewConnection(String provider, String providerUserId, Map<String, Object> attributes) {
        SocialConnection connection = new SocialConnection();
        connection.setProvider(provider);
        connection.setProviderUserId(providerUserId);
        connection.setProviderUserName(extractUserName(provider, attributes));
        connection.setProfileData(convertToJson(attributes));
        connection.setConnectedAt(LocalDateTime.now());
        connection.setLastLoginAt(LocalDateTime.now());
        return connection;
    }
    
    private void updateConnectionInfo(SocialConnection connection, OAuth2UserRequest userRequest, Map<String, Object> attributes) {
        connection.setAccessToken(userRequest.getAccessToken().getTokenValue());
        connection.setExpiresIn((int) userRequest.getAccessToken().getExpiresAt()
            .minusSeconds(System.currentTimeMillis() / 1000).getEpochSecond());
        connection.setLastLoginAt(LocalDateTime.now());
        
//        if (userRequest.getAccessToken().getRefreshToken() != null) {
//            connection.setRefreshToken(userRequest.getAccessToken().getRefreshToken().getTokenValue());
//        }
        
        // 更新用户信息
        connection.setProviderUserName(extractUserName(connection.getProvider(), attributes));
        connection.setProfileData(convertToJson(attributes));
    }
    
    private String extractUserName(String provider, Map<String, Object> attributes) {
        switch (provider.toLowerCase()) {
            case "wechat":
                return (String) attributes.get("nickname");
            case "douyin":
                return (String) attributes.get("nickname");
            case "github":
                return (String) attributes.get("login");
            default:
                return (String) attributes.get("name");
        }
    }
    
    private String convertToJson(Map<String, Object> attributes) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(attributes);
        } catch (Exception e) {
            log.warn("Failed to convert attributes to JSON", e);
            return "{}";
        }
    }
}