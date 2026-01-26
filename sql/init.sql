-- schema.sql
CREATE DATABASE IF NOT EXISTS oidc_auth DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE oidc_auth;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
                                     email VARCHAR(100) UNIQUE,
                                     phone VARCHAR(20),
                                     password VARCHAR(200),
                                     nickname VARCHAR(100),
                                     avatar VARCHAR(500),
                                     status VARCHAR(20) DEFAULT 'ACTIVE',
                                     email_verified BOOLEAN DEFAULT FALSE,
                                     phone_verified BOOLEAN DEFAULT FALSE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     INDEX idx_username (username),
                                     INDEX idx_email (email),
                                     INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户权限表
CREATE TABLE IF NOT EXISTS user_authorities (
                                                user_id BIGINT NOT NULL,
                                                authority VARCHAR(50) NOT NULL,
                                                PRIMARY KEY (user_id, authority),
                                                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                                INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 社交登录关联表
CREATE TABLE IF NOT EXISTS social_connections (
                                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  user_id BIGINT NOT NULL,
                                                  provider VARCHAR(50) NOT NULL,
                                                  provider_user_id VARCHAR(200) NOT NULL,
                                                  provider_user_name VARCHAR(200),
                                                  access_token TEXT,
                                                  expires_in INT,
                                                  refresh_token VARCHAR(500),
                                                  profile_data TEXT,
                                                  connected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                  last_login_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                  UNIQUE KEY uk_provider_user (provider, provider_user_id),
                                                  UNIQUE KEY uk_user_provider (user_id, provider),
                                                  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                                  INDEX idx_provider_user (provider, provider_user_id),
                                                  INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- OAuth2客户端表
CREATE TABLE IF NOT EXISTS oauth2_clients (
                                              id VARCHAR(255) PRIMARY KEY,
                                              client_id VARCHAR(100) NOT NULL UNIQUE,
                                              client_secret VARCHAR(200) NOT NULL,
                                              client_name VARCHAR(200),
                                              client_settings TEXT,
                                              token_settings TEXT,
                                              client_id_issued_at TIMESTAMP NULL,
                                              client_secret_expires_at TIMESTAMP NULL,
                                              enabled BOOLEAN DEFAULT TRUE,
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                              INDEX idx_client_id (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 客户端认证方法表
CREATE TABLE IF NOT EXISTS client_authentication_methods (
                                                             client_id VARCHAR(255) NOT NULL,
                                                             authentication_method VARCHAR(50) NOT NULL,
                                                             PRIMARY KEY (client_id, authentication_method),
                                                             FOREIGN KEY (client_id) REFERENCES oauth2_clients(id) ON DELETE CASCADE,
                                                             INDEX idx_client_id (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 客户端授权类型表
CREATE TABLE IF NOT EXISTS client_grant_types (
                                                  client_id VARCHAR(255) NOT NULL,
                                                  grant_type VARCHAR(50) NOT NULL,
                                                  PRIMARY KEY (client_id, grant_type),
                                                  FOREIGN KEY (client_id) REFERENCES oauth2_clients(id) ON DELETE CASCADE,
                                                  INDEX idx_client_id (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 客户端重定向URI表
CREATE TABLE IF NOT EXISTS client_redirect_uris (
                                                    client_id VARCHAR(255) NOT NULL,
                                                    redirect_uri VARCHAR(1000) NOT NULL,
                                                    PRIMARY KEY (client_id, redirect_uri(255)),
                                                    FOREIGN KEY (client_id) REFERENCES oauth2_clients(id) ON DELETE CASCADE,
                                                    INDEX idx_client_id (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 客户端Scope表
CREATE TABLE IF NOT EXISTS client_scopes (
                                             client_id VARCHAR(255) NOT NULL,
                                             scope VARCHAR(100) NOT NULL,
                                             PRIMARY KEY (client_id, scope),
                                             FOREIGN KEY (client_id) REFERENCES oauth2_clients(id) ON DELETE CASCADE,
                                             INDEX idx_client_id (client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 授权表 (Spring Authorization Server需要)
CREATE TABLE IF NOT EXISTS oauth2_authorization (
                                                    id VARCHAR(100) PRIMARY KEY,
                                                    registered_client_id VARCHAR(100) NOT NULL,
                                                    principal_name VARCHAR(200) NOT NULL,
                                                    authorization_grant_type VARCHAR(100) NOT NULL,
                                                    authorized_scopes VARCHAR(1000),
                                                    attributes TEXT,
                                                    state VARCHAR(500),
                                                    authorization_code_value TEXT,
                                                    authorization_code_issued_at TIMESTAMP,
                                                    authorization_code_expires_at TIMESTAMP,
                                                    authorization_code_metadata TEXT,
                                                    access_token_value TEXT,
                                                    access_token_issued_at TIMESTAMP,
                                                    access_token_expires_at TIMESTAMP,
                                                    access_token_metadata TEXT,
                                                    access_token_type VARCHAR(100),
                                                    access_token_scopes VARCHAR(1000),
                                                    oidc_id_token_value TEXT,
                                                    oidc_id_token_issued_at TIMESTAMP,
                                                    oidc_id_token_expires_at TIMESTAMP,
                                                    oidc_id_token_metadata TEXT,
                                                    refresh_token_value TEXT,
                                                    refresh_token_issued_at TIMESTAMP,
                                                    refresh_token_expires_at TIMESTAMP,
                                                    refresh_token_metadata TEXT,
                                                    INDEX idx_registered_client_id (registered_client_id),
                                                    INDEX idx_principal_name (principal_name),
                                                    INDEX idx_state (state(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初始化数据: 插入一个示例客户端
INSERT INTO oauth2_clients (id, client_id, client_secret, client_name, enabled)
VALUES ('admin-client-id', 'admin', '{noop}secret', 'admin client', TRUE)
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;

INSERT INTO client_authentication_methods (client_id, authentication_method)
VALUES ('admin-client-id', 'client_secret_basic')
ON DUPLICATE KEY UPDATE client_id = client_id;

INSERT INTO client_grant_types (client_id, grant_type)
VALUES ('admin-client-id', 'authorization_code'), ('admin-client-id', 'refresh_token')
ON DUPLICATE KEY UPDATE client_id = client_id;

INSERT INTO client_redirect_uris (client_id, redirect_uri)
VALUES ('admin-client-id', 'http://localhost:8180/login/oauth2/code/admin')
ON DUPLICATE KEY UPDATE client_id = client_id;

INSERT INTO client_scopes (client_id, scope)
VALUES ('admin-client-id', 'openid'), ('admin-client-id', 'profile'), ('admin-client-id', 'email')
ON DUPLICATE KEY UPDATE client_id = client_id;


INSERT INTO users (username, password) VALUES ('admin', 'admin')