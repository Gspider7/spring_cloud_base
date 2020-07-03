package com.acrobat.shiro.filter;

import com.acrobat.shiro.constant.JWTConfig;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * shiro使用JWT(java web token)
 * 1、对于本地请求，可以使用JWT验证替换authc过滤器的验证（当前filter的功能）
 * 2、实现单点登录验证，单点登录系统参考：https://www.cnblogs.com/ywlaker/p/6113927.html
 *      认证中心提供登录接口和认证JWT的接口，其它业务系统不提供登录入口但是保存本地session（JWT是sessionId），避免每次认证都要访问认证中心
 *      业务系统收到带JWT的请求，如果本地没有session，请求认证中心验证JWT，认证中心认证完成把认证结果和请求地址返回给业务系统
 *
 * @author xutao
 * @date 2018-12-05 13:39
 */
public class JWTControlFilter extends AccessControlFilter {




    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);

        // 如果没有JWT，跳转登录页面，如果有则进行验证
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String jwt = httpRequest.getHeader(JWTConfig.JWT_KEY);
        if (jwt == null) {
            // 执行重定向
//            saveRequest(request);
//            WebUtils.issueRedirect(request, response, loginUrl);
//            return false;
        }

        // 解析JWT，校验JWT是否包含用户信息，是否超时失效


        return true;
    }


}
