package util.file;

import java.io.File;

/**
 * Created by lanxuewei in 18:05 2018/3/22
 * 文件名解析：将文件名解析成文件名和后缀名
 */
public class FileNameAnalysis {

    private String realFileName;  //文件名
    private String format;        //文件后缀

    public FileNameAnalysis() {}

    /**
     * 解析文件名
     * @param file 待解析文件
     */
    public void analysisFile(File file) {
        if (!file.exists()) {
            realFileName = "";
            format = "";
            return;
        }
        String fileNameString = file.getName();
        int index = fileNameString.lastIndexOf(".");
        if (index == -1) {  //文件无后缀
            realFileName = fileNameString;
            format = "";
        } else {            //文件包含后缀
            realFileName = fileNameString.substring(0, index);
            format = fileNameString.substring(index, fileNameString.length());
        }
    }

    public String getRealFileName() {
        return realFileName;
    }

    public String getFormat() {
        return format;
    }
}
