package interpreter;


/**
 * Data structure for a new command: Command + Parameters
 *
 */

//@author A0105912N

public class Command {
    private static final String INVALID_TASK_ID = "'%s' is not a valid number";
    private CommandType command;
    private Parameters parameters;

    public Command() {
        command = null;
        parameters = new Parameters();
    }
    
    
    public Command(CommandType command) {
        this.command = command;
        parameters = null;
    }
    
    public Command(CommandType command, Parameters parameters) {
        this.command = command;
        parameters = this.parameters;
    }
    

    
    public void setCommandType(CommandType command) {
        this.command = command;
    }
    
    public CommandFeedback setDescription(String description) {
        return parameters.setDescription(description);
    }
    
    public void setLocation(String location) {
        parameters.setLocation(location);
    }
    
    public CommandFeedback setFolder(String folder) {
        return parameters.setFolder(folder);
    }
    
    public CommandFeedback setPriority(String priority){
        return parameters.setPriority(priority);
    }
    
    public CommandFeedback setTaskId(String id) {
	checkForValidId(id);
        return parameters.setTaskId(Integer.parseInt(id));
    }


    private void checkForValidId(String id) {
	try {
	    Integer.parseInt(id); 
	} catch (NumberFormatException e) {
	    throw new IllegalArgumentException(String.format(INVALID_TASK_ID, id));
	}
    }
    
    public CommandFeedback setStartTime(String time) {
        return parameters.setStartTime(time);
    }
    
    public CommandFeedback setEndTime(String time) {
        return parameters.setEndTime(time);
    }
    
    public CommandFeedback setRemindTime(String time) {
        return parameters.setRemindTime(time);
    }
    
    

    public CommandType getCommandType() {
        return command;
    }
    
    public Parameters getParameters() {
        return parameters;
    }
    
}
