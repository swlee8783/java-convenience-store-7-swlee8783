package store.service;

import store.model.Product;
import store.repository.ProductRepository;
import store.util.ErrorMessages;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductList() {
        return productRepository.getAllProducts();
    }

    @Override
    public Product getProductByName(String name) {
        return productRepository.findProductByName(name)
                .orElseThrow(ErrorMessages.PRODUCT_NOT_FOUND::getException);
    }

    @Override
    public void updateProductStock(String name, int quantity) {
        Product product = getProductByName(name);
        int newQuantity = product.getQuantity() - quantity;
        if (newQuantity < 0) {
            throw ErrorMessages.INSUFFICIENT_STOCK.getException(product.getQuantity());
        }
        product.setQuantity(newQuantity);
        productRepository.updateProduct(product);
    }

    @Override
    public boolean isProductAvailable(String name, int quantity) {
        Product product = getProductByName(name);
        return product.getQuantity() >= quantity;
    }
}
