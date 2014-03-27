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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;


public class TaskItem extends JLayeredPane {
    
    public static PrettyTime p = new PrettyTime();
    public static BufferedGraphics graphics = new BufferedGraphics();
    private JLabel background;
    private JLabel checkMark;
    private JLabel reminder;
    private JLabel priority;
    private JLabel text;
    
    public TaskItem(Task item) {
	
	super();
	
	//loadDetails(item);
	
	
	this.setSize(724, 40);
//	this.setLocation(82, 81);
	this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	
	checkMark = new JLabel();
	checkMark.setLocation(10, 14);
	checkMark.setSize(19, 13);
	checkMark.setIcon(graphics.checkMark);
	checkMark.setVisible(true);
	this.add(checkMark);
	
	reminder = new JLabel();
	reminder.setLocation(689, 7);
	reminder.setSize(25, 26);
	reminder.setIcon(graphics.reminderIcon);
	reminder.setVisible(false);
	this.add(reminder);
	
	priority = new JLabel();
	priority.setLocation(666, 7);
	// Must icon, icon size, and location during use
	priority.setVisible(false);
	this.add(priority);
	
	text = new JLabel("New Task");
	text.setForeground(Color.WHITE);
	text.setFont(new Font("Lato", Font.PLAIN, 14));
	text.setLocation(44, 7);
	text.setSize(600, 24);
	text.setText("<html> Description – <font color='9a9695'>Time @ Location</font></html>");
	this.add(text);
	
	background = new JLabel();
	background.setSize(724, 40);
	background.setIcon(graphics.background);
	this.add(background);
	
	//this.setVisible(true);	
    }

    public void loadDetails(Task item) {
	
	if (item != null) {
	    
	    String description = item.getTaskTitle();
	    String location = item.getLocation();
	    String displayTime = null;
	    Boolean isComplete = item.getIsTaskDone();
	    Boolean isThereReminder = item.getIsThereReminder();

	    /** NEED TO REPLACE ACCORDING TO NARIN's DISPLAY METHOD FOR FLOATING TASKS **/
	    if (item.getStartTime() != null ) {
		displayTime = p.format(item.getStartTime());
	    } else if (item.getEndTime() != null) {
		displayTime = p.format(item.getEndTime());
	    } else {
		displayTime = ""; // No time is displayed
	    }
	    
	    text.setText("<html> " + description + " – <font color='9a9695'>" + displayTime + " @ " + location + "</font></html>");
	}
    }
    
    public void setCheckMark(boolean check) {
	
	if (check) {
	    
	}
	
    }

}
