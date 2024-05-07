package com.example.vueadminjava.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
public class QuestionDto implements Serializable {

    private final static long Uid = 1L;

    private Integer detailId;

    private Integer answer;

    private String remark;

    private String files;
}
