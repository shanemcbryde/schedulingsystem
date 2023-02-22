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
public class Address {
    
    private IntegerProperty id;
    private StringProperty address;
    private StringProperty address2;
    private StringProperty postal;
    private StringProperty phone;
    private City city;
    
    public Address(
        int id,
        String address,
        String address2,
        String postal,
        String phone,
        City city){
        
        this.id = new SimpleIntegerProperty(id);
        this.address = new SimpleStringProperty(address);
        this.address2 = new SimpleStringProperty(address2);
        this.postal = new SimpleStringProperty(postal);
        this.phone = new SimpleStringProperty(phone);
        this.city = city;
    }
    
    public void setId(int id){
        this.id.set(id);
    }
    
    public void setAddress(String address){
        this.address.set(address);
    }
    
    public void setAddress2(String address2){
        this.address2.set(address2);
    }
    
    public void setPostal(String postal){
        this.postal.set(postal);
    }
    
    public void setPhone(String phone){
        this.phone.set(phone);
    }
    
    public void setCity(City city){
        this.city = city;
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getAddress(){
        return address.get();
    }
    
    public String getAddress2(){
        return address2.get();
    }
    
    public String getPostal(){
        return postal.get();
    }
    
    public String getPhone(){
        return phone.get();
    }
    
    public City getCity(){
        return city;
    }
    
    public IntegerProperty idProperty(){
        return id;
    }
    
    public StringProperty addressProperty(){
        return address;
    }
    
    public StringProperty address2Property(){
        return address2;
    }
    
    public StringProperty postalProperty(){
        return postal;
    }
    
    public StringProperty phoneProperty(){
        return phone;
    }
    
    public StringProperty cityProperty(){
        return getCity().nameProperty();
    }
    
}
