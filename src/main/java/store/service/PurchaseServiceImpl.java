package store.service;

import store.model.Product;
import store.model.PurchaseResult;
import store.repository.ProductRepository;
import store.util.ErrorMessages;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PurchaseServiceImpl implements PurchaseService {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final PromotionService promotionService;

    public PurchaseServiceImpl(ProductRepository productRepository, ProductService productService, PromotionService promotionService) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.promotionService = promotionService;
    }

    @Override
    public PurchaseResult processPurchase(String input, boolean useMembership, LocalDate currentDate) {
        Map<String, Integer> purchasedItems = parsePurchaseInput(input);
        validatePurchase(purchasedItems);
        Map<String, Integer> giftItems = calculateGiftItems(purchasedItems, currentDate);
        updateInventory(purchasedItems);
        int totalPrice = calculateTotalPrice(purchasedItems);
        int promotionDiscount = calculatePromotionDiscount(purchasedItems, currentDate);
        int membershipDiscount = calculateMembershipDiscount(totalPrice - promotionDiscount, useMembership);
        return new PurchaseResult(purchasedItems, totalPrice, promotionDiscount, membershipDiscount, giftItems);
    }

    private int calculateMembershipDiscount(int price, boolean useMembership) {
        if (!useMembership) {
            return 0;
        }
        return Math.min((int)(price * 0.3), 8000);
    }

    @Override
    public boolean shouldContinueShopping(String input) {
        return input.equalsIgnoreCase("Y");
    }

    private Map<String, Integer> parsePurchaseInput(String input) {
        Map<String, Integer> items = new HashMap<>();
        String[] itemInputs = input.replaceAll("\\[|\\]", "").split(",");
        for (String itemInput : itemInputs) {
            String[] parts = itemInput.split("-");
            if (parts.length != 2) {
                throw ErrorMessages.INVALID_INPUT_FORMAT.getException();
            }
            String name = parts[0].trim();
            int quantity = Integer.parseInt(parts[1].trim());
            items.put(name, quantity);
        }
        return items;
    }

    private void validatePurchase(Map<String, Integer> items) {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String name = entry.getKey();
            int quantity = entry.getValue();
            if (!productService.isProductAvailable(name, quantity)) {
                Product product = productService.getProductByName(name);
                throw ErrorMessages.INSUFFICIENT_STOCK.getException(product.getQuantity());
            }
        }
    }

    private Map<String, Integer> calculateGiftItems(Map<String, Integer> purchasedItems, LocalDate currentDate) {
        Map<String, Integer> giftItems = new HashMap<>();
        for (Map.Entry<String, Integer> entry : purchasedItems.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            addGiftItemIfEligible(giftItems, productName, quantity, currentDate);
        }
        return giftItems;
    }

    private void addGiftItemIfEligible(Map<String, Integer> giftItems, String productName, int quantity, LocalDate currentDate) {
        Product product = productService.getProductByName(productName);
        if (isEligibleForPromotion(product, currentDate)) {
            int giftQuantity = calculateGiftQuantity(product, quantity, currentDate);
            if (giftQuantity > 0) {
                giftItems.put(productName, giftQuantity);
            }
        }
    }

    private boolean isEligibleForPromotion(Product product, LocalDate currentDate) {
        return product.getPromotion() != null &&
                promotionService.isPromotionValid(product.getPromotion(), currentDate);
    }

    private int calculateGiftQuantity(Product product, int quantity, LocalDate currentDate) {
        int discount = promotionService.calculateDiscount(product, quantity, currentDate);
        return discount / product.getPrice();
    }

    @Override
    public void updateInventory(Map<String, Integer> items) {
        for (Map.Entry<String, Integer> purchaseItem : items.entrySet()) {
            String productName = purchaseItem.getKey();
            int purchasedQuantity = purchaseItem.getValue();
            productService.updateProductStock(productName, purchasedQuantity);
        }
    }

    private int calculateTotalPrice(Map<String, Integer> items) {
        return items.entrySet().stream()
                .mapToInt(purchaseEntry -> {
                    String productName = purchaseEntry.getKey();
                    int quantity = purchaseEntry.getValue();
                    Product product = productService.getProductByName(productName);
                    return product.getPrice() * quantity;
                })
                .sum();
    }

    private int calculatePromotionDiscount(Map<String, Integer> items, LocalDate currentDate) {
        return items.entrySet().stream()
                .mapToInt(purchaseEntry -> {
                    String productName = purchaseEntry.getKey();
                    int quantity = purchaseEntry.getValue();
                    Product product = productService.getProductByName(productName);
                    return promotionService.calculateDiscount(product, quantity, currentDate);
                })
                .sum();
    }

    public int calculateFinalPrice(PurchaseResult result) {
        return result.getTotalPrice() - result.getPromotionDiscount() - result.getMembershipDiscount();
    }
}
