package com.example.vueadminjava.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer detailId;

    private String parentsId;

    private String question;

}
