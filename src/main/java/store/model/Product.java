package store.model;

import store.util.ErrorMessages;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    // Getters
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getPromotion() {
        return promotion;
    }

    public void decreaseQuantity(int amount) {
        if (amount > this.quantity) {
            throw ErrorMessages.INSUFFICIENT_STOCK.getException(this.quantity);
        }
        this.quantity -= amount;
    }
}