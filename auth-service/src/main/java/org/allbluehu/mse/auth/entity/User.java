package org.allbluehu.mse.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true)
    private String email;
    
    private String phone;
    
    private String password;
    
    private String nickname;
    
    private String avatar;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    private Set<String> authorities = new HashSet<>();
    
    private boolean emailVerified = false;
    
    private boolean phoneVerified = false;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SocialConnection> socialConnections = new HashSet<>();
    
    // UserDetails接口实现
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream()
            .map(auth -> (GrantedAuthority) () -> auth)
            .toList();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return status != UserStatus.EXPIRED;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
    
    public enum UserStatus {
        ACTIVE, INACTIVE, LOCKED, EXPIRED
    }
}