package lk.ijse.healthcare.model;

import javafx.collections.ObservableList;
import lk.ijse.healthcare.db.DbConnection;
import lk.ijse.healthcare.dto.Customer;
import lk.ijse.healthcare.dto.CustomerOrderDetails;
import lk.ijse.healthcare.dto.Order;
import lk.ijse.healthcare.tm.CustomerOrderDetailsTm;
import lk.ijse.healthcare.util.CrudUtill;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomerOrderDetailsModel {
    private final static String URL = "jdbc:mysql://localhost:3306/pharmacy";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "root");
    }
    public static boolean saveCusOrderDetails(String orderId, String medicineId,int qty, int total) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO order_detail(order_id,med_Id,qty,total) VALUES(?, ?, ?, ?)";

        return CrudUtill.execute(sql,orderId,medicineId,qty,total);
    }

    public static String generateNextOrderId() throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();

        String sql = "SELECT id FROM orders ORDER BY id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if(resultSet.next()) {
            return splitCusId(resultSet.getString(1));
        }
        return splitCusId(null);
    }

    public static String splitCusId(String currentCusId) {
        if(currentCusId != null) {
            String[] strings = currentCusId.split("O-");
            int id = Integer.parseInt(strings[1]);
            id++;

            return String.format("O-%03d",id);
        }
        return "O-001";
    }
    public static List<CustomerOrderDetails> getAll() throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM order_detail";

        List<CustomerOrderDetails> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new CustomerOrderDetails(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getInt(4)

            ));
        }
        return data;
    }
    public static boolean update(CustomerOrderDetails customerOrderDetails) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE order_detail SET med_Id = ?, qty = ?, total = ? WHERE order_id = ?";
        return CrudUtill.execute(sql,customerOrderDetails.getMedicineId(),customerOrderDetails.getQty(),customerOrderDetails.getTotal(),customerOrderDetails.getCusId());
    }

    public static boolean placeOrder(ObservableList<CustomerOrderDetailsTm> items, Order order) throws SQLException, ClassNotFoundException {
        DbConnection.getInstance().getConnection().setAutoCommit(false);

        try {
            boolean isOrderPlaced = OrderModel.addOrder(order);
            if(isOrderPlaced){
                boolean isAllAdded = addDetail(items);
                if(isAllAdded){
                    boolean isAllQtyUpdated = MedicineModel.decreaseQty(items);
                    if(isAllQtyUpdated){
                        DbConnection.getInstance().getConnection().commit();
                        return true;
                    }
                }
            }
            DbConnection.getInstance().getConnection().rollback();
            return false;
        } catch (SQLException e) {
            DbConnection.getInstance().getConnection().rollback();
            throw e;
        } catch (ClassNotFoundException e) {
            DbConnection.getInstance().getConnection().rollback();
            throw e;
        }finally {
            DbConnection.getInstance().getConnection().setAutoCommit(true);
        }

    }

    public static boolean addDetail(ObservableList<CustomerOrderDetailsTm> items) throws SQLException, ClassNotFoundException {
        for(CustomerOrderDetailsTm ob : items){
            boolean isAdded = saveCusOrderDetails(ob.getOrderId(), ob.getMedicineId(), ob.getQty(), ob.getTotal());
            if(!isAdded){
                return false;
            }
        }
        return true;
    }
}
