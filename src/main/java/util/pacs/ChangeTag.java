package util.pacs;

import java.io.File;
import java.io.IOException;
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
        File src = new File("C:\\Users\\99324\\Desktop\\1031fix\\DICOM\\1.dcm");
        File dest = new File("C:\\Users\\99324\\Desktop\\1031fix\\DICOM\\2.dcm");
        DicomTag dicomTag = new DicomTag(Tag.PatientName, VR.LO, "\uD867\uDCFF");
        basicModifier(src,dest, Collections.singletonList(dicomTag));
    }

    private static boolean basicModifier(File src, File dest, List<DicomTag> modifierTagList) throws IOException {
        if (null == src || null == dest || !src.exists() || CollectionUtils.isEmpty(modifierTagList)) {
            return false;
        }
        dest.delete();
        boolean isSucc;
        DicomInputStream dis = new DicomInputStream(src);
        DicomOutputStream dos = new DicomOutputStream(dest);
        dis.setIncludeBulkData(DicomInputStream.IncludeBulkData.YES);
        Attributes fmi = dis.readFileMetaInformation();
        Attributes dataset = dis.readDataset(-1, -1);
        setAttr(dataset, fmi, modifierTagList);
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
}
