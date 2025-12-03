package org.allbluehu.mse.auth.dto;

import lombok.Data;

@Data
public class AuthResult {
    private boolean success;
    private String message;
    private String token;
    private String refreshToken;
    private Long expiresIn;
    
    public static AuthResult success(String token, String refreshToken, Long expiresIn) {
        AuthResult result = new AuthResult();
        result.setSuccess(true);
        result.setMessage("登录成功");
        result.setToken(token);
        result.setRefreshToken(refreshToken);
        result.setExpiresIn(expiresIn);
        return result;
    }
    
    public static AuthResult failure(String message) {
        AuthResult result = new AuthResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}