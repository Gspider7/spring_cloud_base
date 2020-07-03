package com.acrobat.shiro.dao;

import com.acrobat.shiro.entity.FilterChainConfig;

import java.util.List;

public interface FilterChainConfigMapper {
    int insert(FilterChainConfig record);

    int insertSelective(FilterChainConfig record);


    List<FilterChainConfig> selectAll();
}