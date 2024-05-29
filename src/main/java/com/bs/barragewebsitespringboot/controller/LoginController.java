package com.bs.barragewebsitespringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bs.barragewebsitespringboot.pojo.LoginUser;
import com.bs.barragewebsitespringboot.pojo.User;
import com.bs.barragewebsitespringboot.service.LoginService;
import com.bs.barragewebsitespringboot.service.UserService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    UserService userService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;
    /**
     * 用户修改密码
     * @param request
     * @param user
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("resetPwd")
    public String resetPwd(HttpServletRequest request, @RequestBody User user) throws Exception {
        String encodePwd=passwordEncoder.encode("123456");
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",user.getUserId());
        updateWrapper.set("password",encodePwd);
        userService.update(updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }
    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        //登录
        return loginService.login(user);
    }

    /**
     * 普通用户注册
     * @param user
     * @return
     */
    @PostMapping("/registerNormal")
    public String registerNormal(@RequestBody User user){
        user.setRole("user");
        user.setAvatarUrl("default1.jpg");
        user.setSignature("");
        user.setUserLevel(0);
        user.setExperience(0);
        user.setFollowCounts(0);
        user.setFans(0);
        return loginService.register(user);
    }
    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String register(@RequestBody User user){
        user.setFans(0);
        return loginService.register(user);
    }

    /**
     * 用户退出登录
     * @return
     */
    @GetMapping("logout")
    public String logout(){
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        int userId = loginUser.getUserId();
        //删除redis中的值
        //redisTemplate.opsForValue().getAndDelete(userId);
        stringRedisTemplate.delete(String.valueOf(userId));
        return  ("注销成功");
    }

    /**
     * 用户修改密码
     * @param request
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("changePwd")
    public String changePwd(HttpServletRequest request, @RequestBody User user) throws Exception {
        String encodePwd=passwordEncoder.encode(user.getPassword());
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",request.getAttribute("id"));
        updateWrapper.set("password",encodePwd);
        userService.update(updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }
    @GetMapping("/validateUsername")
    public String validateUsername( String username) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        User user=userService.getOne(queryWrapper);
        Map map=new HashMap();
        if (user==null){
            map.put("code","success");
        }else{
            map.put("code","error");
        }
        return MapperUtils.obj2json(map);
    }
}

