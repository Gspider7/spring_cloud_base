package com.acrobat.shiro.dao;

import com.acrobat.shiro.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper {
    int insert(UserRole record);

    int insertSelective(UserRole record);

    List<UserRole> listByUserId(@Param("userId") long userId);
}