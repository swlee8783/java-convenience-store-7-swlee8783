package store.util;

public enum ErrorMessages {
    PRODUCT_NOT_FOUND("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
    INSUFFICIENT_STOCK("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_INPUT_FORMAT("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    INVALID_QUANTITY("[ERROR] 유효하지 않은 수량입니다. 1 이상의 정수를 입력해 주세요."),
    CONFIG_FILE_NOT_FOUND("[ERROR] 설정 파일을 찾을 수 없습니다: %s"),
    CONFIG_FILE_LOAD_ERROR("[ERROR] 설정 파일 로드 중 오류가 발생했습니다: %s"),
    CONFIG_PROPERTY_NOT_FOUND("[ERROR] 설정 속성을 찾을 수 없습니다: %s"),
    INSUFFICIENT_PROMOTION_STOCK("[ERROR] 프로모션 재고가 부족합니다. 현재 프로모션 재고: %d"),
    PRODUCT_FILE_READ_ERROR("[ERROR] 상품 파일을 읽는 중 오류가 발생했습니다: %s"),
    PROMOTION_FILE_READ_ERROR("[ERROR] 프로모션 파일을 읽는 중 오류가 발생했습니다: %s"),
    PROMOTION_FILE_WRITE_ERROR("[ERROR] 프로모션 파일을 저장하는 중 오류가 발생했습니다: %s"),
    PROMOTION_NOT_FOUND("[ERROR] 해당 프로모션을 찾을 수 없습니다: %s"),
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
