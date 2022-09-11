package com.finalproject.seatudy.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class HeaderTokenExtractor {

    public final String HEADER_PREFIX="Bearer ";

    public String extract(String header, HttpServletRequest request) {

        if(!header.startsWith(HEADER_PREFIX)){
            throw new AuthenticationServiceException("올바른 JWT 정보가 아닙니다.");
        }

        return header.substring(HEADER_PREFIX.length());
    }
}
