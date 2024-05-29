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
@TableName("collections")
public class Collection {
    @TableId(type = IdType.AUTO)
    private Integer collectionId;
    private Integer userId;
    private String collectionName;
    private String description;
    private Integer videoId;
    private Timestamp collectionDate;

}
