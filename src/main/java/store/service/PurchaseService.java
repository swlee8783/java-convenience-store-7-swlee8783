package store.service;

import store.model.PurchaseResult;

import java.util.Map;

public interface PurchaseService {
    PurchaseResult processPurchase(String input);
    void updateInventory(Map<String, Integer> items);
}
