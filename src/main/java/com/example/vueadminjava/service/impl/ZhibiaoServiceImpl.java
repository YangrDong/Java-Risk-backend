package com.example.vueadminjava.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vueadminjava.entity.Question;
import com.example.vueadminjava.entity.ZhiBiaoDto;
import com.example.vueadminjava.entity.Zhibiao;
import com.example.vueadminjava.mapper.AccessMapper;
import com.example.vueadminjava.mapper.ZhibiaoMapper;
import com.example.vueadminjava.service.IZhibiaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author peter
 * @since 2024-03-19
 */
@Service
public class ZhibiaoServiceImpl extends ServiceImpl<ZhibiaoMapper, Zhibiao> implements IZhibiaoService {

    private static boolean flag = true;

    @Autowired
    private ZhibiaoMapper zhibiaoMapper;

    @Autowired
    private AccessMapper accessMapper;

    @Override
    public ZhiBiaoDto getZhiBiaoList() {
        Zhibiao zhibiao = zhibiaoMapper.selectZhibiaos(-1);

        ZhiBiaoDto zhiBiaoDto = new ZhiBiaoDto();
        zhiBiaoDto.setName(zhibiao.getName());
        zhiBiaoDto.setValue(zhibiao.getDetail());
        zhiBiaoDto.setChildren(simplifyChildren(zhibiao.getChildList()));

        return zhiBiaoDto;
    }

    private List<ZhiBiaoDto> simplifyChildren(List<Zhibiao> children) {
        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }
        List<ZhiBiaoDto> simplifiedChildren = new ArrayList<>();
        for (Zhibiao child : children) {
            ZhiBiaoDto zhiBiaoDtoChild = new ZhiBiaoDto();
            zhiBiaoDtoChild.setName(child.getName());
            zhiBiaoDtoChild.setValue(child.getDetail());
            zhiBiaoDtoChild.setChildren(simplifyChildren(child.getChildList()));
            simplifiedChildren.add(zhiBiaoDtoChild);
        }
        return simplifiedChildren;
    }


    @Override
    public List<Zhibiao> getDetailByParam(String detail) {
        List<Zhibiao> details = zhibiaoMapper.queryDetails(detail);
        if (CollectionUtils.isEmpty(details)){
            return null;
        }
        return details;
    }

    @Override
    public List<Question> getQuestion() {
        Page<Zhibiao> page = new Page<>(1,10);
        List<Zhibiao> zhibiaos = zhibiaoMapper.queryQuestions(page);
        List<Question> questions = new ArrayList<>();
        for (Zhibiao zhibiao : zhibiaos) {
            Question question = new Question();
            question.setDetailId(zhibiao.getId());
            question.setQuestion(zhibiao.getDetail());
            //获取1-5级指标的id
            String[] idString = zhibiao.getFullPath().split("-");
            String ids= String.join(",",idString);
            question.setParentsId(ids);
            questions.add(question);
        }

        //同步问题至sys_questions表(同步一次就可以)
//        Boolean aBoolean = accessMapper.insertQuestions(questions);

        return questions;
    }


}
