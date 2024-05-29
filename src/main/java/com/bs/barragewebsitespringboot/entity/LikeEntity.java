package com.bs.barragewebsitespringboot.entity;

import com.bs.barragewebsitespringboot.pojo.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikeEntity extends Like {
    private VideoEntity videoEntity;
    private CommentEntity commentEntity;
    private CommentRepliesEntity commentRepliesEntity;
}
