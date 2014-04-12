package controller;

import java.util.LinkedList;

import storage.TaskWithReminder;
import storage.FloatingTask;

/**
 * @author Narinderpal Singh Dhillon
 * @Matric A0097416X
 */
public class CurrentSystemState {
	private String message;
	private LinkedList<TaskWithReminder> timedTaskList;
	private LinkedList<FloatingTask> floatingTaskList;
	private int displayStatus = 0;
	
	public void setSystemMessage (String message) {
		this.message = message;
		return;
	}
	
	public String getSystemMessage () {
		return message;
	}
	
	public void setTimedList (LinkedList<TaskWithReminder> list){
		this.timedTaskList = list;
		return;
	}
	
	public LinkedList<TaskWithReminder> getTimedList (){
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
	
	public void sortForGUI () {
		int count = 0 , total = timedTaskList.size();
		
		LinkedList<TaskWithReminder> temp = new LinkedList<TaskWithReminder>();
		LinkedList<FloatingTask> temp2 = new LinkedList<FloatingTask>();
		while (count < total){
			if (timedTaskList.get(count).getTask().getIsTaskDone() == false){
				temp.add(timedTaskList.get(count));
			}
			count ++;
		}
		count = 0;
		
		while (count < total){
			if (timedTaskList.get(count).getTask().getIsTaskDone() == true){
				temp.add(timedTaskList.get(count));
			}
			count ++;
		}
		
		count = 0;
		total = floatingTaskList.size();
		while (count < total){
			if (floatingTaskList.get(count).getIsTaskDone() == false){
				temp2.add(floatingTaskList.get(count));
			}
			count ++;
		}
		
		count = 0;
		while (count < total){
			if (floatingTaskList.get(count).getIsTaskDone() == true){
				temp2.add(floatingTaskList.get(count));
			}
			count ++;
		}
		this.timedTaskList = temp;
		this.floatingTaskList = temp2;
	}

}
