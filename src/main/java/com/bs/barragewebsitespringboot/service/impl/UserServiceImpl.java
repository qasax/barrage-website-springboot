package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.UserDAO;
import com.bs.barragewebsitespringboot.entity.UserFollowEntity;
import com.bs.barragewebsitespringboot.pojo.Video;
import com.bs.barragewebsitespringboot.service.UserFollowService;
import com.bs.barragewebsitespringboot.service.UserService;
import com.bs.barragewebsitespringboot.pojo.User;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl  extends ServiceImpl<UserDAO, User> implements UserService {
    @Autowired
    UserDAO userDAO;
    @Lazy//解决与UserFollowDAO循环依赖的问题
    @Autowired
    UserFollowService userFollowService;
    @Override
    public String getFans(HttpServletRequest httpServletRequest) throws Exception {
        return userFollowService.getFans(httpServletRequest);
    }
    @Override
    public String fanPlus(int followingUserId) throws Exception {
        User user=userDAO.selectById(followingUserId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",followingUserId);
        updateWrapper.set("fans",user.getFans()+1);
        userDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }
    @Override
    public String fanSub(int followingUserId) throws Exception {
        User user=userDAO.selectById(followingUserId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",followingUserId);
        updateWrapper.set("fans",user.getFans()-1);
        userDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageUserListAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<User> list=userDAO.getPageUserListAdmin();
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String setUserRoleAdmin(String userId, String role) throws Exception {
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",userId);
        updateWrapper.set("role",role);
        userDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }
    @Override
    public String updateUserInfoAdmin(User user) throws Exception {
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",user.getUserId());
        updateWrapper.set("gender",user.getGender());
        updateWrapper.set("email",user.getEmail());
        updateWrapper.set("birthday",user.getBirthday());
        updateWrapper.set("signature",user.getSignature());
        updateWrapper.set("follow_counts",user.getFollowCounts());
        updateWrapper.set("fans",user.getFans());
        userDAO.update(null,updateWrapper);
        Map map=new HashMap<>();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }
}
