
/**
 * Classes for time and date values
 * 
 */


class Time {
    private static final int HOUR_START = 1;
    private static final int HOUR_END = 12;
    
    private static final int MIN_START = 0;
    private static final int MIN_END = 59;
    
    private static final String AM_STR = "AM";
    private static final String PM_STR = "PM";
    
    private int hour, minute;
    private String ampm;
   
    
    public Time() {
	
    }
    
    private void calculateDay() {

    }
    
    private boolean isValidTime(int hour, int minute, String ampm) {
	if (hour != (Integer) null && hour < HOUR_START && hour > HOUR_END) {
	    return false;
	}
	
	if (minute != (Integer) null && minute < MIN_START && minute > MIN_END) {
	    return false;
	}
	
	if (ampm != (String) null && (!ampm.equalsIgnoreCase(AM_STR) || !ampm.equalsIgnoreCase(PM_STR))) {
	    return false;
	}
	
	return true;
    }
    
    //Mutators:
    
    public CommandFeedback setHour(int hour) {
	
	if(isValidTime(hour, (Integer) null, (String) null)) {
	    hour = this.hour;
	    return CommandFeedback.SUCCESSFUL_OPERATION;
	}
	
	return CommandFeedback.INVALID_TIME_HR;			
    }
    
    public CommandFeedback setMinute(int minute) {
	
	if (isValidTime((Integer) null, minute, (String) null)) {
	    minute = this.minute;
	    return CommandFeedback.SUCCESSFUL_OPERATION;
	}
	
	return CommandFeedback.INVALID_TIME_MIN;
    }
    
    public CommandFeedback setAmpm(String ampm) {
	
	if (isValidTime((Integer) null, (Integer) null, ampm)) {
	    ampm = this.ampm;
	    return CommandFeedback.SUCCESSFUL_OPERATION;
	}
	
	return CommandFeedback.INVALID_TIME_AMPM;
    }
    
}

class Date {
    private int date, day, month, year;
    
    public Date() {
	
	//Initialize values:
    }
    
    // Accessor and Modifier functions:
}



/**
 * Class for storing/accessing different parameters of a command (like add, modify, due)
 * 
 */

public class Parameters {
    private String description, location, folder, priority;
    private Date date;
    private Time time;
    
    public Parameters() {
	
	// Initialize values;
    }
    
    // Accessor and Modifier functions
    
}
