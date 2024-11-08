package store.repository;

import store.model.Product;
import store.model.Promotion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilePromotionRepository implements PromotionRepository {
    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";

    @Override
    public List<Promotion> getAllPromotions() {
        List<Promotion> promotions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROMOTION_FILE_PATH))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    String name = parts[0];
                    int buy = Integer.parseInt(parts[1]);
                    int get = Integer.parseInt(parts[2]);
                    LocalDate startDate = LocalDate.parse(parts[3]);
                    LocalDate endDate = LocalDate.parse(parts[4]);
                    promotions.add(new Promotion(name, buy, get, startDate, endDate));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading product file", e);
        }
        return promotions;
    }
}
