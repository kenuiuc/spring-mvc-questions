package com.ken.demo;

import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.*;

import java.net.URI;
import java.time.Duration;
import java.util.*;

/**
 * @Author weizeyuan
 * @create 2021/2/3
 */
public final class RedisUtil {

    private static final String DEFAULT_PASSWORD = "default_password";
    private final static String CLUSTER = "cluster";
    private final static String SENTINEL = "sentinel";


    public static RedisConnectionFactory connectionFactory(RedisConfigProperties.Property property) {

        JedisClientConfiguration clientConfig = getJedisClientConfiguration(property);

        List<String> uris = new ArrayList<>(Arrays.asList(property.getNodes().split(",")));

        if (CLUSTER.equalsIgnoreCase(property.getType())) {

            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
            redisClusterConfiguration.setMaxRedirects(property.getMaxRedirects());

            List<RedisNode> nodeList = new ArrayList<>();

            for (String uriString : uris) {
                URI uri = URI.create(uriString);
                RedisNode redisNode = new RedisNode(uri.getHost(), uri.getPort());
                nodeList.add(redisNode);
            }

            redisClusterConfiguration.setClusterNodes(nodeList);

            if (!property.getPassword().equals(DEFAULT_PASSWORD)) {
                redisClusterConfiguration.setPassword(RedisPassword.of(property.getPassword()));
            }

            JedisConnectionFactory connectionFactory =
                    new JedisConnectionFactory(redisClusterConfiguration, clientConfig);
            connectionFactory.afterPropertiesSet();
            return connectionFactory;

        } else if (SENTINEL.equals(property.getType())) {

            RedisSentinelConfiguration redisSentinelConfiguration =
                    new RedisSentinelConfiguration();

            redisSentinelConfiguration.setMaster(property.getMaster());

            List<RedisNode> nodeList = new ArrayList<>();
            uris.forEach(uriString -> {
                URI uri = URI.create(uriString);
                RedisNode redisNode = new RedisNode(uri.getHost(), uri.getPort());
                nodeList.add(redisNode);
            });
            if (!property.getPassword().equals(DEFAULT_PASSWORD)) {
                redisSentinelConfiguration.setPassword(RedisPassword.of(property.getPassword()));
            }
            redisSentinelConfiguration.setSentinels(nodeList);
            JedisConnectionFactory connectionFactory =
                    new JedisConnectionFactory(redisSentinelConfiguration, clientConfig);
            connectionFactory.afterPropertiesSet();
            return connectionFactory;
        }

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        URI uri = URI.create(uris.get(0));
        configuration.setHostName(uri.getHost());
        configuration.setPort(uri.getPort());
        if (!property.getPassword().equals(DEFAULT_PASSWORD)) {
            configuration.setPassword(RedisPassword.of(property.getPassword()));
        }

        return new JedisConnectionFactory(configuration, clientConfig);
    }

    public static JedisClientConfiguration getJedisClientConfiguration(RedisConfigProperties.Property property) {
        JedisPoolConfig jedisPoolConfig = getJedisPoolConfig(property);

        Boolean useSslBoolean = property.getUseSsl();
        boolean useSsl = useSslBoolean == null ? false : useSslBoolean;

        JedisClientConfiguration.JedisClientConfigurationBuilder builder =
                JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(jedisPoolConfig)
                .and()
                .readTimeout(Duration.ofMillis(property.getReadTimeout()))
                .connectTimeout(Duration.ofMillis(property.getConnectTimeout()));

        if (useSsl) {
            return builder.useSsl().build();
        } else {
            return builder.build();
        }
    }

    private static JedisPoolConfig getJedisPoolConfig(RedisConfigProperties.Property property) {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        if (property.getMaxTotal() != null) {
            jedisPoolConfig.setMaxTotal(property.getMaxTotal());
        }
        if (property.getMaxIdle() != null) {
            jedisPoolConfig.setMaxIdle(property.getMaxIdle());
        }

        if (property.getMinIdle() != null) {
            jedisPoolConfig.setMinIdle(property.getMinIdle());
        }

        if (property.getMaxWait() != null) {
            jedisPoolConfig.setMaxWaitMillis(property.getMaxWait());
        }
        if (property.getTimeBetweenEvictionRuns() != null) {
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(property.getTimeBetweenEvictionRuns());
        }

        if (property.getMinEvictableIdleTimeMillis() != null) {
            jedisPoolConfig.setMinEvictableIdleTimeMillis(property.getMinEvictableIdleTimeMillis());
        }

        if (property.getTestOnBorrow() != null) {
            jedisPoolConfig.setTestOnBorrow(property.getTestOnBorrow());
        }

        if (property.getTestOnCreate() != null) {
            jedisPoolConfig.setTestOnCreate(property.getTestOnCreate());
        }

        if (property.getTestOnReturn() != null) {
            jedisPoolConfig.setTestOnReturn(property.getTestOnReturn());
        }
        return jedisPoolConfig;
    }
}
