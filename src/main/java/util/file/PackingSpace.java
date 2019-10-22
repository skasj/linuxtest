package util.file;

/**
 * Created by lanxuewei in 15:00 2018/3/15
 * 待打包空间 单例模式
 */
public class PackingSpace {

    private volatile static PackingSpace instance;

    private volatile String packingName;                  //待打包文件夹名

    private volatile Long tempSize;                       //待打包文件夹使用大小

    private Long maxSize = 10 * 1024 * 1024L;    //每个文件夹存储上限默认10M，达到该上限后，重新获取系统时间作为 packName，temp_size=0

    private PackingSpace(){                      //私有构造器
        this.tempSize = 0L;
        this.packingName = "" + System.currentTimeMillis();
    }

    public synchronized static PackingSpace
    getInstance() {   //get Instance
        if (instance == null) {
            instance = new PackingSpace();
        }
        return instance;
    }

    public String getPackingName() {
        return packingName;
    }

    public void setPackingName(String packingName) {
        this.packingName = packingName;
    }

    public Long getTempSize() {
        return tempSize;
    }

    public void setTempSize(Long tempSize) {
        this.tempSize = tempSize;
    }

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }
}
