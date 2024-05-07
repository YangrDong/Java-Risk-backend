package com.example.vueadminjava.controller;


import cn.hutool.core.lang.Assert;
import com.example.vueadminjava.common.dto.PassDto;
import com.example.vueadminjava.common.lang.Const;
import com.example.vueadminjava.common.lang.Result;
import com.example.vueadminjava.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author peter
 * @since 2023-08-03
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/info/{id}")
    public Result<SysUser> info(@PathVariable("id") Long id) {

        SysUser sysUser = sysUserService.getById(id);
        Assert.notNull(sysUser, "找不到该用户");


        Result<SysUser> result = new Result<>();
        result.succ(sysUser);
        return result;
    }



    @PreAuthorize("hasAuthority('sys:user:save')")
    @PostMapping("/save")
    public Result<SysUser> save(@Validated @RequestBody SysUser sysUser) {

        sysUser.setCreated(LocalDateTime.now());
        sysUser.setStatu(Const.STATUS_ON);

        // 默认密码
        String password = passwordEncoder.encode(Const.DEFULT_PASSWORD);
        sysUser.setPassword(password);

        // 默认头像
        sysUser.setAvatar(Const.DEFULT_AVATAR);
        sysUserService.save(sysUser);
        Result<SysUser> result = new Result<>();
        result.succ(sysUser);
        return result;
    }

    @PreAuthorize("hasAuthority('sys:user:update')")
    @PostMapping("/update")
    public Result<SysUser> update(@Validated @RequestBody SysUser sysUser) {

        sysUser.setUpdated(LocalDateTime.now());
        sysUserService.updateById(sysUser);
        Result<SysUser> result = new Result<>();
        result.succ(sysUser);
        return result;
    }

    @PreAuthorize("hasAuthority('sys:user:repass')")
    @PostMapping("/repass")
    public Result<String> repass(@RequestBody Long userId) {

        SysUser sysUser = sysUserService.getById(userId);

        sysUser.setPassword(passwordEncoder.encode(Const.DEFULT_PASSWORD));
        sysUser.setUpdated(LocalDateTime.now());

        sysUserService.updateById(sysUser);
        Result<String> result = new Result<>();
        result.succ("");
        return result;
    }

    @PostMapping("/updatePass")
    public Result<String> updatePass(@Validated @RequestBody PassDto passDto, Principal principal) {

        SysUser sysUser = sysUserService.getByUsername(principal.getName());

        boolean matches = passwordEncoder.matches(passDto.getCurrentPass(), sysUser.getPassword());
        if (!matches) {
            return Result.fail("旧密码不正确");
        }

        sysUser.setPassword(passwordEncoder.encode(passDto.getPassword()));
        sysUser.setUpdated(LocalDateTime.now());

        sysUserService.updateById(sysUser);
        Result<String> result = new Result<>();
        result.succ("");
        return result;
    }
}
