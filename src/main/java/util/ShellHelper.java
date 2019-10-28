package util;

import com.deepwise.cloud.util.UtilLogger;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行linux命令或shell脚本
 *
 * @author cairui
 * @version $Id: ShellHelper, v 0.1 2018/7/10 17:48 cairui Exp $
 */
public class ShellHelper {

    static Logger LOGGER = LoggerFactory.getLogger(ShellHelper.class);

    /**
     * 调用shell脚本
     *
     * @param scriptFilePath 脚本文件绝对路径
     * @param args           参数
     * @return
     */
    public static String callScript(String scriptFilePath, String... args) throws Exception {
        //1. check file exist
        File file = new File(scriptFilePath);
        if (!file.exists()) {
            throw new Exception(MessageFormat.format("shell脚本文件不存在：{0}", scriptFilePath));
        }
        //2. join args with space
        String argsStr = "";
        if (args != null && args.length > 0) {
            argsStr = StringUtils.join(args, " ");
        }
        //3. exec cmd
        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        String result;
        try {
            //3.1 exec cmd and wait for exec status
            String cmd = scriptFilePath + " " + argsStr;
            process = Runtime.getRuntime().exec(cmd);
            int status = process.waitFor();
            if (status == 0) {
                //3.2 exec success
                bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                result = processReader(bufrIn);
            } else {
                //3.3 exec error
                bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
                String outErr = processReader(bufrError);
                throw new Exception(MessageFormat.format("执行shell脚本异常，文件：{0}，错误信息：{1}", scriptFilePath, outErr));
            }
        } finally {
            closeReader(bufrIn);
            closeReader(bufrError);
            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    /**
     * 读取结果
     *
     * @param br
     * @return
     * @throws Exception
     */
    private static String processReader(BufferedReader br) throws Exception {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    /**
     * 关闭流
     *
     * @param stream
     */
    private static void closeReader(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                UtilLogger.error(LOGGER, e);
            }
        }
    }

    /**
     * 通过服务名称判断服务是否运行
     *
     * @param service 服务名称
     * @return boolean
     */
    public static boolean isServiceRunning(String service) {
        if (StringUtils.isBlank(service)) {
            return false;
        }
        boolean status = false;
        BufferedReader br = null;
        try {
            String command = "service " + service + " status";
            Process ps = Runtime.getRuntime().exec(command);
            br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Active")) {
                    if (line.contains("running")) {
                        status = true;
                        break;
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            UtilLogger.error(LOGGER, e, "通过服务名称判断服务是否运行异常");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    UtilLogger.error(LOGGER, e, "关闭BufferedReader异常");
                }
            }
        }
        return status;
    }

    /**
     * 重启服务
     *
     * @param service 服务名
     */
    public static void restartService(String service) {
        String command = "service " + service + " restart";
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            UtilLogger.error(LOGGER, e, "重启" + service + "服务异常");
        }
    }

    /**
     * 重启服务并同步检查是否启动成功，一定时间未启动，设置超时时间，退出当前同步检查
     * @param service 服务名
     * @param timeout 超时时间
     */
    public static void restartAndCheckStatus(String service, long timeout){
        //重启服务
        restartService(service);
        //记录开始时间
        Date startTime = new Date();
        //判断服务是否启动成功
        while(!isServiceRunning(service)) {
            //设置超时时间，超时退出
            if(differSecond(startTime, new Date()) > timeout){
                UtilLogger.infoByFormat(LOGGER, "Restart aiclassifier to check if the restart success time exceeds 150 seconds, break!");
                break;
            }
            //未超时，每隔1s检查服务是否启动成功
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                UtilLogger.error(LOGGER, e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 计算两个时间差值（秒数）
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long differSecond(Date startDate, Date endDate) {
        return (endDate.getTime() - startDate.getTime()) / 1000;
    }
}
