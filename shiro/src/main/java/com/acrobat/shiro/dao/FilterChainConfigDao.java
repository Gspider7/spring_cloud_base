package com.acrobat.shiro.dao;

import com.acrobat.shiro.entity.FilterChainConfig;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro FilterChain配置service
 * @author xutao
 * @date 2018-11-29 17:38
 */
@Repository
public class FilterChainConfigDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FilterChainConfigMapper filterChainConfigMapper;

    /**
     * 获取FilterChain的配置map
     * 注意：LinkedHashMap是有序的
     */
    public Map<String, String> loadFilterChainDefinitions() {
        List<FilterChainConfig> filterChainConfigs = listFilterChainConfigByOrder();
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        for (FilterChainConfig config : filterChainConfigs) {
            filterChainDefinitionMap.put(config.getUri(), config.getFilterConfig());
        }

        return filterChainDefinitionMap;
    }

    /**
     * 列出所有的shiro FilterChain配置，按照order排序
     */
    public List<FilterChainConfig> listFilterChainConfigByOrder() {
        int pageNum = 1;
        int pageSize = 50;

        PageHelper.startPage(pageNum, pageSize, "sort");
        List<FilterChainConfig> configList = filterChainConfigMapper.selectAll();
        List<FilterChainConfig> resultList = new ArrayList<>(configList);
        PageInfo<FilterChainConfig> pageInfo = new PageInfo<>(configList);

        while (pageInfo.isHasNextPage()) {
            pageNum ++;

            PageHelper.startPage(pageNum, pageSize, "order");
            configList = filterChainConfigMapper.selectAll();
            resultList.addAll(configList);
            pageInfo = new PageInfo<>(configList);
        }

        return resultList;
    }



}
