/**
 * Controller:
 * 
 * @author Narinderpal Singh Dhillon
 * @Matric A0097416X
 */
package controller;

import interpreter.*;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.*;

import logic.Logic;
import storage.AllTasks;
import storage.UndoRedo;

public class Controller {
	private static final String MESSAGE_EXPORT_SUCCESSFUL = "All events were successfully exported to ";
	private static final String MESSAGE_IMPORT_SUCCESSFUL = "All events were successfully imported from ";
	private static final String MESSAGE_TASK_ADDED = "The event has been added";
	private static final String MESSAGE_TASK_DELETED = "The event has been deleted.";
	private static final String MESSAGE_TASK_MODIFIED = "The event has been modified";
	private static final String MESSAGE_DISPLAY_TODAY = "All events for today are being displayed";
	private static final String MESSAGE_DISPLAY_TOMORROW = "All events for tomorrow are being displayed";
	private static final String MESSAGE_DISPLAY_WEEK = "All events for the week are being displayed";
	private static final String MESSAGE_DISPLAY_FLOATING = "All events without time are being displayed";
	private static final String MESSAGE_DISPLAY_PERIOD = "All events for the given period are being displayed";
	private static final String MESSAGE_DISPLAY_ALL = "All events are being displayed";
	private static final String MESSAGE_SEARCH = "Search completed";
	private static final String MESSAGE_PROVIDE_TASK_ID = "Please provide event ID to be deleted";
	private static final String MESSAGE_MARK = "The task is now marked as done";
	private static final String MESSAGE_UNMARK = "The task is now marked as not done";
	private static final String MESSAGE_DELETE_COMPLETED = "All completed tasks are now deleted";
	private static final String MESSAGE_UNDO = "Undo successful";
	private static final String MESSAGE_REDO = "Redo successful";
	private static final String MESSAGE_CLEAR = "All events have been cleared";
	private static final String MESSAGE_IMPORT = "Import successful";

	private static final long SYSTEM_TIMER_TASK_PERIOD = 5000;
	private static final int STATUS_ALL = 0;
	private static final int STATUS_TODAY = 1;
	private static final int STATUS_TOMORROW = 2;
	private static final int STATUS_WEEK = 3;
	private static final int STATUS_MONTH = 4;
	private static final int STATUS_PERIOD = 5;
	private static final int STATUS_DISPLAY_FLOAT = 6;

	private String MESSAGE_FILE_NOT_FOUND = "The system files could not be loaded.";
	private String MESSAGE_INVALID_COMMAND = "The command given was invalid.";
	private String MESSAGE_PROVIDE_END_TIME = "Please provide end time for time bounded tasks.";

	private AllTasks allTasks;
	private UndoRedo undoRedo = UndoRedo.getInstance();
	private Scanner myScanner = new Scanner(System.in);
	private SystemMessage systemMessage = new SystemMessage();

	public static Handler handler;
	public static Logger logger = Logger.getLogger("TASCA Log");

	private Timer systemTimer;
	private ReminderTimerTask reminderTimerTask;

	public Controller() {
		initialiseTasks();
		Logic.initStorage(allTasks);
		Logic.initSystemMessage(systemMessage);
		Interpreter newInt = new Interpreter();
		this.executeCommands(newInt
				.getDefaultCommandSyn(CommandType.DISPLAY_ALL));
		systemMessage.setFloatingList(allTasks.getFloatingList());
		try {
			handler = new FileHandler("Tasca Log.txt");
		} catch (Exception e) {

		}
		SimpleFormatter formatter = new SimpleFormatter();
		handler.setFormatter(formatter);
		logger.addHandler(handler);
		return;
	}

	private void initialiseTasks() {
		allTasks = new AllTasks();
		systemTimer = new Timer(true);
		try {
			allTasks.loadData();
		} catch (FileNotFoundException e) {
			System.out.printf("%s\n", MESSAGE_FILE_NOT_FOUND);
		}
		allTasks.clearAllMissedReminders();
		reminderTimerTask = new ReminderTimerTask(allTasks, this);
		systemTimer.scheduleAtFixedRate(reminderTimerTask,
				SYSTEM_TIMER_TASK_PERIOD, SYSTEM_TIMER_TASK_PERIOD);
		return;
	}

	public String getSystemMessageString() {
		return systemMessage.getSystemMessage();
	}

	public SystemMessage getCurrentSystemState() {
		systemMessage.sortForGUI();
		return systemMessage;
	}

	public boolean executeCommands(String input) {
		Interpreter newInt = new Interpreter();

		try {
			newInt.processUserInput(input);
			Command command = newInt.getCommandAndPara();
			assert (command.getCommandType() != null);
			getLogger().setLevel(Level.SEVERE); // Warnings, Info, Fine etc.
												// will be ignored for
												// debugging. Re-enable in final
												// release
			// getLogger().addHandler(handler);
			return executeLogic(command);
		} catch (IllegalArgumentException eI) {
			System.out.println("Exception - " + eI + "\n");
			return false;
		}

	}

	public boolean executeCommands(String input, String currFolder) {
		Interpreter newInt = new Interpreter();

		try {
			newInt.setCurrentFolder(currFolder);
			newInt.processUserInput(input);
			Command command = newInt.getCommandAndPara();
			assert (command.getCommandType() != null);
			return executeLogic(command);
		} catch (IllegalArgumentException eI) {
			System.out.println("Exception - " + eI + "\n");
			return false;
		}

	}

	private void executeQuit() {
		try {
			allTasks.saveData();
		} catch (FileNotFoundException e) {
			System.out.printf("%s", MESSAGE_FILE_NOT_FOUND);
		}
		return;
	}

	public void mutexAdd(LinkedList<String> input) {
		int total = input.size(), count = 0;
		undoRedo.addUndo(allTasks);
		systemMessage.setSystemMessage(MESSAGE_IMPORT);

		while (count < total) {
			boolean isThereReminder = true;
			Calendar startTime = Calendar.getInstance();
			Interpreter newInt = new Interpreter();

			try {
				newInt.processUserInput(input.get(count));
				Command command = newInt.getCommandAndPara();
				isThereReminder = checkForNulls(command, isThereReminder,
						startTime);

				execute_add(command, isThereReminder, startTime);
			} catch (IllegalArgumentException eI) {
				System.out.println("Exception - " + eI + "\n");
			}
		}
		
		updateDisplayGUI(systemMessage.getDisplayStatus());

	}

	private boolean executeLogic(Command command) {
		boolean isThereReminder = true, quit = false;
		Calendar startTime = Calendar.getInstance();

		if (!command.getCommandType().name().equals("MODIFY")) {
			isThereReminder = checkForNulls(command, isThereReminder, startTime);
		} else {
			startTime = null;
			if (command.getParameters().getRemindTime() == null) {
				isThereReminder = false;
			}
		}

		switch (command.getCommandType().name()) {

		case "ADD":
			undoRedo.addUndo(allTasks);
			systemMessage.setSystemMessage(MESSAGE_TASK_ADDED);
			execute_add(command, isThereReminder, startTime);
			updateDisplayGUI(systemMessage.getDisplayStatus());
			break;

		case "DELETE":
			undoRedo.addUndo(allTasks);
			systemMessage.setSystemMessage(MESSAGE_TASK_DELETED);
			execute_delete(command);
			break;

		case "MODIFY":
			undoRedo.addUndo(allTasks);
			systemMessage.setSystemMessage(MESSAGE_TASK_MODIFIED);
			execute_modify(command, isThereReminder);
			updateDisplayGUI(systemMessage.getDisplayStatus());
			break;

		case "DISPLAY_TODAY":
			Logic.displayToday();
			systemMessage.setSystemMessage(MESSAGE_DISPLAY_TODAY);
			systemMessage.setDisplayStatus(STATUS_TODAY);
			break;

		case "MARK":
			undoRedo.addUndo(allTasks);
			systemMessage.setSystemMessage(MESSAGE_MARK);
			Logic.taskIsDone(Integer.parseInt(command.getParameters()
					.getTaskId()));
			updateDisplayGUI(systemMessage.getDisplayStatus());
			break;

		case "UNMARK":
			undoRedo.addUndo(allTasks);
			systemMessage.setSystemMessage(MESSAGE_UNMARK);
			Logic.taskIsNotDone(Integer.parseInt(command.getParameters()
					.getTaskId()));
			updateDisplayGUI(systemMessage.getDisplayStatus());
			break;

		case "SEARCH":
			systemMessage.setSystemMessage(MESSAGE_SEARCH);
			Logic.searchTask(command.getParameters().getDescription());
			systemMessage.setDisplayStatus(STATUS_PERIOD);
			break;

		case "DISPLAY_TOMORROW":
			systemMessage.setSystemMessage(MESSAGE_DISPLAY_TOMORROW);
			Logic.displayTomorrow();
			systemMessage.setDisplayStatus(STATUS_TOMORROW);
			break;

		case "DISPLAY_WEEK":
			systemMessage.setSystemMessage(MESSAGE_DISPLAY_WEEK);
			Logic.displayWeek();
			systemMessage.setDisplayStatus(STATUS_WEEK);
			break;

		case "DISPLAY_ALL":
			systemMessage.setSystemMessage(MESSAGE_DISPLAY_ALL);
			Logic.displayAll();
			systemMessage.setDisplayStatus(STATUS_ALL);
			break;

		case "DISPLAY_ALL_FLOAT":
			systemMessage.setSystemMessage(MESSAGE_DISPLAY_FLOATING);
			Logic.displayAllFloat();
			systemMessage.setDisplayStatus(STATUS_DISPLAY_FLOAT);
			break;

		case "DISPLAY_IN_TIME":
			systemMessage.setSystemMessage(MESSAGE_DISPLAY_PERIOD);
			Logic.displayTasksAtPeriod(command.getParameters().getStartTime()
					.getTime(), command.getParameters().getEndTime().getTime());
			systemMessage.setDisplayStatus(STATUS_PERIOD);
			break;

		case "QUIT":
			executeQuit();
			quit = true;
			break;

		case "DELETE_ALL_COMPLETED":
			undoRedo.addUndo(allTasks);
			systemMessage.setSystemMessage(MESSAGE_DELETE_COMPLETED);
			Logic.deleteAlldone();
			updateDisplayGUI(systemMessage.getDisplayStatus());
			break;

		case "UNDO":
			systemMessage.setSystemMessage(MESSAGE_UNDO);
			if (!undoRedo.isUndoEmpty()) {
				allTasks = undoRedo.undo(allTasks);
				Logic.initStorage(allTasks);
			}
			updateDisplayGUI(systemMessage.getDisplayStatus());
			break;

		case "REDO":
			systemMessage.setSystemMessage(MESSAGE_REDO);
			if (!undoRedo.isRedoEmpty()) {
				allTasks = undoRedo.redo(allTasks);
				Logic.initStorage(allTasks);
			}
			updateDisplayGUI(systemMessage.getDisplayStatus());
			break;

		case "EXPORT":
			String savePath = command.getParameters().getDescription();
			Logic.export(savePath);
			System.out.println(MESSAGE_EXPORT_SUCCESSFUL + savePath);
			break;

		case "IMPORT":
			undoRedo.addUndo(allTasks);
			String filePath = command.getParameters().getDescription();
			Logic.importFile(filePath);
			System.out.println(MESSAGE_IMPORT_SUCCESSFUL + filePath);
			break;

		case "CLEAR":
			undoRedo.addUndo(allTasks);

			String typedFolder = new Config().getFolderId(
					command.getParameters().getFolder()).toString();
			if (typedFolder.equals("FOLDER1")) {
				Logic.clearFolder(1);
			}
			if (typedFolder.equals("FOLDER2")) {
				Logic.clearFolder(2);
			}
			if (typedFolder.equals("FOLDER3")) {
				Logic.clearFolder(3);
			}
			if (typedFolder.equals("FOLDER4")) {
				Logic.clearFolder(4);
			}
			if (typedFolder.equals("FOLDER5")) {
				Logic.clearFolder(5);
			}
			if (typedFolder.equals("DEFAULT")) {
				Logic.clearFolder(0);
			}

			systemMessage.setSystemMessage(MESSAGE_CLEAR);
			updateDisplayGUI(systemMessage.getDisplayStatus());
			break;

		default:
			systemMessage.setSystemMessage(MESSAGE_INVALID_COMMAND);
			break;

		}

		System.out.printf("%s\n", systemMessage.getSystemMessage());

		return quit;
	}

	private void execute_modify(Command command, boolean isThereReminder) {
		int folder = -1;
		try {
			String typedFolder = new Config().getFolderId(
					command.getParameters().getFolder()).toString();
			if (typedFolder.equals("FOLDER1")) {
				folder = 1;
			}
			if (typedFolder.equals("FOLDER2")) {
				folder = 2;
			}
			if (typedFolder.equals("FOLDER3")) {
				folder = 3;
			}
			if (typedFolder.equals("FOLDER4")) {
				folder = 4;
			}
			if (typedFolder.equals("FOLDER5")) {
				folder = 5;
			}
			if (typedFolder.equals("DEFAULT")) {
				folder = 0;
			}
		} catch (NullPointerException e) {
			folder = -1;
		}
		try {
			if (command.getParameters().getEndTime() != null) {
				throw new IllegalArgumentException();
			} else {
				Logic.updateFloatingTask(folder, command.getParameters()
						.getTaskId(), command.getParameters().getPriority(),
						command.getParameters().getDescription(), command
								.getParameters().getLocation());
			}
		} catch (Exception e) {
			execute_modifyTimedTask(folder, command, isThereReminder, command
					.getParameters().getStartTime());
		}
	}

	private void execute_add(Command command, boolean isThereReminder,
			Calendar startTime) {
		int priority, folder = -1;

		if (command.getParameters().getPriority() == "null") {
			priority = 0;
		} else {
			priority = Integer.parseInt(command.getParameters().getPriority());
		}
		try {
			String typedFolder = new Config().getFolderId(
					command.getParameters().getFolder()).toString();
			if (typedFolder.equals("FOLDER1")) {
				folder = 1;
			}
			if (typedFolder.equals("FOLDER2")) {
				folder = 2;
			}
			if (typedFolder.equals("FOLDER3")) {
				folder = 3;
			}
			if (typedFolder.equals("FOLDER4")) {
				folder = 4;
			}
			if (typedFolder.equals("FOLDER5")) {
				folder = 5;
			}
			if (typedFolder.equals("DEFAULT")) {
				folder = 0;
			}
		} catch (NullPointerException e) {
			folder = 0;
		}
		if (command.getParameters().getStartTime() == null
				&& command.getParameters().getEndTime() == null) {
			Logic.addFloatingTask(folder, priority, false, command
					.getParameters().getDescription(), command.getParameters()
					.getLocation());
		} else {

			execute_add_timedTask(command, isThereReminder, startTime,
					priority, folder);

		}
	}

	private boolean checkForNulls(Command command, boolean isThereReminder,
			Calendar startTime) {
		if (command.getParameters().getRemindTime() == null) {
			isThereReminder = false;
		}
		if (command.getParameters().getLocation() == null) {
			command.getParameters().setLocation("NIL");
		}

		if (command.getParameters().getFolder() == null) {
			command.setFolder("default");
		}
		if (command.getParameters().getStartTime() == null
				&& command.getParameters().getEndTime() != null) {
			startTime = Calendar.getInstance();
		} else {
			if (command.getParameters().getStartTime() != null
					&& command.getParameters().getEndTime() != null) {

				startTime.setTime(command.getParameters().getStartTime()
						.getTime());
			}
		}
		return isThereReminder;
	}

	private void execute_add_timedTask(Command command,
			boolean isThereReminder, Calendar startTime, int priority,
			int folder) {
		Calendar endTime = Calendar.getInstance();

		if (command.getParameters().getStartTime() != null
				&& command.getParameters().getEndTime() == null) {
			endTime.add(Calendar.DAY_OF_YEAR, 1);
		} else {
			endTime = command.getParameters().getEndTime();
		}
		if (isThereReminder) {

			Logic.addTask(folder, priority, startTime.getTime(), endTime
					.getTime(), isThereReminder, false, false, command
					.getParameters().getDescription(), command.getParameters()
					.getLocation(), command.getParameters().getRemindTime()
					.getTime());
		} else {
			Logic.addTask(folder, priority, startTime.getTime(), endTime
					.getTime(), isThereReminder, false, false, command
					.getParameters().getDescription(), command.getParameters()
					.getLocation(), null);
		}
	}

	private void execute_modifyTimedTask(int folder, Command command,
			boolean isThereReminder, Calendar startTime) {
		try {
			Logic.updateTask(folder, command.getParameters().getTaskId(),
					command.getParameters().getPriority(), startTime, command
							.getParameters().getEndTime(), isThereReminder,
					command.getParameters().getDescription(), command
							.getParameters().getLocation(), command
							.getParameters().getRemindTime().getTime());
		} catch (Exception e) {
			Logic.updateTask(folder, command.getParameters().getTaskId(),
					command.getParameters().getPriority(), startTime, command
							.getParameters().getEndTime(), isThereReminder,
					command.getParameters().getDescription(), command
							.getParameters().getLocation(), null);
		}
	}

	private void execute_delete(Command command) {
		try {
			if (command.getParameters().getTaskId() != null) {
				Logic.deleteTask(Integer.parseInt(command.getParameters()
						.getTaskId()));
				updateDisplayGUI(systemMessage.getDisplayStatus());
			} else {
				systemMessage.setSystemMessage(MESSAGE_SEARCH);
				Logic.searchTask(command.getParameters().getDescription());
				systemMessage.setDisplayStatus(STATUS_PERIOD);

			}
		} catch (Exception e) {
			systemMessage.setSystemMessage(MESSAGE_SEARCH);
			Logic.searchTask(command.getParameters().getDescription());
			systemMessage.setDisplayStatus(STATUS_PERIOD);
		}
	}

	public void updateDisplayGUI(int displayStatus) {
		if (displayStatus == STATUS_ALL) {
			Logic.displayAll();

		}
		if (displayStatus == STATUS_TODAY) {

			Logic.displayToday();
		}
		if (displayStatus == STATUS_TOMORROW) {

			Logic.displayTomorrow();
		}
		if (displayStatus == STATUS_WEEK) {

			Logic.displayWeek();
		}
		if (displayStatus == STATUS_MONTH) {

		}
		if (displayStatus == STATUS_PERIOD) {

			Logic.displayAll();
			systemMessage.setDisplayStatus(STATUS_ALL);
		}
		if (displayStatus == STATUS_DISPLAY_FLOAT) {
			Logic.displayAllFloat();
		}
		return;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		Controller.logger = logger;
	}

	public int getDisplayStatus() {
		return systemMessage.getDisplayStatus();
	}

	public AllTasks getAllTask() {
		return allTasks;
	}

}
