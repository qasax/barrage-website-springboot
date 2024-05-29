package com.bs.barragewebsitespringboot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String username;
    private String password;
    private String role;
    private String email;
    private String gender;
    private Date birthday;
    private String avatarUrl;
    private String signature;
    private Integer userLevel;
    private Integer experience;
    private Integer followCounts;
    private Integer fans;
    public User(int userId, String username,String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password=password;
        this.role = role;
    }
}
