package org.allbluehu.mse.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "social_connections", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"provider", "provider_user_id"}),
           @UniqueConstraint(columnNames = {"user_id", "provider"})
       })
@Data
public class SocialConnection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "provider", nullable = false)
    private String provider; // wechat, douyin, github, etc.
    
    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId; // openid, unionid, etc.
    
    private String providerUserName;
    
    @Column(length = 2000)
    private String accessToken;
    
    private Integer expiresIn;
    
    private String refreshToken;
    
    @Column(length = 1000)
    private String profileData; // JSON格式的原始用户信息
    
    @CreationTimestamp
    private LocalDateTime connectedAt;
    
    private LocalDateTime lastLoginAt;
    
    @PreUpdate
    public void preUpdate() {
        this.lastLoginAt = LocalDateTime.now();
    }
}