package store.repository;

import store.model.Promotion;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository {
    List<Promotion> getAllPromotions();
    Optional<Promotion> findPromotionByName(String name);
    void updatePromotion(Promotion promotion);
}
