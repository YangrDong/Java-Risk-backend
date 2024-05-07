package com.example.vueadminjava.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ReportVo implements Serializable {

    private final static long UID = 1L;

    private String title;

    private List<QuestionDto> details;
}
