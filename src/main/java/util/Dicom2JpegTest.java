package util;

import com.deepwise.cloud.constant.UtilConstant;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import org.apache.commons.lang.StringUtils;
import org.dcm4che3.image.BufferedImageUtils;
import org.dcm4che3.imageio.codec.ImageReaderFactory;
import org.dcm4che3.imageio.codec.ImageWriterFactory;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2019-11-05 19:16
 */
public class Dicom2JpegTest {

    static {
        ImageReaderFactory.getDefault();
        ImageWriterFactory.getDefault();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入dicom文件路径，如: /root/1.dcm");
        String dcm = scanner.nextLine();
        if (StringUtils.isBlank(dcm)){
//            dcm = "C:\\Users\\99324\\Desktop\\1031fix\\1.dcm";
            dcm = "/root/1.dcm";
        }
        System.out.println("请输入件路径，如: /root/1.jpeg");
        String jpeg = scanner.nextLine();
        if (StringUtils.isBlank(jpeg)){
            jpeg = "/root/1.jpeg";
//            jpeg = "C:\\Users\\99324\\Desktop\\1031fix\\1.jpeg";
        }
        Dicom2JpegTest dicom2JpegTest = new Dicom2JpegTest();
        dicom2JpegTest.dcm2Jpg(new File(dcm),new File(jpeg));
    }

    public void dcm2Jpg(java.io.File dcm, java.io.File jpg) throws IOException {
        if (jpg.exists()){
            jpg.delete();
        }
        try (ImageInputStream iis = ImageIO.createImageInputStream(
                dcm); ImageOutputStream ios = ImageIO.createImageOutputStream(jpg)) {
            BufferedImage bi = readImage(iis);
            bi = convert(bi);
            ImageWriter imageWriter = ImageIO.getImageWritersBySuffix(UtilConstant.FileSuffix.Type_Jpeg).next();
            imageWriter.setOutput(ios);
            ImageWriteParam param = imageWriter.getDefaultWriteParam();
            imageWriter.write(null, new IIOImage(bi, null, null), param);
        }
    }

    /**
     * 读取图像BufferedImage
     *
     * @param iis ImageInputStream
     * @return bufferedImage
     * @throws IOException 读取出错
     */
    private BufferedImage readImage (ImageInputStream iis) throws IOException {
        ImageReader imageReader = ImageIO.getImageReadersByFormatName(UtilConstant.FileSuffix.Type_Dicom).next();
        imageReader.setInput(iis);
        return imageReader.read(0, imageReader.getDefaultReadParam());
    }

    /**
     * 图像色彩模式转换
     *
     * @param bi bufferedImage
     * @return 转换后的bufferedImage
     */
    private BufferedImage convert(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        return cm.getNumComponents() == 3 ? BufferedImageUtils.convertToIntRGB(bi) : bi;
    }
}
