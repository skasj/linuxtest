package acm;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ListNodeSolution {


    public class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    /**
     * @param lists: a list of ListNode
     * @return: The head of one sorted list.
     */
    public ListNode mergeKLists(List<ListNode> lists) {
        // write your code here
        if (null == lists || lists.isEmpty()){
            return null;
        }
        PriorityQueue<ListNode> heap = new PriorityQueue<>(Comparator.comparingInt(o -> o.val));
        for (ListNode tail:lists){
            while (tail!=null){
                heap.add(tail);
                tail = tail.next;
            }
        }
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (!heap.isEmpty()){
            tail.next=heap.poll();
            tail = tail.next;
        }
        return dummy.next;
    }

    /**
     * @param lists: a list of ListNode
     * @return: The head of one sorted list.
     */
    public ListNode mergeKLists_V2(List<ListNode> lists) {
        if (null == lists || lists.isEmpty()) {
            return null;
        }
        return mergeHelper(lists, 0, lists.size() - 1);
    }

    private ListNode mergeHelper(List<ListNode> lists, int start, int end) {
        if (start == end){
            return lists.get(start);
        }
        int mid = (start +end)/2;
        ListNode left = mergeHelper(lists,start,mid);
        ListNode right = mergeHelper(lists,mid+1,end);
        return mergeTwoListNode(left,right);
    }

    private ListNode mergeTwoListNode(ListNode left, ListNode right) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (left!=null && right!=null){
            if (left.val<right.val){
                tail.next = left;
                tail = left;
                left = left.next;
            } else {
                tail.next = right;
                tail = right;
                right = right.next;
            }
        }
        if (right != null){
            tail.next = right;
        } else if (left != null){
            tail.next = left;
        }
        return dummy.next;
    }

}
