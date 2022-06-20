package com.upsic.kkc.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * Параметры для интеграции с СК-11
 */
@Configuration
@Getter
public class Sk11Configuration {

    @Value("${app.sk11.web-client.connect-timeout}")
    public Integer connectTimeout;

    @Value("${app.sk11.web-client.read-timeout}")
    public Long readTimeout;

    @Value("${app.sk11.api-version}")
    private String apiVersion;

    @Value("${app.sk11.model-version}")
    private String modelVersion;

    @Value("${app.sk11.user}")
    private String user;

    @Value("${app.sk11.password}")
    private String password;

    @Value("${app.sk11.base-path}")
    private String basePath;

    @Value("${app.sk11.web-client.max-in-memory-kbytes}")
    private Integer maxInMemoryKBytes;

    @Value("${app.sk11.long-cache.pre-cached-levels}")
    private Integer preCachedLevels;

    @Value("${app.sk11.measurement-values-bulk-size}")
    public Integer measurementValuesBulkSize;

    @Value("${app.sk11.long-cache.expires-after-days}")
    private Integer expiresAfterDays;

    @Value("${app.sk11.long-cache.invalidates-before-hours}")
    private Integer invalidatesBeforeHours;

    @Value("${app.sk11.long-cache.update-timeout-seconds}")
    private Integer longCacheUpdateTimeoutSeconds;

    @Value("${app.sk11.long-cache.random-factor-minutes}")
    private Integer randomFactorMinutes;

    @Value("${app.sk11.cert}")
    private String cert;

    @Value("${app.sk11.measurement-type.interface-state}")
    private UUID interfaceStateTypeUid;

    @Value("${app.sk11.measurement-type.not-updated}")
    private UUID notUpdatedTypeUid;

    @Value("${app.sk11.measurement-type.not-topical}")
    private UUID notTopicalTypeUid;

    @Value("${app.sk11.measurement-type.pnu}")
    private UUID pnuTypeUid;

}
