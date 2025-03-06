package com.ppu.ppu.interceptor;

import com.ppu.ppu.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil = new JwtUtil("hello", Long.valueOf(3600000));
    // TODO: 환경변수로 분리 필요
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
        String accessToken = authorization.split(" ")[1];
        if(! jwtUtil.validateToken(accessToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
        request.setAttribute("id" , jwtUtil.parseToken(accessToken));
        return true;
    }
}
