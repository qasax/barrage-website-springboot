package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.CollectionDAO;
import com.bs.barragewebsitespringboot.entity.CollectionEntity;
import com.bs.barragewebsitespringboot.entity.LikeEntity;
import com.bs.barragewebsitespringboot.pojo.Collection;
import com.bs.barragewebsitespringboot.pojo.Like;
import com.bs.barragewebsitespringboot.service.CollectionService;
import com.bs.barragewebsitespringboot.service.VideoService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionDAO, Collection> implements CollectionService {
    @Autowired
    CollectionDAO collectionDAO;
    @Autowired
    VideoService videoService;
    public  String getPageCollectionByUserId(HttpServletRequest httpServletRequest,int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<String> collectionList=collectionDAO.getPageCollectionByUserId((Integer) httpServletRequest.getAttribute("userId"));
        PageInfo pageInfo=new PageInfo<>(collectionList);
        Map map=new HashMap<>();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
    public String addCollectionByUserId(HttpServletRequest httpServletRequest,Collection collection) throws Exception {
        collection.setUserId((Integer) httpServletRequest.getAttribute("userId"));
        collection.setCollectionDate(new Timestamp(new Date().getTime()));
        collectionDAO.insert(collection);
        videoService.favoritePlus(collection.getVideoId());
        Map map=new HashMap<>();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }
    public String getCollectionDetail(HttpServletRequest httpServletRequest, Collection collection) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        queryWrapper.eq("collection_name",collection.getCollectionName());
        List<Collection> collection1=collectionDAO.selectList(queryWrapper);
        Map map=new HashMap<>();
        map.put("code","success");
        map.put("data",collection1);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String deleteCollection(HttpServletRequest httpServletRequest, Collection collection) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        queryWrapper.eq("collection_name",collection.getCollectionName());
        collectionDAO.delete(queryWrapper);
        Map map=new HashMap<>();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String deleteCollectionByVideoId(HttpServletRequest httpServletRequest, Collection collection) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        queryWrapper.eq("video_id",collection.getVideoId());
        collectionDAO.delete(queryWrapper);
        videoService.favoriteSub(collection.getVideoId());
        Map map=new HashMap<>();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageCollectionDetail(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder,String collectionName) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<CollectionEntity> list=collectionDAO.getPageCollectionDetail((Integer) httpServletRequest.getAttribute("userId"),collectionName);
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String isCollectVideo(HttpServletRequest httpServletRequest, String videoId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId);
        queryWrapper.eq("user_id",httpServletRequest.getAttribute("userId"));
        Collection collection=collectionDAO.selectOne(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        if(collection!=null){
            map.put("data","true");
        }else {
            map.put("data","false");
        }
        return MapperUtils.obj2json(map);
    }

}
