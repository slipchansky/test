package com.upsic.kkc.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.upsic.kkc.annotations.Sk11Itest;
import com.upsic.kkc.configuration.Sk11Configuration;
import com.upsic.kkc.configuration.Sk11WsWebClientConfiguration;
import com.upsic.kkc.configuration.WebClientConfiguration;
import com.upsic.kkc.dto.sk11api.Sk11AddressesDto;
import com.upsic.kkc.dto.sk11api.Sk11AuthDto;
import com.upsic.kkc.dto.sk11api.Sk11ModelDto;


@Sk11Itest
public class Sk11AuthITest {
    
    @Autowired
    Sk11AuthService sk11AuthService;
    
    @Test
    void testAuth() {
        Sk11AddressesDto sk11AddressesDto = sk11AuthService.getAddresses();
        String authPath = sk11AddressesDto.getTokenEndpointBasic();
        Sk11AuthDto sk11AuthDto = sk11AuthService.getAuth(authPath);
    }
}
