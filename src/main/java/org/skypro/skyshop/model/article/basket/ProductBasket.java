package org.skypro.skyshop.model.basket;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map; // ✅ Импорт Map
import java.util.UUID;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProductBasket {
    private final Map<UUID, Integer> products;

    public ProductBasket() {
        this.products = new HashMap<>();
    }

    public void addProduct(UUID productId) {
        products.merge(productId, 1, Integer::sum);
    }

    public Map<UUID, Integer> getAllProducts() {
        return Collections.unmodifiableMap(products);
    }

    public void clear() {
        products.clear();
    }
}