package store.repository;

import store.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileProductRepository implements ProductRepository {
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE_PATH))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    String name = parts[0];
                    int price = Integer.parseInt(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    String promotion = parts[3];
                    products.add(new Product(name, price, quantity, promotion));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading product file", e);
        }
        return products;
    }
}
