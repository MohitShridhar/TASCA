package controller;

import java.util.LinkedList;

import storage.Task;
import storage.FloatingTask;

/**
 * @author Narinderpal Singh Dhillon
 * @Matric A0097416X
 */
public class SystemMessage {
	private String message;
	private LinkedList<Task> timedTaskList;
	private LinkedList<FloatingTask> floatingTaskList;
	private int displayStatus = 0;
	
	public void setSystemMessage (String message) {
		this.message = message;
		return;
	}
	
	public String getSystemMessage () {
		return message;
	}
	
	public void setTimedList (LinkedList<Task> list){
		this.timedTaskList = list;
		return;
	}
	
	public LinkedList<Task> getTimedList (){
		return timedTaskList;
	}
	
	public LinkedList<FloatingTask> getFloatingList (){
		return floatingTaskList;
	}
	
	public void setFloatingList (LinkedList<FloatingTask> list) {
		this.floatingTaskList = list;
		return;
	}
	
	public int getDisplayStatus () {
		return displayStatus;
	}
	
	public void setDisplayStatus(int temp){
		this.displayStatus = temp;
		return;
	}

}
