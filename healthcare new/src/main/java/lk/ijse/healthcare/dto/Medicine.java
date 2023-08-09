package lk.ijse.healthcare.dto;

public class Medicine {

    private String medId;
    private String medName;
    private String medType;
    private String medStock;
    private int price;

    public Medicine(String medId, String medName, String medType, String medStock, int price) {
        this.medId = medId;
        this.medName = medName;
        this.medType = medType;
        this.medStock = medStock;
        this.price = price;
    }

    public Medicine() {

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
