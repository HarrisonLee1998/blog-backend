package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.service.ConfigService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * @author HarrisonLee
 * @date 2020/5/8 22:32
 */
@RestController
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @ApiOperation("保存前台设置")
    @PostMapping("admin/config/frontend")
    public ResponseUtil saveFrontendConfig(@RequestBody Map<String, String>map) {
        final var response = ResponseUtil.factory();
        final var result = configService.saveFrontendConfig(map);
        if(!result) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation("保存后台设置")
    @PostMapping("admin/config/backend")
    public ResponseUtil saveBackendConfig(@RequestBody Map<String, String>map) {
        final var response = ResponseUtil.factory();
        final var result = configService.saveBackendConfig(map);
        if(!result) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation("获取前台设置")
    @GetMapping(value = {"config/frontend", "admin/config/frontend"})
    public ResponseUtil getFrontendConfig() {
        final var response = ResponseUtil.factory();
        final var frontendConfig = configService.getFrontendConfig();
        if(Objects.nonNull(frontendConfig)) {
            response.put("config", frontendConfig);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ApiOperation("获取后台设置")
    @GetMapping(value = {"admin/config/backend"})
    public ResponseUtil getBackendConfig() {
        final var response = ResponseUtil.factory();
        final var backendConfig = configService.getBackendConfig();
        if(Objects.nonNull(backendConfig)) {
            response.put("config", backendConfig);
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
