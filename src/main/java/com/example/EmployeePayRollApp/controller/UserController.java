package com.example.EmployeePayRollApp.controller;

import com.example.EmployeePayRollApp.dto.*;
import com.example.EmployeePayRollApp.interfaces.AuthInterface;
import com.example.EmployeePayRollApp.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class UserController {
    ObjectMapper obj = new ObjectMapper();

    @Autowired
    EmailService emailService;

    @Autowired
    AuthInterface authInterface;

    // Register
    @PostMapping(path = "/register")
    public String register(@RequestBody AuthUserDTO user) throws Exception{
        log.info("Register: {}", obj.writeValueAsString(user));
        return authInterface.register(user);
    }

    // Login
    @PostMapping(path ="/login")
    public String login(@RequestBody LoginDTO user) throws Exception{
        log.info("Login: {}", obj.writeValueAsString(user));
        return authInterface.login(user);
    }

    // Send mail
    @PostMapping(path = "/sendMail")
    public String sendMail(@RequestBody MailDTO message) throws Exception{
        log.info("Send email: {}", obj.writeValueAsString(message));
        emailService.sendEmail(message.getTo(), message.getSubject(), message.getBody());
        return "Mail sent";
    }

    // Forgot password
    @PutMapping("/forgotPassword/{email}")
    public AuthUserDTO forgotPassword(@RequestBody PassDTO pass, @PathVariable String email) throws Exception{
        log.info("Forgot password: {}", obj.writeValueAsString(pass));
        return authInterface.forgotPassword(pass, email);
    }

    // Reset password
    @PutMapping("/resetPassword/{email}")
    public String resetPassword(@PathVariable String email, @RequestBody Map<String, String> requestBody) throws Exception {
        String currentPass = requestBody.get("currentPass");
        String newPass = requestBody.get("newPass");
        return authInterface.resetPassword(email, currentPass, newPass);
    }
}