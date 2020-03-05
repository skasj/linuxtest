package util.dcmCut;


import org.apache.commons.lang.ArrayUtils;

/**
 * @author ：chenhao
 * @version ：V1.0.0
 *
 * 进制转换工具类
 * 根据使用场景不同不一定适用
 *
 */
public class UtilDataType {

    public static short[] byteArray2ShortArray(byte[] data, int items) {
        short[] retVal = new short[items];
        for (int i = 0; i < retVal.length; i++)
            retVal[i] = (short) ((data[i * 2] & 0xff) | (data[i * 2 + 1] & 0xff) << 8);
        return retVal;
    }

    public static short[] byteArray2ShortArray(byte[] data) {
        if (ArrayUtils.isEmpty(data)) {
            return new short[0];
        }
        short[] retVal = new short[data.length];
        for (int i = 0; i < retVal.length; i++) {
            retVal[i] = (short) (data[i] <<8);
        }
        return retVal;
    }

    /**
     * 将一个单字节的byte转换成32位的int
     *
     * @param b
     *
     * @return convert result
     */
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * 将一个单字节的Byte转换成十六进制的数
     * @param b
     * @return
     */
    public static String byteToHex(byte b) {
        int i = b & 0xFF;
        return Integer.toHexString(i);
    }

    /**
     * 将一个4byte的数组转换成32位的int
     * @param buf
     * @param pos
     * @return
     */
    public static long unsigned4BytesToInt(byte[] buf, int pos) {
        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = pos;
        firstByte = (0x000000FF & ((int) buf[index]));
        secondByte = (0x000000FF & ((int) buf[index + 1]));
        thirdByte = (0x000000FF & ((int) buf[index + 2]));
        fourthByte = (0x000000FF & ((int) buf[index + 3]));
        index = index + 4;
        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    /**
     * 将16位的short转换成byte数组
     *
     * @param s
     * @return byte[] 长度为2
     * */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * 将32位整数转换成长度为4的byte数组
     *
     * @param s
     * @return byte[]
     * */
    public static byte[] intToByteArray(int s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * long to byte[]
     *
     * @param s
     * @return byte[]
     * */
    public static byte[] longToByteArray(long s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 8; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * 32位int转byte[]
     * @param res
     * @return
     */
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    /**
     * 将长度为2的byte数组转换为16位int
     *
     * @param res
     * @return int
     * */
    public static int byte2int(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示按位或
        return targets;
    }

    /**
     * int强制转short
     * @param sArr
     * @return
     */
    public static short[] intArrToShortArr(int[] sArr) {
        if(null == sArr || sArr.length == 0) {
            return new short[]{};
        }
        short[] result = new short[sArr.length];
        int i = 0;
        for (int x : sArr) {
            result[i++] = (short) x;
        }
        return result;
    }

    /**
     * shortArr强制转intArr
     * @param sArr
     * @return
     */
    public static int[] shortArrToIntArr(short[] sArr) {
        if(null == sArr || sArr.length == 0) {
            return new int[]{};
        }
        int[] result = new int[sArr.length];
        int i = 0;
        for (short x : sArr) {
            result[i++] = x;
        }
        return result;
    }

    /**
     * @param sArr
     * @return attribute不支持 setShorts 所以做个转换
     */
    public static byte[] shortArrToByteArr(short[] sArr) {
        if (sArr == null || sArr.length == 0) {
            return new byte[0];
        }
        byte[] byteArr = new byte[sArr.length * 2];
        short s;
        for (int i = 0, j = 0; j < sArr.length; ) {
            s = sArr[j++];
            byteArr[i++] = (byte) s;
            byteArr[i++] = (byte) (s >> 8);
        }
        return byteArr;
    }

}
