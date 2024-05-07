package com.example.vueadminjava.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author peter
 * @since 2024-03-19
 */
@Data
public class Zhibiao implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 指标id
     */
    private int id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标层级
     */
    private int level;

    /**
     * 父级指标id
     */
    private int parentId;

    /**
     * 指标id全路径
     */
    private String fullPath;

    /**
     * 指标描述
     */
    private String detail;

    /**
     * 子指标
     */
    private List<Zhibiao> childList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
    public String getDetail() {
        return detail;
    }

    public void setDesc(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Zhibiao{" +
            "id=" + id +
            ", name=" + name +
            ", level=" + level +
            ", parentId=" + parentId +
            ", fullPath=" + fullPath +
            ", detail=" + detail +
        "}";
    }
}
