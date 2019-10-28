package util.pacs;

import careray.API;
import com.deepwise.dicom.common.TranscodeFormat;
import com.deepwise.dicom.transcode.helper.TranscodeHelper;

import java.io.File;
import java.util.Scanner;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2019-03-20 20:59
 */
public class EncodeDicom {

    public static void main(String[] args) {
        try {
//            /root/1.dcm;
//            /root/2.dcm;
//            1.2.840.10008.1.2.4.70;
            testOnLinux();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDicom(){

    }

    private void testOnloacl (){
        File dicom = new File("C:\\Users\\99324\\Desktop\\0731测试用Dicom\\Dicom剪切\\008370多格式\\native.dcm");
//        File dicom_native = new File("C:\\Users\\99324\\Desktop\\0731测试用Dicom\\Dicom剪切\\008370多格式\\native.dcm");
        File dicom_jpeg = new File("C:\\Users\\99324\\Desktop\\0731测试用Dicom\\Dicom剪切\\008370多格式\\jpeg.dcm");
        File dicom_jpeg2000 = new File("C:\\Users\\99324\\Desktop\\0731测试用Dicom\\Dicom剪切\\008370多格式\\jpeg2000.dcm");
        File dicom_jpegls = new File("C:\\Users\\99324\\Desktop\\0731测试用Dicom\\Dicom剪切\\008370多格式\\jpeg-ls.dcm");
        try {
//            TranscodeHelper.transcode(dicom, dicom_native, TranscodeFormat.NATIVE_DEFAULT.getTransferSyntaxUid());
//            TranscodeHelper.transcode(dicom, dicom_jpeg, TranscodeFormat.JPEG_LOSSLESS.getTransferSyntaxUid());
            TranscodeHelper.transcode(dicom, dicom_jpeg, TranscodeFormat.JPEG_LOSSLESS.getTransferSyntaxUid());
            TranscodeHelper.transcode(dicom, dicom_jpeg2000, TranscodeFormat.J2K_LOSSLESS.getTransferSyntaxUid());
            TranscodeHelper.transcode(dicom, dicom_jpegls, TranscodeFormat.JPEG_LS_LOSSLESS.getTransferSyntaxUid());
        } catch (Exception e) {
            // do nothing
        }
    }

    private static void testOnLinux() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要压缩的文件位置，如/root/data1/1.dcm");
        System.out.println(scanner.nextLine());
//        File src = new File(scanner.nextLine());
//        System.out.println("请输入压缩后的文件位置，如/root/data1/2.dcm");
//        File tar = new File(scanner.nextLine());
//        System.out.println("请输入压缩格式，如1，2，3");
//        String type = scanner.nextLine();
//        TranscodeHelper.transcode(src, tar, TranscodeFormat.forUid(type).getTransferSyntaxUid());
        File src = new File("/root/1.dcm");
        File tar = new File("/root/2.dcm");
        API.dcmCjpeg(src.getAbsolutePath(),tar.getAbsolutePath());
    }
}
