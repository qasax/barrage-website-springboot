package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.LikeDAO;
import com.bs.barragewebsitespringboot.entity.LikeEntity;
import com.bs.barragewebsitespringboot.pojo.Like;
import com.bs.barragewebsitespringboot.service.CommentRepliesService;
import com.bs.barragewebsitespringboot.service.CommentService;
import com.bs.barragewebsitespringboot.service.LikeService;
import com.bs.barragewebsitespringboot.service.VideoService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeDAO, Like>  implements LikeService {
    @Autowired
    LikeDAO likeDAO;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepliesService commentRepliesService;
    @Autowired
    VideoService videoService;
    public String addLike(HttpServletRequest httpServletRequest, Like like) throws Exception {
        like.setUserId((Integer) httpServletRequest.getAttribute("userId"));
        like.setLikeDate(new Timestamp(new Date().getTime()));
        likeDAO.insert(like);
        //使对应点赞数增加
        if(like.getActionObject().equals("video")){
            videoService.likePlus(like.getActionObjectId());
        }
        if(like.getActionObject().equals("comment")){
            commentService.likePlus(like.getActionObjectId());
        }
        if(like.getActionObject().equals("reply")){
            commentRepliesService.likePlus(like.getActionObjectId());
        }
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }
    public String subLike(HttpServletRequest httpServletRequest, Like like) throws Exception {
        like.setUserId((Integer) httpServletRequest.getAttribute("userId"));
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",like.getUserId());
        queryWrapper.eq("action_object",like.getActionObject());
        queryWrapper.eq("action_object_id",like.getActionObjectId());
        likeDAO.delete(queryWrapper);
        //使对应点赞数增加
        if(like.getActionObject().equals("video")){
            videoService.likeSub(like.getActionObjectId());
        }
        if(like.getActionObject().equals("comment")){
            commentService.likeSub(like.getActionObjectId());
        }
        if(like.getActionObject().equals("reply")){
            commentRepliesService.likeSub(like.getActionObjectId());
        }
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageVideoLike(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<LikeEntity> list=likeDAO.getPageVideoLike((Integer) httpServletRequest.getAttribute("userId"));
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageCommentLike(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<LikeEntity> list=likeDAO.getPageCommentLike((Integer) httpServletRequest.getAttribute("userId"));
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageReplyLike(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<LikeEntity> list=likeDAO.getPageReplyLike((Integer) httpServletRequest.getAttribute("userId"));
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
    @Override
    public String getVideoLike(HttpServletRequest httpServletRequest) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        queryWrapper.eq("action_object","video");
        List<Like> likeList=likeDAO.selectList(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",likeList);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getCommentLike(HttpServletRequest httpServletRequest) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        queryWrapper.eq("action_object","comment");
        List<Like> likeList=likeDAO.selectList(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",likeList);
        return MapperUtils.obj2json(map);
    }
    @Override
    public String getReplyLike(HttpServletRequest httpServletRequest) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        queryWrapper.eq("action_object","reply");
        List<Like> likeList=likeDAO.selectList(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",likeList);
        return MapperUtils.obj2json(map);
    }
    @Override
    public String isVideoLike(HttpServletRequest httpServletRequest, String videoId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("action_object_id",videoId);
        queryWrapper.eq("action_object","video");
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        Like like=likeDAO.selectOne(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        if(like!=null){
            map.put("data","true");
        }else {
            map.put("data","false");
        }
        return MapperUtils.obj2json(map);
    }

    @Override
    public String isCommentLike(HttpServletRequest httpServletRequest, String commentId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("action_object_id",commentId);
        queryWrapper.eq("action_object","comment");
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        Like like=likeDAO.selectOne(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        if(like!=null){
            map.put("data","true");
        }else {
            map.put("data","false");
        }
        return MapperUtils.obj2json(map);
    }

    @Override
    public String isReplyLike(HttpServletRequest httpServletRequest, String replyId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("action_object_id",replyId);
        queryWrapper.eq("action_object","reply");
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        Like like=likeDAO.selectOne(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        if(like!=null){
            map.put("data","true");
        }else {
            map.put("data","false");
        }
        return MapperUtils.obj2json(map);
    }
}
