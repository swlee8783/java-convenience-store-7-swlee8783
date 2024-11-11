package store.service;

import store.model.PurchaseResult;

import java.time.LocalDate;
import java.util.Map;

public interface PurchaseService {
    void updateInventory(Map<String, Integer> items);

    PurchaseResult processPurchase(String input, boolean useMembership, LocalDate currentDate);

    boolean shouldContinueShopping(String input);

    int calculateFinalPrice(PurchaseResult purchaseResult);
}
