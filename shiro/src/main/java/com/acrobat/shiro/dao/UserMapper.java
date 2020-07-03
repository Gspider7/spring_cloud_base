package com.acrobat.shiro.dao;

import com.acrobat.shiro.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


    User selectByUsername(@Param("username") String username);

    List<User> selectAll();
}