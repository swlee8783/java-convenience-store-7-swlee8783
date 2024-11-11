package store.controller;

import store.model.PurchaseResult;
import store.service.PurchaseService;
import store.view.InputView;
import store.view.OutputView;

import java.time.LocalDate;

public class PurchaseController {
    private final PurchaseService purchaseService;
    private final InputView inputView;
    private final OutputView outputView;

    public PurchaseController(PurchaseService purchaseService, InputView inputView, OutputView outputView) {
        this.purchaseService = purchaseService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void processPurchase() {
        do {
            processSinglePurchase();
        } while (continueShopping());
    }

    private void processSinglePurchase() {
        try {
            String input = inputView.readPurchaseInput();
            boolean useMembership = inputView.readMembershipDiscount();
            LocalDate currentDate = LocalDate.now();
            PurchaseResult result = purchaseService.processPurchase(input, useMembership, currentDate);
            outputView.printPurchaseResult(result);
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
        }
    }

    private boolean continueShopping() {
        String input = String.valueOf(inputView.readContinueShopping());
        return purchaseService.shouldContinueShopping(input);
    }
}
