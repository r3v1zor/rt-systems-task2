package com.rtsystems.webservice.service;

import com.rtsystems.webservice.domain.ExchangeHistoryResponse;
import com.rtsystems.webservice.domain.ExchangeResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class ExchangeRatesApiService {
    private final WebClient webClient;
    private final RedisCacheService redisCacheService;

    public ExchangeRatesApiService(WebClient webClient, RedisCacheService redisCacheService) {
        this.webClient = webClient;
        this.redisCacheService = redisCacheService;
    }

    public Mono<ExchangeResponse> getExchangeResponse(LocalDate date) {
        String isoDate = date.format(DateTimeFormatter.ISO_DATE);
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{date}")
                        .queryParam("symbols", "USD")
                        .build(isoDate)
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ExchangeResponse.class);
//                .bodyToMono(String.class)
//                .map(this::parseExchangeResponse);
    }

    public Mono<ExchangeHistoryResponse> getHistory(LocalDate since) {
        String sinceIso = since.format(DateTimeFormatter.ISO_DATE);
        String toIso = LocalDate.from(Instant.now().atZone(ZoneId.of("UTC"))).format(DateTimeFormatter.ISO_DATE);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/history")
                        .queryParam("start_at", sinceIso)
                        .queryParam("end_at", toIso)
                        .queryParam("symbols", "USD")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ExchangeHistoryResponse.class);
    }

    public Mono<Double> getVarianceForHistory(LocalDate date) {
        return this.getHistory(date)
                .flatMap(response -> Mono.just(response.calculateVariance()).publishOn(Schedulers.boundedElastic()));
    }

    public Mono<ExchangeResponse> test(LocalDate date) {
        String key = date.format(DateTimeFormatter.ISO_DATE);

        return this.redisCacheService.getExchangeResponseByKey(key)
                .switchIfEmpty(Mono.defer(
                        () -> this.getExchangeResponse(date)
                                .flatMap(response -> this.redisCacheService.setExchangeResponse(key, response))
                ));
    }

    private ExchangeResponse parseExchangeResponse(String response) {
        int dateIndex = response.indexOf("date");
        int currencyIndex = response.indexOf("USD");
        int baseIndex = response.indexOf("base");

        double rate = Double.parseDouble(response.substring(currencyIndex + 5, currencyIndex + 10));
        String base = response.substring(baseIndex + 7, baseIndex + 10);
        String date = response.substring(dateIndex + 7, dateIndex + 17);

        return new ExchangeResponse(Map.of("USD", rate), base, date);
    }
}

