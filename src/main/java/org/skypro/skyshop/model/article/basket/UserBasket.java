package org.skypro.skyshop.model.basket;

import java.util.List;

/**
 * Корзина пользователя для отображения
 */
public class UserBasket {
    private final List<BasketItem> items;
    private final int total;

    public UserBasket(List<BasketItem> items) {
        this.items = items;
        this.total = calculateTotal(items);
    }

    /**
     * Рассчитывает общую стоимость корзины с помощью Stream API
     */
    private int calculateTotal(List<BasketItem> items) {
        return items.stream()
                .mapToInt(BasketItem::getTotalPrice)
                .sum();
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }
}