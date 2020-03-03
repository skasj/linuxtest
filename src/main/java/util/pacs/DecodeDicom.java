package util.pacs;

import com.deepwise.dicom.common.TranscodeFormat;
import com.deepwise.dicom.transcode.helper.TranscodeHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * @program: linuxtest
 * @description: 解压
 * @author: YeDongYu
 * @create: 2019-03-20 20:59
 */
public class DecodeDicom {

    public static void main(String[] args) {
        System.loadLibrary("opencv_java320");
        try {
           new  DecodeDicom().testOnloacl();
//            testOnLinux();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testOnloacl() {
        File dicom = new File("C:\\Users\\99324\\Desktop\\pacsView\\打印机\\打印调试Dicom\\1.dcm");
        File dicom_native = new File("C:\\Users\\99324\\Desktop\\pacsView\\打印机\\打印调试Dicom\\1_native.dcm");
        File dicom_jpeg = new File("C:\\Users\\99324\\Desktop\\pacsView\\打印机\\打印调试Dicom\\1_jpeg.dcm");
        File dicom_jpeg2000 = new File("C:\\Users\\99324\\Desktop\\pacsView\\打印机\\打印调试Dicom\\1_2000.dcm");
        File dicom_jpegls = new File("C:\\Users\\99324\\Desktop\\pacsView\\打印机\\打印调试Dicom\\1-jpeg-ls.dcm");
        try {
            TranscodeHelper.transcode(dicom, dicom_native, TranscodeFormat.NATIVE_DEFAULT.getTransferSyntaxUid());
//            TranscodeHelper.transcode(dicom, dicom_jpeg, TranscodeFormat.JPEG_LOSSLESS.getTransferSyntaxUid());
//            TranscodeHelper.transcode(dicom, dicom_jpeg, TranscodeFormat.JPEG_LOSSLESS.getTransferSyntaxUid());
            TranscodeHelper.transcode(dicom, dicom_jpeg2000, TranscodeFormat.J2K_LOSSLESS.getTransferSyntaxUid());
            TranscodeHelper.transcode(dicom, dicom_jpegls, TranscodeFormat.JPEG_LS_LOSSLESS.getTransferSyntaxUid());
            TranscodeHelper.transcode(dicom_jpeg2000, dicom_jpeg, TranscodeFormat.JPEG_LS_LOSSLESS.getTransferSyntaxUid());
        } catch (Exception e) {
            // do nothing
        }
    }

    private static void testOnLinux() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要压缩的文件目录，如/root/data1/");
        String scrDir = scanner.nextLine();
        System.out.println("请输入安置目录，如/root/data2/ 注意不要与上一路径重复");
        String tarDir = scanner.nextLine();
//        File src = new File(scanner.nextLine());
//        System.out.println("请输入压缩后的文件位置，如/root/data1/2.dcm");
//        File tar = new File(scanner.nextLine());
        for (TranscodeFormat transcodeFormat : TranscodeFormat.values()) {
            System.out.printf("格式：%s 说明：%s\n",
                    transcodeFormat.getTransferSyntaxUid(), transcodeFormat.getDesc());
        }
        System.out.println("\n\n请输入目标格式，如需要解压时输入1.2.840.10008.1.2");
        String type = scanner.nextLine();

        DecodeDicom decodeDicom = new DecodeDicom();
        File tarDirFile = new File(tarDir);
        if (tarDirFile.exists()) {
            FileUtils.deleteDirectory(tarDirFile);
        }
        List<File> fileList = decodeDicom.getAllFile(new File(scrDir));
        if (CollectionUtils.isEmpty(fileList)) {
            System.out.println("目录下没有文件，请确认");
            return;
        }
        int count = fileList.size();
        System.out.println("文件总数"+count);
        for (File src : fileList) {
            File tar = new File(src.getAbsolutePath().replace(scrDir, tarDir));
            if (!tar.getParentFile().exists()) {
                tar.getParentFile().mkdirs();
            }
            try {
                TranscodeHelper.transcode(src, tar, TranscodeFormat.forUid(type).getTransferSyntaxUid());
            } catch (Exception e) {
                System.out.println(src.getAbsolutePath() + "转码失败，跳过");
                e.printStackTrace();
            }
            count--;
            System.out.println("剩余未处理文件数量"+count);
        }
    }

    private List<File> getAllFile(File file) {
        List<File> fileList = new ArrayList<>();
        if (null == file || !file.exists()) {
            return fileList;
        }
        if (!file.isDirectory()) {
            fileList.add(file);
            return fileList;
        }
        File[] files = file.listFiles();
        if (ArrayUtils.isEmpty(files)) {
            return fileList;
        }
        for (File file1 : files) {
            fileList.addAll(getAllFile(file1));
        }
        return fileList;
    }
}
