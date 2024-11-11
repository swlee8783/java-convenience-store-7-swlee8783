package store.util;

public enum ErrorMessages {
    INVALID_INPUT_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_QUANTITY("[ERROR] 유효하지 않은 수량입니다. 1 이상의 정수를 입력해 주세요."),
    INSUFFICIENT_STOCK("[ERROR] 재고가 부족합니다. 현재 재고: %d"),
    PRODUCT_NOT_FOUND("[ERROR] 존재하지 않는 상품입니다: %s"),
    PROMOTION_NOT_FOUND("[ERROR] 존재하지 않는 프로모션입니다: %s"),
    PROMOTION_EXPIRED("[ERROR] 프로모션이 만료되었습니다: %s"),
    INVALID_CONTINUE_SHOPPING_INPUT("[ERROR] 잘못된 입력입니다. Y 또는 N을 입력해주세요.");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public IllegalArgumentException getException() {
        return new IllegalArgumentException(getMessage());
    }

    public IllegalArgumentException getException(Object... args) {
        return new IllegalArgumentException(String.format(getMessage(), args));
    }
}