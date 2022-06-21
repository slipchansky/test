package com.upsic.kkc.sk11.services.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.upsic.kkc.sk11.configuration.CacheConfiguration;
import com.upsic.kkc.sk11.configuration.Sk11Configuration;
import com.upsic.kkc.sk11.configuration.WebClientConfiguration;
import com.upsic.kkc.sk11.services.Sk11AddressService;
import com.upsic.kkc.sk11.services.Sk11ApiServiceImpl;
import com.upsic.kkc.sk11.services.Sk11AuthService;
import com.upsic.kkc.sk11.services.utils.Sk11AddressesProviderImpl;


@SpringBootTest(classes= {
        CacheConfiguration.class,
        Sk11AddressesProviderImpl.class,
        Sk11AddressService.class,
        Sk11Configuration.class,
        WebClientConfiguration.class,
        Sk11AuthService.class, 
        Sk11ApiServiceImpl.class, 
        CacheAutoConfiguration.class})
@ActiveProfiles("test" )
@Retention(RUNTIME)
@Target(TYPE)
public @interface Sk11Itest {

}
