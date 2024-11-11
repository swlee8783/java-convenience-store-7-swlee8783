package store;

import store.config.ConfigConstants;
import store.config.ConfigLoader;
import store.controller.ProductController;
import store.controller.PurchaseController;
import store.model.Product;
import store.repository.FileProductRepository;
import store.repository.FilePromotionRepository;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.*;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        try {
            ConfigLoader configLoader = new ConfigLoader(ConfigConstants.CONFIG_FILE);

            // Repositories
            ProductRepository productRepository = new FileProductRepository(
                    configLoader.getProperty(ConfigConstants.PRODUCTS_FILE_KEY)
            );
            PromotionRepository promotionRepository = new FilePromotionRepository(
                    configLoader.getProperty(ConfigConstants.PROMOTIONS_FILE_KEY)
            );

            // Services
            ProductService productService = new ProductServiceImpl(productRepository);
            PromotionService promotionService = new PromotionServiceImpl(promotionRepository);
            PurchaseService purchaseService = new PurchaseServiceImpl(productRepository, productService, promotionService);

            // Controllers
            ProductController productController = new ProductController(productService, purchaseService);

            // Views
            InputView inputView = new InputView();
            OutputView outputView = new OutputView(productService);

            PurchaseController purchaseController = new PurchaseController(purchaseService, inputView, outputView);

            List<Product> productList = productController.displayProductList();
            outputView.printProductList(productList);

            purchaseController.processPurchase();

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}