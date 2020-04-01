package vtk.model;

import java.util.List;
import lombok.Data;
import org.opencv.core.Point3;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2020-03-27 10:33
 */
@Data
public class PolyData {

    @Data
    public static class Line3D{
        Point3 start;
        Point3 end;
    }

    List<Point3> points;
    List<Line3D> line3DS;

}
