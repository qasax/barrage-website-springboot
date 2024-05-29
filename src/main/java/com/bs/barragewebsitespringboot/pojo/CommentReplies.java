package com.bs.barragewebsitespringboot.pojo;

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
@TableName("comment_replies")
public class comment_replies {
    private int replyId;
    private int commentId;
    private int fromUserId;
    private int toUserId;
    private String content;
    private Timestamp timestamp;
}
