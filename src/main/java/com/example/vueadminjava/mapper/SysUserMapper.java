package com.example.vueadminjava.mapper;

import com.example.vueadminjava.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author peter
 */

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

//    List<Long> getNavMenuIds(Long userId);
//
//    List<SysUser> listByMenuId(Long menuId);
}
