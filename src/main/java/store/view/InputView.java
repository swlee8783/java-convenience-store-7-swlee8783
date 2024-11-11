package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.util.InputValidator;
import store.util.ErrorMessages;

public class InputView {
    public String readPurchaseInput() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String input = Console.readLine();
        InputValidator.validatePurchaseInput(input);
        return input;
    }

    public boolean readContinueShopping() {
        System.out.println("추가 구매를 원하시나요? (Y/N)");
        String input = Console.readLine();
        return InputValidator.validateContinueShopping(input);
    }

    public boolean readMembershipDiscount() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String input = Console.readLine().trim().toUpperCase();
        if (input.equals("Y")) {
            return true;
        } else if (input.equals("N")) {
            return false;
        }
        throw ErrorMessages.INVALID_MEMBERSHIP_INPUT.getException();
    }

    public boolean readPromotionAddition(String productName) {
        System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n", productName);
        String input = Console.readLine().trim().toUpperCase();
        if (input.equals("Y")) {
            return true;
        } else if (input.equals("N")) {
            return false;
        }
        throw ErrorMessages.INVALID_PROMOTION_ADDITION_INPUT.getException();
    }
}