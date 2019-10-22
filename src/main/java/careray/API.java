package careray;

/**
 * 调用jni文件中的API
 *
 * @author tzw
 * @description
 * @date 2019/3/19 18:37
 */
public class API {

    public static native void dcmCjpeg(String strSrcFile,String strDstFile);

    // linux环境加载JNI.so
    // windows环境加载JNI.dll
    static {
        System.loadLibrary("JNI");
    }

}
