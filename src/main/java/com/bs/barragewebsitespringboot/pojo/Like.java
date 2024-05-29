package com.bs.barragewebsitespringboot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.bytedeco.flycapture.FlyCapture2.TimeStamp;

import java.sql.Timestamp;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@TableName("likes")
public class Like {
    @TableId(type = IdType.AUTO)
    private Integer likeId;
    private Integer userId;
    private String actionObject;
    private Integer actionObjectId;
    private Timestamp likeDate;
}
