package org.allbluehu.mse.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "oauth2_clients")
@Data
public class OAuth2Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String clientId;
    
    @Column(nullable = false)
    private String clientSecret;
    
    private String clientName;
    
    @ElementCollection
    @CollectionTable(name = "client_authentication_methods", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "authentication_method")
    private Set<String> clientAuthenticationMethods = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "client_grant_types", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "grant_type")
    private Set<String> authorizationGrantTypes = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "client_redirect_uris", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "redirect_uri")
    private Set<String> redirectUris = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "client_scopes", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "scope")
    private Set<String> scopes = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "client_post_logout_redirect_uris", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "post_logout_redirect_uri")
    private Set<String> postLogoutRedirectUris = new HashSet<>();
    
    @Lob
    @Column(length = 4000)
    private String clientSettings; // JSON格式
    
    @Lob
    @Column(length = 4000)
    private String tokenSettings; // JSON格式
    
    private Instant clientIdIssuedAt;
    
    private Instant clientSecretExpiresAt;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private boolean enabled = true;
}