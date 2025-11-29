package org.skypro.skyshop.model.product;

import java.util.UUID;

public class DiscountedProduct extends Product {
    private final int baseCost;
    private final int discountPercent;

    public DiscountedProduct(UUID id, String name, int baseCost, int discountPercent) {
        super(id, name);
        this.baseCost = baseCost;
        this.discountPercent = discountPercent;
    }

    @Override
    public int getCost() {
        return baseCost - (baseCost * discountPercent / 100);
    }

    public int getBaseCost() {
        return baseCost;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }
}