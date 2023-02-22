/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.view_controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import schedulingsystem.DAO.QueryManager;
import schedulingsystem.model.Appointment;
import schedulingsystem.model.Customer;
import schedulingsystem.model.Scheduling;
import schedulingsystem.model.User;

/**
 * FXML Controller class
 *
 * @author Shane
 */
public class AppointmentsController implements Initializable {

    @FXML
    private Button monthlyBtn;
    @FXML
    private Button weeklyBtn;
    @FXML
    private Button customersBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private TextField typeTxt;
    @FXML
    private DatePicker datePkr;
    @FXML
    private Spinner<Integer> timeSpn;
    @FXML
    private ChoiceBox<String> timeChB;
    @FXML
    private Spinner<Integer> durationSpn;
    @FXML
    private ChoiceBox<Integer> durationChB;
    @FXML
    private ChoiceBox<Customer> customerChB;
    @FXML
    private Button addBtn;
    @FXML
    private DatePicker dateModifyPkr;
    @FXML
    private Spinner<Integer> timeModifySpn;
    @FXML
    private ChoiceBox<String> timeModifyChB;
    @FXML
    private Spinner<Integer> durationModifySpn;
    @FXML
    private ChoiceBox<Integer> durationModifyChB;
    @FXML
    private Label customerModifyLbl;
    @FXML
    private Label typeModifyLbl;
    @FXML
    private Button modifyBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private TableView<Appointment> appointmentTbl;
    @FXML
    private TableColumn<Appointment, String> customerCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, String> dateCol;
    @FXML
    private TableColumn<Appointment, String> startCol;
    @FXML
    private TableColumn<Appointment, Integer> endCol;
    @FXML
    private Button reportsBtn;
    
    private int customerId;
    private final User user = schedulingsystem.model.Scheduling.getUser();
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        LocalDate dateAppointment;
        
        //Retrieving only active appointments
        for (Appointment appointment : Scheduling.getAppointments()){
            dateAppointment = LocalDate.parse(appointment.getDate());
            if(dateAppointment.compareTo(LocalDate.now()) >= 0){
                allAppointments.add(appointment);
            }
        }
        //Populating table with appointments
        //Using lambda to pass methods as arguments
        customerCol.setCellValueFactory(cellData -> cellData.getValue().customerProperty());
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        startCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        endCol.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asObject());
        
        appointmentTbl.setItems(allAppointments);
        
        //Order table by customer name
        TableColumn sortColumn = appointmentTbl.getColumns().get(0);
        appointmentTbl.getSortOrder().setAll(sortColumn);
        
        //Initialize appointment controls
        initChoiceBox();
        initDatePicker();
        initSpinner();
        initTable();
    }
    
    private void initTable(){
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        //Add change listener
        appointmentTbl.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Appointment> observableValue, Appointment oldValue, Appointment newValue) -> {
            String time;
            String hour;
            String minutes;
            int hr;
            int min;
            int duration;
            
            //Check whether item is selected and set appointment controls to value of selected item 
            if (appointmentTbl.getSelectionModel().getSelectedItem() != null) {
                customerModifyLbl.setText(newValue.getCustomer().getName());
                typeModifyLbl.setText(newValue.getType());
                LocalDate localDate = LocalDate.parse(newValue.getDate(), formatter);
                dateModifyPkr.setValue(localDate);
                time = newValue.getTime();
                hour = time.substring(0, 2);
                hr = Integer.valueOf(hour);
                timeModifySpn.getValueFactory().setValue(hr);
                minutes = time.substring(3);
                timeModifyChB.setValue(minutes);
                duration = newValue.getDuration();
                hr = duration/60;
                min = duration%60;
                durationModifySpn.getValueFactory().setValue(hr);
                durationModifyChB.setValue(min);
            }
        });
        
    }
    
    private void initChoiceBox() {
        
        //Populate customer choice box
        customerChB.setItems(Scheduling.getCustomers());

        customerChB.setConverter(new StringConverter<Customer>(){
            @Override
            public String toString(Customer object) {
                return object.getName();
            }

            @Override
            public Customer fromString(String string) {
                return customerChB.getItems().stream().filter(customer ->
                customer.getName().equals(string)).findFirst().orElse(null);
            }
        });
        
        customerChB.valueProperty().addListener((obs, oldval, newval)-> {
            if(newval != null){
                customerId = newval.getId();
            }
        });
        
        //Populate time and duration choiceboxes
        timeChB.getItems().add("00");
        timeChB.getItems().add("15");
        timeChB.getItems().add("30");
        timeChB.getItems().add("45");
        timeChB.setValue("00");
        
        timeModifyChB.getItems().add("00");
        timeModifyChB.getItems().add("15");
        timeModifyChB.getItems().add("30");
        timeModifyChB.getItems().add("45");
        
        durationChB.getItems().add(0);
        durationChB.getItems().add(15);
        durationChB.getItems().add(30);
        durationChB.getItems().add(45);
        durationChB.setValue(0);
        
        durationModifyChB.getItems().add(0);
        durationModifyChB.getItems().add(15);
        durationModifyChB.getItems().add(30);
        durationModifyChB.getItems().add(45);
    }
    
    private void initDatePicker() {
        
        //Disable dates prior to today's date
        datePkr.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
        
        dateModifyPkr.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
    }
    
    private void initSpinner() {
        
        //Populate time and duration spinners, restricting time to business hours
        timeSpn.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 16)
        );
        
        timeModifySpn.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 16)
        );
        
        durationSpn.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8)
        );
        
        durationModifySpn.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8)
        );
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
        
        Customer customer;
        String type;
        int hour;
        int minute;
        LocalDate localDate;
        LocalTime localTime;
        LocalDateTime localDateTime;
        Timestamp startTimestamp;
        Timestamp endTimestamp;
        ZonedDateTime zonedDateTimeUTC;
        ZonedDateTime zonedDateTimeLocal;
        Instant instant;
        LocalDateTime startDateTime;
        int hrs;
        int mins;
        int duration;
        String table;
        String query;
        boolean success;
        Appointment newAppointment;
        LocalDate dateAppointment;
        boolean overlap = false;
        LocalTime startAppointment;
        LocalTime endAppointment;
        
        String alertText = "";
        boolean alertMessage = false;
        if(customerChB.getSelectionModel().isEmpty()){
            alertText = "Please select a customer!";
            alertMessage = true;
        }else if(typeTxt.getText().trim().isEmpty()){
            alertText = "Please provide an appointment type!";
            alertMessage = true;
        }else if(datePkr.getValue() == null){
            alertText = "Please select an appointment date!";
            alertMessage = true;
        }else if(timeSpn.getValue() == null){
            alertText = "Please select an appointment time!";
            alertMessage = true;
        }else if(durationSpn.getValue() == null){
            alertText = "Please select an appointment duration!";
            alertMessage = true;
        }
        
        if(alertMessage){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Fields");
            alert.setHeaderText("Empty Fields");
            alert.setContentText(alertText);
            alert.showAndWait();
        }else{
            customer = customerChB.getSelectionModel().getSelectedItem();
            type = typeTxt.getText();
            hour = timeSpn.getValue();
            minute = Integer.parseInt(timeChB.getValue());
            hrs = durationSpn.getValue();
            mins = durationChB.getValue();
            duration = (60*hrs)+mins;
            localDate = datePkr.getValue();
            localTime = LocalTime.of(hour, minute);
            
            //Convert local date/time to UTC Timestamp required by database
            localDateTime = LocalDateTime.of(localDate, localTime);
            zonedDateTimeLocal = localDateTime.atZone(ZoneId.systemDefault());
            instant = zonedDateTimeLocal.toInstant();
            zonedDateTimeUTC = instant.atZone(ZoneId.of("UTC"));
            startDateTime = zonedDateTimeUTC.toLocalDateTime();
            startTimestamp = Timestamp.valueOf(startDateTime);
            endTimestamp = Timestamp.valueOf(startDateTime.plusMinutes(duration));
            
            //Determine if selected appointment time overlaps an existing appointment time
            for (Appointment appointment : allAppointments){
                dateAppointment = LocalDate.parse(appointment.getDate());
                if(dateAppointment.compareTo(localDate) == 0){
                    startAppointment = LocalTime.parse(appointment.getTime());
                    endAppointment = startAppointment.plusMinutes(appointment.getDuration());
                    if(localTime.compareTo(startAppointment) >= 0 & localTime.compareTo(endAppointment) < 0){
                        overlap = true;
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setHeaderText("Overlapping Appointments!");
                        alert2.setContentText("An appointment already exists during this time period! Please choose another appointment time.");
                        alert2.showAndWait();
                        break;
                    }
                }
            }
            
            if(!overlap){
                customerChB.getSelectionModel().clearSelection();
                typeTxt.setText("");
                datePkr.setValue(null);
                timeChB.setValue("00");
                durationChB.setValue(0);
                timeSpn.getValueFactory().setValue(8);
                durationSpn.getValueFactory().setValue(1);
                
                //Add appointment to database
                QueryManager.openDB();
                table = "newAppointment";
                query = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES(" + customerId + ", " + user.getId() + ", \"not needed\", \"not needed\", \"not needed\", \"not needed\", \"" + type + "\", \"not needed\", '" + startTimestamp + "', '" + endTimestamp + "', NOW(), \"" + user.getName() + "\", NOW(), \"" + user.getName() + "\")";
                success = QueryManager.makeQuery(table, query);
                QueryManager.closeDB();

                if(success){
                    newAppointment = new Appointment(
                    QueryManager.getLastInsertedId(),
                    type,
                    localDate.toString(),
                    localTime.toString(),
                    duration,
                    customer);

                    //Add appointment to appointments and active appointments lists
                    Scheduling.addAppointment(newAppointment);
                    allAppointments.add(newAppointment);
                }
            }
        }
    }

    @FXML
    private void modifyBtnHandler(ActionEvent event) throws SQLException {
        
        int hour;
        int minute;
        LocalDate localDate;
        LocalTime localTime;
        LocalDateTime localDateTime;
        ZonedDateTime zonedDateTimeUTC;
        ZonedDateTime zonedDateTimeLocal;
        Instant instant;
        LocalDateTime startDateTime;
        Timestamp startTimestamp;
        Timestamp endTimestamp;
        int hrs;
        int mins;
        int duration;
        String table;
        String query;
        boolean success;
        int appointmentId;
        Appointment selectedAppointment = appointmentTbl.getSelectionModel().getSelectedItem();
        boolean overlap = false;
        LocalDate dateAppointment;
        LocalTime startAppointment;
        LocalTime endAppointment;
        
        if(selectedAppointment != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modify Appointment");
            alert.setHeaderText("This Action Will Modify The Appointment Record!");
            alert.setContentText("Do you want to make these changes to the appointment?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                appointmentId = selectedAppointment.getId();
                hour = timeModifySpn.getValue();
                minute = Integer.parseInt(timeModifyChB.getValue());
                hrs = durationModifySpn.getValue();
                mins = durationModifyChB.getValue();
                duration = (60*hrs)+mins;
                localDate = dateModifyPkr.getValue();
                localTime = LocalTime.of(hour, minute);
                
                //Convert local date/time to UTC Timestamp required by database
                localDateTime = LocalDateTime.of(localDate, localTime);
                zonedDateTimeLocal = localDateTime.atZone(ZoneId.systemDefault());
                instant = zonedDateTimeLocal.toInstant();
                zonedDateTimeUTC = instant.atZone(ZoneId.of("UTC"));
                startDateTime = zonedDateTimeUTC.toLocalDateTime();
                startTimestamp = Timestamp.valueOf(startDateTime);
                endTimestamp = Timestamp.valueOf(startDateTime.plusMinutes(duration));
                
                //Determine if selected appointment time overlaps an existing appointment time
                for (Appointment appointment : Scheduling.getAppointments()){
                    if(!(appointment.getId() == appointmentId)){
                        dateAppointment = LocalDate.parse(appointment.getDate());
                        if(dateAppointment.compareTo(localDate) == 0){
                            startAppointment = LocalTime.parse(appointment.getTime());
                            endAppointment = startAppointment.plusMinutes(appointment.getDuration());
                            if(localTime.compareTo(startAppointment) >= 0 & localTime.compareTo(endAppointment) < 0){
                                overlap = true;
                                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                alert2.setHeaderText("Overlapping Appointments!");
                                alert2.setContentText("An appointment already exists during this time period! Please choose another appointment time.");
                                alert2.showAndWait();
                                break;
                            }
                        }
                    }
                }
                
                if(!overlap){
                    customerModifyLbl.setText("");
                    typeModifyLbl.setText("");
                    dateModifyPkr.setValue(null);
                    timeModifyChB.setValue("00");
                    durationModifyChB.setValue(0);
                    timeModifySpn.getValueFactory().setValue(8);
                    durationModifySpn.getValueFactory().setValue(1);
                    appointmentTbl.getSelectionModel().clearSelection();
                    
                    //Modify appointment in database
                    QueryManager.openDB();
                    table = "modifyAppointment";
                    query = "UPDATE appointment SET start = '" + startTimestamp + "', end = '" + endTimestamp + "', lastUpdate = NOW(), lastUpdateBy = \"" + user.getName() + "\" WHERE appointmentId = " + appointmentId;
                    success = QueryManager.makeQuery(table, query);
                    QueryManager.closeDB();

                    //Modify appointment lists
                    if(success){
                        Appointment appointmentObj = Scheduling.getAppointment(appointmentId);
                        appointmentObj.setDate(localDate.toString());
                        appointmentObj.setTime(localTime.toString());
                        appointmentObj.setDuration(duration);
                        
                        for(Appointment appointment: allAppointments){
                            if(appointment.getId() == appointmentId){
                                appointment.setDate(localDate.toString());
                                appointment.setTime(localTime.toString());
                                appointment.setDuration(duration);
                            }
                        }
                    }
                }
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Modify");
            alert.setHeaderText("No Appointment Selected!");
            alert.setContentText("Please select an appointment and try again.");
            alert.showAndWait();
        }
        
    }
    
    @FXML
    private void deleteBtnHandler(ActionEvent event) throws SQLException {
        
        int appointmentId;
        String table;
        String query;
        boolean success;
        Appointment selectedAppointment = appointmentTbl.getSelectionModel().getSelectedItem();
        
        //Determine if an appointment has been selected and give alert if no selection
        if(selectedAppointment != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("This Action Will Delete The Appointment Record!");
            alert.setContentText("Do you want to delete this appointment?");

            //Give alert that appointment will be deleted, carry out deletion if OK
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                appointmentId = selectedAppointment.getId();
                
                QueryManager.openDB();
                table = "deleteAppointment";
                query = "DELETE FROM appointment WHERE appointmentId = " + appointmentId;
                success = QueryManager.makeQuery(table, query);
                QueryManager.closeDB();
                
                if(success){
                    Scheduling.deleteAppointment(selectedAppointment);
                    allAppointments.remove(selectedAppointment);
                }
                
                appointmentTbl.getSelectionModel().clearSelection();
                customerModifyLbl.setText("");
                typeModifyLbl.setText("");
                dateModifyPkr.setValue(null);
                timeModifyChB.getSelectionModel().clearSelection();
                durationModifyChB.getSelectionModel().clearSelection();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Delete");
            alert.setHeaderText("No Appointment Selected!");
            alert.setContentText("Please select an appointment and try again.");
            alert.showAndWait();
        }
    }

}
