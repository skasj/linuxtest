package util.pacs;

import org.dcm4che3.data.VR;

/**
 * DicomTag
 *
 * @author cairui
 * @version v1.0.0 2019/3/12 19:40
 */
public class DicomTag {

    private int tag;
    private Object value;
    private VR vr;
    private boolean isFmi = false;

    public DicomTag() {
    }

    public DicomTag(int tag, Object value) {
        this.tag = tag;
        this.value = value;
    }

    public DicomTag(int tag, VR vr, Object value) {
        this.tag = tag;
        this.value = value;
        this.vr = vr;
    }

    public DicomTag(boolean isFmi, int tag, VR vr) {
        this.tag = tag;
        this.value = value;
        this.vr = vr;
        this.isFmi = isFmi;
    }

    public DicomTag(boolean isFmi, int tag, VR vr, Object value) {
        this.tag = tag;
        this.value = value;
        this.vr = vr;
        this.isFmi = isFmi;
        this.value = value;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public VR getVr() {
        return vr;
    }

    public void setVr(VR vr) {
        this.vr = vr;
    }

    public boolean isFmi() {
        return isFmi;
    }

    public void setFmi(boolean fmi) {
        isFmi = fmi;
    }
}
