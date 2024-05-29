package com.bs.barragewebsitespringboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bs.barragewebsitespringboot.config.NonStaticResourceHttpRequestHandler;
import com.bs.barragewebsitespringboot.constant.DPlayerConstants;
import com.bs.barragewebsitespringboot.pojo.User;
import com.bs.barragewebsitespringboot.pojo.UserInfo;
import com.bs.barragewebsitespringboot.service.UserService;
import com.bs.barragewebsitespringboot.utils.FileUtils;
import com.bs.barragewebsitespringboot.utils.JwtUtils;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    public UserService userService;
    @Value("${value.avatarBaseURL}")
    String avatarBaseURL;
    @Autowired
    NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/list")
    public String getList() throws Exception {
        Map map=new HashMap<>();
        map.put("code","success");
        map.put("data",userService.list());
        return MapperUtils.obj2json(map);
    }

    /**
     * 分页查询-用户列表
     * @param pageSize
     * @param pageNo
     * @param orderColumn
     * @param sortOrder
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("getPageUserListAdmin")
    public String getPageUserList( int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        return  userService.getPageUserListAdmin(pageSize,pageNo,orderColumn,sortOrder);
    }

    /**
     * 修改用户角色
     * @param
     * @param
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping("setUserRoleAdmin")
    public String setUserRoleAdmin( String userId,String role) throws Exception {
        return  userService.setUserRoleAdmin(userId,role);
    }
    /**
     * 修改用户普通信息
     * @param
     * @param
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PutMapping("updateUserInfoAdmin")
    public String updateUserInfoAdmin( @RequestBody User user) throws Exception {
        return  userService.updateUserInfoAdmin(user);
    }
    /**
     * 删除用户
     * @param
     * @param
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @DeleteMapping("deleteUser")
    public String deleteUser( @RequestBody User user) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getUserId());
        userService.remove(queryWrapper);
        Map map=new HashMap<>();
        map.put("code", "success");
        map.put("data","success");
        return  MapperUtils.obj2json(map);
    }







    //头像上传
    /**
     * 用户头像的上传
     *
     * @return
     */
    @PostMapping("/upload/avatar")
    @ResponseBody
    public String uploadAvatar(@RequestParam MultipartFile file, HttpServletRequest request,String df) throws Exception {
        if (df==null || df.equals("")){
            String avatarName = request.getAttribute("userId")+ "_" +request.getAttribute("uname") + ".jpg";
            String avatarFilePath = avatarBaseURL + avatarName;
            FileUtils.upFile(avatarFilePath,file);
            UpdateWrapper updateWrapper=new UpdateWrapper();
            updateWrapper.eq("user_id",request.getAttribute("userId"));
            updateWrapper.set("avatar_url",avatarName);
            userService.update(updateWrapper);
        }else{
            UpdateWrapper updateWrapper=new UpdateWrapper();
            updateWrapper.eq("user_id",request.getAttribute("userId"));
            updateWrapper.set("avatar_url",df);
            userService.update(updateWrapper);
        }
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    //返回前台头像
    /**
     * 向前台返回对应用户的头像
     *
     * @return
     */
    @GetMapping(value = "/getAvatar", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public void getAvatar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",request.getAttribute("userId"));
        User user=userService.getOne(queryWrapper);
        //return FileUtils.returnFile(avatarBaseURL+ user.getAvatarUrl());
        request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, avatarBaseURL+user.getAvatarUrl());
        nonStaticResourceHttpRequestHandler.handleRequest(request, response);
    }

    /**
     * 获取指定用户头像
     * @param userId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getAvatarByUserId")
    @ResponseBody
    public void getAvatarByUserId(HttpServletRequest request, HttpServletResponse response,@RequestParam int userId) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        User user=userService.getOne(queryWrapper);
        request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, avatarBaseURL+user.getAvatarUrl());
        nonStaticResourceHttpRequestHandler.handleRequest(request, response);
       // return FileUtils.returnFile(avatarBaseURL+ user.getAvatarUrl());
    }
    /**
     * 获取制定用户个人信息
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("getUserInfoByUserId")
    @ResponseBody
    public String getUserInfoByUserId(int userId ) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        User user= userService.getOne(queryWrapper);
        user.setPassword(null);
        user.setEmail(null);
        user.setRole(null);
        Map map=new HashMap<>();
        map.put("code", "success");
        map.put("data",user);
        return MapperUtils.obj2json(map);
    }
    /**
     * 获取用户个人信息
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("getUserInfo")
    @ResponseBody
    public String getUserInfo(HttpServletRequest request) throws Exception {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",request.getAttribute("userId"));
        User user= userService.getOne(queryWrapper);
        Map map=new HashMap<>();
        map.put("code", "success");
        map.put("data",user);
        return MapperUtils.obj2json(map);
    }

    /**
     * 更新用户个人信息
     * @param request
     * @param
     * @return
     * @throws Exception
     */
    @PutMapping("updateUserInfo")
    @ResponseBody
    public String updateUserInfo(HttpServletRequest request,@RequestBody User user ) throws Exception {
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("user_id",request.getAttribute("userId"));
        updateWrapper.set("email",user.getEmail());
        updateWrapper.set("gender",user.getGender());
        updateWrapper.set("birthday",user.getBirthday());
        updateWrapper.set("signature",user.getSignature());
        userService.update(updateWrapper);
        Map map=new HashMap();
        map.put("code","success");
        return MapperUtils.obj2json(map);
    }

    /**
     * 获取用户粉丝
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getFans")
    public String getFans(HttpServletRequest httpServletRequest) throws Exception {
        return userService.getFans(httpServletRequest);
    }
    /**
     * 获取用户未读消息数
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("getUnReadMsg")
    public String getUnReadMsg(HttpServletRequest httpServletRequest) throws Exception {
        String msg="noMsg";
    if(stringRedisTemplate.opsForHash().get("unreadMessages",String.valueOf(httpServletRequest.getAttribute("userId")))!=null){
        msg= (String) stringRedisTemplate.opsForHash().get("unreadMessages",String.valueOf(httpServletRequest.getAttribute("userId")));
    }
    Map map=new HashMap<>();
    map.put("code","success");
    map.put("data",msg);
    return  MapperUtils.obj2json(map);
    }
    /**
     * 用户已读消息操作
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("setReadMsg")
    public String setReadMsg(HttpServletRequest httpServletRequest) throws Exception {
        stringRedisTemplate.opsForHash().delete("unreadMessages",String.valueOf(httpServletRequest.getAttribute("userId")));
        Map map=new HashMap<>();
        map.put("code","success");
        map.put("data","success");
        return  MapperUtils.obj2json(map);
    }

    /**
     * 获取未读的系统消息数
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("/getUnreadSystemMsgCount")
    public String getUnreadSystemMsgCount(HttpServletRequest httpServletRequest) throws Exception {
        // 获取当前用户ID
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        String key = "unreadSystemMsg"; // Redis中存储未读系统消息计数的Key
        String userKey = String.valueOf(userId); // 根据用户ID构建特定用户的Key

        // 从Redis中获取未读消息数
        Object unreadMsgCount = stringRedisTemplate.opsForHash().get(key, userKey);
        String msg = unreadMsgCount != null ? unreadMsgCount.toString() : "0"; // 如果未读消息数为空，则默认为0

        // 构建响应
        Map<String, String> map = new HashMap<>();
        map.put("code", "success");
        map.put("data", msg);

        return MapperUtils.obj2json(map);
    }

    /**
     * 用户已读系统消息
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("/markSystemMsgAsRead")
    public String markSystemMsgAsRead(HttpServletRequest httpServletRequest) throws Exception {
        // 获取当前用户ID
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        String key = "unreadSystemMsg"; // Redis中存储未读系统消息计数的Key
        String userKey = String.valueOf(userId); // 根据用户ID构建特定用户的Key

        // 从Redis中删除指定用户的未读系统消息计数，即标记所有消息为已读
        stringRedisTemplate.opsForHash().delete(key, userKey);

        // 构建响应
        Map<String, String> map = new HashMap<>();
        map.put("code", "success");
        map.put("data", "All messages marked as read");

        return MapperUtils.obj2json(map);
    }
    /**
     * 获取未读的回复我的消息数
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("/getReplyToMeMsgCount")
    public String getReplyToMeMsgCount(HttpServletRequest httpServletRequest) throws Exception {
        // 获取当前用户ID
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        String key = "unreadReplyToMeMsg"; // Redis中存储未读系统消息计数的Key
        String userKey = String.valueOf(userId); // 根据用户ID构建特定用户的Key

        // 从Redis中获取未读消息数
        Object unreadMsgCount = stringRedisTemplate.opsForHash().get(key, userKey);
        String msg = unreadMsgCount != null ? unreadMsgCount.toString() : "0"; // 如果未读消息数为空，则默认为0

        // 构建响应
        Map<String, String> map = new HashMap<>();
        map.put("code", "success");
        map.put("data", msg);

        return MapperUtils.obj2json(map);
    }

    /**
     * 用户已读回复我的消息
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @GetMapping("/markReplyToMeAsRead")
    public String markReplyToMeAsRead(HttpServletRequest httpServletRequest) throws Exception {
        // 获取当前用户ID
        Integer userId = (Integer) httpServletRequest.getAttribute("userId");
        String key = "unreadReplyToMeMsg"; // Redis中存储未读系统消息计数的Key
        String userKey = String.valueOf(userId); // 根据用户ID构建特定用户的Key

        // 从Redis中删除指定用户的未读系统消息计数，即标记所有消息为已读
        stringRedisTemplate.opsForHash().delete(key, userKey);

        // 构建响应
        Map<String, String> map = new HashMap<>();
        map.put("code", "success");
        map.put("data", "All messages marked as read");

        return MapperUtils.obj2json(map);
    }
}
