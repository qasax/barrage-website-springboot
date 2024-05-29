package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.pojo.Comment;
import com.bs.barragewebsitespringboot.pojo.CommentReplies;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService extends IService<Comment> {
        String sendCommentByVideo(HttpServletRequest httpServletRequest, Comment comment) throws Exception;

        String replyCommentByVideo(HttpServletRequest httpServletRequest, CommentReplies commentReplies) throws Exception;

        public String likePlus(int commentId) throws Exception ;

        @Transactional
        public String likeSub(int commentId) throws Exception;



        String getPageComment(int videoId, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

        String getPageCommentAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

        String getCommentByUserId(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;
}
