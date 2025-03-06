package com.ppu.ppu.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    private final String secret;
    private final Long exp;

    public JwtUtil(String secret, Long exp) {
        this.secret = secret;
        this.exp = exp;
    }

    public String generateToken(String id) {
        return Jwts.builder()
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("id", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date()); // 만료 여부 확인
        } catch (Exception e) {
            return false; // 유효하지 않은 토큰
        }
    }

}
