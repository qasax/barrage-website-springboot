package com.bs.barragewebsitespringboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bs.barragewebsitespringboot.entity.VideoEntity;
import com.bs.barragewebsitespringboot.pojo.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface VideoDAO extends BaseMapper<Video> {
    public List<Video> getAllVideo();
    public int uploadVideo(@Param("video") Video video);

    public int uploadVideoPic(@Param("video") Video video);

    List<VideoEntity> getPageVideoByType(@Param("type") String type);

    List<VideoEntity> getVideoByIds(@Param("videoIdList") List<Integer> videoIdList);

    List<VideoEntity> getPageVideoBySearch(@Param("keyword") String keyword);

    List<Video> getPageVideoAdmin();

    Video getVideoDetail(@Param("videoId") int videoId);

    List<VideoEntity> getPageVideoByUserId(Object userId);
}
