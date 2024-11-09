package store.controller;

import store.model.Product;
import store.service.ProductService;
import store.service.PurchaseService;

import java.util.List;
import java.util.Map;

public class ProductController {
    private final ProductService productService;
    private final PurchaseService purchaseService;

    public ProductController(ProductService productService, PurchaseService purchaseService) {
        this.productService = productService;
        this.purchaseService = purchaseService;
    }

    public List<Product> displayProductList() {
        return productService.getProductList();
    }

    public Map<String, Integer> processPurchase(String input) {
        return purchaseService.processPurchase(input);
    }
}
