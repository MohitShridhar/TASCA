package Storage;

public class Task {
	private int taskID;
	private Time startTime, endTime;
	private boolean isThereReminder;
	private boolean isTaskDone;
	private boolean isAllDayEvent;
	private String taskTitle;
	private String remarks;
	
	public Task (int taskID, Time startTime, Time endTime, boolean isThereReminder, boolean isTaskDone, boolean isAllDayEvent, String taskTitle, String remarks) {
		this.taskID = taskID;
		
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.isThereReminder = isThereReminder;
		this.isAllDayEvent = isAllDayEvent;
		this.isTaskDone = isTaskDone;
		
		this.taskTitle = taskTitle;
		this.remarks = remarks;
	}
	

	public Time getStartTime() {
		return startTime;
	}
	
	public Time getEndTime() {
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
	
	public String getRemarks() {
		return remarks;
	}
	
	public int getTaskID() {
		return taskID;
	}
	
	public void setTaskID(int taskID) {
		this.taskID = taskID;
		return;
	}
	
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
		return;
	}
	
	public void setEndTime(Time endTime) {
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
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
		return;
	}

}
