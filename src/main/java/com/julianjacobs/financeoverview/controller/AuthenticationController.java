package com.julianjacobs.financeoverview.controller;

import com.julianjacobs.financeoverview.controller.request.RegisterRequest;
import com.julianjacobs.financeoverview.controller.response.LoginResponse;
import com.julianjacobs.financeoverview.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;

    public AuthenticationController(UserService service) {
        this.userService = service;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest request) {

        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody RegisterRequest request) {
        return  userService.login(request);
    }

}
