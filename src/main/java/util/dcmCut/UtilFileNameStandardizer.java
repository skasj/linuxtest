/*
 * Copyright (c) 2017-2018 DeepWise All Rights Reserved.
 * http://www.deepwise.com
 */
package util.dcmCut;

import java.io.File;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.util.AttributesFormat;

/**
 * @author Yuanzenan
 * @version V2.0.0
 * Created in 2018/4/26
 */
public class UtilFileNameStandardizer {
    public static final String COMPLETE_MARK       = "_completed";
    public static final String PREFIX              = "PREFIX";
    public static final String EXT                 = "EXT";
    public static final String DICOM_PATH          = "/{00100020,hash}/{0020000D,hash}/{0020000E,hash}/";
    public static final String NOT_COMPLETE_MARK   = "_not_completed";

    /** 自定义的机构码分割识别符号 */
    public static final String ORG_SEPARATOR       = "@org_";
    /** 自定义的主任务分割识别符号 */
    public static final String MAIN_TASK_SEPARATOR = "@taskId_";
    /** 本次推送关联任务的唯一标识 */
    public static final String PUSH_CODE_SEPARATOR = "@pushCode_";

    /**
     * 目标文件名 "${机构码}@org_${ris阅片主任务ID}@taskId_${本次推送code}@pushCode_{uuid}.${extension}"
     *
     * @param originalFileName 原始文件名
     * @param orgCode          机构码
     * @param taskId           ris阅片主任务ID（可以为空）
     * @param pushCode         关联任务本次推送code，pushCode（可以为空）
     * @return "${机构码}@org_${ris阅片主任务ID}@taskId_${本次推送code}@pushCode_${uuid}.${extension}"
     */
    public static String standardizeFileName (String originalFileName, String orgCode, String taskId, String pushCode) {
        originalFileName = clean(originalFileName);
        orgCode = clean(orgCode);
        taskId = clean(taskId);
        pushCode = clean(pushCode);
        // 1.0 出现SE0/SE01.dcm这种文件名时，SE0/保留
        int index = originalFileName.lastIndexOf("/");
        if (index != -1) {
            // 1.1 单纯目录直接返回
            if (index == originalFileName.length() - 1) {
                return originalFileName;
            }
            return originalFileName.substring(0, index + 1) + orgCode + ORG_SEPARATOR
                    + taskId + MAIN_TASK_SEPARATOR + pushCode + PUSH_CODE_SEPARATOR
                    + uuid() + parseFileName(originalFileName, EXT);
        }
        return orgCode + ORG_SEPARATOR + taskId + MAIN_TASK_SEPARATOR + pushCode + PUSH_CODE_SEPARATOR + uuid() + parseFileName(originalFileName, EXT);
    }

    /**
     * 获取文件名中的机构码
     *
     * @param fileName 文件名
     * @return 机构码
     */
    public static String getOrgCodeFromFileName(String fileName) {
        if (!fileName.contains(ORG_SEPARATOR)) {
            return "";
        }
        return fileName.split(ORG_SEPARATOR)[0];
    }

    /**
     * 获取文件名中的ris阅片主任务ID
     *
     * @param fileName 文件名
     * @return ris阅片主任务ID
     */
    public static String getMainTaskIdFromFileName (String fileName) {
        if (!fileName.contains(MAIN_TASK_SEPARATOR)) {
            return "";
        }
        String taskIdOfString = fileName.split(MAIN_TASK_SEPARATOR)[0];
        if (fileName.contains(ORG_SEPARATOR)) {
            String[] strArray = taskIdOfString.split(ORG_SEPARATOR);
            return strArray.length>1 ? taskIdOfString.split(ORG_SEPARATOR)[1] : "";
        }
        return taskIdOfString;
    }

    /**
     * 获取文件名中的pushCode
     *
     * @param fileName 文件名
     * @return pushCode
     */
    public static String getPushCodeFromFileName (String fileName) {
        if (!fileName.contains(PUSH_CODE_SEPARATOR)) {
            return "";
        }
        String pushCodeOfString = fileName.split(PUSH_CODE_SEPARATOR)[0];
        if (fileName.contains(MAIN_TASK_SEPARATOR)) {
            String[] strArray = pushCodeOfString.split(MAIN_TASK_SEPARATOR);
            return strArray.length>1 ? pushCodeOfString.split(MAIN_TASK_SEPARATOR)[1] : "";
        }
        return pushCodeOfString;
    }

    /**
     * 将处理完毕的文件，加上"_complete"后缀
     *
     * @param file 处理完毕的文件
     * @return 加后缀成功，则返回true；否则返回false
     */
    public static boolean markCompleted(File file) {
        if (file.exists()) {
            String completeName = markCompleted(file.getName());
            File complete = new File(file.getParentFile(), completeName);
            complete.delete();
            return file.renameTo(complete);
        }
        return false;
    }

    /**
     * 将处理完毕的文件，加上"_complete"后缀
     *
     * @param file 处理完毕的文件
     * @return 加后缀成功，则返回true；否则返回false
     */
    public static File markCompletedAndReturnFile(File file) {
        if (file.exists()) {
            String completeName = markCompleted(file.getName());
            File complete = new File(file.getParentFile(), completeName);
            complete.delete();
            if (file.renameTo(complete)) {
                return complete;
            }
        }
        return null;
    }

    /**
     * 去除_not_completed标识
     *
     * @param file 文件名
     * @return 无_not_completed标识的文件名
     */
    public static boolean unMarkNotCompleted(File file) {
        if (file.exists()) {
            String completeName = unMarkNotCompleted(file.getName());
            File complete = new File(file.getParentFile(), completeName);
            complete.delete();
            return file.renameTo(complete);
        }
        return false;
    }

    /**
     * 判断当前处理的文件是否已经被上一步（别的线程）处理完毕。
     * 通过文件名中是否包含_completed标识来判断。
     *
     * @param file 待判断的文件
     * @return 如果文件名包含_completed标识，则返回true；否则返回false
     */
    public static boolean isCompleted(File file) {
        return file != null && file.exists() && hasCompletedMark(file.getName());
    }

    /**
     * 判断文件名是否有完成标识
     *
     * @param filename 文件名
     * @return 有完成标识，返回true；没有完成标识，返回false
     */
    public static boolean hasCompletedMark(String filename) {
        filename = clean(filename);
        return filename.contains(COMPLETE_MARK);
    }

    /**
     * 在原文件名的基础上，增加完成标识
     *
     * @param originalFileName 原始文件名
     * @return 返回加了完成标识的文件名
     */
    public static String markCompleted(String originalFileName) {
        return markFilename(originalFileName, COMPLETE_MARK);
    }

    /**
     * 去除_not_completed标识
     *
     * @param filename 文件名
     * @return 无_not_completed标识的文件名
     */
    public static String unMarkNotCompleted(String filename) {
        filename = clean(filename);
        if (filename.contains(NOT_COMPLETE_MARK)) {
            return filename.replace(NOT_COMPLETE_MARK, "");
        }
        return filename;
    }

    /**
     * 去除_completed标识
     *
     * @param filename 文件名
     * @return 无_completed标识的文件名
     */
    public static String unMarkCompleted(String filename) {
        filename = clean(filename);
        if (filename.contains(COMPLETE_MARK)) {
            return filename.replace(COMPLETE_MARK, "");
        }
        return filename;
    }

    /**
     * 在原文件名的基础上，增加完成标识
     *
     * @param originalFileName 原始文件名
     * @param mark             自定义标识
     * @return 加上标识的文件名
     */
    public static String markFilename(String originalFileName, String mark) {
        originalFileName = clean(originalFileName);
        return parseFileName(originalFileName, PREFIX) + mark + parseFileName(originalFileName, EXT);
    }

    /**
     * 更换标准名称中的uuid部分
     *
     * @param standardFileName 标准的文件名
     * @param needCompleteMark 原名中有无完成标识
     * @return 更换uuid后的标准名称
     */
    public static String changeUuid(String standardFileName, boolean needCompleteMark) {
        standardFileName = clean(standardFileName);
        String orgCode = getOrgCodeFromFileName(standardFileName);
        String taskId = getMainTaskIdFromFileName(standardFileName);
        String pushCode = getPushCodeFromFileName(standardFileName);
        String newStandardName = standardizeFileName(standardFileName, orgCode, taskId, pushCode);
        return needCompleteMark ? markCompleted(newStandardName) : newStandardName;
    }

    /**
     * 标准化文件路径（带路径）—— ${parentDirPath}/${机构码}@org_${uuid}.${extension}
     *
     * @param parentDirPath    文件的父目录路径
     * @param originalFileName 原始文件名
     * @param orgCode          机构码
     * @param taskId           taskId
     * @param pushCode         pushCode
     * @return 标准化后的文件路径（带目录）
     */
    public static String standardizeFilePath (String parentDirPath, String originalFileName, String orgCode, String taskId, String pushCode) {
        if (StringUtils.isEmpty(parentDirPath)) {
            return standardizeFileName(originalFileName, orgCode, taskId, pushCode);
        }
        StringBuilder result = new StringBuilder();
        // 1.0 拼接父目录
        result.append(parentDirPath);
        if (!parentDirPath.endsWith(File.separator)) {
            result.append(File.separator);
        }
        // 2.0 拼接文件名
        result.append(standardizeFileName(originalFileName, orgCode, taskId, pushCode));
        return result.toString();
    }

    /**
     * 获取文件后缀名，默认取最后一个"."开始的作为后缀名。如果有后缀名，返回的后缀名中包含"."
     *
     * @param fileName 文件的原始名称
     * @param which    需要文件名的哪一部分
     * @return 如果需要文件有后缀名，则返回后缀名（包含"."），若需要前缀，则返回前缀；如果which不被识别，则返回文件名本身
     */
    public static String parseFileName(String fileName, String which) {
        fileName = clean(fileName);
        which = clean(which);
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            if (EXT.equalsIgnoreCase(which)) {
                return fileName.substring(index);
            } else if (PREFIX.equalsIgnoreCase(which)) {
                return fileName.substring(0, index);
            } else {
                return fileName;
            }
        } else {
            if (EXT.equalsIgnoreCase(which)) {
                return "";
            } else {
                return fileName;
            }
        }
    }

    /**
     * 再给定目录下，通过orgCode/patientId/studyUid/seriesUid/standardFileName获取文件路径
     *
     * @param basePath          给定目录
     * @param dataset           dicom文件Dataset
     * @param standardFileName  带有orgCode信息的标准文件名（普通文件名也支持，但无法获取到机构码了）
     * @param needCompletedMark 是否需要带有_complete标识（不需要的话，即使原文件名中有也会清除掉）
     * @return basePath/orgCode/patientId/studyUid/seriesUid/standardFileName的文件路径字符串
     */
    public static String formatDicomPath(String basePath, Attributes dataset, String standardFileName, boolean needCompletedMark) {
        return formatDicomPath(basePath, dataset, DICOM_PATH, standardFileName, needCompletedMark);
    }

    /**
     * 再给定目录下，通过orgCode/${attributesFormat}/standardFileName获取文件路径
     *
     * @param basePath 给定目录
     * @param dataset dicom文件Dataset
     * @param attributesFormat Attributes匹配格式，默认格式是"/{00100020,hash}/{0020000D,hash}/{0020000E,hash}/"
     * @param standardFileName 带有orgCode信息的标准文件名（普通文件名也支持，但无法获取到机构码了）
     * @param needCompletedMark 是否需要带有_complete标识（不需要的话，即使原文件名中有也会清除掉）
     * @return basePath/orgCode/patientId/studyUid/seriesUid/standardFileName的文件路径字符串
     */
    public static String formatDicomPath(String basePath, Attributes dataset, String attributesFormat,
            String standardFileName, boolean needCompletedMark) {
        basePath = clean(basePath);
        standardFileName = clean(standardFileName);
        String orgCode = getOrgCodeFromFileName(standardFileName);
        String destFilename = standardFileName;
        attributesFormat = StringUtils.isNotBlank(attributesFormat) ? attributesFormat : DICOM_PATH;
        if (needCompletedMark) {
            // 需要完成标识
            if (!hasCompletedMark(standardFileName)) {
                // 没有完成标识则加上
                destFilename = markCompleted(standardFileName);
            }
        } else {
            // 不需要完成标识
            if (hasCompletedMark(standardFileName)) {
                // 有完成标识则去掉
                destFilename = unMarkCompleted(standardFileName);
            }
        }
        if (dataset != null) {
            AttributesFormat format = new AttributesFormat(attributesFormat);
            String dicomPath = format.format(dataset);
            if (StringUtils.isBlank(orgCode)) {
                dicomPath = dicomPath.substring(1);
            }
            return basePath.endsWith(File.separator) ? (basePath + orgCode + dicomPath + destFilename)
                    : (basePath + File.separator + orgCode + dicomPath + destFilename);
        }
        return basePath.endsWith(File.separator) ? (basePath + orgCode + File.separator + destFilename)
                : (basePath + File.separator + orgCode + File.separator + destFilename);
    }

    public static String  generateFileNameWithUUID(String fileName) {
        return fileName + "@@@" + uuid();
    }

    public static String  restoreFileNameWithUUID(String fileName) {
        int index = fileName.indexOf("@@@");
        if (index == -1) {
            return fileName;
        }
        return fileName.substring(0, index);
    }

    /**
     * uuid 去掉"-"，转为小写
     *
     * @return uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * trim处理字符串
     *
     * @param s 待处理字符串
     * @return 返回处理后的字符串
     */
    private static String clean(String s) {
        return s == null ? "" : s.trim();
    }
}
