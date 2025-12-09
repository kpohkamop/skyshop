package org.skypro.skyshop.service;

import org.skypro.skyshop.model.SearchResult;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.Searchable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    private UUID productId;
    private UUID articleId;
    private SimpleProduct product;
    private Article article;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        articleId = UUID.randomUUID();
        product = new SimpleProduct(productId, "Тестовый продукт", 100);
        article = new Article(articleId, "Тестовая статья", "Содержание статьи");
    }

    @Test
    void testSearchWhenStorageServiceIsEmpty() {

        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        Collection<SearchResult> results = searchService.search("тест");

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    void testSearchWhenNoMatchingObjects() {
        List<Searchable> searchables = Arrays.asList(product, article);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        Collection<SearchResult> results = searchService.search("несуществующий");

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    void testSearchWhenMatchingProductExists() {

        List<Searchable> searchables = Arrays.asList(product, article);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        Collection<SearchResult> results = searchService.search("продукт");

        assertNotNull(results);
        assertEquals(1, results.size());

        SearchResult result = results.iterator().next();
        assertEquals("Тестовый продукт", result.getName());
        assertEquals("PRODUCT", result.getContentType());
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    void testSearchWhenMatchingArticleExists() {

        List<Searchable> searchables = Arrays.asList(product, article);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        Collection<SearchResult> results = searchService.search("статья");

        assertNotNull(results);
        assertEquals(1, results.size());

        SearchResult result = results.iterator().next();
        assertEquals("Тестовая статья", result.getName());
        assertEquals("ARTICLE", result.getContentType());
        verify(storageService, times(1)).getAllSearchables();
    }

    @Test
    void testSearchWithEmptyPattern() {

        Collection<SearchResult> results = searchService.search("");

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(storageService, never()).getAllSearchables();
    }

    @Test
    void testSearchWithNullPattern() {

        Collection<SearchResult> results = searchService.search(null);

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(storageService, never()).getAllSearchables();
    }

    @Test
    void testSearchCaseInsensitive() {

        List<Searchable> searchables = Collections.singletonList(product);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        Collection<SearchResult> results1 = searchService.search("ТЕСТОВЫЙ");
        Collection<SearchResult> results2 = searchService.search("тестовый");
        Collection<SearchResult> results3 = searchService.search("ТеСтОвЫй");

        assertEquals(1, results1.size());
        assertEquals(1, results2.size());
        assertEquals(1, results3.size());
        verify(storageService, times(3)).getAllSearchables();
    }

    @Test
    void testSearchWithPartialMatch() {

        List<Searchable> searchables = Collections.singletonList(product);
        when(storageService.getAllSearchables()).thenReturn(searchables);

        Collection<SearchResult> results = searchService.search("тест");

        assertEquals(1, results.size());
        verify(storageService, times(1)).getAllSearchables();
    }
}