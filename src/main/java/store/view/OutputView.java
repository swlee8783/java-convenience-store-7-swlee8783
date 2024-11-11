package store.view;

import store.model.Product;
import store.model.PurchaseResult;
import store.service.ProductService;

import java.util.List;
import java.util.Map;

public class OutputView {
    private final ProductService productService;

    public OutputView(ProductService productService) {
        this.productService = productService;
    }

    public void printProductList(List<Product> products) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (Product product : products) {
            printProductInfo(product);
        }
    }

    private void printProductInfo(Product product) {
        String quantityStatus = getQuantityStatus(product.getQuantity());
        String promotionDisplay = getPromotionDisplay(product.getPromotion());
        System.out.printf("- %s %d원 %s%s\n", product.getName(), product.getPrice(), quantityStatus, promotionDisplay);
    }

    private String getQuantityStatus(int quantity) {
        return quantity > 0 ? quantity + "개" : "재고 없음";
    }

    private String getPromotionDisplay(String promotion) {
        return promotion != null ? " " + promotion : "";
    }

    public void printPurchaseResult(PurchaseResult result) {
        System.out.println("\n===========W 편의점=============");
        System.out.println("상품명\t\t수량\t금액");
        printPurchasedItems(result.getPurchasedItems());
        printTotalAmount(result);
    }

    private void printPurchasedItems(Map<String, Integer> purchasedItems) {
        for (Map.Entry<String, Integer> entry : purchasedItems.entrySet()) {
            Product product = productService.getProductByName(entry.getKey());
            System.out.printf("%s\t\t%d\t%d\n", product.getName(), entry.getValue(), product.getPrice() * entry.getValue());
        }
    }

    private void printTotalAmount(PurchaseResult result) {
        System.out.println("==============================");
        System.out.printf("총구매액\t\t\t%d\n", result.getTotalPrice());
        System.out.printf("행사할인\t\t\t-%d\n", result.getPromotionDiscount());
        System.out.printf("프로모션 할인\t\t-%d원\n", result.getPromotionDiscount());
        System.out.printf("내실돈\t\t\t%d\n", result.getTotalPrice() - result.getPromotionDiscount());
    }

    public void printError(String message) {
        System.out.println(message);
    }

    public void printThankYouMessage() {
        System.out.println("저희 편의점을 이용해주셔서 감사합니다. 안녕히 가세요.");
    }
}