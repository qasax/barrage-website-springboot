package com.bs.barragewebsitespringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bs.barragewebsitespringboot.constant.DPlayerConstants;
import com.bs.barragewebsitespringboot.pojo.Comment;
import com.bs.barragewebsitespringboot.pojo.CommentReplies;
import com.bs.barragewebsitespringboot.service.CommentService;
import com.bs.barragewebsitespringboot.service.impl.CommentServiceImpl;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    /**
     * 分页查询评论 管理员
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping(value = "getPageCommentAdmin")
    public String getPageCommentAdmin(int pageSize,int pageNo,String orderColumn,String sortOrder) throws Exception {
        return commentService.getPageCommentAdmin(pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 修改评论 管理员
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping(value = "updateCommentAdmin")
    public String updateCommentAdmin(@RequestBody Comment comment) throws Exception {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("comment_id",comment.getCommentId());
        commentService.update(comment,updateWrapper);
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
    @DeleteMapping(value = "deleteCommentAdmin")
    public String deleteCommentAdmin(@RequestBody Comment comment) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("comment_id",comment.getCommentId());
        commentService.remove(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }
    /**\
     * 分页获取视频评论，以主评论为分页依据。即每页显示pagesize个主评论，及主评论的所有子评论。
     * @param videoId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "getPageComment")
    public String getPageComment(  int videoId,  int pageSize,  int pageNo,  String orderColumn,  String sortOrder) throws Exception {
        return commentService.getPageComment(videoId,pageSize,pageNo,orderColumn,sortOrder);
    }
    /**\
     * 获取指定用户的评论
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping(value = "getCommentByUserId")
    public String getCommentByUserId( HttpServletRequest request,   int pageSize,  int pageNo,  String orderColumn,  String sortOrder) throws Exception {
        return commentService.getCommentByUserId(request,pageSize,pageNo,orderColumn,sortOrder);
    }
    /**\
     * 删除指定的评论
     * @param
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "deleteCommentByUserId")
    public String deleteCommentByUserId(HttpServletRequest request,int commentId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("sender_user_id",request.getAttribute("userId"));
        queryWrapper.eq("comment_id",commentId);
       commentService.remove(queryWrapper);
       Map map=new HashMap();
       map.put("code","success");
       map.put("data","delete success");
        return MapperUtils.obj2json(map);
    }
    /**
     * 给视频发送评论
     * @param comment
     * @return
     */
    @PostMapping("sendCommentByVideo")
    public String sendCommentByVideo(HttpServletRequest httpServletRequest,@RequestBody Comment comment) throws Exception {
        return commentService.sendCommentByVideo(httpServletRequest,comment);
    }

    /**
     * 回复视频下方的用户评论--已弃用
     * @param commentReplies
     * @return
     */
    @PostMapping("replyCommentByVideo")
    public String replyCommentByVideo(HttpServletRequest httpServletRequest, @RequestBody CommentReplies commentReplies) throws Exception {
        return commentService.replyCommentByVideo(httpServletRequest,commentReplies);
    }
}
