package com.rtsystems.webservice.config;

import com.rtsystems.webservice.domain.ExchangeResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    ReactiveRedisOperations<String, ExchangeResponse> redisRatesOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<ExchangeResponse> serializer = new Jackson2JsonRedisSerializer<>(ExchangeResponse.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, ExchangeResponse> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, ExchangeResponse> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    ReactiveRedisOperations<String, Double> redisVarianceOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Double> serializer = new Jackson2JsonRedisSerializer<>(Double.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Double> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Double> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}
