package com.bs.barragewebsitespringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bs.barragewebsitespringboot.pojo.Collection;
import com.bs.barragewebsitespringboot.service.CollectionService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("collection")
public class CollectionController {
    @Autowired
    CollectionService collectionService;

    /**
     * 分页获取用户收藏夹
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getPageCollectionByUserId")
    public String getPageCollectionByUserId(HttpServletRequest httpServletRequest,@RequestParam int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        return collectionService.getPageCollectionByUserId(httpServletRequest,pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 分页获取--用户某个收藏夹内详细内容
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getPageCollectionDetail")
    public String getPageCollectionDetail(HttpServletRequest httpServletRequest, int pageSize,int pageNo,String orderColumn,String sortOrder,String collectionName) throws Exception {
        return collectionService.getPageCollectionDetail(httpServletRequest,pageSize,pageNo,orderColumn,sortOrder,collectionName);
    }
    /**
     * 添加收藏及新建收藏夹
     * @param httpServletRequest
     * @param collection
     * @return
     * @throws Exception
     */
    @PostMapping("addCollectionByUserId")
    public String addCollectionByUserId(HttpServletRequest httpServletRequest, @RequestBody Collection collection ) throws Exception {
        return     collectionService.addCollectionByUserId(httpServletRequest,collection);
    }

    /**
     * 获取某用户某收藏夹内收藏内容
     * @param httpServletRequest
     * @param collection
     * @return
     */
    @RequestMapping("getCollectionDetail")
    public String getCollectionDetail(HttpServletRequest httpServletRequest, @RequestBody Collection collection ) throws Exception {
        return collectionService.getCollectionDetail(httpServletRequest,collection);
    }

    /**
     * 删除收藏夹及其全部内容
     * @param httpServletRequest
     * @param collection
     * @return
     * @throws Exception
     */
    @DeleteMapping("deleteCollection")
    public String deleteCollection(HttpServletRequest httpServletRequest, @RequestBody Collection collection ) throws Exception {
        return collectionService.deleteCollection(httpServletRequest,collection);
    }

    /**
     * 删除某收藏夹某个视频
     * @param httpServletRequest
     * @param collection
     * @return
     * @throws Exception
     */
    @DeleteMapping("deleteCollectionByVideoId")
    public String deleteCollectionByVideoId(HttpServletRequest httpServletRequest, @RequestBody Collection collection ) throws Exception {
        return collectionService.deleteCollectionByVideoId(httpServletRequest,collection);
    }

    /**
     * 获取用户是否收藏了该视频
     * @param videoId
     * @return
     */
    @GetMapping("isCollectVideo")
    public String isCollectVideo(HttpServletRequest httpServletRequest,String videoId) throws Exception {
        return collectionService.isCollectVideo(httpServletRequest,videoId);
    }
    }

