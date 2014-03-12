package nlp;


/**
 * Data structure for a new command: Command + Parameters
 *
 */

public class Command {
    private CommandType command;
    private Parameters parameters;
    
    public Command() {
        command = null;
        parameters = new Parameters();
    }
    
    // Initializers: 
    public Command(CommandType command) {
        this.command = command;
        parameters = null;
    }
    
    public Command(CommandType command, Parameters parameters) {
        this.command = command;
        parameters = this.parameters;
    }
    
    // Mutators:
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
        return parameters.setTaskId(Integer.parseInt(id));
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
    
    
//    private String description, location, folder;
//    private Integer priority, taskId;
//    private StringToTime dateTime;
    
    
    //Accessors:
    public CommandType getCommandType() {
        return command;
    }
    
    public Parameters getParameters() {
        return parameters;
    }
    
}
