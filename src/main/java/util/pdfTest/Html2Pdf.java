package util.pdfTest;

import java.io.File;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import util.ShellHelper;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2019-10-25 16:03
 */
public class Html2Pdf {

    private final static String DOWNLOAD_TEMPLATE_PATH = "/root/aisvr/classes/template/";
    private final static String TEMPLATE_REPORT_FILE = "report.html";
    private final static String TEMPLATE_FILE_SUFFIX = ".html";
    private final static String EXPORT_FILE_SUFFIX = ".pdf";
    private final static String TEMPLATE_FILE_TAG_BODY = "<body>";
    private final static String EXPORT_REPORT_SHELL = "/root/aisvr/export_report.sh";

    /**
     * 图文报告下载pdf
     *
     * @param html html
     * @return pdf文件
     */
    public File convertReportHtml2Pdf(String html, String pdfFileName) {
        File pdf = null;
        File tmpTemplate = null;
        try {
            File template = new File(DOWNLOAD_TEMPLATE_PATH, TEMPLATE_REPORT_FILE);
            String htmlString = FileUtils.readFileToString(template);
            tmpTemplate = new File(DOWNLOAD_TEMPLATE_PATH, pdfFileName + TEMPLATE_FILE_SUFFIX);
            htmlString = htmlString.replace(TEMPLATE_FILE_TAG_BODY, TEMPLATE_FILE_TAG_BODY + html);
            FileUtils.writeStringToFile(tmpTemplate, htmlString);
            ShellHelper.callScript(EXPORT_REPORT_SHELL, pdfFileName);
            pdf = new File(DOWNLOAD_TEMPLATE_PATH, pdfFileName + EXPORT_FILE_SUFFIX);
            checkPdf(pdf, 30);
            if (!pdf.exists()) {
                System.out.println(pdfFileName + "生成失败");
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (tmpTemplate != null && tmpTemplate.exists()) {
               tmpTemplate.delete();
            }
        }
        return pdf;
    }

    /**
     * 检查pdf文件是否生成
     *
     * @param pdfFile pdf
     * @param timeout 超时时间，单位：s
     */
    public static void checkPdf(File pdfFile, long timeout) {
        //记录开始时间
        Date startTime = new Date();
        //判断pdf是否生成
        while (!pdfFile.exists()) {
            //设置超时时间，超时退出
            if (ShellHelper.differSecond(startTime, new Date()) > timeout) {
                break;
            }
            //未超时，每隔1s检查pdf是否生成
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
