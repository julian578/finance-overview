package com.julianjacobs.financeoverview.service;

import com.julianjacobs.financeoverview.controller.request.RegisterRequest;
import com.julianjacobs.financeoverview.controller.response.LoginResponse;
import com.julianjacobs.financeoverview.entity.User;
import com.julianjacobs.financeoverview.repository.UserRepository;
import com.julianjacobs.financeoverview.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository repository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    public UserService(UserRepository repo,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider

        ) {
        this.repository = repo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity registerUser(RegisterRequest request) {

        try {
            if(request.getPassword() != null && request.getEmail() != null) {
                User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()));
                repository.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }



    }

    public ResponseEntity<LoginResponse> login(RegisterRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateJwt(authentication);
            return new ResponseEntity<>(new LoginResponse(jwt), HttpStatus.OK);
        } catch( BadCredentialsException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }

}
