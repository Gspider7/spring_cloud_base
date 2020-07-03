package com.acrobat.shiro.dao;

import com.acrobat.shiro.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionMapper {
    int insert(RolePermission record);

    int insertSelective(RolePermission record);


    List<RolePermission> listByRoleId(@Param("roleId") long roleId);
}