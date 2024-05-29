package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.entity.VideoEntity;
import com.bs.barragewebsitespringboot.pojo.Video;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
public interface VideoService extends IService<Video> {
   public List<Video> getAllVideo();
   public String uploadVideo(MultipartFile file, HttpServletRequest request,String VideoBaseURL,String videoPicBaseURL) throws FileNotFoundException;
   String uploadVideoDetail(HttpServletRequest request, Video video, String videoBaseURL, String videoPicBaseURL) throws Exception;
   public String uploadVideoPic(MultipartFile file, HttpServletRequest request,String VideoPicBaseURL,Video video) throws FileNotFoundException;
   public String getPageVideoAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;
   public String viewsPlus(int videoId) throws Exception;
   public String likePlus(int videoId) throws Exception;
   public String commentPlus(int videoId) throws Exception;
   public String barragePlus(int videoId) throws Exception;
   public String favoritePlus(int videoId) throws Exception;
   public String sharesPlus(int videoId) throws Exception;
   public String commentSub(int videoId) throws Exception;
   public String barrageSub(int videoId) throws Exception;
   @Transactional
   String likeSub(int videoId) throws Exception;

   @Transactional
   String favoriteSub(int videoId) throws Exception;

   String getPageVideoByType(int pageSize, int pageNo, String orderColumn, String sortOrder, String type) throws Exception;


   List<VideoEntity> getVideoByIds(List<Integer> videoIdList);

   String getPageVideoBySearch(int pageSize, int pageNo, String orderColumn, String sortOrder, String keyword) throws Exception;

   String getVideoDetail(int videoId) throws Exception;

   String getPageVideoByUserId(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;

   String getUserPageVideo(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder, int userId) throws Exception;
}
