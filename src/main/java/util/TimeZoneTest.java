package util;

import java.util.Date;
import java.util.TimeZone;

/**
 * @program: linuxtest
 * @description: 获取Linux上Jvm的时间，检验时区问题
 * @author: YeDongYu
 * @create: 2019-03-12 13:41
 */
public class TimeZoneTest {
    
    public static void main(String[] args){
        System.out.println(TimeZone.getDefault());
        System.out.println(new Date());
    }
}
