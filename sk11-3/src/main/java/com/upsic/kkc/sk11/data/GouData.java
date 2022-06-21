package com.upsic.kkc.sk11.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.upsic.kkc.sk11.enums.GouType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GouData {

    private String idGou;

    private UUID uid;

    private GouType type;

    private String parentId;

    private Boolean inSpread;

    private List<PowerGenValueData> values;

    private String name;

    private String priznakGou;

}
