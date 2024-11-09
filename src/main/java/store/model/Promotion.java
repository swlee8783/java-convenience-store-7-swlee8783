package store.model;

import java.time.LocalDate;

public class Promotion {
    private String name;
    private int buyQuantity;
    private int getFreeQuantity;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buyQuantity, int getFreeQuantity, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getFreeQuantity = getFreeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getGetFreeQuantity() {
        return getFreeQuantity;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isValidOn(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
