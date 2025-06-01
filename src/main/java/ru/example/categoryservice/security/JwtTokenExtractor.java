package ru.example.categoryservice.security;

import com.auth0.jwt.JWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtTokenExtractor {

    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaim(token, "user_id"));
    }

    public GrantedAuthority extractRole(String token) {
        return new SimpleGrantedAuthority(extractClaim(token, "role"));
    }

    private String extractClaim(String token, String claim) {
        return JWT.decode(token)
                .getClaim(claim)
                .asString();
    }
}
