package org.skypro.skyshop.model.product;

import java.util.UUID;

public class FixPriceProduct extends Product {
    private static final int FIXED_PRICE = 10;

    public FixPriceProduct(UUID id, String name) {
        super(id, name);
    }

    @Override
    public int getCost() {
        return FIXED_PRICE;
    }
}