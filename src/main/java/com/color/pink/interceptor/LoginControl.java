package com.color.pink.interceptor;

import com.color.pink.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HarrisonLee
 * @date 2020/5/3 4:32
 */
@Component
public class LoginControl implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String uri = request.getRequestURI();
//        System.out.println(uri);
//        if(uri.startsWith("/admin") && !Objects.equals(uri, "/admin/login")) {
//            final var cookies = request.getCookies();
//            if(Objects.isNull(cookies)) {
//                response.setStatus(401);
//                return false;
//            }
//            String token = null;
//            for(var i = 0;i < cookies.length; ++i) {
//                if(cookies[i].getName().equals("token")) {
//                    token = cookies[i].getValue();
//                    break;
//                }
//            }
//            final var b = loginService.checkToken(token);
//            if(!b) {
//                response.setStatus(401);
//                return false;
//            }
//        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
