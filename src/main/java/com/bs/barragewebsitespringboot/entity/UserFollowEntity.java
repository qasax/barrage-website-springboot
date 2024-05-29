package com.bs.barragewebsitespringboot.entity;

import com.bs.barragewebsitespringboot.pojo.User;
import com.bs.barragewebsitespringboot.pojo.UserFollow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFollowEntity extends UserFollow {
    private User user;
}
