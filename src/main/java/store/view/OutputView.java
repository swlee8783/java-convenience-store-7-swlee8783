package store.view;

import store.model.Product;
import store.service.ProductService;

import java.util.List;
import java.util.Map;

public class OutputView {
    private final ProductService productService;

    public OutputView(ProductService productService){
        this.productService = productService;
    }

    public static void printProductList(List<Product> products) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (Product product : products) {
            String quantityStatus = getQuantityStatus(product.getQuantity());
            String promotionDisplay = getPromotionDisplay(product.getPromotion());
            System.out.printf("- %s %d원 %s%s\n", product.getName(), product.getPrice(), quantityStatus, promotionDisplay);
        }
    }

    private static String getQuantityStatus(int quantity) {
        if (quantity > 0) {
            return quantity + "개";
        }
        return "재고 없음";
    }

    private static String getPromotionDisplay(String promotion) {
        if (promotion != null) {
            return " " + promotion;
        }
        return "";
    }

    public void printPurchaseResult(Map<String, Integer> items, int totalPrice, int promotionDiscount) {
        System.out.println("\n===========W 편의점=============");
        System.out.println("상품명\t\t수량\t금액");
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Product product = productService.getProductByName(entry.getKey());
            System.out.printf("%s\t\t%d\t%d\n", product.getName(), entry.getValue(), product.getPrice() * entry.getValue());
        }
        System.out.println("==============================");
        System.out.printf("총구매액\t\t\t%d\n", totalPrice);
        System.out.printf("행사할인\t\t\t-%d\n", promotionDiscount);
        System.out.printf("내실돈\t\t\t%d\n", totalPrice - promotionDiscount);
    }
}
