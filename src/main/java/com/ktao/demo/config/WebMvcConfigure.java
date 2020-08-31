package com.ktao.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * WebMvc的通用配置类
 * @author kongtao
 * @version 1.0
 * @description:
 * @date 2020/8/28
 */
@Configuration
public class WebMvcConfigure extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration loginRequiredInterceptor = registry.addInterceptor(loginRequiredInterceptor());
        pathPatterns(loginRequiredInterceptor);

        InterceptorRegistration userCookieInterceptor = registry.addInterceptor(userCookieInterceptor());
        pathPatterns(userCookieInterceptor);

        InterceptorRegistration urlPermissionInterceptor = registry.addInterceptor(urlPermissionInterceptor());
        pathPatterns(urlPermissionInterceptor);

        InterceptorRegistration traceIdInterceptor = registry.addInterceptor(httpServerTraceIdInterceptor());
        pathPatterns(traceIdInterceptor);
    }

    /**
     *
     * @param interceptor
     */
    private void pathPatterns(InterceptorRegistration interceptor){
        interceptor.excludePathPatterns("/error");
        interceptor.excludePathPatterns("/login**");
        interceptor.addPathPatterns("/**");
    }


//    @Bean
//    public UserCookieInterceptor userCookieInterceptor() {
//        return new UserCookieInterceptor();
//    }
//
//    @Bean
//    public LoginRequiredInterceptor loginRequiredInterceptor() {
//        return new LoginRequiredInterceptor();
//    }
//
//    @Bean
//    public UrlPermissionInterceptor urlPermissionInterceptor() {
//        return new UrlPermissionInterceptor();
//    }
//
//    @Bean
//    public HttpServerTraceIdInterceptor httpServerTraceIdInterceptor() {
//        return new HttpServerTraceIdInterceptor();
//    }
}
