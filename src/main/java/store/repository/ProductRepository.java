package store.repository;

import store.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> getAllProducts();
    Optional<Product> findProductByName(String name);
    void updateProduct(Product product);
}

