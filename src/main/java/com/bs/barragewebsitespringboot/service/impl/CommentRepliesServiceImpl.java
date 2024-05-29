package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.CommentRepliesDAO;
import com.bs.barragewebsitespringboot.entity.CommentRepliesEntity;
import com.bs.barragewebsitespringboot.pojo.CommentReplies;
import com.bs.barragewebsitespringboot.pojo.SystemMsg;
import com.bs.barragewebsitespringboot.service.CommentRepliesService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CommentRepliesServiceImpl extends ServiceImpl<CommentRepliesDAO, CommentReplies> implements CommentRepliesService {
    @Autowired
    CommentRepliesDAO commentRepliesDAO;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public String replyCommentByVideo(HttpServletRequest request, CommentReplies commentReplies) throws Exception {
        Timestamp timestamp=new Timestamp(new Date().getTime());
        commentReplies.setTimestamp(timestamp);
        commentReplies.setFromUserId((Integer) request.getAttribute("userId"));
        commentReplies.setLikes(0);
        commentRepliesDAO.insert(commentReplies);
        //放入被回复用户的收件箱
        int userId = commentReplies.getToUserId();
        String key="ReplyToMeMsg"+userId;
        stringRedisTemplate.opsForZSet().add(key, String.valueOf(commentReplies.getReplyId()),System.currentTimeMillis());
        stringRedisTemplate.opsForHash().increment("unreadReplyToMeMsg",String.valueOf(userId),1);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",commentReplies);
        return MapperUtils.obj2json(map);

    }
    @Override
    @Transactional
    public String likePlus(int replyId) throws Exception {
        CommentReplies commentReplies=commentRepliesDAO.selectById(replyId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("reply_id",commentReplies.getReplyId());
        updateWrapper.set("likes",commentReplies.getLikes()+1);
        commentRepliesDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }
    @Override
    @Transactional
    public String likeSub(int replyId) throws Exception {
        CommentReplies commentReplies=commentRepliesDAO.selectById(replyId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("reply_id",commentReplies.getReplyId());
        updateWrapper.set("likes",commentReplies.getLikes()-1);
        commentRepliesDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getReplyCommentAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<CommentReplies> list=commentRepliesDAO.getReplyCommentAdmin();
        PageInfo<CommentReplies> pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getReplyToMeMsg(HttpServletRequest httpServletRequest, long max, int offset) throws Exception {
        // 获取当前用户id
        int userId = (int) httpServletRequest.getAttribute("userId");
        String key = "ReplyToMeMsg" + userId;

        // 从Redis中获取当前用户的系统消息信息
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet = stringRedisTemplate
                .opsForZSet()
                .reverseRangeByScoreWithScores(key, 0, max, offset, 3); // 限定查询的消息数为3

        Map<String, Object> map = new HashMap<>();

        if (typedTupleSet == null || typedTupleSet.isEmpty()) {
            List<Object> list = new ArrayList<>();
            map.put("code", "error");
            map.put("data", list);
            map.put("offset", 0);
            map.put("lastTime", 0); // 停止查询—没有小于零的结果
            return MapperUtils.obj2json(map);
        }

        // 滚动分页查询当前用户需要获取的消息ID列表
        List<Long> messageIdList = new ArrayList<>(typedTupleSet.size());
        long minTime = 0;
        int os = 1;
        for (ZSetOperations.TypedTuple<String> tuple : typedTupleSet) {
            messageIdList.add(Long.valueOf(tuple.getValue()));
            long time = tuple.getScore().longValue();
            if (time == minTime) {
                os++;
            } else {
                minTime = time;
                os = 1;
            }
        }

        // 根据消息ID查询用户需要获取的数据—需要保证按照时间顺序获取，最上面的数据的时间为最新的。
        List<CommentRepliesEntity> replyToMeMsgList = commentRepliesDAO.getReplyToMeMsg(messageIdList); // 假设这是实现该查询的服务方法
        map.put("code", "success");
        map.put("data", replyToMeMsgList);
        map.put("lastTime", minTime);
        map.put("offset", os);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getReplyByUserId(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<CommentReplies> list=commentRepliesDAO.getReplyByUserId((Integer) request.getAttribute("userId"));
        PageInfo<CommentReplies> pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
}
