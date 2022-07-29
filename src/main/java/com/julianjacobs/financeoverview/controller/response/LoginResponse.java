package com.julianjacobs.financeoverview.controller.response;

public class LoginResponse {

    private String jwt;

    public LoginResponse(String jwt) {
        this.jwt = jwt;
    }

    public LoginResponse() {}

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
