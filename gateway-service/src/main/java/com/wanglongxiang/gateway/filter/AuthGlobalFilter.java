package com.wanglongxiang.gateway.filter;

import com.wanglongxiang.gateway.properties.AuthProperties;
import com.wanglongxiang.gateway.properties.JwtProperties;
import com.wanglongxiang.gateway.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    AuthProperties authProperties;
     private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public final String UID = "uid";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        //        判断是否需要做登录拦截
        String path = request.getPath().toString();
//        System.out.println("path = " + request.getPath());
        if(isExclude(path)){
            return chain.filter(exchange);
        }


        HttpHeaders headers = request.getHeaders();
        try{
            String token = headers.get(jwtProperties.getTokenName()).get(0);
            Claims claims = JwtUtils.parseJWT(jwtProperties.getSecretKey(),token);
            String userId = claims.get(UID).toString();
            //        将用户信息添加到请求头向下传递
            String userInfo = userId.toString();
            ServerWebExchange build = exchange.mutate()
                    .request(builder -> builder.header(UID, userInfo))
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    private boolean isExclude(String path) {
        log.info("{}",authProperties.getExcludePaths());
        for (String excludePath : authProperties.getExcludePaths()) {
            if(antPathMatcher.match(excludePath,path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
