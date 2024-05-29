package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.pojo.Barrage;

import java.util.List;
import java.util.Map;

public interface BarrageService  extends IService<Barrage>{
    public int sendBarrage(Map<String,String> param);
    public List<Barrage> getBarrage(String id);

    String getPageBarrageAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;
}
