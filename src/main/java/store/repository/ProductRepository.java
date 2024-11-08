package store.repository;

import store.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAllProducts();
}

