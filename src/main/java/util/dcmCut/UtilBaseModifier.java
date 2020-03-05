package util.dcmCut;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.pacs.DicomTag;

/**
 * @program: dw_front_dfs
 * @description: dicom文件基本修改工具类
 * @author: YeDongYu
 * @create: 2019-08-26 19:24
 */
public class UtilBaseModifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilBaseModifier.class);

    public static boolean basicModifier(File src, File dest, List<DicomTag> modifierTagList){
        if (null == src || null == dest || !src.exists() || CollectionUtils.isEmpty(modifierTagList)) {
            return false;
        }
        dest.delete();
        boolean isSucc;
        try (DicomInputStream dis = new DicomInputStream(src); DicomOutputStream dos = new DicomOutputStream(dest)) {
            dis.setIncludeBulkData(DicomInputStream.IncludeBulkData.YES);
            Attributes fmi = dis.readFileMetaInformation();
            Attributes dataset = dis.readDataset(-1, -1);
            setAttr(dataset, fmi, modifierTagList);
            dos.writeDataset(fmi, dataset);
            isSucc = true;
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Err Basic Modifier For {0}", src), e);
            isSucc = false;
        }
        return isSucc;
    }

    /**
     * 修改data
     *
     * @param attributes 原attributes
     * @param fmi     Attributes
     * @param tags    待修改的tags
     */
    private static void setAttr(Attributes attributes, Attributes fmi, List<DicomTag> tags){
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
