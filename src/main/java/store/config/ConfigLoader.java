package store.config;

import store.util.ErrorMessages;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;

    public ConfigLoader(String propertiesFileName) {
        properties = new Properties();
        loadProperties(propertiesFileName);
    }

    private void loadProperties(String fileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw ErrorMessages.CONFIG_FILE_NOT_FOUND.getException(fileName);
            }
            properties.load(input);
        } catch (IOException ex) {
            throw ErrorMessages.CONFIG_FILE_LOAD_ERROR.getException(fileName);
        }
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw ErrorMessages.CONFIG_PROPERTY_NOT_FOUND.getException(key);
        }
        return value;
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}