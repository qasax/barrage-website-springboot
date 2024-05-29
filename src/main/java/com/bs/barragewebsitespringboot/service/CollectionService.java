package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.pojo.Collection;
import com.bs.barragewebsitespringboot.pojo.Video;
import jakarta.servlet.http.HttpServletRequest;

public interface CollectionService extends IService<Collection> {
    public String addCollectionByUserId(HttpServletRequest httpServletRequest,Collection collection) throws Exception;
    public  String getPageCollectionByUserId(HttpServletRequest httpServletRequest,int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

    public String getCollectionDetail(HttpServletRequest httpServletRequest, Collection collection) throws Exception;

    public String deleteCollection(HttpServletRequest httpServletRequest, Collection collection) throws Exception;

    public String deleteCollectionByVideoId(HttpServletRequest httpServletRequest, Collection collection) throws Exception;

    String getPageCollectionDetail(HttpServletRequest httpServletRequest, int pageSize, int pageNo, String orderColumn, String sortOrder,String collectionName) throws Exception;

    String isCollectVideo(HttpServletRequest httpServletRequest, String videoId) throws Exception;
}
