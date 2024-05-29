package com.bs.barragewebsitespringboot.controller;

import com.bs.barragewebsitespringboot.pojo.UserFollow;
import com.bs.barragewebsitespringboot.service.UserFollowService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("userFollow")
public class UserFollowController {
    @Autowired
    UserFollowService userFollowService;

    /**
     * 获取用户的关注列表
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getFollowByUserId")
    public String getFollowByUserId(HttpServletRequest httpServletRequest) throws Exception {
        return userFollowService.getFollowByUserId(httpServletRequest);
    }
    /**
     * 滚动分页获取动态
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getLatelyMsg")
    public String getLatelyMsg(HttpServletRequest httpServletRequest, long max,@RequestParam(value = "offset",defaultValue = "0") int offset) throws Exception {
        return userFollowService.getLatelyMsg(httpServletRequest,max,offset);
    }
    /**
     * 分页获取用户关注列表
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getPageFollow")
    public String getPageFollowByUserId(HttpServletRequest httpServletRequest,@RequestParam int pageSize,int pageNo,String orderColumn,String sortOrder) throws Exception {
        return userFollowService.getPageFollowByUserId(httpServletRequest,pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 分页获取用户粉丝列表
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getPageFans")
    public String getPageFans(HttpServletRequest httpServletRequest,@RequestParam int pageSize,int pageNo,String orderColumn,String sortOrder) throws Exception {
        return userFollowService.getPageFans(httpServletRequest,pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 分页获取 指定用户id的用户关注列表
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getUserPageFollow")
    public String getUserPageFollow(HttpServletRequest httpServletRequest,@RequestParam int pageSize,int pageNo,String orderColumn,String sortOrder,int userId) throws Exception {
        return userFollowService.getUserPageFollow(httpServletRequest,pageSize,pageNo,orderColumn,sortOrder,userId);
    }
    /**
     * 分页获取 指定用户id的用户粉丝列表
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getUserPageFans")
    public String getUserPageFans(HttpServletRequest httpServletRequest,@RequestParam int pageSize,int pageNo,String orderColumn,String sortOrder,int userId) throws Exception {
        return userFollowService.getUserPageFans(httpServletRequest,pageSize,pageNo,orderColumn,sortOrder,userId);
    }
    /**
     * 添加关注用户
     * @param httpServletRequest
     * @param userFollow
     * @return
     * @throws Exception
     */
    @PostMapping("addFollow")
    public String addFollow(HttpServletRequest httpServletRequest, @RequestBody UserFollow userFollow) throws Exception {
        return userFollowService.addFollow(httpServletRequest,userFollow);
    }

    /**
     * 取消关注用户
     * @param httpServletRequest
     * @param userFollow
     * @return
     * @throws Exception
     */
    @DeleteMapping("deleteFollow")
    public String deleteFollow(HttpServletRequest httpServletRequest, @RequestBody UserFollow userFollow) throws Exception {
        return userFollowService.deleteFollow(httpServletRequest,userFollow);
    }
    @PostMapping("isFollowUser")
    public String isFollowUser(HttpServletRequest httpServletRequest,String followingUserId) throws Exception {
        return  userFollowService.isFollowUser(httpServletRequest,followingUserId);
    }
}
