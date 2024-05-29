package com.bs.barragewebsitespringboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bs.barragewebsitespringboot.entity.CommentEntity;
import com.bs.barragewebsitespringboot.entity.CommentRepliesEntity;
import com.bs.barragewebsitespringboot.pojo.CommentReplies;
import com.bs.barragewebsitespringboot.pojo.SystemMsg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepliesDAO extends BaseMapper<CommentReplies>{

    List<CommentReplies> getReplyCommentAdmin();

    List<CommentRepliesEntity> getReplyToMeMsg(@Param("messageIdList") List<Long> messageIdList);

    List<CommentReplies> getReplyByUserId(@Param("userId") int userId);
}
