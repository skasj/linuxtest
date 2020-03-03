package util;

import com.deepwise.dicom.pixel.PixelPicker;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.Raster;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: DeepWise
 * @description:
 * @author: YeDongYu
 * @create: 2019-07-16 10:22
 */
public class UtilDicomImageReader {

    private UtilDicomImageReader(){
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilDicomImageReader.class);

    public static int[] getIntArrPixelsFromRaster(Raster raster){
        int[] pixelDataArr = null;
        if (raster != null) {
            DataBuffer db = raster.getDataBuffer();
            switch (db.getDataType()) {
                case DataBuffer.TYPE_BYTE:
                    int[] temp = new int[raster.getWidth()*raster.getHeight()*raster.getNumBands()];
                    pixelDataArr = raster.getPixels(0, 0, raster.getWidth(), raster.getHeight(), temp);
                    break;
                case DataBuffer.TYPE_USHORT:
                    pixelDataArr = UtilDataType.shortArrToIntArr(((DataBufferUShort) db).getData());
                    break;
                case DataBuffer.TYPE_SHORT:
                    pixelDataArr = UtilDataType.shortArrToIntArr(((DataBufferShort) db).getData());
                    break;
                case DataBuffer.TYPE_INT:
                    pixelDataArr = ((DataBufferInt) db).getData();
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported Datatype: " + db.getDataType());
            }
        }
        return pixelDataArr;
    }

    public static int[] getIntArrPixeldata(File dicom){
        if (null == dicom || !dicom.exists()) {
            return new int[0];
        }
        try {
            Raster raster = PixelPicker.pickPixelEach(dicom, 0);
            if (raster != null) {
                return getIntArrPixelsFromRaster(raster);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("get pixel data of dicom file(%s) error", dicom.getAbsolutePath()), e);
        }
        return new int[0];
    }
}
