// package org.allbluehu.mse.auth.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;
//
// /**
//  * @author huaolan created on 2025/10/24
//  */
//
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
//
//     @Bean
//     @Order(2)
//     public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//         return http
//                 .authorizeHttpRequests(authorize -> authorize
//                         .requestMatchers("/auth/**", "/login", "/error", "/oauth2/**").permitAll()
//                         .anyRequest().authenticated()
//                 )
//                 .formLogin(AbstractHttpConfigurer::disable
//                 )
//                 .csrf(csrf -> csrf.ignoringRequestMatchers("/auth/**", "/oauth2/**"))
//                 .build();
//     }
//     //
//     // @Bean
//     // public UserDetailsService userDetailsService() {
//     //     UserDetails user = User.builder()
//     //             .username("user")
//     //             .password(passwordEncoder().encode("password"))
//     //             .roles("USER")
//     //             .build();
//     //     UserDetails admin = User.builder()
//     //             .username("admin")
//     //             .password(passwordEncoder().encode("admin"))
//     //             .roles("ADMIN", "USER")
//     //             .build();
//     //     return new InMemoryUserDetailsManager(user, admin);
//     // }
//     //
//     // @Bean
//     // public PasswordEncoder passwordEncoder() {
//     //     return new BCryptPasswordEncoder();
//     // }
//
// }
