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
@TableName("barrages")
public class Barrage {
    @TableId(type = IdType.AUTO)
    private Integer barrageId;
    private Integer videoId;
    private Float time;
    private Integer type;//0流动弹幕 1顶部弹幕 2底部弹幕
    private Integer color;
    private Integer userId;
    private String text;
    public Barrage(int videoId, float time, int type, int color, int userId, String text) {
        this.videoId = videoId;
        this.time = time;
        this.type = type;
        this.color = color;
        this.userId = userId;
        this.text = text;
    }
}
