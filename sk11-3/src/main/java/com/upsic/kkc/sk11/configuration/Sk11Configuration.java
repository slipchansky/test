package com.upsic.kkc.sk11.configuration;

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

    @Value("${app.sk11.api-path}")
    private String apiPath;

    @Value("${app.sk11.model-version}")
    private String modelVersion;

    @Value("${app.sk11.user}")
    private String user;

    @Value("${app.sk11.password}")
    private String password;

    @Value("${app.sk11.web-client.max-in-memory-kbytes}")
    private Integer maxInMemoryKBytes;

    @Value("${app.sk11.cert}")
    private String cert;

    @Value("${app.sk11.guid.general-model}")
    private UUID generalModelGuid;

    @Value("${app.sk11.guid.measurement-type.p-gen}")
    private UUID pGenGuid;

    @Value("${app.sk11.guid.measurement-type.q-gen}")
    private UUID qGenGuid;

    @Value("${app.sk11.guid.measurement-type.q-load-reserve}")
    private UUID qLoadReserveGuid;

    @Value("${app.sk11.guid.measurement-type.q-unload-reserve}")
    private UUID qUnloadReserveGuid;

    @Value("${app.sk11.guid.measurement-type.voltage}")
    private UUID voltageGuid;


    @Value("${app.sk11.guid.measurement-value-type.udg}")
    private UUID udgGuid;

    @Value("${app.sk11.guid.measurement-value-type.fact}")
    private UUID factGuid;

    @Value("${app.sk11.guid.measurement-value-type.pbr}")
    private UUID pbrGuid;

    @Value("${app.sk11.guid.operational-authority.shift-work}")
    private UUID shiftWorkGuid;

}
