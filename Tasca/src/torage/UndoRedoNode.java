package torage;

public class UndoRedoNode {
	private Task oldTask, newTask;
	private Reminder oldReminder, newReminder;
	private String command;
	
	public UndoRedoNode (Task oldTask, Reminder oldReminder, Task newTask, Reminder newReminder,String command) {
		this.oldTask = oldTask;
		this.newTask = newTask;
		this.oldReminder = oldReminder;
		this.newReminder = newReminder;
		this.command = command;
		return;
	}
	
	public Task getOldTask () {
		return oldTask;
	}
	
	public Task getNewTask () {
		return newTask;
	}
	
	public Reminder getOldReminder () {
		return oldReminder;
	}
	
	public Reminder getNewReminder () {
		return newReminder;
	}
	
	public String getCommand() {
		return command;
	}

}
