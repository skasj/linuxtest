package util;

import com.alibaba.fastjson.JSONObject;
import com.deepwise.cloud.util.UtilLogger;
import org.apache.commons.lang.StringUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @program: DeepwiseTool
 * @description: 图片翻转
 * @author: YeDongYu
 * @create: 2019-01-23 10:14
 */
public class UtilFlipImage {

    static Logger LOGGER = LoggerFactory.getLogger(UtilFlipImage.class);

    public static final int FLIP_HORIZONTALLY = 1;
    public static final int FLIP_VERTICALLY = 2;
    public static final int FLIP_HORIZONTALLY_VERTICALLY = 0;
    
    /**
     * 改变DCM文件中的像素
     * Extract the array element from the Raster DataBuffer;
     **/
    public static byte[] flipDICOMPixelData(File dicom, Attributes attributes, int flipCode) {
        if (null == dicom || !dicom.exists() || null == attributes) {
            return new byte[0];
        }

        // 暂时认为图片的长宽都存在且正确，如果长宽不存在可以尝试使用openCV的方法翻转
        int imageRows = Integer.parseInt(attributes.getString(Tag.Rows));
        int imageColumns = Integer.parseInt(attributes.getString(Tag.Columns));

        short[] pixelData = getPixeldata(dicom);
        if (null == pixelData) {
            return new byte[0];
        }
        try {
            pixelData = flipPixelData(pixelData, imageRows, imageColumns, flipCode);
            return shortArrToByteArr(pixelData);
        } catch (Exception e) {
            UtilLogger.error(LOGGER, e, String.format("flip dicom error:{}", dicom.getAbsolutePath()));
            return new byte[0];
        }
    }

    /**
     * 读取Dicom文件的像素信息
     *
     * @param dicom
     * @return
     */
    private static short[] getPixeldata(File dicom) {
        if (null == dicom || !dicom.exists()) {
            return null;
        }
        try (ImageInputStream iis = ImageIO.createImageInputStream(dicom)) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis); // 包含了解压步骤
            ImageReader reader = iter.next();
            DicomImageReadParam param = (DicomImageReadParam) reader.getDefaultReadParam();
            reader.setInput(iis, false);
            Raster raster = reader.readRaster(0, param); // sof Exception
            DataBufferUShort buffer;
            if (raster != null) {
                // TODO: 2019/4/19 需要兼容DateBufferInt DateBufferNative等情况
                buffer = (DataBufferUShort) raster.getDataBuffer();
                return buffer.getData();               //像素数组
            }
        } catch (IOException e) {
            LOGGER.error(String.format("get pixel data of dicom file(%s) error", dicom.getAbsolutePath()), e);
        }
        return null;
    }
    
    /**
     * @param sArr
     * @return attribute不支持 setShorts 所以做个转换
     */
    private static byte[] shortArrToByteArr(short[] sArr) {
        if (sArr == null || sArr.length == 0) {
            return new byte[0];
        }
        byte[] byteArr = new byte[sArr.length * 2];
        short s;
        for (int i = 0, j = 0; j < sArr.length; ) {
            s = sArr[j++];
            byteArr[i++] = (byte) s;
            byteArr[i++] = (byte) (s >> 8);
        }
        return byteArr;
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
                return flipHorizontallyVertically(pixelData, rowN, colN);
            default:
                return null;
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
     * @param rowN
     * @param colN
     * @return
     */
    private static short[] flipHorizontallyVertically(short[] source, int rowN, int colN) {
        short[] target = new short[source.length];
        int sourceNum = 0;
        int targetNum = source.length - 1;
        while (targetNum >= 0) {
            target[targetNum--] = source[sourceNum++];
        }
        return target;
    }
}
