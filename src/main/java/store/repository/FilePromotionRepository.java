package store.repository;

import store.model.Promotion;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                String[] parts = line.split(",");
                String name = parts[0];
                int buy = Integer.parseInt(parts[1]);
                int get = Integer.parseInt(parts[2]);
                LocalDate startDate = LocalDate.parse(parts[3]);
                LocalDate endDate = LocalDate.parse(parts[4]);
                int stock = 100; // 임의의 초기 재고 설정
                promotions.add(new Promotion(name, buy, get, startDate, endDate, stock));
            }
        } catch (IOException e) {
            throw new RuntimeException("프로모션 파일을 읽는 중 오류가 발생했습니다.", e);
        }
        return promotions;
    }

    @Override
    public Optional<Promotion> findPromotionByName(String name) {
        return getAllPromotions().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();
    }

    @Override
    public void updatePromotion(Promotion promotion) {
        List<Promotion> promotions = getAllPromotions();
        for (int i = 0; i < promotions.size(); i++) {
            if (promotions.get(i).getName().equals(promotion.getName())) {
                promotions.set(i, promotion);
                break;
            }
        }
        savePromotions(promotions);
    }

    private void savePromotions(List<Promotion> promotions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("name,buy,get,start_date,end_date\n");
            for (Promotion promotion : promotions) {
                writer.write(String.format("%s,%d,%d,%s,%s\n",
                        promotion.getName(),
                        promotion.getBuyQuantity(),
                        promotion.getGetFreeQuantity(),
                        promotion.getStartDate(),
                        promotion.getEndDate()));
            }
        } catch (IOException e) {
            throw new RuntimeException("프로모션 파일을 저장하는 중 오류가 발생했습니다.", e);
        }
    }
}