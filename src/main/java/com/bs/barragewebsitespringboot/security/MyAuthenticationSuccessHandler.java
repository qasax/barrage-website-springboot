package com.bs.barragewebsitespringboot.security;

import com.alibaba.fastjson.JSON;
import com.bs.barragewebsitespringboot.pojo.LoginUser;
import com.bs.barragewebsitespringboot.pojo.User;
import com.bs.barragewebsitespringboot.utils.JwtUtils;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

       /* response.setContentType("application/json;charset=UTF-8");
        //获取用户身份信息
        LoginUser model = (LoginUser) authentication.getPrincipal();
        String jwtToken = JwtUtils.getJwtToken(String.valueOf(model.getUserId()), model.getPassword(), model.getRole());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = new User();
        user.setUsername(model.getUsername());
        user.setRole(model.getRole());
        user.setRole(user.getRole().substring("ROLE_".length()));

        //创建结果对象
        HashMap result = new HashMap();
        result.put("code", 0);
        result.put("message", "登录成功");
        result.put("data", user);
        result.put("token", jwtToken);
        //返回响应
        String json = null;
        try {
            json = MapperUtils.obj2json(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.getWriter().println(json);*/

        LoginUser model = (LoginUser) authentication.getPrincipal();
        //创建结果对象
        HashMap result = new HashMap();
        result.put("code", 0);
        result.put("message", model);

        //转换成json字符串
        String json = null;
        try {
            json = MapperUtils.obj2json(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //返回响应
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }

}