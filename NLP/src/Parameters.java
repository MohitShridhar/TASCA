import com.clutch.dates.StringToTime;
import com.clutch.dates.StringToTimeException;

// import com.clutch.dates.* //<<If needed>>

/**
 * Class for storing/accessing different parameters of a command (like add, modify, due)
 * 
 */

public class Parameters {
    
    private static final int PRIORITY_HIGH = 1;
    private static final int PRIORITY_MEDIUM = 2;
    private static final int PRIORITY_LOW = 3;
    private static final int PRIORITY_INVALID_REF = -1;
    
    private String description, location, folder;
    private int priority;
    private StringToTime dateTime;
    

    public Parameters() {
	// Initialize all parameters to null
	
	priority = (Integer) null;
    }
  
    //Mutators:
    public CommandFeedback setDateTime (String rawInput) {
	try {
	    dateTime = new StringToTime(rawInput);
	} catch (StringToTimeException e) {
	    System.err.println("Error: " + e);
	    return CommandFeedback.INVALID_TIME;
	}
	
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setDescription(String description) {
	description = this.description;
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setLocation(String location) {
	location = this.location;
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setPriority(String priority) {
	int intPriority = stringToIntPriority(priority);
	
	if (intPriority == PRIORITY_INVALID_REF) {
	    return CommandFeedback.INVALID_PRIORITY;
	}
	
	intPriority = this.priority;
	return CommandFeedback.SUCCESSFUL_OPERATION;	
    }
    
    public CommandFeedback setFolder(String folder) {
	if (!isValidFolder(folder)) {
	    return CommandFeedback.INVALID_FOLDER_REF;
	}
	
	folder = this.folder;
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    // Accessors:
    
    public StringToTime getDateTime() {
	return dateTime;
    }
    
    public String getDescription() {
	return description;
    }
    
    public String getLocation() {
	return location;
    }
    
    public String getFolder() {
	return folder;
    }
    
    public int getPriority() {
	return priority;
    }
    
    
    private boolean isValidFolder(String folderName) {
	//MUST INTEGRATE LATER!!!
	return true;
    }
    
    private int stringToIntPriority(String priorityString) {
	if (priorityString.equalsIgnoreCase("HIGH") || priorityString.equalsIgnoreCase("H") || priorityString.equalsIgnoreCase("imp") || priorityString.equalsIgnoreCase("important")) {
	    return PRIORITY_HIGH;
	} else if (priorityString.equalsIgnoreCase("MEDIUM") || priorityString.equalsIgnoreCase("MED") || priorityString.equalsIgnoreCase("M")) {
	    return PRIORITY_MEDIUM;
	} else if (priorityString.equalsIgnoreCase("LOW") || priorityString.equalsIgnoreCase("not imp") || priorityString.equalsIgnoreCase("L")) {
	    return PRIORITY_LOW;
	}
	
	
	return PRIORITY_INVALID_REF;
		
    }
    
}
