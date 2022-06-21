package com.upsic.kkc.sk11.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class LuaScriptDto {
    private String luaScript;
    private String name;
}
