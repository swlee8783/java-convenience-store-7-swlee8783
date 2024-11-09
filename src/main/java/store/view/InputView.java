package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.util.ErrorMessages;

public class InputView {
    public String readPurchaseInput() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [제로콜라-2],[팝콘-1])");
        return Console.readLine();
    }

    public boolean readContinueShopping() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        String input = Console.readLine();
        if (input.equalsIgnoreCase("Y")) {
            return true;
        } else if (input.equalsIgnoreCase("N")) {
            return false;
        } else {
            throw ErrorMessages.INVALID_INPUT_FORMAT.getException();
        }
    }
}