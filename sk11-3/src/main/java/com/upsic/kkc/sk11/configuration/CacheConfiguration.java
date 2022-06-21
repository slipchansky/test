package com.upsic.kkc.sk11.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.upsic.kkc.sk11.dto.Sk11AuthDto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.concurrent.TimeUnit;
import static com.upsic.kkc.sk11.utils.Sk11Constants.Cache.*;

@Configuration
@EnableCaching
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class CacheConfiguration {
    @Bean
    public CaffeineCache childrenCache() {
        return new CaffeineCache(SK11_ADDRESS_ENTITIES_CACHE, Caffeine.newBuilder()
                .build());
    }
    
    /**
     * Специальный кеш для хранения токена.
     * Обновляется когда в хранимом объекте истечет ExpiresIn количество секунд
     *
     * @return кеш SK11_AUTH_CACHE
     */
    @Bean
    public CaffeineCache sk11AuthCache() {
        return new CaffeineCache(SK11_AUTH_CACHE, Sk11AuthCacheCaffeineBuilder().build());
    }

    private Caffeine Sk11AuthCacheCaffeineBuilder() {
        return Caffeine.newBuilder().maximumSize(1).expireAfter(new Expiry<String, Sk11AuthDto>() {
            @Override
            public long expireAfterCreate(@NonNull String key, @NonNull Sk11AuthDto value, long currentTime) {
                return TimeUnit.SECONDS.toNanos(value.getExpiresIn());
            }

            @Override
            public long expireAfterUpdate(@NonNull String key, @NonNull Sk11AuthDto value, long currentTime, long currentDuration) {
                return TimeUnit.SECONDS.toNanos(value.getExpiresIn());
            }

            @Override
            public long expireAfterRead(@NonNull String key, @NonNull Sk11AuthDto value, long currentTime, long currentDuration) {
                return currentDuration;
            }
        });
    }
}
