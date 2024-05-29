package com.bs.barragewebsitespringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bs.barragewebsitespringboot.pojo.Comment;
import com.bs.barragewebsitespringboot.pojo.CommentReplies;
import com.bs.barragewebsitespringboot.service.CommentRepliesService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("commentReplies")
public class CommentRepliesController {
    @Autowired
    CommentRepliesService commentRepliesService;
    /**
     * 分页查询回复
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping(value = "getReplyCommentAdmin")
    public String getPageCommentAdmin(int pageSize,int pageNo,String orderColumn,String sortOrder) throws Exception {
        return commentRepliesService.getReplyCommentAdmin(pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 删除评论评论 管理员
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping(value = "updateReplyAdmin")
    public String updateReplyAdmin(@RequestBody CommentReplies commentReplies) throws Exception {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("reply_id",commentReplies.getCommentId());
        commentRepliesService.update(commentReplies,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }
    /**
     * 删除评论评论 管理员
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping(value = "deleteReplyAdmin")
    public String deleteReplyAdmin(@RequestBody CommentReplies commentReplies) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("reply_id",commentReplies.getReplyId());
        commentRepliesService.remove(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }
    /**
     * 回复视频下方的评论
     * @param request
     * @param commentReplies
     * @return
     * @throws Exception
     */
    @PostMapping("replyCommentByVideo")
    public String replyCommentByVideo(HttpServletRequest request, @RequestBody CommentReplies commentReplies) throws Exception {
        return commentRepliesService.replyCommentByVideo(request,commentReplies);
    }
    /**\
     * 获取指定用户的评论
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping(value = "getReplyByUserId")
    public String getReplyByUserId( HttpServletRequest request,  int pageSize,  int pageNo,  String orderColumn,  String sortOrder) throws Exception {
        return commentRepliesService.getReplyByUserId(request,pageSize,pageNo,orderColumn,sortOrder);
    }
    /**\
     * 删除指定的评论
     * @param
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "deleteReplyByUserId")
    public String deleteReplyByUserId(HttpServletRequest request,int replyId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("from_user_id",request.getAttribute("userId"));
        queryWrapper.eq("reply_id",replyId);
        commentRepliesService.remove(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","delete success");
        return MapperUtils.obj2json(map);
    }
    /**
     * 滚动分页获取   回复我的消息
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("getReplyToMeMsg")
    public String getReplyToMeMsg(HttpServletRequest httpServletRequest, long max,@RequestParam(value = "offset",defaultValue = "0") int offset) throws Exception {
        return commentRepliesService.getReplyToMeMsg(httpServletRequest,max,offset);
    }
}
