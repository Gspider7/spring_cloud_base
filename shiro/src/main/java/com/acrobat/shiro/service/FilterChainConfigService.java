package com.acrobat.shiro.service;

import com.acrobat.shiro.dao.FilterChainConfigDao;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xutao
 * @date 2018-11-29 19:20
 */
@Service
public class FilterChainConfigService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private FilterChainConfigDao filterChainConfigDao;
    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;


    /**
     * 重新加载shiro FilterChain配置，定义一个Controller开放这个接口
     */
    public void refreshFilterChainConfiguration()  {

        synchronized (this) {

            try {
                AbstractShiroFilter shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
                PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                        .getFilterChainResolver();
                DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                        .getFilterChainManager();

                // 清空旧的配置，加载新的配置
                Map<String, String> filterChainDefinitionMap = filterChainConfigDao.loadFilterChainDefinitions();
                manager.getFilterChains().clear();
                shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
                shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

                // 重新构建生成
                Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
                for (Map.Entry<String, String> entry : chains.entrySet()) {
                    String url = entry.getKey();
                    String chainDefinition = entry.getValue().trim().replace(" ", "");
                    manager.createChain(url, chainDefinition);
                }

                logger.info("更新shiro FilterChain配置成功");
            }
            catch (Exception e) {
                logger.error("更新shiro FilterChain配置失败", e);
            }
        }
    }
}
