package com.bs.barragewebsitespringboot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("videos")
public class Video {
    @TableId(type = IdType.AUTO)
    private Integer videoId;
    private String title;
    private String description;
    private Integer uploaderUserId;
    private Date uploadDate;
    private String videoUrl;
    private String subtitleUrl;
    private String coverImageUrl;
    private Integer views;
    private Integer comments;
    private Integer barrages;
    private Integer likes;
    private Integer favorites;
    private Integer shares;
    private String tags;
    private String category;
    private String type;
}
