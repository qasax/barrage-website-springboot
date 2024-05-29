package com.bs.barragewebsitespringboot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.sql.Date;
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserInfo {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String username;
    private String password;
    private String role;
    private boolean enabled;
    private String email;
    private String gender;
    private String birthday;
    private String avatarUrl;
    private String signature;
    private Integer userLevel;
    private Integer experience;
    private Integer fans;
}
