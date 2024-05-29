package com.bs.barragewebsitespringboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bs.barragewebsitespringboot.dao.BarrageDAO;
import com.bs.barragewebsitespringboot.pojo.Barrage;
import com.bs.barragewebsitespringboot.pojo.Video;
import com.bs.barragewebsitespringboot.service.BarrageService;
import com.bs.barragewebsitespringboot.utils.MapperUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BarrageServiceImpl extends ServiceImpl<BarrageDAO,Barrage> implements BarrageService {
    @Autowired
    BarrageDAO barrageDAO;
    @Override
    public int sendBarrage(Map<String,String> param) {
        Barrage barrage = new Barrage();
        barrage.setVideoId(Integer.parseInt(param.get("id")));
        barrage.setUserId(Integer.parseInt(param.get("author")));
        barrage.setTime(Float.parseFloat(param.get("time")));
        barrage.setText(param.get("text"));
        barrage.setColor(Integer.parseInt(param.get("color")));
        barrage.setType(Integer.parseInt(param.get("type")));
       return barrageDAO.sendBarrage(barrage);
    }

    @Override
    public List<Barrage> getBarrage(String id) {
        return barrageDAO.getBarrage(id);
    }

    @Override
    public String getPageBarrageAdmin(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception {
        PageHelper.orderBy(orderColumn+" "+sortOrder);
        PageHelper.startPage(pageNo,pageSize);
        List<Barrage> list = barrageDAO.getPageBarrageAdmin();
        PageInfo pageInfo=new PageInfo<>(list);
        Map map=new HashMap();
        map.put("code","success");
        map.put("data",pageInfo);
        return MapperUtils.obj2json(map);
    }
}
