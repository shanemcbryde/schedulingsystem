/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import static schedulingsystem.DAO.DBConnection.conn;
import schedulingsystem.model.Address;
import schedulingsystem.model.Appointment;
import schedulingsystem.model.City;
import schedulingsystem.model.Country;
import schedulingsystem.model.Customer;
import schedulingsystem.model.Scheduling;
import schedulingsystem.model.User;

/**
 *
 * @author Shane
 */
public class QueryManager {
    
    private static Statement stmt;
    private static ResultSet result;
    private static int lastInsertedId;
    
    //Determine type of query made to the database
    public static boolean makeQuery(String table, String query){
        
        boolean accessed = false;
        try{
            //Determine query execution, returns a result or not
            boolean isResultSet = stmt.execute(query);
            if(isResultSet){
                result = stmt.getResultSet();
                
                switch(table){
                    case "user":
                        accessed = user();
                        break;
                    case "country":
                        accessed = country();
                        break;
                    case "city":
                        accessed = city();
                        break;
                    case "address":
                        accessed = address();
                        break;
                    case "customer":
                        accessed = customer();
                        break;
                    case "appointment":
                        accessed = appointment();
                        break;
                }
            }else{
                switch(table){
                    case "newAddress":
                        accessed = lastInsertedId();
                        break;
                    case "newCustomer":
                        accessed = lastInsertedId();
                        break;
                    case "newAppointment":
                        accessed = lastInsertedId();
                        break;
                    case "modifyCustomer":
                        accessed = true;
                        break;
                    case "modifyAppointment":
                        accessed = true;
                        break;
                    case "deleteCustomer":
                        accessed = true;
                        break;
                    case "deleteAppointment":
                        accessed = true;
                        break;
                }
            }
        }catch(SQLException ex){
            System.out.println("***Error***: " + ex.getMessage());
        }
        return accessed;
    }
    
    public static void openDB() throws SQLException{
        
        DBConnection.makeConnection();
        stmt = conn.createStatement();
    }
    
    public static void closeDB(){
        
        DBConnection.closeConnection();
    }
    
    public static int getLastInsertedId() throws SQLException{
        return lastInsertedId;
    }
    
    public static boolean lastInsertedId() throws SQLException{
        
        //Retrieves the last inserted ID from the database
        boolean success = false;
        String query = "SELECT LAST_INSERT_ID()";
        stmt.execute(query);
        result = stmt.getResultSet();
        if(result.next()){
            lastInsertedId = result.getInt(1);
            success = true;
        }
        return success;
    }
    
    private static boolean user() throws SQLException{
        
        User user;
        int userID;
        String userName;
        int active = 0;
        
        while(result.next()){
            userID = result.getInt("userId");
            userName = result.getString("userName");
            active = result.getInt("active");
            
            if(active == 1){
                user = new User(
                userID,
                userName);

                Scheduling.addUser(user);
            }
        }
        return (active == 1);
    }

    private static boolean country() throws SQLException{
        
        Country country;
        int countryID;
        String countryName;
        
        while(result.next()){
            countryID = result.getInt("countryId");
            countryName = result.getString("country");
            
            country = new Country(
            countryID,
            countryName);
            
            Scheduling.addCountry(country);
        }
        return true;
    }
    
    private static boolean city() throws SQLException{
        
        City city;
        int cityID;
        int countryID;
        String cityName;
        Country country;
        
        while(result.next()){
            cityID = result.getInt("cityId");
            cityName = result.getString("city");
            countryID = result.getInt("countryId");
            country = Scheduling.getCountry(countryID);
            
            city = new City(
            cityID,
            cityName,
            country);
            
            Scheduling.addCity(city);
        }
        return true;
    }
    
    private static boolean address() throws SQLException{
        
        Address address;
        int addressID;
        int cityID;
        String address1;
        String address2;
        String postal;
        String phone;
        City city;
        
        while(result.next()){
            addressID = result.getInt("addressId");
            address1 = result.getString("address");
            address2 = result.getString("address2");
            postal = result.getString("postalCode");
            phone = result.getString("phone");
            cityID = result.getInt("cityId");
            city = Scheduling.getCity(cityID);
            
            address = new Address(
            addressID,
            address1,
            address2,
            postal,
            phone,
            city);
            
            Scheduling.addAddress(address);
        }
        return true;
    }
    
    private static boolean customer() throws SQLException{
        
        Customer customer;
        int customerID;
        int addressID;
        String name;
        Address address;
        
        while(result.next()){
            customerID = result.getInt("customerId");
            name = result.getString("customerName");
            addressID = result.getInt("addressId");
            address = Scheduling.getAddress(addressID);
            
            customer = new Customer(
            customerID,
            name,
            address);
            
            Scheduling.addCustomer(customer);
        }
        return true;
    }
    
    private static boolean appointment() throws SQLException{
        
        Appointment appointment;
        int appointmentID;
        int customerID;
        String type;
        Customer customer;
        String date;
        String time;
        int duration;
                
        Timestamp timestamp;
        LocalDateTime localDateTime;
        ZonedDateTime zonedDateTimeUTC;
        ZonedDateTime zonedDateTimeLocal;
        Instant instant;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        LocalDate startDate;
        LocalTime startTime;
        
        
        while(result.next()){
            appointmentID = result.getInt("appointmentId");
            customerID = result.getInt("customerId");
            type = result.getString("type");
            customer = Scheduling.getCustomer(customerID);
            
            //Convert database Timestamp to LocalDate and LocalTime
            timestamp = result.getTimestamp("start");
            localDateTime = timestamp.toLocalDateTime();
            zonedDateTimeUTC = localDateTime.atZone(ZoneId.of("UTC"));
            instant = zonedDateTimeUTC.toInstant();
            zonedDateTimeLocal = instant.atZone(ZoneId.systemDefault());
            startDateTime = zonedDateTimeLocal.toLocalDateTime();
            startDate = startDateTime.toLocalDate();
            startTime = startDateTime.toLocalTime();
            timestamp = result.getTimestamp("end");
            localDateTime = timestamp.toLocalDateTime();
            zonedDateTimeUTC = localDateTime.atZone(ZoneId.of("UTC"));
            instant = zonedDateTimeUTC.toInstant();
            zonedDateTimeLocal = instant.atZone(ZoneId.systemDefault());
            endDateTime = zonedDateTimeLocal.toLocalDateTime();
            
            date = startDate.toString();
            time = startTime.toString();
            duration = (int)(ChronoUnit.MINUTES.between(startDateTime, endDateTime));
            
            appointment = new Appointment(
                appointmentID,
                type,
                date,
                time,
                duration,
                customer);
          
            Scheduling.addAppointment(appointment);
        }
        return true;
    }   
    
}
