package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    /**
     * Добавляет товар в корзину по ID
     */
    public void addProductToBasket(UUID productId) {
        Product product = storageService.getProductById(productId)
                .orElseThrow(() -> new NoSuchProductException("Товар с ID " + productId + " не найден"));

        productBasket.addProduct(productId);
    }

    /**
     * Возвращает корзину пользователя
     */
    public UserBasket getUserBasket() {
        Map<UUID, Integer> basketProducts = productBasket.getAllProducts();

        List<BasketItem> basketItems = basketProducts.entrySet().stream()
                .map(entry -> {
                    Product product = storageService.getProductById(entry.getKey())
                            .orElseThrow(() -> new IllegalStateException("Товар не найден в хранилище"));
                    return new BasketItem(product, entry.getValue());
                })
                .collect(Collectors.toList());

        return new UserBasket(basketItems);
    }

    /**
     * Очищает корзину
     */
    public void clearBasket() {
        productBasket.clear();
    }
}