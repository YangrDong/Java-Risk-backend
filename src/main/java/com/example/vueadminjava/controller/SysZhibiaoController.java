package com.example.vueadminjava.controller;

import com.example.vueadminjava.common.lang.Result;
import com.example.vueadminjava.entity.ZhiBiaoDto;
import com.example.vueadminjava.entity.Zhibiao;
import com.example.vueadminjava.service.IZhibiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author peter
 * @since 2024-03-19
 */
@RestController
@RequestMapping("/sys")
public class SysZhibiaoController {

    @Autowired
    private IZhibiaoService zhibiaoService;

    /**
     * 指标树形图展示
     * @return
     */
    @GetMapping("/show")
    @ResponseBody
    public Result<ZhiBiaoDto> show(){
        ZhiBiaoDto zhiBiaoDto = zhibiaoService.getZhiBiaoList();
        Result<ZhiBiaoDto> result = new Result<>();
        result.setData(zhiBiaoDto);
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    /**
     * 查询指标详情
     * @param detail
     * @return
     */
    @PostMapping("/search")
    public Result<List<Zhibiao>> search(@RequestParam("detail") String detail){
        List<Zhibiao> details = zhibiaoService.getDetailByParam(detail);
        Result<List<Zhibiao>> result = new Result<>();
        if (CollectionUtils.isEmpty(details)|| StringUtils.isEmpty(detail)){
            result.setMsg("执行成功");
            result.setCode(200);
            result.setData(null);
            return result;
        }
        result.setMsg("执行成功");
        result.setCode(200);
        result.setData(details);
        return result;
    }




}
