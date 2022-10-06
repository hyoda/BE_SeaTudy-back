package com.finalproject.seatudy.security.filter;

import com.finalproject.seatudy.security.jwt.HeaderTokenExtractor;
import com.finalproject.seatudy.security.jwt.JwtPreProcessingToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {
    private final HeaderTokenExtractor extractor;

    public JwtAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher, HeaderTokenExtractor extractor) {
        super(requiresAuthenticationRequestMatcher);
        this.extractor = extractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.info("JWT_FILTER : attemptAuthentication 실행");
        // JWT 값을 담아주는 변수 TokenPayload
        String tokenPayload = request.getHeader("Authorization");
        if (tokenPayload == null || tokenPayload.equals("")) {
            throw new AuthenticationCredentialsNotFoundException("토큰이 존재하지 않습니다");
        }

        JwtPreProcessingToken jwtToken = new JwtPreProcessingToken(extractor.extract(tokenPayload, request));

        return super
                .getAuthenticationManager()
                .authenticate(jwtToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        super.unsuccessfulAuthentication(request, response, failed);
    }
}