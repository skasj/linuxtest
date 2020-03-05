package util.dcmCut;

/**
 * @program: dw_cloud_dfs
 * @description: 配置信息名称存放
 * @author: YeDongYu
 * @create: 2019-05-09 16:33
 */
public interface DwConfigParamName {

    /** Dicom图片对应的任务类型判断，所用到的配置属性 */
    interface IMAGE_JUDGEMENT {

        /** 配置名称 */
        String CONFIG_KEY = "ImageJudgement";

        /** 任务类型 */
        String TASK_TYPE = "taskType";

        /** 影像类型， */
        String MODALITY  = "modality";

        /** Dicom 标签 */
        String TAGS       = "tags";

        /** Dicom 标签 */
        String TAG        = "tag";

        /** Dicom 标签的值 */
        String TAG_VALUE = "tagValue";
    }

    /** 乳腺特殊数据处理配置(数据方向旋转)常量 */
    interface BREAST_IMAGE_CONFIG{

        /** 是否需要旋转、翻转数据 */
        String IS_ABNORMAL_DIRECTION = "isAbnormalDirection";

        /** 哪一侧的方向需要旋转，L（左侧），R（右侧） */
        String SIDE_TO_TURN = "sideToTurn"; //L、R

        /** 旋转模式：1水平翻转，0垂直翻转，-1水平垂直翻转 */
        String TURNING_MODE = "turningMode";
    }

    /** 乳腺异常数据配置，异常的数据需要过滤不上传，不存入数据库 **/
    interface BREAST_SPECIAL_DATA_CONFIG{

        /** 是否过滤异常数据 */
        String IS_NOT_TO_SAVE_ABNORMAL_DATA = "isNotToSaveAbnormalData";

        /** 标记异常数据的标签 */
        String TAG_WITH_ABNORMAL_DATA = "tagWithAbnormalData";

        /** 异常数据的标签内容 */
        String CONTENT_WITH_ABNORMAL_DATA_TAG = "contentWithAbnormalDataTag";
    }

    /** 乳腺位置识别配置常量 */
    interface BREAST_POSITION {

        /** 左右侧判断标签 */
        String TAG_WITH_BREASTS = "tagWithBreasts";

        /** 左侧标签内容 */
        String VALUES_WITH_LEFT_BREAST = "valuesWithLeftBreast";

        /** 右侧标签内容 */
        String VALUES_WITH_RIGHT_BREAST = "valuesWithRightBreast";

        /** CC和MLO判断标签 */
        String TAG_WITH_CC_AND_MLO = "tagWithCCAndMLO";

        /** CC标签内容 */
        String VALUES_WITH_CC = "valuesWithCC";

        /** MLO标签内容 */
        String VALUES_WITH_MLO = "valuesWithMLO";

        /** 起始Y坐标 */
        String START_Y = "startY";
        /** 用于区别CC/MLO的像素和阈值 */
        String CM_COL_SUM_UP_THRESH = "cmColSumUpThresh";
        /** 用于区别左右侧的宽度百分比 */
        String IS_INVERSE = "isInverse";

    }

    /** 脑梗序列名称判断用到的标识 */
    interface ASPECT_EVALUATE {

        String SERIES_DESCRIPTION_T1    = "seriesDescriptionT1";

        String SERIES_DESCRIPTION_T2    = "seriesDescriptionT2";

        String SERIES_DESCRIPTION_FLAIR = "seriesDescriptionFLAIR";

        String SERIES_DESCRIPTION_DWI   = "seriesDescriptionDWI";

        /** 脑梗正逆序开关 */
        String CENTRAL_MANGAGEMENT_FLAG = "displayDicomInOrderWithAspect";
    }

    /** 胸平片判断正侧位配置 */
    interface CHEST_POSITION {

        /** 用于判断的标签 */
        String NORMOTOPIA_TAG = "normotopiaTag";

        /** 正位片的标签值 */
        String NORMOTOPIA_TAG_VALUES = "normotopiaTagValues";
    }

    interface LUNG{

        /** 层厚配置 */
        String SLICE_THICKNESS_CONFIG = "sliceThicknessConfig";

        /** 层厚过滤是否打开 */
        String IS_VERIFIED_SLICE_THICKNESS = "isVerifiedSliceThickness";

        /** 层厚过滤范围 */
        String SLICE_THICKNESS_RANGE       = "sliceThicknessRange";

        /** 肺图文报告配置 */
        String REPORT_CONFIG = "reportConfig";

        /** 缩略图是否显示轮廓按钮 */
        String SHOW_BBOX = "showBbox";
    }

    /** dicom文件排序配置 */
    interface DISPLAY_DICOM_ORDER{
        String NAME = "displayDicomOrder";
        /** 正逆序配置，true-正序，false-倒序 */
        String INSTANCE_NUM_ASC = "instanceNumAsc";
    }

    /** DCM裁剪配置 */
    interface CUT_DCM{

        /** 是否开启裁剪 */
        String IS_CUT_DCM = "isCutDcm";

        /** 裁剪比例 */
        String CUT_PERCENTAGE = "cutPercentage";

        /** 回退像素数量 */
        String BACK_PIXEL = "backPixel";
    }

    /**
     * 通用配置
     */
    interface ORG_SERVICE {

        String AI_CALC = "ai_calc";
    }
}
