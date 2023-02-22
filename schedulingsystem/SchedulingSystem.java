/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem;

import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Shane
 */
public class SchedulingSystem extends Application {
    
    //Alternative Norwegian language login screen
    public static String localLanguage;
    public static String altLanguage = "no";
    public static Locale myLocale = Locale.getDefault();
    //Locale myLocale = new Locale("no", "NO");
    
    @Override
    public void start(Stage stage) throws Exception {
        
        //Open Login window
        localLanguage = myLocale.getLanguage();
        Parent root = FXMLLoader.load(getClass().getResource("view_controller/LogIn.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        if(localLanguage.equals(altLanguage)){
            stage.setTitle("Styringssystem For Brukerplaner");
        }else{
            stage.setTitle("User Schedule Management System");
        }
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        launch(args);
    }
    
}
