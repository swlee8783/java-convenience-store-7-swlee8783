package store.util;

import store.model.Product;
import store.model.PurchaseResult;

public class OutputFormatter {
    public static String formatProductInfo(Product product) {
        String quantityStatus = formatQuantityStatus(product.getQuantity());
        String promotionDisplay = formatPromotionDisplay(product.getPromotion());
        return String.format("- %s %,d원 %s%s", product.getName(), product.getPrice(), quantityStatus, promotionDisplay);
    }

    public static String formatQuantityStatus(int quantity) {
        if (quantity > 0) {
            return quantity + "개";
        }
        return "재고 없음";
    }

    private static String formatPromotionDisplay(String promotion) {
        if (promotion != null && !promotion.isEmpty()) {
            return " " + promotion;
        }
        return "";
    }

    public static String formatPurchaseItem(String name, int quantity, int price) {
        return String.format("%s\t\t%d\t%d", name, quantity, price);
    }

    public static String formatTotalAmount(PurchaseResult result) {
        return String.format("총구매액\t\t\t%,d\n행사할인\t\t\t-%,d\n프로모션 할인\t\t-%,d원\n내실돈\t\t\t%,d",
                result.getTotalPrice(),
                result.getPromotionDiscount(),
                result.getPromotionDiscount(),
                result.getTotalPrice() - result.getPromotionDiscount());
    }

    public static String formatPurchaseHeader() {
        return "상품명\t\t수량\t금액";
    }

    public static String formatStoreHeader() {
        return "===========W 편의점=============";
    }

    public static String formatStoreDivider() {
        return "==============================";
    }

    public static String formatThankYouMessage() {
        return "저희 편의점을 이용해주셔서 감사합니다. 안녕히 가세요.";
    }

    public static String formatWelcomeMessage() {
        return "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    }
}