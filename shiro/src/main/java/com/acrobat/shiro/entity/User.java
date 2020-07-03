package com.acrobat.shiro.entity;


import com.acrobat.shiro.utils.RandomStringUtil;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.io.Serializable;

public class User implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String name;
    /** 用来生成MD5的随机值 */
    private String salt;

    public User() {
    }

    public User(String username, String password, String name) {
        this.username = username;
        this.name = name;

        this.salt = RandomStringUtil.getRandomString(12);
        // Shiro工具类Md5Hash计算MD5
        this.password = new Md5Hash(password, salt).toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }
}