package com.bs.barragewebsitespringboot.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bs.barragewebsitespringboot.dao.UserDAO;
import com.bs.barragewebsitespringboot.pojo.LoginUser;
import com.bs.barragewebsitespringboot.pojo.User;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class DBUserDetailsManager implements UserDetailsService {

    @Resource
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userDAO.selectOne(queryWrapper);
        //System.out.println("数据库中加载的用户role--"+user.getRole());
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            LoginUser loginUser = new LoginUser(user.getUserId(), user.getUsername(), user.getPassword(), user.getRole());
            List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(loginUser.getRole());
            LoginUser lg = new LoginUser(loginUser.getUserId(), loginUser.getUsername(),loginUser.getPassword(), loginUser.getRole(), auths);
            return lg;
        }
    }
}