package org.skypro.skyshop.model.product;

import java.util.UUID;

public class SimpleProduct extends Product {
    private final int cost;

    public SimpleProduct(UUID id, String name, int cost) {
        super(id, name);
        this.cost = cost;
    }

    @Override
    public int getCost() {
        return cost;
    }
}