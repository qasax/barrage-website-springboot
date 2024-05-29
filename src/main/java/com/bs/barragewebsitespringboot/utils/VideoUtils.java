package com.bs.barragewebsitespringboot.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

@Getter
@Setter
public class VideoUtils {
    /**
     * 总帧数
     **/
    private int lengthInFrames;

    /**
     * 帧率
     **/
    private double frameRate;

    /**
     * 时长
     **/
    private double duration;

    /**
     * 视频编码
     */
    private String videoCode;
    /**
     * 音频编码
     */
    private String audioCode;

    private int width;
    private int height;
    private int audioChannel;
    private String md5;
    /**
     * 音频采样率
     */
    private Integer sampleRate;

    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            return "";
        }
    }
    public static VideoUtils getVideoInfo(File file) {
        VideoUtils videoInfo = new VideoUtils();
        FFmpegFrameGrabber grabber = null;
        try {
            grabber = new FFmpegFrameGrabber(file);
            // 启动 FFmpeg
            grabber.start();

            // 读取视频帧数
            videoInfo.setLengthInFrames(grabber.getLengthInVideoFrames());

            // 读取视频帧率
            videoInfo.setFrameRate(grabber.getVideoFrameRate());

            // 读取视频秒数
            videoInfo.setDuration(grabber.getLengthInTime() / 1000000.00);

            // 读取视频宽度
            videoInfo.setWidth(grabber.getImageWidth());

            // 读取视频高度
            videoInfo.setHeight(grabber.getImageHeight());


            videoInfo.setAudioChannel(grabber.getAudioChannels());

            videoInfo.setVideoCode(grabber.getVideoCodecName());

            videoInfo.setAudioCode(grabber.getAudioCodecName());
            // String md5 = MD5Util.getMD5ByInputStream(new FileInputStream(file));

            videoInfo.setSampleRate(grabber.getSampleRate());
            return videoInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (grabber != null) {
                    // 此处代码非常重要，如果没有，可能造成 FFmpeg 无法关闭
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                System.out.println("getVideoInfo grabber.release failed 获取文件信息失败：{}"+ e.getMessage());
            }
        }
    }
    /**
     * 随机获取视频截图
     * @param videoFile 视频文件
     * @return 截图列表
     * */
    public static String randomGrabberFFmpegImage(File videoFile,String videoPicPath,String videoPicName) {
        FFmpegFrameGrabber grabber = null;

        try {
            grabber = new FFmpegFrameGrabber(videoFile);
            grabber.start();
            // 获取视频总帧数
            // int lengthInVideoFrames = grabber.getLengthInVideoFrames();
            // 获取视频时长， / 1000000 将单位转换为秒
            long delayedTime = grabber.getLengthInTime() / 1000000;

            Random random = new Random();
                grabber.setTimestamp((random.nextInt((int)delayedTime - 1) + 1) * 1000000L);
                Frame f = grabber.grabImage();
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bi = converter.getBufferedImage(f);
                String imageName = videoPicName;
                File out = Paths.get(videoPicPath, imageName).toFile();
                ImageIO.write(bi, "jpg", out);
            return "生成图片";
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (grabber != null) {
                    grabber.stop();
                    grabber.release();
                }
            } catch (FFmpegFrameGrabber.Exception e) {
                System.out.println("getVideoInfo grabber.release failed 获取文件信息失败：{}"+ e.getMessage());
            }
        }
    }

}
