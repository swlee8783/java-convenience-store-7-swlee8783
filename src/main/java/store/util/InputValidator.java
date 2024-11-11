package store.util;

public class InputValidator {
    public static void validatePurchaseInput(String input) {
        if (!input.matches("(\\[\\w+-\\d+\\],?)+")) {
            throw ErrorMessages.INVALID_INPUT_FORMAT.getException();
        }
    }

    public static boolean validateContinueShopping(String input) {
        String trimmedInput = input.trim().toUpperCase();
        if (trimmedInput.equals("Y") || trimmedInput.equals("N")) {
            return trimmedInput.equals("Y");
        }
        throw ErrorMessages.INVALID_CONTINUE_SHOPPING_INPUT.getException();
    }
}
