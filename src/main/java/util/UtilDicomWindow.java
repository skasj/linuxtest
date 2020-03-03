package util;

import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 窗宽床位相关
 *
 * @author yedongyu
 * @version v2.0.0 2019/9/2 16:43
 */
public class UtilDicomWindow {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilDicomWindow.class);

    private UtilDicomWindow() {
    }

    public static void calculateWWandWL(Raster raster) {
        List<Map<String, Double>> WWandWL = new ArrayList<>();
        int[] pixels = UtilDicomImageReader.getIntArrPixelsFromRaster(raster);
        List<Integer> number = Arrays.asList(ArrayUtils.toObject(pixels));
        IntSummaryStatistics stats = number.stream().mapToInt((x) -> x).summaryStatistics();
        double slope = 2.81525;
        double intercept = -240;
        double ctMin = stats.getMin() * slope + intercept;
        double ctMax = stats.getMax() * slope + intercept;
        // windowWindowForAI设置为最大像素值
        double windowWindowForAI = stats.getMax();
        // 通过最大最小CT值得出windowCenterForAI
        double windowCenterForAI = (ctMin + ctMax) / 2;
        System.out.println("windowWindowForAI:" + windowWindowForAI);
        System.out.println("windowCenterForAI:" + windowCenterForAI);
    }
}
