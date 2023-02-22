/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.DAO;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Shane
 */
public class DBConnection{
    
    private static final String DB_NAME = "U04VCX";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/" + DB_NAME;
    private static final String USERNAME = "U04VCX";
    private static final String PASSWORD = "53688353720";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    //Create Connection object
    public static Connection conn;
    
    public static void makeConnection(){
        
        //Open connection to database
        try{
            Class.forName(DRIVER);
            conn = (Connection) DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        }catch(ClassNotFoundException | SQLException e){
            System.out.println(e.toString());
        }
    }
    
    public static void closeConnection(){
        
        //Close connection to database
        try {
            conn.close();
        }catch(SQLException e){
            System.out.println(e.toString());
        }
    }
}
