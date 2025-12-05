package org.allbluehu.mse.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOidcUserInfoService extends OidcUserService {
//
//    private final UserDetailsService userDetailsService;
//
//    public CustomOidcUserInfoService(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    public OidcUserInfo loadUser(OidcUserInfoAuthenticationContext context) {
//        OAuth2Authorization authorization = context.getAuthorization();
//        Authentication principal = authorization.getAttribute(Principal.class.getName());
//
//        // 从数据库或其他来源获取用户详细信息
//        UserDetails userDetails = userDetailsService.loadUserByUsername(
//            principal.getName());
//
//        // 构建 OIDC 标准用户信息
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(OidcStandardClaimNames.SUB, userDetails.getUsername());
//        claims.put(OidcStandardClaimNames.PREFERRED_USERNAME, userDetails.getUsername());
//        claims.put(OidcStandardClaimNames.NAME, "张三"); // 从数据库获取
//        claims.put(OidcStandardClaimNames.GIVEN_NAME, "三");
//        claims.put(OidcStandardClaimNames.FAMILY_NAME, "张");
//        claims.put(OidcStandardClaimNames.EMAIL, "zhangsan@example.com");
//        claims.put(OidcStandardClaimNames.EMAIL_VERIFIED, true);
//        claims.put(OidcStandardClaimNames.PHONE_NUMBER, "+86 13800138000");
//        claims.put(OidcStandardClaimNames.PHONE_NUMBER_VERIFIED, true);
//        claims.put(OidcStandardClaimNames.ADDRESS, Map.of(
//            "formatted", "北京市朝阳区...",
//            "street_address", "...",
//            "locality": "北京市",
//            "region": "北京",
//            "country": "中国"
//        ));
//        claims.put(OidcStandardClaimNames.PROFILE,
//            "http://localhost:9000/auth/user/" + userDetails.getUsername());
//        claims.put(OidcStandardClaimNames.PICTURE,
//            "http://localhost:9000/auth/avatar/" + userDetails.getUsername());
//        claims.put(OidcStandardClaimNames.WEBSITE, "https://example.com");
//        claims.put(OidcStandardClaimNames.GENDER, "male");
//        claims.put(OidcStandardClaimNames.BIRTHDATE, "1990-01-01");
//        claims.put(OidcStandardClaimNames.ZONEINFO, "Asia/Shanghai");
//        claims.put(OidcStandardClaimNames.LOCALE, "zh-CN");
//
//        // 添加自定义声明
//        claims.put("department", "技术部");
//        claims.put("employee_id", "EMP001");
//        claims.put("roles", userDetails.getAuthorities().stream()
//            .map(GrantedAuthority::getAuthority)
//            .collect(Collectors.toList()));
//
//        return new OidcUserInfo(claims);
//    }
}