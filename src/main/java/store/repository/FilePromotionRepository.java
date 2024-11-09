package store.repository;

import store.model.Promotion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilePromotionRepository implements PromotionRepository {
    private final String filePath;

    public FilePromotionRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Promotion> getAllPromotions() {
        List<Promotion> promotions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip header
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
            throw new RuntimeException("Error reading promotion file", e);
        }
        return promotions;
    }
}