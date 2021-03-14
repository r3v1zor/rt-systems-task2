package com.rtsystems.webservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResponse {
    private Map<String, Double> rates;
    private String base;
    private String date;
}
