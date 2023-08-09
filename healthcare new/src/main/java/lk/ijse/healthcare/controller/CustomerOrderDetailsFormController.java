package lk.ijse.healthcare.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import lk.ijse.healthcare.db.DbConnection;
import lk.ijse.healthcare.dto.Customer;
import lk.ijse.healthcare.dto.CustomerOrderDetails;
import lk.ijse.healthcare.dto.Medicine;
import lk.ijse.healthcare.dto.Order;
import lk.ijse.healthcare.model.CustomerModel;
import lk.ijse.healthcare.model.CustomerOrderDetailsModel;
import lk.ijse.healthcare.model.MedicineModel;
import lk.ijse.healthcare.tm.CustomerOrderDetailsTm;
import lk.ijse.healthcare.tm.CustomerTm;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerOrderDetailsFormController implements Initializable {
    public JFXTextField txtQtyOnHand;
    public ComboBox<Medicine> cbMedicine;
    public ComboBox<Customer> cbCustomer;
    public JFXTextField txtQty;
    public TextField txtOrderId
            ;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextCusId();

        setCellValueFactory();
        try {
            setComboBoxes();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        txtQty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                if(!newValue.matches("^\\d*")){
                    txtQty.setText(newValue.replaceAll("[^\\d]",""));
                }
            }
        });

    }

    public void setCmb() throws SQLException, ClassNotFoundException {
        cbCustomer.setItems(FXCollections.observableArrayList(CustomerModel.getAll()));
        cbCustomer.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer.getCustId();
            }

            @Override
            public Customer fromString(String s) {
                return null;
            }
        });
    }

    public JFXTextField txtCusOrderDetailsTotal;

    public JFXButton btnCODSave;
    public TableView<CustomerOrderDetailsTm> tblCOD;
    public TableColumn<CustomerOrderDetailsTm,String> colCODMedicineId;
    public TableColumn<CustomerOrderDetailsTm,Integer> colCODQuantity;
    public TableColumn<CustomerOrderDetailsTm,Integer> colCODTotal;

    public void btnCusOrderDetailsSaveOnAction(ActionEvent actionEvent) {
       /* String orderId = txtOrderId.getText();
        String cusId = cbCustomer.getSelectionModel().getSelectedItem().getCustId();
        String medicineId = cbMedicine.getSelectionModel().getSelectedItem().getMedId();
        int qty = Integer.parseInt(txtQty.getText());
        int total = Integer.parseInt(txtCusOrderDetailsTotal.getText());


        try{
            boolean isCreate = CustomerOrderDetailsModel.saveCusOrderDetails(cusId,medicineId,qty,total);
            if(isCreate){
                new Alert(Alert.AlertType.CONFIRMATION,"Save Owner Successfully...");
                generateNextCusId();
                getAll();
                cbCustomer.getSelectionModel().select(null);
                txtQty.clear();
                txtCusOrderDetailsTotal.clear();

            }
        }catch (SQLException | ClassNotFoundException e){
            System.out.println(e);
        }*/
    }
    private void generateNextCusId() {
        try {
            String nextId = CustomerOrderDetailsModel.generateNextOrderId();
            txtOrderId.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    void setCellValueFactory() {
        //colCusId.setCellValueFactory(new PropertyValueFactory<>("cusId"));
        colCODMedicineId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colCODQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colCODTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

    }
    void getAll() {
        try {
            ObservableList<CustomerOrderDetailsTm> obList = FXCollections.observableArrayList();
            List<CustomerOrderDetails> CusOrderDetailsList = CustomerOrderDetailsModel.getAll();

            for (CustomerOrderDetails customerOrderDetails : CusOrderDetailsList) {
                obList.add(new CustomerOrderDetailsTm(
                        customerOrderDetails.getCusId(),
                        customerOrderDetails.getMedicineId(),
                        customerOrderDetails.getQty(),
                        customerOrderDetails.getTotal()


                ));
            }
            tblCOD.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }


    public void btnCODDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnCODUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        /*String cusId = txtCusOrderDetailsCusId.getText();
        String medicineId = txtCusOrderDetailsMediId.getText();
        int qty = Integer.parseInt(txtCusOrderDetailsQuantity.getText());
        int total =Integer.parseInt(txtCusOrderDetailsTotal.getText());


        CustomerOrderDetails customerOrderDetails = new CustomerOrderDetails(cusId,medicineId,qty,total);

        boolean isUpdated = CustomerOrderDetailsModel.update(customerOrderDetails);
        if (isUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "huree! Supplier Updated!").show();
            getAll();
        }else{
            new Alert(Alert.AlertType.ERROR, "oops! something happened!").show();
        }*/

    }

    public void setComboBoxes() throws SQLException, ClassNotFoundException {
        cbCustomer.setItems(FXCollections.observableArrayList(CustomerModel.getAll()));
        cbMedicine.setItems(FXCollections.observableArrayList(MedicineModel.getAll()));
        cbCustomer.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer==null ? null : customer.getCustId();
            }

            @Override
            public Customer fromString(String s) {
                return null;
            }
        });

        cbMedicine.setConverter(new StringConverter<Medicine>() {
            @Override
            public String toString(Medicine medicine) {
                return medicine==null ? null : medicine.getMedId();
            }

            @Override
            public Medicine fromString(String s) {
                return null;
            }
        });
    }


    public void btnSaveOrderOnAction(ActionEvent actionEvent) {
        int total = 200;
        ObservableList<CustomerOrderDetailsTm> items = tblCOD.getItems();
        Order order = new Order(txtOrderId.getText(),
                cbCustomer.getSelectionModel().getSelectedItem().getCustId()
                , total);
        try {
            boolean isPlaced = CustomerOrderDetailsModel.placeOrder(items, order);
            if(isPlaced){
                new Alert(Alert.AlertType.INFORMATION,"Order Placed Success :)").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Order Placing Failed :(").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Error Occured").show();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        CustomerOrderDetailsTm selectedItem = tblCOD.getSelectionModel().getSelectedItem();
        tblCOD.getItems().remove(selectedItem);
        if(selectedItem==null)return;
        ObservableList<Medicine> items = cbMedicine.getItems();
        for(Medicine ob:items){
            if(ob.getMedId().equals(selectedItem.getMedicineId())){
                ob.setMedStock(String.valueOf(Integer.parseInt(ob.getMedStock())+selectedItem.getQty()));
            }
        }
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        Medicine selectedItem = cbMedicine.getSelectionModel().getSelectedItem();
        CustomerOrderDetailsTm customerOrderDetailsTm = new CustomerOrderDetailsTm(txtOrderId.getText(), selectedItem.getMedId(),
                Integer.parseInt(txtQty.getText()), Integer.parseInt(txtCusOrderDetailsTotal.getText()));
        ObservableList<CustomerOrderDetailsTm> items = tblCOD.getItems();
        selectedItem.setMedStock(String.valueOf(Integer.parseInt(selectedItem.getMedStock())-Integer.parseInt(txtQty.getText())));
        for(CustomerOrderDetailsTm ob : items){
            if(ob.getMedicineId().equals(customerOrderDetailsTm.getMedicineId())){
                ob.setQty(customerOrderDetailsTm.getQty()+ob.getQty());
                ob.setTotal(customerOrderDetailsTm.getTotal()+ob.getTotal());
                tblCOD.refresh();
                return;
            }
        }
        items.addAll(customerOrderDetailsTm);
    }

    public void txtQtyOnKeyReleasedAction(KeyEvent keyEvent) {
        Medicine selectedItem = cbMedicine.getSelectionModel().getSelectedItem();
        txtCusOrderDetailsTotal.setText(String.valueOf(Integer.parseInt(txtQty.getText())*selectedItem.getPrice()));
    }

    public void cbMedicineOnAction(ActionEvent actionEvent) {
        txtQtyOnHand.setText(cbMedicine.getSelectionModel().getSelectedItem().getMedStock());
    }

    public void custOrderDetailsPrint(ActionEvent actionEvent) {
        new Thread() {
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\DARSHANA\\Desktop\\courseWork\\healthcare01\\src\\main\\resources\\js-reports\\orderDetails_all_form.jrxml"));

                    JasperReport js = JasperCompileManager.compileReport(load);

                    JasperPrint jp = JasperFillManager.fillReport(js, null, DbConnection.getInstance().getConnection());

                    JasperViewer viewer = new JasperViewer(jp , false);
                    viewer.show();

                } catch (JRException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                this.stop();
            }
        }.start();
    }
}
