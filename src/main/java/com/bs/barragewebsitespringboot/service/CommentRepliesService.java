package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.pojo.CommentReplies;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepliesService extends IService<CommentReplies> {
   public String replyCommentByVideo(HttpServletRequest request, CommentReplies commentReplies) throws Exception;

    @Transactional
    String likePlus(int replyId) throws Exception;

    @Transactional
    String likeSub(int replyId) throws Exception;

    String getReplyCommentAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

 String getReplyToMeMsg(HttpServletRequest httpServletRequest, long max, int offset) throws Exception;

    String getReplyByUserId(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;
}
