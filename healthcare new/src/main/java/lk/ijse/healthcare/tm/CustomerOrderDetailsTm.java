package lk.ijse.healthcare.tm;

public class CustomerOrderDetailsTm {
    private String orderId;
    private String medicineId;
    private int qty;
    private int total;

    public CustomerOrderDetailsTm() {

    }


    public CustomerOrderDetailsTm(String orderId, String medicineId, int qty, int total) {
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.qty = qty;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
