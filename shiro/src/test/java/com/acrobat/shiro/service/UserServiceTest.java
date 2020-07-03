package com.acrobat.shiro.service;

import com.acrobat.shiro.entity.User;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xutao
 * @date 2018-11-28 18:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;


    @Test
    public void any() {
        String password = "123456";
        String salt = "aYPaIR3qYhM7";

        String md5 = new Md5Hash(password, salt).toString();
        System.out.println(md5);
    }

    @Test
    public void addUser() {
        userService.addUser(new User("11", "123456", "a"));
        userService.addUser(new User("22", "123456", "a"));
        userService.addUser(new User("33", "123456", "a"));
        userService.addUser(new User("44", "123456", "a"));
        userService.addUser(new User("55", "123456", "a"));
        userService.addUser(new User("66", "123456", "a"));
        userService.addUser(new User("77", "123456", "a"));
    }

    @Test
    public void selectPage() {
        PageInfo<User> pageInfo = userService.selectPage(3, 3);

        System.out.println(JSON.toJSONString(pageInfo));
    }
}