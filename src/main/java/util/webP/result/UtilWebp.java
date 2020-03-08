//package util.webP.result;
//
//import com.deepwise.cloud.constant.UtilConstant;
//import com.deepwise.dicom.pixel.ImagePicker;
//import com.luciad.imageio.webp.WebPImageReaderSpi;
//import com.luciad.imageio.webp.WebPImageWriterSpi;
//import com.luciad.imageio.webp.WebPWriteParam;
//import java.awt.Graphics;
//import java.awt.image.BufferedImage;
//import java.awt.image.ColorModel;
//import java.awt.image.DataBuffer;
//import java.awt.image.Raster;
//import java.awt.image.WritableRaster;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.IIOImage;
//import javax.imageio.ImageIO;
//import javax.imageio.ImageReader;
//import javax.imageio.ImageWriter;
//import javax.imageio.spi.IIORegistry;
//import javax.imageio.stream.ImageInputStream;
//import javax.imageio.stream.ImageOutputStream;
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import util.UtilDicomWindow;
//
///**
// * webp工具类
// *
// * @author cairui
// * @version v1.0.0 2019/5/6 20:13
// */
//public class UtilWebp {
//
//    private UtilWebp() {
//        throw new IllegalStateException("Utility class");
//    }
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(UtilWebp.class);
//
//    /**
//     * 注册webp的Native方法
//     */
//    private static IIORegistry iioRegistry = IIORegistry.getDefaultInstance();
//
//    private static final String DEFAULT_COMPRESSION_TYPE = "Lossless";
//
//    static {
//        iioRegistry.registerServiceProvider(new WebPImageWriterSpi());
//        iioRegistry.registerServiceProvider(new WebPImageReaderSpi());
//    }
//
//    /**
//     * 调试方法，可删
//     */
//    public static void main(String[] filePath) throws Exception {
////        File dir = new File("C:\\Users\\99324\\Desktop\\1031fix迁移\\钼靶颜色异常\\4109671\\5A1D6C7A\\BB70091A");
////        File dir = new File("C:\\Users\\99324\\Desktop\\1031fix迁移\\各类型文件\\Breast\\00543407\\882060D6\\19CDB35F");
////        File[] files = dir.listFiles();
////        File file = new File("C:\\Users\\99324\\Desktop\\1031fix迁移\\钼靶颜色异常\\上传\\DICOM\\PT0\\ST0\\SE0\\1");
//        File file = new File("C:\\Users\\99324\\Desktop\\1031fix迁移\\钼靶颜色异常\\4109671\\5A1D6C7A\\BB70091A\\1001.dcm");
////        File file = new File("C:\\Users\\99324\\Desktop\\1031fix迁移\\钼靶颜色异常\\1");
////        for (File file : files) {
//            dcm2Webp(file, new File(file.getParent() + "\\1001.webp"));
////        }
//    }
//
//    /**
//     * dcm转webP
//     *
//     * @param src 源文件
//     * @param dest 目标文件
//     */
//    public static void dcm2Webp(File src, File dest) throws Exception {
//        if (null == src || !src.exists()) {
//            return;
//        }
//        if (dest.exists()) {
//            FileUtils.deleteQuietly(dest);
//        }
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs();
//        }
//        try (ImageOutputStream ios = ImageIO.createImageOutputStream(dest)) {
//            // 将图片从灰度转变为BGR颜色模式，在内存中进行
//            BufferedImage image = gray2sRGB(src);
//            // 将BGR转变为WebP
//            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
//            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
//            writeParam.setCompressionType(DEFAULT_COMPRESSION_TYPE);
//            writer.setOutput(ios);
//            writer.write(null, new IIOImage(image, null, null), writeParam);
//        }
//    }
//
//    /**
//     * 将16位的灰度像素，转变为24位的sRGB像素
//     *
//     * @param src
//     * @return
//     */
//    private static BufferedImage gray2sRGB(File src) throws IOException {
//        BufferedImage bufferedImage = readMGDicom(src);
//        if (null == bufferedImage) {
//            System.out.println("图片读取异常");
//            return null;
//        }
//        Raster dicomRaster = bufferedImage.getRaster();
//        UtilDicomWindow.calculateWWandWL(dicomRaster);
//        int w = dicomRaster.getWidth();
//        int h = dicomRaster.getHeight();
//        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
//        ColorModel cm = bi.getColorModel();
//        WritableRaster discreteRaster = cm.createCompatibleWritableRaster(w, h);
//        Object obj = null;
//        byte[] grbData = new byte[w * 3];
//        short[] grayDate = null;
//        int rX = dicomRaster.getMinX();
//        int rY = dicomRaster.getMinY();
//        int r, g, b;
//        for (int y = 0; y < h; y++, rY++) {
//            obj = dicomRaster.getDataElements(rX, rY, w, 1, obj);
//            grayDate = (short[]) obj;
//            for (int x = 0; x < grayDate.length; x++) {
//                // 按照与前端的约定CT = 16 * r + 256 * g + b
//                g = ((grayDate[x] >> 12) << 4) & (0xff);
//                r = ((grayDate[x] >> 8) << 4) & (0xff);
//                b = grayDate[x] & (0xff);
//                grbData[x * 3] = (byte) r;
//                grbData[x * 3 + 1] = (byte) g;
//                grbData[x * 3 + 2] = (byte) b;
//            }
//            discreteRaster.setDataElements(0, y, w, 1, grbData);
//        }
//        return new BufferedImage(cm, discreteRaster, false, null);
//    }
//
//    private static BufferedImage readMGDicom(File dcm) {
//        BufferedImage bufferedImage = null;
//        try {
//            bufferedImage = ImagePicker.pickImageEach(dcm, 0);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return convertColorModel(bufferedImage);
////        try (ImageInputStream iis = ImageIO.createImageInputStream(
////                dcm)) {
////            ImageReader imageReader = ImageIO.getImageReadersByFormatName(UtilConstant.FileSuffix.Type_Dicom).next();
////            imageReader.setInput(iis);
////            BufferedImage bi = imageReader.read(0, imageReader.getDefaultReadParam());
////            UtilDicomWindow.calculateWWandWL(bi.getRaster());
////            return convertColorModel(bi);
//////            return bi;
////        } catch (IOException e) {
////            System.out.println(e.toString());
////            return null;
////        }
//    }
//
//    private static BufferedImage convertColorModel(BufferedImage src) {
//        Raster data = src.getData();
//        if (null == data || null == data.getDataBuffer()) {
//            return null;
//        }
//        if (DataBuffer.TYPE_USHORT == data.getDataBuffer().getDataType()) {
//            return src;
//        }
//        BufferedImage byteGray = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
//        Graphics graphics = byteGray.getGraphics();
//        try {
//            graphics.drawImage(src, 0, 0, null);
//        } finally {
//            graphics.dispose();
//        }
//        return byteGray;
//    }
//}
