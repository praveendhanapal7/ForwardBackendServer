package com.forwardagency.forwardbackend.web;

import com.forwardagency.forwardbackend.Model.Users;
import com.forwardagency.forwardbackend.Service.AuthTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class BearerAuthFilter extends OncePerRequestFilter {

    public static final String CURRENT_USER_ATTR = "currentUser";
    public static final String CURRENT_TOKEN_ATTR = "currentToken";

    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthTokenService authTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String token = header.substring(BEARER_PREFIX.length()).trim();
            Optional<Users> userOpt = authTokenService.resolve(token);
            if (userOpt.isPresent()) {
                request.setAttribute(CURRENT_USER_ATTR, userOpt.get());
                request.setAttribute(CURRENT_TOKEN_ATTR, token);
            }
        }
        filterChain.doFilter(request, response);
    }
}
