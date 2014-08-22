package storage;

public class UndoRedoNode {
	private Task oldTask, newTask;
	private TaskWithReminder oldReminder, newReminder;
	private String command;
	
	public UndoRedoNode (Task oldTask, TaskWithReminder oldReminder, Task newTask, TaskWithReminder newReminder,String command) {
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
	
	public TaskWithReminder getOldReminder () {
		return oldReminder;
	}
	
	public TaskWithReminder getNewReminder () {
		return newReminder;
	}
	
	public String getCommand() {
		return command;
	}

}
