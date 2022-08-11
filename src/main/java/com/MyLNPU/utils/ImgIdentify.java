package com.MyLNPU.utils;

import com.MyLNPU.model.RandomCode;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

/**
 * @Author xiyuan
 * @detail 验证码识别
 */

public class ImgIdentify {

    private int height = 40;
    private int width = 80;
    private int rgbThres = 150;

    public int[][] binaryImg(BufferedImage img) {
        int[][] imgArr = new int[this.height][this.width];
        for (int x = 0; x < this.width; ++x) {
            for (int y = 0; y < this.height; ++y) {
                if (x == 0 || y == 0 || x == this.width - 1 || y == this.height - 1) {
                    imgArr[y][x] = 1;
                    continue;
                }
                int pixel = img.getRGB(x, y);
                if (((pixel & 0xff0000) >> 16) < this.rgbThres && ((pixel & 0xff00) >> 8) < this.rgbThres && (pixel & 0xff) < this.rgbThres) {
                    imgArr[y][x] = 0;
                } else {
                    imgArr[y][x] = 1;
                }
            }
        }
        return imgArr;
    }

    // 去掉干扰线
    public void removeByLine(int[][] imgArr) {
        for (int y = 1; y < this.height - 1; ++y) {
            for (int x = 1; x < this.width - 1; ++x) {
                if (imgArr[y][x] == 0) {
                    int count = imgArr[y][x - 1] + imgArr[y][x + 1] + imgArr[y + 1][x] + imgArr[y - 1][x];
                    if (count > 2) imgArr[y][x] = 1;
                }
            }
        }
    }

    // 裁剪
    public int[][][] imgCut(int[][] imgArr, int[][] xCut, int[][] yCut, int num) {
        int[][][] imgArrArr = new int[num][yCut[0][1] - yCut[0][0]][xCut[0][1] - xCut[0][0]];
        for (int i = 0; i < num; ++i) {
            for (int j = yCut[i][0]; j < yCut[i][1]; ++j) {
                for (int k = xCut[i][0]; k < xCut[i][1]; ++k) {
                    imgArrArr[i][j - yCut[i][0]][k - xCut[i][0]] = imgArr[j][k];
                }
            }
        }
        return imgArrArr;
    }

    // 转字符串
    public String getString(int[][] imgArr) {
        StringBuilder s = new StringBuilder();
        int unitHeight = imgArr.length;
        int unitWidth = imgArr[0].length;
        for (int y = 0; y < unitHeight; ++y) {
            for (int x = 0; x < unitWidth; ++x) {
                s.append(imgArr[y][x]);
            }
        }
        return s.toString();
    }

    // 相同大小直接对比
    private int comparedText(String s1, String s2) {
        int n = s1.length();
        int percent = 0;
        for (int i = 0; i < n; ++i) {
            if (s1.charAt(i) == s2.charAt(i)) percent++;
        }
        return percent;
    }

    /**
     * 匹配识别
     *
     * @param imgArrArr
     * @return
     */
    public String matchCode(int[][][] imgArrArr) {
        StringBuilder s = new StringBuilder();
        Map<String, String> charMap = CharMap.getCharMap();
        for (int[][] imgArr : imgArrArr) {
            int maxMatch = 0;
            String tempRecord = "";
            for (Map.Entry<String, String> m : charMap.entrySet()) {
                int percent = this.comparedText(this.getString(imgArr), m.getValue());
                if (percent > maxMatch) {
                    maxMatch = percent;
                    tempRecord = m.getKey();
                }
            }
            s.append(tempRecord);
        }
        return s.toString();
    }

    // 写入硬盘
    public void writeImage(BufferedImage sourceImg) {
        File imageFile = new File("randomcode.jpg");
        try (FileOutputStream outStream = new FileOutputStream(imageFile)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(sourceImg, "jpg", out);
            byte[] data = out.toByteArray();
            outStream.write(data);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    // 控制台打印
    public void showImg(int[][] imgArr) {
        int unitHeight = imgArr.length;
        int unitWidth = imgArr[0].length;
        for (int y = 0; y < unitHeight; ++y) {
            for (int x = 0; x < unitWidth; ++x) {
                System.out.print(imgArr[y][x]);
            }
            System.out.println();
        }
    }

    public static RandomCode getVerifyMessage() throws IOException {
        String code = null;
        ImgIdentify imgIdentify = new ImgIdentify();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://jwxt.lnpu.edu.cn/jsxsd/verifycode.servlet");
        get.setHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse httpResponse = httpClient.execute(get);
        InputStream content = httpResponse.getEntity().getContent();
        Header[] cookies = httpResponse.getHeaders("Set-Cookie");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(cookies[0].toString().split(":")[1].trim());
        stringBuffer.append(",");
        stringBuffer.append(cookies[1].toString().split(":")[1].trim());
        String cookie = stringBuffer.toString();
        System.out.println("获取的cookie：" + cookie);
        try (InputStream is = httpResponse.getEntity().getContent()) {
            BufferedImage sourceImg = ImageIO.read(is);
            imgIdentify.writeImage(sourceImg); // 图片写入硬盘
            int[][] imgArr = imgIdentify.binaryImg(sourceImg); // 二值化
            imgIdentify.removeByLine(imgArr); // 去除干扰先 引用传递
            int[][][] imgArrArr = imgIdentify.imgCut(imgArr,
                    new int[][]{new int[]{3, 20}, new int[]{21, 38}, new int[]{39, 56}, new int[]{57, 74}},
                    new int[][]{new int[]{10, 31}, new int[]{10, 31}, new int[]{10, 31}, new int[]{10, 31}},
                    4);
            code = imgIdentify.matchCode(imgArrArr);
            System.out.println("验证码为：" + code); // 识别
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RandomCode(code,cookie);
    }
}