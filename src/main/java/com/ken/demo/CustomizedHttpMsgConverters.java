package com.ken.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.util.List;

public class CustomizedHttpMsgConverters extends HttpMessageConverters {

    @Override
    protected List<HttpMessageConverter<?>> postProcessConverters(List<HttpMessageConverter<?>> converters) {
        System.out.println("before:");
        converters.forEach(converter -> System.out.println(converter.getClass().getSimpleName()));
        converters.removeIf(converter -> converter instanceof FormHttpMessageConverter);
        converters.add( new CustomizedFormHttpMsgConverter());
        System.out.println("after:");
        converters.forEach(converter -> System.out.println(converter.getClass().getSimpleName()));
        return converters;
    }
}
