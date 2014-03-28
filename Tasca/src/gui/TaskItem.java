package gui;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.ocpsoft.prettytime.PrettyTime;

import storage.Task;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;

import logic.Logic;
import javax.swing.SwingConstants;


public class TaskItem extends JLayeredPane {
    
    private static final int HTML_CODE_LENGTH = 20;
    private static final int MAX_DISPLAY_TEXT_LENGTH = 90;
    private static final int INITIAL_SPACE_OFFSET = 82;
    private static final int FINAL_SPACE_OFFSET = 31;
    public static PrettyTime p = new PrettyTime();
    public static BufferedGraphics graphics = new BufferedGraphics();
    private JLabel background;
    private JLabel checkMark;
    private JLabel reminder;
    private JLabel priority;
    private JLabel text;
    private JLabel unchecked;
    private Task taskProp;
    private JLabel ellipsis;
    private JLabel apparentId;
    
    
    public TaskItem() {
	
	super();
	
	//loadDetails(item);
	
	
	this.setSize(888, 40);
//	this.setLocation(82, 81);
	this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	
	checkMark = new JLabel();
	checkMark.setLocation(10 + INITIAL_SPACE_OFFSET, 14);
	checkMark.setSize(19, 13);
	checkMark.setIcon(graphics.checkMark);
	checkMark.setVisible(false);
	this.add(checkMark);
	
	unchecked = new JLabel();
	unchecked.setLocation(12 + INITIAL_SPACE_OFFSET, 13);
	unchecked.setSize(15, 15);
	unchecked.setIcon(graphics.unchecked);
	unchecked.setVisible(true);
	this.add(unchecked);
	
	reminder = new JLabel();
	reminder.setLocation(689 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET, 7);
	reminder.setSize(25, 26);
	reminder.setIcon(graphics.reminderIcon);
	reminder.setVisible(false);
	this.add(reminder);
	
	priority = new JLabel();
	priority.setLocation(666 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET, 7);
	// Must icon, icon size, and location during use
	priority.setVisible(false);
	this.add(priority);
	
	ellipsis = new JLabel(" ...");
	ellipsis.setForeground(Color.WHITE);
	ellipsis.setFont(new Font("Menlo", Font.PLAIN, 14));
	ellipsis.setBounds(612 + INITIAL_SPACE_OFFSET, 9, 40, 24);
	ellipsis.setVisible(false);
	add(ellipsis);
	
	text = new JLabel("New Task");
	text.setForeground(Color.WHITE);
	text.setFont(new Font("Lato", Font.PLAIN, 14));
	text.setLocation(44 + INITIAL_SPACE_OFFSET, 7);
	text.setSize(600, 24);
	text.setText("<html> Description – <font color='9a9695'>Time @ Location</font></html>");
	this.add(text);
	
	apparentId = new JLabel("1");
	apparentId.setHorizontalAlignment(SwingConstants.CENTER);
	apparentId.setFont(new Font("Lato", Font.PLAIN, 12));
	apparentId.setForeground(Color.WHITE);
	apparentId.setBounds(39, 11, 25, 16);
	this.add(apparentId);
	
	background = new JLabel();
	background.setSize(888, 40);
	background.setIcon(graphics.background);
	this.add(background);
	
	//this.setVisible(true);	
    }

    public void loadDetails(Task item, int guiId) {
	
	taskProp = item;
	
	if (taskProp != null) {
	    
	    
	    String description = taskProp.getTaskTitle();
	    String location = taskProp.getLocation();
	    String displayTime = null;
	    
	    int taskId = taskProp.getTaskID();

	    /** NEED TO REPLACE ACCORDING TO NARIN's DISPLAY METHOD FOR FLOATING TASKS **/
	    if (taskProp.getStartTime() != null ) {
		displayTime = p.format(taskProp.getStartTime());
	    } else if (taskProp.getEndTime() != null) {
		displayTime = p.format(taskProp.getEndTime());
	    } else {
		displayTime = ""; // No time is displayed
	    }
	    
	    setTimedDisplayText(description, location, displayTime);
	    setCheckMark(taskProp.getIsTaskDone());
	    setReminderIcon(taskProp.getIsThereReminder());
	    setPriorityIcon(taskProp.getPriority(), taskProp.getIsThereReminder());
	    apparentId.setText(guiId + "");
	}
    }
    
    public void setPriorityIcon(int priorityRef, boolean isThereReminder) {
	
	int withoutReminderOffset = 35;
	
	if (isThereReminder) {
	    withoutReminderOffset = 0;
	}
	
	if (priorityRef == 1) {
	    priority.setIcon(graphics.highPri);
	    priority.setSize(23, 20);
	    priority.setLocation(655 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET + withoutReminderOffset, 10);
	    priority.setVisible(true);
	} else if (priorityRef == 2) {
	    priority.setIcon(graphics.medPri);
	    priority.setSize(15, 20);
	    priority.setLocation(663 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET + withoutReminderOffset,  10);
	    priority.setVisible(true);

	} else if (priorityRef == 3) {
	    priority.setIcon(graphics.lowPri);
	    priority.setSize(6, 20);
	    priority.setLocation(672 + INITIAL_SPACE_OFFSET + FINAL_SPACE_OFFSET + withoutReminderOffset,  10);
	    priority.setVisible(true);

	} else {
	    priority.setVisible(false);
	}


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
	String displayText = "<html><nobr> " + description + " – <font color='9a9695'>" + displayTime + " @ " + location + "</font></nobr></html>";
	
	text.setText(displayText);

	
	Dimension dimensions = text.getPreferredSize();
	if (dimensions.getWidth() > 600) {
	    text.setSize(575, 24);
	    ellipsis.setVisible(true);
	} else {
	    ellipsis.setVisible(false);
	}
    }
    
    
    public void setCheckMark(boolean check) {
	
	if (check) {
	    unchecked.setVisible(false);
	    checkMark.setVisible(true);
	    
	    // TODO: Update that task in Storage:
	} else {
	    unchecked.setVisible(true);
	    checkMark.setVisible(false);
	    
	    // TODO: Update that task in Storage:
	}
	
    }
}
