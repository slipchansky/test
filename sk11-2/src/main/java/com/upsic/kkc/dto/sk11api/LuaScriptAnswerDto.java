package com.upsic.kkc.dto.sk11api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class LuaScriptAnswerDto<T> {

    private Long version;
    private List<T> value;
}
