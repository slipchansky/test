package com.upsic.kkc.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * Параметры для интеграции с СК-11
 */
@Configuration

public class Sk11Configuration {

    @Value("${app.sk11.web-client.connect-timeout}")
    @Getter public Integer connectTimeout;

    @Value("${app.sk11.web-client.read-timeout}")
    @Getter public Long readTimeout;

    @Value("${app.sk11.api-version}")
    @Getter private String apiVersion;

    @Value("${app.sk11.model-version}")
    @Getter private String modelVersion;

    @Value("${app.sk11.user}")
    @Getter private String user;

    @Value("${app.sk11.password}")
    @Getter private String password;

    @Value("${app.sk11.base-path}")
    @Getter private String basePath;

    @Value("${app.sk11.web-client.max-in-memory-kbytes}")
    @Getter private Integer maxInMemoryKBytes;

    @Value("${app.sk11.long-cache.pre-cached-levels}")
    @Getter private Integer preCachedLevels;

    @Value("${app.sk11.measurement-values-bulk-size}")
    @Getter public Integer measurementValuesBulkSize;

    @Value("${app.sk11.long-cache.expires-after-days}")
    @Getter private Integer expiresAfterDays;

    @Value("${app.sk11.long-cache.invalidates-before-hours}")
    @Getter private Integer invalidatesBeforeHours;

    @Value("${app.sk11.long-cache.update-timeout-seconds}")
    @Getter private Integer longCacheUpdateTimeoutSeconds;

    @Value("${app.sk11.long-cache.random-factor-minutes}")
    @Getter private Integer randomFactorMinutes;

    @Value("${app.sk11.cert}")
    @Getter private String cert;

    @Value("${app.sk11.measurement-type.interface-state}")
    @Getter private UUID interfaceStateTypeUid;

    @Value("${app.sk11.measurement-type.not-updated}")
    @Getter private UUID notUpdatedTypeUid;

    @Value("${app.sk11.measurement-type.not-topical}")
    @Getter private UUID notTopicalTypeUid;

    @Value("${app.sk11.measurement-type.pnu}")
    @Getter private UUID pnuTypeUid;

}
