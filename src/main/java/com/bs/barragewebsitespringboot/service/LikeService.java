package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.pojo.Like;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface LikeService extends IService<Like> {
    public String addLike(HttpServletRequest httpServletRequest,  Like like) throws Exception;

    public String getVideoLike(HttpServletRequest httpServletRequest) throws Exception;

    public String getCommentLike(HttpServletRequest httpServletRequest) throws Exception;

    public String getReplyLike(HttpServletRequest httpServletRequest) throws Exception;

    public String subLike(HttpServletRequest httpServletRequest, Like like) throws Exception;

    public String getPageVideoLike(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

    public String getPageCommentLike(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

    public String getPageReplyLike(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

    String isVideoLike(HttpServletRequest httpServletRequest, String videoId) throws Exception;

    String isCommentLike(HttpServletRequest httpServletRequest, String commentId) throws Exception;

    String isReplyLike(HttpServletRequest httpServletRequest, String replyId) throws Exception;
}
