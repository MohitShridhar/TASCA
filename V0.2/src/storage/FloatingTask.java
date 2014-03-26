package storage;

public class FloatingTask {

	private int taskID, priority;
	
	private boolean isTaskDone;
	private String taskTitle;
	private String location;

	// Note for NARIN: The convention used for NLP: if a parameter is not
	// specified then it's NULL.

	public FloatingTask(int taskID, int priority,
			boolean isTaskDone,  String taskTitle,
			String location) {
		this.taskID = taskID;
		this.priority = priority;

		this.isTaskDone = isTaskDone;

		this.taskTitle = taskTitle;
		this.location = location;
	}

	public int getPriority() {
		return priority;
	}

	

	public boolean getIsTaskDone() {
		return isTaskDone;
	}

	

	public String getTaskTitle() {
		return taskTitle;
	}

	public String getLocation() {
		return location;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setPriority(int priority) {
		this.priority = priority;
		return;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
		return;
	}

	

	public void setIsTaskDone(boolean isTaskDone) {
		this.isTaskDone = isTaskDone;
		return;
	}


	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
		return;
	}

	public void setLocation(String location) {
		this.location = location;
		return;
	}

}
