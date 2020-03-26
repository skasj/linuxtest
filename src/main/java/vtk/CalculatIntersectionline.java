package vtk;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2020-03-25 16:05
 */
public class CalculatIntersectionline {

    // aCell[0]是法向量
    // aCell[1]是面上一点
    // 对应的一般方程是: x + y + z = 6
    // 6 等于aCell[0]与aCell[0]的点积
    static float[][] aCell = {{1, 1, 1},{1,2,3}};

    // 对应的一般方程是: x - y + z = 2
    // 2 等于bCell[0]与bCell[0]的点积
    static float[][] bCell = {{1, -1, 1},{1,2,3}};

    public static void main(String[] args) {
        float x,y,z;
        // 1. 求交线上任一一点：
        // 先假定 z= 0;
        // 带入aCell 和 bCell, 得到两个二元依次方程
        z = 0;
        // x + y = 6 ,也即 aCell[0][0] * x + aCell[0][1] *y = sum1
        // x - y = 2 ,也即 bCell[0][0] * x + bCell[0][1] *y = sum2
        // 求解二元一次方程
        // sum1 是aCell[0]与aCell[0]的点积，即6
        float sum1 = aCell[0][0]*aCell[1][0] + aCell[0][1]*aCell[1][1] + aCell[0][2]*aCell[1][2];
        float sum2 = bCell[0][0]*bCell[1][0] + bCell[0][1]*bCell[1][1] + bCell[0][2]*bCell[1][2];
        y = (sum2*aCell[0][0] - bCell[0][0]*sum1)/(aCell[0][0]*bCell[0][1]-bCell[0][0]*aCell[0][1]);
        x = (sum1-aCell[0][1]* y)/aCell[0][0];
        // 获得交线上一点
        System.out.println(String.format("%f %f %f",x,y,z));
    }
}
