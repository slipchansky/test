package com.upsic.kkc.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.upsic.kkc.configuration.Sk11Configuration;
import com.upsic.kkc.configuration.Sk11WsWebClientConfiguration;
import com.upsic.kkc.configuration.WebClientConfiguration;
import com.upsic.kkc.dto.sk11api.Sk11AddressesDto;
import com.upsic.kkc.dto.sk11api.Sk11AuthDto;
import com.upsic.kkc.dto.sk11api.Sk11ModelDto;


@SpringBootTest(classes= {
        Sk11Configuration.class,
        WebClientConfiguration.class,
        Sk11WsWebClientConfiguration.class,
        Sk11AuthService.class, 
        Sk11ApiServiceImpl.class})
public class Sk11AuthTest {
    
    @Autowired
    Sk11AuthService sk11AuthService;
    
    @Autowired
    Sk11ApiService sk11ApiService;
    

    @Test
    void testAuth() {
        Sk11AddressesDto sk11AddressesDto = sk11AuthService.getAddresses();
        String authPath = sk11AddressesDto.getTokenEndpointBasic();
        Sk11AuthDto sk11AuthDto = sk11AuthService.getAuth(authPath);
        int k = 0;
        k++;
    }
    
    @Test
    void testApiService() {
        List<Sk11ModelDto> models = sk11ApiService.getModels();
        assertTrue(models.size()>0);
    }
    
//    @Bean
//    Sk11AuthService sk11AuthService(WebClient webClient, Sk11Configuration sk11Configuration) {
//        return new Sk11AuthService(webClient, sk11Configuration);
//    }
    
//    @Bean
//    public Sk11ApiService sk11ApiService(WebClient sk11AuthorizedWebClient, Sk11AuthService sk11AuthService,
//            Sk11Configuration sk11Configuration) {
//        return new Sk11ApiServiceImpl(sk11AuthorizedWebClient, sk11AuthService, sk11Configuration);
//    }
    

}
