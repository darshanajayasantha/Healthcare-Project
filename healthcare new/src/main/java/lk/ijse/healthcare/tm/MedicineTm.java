package lk.ijse.healthcare.tm;

public class MedicineTm {
    private String medId;
    private String medName;
    private String medType;
    private String medStock;

    public MedicineTm(String medId, String medName, String medType, String medStock) {
        this.medId = medId;
        this.medName = medName;
        this.medType = medType;
        this.medStock = medStock;
    }

    public MedicineTm() {

    }

    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getMedStock() {
        return medStock;
    }

    public void setMedStock(String medStock) {
        this.medStock = medStock;
    }
}
