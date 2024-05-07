package com.example.vueadminjava.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ZhiBiaoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String value;

    private List<ZhiBiaoDto> children;
}
