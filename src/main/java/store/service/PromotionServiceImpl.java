package store.service;

import store.model.Product;
import store.model.Promotion;
import store.repository.PromotionRepository;
import store.util.ErrorMessages;

import java.time.LocalDate;

public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public int calculateDiscount(Product product, int quantity, LocalDate currentDate) {
        if (product.getPromotion() == null) {return 0;}
        Promotion promotion = getPromotionByName(product.getPromotion());
        if (promotion == null || !promotion.isValidOn(currentDate)) {return 0;}
        int sets = quantity / (promotion.getBuyQuantity() + promotion.getGetFreeQuantity());
        return sets * promotion.getGetFreeQuantity() * product.getPrice();
    }

    @Override
    public boolean isPromotionValid(String promotionName, LocalDate currentDate) {
        return promotionRepository.findPromotionByName(promotionName)
                .map(promotion -> promotion.isValidOn(currentDate))
                .orElse(false);
    }

    @Override
    public void applyPromotion(Product product, int quantity) {
        Promotion promotion = promotionRepository.findPromotionByName(product.getPromotion())
                .orElseThrow(() -> ErrorMessages.PROMOTION_NOT_FOUND.getException(product.getPromotion()));

        int promotionAppliedQuantity = (quantity / (promotion.getBuyQuantity() + promotion.getGetFreeQuantity()))
                * promotion.getGetFreeQuantity();

        promotion.decreasePromotionStock(promotionAppliedQuantity);
        promotionRepository.updatePromotion(promotion);
    }

    @Override
    public Promotion getPromotionByName(String name) {
        return promotionRepository.findPromotionByName(name)
                .orElseThrow(() -> ErrorMessages.PROMOTION_NOT_FOUND.getException(name));
    }
}
