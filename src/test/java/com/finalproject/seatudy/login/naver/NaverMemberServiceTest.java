package com.finalproject.seatudy.login.naver;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.security.SecureRandom;


@ExtendWith(MockitoExtension.class)
class NaverMemberServiceTest {




    public String generateState() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }



}