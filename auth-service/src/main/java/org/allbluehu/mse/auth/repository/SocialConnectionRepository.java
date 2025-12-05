package org.allbluehu.mse.auth.repository;

import org.allbluehu.mse.auth.entity.SocialConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialConnectionRepository extends JpaRepository<SocialConnection, Long> {
    
    Optional<SocialConnection> findByProviderAndProviderUserId(String provider, String providerUserId);
    
    Optional<SocialConnection> findByUserIdAndProvider(Long userId, String provider);
    
    boolean existsByProviderAndProviderUserId(String provider, String providerUserId);
}