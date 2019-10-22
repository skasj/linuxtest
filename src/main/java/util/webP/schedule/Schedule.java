package util.webP.schedule;

import com.luciad.imageio.webp.WebPImageReaderSpi;
import com.luciad.imageio.webp.WebPImageWriterSpi;
import com.luciad.imageio.webp.WebPWriteParam;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2019-04-11 17:14
 */
public class Schedule {
    
    /**
     * 注册webp的Native方法
     */
    public static IIORegistry iioRegistry = IIORegistry.getDefaultInstance();
    static{
        iioRegistry.registerServiceProvider(new WebPImageWriterSpi());
        iioRegistry.registerServiceProvider(new WebPImageReaderSpi());
    }
    
    /** 线程池，数量依据——小于CPU内核数量/2*/
    private static ExecutorService service = Executors.newFixedThreadPool(1);
    
    /** 调试方法，可删 */
    public static void main(String[] filePath){
        System.out.println("请输入dcm文件夹");
//        Scanner scanner = new Scanner(System.in);
//        File dir = new File(scanner.next());
        File dir = new File("C:\\Users\\99324\\Desktop\\新建文件夹-webp\\DICOMDIR");
        System.out.println("请输入webp文件夹");
//        File webpDir = new File(scanner.next());
        File webpDir = new File("C:\\Users\\99324\\Desktop\\新建文件夹-webp\\DICOMDIR2");
        File[] files = dir.listFiles();
        System.out.println("请输入线程数量");
//        int n = scanner.nextInt();
//        service = Executors.newFixedThreadPool(n);
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionQuality(1.0f);
        long temp = System.currentTimeMillis();
        System.out.println("当前占用内存总数" + Runtime.getRuntime().totalMemory());
        System.out.println("进程号：" + getProcessID());
        for (File file : files) {
            service.execute(() -> {
                Dicom2Webp dicom2Webp = new Dicom2Webp();
                dicom2Webp.dcm2webP(file, new File(getChangedFileName(webpDir.getAbsolutePath(),file.getAbsolutePath())),
                    new SingleColorConvertOp(1), writeParam);
            });
        }
        System.out.println("总耗时cost:" + (System.currentTimeMillis() - temp));
        service.shutdown();
    }
    
    public static final int getProcessID(){
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();
    }
    
    private static String getChangedFileName(String dir, String fileName){
        String realfileName = dir + fileName.substring(fileName.lastIndexOf('\\'), fileName.length()) + ".webp";
        System.out.println(realfileName);
        return realfileName;
    }
}
