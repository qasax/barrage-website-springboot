package com.bs.barragewebsitespringboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bs.barragewebsitespringboot.entity.CommentEntity;
import com.bs.barragewebsitespringboot.pojo.Comment;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDAO extends BaseMapper<Comment> {
    public List<CommentEntity> getPageComment(@Param("videoId") int videoId);

    List<CommentEntity> getPageCommentAdmin();

    List<CommentEntity> getCommentByUserId(@Param("userId")int userId);
}
