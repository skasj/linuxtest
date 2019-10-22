package util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by lanxuewei in 10:48 2018/3/22
 */
public class UtilFile {

    private static final Logger logger = LoggerFactory.getLogger(UtilFile.class);

    public UtilFile() {}

    /**
     * create Dir if Dir is not exist
     * @param dirPath
     * @return file
     */
    public static File createDirIfNotExist (String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 删除空目录，即使其中包含空目录
     * @param file 待删除文件夹
     */
    public static void deleteDirIfEmpty(File file) {
        if (file != null && file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File item : files) {
                    if (item.isDirectory()) {
                        deleteDirIfEmpty(item);  //递归到最后目录
                        File[] files1 = item.listFiles();
                        if (files1 != null && files1.length == 0) {
                            item.delete();      //如果是文件夹里面没有文件则为空，删除
                        }
                    }
                }
            }
        }
    }

    /**
     * delete directory
     * 用处：删除文件目录下所有文件以及文件夹，File的 delete() 函数只能删除文件或者空目录
     * @param filePath 待删除文件目录
     */
    public static void deleteDir(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {  //目录存在
            deleteFile(file); //先删除其中文件
            file.delete();    //删除顶级空目录
        }
    }

    /**
     * 递归删除目录下所有文件
     * @param file 待删除文件目录
     */
    private static void deleteFile(File file) {
        if (file.exists()) {                   //目录存在
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File item : files) {  //递归
                        deleteFile(item);
                    }
                }
            } else {  //文件 删除
                file.delete();
            }
        }
    }

    /**
     * 删除文件
     * @param fileString 文件
     * @return 操作是否成功
     */
    public static boolean deleteFile(String fileString) {
        File file = new File(fileString);
        if (file.exists() && file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否完整(有可能还在写入)
     * 思路：获取文件大小，线程等待0.3s，再获取文件大小，如果两次一致，则文件完整，否则不完整。
     * @param file 原文件
     * @return true->文件完整，false->文件不完整
     */
    /*public static boolean isFileComplete(File file) {
        if (file != null && file.exists()) {
            long firstFileSize = file.length();
            try {
                Thread.sleep(300);          //线程等待0.3s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long lastFileSize = file.length();
            if (firstFileSize == lastFileSize) {  //表示文件没有写入，即文件完整
                return true;
            }
        }
        return false;
    }*/

    /**
     * 该路径下文件不存在则创建该文件
     * @param filePath 文件路径以及文件名 例: D:\\test\\a.txt
     */
    public static void createFileIfNotExist(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            try {
                file.createNewFile();  //文件不存在才会创建
            } catch (IOException e) {
                logger.error("", e);
            }
        } else {
            logger.info("create file fail,the filePath is null!");
        }
    }

    /**
     * 判断文件类型是否为 dcm
     * 思路：跳过128字节导言部分，读取前4个字节，进行判断确认是否为 44 49 43 4d
     * @param file source file
     * @return
     */
    public static boolean isDcmFile(File file) {
        if (file.exists() && file.isFile()) {
            BufferedInputStream bufferedInputStream = null;
            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                byte[] buff = new byte[132];  //只读取128导言和4位文件头标识
                bufferedInputStream.read(buff, 0, buff.length);
                String fileHeadCode = bytesToHexString(buff);
                String realFileHeadCode = null;
                if (fileHeadCode != null) {
                    realFileHeadCode = fileHeadCode.substring(256, fileHeadCode.length()).toLowerCase();  //截取真正文件头
                }
                if (realFileHeadCode != null && realFileHeadCode.equals("4449434d")) {
                    logger.debug("the file is a dcm file.");
                    return true;
                }
                logger.info("the file is not a dcm file.");
                return false;
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
        return false;
    }

    /**
     * change buff[] to String
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if(null==src || src.length <= 0){
            return null;
        }
        for(int i = 0; i < src.length; i++){
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if(hv.length() < 2){
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
