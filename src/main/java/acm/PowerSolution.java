package acm;

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

    public static void main(String[] args) {
        System.out.println(new PowerSolution().fastPower(2, 3, 31));
        System.out.println(new PowerSolution().fastPower(100, 1000, 1000));
        System.out.println(new PowerSolution().fastPower(27123, 5201314, 78965412));
    }
}
