package store.service;

import store.model.Product;
import store.repository.ProductRepository;
import store.util.ErrorMessages;

import java.util.Comparator;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductList() {
        List<Product> products = productRepository.getAllProducts();
        products.sort(createProductComparator());
        return products;
    }

    private Comparator<Product> createProductComparator() {
        return Comparator.comparing(Product::getName)
                .thenComparing(this::comparePromotion)
                .thenComparing(Product::getQuantity, Comparator.reverseOrder())
                .thenComparing(Product::getPromotion, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    private int comparePromotion(Product product) {
        if (product.getPromotion() != null && !product.getPromotion().isEmpty()) {
            return 0;
        }
        return 1;
    }

    @Override
    public Product getProductByName(String name) {
        List<Product> products = productRepository.findProductByName(name)
                .map(List::of)
                .orElseThrow(ErrorMessages.PRODUCT_NOT_FOUND::getException);
        return selectBestProduct(products);
    }

    private Product selectBestProduct(List<Product> products) {
        return products.stream()
                .filter(p -> p.getQuantity() > 0)
                .min(Comparator.comparing(p -> p.getPromotion() != null))
                .orElse(products.getFirst());
    }

    @Override
    public void updateProductStock(String name, int quantity) {
        List<Product> products = productRepository.findProductByName(name)
                .map(List::of)
                .orElseThrow(ErrorMessages.PRODUCT_NOT_FOUND::getException);
        updateProductStockRecursive(products, quantity);
    }

    private void updateProductStockRecursive(List<Product> products, int remainingQuantity) {
        if (products.isEmpty() || remainingQuantity == 0) {
            return;
        }
        Product product = selectBestProduct(products);
        int updatedQuantity = Math.max(0, product.getQuantity() - remainingQuantity);
        int deductedQuantity = product.getQuantity() - updatedQuantity;
        product.setQuantity(updatedQuantity);
        productRepository.updateProduct(product);
        updateProductStockRecursive(products.subList(1, products.size()), remainingQuantity - deductedQuantity);
    }

    @Override
    public boolean isProductAvailable(String name, int quantity) {
        Product product = getProductByName(name);
        return product.getQuantity() >= quantity;
    }
}
