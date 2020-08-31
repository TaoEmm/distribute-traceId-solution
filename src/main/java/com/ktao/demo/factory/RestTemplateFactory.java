package com.ktao.demo.factory;

import com.ktao.demo.interceptor.HttpClientTraceIdInterceptor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * RestTemplate的工厂类，用来获取一个RestTemplate的实例
 * @author kongtao
 * @version 1.0
 * @description
 * @date 2020/8/28
 */
public class RestTemplateFactory {
    private static class RestTemplateFactoryHolder{
        private static RestTemplateFactory instance = new RestTemplateFactory();
    }

    private RestTemplateFactory(){

    }

    public static RestTemplateFactory getInstance(){

        return RestTemplateFactoryHolder.instance;
    }


    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        initRestTemplate(restTemplate);
        return restTemplate;
    }

    private void initRestTemplate(RestTemplate restTemplate) {
        // add interceptors
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HttpClientTraceIdInterceptor());
        restTemplate.setInterceptors(interceptors);
    }
}