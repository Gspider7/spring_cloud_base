package com.acrobat.shiro.controller;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取druid监控信息
 * @author xutao
 * @date 2018-11-28 17:24
 */
@RestController
public class DruidStatController {

    @GetMapping("/druid/stat")
    public Object druidStat(){
        // 获取所有数据源的监控数据，除此之外DruidStatManagerFacade 还提供了一些其他方法，你可以按需选择使用。
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}
