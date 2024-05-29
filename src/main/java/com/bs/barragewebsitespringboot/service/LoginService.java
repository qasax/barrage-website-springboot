package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.pojo.LoginUser;
import com.bs.barragewebsitespringboot.pojo.User;

public interface LoginService  {
    public String login(User user) ;
    public String register(User user);
}
