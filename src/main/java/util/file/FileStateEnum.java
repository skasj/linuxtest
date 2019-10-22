package util.file;

/**
 * Created by lanxuewei in 13:40 2018/3/22
 * 文件状态标识
 */
public enum FileStateEnum {

    /**
     * 文件状态以及信息
     */
    TRANSFERING(0, "-transfering", "transfering file"),  //不完整文件
    UPLOADING(1, "-uploading", "Prepare for Upload"),    //完整待上传的zip压缩包
    ARCHIVES(2, "-archives", "Prepare for Archives"),    //完整待归档的zip压缩包
    ReadyForCloud(3, "-readyForCloud", "prepare for upload to cloud");  //本地解析完成，完整待上传云端的文件

    private Integer state;     //状态码
    private String stateInfo;  //状态信息
    private String stateDes;   //状态描述

    public static FileStateEnum stateOf(int index) {
        for (FileStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

    FileStateEnum(Integer state, String stateInfo, String stateDes) {
        this.state = state;
        this.stateInfo = stateInfo;
        this.stateDes = stateDes;
    }

    public Integer getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public String getStateDes() {
        return stateDes;
    }
}
