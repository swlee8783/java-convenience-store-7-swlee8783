package store;

import store.model.Product;
import store.repository.FileProductRepository;
import store.repository.ProductRepository;
import store.service.ProductService;
import store.service.ProductServiceImpl;
import store.view.OutputView;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        ProductRepository repository = new FileProductRepository();
        ProductService service = new ProductServiceImpl(repository);
        ProductController controller = new ProductController(service);

        List<Product> productList = controller.displayProductList();
        OutputView.printProductList(productList);
    }
}
