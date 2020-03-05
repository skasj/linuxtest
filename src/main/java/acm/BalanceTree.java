package acm;

/**
 * @program: linuxtest
 * @description: 平衡二叉树
 * @author: YeDongYu
 * @create: 2020-03-05 14:37
 */
public class BalanceTree {


    public class TreeNode {

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
}
