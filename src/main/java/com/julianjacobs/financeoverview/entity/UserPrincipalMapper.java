package com.julianjacobs.financeoverview.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class UserPrincipalMapper {

    public static UserPrincipal userToPrincipal(User user) {
        UserPrincipal principal = new UserPrincipal();
        List<SimpleGrantedAuthority> authorityList =new ArrayList<SimpleGrantedAuthority>();

        principal.setUsername(user.getEmail());
        principal.setPassword(user.getPassword());
        principal.setAuthorities(authorityList);

        return principal;
    }
}
