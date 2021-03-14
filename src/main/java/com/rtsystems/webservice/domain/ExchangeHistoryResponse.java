package com.rtsystems.webservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rtsystems.webservice.util.MathUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeHistoryResponse {
    private String base;
    private Map<String, Map<String, Double>> rates;

    @JsonProperty("start_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;

    @JsonProperty("end_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAt;

    public Double calculateVariance() {
        final List<Double> rates = getRates().values().parallelStream()
                .map(value -> value.get("USD"))
                .collect(Collectors.toList());

        return MathUtils.getVariance(rates);
    }
}
