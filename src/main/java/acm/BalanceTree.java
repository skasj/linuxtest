package acm;

import javax.xml.transform.Source;

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

    /**
     * @param root: The root of binary tree.
     * @return: An integer
     */
    public int maxPathSum(TreeNode root) {
        // write your code here
        if (null == root){
            return 0;
        }
        int left = maxSum(root.left);
        int right = maxSum(root.right);
        left = left < 0 ? 0 : left;
        right = right < 0 ? 0 : right;
        return left+root.val+right;
    }

    private int maxSum(TreeNode root){
        if (null == root){
            return 0;
        } else {
            int left = maxSum(root.left);
            int right = maxSum(root.right);
            left = left < 0 ? 0 : left;
            right = right < 0 ? 0 : right;
            return Math.max(left,right)+root.val;
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
