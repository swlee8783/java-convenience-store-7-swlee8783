package store.service;

import store.model.Product;
import store.model.PurchaseResult;
import store.repository.ProductRepository;
import store.util.ErrorMessages;

import java.util.HashMap;
import java.util.Map;

import java.util.List;
import java.util.Optional;

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
        validatePurchase(purchasedItems);
        updateInventory(purchasedItems);
        int totalPrice = calculateTotalPrice(purchasedItems);
        int promotionDiscount = calculatePromotionDiscount(purchasedItems);
        return new PurchaseResult(purchasedItems, totalPrice, promotionDiscount);
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
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
            String name = parts[0].trim();
            int quantity = Integer.parseInt(parts[1].trim());
            items.put(name, quantity);
        }
        return items;
    }

    private void validatePurchase(Map<String, Integer> items) {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Product product = productService.getProductByName(entry.getKey());
            String name = entry.getKey();
            int quantity = entry.getValue();
            if (quantity <= 0) {
                throw ErrorMessages.INVALID_QUANTITY.getException();
            }
            if (quantity > product.getQuantity()) {
                throw ErrorMessages.INSUFFICIENT_STOCK.getException();
            }
        }
    }

    private Optional<Product> findProduct(List<Product> products, String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst();
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
        return items.entrySet().stream()
                .mapToInt(entry -> {
                    Product product = productService.getProductByName(entry.getKey());
                    return promotionService.calculateDiscount(product, entry.getValue());
                })
                .sum();
    }
}
