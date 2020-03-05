package acm;

/**
 * @program: linuxtest
 * @description: 链表
 * @author: YeDongYu
 * @create: 2020-03-05 15:11
 */
public class ListNodeWorker {

    public class ListNode {

        int val;

        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    /**
     * @param head: n
     */
    public ListNode reverse(ListNode head) {
        ListNode preHead = new ListNode(0);
        ListNode tmp = null;
        while (head != null) {
            tmp = preHead.next;
            preHead.next = head;
            head = head.next;
            preHead.next.next = tmp;
        }
        return preHead.next;
    }
}
