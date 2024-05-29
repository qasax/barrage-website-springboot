package com.bs.barragewebsitespringboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bs.barragewebsitespringboot.entity.UserFollowEntity;
import com.bs.barragewebsitespringboot.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDAO extends BaseMapper<User> {

    List<User> getPageUserListAdmin();
}
