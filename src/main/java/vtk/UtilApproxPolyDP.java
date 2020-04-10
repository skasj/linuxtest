package vtk;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.opencv.core.Core;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

/**
 * @program: dw-cloud-dfs
 * @description:
 * @author: YeDongYu
 * @create: 2020-04-10 09:24
 */
public class UtilApproxPolyDP {
    /** 加载Native方法 */
    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static double defaultEpsilonValue = 3;

    /**
     * 拟合
     *
     * @param srcPoint
     */
    public static MatOfPoint2f approxPolyDP(List<List<Double>> srcPoint, double epsilonValue){
        if (CollectionUtils.isEmpty(srcPoint)){
            return null;
        }
        MatOfPoint2f src = transferListToMatOfPoint2f(srcPoint);
        MatOfPoint2f tar = new MatOfPoint2f();
        Imgproc.approxPolyDP(src, tar, epsilonValue, true);
        return tar;
    }

    public static List<List<Double>> approxPolyDP(List<List<Double>> srcPoint){
        MatOfPoint2f mat = approxPolyDP(srcPoint, defaultEpsilonValue);
        return transferMatOfPoint2fToList(mat);
    }

    private static MatOfPoint2f transferListToMatOfPoint2f(List<List<Double>> src){
        List<Point> points = new ArrayList<>();
        for (List<Double> object : src) {
            Point point = new Point();
            point.x = object.get(0);
            point.y = object.get(1);
            points.add(point);
        }
        Point[] pointArray = points.toArray(new Point[0]);
        return new MatOfPoint2f(pointArray);
    }

    private static List<List<Double>> transferMatOfPoint2fToList(MatOfPoint2f matOfPoint2f){
        List<Point> points = matOfPoint2f.toList();
        List<List<Double>> pointList = new ArrayList<>();
        for (Point point : points) {
            List<Double> object = new ArrayList<>();
            object.add(point.x);
            object.add(point.y);
            pointList.add(object);
        }
        return pointList;
    }
}
