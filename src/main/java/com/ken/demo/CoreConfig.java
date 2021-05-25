package com.ken.demo;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.MultiValueMap;

@Configuration
public class CoreConfig {

    @Bean
    public FormHttpMessageConverter createMyConverter() {
        return new CustomizedFormHttpMsgConverter();
    }
//    @Bean
//    public HttpMessageConverters createConverters() {
//        return new CustomizedHttpMsgConverters();
//    }
}
