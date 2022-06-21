package com.upsic.kkc.dto.sk11api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class Sk11CheckAddingLevelDto {

    private Boolean correct; //Если этот флаг false - то добавление запрещено

}
