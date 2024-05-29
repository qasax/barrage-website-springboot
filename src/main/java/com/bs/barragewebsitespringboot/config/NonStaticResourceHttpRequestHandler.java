package com.bs.barragewebsitespringboot.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.core.io.Resource;
@Component
/**
 * 调用springboot原生的视频流响应方式
 */
public class NonStaticResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    public final static String ATTR_FILE = "NON-STATIC-FILE";

    @Override
    public Resource getResource(HttpServletRequest request) {
        String filePath =  (String) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(filePath);
    }
}