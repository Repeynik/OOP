package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonConfigLoader implements PizzaConfigLoader {
    private final String configPath;

    public JsonConfigLoader(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public PizzaConfig loadConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(configPath), PizzaConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }
}
