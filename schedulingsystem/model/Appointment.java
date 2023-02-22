/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Shane
 */
public class Appointment {
    
    private IntegerProperty id;
    private StringProperty type;
    private StringProperty date;
    private StringProperty time;
    private IntegerProperty duration;
    private Customer customer;
        
    public Appointment(
        int id,
        String type,
        String date,
        String time,
        int duration,
        Customer customer){
        
        this.id = new SimpleIntegerProperty(id);
        this.type = new SimpleStringProperty(type);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.duration = new SimpleIntegerProperty( duration);
        this.customer = customer;
    }
    
    public void setId(int id){
        this.id.set(id);
    }
    
    public void setType(String type){
        this.type.set(type);
    }
    
    public void setDate(String date){
        this.date.set(date);
    }
    
    public void setTime(String time){
        this.time.set(time);
    }
    
    public void setDuration(int duration){
        this.duration.set(duration);
    }
    
    public void setCustomer(Customer customer){
        this.customer = customer;
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getType(){
        return type.get();
    }
    
    public String getDate(){
        return date.get();
    }
    
    public String getTime(){
        return time.get();
    }
    
    public int getDuration(){
        return duration.get();
    }
    
    public Customer getCustomer(){
        return customer;
    }
    
    public IntegerProperty idProperty(){
        return id;
    }
    
    public StringProperty typeProperty(){
        return type;
    }
    
    public StringProperty dateProperty(){
        return date;
    }
    
    public StringProperty timeProperty(){
        return time;
    }
    
    public IntegerProperty durationProperty(){
        return duration;
    }
    
    public StringProperty customerProperty(){
        return getCustomer().nameProperty();
    }
    
    public StringProperty addressProperty(){
        return getCustomer().addressProperty();
    }
    
    public StringProperty cityProperty(){
        return getCustomer().cityProperty();
    }
    
    public StringProperty postalProperty(){
        return getCustomer().postalProperty();
    }
    
    public StringProperty phoneProperty(){
        return getCustomer().phoneProperty();
    }
    
    public StringProperty countryProperty(){
        return getCustomer().getAddress().getCity().countryProperty();
    }
    
}
