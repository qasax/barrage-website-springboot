package com.bs.barragewebsitespringboot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
@Component
public class AISubtitlesUtils {
    @Value("${value.pyProgramURL}")
    String pyProgramURL;
    @Value("${value.pythonURL}")
    String pythonURL;
    @Value("${value.videoSubURL}")
    String videoSubURL;
    @Value("${value.videoBaseURL}")
    String   videoBaseURL;
    public  String aiSubtitle(String videoName,String videoSuffix) throws Exception{
        //终端输出乱码，解决方法在python脚本中指定编码为utf-8 原因 python默认输出编码为unicode java则为utf-8 两者都可以显示中文，日文字符，但是不可直接互转
        Process proc;
        String arg[]=new String[]{pythonURL,pyProgramURL,videoBaseURL,videoName,videoSuffix,videoSubURL};
        proc= Runtime.getRuntime().exec(arg);
        BufferedReader in=new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line=null;
        while ((line=in.readLine())!=null){
            System.out.println(line);
        }
        System.out.println("进程返回值"+proc.waitFor());;
        in.close();
        return "ok";
    }
}
