package com.example.vueadminjava.service;

import com.example.vueadminjava.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author peter
 * @since 2023-08-03
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);
//
    String getUserAuthorityInfo(Long userId);




}
