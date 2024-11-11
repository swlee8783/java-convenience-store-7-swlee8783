package store.service;

import store.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProductList();

    Product getProductByName(String name);

    void updateProductStock(String name, int quantity);

    boolean isProductAvailable(String name, int quantity);
}
