package util.webP.schedule;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;

/**
 * @program: linuxtest
 * @description: 单通道转换
 * @author: YeDongYu
 * @create: 2019-04-11 15:37
 */
public class SingleColorConvertOp implements BufferedImageOp {
    
    /** 0 红色，1 绿色，2蓝色 */
    private int[] color = { 0, 0, 0 };
    
    public void setSingleColor (int colorPath){
        color[colorPath] = 1;
    }
    
    public SingleColorConvertOp(int colorPath){
        color[colorPath] = 1;
    }
    
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest){
        
        ColorModel cm = src.getColorModel();
        Raster raster = src.getRaster();
        
        int w = raster.getWidth();
        int h = raster.getHeight();
        WritableRaster discreteRaster = cm.createCompatibleWritableRaster(w, h);
        Object obj = null;
        byte[] data;
        
        int rX = raster.getMinX();
        int rY = raster.getMinY();
        for (int y = 0; y < h; y++, rY++) {
            obj = raster.getDataElements(rX, rY, w, 1, obj);
            data = (byte[]) obj;
            for (int x = 0; x < w; x++) {
                data[x*3] *= color[0];
                data[x*3 + 1] *= color[1];
                data[x*3 + 2] *= color[2];
            }
            discreteRaster.setDataElements(0, y, w, 1, data);
        }
        dest = new BufferedImage(cm, discreteRaster, false, null);
        return dest;
    }
    
    @Override
    public Rectangle2D getBounds2D(BufferedImage src){
        return src.getRaster().getBounds();
    }
    
    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM){
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt){
        if (dstPt == null) {
            dstPt = new Point2D.Float();
        }
        dstPt.setLocation(srcPt.getX(), srcPt.getY());
        return dstPt;
    }
    
    @Override
    public RenderingHints getRenderingHints(){
        throw new UnsupportedOperationException();
    }
}
