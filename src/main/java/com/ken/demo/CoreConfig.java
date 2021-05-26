package com.ken.demo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;

@Configuration
public class CoreConfig {

    @Bean("customizedFormHttpMsgConverter")
    public FormHttpMessageConverter createMyConverter() {
        return new CustomizedFormHttpMsgConverter();
    }

//    @Bean
//    @ConditionalOnBean(value = FormHttpMessageConverter.class, annotation = MyBean.class)
//    public HttpMessageConverters createConverters() {
//        return new CustomizedHttpMsgConverters();
//    }
}
