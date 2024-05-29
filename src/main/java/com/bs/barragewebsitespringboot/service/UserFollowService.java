package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.dao.UserFollowDAO;
import com.bs.barragewebsitespringboot.pojo.UserFollow;
import jakarta.servlet.http.HttpServletRequest;

public interface UserFollowService extends IService<UserFollow> {
    public String addFollow(HttpServletRequest httpServletRequest, UserFollow userFollow) throws Exception;

    public String deleteFollow(HttpServletRequest httpServletRequest, UserFollow userFollow) throws Exception;

    public String getFollowByUserId(HttpServletRequest httpServletRequest) throws Exception;

    public String getFans(HttpServletRequest httpServletRequest) throws Exception;

    String getPageFollowByUserId(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

    String isFollowUser(HttpServletRequest httpServletRequest, String followingUserId) throws Exception;

    String getPageFans(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

    String getLatelyMsg(HttpServletRequest httpServletRequest, long max, int offset ) throws Exception;

    String getUserPageFollow(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder, int userId) throws Exception;

    String getUserPageFans(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder, int userId) throws Exception;
}
