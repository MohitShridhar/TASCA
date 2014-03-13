package storage;

import java.util.Calendar;

public class Reminder {
	private Calendar reminderTime;
	private Task task;
	
	public Reminder (Calendar reminderTime, Task task){
		this.reminderTime = reminderTime;
		this.task = task;
		return;
	}
	
	public Calendar getReminderTime () {
		return reminderTime;
	}
	
	public Task getTask () {
		return task;
	}
	
	public void setReminderTime (Calendar time) {
		reminderTime = time;
		return;
	}
	
	public void setTask (Task task) {
		this.task = task;
		return;
	}

}
