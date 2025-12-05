package org.allbluehu.mse.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    
    @Override
    public void customize(JwtEncodingContext context) {
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            customizeAccessToken(context);
        } else if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            customizeIdToken(context);
        }
    }
    
    private void customizeAccessToken(JwtEncodingContext context) {
        Authentication principal = context.getPrincipal();
        JwtClaimsSet.Builder claims = context.getClaims();
        
        // 添加标准声明
        claims.claim("auth_time", Instant.now().getEpochSecond());
        
        // 添加用户角色
        if (principal != null && principal.getAuthorities() != null) {
            Set<String> authorities = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
            claims.claim("authorities", authorities);
        }
        
        // 添加客户端信息
        claims.claim("client_id", context.getRegisteredClient().getClientId());
    }
    
    private void customizeIdToken(JwtEncodingContext context) {
        JwtClaimsSet.Builder claims = context.getClaims();
        Authentication principal = context.getPrincipal();
        
        // 添加标准OIDC声明
        claims.claim(IdTokenClaimNames.AUTH_TIME, Instant.now().getEpochSecond());
        
        // 添加自定义声明
        claims.claim("auth_method", "oauth2");
        
        if (principal != null) {
            claims.claim("login_ip", getClientIp(context));
            
            // 添加用户信息
            if (principal.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                OAuth2User oauth2User = 
                    (org.springframework.security.oauth2.core.user.OAuth2User) principal.getPrincipal();
                claims.claim("name", oauth2User.getAttribute("name"));
                claims.claim("nickname", oauth2User.getAttribute("nickname"));
                claims.claim("picture", oauth2User.getAttribute("picture"));
            }
        }
    }

    
    private String getClientIp(JwtEncodingContext context) {
        // 从请求中获取客户端IP
        // 实际实现中需要从HttpServletRequest中获取
        return "127.0.0.1";
    }
}