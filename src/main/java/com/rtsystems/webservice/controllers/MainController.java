package com.rtsystems.webservice.controllers;

import com.rtsystems.webservice.domain.ExchangeResponse;
import com.rtsystems.webservice.service.ExchangeRatesApiService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@RestController
public class MainController {
    private final ExchangeRatesApiService exchangeRatesApiService;

    public MainController(ExchangeRatesApiService exchangeRatesApiService) {
        this.exchangeRatesApiService = exchangeRatesApiService;
    }

    @GetMapping("/dispersion/{since}")
    public Mono<Double> getDispersionSince(@PathVariable("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since) {
        return exchangeRatesApiService
                .getVarianceForHistory(since)
                /*.subscribeOn(Schedulers.parallel())*/;
    }

    @GetMapping("/rate/{date}")
    public Mono<ExchangeResponse> getRateFrom(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return exchangeRatesApiService
                .getExchangeResponse(date)
                /*.subscribeOn(Schedulers.parallel())*/;
    }
}