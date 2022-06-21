package com.upsic.kkc.sk11.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerErrorDto {

    private String type;

    private String title;

    private Integer status;

    private String detail;

    private String instance;

    private AnswerErrorEntityDto entity;

}
