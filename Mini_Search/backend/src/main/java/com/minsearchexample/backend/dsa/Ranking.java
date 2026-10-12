package com.minsearchexample.backend.dsa;

import java.util.*;

public class Ranking {

    public Map<Integer, Double> rank(
            Map<Integer, Double> scores) {

        List<Map.Entry<Integer, Double>> entries =
                new ArrayList<>(scores.entrySet());

        entries.sort(
                (a, b) -> Double.compare(
                        b.getValue(),
                        a.getValue()
                )
        );

        Map<Integer, Double> ranked =
                new LinkedHashMap<>();

        for (Map.Entry<Integer, Double> entry : entries) {
            ranked.put(
                    entry.getKey(),
                    entry.getValue()
            );
        }

        return ranked;
    }
}