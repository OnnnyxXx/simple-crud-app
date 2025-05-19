package com.example.demo.auth.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthentication{

    private boolean authenticated;
    private String username;
    private String firstName;


    public Object getCredentials() { return null; }

    public Object getDetails() { return null; }

    public Object getPrincipal() { return username; }

    public boolean isAuthenticated() { return authenticated; }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    public String getName() { return firstName; }

}