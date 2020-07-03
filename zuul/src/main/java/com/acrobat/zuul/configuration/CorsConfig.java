package com.acrobat.zuul.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 网关cors跨域设置，通过配置springMVC的filter来实现
 * @author xutao
 * @date 2018-11-20 17:11
 */
@Configuration
public class CorsConfig {


    /**
     * 如果有问题，尝试在yml中配置
     * zuul:
     *   ignored-headers: Access-Control-Allow-Origin,H-APP-Id,Token,APPToken
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);       // 允许cookies跨域
        config.addAllowedOrigin("*");           // 允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedHeader("*");           // 允许访问的头信息，*表示全部
        config.setMaxAge(18000L);               // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("*");           // 允许提交请求的方法，*表示全部允许
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    /**
     * 网关配置了跨域，微服务就不需要配置跨域了
     * 如果微服务要配置，应该这样配置：添加如下class
     */
//    @Component
//    public class CorsFilter implements Filter {
//        @Override
//        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//            HttpServletRequest request = (HttpServletRequest) req;
//            HttpServletResponse response = (HttpServletResponse) res;
//       /* String curOrigin = request.getHeader("Origin");
//        System.out.println("###跨域过滤器->当前访问来源->"+curOrigin+"###");   */
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "*");
//            response.setHeader("Access-Control-Max-Age", "3600");
//            response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//            chain.doFilter(req, res);
//        }
//        @Override
//        public void init(FilterConfig filterConfig) {}
//
//        @Override
//        public void destroy() {}
//    }
}
