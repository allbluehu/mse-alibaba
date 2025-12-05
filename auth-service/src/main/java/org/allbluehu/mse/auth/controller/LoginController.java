package org.allbluehu.mse.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.allbluehu.mse.auth.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {
    
    private final UserService userService;
    
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        if (logout != null) {
            model.addAttribute("message", "您已成功登出");
        }
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userForm") UserForm userForm,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            model.addAttribute("error", "两次输入的密码不一致");
            return "register";
        }
        
        try {
            userService.createUser(userForm.getUsername(), userForm.getPassword(), userForm.getEmail());
            model.addAttribute("message", "注册成功，请登录");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
    
    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("attributes", principal.getAttributes());
        }
        return "index";
    }
    
    @GetMapping("/oauth2/consent")
    public String consentPage(@RequestParam("client_id") String clientId,
                             @RequestParam("scope") String scope,
                             @RequestParam("state") String state,
                             Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("scope", scope);
        model.addAttribute("state", state);
        return "consent";
    }
    
    @GetMapping("/user/profile")
    @ResponseBody
    public Map<String, Object> getUserProfile(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("username", principal.getName());
        profile.put("attributes", principal.getAttributes());
        return profile;
    }
    
    // 用户注册表单
    public static class UserForm {
        private String username;
        private String email;
        private String password;
        private String confirmPassword;
        
        // getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    }
}