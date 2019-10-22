package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: linuxtest
 * @description: 下载重命名测试
 * @author: YeDongYu
 * @create: 2018-12-19 20:59
 */
public class UtilDownloadTest {

    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(2);
        cachedThreadPool.execute(new DownloadThread());
        cachedThreadPool.execute(new RenameThread());
    }
}
