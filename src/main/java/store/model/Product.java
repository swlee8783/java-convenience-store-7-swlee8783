package store.model;

import store.util.ErrorMessages;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final String promotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = setValidPromotion(promotion);
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
        if (promotion == null) {
            return "";
        }
        return promotion;
    }

    // Setter
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isOutOfStock() {
        return quantity == 0;
    }

    public void decreaseQuantity(int amount) {
        if (amount > this.quantity) {
            throw ErrorMessages.INSUFFICIENT_STOCK.getException(this.quantity);
        }
        this.quantity -= amount;
    }

    public boolean hasPromotion() {
        return promotion != null && !promotion.isEmpty();
    }

    private String setValidPromotion(String promotion) {
        if (promotion == null) {
            return "";
        }
        return promotion;
    }
}