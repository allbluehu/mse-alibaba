package org.allbluehu.mse.auth.repository;

import org.allbluehu.mse.auth.entity.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, String> {
    
    /**
     * 根据客户端ID查找客户端
     */
    Optional<OAuth2Client> findByClientId(String clientId);
    
    /**
     * 检查客户端ID是否存在
     */
    boolean existsByClientId(String clientId);
    
    /**
     * 根据客户端ID和启用状态查找客户端
     */
    Optional<OAuth2Client> findByClientIdAndEnabled(String clientId, boolean enabled);
    
    /**
     * 查找所有启用的客户端
     */
    @Query("SELECT c FROM OAuth2Client c WHERE c.enabled = true")
    java.util.List<OAuth2Client> findAllEnabled();
    
    /**
     * 根据客户端名称模糊查询
     */
    @Query("SELECT c FROM OAuth2Client c WHERE c.clientName LIKE %:name%")
    java.util.List<OAuth2Client> findByClientNameContaining(@Param("name") String name);
    
    /**
     * 根据创建时间范围查询
     */
    @Query("SELECT c FROM OAuth2Client c WHERE c.createdAt BETWEEN :start AND :end")
    java.util.List<OAuth2Client> findByCreatedAtBetween(
            @Param("start") java.time.LocalDateTime start,
            @Param("end") java.time.LocalDateTime end);
    
    /**
     * 统计启用和禁用的客户端数量
     */
    @Query("SELECT c.enabled, COUNT(c) FROM OAuth2Client c GROUP BY c.enabled")
    java.util.List<Object[]> countByEnabled();
}