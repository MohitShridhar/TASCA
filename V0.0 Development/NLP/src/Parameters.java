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
    private Integer priority, taskId;
    private StringToTime startTime, endTime, remindTime;
    
    

    public Parameters() {
	// Initialize all parameters to null	
    }
  
    //Mutators:
    public CommandFeedback setStartTime (String rawInput) {
	try {
	    startTime = new StringToTime(rawInput);
	} catch (StringToTimeException e) {
	    //System.err.println("Error: " + e);
	    return CommandFeedback.INVALID_START_TIME;
	}
	
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setEndTime (String rawInput) {
	try {
	    endTime = new StringToTime(rawInput);
	} catch (StringToTimeException e) {
	    //System.err.println("Error: " + e);
	    return CommandFeedback.INVALID_END_TIME;
	}
	
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setRemindTime (String rawInput) {
	try {
	    remindTime = new StringToTime(rawInput);
	} catch (StringToTimeException e) {
	    //System.err.println("Error: " + e);
	    return CommandFeedback.INVALID_REMIND_TIME;
	}
	
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setDescription(String description) {	
	if (description.isEmpty() || description == null) {
	    return CommandFeedback.EMPTY_DESCRIPTION;
	}
	
	this.description = description;
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setLocation(String location) {
	if (location.isEmpty() || location == null) {
	    return CommandFeedback.EMPTY_LOCATION;
	}
	
	this.location = location;
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setPriority(String priority) {
	int intPriority = stringToIntPriority(priority);
	
	if (intPriority == PRIORITY_INVALID_REF) {
	    return CommandFeedback.INVALID_PRIORITY;
	}
	
	this.priority = intPriority;
	return CommandFeedback.SUCCESSFUL_OPERATION;	
    }
    
    public CommandFeedback setFolder(String folder) {
	if (!isValidFolder(folder)) {
	    return CommandFeedback.INVALID_FOLDER_REF;
	}
	
	this.folder = folder;
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setTaskId(int id) {
	// MUST INTEGRATE VALID CHECKER LATER!!!
	
	this.taskId = id;
	
	return CommandFeedback.SUCCESSFUL_OPERATION;
    }

    
    // Accessors:
    
    public StringToTime getStartTime() {
	return startTime;
    }
    
    public StringToTime getEndTime() {
	return endTime;
    }
    
    public StringToTime getRemindTime() {
	return remindTime;
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
    
    public String getPriority() {
	if (priority == null) {
	    return "null";
	}
	
	return Integer.toString(priority);
    }
    
    public String getTaskId() {
	if (taskId == null) {
	    return "null";
	}
	
	return Integer.toString(taskId);
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
