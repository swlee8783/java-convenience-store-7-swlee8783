package store.controller;

import store.view.InputView;
import store.view.OutputView;

import java.util.Map;

public class PurchaseController {
    private final ProductController productController;
    private final InputView inputView;
    private final OutputView outputView;

    public PurchaseController(ProductController productController, InputView inputView, OutputView outputView) {
        this.productController = productController;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void processPurchase() {
        try {
            String input = inputView.readPurchaseInput();
            Map<String, Integer> purchasedItems = productController.processPurchase(input);
            outputView.printPurchaseResult(purchasedItems);
        } catch (IllegalArgumentException e) {
            outputView.printError(e.getMessage());
            processPurchase();
        }
    }
}
