package Storage;

public class Reminder {
	private Time reminderTime;
	private Task task;
	
	public Reminder (Time reminderTime, Task task){
		this.reminderTime = reminderTime;
		this.task = task;
		return;
	}
	
	public Time getReminderTime () {
		return reminderTime;
	}
	
	public Task getTask () {
		return task;
	}
	
	public void setReminderTime (Time time) {
		reminderTime = time;
		return;
	}
	
	public void setTask (Task task) {
		this.task = task;
		return;
	}

}
