package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.UserDAO;
import com.bs.barragewebsitespringboot.pojo.LoginUser;
import com.bs.barragewebsitespringboot.pojo.User;
import com.bs.barragewebsitespringboot.service.LoginService;
import com.bs.barragewebsitespringboot.utils.JwtUtils;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
   @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String login(User user) {
        try{
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authentication = authenticationManager.authenticate(auth);
            //如果认证没通过，给出对应的提示（如果authenticate为null，则认证不通过）
            if (Objects.isNull(authentication)) {
                throw new RuntimeException("登录失败");
            }
            //如果认证通过了，使用userId生成一个jwt，返回
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            //System.out.println(loginUser.getUserId()+"--login userId");//读取当前登录用户的id
            //System.out.println(loginUser.getRole()+"--login role");
            String jwtToken = JwtUtils.getJwtToken(String.valueOf(loginUser.getUserId()),user.getUsername(),loginUser.getRole());
            HashMap result = new HashMap();
            result.put("code", "success");
            result.put("message", "登录成功");
            result.put("data", user);
            result.put("token", jwtToken);
            stringRedisTemplate.opsForValue().set(String.valueOf(loginUser.getUserId()),jwtToken, Duration.ofHours(72));
            return MapperUtils.obj2json(result);
        } catch (Exception e){
            HashMap result = new HashMap();
            result.put("code", "error");
            result.put("message", "登录失败");
            try {
                return MapperUtils.obj2json(result);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    @Autowired
    UserDAO userDAO;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public String register(User user) {
        try {
            String oPassword = user.getPassword();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            HashMap result = new HashMap();
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, user.getUsername());
            if (userDAO.selectList(wrapper).size() == 0){
               userDAO.insert(user);
                int id=userDAO.selectOne(wrapper).getUserId();//获取当前注册用户的id
               String token= JwtUtils.getJwtToken(String.valueOf(id),user.getUsername(),user.getRole());
                result.put("code", "success");
                result.put("message", "注册成功");
                result.put("data", user);
                result.put("token",token);
                stringRedisTemplate.opsForValue().set(String.valueOf(id),token, Duration.ofHours(24));
                return MapperUtils.obj2json(result);
            }else {
                result.put("code", "error");
                result.put("message", "用户名存在");
                return  MapperUtils.obj2json(result);
            }
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
          /*   //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userId生成一个jwt，jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = String.valueOf(loginUser.getUser().getUserId());
        String username=loginUser.getUser().getUsername();
        String role=loginUser.getUser().getRole();
        String jwt = JwtUtils.getJwtToken(userId,username,role);
        Map<String,String> map = new HashMap<>();
        map.put("token", jwt);
        //把完整的用户信息存入redis，userId作为key
       // redisCache.setCacheObject("login:"+userId, loginUser);
        return new ResponseResult(200, "登录成功", map);*/
