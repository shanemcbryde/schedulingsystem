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
public class Country {
    
    private final IntegerProperty id;
    private final StringProperty name;
    
    public Country(
        int id,
        String name){
        
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }
    
    public void setId(int id){
        this.id.set(id);
    }
    
    public void setName(String name){
        this.name.set(name);
    }
    
    public int getId(){
        return id.get();
    }
    
    public String getName(){
        return name.get();
    }
    
    public IntegerProperty idProperty(){
        return id;
    }
    
    public StringProperty nameProperty(){
        return name;
    }
}
