package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.SystemMsgDAO;
import com.bs.barragewebsitespringboot.dao.UserFollowDAO;
import com.bs.barragewebsitespringboot.entity.VideoEntity;
import com.bs.barragewebsitespringboot.pojo.SystemMsg;
import com.bs.barragewebsitespringboot.pojo.UserFollow;
import com.bs.barragewebsitespringboot.service.SystemMsgService;
import com.bs.barragewebsitespringboot.service.UserFollowService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SystemMsgServiceImpl extends ServiceImpl<SystemMsgDAO, SystemMsg> implements SystemMsgService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    SystemMsgDAO systemMsgDAO;

    /**
     * 获取用户的最近系统消息
     * @param httpServletRequest HTTP请求
     * @param max 最大时间戳（用于限定查询的消息）
     * @param offset 分页偏移量
     * @return JSON字符串形式的消息列表和分页信息
     * @throws Exception
     */
    @Override
    public String getSystemMsg(HttpServletRequest httpServletRequest, long max, int offset) throws Exception {
        // 获取当前用户id
        int userId = (int) httpServletRequest.getAttribute("userId");
        String key = "systemMsg" + userId;

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
        List<SystemMsg> systemMsgList = systemMsgDAO.getSystemMsgByIds(messageIdList); // 假设这是实现该查询的服务方法
        map.put("code", "success");
        map.put("data", systemMsgList);
        map.put("lastTime", minTime);
        map.put("offset", os);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageSystemMsg(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<SystemMsg> list=systemMsgDAO.getPageSystemMsg();
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
}
