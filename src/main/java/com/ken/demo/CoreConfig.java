package com.ken.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.MultiValueMap;

@Configuration
public class CoreConfig {
    @Bean
    public HttpMessageConverter<MultiValueMap<String, ?>> createConverter() {
        return new CustomizedFormHttpMsgConverter();
    }
}
