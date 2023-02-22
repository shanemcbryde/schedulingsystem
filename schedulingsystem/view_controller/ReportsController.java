/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.view_controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import schedulingsystem.model.Appointment;
import schedulingsystem.model.Report;
import schedulingsystem.model.Scheduling;

/**
 * FXML Controller class
 *
 * @author Shane
 */
public class ReportsController implements Initializable {

    @FXML
    private Button appointmentsBtn;
    @FXML
    private Button customersBtn;
    @FXML
    private Button monthlyBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Button weeklyBtn;
    @FXML
    private Button scheduleBtn;
    @FXML
    private Button byTypeBtn;
    @FXML
    private Button byCustomerBtn;
    @FXML
    private TextArea reportTxtArea;
    
    private static ObservableList<Report> reportData;
    private static ObservableList<Report> sortedByName;
    private static ObservableList<Report> sortedByDt;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        reportData = FXCollections.observableArrayList();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        Report report;
        int id;
        String name;
        LocalDate localDate;
        LocalTime localTime;
        LocalDateTime dt;
        
        //Populate reportData list for use throughout
        for(Appointment appointment: Scheduling.getAppointments()){
            id = appointment.getId();
            name = appointment.getCustomer().getName();
            localDate = LocalDate.parse(appointment.getDate(), dateFormatter);
            localTime = LocalTime.parse(appointment.getTime(), timeFormatter);
            dt = LocalDateTime.of(localDate, localTime);
            
            report = new Report(
            id,
            name,
            dt);
            
            reportData.add(report);
        }
        //Populate sorted datetime and name lists
        //Using lambda within a stream for ease of use
        sortedByDt = reportData.stream()
            .sorted(Comparator.comparing(Report::getDt))
            .collect(Collectors.toCollection(()-> FXCollections.observableArrayList()));
        //Using lambda within a stream for ease of use
        sortedByName = reportData.stream()
                .sorted(Comparator.comparing(Report::getName))
                .collect(Collectors.toCollection(()-> FXCollections.observableArrayList()));
        
    }

    @FXML
    private void scheduleBtnHandler(ActionEvent event) {
        
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime dataDateTime;
        String monthPrevious = "";
        String month;
        String name;
        String type;
        String date;
        String time;
        String text = String.format("\t\t\t%-35s\t%-35s\t\t%-35s\t\t%-5s\n","CUSTOMER" ,"TYPE" ,"DATE" ,"TIME");
        Appointment appointment;
        
        //Create appointment schedule report for existing user sorted by month and date
        for(Report report: sortedByDt){
            appointment = Scheduling.getAppointment(report.getId());
            dataDateTime = report.getDt();
            month = dataDateTime.getMonth().name();
            name = appointment.getCustomer().getName();
            type = appointment.getType();
            date = appointment.getDate();
            time = appointment.getTime();
            
            if(!month.equals(monthPrevious)){
                text = text + "\n" + month + "\n";
                monthPrevious = month;
            }
            
            //List past appointments as "Expired"
            if(dataDateTime.compareTo(localDateTime) < 0){
                text = text + "\t(Expired)";
            }else{
                text = text + "\t\t";
            }
            
            text = text + String.format("\t%-35s\t%-35s\t%-35s\t%-5s\n",name ,type ,date ,time);
        }
        reportTxtArea.setText(text);
        
    }

    @FXML
    private void byTypeBtnHandler(ActionEvent event) {
        
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime dataDateTime;
        String appMonth;
        int typeTotal;
        String text = String.format("\t\t\t%-20s\t\t%-5s\n","TYPE" ,"TOTAL");
        
        Appointment appointment;
        Set<String> months = new TreeSet<>();
        Set<String> types = new TreeSet<>();
        
        //Create ordered TreeSets of months and types
        for(Report report: sortedByDt){
            dataDateTime = report.getDt();
            months.add(dataDateTime.getMonth().name());
            appointment = Scheduling.getAppointment(report.getId());
            types.add(appointment.getType());
        }
        
        //Create type report sorted by month and type
        for(String month: months){
            text = text + "\n" + month + "\n";
            for(String type: types){
                typeTotal = 0;
                for(Report report: sortedByDt){
                    appointment = Scheduling.getAppointment(report.getId());
                    dataDateTime = report.getDt();
                    appMonth = dataDateTime.getMonth().name();
                    
                    if(type.equals(appointment.getType()) && month.equals(appMonth)){
                        ++typeTotal;
                    }
                }
                if(typeTotal > 0){
                    text = text + String.format("\t\t\t%-20s\t%-5s\n",type + ":" ,typeTotal);
                }
            }
        }
        reportTxtArea.setText(text);
        
    }

    @FXML
    private void byCustomerBtnHandler(ActionEvent event) {
        
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime dataDateTime;
        String text = String.format("\t\t\t%-35s\t\t%-35s\t\t%-5s\n","TYPE" ,"DATE" ,"TIME");
        
        Appointment appointment;
        Set<String> customers = new TreeSet<>();
        
        //Create sorted TreeSet of customers
        for(Report report: sortedByName){
            appointment = Scheduling.getAppointment(report.getId());
            customers.add(appointment.getCustomer().getName());
        }
        
        //Create customer report sorted by name and date
        for(String customer: customers){
            text = text + "\n\n" + customer + "\n";
            for(Report report: sortedByName){
                appointment = Scheduling.getAppointment(report.getId());
                
                if(customer.equals(appointment.getCustomer().getName())){
                    dataDateTime = report.getDt();
                    String type = appointment.getType();
                    String date = appointment.getDate();
                    String time = appointment.getTime();
                    
                    //List past appointments as "Expired"
                    if(dataDateTime.compareTo(localDateTime) < 0){
                        text = text + "\t(Expired)";
                    }else{
                        text = text + "\t\t";
                    }
                    
                    text = text + String.format("\t%-35s\t%-35s\t%-5s\n",type ,date ,time);
                }
            }
        }
        reportTxtArea.setText(text);
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
    private void exitBtnHandler(ActionEvent event) {
        
        Platform.exit();
    }
    
}
