package io;

import interpreter.CommandType;
import interpreter.Interpreter;
import interpreter.ParameterType;
import interpreter.Parameters;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;

import controller.Controller;

//@author A0105912N
public class Importer {
        
    public final static Logger logger = Controller.getLogger();
    
    private static final String MESSAGE_COULD_NOT_READ_FILE = "Could not read ICS file";
    private static final String MESSAGE_COULD_NOT_BUILD_ICS = "Could not build ics calendar";
    private static final String INFO_IMPORT_FROM_COMMAND_LINE = "Import intiated from command line input";
    private static final String INFO_IMPORT_FROM_GUI = "Impor initated from GUI file finder interface";
    
    private static final int PRIORITY_MED_REF = 2;
    private static final int PRIORITY_HIGH_REF = 1;
    
    private static final String PRIORITY_LOW = "Low";
    private static final String PRIORITY_MEDIUM = "Med";
    private static final String PRIORITY_HIGH = "High";
    
    private static final String DELIMETER_SPACED = " -";
    private static final String SINGLE_SPACE = " ";

    private static final String IDENTIFIER_LOCATION = "LOCATION:";
    private static final String IDENTIFIER_SUMMARY = "SUMMARY:";

    private static final String DEFAULT_DESCRIPTION_STRING = "No Description";
    private static final String DEFAULT_EMPTY = "";

    private static final String CONSTANT_VALUE_DATE = "VALUE=DATE";
    private static final String CONSTANT_NEW_LINE_REF = "\n";
    
    private static final String FORMAT_DATE_AND_TIME = "dd MMM yyyy HH:mm";

    private Integer priority;
    private java.util.Calendar startTime;
    private java.util.Calendar endTime;
    private java.util.Calendar remindTime;
    private java.util.Calendar recurEndTime;
    private Boolean isTaskDone;
    private Boolean isThereReminder;
    private Boolean isAllDayEvent;
    private String description;
    private String location;

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat(FORMAT_DATE_AND_TIME);

    private static Controller controller;

    public Importer(String filePath) {
	logger.log(Level.INFO, INFO_IMPORT_FROM_COMMAND_LINE);
	
	controller = new Controller();
	importIcs(filePath);
    }

    public Importer(String filePath, Controller controller) {
	logger.log(Level.INFO, INFO_IMPORT_FROM_GUI);
	
	Importer.controller = controller;
	importIcs(filePath);	
    }

    private boolean isEventAllDay(VEvent event) {
	return event.getStartDate().toString().indexOf(CONSTANT_VALUE_DATE) != -1;
    }

    private void clearAllPropertyVars() {
	priority = null;
	startTime = null;
	endTime = null;
	remindTime = null;
	recurEndTime = null;
	isTaskDone = null;
	isThereReminder = null;
	isAllDayEvent = null;
	description = null;
	location = null;
    }

    private void importIcs(String filePath) {
	Calendar calendar = initializeCalParser(filePath);
	parseAndSaveItems(calendar);
    }

    public void parseAndSaveItems(Calendar calendar) {
	for (@SuppressWarnings("rawtypes")
	Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
	    VEvent newTask = (VEvent) i.next();
	    readTaskProperties(newTask);

	    VAlarm newReminder = (VAlarm) newTask.getAlarms().getComponent(Component.VALARM);
	    readReminderProperties(newReminder);	  	    

	    saveItem();
	}
    }

    public void saveItem() {

	Interpreter interpreter = new Interpreter();

	String description = DEFAULT_DESCRIPTION_STRING;
	String startTime = DEFAULT_EMPTY;
	String endTime = DEFAULT_EMPTY;
	String remindTime = DEFAULT_EMPTY;
	String location = DEFAULT_EMPTY;
	String priority = DEFAULT_EMPTY;

	if (this.description != null) {
	    description = SINGLE_SPACE + this.description.replaceAll(IDENTIFIER_SUMMARY, DEFAULT_EMPTY) + SINGLE_SPACE;
	}

	// Time properties: 
	if (this.endTime == null && this.startTime != null) {
	    endTime = DELIMETER_SPACED + interpreter.getDefaultParaSyn(ParameterType.END_TIME) + SINGLE_SPACE + dateFormatter.format(this.startTime.getTime());
	} else if (this.endTime != null && this.startTime == null) {
	    endTime = DELIMETER_SPACED + interpreter.getDefaultParaSyn(ParameterType.END_TIME) + SINGLE_SPACE + dateFormatter.format(this.endTime.getTime());
	} else if (this.endTime != null && this.startTime != null) {
	    startTime = DELIMETER_SPACED + interpreter.getDefaultParaSyn(ParameterType.START_TIME) + SINGLE_SPACE + dateFormatter.format(this.startTime.getTime());
	    endTime = DELIMETER_SPACED + interpreter.getDefaultParaSyn(ParameterType.END_TIME) + SINGLE_SPACE + dateFormatter.format(this.endTime.getTime());
	}

	// Other Properties:
	if (this.remindTime != null) {
	    remindTime = DELIMETER_SPACED + interpreter.getDefaultParaSyn(ParameterType.REMINDER_TIME) + SINGLE_SPACE + dateFormatter.format(this.remindTime.getTime());
	}

	if (this.location != null) {
	    location = DELIMETER_SPACED + interpreter.getDefaultParaSyn(ParameterType.LOCATION) + SINGLE_SPACE + this.location.replaceAll(IDENTIFIER_LOCATION,DEFAULT_EMPTY);
	}

	if (this.priority != null) {
	    priority = DELIMETER_SPACED + interpreter.getDefaultParaSyn(ParameterType.PRIORITY) + SINGLE_SPACE + decodePriority();
	}

	controller.executeCommands(interpreter.getDefaultCommandSyn(CommandType.ADD) + SINGLE_SPACE + description + endTime + startTime + remindTime + location + priority);	
    }

    private String decodePriority() {
	if (priority == PRIORITY_HIGH_REF) {
	    return PRIORITY_HIGH;
	} else if (priority == PRIORITY_MED_REF) {
	    return PRIORITY_MEDIUM;
	} else {
	    return PRIORITY_LOW;
	}

    }

    public void readReminderProperties(VAlarm newReminder) {
	if (newReminder != null) {
	    isThereReminder = true;
	    remindTime = Parameters.dateToCal(newReminder.getTrigger().getDateTime());
	} else {
	    isThereReminder = false;
	}
    }

    public void readTaskProperties(VEvent newTask) {
	Property summaryProperty = newTask.getProperty(Property.SUMMARY);
	Property locationProperty = newTask.getProperty(Property.LOCATION);
	Property priorityProperty = newTask.getProperty(Property.PRIORITY);
	Property startTimeProperty = newTask.getProperty(Property.DTSTART);
	Property endTimeProperty = newTask.getProperty(Property.DTEND);
	Property isTaskDoneProperty = newTask.getProperty(Property.COMPLETED);

	clearAllPropertyVars();


	readTimeProperties(newTask, startTimeProperty, endTimeProperty);

	readOtherProperties(summaryProperty, locationProperty,
		priorityProperty, isTaskDoneProperty);


	isAllDayEvent = isEventAllDay(newTask);
    }

    public void readOtherProperties(Property summaryProperty,
	    Property locationProperty, Property priorityProperty,
	    Property isTaskDoneProperty) {
	if (summaryProperty != null) {
	    description = summaryProperty.toString().replace(CONSTANT_NEW_LINE_REF, DEFAULT_EMPTY); // remove new line from string
	}

	if (locationProperty != null) {
	    location = locationProperty.toString().replace(CONSTANT_NEW_LINE_REF, DEFAULT_EMPTY);
	}

	if (priorityProperty != null) {
	    priority = Integer.parseInt(priorityProperty.toString().substring(9,10)); // 9th character contains the actual priority num reference according to ICS file standards
	}

	if (isTaskDoneProperty != null) {
	    isTaskDone = true;
	} else {
	    isTaskDone = false;
	}
    }

    public void readTimeProperties(VEvent newTask, Property startTimeProperty,
	    Property endTimeProperty) {
	if (startTimeProperty != null) {
	    startTime = Parameters.dateToCal(new DateTime(newTask.getStartDate().getDate()));
	}

	if (endTimeProperty != null) {
	    endTime = Parameters.dateToCal(new DateTime(newTask.getEndDate().getDate()));
	}
    }

    public Calendar initializeCalParser(String filePath) {
	FileInputStream fin = null;

	try {
	    fin = new FileInputStream(filePath);
	} catch (FileNotFoundException e) {
	    logger.log(Level.SEVERE, MESSAGE_COULD_NOT_READ_FILE + e.getStackTrace());
	}

	CalendarBuilder builder = new CalendarBuilder();

	Calendar calendar = null;
	try {
	    calendar = builder.build(fin);
	} catch (IOException | ParserException e) {
	    logger.log(Level.SEVERE, MESSAGE_COULD_NOT_BUILD_ICS + e.getStackTrace());
	}
	return calendar;
    }


}
