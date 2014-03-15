package ics;

import interpreter.Parameters;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;

public class Importer {

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
    
    
    public Importer() {
	
    }
    
    private boolean isEventAllDay(VEvent event) {
	return event.getStartDate().toString().indexOf("VALUE=DATE") != -1;
    }

    private void clearAllVar() {
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
    
    public void importIcs(String filePath) {
	Calendar calendar = initializeCalParser(filePath);
	
	for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
	    VEvent newTask = (VEvent) i.next();
	    readTaskProperties(newTask);
	    
    	    VAlarm newReminder = (VAlarm) newTask.getAlarms().getComponent(Component.VALARM);
    	    readReminderProperties(newReminder);	  	    
    	    
    	    // TODO: SAVE using Narin's new task feature
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
	
	clearAllVar();
		    
	
	if (summaryProperty != null) {
		description = summaryProperty.toString().replace("\n", ""); // remove new line from string
	}
	
	if (locationProperty != null) {
		location = locationProperty.toString().replace("\n", "");
	}
	
	if (priorityProperty != null) {
		priority = Integer.parseInt(priorityProperty.toString().substring(9,10)); // 9th character contains the actual priority num reference
	}
	
	if (startTimeProperty != null) {
	startTime = Parameters.dateToCal(new DateTime(newTask.getStartDate().getDate()));
	}
	
	if (endTimeProperty != null) {
	endTime = Parameters.dateToCal(new DateTime(newTask.getEndDate().getDate()));
	}
	
	
	if (isTaskDoneProperty != null) {
	isTaskDone = true;
	} else {
	isTaskDone = false;
	}
	
	isAllDayEvent = isEventAllDay(newTask);
    }

    public Calendar initializeCalParser(String filePath) {
	FileInputStream fin = null;
	
	try {
	    fin = new FileInputStream(filePath);
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	CalendarBuilder builder = new CalendarBuilder();

	Calendar calendar = null;
	try {
	    calendar = builder.build(fin);
	} catch (IOException | ParserException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return calendar;
    }
    
    
}
