package store;

import store.controller.ProductController;
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
import java.util.Map;

public class Application {
    public static void main(String[] args) {
        ProductRepository repository = new FileProductRepository();
        ProductService productService = new ProductServiceImpl(repository);
        PurchaseService purchaseService = new PurchaseServiceImpl(repository);
        ProductController controller = new ProductController(productService, purchaseService);

        List<Product> productList = controller.displayProductList();
        OutputView.printProductList(productList);

        try {
            String input = InputView.readPurchaseInput();
            Map<String, Integer> purchasedItems = controller.processPurchase(input);
            System.out.println("구매가 완료되었습니다.");
            // 여기에 구매 내역 출력 로직을 추가할 수 있습니다.
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
