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

	private String MESSAGE_FILE_NOT_FOUND = "The system files could not be loaded.";
	private String MESSAGE_INVALID_COMMAND = "The command given was invalid.";

	private AllTasks allTasks;
	private UndoRedo undoRedo = UndoRedo.getInstance();
	private Scanner myScanner = new Scanner(System.in);

	private Handler handler;
	private Logger logger = Logger.getLogger("Controller");

	public Controller() {
		initialiseTasks();
		Logic.initStorage(allTasks);
		try {
			handler = new FileHandler("log.txt");
		} catch (Exception e) {

		}
		SimpleFormatter formatter = new SimpleFormatter();
		handler.setFormatter(formatter);
		logger.addHandler(handler);
		return;
	}

	private void initialiseTasks() {
		allTasks = new AllTasks();
		try {
			allTasks.loadData();
		} catch (FileNotFoundException e) {
			System.out.printf("%s\n", MESSAGE_FILE_NOT_FOUND);
		}
	}

	public boolean executeCommands() {
		Interpreter newInt = new Interpreter();
		String input;

		input = myScanner.nextLine();

		try {
			newInt.processUserInput(input);
			Command command = newInt.getCommandAndPara();
			assert (command.getCommandType() != null);
			logger.log(Level.SEVERE, "Entering executeLogic",
					command.getCommandType());
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

	private boolean executeLogic(Command command) {
		boolean isThereReminder = true, quit = false;
		Calendar startTime = Calendar.getInstance();

		if (!command.getCommandType().name().equals("MODIFY")) {
			isThereReminder = checkForNulls(command, isThereReminder, startTime);
		} else {
			startTime =null;
			if (command.getParameters().getRemindTime() == null){
				isThereReminder = false;
			}
		}
		
			

		switch (command.getCommandType().name()) {

		case "ADD":
			undoRedo.addUndo(allTasks);
			execute_add(command, isThereReminder, startTime);
			break;

		case "DELETE":
			undoRedo.addUndo(allTasks);
			execute_delete(command);
			break;

		case "MODIFY":
			undoRedo.addUndo(allTasks);
			execute_modify(command, isThereReminder, startTime);
			break;

		case "DISPLAY_TODAY":
			Logic.displayToday();
			break;

		case "MARK":
			undoRedo.addUndo(allTasks);
			Logic.taskIsDone(Integer.parseInt(command.getParameters()
					.getTaskId()));
			break;

		case "SEARCH":
			Logic.searchTask(command.getParameters().getDescription());
			break;

		case "DISPLAY_TOMORROW":
			Logic.displayTomorrow();
			break;

		case "DISPLAY_WEEK":
			Logic.displayTasksAtPeriod(command.getParameters().getStartTime()
					.getTime(), command.getParameters().getEndTime().getTime());
			break;

		case "DISPLAY_ALL":
			Logic.displayAll();
			break;

		case "DISPLAY_IN_TIME":
			Logic.displayTasksAtPeriod(command.getParameters().getStartTime()
					.getTime(), command.getParameters().getEndTime().getTime());
			break;

		case "QUIT":
			executeQuit();
			quit = true;
			break;

		case "DELETE_ALL_COMPLETED":
			undoRedo.addUndo(allTasks);
			Logic.deleteAlldone();
			break;

		case "UNDO":
			if (!undoRedo.isUndoEmpty()) {
				allTasks = undoRedo.undo(allTasks);
				Logic.initStorage(allTasks);
			}
			break;

		case "REDO":
			if (!undoRedo.isRedoEmpty()) {
				allTasks = undoRedo.redo(allTasks);
				Logic.initStorage(allTasks);
			}

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
			Logic.clearAll();
			break;

		default:
			System.out.printf("%s\n", MESSAGE_INVALID_COMMAND);
			break;

		}

		return quit;
	}


	private boolean checkForNulls(Command command, boolean isThereReminder,
			Calendar startTime) {
		if (command.getParameters().getRemindTime() == null) {
			isThereReminder = false;
		}
		if (command.getParameters().getLocation() == null) {
			command.getParameters().setLocation("NIL");
		}
		if (command.getParameters().getPriority() == "null") {
			command.setPriority("LOW");
		}
		if (command.getParameters().getFolder() == null) {
			command.setFolder("default");
		}
		if (command.getParameters().getStartTime() == null
				&& command.getParameters().getEndTime() != null) {
			startTime = Calendar.getInstance();
		} else { 
			if(command.getParameters().getStartTime() != null
				&& command.getParameters().getEndTime() != null){
				
			startTime = Calendar.getInstance();
			startTime.setTime(command.getParameters().getStartTime().getTime());
			}
		}
		return isThereReminder;
	}

	private void execute_add(Command command, boolean isThereReminder,
			Calendar startTime) {
		if (isThereReminder) {
			
			Logic.addTask(Integer.parseInt(command.getParameters()
					.getPriority()), startTime.getTime(), command
					.getParameters().getEndTime().getTime(), isThereReminder,
					false, false, command.getParameters().getDescription(),
					command.getParameters().getLocation(), command
							.getParameters().getRemindTime().getTime());
		} else {
			Logic.addTask(Integer.parseInt(command.getParameters()
					.getPriority()), startTime.getTime(), command
					.getParameters().getEndTime().getTime(), isThereReminder,
					false, false, command.getParameters().getDescription(),
					command.getParameters().getLocation(), null);
		}
	}

	private void execute_modify(Command command, boolean isThereReminder,
			Calendar startTime) {
		try {
			Logic.updateTask(command.getParameters()
					.getTaskId(), command.getParameters()
					.getPriority(), startTime, command
					.getParameters().getEndTime(), isThereReminder,
					command.getParameters().getDescription(), command
							.getParameters().getLocation(), command
							.getParameters().getRemindTime().getTime());
		} catch (Exception e) {
			Logic.updateTask(command.getParameters()
					.getTaskId(), command.getParameters()
					.getPriority(), startTime, command
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
			}
		} catch (Exception e) {
			Logic.deleteSearch(command.getParameters().getDescription(),
					myScanner);
		}
	}

}
