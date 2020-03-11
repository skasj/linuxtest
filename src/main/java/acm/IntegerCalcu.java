package acm;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: linuxtest
 * @description: 整数相关计算
 * @author: YeDongYu
 * @create: 2020-03-09 15:42
 */
public class IntegerCalcu {

    /**
     * @param n: an integer
     * @return: return a string
     */
    public String lastFourDigitsOfFn(int n) {
        // write your code here
        if (n == 0 || n == 1){
            return String.valueOf(n);
        }
        int result = 1;
        int tmp = result;
        while (n>0){
            tmp = result;
        }
        return "test";
    }

    Map<Integer, int[]> memo;

    public int[] beautifulArray(int N) {
        memo = new HashMap<>();
        return f(N);
    }

    public int[] f(int N) {
        if (memo.containsKey(N)){
            return memo.get(N);
        }
        int[] intArray = new int[N];
        if (N == 1){
            intArray[0] = 1;
        } else {
            int n =0;
            for (int i:f((N+1)/2)){
                intArray[n] = 2*i - 1;
                n++;
            }
            for (int i:f(N/2)){
                intArray[n] = 2*i;
                n++;
            }
            memo.put(N,intArray);
        }
        return intArray;
    }

    public static void main(String[] args) {
        for (int i:new IntegerCalcu().beautifulArray(10)){
            System.out.print(i + " ");
        }
    }
}
