/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsystem.model;

import java.time.LocalDateTime;

/**
 *
 * @author Shane
 */
public class Report {
    
    private int id;
    private String name;
    private LocalDateTime dt;
    
    public Report(
        int id,
        String name,
        LocalDateTime dt){
        
        setId(id);
        setName(name);
        setDt(dt);
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setDt(LocalDateTime dt){
        this.dt = dt;
    }
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public LocalDateTime getDt(){
        return dt;
    }
    
}
