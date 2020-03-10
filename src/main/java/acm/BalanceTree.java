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
     *            Binary Search Tree（二叉搜索树）
     */
    public boolean isBalanced(TreeNode root) {
        // write your code here
        return maxDepth(root) != -1;
    }

    private int maxDepth(TreeNode root) {
        if (null == root) {
            return 0;
        }
        int left = maxDepth(root.left);
        int right = maxDepth(root.right);
        return Math.max(left, right) + 1;
    }

    public static class ResultType {
        int simpleMaxSum = 0;
        int maxSum = 0;

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

    private ResultType maxSum(TreeNode root) {
        ResultType resultType = new ResultType(Integer.MIN_VALUE, Integer.MIN_VALUE);
        if (null == root) {
            return resultType;
        } else {
            ResultType left = maxSum(root.left);
            ResultType right = maxSum(root.right);
            resultType.simpleMaxSum = Math.max(0, Math.max(left.simpleMaxSum, right.simpleMaxSum)) + root.val;
            resultType.maxSum = Math.max(Math.max(left.maxSum, right.maxSum),
                                         Math.max(0, left.simpleMaxSum) + Math.max(0, right.simpleMaxSum) + root.val);
            return resultType;
        }
    }

    public static class IsValidBSTResultType {
        int maxNum = Integer.MIN_VALUE;
        int minNum = Integer.MAX_VALUE;
        boolean isValidBST = false;

        IsValidBSTResultType(boolean isValidBST) {
            this.isValidBST = isValidBST;
        }

        IsValidBSTResultType(int maxNum, int minNum, boolean isValidBST) {
            this.maxNum = maxNum;
            this.minNum = minNum;
            this.isValidBST = isValidBST;
        }
    }

    /**
     * @param root: The root of binary tree.
     * @return: True if the binary tree is BST, or false
     */
    public boolean isValidBST(TreeNode root) {
        // write your code here
        return isValidBSTHelper(root).isValidBST;
    }

    /**
     * @param root: The root of binary tree.
     * @return: True if the binary tree is BST（Binary Search Tree）, or false
     */
    private IsValidBSTResultType isValidBSTHelper(TreeNode root) {
        // write your code here
        // 节点的左子树中的值要严格小于该节点的值。
        if (null == root) {
            return new IsValidBSTResultType(true);
        }
        IsValidBSTResultType leftResult = isValidBSTHelper(root.left);
        IsValidBSTResultType rightResult = isValidBSTHelper(root.right);
        if ((root.left==null || root.val > leftResult.maxNum) && (root.val < rightResult.minNum || root.right ==null)&& leftResult.isValidBST && rightResult.isValidBST) {
            return new IsValidBSTResultType(Math.max(root.val, rightResult.maxNum),
                                            Math.min(root.val, leftResult.minNum), true);
        }
        return new IsValidBSTResultType(false);
    }

    /**
     * @param root: root of a tree
     */
    public TreeNode treeToDoublyList(TreeNode root) {
        // Write your code here.
        TreeNode tail = resetNodeLink(root, null);
        TreeNode head = tail;
        while (head.left != null){
            head = head.left;
        }
        head.left = tail;
        tail.right = head;
        return head;
    }


    private TreeNode resetNodeLink(TreeNode root, TreeNode lastNodeInList) {
        if (root == null){
            return lastNodeInList;
        }
        if (root.left == null) {
            if (lastNodeInList != null) {
                lastNodeInList.right = root;
                root.left = lastNodeInList;
            }
        } else {
            lastNodeInList = resetNodeLink(root.left, lastNodeInList);
            lastNodeInList.right = root;
            root.left = lastNodeInList;
        }
        return resetNodeLink(root.right,root);
    }

    public static void main(String[] args) {
        TreeNode treeNode1 = new TreeNode(62);
        TreeNode treeNode2 = new TreeNode(23);
        TreeNode treeNode3 = new TreeNode(65);
        TreeNode treeNode4 = new TreeNode(6);
        TreeNode treeNode5 = new TreeNode(4);
        TreeNode treeNode6 = new TreeNode(16);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode3;
        treeNode2.left = treeNode4;
        treeNode3.right = treeNode5;
        treeNode4.left = treeNode6;
        TreeNode head = new BalanceTree().treeToDoublyList(treeNode1);
        TreeNode index = head;
        while (index.right.val != head.val){
            System.out.println(index.val);
            index = index.right;
        }
        System.out.println(index.val);
    }
}
