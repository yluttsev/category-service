package ru.example.categoryservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAccessFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final Map<String, HttpMethod> securedEndpoints;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (accessTokenHeader != null && accessTokenHeader.startsWith("Bearer ")) {
            String accessToken = accessTokenHeader.substring(7);
            Authentication authentication = authenticationManager.authenticate(
                    new JwtAccessAuthenticationToken(accessToken)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Optional.ofNullable(securedEndpoints.get(request.getRequestURI()))
                .map(value -> value.matches(request.getMethod()))
                .orElse(false);
    }
}
