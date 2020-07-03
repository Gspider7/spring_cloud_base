package com.acrobat.zuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 针对服务id进行限流
 *
 * 常用限流策略：针对调用服务id/服务url进行限流，针对请求ip进行限流，针对登录用户进行限流，混合限流策略
 *
 * @author xutao
 * @date 2018-11-16 17:22
 */
public class MyRateLimitFilter extends ZuulFilter {


    private Map<String, RateLimiter> map = new ConcurrentHashMap<>();

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 这边的order一定要大于org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter的order
        // 也就是要大于5
        // 否则，RequestContext.getCurrentContext()里拿不到serviceId等数据。
        return 7;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }


    /**
     * 限制每秒对服务的请求量，不适用于zuul集群
     *
     * 实际限流时可以结合redis，如果限制每分钟的请求量，就建一个有效时间1分钟的自增key
     * 每次请求对key增1，只要不超过预定次数就允许请求
     */
    @Override
    public Object run() throws ZuulException {
        try {
            RequestContext context = RequestContext.getCurrentContext();
            HttpServletResponse response = context.getResponse();
            String key = null;

            // 获取请求对应的服务id，并对每个服务做限流
            String serviceId = (String) context.get("serviceId");
            if (serviceId != null) {
                key = serviceId;
//                // 创建一个令牌桶，每秒生成1000个令牌（每隔一秒会清空再重新创建）
//                map.putIfAbsent(serviceId, RateLimiter.create(1000.0));
                map.putIfAbsent(serviceId, RateLimiter.create(0.1));        // 10秒只能请求1次
            }
            // 如果不是调用服务，而是直接调用url
            else {
                // 对于URL格式的路由，走SimpleHostRoutingFilter
                URL routeHost = context.getRouteHost();
                if (routeHost != null) {
                    String url = routeHost.toString();
                    key = url;
                    map.putIfAbsent(url, RateLimiter.create(2000.0));
                }
            }

            RateLimiter rateLimiter = map.get(key);
            if (!rateLimiter.tryAcquire()) {
                HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                response.setStatus(httpStatus.value());
                response.getWriter().append(httpStatus.getReasonPhrase());
                context.setSendZuulResponse(false);
                throw new ZuulException(
                        httpStatus.getReasonPhrase(),
                        httpStatus.value(),
                        httpStatus.getReasonPhrase()
                );
            }
        } catch (IOException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        return null;
    }
}
