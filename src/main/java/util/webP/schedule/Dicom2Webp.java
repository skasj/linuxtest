package util.webP.schedule;

import com.luciad.imageio.webp.WebPWriteParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

/**
 * @program: linuxtest
 * @description: 将dicom图片转换为webp
 * @author: YeDongYu
 * @create: 2019-04-11 13:55
 */
public class Dicom2Webp {
    
    private static Logger logger = LoggerFactory.getLogger(Dicom2Webp.class);
    
    private static int i = 0;
    
    
    private static long time = System.currentTimeMillis();
    
    /**
     * Jpg转webP
     *
     * @throws IOException dicom转jpg出错
     */
    public boolean dcm2webP(File src, File tar, BufferedImageOp convertOp, WebPWriteParam writeParam){
        long temp = System.currentTimeMillis();
        // 请求系统进行垃圾回收
        System.runFinalization();
        System.gc();
        try (ImageInputStream iis = ImageIO.createImageInputStream(
            src); ImageOutputStream ios = ImageIO.createImageOutputStream(tar)) {
            BufferedImage image = readImage(iis);
            image = convert(image, convertOp);
            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
            writer.setOutput(ios);
            IIOImage iioImage = new IIOImage(image, null, null);
            writer.write(null, iioImage, writeParam);
            i++;
            System.out.println("当前占用内存总数" + Runtime.getRuntime().totalMemory());
            System.out.println("当前消耗内存总数" + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
            System.out.println(i + "耗时：" + (System.currentTimeMillis() - temp));
            System.out.println("总耗时:" + (System.currentTimeMillis() - time));
            return true;
        } catch (Exception e) {
            logger.error(src.getAbsolutePath() + "文件转webP格式失败", e);
            return false;
        }
    }
    
    /**
     * 读取图像BufferedImage
     *
     * @param iis ImageInputStream
     * @return bufferedImage
     * @throws IOException 读取出错
     */
    private BufferedImage readImage(ImageInputStream iis) throws IOException{
        ImageReader imageReader = ImageIO.getImageReadersByFormatName("DICOM").next();
        imageReader.setInput(iis);
        return imageReader.read(0, imageReader.getDefaultReadParam());
    }
    
    /**
     * 像素转换
     *
     * @param bi bufferedImage
     * @return 转换后的bufferedImage
     */
    private BufferedImage convert(BufferedImage bi, BufferedImageOp convertOp){
        BufferedImage intRGB = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics graphics = intRGB.getGraphics();
        try {
            // 图像色彩格式转换BGR格式
            graphics.drawImage(bi, 0, 0, null);
        } finally {
            graphics.dispose();
        }
        if (null != convertOp) {
            // 图像自定义转换
            intRGB = convertOp.filter(intRGB, null);
        }
        return intRGB;
    }
}
