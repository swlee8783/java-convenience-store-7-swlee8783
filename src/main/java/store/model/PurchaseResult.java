package store.model;

import java.util.Map;

public class PurchaseResult {
    private final Map<String, Integer> purchasedItems;
    private final int totalPrice;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final Map<String, Integer> giftItems;

    public PurchaseResult(Map<String, Integer> purchasedItems, int totalPrice, int promotionDiscount, int membershipDiscount, Map<String, Integer> giftItems) {
        this.purchasedItems = purchasedItems;
        this.totalPrice = totalPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.giftItems = giftItems;
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

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public Map<String, Integer> getGiftItems() {
        return giftItems;
    }

    public int getTotalQuantity() {
        return purchasedItems.values().stream().mapToInt(Integer::intValue).sum();
    }
}