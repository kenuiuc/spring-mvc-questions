package com.ken.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;

import java.util.List;

public class CustomizedHttpMsgConverters extends HttpMessageConverters {

    @Autowired
    @Qualifier("customizedFormHttpMsgConverter")
    private FormHttpMessageConverter customizedFormHttpMsgConverter;

    @Override
    protected List<HttpMessageConverter<?>> postProcessConverters(List<HttpMessageConverter<?>> converters) {
        System.out.println("Before touching the converters:");
        converters.forEach(converter -> System.out.println(converter.getClass().getSimpleName()));
        System.out.println();

        converters.removeIf(converter -> converter.getClass().equals(
                AllEncompassingFormHttpMessageConverter.class));

        System.out.println("After removing default form converter:");
        converters.forEach(converter -> System.out.println(converter.getClass().getSimpleName()));
        System.out.println();

        converters.add(customizedFormHttpMsgConverter);

        System.out.println("After adding my customized form converter:");
        converters.forEach(converter -> System.out.println(converter.getClass().getSimpleName()));
        System.out.println();

        return converters;
    }
}
