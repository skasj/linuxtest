package util;

import java.io.*;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2018-12-20 10:25
 */
public class DownloadThread implements Runnable {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private static final int EOF = -1;

    @Override
    public synchronized void run () {
        File target = new File("/data2/target");
        File source = new File("/data2/source");
        File dir = target.getParentFile();
        dir.mkdirs();
        OutputStream output = null;
        InputStream input = null;
        try {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            input = new FileInputStream(source);
            output = new FileOutputStream(target);
            long count = 0;
            int n = 0;
            System.out.println("开始下载");
            while (EOF != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
                System.out.println("已经下载字节数:" + count);
                this.wait(100);
            }
            System.out.println("下载结束");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
