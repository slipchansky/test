package com.upsic.kkc.sk11.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.upsic.kkc.sk11.dto.Sk11AddressesDto;
import com.upsic.kkc.sk11.dto.Sk11AuthDto;
import com.upsic.kkc.sk11.services.annotations.Sk11Itest;

/**
 * Для проверки что он жив
 *
 */
@Sk11Itest
@Disabled // закомментируй это чтобы выполнить.
class Sk11AuthServiceITtueTest {

    @Autowired
    Sk11AuthService sk11AuthService;

    @Test()
    void testAuth() {
        Sk11AddressesDto sk11AddressesDto = sk11AuthService.getAddresses();
        String authPath = sk11AddressesDto.getTokenEndpointBasic();
        Sk11AuthService spy = Mockito.spy(sk11AuthService);
        Sk11AuthDto sk11AuthDto = sk11AuthService.getAuth(authPath);
        System.out.println(sk11AuthDto);
    }

}
