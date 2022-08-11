package com.MyLNPU.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 将图片写入硬盘
 */
public class SaveImage {
    public static boolean save(InputStream inputStream, String filePath) {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, "png", out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
