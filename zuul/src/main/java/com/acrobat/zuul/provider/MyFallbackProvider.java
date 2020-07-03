package com.acrobat.zuul.provider;

import com.acrobat.zuul.constant.Statistic;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义服务调用超时熔断返回
 * @author xutao
 * @date 2018-11-16 15:38
 */
public class MyFallbackProvider implements FallbackProvider {

    @Override
    public String getRoute() {
//        return "*";             // 该Provider应用在哪个服务/路由上，*表示对所有服务生效
        return "eureka-client";
    }

    /**
     * 具体的熔断处理
     */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        String message = "";
        if (cause instanceof HystrixTimeoutException) {
            message = "请求超时";
        } else {
            message = "请求异常" + cause;
        }

        Statistic.fallback_count.getAndAdd(1);
        return fallbackResponse(message);
    }


    private ClientHttpResponse fallbackResponse(String message) {

        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                String bodyText = String.format("{\"code\": 999,\"message\": \"Service unavailable:%s\"}", message);
                return new ByteArrayInputStream(bodyText.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };

    }
}
