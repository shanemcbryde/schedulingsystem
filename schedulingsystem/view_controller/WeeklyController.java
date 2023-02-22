/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.view_controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import schedulingsystem.model.Appointment;
import schedulingsystem.model.Scheduling;

/**
 * FXML Controller class
 *
 * @author Shane
 */
public class WeeklyController implements Initializable {

    @FXML
    private Button appointmentsBtn;
    @FXML
    private Button customersBtn;
    @FXML
    private Button monthlyBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Label startLbl;
    @FXML
    private Label endLbl;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM, dd YYYY");
        LocalDate localDateUse = LocalDate.now().plusWeeks(Scheduling.weeksNextPrevious);
        startLbl.setText(formatter.format(localDateUse));
        endLbl.setText(formatter.format(localDateUse.plusDays(6)));
        
        ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
        LocalDate dateAppointment;
        
        for (Appointment appointment : Scheduling.getAppointments()){
            dateAppointment = LocalDate.parse(appointment.getDate());
            if(dateAppointment.compareTo(localDateUse) >= 0 & dateAppointment.compareTo(localDateUse.plusDays(6)) <= 0){
                weeklyAppointments.add(appointment);
            }
        }
        //Populating weekly table
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
        
        scheduleTbl.setItems(weeklyAppointments);
        
        //Order table by date and start time
        TableColumn sortColumn = scheduleTbl.getColumns().get(1);
        scheduleTbl.getSortOrder().setAll(sortColumn);
        sortColumn = scheduleTbl.getColumns().get(0);
        scheduleTbl.getSortOrder().setAll(sortColumn);
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
    private void previousBtnHandler(ActionEvent event) throws IOException {
        
        Scheduling.weeksNextPrevious -= 1;
        
        Stage stage;
        Parent root;
        
        stage = (Stage) previousBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Weekly.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void nextBtnHandler(ActionEvent event) throws IOException {
        
        Scheduling.weeksNextPrevious += 1;
        
        Stage stage;
        Parent root;
        
        stage = (Stage) nextBtn.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("Weekly.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
}
