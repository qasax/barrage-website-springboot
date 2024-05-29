package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.UserFollowDAO;
import com.bs.barragewebsitespringboot.entity.LikeEntity;
import com.bs.barragewebsitespringboot.entity.UserFollowEntity;
import com.bs.barragewebsitespringboot.entity.VideoEntity;
import com.bs.barragewebsitespringboot.pojo.Collection;
import com.bs.barragewebsitespringboot.pojo.UserFollow;
import com.bs.barragewebsitespringboot.service.UserFollowService;
import com.bs.barragewebsitespringboot.service.UserService;
import com.bs.barragewebsitespringboot.service.VideoService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowDAO, UserFollow> implements UserFollowService {
    @Lazy//解决与UserFollowServiceImpl循环依赖的问题
    @Autowired
    UserFollowDAO userFollowDAO;
    @Autowired
    UserService userService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Lazy
    @Autowired
    VideoService videoService;
    @Override
    public String getFollowByUserId(HttpServletRequest httpServletRequest) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("follower_user_id",httpServletRequest.getAttribute("userId"));
        List<UserFollow> userFollow=userFollowDAO.selectList(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",userFollow);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getFans(HttpServletRequest httpServletRequest) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("following_user_id",httpServletRequest.getAttribute("userId"));
        List<UserFollow> userFollowList=userFollowDAO.selectList(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",userFollowList);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageFollowByUserId(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<UserFollowEntity> list=userFollowDAO.getPageFollowByUserId((Integer) httpServletRequest.getAttribute("userId"));
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
    @Override
    public String getPageFans(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<UserFollowEntity> list=userFollowDAO.getPageFans((Integer) httpServletRequest.getAttribute("userId"));
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
    @Override
    public String getUserPageFollow(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder, int userId) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<UserFollowEntity> list=userFollowDAO.getPageFollowByUserId(userId);
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getUserPageFans(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder, int userId) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<UserFollowEntity> list=userFollowDAO.getPageFans((userId));
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getLatelyMsg(HttpServletRequest httpServletRequest, long max, int offset) throws Exception {
        //获取当前用户id
        int userId= (int) httpServletRequest.getAttribute("userId");
        String key="feed"+userId;
        //redis中获取当前用户被投喂的 video信息
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet=stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key,0,max,offset,3);
        Map map=new HashMap<>();

        if(typedTupleSet==null||typedTupleSet.isEmpty()){
            List list=new ArrayList<>();
            map.put("code","error");
            map.put("data",list);
            map.put("offset",0);
            map.put("lastTime",0);//停止查询-- 没有小于零的结果
            return MapperUtils.obj2json(map);
        }
        //滚动分页查询当前用户需要获取的 videoId列表
        List<Integer> videoIdList=new ArrayList<>(typedTupleSet.size());
        long minTime=0;
        int os=1;
        for (ZSetOperations.TypedTuple<String> tuple : typedTupleSet) {
            videoIdList.add(Integer.valueOf(tuple.getValue()));
            long time=tuple.getScore().longValue();
            if(time==minTime){
                os++;
            }else {
                minTime=time;
                os=1;
            }
            minTime=tuple.getScore().longValue();
        }
        //根据videoId查询用户需要获取的数据-- 需要保证按照时间顺序获取，最上面的数据的时间为最新的。
        QueryWrapper queryWrapper=new QueryWrapper<>();
        List<VideoEntity> videoEntityList=videoService.getVideoByIds(videoIdList);
        map.put("code","success");
        map.put("data",videoEntityList);
        map.put("lastTime",minTime);
        map.put("offset",os);
        return MapperUtils.obj2json(map);
    }
    @Override
    public String addFollow(HttpServletRequest httpServletRequest, UserFollow userFollow) throws Exception {
        userFollow.setFollowerUserId((Integer) httpServletRequest.getAttribute("userId"));
        userFollow.setFollowDate(new Timestamp(new Date().getTime()));
        userFollowDAO.insert(userFollow);
        userService.fanPlus(userFollow.getFollowingUserId());//给被关注用户增加粉丝量
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",userFollow);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String deleteFollow(HttpServletRequest httpServletRequest, UserFollow userFollow) throws Exception {
        userFollow.setFollowerUserId((Integer) httpServletRequest.getAttribute("userId"));;
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("follower_user_id",userFollow.getFollowerUserId());
        queryWrapper.eq("following_user_id",userFollow.getFollowingUserId());
        userFollowDAO.delete(queryWrapper);
        userService.fanSub(userFollow.getFollowingUserId());//给被取消关注用户 减少粉丝量
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",userFollow);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String isFollowUser(HttpServletRequest httpServletRequest, String followingUserId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("following_user_id",followingUserId);
        queryWrapper.eq("follower_user_id",httpServletRequest.getAttribute("userId"));
        UserFollow userFollow=userFollowDAO.selectOne(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        if(userFollow!=null){
            map.put("data","true");
        }else {
            map.put("data","false");
        }
        return MapperUtils.obj2json(map);
    }



}
