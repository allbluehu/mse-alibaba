-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS auth_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE auth_service;

-- 客户端表
CREATE TABLE IF NOT EXISTS oauth2_registered_client (
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    client_id VARCHAR(100) NOT NULL,
    client_id_issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret VARCHAR(200) NULL,
    client_secret_expires_at TIMESTAMP NULL,
    client_name VARCHAR(200) NOT NULL,
    client_authentication_methods VARCHAR(1000) NOT NULL,
    authorization_grant_types VARCHAR(1000) NOT NULL,
    redirect_uris VARCHAR(1000) NULL,
    post_logout_redirect_uris VARCHAR(1000) NULL,
    scopes VARCHAR(1000) NOT NULL,
    client_settings VARCHAR(2000) NOT NULL,
    token_settings VARCHAR(2000) NOT NULL
);

-- 授权表
CREATE TABLE IF NOT EXISTS oauth2_authorization (
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    registered_client_id VARCHAR(100) NOT NULL,
    principal_name VARCHAR(200) NOT NULL,
    authorization_grant_type VARCHAR(100) NOT NULL,
    authorized_scopes VARCHAR(1000) NULL,
    attributes TEXT NULL,
    state VARCHAR(500) NULL,
    authorization_code_value TEXT NULL,
    authorization_code_issued_at TIMESTAMP NULL,
    authorization_code_expires_at TIMESTAMP NULL,
    authorization_code_metadata TEXT NULL,
    access_token_value TEXT NULL,
    access_token_issued_at TIMESTAMP NULL,
    access_token_expires_at TIMESTAMP NULL,
    access_token_metadata TEXT NULL,
    access_token_type VARCHAR(100) NULL,
    access_token_scopes VARCHAR(1000) NULL,
    refresh_token_value TEXT NULL,
    refresh_token_issued_at TIMESTAMP NULL,
    refresh_token_expires_at TIMESTAMP NULL,
    refresh_token_metadata TEXT NULL,
    oidc_id_token_value TEXT NULL,
    oidc_id_token_issued_at TIMESTAMP NULL,
    oidc_id_token_expires_at TIMESTAMP NULL,
    oidc_id_token_metadata TEXT NULL,
    user_code_value TEXT NULL,
    user_code_issued_at TIMESTAMP NULL,
    user_code_expires_at TIMESTAMP NULL,
    user_code_metadata TEXT NULL,
    device_code_value TEXT NULL,
    device_code_issued_at TIMESTAMP NULL,
    device_code_expires_at TIMESTAMP NULL,
    device_code_metadata TEXT NULL,
    FOREIGN KEY (registered_client_id) REFERENCES oauth2_registered_client(id)
);

-- 授权同意表
CREATE TABLE IF NOT EXISTS oauth2_authorization_consent (
    registered_client_id VARCHAR(100) NOT NULL,
    principal_name VARCHAR(200) NOT NULL,
    authorities VARCHAR(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name),
    FOREIGN KEY (registered_client_id) REFERENCES oauth2_registered_client(id)
);

-- 插入默认客户端数据
INSERT INTO oauth2_registered_client (id, client_id, client_id_issued_at, client_secret, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES
('messaging-client', 'messaging-client', CURRENT_TIMESTAMP, '{noop}secret', 'Messaging Client', 'client_secret_basic', 'authorization_code,refresh_token,client_credentials', 'http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc,http://127.0.0.1:8080/authorized', 'http://127.0.0.1:8080/logged-out', 'openid,profile,message.read,message.write,user.read', '{"@class":"java.util.HashMap","requireAuthorizationConsent":"true"}', '{"@class":"java.util.HashMap"}'),
('device-messaging-client', 'device-messaging-client', CURRENT_TIMESTAMP, NULL, 'Device Messaging Client', 'none', 'device_code,refresh_token', NULL, NULL, 'message.read,message.write', '{"@class":"java.util.HashMap"}', '{"@class":"java.util.HashMap"}'),
('token-client', 'token-client', CURRENT_TIMESTAMP, '{noop}token', 'Token Exchange Client', 'client_secret_basic', 'urn:ietf:params:oauth:grant-type:token-exchange', NULL, NULL, 'message.read,message.write', '{"@class":"java.util.HashMap"}', '{"@class":"java.util.HashMap"}'),
('mtls-demo-client', 'mtls-demo-client', CURRENT_TIMESTAMP, NULL, 'MTLS Demo Client', 'tls_client_auth,self_signed_tls_client_auth', 'client_credentials', NULL, NULL, 'message.read,message.write', '{"@class":"java.util.HashMap","x509CertificateSubjectDN":"CN=demo-client-sample,OU=Spring Samples,O=Spring,C=US","jwkSetUrl":"http://127.0.0.1:8080/jwks"}', '{"@class":"java.util.HashMap","x509CertificateBoundAccessTokens":"true"}');
