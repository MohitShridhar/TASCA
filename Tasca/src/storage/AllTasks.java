package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Date;

/**
 * @author Narinderpal Singh Dhillon
 * @Matric A0097416X
 */
public class AllTasks {
        private LinkedList<Task> allTasks;
        private Calendar currentTime;
        private Task currentTask;
        private LinkedList<Reminder> allReminders;
        private Reminder currentReminder;
        private LinkedList<FloatingTask> allFloatingTasks;

        public AllTasks() {
                allTasks = new LinkedList<Task>();
                allReminders = new LinkedList<Reminder>();
                allFloatingTasks = new LinkedList<FloatingTask>();
                return;
        }

        public void loadData() throws FileNotFoundException {
                loadTasks();
                loadFloatingTasks();
                return;
        }

        private void loadFloatingTasks() throws FileNotFoundException {
                FileInputStream fStream = new FileInputStream(
                                "system_saved_floatingTasks.txt");
                Scanner fileScanner = new Scanner(fStream);

                while (fileScanner.hasNext()) {
                        int folder = fileScanner.nextInt();
                        int taskID = fileScanner.nextInt();
                        int priority = fileScanner.nextInt();

                        allFloatingTasks.add(new FloatingTask(folder, taskID, priority,
                                        fileScanner.nextBoolean(), eliminateFrontSpace(fileScanner
                                                        .nextLine()), fileScanner.nextLine()));
                }
                fileScanner.close();

        }

        private void loadTasks() throws FileNotFoundException {
                FileInputStream fStream = new FileInputStream("system_saved_tasks.txt");
                Scanner fileScanner = new Scanner(fStream);

                while (fileScanner.hasNext()) {
                        int folder = fileScanner.nextInt();
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

                        allTasks.add(new Task(folder, taskID, priority, startTime, endTime,
                                        fileScanner.nextBoolean(), fileScanner.nextBoolean(),
                                        fileScanner.nextBoolean(), eliminateFrontSpace(fileScanner
                                                        .nextLine()), fileScanner.nextLine()));
                        if (allTasks.getLast().getIsThereReminder() == true) {
                                Calendar reminderTime = Calendar.getInstance();
                                reminderTime.set(fileScanner.nextInt(), fileScanner.nextInt(),
                                                fileScanner.nextInt(), fileScanner.nextInt(),
                                                fileScanner.nextInt());

                                allReminders
                                                .add(new Reminder(reminderTime, allTasks.getLast()));
                                fileScanner.nextLine();
                        }
                }
                fileScanner.close();
                this.sortReminders();
        }

        public void saveData() throws FileNotFoundException {
                saveTasks();
                saveFloatingTasks();
                return;
        }

        private void saveFloatingTasks() {
                int counter = this.getTaskSize();
                int lastTaskID = this.getTaskSize() + this.getFloatingTaskSize();
                int index = 0;
                FileOutputStream out;
                PrintStream prt;

                try {
                        out = new FileOutputStream("system_saved_floatingTasks.txt");
                        prt = new PrintStream(out);
                        if (lastTaskID == 0) {
                                prt.printf(" ");
                        } else {

                                while (counter < lastTaskID) {
                                        prt.printf("%d %d %d %b %s", allFloatingTasks.get(index)
                                                        .getFolder(), counter, allFloatingTasks.get(index)
                                                        .getPriority(), allFloatingTasks.get(index)
                                                        .getIsTaskDone(),

                                        allFloatingTasks.get(index).getTaskTitle());
                                        prt.println();
                                        prt.printf("%s", allFloatingTasks.get(index).getLocation());
                                        prt.println();

                                        counter = counter + 1;
                                        index = index + 1;
                                }
                        }
                        prt.close();
                } catch (Exception e) {
                        System.out.println("Write error");
                }

        }

        private void saveTasks() {
                int counter = 0;
                FileOutputStream out;
                PrintStream prt;

                try {

                        out = new FileOutputStream("system_saved_tasks.txt");
                        prt = new PrintStream(out);
                        if (allTasks.size() == 0) {
                                prt.printf(" ");
                        } else {

                                while (isValidTaskId(counter)) {
                                        prt.printf(
                                                        "%d %d %d %d %d %d %d %d %d %d %d %d %d %b %b %b %s",
                                                        allTasks.get(counter).getFolder(),
                                                        counter,
                                                        allTasks.get(counter).getPriority(),
                                                        allTasks.get(counter).getStartTime()
                                                                        .get(Calendar.YEAR),
                                                        allTasks.get(counter).getStartTime()
                                                                        .get(Calendar.MONTH),
                                                        allTasks.get(counter).getStartTime()
                                                                        .get(Calendar.DAY_OF_MONTH),
                                                        allTasks.get(counter).getStartTime()
                                                                        .get(Calendar.HOUR_OF_DAY),
                                                        allTasks.get(counter).getStartTime()
                                                                        .get(Calendar.MINUTE),
                                                        allTasks.get(counter).getEndTime()
                                                                        .get(Calendar.YEAR),
                                                        allTasks.get(counter).getEndTime()
                                                                        .get(Calendar.MONTH),
                                                        allTasks.get(counter).getEndTime()
                                                                        .get(Calendar.DAY_OF_MONTH),
                                                        allTasks.get(counter).getEndTime()
                                                                        .get(Calendar.HOUR_OF_DAY),
                                                        allTasks.get(counter).getEndTime()
                                                                        .get(Calendar.MINUTE), allTasks
                                                                        .get(counter).getIsThereReminder(),
                                                        allTasks.get(counter).getIsTaskDone(), allTasks
                                                                        .get(counter).getIsAllDayEvent(), allTasks
                                                                        .get(counter).getTaskTitle());
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
                return allTasks.size() + allFloatingTasks.size();
        }

        public int getTaskSize() {
                return allTasks.size();
        }

        public int getReminderSize() {
                return allReminders.size();
        }

        public Task getTask(int index) {
                return allTasks.get(index);
        }

        public boolean deleteTask(int index) {
                allTasks.remove(index);
                updateTaskID(index);
                this.updateFloatingTaskID();
                return true;
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

        public boolean deleteReminder(Task task) {
                allReminders.remove(searchForCorrespondingReminder(task));
                return true;
        }

        public boolean addTask(int index, Task task) {
                allTasks.add(index, task);
                updateTaskID(index);
                this.updateFloatingTaskID();
                return true;
        }

        public Reminder getReminder(int index) {
                return allReminders.get(index);
        }

        public boolean addReminder(int index, Reminder reminder) {
                allReminders.add(index, reminder);
                sortReminders();
                return true;
        }

        public boolean deleteReminder(int index) {
                allReminders.remove(index);
                return true;
        }

        private void updateTaskID(int index) {
                while (isValidTaskId(index)) {
                        allTasks.get(index).setTaskID(index);
                        index = index + 1;
                }
                return;
        }

        public boolean isValidTaskId(int index) {
                return index < allTasks.size() && index >= 0;
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

        public boolean clearAllMissedReminders() {
                int counter = 0, index = -1;

                setCurrentTime();
                while (counter < allReminders.size()) {
                        if (currentTime.getTime().after(
                                        allReminders.get(counter).getReminderTime().getTime())) {
                                index = counter;
                        }
                        counter = counter + 1;
                }
                counter = 0;

                while (counter <= index && allReminders.size() != 0) {
                        currentReminder = allReminders.get(0);
                        currentReminder.getTask().setIsThereReminder(false);
                        allReminders.remove(0);
                        counter = counter + 1;
                }
                return true;
        }

        private boolean setCurrentReminder() {
                int counter = 0, index = -1;

                setCurrentTime();
                while (counter < allReminders.size()) {
                        if (currentTime.getTime().after(
                                        allReminders.get(counter).getReminderTime().getTime())) {
                                index = counter;
                        }
                        counter = counter + 1;
                }
                if (index == -1) {
                        currentReminder = null;
                        return false;
                } else {
                        currentReminder = allReminders.get(index);
                        currentReminder.getTask().setIsThereReminder(false);
                        allReminders.remove(index);
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

        public int getFloatingTaskSize() {
                return allFloatingTasks.size();
        }

        public boolean addFloatingTask(FloatingTask node) {
                allFloatingTasks.add(node);
                updateFloatingTaskID();
                return true;
        }

        private void updateFloatingTaskID() {
                int count = 0;
                int timedTasksSize = this.getTaskSize();
                int floatingTaskSize = this.getFloatingTaskSize();
                while (count < floatingTaskSize) {
                        allFloatingTasks.get(count).setTaskID(timedTasksSize + count);
                        count = count + 1;
                }
                return;
        }

        public FloatingTask getFloatingTask(int index) {
                return allFloatingTasks.get(index - this.getTaskSize());
        }

        public boolean deleteFloatingTask(int index) {
                allFloatingTasks.remove(index - this.getTaskSize());
                this.updateFloatingTaskID();
                return true;
        }
        
        public LinkedList<Task> getTimedList () {
                return allTasks;
        }
        
        public LinkedList<FloatingTask> getFloatingList () {
                return allFloatingTasks;
        }

}