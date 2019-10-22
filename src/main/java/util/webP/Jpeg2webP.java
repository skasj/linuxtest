package util.webP;

import com.deepwise.cloud.util.UtilDicomFileParser;
import com.deepwise.dicom.pixel.PixelPicker;
import com.luciad.imageio.webp.WebPImageReaderSpi;
import com.luciad.imageio.webp.WebPImageWriterSpi;
import com.luciad.imageio.webp.WebPWriteParam;
import org.dcm4che3.image.PaletteColorModel;
import sun.awt.SunHints;

import javax.imageio.*;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: linuxtest
 * @description: 将jpg的图片格式转换为webP
 * @author: YeDongYu
 * @create: 2019-03-25 10:13
 */
public class Jpeg2webP {
    
    public static IIORegistry iioRegistry = IIORegistry.getDefaultInstance();
    
    static{
        iioRegistry.registerServiceProvider(new WebPImageWriterSpi());
        iioRegistry.registerServiceProvider(new WebPImageReaderSpi());
    }
    
    int[] rgb;
    private int pixel_mask;
    
    public static void main(String[] args) throws IOException{
//        Scanner in = new Scanner(System.in);
//        System.out.println("输入dcm文件路径");
//        String dicom_native_str = in.nextLine();
//        System.out.println("输入webP文件路径");
//        String webp_native_str = in.nextLine();
//        File dicom_native = new File(dicom_native_str);
//        File webp_native = new File(webp_native_str);
        
        File dicom_native = new File("test_pic/25211.dcm"); // ok 7596ms
        File dicom_jpeg = new File("test_pic/mg_jpeg.dcm"); // ok 7918ms
        File dicom_jpeg2000 = new File("test_pic/mg_jpeg2000.dcm");// ok 8430ms
        File webp_native = new File("test_pic/25211有blue(3).webp"); //ok 7918ms
        File jpeg_native = new File("test_pic/jpeg_native.jpeg"); //ok
        File raw_native = new File("test_pic/raw_native.raw"); //ok
        File png_native = new File("test_pic/png_native.png"); //ok
        
        long st = System.currentTimeMillis();
//        dcm2jpg(dicom_native, jpeg_native, "DICOM", "jpeg"); //ok 1545ms
//        dcm2jpg(dicom_native,png_native,"DICOM","png"); //ok 4545ms
//        dcm2jpg(dicom_native,raw_native,"DICOM","image/raw"); //ok 4545ms
        dcm2webP(dicom_native, webp_native); //ok
//        dcm2webP(dicom_jpeg2000,webp_native);
        System.out.println("cost: " + (System.currentTimeMillis() - st));
    }
    
    /**
     * Jpg转webP
     *
     * @throws IOException dicom转jpg出错
     */
    public static void dcm2webP(File src, File tar) throws IOException{
        if (tar.exists()){
            tar.delete();
        }
        try (/*ImageInputStream iis = ImageIO.createImageInputStream(
            src); */ImageOutputStream ios = ImageIO.createImageOutputStream(tar)) {
            long s1 = System.currentTimeMillis();
//            BufferedImage image = readImage(iis, "DICOM");
            BufferedImage image = readAndTransfer(src);
            long s2 = System.currentTimeMillis();
//            image = convert(image);
            long s3 = System.currentTimeMillis();
            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
//            writeParam.setCompressionQuality(1.0f);
            writeParam.setCompressionType("Lossless");
            writer.setOutput(ios);
            long s6 = System.currentTimeMillis();
            writer.write(null, new IIOImage(image, null, null), writeParam);
            long s7 = System.currentTimeMillis();
            System.out.println(String.format("s1:%d s2:%d s3:%d", s2 - s1, s3 - s1, s7 - s6));
        }
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
//            deleteR(bi);
        BufferedImage intRGB = new BufferedImage(bi.getWidth(), bi.getHeight(), 5);
        Graphics graphics = intRGB.getGraphics();
        try {
            graphics.drawImage(bi, 0, 0, null);
        } finally {
            graphics.dispose();
        }
        intRGB = deleteR(intRGB);
        return intRGB;
    }

//    private static BufferedImage convertToRGB(BufferedImage src){
//        if (src.getColorModel() instanceof IndexColorModel) {
//            IndexColorModel icm = (IndexColorModel) src.getColorModel();
//            src = icm.convertToIntDiscrete(src.getRaster(), true);
//        }
//    }
    
    /**
     *
     * @param BGR
     */
    private static BufferedImage deleteR(BufferedImage BGR){
        ColorModel cm = BGR.getColorModel();
        Raster raster = BGR.getRaster();
        int w = raster.getWidth();
        int h = raster.getHeight();
        WritableRaster discreteRaster = cm.createCompatibleWritableRaster(w, h);
        Object obj = null;
        byte[] data = null;
        int rX = raster.getMinX();
        int rY = raster.getMinY();
        int r, g, b, gray;
        for (int y = 0; y < h; y++, rY++) {
            obj = raster.getDataElements(rX, rY, w, 1, obj);
//            if (obj instanceof int[]) {
//                data = (int[]) obj;
//            } else {

//            }
//            data = toIntArray(obj);
            data = (byte[]) obj;
            for (int x = 0; x < w; x++) {
                r = data[x*3];
                g = data[x*3 + 1];
                b = data[x*3 + 2];
//                r = 0;
//                g = (data[x]>>8) & 0xff;
//                b = (data[x]>>16) & 0xff;
//                data[x*3] = (byte) ((r & 0xf0) | (g & 0xf));
//                data[x*3 + 1] = (byte) ((g & 0xf0) | (b & 0xf));
//                data[x*3 + 2] = (byte) ((b & 0xf0) | (r & 0xf));
//                data[x*3] = (byte) ((g & 0xf0)>>4);
                gray = (r*30 + g*56 + b*11)/100;
                data[x*3] = 0;
                data[x*3 + 1] = (byte) (gray >> 4);
//                data[x*3 + 1] =  (byte) g;
//                data[x*3 + 2] = (byte) (((g & 0x0f)<<4) | (b & 0xf));
//                data[x*3 + 2] = (byte) (gray & 0xf);
                data[x*3 + 2] = (byte) (b & 0xf);
//                data[x*3 +2] = 0;
//                data[x] =(byte) (((g << 4) | (b >> 4)) << 8 | (b& 0xf));
//                discreteRaster.setSample();
            }
            discreteRaster.setDataElements(0, y, w, 1, data);
//            data1 = toIntArray(data);
//            bi.setRGB(0,y,w,1,data,0,1);
        }
        return new BufferedImage(cm, discreteRaster, false, null);
    }
    
    private static BufferedImage readAndTransfer(File src) throws IOException{
        Raster raster = PixelPicker.pickPixelFully(src).get(0);
        int w = raster.getWidth();
        int h = raster.getHeight();
        BufferedImage bi = new BufferedImage(w, h, 5);
        ColorModel cm = bi.getColorModel();
        WritableRaster discreteRaster = cm.createCompatibleWritableRaster(w, h);
        Object obj = null;
        byte[] data = new byte[w * 3];
        short[] data2 = null;
        int rX = raster.getMinX();
        int rY = raster.getMinY();
        int g, b;
        for (int y = 0; y < h; y++, rY++) {
            obj = raster.getDataElements(rX, rY, w, 1, obj);
            data2 = (short[]) obj;
            for (int x = 0; x < w; x++) {
//                System.out.println(data2[x]);
//                System.out.println(~(data2[x]));
//                System.out.println(-(data2[x]));
//                System.out.println(Integer.toBinaryString(data2[x]));
//                System.out.println(Integer.toBinaryString((short) ~(data2[x])));
//                System.out.println(Integer.toBinaryString((short) -(data2[x])));
//                data2[x] =(short) -(data2[x]);
//                data2[x] = (short) (((data2[x] & 0xff00) >> 8) | ((data2[x] & 0xff) << 8));
                data2[x] = (short) (4095-data2[x]);
                g = (data2[x] & 0xff0) >> 4;
                b = data2[x]& 0xf;
//                g = -((data2[x] & 0xff0) >> 4);
//                b = -(data2[x]& 0xf);
//                data[x*3] = 0;
                data[x*3 + 1] = (byte) g;
                data[x*3 + 2] = (byte) b;
            }
            discreteRaster.setDataElements(0, y, w, 1, data);
        }
        return new BufferedImage(cm, discreteRaster, false, null);
    }
    
    static int[] toIntArray(Object obj){
        if (obj instanceof int[]) {
            return (int[]) obj;
        } else if (obj == null) {
            return null;
        } else if (obj instanceof short[]) {
            short sdata[] = (short[]) obj;
            int idata[] = new int[sdata.length];
            for (int i = 0; i < sdata.length; i++) {
                idata[i] = (int) sdata[i] & 0xffff;
            }
            return idata;
        } else if (obj instanceof byte[]) {
            byte bdata[] = (byte[]) obj;
            int idata[] = new int[bdata.length];
            for (int i = 0; i < bdata.length; i++) {
                idata[i] = 0xffff & (int) bdata[i];
            }
            return idata;
        }
        return null;
    }
    
    public static final BufferedImage convert2(BufferedImage originalPic){
        int imageWidth = originalPic.getWidth();
        int imageHeight = originalPic.getHeight();
        
        BufferedImage newPic = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
        
        ColorConvertOp cco = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB),
            new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, SunHints.KEY_ALPHA_INTERPOLATION));
        cco.filter(originalPic, newPic);
        return newPic;
    }
    
    private static RenderingHints generateRenderingHints(){
        Map<RenderingHints.Key, Object> map = new HashMap<>();
        map.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        map.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        map.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        map.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        map.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        map.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        return new RenderingHints(map);
    }
    
    /**
     * dicom转Jpg、
     *
     * @param src dicom文件
     * @param tar 生成的jpg
     * @throws IOException dicom转jpg出错
     */
    public static void dcm2jpg(File src, File tar, String srcType, String tarType) throws IOException{
        try (ImageInputStream iis = ImageIO.createImageInputStream(
            src); ImageOutputStream ios = ImageIO.createImageOutputStream(tar)) {
            long s1 = System.currentTimeMillis();
            BufferedImage bi = readImage(iis, srcType);
            long s2 = System.currentTimeMillis();
            bi = convert(bi);
            long s3 = System.currentTimeMillis();
            ImageWriter imageWriter = ImageIO.getImageWritersBySuffix(tarType).next();
            imageWriter.setOutput(ios);
            ImageWriteParam param = imageWriter.getDefaultWriteParam();
            param.setCompressionMode(WebPWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1);
            long s6 = System.currentTimeMillis();
            imageWriter.write(null, new IIOImage(bi, null, null), param);
            long s7 = System.currentTimeMillis();
            System.out.println(String.format("s1:%d s2:%d s3:%d ", s2 - s1, s3 - s2, s7 - s6));
        }
    }
    
    private int calcRealMapSize(int bits, int size){
        int newSize = Math.max(1 << bits, size);
        return Math.max(newSize, 256);
    }
}
