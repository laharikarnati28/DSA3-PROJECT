package com.minsearchexample.backend.dsa;

import java.util.Set;

public class ExactSearch {

    private final InvertedIndex invertedIndex;

    public ExactSearch(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    public Set<Integer> search(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return Set.of();
        }

        return invertedIndex.search(keyword.trim());
    }
}
