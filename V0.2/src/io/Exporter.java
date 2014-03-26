package io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.security.InvalidParameterException;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Date;
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

import storage.AllTasks;
import storage.Reminder;
import storage.Task;

public class Exporter {
    private static final String EXPORT_FILE_NAME = "TASCA_Export.ics";
    private static final String ERROR_ALL_FLOATING_TASKS = ".ics files are not used for storing ONLY floating tasks";
    
    private static final int EMPTY_PRIORITY_REF = 0;
    private static final int INVALID_PRIORITY_REF = -1;
    
    private static final int PRIORITY_HIGH = 1;
    private static final int PRIORITY_MEDIUM = 2;
    private static final int PRIORITY_LOW = 3;

    public Exporter(String savePath) {
	exportToIcs(savePath);
    }
    
    private static VEvent extractCalEvent(java.util.Calendar startTime, String description) {
	// initialise as an all-day event..
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
	    ug = new UidGenerator("1");
	} catch (SocketException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	newEvent.getProperties().add(ug.generateUid());
    }
    
    private static VEvent addOtherProperties(VEvent newEvent, Task newTask, AllTasks allTasks)
    {
	if (newTask.getLocation() != null) {
	    newEvent.getProperties().add(new Location(newTask.getLocation()));
	}
	
	if (newTask.getPriority() != INVALID_PRIORITY_REF || newTask.getPriority() != EMPTY_PRIORITY_REF) {
	    int priority = newTask.getPriority();
	    
	    if (priority == PRIORITY_HIGH) {
		newEvent.getProperties().add(new Priority(1));
	    } else if (priority == PRIORITY_MEDIUM) {
		newEvent.getProperties().add(new Priority(2));
	    } else if (priority == PRIORITY_LOW) {
		newEvent.getProperties().add(new Priority(3));
	    }
	}
	
	if (newTask.getIsThereReminder()) {
	    Reminder reminderTime = allTasks.getReminder(allTasks.searchForCorrespondingReminder(newTask));
	    
	    // Reminder properties: CURRENTLY HANDLES ONLY 1 REMINDER. Waiting for recurring reminders support
	    VAlarm reminder = new VAlarm(new DateTime(reminderTime.getReminderTime().getTime()));
	    reminder.getProperties().add(Action.DISPLAY);
	    reminder.getProperties().add(new Description(newTask.getTaskTitle()));
	    
	    newEvent.getAlarms().add(reminder);
	}
	
	return newEvent;
    }
    
    private static void exportToIcs(String savePath) throws InvalidParameterException {
	net.fortuna.ical4j.model.Calendar exportCal = initializeCalExporter();
	
	// Initialize task list:
	AllTasks allTasks = new AllTasks();
	try {
	    allTasks.loadData();
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	
	int timedTaskCounter = 0;
	
	timedTaskCounter = calToEvent(exportCal, allTasks, timedTaskCounter);
	
	if (timedTaskCounter == 0) {
	    throw new InvalidParameterException(ERROR_ALL_FLOATING_TASKS);
	}
	
	String outputPath;
	if (savePath == null) {
	    outputPath = EXPORT_FILE_NAME;
	} else {
	    outputPath = savePath + "/" + EXPORT_FILE_NAME; // Save inside the specified folder
	}

	FileOutputStream fout = null;
	try {
	    fout = new FileOutputStream(outputPath);
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	CalendarOutputter outputter = new CalendarOutputter();
	
	try {
	    outputter.output(exportCal, fout);
	} catch (IOException | ValidationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static int calToEvent(net.fortuna.ical4j.model.Calendar exportCal,
	    AllTasks allTasks, int timedTaskCounter) {
	for (int i=0; i<allTasks.getTaskSize(); i++) {
	    Task newTask = allTasks.getTask(i);
	    VEvent newEvent;
	    
	    if (newTask.getStartTime() != null && newTask.getEndTime() != null) { // Check if it's a floating task
		if (newTask.getIsAllDayEvent()) {
		    newEvent = extractCalEvent(newTask.getStartTime(), newTask.getTaskTitle());
		}

		newEvent = extractCalEvent(newTask.getStartTime(), newTask.getEndTime(), newTask.getTaskTitle());
		
		newEvent = addOtherProperties(newEvent, newTask, allTasks);
		
		timedTaskCounter++;
		exportCal.getComponents().add(newEvent);	  
	    }
	      
	}
	return timedTaskCounter;
    }

    private static net.fortuna.ical4j.model.Calendar initializeCalExporter() {
	net.fortuna.ical4j.model.Calendar ical4j = new net.fortuna.ical4j.model.Calendar();
	ical4j.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
	ical4j.getProperties().add(Version.VERSION_2_0);
	ical4j.getProperties().add(CalScale.GREGORIAN);
	return ical4j;
    }
    
}