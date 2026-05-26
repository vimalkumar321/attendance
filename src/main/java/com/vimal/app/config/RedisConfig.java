package com.vimal.app.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.vimal.app.exception.RedisCacheErrorHandler;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class RedisConfig {
	@Bean
	@ConditionalOnProperty(
	        value = "spring.data.redis.enabled",
	        havingValue = "true",
	        matchIfMissing = true
	    )
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String,Object> template = new RedisTemplate<>();

	    template.setConnectionFactory(connectionFactory);

	    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

	    template.setKeySerializer(new StringRedisSerializer());

	    template.setHashKeySerializer(new StringRedisSerializer());

	    template.setValueSerializer(serializer);

	    template.setHashValueSerializer(serializer);

	    return template;
    }
	
	@Bean
    CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration cacheConfiguration =

                RedisCacheConfiguration.defaultCacheConfig()

                        .entryTtl(Duration.ofHours(1))

                        .disableCachingNullValues()

                        .serializeKeysWith(

                                RedisSerializationContext
                                		.SerializationPair
                                        .fromSerializer(
                                                new StringRedisSerializer()
                                        )
                        )

                        .serializeValuesWith(
                                RedisSerializationContext
                                        .SerializationPair
                                        .fromSerializer(
                                                new GenericJackson2JsonRedisSerializer()
                                        )
                        );



        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .transactionAware()
                .build();

    }

	@Bean
    CacheErrorHandler cacheErrorHandler() {
        return new RedisCacheErrorHandler();
    }
}
