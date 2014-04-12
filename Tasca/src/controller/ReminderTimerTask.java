package controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import storage.AllTasks;
import storage.TaskWithReminder;
import storage.Task;

/**
 * @author Narinderpal Singh Dhillon
 * @Matric A0097416X
 */
public class ReminderTimerTask extends TimerTask {
	private AllTasks allTasks;
	private Controller controller;

	public void run() {
		TaskWithReminder currentReminder = allTasks.getCurrentReminder();

		int count = 0, total = allTasks.getTaskSize();
		Calendar temp = Calendar.getInstance();
		while (count < total) {
			Task task = allTasks.getTask(count);
			if (task.getEndTime().getTime().before(temp.getTime())) {
				allTasks.getTask(count).setIsTaskDone(true);
			}
			count++;
		}
		controller.updateDisplayGUI(controller.getDisplayStatus());
		if (currentReminder == null) {
			return;
		} else {
			Task task = currentReminder.getTask();

			SimpleDateFormat display = new SimpleDateFormat(
					"E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			System.out
					.printf("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n");
			System.out
					.printf("*-*-*-*-*-*-*-*-*-*-*-*-!!!REMINDER!!!!-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n");
			System.out
					.printf("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n");
			System.out.println("id: " + task.getTaskID());
			System.out.println("priority: " + task.getPriority());
			System.out.println("start: "
					+ display.format(task.getStartTime().getTime()));
			System.out.println("end: "
					+ display.format(task.getEndTime().getTime()));
			System.out.println("title: " + task.getTaskTitle());
			try {
				System.out.println("location: " + task.getLocation());
			} catch (Exception e1) {
				System.out.println("location: NIL");
			}
			System.out.printf("Is Task Done: ");
			System.out.println((task.getIsTaskDone()) ? "YES" : "NO");
			System.out.printf("Is All Day Event: ");
			System.out.println((task.getIsAllDayEvent()) ? "YES" : "NO");
			System.out
					.printf("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n");
			System.out
					.printf("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n");
			System.out
					.printf("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n");
		}

		return;
	}

	public ReminderTimerTask(AllTasks allTasks, Controller controller) {
		this.allTasks = allTasks;
		this.controller = controller;
		return;
	}

}
