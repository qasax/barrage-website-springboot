package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.CommentDAO;
import com.bs.barragewebsitespringboot.dao.CommentRepliesDAO;
import com.bs.barragewebsitespringboot.entity.CommentEntity;
import com.bs.barragewebsitespringboot.pojo.Comment;
import com.bs.barragewebsitespringboot.pojo.CommentReplies;
import com.bs.barragewebsitespringboot.service.CommentService;
import com.bs.barragewebsitespringboot.service.VideoService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDAO,Comment> implements CommentService {
    @Autowired
    CommentDAO commentDAO;
    @Autowired
    CommentRepliesDAO commentRepliesDAO;
    @Autowired
    VideoService videoService;
    @Override
    public String sendCommentByVideo(HttpServletRequest httpServletRequest, Comment comment) throws Exception {
        //request域中储存本次请求的用户id
        Timestamp timestamp=new Timestamp(new Date().getTime());
        comment.setTimestamp(timestamp);
        comment.setSenderUserId((Integer) httpServletRequest.getAttribute("userId"));
        comment.setLikes(0);
        commentDAO.insert(comment);
        videoService.commentPlus(comment.getVideoId());
        Map map=new HashMap<>();
        map.put("code","success");
        map.put("data",comment);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String replyCommentByVideo(HttpServletRequest httpServletRequest, CommentReplies commentReplies) throws Exception {
        Timestamp timestamp=new Timestamp(new Date().getTime());
        commentReplies.setTimestamp(timestamp);
        commentReplies.setFromUserId((Integer) httpServletRequest.getAttribute("userId"));
        commentReplies.setLikes(0);
        commentRepliesDAO.insert(commentReplies);
        Map map=new HashMap<>();
        map.put("code","success");
        map.put("data",commentReplies);
        return MapperUtils.obj2json(map);
    }
    @Override
    @Transactional
    public String likePlus(int commentId) throws Exception {
        Comment comment=commentDAO.selectById(commentId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("comment_id",comment.getCommentId());
        updateWrapper.set("likes",comment.getLikes()+1);
        commentDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }
    @Override
    @Transactional
    public String likeSub(int commentId) throws Exception {
        Comment comment=commentDAO.selectById(commentId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("comment_id",comment.getCommentId());
        updateWrapper.set("likes",comment.getLikes()-1);
        commentDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageComment(int videoId, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<CommentEntity> list=commentDAO.getPageComment(videoId);
        PageInfo<CommentEntity> pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageCommentAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<CommentEntity> list=commentDAO.getPageCommentAdmin();
        PageInfo<CommentEntity> pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getCommentByUserId(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<CommentEntity> list=commentDAO.getCommentByUserId((Integer) request.getAttribute("userId"));
        PageInfo<CommentEntity> pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
}
