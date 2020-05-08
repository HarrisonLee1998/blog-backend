package com.color.pink.service;

import com.color.pink.util.JWTUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author HarrisonLee
 * @date 2020/5/6 22:33
 */
@Service
public class LoginService {

    private Map<String, String>info;

    private boolean logined;

    public String getUser() {
        return logined ? info.get("username") : null;
    }

    public String login(String username, String password) {
        if(Objects.isNull(info)) {
            final var b = readInfoFile();
            if (!b) {
                return null;
            }
        }
        if(Objects.equals(username, info.get("username")) && Objects.equals(password, info.get("password"))){
            logined = true;
            return getToken(username, password);
        } else {
            return null;
        }
    }

    public void logout() {
        this.logined = false;
    }

    public String getToken(String username, String password) {
        return JWTUtil.createJWT(username, password);
    }

    public boolean checkToken(String token) {
        if(Objects.isNull(info)) {
            final var b = readInfoFile();
            if (!b) {
                return false;
            }
        }
        String username = info.get("username");
        String password = info.get("password");
        return JWTUtil.verify(token, username, password);
    }

    private boolean readInfoFile() {
        final var properties = new Properties();
        try(final var inputStream = this.getClass().getClassLoader().getResourceAsStream("properties/login-info.properties")) {
            Objects.requireNonNull(inputStream);
            properties.load(inputStream);
            this.info = new HashMap<>();
            properties.forEach((key, value)->{
                this.info.put((String) key, (String) value);
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
