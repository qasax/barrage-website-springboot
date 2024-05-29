package com.bs.barragewebsitespringboot.entity;

import com.bs.barragewebsitespringboot.pojo.Comment;
import com.bs.barragewebsitespringboot.pojo.CommentReplies;
import com.bs.barragewebsitespringboot.pojo.User;
import com.bs.barragewebsitespringboot.pojo.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentRepliesEntity extends CommentReplies {
    private User fromUserDetail;
    private User toUserDetail;
    private Comment comment;
    private Video video;
}
