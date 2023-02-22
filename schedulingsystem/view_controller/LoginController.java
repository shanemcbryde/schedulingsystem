/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.view_controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import schedulingsystem.DAO.QueryManager;
import schedulingsystem.model.Scheduling;

/**
 * FXML Controller class
 *
 * @author Shane
 */
public class LoginController implements Initializable {

    @FXML
    private Label usernameLbl;
    @FXML
    private Label passwordLbl;
    @FXML
    private TextField usernameTxt;
    @FXML
    private TextField passwordTxt;
    @FXML
    private Button loginBtn;
    @FXML
    private Button cancelBtn;
    
    private static int userID;
    private static String table;
    private static String query;
    private static String inputUsername;
    private static String inputPassword;
    private static String localLanguage;
    private static final String ALT_LANGUAGE = "no";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        //Alternative Norwegian language login screen
        Locale myLocale = Locale.getDefault();
        //Locale myLocale = new Locale("no", "NO");
        localLanguage = myLocale.getLanguage();
        
        if(localLanguage.equals(ALT_LANGUAGE)){
            usernameLbl.setText("Brukernavn:");
            passwordLbl.setText("Passord:");
            String prompt = "bruk \"test\" for å logge inn";
            usernameTxt.setPromptText(prompt);
            passwordTxt.setPromptText(prompt);
            loginBtn.setText("Logg Inn");
            cancelBtn.setText("Avbryt");
        }
    }    
    
    @FXML
    private void loginBtnHandler(ActionEvent event) throws IOException, SQLException {
        
        boolean valid;
        inputUsername = usernameTxt.getText();
        inputPassword = passwordTxt.getText();
        
        QueryManager.openDB();
        valid = loginQuery();
        QueryManager.closeDB();
        
        writeActivityLog(valid);
        if(!valid){
            showAlert();
        }else{
            Stage stage;
            Parent root;

            stage = (Stage) loginBtn.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("Monthly.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    } 

    private boolean loginQuery() throws SQLException{
        
        table = "user";
        query = "SELECT userId, userName, active FROM user WHERE userName='" + inputUsername + "' AND password='" + inputPassword + "'";
        
        return QueryManager.makeQuery(table, query);
    }

    private void writeActivityLog(boolean valid){
        
        String logItem;
        String filename = "src/files/activity_log.txt";
        
        //Calendar object for Date and Time
        Calendar logCalendar = Calendar.getInstance();
        
        //Maintain log file
        try{
        FileWriter fwriter = new FileWriter(filename, true);
            try(PrintWriter outputFile = new PrintWriter(fwriter)) {
                if(valid){
                    userID = Scheduling.getUser().getId();
                    logItem = "Successful login attempt (UserID: " + userID + ") - " + logCalendar.getTime();
                }else{
                    logItem = "**Unsuccessful attempt (" + inputUsername + " : " + inputPassword + ") - " + logCalendar.getTime();
                }
                outputFile.println(logItem);
            }
        }catch(IOException ex){
            System.out.println("***ERROR***: " + ex.getMessage());
        }
    }
    
    private void showAlert(){
        Alert alert = new Alert(AlertType.ERROR);
        if(localLanguage.equals(ALT_LANGUAGE)){
            alert.setHeaderText("Ikke funnet!");
            alert.setContentText("Kontroller innspillene dine og prøv igjen.");
        }else{
            alert.setHeaderText("Not found!");
            alert.setContentText("Please check your input and try again.");
        }
        alert.showAndWait();
        usernameTxt.setText("");
        passwordTxt.setText("");
    }
     
    @FXML
    private void cancelBtnHandler(ActionEvent event) {
        
        Platform.exit();
    }

}
