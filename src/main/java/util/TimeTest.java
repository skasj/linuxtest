package util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: linuxtest
 * @description: 测试Linux上获取各类时间
 * @author: YeDongYu
 * @create: 2019-01-15 18:56
 */
public class TimeTest {
    public static void main(String[] args) {
        System.out.println("系统时间");
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date(System.currentTimeMillis()));
        File dir = new File("/test/TimeTest");
        dir.mkdirs();
        System.out.println("文件夹创建时间");
        System.out.println(dir.lastModified());
        System.out.println(new Date(dir.lastModified()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-");
        File txt = new File("/test/TimeTest/1.txt");
        try {
            txt.createNewFile();
            System.out.println("文件创建时间");
            System.out.println(txt.lastModified());
            System.out.println(new Date(txt.lastModified()));
            System.out.println("文件夹修改时间");
            System.out.println(dir.lastModified());
            System.out.println(new Date(dir.lastModified()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
