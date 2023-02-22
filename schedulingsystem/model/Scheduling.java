/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.model;

import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Shane
 */
public class Scheduling {
    public static boolean populated = false;
    public static int monthsNextPrevious = 0;
    public static int weeksNextPrevious = 0;
    private static User currentUser;
    
    private static ObservableList<Country> countrys = FXCollections.observableArrayList();
    private static ObservableList<City> citys = FXCollections.observableArrayList();
    private static ObservableList<Address> addresses = FXCollections.observableArrayList();
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    
    //currentUser controls
    public static void addUser(User user){
        currentUser = user;
    }
    
    public static User getUser(){
        return currentUser;
    }
    
    //countrys list controls
    public static void addCountry(Country country){
        countrys.add(country);
    }
    
    public static Country getCountry(int id){
        for(Country country: getCountrys()){
            if(country.getId() == id){
                return country;
            }
        }
        return null;
    }

    public static ObservableList<Country> getCountrys(){
        return countrys;
    }
    
    //citys list controls
    public static void addCity(City city){
        citys.add(city);
    }
    
    public static City getCity(int id){
        for(City city: getCitys()){
            if(city.getId() == id){
                return city;
            }
        }
        return null;
    }
    
    public static ObservableList<City> getCitys(){
        return citys;
    }
    
    //addresses list controls
    public static void addAddress(Address address){
        addresses.add(address);
    }
    
    public static Address getAddress(int id){
        for(Address address: getAddresses()){
            if(address.getId() == id){
                return address;
            }
        }
        return null;
    }

    public static boolean deleteAddress(Address address){
        if(address != null){
            addresses.remove(address);
            return true;
        }else{
            return false;
        }
    }
    
    public static ObservableList<Address> getAddresses(){
        return addresses;
    }

    //customers list controls
    public static void addCustomer(Customer customer){
        customers.add(customer);
    }
    
    public static Customer getCustomer(int id){
        for(Customer customer: getCustomers()){
            if(customer.getId() == id){
                return customer;
            }
        }
        return null;
    }
    
    public static boolean deleteCustomer(Customer customer){
        if(customer != null){
            customers.remove(customer);
            return true;
        }else{
            return false;
        }
    }
    
    public static ObservableList<Customer> getCustomers(){
        return customers;
    }

    //appointments list controls
    public static void addAppointment(Appointment appointment){
        appointments.add(appointment);
    }

    public static Appointment getAppointment(int id){
        for(Appointment appointment: getAppointments()){
            if(appointment.getId() == id){
                return appointment;
            }
        }
        return null;
    }
    
    public static boolean deleteAppointment(Appointment appointment){
        if(appointment != null){
            appointments.remove(appointment);
            return true;
        }else{
            return false;
        }
    }
    
    public static ObservableList<Appointment> getAppointments(){
        return appointments;
    }
    
}
