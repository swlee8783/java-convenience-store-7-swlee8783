package store.controller;

import store.model.Product;
import store.service.ProductService;
import store.service.PurchaseService;

import java.util.List;
import java.util.Map;

public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> displayProductList() {
        return productService.getProductList();
    }
}
