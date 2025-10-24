package org.allbluehu.mse.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.concurrent.TimeUnit;

/**
 * @author huaolan created on 2025/10/24
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @GetMapping
    public String test(String args) {
        return "";
    }

}
