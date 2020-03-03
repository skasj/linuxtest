package util.breastPosition;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deepwise.cloud.constant.UtilConstant;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @program: dw-cloud-dfs
 * @description: 识别判断钼靶像位信息
 * @author: YeDongYu
 * @create: 2019-11-22 10:13
 */
public class DetectBreastPosition {

    private static JSONObject jsonObject = JSON
            .parseObject("{\"is_inverse\":false,\"sum_y0\":100,\"cm_col_sum_up_thresh\":16000}");

    public static void main(String[] args) {
        DetectBreastPosition detectBreastPosition = new DetectBreastPosition();
        for (int i = 1001; i < 1005; i++) {
            System.out.println(i);
            BufferedImage mgDicom = detectBreastPosition
                    .readMGDicom("C:\\Users\\99324\\Desktop\\1031fix迁移\\钼靶像位信息判断\\" + i);
            getViewPositionAndLaterality(mgDicom,i);
        }
//
//        BufferedImage mgDicom = detectBreastPosition
//                .readMGDicom("C:\\Users\\99324\\Desktop\\1031fix迁移\\222\\DICOM\\PT0\\ST0\\SE1\\IM0");
//        getViewPositionAndLaterality(mgDicom,1001);
    }

    private static void getViewPositionAndLaterality(BufferedImage mgDicom,int i) {
        Mat src = Mat.eye(mgDicom.getHeight(), mgDicom.getWidth(), CvType.CV_8U);
        src.put(0, 0, ((DataBufferByte) mgDicom.getRaster().getDataBuffer()).getData());
        int thresh_mode = jsonObject.getBoolean("is_inverse") ? Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU
                : Imgproc.THRESH_OTSU;
        Mat tar = Mat.eye(mgDicom.getHeight(), mgDicom.getWidth(), CvType.CV_8U);
        System.out.println(Imgproc.threshold(src, tar, 0, 255, thresh_mode));
        src.release();
        writeMatToJPEG(tar,i);
        double left = 0, right = 0;
        int borderline = tar.cols() / 10;
        for (int row = 0; row < tar.rows(); row++) {
            for (int col = 0; col < borderline; col++) {
                left += tar.get(row, col)[0];
            }
            for (int col = tar.cols() - borderline; col < tar.cols(); col++) {
                right += tar.get(row, col)[0];
            }
        }
        String laterality = left < right ? "R" : "L";
        System.out.printf("left:%f right:%f\n", left, right);
        int maxCol = "L".equals(laterality) ? borderline : tar.cols();
        int initCol = "L".equals(laterality) ? 0 : tar.cols() - borderline;
        double mcol = 0;
        int initRow = jsonObject.getInteger("sum_y0");
        int maxRow = initRow + 10;
        for (int row = initRow; row < maxRow; row++) {
            for (int col = initCol; col < maxCol; col++) {
                mcol += tar.get(row, col)[0];
            }
        }
        System.out.println(mcol);
        String viewPosition = mcol < jsonObject.getInteger("cm_col_sum_up_thresh") ? "CC" : "MLO";
        System.out.printf("%s-%s\n", laterality, viewPosition);
    }

    private static void writeMatToJPEG(Mat tar,int i) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", tar, mob);
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        File jpeg = new File("C:\\Users\\99324\\Desktop\\1031fix迁移\\钼靶像位信息判断\\"+i+".jpeg");
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(jpeg)) {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            ImageWriter imageWriter = ImageIO.getImageWritersBySuffix(UtilConstant.FileSuffix.Type_Jpeg).next();
            imageWriter.setOutput(ios);
            imageWriter.write(bufImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private BufferedImage readMGDicom(String dcmPath) {
        File dcm = new File(dcmPath);
        if (!dcm.exists()) {
            System.out.printf("文件%s不存在", dcmPath);
        }
        try (ImageInputStream iis = ImageIO.createImageInputStream(
                dcm)) {
            ImageReader imageReader = ImageIO.getImageReadersByFormatName(UtilConstant.FileSuffix.Type_Dicom).next();
            imageReader.setInput(iis);
            BufferedImage bi = imageReader.read(0, imageReader.getDefaultReadParam());
            return convertColorModel(bi);
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private BufferedImage convertColorModel(BufferedImage src) {
        Raster data = src.getData();
        if (null == data || null == data.getDataBuffer()) {
            return null;
        }
        if (DataBuffer.TYPE_BYTE == data.getDataBuffer().getDataType()) {
            return src;
        }
        BufferedImage byteGray = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics graphics = byteGray.getGraphics();
        try {
            graphics.drawImage(src, 0, 0, null);
        } finally {
            graphics.dispose();
        }
        return byteGray;
    }
}
