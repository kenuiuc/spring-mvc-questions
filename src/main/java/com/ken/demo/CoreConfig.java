package com.ken.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;

@Configuration
public class CoreConfig {
    @Bean("customizedFormHttpMsgConverter")
    public FormHttpMessageConverter createMyConverter() {
        return new CustomizedFormHttpMsgConverter();
    }
}
