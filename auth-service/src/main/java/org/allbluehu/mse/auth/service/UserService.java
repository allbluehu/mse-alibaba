package org.allbluehu.mse.auth.service;

import lombok.RequiredArgsConstructor;
import org.allbluehu.mse.auth.entity.User;
import org.allbluehu.mse.auth.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public User createUser(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        if (email != null && userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setNickname(username);
        
        // 设置默认权限
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_USER");
        user.setAuthorities(authorities);
        
        return userRepository.save(user);
    }
    
    @Transactional
    public User createUserFromSocial(String provider, Map<String, Object> attributes) {
        String username = generateSocialUsername(provider, attributes);
        String nickname = extractNickname(provider, attributes);
        String email = extractEmail(provider, attributes);
        
        // 确保用户名唯一
        String finalUsername = username;
        int suffix = 1;
        while (userRepository.existsByUsername(finalUsername)) {
            finalUsername = username + "_" + suffix;
            suffix++;
        }
        
        User user = new User();
        user.setUsername(finalUsername);
        user.setNickname(nickname);
        user.setEmail(email);
        
        if (email != null) {
            user.setEmailVerified(isEmailVerified(provider, attributes));
        }
        
        // 设置默认权限
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_USER");
        user.setAuthorities(authorities);
        
        return userRepository.save(user);
    }
    
    public OAuth2User createOAuth2User(User user, Map<String, Object> attributes) {
        return new DefaultOAuth2User(
            user.getAuthorities().stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .toList(),
            attributes,
            "sub" // 用户名属性名
        );
    }
    
    private String generateSocialUsername(String provider, Map<String, Object> attributes) {
        String baseName = extractNickname(provider, attributes);
        if (baseName == null || baseName.trim().isEmpty()) {
            baseName = provider.toLowerCase() + "_user";
        }
        
        // 移除特殊字符，只保留字母数字
        return baseName.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
    }
    
    private String extractNickname(String provider, Map<String, Object> attributes) {
        switch (provider.toLowerCase()) {
            case "wechat":
                return (String) attributes.get("nickname");
            case "douyin":
                return (String) attributes.get("nickname");
            case "github":
                return (String) attributes.get("name");
            case "google":
                return (String) attributes.get("name");
            default:
                Object name = attributes.get("name");
                return name != null ? name.toString() : null;
        }
    }
    
    private String extractEmail(String provider, Map<String, Object> attributes) {
        switch (provider.toLowerCase()) {
            case "github":
            case "google":
                return (String) attributes.get("email");
            default:
                return null;
        }
    }
    
    private boolean isEmailVerified(String provider, Map<String, Object> attributes) {
        switch (provider.toLowerCase()) {
            case "github":
                return true; // GitHub邮箱已验证
            case "google":
                Boolean verified = (Boolean) attributes.get("email_verified");
                return verified != null && verified;
            default:
                return false;
        }
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Transactional
    public void updateUserInfo(Long userId, Map<String, Object> updates) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (updates.containsKey("nickname")) {
            user.setNickname((String) updates.get("nickname"));
        }
        
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            if (!newEmail.equals(user.getEmail())) {
                user.setEmail(newEmail);
                user.setEmailVerified(false);
            }
        }
        
        if (updates.containsKey("avatar")) {
            user.setAvatar((String) updates.get("avatar"));
        }
        
        userRepository.save(user);
    }
}