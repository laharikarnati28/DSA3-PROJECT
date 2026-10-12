package com.minsearchexample.backend.service;

import com.minsearchexample.backend.dsa.ExactSearch;
import com.minsearchexample.backend.dsa.FuzzySearch;
import com.minsearchexample.backend.dsa.InvertedIndex;
import com.minsearchexample.backend.dsa.PhraseSearch;
import com.minsearchexample.backend.dsa.Ranking;
import com.minsearchexample.backend.dsa.SimilaritySearch;
import com.minsearchexample.backend.model.Document;
import com.minsearchexample.backend.model.SearchResult;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    private final List<Document> documents = new ArrayList<>();

    private final InvertedIndex invertedIndex =
            new InvertedIndex();

    private final ExactSearch exactSearch =
            new ExactSearch(invertedIndex);

    private final FuzzySearch fuzzySearch =
            new FuzzySearch();

    private final PhraseSearch phraseSearch =
            new PhraseSearch();

    private final SimilaritySearch similaritySearch =
            new SimilaritySearch();

    private final Ranking ranking =
            new Ranking();

    public SearchService() {

        addDocument(
                new Document(
                        1,
                        "Data Structures",
                        "Data structures organize and store data efficiently."
                )
        );

        addDocument(
                new Document(
                        2,
                        "Algorithms",
                        "Algorithms are step by step procedures used to solve problems."
                )
        );

        addDocument(
                new Document(
                        3,
                        "Information Retrieval",
                        "Information retrieval is the process of finding useful information from documents."
                )
        );

        addDocument(
                new Document(
                        4,
                        "Search Engines",
                        "Search engines use indexing and ranking algorithms to find relevant information."
                )
        );

        addDocument(
                new Document(
                        5,
                        "Java Programming",
                        "Java is a popular programming language used to build many applications."
                )
        );
    }

    private void addDocument(Document document) {

        documents.add(document);

        invertedIndex.addDocument(
                document.getId(),
                document.getTitle() + " " + document.getContent()
        );
    }

    public List<Document> search(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchWord = keyword.toLowerCase().trim();

        Set<Integer> matchingIds =
                exactSearch.search(keyword);

        Map<Integer, Double> scores =
                new HashMap<>();

        for (Document document : documents) {

            String title =
                    document.getTitle().toLowerCase();

            String content =
                    document.getContent().toLowerCase();

            String fullText =
                    title + " " + content;

            double score = 0.0;

            // 1. Exact Search
            if (matchingIds.contains(document.getId())) {
                score += 3.0;
            }

            // 2. Title Match
            if (title.contains(searchWord)) {
                score += 2.0;
            }

            // 3. Content Match
            if (content.contains(searchWord)) {
                score += 1.0;
            }

            // 4. Phrase Search
            if (phraseSearch.containsPhrase(
                    fullText,
                    searchWord)) {

                score += 2.0;
            }

            // 5. Fuzzy Search
            String[] words =
                    searchWord.split("\\s+");

            for (String word : words) {

                String[] documentWords =
                        fullText.split("\\s+");

                for (String documentWord : documentWords) {

                    String cleanedWord =
                            documentWord.replaceAll(
                                    "[^a-zA-Z0-9]",
                                    ""
                            );

                    if (!cleanedWord.isEmpty()
                            && fuzzySearch.isSimilar(
                                    word,
                                    cleanedWord)) {

                        score += 0.5;
                        break;
                    }
                }
            }

            // 6. Similarity Search
            double similarity =
                    similaritySearch.calculateSimilarity(
                            searchWord,
                            fullText
                    );

            score += similarity * 5.0;

            if (score > 0) {

                scores.put(
                        document.getId(),
                        score
                );
            }
        }

        // 7. Ranking
        Map<Integer, Double> rankedScores =
                ranking.rank(scores);

        List<Document> results =
                new ArrayList<>();

        for (Integer documentId :
                rankedScores.keySet()) {

            for (Document document :
                    documents) {

                if (document.getId() == documentId) {

                    results.add(document);
                    break;
                }
            }
        }

        return results;
    }

    // Search with scores
    public List<SearchResult> searchWithScores(
            String keyword) {

        List<SearchResult> results =
                new ArrayList<>();

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            return results;
        }

        String searchWord =
                keyword.toLowerCase().trim();

        Set<Integer> matchingIds =
                exactSearch.search(keyword);

        for (Document document :
                documents) {

            String title =
                    document.getTitle().toLowerCase();

            String content =
                    document.getContent().toLowerCase();

            String fullText =
                    title + " " + content;

            double score = 0.0;

            // Exact Search
            if (matchingIds.contains(
                    document.getId())) {

                score += 3.0;
            }

            // Title Match
            if (title.contains(searchWord)) {
                score += 2.0;
            }

            // Content Match
            if (content.contains(searchWord)) {
                score += 1.0;
            }

            // Phrase Search
            if (phraseSearch.containsPhrase(
                    fullText,
                    searchWord)) {

                score += 2.0;
            }

            // Similarity Search
            double similarity =
                    similaritySearch.calculateSimilarity(
                            searchWord,
                            fullText
                    );

            score += similarity * 5.0;

            if (score > 0) {

                results.add(
                        new SearchResult(
                                document,
                                score
                        )
                );
            }
        }

        // Sort by highest score
        results.sort(
                (a, b) -> Double.compare(
                        b.getScore(),
                        a.getScore()
                )
        );

        return results;
    }
}