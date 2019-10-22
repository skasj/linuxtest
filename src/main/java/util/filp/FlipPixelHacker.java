package util.filp;

import com.deepwise.dicom.transcode.component.AbstractPixelHacker;
import com.deepwise.dicom.transcode.helper.TranscodeHelper;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;


/**
 * @program: dw_cloud_dfs
 * @description:
 * @author: YeDongYu
 * @create: 2019-07-09 14:15
 */
public class FlipPixelHacker extends AbstractPixelHacker {
    
    static {
        TranscodeHelper.resetImageReaderFactory();
    }
    
    public static final int FLIP_HORIZONTALLY = 1;
    public static final int FLIP_VERTICALLY = 2;
    public static final int FLIP_HORIZONTALLY_VERTICALLY = 0;
    
    @Override
    public void handlePixelByte(byte[] pixel, Attributes dataset) throws Exception{
        // 暂时认为图片的长宽都存在且正确，如果长宽不存在可以尝试使用openCV的方法翻转
        int imageRows = Integer.parseInt(dataset.getString(Tag.Rows));
        int imageColumns = Integer.parseInt(dataset.getString(Tag.Columns));
//        int turnMode = Integer.parseInt(dataset.getString(Tag.PixelData));
        int turnMode = FLIP_HORIZONTALLY_VERTICALLY;
        byte[] target = flipPixelData(pixel,imageRows,imageColumns,turnMode);
        if (null == target || 0 == target.length){
            return;
        }
        for (int i=0;i<pixel.length;i++){
            pixel[i] = target[i];
        }
    }
    
    @Override
    public void handlePixelShort(short[] pixel, Attributes dataset) throws Exception{
        // 暂时认为图片的长宽都存在且正确，如果长宽不存在可以尝试使用openCV的方法翻转
        int imageRows = Integer.parseInt(dataset.getString(Tag.Rows));
        int imageColumns = Integer.parseInt(dataset.getString(Tag.Columns));
//        int turnMode = Integer.parseInt(dataset.getString(Tag.PixelData));
        int turnMode = FLIP_HORIZONTALLY_VERTICALLY;
        short[] target = flipPixelData(pixel,imageRows,imageColumns,turnMode);
        if (null == target || 0 == target.length){
            return;
        }
        for (int i=0;i<pixel.length;i++){
            pixel[i] = target[i];
        }
    }
    
    @Override
    public void handlePixelInt(int[] pixel, Attributes dataset) throws Exception{
        // 暂时认为图片的长宽都存在且正确，如果长宽不存在可以尝试使用openCV的方法翻转
        int imageRows = Integer.parseInt(dataset.getString(Tag.Rows));
        int imageColumns = Integer.parseInt(dataset.getString(Tag.Columns));
//        int turnMode = Integer.parseInt(dataset.getString(Tag.PixelData));
        int turnMode = FLIP_HORIZONTALLY_VERTICALLY;
        int[] target = flipPixelData(pixel,imageRows,imageColumns,turnMode);
        if (null == target || 0 == target.length){
            return;
        }
        for (int i=0;i<pixel.length;i++){
            pixel[i] = target[i];
        }
    }
    
    /**
     * 翻转图片的像素信息
     *
     * @param pixelData 像素信息
     * @param rowN      行数
     * @param colN      列数
     * @param flipCode  翻转方式，1水平翻转，0垂直翻转，-1水平垂直翻转
     * @return
     */
    private static short[] flipPixelData(short[] pixelData, int rowN, int colN, int flipCode) {
        switch (flipCode) {
            case FLIP_HORIZONTALLY:
                return flipHorizontally(pixelData, rowN, colN);
            case FLIP_VERTICALLY:
                return flipVertically(pixelData, rowN, colN);
            case FLIP_HORIZONTALLY_VERTICALLY:
                return flipHorizontallyVertically(pixelData);
            default:
                return pixelData;
        }
    }
    
    /**
     * 水平翻转
     *
     * @param source
     * @param rowN
     * @param colN
     * @return
     */
    private static short[] flipHorizontally(short[] source, int rowN, int colN) {
        short[] target = new short[source.length];
        int sourceNum = 0;
        for (int y = 0; y < rowN; y++) {
            int targetNum = colN * (y + 1) - 1;
            for (int x = 0; x < colN; x++) {
                target[targetNum--] = source[sourceNum++];
            }
        }
        return target;
    }
    
    /**
     * 垂直翻转
     *
     * @param source
     * @param rowN
     * @param colN
     * @return
     */
    private static short[] flipVertically(short[] source, int rowN, int colN) {
        short[] target = new short[source.length];
        int sourceNum = 0;
        for (int y = 0; y < rowN; y++) {
            int targetNum = colN * (rowN - 1 - y);
            for (int x = 0; x < colN; x++) {
                target[targetNum++] = source[sourceNum++];
            }
        }
        return target;
    }
    
    /**
     * 水平垂直翻转
     *
     * @param source
     * @return
     */
    private static short[] flipHorizontallyVertically(short[] source) {
        short[] target = new short[source.length];
        int sourceNum = 0;
        int targetNum = source.length - 1;
        while (targetNum >= 0) {
            target[targetNum--] = source[sourceNum++];
        }
        return target;
    }
    
    /**
     * 翻转图片的像素信息
     *
     * @param pixelData 像素信息
     * @param rowN      行数
     * @param colN      列数
     * @param flipCode  翻转方式，1水平翻转，0垂直翻转，-1水平垂直翻转
     * @return
     */
    private static int[] flipPixelData(int[] pixelData, int rowN, int colN, int flipCode) {
        switch (flipCode) {
            case FLIP_HORIZONTALLY:
                return flipHorizontally(pixelData, rowN, colN);
            case FLIP_VERTICALLY:
                return flipVertically(pixelData, rowN, colN);
            case FLIP_HORIZONTALLY_VERTICALLY:
                return flipHorizontallyVertically(pixelData);
            default:
                return pixelData;
        }
    }
    
    /**
     * 水平翻转
     *
     * @param source
     * @param rowN
     * @param colN
     * @return
     */
    private static int[] flipHorizontally(int[] source, int rowN, int colN) {
        int[] target = new int[source.length];
        int sourceNum = 0;
        for (int y = 0; y < rowN; y++) {
            int targetNum = colN * (y + 1) - 1;
            for (int x = 0; x < colN; x++) {
                target[targetNum--] = source[sourceNum++];
            }
        }
        return target;
    }
    
    /**
     * 垂直翻转
     *
     * @param source
     * @param rowN
     * @param colN
     * @return
     */
    private static int[] flipVertically(int[] source, int rowN, int colN) {
        int[] target = new int[source.length];
        int sourceNum = 0;
        for (int y = 0; y < rowN; y++) {
            int targetNum = colN * (rowN - 1 - y);
            for (int x = 0; x < colN; x++) {
                target[targetNum++] = source[sourceNum++];
            }
        }
        return target;
    }
    
    /**
     * 水平垂直翻转
     *
     * @param source
     * @return
     */
    private static int[] flipHorizontallyVertically(int[] source) {
        int[] target = new int[source.length];
        int sourceNum = 0;
        int targetNum = source.length - 1;
        while (targetNum >= 0) {
            target[targetNum--] = source[sourceNum++];
        }
        return target;
    }
    
    /**
     * 翻转图片的像素信息
     *
     * @param pixelData 像素信息
     * @param rowN      行数
     * @param colN      列数
     * @param flipCode  翻转方式，1水平翻转，0垂直翻转，-1水平垂直翻转
     * @return
     */
    private static byte[] flipPixelData(byte[] pixelData, int rowN, int colN, int flipCode) {
        switch (flipCode) {
            case FLIP_HORIZONTALLY:
                return flipHorizontally(pixelData, rowN, colN);
            case FLIP_VERTICALLY:
                return flipVertically(pixelData, rowN, colN);
            case FLIP_HORIZONTALLY_VERTICALLY:
                return flipHorizontallyVertically(pixelData);
            default:
                return pixelData;
        }
    }
    
    /**
     * 水平翻转
     *
     * @param source
     * @param rowN
     * @param colN
     * @return
     */
    private static byte[] flipHorizontally(byte[] source, int rowN, int colN) {
        byte[] target = new byte[source.length];
        int sourceNum = 0;
        for (int y = 0; y < rowN; y++) {
            int targetNum = colN * (y + 1) - 1;
            for (int x = 0; x < colN; x++) {
                target[targetNum--] = source[sourceNum++];
            }
        }
        return target;
    }
    
    /**
     * 垂直翻转
     *
     * @param source
     * @param rowN
     * @param colN
     * @return
     */
    private static byte[] flipVertically(byte[] source, int rowN, int colN) {
        byte[] target = new byte[source.length];
        int sourceNum = 0;
        for (int y = 0; y < rowN; y++) {
            int targetNum = colN * (rowN - 1 - y);
            for (int x = 0; x < colN; x++) {
                target[targetNum++] = source[sourceNum++];
            }
        }
        return target;
    }
    
    /**
     * 水平垂直翻转
     *
     * @param source
     * @return
     */
    private static byte[] flipHorizontallyVertically(byte[] source) {
        byte[] target = new byte[source.length];
        int sourceNum = 0;
        int targetNum = source.length - 1;
        while (targetNum >= 0) {
            target[targetNum--] = source[sourceNum++];
        }
        return target;
    }
    
//    public static void main(String[] args){
//        int[] is = {1,2,3,4,5};
//        test(is);
//        System.out.println(Arrays.toString(is));
//    }
//
//    private static void test(int[] is){
//        int[] target = {2,3,4,6};
//        is = target;
////        is[2] = 4;
//    }
}
