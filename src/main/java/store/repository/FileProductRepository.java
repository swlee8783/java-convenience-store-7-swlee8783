package store.repository;

import store.model.Product;
import store.util.ErrorMessages;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileProductRepository implements ProductRepository {
    private final String filePath;

    public FileProductRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    String name = parts[0];
                    int price = Integer.parseInt(parts[1]);
                    int quantity = Integer.parseInt(parts[2]);
                    String promotion = "null".equals(parts[3]) ? null : parts[3];
                    products.add(new Product(name, price, quantity, promotion));
                }
            }
        } catch (IOException e) {
            ErrorMessages.PRODUCT_FILE_READ_ERROR.getException(e.getMessage());
        }
        return products;
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        return getAllProducts().stream()
                .filter(product -> product.getName().equals(name))
                .findFirst();
    }

    @Override
    public void updateProduct(Product product) {
        List<Product> products = getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(product.getName())) {
                products.set(i, product);
                break;
            }
        }
        saveProducts(products);
    }

    private void saveProducts(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("name,price,quantity,promotion\n");
            for (Product product : products) {
                writer.write(String.format("%s,%d,%d,%s\n",
                        product.getName(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getPromotion() != null ? product.getPromotion() : ""));
            }
        } catch (IOException e) {
            throw ErrorMessages.PRODUCT_FILE_WRITE_ERROR.getException(e.getMessage());
        }
    }
}
