package com.ken.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @PutMapping(value = "/user", consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public String putUser(@RequestParam Map<String, String> data) throws Exception {
        return data.get("name");
    }

    @GetMapping(value = "/user")
    public String getUser() {
        return "KEN";
    }

    // DEBUG area
    @Autowired
    private List<WebMvcConfigurer> configurers;
    @Autowired
    private List<HttpMessageConverter<?>> converters;
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @GetMapping("myConfig")
    public Map<String, Object> config() {
        Map<String, Object> map = new HashMap<>();
        map.put("configurers", toStrings(configurers));
        map.put("converters", toStrings(converters));
        map.put("requestMappingHandlerAdapter-converters", toStrings(requestMappingHandlerAdapter.getMessageConverters()));
        return map;
    }

    private Object toStrings(Collection<?> collection) {
        return collection != null
                ? collection.stream().map(Object::toString).collect(Collectors.toList())
                : "N/A";
    }
}
