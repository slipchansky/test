package com.upsic.kkc.sk11.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Sk11ModelDto {

    private UUID modelUid;

    private String modelDisplayName;

    private String modelCanonicalName;

    private String profileVersion;

    private List<String> features;

}
