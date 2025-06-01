package ru.example.categoryservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAccessAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenExtractor jwtTokenExtractor;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();
        UUID userId = jwtTokenExtractor.extractUserId(accessToken);
        GrantedAuthority role = jwtTokenExtractor.extractRole(accessToken);
        UserDetails userDetails = new UserDetailsImpl(userId, role);
        return new JwtAccessAuthenticationToken(userDetails, accessToken, Collections.singletonList(role));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAccessAuthenticationToken.class);
    }
}
