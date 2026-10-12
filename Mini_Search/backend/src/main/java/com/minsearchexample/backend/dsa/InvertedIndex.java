package com.minsearchexample.backend.dsa;

import java.util.*;

public class InvertedIndex {

    private final Map<String, Set<Integer>> index = new HashMap<>();

    public void addDocument(int documentId, String content) {

        String[] words = content
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ")
                .split("\\s+");

        for (String word : words) {

            if (!word.isEmpty()) {

                index
                    .computeIfAbsent(word, k -> new HashSet<>())
                    .add(documentId);
            }
        }
    }

    public Set<Integer> search(String word) {

        return index.getOrDefault(
                word.toLowerCase(),
                Collections.emptySet()
        );
    }

    public int getNumberOfIndexedWords() {

        return index.size();
    }

    public Map<String, Set<Integer>> getIndex() {

        return index;
    }
}