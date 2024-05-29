package com.bs.barragewebsitespringboot.security;

import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.HashMap;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HashMap map=new HashMap();
        map.put("code",-1);
        map.put("data",authException.getMessage());
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
