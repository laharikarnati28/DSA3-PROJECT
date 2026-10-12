package com.minsearchexample.backend.dsa;
import java.util.HashSet;
import java.util.Set;
public class SimilaritySearch {
    public double calculateSimilarity(String first, String second) {
        Set<String> firstWords = getWords(first);
        Set<String> secondWords = getWords(second);
        if (firstWords.isEmpty() && secondWords.isEmpty()) {
            return 1.0;
        }
        Set<String> intersection = new HashSet<>(firstWords);
        intersection.retainAll(secondWords);
        Set<String> union = new HashSet<>(firstWords);
        union.addAll(secondWords);
        if (union.isEmpty()) {
            return 0.0;
        }
        return (double) intersection.size() / union.size();
    }
    private Set<String> getWords(String text) {
        Set<String> words = new HashSet<>();

        if (text == null) {
            return words;
        }
        String cleanedText = text
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", " ");

        String[] tokens = cleanedText.trim().split("\\s+");

        for (String token : tokens) {
            if (!token.isEmpty()) {
                words.add(token);
            }
        }

        return words;
    }
}