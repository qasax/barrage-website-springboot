package com.bs.barragewebsitespringboot.security;

import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        HashMap map=new HashMap();
        map.put("code",-1);
        map.put("data","授权不足");
        String json = null;
        try {
            json = MapperUtils.obj2json(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //处理异常
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }
}
