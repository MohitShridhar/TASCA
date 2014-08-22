package interpreter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;



/**
 * Class for storing/accessing different parameters of a command (like add, modify, due)
 * 
 */

//@author A0105912N
public class Parameters {
    
    private static final int SMALLEST_ID_REFERENCE = 0;
    private static final int EXCEPTION_INVALID_ID = -1;
    private static final int FIRST_DATE_PARSED = 0;
    
    private static final String STRING_DEFAULT = "default";
    private static final String STRING_NULL = "null";
    
    private static final int PRIORITY_NONE = 0;
    private static final int PRIORITY_HIGH = 1;
    private static final int PRIORITY_MEDIUM = 2;
    private static final int PRIORITY_LOW = 3;
    private static final int PRIORITY_INVALID_REF = -1;
    
    private static Config cfg = new Config(); 
    
    private String description, location, folder;
    private Integer priority, taskId, guiIdRef = -1;
    private Calendar startTime, endTime, remindTime, recurEndTime;
    
    Parser parser;    

    public Parameters() {
	parser = new Parser(); 
    }
    
    public static Calendar dateToCal(Date date){ 
	  Calendar cal = Calendar.getInstance();
	  
	  if (date == null) {
	      return null;
	  }
	  
	  cal.setTime(date);
	  return cal;
    }
  
    public CommandFeedback setStartTime (String rawInput) {
	List<DateGroup> groups = parser.parse(rawInput);
	
	if (groups.isEmpty()) {
	    return CommandFeedback.INVALID_START_TIME;
	} 
	
	startTime = dateToCal(groups.get(FIRST_DATE_PARSED).getDates().get(FIRST_DATE_PARSED));
        
        return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setEndTime (String rawInput) {
	List<DateGroup> groups = parser.parse(rawInput);
	
	if (groups.isEmpty()) {
	    return CommandFeedback.INVALID_END_TIME;
	} 
	
	endTime = dateToCal(groups.get(FIRST_DATE_PARSED).getDates().get(FIRST_DATE_PARSED));
	
        return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setRemindTime (String rawInput) {
	List<DateGroup> groups = parser.parse(rawInput);
	
	if (groups.isEmpty()) {
	    return CommandFeedback.INVALID_REMIND_TIME;
	} 
	
	remindTime = dateToCal(groups.get(FIRST_DATE_PARSED).getDates().get(FIRST_DATE_PARSED));

	if (groups.get(FIRST_DATE_PARSED).isRecurring()) { // Note: Recurring reminders are not supported by Logic (yet)
	    recurEndTime = dateToCal(groups.get(FIRST_DATE_PARSED).getRecursUntil());
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
        
        this.folder = folder.toLowerCase();
        return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    public CommandFeedback setTaskId(int id) {
	
        if (!isValidId(id)) {
            return CommandFeedback.INVALID_TASK_ID;
        }
	
        if (Interpreter.checkIsGuiIdEnabled()) {
            this.taskId = Interpreter.getRealId(id);
            this.guiIdRef = id;
        } else {
            this.taskId = id;
        }
        
        return CommandFeedback.SUCCESSFUL_OPERATION;
    }
    
    
    public int getGuiIdRef(){
	return guiIdRef;
    }
    
    public Calendar getStartTime() {
        return startTime;
    }
    
    public Calendar getEndTime() {
        return endTime;
    }
    
    public Calendar getRemindTime() {
        return remindTime;
    }
    
    public Calendar getRecurEndTime() {
	return recurEndTime;
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
            return STRING_NULL;
        }
        
        return Integer.toString(priority);
    }
    
    public String getTaskId() {
        if (taskId == null) {
            return STRING_NULL;
        }
        
        return Integer.toString(taskId);
    }
    
    
    private boolean isValidFolder(String folderName) {
	if (folderName.toLowerCase().equalsIgnoreCase(STRING_DEFAULT)) {
	    return true;
	} else if (cfg.getFolderId(folderName.toLowerCase()) != null) {
	    return true;
	}
	
        return false;
    }
    
    private boolean isValidId(int id) {
	
	if (Interpreter.checkIsGuiIdEnabled() && Interpreter.getRealId(id) != EXCEPTION_INVALID_ID) {
	    return true;
	} else if (!Interpreter.checkIsGuiIdEnabled() && id >= SMALLEST_ID_REFERENCE) {
	    return true;
	}
	
	return false;
    }
    
    private int stringToIntPriority(String priorityString) {
        if (isHigh(priorityString)) {
            return PRIORITY_HIGH;
        } else if (isMed(priorityString) ) {
            return PRIORITY_MEDIUM;
        } else if (isLow(priorityString)) {
            return PRIORITY_LOW;
        } else if (isNoneAssigned(priorityString)) {
            return PRIORITY_NONE;
        }
        
        return PRIORITY_INVALID_REF;
    }

    public boolean isNoneAssigned(String priorityString) {
	return priorityString.equalsIgnoreCase("none") || priorityString.equalsIgnoreCase("remove") || priorityString.equalsIgnoreCase("nothing") || priorityString.equalsIgnoreCase("0");
    }

    public boolean isLow(String priorityString) {
	return priorityString.equalsIgnoreCase("LOW") || priorityString.equalsIgnoreCase("not imp") || priorityString.equalsIgnoreCase("L") || priorityString.equalsIgnoreCase("3");
    }

    public boolean isMed(String priorityString) {
	return priorityString.equalsIgnoreCase("MEDIUM") || priorityString.equalsIgnoreCase("MED") || priorityString.equalsIgnoreCase("M") || priorityString.equalsIgnoreCase("2");
    }

    public boolean isHigh(String priorityString) {
	return priorityString.equalsIgnoreCase("HIGH") || priorityString.equalsIgnoreCase("H") || priorityString.equalsIgnoreCase("imp") || priorityString.equalsIgnoreCase("important") || priorityString.equalsIgnoreCase("1");
    }
    
}