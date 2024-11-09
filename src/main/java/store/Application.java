package store;

import store.config.ConfigConstants;
import store.config.ConfigLoader;
import store.controller.ProductController;
import store.controller.PurchaseController;
import store.model.Product;
import store.repository.FileProductRepository;
import store.repository.ProductRepository;
import store.service.ProductService;
import store.service.ProductServiceImpl;
import store.service.PurchaseService;
import store.service.PurchaseServiceImpl;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader(ConfigConstants.CONFIG_FILE);

        // Repositories
        ProductRepository productRepository = new FileProductRepository(
                configLoader.getProperty(ConfigConstants.PRODUCTS_FILE_KEY)
        );

        // Services
        ProductService productService = new ProductServiceImpl(productRepository);
        PurchaseService purchaseService = new PurchaseServiceImpl(productRepository, productService);

        // Controllers
        ProductController productController = new ProductController(productService, purchaseService);

        // Views
        InputView inputView = new InputView();
        OutputView outputView = new OutputView(productService);

        PurchaseController purchaseController = new PurchaseController(productController, inputView, outputView);

        List<Product> productList = productController.displayProductList();
        outputView.printProductList(productList);

        purchaseController.processPurchase();
    }
}
