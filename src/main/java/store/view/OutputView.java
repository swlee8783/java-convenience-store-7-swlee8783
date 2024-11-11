package store.view;

import store.model.Product;
import store.model.PurchaseResult;
import store.service.ProductService;
import store.service.PurchaseService;
import store.util.OutputFormatter;

import java.util.List;
import java.util.Map;

public class OutputView {
    private final ProductService productService;
    private final PurchaseService purchaseService;

    public OutputView(ProductService productService, PurchaseService purchaseService) {
        this.productService = productService;
        this.purchaseService = purchaseService;
    }

    public void printProductList(List<Product> products) {
        System.out.println(OutputFormatter.formatWelcomeMessage());
        for (Product product : products) {
            System.out.println(OutputFormatter.formatProductInfo(product));
        }
    }

    private void printProductInfo(Product product) {
        String quantityStatus = OutputFormatter.formatQuantityStatus(product.getQuantity());
        String promotionDisplay = getPromotionDisplay(product.getPromotion());
        System.out.printf("- %s %,d원 %s%s\n", product.getName(), product.getPrice(), quantityStatus, promotionDisplay);
    }

    private String getPromotionDisplay(String promotion) {
        if (promotion == null || promotion.isEmpty()) {
            return "";
        }
        return " " + promotion;
    }

    public void printPurchaseResult(PurchaseResult result) {
        System.out.println("\n==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");
        printPurchasedItems(result.getPurchasedItems());
        printGiftItems(result.getGiftItems());
        printTotalAmount(result);
    }

    private void printGiftItems(Map<String, Integer> giftItems) {
        if (!giftItems.isEmpty()) {
            System.out.println("=============증\t정===============");
            for (Map.Entry<String, Integer> entry : giftItems.entrySet()) {
                System.out.printf("%s\t\t%d\n", entry.getKey(), entry.getValue());
            }
        }
    }

    private void printPurchasedItems(Map<String, Integer> purchasedItems) {
        for (Map.Entry<String, Integer> entry : purchasedItems.entrySet()) {
            Product product = productService.getProductByName(entry.getKey());
            System.out.printf("%s\t\t%d\t%d\n", product.getName(), entry.getValue(), product.getPrice() * entry.getValue());
        }
    }

    private void printTotalAmount(PurchaseResult result) {
        System.out.println("====================================");
        System.out.printf("총구매액\t\t%d\t%,d\n", result.getTotalQuantity(), result.getTotalPrice());
        System.out.printf("행사할인\t\t\t-%,d\n", result.getPromotionDiscount());
        System.out.printf("멤버십할인\t\t\t-%,d\n", result.getMembershipDiscount());
        System.out.printf("내실돈\t\t\t %,d\n", purchaseService.calculateFinalPrice(result));
    }

    public void printError(String message) {
        System.out.println(message);
    }

    public void printThankYouMessage() {
        System.out.println("저희 편의점을 이용해주셔서 감사합니다. 안녕히 가세요.");
    }
}