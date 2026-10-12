package com.minsearchexample.backend.controller;

import com.minsearchexample.backend.model.Document;
import com.minsearchexample.backend.model.SearchResult;
import com.minsearchexample.backend.service.SearchService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "http://localhost:5173")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<Document> search(
            @RequestParam("q") String keyword) {

        return searchService.search(keyword);
    }

    @GetMapping("/scored")
    public List<SearchResult> scoredSearch(
            @RequestParam("q") String keyword) {

        return searchService.searchWithScores(keyword);
    }
}