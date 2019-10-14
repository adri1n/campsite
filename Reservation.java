package hello;

import java.time.LocalDate;
import java.text.SimpleDateFormat;  

/**
 * 
 * @author Adrien
 *
 * Represents a reservation
 */
public class Reservation {

    private String email;
    private String name;
    private LocalDate arrival;
    private LocalDate departure;

    public Reservation(String email,String name, LocalDate arr, LocalDate dep)  {
        this.email = email;
        this.name = name;
        this.arrival = arr;
        this.departure = dep;
       
        
    }

    public String toString() {
    	return "|| "+name+" "+" "+arrival.toString()+" "+departure.toString()+" ||";
    }
    
    public void setEmail(String newm) {
    	this.email=newm;
    }
    
    public void setName(String newn) {
    	this.name=newn;
    }
    
    public void setArrival(LocalDate newa) {
    	this.arrival=newa;
    }
    
    public void setDeparture(LocalDate newd) {
    	this.departure=newd;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public LocalDate getArrival() {
    	return this.arrival;
    }
    
    public LocalDate getDeparture() {
    	return this.departure;
    }
    
}