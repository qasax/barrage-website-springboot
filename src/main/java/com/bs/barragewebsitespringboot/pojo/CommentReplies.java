package com.bs.barragewebsitespringboot.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("Comment_replies")
public class CommentReplies {
    @TableId(type = IdType.AUTO)
    private Integer replyId;
    private Integer commentId;
    private Integer fromUserId;
    private Integer toUserId;
    private String content;
    private Timestamp timestamp;
    private Integer likes;
}
