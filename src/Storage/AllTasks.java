package Storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Scanner;

public class AllTasks {
	private LinkedList<Task> allTasks = new LinkedList<Task>();
	private Time currentTime;
	private Task currentTask;
	private LinkedList<Reminder> allReminders = new LinkedList<Reminder>();
	private Reminder currentReminder;
	
	public void loadData() throws FileNotFoundException {
		FileInputStream fStream = new FileInputStream("system_saved_tasks.txt");
        Scanner fileScanner = new Scanner(fStream);
        
        FileInputStream fStream2 = new FileInputStream("system_saved_reminders.txt");
        Scanner fileScanner2 = new Scanner(fStream2);
        
        while (fileScanner.hasNext()){
        	allTasks.add(new Task(fileScanner.nextInt(),
        			new Time(fileScanner.nextInt(),fileScanner.nextInt(), fileScanner.nextInt(),fileScanner.nextInt(),fileScanner.nextInt()),
        			new Time(fileScanner.nextInt(),fileScanner.nextInt(), fileScanner.nextInt(),fileScanner.nextInt(),fileScanner.nextInt()),
        			fileScanner.nextBoolean(), fileScanner.nextBoolean(), fileScanner.nextBoolean(), fileScanner.nextLine(), fileScanner.nextLine()));
        	if (allTasks.getLast().getIsThereReminder() == true) {
        		allReminders.add(new Reminder(new Time(fileScanner2.nextInt(), fileScanner2.nextInt(), fileScanner2.nextInt(), fileScanner2.nextInt(), fileScanner2.nextInt()),
        				allTasks.getLast()));
        		fileScanner2.nextLine();
        	}
        }
        fileScanner.close();
        fileScanner2.close();
		return;		
	}
	
	public void saveData() throws FileNotFoundException {
		int counter=0, counter2=0;
		FileOutputStream out,out2;
        PrintStream prt,prt2;

        try {
            out = new FileOutputStream("system_saved_tasks.txt");
            prt = new PrintStream(out);
            
            out2 = new FileOutputStream("system_saved_reminders.txt");
            prt2 = new PrintStream(out2);
            while (counter < allTasks.size()){
            	prt.printf("%d %d %d %d %d %d %d %d %d %d %d %b %b %b %s\n%s\n", counter, allTasks.get(counter).getStartTime().getYear(),
            			allTasks.get(counter).getStartTime().getMonth(), allTasks.get(counter).getStartTime().getDay(), allTasks.get(counter).getStartTime().getHour(),
            			allTasks.get(counter).getStartTime().getMinute(), allTasks.get(counter).getEndTime().getYear(),allTasks.get(counter).getEndTime().getMonth(),
            			allTasks.get(counter).getEndTime().getDay(), allTasks.get(counter).getEndTime().getHour(), allTasks.get(counter).getEndTime().getMinute(),
            			allTasks.get(counter).getIsThereReminder(), allTasks.get(counter).getIsTaskDone(), allTasks.get(counter).getIsAllDayEvent(), 
            			allTasks.get(counter).getTaskTitle(), allTasks.get(counter).getRemarks());
            	
            	if (allTasks.get(counter).getIsThereReminder() == true) {
            		prt2.printf("%d %d %d %d %d\n", allReminders.get(counter2).getReminderTime().getYear(), allReminders.get(counter2).getReminderTime().getMonth(),
            				allReminders.get(counter2).getReminderTime().getDay(), allReminders.get(counter2).getReminderTime().getHour(), allReminders.get(counter2).getReminderTime().getMinute());
            		
            	}
            	counter=counter+1;
            }
            prt.close();
            prt2.close();
        } catch(Exception e) {
            System.out.println("Write error");
        }
	}
	
	public int getSize() {
		return allTasks.size();
	}
	
	public int getReminderSize() {
		return allReminders.size();
	}
	
	public Task getTask (int index) {
		return allTasks.get(index);
	}
	
	public void deleteTask (int index) {
		allTasks.remove(index);
		updateTaskID(index);
		return;
	}
	
	public int searchForCorrespondingReminder (Task task) {
		int counter = 0;
		int index=-1;
		while (counter < allReminders.size()) {
			if (allReminders.get(counter).getTask() == task){
				index= counter;
				counter = allReminders.size();
			}
			counter = counter + 1;
		}
		return index;
		
	}
	
	public void deleteReminder (Task task) {
		allReminders.remove(searchForCorrespondingReminder(task));
		return;
	}
	
	public void addTask (int index, Task task) {
		allTasks.add(index, task);
		updateTaskID(index);
		return;
	}
	
	public Reminder getReminder (int index) {
		return allReminders.get(index);
	}
	
	public void addReminder (int index, Reminder reminder) {
		allReminders.add(index, reminder);
		return;
	}
	
	public void deleteReminder (int index) {
		allReminders.remove(index);
		return;
	}
	
	private void updateTaskID (int index) {
		while (index < allTasks.size()) {
			allTasks.get(index).setTaskID(index+1);
			index = index + 1;
		}
		return;
	}
	
	private void setCurrentTime() {
		Calendar current = Calendar.getInstance();
		currentTime.setYear(current.get(Calendar.YEAR));
		currentTime.setMonth(current.get(Calendar.MONTH));
		currentTime.setDay(current.get(Calendar.DAY_OF_MONTH));
		currentTime.setHour(current.get(Calendar.HOUR_OF_DAY));
		currentTime.setMinute(current.get(Calendar.MINUTE));
		return;
	}
	
	private boolean setCurrentTask() {
		int counter = 0;
		boolean check=true;
		
		setCurrentTime();
		while (counter < allTasks.size() && check) {
			if (currentTime.getYear() <= allTasks.get(counter).getStartTime().getYear()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.size() && check) {
			if (currentTime.getMonth() <= allTasks.get(counter).getStartTime().getMonth()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.size() && check) {
			if (currentTime.getDay() <= allTasks.get(counter).getStartTime().getDay()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.size() && check) {
			if (currentTime.getHour() <= allTasks.get(counter).getStartTime().getHour()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.size() && check) {
			if (currentTime.getMinute() <= allTasks.get(counter).getStartTime().getMinute()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		if (counter >= allTasks.size()) {
			currentTask = null;
			return false;
		} else {
			return true;
		}
		
	}
	
	private boolean setCurrentReminder () {
		int counter = 0;
		boolean check=true;
		
		setCurrentTime();
		while (counter < allReminders.size() && check) {
			if (currentTime.getYear() == allReminders.get(counter).getReminderTime().getYear()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.size() && check) {
			if (currentTime.getMonth() == allReminders.get(counter).getReminderTime().getMonth()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.size() && check) {
			if (currentTime.getDay() == allReminders.get(counter).getReminderTime().getDay()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.size() && check) {
			if (currentTime.getHour() == allReminders.get(counter).getReminderTime().getHour()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		check = true;
		while (counter < allTasks.size() && check) {
			if (currentTime.getMinute() == allReminders.get(counter).getReminderTime().getMinute()) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}
		
		if (counter >= allTasks.size()) {
			currentReminder = null;
			return false;
		} else {
			currentReminder = allReminders.get(counter);
			currentReminder.getTask().setIsThereReminder(false);
			allReminders.remove(counter);
			return true;
		}
	}
	
	public Task getCurrentTask() {
		if(setCurrentTask()){
			return currentTask;
		} else {
			return null;
		}
		
	}
	
	public Reminder getCurrentReminder () {
		if(setCurrentReminder()) {
		return currentReminder;
		} else{
			return null;
		}
	}

}
