package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.util.ErrorMessages;

public class InputView {
    public String readPurchaseInput() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [제로콜라-2],[팝콘-1])");
        return Console.readLine();
    }

    public boolean readContinueShopping() {
        System.out.println("추가 구매를 원하시나요? (Y/N)");
        String input = Console.readLine().trim().toUpperCase();
        if (input.equals("Y") || input.equals("N")) {
            return input.equals("Y");
        }
        throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. Y 또는 N을 입력해주세요.");
    }
}