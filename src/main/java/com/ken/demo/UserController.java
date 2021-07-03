package com.ken.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private RedisService redisService;

    @GetMapping(value = "/redis/dummyKey")
    public String getValue() {
        return redisService.getCache();
    }

    @PutMapping(value = "/redis/dummyKey", consumes = {"application/x-www-form-urlencoded;charset=UTF-8"})
    public String putValue(@RequestParam Map<String, String> data) {
        String value = data.get("dummyKey");
        redisService.setCache(value);
        return redisService.getCache();
    }

    @DeleteMapping(value = "/redis/dummyKey")
    public String deleteValue() {
        redisService.delCache();
        return redisService.getCache();
    }

}
