package com.bs.barragewebsitespringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bs.barragewebsitespringboot.constant.DPlayerConstants;
import com.bs.barragewebsitespringboot.pojo.Barrage;
import com.bs.barragewebsitespringboot.service.BarrageService;
import com.bs.barragewebsitespringboot.service.VideoService;
import com.bs.barragewebsitespringboot.service.impl.BarrageServiceImpl;
import com.bs.barragewebsitespringboot.utils.JwtUtils;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "barrage")
public class BarrageController {
    @Autowired
    BarrageService barrageService;
    /**
     * 分页查询弹幕信息 管理员使用
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @ResponseBody
    @GetMapping(value = "getPageBarrageAdmin")
    public String getPageBarrageAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        return  barrageService.getPageBarrageAdmin(pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 修改弹幕
     * @param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping(value = "updateBarrage")
    public String updateBarrage(@RequestBody  Barrage barrage) throws Exception {
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("barrage_id",barrage.getBarrageId());
        barrageService.update(barrage,updateWrapper);
        Map map=new HashMap();
        map.put("code", DPlayerConstants.DPLAYER_SUCCESS_CODE);
        map.put("data","update success");
        return MapperUtils.obj2json(map);
    }
    /**
     * 删除指定id弹幕
     * @param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping(value = "deleteBarrage")
    public String deleteBarrage(   int barrageId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("barrage_id",barrageId);
        barrageService.remove(queryWrapper);
        Map map=new HashMap();
        map.put("code", DPlayerConstants.DPLAYER_SUCCESS_CODE);
        map.put("data","delete success");
        return MapperUtils.obj2json(map);
    }

    /**
     * 获取弹幕库
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "v3/", method = RequestMethod.GET)
    public String getBarrage(@RequestParam String id) throws Exception {
        System.out.println(id);
        Map map = new HashMap();
        List<Barrage> data=barrageService.getBarrage(id);
        Iterator<Barrage> iterator=data.iterator();
        List<List<Object>> newData = new ArrayList<>();
        while (iterator.hasNext()){
            Barrage barrage=iterator.next();//"#"+Integer.toHexString(barrage.getColor())
            List<Object> list=List.of(barrage.getTime(),barrage.getType(),barrage.getColor(),barrage.getUserId(),barrage.getText());
            newData.add(list);
        }
        map.put("code", DPlayerConstants.DPLAYER_SUCCESS_CODE);
        map.put("data",newData);
        return MapperUtils.obj2json(map);
    }

    /**
     * 发送弹幕
     * @param param
     * @param request
     * @return
     * @throws Exception
     */
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    VideoService videoService;
    @ResponseBody
    @RequestMapping(value = "v3/", method = RequestMethod.POST)
    public String sendBarrage(@RequestBody Map<String,String> param, HttpServletRequest request) throws Exception {

        Map map = new HashMap();
        try {
            String id=(String) JwtUtils.getClaims(param.get("token")).get("userId");
            String redisToken= stringRedisTemplate.opsForValue().get(id);
            param.put("author",id);
            barrageService.sendBarrage(param);
            videoService.barragePlus(Integer.parseInt(param.get("id")));//param的id对应的是视频的videoId
            map.put("code",DPlayerConstants.DPLAYER_SUCCESS_CODE);
        } catch (Exception e){
            System.out.println("发送弹幕者为失效身份");
            map.put("code",-1);
        }
        map.put("data",param);
        return MapperUtils.obj2json(map);
    }
}
 