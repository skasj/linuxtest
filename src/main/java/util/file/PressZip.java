package util.file;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Scanner;

/**
 * @program: linuxtest
 * @description: 压缩文件生成zip包
 * @author: YeDongYu
 * @create: 2019-03-27 14:41
 */
public class PressZip {
    
    private static String saveFilePath;
    private static String uploadingFilePath;
    
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        PressZip pressZip = new PressZip();
        System.out.println("输入文件来源路径 ,例如：/data1/lung");
        saveFilePath = in.nextLine();
        System.out.println("输入压缩包压缩路径 ,例如：/data1/press");
        uploadingFilePath = in.nextLine();
        System.out.println("开始打包");
        pressZip.scanAndMoveFilesForUpload(new File(saveFilePath));
        pressZip.packThePackageIfFileExist();
        System.out.println("结束打包");
    }
    
    /**
     * 遍历文件夹下所有文件并转移到目标文件夹中 使用递归 用于上传
     * 用途：转移后进行批量打包，准备上传
     *
     * @param fromFile 原文件夹
     */
    private void scanAndMoveFilesForUpload(File fromFile) {
        if (fromFile.isDirectory()) {
            File[] files = fromFile.listFiles();
            if (files != null) {
                for (File item : files) {
                    scanAndMoveFilesForUpload(item);
                }
            }
        }
        checkPackingSpaceAndPacking(fromFile);
    }
    
    /**
     * 检查打包，计算待打包空间使用量，达到条件则进行打包处理，最后移动文件
     * 1、如果使用量达到上限，则进行具体打包流程
     * 2、如果使用量未达到上限，使用量加上该文件大小
     * 3、最后进行移动文件
     *
     * @param file 待转移文件
     */
    private void checkPackingSpaceAndPacking(File file) {
        PackingSpace packingSpace = PackingSpace.getInstance();
        String packingSpaceName = uploadingFilePath + File.separator + packingSpace.getPackingName();
        createDirIfNotExist(packingSpaceName);
        long fileSize = file.length();
        //计算加上该文件后使用量大小
        long temp = packingSpace.getTempSize() + fileSize;
        //加上该文件后，超出上限
        if (temp > packingSpace.getMaxSize()) {
            moveFile(file,
                new File(uploadingFilePath + File.separator + packingSpace.getPackingName()));
            //加上使用量
            packingSpace.setTempSize(fileSize + packingSpace.getTempSize());
            //具体打包流程
            packingFileAndRenamePackingSpace();
            return;
        }
        moveFile(file, new File(uploadingFilePath + File.separator + packingSpace.getPackingName()));
        //加上使用量
        packingSpace.setTempSize(fileSize + packingSpace.getTempSize());
    }
    
    /**
     * create Dir if Dir is not exist
     * @param dirPath
     * @return file
     */
    private File createDirIfNotExist (String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    
    /**
     * 定时打包，防止大小达不到上限而一直滞留不打包的情况
     * 1、如果使用量大于0，表示存在文件，进行具体打包流程
     * 2、如果使用量不大于0，则不处理
     */
    public void packThePackageIfFileExist() {
        PackingSpace packingSpace = PackingSpace.getInstance();
        if (packingSpace.getTempSize() > 0) {
            packingFileAndRenamePackingSpace(); //具体打包流程
        }
    }
    
    /**
     * 具体打包流程，打包完成后删除原文件，修改打包文件名为 文件名 + "-" + "uploading" 标识为：待上传的完整zip包，重新命名和创建待打包空间，清空使用量
     */
    public void packingFileAndRenamePackingSpace() {
        PackingSpace packingSpace = PackingSpace.getInstance();
        if (packingSpace.getTempSize() <= 0) {
            return;
        }
        //改名 添加 -uploading 标识 压缩完成，待上传
        String zipFileName = uploadingFilePath + File.separator + packingSpace.getPackingName();
        //进行压缩打包
        UtilZip.startCompress(zipFileName);
        addSignForFile(new File(zipFileName + ".zip"), FileStateEnum.UPLOADING.getStateInfo());
        //删除原文件
        UtilFile.deleteDir(zipFileName);
        //重新修改文件夹名
        packingSpace.setPackingName("" + System.currentTimeMillis());
        String newPackingSpace = uploadingFilePath + File.separator + packingSpace.getPackingName();
        //不存在则创建新文件夹
        UtilFile.createDirIfNotExist(newPackingSpace);
        //清空使用量
        packingSpace.setTempSize(0L);
    }
    
    /**
     * 移动文件
     *
     * @param sourceFile 源文件
     * @param targetFile 转移后目标文件夹
     */
    private void moveFile(File sourceFile, File targetFile) {
        File newFile = createFile(targetFile, sourceFile); //新建文件，用于写入
        boolean result = writeByFileChannel(sourceFile, newFile); //文件写入
        if (result) { //true 表示文件完全写入
            deleteSignForFile(newFile, FileStateEnum.TRANSFERING.getStateInfo()); //去掉 -transfering 标识，表示完整文件
        } else { //false 表示文件未完全写入
            newFile.delete(); //删除newFile
        }
    }
    
    /**
     * 创建文件，用于写入
     * 新建文件名为 = 原文件名 + "-" + 系统时间 + "-transfering" + 后缀
     *
     * @param sourceFile     原文件
     * @param targetFilePath 转移后目标文件夹
     * @return 新建文件(空, 未写入信息)
     */
    private File createFile(File targetFilePath, File sourceFile) {
        FileNameAnalysis fileNameAnalysis = new FileNameAnalysis(); //文件名解析 model
        fileNameAnalysis.analysisFile(sourceFile); //进行解析
        
        long nowTime = System.currentTimeMillis();
        String fileName = fileNameAnalysis.getRealFileName() + "-" + nowTime
                          + FileStateEnum.TRANSFERING.getStateInfo() + fileNameAnalysis.getFormat(); //原文件名添加系统时间防止重名情况
        File newFile = new File(targetFilePath.getAbsolutePath() + File.separator + fileName);
        try {
            newFile.createNewFile(); //新建文件
        } catch (IOException e) {
        }
        return newFile;
    }
    
    /**
     * 为文件名添加 str 标识
     *
     * @param file 文件
     * @param str  待添加的标识
     * @return 修改后文件名(AbsolutePath)
     */
    private String addSignForFile(File file, String str) {
        if (file.exists()) { //文件存在
            FileNameAnalysis fileNameAnalysis = new FileNameAnalysis(); //解析文件名
            fileNameAnalysis.analysisFile(file);
            String realName = file.getParent() + File.separator
                              + fileNameAnalysis.getRealFileName() //父目录以及文件名
                              + str //在文件名后添加str标识
                              + fileNameAnalysis.getFormat(); //文件后缀
            file.renameTo(new File(realName)); //重命名
            return realName;
        }
        return null;
    }
    
    /**
     * 为文件去掉 str 标识
     * @param file 文件
     * @param str 标识
     * @return 修改后文件名(AbsolutePath)
     */
    private String deleteSignForFile(File file, String str) {
        if (file.exists()) {
            if (file.getAbsolutePath().contains(str)) { //如果包含该项标识
                String newFileName = file.getAbsolutePath().replace(str, "");
                file.renameTo(new File(newFileName));
                return newFileName;
            }
        }
        return null;
    }
    
    /**
     * 用 fileChannel 文件转移具体操作(文件内容 文件1->文件2,且只保留文件2) 效率比普通IO流方式更高
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     * @return true->完成写入，false->未完成写入
     */
    private boolean writeByFileChannel(File fromFile, File toFile) {
        boolean resultTag = false;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannelInput = null;
        FileChannel fileChannelOutput = null;
        try {
            fileInputStream = new FileInputStream(fromFile);
            fileOutputStream = new FileOutputStream(toFile);
            fileChannelInput = fileInputStream.getChannel(); //得到fileInputStream的文件通道
            fileChannelOutput = fileOutputStream.getChannel(); //得到fileOutputStream的文件通道
            fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput); //将fileChannelInput通道的数据，写入到fileChannelOutput通道
            resultTag = true; //正常流程，已将file1文件内容全部写入file2
        } catch (IOException e) {
            resultTag = false; //出现异常情况，传输过程中断
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileChannelInput != null) {
                    fileChannelInput.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (fileChannelOutput != null) {
                    fileChannelOutput.close();
                }
            } catch (IOException e) {
            }
        }
        return resultTag;
    }
}
