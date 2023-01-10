package com.example.uberapp_tim6.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class JwtDecoder {

    private static Jws<Claims> claimsJws;

    public JwtDecoder(){
    }

    public static String getRoleFromToken(String jwtToken){
        claimsJws = Jwts.parser().setSigningKey("amF2YWludXNl")
                .parseClaimsJws(jwtToken);
        return claimsJws.getBody().get("role",String.class);
    }

    public static Integer getIdFromToken(String jwtToken){
        claimsJws = Jwts.parser().setSigningKey("amF2YWludXNl")
                .parseClaimsJws(jwtToken);
        return claimsJws.getBody().get("id",Integer.class);
    }

    public static String getEmailFromToken(String jwtToken){
        claimsJws = Jwts.parser().setSigningKey("amF2YWludXNl")
                .parseClaimsJws(jwtToken);
        return claimsJws.getBody().getSubject();
    }
}
