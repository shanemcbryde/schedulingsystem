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
public class Customer {
    
    private IntegerProperty id;
    private StringProperty name;
    private Address address; 
    
    public Customer(
        int id,
        String name,
        Address address){
        
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.address = address;
    }
    
    public void setId(int id){
        this.id.set(id);
    }
    
    public void setName(String name){
        this.name.set(name);
    }
    
    public void setAddress(Address address){
        this.address = address;
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getName(){
        return name.get();
    }
    
    public Address getAddress(){
        return address;
    }
    
    public IntegerProperty idProperty(){
        return id;
    }
    
    public StringProperty nameProperty(){
        return name;
    }
    
    public StringProperty addressProperty(){
        return getAddress().addressProperty();
    }
    
    public StringProperty addressProperty2(){
        return getAddress().address2Property();
    }
    
    public StringProperty postalProperty(){
        return getAddress().postalProperty();
    }
    
    public StringProperty phoneProperty(){
        return getAddress().phoneProperty();
    }
    
    public StringProperty cityProperty(){
        return getAddress().getCity().nameProperty();
    }
    
}
