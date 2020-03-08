package acm;


/**
 * @program: linuxtest
 * @description: 二叉树
 * @author: YeDongYu
 * @create: 2020-03-05 14:37
 */
public class BalanceTree {


    public static class TreeNode {

        public int val;

        public TreeNode left, right;

        public TreeNode(int val) {
            this.val = val;
            this.left = this.right = null;
        }
    }

    /**
     * @param root: The root of binary tree.
     */
    public boolean isBalanced(TreeNode root) {
        // write your code here
        return maxDepth(root)!= -1;
    }

    private int maxDepth(TreeNode root){
        if (null == root){
            return 0;
        }
        int left = maxDepth(root.left);
        int right = maxDepth(root.right);
        return Math.max(left,right)+1;
    }

    public static class ResultType {
        int simpleMaxSum = 0;
        int maxSum= 0;

        public ResultType(int simpleMaxSum, int maxSum) {
            this.simpleMaxSum = simpleMaxSum;
            this.maxSum = maxSum;
        }
    }

    /**
     * @param root: The root of binary tree.
     * @return: An integer
     */
    public int maxPathSum(TreeNode root) {
        // write your code here
        ResultType resultType = maxSum(root);
        return resultType.maxSum;
    }

    private ResultType maxSum(TreeNode root){
        ResultType resultType = new ResultType(Integer.MIN_VALUE, Integer.MIN_VALUE);
        if (null == root){
            return resultType;
        }else {
            ResultType left = maxSum(root.left);
            ResultType right = maxSum(root.right);
            resultType.simpleMaxSum =Math.max(0,Math.max(left.simpleMaxSum,right.simpleMaxSum))+root.val;
            resultType.maxSum =Math.max(Math.max(left.maxSum,right.maxSum),Math.max(0,left.simpleMaxSum)+Math.max(0,right.simpleMaxSum)+root.val);
            return resultType;
        }
    }

    public static void main(String[] args) {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(-5);
        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode6 = new TreeNode(5);
        TreeNode treeNode7 = new TreeNode(6);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode3;
        treeNode2.left =treeNode4;
        treeNode3.left = treeNode6;
        treeNode3.right = treeNode7;
        System.out.println(new BalanceTree().maxPathSum(treeNode1));
    }
}
