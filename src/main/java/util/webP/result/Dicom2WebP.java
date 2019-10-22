package util.webP.result;

import com.deepwise.cloud.constant.UtilConstant;
import com.deepwise.cloud.util.UtilLogger;
import com.luciad.imageio.webp.WebPImageReaderSpi;
import com.luciad.imageio.webp.WebPImageWriterSpi;
import com.luciad.imageio.webp.WebPWriteParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: linuxtest
 * @description: 将jpg的图片格式转换为webP
 * @author: YeDongYu
 * @create: 2019-03-25 10:13
 */
public class Dicom2WebP {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Dicom2WebP.class);
    
    /**
     * 注册webp的Native方法
     */
    private static IIORegistry iioRegistry = IIORegistry.getDefaultInstance();
    /** 线程池，数量依据——小于等于CPU内核数量/2 */
    private static ExecutorService service = Executors.newFixedThreadPool(1);
    private static String DefaultCompressionType = "Lossless";
    
    static{
        iioRegistry.registerServiceProvider(new WebPImageWriterSpi());
        iioRegistry.registerServiceProvider(new WebPImageReaderSpi());
    }
    
    /**
     * 调试方法
     * @param args
     * @throws IOException
     */

    /** 调试方法，可删 */
    public static void main(String[] filePath){
        System.out.println("请输入dcm文件夹");
        Scanner scanner = new Scanner(System.in);
        File dir = new File(scanner.next());
        File[] files = dir.listFiles();
        for (File file : files) {
            service.execute(() -> {
                Dicom2WebP dicom2Webp = new Dicom2WebP();
                dicom2Webp.dcm2webP(file, dicom2Webp.generateTargetFile(file));
            });
        }
        service.shutdown();
    }
    
    /**
     * Jpg转webP
     *
     * @throws IOException dicom转jpg出错
     */
    public void dcm2webP(File src, File tar){
        if (null == src || !src.exists()){
            return;
        }
        if (tar.exists()) {
            tar.delete();
        }
        // 请求系统进行垃圾回收,用于控制内存占用
        System.runFinalization();
        System.gc();
        System.out.println("垃圾回收后占用内存总数" + (Runtime.getRuntime().totalMemory()>>20));
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(tar)) {
            // 将图片从灰度转变为BGR颜色模式，在内存中进行
            BufferedImage image = UtilColorArrayTransfer.gray12toBGR(src);
            // 将BGR转变为WebP
            System.out.println("转变BGR后占用内存总数" + (Runtime.getRuntime().totalMemory()>>20));
            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
//            writeParam.setCompressionQuality(0.8F);
            writeParam.setCompressionType(DefaultCompressionType);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), writeParam);
            System.out.println("转变webP后占用内存总数" + (Runtime.getRuntime().totalMemory()>>20));
        } catch (IOException e) {
            UtilLogger.error(LOGGER, e, "dicom文件:", src.getAbsolutePath());
        }
    }
    
    private File generateTargetFile(File src){
        if (null == src || !src.exists()){
            return null;
        }
        String tarDirName = src.getParent() + "-webp";
        File tarDir = new File(tarDirName);
        if (!tarDir.isDirectory()){
            tarDir.mkdirs();
        }
        String srcName = src.getName();
        String targetFilePath = tarDir.getAbsolutePath() + UtilConstant.Separator.SLASH + srcName + ".webp";
        return new File(targetFilePath);
    }
}
