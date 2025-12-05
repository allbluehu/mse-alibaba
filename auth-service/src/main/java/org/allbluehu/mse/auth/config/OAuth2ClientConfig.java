package org.allbluehu.mse.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OAuth2ClientConfig {
    
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = Arrays.asList(
            wechatClientRegistration(),
            douyinClientRegistration(),
            githubClientRegistration(),
            googleClientRegistration()
        );
        
        return new InMemoryClientRegistrationRepository(registrations);
    }
    
    private ClientRegistration wechatClientRegistration() {
        return ClientRegistration.withRegistrationId("wechat")
            .clientId("你的微信AppID")
            .clientSecret("你的微信AppSecret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope("snsapi_login")
            .authorizationUri("https://open.weixin.qq.com/connect/qrconnect")
            .tokenUri("https://api.weixin.qq.com/sns/oauth2/access_token")
            .userInfoUri("https://api.weixin.qq.com/sns/userinfo")
            .userNameAttributeName("openid")
            .clientName("微信")
            .build();
    }
    
    private ClientRegistration douyinClientRegistration() {
        return ClientRegistration.withRegistrationId("douyin")
            .clientId("你的抖音ClientKey")
            .clientSecret("你的抖音ClientSecret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope("user_info")
            .authorizationUri("https://open.douyin.com/platform/oauth/connect/")
            .tokenUri("https://open.douyin.com/oauth/access_token/")
            .userInfoUri("https://open.douyin.com/api/userinfo/")
            .userNameAttributeName("open_id")
            .clientName("抖音")
            .build();
    }
    
    private ClientRegistration githubClientRegistration() {
        return ClientRegistration.withRegistrationId("github")
            .clientId("你的GitHub Client ID")
            .clientSecret("你的GitHub Client Secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope("read:user", "user:email")
            .authorizationUri("https://github.com/login/oauth/authorize")
            .tokenUri("https://github.com/login/oauth/access_token")
            .userInfoUri("https://api.github.com/user")
            .userNameAttributeName("id")
            .clientName("GitHub")
            .build();
    }
    
    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
            .clientId("你的Google Client ID")
            .clientSecret("你的Google Client Secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope("openid", "profile", "email")
            .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("https://oauth2.googleapis.com/token")
            .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
            .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
            .userNameAttributeName("sub")
            .clientName("Google")
            .build();
    }
}