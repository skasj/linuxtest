package util.dcmCut;

/**
 * @author ydy
 */
public interface Constants {

    /**
     * 缩略图类型
     */
    interface NodeScreenShotType{
        /** 普通类型 */
        String normal = "normal";
        /** 带有轮廓 */
        String withBox = "withBox";
    }

    /**
     * appId 校验相关
     */
    interface AppIdAuthentication {
        String App_Id      = "appId";           // 深睿颁发的AppId
        String App_Secret  = "appSecret";       // AppId对应的AppSecret

        String Sign_Str    = "sign";            // 签名
        String Time_Stamp  = "timestamp";       // 时间戳

        // private static final String Org_Code = "orgCode";
    }

    /** 自定义标签 */
    interface CustomDicomTag {
        /** (0019,8000) VR=CS VM=1 Image Laterality For AI */
        int ImageLateralityForAI = 0x00198000;
        /** (0019,0029) VR=US VM=1 TurnedMode, 0 = 180旋转, 1 = 水平翻转, 2 = 上下翻转*/
        int TurnedMode = 0x00190029;
        /** (0019,0030) VR=LO VM=1 HasTurnedForAI, 0 = false, 1 = true */
        int HasTurnedForAI = 0x00190030;
        /** (0019,8001) VR=CS VM=1 View Position For AI */
        int ViewPositionForAI = 0x00198001;
        /** (0019,0031) VR=Lo VM=1 TaskTypeForAI*/
        int TaskTypeForAI = 0x00190031;
        /** (0019,0032) VR=CS VM=1 SeriesNameForAI*/
        int SeriesNameForAI = 0x00190032;
        /** (0019,8002) VR=LO VM=1 灰度转换为RBG时，green通道所占位数*/
        int GreenSize = 0x00198002;
        /** (0019,8003) VR=LO VM=1 灰度转换为RBG时，red通道所占位数*/
        int RedSize = 0x00198003;
        /** (0019,8004) VR=LO VM=1 灰度转换为RBG时，blue通道所占位数*/
        int BlueSize = 0x00198004;
        /** (0019,8010) VR=DS VM=1 Window Width For AI*/
        int WindowWidthForAI = 0x00198010;
        /** (0019,8011) VR=DS VM=1 Window Center For AI*/
        int WindowCenterForAI = 0x00198011;
        /** (0019,8012) VR=SH VM=1 Cut Dcm Flag*/
        int CutDcmFlag = 0x00198012;
    }

    /** 像位信息之侧别 */
    interface IMAGE_LATERALITY {
        String R = "R";
        String L = "L";
    }
    /** 像位信息之视图位置 */
    interface VIEW_POSITION {
        /** 乳腺 **/
        String CC = "CC";
        String MLO = "MLO";
        String RXCCL = "RXCCL";
        String LMO = "LMO";
        String ML = "ML";
        /** 胸平片 **/
        String PA = "PA";
        String LL = "LL";
    }

    /**
     * 报告相关常量
     */
    interface Report {
        /** 一级报告类型 */
        interface ReportType {
            int BONE_GROWING = 0; //骨龄生长发育评估报告
            int BONE_AGE = 1; //骨龄普通报告
        }

        /** 保存类型 */
        interface SaveType {
            int NORMAL = 0; //普通保存
            int ELEC_SIGN = 1; //电子签名保存
        }
        /** 保存响应类型 */
        interface SaveResponseType {
            int SUCCESS = 0; // 保存成功
            int UNLOGIN = 1; // 未登录
            int NEED_ELEC_SIGN = 2; //原有报告已有电子签名，再次保存需要电子签名保存
        }
    }

    /**
     * ai相关文件常量，如Linux下及Windows下的分隔符
     */
    interface AiFolder {
        String Suffix_AI_Result_File    = ".airesult";
        String Suffix_Gsps_Folder       = "_gsps";
        String Suffix_KeySeries_Folder  = "_keySeries";
        String Suffix_Screenshot_Folder = "_screenshot";
        String Suffix_Vti_Folder        = "_vti";
        String Suffix_Bbox_Folder       = "_screenshot_bbox";
        String Suffix_RIB_Folder        = "_rib";            //CT骨折3D图像路径，由算法生成文件并写入
        String Suffix_CTA_Folder        = "_CTA";            //CTA重建生成的文件存放根路径
        String Suffix_MPR_Folder        = "_mpr";

        /** CT肋骨骨折CRP图像名称，由算法生成，存于 series_id_rib文件夹下 */
        String CT_Fracture_CPR          = "unfold.bmp";
    }
}
