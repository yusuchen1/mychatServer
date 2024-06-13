package com.wanglongxiang.mychat.config;

import com.wanglongxiang.mychat.interceptor.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Slf4j
@EnableSwagger2
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    UserInterceptor userInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("正在注册拦截器...");
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login","/user/register","/swagger*","/v2/**");
        super.addInterceptors(registry);
    }

    /**
     * swagger和postman功能很像，用于给程序员测试接口，但是swagger效率高于postman
     * 通过knife4j生成接口文档
     * knife4j是为java mvc框架集成swagger生成api文档的增强解决方案，可以通过扫描包的方式自动生成接口测试案例
     * @return
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("mychat项目接口文档")
                .version("1.0")
                .description("mychat项目接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wanglongxiang.mychat.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
