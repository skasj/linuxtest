package util.filp;


import com.deepwise.dicom.common.TranscodeFormat;
import com.deepwise.dicom.transcode.helper.TranscodeHelper;

import java.io.File;

public class FlipPixelHackerTest {
    
    public static void main(String[] args) throws Exception{
        File src = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0");
        File src_native = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0_native.dcm");
        File src_jpeg = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0_jpeg.dcm");
        File src_jpeg2000 = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0_jpeg2000.dcm");
        File src_jpegls = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0_jpegLs.dcm");
        File dest = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0-Dest-flip.dcm");
        File dest_native = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0_Dest_native.dcm");
        File dest_jpeg = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0_Dest_jpeg.dcm");
        File dest_jpeg2000 = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0_Dest_jpeg2000.dcm");
        File dest_jpegls = new File("C:\\Users\\99324\\Desktop\\翻转opencv\\IM0_Dest_jpegLs.dcm");
        TranscodeHelper.transcode(src, dest, TranscodeFormat.NATIVE_EL.getTransferSyntaxUid(), new FlipPixelHacker());
        TranscodeHelper.transcode(src_native, dest_native, TranscodeFormat.NATIVE_EL.getTransferSyntaxUid(), new FlipPixelHacker());
        TranscodeHelper.transcode(src_jpeg, dest_jpeg, TranscodeFormat.NATIVE_EL.getTransferSyntaxUid(), new FlipPixelHacker());
        TranscodeHelper.transcode(src_jpeg2000, dest_jpeg2000, TranscodeFormat.NATIVE_EL.getTransferSyntaxUid(), new FlipPixelHacker());
        TranscodeHelper.transcode(src_jpegls, dest_jpegls, TranscodeFormat.NATIVE_EL.getTransferSyntaxUid(), new FlipPixelHacker());
    }
}
