package store.service;

import store.model.Product;
import store.model.Promotion;

import java.time.LocalDate;

public interface PromotionService {
    int calculateDiscount(Product product, int quantity);

    boolean isPromotionValid(String promotionName, LocalDate currentDate);

    void applyPromotion(Product product, int quantity);

    Promotion getPromotionByName(String promotion);
}
