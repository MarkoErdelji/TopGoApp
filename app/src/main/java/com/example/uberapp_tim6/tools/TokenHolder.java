package com.example.uberapp_tim6.tools;

public class TokenHolder {
    private static TokenHolder instance;

    private String jwtToken;
    private String refreshToken;

    private TokenHolder() {
    }

    public static TokenHolder getInstance() {
        if (instance == null) {
            instance = new TokenHolder();
        }
        return instance;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
