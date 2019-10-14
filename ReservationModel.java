package hello;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.LocalDate;

/**
 * 
 * @author Adrien
 *
 * Storage and accessor functions
 */
public class ReservationModel {
	
	private int index=0;
	public int fixedSize = 50;
	
	private Hashtable<Integer, Reservation> reservations;
	
	public ReservationModel() {
		this.reservations = new Hashtable<Integer, Reservation> ();
	}
	
	

	// thread safe
	public synchronized boolean add(Reservation re) {
		if(this.reservations.size()<fixedSize) {
			this.reservations.put(index,re);
			index+=1;
			return true;
		}
		return false;
	}
	
	public synchronized void remove(int uid) {
		this.reservations.remove(uid);
	}
	
	public Hashtable<Integer, Reservation> getAll(){
		return reservations;
	}
	
	public Reservation get(Integer uid) {
		return reservations.get(uid);
	}
	
	public String toString() {
		return reservations.toString();
	}
	
	public Map<Integer, Reservation> findName(String name) {
		Map<Integer, Reservation> results = reservations.entrySet() 
		          .stream() 
		          .filter(map -> map.getValue().getName().equals(name)) 
		          .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));  
		return results;
	}
	
	public Map<Integer, Reservation> findDates(LocalDate from, LocalDate to) {
		Map<Integer, Reservation> results = reservations.entrySet() 
		          .stream() 
		          .filter(map -> (map.getValue().getArrival().isBefore(from) || map.getValue().getArrival().equals(from))  && (map.getValue().getDeparture().isAfter(to) || map.getValue().getDeparture().equals(to))) 
		          .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));  
		return results;
	}
}
