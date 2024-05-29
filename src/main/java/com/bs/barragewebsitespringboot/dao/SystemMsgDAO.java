package com.bs.barragewebsitespringboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bs.barragewebsitespringboot.entity.VideoEntity;
import com.bs.barragewebsitespringboot.pojo.SystemMsg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemMsgDAO extends BaseMapper<SystemMsg> {
    List<SystemMsg> getSystemMsgByIds(@Param("messageIdList") List<Long> messageIdList);

    List<SystemMsg> getPageSystemMsg();
}
