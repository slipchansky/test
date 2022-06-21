package com.upsic.kkc.sk11.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Sk11AddressDto {
    private String address;
    private Integer priority;
}
