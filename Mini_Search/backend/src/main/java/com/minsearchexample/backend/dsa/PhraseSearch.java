package com.minsearchexample.backend.dsa;

public class PhraseSearch {

    public boolean containsPhrase(String content, String phrase) {

        if (content == null || phrase == null) {
            return false;
        }

        return content
                .toLowerCase()
                .contains(phrase.toLowerCase());
    }
}
