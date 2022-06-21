package com.upsic.kkc.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.upsic.kkc.configuration.Sk11Configuration;
import com.upsic.kkc.configuration.Sk11WsWebClientConfiguration;
import com.upsic.kkc.configuration.WebClientConfiguration;
import com.upsic.kkc.services.Sk11ApiServiceImpl;
import com.upsic.kkc.services.Sk11AuthService;

@SpringBootTest(classes= {
        Sk11Configuration.class,
        WebClientConfiguration.class,
        Sk11WsWebClientConfiguration.class,
        Sk11AuthService.class, 
        Sk11ApiServiceImpl.class})
@ActiveProfiles("test")
@Retention(RUNTIME)
@Target(TYPE)
public @interface Sk11Itest {

}
