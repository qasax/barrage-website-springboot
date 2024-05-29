package com.bs.barragewebsitespringboot.entity;

import com.bs.barragewebsitespringboot.pojo.Collection;
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
public class CollectionEntity extends Collection {
    private VideoEntity videoEntity;
}
