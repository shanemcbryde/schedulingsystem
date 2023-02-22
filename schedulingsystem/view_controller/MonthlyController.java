/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.view_controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import schedulingsystem.DAO.QueryManager;
import schedulingsystem.model.Appointment;
import schedulingsystem.model.Scheduling;
import schedulingsystem.model.User;

/**
 * FXML Controller class
 *
 * @author Shane
 */
public class MonthlyController implements Initializable {

    @FXML
    private Button appointmentsBtn;
    @FXML
    private Button customersBtn;
    @FXML
    private Button weeklyBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Label monthLbl;
    @FXML
    private Label yearLbl;
    @FXML
    private TableView<Appointment> scheduleTbl;
    @FXML
    private TableColumn<Appointment, String> dateCol;
    @FXML
    private TableColumn<Appointment, String> startCol;
    @FXML
    private TableColumn<Appointment, Integer> endCol;
    @FXML
    private TableColumn<Appointment, String> customerCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, String> addressCol;
    @FXML
    private TableColumn<Appointment, String> cityCol;
    @FXML
    private TableColumn<Appointment, String> postalCol;
    @FXML
    private TableColumn<Appointment, String> countryCol;
    @FXML
    private TableColumn<Appointment, String> phoneCol;
    @FXML
    private Button reportsBtn;
    @FXML
    private Button previousBtn;
    @FXML
    private Button nextBtn;
    
    private String table;
    private String query;
    private final User user = schedulingsystem.model.Scheduling.getUser();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDate nextPrevious = today.plusMonths(Scheduling.monthsNextPrevious);
        String monthToday = nextPrevious.getMonth().name();
        String yearToday =  Integer.toString(nextPrevious.getYear());
        LocalTime timeAppointment;
        LocalDate dateAppointment;
        String monthAppointment;
        int yearAppointment;
        String appointmentName = "";
        int appointmentId = 0;
        
        monthLbl.setText(monthToday);
        yearLbl.setText(yearToday);
        
        ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
        
        //Populate lists when first opened
        if(!Scheduling.populated){
            try {
                queryHandler();
            } catch (SQLException ex) {
                Logger.getLogger(MonthlyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Determine if there is an appointment within 15 minute of opening Monthly View
        for (Appointment appointment : Scheduling.getAppointments()) {
            timeAppointment = LocalTime.parse(appointment.getTime());
            dateAppointment = LocalDate.parse(appointment.getDate());
            yearAppointment = dateAppointment.getYear();
            if(yearAppointment == nextPrevious.getYear()){
                monthAppointment = dateAppointment.getMonth().name();
                if(monthAppointment.equals(monthToday)){
                    monthlyAppointments.add(appointment);
                    if(dateAppointment.compareTo(today) == 0){
                        if(timeAppointment.compareTo(time) >= 0 && timeAppointment.compareTo(time.plusMinutes(15)) <= 0){
                            appointmentId = appointment.getId();
                            appointmentName = appointment.getCustomer().getName();
                        }
                    }
                }
            }
        }
        //Populated monthly table
        //Using lambda to pass methods as arguments
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        startCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        endCol.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asObject());
        customerCol.setCellValueFactory(cellData -> cellData.getValue().customerProperty());
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        addressCol.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        cityCol.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        postalCol.setCellValueFactory(cellData -> cellData.getValue().postalProperty());
        countryCol.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        
        scheduleTbl.setItems(monthlyAppointments);
        
        //Order table by date and start time
        TableColumn sortColumn = scheduleTbl.getColumns().get(1);
        scheduleTbl.getSortOrder().setAll(sortColumn);
        sortColumn = scheduleTbl.getColumns().get(0);
        scheduleTbl.getSortOrder().setAll(sortColumn);
        
        if(appointmentId > 0){
            initTable(appointmentId, appointmentName);
        }
        
    }
    
    private void initTable(int id, String name){
        
        //If appointment within 15 minutes, alert and highlight appointment in table
        for(int i = 0; i < scheduleTbl.getItems().size(); i++){
            Appointment tableAppointment = scheduleTbl.getItems().get(i);
            if(tableAppointment.getId() == id){
                scheduleTbl.requestFocus();
                scheduleTbl.getSelectionModel().select(i);
                scheduleTbl.getFocusModel().focus(i);
            }
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Appointment Alert!");
        alert.setContentText("You have an appointment with " + name + " within 15 minutes!");
        alert.showAndWait();
    }
    
    private void queryHandler() throws SQLException{
        
        boolean success;
        
        //Open database and populate all lists
        QueryManager.openDB();
        
        //Set all customers in database to active for testing purposes
//        table = "deleteCustomer";
//        query = "UPDATE customer SET active = 1";
//        QueryManager.makeQuery(table, query);
        
        table = "country";
        query = "SELECT countryId, country FROM country";
        success = QueryManager.makeQuery(table, query);
        
        if(success) {
            table = "city";
            query = "SELECT cityId, city, countryId FROM city";
            success = QueryManager.makeQuery(table, query);
        }
        
        if(success) {
            table = "address";
            query = "SELECT addressId, address, address2, cityId, postalCode, phone FROM address";
            success = QueryManager.makeQuery(table, query);
        }
        
        if(success) {
            table = "customer";
            query = "SELECT customerId, customerName, addressId FROM customer Where active = 1";
            success = QueryManager.makeQuery(table, query);
        }
        
        if(success) {
            table = "appointment";
            query = "SELECT appointmentId, customerId, type, start, end FROM appointment WHERE userId = " + user.getId();
            success = QueryManager.makeQuery(table, query);
        }
        
        QueryManager.closeDB();
        
        Scheduling.populated = success;
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
    private void customersBtnHandler(ActionEvent event) throws IOException {
        
        Stage stage;
        Parent root;
        
        stage = (Stage) customersBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Customers.fxml"));
        
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
    private void PreviousBtnHandler(ActionEvent event) throws IOException {
        
        Scheduling.monthsNextPrevious -= 1;
        
        Stage stage;
        Parent root;
        
        stage = (Stage) previousBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Monthly.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void nextBtnHandler(ActionEvent event) throws IOException {
        
        Scheduling.monthsNextPrevious += 1;
        
        Stage stage;
        Parent root;
        
        stage = (Stage) nextBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Monthly.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
}
