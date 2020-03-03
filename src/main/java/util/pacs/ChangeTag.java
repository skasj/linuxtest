package util.pacs;

import com.deepwise.dicom.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;

/**
 * @program: linuxtest
 * @description: 修改dicom文件的Tag值
 * @author: YeDongYu
 * @create: 2019-11-12 13:48
 */
public class ChangeTag {


    public static void main(String[] args) throws IOException {
        File src = new File("C:\\Users\\99324\\Desktop\\pacsView\\356\\1.dcm");
//        File src = new File("C:\\Users\\99324\\Desktop\\pacsView\\打印机\\0.dcm");
        File dest = new File("C:\\Users\\99324\\Desktop\\pacsView\\356\\1-change.dcm");
//        File dest = new File("C:\\Users\\99324\\Desktop\\pacsView\\打印机\\0-change.dcm");
        List<DicomTag> dicomTagList = new ArrayList<>();
//        dicomTagList.add(new DicomTag(Tag.SOPInstanceUID, VR.UI, "1.3.12.2.1107.5.1.4.75509.20180423102634660330"));
//        dicomTagList.add(new DicomTag(Tag.MediaStorageSOPClassUID, VR.UI, "1.2.840.10008.5.1.4.1.1.7"));
//        dicomTagList.add(new DicomTag(Tag.MediaStorageSOPInstanceUID, VR.UI, "1.3.6.1.4.1.30071.8.247207022285742.6208763952390564"));
//        dicomTagList.add(new DicomTag(Tag.SOPInstanceUID, VR.UI, "1.3.12.2.1107.5.1.4.75509.20180423102634660330"));
        basicModifier(src,dest, dicomTagList);
    }

    private static boolean basicModifier(File src, File dest, List<DicomTag> modifierTagList) throws IOException {
        if (null == src || null == dest || !src.exists() || CollectionUtils.isEmpty(modifierTagList)) {
//            return false;
        }
        dest.delete();
        boolean isSucc;
        DicomInputStream dis = new DicomInputStream(src);
        DicomOutputStream dos = new DicomOutputStream(dest);
        dis.setIncludeBulkData(DicomInputStream.IncludeBulkData.YES);
        Attributes fmi = dis.readFileMetaInformation();
        Attributes dataset = readFully(src,true);
        dataset.getDate(Tag.StudyDateAndTime);
//        setAttr(dataset, fmi, modifierTagList);
        dos.writeDataset(fmi, dataset);
        return true;
    }

    /**
     * 修改data
     *
     * @param attributes 原attributes
     * @param fmi Attributes
     * @param tags 待修改的tags
     */
    private static void setAttr(Attributes attributes, Attributes fmi, List<DicomTag> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return;
        }
        for (DicomTag dicomTag : tags) {
            VR vr = dicomTag.getVr() == null ? attributes.getVR(dicomTag.getTag()) : dicomTag.getVr();
            if (dicomTag.isFmi()) {
                fmi.setValue(dicomTag.getTag(), vr, dicomTag.getValue());
            } else {
                attributes.setValue(dicomTag.getTag(), vr, dicomTag.getValue());
            }
        }
    }

    /**
     * 读取dicom文件Tag值（包括PixelData）
     *
     * @param dcm        dicom文件
     * @param includeFmi 是否需要fmi
     * @return 读取成功，返回dataset（+fmi）；否则返回null
     */
    public static Attributes readFully(File dcm, boolean includeFmi) {
        if (FileUtils.exists(dcm)) {
            try (DicomInputStream dis = new DicomInputStream(dcm)) {
                Attributes dataset = dis.readDataset(-1, -1);
                if (includeFmi) {
                    return assembleFull(dis, dataset);
                }
                return dataset;
            } catch (IOException e) {
//                log.error(MessageFormat.format("Read Dicom {0} Failed.", dcm), e);
            }
        }
        return null;
    }

    private static Attributes assembleFull(DicomInputStream dis, Attributes dataset) {
        Attributes fmi;
        try {
            fmi = dis.readFileMetaInformation();
        } catch (IOException e) {
            e.printStackTrace();
            return dataset;
        }
        Attributes full = new Attributes(dataset.size() + fmi.size());
        full.addAll(fmi);
        full.addAll(dataset);
        return full;
    }
}
