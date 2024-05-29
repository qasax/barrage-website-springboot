package com.bs.barragewebsitespringboot;

import com.bs.barragewebsitespringboot.utils.AISubtitlesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BarrageWebsiteSpringbootApplicationTests {
    @Value("${value.videoBaseURL}")
    String   videoBaseURL;
    @Autowired
    AISubtitlesUtils aiSubtitlesUtils;
    @Test
    void contextLoads() {
        System.out.println(videoBaseURL);
    }
    @Test
    void test1() throws Exception {
        System.out.println(aiSubtitlesUtils.aiSubtitle("1_rootfile_0.86836213962099",".mp4"));
    }

}
