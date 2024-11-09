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

    public Product getProductByName(String name) {
        return productRepository.findProductByName(name)
                .orElseThrow(ErrorMessages.PRODUCT_NOT_FOUND::getException);
    }
}
