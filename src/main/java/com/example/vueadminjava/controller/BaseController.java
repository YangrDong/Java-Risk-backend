package com.example.vueadminjava.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vueadminjava.service.*;
import com.example.vueadminjava.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;


public class BaseController {

    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SysUserService sysUserService;


    /**
     * 获取页面
     * @return
     */
    public Page getPage() {
        int current = ServletRequestUtils.getIntParameter(req, "cuurent", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 10);

        return new Page(current, size);
    }

}
