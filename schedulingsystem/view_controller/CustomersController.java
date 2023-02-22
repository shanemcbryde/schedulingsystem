/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.view_controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import schedulingsystem.DAO.QueryManager;
import schedulingsystem.model.Country;
import schedulingsystem.model.Customer;
import schedulingsystem.model.Address;
import schedulingsystem.model.Appointment;
import schedulingsystem.model.City;
import schedulingsystem.model.Scheduling;
import schedulingsystem.model.User;

/**
 * FXML Controller class
 *
 * @author Shane
 */
public class CustomersController implements Initializable {

    @FXML
    private Button monthlyBtn;
    @FXML
    private Button weeklyBtn;
    @FXML
    private Button appointmentsBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private TextField addressTxt;
    @FXML
    private TextField customerTxt;
    @FXML
    private TextField address2Txt;
    @FXML
    private TextField phoneTxt;
    @FXML
    private TextField postalTxt;
    @FXML
    private ChoiceBox<Country> countryChB;
    @FXML
    private ChoiceBox<City> cityChB;
    @FXML
    private Button addBtn;
    @FXML
    private Button modifyBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private TextField addressModifyTxt;
    @FXML
    private TextField address2ModifyTxt;
    @FXML
    private TextField phoneModifyTxt;
    @FXML
    private TextField postalModifyTxt;
    @FXML
    private Label customerModifyLbl;
    @FXML
    private ChoiceBox<Country> countryModifyChB;
    @FXML
    private ChoiceBox<City> cityModifyChB;
    @FXML
    private TableView<Customer> customerTbl;
    @FXML
    private TableColumn<Customer, String> customerCol;
    @FXML
    private TableColumn<Customer, String> addressCol;
    @FXML
    private TableColumn<Customer, String> cityCol;
    @FXML
    private TableColumn<Customer, String> phoneCol;
    @FXML
    private Button reportsBtn;

    private int countryId;
    private String customerName;
    private String address;
    private String address2;
    private int cityId;
    private String postalCode;
    private String phone;
    private int addressId;
    private int customerId;
    private String query;
    private String table;
    private boolean success;
    private Address newAddress;
    private Customer newCustomer;
    private City city;
    private final User user = schedulingsystem.model.Scheduling.getUser();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        //Populate table with customers
        //Using lambda to pass methods as arguments
        customerCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressCol.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        cityCol.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        
        customerTbl.setItems(Scheduling.getCustomers());
        
        //Order table by customer name
        TableColumn sortColumn = customerTbl.getColumns().get(0);
        customerTbl.getSortOrder().setAll(sortColumn);
        
        //Initialize customer controls
        initChoiceBox();
        initTable();
        
    }
    
    private void initTable(){
        
        //Add change listener
        customerTbl.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Customer> observableValue, Customer oldValue, Customer newValue) -> {
            
            //Check whether item is selected and set customer controls to value of selected item
            if (customerTbl.getSelectionModel().getSelectedItem() != null) {
                customerModifyLbl.setText(newValue.getName());
                addressModifyTxt.setText(newValue.getAddress().getAddress());
                address2ModifyTxt.setText(newValue.getAddress().getAddress2());
                countryModifyChB.setValue(newValue.getAddress().getCity().getCountry());
                cityModifyChB.setValue(newValue.getAddress().getCity());
                postalModifyTxt.setText(newValue.getAddress().getPostal());
                phoneModifyTxt.setText(newValue.getAddress().getPhone());
            }
        });

    }

    private void initChoiceBox() {
        
        //Populate country choice box
        countryChB.setItems(Scheduling.getCountrys());

        countryChB.setConverter(new StringConverter<Country>(){
            @Override
            public String toString(Country object) {
                return object.getName();
            }

            @Override
            public Country fromString(String string) {
                return countryChB.getItems().stream().filter(country ->
                country.getName().equals(string)).findFirst().orElse(null);
            }
        });
        
        //Add change listener and populate city choice box
        countryChB.valueProperty().addListener((obs, oldval, newval)-> {
            if(newval != null){
                countryId = newval.getId();
                ObservableList<City> citys = FXCollections.observableArrayList();
            
                //Uses lambda for clarity
                Scheduling.getCitys().forEach((cityObj)-> {
                    if(cityObj.getCountry().getId() == countryId)
                    citys.add(cityObj);
                });
                
                cityChB.setItems(citys);
            }
        });
        
        cityChB.setConverter(new StringConverter<City>(){
            @Override
            public String toString(City object) {
                return object.getName();
            }

            @Override
            public City fromString(String string) {
                return cityChB.getItems().stream().filter(city ->
                city.getName().equals(string)).findFirst().orElse(null);
            }
        });
        
        //Populate country choice box
        countryModifyChB.setItems(Scheduling.getCountrys());

        countryModifyChB.setConverter(new StringConverter<Country>(){
            @Override
            public String toString(Country object) {
                return object.getName();
            }

            @Override
            public Country fromString(String string) {
                return countryModifyChB.getItems().stream().filter(country ->
                country.getName().equals(string)).findFirst().orElse(null);
            }
        });
        
        //Add change listener and populate city choice box
        countryModifyChB.valueProperty().addListener((obs, oldval, newval)-> {
            if(newval != null){
                countryId = newval.getId();
                ObservableList<City> citys = FXCollections.observableArrayList();
            
                //Uses lambda for clarity
                Scheduling.getCitys().forEach((cityObj)-> {
                    if(cityObj.getCountry().getId() == countryId)
                    citys.add(cityObj);
                });
                
                cityModifyChB.setItems(citys);
            }
        });
        
        cityModifyChB.setConverter(new StringConverter<City>(){
            @Override
            public String toString(City object) {
                return object.getName();
            }

            @Override
            public City fromString(String string) {
                return cityModifyChB.getItems().stream().filter(city ->
                city.getName().equals(string)).findFirst().orElse(null);
            }
        });
    }    

    @FXML
    private void monthlyBtnHandler(ActionEvent event) throws IOException {
        
        Stage stage;
        Parent root;
        
        stage = (Stage) monthlyBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Monthly.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void weeklyBtnHandler(ActionEvent event) throws IOException {
        
        Stage stage;
        Parent root;
        
        stage = (Stage) weeklyBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Weekly.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void appointmentsBtnHandler(ActionEvent event) throws IOException {
        
        Stage stage;
        Parent root;
        
        stage = (Stage) appointmentsBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Appointments.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void reportsBtnHandler(ActionEvent event) throws IOException {
        
        Stage stage;
        Parent root;
        
        stage = (Stage) reportsBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Reports.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void exitBtnHandler(ActionEvent event) {
        
        Platform.exit();
    }

    @FXML
    private void addBtnHandler(ActionEvent event) throws SQLException {
        
        String alertText = "";
        boolean alertMessage = false;
        if(customerTxt.getText().trim().isEmpty()){
            alertText = "Please provide a customer name!";
            alertMessage = true;
        }else if(addressTxt.getText().trim().isEmpty()){
            alertText = "Please provide a customer address!";
            alertMessage = true;
        }else if(cityChB.getSelectionModel().isEmpty()){
            alertText = "Please select a city!";
            alertMessage = true;
        }
        
        if(alertMessage){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Fields");
            alert.setHeaderText("Empty Fields");
            alert.setContentText(alertText);
            alert.showAndWait();
        }else{
            customerName = customerTxt.getText();
            address = addressTxt.getText();
            address2 = address2Txt.getText();
            cityId = cityChB.getValue().getId();
            postalCode = postalTxt.getText();
            phone = phoneTxt.getText();
        
            customerTxt.setText("");
            addressTxt.setText("");
            address2Txt.setText("");
            postalTxt.setText("");
            phoneTxt.setText("");
            cityChB.getSelectionModel().clearSelection();
            countryChB.getSelectionModel().clearSelection();
            
            //Add customer and address to database
            QueryManager.openDB();
            table = "newAddress";
            query = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
            + "VALUES(\"" + address + "\", \"" + address2 + "\", " + cityId + ", \"" + postalCode + "\", \"" + phone + "\", NOW(), \"" + user.getName() + "\", NOW(), \"" + user.getName() + "\")";
            success = QueryManager.makeQuery(table, query);

            if(success){
                addressId = QueryManager.getLastInsertedId();
                table = "newCustomer";
                query = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES(\"" + customerName + "\", " + addressId + ", 1, NOW(), \"" + user.getName() + "\", NOW(), \"" + user.getName() + "\")";
                success = QueryManager.makeQuery(table, query);
            }
            QueryManager.closeDB();

            //Add customer and address to lists
            if(success){
                customerId = QueryManager.getLastInsertedId();
                city = Scheduling.getCity(cityId);
                newAddress = new Address(
                    addressId,
                    address,
                    address2,
                    postalCode,
                    phone,
                    city);
                    Scheduling.addAddress(newAddress);

                    newAddress = Scheduling.getAddress(addressId);
                    newCustomer = new Customer(
                    customerId,
                    customerName,
                    newAddress);

                    Scheduling.addCustomer(newCustomer);
            }
        }
    }

    @FXML
    private void modifyBtnHandler(ActionEvent event) throws SQLException {
        Customer selectedCustomer = customerTbl.getSelectionModel().getSelectedItem();
        
        if(selectedCustomer != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modify Customer");
            alert.setHeaderText("This Action Will Modify The Customer Record!");
            alert.setContentText("Do you want to make these changes to the customer's data?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                customerId = selectedCustomer.getId();
                addressId = selectedCustomer.getAddress().getId();
                address = addressModifyTxt.getText();
                address2 = address2ModifyTxt.getText();
                cityId = cityModifyChB.getValue().getId();
                city = Scheduling.getCity(cityId);
                postalCode = postalModifyTxt.getText();
                phone = phoneModifyTxt.getText();
                
                //Modify address in database
                QueryManager.openDB();
                table = "modifyCustomer";
                query = "UPDATE address SET address = \"" + address + "\", address2 = \"" + address2 + "\", cityId = " + cityId + ", postalCode = \"" + postalCode + "\", phone = \"" + phone + "\", lastUpdate = NOW(), lastUpdateBy = \"" + user.getName() + "\"  WHERE addressId = " + addressId;
                success = QueryManager.makeQuery(table, query);
                QueryManager.closeDB();
                
                //Modify address list
                if(success){
                    Customer customerObj = Scheduling.getCustomer(customerId);
                    Address addressObj = customerObj.getAddress();
                    addressObj.setAddress(address);
                    addressObj.setAddress2(address2);
                    addressObj.setCity(city);
                    addressObj.setPostal(postalCode);
                    addressObj.setPhone(phone);
                }
                customerTbl.getSelectionModel().clearSelection();
                customerModifyLbl.setText("");
                addressModifyTxt.setText("");
                address2ModifyTxt.setText("");
                postalModifyTxt.setText("");
                phoneModifyTxt.setText("");
                cityModifyChB.getSelectionModel().clearSelection();
                countryModifyChB.getSelectionModel().clearSelection();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Modify");
            alert.setHeaderText("No Customer Selected!");
            alert.setContentText("Please select a customer and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteBtnHandler(ActionEvent event) throws SQLException {
        
        Customer selectedCustomer = customerTbl.getSelectionModel().getSelectedItem();
        
        if(selectedCustomer != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("Customer May Have Appointments Set!");
            alert.setContentText("Do you want to delete this customer and any set appointments anyway?");

            //Give alert that customer will be deleted, carry out deletion if OK
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                customerId = selectedCustomer.getId();
                
                //Set customer as inactive in database and delete associated customer appointments
                QueryManager.openDB();
                table = "deleteCustomer";
                query = "UPDATE customer SET active = 0 WHERE customerId = " + customerId;
                success = QueryManager.makeQuery(table, query);
                table = "deleteAppointment";
                query = "DELETE FROM appointment WHERE customerId = " + customerId;
                success = QueryManager.makeQuery(table, query);
                QueryManager.closeDB();
                
                //Delete customer and associated appointments in lists
                if(success){
                    Iterator<Appointment> iterator = Scheduling.getAppointments().iterator();
                    while (iterator.hasNext()) {
                        Appointment appointment = iterator.next();
                        if(appointment.getCustomer().getId() == customerId){
                            iterator.remove();
                        }
                    }
                    Scheduling.deleteCustomer(Scheduling.getCustomer(customerId));
                }
                
                customerTbl.getSelectionModel().clearSelection();
                customerModifyLbl.setText("");
                addressModifyTxt.setText("");
                address2ModifyTxt.setText("");
                postalModifyTxt.setText("");
                phoneModifyTxt.setText("");
                cityModifyChB.getSelectionModel().clearSelection();
                countryModifyChB.getSelectionModel().clearSelection();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Delete");
            alert.setHeaderText("No Customer Selected!");
            alert.setContentText("Please select a customer and try again.");
            alert.showAndWait();
        }
    }
    
}
