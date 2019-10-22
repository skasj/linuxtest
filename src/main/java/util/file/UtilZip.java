package util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by lanxuewei in 17:09 2018/3/14
 * 文件压缩以及解压
 */
public class UtilZip {

    private static final Logger logger = LoggerFactory.getLogger(UtilZip.class);

    /**
     * 实现文件压缩 外部调用接口
     * @param filePath 待压缩文件或文件夹路径
     */
    public static void startCompress(String filePath) {
        File file = new File(filePath);
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream= new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath() + ".zip")));
            String base = file.getName();
            compressZip(zipOutputStream, file, base);  //进行压缩
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipOutputStream != null) {             //关闭
                try {
                    zipOutputStream.closeEntry();
                } catch (IOException e) {
                    logger.error("", e);
                }
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
    }

    /**
     * 因为子文件夹中可能还有文件夹，所以进行递归
     * @param zipOutput
     * @param file 待压缩文件或文件夹
     * @param base
     * @exception FileNotFoundException file文件不存在
     */
    private static void compressZip(ZipOutputStream zipOutput, File file, String base) throws FileNotFoundException{
        if (file.exists()) {  //文件路径存在
            if (file.isDirectory()) {              //文件夹
                File[] files = file.listFiles();   //获得当前目录下所有文件以及文件夹
                if (files != null) {
                    for (File item : files) {
                        if (item.isDirectory()) {  //递归
                            compressZip(zipOutput, item, base);
                        } else {                   //压缩
                            zip(zipOutput, item, base);
                        }
                    }
                }
            } else {  //文件
                zip(zipOutput, file, base);
            }
        }
    }

    /**
     * 具体实现文件压缩
     * @param zipOutput
     * @param file 待压缩文件或文件夹
     * @param base
     */
    private static void zip(ZipOutputStream zipOutput, File file, String base) {
        ZipEntry zipEntry = new ZipEntry(base + File.separator + file.getName());  //ZipEntry为即将要压缩文件的文件信息
        BufferedInputStream bufferedInputStream = null;
        try {
            zipOutput.putNextEntry(zipEntry);  //将其添加到压缩包中，准备进行压缩
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = bufferedInputStream.read(buffer)) != -1) {
                zipOutput.write(buffer, 0, read);
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }

    }

    /**
     * zip压缩文件解压 外部调用接口
     * @param zipFilePath 解压文件路径
     * @throws FileNotFoundException 解压文件路径错误
     */
    public static void decompression(String zipFilePath) throws FileNotFoundException {
        File file = new File(zipFilePath);
        if (file.exists()) {
            ZipInputStream zipInputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                zipInputStream = new ZipInputStream(
                        new BufferedInputStream(new FileInputStream(file)));
                File fout = null;
                String parent = file.getParent();
                ZipEntry zipEntry;
                while ((zipEntry = zipInputStream.getNextEntry()) != null &&
                        !zipEntry.isDirectory()) {
                    fout = new File(parent, zipEntry.getName());
                    if (!fout.exists()) {
                        (new File(fout.getParent())).mkdirs();
                    }
                    bufferedOutputStream = new BufferedOutputStream(
                            new FileOutputStream(fout));
                    int read = 0;
                    byte[] buffer = new byte[1024];
                    while ((read = zipInputStream.read(buffer)) != -1) {
                        bufferedOutputStream.write(buffer, 0, read);
                        bufferedOutputStream.flush();
                    }
                }
            } catch (IOException e) {
                logger.error("", e);
            } finally {
                if (zipInputStream != null) {
                    try {
                        zipInputStream.close();
                    } catch (IOException e) {
                        logger.error("", e);
                    }
                }
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e) {
                        logger.error("", e);
                    }
                }
            }
        } else {
            throw new FileNotFoundException("zipFilePath");
        }
    }
}
