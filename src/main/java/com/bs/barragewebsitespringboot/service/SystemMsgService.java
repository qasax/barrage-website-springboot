package com.bs.barragewebsitespringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bs.barragewebsitespringboot.pojo.SystemMsg;
import jakarta.servlet.http.HttpServletRequest;

public interface SystemMsgService extends IService<SystemMsg> {
    String getSystemMsg(HttpServletRequest httpServletRequest, long max, int offset) throws Exception;

    String getPageSystemMsg(int pageSize, int pageNo, String orderColumn, String sortOrder) throws Exception;
}
