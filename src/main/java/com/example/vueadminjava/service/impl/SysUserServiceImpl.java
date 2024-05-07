package com.example.vueadminjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vueadminjava.entity.SysUser;
import com.example.vueadminjava.mapper.SysUserMapper;
import com.example.vueadminjava.service.SysUserService;
import com.example.vueadminjava.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author peter
 * @since 2023-08-03
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {



    @Autowired
    SysUserMapper sysUserMapper;


    @Autowired
    RedisUtil redisUtil;

    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {

        SysUser sysUser = sysUserMapper.selectById(userId);

        //  ROLE_admin,ROLE_normal,sys:user:list,....       所有的角色前面都要加入ROLE_前缀，这样才能识别好像
        String authority = "ROLE_admin";

        // redis中有缓存的话就从缓存中获取
        if (redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
            authority = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername());
        } else {
            // 获取角色编码
            redisUtil.set("GrantedAuthority:" + sysUser.getUsername(), authority);
            redisUtil.persist("GrantedAuthority:" + sysUser.getUsername());//设置永不过期
        }

        return authority;
    }




}
