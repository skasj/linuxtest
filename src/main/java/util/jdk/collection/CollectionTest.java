package util.jdk.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2019-10-22 15:01
 */
public class CollectionTest {
    static class WeekDay {

        private String[] weekDays = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};

        private String day;

        private String random() {
            return weekDays[new Random().nextInt(7)];
        }

        WeekDay() {
        }

        WeekDay(String day) {
            this.day = day;
        }

        public List<WeekDay> arrayList(int num) {
            List<WeekDay> weekDayList = new ArrayList<>();
            while (num > 0) {
                weekDayList.add(new WeekDay(random()));
                num--;
            }
            return weekDayList;
        }

        @Override
        public String toString() {
            return this.day;
        }
    }

    private static void testList1(){
        WeekDay weekDay = new WeekDay();
        List<WeekDay> weekDayList = weekDay.arrayList(8);
        System.out.println(weekDayList);
        ListIterator<WeekDay> weekDayListIterator = weekDayList.listIterator(0);
        System.out.print("[");
        while (weekDayListIterator.hasNext()) {
            System.out.print(weekDayListIterator.previousIndex()+ " " + weekDayListIterator.next().toString()+ " " + weekDayListIterator.previousIndex()
                    +" " + weekDayListIterator.nextIndex() + ", ");
        }
        System.out.print("]");
    }

    private static void testShuffle(){
        Integer[] ia = {1,2,3,4,5,6,7,8,9};
        List<Integer> integers = Arrays.<Integer>asList(ia);
        Collections.shuffle(integers,new Random(47));
        /* error with integers.add(10); */
        System.out.println(Arrays.toString(ia));
    }

    public static void main(String[] args) {

    }
}
