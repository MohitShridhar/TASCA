package storage;

import java.util.Calendar;
/**
 * @author Narinderpal Singh Dhillon
 * @Matric A0097416X
 */
public class Reminder {
	private Calendar reminderTime;
	private Task task;
	
	public Reminder (Calendar reminderTime, Task task){
		this.reminderTime = reminderTime;
		this.task = task;
		return;
	}
	
	// Note: The export to ics feature is designed to output only one reminder per task.
	// if recurring reminders are implemented, then notify MOHIT
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
