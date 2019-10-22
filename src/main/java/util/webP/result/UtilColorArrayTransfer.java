package util.webP.result;

import com.deepwise.dicom.pixel.PixelPicker;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * @program: linuxtest
 * @description: 将Dicom文件中的灰度值转变为RGB
 * @author: YeDongYu
 * @create: 2019-04-25 14:47
 *
 * 目前是直接读取short数组，只支持小于12位的灰度模式，如果以后需要支持RGB，YGB等模式，推荐实现BufferedImageOp接口
 */
public class UtilColorArrayTransfer {
    
    /**
     * 将12位的灰度像素，转变为24位的BGR像素
     * @param src
     * @return
     * @throws IOException
     */
    public static BufferedImage gray12toBGR(File src) throws IOException{
        Raster dicomRaster = PixelPicker.pickPixelFully(src).get(0);
        int w = dicomRaster.getWidth();
        int h = dicomRaster.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        ColorModel cm = bi.getColorModel();
        WritableRaster discreteRaster = cm.createCompatibleWritableRaster(w, h);
        Object obj = null;
        byte[] bgrData = new byte[w * 3];
        short[] grayDate = null;
        int rX = dicomRaster.getMinX();
        int rY = dicomRaster.getMinY();
        int g, b;
        short max = 0;
        int bSize = 4;
        for (int y = 0; y < h; y++, rY++) {
            obj = dicomRaster.getDataElements(rX, rY, w, 1, obj);
            grayDate = (short[]) obj;
            for (int x = 0; x < w; x++) {
                if (grayDate[x]>max){
                    max = grayDate[x];
                }
                g = (grayDate[x] & (0xff << bSize)) >> bSize;
                b = grayDate[x] & ((2 << bSize) - 1);
                bgrData[x*3 + 1] = (byte) g;
                bgrData[x*3 + 2] = (byte) b;
            }
            discreteRaster.setDataElements(0, y, w, 1, bgrData);
        }
        System.out.println(max);
        return new BufferedImage(cm, discreteRaster, false, null);
    }
}
