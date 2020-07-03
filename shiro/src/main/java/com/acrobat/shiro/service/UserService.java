package com.acrobat.shiro.service;

import com.acrobat.shiro.dao.UserMapper;
import com.acrobat.shiro.entity.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xutao
 * @date 2018-11-28 18:45
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public void addUser(User user) {

        userMapper.insertSelective(user);
    }


    public PageInfo<User> selectPage(int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
        PageHelper.startPage(pageNum, pageSize, "id");
        List<User> userList = userMapper.selectAll();
        PageInfo<User> resultPage = new PageInfo<>(userList);

        return resultPage;
    }
}
