package util;

import java.io.File;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2018-12-20 10:14
 */
public class RenameThread implements Runnable{

    @Override
    public synchronized void run () {
        try {
            this.wait(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File source = new File("/data2");
        File target = new File("/data3");
        System.out.println("开始重命名");
        boolean result = source.renameTo(target);
        System.out.println("rename result : " + result );
    }
}
