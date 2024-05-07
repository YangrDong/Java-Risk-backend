package com.example.vueadminjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vueadminjava.entity.Zhibiao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author peter
 * @since 2024-03-19
 */

@Mapper
public interface ZhibiaoMapper extends BaseMapper<Zhibiao> {

    Zhibiao selectZhibiaos(int parentId);

    List<Zhibiao> queryDetails(String detail);

    List<Zhibiao> queryQuestions(Page<Zhibiao> page);

}
