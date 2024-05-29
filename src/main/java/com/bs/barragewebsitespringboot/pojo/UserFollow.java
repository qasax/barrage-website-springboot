package com.bs.barragewebsitespringboot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.sql.Timestamp;
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@TableName("user_follows")
public class UserFollow {
    @TableId(type = IdType.AUTO)
    private Integer followId;
    private Integer followerUserId;
    private Integer followingUserId;
    private Timestamp followDate;
}
