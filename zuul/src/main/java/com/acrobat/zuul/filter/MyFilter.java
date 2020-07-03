package com.acrobat.zuul.filter;

import com.acrobat.zuul.constant.Statistic;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;


/**
 * 继承Zuul过滤器，并配置过滤规则
 * @author xutao
 * @date 2018-11-16 10:35
 */
public class MyFilter extends ZuulFilter {


    /**
     * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
         * pre：可以在请求被路由之前调用
         * routing：在路由请求时候被调用
         * post：在routing和error过滤器之后被调用
         * error：处理请求时发生错误时被调用
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 过滤器的执行顺序，数字越小越先执行
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * @return  过滤器是否应该被执行
     */
    @Override
    public boolean shouldFilter() {
        return false;
    }

    /**
     * 具体的过滤规则
     * 这里要求请求必须传任意名为name的参数
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        Statistic.req_count.getAndAdd(1);

//        Object accessToken = request.getParameter("name");
//        if (accessToken == null) {
//            ctx.setSendZuulResponse(false);                 // 不再对请求进行路由
//            ctx.setResponseStatusCode(401);                 // 返回请求错误码
//            return null;
//        }

        return null;
    }
}
