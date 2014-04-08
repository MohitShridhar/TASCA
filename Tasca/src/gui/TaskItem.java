package gui;

import interpreter.CommandType;
import interpreter.Interpreter;
import interpreter.ParameterType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

import org.ocpsoft.prettytime.PrettyTime;

import storage.FloatingTask;
import storage.Task;
import controller.Controller;

//@author A0105912N
public class TaskItem extends JLayeredPane {

    private static final int PROGRESS_BAR_UPDATE_PERIOD = 120000;
    private static final int MIN_WIDTH_PROGRESS_BAR = 0;
    private static final int MAX_WIDTH_PROGRESS_BAR = 762;
    private static final int HEIGHT_PROGRESS_BAR = 38;
    private static final Point LOCATION_PROGRESS_BAR = new Point(83, 1);
   
    private static final Dimension DIMENSIONS_PRIORITY_ICON = new Dimension(23, 20);
    private static final Dimension DIMENSIONS_INITIAL_PROGRESS_BAR = new Dimension(MIN_WIDTH_PROGRESS_BAR, HEIGHT_PROGRESS_BAR);
    private static final Dimension DIMENSIONS_TASK_ITEM = new Dimension(888, 40);
    private static final Dimension DIMENSIONS_DATE_ICON = new Dimension(25, 25);
    private static final Dimension DIMENSIONS_INFO_ICON = DIMENSIONS_DATE_ICON;
    private static final Dimension DIMENSIONS_ITEM_BACKGROUND = DIMENSIONS_TASK_ITEM;
    private static final Rectangle BOUNDS_TEXT_LABEL = new Rectangle(126, -2, 600, 42);
    private static final Rectangle BOUNDS_DELETE_ICON = new Rectangle(35, 3, 34, 34);
    private static final Rectangle BOUNDS_APPARENT_ID = new Rectangle(34, 2, 34, 34);
    
    private static final int YPOS_PRIORITY_ICON = 10;
    private static final int YPOS_TOGGLE_ICONS = 7;
    private static final int HEIGHT_ELLIPSIS = 42;
    
    private static final int MAX_DISPLAY_TEXT_LENGTH = 600;
    private static final int INT_PRIORITY_LOW = 3;
    private static final int INT_PRIORITY_MEDIUM = 2;
    private static final int INT_PRIORITY_HIGH = 1;
    private static final int INT_PRIORITY_NONE = 0;
    
    private static final String DATE_FORMAT_ENDED = "ended ";
    private static final String DATE_FORMAT_ENDS = "ends ";
    private static final String DATE_FORMAT_STARTS = "starts ";
    
    private static final String PRI_STRING_MEDIUM = "med";
    private static final String PRI_STRING_HIGH = "high";
    private static final String PRI_STRING_LOW = "low";
    
    private static final String EMPTY_LOCATION = "NIL";

    private static final String HTML_END_TAG_WITH_STRIKE = "</strike></html>";
    private static final String HTML_START_TAG_WITH_STRIKE = "<html><strike>";
    private static final String HTML_END_TAG = "</html>";
    private static final String HTML_START_TAG = "<html>";

    private static final String DEFAULT_DISPLAY_TEXT = "<html> Description – <font color='9a9695'>Time @ Location</font></html>";
    
    private static final String HTML_FLOATING_TASK = "<html><nobr><font color='f7bbbb'> %1$s</font> <font color='9a9695'>%2$s</font></nobr></html>";
    private static final String HTML_TIME_DISPLAY_WITH_REMINDER = "<html><nobr> Start: <font color='9a9695'>%1$s</font>End: <font color='9a9695'>%2$s</font>Reminder: <font color='9a9695'>%3$s</font></nobr></html>";
    private static final String HTML_TIME_DISPLAY = "<html><nobr> Start: <font color='9a9695'>%1$s</font>End: <font color='9a9695'>%2$s</font></nobr></html>";
    private static final String HTML_INFO_DISPLAY = "<html><nobr> %1$s&nbsp; – <font color='9a9695'>%2$s%3$s</font></nobr></html>";
    
    private static final String MESSAGE_OVERFLOW_ELLIPSIS = " ...";
    private static final String SINGLE_SPACE = " ";
    private static final String INPUT_DELIMITER = " -";
    private static final String DESCRIPTION_SEPERATION_HYPHEN = " – ";

    private static final Border SETTINGS_EMPTY_BORDER = BorderFactory.createEmptyBorder(0, 0, 0, 0);

    private static final long serialVersionUID = 1L;

    private static final String HTML_SPACING = "&nbsp;&nbsp;&nbsp;";
    private static final String PREFIX_AT_LOCATION = " @ ";
    
    private static final int DISPLAY_TIME_UPDATE_PERIOD = 60000; //milliseconds

    private static final int LEADING_XPOS_OFFSET = 82;
    private static final Rectangle BOUNDS_CHECK_MARK = new Rectangle(8 + LEADING_XPOS_OFFSET, 9, 23, 23);
    private static final Rectangle BOUNDS_UNCHECKED_MARK = BOUNDS_CHECK_MARK;
    private static final Rectangle BOUNDS_ELLIPSIS = new Rectangle(635 + LEADING_XPOS_OFFSET, 9, 40, 24);
    private static final int TRAILING_SPACE_OFFSET = 31;
    private static final Rectangle BOUNDS_REMINDER_ICON = new Rectangle(689 + 10 + LEADING_XPOS_OFFSET + TRAILING_SPACE_OFFSET, 7, 25, 26);
    private static final Point LOCATION_PRIORITY_ICON_INIT = new Point(666 + LEADING_XPOS_OFFSET + TRAILING_SPACE_OFFSET, 7);
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm");

    public static PrettyTime prettyTimeParser = new PrettyTime();
    public static BufferedGraphics bufferedGraphics = new BufferedGraphics();

    private JLabel background;
    private JLabel checkMark;
    private JLabel reminderIcon;
    private JLabel priorityIcon;
    private JLabel text;
    private JLabel dateIcon;
    private JLabel infoIcon;
    private JLabel uncheckedMark;
    private JLabel ellipsis;
    private JLabel apparentId;
    private JLabel deleteIcon;
    
    private ProgressBar progressBar;

    private String description;
    private String location;
    private String infoDisplayTime, dateDisplayTime;
    private int guiId;
    private static Controller controller;
    private Calendar reminderTime;

    private boolean isFloatingTask = false;
    private boolean inDateDisplayState = false;

    private Task timedTask;
    private FloatingTask floatingTask;

    private int withoutReminderXPosOffset;

    private static Interpreter interpreter;
    private static JTextPane textPane;

    private ActionListener updateTime = new ActionListener() {
	public void actionPerformed(ActionEvent evt) {
	    if (!inDateDisplayState && !isFloatingTask) {
		setTimedDisplayText(description, location, formatInfoTimings());
	    }
	}
    };
    
    private ActionListener updateProgressBar = new ActionListener() {
	public void actionPerformed(ActionEvent evt) {
	    if (!isFloatingTask) {
		updateProgressBar();
	    }
	}
    };

    public TaskItem(JTextPane textPane, Controller controller, final int guiId, Interpreter interpreter) {

	super();
	linkMainInterfaceComponents(textPane, controller, guiId, interpreter);
	loadItemSettings(); 

	loadGuiComponents(guiId);
	
	addTaskItemBackground();
	addToggleIconMouseListener();
    }

    private void loadGuiComponents(final int guiId) {
	
	addCheckMark(guiId);
	addUncheckedMark(guiId);

	addDateIcon();
	addInfoIcon();

	addReminderIcon();
	addPriorityIcon();

	addOverflowEllipsis();
	addTextLabel();

	addDeleteIcon();
	addApparentTaskId();

    }

    private void addTaskItemBackground() {
	
	addStatusProgressBar();
	addDefaultPlainBackground();
	
    }

    private void addStatusProgressBar() {
	progressBar = new ProgressBar(0, 0); // initialize at coordinate {0, 0}. Location will be set later
	progressBar.setSize(DIMENSIONS_INITIAL_PROGRESS_BAR);
	progressBar.setLocation(LOCATION_PROGRESS_BAR);
	this.add(progressBar);
    }

    private void addDefaultPlainBackground() {
	background = new JLabel();
	background.setSize(DIMENSIONS_ITEM_BACKGROUND);
	background.setIcon(bufferedGraphics.getBackground());
	this.add(background);
    }

    private void addToggleIconMouseListener() {
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

    private void addApparentTaskId() {
	apparentId = new JLabel("");
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
	apparentId.setFont(MainInterface.latoReg12); 
	apparentId.setForeground(Color.WHITE);
	apparentId.setBounds(BOUNDS_APPARENT_ID);
	this.add(apparentId);
    }

    private void addDeleteIcon() {
	deleteIcon = new JLabel();
	deleteIcon.setIcon(bufferedGraphics.getDelete());
	deleteIcon.setBounds(BOUNDS_DELETE_ICON);
	deleteIcon.setVisible(false);
	this.add(deleteIcon);
    }

    private void addTextLabel() {
	text = new JLabel("");
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
	text.setFont(MainInterface.latoReg14);
	text.setBounds(BOUNDS_TEXT_LABEL);
	text.setText(DEFAULT_DISPLAY_TEXT);
	this.add(text);
    }

    private void addOverflowEllipsis() {
	ellipsis = new JLabel(MESSAGE_OVERFLOW_ELLIPSIS);
	ellipsis.setForeground(Color.WHITE);
	ellipsis.setFont(MainInterface.mesloReg16);
	ellipsis.setBounds(BOUNDS_ELLIPSIS);
	ellipsis.setVisible(false);
	add(ellipsis);
    }

    private void addPriorityIcon() {
	priorityIcon = new JLabel();
	priorityIcon.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (isFloatingTask) {
		    cyclePriorities(floatingTask.getPriority());
		} else {
		    cyclePriorities(timedTask.getPriority());
		}
	    }
	});
	priorityIcon.setLocation(LOCATION_PRIORITY_ICON_INIT);
	priorityIcon.setVisible(false);
	this.add(priorityIcon);
    }

    private void addReminderIcon() {
	reminderIcon = new JLabel();
	reminderIcon.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		reminderShortcut();
	    }
	});
	reminderIcon.setBounds(BOUNDS_REMINDER_ICON);
	reminderIcon.setIcon(bufferedGraphics.getReminderIcon());
	reminderIcon.setVisible(false);
	this.add(reminderIcon);
    }

    private void addInfoIcon() {
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
	infoIcon.setSize(DIMENSIONS_INFO_ICON);
	infoIcon.setIcon(bufferedGraphics.getInfo());
	infoIcon.setVisible(false);
	this.add(infoIcon);
    }

    private void addDateIcon() {
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
	dateIcon.setSize(DIMENSIONS_DATE_ICON);
	dateIcon.setIcon(bufferedGraphics.getDate());
	dateIcon.setVisible(false);
	this.add(dateIcon);
    }

    private void addUncheckedMark(final int guiId) {
	uncheckedMark = new JLabel();
	uncheckedMark.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		executeCheck(guiId);
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
	uncheckedMark.setBounds(BOUNDS_UNCHECKED_MARK);
	uncheckedMark.setIcon(bufferedGraphics.getUnchecked());
	uncheckedMark.setVisible(true);
	this.add(uncheckedMark);
    }

    private void addCheckMark(final int guiId) {
	checkMark = new JLabel();
	checkMark.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		executeUncheck(guiId);
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
	checkMark.setBounds(BOUNDS_CHECK_MARK);
	checkMark.setIcon(bufferedGraphics.getCheckMark());
	checkMark.setVisible(false);
	this.add(checkMark);
    }

    private void loadItemSettings() {
	this.setSize(DIMENSIONS_TASK_ITEM);
	this.setBorder(SETTINGS_EMPTY_BORDER);

	new Timer(DISPLAY_TIME_UPDATE_PERIOD, updateTime).start();
	new Timer(PROGRESS_BAR_UPDATE_PERIOD, updateProgressBar).start();
    }

    private void linkMainInterfaceComponents(JTextPane textPane,
	    Controller controller, final int guiId, Interpreter interpreter) {
	TaskItem.textPane = textPane;
	TaskItem.controller = controller;
	TaskItem.interpreter = interpreter;
	this.guiId = guiId;
    }

    private void addStrikeThrough() {
	String currentHtmlString = text.getText();

	String stringWithStartTag = currentHtmlString.replace(HTML_START_TAG, HTML_START_TAG_WITH_STRIKE);
	String stringWithStartAndEndTag = stringWithStartTag.replace(HTML_END_TAG, HTML_END_TAG_WITH_STRIKE);

	text.setText(stringWithStartAndEndTag);
    }

    private void removeStrikeThrough() {
	String currentHtmlString = text.getText();

	String stringWithStartTag = currentHtmlString.replace(HTML_START_TAG_WITH_STRIKE, HTML_START_TAG);
	String stringWithStartAndEndTag = stringWithStartTag.replace(HTML_END_TAG_WITH_STRIKE, HTML_END_TAG);

	text.setText(stringWithStartAndEndTag);
    }

    private void activateInfoState() {

	inDateDisplayState = false;
	infoIcon.setVisible(false);
	dateIcon.setVisible(true);

	setTimedDisplayText(description, location, infoDisplayTime);
	placeEllipsis(description, location, infoDisplayTime);
    }

    private void activateDateState() {

	inDateDisplayState = true;
	dateIcon.setVisible(false);
	infoIcon.setVisible(true);

	text.setText(dateDisplayTime);
	ellipsis.setVisible(false);
    }

    private void reminderShortcut() {

	MainInterface.clearTextPane();
	String reminderTime = dateFormatter.format(this.reminderTime.getTime());

	textPane.replaceSelection(interpreter.getDefaultCommandSyn(CommandType.MODIFY) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.REMINDER_TIME) + SINGLE_SPACE + reminderTime);
    }

    private void deleteTaskShortcut() {	

	controller.executeCommands(interpreter.getDefaultCommandSyn(CommandType.DELETE) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId); 
	refreshDisplay();

    }

    public void refreshDisplay() {
	MainInterface.updateTaskDisplay();
    }

    private void modifyDescriptionShortcut() {

	MainInterface.clearTextPane();

	if (isFloatingTask) {
	    generateFloatingTaskDetails(); 
	    return;
	}

	if (!inDateDisplayState) {
	    generateInfoDetails();
	} else {
	    generateTimeDetails();
	}
    }

    private void generateTimeDetails() {
	if (timedTask.getIsThereReminder()) {
	    textPane.replaceSelection(interpreter.getDefaultCommandSyn(CommandType.MODIFY) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.START_TIME) + SINGLE_SPACE + dateFormatter.format(timedTask.getStartTime().getTime()) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.END_TIME) + SINGLE_SPACE
		    + dateFormatter.format(timedTask.getEndTime().getTime()) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.REMINDER_TIME) + SINGLE_SPACE + dateFormatter.format(this.reminderTime.getTime()));
	} else {
	    textPane.replaceSelection(interpreter.getDefaultCommandSyn(CommandType.MODIFY) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.START_TIME) + SINGLE_SPACE + dateFormatter.format(timedTask.getStartTime().getTime()) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.END_TIME) + SINGLE_SPACE 
		    + dateFormatter.format(timedTask.getEndTime().getTime()));
	}
    }

    private void generateInfoDetails() {
	String location = "";
	if (!timedTask.getLocation().equals(EMPTY_LOCATION)) {
	    location = INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.LOCATION) + SINGLE_SPACE + timedTask.getLocation();
	}

	textPane.replaceSelection(interpreter.getDefaultCommandSyn(CommandType.MODIFY) + SINGLE_SPACE + timedTask.getTaskTitle() + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId + location + SINGLE_SPACE);
    }

    private void generateFloatingTaskDetails() {
	String location = "";

	if (!floatingTask.getLocation().equals(EMPTY_LOCATION)) {
	    location = INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.LOCATION) + SINGLE_SPACE + floatingTask.getLocation();
	}

	textPane.replaceSelection(interpreter.getDefaultCommandSyn(CommandType.MODIFY) + SINGLE_SPACE + floatingTask.getTaskTitle() + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId + location + SINGLE_SPACE);
    }

    private void cyclePriorities(int currentPriority) {

	switch(currentPriority) {
	case 0:
	    return;
	case 1:
	    controller.executeCommands(interpreter.getDefaultCommandSyn(CommandType.MODIFY) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.PRIORITY) + SINGLE_SPACE + PRI_STRING_LOW + SINGLE_SPACE + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.FOLDER) + SINGLE_SPACE + MainInterface.getCurrentFolderName());
	    break;
	case 2:
	    controller.executeCommands(interpreter.getDefaultCommandSyn(CommandType.MODIFY) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.PRIORITY) + SINGLE_SPACE + PRI_STRING_HIGH + SINGLE_SPACE + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.FOLDER) + SINGLE_SPACE + MainInterface.getCurrentFolderName());
	    break;
	case 3:
	    controller.executeCommands(interpreter.getDefaultCommandSyn(CommandType.MODIFY) + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.PRIORITY) + SINGLE_SPACE + PRI_STRING_MEDIUM + SINGLE_SPACE + INPUT_DELIMITER + interpreter.getDefaultParaSyn(ParameterType.FOLDER) + SINGLE_SPACE + MainInterface.getCurrentFolderName());
	    break;
	}

	refreshDisplay();

    }

    public void loadTimedTaskDetails(Task item, int guiId, Calendar reminderTime) {

	timedTask = item;

	assert (timedTask != null);

	loadTimedTaskGuiSettings(reminderTime);
	initializeTimedDisplayTexts();
	loadDisplayTime();

	setTimedDisplayText(description, location, infoDisplayTime);
	setCheckMark(timedTask.getIsTaskDone());

	setReminderIcon(timedTask.getIsThereReminder());
	setPriorityIcon(timedTask.getPriority(), timedTask.getIsThereReminder());

	setTaskStrikethrough(timedTask);
	setToggleButtonPos();
	
	updateProgressBar();	
    }
    
    private void updateProgressBar() {
	progressBar.setSize(computeProgressBarWidth(timedTask.getStartTime().getTimeInMillis(), timedTask.getEndTime().getTimeInMillis()), HEIGHT_PROGRESS_BAR);		
    }
    
    public static int computeProgressBarWidth(Long startMillis, Long endMillis) {
	
	long millisNow = System.currentTimeMillis();
	
	if (startMillis >= endMillis || millisNow >= endMillis) {
	    return MAX_WIDTH_PROGRESS_BAR;
	}
	
	if (millisNow <= startMillis) {
	    return MIN_WIDTH_PROGRESS_BAR; 
	}
	
	return (int) (MAX_WIDTH_PROGRESS_BAR - ((endMillis - millisNow) * MAX_WIDTH_PROGRESS_BAR / (endMillis - startMillis)));
    }

    private void setToggleButtonPos() {
	int xPosOfToggleBtn = calculateToggleBtnXPos();
	dateIcon.setLocation(xPosOfToggleBtn, YPOS_TOGGLE_ICONS);
	infoIcon.setLocation(xPosOfToggleBtn, YPOS_TOGGLE_ICONS);
    }

    private void setTaskStrikethrough(Task timedTask) {
	if (timedTask.getIsTaskDone()) {
	    addStrikeThrough();
	} else {
	    removeStrikeThrough();
	}
    }

    private void setTaskStrikethrough(FloatingTask floatingTask) {
	if (floatingTask.getIsTaskDone()) {
	    addStrikeThrough();
	} else {
	    removeStrikeThrough();
	}
    }

    private void loadDisplayTime() {
	infoDisplayTime = formatInfoTimings();

	if (timedTask.getIsThereReminder()) {
	    dateDisplayTime = updateDateTimings(timedTask.getStartTime(), timedTask.getEndTime(), this.reminderTime); 
	} else {
	    dateDisplayTime = updateDateTimings(timedTask.getStartTime(), timedTask.getEndTime());
	}
    }

    private void initializeTimedDisplayTexts() {

	description = timedTask.getTaskTitle();
	location = timedTask.getLocation();
	infoDisplayTime = "";

	if (location.equals(EMPTY_LOCATION)) {
	    location = "";
	} else {
	    location = PREFIX_AT_LOCATION + location;
	}

	apparentId.setText(guiId + "");
    }

    private void loadTimedTaskGuiSettings(Calendar reminderTime) {
	this.reminderTime = reminderTime;
	isFloatingTask = false;

	reminderIcon.setEnabled(true);
	infoIcon.setEnabled(true);
	dateIcon.setEnabled(true);
    }

    public void loadFloatingTaskDetails(FloatingTask item, int guiId) {

	floatingTask = item;

	assert(floatingTask != null);

	loadFloatingTaskGuiSettings();
	loadFloatingTaskDisplayText();

	setCheckMark(floatingTask.getIsTaskDone());
	setPriorityIcon(floatingTask.getPriority(), false);
	
	// Note: floating task currently doesn't support reminders
    }	

    private void loadFloatingTaskDisplayText() {
	String description = floatingTask.getTaskTitle();
	String location = "";

	if (floatingTask.getLocation().equals(EMPTY_LOCATION)) {
	    location = "";
	} else {
	    location = PREFIX_AT_LOCATION + floatingTask.getLocation();
	}

	String displayText = String.format(HTML_FLOATING_TASK, description, location);
	text.setText(displayText);

	setTaskStrikethrough(floatingTask);
    }

    private void loadFloatingTaskGuiSettings() {
	isFloatingTask = true;

	this.remove(reminderIcon);
	this.remove(infoIcon);
	this.remove(dateIcon);

	apparentId.setText(guiId + "");
    }

    private String updateDateTimings(Calendar startTime, Calendar endTime, Calendar remindTime) {

	String displayText = "";

	String startTimeString = dateFormatter.format(startTime.getTime()) + HTML_SPACING;
	String endTimeString = dateFormatter.format(endTime.getTime()) + HTML_SPACING;
	String remindTimeString = dateFormatter.format(remindTime.getTime());

	displayText = String.format(HTML_TIME_DISPLAY_WITH_REMINDER, startTimeString, endTimeString, remindTimeString);

	return displayText;
    }

    private String updateDateTimings(Calendar startTime, Calendar endTime) {
	String displayText = "";

	String startTimeString = dateFormatter.format(startTime.getTime()) + HTML_SPACING;
	String endTimeString = dateFormatter.format(endTime.getTime());
	
	displayText = String.format(HTML_TIME_DISPLAY, startTimeString, endTimeString);
	
	return displayText;	
    }
    
    // The following int constants were generated on a trial&error basis:
    private int calculateToggleBtnXPos() {
	if (timedTask.getPriority() == INT_PRIORITY_NONE && !timedTask.getIsThereReminder()) {
	    return 699 + LEADING_XPOS_OFFSET + TRAILING_SPACE_OFFSET;
	} else if (timedTask.getPriority() == INT_PRIORITY_NONE && timedTask.getIsThereReminder()) {
	    return 699 + LEADING_XPOS_OFFSET + TRAILING_SPACE_OFFSET - 25 - 10;
	}

	return priorityIcon.getLocation().x - 13 - 25; 
    }

    private String formatInfoTimings() {
	String displayTime;
	if (timedTask.getStartTime().after(Calendar.getInstance())) {
	    displayTime = DATE_FORMAT_STARTS + prettyTimeParser.format(timedTask.getStartTime());
	} else if (timedTask.getEndTime().after(Calendar.getInstance())) {
	    displayTime = DATE_FORMAT_ENDS + prettyTimeParser.format(timedTask.getEndTime());
	} else {
	    displayTime = DATE_FORMAT_ENDED + prettyTimeParser.format(timedTask.getEndTime());
	}
	return displayTime;
    }

    private void setPriorityIcon(int priorityRef, boolean isThereReminder) {
	
	// The following int constants were generated on a trial&error basis::
	withoutReminderXPosOffset = 45;

	if (!isFloatingTask && isThereReminder) {
	    withoutReminderXPosOffset = 10;
	}

	if (priorityRef == INT_PRIORITY_HIGH) {
	    setPriHigh(withoutReminderXPosOffset);
	} else if (priorityRef == INT_PRIORITY_MEDIUM) {
	    setPriMed(withoutReminderXPosOffset);
	} else if (priorityRef == INT_PRIORITY_LOW) {
	    setPriLow(withoutReminderXPosOffset);
	} else {
	    priorityIcon.setVisible(false);
	}

    }
    
    // The following int constants were generated on a trial&error basis:
    
    private void setPriLow(int withoutReminderOffset) {
	priorityIcon.setIcon(bufferedGraphics.getLowPri());
	priorityIcon.setSize(DIMENSIONS_PRIORITY_ICON);
	priorityIcon.setLocation(655 + LEADING_XPOS_OFFSET + TRAILING_SPACE_OFFSET + withoutReminderOffset,  YPOS_PRIORITY_ICON);
	priorityIcon.setVisible(true);
    }

    private void setPriMed(int withoutReminderOffset) {
	priorityIcon.setIcon(bufferedGraphics.getMedPri());
	priorityIcon.setSize(DIMENSIONS_PRIORITY_ICON);
	priorityIcon.setLocation(655 + LEADING_XPOS_OFFSET + TRAILING_SPACE_OFFSET + withoutReminderOffset,  YPOS_PRIORITY_ICON);
	priorityIcon.setVisible(true);
    }

    private void setPriHigh(int withoutReminderOffset) {
	priorityIcon.setIcon(bufferedGraphics.getHighPri());
	priorityIcon.setSize(DIMENSIONS_PRIORITY_ICON);
	priorityIcon.setLocation(655 + LEADING_XPOS_OFFSET + TRAILING_SPACE_OFFSET + withoutReminderOffset, YPOS_PRIORITY_ICON);
	priorityIcon.setVisible(true);
    }

    private void setReminderIcon (boolean show) {
	if (show) {
	    reminderIcon.setVisible(true);
	} else {
	    reminderIcon.setVisible(false);
	}
    }

    private void setTimedDisplayText(String description, String location, String infoDisplayTime) {
	
	String displayText = String.format(HTML_INFO_DISPLAY, description, infoDisplayTime, location);
	text.setText(displayText);

	placeEllipsis(description, location, infoDisplayTime);
    }

    private void placeEllipsis(String description, String location,
	    String displayTime) {
	Dimension dimensions = text.getPreferredSize();
	if (dimensions.getWidth() > MAX_DISPLAY_TEXT_LENGTH) {
	    text.setSize(MAX_DISPLAY_TEXT_LENGTH, HEIGHT_ELLIPSIS);

	    ellipsis.setVisible(true);
	    text.setToolTipText(description + DESCRIPTION_SEPERATION_HYPHEN + displayTime + location);
	} else {
	    ellipsis.setVisible(false);
	    text.setToolTipText("");
	}
    }


    private void setCheckMark(boolean check) {

	if (check) {
	    uncheckedMark.setVisible(false);
	    checkMark.setVisible(true);


	} else {
	    uncheckedMark.setVisible(true);
	    checkMark.setVisible(false);

	}

    }

    private void showDateInfoIcon() {
	if (inDateDisplayState) {
	    infoIcon.setVisible(true);
	    dateIcon.setVisible(false);
	} else {
	    dateIcon.setVisible(true);
	    infoIcon.setVisible(false);
	}
    }

    private void hideDateInfoIcon() {
	dateIcon.setVisible(false);
	infoIcon.setVisible(false);
    }

    private void executeUncheck(final int guiId) {
	setCheckMark(false);
	TaskItem.controller.executeCommands(TaskItem.interpreter.getDefaultCommandSyn(CommandType.UNMARK) + INPUT_DELIMITER + TaskItem.interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId);
	MainInterface.getSystemStatusMessage().setText(TaskItem.controller.getSystemMessageString());
	removeStrikeThrough();
    }

    private void executeCheck(final int guiId) {
	setCheckMark(true);
	TaskItem.controller.executeCommands(TaskItem.interpreter.getDefaultCommandSyn(CommandType.MARK) + INPUT_DELIMITER + TaskItem.interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE + guiId);
	MainInterface.getSystemStatusMessage().setText(TaskItem.controller.getSystemMessageString());
	addStrikeThrough();
    }

}

