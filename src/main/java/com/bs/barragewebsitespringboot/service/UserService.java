package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.pojo.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {
    public String getFans(HttpServletRequest httpServletRequest) throws Exception;

    public String fanPlus(int followingUserId) throws Exception;

    public String fanSub(int followingUserId) throws Exception;

    String getPageUserListAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

    String setUserRoleAdmin(String userId, String role) throws Exception;

    String updateUserInfoAdmin(User user) throws Exception;
}
