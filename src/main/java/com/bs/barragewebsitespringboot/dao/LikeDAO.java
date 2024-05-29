package com.bs.barragewebsitespringboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bs.barragewebsitespringboot.entity.LikeEntity;
import com.bs.barragewebsitespringboot.pojo.Like;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeDAO extends BaseMapper<Like> {
     List<LikeEntity> getPageVideoLike(@Param("userId") int id);

     List<LikeEntity> getPageCommentLike(@Param("userId")Integer id);

    List<LikeEntity> getPageReplyLike(@Param("userId")Integer id);
}
