package store.model;

import store.util.ErrorMessages;
import java.time.LocalDate;

public class Promotion {
    private String name;
    private int buyQuantity;
    private int getFreeQuantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private int promotionStock;  // 추가된 필드

    public Promotion(String name, int buyQuantity, int getFreeQuantity, LocalDate startDate, LocalDate endDate, int promotionStock) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getFreeQuantity = getFreeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.promotionStock = promotionStock;
    }

    public String getName() {
        return name;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGetFreeQuantity() {
        return getFreeQuantity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public void decreasePromotionStock(int amount) {
        if (amount > this.promotionStock) {
            throw ErrorMessages.INSUFFICIENT_PROMOTION_STOCK.getException(this.promotionStock);
        }
        this.promotionStock -= amount;
    }

    public boolean isValidOn(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
