package com.example.vueadminjava.service;

import com.example.vueadminjava.entity.Question;
import com.example.vueadminjava.entity.ZhiBiaoDto;
import com.example.vueadminjava.entity.Zhibiao;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author peter
 * @since 2024-03-19
 */
public interface IZhibiaoService extends IService<Zhibiao> {

//    List<JSONObject> getZhiBiaoList();
    ZhiBiaoDto getZhiBiaoList();

    List<Zhibiao> getDetailByParam(String detail);

    List<Question> getQuestion();
}

