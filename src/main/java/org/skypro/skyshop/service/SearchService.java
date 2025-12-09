package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID; // ✅ Добавляем импорт UUID
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

    /**
     * Пример метода, который может выбрасывать NoSuchProductException
     */
    public SearchResult getProductInfo(UUID productId) {
        // Этот метод демонстрирует использование исключения в других сервисах
        // В реальном приложении здесь могла бы быть другая логика
        throw new NoSuchProductException("Товар с ID " + productId + " не найден в каталоге");
    }
}