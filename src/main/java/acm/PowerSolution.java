package acm;

import org.junit.Assert;

public class PowerSolution {
    /**
     * @param a: A 32bit integer
     * @param b: A 32bit integer
     * @param n: A 32bit integer
     * @return: An integer
     */
    public int fastPower(int a, int b, int n) {
        // write your code here
        if (n == 0) {
            return 1 % b;
        }
        long tmp = a;
        long sign = 1;
        while (n != 0) {
            if ((n & 1) == 1) {
                sign = (tmp * sign) % b;
            }
            tmp = (tmp * tmp) % b;
            n = n >> 1;
        }
        return (int) sign % b;
    }

    /**
     * @param x: the base number
     * @param n: the power number
     * @return: the result
     */
    public double myPow(double x, int n) {
        // write your code here
        if (n == 0){
            return 1;
        }
        int t = n;
        n = Math.abs(n);
        double result = 1;
        double tmp = x;
        while (n!=0){
            if ((n & 1) == 1){
                result = tmp * result;
            }
            tmp = tmp * tmp;
            n = n/2;
        }
        if (t < 0){
            return 1/result;
        } else {
            return result;
        }
    }

    public static void main(String[] args) {
        Assert.assertEquals(new PowerSolution().fastPower(2, 3, 31),2);
        Assert.assertEquals(new PowerSolution().fastPower(100, 1000, 1000),0);
        Assert.assertEquals(new PowerSolution().fastPower(27123, 5201314, 78965412),842799);
        System.out.println(new PowerSolution().myPow(9.88023, 3));
    }
}
