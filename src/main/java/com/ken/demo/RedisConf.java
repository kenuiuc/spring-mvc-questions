package com.ken.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableConfigurationProperties(RedisConfigProperties.class)
@Slf4j
public class RedisConf {

    @Bean(name = "redisTemplate")
    public StringRedisTemplate metadataRedisTemplate(RedisConfigProperties redisConfigProperties) {
        return getStringRedisTemplate(redisConfigProperties.getMetadata());
    }

    private StringRedisTemplate getStringRedisTemplate(RedisConfigProperties.Property pool) {
        log.info("resolved Jedis property is: {}", pool.toString());

        RedisConnectionFactory redisConnectionFactory = connectionFactory(pool);
        return new StringRedisTemplate(redisConnectionFactory);
    }

    private RedisConnectionFactory connectionFactory(RedisConfigProperties.Property pool) {
        return RedisUtil.connectionFactory(pool);
    }
}
