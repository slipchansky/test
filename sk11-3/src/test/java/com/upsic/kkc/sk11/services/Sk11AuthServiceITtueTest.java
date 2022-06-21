package com.upsic.kkc.sk11.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.upsic.kkc.sk11.dto.Sk11AddressesDto;
import com.upsic.kkc.sk11.dto.Sk11AuthDto;
import com.upsic.kkc.sk11.services.annotations.Sk11Itest;

@Sk11Itest
class Sk11AuthServiceITtueTest {
    
    @Autowired
    Sk11AuthService authService;
    
    @Value("${sk11.base-path}")
    private String address; 

    @Test
    void testAuth() {
        Sk11AuthDto a = authService.getAuth(address);
        String authPath = a.getAccessToken();
        Sk11AuthDto sk11AuthDto = authService.getAuth(authPath);
    }

}
