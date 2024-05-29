package com.bs.barragewebsitespringboot.controller;

import com.bs.barragewebsitespringboot.pojo.Like;
import com.bs.barragewebsitespringboot.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("like")
public class LikeController {
    @Autowired
    LikeService likeService;

    /**
     * 添加点赞 -视频，评论，回复
     * @param httpServletRequest
     * @param like
     * @return
     * @throws Exception
     */
    @PostMapping("addLike")
    public String addLike(HttpServletRequest httpServletRequest, @RequestBody Like like) throws Exception {
        return likeService.addLike(httpServletRequest,like);
    }

    /**
     * 取消点赞-视频，评论，回复
     * @param httpServletRequest
     * @param like
     * @return
     * @throws Exception
     */
    @PostMapping("subLike")
    public String subLike(HttpServletRequest httpServletRequest, @RequestBody Like like) throws Exception {
        return likeService.subLike(httpServletRequest,like);
    }

    /**
     * 分页获取用户点赞视频
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getPageVideoLike")
    public String getPageVideoLike(HttpServletRequest httpServletRequest, int pageSize,int pageNo,String orderColumn,String sortOrder) throws Exception {
        return likeService.getPageVideoLike(httpServletRequest,pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 获取用户点赞视频
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getVideoLike")
    public String getVideoLike(HttpServletRequest httpServletRequest) throws Exception {
        return likeService.getVideoLike(httpServletRequest);
    }

    /**
     * 分页获取用户点赞评论
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getPageCommentLike")
    public String getPageCommentLike(HttpServletRequest httpServletRequest, int pageSize,int pageNo,String orderColumn,String sortOrder) throws Exception {
        return likeService.getPageCommentLike(httpServletRequest,pageSize,pageNo,orderColumn,sortOrder);
    }


    /**
     * 获取用户点赞评论
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getCommentLike")
    public String getCommentLike(HttpServletRequest httpServletRequest) throws Exception {
        return likeService.getCommentLike(httpServletRequest);
    }
    /**
     * 分页获取用户点赞回复
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getPageReplyLike")
    public String getPageReplyLike(HttpServletRequest httpServletRequest, int pageSize,int pageNo,String orderColumn,String sortOrder) throws Exception {
        return likeService.getPageReplyLike(httpServletRequest,pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 获取用户点赞回复
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getReplyLike")
    public String getReplyLike(HttpServletRequest httpServletRequest) throws Exception {
        return likeService.getReplyLike(httpServletRequest);
    }

    /**
     * 获取用户是否点赞了视频
     * @param httpServletRequest
     * @param videoId
     * @return
     * @throws Exception
     */
    @GetMapping("isVideoLike")
    public String isVideoLike(HttpServletRequest httpServletRequest,String videoId) throws Exception{
        return likeService.isVideoLike(httpServletRequest,videoId);
    }

    /**
     * 获取用户是否点赞了评论
     * @param httpServletRequest
     * @param commentId
     * @return
     * @throws Exception
     */
    @GetMapping("isCommentLike")
    public String isCommentLike(HttpServletRequest httpServletRequest,String commentId) throws Exception{
        return likeService.isCommentLike(httpServletRequest,commentId);
    }

    /**
     * 获取用户是否点赞了回复
     * @param httpServletRequest
     * @param replyId
     * @return
     * @throws Exception
     */
    @GetMapping("isReplyLike")
    public String isReplyLike(HttpServletRequest httpServletRequest,String replyId) throws Exception{
        return likeService.isReplyLike(httpServletRequest,replyId);
    }
}
