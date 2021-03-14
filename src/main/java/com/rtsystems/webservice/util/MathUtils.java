package com.rtsystems.webservice.util;

import java.util.List;

public class MathUtils {
    public static double getVariance(List<Double> list) {
        double mean = getMean(list);

        return Math.sqrt(list.parallelStream()
                .mapToDouble(value -> (value - mean) * (value - mean))
                .sum() / list.size());
//        double temp = 0;
//        for (double a : list)
//            temp += (a - mean) * (a - mean);
//        return temp / (list.size() - 1);
    }

    public static double getMean(List<Double> list) {
        return list.parallelStream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0D);
    }
}
