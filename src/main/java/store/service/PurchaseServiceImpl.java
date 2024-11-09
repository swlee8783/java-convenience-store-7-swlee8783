package store.service;

import store.model.Product;
import store.repository.ProductRepository;

import java.util.HashMap;
import java.util.Map;

import java.util.List;
import java.util.Optional;

public class PurchaseServiceImpl implements PurchaseService {
    private final ProductRepository productRepository;

    public PurchaseServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Map<String, Integer> processPurchase(String input) {
        Map<String, Integer> purchaseItems = parsePurchaseInput(input);
        validatePurchase(purchaseItems);
        return purchaseItems;
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
        List<Product> products = productRepository.getAllProducts();
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String name = entry.getKey();
            int quantity = entry.getValue();
            Product product = findProduct(products, name)
                    .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."));
            if (quantity > product.getQuantity()) {
                throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
            }
        }
    }

    private Optional<Product> findProduct(List<Product> products, String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst();
    }
}
