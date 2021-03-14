package com.rtsystems.webservice.service;

import com.rtsystems.webservice.domain.ExchangeResponse;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisCacheService {
    private final ReactiveRedisOperations<String, ExchangeResponse> ratesOps;
    private final ReactiveRedisOperations<String, Double> varianceOps;

    public RedisCacheService(ReactiveRedisOperations<String, ExchangeResponse> ratesOps, ReactiveRedisOperations<String, Double> varianceOps) {
        this.ratesOps = ratesOps;
        this.varianceOps = varianceOps;
    }


    public Mono<ExchangeResponse> getExchangeResponseByKey(String key) {
        return this.ratesOps.opsForValue().get(key);
    }

    public Mono<ExchangeResponse> setExchangeResponse(String key, ExchangeResponse exchangeResponse) {
        return this.ratesOps.opsForValue().set(key, exchangeResponse).map(value -> exchangeResponse);
    }
}
