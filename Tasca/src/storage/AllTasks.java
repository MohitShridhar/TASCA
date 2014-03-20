package storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Date;

public class AllTasks {
	private LinkedList<Task> allTasks;
	private Calendar currentTime;
	private Task currentTask;
	private LinkedList<Reminder> allReminders;
	private Reminder currentReminder;

	public AllTasks() {
		allTasks = new LinkedList<Task>();
		allReminders = new LinkedList<Reminder>();
		return;
	}

	public void loadData() throws FileNotFoundException {
		FileInputStream fStream = new FileInputStream("system_saved_tasks.txt");
		Scanner fileScanner = new Scanner(fStream);


		while (fileScanner.hasNext()) {
			int taskID = fileScanner.nextInt();
			int priority = fileScanner.nextInt();

			Calendar startTime = Calendar.getInstance();
			startTime.set(fileScanner.nextInt(), fileScanner.nextInt(),
					fileScanner.nextInt(), fileScanner.nextInt(),
					fileScanner.nextInt());

			Calendar endTime = Calendar.getInstance();
			endTime.set(fileScanner.nextInt(), fileScanner.nextInt(),
					fileScanner.nextInt(), fileScanner.nextInt(),
					fileScanner.nextInt());

			allTasks.add(new Task(taskID, priority, startTime, endTime,
					fileScanner.nextBoolean(), fileScanner.nextBoolean(),
					fileScanner.nextBoolean(), eliminateFrontSpace(fileScanner
							.nextLine()), fileScanner.nextLine()));
			if (allTasks.getLast().getIsThereReminder() == true) {
				Calendar reminderTime = Calendar.getInstance();
				reminderTime.set(fileScanner.nextInt(),
						fileScanner.nextInt(), fileScanner.nextInt(),
						fileScanner.nextInt(), fileScanner.nextInt());

				allReminders
						.add(new Reminder(reminderTime, allTasks.getLast()));
				fileScanner.nextLine();
			}
		}
		fileScanner.close();
		this.sortReminders();
		return;
	}

	public void saveData() throws FileNotFoundException {
		int counter = 0;
		FileOutputStream out;
		PrintStream prt;

		try {
			out = new FileOutputStream("system_saved_tasks.txt");
			prt = new PrintStream(out);

			while (isValidTaskId(counter)) {
				prt.printf(
						"%d %d %d %d %d %d %d %d %d %d %d %d %b %b %b %s",
						counter,
						allTasks.get(counter).getPriority(),
						allTasks.get(counter).getStartTime().get(Calendar.YEAR),
						allTasks.get(counter).getStartTime()
								.get(Calendar.MONTH),
						allTasks.get(counter).getStartTime()
								.get(Calendar.DAY_OF_MONTH),
						allTasks.get(counter).getStartTime()
								.get(Calendar.HOUR_OF_DAY),
						allTasks.get(counter).getStartTime()
								.get(Calendar.MINUTE),
						allTasks.get(counter).getEndTime().get(Calendar.YEAR),
						allTasks.get(counter).getEndTime().get(Calendar.MONTH),
						allTasks.get(counter).getEndTime()
								.get(Calendar.DAY_OF_MONTH),
						allTasks.get(counter).getEndTime()
								.get(Calendar.HOUR_OF_DAY),
						allTasks.get(counter).getEndTime().get(Calendar.MINUTE),
						allTasks.get(counter).getIsThereReminder(), allTasks
								.get(counter).getIsTaskDone(),
						allTasks.get(counter).getIsAllDayEvent(),
						allTasks.get(counter).getTaskTitle());
				prt.println();
				prt.printf("%s", allTasks.get(counter).getLocation());
				prt.println();

				if (allTasks.get(counter).getIsThereReminder()) {
					prt.printf(
							"%d %d %d %d %d",
							allReminders
									.get(this
											.searchForCorrespondingReminder(allTasks
													.get(counter)))
									.getReminderTime().get(Calendar.YEAR),
							allReminders
									.get(this
											.searchForCorrespondingReminder(allTasks
													.get(counter)))
									.getReminderTime().get(Calendar.MONTH),
							allReminders
									.get(this
											.searchForCorrespondingReminder(allTasks
													.get(counter)))
									.getReminderTime()
									.get(Calendar.DAY_OF_MONTH),
							allReminders
									.get(this
											.searchForCorrespondingReminder(allTasks
													.get(counter)))
									.getReminderTime()
									.get(Calendar.HOUR_OF_DAY),
							allReminders
									.get(this
											.searchForCorrespondingReminder(allTasks
													.get(counter)))
									.getReminderTime().get(Calendar.MINUTE));
					prt.println();

				}
				counter = counter + 1;
			}
			prt.close();
		} catch (Exception e) {
			System.out.println("Write error");
		}
	}

	private void sortReminders() {
		LinkedList<Reminder> temp = new LinkedList<Reminder>();
		int numOfReminders = this.getReminderSize();

		while (numOfReminders != 0) {
			int count = 1, index = 0;
			Reminder smallest = allReminders.get(0);

			while (count < numOfReminders) {
				Date next = allReminders.get(count).getReminderTime().getTime();
				if (smallest.getReminderTime().getTime().after(next)) {
					smallest = allReminders.get(count);
					index = count;
				}
				count++;
			}
			temp.add(smallest);
			allReminders.remove(index);
			numOfReminders--;
		}
		allReminders = temp;
		return;
	}

	public String eliminateFrontSpace(String input) {
		return input.substring(1);
	}

	public int getSize() {
		return allTasks.size();
	}

	public int getReminderSize() {
		return allReminders.size();
	}

	public Task getTask(int index) {
		return allTasks.get(index);
	}

	public void deleteTask(int index) {
		allTasks.remove(index);
		updateTaskID(index);
		return;
	}

	public int searchForCorrespondingReminder(Task task) {
		int counter = 0;
		int index = -1;
		while (counter < allReminders.size()) {
			if (allReminders.get(counter).getTask() == task) {
				index = counter;
				counter = allReminders.size();
			}
			counter = counter + 1;
		}
		return index;

	}

	public void deleteReminder(Task task) {
		allReminders.remove(searchForCorrespondingReminder(task));
		return;
	}

	public void addTask(int index, Task task) {
		allTasks.add(index, task);
		updateTaskID(index);
		return;
	}

	public Reminder getReminder(int index) {
		return allReminders.get(index);
	}

	public void addReminder(int index, Reminder reminder) {
		allReminders.add(index, reminder);
		sortReminders();
		return;
	}

	public void deleteReminder(int index) {
		allReminders.remove(index);
		return;
	}

	private void updateTaskID(int index) {
		while (isValidTaskId(index)) {
			allTasks.get(index).setTaskID(index);
			index = index + 1;
		}
		return;
	}

	public boolean isValidTaskId(int index) {
		return index < allTasks.size();
	}

	private void setCurrentTime() {
		Calendar current = Calendar.getInstance();
		currentTime = current;
		return;
	}

	private boolean setCurrentTask() {
		int counter = 0;
		boolean check = true;

		setCurrentTime();
		while (isValidTaskId(counter) && check) {
			if (currentTime.get(Calendar.YEAR) <= allTasks.get(counter)
					.getStartTime().get(Calendar.YEAR)) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}

		check = true;
		while (isValidTaskId(counter) && check) {
			if (currentTime.get(Calendar.MONTH) <= allTasks.get(counter)
					.getStartTime().get(Calendar.MONTH)) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}

		check = true;
		while (isValidTaskId(counter) && check) {
			if (currentTime.get(Calendar.DAY_OF_MONTH) <= allTasks.get(counter)
					.getStartTime().get(Calendar.DAY_OF_MONTH)) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}

		check = true;
		while (isValidTaskId(counter) && check) {
			if (currentTime.get(Calendar.HOUR_OF_DAY) <= allTasks.get(counter)
					.getStartTime().get(Calendar.HOUR_OF_DAY)) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}

		check = true;
		while (isValidTaskId(counter) && check) {
			if (currentTime.get(Calendar.MINUTE) <= allTasks.get(counter)
					.getStartTime().get(Calendar.MINUTE)) {
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

	private boolean setCurrentReminder() {
		int counter = 0;
		boolean check = true;

		setCurrentTime();
		while (counter < allReminders.size() && check) {
			if (currentTime.get(Calendar.YEAR) == allReminders.get(counter)
					.getReminderTime().get(Calendar.YEAR)) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}

		check = true;
		while (isValidTaskId(counter) && check) {
			if (currentTime.get(Calendar.MONTH) == allReminders.get(counter)
					.getReminderTime().get(Calendar.MONTH)) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}

		check = true;
		while (isValidTaskId(counter) && check) {
			if (currentTime.get(Calendar.DAY_OF_MONTH) == allReminders
					.get(counter).getReminderTime().get(Calendar.DAY_OF_MONTH)) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}

		check = true;
		while (isValidTaskId(counter) && check) {
			if (currentTime.get(Calendar.HOUR_OF_DAY) == allReminders
					.get(counter).getReminderTime().get(Calendar.HOUR_OF_DAY)) {
				check = false;
				counter = counter - 1;
			}
			counter = counter + 1;
		}

		check = true;
		while (isValidTaskId(counter) && check) {
			if (currentTime.get(Calendar.MINUTE) == allReminders.get(counter)
					.getReminderTime().get(Calendar.MINUTE)) {
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
		if (setCurrentTask()) {
			return currentTask;
		} else {
			return null;
		}

	}

	public Reminder getCurrentReminder() {
		if (setCurrentReminder()) {
			return currentReminder;
		} else {
			return null;
		}
	}

}
