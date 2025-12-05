package org.allbluehu.mse.auth.repository;

import org.allbluehu.mse.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhone(String phone);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u JOIN u.socialConnections sc WHERE sc.provider = :provider AND sc.providerUserId = :providerUserId")
    Optional<User> findBySocialConnection(@Param("provider") String provider, 
                                          @Param("providerUserId") String providerUserId);
}