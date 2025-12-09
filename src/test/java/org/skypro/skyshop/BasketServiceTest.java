package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.SimpleProduct;
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
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    private UUID productId1;
    private UUID productId2;
    private SimpleProduct product1;
    private SimpleProduct product2;

    @BeforeEach
    void setUp() {
        productId1 = UUID.randomUUID();
        productId2 = UUID.randomUUID();
        product1 = new SimpleProduct(productId1, "Продукт 1", 100);
        product2 = new SimpleProduct(productId2, "Продукт 2", 200);
    }

    @Test
    void testAddProductThrowsExceptionWhenProductNotFound() {

        UUID nonExistentId = UUID.randomUUID();
        when(storageService.getProductById(nonExistentId)).thenReturn(Optional.empty());

        NoSuchProductException exception = assertThrows(
                NoSuchProductException.class,
                () -> basketService.addProductToBasket(nonExistentId)
        );

        assertEquals("Товар с ID " + nonExistentId + " не найден", exception.getMessage());
        verify(storageService, times(1)).getProductById(nonExistentId);
        verify(productBasket, never()).addProduct(any());
    }

    @Test
    void testAddProductCallsProductBasketWhenProductExists() {

        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));

        basketService.addProductToBasket(productId1);

        verify(storageService, times(1)).getProductById(productId1);
        verify(productBasket, times(1)).addProduct(productId1);
    }

    @Test
    void testGetUserBasketReturnsEmptyWhenProductBasketIsEmpty() {

        when(productBasket.getAllProducts()).thenReturn(Collections.emptyMap());

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertNotNull(userBasket.getItems());
        assertTrue(userBasket.getItems().isEmpty());
        assertEquals(0, userBasket.getTotal());
        verify(productBasket, times(1)).getAllProducts();
        verify(storageService, never()).getProductById(any());
    }

    @Test
    void testGetUserBasketReturnsCorrectBasketWhenProductsExist() {

        Map<UUID, Integer> basketProducts = new HashMap<>();
        basketProducts.put(productId1, 2); // 2 единицы продукта 1
        basketProducts.put(productId2, 1); // 1 единица продукта 2

        when(productBasket.getAllProducts()).thenReturn(basketProducts);
        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));
        when(storageService.getProductById(productId2)).thenReturn(Optional.of(product2));

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertEquals(2, userBasket.getItems().size());
        assertEquals(400, userBasket.getTotal()); // (100 * 2) + (200 * 1) = 400

        List<BasketItem> items = userBasket.getItems();
        boolean foundProduct1 = false;
        boolean foundProduct2 = false;

        for (BasketItem item : items) {
            if (item.getProduct().getId().equals(productId1)) {
                assertEquals(2, item.getQuantity());
                assertEquals(200, item.getTotalPrice()); // 100 * 2
                foundProduct1 = true;
            }
            if (item.getProduct().getId().equals(productId2)) {
                assertEquals(1, item.getQuantity());
                assertEquals(200, item.getTotalPrice()); // 200 * 1
                foundProduct2 = true;
            }
        }

        assertTrue(foundProduct1 && foundProduct2);

        verify(productBasket, times(1)).getAllProducts();
        verify(storageService, times(1)).getProductById(productId1);
        verify(storageService, times(1)).getProductById(productId2);
    }

    @Test
    void testGetUserBasketThrowsExceptionWhenProductNotFoundInStorage() {

        Map<UUID, Integer> basketProducts = new HashMap<>();
        basketProducts.put(productId1, 1);

        when(productBasket.getAllProducts()).thenReturn(basketProducts);
        when(storageService.getProductById(productId1)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> basketService.getUserBasket()
        );

        assertEquals("Товар не найден в хранилище", exception.getMessage());
        verify(productBasket, times(1)).getAllProducts();
        verify(storageService, times(1)).getProductById(productId1);
    }

    @Test
    void testAddSameProductMultipleTimes() {

        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));

        basketService.addProductToBasket(productId1);
        basketService.addProductToBasket(productId1);
        basketService.addProductToBasket(productId1);

        verify(storageService, times(3)).getProductById(productId1);
        verify(productBasket, times(3)).addProduct(productId1);
    }

    @Test
    void testClearBasketCallsProductBasketClear() {

        basketService.clearBasket();

        verify(productBasket, times(1)).clear();
    }

    @Test
    void testGetUserBasketWithZeroQuantity() {

        Map<UUID, Integer> basketProducts = new HashMap<>();
        basketProducts.put(productId1, 0); // 0 единиц

        when(productBasket.getAllProducts()).thenReturn(basketProducts);
        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertEquals(1, userBasket.getItems().size());
        assertEquals(0, userBasket.getTotal()); // 100 * 0 = 0

        BasketItem item = userBasket.getItems().get(0);
        assertEquals(0, item.getQuantity());
        assertEquals(0, item.getTotalPrice());
    }

    @Test
    void testTotalPriceCalculationWithMultipleProducts() {

        Map<UUID, Integer> basketProducts = new HashMap<>();
        basketProducts.put(productId1, 3); // 3 единицы по 100 = 300
        basketProducts.put(productId2, 2); // 2 единицы по 200 = 400

        when(productBasket.getAllProducts()).thenReturn(basketProducts);
        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));
        when(storageService.getProductById(productId2)).thenReturn(Optional.of(product2));

        UserBasket userBasket = basketService.getUserBasket();

        assertEquals(700, userBasket.getTotal()); // 300 + 400 = 700
    }
}