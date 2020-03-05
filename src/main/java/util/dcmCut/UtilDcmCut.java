package util.dcmCut;

import com.alibaba.fastjson.JSONObject;
import com.deepwise.cloud.util.UtilLogger;
import com.deepwise.dicom.pixel.PixelPicker;
import com.deepwise.dicom.transcode.helper.TranscodeHelper;
import java.awt.Point;
import java.awt.image.Raster;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.pacs.DicomTag;

/**
 * @program: dw_front_dfs
 * @description: dcm图像裁剪
 * @author: YeDongYu
 * @create: 2019-08-26 13:38
 */
public class UtilDcmCut {

    private UtilDcmCut(){
        throw new IllegalStateException("UtilDcmCut class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilDcmCut.class);

    static {
        TranscodeHelper.useServiceLoader(false);
        TranscodeHelper.resetImageReaderFactory();
        TranscodeHelper.resetImageWriterFactory();
    }

    /**
     * 裁剪乳腺钼靶dcm影像背景
     *
     * @param src
     * @param imageLaterality dcm影像朝向,向左或者向右
     * @param dataset
     */
    public static File cutDicom(File src, String imageLaterality, Attributes dataset, JSONObject cutConfig){
        // 1.0 如果没有配置裁剪，或者配置不正确，则不需要裁剪;如果已经存在此tag值，说明被裁剪过，不需再裁剪;如果无法判断左右，也不用裁剪
        if (isCutConfigError(cutConfig) ||
                StringUtils.isNotBlank(dataset.getString(Constants.CustomDicomTag.CutDcmFlag)) ||
                (!Constants.IMAGE_LATERALITY.L.equalsIgnoreCase(imageLaterality) &&
                        !Constants.IMAGE_LATERALITY.R.equalsIgnoreCase(imageLaterality)) || null == src || !src.exists()) {
            return src;
        }
        try {
            // 2.0 图像解析
            Raster dicomRaster = PixelPicker.pickPixelEach(src, 0);
            if (null == dicomRaster) {
                return src;
            }
            // 3.0 从raster中分析出背景像素值
            Short threshold = analysisThreshold(dicomRaster, imageLaterality);
            if (null == threshold) {
                return src;
            }
            // 4.0 找到非背景边缘点，记录位置
            Point edge = findEdgeByThreshold(dicomRaster, threshold, imageLaterality, cutConfig);
            if (edge == null) {
                UtilLogger.warnByFormat(LOGGER, "{}{}{} 非背景边缘点获取失败，不对原dcm进行裁剪", dataset.getString(Tag.PatientID),
                        dataset.getString(Tag.StudyInstanceUID), dataset.getString(Tag.SeriesInstanceUID));
                return src;
            }
            return cutDicom(src, imageLaterality, dataset, cutConfig, dicomRaster, edge);
        } catch (Exception e) {
            UtilLogger.errorByFormat(LOGGER, "{}{}{} 乳腺钼靶裁剪失败，不对原dcm进行操作", dataset.getString(Tag.PatientID),
                    dataset.getString(Tag.StudyInstanceUID), dataset.getString(Tag.SeriesInstanceUID));
        }
        return src;
    }

    private static File cutDicom(File src, String imageLaterality, Attributes dataset, JSONObject cutConfig,
            Raster dicomRaster, Point edge){
        // 1.0 根据边缘点截取pixels
        int oriW = dicomRaster.getWidth();
        int oriH = dicomRaster.getHeight();
        int cutW = 0;
        int x = 0;
        int y = 0;
        Object obj = null;
        int backPixel = cutConfig.getIntValue(DwConfigParamName.CUT_DCM.BACK_PIXEL);
        if (Constants.IMAGE_LATERALITY.L.equalsIgnoreCase(imageLaterality)) {
            cutW = (int) edge.getX();
            // 1.1 收缩100px
            if (cutW + backPixel < oriW) {
                cutW = cutW + backPixel;
            }
            obj = dicomRaster.getDataElements(x, y, cutW, oriH, obj);
        } else if (Constants.IMAGE_LATERALITY.R.equalsIgnoreCase(imageLaterality)) {
            cutW = oriW - (int) edge.getX();
            x = (int) edge.getX();
            // 1.2 收缩100px
            if (cutW + backPixel < oriW && x - backPixel > 0) {
                cutW = cutW + backPixel;
                x = x - backPixel;
            }
            obj = dicomRaster.getDataElements(x, y, cutW, oriH, obj);
        }
        if (null == obj) {
            return src;
        }
        // 2.0 将裁剪影响相关tagValue存入原dcm
        String originTsuid = dataset.getString(Tag.TransferSyntaxUID);
        if (StringUtils.isBlank(originTsuid)) {
            LOGGER.error("no transfer syntax uid when cutDicom, {}", src.getAbsolutePath());
        }
        boolean bigEndian = StringUtils.equals(originTsuid, UID.ExplicitVRBigEndianRetired);
        byte[] pixels = UtilDataType.shortArrToByteArr(dataElements2ShortArr(obj), bigEndian);
        if (ArrayUtils.isEmpty(pixels)) {
            return src;
        }
        // 决定tsuid
        final String tsuid = bigEndian ? UID.ExplicitVRBigEndianRetired : UID.ExplicitVRLittleEndian;
        // 2.1 图片裁剪一律会先解压，所以这里pixelData的VR默认设为OW即可
        DicomTag cutDcmFlagTag = new DicomTag(Constants.CustomDicomTag.CutDcmFlag, VR.SH, "CUTDCM");
        DicomTag pixelsTag = new DicomTag(Tag.PixelData, VR.OW, pixels);
        // 2.1.1 调用UtilTag.writeTags需要把short类型转为数组后再转为byte[]
        DicomTag colsTag = new DicomTag(Tag.Columns, VR.US,
                UtilDataType.shortArrToByteArr(new short[]{(short) cutW}, bigEndian));
        DicomTag transferSyntaxUidTag = new DicomTag(true, Tag.TransferSyntaxUID, VR.UI, tsuid);

        List<DicomTag> dicomTags = Arrays.asList(cutDcmFlagTag, pixelsTag, colsTag, transferSyntaxUidTag);
        // 3.0 设置剪切后图片的路径
        File dest = new File(src.getParentFile(), UtilFileNameStandardizer.changeUuid(src.getName(), true));
        boolean isSuccess = UtilBaseModifier.basicModifier(src, dest, dicomTags);
        if (isSuccess) {
            FileUtils.deleteQuietly(src);
            dataset.setValue(Tag.TransferSyntaxUID, VR.UI, tsuid);
            return dest;
        }
        return src;
    }

    private static boolean isCutConfigError(JSONObject cutConfig){
        return null == cutConfig ||
                BooleanUtils.isNotTrue(cutConfig.getBoolean(DwConfigParamName.CUT_DCM.IS_CUT_DCM)) ||
                null == cutConfig.getInteger(DwConfigParamName.CUT_DCM.BACK_PIXEL) ||
                null == cutConfig.getDouble(DwConfigParamName.CUT_DCM.CUT_PERCENTAGE);
    }

    /**
     * 从raster中获取背景像素值
     * 左乳影像取最右列，右乳影像取最左列
     * 取其中出现频率最高值
     *
     * @param dicomRaster
     * @param imageLaterality
     * @return
     */
    private static Short analysisThreshold(Raster dicomRaster, String imageLaterality){
        // 1.0 初始化
        Object obj = null;
        Short[] grayDate = null;
        int w = dicomRaster.getWidth();
        int h = dicomRaster.getHeight();
        int rX = dicomRaster.getMinX();
        int rY = dicomRaster.getMinY();
        // 2.0 获取侧边列
        int sideX;
        if (Constants.IMAGE_LATERALITY.L.equalsIgnoreCase(imageLaterality)) {
            // 2.1 左乳影像取最右列
            sideX = rX + w - 1;
        } else if (Constants.IMAGE_LATERALITY.R.equalsIgnoreCase(imageLaterality)) {
            // 2.2 右乳影像取最左列
            sideX = rX;
        } else {
            // 2.3 无法判断左右，直接退出
            return null;
        }
        obj = dicomRaster.getDataElements(sideX, rY, 1, h, obj);
        grayDate = ArrayUtils.toObject(dataElements2ShortArr(obj));
        if (ArrayUtils.isEmpty(grayDate)) {
            return null;
        }
        // 3. 把grayDate转为map<k,v>，k为gratDate的值，v为k在grayDate中出现的次数
        Map<Short, Long> map = Arrays.stream(grayDate).collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        // 4. max方法是根据比较器（按照map的value进行排序）找出最大值
        return map.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).orElse(
                null);
    }

    /**
     * 根据背景像素值，逐列扫描dcm影像，遇到非背景像素停止，记录位置
     * 左乳影像从右向扫描，右乳影像从左向右扫描
     *
     * @param dicomRaster     dicom图片
     * @param threshold       背景色
     * @param imageLaterality 图片朝向L/R
     * @param cutConfig       机构配置
     * @return
     */
    private static Point findEdgeByThreshold(Raster dicomRaster, Short threshold, String imageLaterality,
            JSONObject cutConfig){
        // 1.0 初始化
        int rX = dicomRaster.getMinX();
        int rY = dicomRaster.getMinY();
        int w = dicomRaster.getWidth();
        int h = dicomRaster.getHeight();
        double cutPercentage = cutConfig.getDoubleValue(DwConfigParamName.CUT_DCM.CUT_PERCENTAGE);
        int cutWidthBeforehand = (int) (w*cutPercentage);
        Object obj = null;
        Point point = null;
        // 2.0 找到边缘点的标志
        if (Constants.IMAGE_LATERALITY.L.equalsIgnoreCase(imageLaterality)) {
            rX = rX - cutWidthBeforehand;
            for (int x = (w - 1) - cutWidthBeforehand; x >= 0; x--, rX--) {
                point = findEdgePoint(dicomRaster.getDataElements(rX + w - 1, rY, 1, h, obj), threshold, x);
                if (null != point) {
                    return point;
                }
            }
        } else if (Constants.IMAGE_LATERALITY.R.equalsIgnoreCase(imageLaterality)) {
            rX = rX + cutWidthBeforehand;
            for (int x = cutWidthBeforehand; x < w; x++, rX++) {
                point = findEdgePoint(dicomRaster.getDataElements(rX, rY, 1, h, obj), threshold, x);
                if (null != point) {
                    return point;
                }
            }
        }
        return point;
    }

    private static Point findEdgePoint(Object obj, Short threshold, int x){
        Short[] grayDate = ArrayUtils.toObject(dataElements2ShortArr(obj));
        for (Short date : grayDate) {
            if (!date.equals(threshold)) {
                // 记录非背景边缘点，只需记录x轴位置
                return new Point(x, 0);
            }
        }
        return null;
    }

    private static short[] dataElements2ShortArr(Object obj){
        if (obj instanceof short[]) {
            return (short[]) obj;
        } else if (obj instanceof byte[]) {
            return UtilDataType.byteArray2ShortArray((byte[]) obj, ((byte[]) obj).length/2);
        } else if (obj instanceof int[]) {
            return UtilDataType.intArrToShortArr((int[]) obj);
        }
        throw new UnsupportedOperationException("Unsupported Datatype: " + obj.getClass().getName());
    }
}
