package com.bs.barragewebsitespringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bs.barragewebsitespringboot.config.NonStaticResourceHttpRequestHandler;
import com.bs.barragewebsitespringboot.pojo.Video;
import com.bs.barragewebsitespringboot.service.VideoService;
import com.bs.barragewebsitespringboot.service.impl.VideoServiceImpl;
import com.bs.barragewebsitespringboot.utils.FileUtils;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("video")
public class VideoController {
    @Autowired
    VideoService videoService;
    @Value("${value.videoBaseURL}")
    String videoBaseURL;
    @Value("${value.videoPicBaseURL}")
    String videoPicBaseURL;
    @Value("${value.videoSubURL}")
    String videoSubURL;
    @Autowired
    NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    /**
     * 分页查询视频信息 管理员使用
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @ResponseBody
    @GetMapping(value = "getPageVideoAdmin")
    public String getPageVideoAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
    return  videoService.getPageVideoAdmin(pageSize,pageNo,orderColumn,sortOrder);
    }

    /**
     * 更新视频信息
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @ResponseBody
    @PutMapping(value = "updateVideoInfoAdmin")
    public String updateVideoInfoAdmin(@RequestBody Video video) throws Exception {
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",video.getVideoId());
        videoService.update(video,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }
    /**
     * 删除视频
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @ResponseBody
    @DeleteMapping(value = "deleteVideoAdmin")
    public String deleteVideoAdmin(@RequestBody Video video) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("video_id",video.getVideoId());
        videoService.remove(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }



    /**
     * 分页查询视频表,基于视频类型
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "getPageVideoByType")
    public String getPageVideoByType( int pageSize, int pageNo, String orderColumn, String sortOrder, String type) throws Exception {
        return  videoService.getPageVideoByType(pageSize,pageNo,orderColumn,sortOrder,type);
    }
    /**
     * 分页查询视频表,基于搜索
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "getPageVideoBySearch")
    public String getPageVideoBySearch( int pageSize, int pageNo, String orderColumn, String sortOrder, String keyword) throws Exception {
        return  videoService.getPageVideoBySearch(pageSize,pageNo,orderColumn,sortOrder,keyword);
    }
    /**
     * 分页查询 某用户发布的视频
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "getPageVideoByUserId")
    public String getPageVideoByUserId(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        return  videoService.getPageVideoByUserId(request,pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 分页查询 获取指定用户Id发布的视频
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "getUserPageVideo")
    public String getUserPageVideo(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder,int userId) throws Exception {
        return  videoService.getUserPageVideo(request,pageSize,pageNo,orderColumn,sortOrder,userId);
    }
    /**
     * 删除 某用户发布的视频
     * @return
     * @throws Exception
     */
    @ResponseBody
    @DeleteMapping(value = "deleteVideoByUserId")
    public String deleteVideoByType(HttpServletRequest request, int videoId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("uploader_user_id",request.getAttribute("userId"));
        queryWrapper.eq("video_id",videoId);
        videoService.remove(queryWrapper);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","delete success");
        return  MapperUtils.obj2json(map);
    }
    /**
     * 获取指定视频的详细信息
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "getVideoDetail")
    public String getVideoDetail( int videoId) throws Exception {
        return  videoService.getVideoDetail(videoId);
    }

    /**
     * 用户上传视频
     * @param file
     * @param
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/video")
    @ResponseBody
    public String uploadVideo(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception{
        return videoService.uploadVideo(file,request,videoBaseURL,videoPicBaseURL);
    }
    /**
     * 用户提交 视频信息表单
     * @param
     * @param video
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/videoDetail")
    @ResponseBody
    public String videoDetail(@RequestBody Video video, HttpServletRequest request) throws Exception{
        return videoService.uploadVideoDetail(request,video,videoBaseURL,videoPicBaseURL);
    }
    /**
     * 获取视频文件
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getVideo")
    @ResponseBody
    public void getVideo(HttpServletRequest request,HttpServletResponse response ,  @RequestParam String videoUrl) throws Exception {
        Video video = new Video();
        video.setVideoUrl(videoUrl);
        //FileUtils.sendFileToResponse(videoBaseURL + video.getVideoUrl(),response);
       // return FileUtils.returnFile(videoBaseURL + video.getVideoUrl());
        request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, videoBaseURL+video.getVideoUrl());
        nonStaticResourceHttpRequestHandler.handleRequest(request, response);
    }

    /**
     * 上传视频封面
     * @param file
     * @param request
     * @param video
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/videoPic")
    @ResponseBody
    public String uploadVideoPic(@RequestParam MultipartFile file, HttpServletRequest request, Video video) throws Exception{
        videoService.uploadVideoPic(file, request,videoPicBaseURL,video);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    /**
     * 获取视频封面文件
     * @param request
     * @param video
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getVideoPic")
    @ResponseBody
    public void getVideoPic(HttpServletRequest request,HttpServletResponse response, Video video) throws Exception {

        //return FileUtils.returnFile(videoPicBaseURL + video.getCoverImageUrl());
        request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, videoPicBaseURL + video.getCoverImageUrl());
        nonStaticResourceHttpRequestHandler.handleRequest(request, response);
    }

    /**
     * 获取视频对应字幕文件
     * @param request
     * @param video
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getSubtitle")
    @ResponseBody
    public void getSubtitle(HttpServletRequest request ,HttpServletResponse response, Video video) throws Exception {
        request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, videoSubURL + video.getSubtitleUrl());
        nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        //return FileUtils.returnFile(videoSubURL + video.getSubtitleUrl());
    }
    /**
     * 获取视频对应字幕文件
     * @param request
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/viewsPlus")
    @ResponseBody
    public String viewsPlus(HttpServletRequest request ,HttpServletResponse response, String videoId) throws Exception {
    return videoService.viewsPlus(Integer.parseInt(videoId));
    }

}
