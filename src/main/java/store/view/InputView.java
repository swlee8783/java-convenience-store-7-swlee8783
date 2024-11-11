package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.util.ErrorMessages;

public class InputView {
    public String readPurchaseInput() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [제로콜라-2],[팝콘-1])");
        String input = Console.readLine();
        if (!input.matches("(\\[\\w+-\\d+\\],?)+")) {
            throw ErrorMessages.INVALID_INPUT_FORMAT.getException();
        }
        return input;
    }

    public boolean readContinueShopping() {
        System.out.println("추가 구매를 원하시나요? (Y/N)");
        String input = Console.readLine().trim().toUpperCase();
        if (input.equals("Y")) {
            return true;
        } else if (input.equals("N")) {
            return false;
        }
        throw ErrorMessages.INVALID_CONTINUE_SHOPPING_INPUT.getException();
    }
}