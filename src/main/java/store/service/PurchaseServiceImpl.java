package store.service;

import store.model.Product;
import store.model.Promotion;
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
    public PurchaseResult processPurchase(String input) {
        Map<String, Integer> purchasedItems = parsePurchaseInput(input);
        Map<String, Integer> giftItems = calculateGiftItems(purchasedItems);
        validatePurchase(purchasedItems);
        updateInventory(purchasedItems);
        int totalPrice = calculateTotalPrice(purchasedItems);
        int promotionDiscount = calculatePromotionDiscount(purchasedItems);
        return new PurchaseResult(purchasedItems, totalPrice, promotionDiscount, giftItems);
    }

    private Map<String, Integer> calculateGiftItems(Map<String, Integer> purchasedItems) {
        Map<String, Integer> giftItems = new HashMap<>();
        LocalDate currentDate = LocalDate.now();
        for (Map.Entry<String, Integer> entry : purchasedItems.entrySet()) {
            Product product = productService.getProductByName(entry.getKey());
            if (product.getPromotion() != null) {
                Promotion promotion = promotionService.getPromotionByName(product.getPromotion());
                if (promotion.isValidOn(currentDate)) {
                    int giftQuantity = (entry.getValue() / (promotion.getBuyQuantity() + promotion.getGetFreeQuantity())) * promotion.getGetFreeQuantity();
                    if (giftQuantity > 0) {
                        giftItems.put(product.getName(), giftQuantity);
                    }
                }
            }
        }
        return giftItems;
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
            Product product = productService.getProductByName(name);

            if (product == null) {
                throw ErrorMessages.PRODUCT_NOT_FOUND.getException(name);
            }
            if (quantity <= 0) {
                throw ErrorMessages.INVALID_QUANTITY.getException();
            }
            if (quantity > product.getQuantity()) {
                throw ErrorMessages.INSUFFICIENT_STOCK.getException(product.getQuantity());
            }
        }
    }

    @Override
    public void updateInventory(Map<String, Integer> items) {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Product product = productService.getProductByName(entry.getKey());
            product.decreaseQuantity(entry.getValue());
            productRepository.updateProduct(product);
        }
    }

    private int calculateTotalPrice(Map<String, Integer> items) {
        return items.entrySet().stream()
                .mapToInt(entry -> {
                    Product product = productService.getProductByName(entry.getKey());
                    return product.getPrice() * entry.getValue();
                })
                .sum();
    }

    private int calculatePromotionDiscount(Map<String, Integer> items) {
        LocalDate currentDate = LocalDate.now();
        return items.entrySet().stream()
                .mapToInt(entry -> {
                    Product product = productService.getProductByName(entry.getKey());
                    if (product.getPromotion() != null) {
                        Promotion promotion = promotionService.getPromotionByName(product.getPromotion());
                        if (promotion == null) {
                            throw ErrorMessages.PROMOTION_NOT_FOUND.getException(product.getPromotion());
                        }
                        if (!promotion.isValidOn(currentDate)) {
                            throw ErrorMessages.PROMOTION_EXPIRED.getException(product.getPromotion());
                        }
                        return promotionService.calculateDiscount(product, entry.getValue());
                    }
                    return 0;
                })
                .sum();
    }
}
