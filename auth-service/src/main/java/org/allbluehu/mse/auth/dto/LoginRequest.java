package org.allbluehu.mse.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "登录类型不能为空")
    private String loginType; // PASSWORD, SMS, WECHAT
    
    private String username;
    private String password;
    private String phone;
    private String smsCode;
    private String wechatCode;
    
    @NotBlank(message = "客户端类型不能为空")
    private String clientType; // APP, WEB, MINI_APP
}