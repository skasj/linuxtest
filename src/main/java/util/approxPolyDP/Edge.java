package util.approxPolyDP;

import java.util.List;

/**
 * @program: linuxtest
 * @description: 轮廓
 * @author: YeDongYu
 * @create: 2019-04-26 15:51
 */
public class Edge {
    
    private List<List<Integer>> pointList;
    
    public List<List<Integer>> getPointList(){
        return pointList;
    }
    
    public void setPointList(List<List<Integer>> pointList){
        this.pointList = pointList;
    }
}
