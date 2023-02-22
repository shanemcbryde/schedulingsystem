/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.model;

/**
 *
 * @author Shane
 */
public class User {
    private int id;
    private String name;
    
    
    public User(
        int id,
        String name){
        
        setId(id);
        setName(name);
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
}
