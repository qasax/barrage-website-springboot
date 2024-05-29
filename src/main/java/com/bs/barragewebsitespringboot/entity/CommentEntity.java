package com.bs.barragewebsitespringboot.entity;

import com.bs.barragewebsitespringboot.pojo.Comment;
import com.bs.barragewebsitespringboot.pojo.CommentReplies;
import com.bs.barragewebsitespringboot.pojo.User;
import com.bs.barragewebsitespringboot.pojo.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentEntity extends Comment {
    private List<CommentRepliesEntity> commentRepliesEntities;
    private User senderUserDetail;
    private Video video;
}
