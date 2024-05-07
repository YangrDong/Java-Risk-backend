package com.example.vueadminjava.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.vueadminjava.common.lang.Result;
import com.example.vueadminjava.entity.ZhiBiaoDto;
import com.example.vueadminjava.service.IZhibiaoService;
import com.example.vueadminjava.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    IZhibiaoService zhibiaoService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

//    @PreAuthorize("hasRole('admin')")
//    @GetMapping("/test")
//    public Object test() {
//        return Result.succ(sysUserService.list());
//    }

    @GetMapping("/show1")
    @ResponseBody
    public Result show(){

        ZhiBiaoDto simplifiedList = zhibiaoService.getZhiBiaoList();
        Result result = new Result();
        result.setData(simplifiedList);
        return result;
    }

    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/test/pass")
    public Result<String> pass() {

        // 加密后密码
        String password = bCryptPasswordEncoder.encode("111111");

        boolean matches = bCryptPasswordEncoder.matches("111111", password);

        System.out.println("匹配结果：" + matches);

        Result<String> result = new Result<>();
        result.succ(password);
        return result;
    }
}
