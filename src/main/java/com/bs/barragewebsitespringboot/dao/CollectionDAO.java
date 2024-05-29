package com.bs.barragewebsitespringboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bs.barragewebsitespringboot.entity.CollectionEntity;
import com.bs.barragewebsitespringboot.entity.LikeEntity;
import com.bs.barragewebsitespringboot.pojo.Collection;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionDAO extends BaseMapper<Collection> {
    public List<String> getPageCollectionByUserId(@Param("userId") int userId);

    List<CollectionEntity> getPageCollectionDetail(@Param("userId") Integer id, @Param("collectionName") String collectionName);
}
