package store.model;

import java.util.Map;

public class PurchaseResult {
    private final Map<String, Integer> purchasedItems;
    private final int totalPrice;
    private final int promotionDiscount;

    public PurchaseResult(Map<String, Integer> purchasedItems, int totalPrice, int promotionDiscount) {
        this.purchasedItems = purchasedItems;
        this.totalPrice = totalPrice;
        this.promotionDiscount = promotionDiscount;
    }

    public Map<String, Integer> getPurchasedItems() {
        return purchasedItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }
}
