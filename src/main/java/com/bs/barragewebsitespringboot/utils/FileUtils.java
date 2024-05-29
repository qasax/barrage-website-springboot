package com.bs.barragewebsitespringboot.utils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtils {
    public static  ResponseEntity<byte[]> downFile(String fileName,String downName) {
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
    public static  byte[] returnFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        try(FileInputStream inputStream = new FileInputStream(file)){
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static  void upFile(String filePath, MultipartFile file) throws FileNotFoundException {
        try(OutputStream out = new FileOutputStream(filePath)){
            out.write(file.getBytes());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //返回给前端 视频文件
    public static void sendFileToResponse(String filePath, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        response.setContentType("video/mp4");
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

        try (FileInputStream inputStream = new FileInputStream(file)) {
            // 使用BufferedOutputStream来提高效率
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[4096]; // 4KB buffer
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
