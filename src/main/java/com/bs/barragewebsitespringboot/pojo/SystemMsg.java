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
@TableName("system_msg")
public class SystemMsg {
    @TableId(type = IdType.AUTO)
    private Integer messageId;
    private String title;
    private String content;
    private Timestamp timestamp;
}
