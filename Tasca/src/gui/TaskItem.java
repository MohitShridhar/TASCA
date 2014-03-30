package gui;

import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.Timer;

import org.ocpsoft.prettytime.PrettyTime;

import controller.Controller;

import storage.AllTasks;
import storage.Task;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logic.Logic;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.SimpleDateFormat;


public class TaskItem extends JLayeredPane {
    
    private static final String HTML_SPACING = "&nbsp;&nbsp;&nbsp;";
    private static final int DISPLAY_TIME_UPDATE_PERIOD = 60000; //milliseconds
    private static final String LOCATION_AT_PREFIX = " @ ";
    private static final int HTML_CODE_LENGTH = 20;
    private static final int MAX_DISPLAY_TEXT_LENGTH = 90;
    private static final int INITIAL_SPACE_OFFSET = 82;
    private static final int FINAL_SPACE_OFFSET = 31;
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
    
    public static PrettyTime p = new PrettyTime();
    public static BufferedGraphics graphics = new BufferedGraphics();
    private JLabel background;
    private JLabel checkMark;
    private JLabel reminder;
    private JLabel priority;
    private JLabel text;
    private JLabel dateIcon;
    private JLabel infoIcon;
    private JLabel unchecked;
    private Task taskProperties;
    private JLabel ellipsis;
    private JLabel apparentId;
    private JLabel deleteIcon;
    
    int delay = DISPLAY_TIME_UPDATE_PERIOD; 
    ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            if (!inDateDisplayState) {
            	setTimedDisplayText(description, location, updateInfoTimings());
            }
        }
    };
    private String description;
    private String location;
    private String infoDisplayTime, dateDisplayTime;
    private int realTaskId, guiId;
    private static Controller controller;
    
    private static JTextPane textPane;
    
    private boolean isFloatingTask = false;
    private boolean inDateDisplayState = false;
    
    private int withoutReminderOffset;

    public TaskItem(JTextPane textPane, Controller controller, int guiId) {
	
	super();
	TaskItem.textPane = textPane;
	this.controller = controller;
	this.guiId = guiId;
	
	new Timer(delay, taskPerformer).start(); // Update timings every minute 

	
	this.setSize(888, 40);
	this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	
	checkMark = new JLabel();
	checkMark.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    setCheckMark(false);
		 // TODO: Update that task in Storage: UNMARK Function
		}
	});
	checkMark.setLocation(8 + INITIAL_SPACE_OFFSET, 9);
	checkMark.setSize(23, 23);
	checkMark.setIcon(graphics.checkMark);
	checkMark.setVisible(false);
	this.add(checkMark);
	
	unchecked = new JLabel();
	unchecked.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    setCheckMark(true);
		    Logic.taskIsDone(realTaskId);
		}
	});
	unchecked.setLocation(8 + INITIAL_SPACE_OFFSET, 9);
	unchecked.setSize(23, 23);
	unchecked.setIcon(graphics.unchecked);
	unchecked.setVisible(true);
	this.add(unchecked);
	
	dateIcon = new JLabel();
	dateIcon.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    activateDateState();
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		    showDateInfoIcon();
		}

		@Override
		public void mouseExited(MouseEvent e) {
		    hideDateInfoIcon();
		}
	});
	dateIcon.setSize(25, 25);
	dateIcon.setIcon(graphics.date);
	dateIcon.setVisible(false);
	this.add(dateIcon);
	
	infoIcon = new JLabel();
	infoIcon.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    activateInfoState();
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
		    showDateInfoIcon();
		}

		@Override
		public void mouseExited(MouseEvent e) {
		    hideDateInfoIcon();
		}
	});
	infoIcon.setSize(25, 25);
	infoIcon.setIcon(graphics.info);
	infoIcon.setVisible(false);
	this.add(infoIcon);
	
	reminder = new JLabel();
	reminder.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    reminderShortcut();
		}
	});
	reminder.setLocation(689 + 10 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET, 7);
	reminder.setSize(25, 26);
	reminder.setIcon(graphics.reminderIcon);
	reminder.setVisible(false);
	this.add(reminder);
	
	priority = new JLabel();
	priority.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    cyclePriorities();
		}
	});
	priority.setLocation(666 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET, 7);
	// Must set icon, icon size, and location during use
	priority.setVisible(false);
	this.add(priority);
	
	ellipsis = new JLabel(" ...");
	ellipsis.setForeground(Color.WHITE);
	ellipsis.setFont(new Font("Menlo", Font.PLAIN, 14));
	ellipsis.setBounds(612 + INITIAL_SPACE_OFFSET, 9, 40, 24);
	ellipsis.setVisible(false);
	add(ellipsis);
	
	text = new JLabel("New Task");
	text.setToolTipText("");
	text.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    modifyDescriptionShortcut();
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		    showDateInfoIcon();
		}

		@Override
		public void mouseExited(MouseEvent e) {
		    hideDateInfoIcon();
		}
	});
	text.setForeground(Color.WHITE);
	text.setFont(new Font("Lato", Font.PLAIN, 14));
	text.setLocation(44 + INITIAL_SPACE_OFFSET, 7);
	text.setSize(600, 24);
	text.setText("<html> Description – <font color='9a9695'>Time @ Location</font></html>");
	this.add(text);
	
	deleteIcon = new JLabel();
	deleteIcon.setIcon(graphics.delete);
	deleteIcon.setBounds(35, 3, 34, 34);
	deleteIcon.setVisible(false);
	this.add(deleteIcon);
	
	
	apparentId = new JLabel("1");
	apparentId.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    deleteTaskShortcut();
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		    deleteIcon.setVisible(true);		    
		}
		@Override
		public void mouseExited(MouseEvent e) {
		    deleteIcon.setVisible(false);
		}
	});
	apparentId.setHorizontalAlignment(SwingConstants.CENTER);
	apparentId.setFont(new Font("Lato", Font.PLAIN, 12));
	apparentId.setForeground(Color.WHITE);
	apparentId.setBounds(34, 2, 34, 34);
	this.add(apparentId);
	
	background = new JLabel();
	background.setSize(888, 40);
	background.setIcon(graphics.background);
	this.add(background);
	
	//this.setVisible(true);
	
	this.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseEntered(MouseEvent e) {
		showDateInfoIcon();
	    }
	    
	    @Override
	    public void mouseExited(MouseEvent e) {
		hideDateInfoIcon();
	    }
	});
    }
    
    private void activateInfoState() {
	inDateDisplayState = false;
	infoIcon.setVisible(false);
	dateIcon.setVisible(true);
	
	setTimedDisplayText(description, location, infoDisplayTime);
    }
    
    private void activateDateState() {
	inDateDisplayState = true;
	dateIcon.setVisible(false);
	infoIcon.setVisible(true);
	
	text.setText(dateDisplayTime);
    }
    
    private void reminderShortcut() {
	// TODO: save and clear text
	
	String reminderTime = "<NARIN, how do I get remind time?>";
	
	textPane.setText("modify " + "-id " + guiId + " -remind " + reminderTime);
    }
      
    private void deleteTaskShortcut() {
//	controller.executeCommands("delete -id " + guiId); // Increases coupling
	
	Logic.deleteTask(taskProperties.getTaskID());
	MainInterface.updateTaskDisplay();
    }
    
    private void modifyDescriptionShortcut() {
	
	// TODO: save and clear text
	
	if (!inDateDisplayState) {
	    String location = "";
	    if (!taskProperties.getLocation().equals("NIL")) {
		location = " -loc " + taskProperties.getLocation();
	    }

	    textPane.setText("modify " + taskProperties.getTaskTitle() + " " + "-id " + guiId + location + " ");

	} else {
	    if (taskProperties.getIsThereReminder()) {
		textPane.setText("modify -id " + guiId + " -start " + dateFormatter.format(taskProperties.getStartTime().getTime()) + " -end " 
			+ dateFormatter.format(taskProperties.getEndTime().getTime()) + " -remind " + dateFormatter.format(taskProperties.getEndTime().getTime())); // TODO: Implement remind time
	    } else {
		textPane.setText("modify -id " + guiId + " -start " + dateFormatter.format(taskProperties.getStartTime().getTime()) + " -end " 
			+ dateFormatter.format(taskProperties.getEndTime().getTime())); // TODO: Implement remind time
	    }
	}
    }
    
    private void cyclePriorities() {
	int currentPriority = taskProperties.getPriority();
	int newPriority;
	
	// TODO: Less coupling
	
	switch(currentPriority) {
	case 0:
	    break;
	
	case 1:
	    if (!isFloatingTask) {
		newPriority = 2;
		

	    } else {
		
	    }
	    
	    break;
	    
	case 2:
	    if (!isFloatingTask) {
		newPriority = 3;

	    } else {
		
	    }
	    
	    break;
	case 3:
	    if (!isFloatingTask) {
		newPriority = 0;
	    } else {
		
	    }
	    break;
	    
	}
	
    }

    public void loadTimedTaskDetails(Task item, int guiId) {
	
	taskProperties = item;
	
	if (taskProperties != null) {
	    
	    isFloatingTask = false;
	    
	    description = taskProperties.getTaskTitle();
	    location = taskProperties.getLocation();
	    infoDisplayTime = "";
	    
	    this.realTaskId = taskProperties.getTaskID();
	    
	    if (location.equals("NIL")) {
		location = "";
	    } else {
		location = LOCATION_AT_PREFIX + location;
	    }

	    /** NEED TO REPLACE ACCORDING TO NARIN's DISPLAY METHOD FOR FLOATING TASKS **/

	    infoDisplayTime = updateInfoTimings();
	    
	    if (taskProperties.getIsThereReminder()) {
	    	dateDisplayTime = updateDateTimings(taskProperties.getStartTime(), taskProperties.getEndTime(), taskProperties.getStartTime()); // TODO: Replace last with remind time
	    } else {
		dateDisplayTime = updateDateTimings(taskProperties.getStartTime(), taskProperties.getEndTime());
	    }
	    
	    setTimedDisplayText(description, location, infoDisplayTime);
	    setCheckMark(taskProperties.getIsTaskDone());
	    setReminderIcon(taskProperties.getIsThereReminder());
	    setPriorityIcon(taskProperties.getPriority(), taskProperties.getIsThereReminder());
	    apparentId.setText(guiId + "");
	    
	    // Set info/date icon toggle button:
	    
	    int xPosOfToggle = calculateXPos();
	    dateIcon.setLocation(xPosOfToggle, 7);
	    infoIcon.setLocation(xPosOfToggle, 7);
	}
    }
    
    private String updateDateTimings(Calendar startTime, Calendar endTime, Calendar remindTime) {
	
	String displayText = "";
	
	String startTimeString = dateFormatter.format(startTime.getTime()) + HTML_SPACING;
	String endTimeString = dateFormatter.format(endTime.getTime()) + HTML_SPACING;
	String remindTimeString = dateFormatter.format(remindTime.getTime());
	
	displayText = "<html><nobr> Start: <font color='9a9695'>" + startTimeString + "</font>End: <font color='9a9695'>" + endTimeString + "</font>" 
		+ "Reminder: <font color='9a9695'>" + remindTimeString + "</font>";
	
	return displayText + "</nobr></html>";
    }
    
    private String updateDateTimings(Calendar startTime, Calendar endTime) {
	String displayText = "";
	
	String startTimeString = dateFormatter.format(startTime.getTime()) + HTML_SPACING;
	String endTimeString = dateFormatter.format(endTime.getTime());
	
	displayText = "<html><nobr> Start: <font color='9a9695'>" + startTimeString + "</font>End: <font color='9a9695'>" + endTimeString + "</font>";
	
	return displayText + "</nobr></html>";	
    }
    
    private int calculateXPos() {
	if (taskProperties.getPriority() == 0 && !taskProperties.getIsThereReminder()) {
	    return 689 + 10 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET;
	} 
	
	return priority.getLocation().x - 13 - 25; // priority.getIcon().getIconWidth()
    }
    
    public String updateInfoTimings() {
	String displayTime;
	if (taskProperties.getStartTime().after(Calendar.getInstance())) {
	displayTime = "starts " + p.format(taskProperties.getStartTime());
	} else if (taskProperties.getEndTime().after(Calendar.getInstance())) {
	displayTime = "ends " + p.format(taskProperties.getEndTime());
	} else {
	displayTime = "ended " + p.format(taskProperties.getEndTime());
	}
	return displayTime;
    }
    
    public void setPriorityIcon(int priorityRef, boolean isThereReminder) {
	
	withoutReminderOffset = 35 + 10;
	
	if (isThereReminder) {
	    withoutReminderOffset = 10;
	}
	
	if (priorityRef == 1) {
	    setHighPri(withoutReminderOffset);
	} else if (priorityRef == 2) {
	    setMedPri(withoutReminderOffset);

	} else if (priorityRef == 3) {
	    setLowPri(withoutReminderOffset);

	} else {
	    priority.setVisible(false);
	}


    }

    public void setLowPri(int withoutReminderOffset) {
	priority.setIcon(graphics.lowPri);
	priority.setSize(6, 20);
	priority.setLocation(672 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET + withoutReminderOffset,  10);
	priority.setVisible(true);
    }

    public void setMedPri(int withoutReminderOffset) {
	priority.setIcon(graphics.medPri);
	priority.setSize(15, 20);
	priority.setLocation(663 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET + withoutReminderOffset,  10);
	priority.setVisible(true);
    }

    public void setHighPri(int withoutReminderOffset) {
	priority.setIcon(graphics.highPri);
	priority.setSize(23, 20);
	priority.setLocation(655 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET + withoutReminderOffset, 10);
	priority.setVisible(true);
    }
    
    public void setReminderIcon (boolean show) {
	if (show) {
	    reminder.setVisible(true);
	} else {
	    reminder.setVisible(false);
	}
    }

    public void setTimedDisplayText(String description, String location, String displayTime) {
	
	// TODO: Check for NIL standards with Narin and add SWITCH CASE
	String displayText = "<html><nobr> " + description + " – <font color='9a9695'>" + displayTime + location + "</font></nobr></html>";
	
	text.setText(displayText);

	
	Dimension dimensions = text.getPreferredSize();
	if (dimensions.getWidth() > 600) {
	    text.setSize(575, 24);
	    
	    ellipsis.setVisible(true);
	    text.setToolTipText(description + " – " + displayTime + location);
	} else {
	    ellipsis.setVisible(false);
	    text.setToolTipText("");
	}
    }
    
    
    public void setCheckMark(boolean check) {
	
	if (check) {
	    unchecked.setVisible(false);
	    checkMark.setVisible(true);
	    
	    
	} else {
	    unchecked.setVisible(true);
	    checkMark.setVisible(false);
	    
	}
	
    }

    public void showDateInfoIcon() {
	if (inDateDisplayState) {
	    infoIcon.setVisible(true);
	    dateIcon.setVisible(false);
	} else {
	    dateIcon.setVisible(true);
	    infoIcon.setVisible(false);
	}
    }

    public void hideDateInfoIcon() {
	dateIcon.setVisible(false);
	infoIcon.setVisible(false);
    }
}
