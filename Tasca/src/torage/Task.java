package torage;

import java.util.Calendar;

public class Task {
	private int taskID, priority;
	private Calendar startTime, endTime;
	private boolean isThereReminder;
	private boolean isTaskDone;
	private boolean isAllDayEvent;
	private String taskTitle;
	private String location;
	
	// Note for NARIN: The convention used for NLP: if a parameter is not specified then it's NULL. 
	
	public Task (int taskID, int priority, Calendar startTime, Calendar endTime, boolean isThereReminder, boolean isTaskDone, 
			boolean isAllDayEvent, String taskTitle, String remarks) {
		this.taskID = taskID;
		this.priority = priority;
		
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.isThereReminder = isThereReminder;
		this.isAllDayEvent = isAllDayEvent;
		this.isTaskDone = isTaskDone;
		
		this.taskTitle = taskTitle;
		this.location = remarks;
	}
	
	public int getPriority() {
		return priority;
	}
	

	public Calendar getStartTime() {
		return startTime;
	}
	
	public Calendar getEndTime() {
		return endTime;
	}
	
	public boolean getIsThereReminder() {
		return isThereReminder;
	}
	
	public boolean getIsTaskDone() {
		return isTaskDone;
	}
	
	public boolean getIsAllDayEvent() {
		return isAllDayEvent;
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
	
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
		return;
	}
	
	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
		return;
	}
	
	public void setIsThereReminder(boolean isThereReminder) {
		this.isThereReminder = isThereReminder;
		return;
	}
	
	public void setIsTaskDone(boolean isTaskDone) {
		this.isTaskDone = isTaskDone;
		return;
	}
	
	public void setIsAllDayEvent(boolean isAllDayEvent) {
		this.isAllDayEvent = isAllDayEvent;
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
