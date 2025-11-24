package org.skypro.skyshop.service;

import org.skypro.skyshop.model.SearchResult;
import org.skypro.skyshop.model.search.Searchable; // ✅ Теперь пакет существует
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Collection<SearchResult> search(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }

        String lowerPattern = pattern.toLowerCase();

        return storageService.getAllSearchables().stream()
                .filter(searchable -> searchable.getSearchTerm()
                        .toLowerCase()
                        .contains(lowerPattern))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toList());
    }
}