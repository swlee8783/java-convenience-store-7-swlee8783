package store.controller;

import store.model.PurchaseResult;
import store.service.PurchaseService;
import store.view.InputView;
import store.view.OutputView;

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
        outputView.printThankYouMessage();
    }

    private void processSinglePurchase() {
        try {
            String input = inputView.readPurchaseInput();
            PurchaseResult result = purchaseService.processPurchase(input);
            outputView.printPurchaseResult(result);
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
        }
    }

    private boolean continueShopping() {
        try {
            return inputView.readContinueShopping();
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
            return continueShopping();
        }
    }
}
