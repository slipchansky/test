package com.upsic.kkc.dto.sk11api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Sk11AuthDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("user_login")
    private String userLogin;

    @JsonProperty("user_host")
    private String userHost;

    @JsonProperty("user_ip")
    private String userIp;

}
