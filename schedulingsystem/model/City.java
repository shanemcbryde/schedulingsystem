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
public class City {
    
    private IntegerProperty id;
    private StringProperty name;
    private Country country;
    
    public City(
        int id,
        String name,
        Country country){
        
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.country = country;
    }
    
    public void setId(int id){
        this.id.set(id);
    }
    
    public void setName(String name){
        this.name.set(name);
    }
    
    public void setCountry(Country country){
        this.country = country;
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getName(){
        return name.get();
    }
    
    public Country getCountry(){
        return country;
    }
    
    public IntegerProperty idProperty(){
        return id;
    }
    
    public StringProperty nameProperty(){
        return name;
    }
    
    public StringProperty countryProperty(){
        return getCountry().nameProperty();
    }
}
