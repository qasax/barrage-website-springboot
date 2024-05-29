package com.bs.barragewebsitespringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bs.barragewebsitespringboot.config.NonStaticResourceHttpRequestHandler;
import com.bs.barragewebsitespringboot.pojo.SystemMsg;
import com.bs.barragewebsitespringboot.pojo.User;
import com.bs.barragewebsitespringboot.pojo.UserFollow;
import com.bs.barragewebsitespringboot.service.SystemMsgService;
import com.bs.barragewebsitespringboot.service.UserService;
import com.bs.barragewebsitespringboot.utils.FileUtils;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("website")
public class WebsiteController {
    @Autowired
    NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Value("${value.carouseBaselUrl}")
    String carouseBaselUrl;
    @Autowired
    SystemMsgService systemMsgService;
    @Autowired
    UserService userService;
    /**
     * 添加走马灯图片
     * @param file
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("uploadCarousel")
    public  String uploadCarousel(@RequestParam MultipartFile file) throws Exception {
        String extensionName=null;
        String filename = file.getOriginalFilename();
        if (filename != null && filename.contains(".")) {
            extensionName=filename.substring(filename.lastIndexOf("."));
        }
        String carouseFileName ="carouse_"+System.currentTimeMillis()+extensionName ;//生成随机名字，防止重复
        String path=carouseBaselUrl+carouseFileName;
        FileUtils.upFile(path,file);
        stringRedisTemplate.opsForList().leftPush("carouse",carouseFileName);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }

    /**
     * 删除指定走马灯图片
     * @param carouseFileName
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("deleteCarousel")
    public  String deleteCarousel(String carouseFileName) throws Exception {
        stringRedisTemplate.opsForList().remove("carouse",1,carouseFileName);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }
    /**
     * 分页获取系统消息
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("getPageSystemMsg")
    public String getPageSystemMsg(int pageSize, int pageNo, String orderColumn, String sortOrder, String keyword) throws Exception {
        return systemMsgService.getPageSystemMsg(pageSize,pageNo,orderColumn,sortOrder);
    }
    /**
     * 发送系统消息
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("publishSystemMsg")
    public  String publishSystemMsg(@RequestBody SystemMsg systemMsg) throws Exception {
        systemMsg.setTimestamp(new Timestamp(new Date().getTime()));
        systemMsgService.save(systemMsg);
        //将数据推送到粉丝的收件箱
        List<User> userList=userService.list();
        for(User user:userList){
            int userId = user.getUserId();
            String key="systemMsg"+userId;
            stringRedisTemplate.opsForZSet().add(key, String.valueOf(systemMsg.getMessageId()),System.currentTimeMillis());
            stringRedisTemplate.opsForHash().increment("unreadSystemMsg",String.valueOf(userId),1);
        }
        Map map=new HashMap();
        map.put("code","success");
        map.put("data","success");
        return MapperUtils.obj2json(map);
    }
    /**
     * 删除系统消息
     * @param messageId 要删除的消息ID
     * @return 操作结果
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("deleteSystemMsg")
    public String deleteSystemMsg(int messageId) throws Exception {
        // 从数据库中删除消息
        boolean success = systemMsgService.removeById(messageId);
        if (!success) {
            throw new Exception("删除消息失败");
        }

        // 获取所有用户列表
        List<User> userList = userService.list();
        for (User user : userList) {
            int userId = user.getUserId();
            String key = "systemMsg" + userId;

            // 从每个用户的收件箱中移除该消息
            stringRedisTemplate.opsForZSet().remove(key, String.valueOf(messageId));

            // 如果该消息是未读消息，则减少未读消息计数
            // 这里假设我们有一个方法来检查消息是否未读，根据你的实际情况可能需要调整
                stringRedisTemplate.opsForHash().increment("unreadSystemMsg", String.valueOf(userId), -1);

        }

        // 返回成功响应
        Map<String, String> map = new HashMap<>();
        map.put("code", "success");
        map.put("data", "Message deleted successfully.");
        return MapperUtils.obj2json(map);
    }


    /**
     * 分页获取系统消息
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("updateSystemMsg")
    public String updateSystemMsg(@RequestBody SystemMsg systemMsg) throws Exception {
        UpdateWrapper updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("message_id",systemMsg.getMessageId());
        systemMsgService.update(systemMsg,updateWrapper);
        Map<String, String> map = new HashMap<>();
        map.put("code", "success");
        map.put("data", "Message update successfully.");
        return MapperUtils.obj2json(map);
    }


    /**
     * 滚动分页获取系统消息
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getSystemMsg")
    public String getLatelyMsg(HttpServletRequest httpServletRequest, long max,@RequestParam(value = "offset",defaultValue = "0") int offset) throws Exception {
        return systemMsgService.getSystemMsg(httpServletRequest,max,offset);
    }

    /**
     * 获取全部走马灯 文件名称
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("getCarouselFileName")
    public  String getCarouselFileName() throws Exception {
        List<String> carouseFileNameList = stringRedisTemplate.opsForList().range("carouse", 0, -1);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",carouseFileNameList);
        return MapperUtils.obj2json(map);

    }

    /**
     * 根据名称 获取对应的走马灯文件
     * @param request
     * @param response
     * @param carouseFileName
     * @throws Exception
     */
    @GetMapping("getCarouselImg")
    public  void getCarouselImg(HttpServletRequest request, HttpServletResponse response ,String carouseFileName) throws Exception {
        request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, carouseBaselUrl+carouseFileName);
        nonStaticResourceHttpRequestHandler.handleRequest(request, response);
    }
}
