package store.util;

public enum ErrorMessages {
    PRODUCT_NOT_FOUND("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
    INSUFFICIENT_STOCK("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_INPUT_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_QUANTITY("[ERROR] 유효하지 않은 수량입니다. 1 이상의 정수를 입력해 주세요.");

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
}
}
