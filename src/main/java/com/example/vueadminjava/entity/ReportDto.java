package com.example.vueadminjava.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReportDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer reportId;

    private String question;

    private String firstOrderName;

    private String secondOrderName;

    private String thirdOrderName;

    private String fourthOrderName;

    private String fifthOrderName;

    private String remark;

    private String[] imagePath;
}
