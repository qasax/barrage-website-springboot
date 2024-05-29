package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.VideoDAO;
import com.bs.barragewebsitespringboot.entity.VideoEntity;
import com.bs.barragewebsitespringboot.pojo.UserFollow;
import com.bs.barragewebsitespringboot.pojo.Video;
import com.bs.barragewebsitespringboot.service.UserFollowService;
import com.bs.barragewebsitespringboot.service.VideoService;
import com.bs.barragewebsitespringboot.utils.AISubtitlesUtils;
import com.bs.barragewebsitespringboot.utils.FileUtils;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import com.bs.barragewebsitespringboot.utils.VideoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoDAO, Video> implements VideoService {
    @Autowired
    VideoDAO videoDAO;
    @Autowired
    AISubtitlesUtils aiSubtitlesUtils;
    @Value("${value.videoSubURL}")
    String videoSubURL;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserFollowService userFollowService;
    public String getPageVideoAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<Video> list = videoDAO.getPageVideoAdmin();
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
    @Override
    public String uploadVideo(MultipartFile file, HttpServletRequest request,String videoBaseURL,String videoPicBaseURL) throws FileNotFoundException {
        //获取文件拓展名
        String extensionName=null;
        String filename = file.getOriginalFilename();
        if (filename != null && filename.contains(".")) {
             extensionName=filename.substring(filename.lastIndexOf("."));
        }
        String videoFileName = request.getAttribute("userId")+ "_" +request.getAttribute("uname") +"_"+System.currentTimeMillis() ;//生成随机名字，防止重复
        String videoName = videoFileName+ extensionName;//视频名称
        String subtitleName= videoFileName+".vtt";
        String videoPicName = videoFileName+".jpg";//视频封面名称
        String videoFilePath = videoBaseURL +videoName ;//视频绝对路径
        String finalExtensionName = extensionName;
        Runnable runnable= () -> {
            try {
                FileUtils.upFile(videoFilePath,file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            VideoUtils.randomGrabberFFmpegImage(new File(videoFilePath),videoPicBaseURL,videoPicName);;//生成随机视频封面
            //生成srt字幕文件
            try {
                aiSubtitlesUtils.aiSubtitle(videoFileName, finalExtensionName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
        Map detail=new HashMap<>();
        detail.put("videoUrl",videoName);
        detail.put("subtitleUrl",subtitleName);
        detail.put("coverImageUrl",videoPicName);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",detail);
        try {
            return MapperUtils.obj2json(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadVideoDetail(HttpServletRequest request, Video video, String videoBaseURL, String videoPicBaseURL) throws Exception {
        //将视频相关信息插入到数据库
        video.setUploaderUserId((Integer) request.getAttribute("userId"));
        video.setUploadDate(new Timestamp(new Date().getTime()));
        videoDAO.uploadVideo(video);
        //将数据推送到粉丝的收件箱
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("following_user_id",request.getAttribute("userId"));
        List<UserFollow> fansList=userFollowService.list(queryWrapper);
        for(UserFollow fan:fansList){
            int userId = fan.getFollowerUserId();
            String key="feed"+userId;
            stringRedisTemplate.opsForZSet().add(key, String.valueOf(video.getVideoId()),System.currentTimeMillis());
            stringRedisTemplate.opsForHash().increment("unreadMessages",String.valueOf(userId),1);
        }
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String uploadVideoPic(MultipartFile file, HttpServletRequest request,String VideoPicBaseURL,Video video) throws FileNotFoundException {
        String videoURL=video.getVideoUrl();
        String videoPicURL = videoURL.substring(0, videoURL.length() - 4)+".jpg";//设置视频封面的文件名
        String videoPicFilePath = VideoPicBaseURL+ videoPicURL ;
        FileUtils.upFile(videoPicFilePath,file);
        video.setCoverImageUrl(videoPicURL);//准备在数据库中修改视频封面的路径
        videoDAO.uploadVideoPic(video);
        return "ok";
    }
    @Override
    @Transactional
    public String viewsPlus(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("views",video.getViews()+1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    @Transactional
    public String likePlus(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("likes",video.getLikes()+1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String commentPlus(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("comments",video.getLikes()+1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String barragePlus(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("barrages",video.getBarrages()+1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    @Transactional
    public String favoritePlus(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("favorites",video.getFavorites()+1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    @Transactional
    public String sharesPlus(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("shares",video.getShares()+1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String commentSub(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("comments",video.getLikes()-1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String barrageSub(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("barrages",video.getBarrages()-1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageVideoByType(int pageSize, int pageNo, String orderColumn, String sortOrder, String type) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<VideoEntity> list=videoDAO.getPageVideoByType(type);
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
    @Override
    public String getPageVideoBySearch(int pageSize, int pageNo, String orderColumn, String sortOrder, String keyword) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<VideoEntity> list=videoDAO.getPageVideoBySearch(keyword);
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getVideoDetail(int videoId) throws Exception {
        Video video=videoDAO.getVideoDetail(videoId);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",video);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getPageVideoByUserId(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<VideoEntity> list=videoDAO.getPageVideoByUserId(request.getAttribute("userId"));
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public String getUserPageVideo(HttpServletRequest request, int pageSize, int pageNo, String orderColumn, String sortOrder, int userId) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<VideoEntity> list=videoDAO.getPageVideoByUserId(userId);
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }

    @Override
    public List<VideoEntity> getVideoByIds(List<Integer> videoIdList) {
        return videoDAO.getVideoByIds(videoIdList);
    }


    @Override
    @Transactional
    public String likeSub(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("likes",video.getLikes()-1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    @Override
    @Transactional
    public String favoriteSub(int videoId) throws Exception {
        Video video=videoDAO.selectById(videoId);
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("video_id",videoId);
        updateWrapper.set("favorites",video.getFavorites()-1);
        videoDAO.update(null,updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }
    @Override
    @Transactional
    public List<Video> getAllVideo() {
        return null;
    }

}
