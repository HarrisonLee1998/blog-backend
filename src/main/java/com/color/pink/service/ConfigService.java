package com.color.pink.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author HarrisonLee
 * @date 2020/5/8 22:37
 */
@Service
public class ConfigService {

    private final String frontendConfigFile = "properties/config/frontend.properties";
    private final String backendConfigFile = "properties/config/backend.properties";

    private Map<String, String> frontendConfigMap;
    private Map<String, String> backendConfigMap;

    public Map<String, String> getFrontendConfig() {
        if(Objects.nonNull(frontendConfigMap)) {
            return frontendConfigMap;
        } else {
            final var map = readFile(frontendConfigFile);
            if(Objects.nonNull(map)) {
                this.frontendConfigMap = map;
                return frontendConfigMap;
            } else {
                return null;
            }
        }
    }

    public Map<String, String> getBackendConfig() {
        if(Objects.nonNull(backendConfigMap)) {
            return backendConfigMap;
        } else {
            final var map = readFile(backendConfigFile);
            if(Objects.nonNull(map)) {
                this.backendConfigMap = map;
                return backendConfigMap;
            } else {
                return null;
            }
        }
    }

    public Boolean saveFrontendConfig(Map<String, String> map) {
        return saveFile(map, frontendConfigFile);
    }

    public Boolean saveBackendConfig(Map<String, String> map) {
        return saveFile(map, backendConfigFile);
    }

    private Map<String, String> readFile(String file) {
        final var properties = new Properties();
        try (final var inputStream = this.getClass().getClassLoader().getResourceAsStream(file)) {
            assert inputStream != null;
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            final var tempMap = new HashMap<String, String>();
            properties.forEach((key, value) -> {
                tempMap.put((String) key, (String) value);
            });
            return tempMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean saveFile(Map<String, String>map, String file){
        final var properties = new Properties();
        properties.putAll(map);
        FileWriter fileWriter = null;
        try {
            final File f = new ClassPathResource(file).getFile();
            fileWriter = new FileWriter(f);
            properties.store(fileWriter, "修改" + file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
