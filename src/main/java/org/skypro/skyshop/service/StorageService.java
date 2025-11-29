package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.*;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
    private final Map<UUID, Product> products;
    private final Map<UUID, Article> articles;

    public StorageService() {
        this.products = new HashMap<>();
        this.articles = new HashMap<>();
        initializeTestData();
    }

    private void initializeTestData() {
        // Добавляем тестовые продукты
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(), "Банан", 20));
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(), "Груша", 30));
        products.put(UUID.randomUUID(), new DiscountedProduct(UUID.randomUUID(), "Яблоко", 50, 20));
        products.put(UUID.randomUUID(), new FixPriceProduct(UUID.randomUUID(), "Дыня"));
        products.put(UUID.randomUUID(), new DiscountedProduct(UUID.randomUUID(), "Арбуз", 100, 30));
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(), "Тыква", 40));
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(), "Яблочный сок", 80));
        products.put(UUID.randomUUID(), new SimpleProduct(UUID.randomUUID(), "Банановый молочный коктейль", 120));

        // Добавляем тестовые статьи
        articles.put(UUID.randomUUID(), new Article(
                UUID.randomUUID(),
                "Польза бананов для здоровья",
                "Бананы богаты калием и другими полезными микроэлементами..."
        ));

        articles.put(UUID.randomUUID(), new Article(
                UUID.randomUUID(),
                "Как выбрать спелый арбуз",
                "При выборе арбуза обратите внимание на цвет корки и звук при постукивании..."
        ));

        articles.put(UUID.randomUUID(), new Article(
                UUID.randomUUID(),
                "Рецепты из яблок",
                "Яблоки можно использовать для приготовления пирогов, компотов и соков..."
        ));

        articles.put(UUID.randomUUID(), new Article(
                UUID.randomUUID(),
                "Экзотические фрукты и овощи",
                "В нашем магазине появились новые экзотические фрукты и овощи..."
        ));
    }

    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(products.values());
    }

    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articles.values());
    }

    public Collection<Searchable> getAllSearchables() {
        List<Searchable> searchables = new ArrayList<>();
        searchables.addAll(products.values());
        searchables.addAll(articles.values());
        return Collections.unmodifiableCollection(searchables);
    }
}