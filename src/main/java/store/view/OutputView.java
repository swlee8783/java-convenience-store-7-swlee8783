package store.view;

import store.model.Product;

import java.util.List;

public class OutputView {
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
}
