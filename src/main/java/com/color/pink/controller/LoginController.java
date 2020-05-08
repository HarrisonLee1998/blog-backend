package com.color.pink.controller;

import com.color.pink.annotation.ApiOperation;
import com.color.pink.service.LoginService;
import com.color.pink.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @author HarrisonLee
 * @date 2020/5/6 22:51
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation("登录")
    @PostMapping("admin/login")
    public ResponseUtil login(HttpServletResponse response, @RequestBody Map<String, String>map) {
        final var username = map.get("username");
        final var password = map.get("password");
        final var token = loginService.login(username, password);
        final var res = ResponseUtil.factory();
        if(Objects.isNull(token)) {
            res.setStatus(HttpStatus.UNAUTHORIZED);
        } else {
            final var cookie = new Cookie("token", token);
            // cookie.setHttpOnly(true);
            cookie.setMaxAge(60*10);
            response.addCookie(cookie);
            res.put("token", token);
        }
        return res;
    }

    @ApiOperation("登出")
    @PostMapping("admin/logout")
    public void logout(){
        loginService.logout();
    }

    @ApiOperation("验证token有效性")
    @GetMapping("auth/user")
    public ResponseUtil checkToken() {
        return ResponseUtil.factory().put("user", loginService.getUser());
    }
}
