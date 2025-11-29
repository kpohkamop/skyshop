package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
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
        UUID bananaId = UUID.randomUUID();
        products.put(bananaId, new SimpleProduct(bananaId, "Банан", 20));

        UUID pearId = UUID.randomUUID();
        products.put(pearId, new SimpleProduct(pearId, "Груша", 30));

        UUID appleId = UUID.randomUUID();
        products.put(appleId, new DiscountedProduct(appleId, "Яблоко", 50, 20));

        UUID melonId = UUID.randomUUID();
        products.put(melonId, new FixPriceProduct(melonId, "Дыня"));

        UUID watermelonId = UUID.randomUUID();
        products.put(watermelonId, new DiscountedProduct(watermelonId, "Арбуз", 100, 30));

        UUID pumpkinId = UUID.randomUUID();
        products.put(pumpkinId, new SimpleProduct(pumpkinId, "Тыква", 40));

        UUID appleJuiceId = UUID.randomUUID();
        products.put(appleJuiceId, new SimpleProduct(appleJuiceId, "Яблочный сок", 80));

        UUID bananaMilkShakeId = UUID.randomUUID();
        products.put(bananaMilkShakeId, new SimpleProduct(bananaMilkShakeId, "Банановый молочный коктейль", 120));

        // Добавляем тестовые статьи
        UUID article1Id = UUID.randomUUID();
        articles.put(article1Id, new Article(
                article1Id,
                "Польза бананов для здоровья",
                "Бананы богаты калием и другими полезными микроэлементами. " +
                        "Регулярное употребление бананов помогает улучшить пищеварение " +
                        "и поддерживать здоровье сердечно-сосудистой системы."
        ));

        UUID article2Id = UUID.randomUUID();
        articles.put(article2Id, new Article(
                article2Id,
                "Как выбрать спелый арбуз",
                "При выборе арбуза обратите внимание на цвет корки и звук при постукивании. " +
                        "Спелый арбуз должен иметь яркую полосатую окраску и издавать глухой звук. " +
                        "Также обратите внимание на желтое пятно на боку - это признак спелости."
        ));

        UUID article3Id = UUID.randomUUID();
        articles.put(article3Id, new Article(
                article3Id,
                "Рецепты из яблок",
                "Яблоки можно использовать для приготовления пирогов, компотов и соков. " +
                        "Яблочный пирог - классический десерт для всей семьи. " +
                        "Также из яблок можно приготовить полезный компот без сахара."
        ));

        UUID article4Id = UUID.randomUUID();
        articles.put(article4Id, new Article(
                article4Id,
                "Экзотические фрукты и овощи",
                "В нашем магазине появились новые экзотические фрукты и овощи. " +
                        "Попробуйте манго, папайю, питахайю и другие тропические фрукты. " +
                        "Все они богаты витаминами и обладают уникальным вкусом."
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

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    public Optional<Article> getArticleById(UUID id) {
        return Optional.ofNullable(articles.get(id));
    }

    public boolean productExists(UUID id) {
        return products.containsKey(id);
    }

    public boolean articleExists(UUID id) {
        return articles.containsKey(id);
    }

    public int getProductsCount() {
        return products.size();
    }

    public int getArticlesCount() {
        return articles.size();
    }

    public int getTotalObjectsCount() {
        return products.size() + articles.size();
    }
}
