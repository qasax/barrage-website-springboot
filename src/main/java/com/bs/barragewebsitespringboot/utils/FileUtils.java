package com.example.ncre_system_idea.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtils {
    public ResponseEntity<byte[]> downFile(String fileName,String downName) {
        //创建输入流
        //使用try-with-resources语句自动关闭资源
        try (InputStream is = new FileInputStream(fileName)) {
            //创建字节数组
            byte[] bytes = new byte[is.available()];
            //将流读到字节数组中
            is.read(bytes);
            //创建HttpHeaders对象设置响应头信息
            MultiValueMap<String, String> headers = new HttpHeaders();
            //设置要下载方式以及下载文件的名字
            headers.add("Content-Disposition", "attachment;filename="+downName);
            //设置响应状态码
            HttpStatus statusCode = HttpStatus.OK;
            //创建ResponseEntity对象
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
            return responseEntity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void upFile(String fileName, MultipartFile file) throws FileNotFoundException {
        try(OutputStream out = new FileOutputStream(fileName)){
            out.write(file.getBytes());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
