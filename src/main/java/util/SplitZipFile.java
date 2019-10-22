package util;

import java.io.*;
import java.util.Scanner;

/**
 * @program: linuxtest
 * @description: 分割zip文件
 * @author: YeDongYu
 * @create: 2018-12-10 17:43
 */
public class SplitZipFile {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("please write zip path,like D://temp//DicomDir.zip");
        String zipPath = scanner.nextLine();
        System.out.println("please write partFile store path,like D://temp//temp//");
        String partFileStorePath = scanner.nextLine();
        System.out.println("please write partFile size,unit Mb,like 10");
        int partFileSize = scanner.nextInt();
        splitZipFile(zipPath,partFileStorePath,partFileSize);
    }

    private static void splitZipFile(String zipPath, String partFileStorePath, int partFileSize){
        File zip = new File(zipPath);
        File partFileStoreDir = new File(partFileStorePath);
        if (!zip.exists()){
            System.out.println(zipPath + " not exist");
            return;
        } else if (!partFileStoreDir.isDirectory()){
            partFileStoreDir.mkdirs();
            System.out.println("warn: " + partFileStoreDir + " is not Directory");
        }
        System.out.println("start split Zip File");
        try(FileInputStream fis = new FileInputStream(zipPath)) {
            //将文件分割成partFileSize大小的碎片
            byte[] buf = new byte[partFileSize*1024*1024];
            int len,count = 0;
            while((len=fis.read(buf))!=-1)
            {
                //要在循环内部创建FileOutputStream对象
                try(FileOutputStream fos = new FileOutputStream(partFileStorePath+(count++))){
                    fos.write(buf,0,len);
                    fos.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("please write partFile size,unit Mb,like 10");
        }
        System.out.println("end split Zip File");
    }
}
