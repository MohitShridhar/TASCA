/**
 * Data structure for a new command: Command + Parameters
 *
 */

public class Command {
    private CommandType command;
    private Parameters parameters;
    
    public Command() {
	command = null;
	parameters = null;
    }
    
    // Initializers: 
    public Command(CommandType command) {
	command = this.command;
	parameters = null;
    }
    
    public Command(CommandType command, Parameters parameters) {
	command = this.command;
	parameters = this.parameters;
    }
    
    // Mutators:
    public void setCommandType(CommandType command) {
	command = this.command;
    }
    
    public void setParameters(Parameters parameters) {
	parameters = this.parameters;
    }
    
    //Accessors:
    public CommandType getCommandType() {
	return command;
    }
    
    public Parameters getParameters() {
	return parameters;
    }
    
}