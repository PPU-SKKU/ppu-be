package com.ppu.ppu.utils.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private final String secret;
    private final Long accessExp;
    private final Long refreshExp;

    public JwtUtil() {
        this.secret = System.getenv("TOKEN_SECRET");
        this.accessExp = Long.valueOf(System.getenv("ACCESS_TOKEN_EXP"));
        this.refreshExp = Long.valueOf(System.getenv("REFRESH_TOKEN_EXP"));
    }

    private String generateToken(UUID id, Long exp) {
        return Jwts.builder()
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateAccessToken(UUID id) {
        return generateToken(id, accessExp);
    }

    public String generateRefreshToken(UUID id) {
        return generateToken(id, refreshExp);
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
