package com.bs.barragewebsitespringboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bs.barragewebsitespringboot.pojo.Barrage;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BarrageDAO extends BaseMapper<Barrage> {
    public int sendBarrage(@Param("barrage") Barrage barrage);
    @MapKey("barrageId")
    public List<Barrage> getBarrage(@Param("videoId") String id);

    List<Barrage> getPageBarrageAdmin();
}
