package acm;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2020-03-05 16:43
 */
public class ArrayCalcu {

    /**
     * @param nums: An integer array
     */
    public void recoverRotatedSortedArray(List<Integer> nums) {
        // write your code here
        int index = 0;
        for (int i = 0; i < nums.size(); i++) {
            if (i == nums.size() - 1) {
                return;
            } else if (nums.get(i) > nums.get(i + 1)) {
                index = i;
                break;
            }
        }
        flipList(nums, 0, index);
        flipList(nums, index + 1, nums.size() - 1);
        flipList(nums, 0, nums.size() - 1);
    }

    private void flipList(List<Integer> nums, int start, int end) {
        int mid = (start + end) / 2;
        Integer tmp;
        int morror;
        for (int i = start; i <= mid; i++) {
            tmp = nums.get(i);
            morror = end + start - i;
            nums.set(i, nums.get(morror));
            nums.set(morror, tmp);
        }
    }

    /**
     * @param nums: A list of integers
     */
    public int maxSubArray(int[] nums) {
        // write your code here
        if (nums.length == 0) {
            return 0;
        }
        int maxSum = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (sum > maxSum) {
                maxSum = sum;
            }
            if (sum < 0) {
                sum = 0;
            }
        }
        return maxSum;
    }

    /**
     * @param nums: A list of integers
     */
    public int minSubArray(List<Integer> nums) {
        // write your code here
        if (nums.size() == 0) {
            return 0;
        }
        int minSum = Integer.MAX_VALUE;
        int sum = 0;
        for (int i = 0; i < nums.size(); i++) {
            sum += nums.get(i);
            minSum = Math.min(sum,minSum);
            if (sum > 0) {
                sum = 0;
            }
        }
        return minSum;
    }

    /*
     * @param nums: a list of integers
     * @return: find a  majority number
     */
    public int majorityNumber(List<Integer> nums) {
        // write your code here
        int majority=0,count=0;
        for (Integer num : nums){
            if (count == 0){
                majority = num;
            }
            if (num == majority){
                count ++;
            } else {
                count --;
            }
        }
        return majority;
    }
}
