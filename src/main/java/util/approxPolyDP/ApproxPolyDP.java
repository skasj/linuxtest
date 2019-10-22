package util.approxPolyDP;

import com.alibaba.fastjson.JSON;
import com.deepwise.dicom.transcode.helper.TranscodeHelper;
import org.apache.commons.lang.StringUtils;
import org.opencv.core.Core;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: linuxtest
 * @description: 拟合轮廓并画图
 * @author: YeDongYu
 * @create: 2019-04-26 15:13
 */
public class ApproxPolyDP {
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private static final Logger         LOGGER        = LoggerFactory.getLogger(ApproxPolyDP.class);
    private static       String         picFile       = "C:\\Users\\99324\\Desktop\\脑出血轮廓拟合\\脑出血轮廓拟合.png";
    private static Map<Character,int[]> pointMap      = new HashMap<>();
    private static final int            TAR_MOVE_SIZE = 0;
    public static void main(String[] args){
        ApproxPolyDP approxPolyDP = new ApproxPolyDP();
        File pic = new File(picFile);
        if (pic.exists()) {
            pic.delete();
        }
        Edge src = approxPolyDP.getEdge();
        Edge transferred = approxPolyDP.approxPolyDP(src);
        List<Integer> posList = approxPolyDP.findPointPos(src,transferred);
        try {
            approxPolyDP.drawWritePic(src,transferred);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
    
    private void drawWritePic(Edge src,Edge transferred) throws IOException{
        //得到图片缓冲区
        int h = 400;
        int w = 400;
        BufferedImage bi = new BufferedImage(h, w, BufferedImage.TYPE_INT_RGB);
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        //填充一个矩形（背景）
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, h, w);
        
        // 画原图形
        g2.setColor(Color.GREEN);
        int n = src.getPointList().size();
        setArray(src);
        g2.drawPolygon(pointMap.get('x'),pointMap.get('y'),n);
        // 画新图形
        g2.setColor(Color.RED);
        n = transferred.getPointList().size();
        setArray(transferred);
        g2.drawPolygon(pointMap.get('x'),pointMap.get('y'),n);
        //保存图片 JPEG表示保存格式
        ImageIO.write(bi, "PNG", new FileOutputStream(picFile));
    }
    
    private Edge getEdge(){
        String edgeString1 = "{\"pointList\":[[235,118],[234,119],[234,120],[230,124],[230,125],[228,127],[227,127],[227,136],[226,137],[226,144],[225,145],[225,147],[224,148],[224,161],[223,162],[223,164],[222,165],[222,166],[221,167],[221,168],[220,169],[220,170],[219,171],[219,177],[224,182],[226,182],[227,183],[229,183],[230,184],[231,184],[232,183],[235,183],[236,182],[238,182],[239,183],[245,183],[246,184],[248,184],[249,185],[253,185],[254,186],[255,185],[258,185],[260,183],[260,182],[261,181],[261,180],[268,173],[269,173],[270,172],[270,171],[271,170],[271,169],[273,167],[273,166],[275,164],[275,163],[276,162],[276,161],[277,160],[277,159],[281,155],[282,155],[283,154],[284,154],[285,153],[286,153],[287,152],[287,151],[288,150],[288,148],[289,147],[289,143],[290,142],[290,138],[289,137],[289,135],[288,134],[288,133],[287,132],[287,131],[282,126],[281,126],[279,124],[278,124],[277,123],[276,123],[275,122],[274,122],[273,121],[271,121],[270,120],[267,120],[266,119],[258,119],[257,120],[255,120],[254,121],[253,121],[252,122],[250,122],[249,123],[239,123],[236,120],[236,119]]}";
        String edgeString2 = "{\"pointList\":[[263,152],[262,153],[261,153],[260,154],[259,154],[257,156],[256,156],[255,157],[254,157],[253,158],[252,158],[251,159],[250,159],[245,164],[245,165],[244,166],[244,168],[243,169],[243,170],[242,171],[242,179],[241,180],[241,181],[240,182],[240,183],[238,185],[238,187],[237,188],[237,192],[238,193],[238,196],[239,197],[239,198],[242,201],[244,201],[245,202],[247,202],[248,203],[249,203],[250,204],[255,204],[256,203],[256,202],[257,201],[257,200],[259,198],[259,197],[262,194],[262,193],[264,191],[264,190],[265,189],[265,186],[266,185],[266,184],[270,180],[270,179],[273,176],[273,175],[278,170],[278,169],[279,168],[279,167],[280,166],[280,162],[279,161],[279,159],[276,156],[275,156],[274,155],[273,155],[272,154],[271,154],[270,153],[268,153],[267,152]]}";
        String edgeString3 = "{\"pointList\":[[284,147],[284,149],[285,150],[286,150],[288,152],[289,152],[291,154],[292,154],[295,157],[296,157],[298,159],[300,159],[301,160],[302,160],[303,161],[303,162],[305,162],[307,164],[308,164],[310,166],[310,167],[311,168],[312,168],[314,170],[315,170],[317,172],[317,173],[318,174],[319,174],[320,175],[319,176],[320,176],[321,177],[321,178],[323,180],[324,180],[326,182],[327,182],[329,184],[329,185],[330,186],[331,186],[332,187],[331,188],[332,188],[335,191],[335,192],[336,192],[337,193],[337,194],[340,197],[340,198],[341,198],[342,199],[341,200],[342,200],[344,202],[344,203],[345,204],[346,204],[348,206],[348,207],[349,208],[350,208],[352,210],[352,211],[353,212],[354,212],[355,213],[355,214],[357,216],[358,216],[359,217],[359,218],[361,220],[362,220],[364,222],[365,222],[367,224],[368,224],[370,226],[371,226],[374,229],[377,229],[380,232],[381,231],[382,232],[383,232],[385,234],[387,234],[388,233],[387,233],[384,230],[381,230],[378,227],[377,228],[376,227],[375,227],[373,225],[372,225],[369,222],[368,222],[366,220],[365,220],[363,218],[362,218],[360,216],[360,215],[359,214],[358,214],[356,212],[356,211],[355,210],[354,210],[353,209],[353,208],[351,206],[350,206],[349,205],[350,204],[349,204],[347,202],[346,202],[345,201],[345,200],[342,197],[342,196],[341,196],[340,195],[341,194],[340,194],[338,192],[338,191],[330,183],[330,182],[329,181],[328,181],[325,178],[324,178],[322,176],[322,175],[317,170],[317,169],[316,169],[313,166],[312,166],[310,164],[310,163],[309,162],[308,162],[306,160],[305,161],[304,160],[304,159],[303,158],[301,158],[300,157],[299,157],[297,155],[296,155],[294,153],[293,153],[290,150],[289,150],[287,148],[286,148],[285,147]]}";
        String edgeString4 = "{\"pointList\":[[261,153],[256,158],[254,158],[253,159],[249,159],[248,160],[247,160],[246,161],[245,161],[241,165],[240,165],[235,170],[235,173],[234,174],[234,177],[233,178],[234,179],[234,182],[235,183],[235,184],[236,185],[236,194],[237,195],[237,198],[238,199],[238,200],[239,200],[241,202],[245,202],[246,201],[248,201],[249,200],[250,200],[252,198],[253,198],[256,195],[257,195],[259,193],[260,193],[260,192],[263,189],[264,189],[264,188],[268,184],[269,184],[270,183],[273,183],[274,182],[275,182],[276,181],[276,180],[277,179],[277,178],[278,177],[278,174],[277,173],[277,171],[276,170],[276,169],[275,168],[275,167],[273,165],[273,164],[271,162],[271,161],[270,160],[270,158],[269,157],[269,156],[266,153]]}";
        String edgeString5 = "{\"pointList\":[[184,152],[183,153],[181,153],[178,156],[178,157],[177,158],[177,159],[176,160],[176,165],[175,166],[175,167],[173,169],[173,170],[171,172],[171,173],[170,174],[170,180],[171,181],[171,182],[172,183],[172,184],[173,185],[173,186],[177,190],[178,190],[179,191],[180,191],[181,192],[186,192],[187,193],[191,193],[194,196],[195,196],[196,197],[199,197],[200,196],[201,196],[201,195],[203,193],[203,192],[204,191],[204,190],[205,189],[205,188],[206,187],[206,184],[207,183],[207,181],[206,180],[206,178],[205,177],[205,176],[204,175],[204,173],[201,170],[201,169],[200,168],[200,162],[199,161],[199,160],[193,154],[191,154],[190,153],[187,153],[186,152]]}";
        String edgeString6 = "{\"pointList\":[[257,118],[256,119],[252,119],[251,120],[251,121],[250,122],[250,123],[249,124],[248,124],[247,125],[245,125],[244,126],[242,126],[241,127],[239,127],[238,126],[235,126],[233,128],[232,128],[232,129],[228,133],[227,133],[227,136],[225,138],[225,139],[224,140],[224,141],[223,142],[223,149],[224,150],[224,160],[223,161],[223,164],[222,165],[222,168],[221,169],[221,174],[220,175],[220,184],[221,185],[221,186],[222,187],[223,187],[225,189],[229,189],[230,190],[239.5,190.7],[246.5,190.1],[257,189],[258,188],[259,188],[262,185],[262,184],[264,182],[264,181],[265,180],[266,180],[267,179],[269,179],[270,178],[271,178],[272,177],[274,177],[277,174],[277,173],[278,172],[278,170],[279,169],[279,168],[281,166],[281,163],[282,162],[282,155],[283,154],[283,152],[284,151],[284,149],[285,148],[285,147],[286,146],[286,143],[287,142],[287,140],[286,139],[286,137],[284,135],[284,134],[283,133],[282,133],[275,126],[274,126],[272,124],[271,124],[269,122],[267,122],[266,121],[265,121],[264,120],[262,120],[261,119],[258,119]]}";
        Edge edge = JSON.parseObject(edgeString6,Edge.class);
        return edge;
    }
    
    private void setArray(Edge edge){
        int[] xpoint = new int[edge.getPointList().size()];
        int[] ypoint = new int[edge.getPointList().size()];
        int i = 0;
        for (List<Integer> object : edge.getPointList()){
            xpoint[i] = object.get(0);
            ypoint[i] = object.get(1);
            i++;
        }
        pointMap.put('x',xpoint);
        pointMap.put('y',ypoint);
    }
    
    /**
     * 拟合
     * @param edge
     */
    private Edge approxPolyDP(Edge edge){
        MatOfPoint2f src = transferEdgeToMatOfPoint2f(edge);
        MatOfPoint2f tar = new MatOfPoint2f();
        Imgproc.approxPolyDP(src,tar,-3,true);
        return transferMatOfPoint2fToEdge(tar);
    }
    
    /**
     * 找到tar所有点在src上的位置
     * @param src
     * @param tar
     * @return
     */
    private List<Integer> findPointPos(Edge src,Edge tar){
        List<Integer> posList = new ArrayList<>();
        List<List<Integer>> pointListOfSrc = src.getPointList();
        List<List<Integer>> pointListOfTar = tar.getPointList();
        for (List<Integer> pointOfTar : pointListOfTar){
            int n=0;
            for (List<Integer> pointOfSrc : pointListOfSrc){
                if (pointOfSrc.get(0).equals(pointOfTar.get(0) + TAR_MOVE_SIZE) && pointOfSrc.get(1).equals(pointOfTar.get(1))){
                    posList.add(n);
                }
                n++;
            }
        }
        LOGGER.info(StringUtils.join(posList,' '));
        return posList;
    }
    
    private MatOfPoint2f transferEdgeToMatOfPoint2f(Edge edge){
        List<Point> points = new ArrayList<>();
        for (List<Integer> object : edge.getPointList()){
            Point point = new Point();
            point.x = object.get(0)-TAR_MOVE_SIZE;
            point.y = object.get(1);
            points.add(point);
        }
        Point[] pointArray = points.toArray(new Point[0]);
        return new MatOfPoint2f(pointArray);
    }
    
    private Edge transferMatOfPoint2fToEdge(MatOfPoint2f matOfPoint2f){
        List<Point> points = matOfPoint2f.toList();
        Edge edge = new Edge();
        List<List<Integer>> pointList = new ArrayList<>();
        for (Point point : points){
            List<Integer> object = new ArrayList<>();
            object.add(new Double(point.x).intValue());
            object.add(new Double(point.y).intValue());
            pointList.add(object);
        }
        edge.setPointList(pointList);
        return edge;
    }
}
