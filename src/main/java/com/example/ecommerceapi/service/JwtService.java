package com.example.ecommerceapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private String jwtSecret = System.getenv ("APP_JWT_SECRET");
    private long JWT_EXPIRATION = 1000 * 60 * 60 * 24;

    public String generateToken (String username) {
        return Jwts.builder ()
                .setSubject (username)
                .setIssuedAt (new Date ())
                .setExpiration (new Date (System.currentTimeMillis () + this.JWT_EXPIRATION))
                .signWith (SignatureAlgorithm.HS256, this.jwtSecret)
                .compact ();
    }

    public boolean isTokenValid (String token, String username) {
        return username.equals (extractUsername (token)) && !isTokenExpired (token);
    }

    public String extractUsername (String token) {
        return extractClaim (token, Claims::getSubject);
    }

    public Date extractExpiration (String token) {
        return extractClaim (token, Claims::getExpiration);
    }

    public <T> T extractClaim (String token, Function <Claims, T> claimsResolver) {
        Claims claims = extractAllClaims (token);
        return claimsResolver.apply (claims);
    }

    private Claims extractAllClaims (String token) {
        return Jwts.parser ()
                .setSigningKey (this.jwtSecret)
                .parseClaimsJws (token)
                .getBody ();
    }

    private boolean isTokenExpired (String token) {
        return extractExpiration (token).before (new Date());
    }
}
