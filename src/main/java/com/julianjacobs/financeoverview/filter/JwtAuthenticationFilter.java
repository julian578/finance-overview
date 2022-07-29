package com.julianjacobs.financeoverview.filter;

import com.julianjacobs.financeoverview.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       try {
           String jwt = getJwtFromRequest(request);

           if(jwt != null && jwtProvider.validateJwt(jwt)) {
               String username = jwtProvider.getUsernameFromToken(jwt);
               UserDetails userDetails = userDetailsService.loadUserByUsername(username);
               UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }
       } catch(BadCredentialsException e) {
           e.printStackTrace();
       }

       filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        String jwt = request.getHeader("Authorization");
        if(jwt != null && jwt != "" && jwt.startsWith("Bearer ")) {
            return jwt.substring(7, jwt.length());
        }

        return null;
    }
}
