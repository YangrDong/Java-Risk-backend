package com.example.vueadminjava.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class CfSysQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer reportId;

    private Integer detailId;

    private String question;

    private Boolean answer;

    private String remark;

    private String imagePath;
}
