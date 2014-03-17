/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.entityfile.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author ronald
 */
public class ImageUtil {

    public static void resizeJPEGImage(File input, File output, int thumbWidth, int thumbHeight) {
        try {
            Image image = ImageIO.read(input);


            double thumbRatio = (double) thumbWidth / (double) thumbHeight;
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);
            double imageRatio = (double) imageWidth / (double) imageHeight;
            if (thumbRatio < imageRatio) {
                thumbHeight = (int) (thumbWidth / imageRatio);
            } else {
                thumbWidth = (int) (thumbHeight * imageRatio);
            }


            BufferedImage newImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = newImage.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setBackground(Color.white);
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, thumbWidth, thumbHeight);
            g2d.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
            g2d.dispose();

            ImageIO.write(newImage, "jpeg", output);
        } catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }
}