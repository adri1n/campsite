package hello;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.scheduling.annotation.Async;
import java.util.concurrent.CompletableFuture;

/**
 * 
 * @author Adrien
 *
 * Handles all requests.
 */
@RestController
public class ReservationController {
	
	private ReservationModel rm = new ReservationModel();

	private static final String NOTFOUND = "No Reservation has been found using this UID.";
	private static final String MODIFIED = "This reservation has been modified and is now: \n";
	private static final String DELETED = "This reservation has been removed.";
	private static final String UNVDATES = "The dates you selected are not valid.";
	private static final String FULL = "The campsite is full.";
	private static final String ERROR = "The parameters you have entered are incorrects.";
	

    @RequestMapping("/add/{email}/{name}/{arr}/{dep}")
    @Async
    public CompletableFuture<String> newReservation(@PathVariable("email")String email,@PathVariable("name")String name,@PathVariable("arr")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate arr,@PathVariable("dep")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dep) {
       if(datesAreOk(arr,dep)) {
	       Reservation re =  new Reservation(email,name,arr,dep);
	       boolean isAdded = rm.add(re);
	       if(isAdded)
	    	   return CompletableFuture.completedFuture(re.toString());
	       else
	    	   return CompletableFuture.completedFuture(FULL);
       }
       else {
    	   return CompletableFuture.completedFuture(UNVDATES);
       }
       
    }
    
    @RequestMapping("/getavailability/{from}/{to}")
    @Async
    public CompletableFuture<String> getAvailability(@PathVariable("from")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate from,@PathVariable("to")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate to) {
    	String infosToReturn = "";
    	for (LocalDate date = from; date.isBefore(to); date = date.plusDays(1))
    	{
    		Map<Integer, Reservation> dates = rm.findDates(date,date.plusDays(1));
    		infosToReturn += rm.fixedSize-dates.size()+" places are available on "+date.toString()+"\\r\\n";
    	}
    	
		return CompletableFuture.completedFuture(infosToReturn);
       
    }
    
    @RequestMapping("/find/{from}/{to}")
    @Async
    public CompletableFuture<String> findDates(@PathVariable("from")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate from,@PathVariable("to")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate to) {
    
		Map<Integer, Reservation> dates = rm.findDates(from,to);
		return CompletableFuture.completedFuture(dates.toString());
       
    }
    
    /**
     * 
     * @param arr
     * @param dep
     * @return if dates contrainsts are satisfied.
     */
    private boolean datesAreOk(LocalDate arr, LocalDate dep) {
    	Period betweenAandD = Period.between(arr,dep);
    	if (betweenAandD.getDays()>3) { 
    		System.out.print("above 3 days");
    		return false;
    	}
    	LocalDate now = LocalDate.now();
    	Period betweenAandNow = Period.between(now, arr);
    	if(betweenAandNow.getDays()<1 || betweenAandNow.getMonths()>1) {
    		System.out.print(betweenAandNow.getDays()+" "+betweenAandNow.getMonths());
    		return false;
    	}
    	return true;
    }
    
    
    @RequestMapping("/remove/{uid}")
    @Async
    public CompletableFuture<String> removeReservation(@PathVariable("uid")int uid) {
       rm.remove(uid);
       return CompletableFuture.completedFuture(DELETED);
    }
    
    @RequestMapping("/all")
    @Async
    public CompletableFuture<String> r() {
       return CompletableFuture.completedFuture(rm.toString());
       
    }
    
    @RequestMapping("/get")
    @Async
    public CompletableFuture<String> getReservation(@RequestParam(value="uid")int uid) {
    	Reservation r = rm.get(uid);
    	if(r!=null)
    		return CompletableFuture.completedFuture(r.toString());
    	else return CompletableFuture.completedFuture(NOTFOUND);
       
    }
    
    @RequestMapping("/setemail")
    @Async
    public CompletableFuture<String> setEmail(@RequestParam(value="uid")int uid,@RequestParam(value="new")String newm) {
    	Reservation r = rm.get(uid);
    	if(r!=null) {
    		r.setEmail(newm);
    		return CompletableFuture.completedFuture(MODIFIED+r.toString());
    	}
    	else return CompletableFuture.completedFuture(NOTFOUND);
       
    }
    
    
    @RequestMapping("/setdeparture/{uid}/{date}")
    @Async
    public CompletableFuture<String> setDeparture(@PathVariable("uid")int uid,@PathVariable("date")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate newm) {
    	Reservation r = rm.get(uid);
		if(r!=null) {
    		r.setDeparture(newm);
    		return CompletableFuture.completedFuture(MODIFIED+r.toString());
    	}
    	else return CompletableFuture.completedFuture(NOTFOUND); 
    }
    
    @RequestMapping("/setarrival/{uid}/{date}")
    @Async
    public CompletableFuture<String> setArrival(@PathVariable("uid")int uid,@PathVariable("date")@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate newm) {
    	Reservation r = rm.get(uid);
		if(r!=null) {
    		r.setArrival(newm);
    		return CompletableFuture.completedFuture(MODIFIED+r.toString());
    	}
    	else return CompletableFuture.completedFuture(NOTFOUND); 
    }
    
    @RequestMapping("/setname/{uid}/{name}")
    @Async
    public CompletableFuture<String> getName(@PathVariable("uid")int uid,@PathVariable("name")String newm) {
    	Reservation r = rm.get(uid);
		if(r!=null) {
    		r.setName(newm);
    		return CompletableFuture.completedFuture(MODIFIED+r.toString());
    	}
    	else return CompletableFuture.completedFuture(NOTFOUND); 
    }
    
    @RequestMapping("/setname/{uid}/{email}")
    @Async
    public CompletableFuture<String> setName(@PathVariable("uid")int uid,@PathVariable("email")String newm) {
    	Reservation r = rm.get(uid);
		if(r!=null) {
    		r.setEmail(newm);
    		return CompletableFuture.completedFuture(MODIFIED+r.toString());
    	}
    	else return CompletableFuture.completedFuture(NOTFOUND); 
    }
    
    @RequestMapping("/findname")
    @Async
    public CompletableFuture<String> findName(@RequestParam(value="name")String name) {
    	Object r = rm.findName(name).toString();
    	if (r!=null)
    	return CompletableFuture.completedFuture(r.toString());
    	else return CompletableFuture.completedFuture(NOTFOUND); 
    }
    
    @RequestMapping
    @Async
    public CompletableFuture<String> error() {
		return CompletableFuture.completedFuture(ERROR);
	}
    
    
    
}