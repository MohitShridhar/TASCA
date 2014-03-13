package controller;

import interpreter.*;

import java.io.FileNotFoundException;
//import java.util.Calendar;
//
//import com.clutch.dates.StringToTime;


import java.util.*;

import storage.AllTasks;
import storage.UndoRedo;
import storage.UndoRedoNode;

import logic.Logic;


public class Controller {
	private String MESSAGE_FILE_NOT_FOUND = "The system files could not be loaded.";
	private String MESSAGE_INVALID_COMMAND = "The command given was invalid.";

	private AllTasks allTasks;
	private UndoRedo undoRedo = new UndoRedo();
	private Scanner myScanner = new Scanner(System.in);

	public Controller() {
		initialiseTasks();
		Logic.initStorage(allTasks, undoRedo);
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

		try {

			if (command.getParameters().getRemindTime() == null) {
				isThereReminder = false;
			}
		} catch (NullPointerException e) {
			isThereReminder = false;
		}

		switch (command.getCommandType().name()) {

		case "ADD":
			try {
				if (command.getParameters().getRemindTime() != null) {
					Logic.addTask(Integer.parseInt(command.getParameters()
							.getPriority()), command.getParameters()
							.getStartTime().getTime(), command
							.getParameters().getEndTime().getTime(),
							isThereReminder, false, false, command
									.getParameters().getDescription(), command
									.getParameters().getLocation(), command
									.getParameters().getRemindTime()
									.getTime());
				} else {
					Logic.addTask(Integer.parseInt(command.getParameters()
							.getPriority()), command.getParameters()
							.getStartTime().getTime(), command
							.getParameters().getEndTime().getTime(),
							isThereReminder, false, false, command
									.getParameters().getDescription(), command
									.getParameters().getLocation(), null);
				}
			} catch (NullPointerException e) {
				System.out.println("Exception - " + e + "\n");
			}
			break;

		case "DELETE":
			try {
				if (command.getParameters().getTaskId() != null) {
					Logic.deleteTask(Integer.parseInt(command.getParameters()
							.getTaskId()));
				}
			} catch (Exception e) {
				Logic.deleteSearch(command.getParameters().getDescription(),
						myScanner);
			}
			break;

		case "MODIFY":
			try{
			Logic.updateTask(
					Integer.parseInt(command.getParameters().getTaskId()),
					Integer.parseInt(command.getParameters().getPriority()),
					command.getParameters().getStartTime().getTime(),
					command.getParameters().getEndTime().getTime(),
					isThereReminder, command.getParameters().getDescription(),
					command.getParameters().getLocation(), command
							.getParameters().getRemindTime().getTime());
			} catch (Exception e){
				Logic.updateTask(
						Integer.parseInt(command.getParameters().getTaskId()),
						Integer.parseInt(command.getParameters().getPriority()),
						command.getParameters().getStartTime().getTime(),
						command.getParameters().getEndTime().getTime(),
						isThereReminder, command.getParameters().getDescription(),
						command.getParameters().getLocation(), null);
			}
			break;

		case "DISPLAY_TODAY":
			Logic.displayToday();
			break;

		case "MARK":
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
					.getTime(), command.getParameters().getEndTime()
					.getTime());
			break;

		case "DISPLAY_ALL":
			Logic.displayAll();
			break;

		case "DISPLAY_IN_TIME":
			Logic.displayTasksAtPeriod(command.getParameters().getStartTime().getTime(),
					command.getParameters().getEndTime().getTime());
			break;

		case "QUIT":
			executeQuit();
			quit = true;
			break;

		case "DELETE_ALL_COMPLETED":
			Logic.deleteAlldone();
			break;

		case "UNDO":
			UndoRedoNode node = undoRedo.undo();
			if(node!= null){
				if (node.getCommand().equals("add")){
					Logic.deleteTaskUndoRedo(node.getNewTask().getTaskID());
				} else {
					if (node.getCommand().equals("delete")){
						Logic.addTaskUndo(node);
					}else {
						Logic.deleteTaskUndoRedo(node.getNewTask()
								.getTaskID());
						Logic.addTaskUndo(node);
					}
				}
			}
			break;

		case "REDO":
			UndoRedoNode node2 = undoRedo.redo();
			if(node2!= null){
				if (node2.getCommand().equals("add")){
					Logic.addTaskRedo(node2);
				} else {
					if (node2.getCommand().equals("delete")){
						Logic.deleteTaskUndoRedo(node2.getOldTask().getTaskID());
					}else {
						Logic.deleteTaskUndoRedo(node2.getOldTask()
								.getTaskID());
						Logic.addTaskRedo(node2);
					}
				}
			}

			break;

		default:
			System.out.printf("%s\n", MESSAGE_INVALID_COMMAND);
			break;

		}

		return quit;
	}

}
