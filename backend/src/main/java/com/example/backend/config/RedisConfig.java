package com.example.backend.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Xinran
 * @version 1.0
 * @description redis配置，可以不写，但是我们写是为了更改默认的序列化器
 * @date 2023/3/11 16:43
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    //好像加上了这个注解就不会报红了
    @ConditionalOnSingleCandidate
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        //默认的Key序列化器为：JdkSerializationRedisSerializer，这里改为string序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置value序列化器
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
