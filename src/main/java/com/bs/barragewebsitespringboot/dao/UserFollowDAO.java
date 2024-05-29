package com.bs.barragewebsitespringboot.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bs.barragewebsitespringboot.entity.UserFollowEntity;
import com.bs.barragewebsitespringboot.pojo.UserFollow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowDAO extends BaseMapper<UserFollow> {

     List<UserFollowEntity> getPageFollowByUserId(@Param("userId")Integer id);

     List<UserFollowEntity> getPageFans(Integer id);
}
