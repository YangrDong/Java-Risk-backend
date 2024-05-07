package com.example.vueadminjava.entity;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Report implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String reportTitle;

    private String questionIds;//存储答案=1的问题的detail_id

    private Timestamp createTime;

}
