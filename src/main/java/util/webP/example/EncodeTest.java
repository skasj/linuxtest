package util.webP.example;

import com.luciad.imageio.webp.WebPWriteParam;
import org.dcm4che3.image.BufferedImageUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

public class EncodeTest {
    public static void main(String args[]) throws IOException{
//        String inputPngPath = "test_pic/test.png";
//        String inputJpgPath = "test_pic/test.jpg";
//        String inputBmpPath = "test_pic/e.jpeg";
//        String outputWebpPath = "test_pic/test_.webp";
//
//        long st = System.currentTimeMillis();
//        // Obtain an image to encode from somewhere
//        ImageInputStream iis = ImageIO.createImageInputStream(new File(inputBmpPath));
//        BufferedImage image = readImage(iis,"jpeg");
//
//        // Obtain a WebP ImageWriter instance
//        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
//
//        // Configure encoding parameters
//        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
//        writeParam.setCompressionMode(WebPWriteParam.MODE_EXPLICIT);
//        writeParam.setCompressionType("Lossless");
//
//        // Configure the output on the ImageWriter
//        writer.setOutput(new FileImageOutputStream(new File(outputWebpPath)));
//
//        // Encode
////        long st = System.currentTimeMillis();
////        writer.write(null, new IIOImage(image, null, null), writeParam);
////        System.out.println("cost: " + (System.currentTimeMillis() - st));
        File webp_native = new File("test_pic/e.webp");
        File jpeg_native = new File("test_pic/e.jpeg"); //ok
        jpg2webP(jpeg_native,webp_native);
    }
    
    /**
     * Jpg转webP
     *
     * @throws IOException dicom转jpg出错
     */
    public static void jpg2webP(File src, File tar) throws IOException{
        long st = System.currentTimeMillis();
        BufferedImage image = ImageIO.read(src);
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(WebPWriteParam.MODE_DEFAULT);
        writer.setOutput(new FileImageOutputStream(tar));
        writer.write(null, new IIOImage(image, null, null), writeParam);
        System.out.println("cost: " + (System.currentTimeMillis() - st));
    }
    
    /**
     * 读取图像BufferedImage
     *
     * @param iis ImageInputStream
     * @return bufferedImage
     * @throws IOException 读取出错
     */
    private static BufferedImage readImage(ImageInputStream iis, String readerType) throws IOException{
        ImageReader imageReader = ImageIO.getImageReadersByFormatName(readerType).next();
        imageReader.setInput(iis);
        return imageReader.read(0, imageReader.getDefaultReadParam());
    }
    
    /**
     * 图像色彩模式转换
     *
     * @param bi bufferedImage
     * @return 转换后的bufferedImage
     */
    private static BufferedImage convert(BufferedImage bi){
        ColorModel cm = bi.getColorModel();
        return cm.getNumComponents() == 3 ? BufferedImageUtils.convertToIntRGB(bi) : bi;
    }
}
