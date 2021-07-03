package com.ken.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private static final String DUMMY_KEY = "dummyKey";

    @Autowired
    @Qualifier("redisTemplate")
    private StringRedisTemplate redisTemplate;

    public void setCache(String value) {
        redisTemplate.boundValueOps(DUMMY_KEY).set(value);
    }

    public void delCache() {
        redisTemplate.delete(DUMMY_KEY);
    }

    public String getCache() {
        return redisTemplate.boundValueOps(DUMMY_KEY).get();
    }
}
