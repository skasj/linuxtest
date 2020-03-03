package util;

import java.util.Scanner;

/**
 * @program: linuxtest
 * @description:
 * @author: YeDongYu
 * @create: 2020-02-19 13:26
 */
public class test {

    public static void main(String[] args) {
//        W = dicom文件窗位；
//        L = dicom文件窗宽；
        Float w = 2589f;
        Float l = 1294f;
//        dw = 缩略图的窗宽；
//        dl = 缩略图的窗位；
//        Float dw = 87f;
        Float dw = 104f;
//        Float dl = 126f;
        Float dl = 81f;
        Float b = (w/255.0f)*(dl - 128.0f);
        Float tw = dw*(w/255.0f);
        Float tl = l + b;
        System.out.println("b:"+b);
        System.out.println("窗宽"+tw);
        System.out.println("窗位"+tl);
    }
}
