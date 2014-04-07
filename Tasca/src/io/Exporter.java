package io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.security.InvalidParameterException;
import java.util.LinkedList;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import storage.Reminder;
import storage.Task;
import controller.Controller;

//@author A0105912N
public class Exporter {

    private static final String PROPERTY_PROD_ID = "-//Ben Fortuna//iCal4j 1.0//EN";
    private static final String FILEPATH_BACKSLASH = "/";
    private static final String EXCEPTION_LOCATION_NOT_SPECIFIED = "NIL";
    private static final String CONSTANT_UID_REF = "1";
    private static final String ERROR_ALL_FLOATING_TASKS = ".ics files are not used for storing ONLY floating tasks";

    private static String exportFileName = "TASCA_Export.ics";

    private static final int EMPTY_PRIORITY_REF = 0;
    private static final int EMPTY_LIST = 0;
    private static final int INVALID_PRIORITY_REF = -1;

    private static final int PRIORITY_HIGH = 1;
    private static final int PRIORITY_MEDIUM = 2;
    private static final int PRIORITY_LOW = 3;

    private static Controller controller;

    /*
     * For command line input
     */

    public Exporter(String savePath) {
	exportFileName = "TASCA_Export.ics";
	controller = new Controller();
	exportToIcs(savePath);
    }

    /*
     *  To be used by the GUI save dialog
     */

    public Exporter(String savePath, String fileName, Controller controller) {
	exportFileName = fileName + ".ics";
	Exporter.controller = controller;
	exportToIcs(savePath);
    }

    private static VEvent extractCalEvent(java.util.Calendar startTime, String description) {
	// initialise as an all-day event
	VEvent newEvent = new VEvent(new DateTime(startTime.getTime()), description);

	generateUid(newEvent);
	return newEvent;
    }

    private static VEvent extractCalEvent(java.util.Calendar startTime, java.util.Calendar endTime, String description) {
	// initialise non-all-day event
	VEvent newEvent = new VEvent(new DateTime(startTime.getTime()), new DateTime(endTime.getTime()), description);

	generateUid(newEvent);
	return newEvent;
    }

    public static void generateUid(VEvent newEvent) {
	UidGenerator ug = null;
	try {
	    ug = new UidGenerator(CONSTANT_UID_REF);
	} catch (SocketException e1) {
	    e1.printStackTrace();
	}
	newEvent.getProperties().add(ug.generateUid());
    }

    private static VEvent addOtherProperties(VEvent newEvent, Task newTask, java.util.Calendar reminderTime)
    {
	if (!newTask.getLocation().equals(EXCEPTION_LOCATION_NOT_SPECIFIED)) {
	    newEvent.getProperties().add(new Location(newTask.getLocation()));
	}

	if (newTask.getPriority() != INVALID_PRIORITY_REF || newTask.getPriority() != EMPTY_PRIORITY_REF) {
	    extractAndParsePriority(newEvent, newTask);
	}

	if (newTask.getIsThereReminder()) {
	    extractAndParseReminder(newEvent, newTask, reminderTime);
	}

	return newEvent;
    }

    public static void extractAndParseReminder(VEvent newEvent, Task newTask,
	    java.util.Calendar reminderTime) {
	VAlarm reminder = new VAlarm(new DateTime(reminderTime.getTime()));
	reminder.getProperties().add(Action.DISPLAY);
	reminder.getProperties().add(new Description(newTask.getTaskTitle()));

	newEvent.getAlarms().add(reminder);
    }

    public static void extractAndParsePriority(VEvent newEvent, Task newTask) {
	int priority = newTask.getPriority();

	if (priority == PRIORITY_HIGH) {
	    newEvent.getProperties().add(new Priority(PRIORITY_HIGH));
	} else if (priority == PRIORITY_MEDIUM) {
	    newEvent.getProperties().add(new Priority(PRIORITY_MEDIUM));
	} else if (priority == PRIORITY_LOW) {
	    newEvent.getProperties().add(new Priority(PRIORITY_LOW));
	}
    }

    private static void exportToIcs(String savePath) throws InvalidParameterException {
	net.fortuna.ical4j.model.Calendar exportCal = readAndParseCurrentState();
	writeIcsFile(savePath, exportCal);
    }

    public static net.fortuna.ical4j.model.Calendar readAndParseCurrentState() {
	net.fortuna.ical4j.model.Calendar exportCal = initializeCalExporter();
	LinkedList<Reminder> timedTask = controller.getCurrentSystemState().getTimedList();

	int noOfTimedTasks = EMPTY_LIST;

	noOfTimedTasks = calToEvent(exportCal, timedTask, noOfTimedTasks);

	checkIfAllFloatingTasks(noOfTimedTasks);

	return exportCal;
    }

    public static void checkIfAllFloatingTasks(int timedTaskCounter) {
	if (timedTaskCounter == EMPTY_LIST) {
	    throw new InvalidParameterException(ERROR_ALL_FLOATING_TASKS);
	}
    }

    public static void writeIcsFile(String savePath,
	    net.fortuna.ical4j.model.Calendar exportCal) {
	String outputPath;
	if (savePath == null) {
	    outputPath = exportFileName;
	} else {
	    outputPath = savePath + FILEPATH_BACKSLASH + exportFileName; 
	}

	FileOutputStream fout = null;
	try {
	    fout = new FileOutputStream(outputPath);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

	CalendarOutputter outputter = new CalendarOutputter();

	try {
	    outputter.output(exportCal, fout);
	} catch (IOException | ValidationException e) {
	    e.printStackTrace();
	}
    }

    public static int calToEvent(net.fortuna.ical4j.model.Calendar exportCal,
	    LinkedList<Reminder> allTimedTasks, int noOfTimedTasks) {
	
	for (int i=0; i<allTimedTasks.size(); i++) {
	    noOfTimedTasks = addNewEvent(exportCal, allTimedTasks, noOfTimedTasks, i);	  
	}
	
	return noOfTimedTasks;
    }

    public static int addNewEvent(net.fortuna.ical4j.model.Calendar exportCal,
	    LinkedList<Reminder> allTimedTasks, int noOfTimedTasks, int i) {
	Task newTask = allTimedTasks.get(i).getTask();
	VEvent newEvent;

	assert (newTask.getStartTime() != null && newTask.getEndTime() != null);

	newEvent = addTimeProperties(newTask);
	newEvent = addOtherProperties(newEvent, newTask, allTimedTasks.get(i).getReminderTime());

	noOfTimedTasks++;
	exportCal.getComponents().add(newEvent);
	
	return noOfTimedTasks;
    }

    public static VEvent addTimeProperties(Task newTask) {
	VEvent newEvent;
	if (newTask.getIsAllDayEvent()) {
	    newEvent = extractCalEvent(newTask.getStartTime(), newTask.getTaskTitle());
	}

	newEvent = extractCalEvent(newTask.getStartTime(), newTask.getEndTime(), newTask.getTaskTitle());
	return newEvent;
    }

    private static net.fortuna.ical4j.model.Calendar initializeCalExporter() {
	net.fortuna.ical4j.model.Calendar ical4j = new net.fortuna.ical4j.model.Calendar();
	ical4j.getProperties().add(new ProdId(PROPERTY_PROD_ID));
	ical4j.getProperties().add(Version.VERSION_2_0);
	ical4j.getProperties().add(CalScale.GREGORIAN);
	return ical4j;
    }

}
