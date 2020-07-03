package com.acrobat.shiro.dao;

import com.acrobat.shiro.entity.Role;

public interface RoleMapper {
    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}