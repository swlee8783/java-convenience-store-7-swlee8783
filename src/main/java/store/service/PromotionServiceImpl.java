package store.service;

import store.model.Product;
import store.model.Promotion;
import store.repository.PromotionRepository;

import java.time.LocalDate;

public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public int calculateDiscount(Product product, int quantity) {
        Promotion promotion = promotionRepository.findPromotionByName(product.getPromotion())
                .orElseThrow(() -> new IllegalArgumentException("해당 프로모션을 찾을 수 없습니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("해당 프로모션을 찾을 수 없습니다."));

        int promotionAppliedQuantity = (quantity / (promotion.getBuyQuantity() + promotion.getGetFreeQuantity()))
                * promotion.getGetFreeQuantity();

        promotion.decreasePromotionStock(promotionAppliedQuantity);
        promotionRepository.updatePromotion(promotion);
    }
}
