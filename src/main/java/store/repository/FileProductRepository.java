package store.repository;

import store.model.Product;
import store.util.ErrorMessages;

import java.io.*;
import java.util.*;

public class FileProductRepository implements ProductRepository {
    private final String filePath;

    public FileProductRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Product> getAllProducts() {
        Map<String, List<Product>> productMap = readProductsFromFile();
        return createFinalProductList(productMap);
    }

    private Map<String, List<Product>> readProductsFromFile() {
        Map<String, List<Product>> productMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(productMap, line);
            }
        } catch (IOException e) {
            throw ErrorMessages.PRODUCT_FILE_READ_ERROR.getException(e.getMessage());
        }
        return productMap;
    }

    private void processLine(Map<String, List<Product>> productMap, String line) {
        if (!line.trim().isEmpty()) {
            addProductToMap(productMap, line);
        }
    }

    private void addProductToMap(Map<String, List<Product>> productMap, String line) {
        String[] parts = line.split(",");
        validateProductData(parts, line);
        Product product = createProduct(parts);
        addToProductMap(productMap, product);
    }

    private void validateProductData(String[] parts, String line) {
        if (parts.length < 3) {
            throw ErrorMessages.INVALID_PRODUCT_DATA_FORMAT.getException(line);
        }
    }

    private Product createProduct(String[] parts) {
        String name = parts[0];
        int price = Integer.parseInt(parts[1]);
        int quantity = Integer.parseInt(parts[2]);
        String promotion = getPromotion(parts);
        return new Product(name, price, quantity, promotion);
    }

    private String getPromotion(String[] parts) {
        if (parts.length > 3 && !parts[3].trim().isEmpty()) {
            return parts[3];
        }
        return null;
    }

    private void addToProductMap(Map<String, List<Product>> productMap, Product product) {
        productMap.computeIfAbsent(product.getName(), k -> new ArrayList<>()).add(product);
    }

    private List<Product> createFinalProductList(Map<String, List<Product>> productMap) {
        List<Product> finalProducts = new ArrayList<>();
        for (List<Product> productList : productMap.values()) {
            addProductsToFinalList(finalProducts, productList);
            addOutOfStockProduct(finalProducts, productList);
        }
        return finalProducts;
    }

    private void addProductsToFinalList(List<Product> finalProducts, List<Product> productList) {
        for (Product product : productList) {
            if (product.getQuantity() > 0) {
                finalProducts.add(createProductWithValidPromotion(product));
            }
        }
        if (finalProducts.isEmpty()) {
            addOutOfStockProduct(finalProducts, productList);
        }
    }

    private Product createProductWithValidPromotion(Product product) {
        String promotion = getValidPromotion(product.getPromotion());
        return new Product(
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                promotion
        );
    }

    private String getValidPromotion(String promotion) {
        if (promotion == null) {
            return "";
        }
        return promotion;
    }

    private void addOutOfStockProduct(List<Product> finalProducts, List<Product> productList) {
        Product outOfStockProduct = productList.get(0);
        String promotion = getValidPromotion(outOfStockProduct.getPromotion());
        finalProducts.add(new Product(
                outOfStockProduct.getName(),
                outOfStockProduct.getPrice(),
                0,
                promotion
        ));
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        return getAllProducts().stream()
                .filter(product -> product.getName().equals(name))
                .findFirst();
    }

    @Override
    public void updateProduct(Product updatedProduct) {
        List<Product> products = readAllProductsFromFile();
        updateProductInList(products, updatedProduct);
        saveProductsToFile(products);
    }

    private List<Product> readAllProductsFromFile() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                products.add(createProductFromLine(line));
            }
        } catch (IOException e) {
            throw ErrorMessages.PRODUCT_FILE_READ_ERROR.getException(e.getMessage());
        }
        return products;
    }

    private Product createProductFromLine(String line) {
        String[] parts = line.split(",");
        validateProductData(parts, line);
        return createProduct(parts);
    }

    private void updateProductInList(List<Product> products, Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(updatedProduct.getName())) {
                products.set(i, updatedProduct);
                return;
            }
        }
    }

    private void saveProductsToFile(List<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("name,price,quantity,promotion\n");
            for (Product product : products) {
                writer.write(formatProductLine(product));
            }
        } catch (IOException e) {
            throw ErrorMessages.PRODUCT_FILE_WRITE_ERROR.getException(e.getMessage());
        }
    }

    private String formatProductLine(Product product) {
        String promotion = product.getPromotion();
        if (promotion == null) {
            promotion = "null";
        }
        return String.format("%s,%d,%d,%s\n",
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                promotion);
    }
}
