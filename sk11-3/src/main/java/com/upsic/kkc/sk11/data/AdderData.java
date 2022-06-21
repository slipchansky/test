package com.upsic.kkc.sk11.data;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import com.upsic.kkc.sk11.enums.AdderType;

@Data
@Builder
public class AdderData {

    private String idAdder;

    private UUID uid;

    private AdderType type;

    private String parentId;

    private String name;

}
