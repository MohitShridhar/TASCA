package gui;

import interpreter.CommandType;
import interpreter.Config;
import interpreter.FolderName;
import interpreter.Interpreter;
import interpreter.ParameterType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import junit.framework.Assert;

import storage.FloatingTask;
import storage.Reminder;
import controller.Controller;


//@author A0105912N
public class MainInterface {
   
    private static final int INVALID_INPUT_HISTORY_REF = -1;
    private static final int VERTICAL_GAP_BETWEEN_ITEMS = 13;
    private static final Color COLOR_UI_BACKGROUND = Color.decode("#272822");
    private static final int DISPLAY_PANE_MAX_HEIGHT = 262;

    private static final String FONT_NAME_LUCIDA_GRANDE = "Lucida Grande";
    private static final String FONT_NAME_LATO = "Lato";
    private static final String FONT_NAME_MESLO_BOLD = "Meslo LG S";
    private static final String FONT_NAME_MESLO_PLAIN = "Meslo LG M";

    private static final String FILEPATH_FONT_MESLO_BOLD = "/GUI Graphics/Fonts/MesloLGS-Bold.ttf";
    private static final String FILEPATH_FONT_MESLO_REG = "/GUI Graphics/Fonts/MesloLGM-Regular.ttf";
    private static final String FILEPATH_FONT_LUCIDA_GRANDE = "/GUI Graphics/Fonts/Lucida Grande.ttf";
    private static final String FILEPATH_FONT_LATO_REG = "/GUI Graphics/Fonts/Lato-Reg.ttf";
    
    private static final String INFO_CREATING_SETTINGS_PANE = "Creating settings pane";
    private static final String INFO_CREATING_IO_PANE = "Creating IOPane";
    private static final String MESSAGE_LOADING_FONTS_FAILED = "Could not load important fonts";

    private static Controller controller = new Controller();
    private static JFrame mainFrame;
    private static Logger logger = Controller.getLogger();
    
    // Window + components:
    public static Font mesloReg16, mesloBold16,menloReg, latoReg, latoReg15, latoReg14, latoReg12, latoBold13, latoBold20, latoBold16,latoReg13, lucidaReg22;
    private static int windowPosX=0,windowPosY=0;
    private static final String TITLELESS_JFRAME = "TitleLessJFrame";
    private static final Rectangle BOUNDS_MAIN_FRAME = new Rectangle(0, 0, 888, 500);
    
    private static final ImageIcon IMAGE_UI_BACKGROUND = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/UI Background.png"));
    private static final ImageIcon ICON_EXPORT_BUTTON = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Export Icon.png"));
    private static final ImageIcon ICON_SETTINGS_BUTTON = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Settings Icon.png"));
    private static final ImageIcon ICON_INPUT_BACKGROUND = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Empty Input Bar.gif"));
    private static final ImageIcon ICON_FEEDBACK_BACKGROUND = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Error Feedback Background.png"));
    private static final ImageIcon ICON_CLOSE_BUTTON = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Close button.png"));
    private static final ImageIcon ICON_MINIMIZE_BUTTON = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Minimize Button.png"));
    
    private static final String FILEPATH_ICON_DOWN_INDICATOR = "/GUI Graphics/Size Arrow Down.png";
    private static final String FILEPATH_ICON_UP_INDICATOR = "/GUI Graphics/Size Arrow Up.png";
    
    private static final String MESSAGE_WELCOME = "memora vivere";
    private static final String MESSAGE_EMPTY_LIST = "Just do it. Later.";
    private static final Border SETTINGS_EMPTY_BORDER = BorderFactory.createEmptyBorder(0,0,0,0);
    private static final int VIEWPORT_HEIGHT_DISPLAY_PANE = 262;
    
    // Input bar & Input history:
    private static UserInputTextPane textPane;
    private static List<InputHistory.StateMemory> savedUserInput = new ArrayList<InputHistory.StateMemory>();
    private static InputHistory inputHistory = new InputHistory();
    private static int currentHistoryState = INVALID_INPUT_HISTORY_REF;
    private static int inputHistorySize = INVALID_INPUT_HISTORY_REF;
    private static final int MAX_INPUT_HISTORY = 30;
    
    private static final String SINGLE_SPACE = " ";
    private static final String DELIMITER = "-";

    // Folder tab manager:
    private static final int NUM_FOLDERS = 5;  
    private static FolderName currFolder, prevFolder, defaultFolder;
    private static String folder1Name, folder2Name, folder3Name, folder4Name, folder5Name;
    private static FolderName folderCycle[] = {FolderName.FOLDER1, FolderName.FOLDER2, FolderName.FOLDER3, FolderName.FOLDER4, FolderName.FOLDER5};
    private static FolderTab folder1, folder2, folder3, folder4, folder5;
    private static int cycleRef = 1;
    
    private static final Point LOCATION_FOLDER5 = new Point(636, 4);
    private static final Point LOCATION_FOLDER4 = new Point(477, 4);
    private static final Point LOCATION_FOLDER3 = new Point(318, 4);
    private static final Point LOCATION_FOLDER2 = new Point(159, 4);
    private static final Point LOCATION_FOLDER1 = new Point(-2, 4);

    // Display pane:
    private static JScrollPane taskPane;
    private static Interpreter interpreter;

    private static JButton btnSettings, btnExport;
    private static JLabel upIndicator, downIndicator, inputBackground, feedbackText, feedbackBackground, msgEmptyList, systemStatusMessage;

    private static Config cfg;
    
    private static SimpleAttributeSet parameterSetting = new SimpleAttributeSet();
    private static SimpleAttributeSet normalSetting = new SimpleAttributeSet();
    
    private static final int MAX_SCROLL_SPEED_IN_PIXELS = 1;
    private static final int SCROLL_THUMB_WIDTH = 16;
    private static final int DELAY_KEY_PRESS = 170;
    
    // Component Dimensions/Locations:    
    private static final Rectangle BOUNDS_CLOSE_BUTTON = new Rectangle(862, 7, 17, 17);
    private static final Rectangle BOUNDS_LAYERED_PANE = new Rectangle(0, 0, 888, 342);
    private static final Rectangle BOUNDS_TASK_PANE = new Rectangle(0, 80, 888, 262);
    private static final Rectangle BOUNDS_DOWN_INDICATOR = new Rectangle(861, 440, 18, 18);
    private static final Rectangle BOUNDS_UP_INDICATOR = new Rectangle(861, 412, 18, 18);
    private static final Rectangle BOUNDS_SCROLL_PANE = new Rectangle(50, 423, 745, 23);
    private static final Rectangle BOUNDS_EMPTY_LIST_MESSAGE = new Rectangle(214, 200, 460, 28);
    private static final Rectangle BOUNDS_SYSTEM_MESSAGE = new Rectangle(0, 484, 888, 16);
    private static final Rectangle BOUNDS_MINIMIZE_BUTTON = new Rectangle(836, 7, 18, 18);
    private static final Rectangle BOUNDS_INPUT_BAR_BACKGROUND = new Rectangle(37, 412, 814, 46);
    private static final Rectangle BOUNDS_FEEDBACK_TEXT_BACKGROUND = new Rectangle(77, 361, 750, 52);
    private static final Rectangle BOUNDS_FEEDBACK_TEXT = new Rectangle(99, 373, 694, 18);
    private static final Rectangle BOUNDS_SETTINGS_BUTTON = new Rectangle(505, 365, 27, 27);
    private static final Rectangle BOUNDS_EXPORT_BUTTON = new Rectangle(351, 365, 26, 26);
    
    private static final Dimension DIMENSIONS_TASK_ITEM = new Dimension(888, 40);
    private static final Dimension DIMENSIONS_MAIN_FRAME = new Dimension(888, 500);
    private static final Rectangle DISPLAY_PANE_BOUNDS = BOUNDS_TASK_PANE;

    // Input color filter:
    private static InputColorFilter colorFilter;
    static {
	StyleConstants.setBold((MutableAttributeSet) parameterSetting, false);
	StyleConstants.setForeground((MutableAttributeSet) parameterSetting, Color.WHITE);

	StyleConstants.setBold((MutableAttributeSet) normalSetting, false);
	StyleConstants.setForeground((MutableAttributeSet) normalSetting, Color.WHITE);
    }

    public MainInterface() {

	readFonts();
	customizeAndLoadFonts();

	loadFrameSettings();
	initializeGuiComponents(mainFrame);

	createShutdownHook();
	updateTaskDisplay();

	showMainFrame();
    }

    private void customizeAndLoadFonts() {

	mesloReg16 = new Font(FONT_NAME_MESLO_PLAIN, Font.PLAIN, 16);
	mesloBold16 = new Font(FONT_NAME_MESLO_BOLD, Font.BOLD, 16);

	latoBold20 = new Font(FONT_NAME_LATO, Font.BOLD, 20);
	latoBold16 = new Font(FONT_NAME_LATO, Font.BOLD, 16);

	latoReg15 = new Font(FONT_NAME_LATO, Font.PLAIN, 15);
	latoReg14 = new Font(FONT_NAME_LATO, Font.PLAIN, 14);
	latoReg13 = new Font(FONT_NAME_LATO, Font.PLAIN, 13);
	latoReg12 = new Font(FONT_NAME_LATO, Font.PLAIN, 12);

	lucidaReg22 = new Font(FONT_NAME_LUCIDA_GRANDE, Font.PLAIN, 22);

    }

    private void readFonts() {
	try {
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MainInterface.class.getResourceAsStream(FILEPATH_FONT_LATO_REG)));
	    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MainInterface.class.getResourceAsStream(FILEPATH_FONT_LUCIDA_GRANDE)));
	    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MainInterface.class.getResourceAsStream(FILEPATH_FONT_MESLO_REG)));
	    ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MainInterface.class.getResourceAsStream(FILEPATH_FONT_MESLO_BOLD)));

	} catch (IOException | FontFormatException e) {
	    logger.log(Level.SEVERE, MESSAGE_LOADING_FONTS_FAILED + e.getStackTrace());
	    Assert.fail(MESSAGE_LOADING_FONTS_FAILED);
	}
    }

    public static void main(String[] args)  {

	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		new MainInterface();
	    }
	});

    }

    public static boolean getIsActiveFeedbackEnabled() {
	return cfg.getIsActiveFeedbackEnabled();
    }

    
    public static boolean getIsDefaultCollated() {
	return cfg.getIsDefaultCollated();
    }

    public static void loadAllFolderLabels(){
	cfg = new Config();

	folder1Name = cfg.getFolderName(FolderName.FOLDER1);
	folder2Name = cfg.getFolderName(FolderName.FOLDER2);
	folder3Name = cfg.getFolderName(FolderName.FOLDER3);
	folder4Name = cfg.getFolderName(FolderName.FOLDER4);
	folder5Name = cfg.getFolderName(FolderName.FOLDER5);

	setDefaultFolder(cfg.getDefaultFolder());
    }

    public static void updateAllFolderLabels() {

	folder1.setLabelText(folder1Name);
	folder2.setLabelText(folder2Name);
	folder3.setLabelText(folder3Name);
	folder4.setLabelText(folder4Name);
	folder5.setLabelText(folder5Name);

    }


    private static LinkedList<Reminder> folderSortTimedTasks() {

	LinkedList<Reminder> original = getController().getCurrentSystemState().getTimedList();
	LinkedList<Reminder> folderSortedList = new LinkedList<Reminder>();

	for (int i=0; i<original.size(); i++) {
	    if (isCurrentFolder(getReminderFolder(original, i))) {
		folderSortedList.add(original.get(i));
	    }
	}

	return folderSortedList;
    }

    private static FolderName getReminderFolder(LinkedList<Reminder> original, int index) {
	return cfg.getFolderId(original.get(index).getTask().getFolder());
    }

    private static boolean isCurrentFolder(FolderName folder) {

	if (getIsDefaultCollated()) {
	    return folder == getCurrFolder() ||  getCurrFolder() == getDefaultFolder();
	} else {
	    return folder == getCurrFolder() || (folder == FolderName.DEFAULT && getCurrFolder() == getDefaultFolder());
	}
	
    }

    private static LinkedList<FloatingTask> folderSortFloatingTasks() {

	LinkedList<FloatingTask> original = getController().getCurrentSystemState().getFloatingList();
	LinkedList<FloatingTask> folderSortedList = new LinkedList<FloatingTask>();
	
	for (int i=0; i<original.size(); i++) {
	    if (isCurrentFolder(getTaskFolder(original, i))) {
		folderSortedList.add(original.get(i));
	    }
	}
	
	return folderSortedList;
    }

    private static FolderName getTaskFolder(LinkedList<FloatingTask> original, int index) {
	return cfg.getFolderId(original.get(index).getFolder());
    }

    public static void updateTaskDisplay() {
	int currentScrollPos = getVeriticalScrollPos();

	LinkedList<Reminder> folderSortedTimedTasks = folderSortTimedTasks(); 
	LinkedList<FloatingTask> folderSortedFloatingTasks = folderSortFloatingTasks();

	if (isListEmpty(folderSortedTimedTasks, folderSortedFloatingTasks)) {
	    taskPane.setVisible(false);
	    msgEmptyList.setVisible(true);
	    return;
	} else {
	    msgEmptyList.setVisible(false);
	}

	JPanel displayPanel = initDisplayPanel(folderSortedTimedTasks, folderSortedFloatingTasks); 
	rebuildItemList(currentScrollPos, folderSortedTimedTasks,folderSortedFloatingTasks, displayPanel);
    }

    private static void rebuildItemList(int scrollPos,
	    LinkedList<Reminder> folderSortedTimedTasks,
	    LinkedList<FloatingTask> folderSortedFloatingTasks,
	    JPanel displayPanel) {

	int numOfTimedTasks = addTimedTasks(folderSortedTimedTasks, displayPanel);
	addFloatingTasks(folderSortedFloatingTasks, displayPanel, numOfTimedTasks);

	adjustDisplayViewport(displayPanel, scrollPos);
	updateStatusMessage();

    }

    private static void updateStatusMessage() {
	getSystemStatusMessage().setText(getController().getSystemMessageString());
    }

    private static JPanel initDisplayPanel(LinkedList<Reminder> folderSortedTimedTasks, LinkedList<FloatingTask> folderSortedFloatingTasks) {

	JPanel displayPanel = new JPanel(new GridLayout(folderSortedTimedTasks.size() + folderSortedFloatingTasks.size(), 0, 0, VERTICAL_GAP_BETWEEN_ITEMS));
	displayPanel.setBackground(COLOR_UI_BACKGROUND);
	Interpreter.clearGuiIdMap();

	return displayPanel;
    }

    private static void adjustDisplayViewport(JPanel displayPanel, int previousScrollPos) {
	double preferredHeight = displayPanel.getPreferredSize().getHeight(); 

	if (preferredHeight < DISPLAY_PANE_MAX_HEIGHT) {
	    taskPane.setSize(displayPanel.getPreferredSize());
	} else {
	    taskPane.setBounds(DISPLAY_PANE_BOUNDS);
	}

	taskPane.setViewportView(displayPanel);
	taskPane.setVisible(true);

	setVerticalScollPos(previousScrollPos);
    }

    private static void addFloatingTasks(
	    LinkedList<FloatingTask> folderSortedFloatingTasks,
	    JPanel displayPanel, int numOfTimedTasks) {

	for (int i = 0; i < folderSortedFloatingTasks.size(); i++) {
	    TaskItem taskBar = new TaskItem(textPane, getController(), computeFloatingTaskGuiId(numOfTimedTasks, i), interpreter);
	    taskBar.loadFloatingTaskDetails(folderSortedFloatingTasks.get(i), computeFloatingTaskGuiId(numOfTimedTasks, i));

	    Interpreter.addGuiId(computeFloatingTaskGuiId(numOfTimedTasks, i), folderSortedFloatingTasks.get(i).getTaskID());
	    loadTaskItemProperties(displayPanel, taskBar);
	}
    }

    private static int computeFloatingTaskGuiId(int numOfTimedTasks, int index) {
	return index + 1 + numOfTimedTasks;
    }

    private static void loadTaskItemProperties(JPanel displayPanel,
	    TaskItem taskBar) {
	taskBar.setPreferredSize(DIMENSIONS_TASK_ITEM);
	taskBar.setVisible(true);
	displayPanel.add(taskBar);
    }

    private static int addTimedTasks(
	    LinkedList<Reminder> folderSortedTimedTasks, JPanel displayPanel) {
	int i;
	for (i=0; i < folderSortedTimedTasks.size(); i++) {
	    TaskItem taskBar = new TaskItem(textPane, getController(), computeTimedTaskGuiId(i), interpreter);
	    taskBar.loadTimedTaskDetails(folderSortedTimedTasks.get(i).getTask(), computeTimedTaskGuiId(i), folderSortedTimedTasks.get(i).getReminderTime());

	    Interpreter.addGuiId(computeTimedTaskGuiId(i), folderSortedTimedTasks.get(i).getTask().getTaskID());
	    loadTaskItemProperties(displayPanel, taskBar);
	}
	return i;
    }

    private static int computeTimedTaskGuiId(int index) {
	return index+1;
    }

    private static boolean isListEmpty(
	    LinkedList<Reminder> folderSortedTimedTasks,
	    LinkedList<FloatingTask> folderSortedFloatingTasks) {
	return folderSortedTimedTasks.size() + folderSortedFloatingTasks.size() == 0;
    }

    public static void clearTextPane() {

	if (savedUserInput.size() > MAX_INPUT_HISTORY) {
	    savedUserInput.clear();
	    setCurrentHistoryState(INVALID_INPUT_HISTORY_REF);
	}

	if (!textPane.getText().trim().isEmpty()) {
	    inputHistory.set(textPane.getText());
	    savedUserInput.add(inputHistory.saveToHistory());
	    setCurrentHistoryState(getCurrentHistoryState() + 1); // increment current state pointer 
	    setInputHistorySize(getInputHistorySize() + 1); 
	}

	textPane.setText("");
    }

    public static int getVeriticalScrollPos() {
	return taskPane.getVerticalScrollBar().getValue();
    }

    public static void setVerticalScollPos(int yPos) {
	taskPane.getVerticalScrollBar().setValue(yPos);
    }


    public static JButton getSettingsBtn() {
	return btnSettings;
    }

    public static JButton getExportBtn() {
	return btnExport;
    }

    public static void setUpIndicator(boolean state) {
	upIndicator.setVisible(state);
    }

    public static void setDownIndicator(boolean state) {
	downIndicator.setVisible(state);
    }


    private static void initializeGuiComponents(final JFrame currFrame) {

	loadAllFolderLabels();
	initDefaultFolderState();

	addCloseButton(currFrame);
	addMinimizeButton(currFrame);

	createTaskListPane(currFrame);

	initLabels();
	createInputBar(currFrame, inputBackground, feedbackText, feedbackBackground);   

	createEmptyListMessageLabel();
	createSystemMessageLabel();
	createFolderTabs(currFrame);
	createActiveFeedback(currFrame);

	addSettingsButton(currFrame);
	addExportButton(currFrame);

	setMainFrameBackground(currFrame);
	addWindowDragListener(currFrame);
	addKeyboardShortcutListener();
	
    }

    private static void addKeyboardShortcutListener() {
	KeyboardFocusManager.getCurrentKeyboardFocusManager()
	.addKeyEventDispatcher(new KeyEventDispatcher() {
	    private long lastPressTime = 0;

	    @Override
	    public boolean dispatchKeyEvent(KeyEvent keyEvent) {

		if(System.currentTimeMillis() - lastPressTime > DELAY_KEY_PRESS) {
		    executeShortcut(keyEvent);
		    lastPressTime = System.currentTimeMillis();
		}
		
		return false; //continue to monitor other key events
	    }

	});
    }

    private static void addWindowDragListener(final JFrame currFrame) {
	currFrame.addMouseListener(new MouseAdapter()
	{
	    public void mousePressed(MouseEvent event)
	    {
		windowPosX=event.getX();
		windowPosY=event.getY();
	    }
	});

	currFrame.addMouseMotionListener(new MouseAdapter()
	{
	    public void mouseDragged(MouseEvent event)
	    {
		//sets frame position when mouse dragged			
		currFrame.setLocation (event.getXOnScreen()-windowPosX,event.getYOnScreen()-windowPosY);

	    }
	});
    }

    private static void setMainFrameBackground(final JFrame frame) {
	JLabel mainFrameBackground = new JLabel(IMAGE_UI_BACKGROUND);
	mainFrameBackground.setBackground(Color.BLACK);
	mainFrameBackground.setBounds(BOUNDS_MAIN_FRAME);
	frame.getContentPane().add(mainFrameBackground);
    }

    private static void addExportButton(final JFrame frame) {
	btnExport = new JButton(ICON_EXPORT_BUTTON);
	btnExport.setBounds(BOUNDS_EXPORT_BUTTON);
	btnExport.setContentAreaFilled(false);
	btnExport.setBorder(BorderFactory.createEmptyBorder());
	btnExport.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		logger.log(Level.INFO, INFO_CREATING_IO_PANE);
		new IOPane(frame, getSystemStatusMessage(), getController());
	    }
	});   

	frame.getContentPane().add(btnExport);
    }

    private static void addSettingsButton(final JFrame frame) {
	btnSettings = new JButton(ICON_SETTINGS_BUTTON);
	btnSettings.setBounds(BOUNDS_SETTINGS_BUTTON);
	btnSettings.setContentAreaFilled(false);
	btnSettings.setBorder(BorderFactory.createEmptyBorder());
	btnSettings.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		logger.log(Level.INFO, INFO_CREATING_SETTINGS_PANE);
		initiateSettingsPane(frame);
	    }
	});     
	frame.getContentPane().add(btnSettings);
    }

    private static void createActiveFeedback(final JFrame frame) {
	feedbackText.setHorizontalAlignment(SwingConstants.CENTER);
	feedbackText.setForeground(Color.WHITE);
	feedbackText.setFont(latoReg15);
	feedbackText.setBounds(BOUNDS_FEEDBACK_TEXT);
	frame.getContentPane().add(feedbackText);

	feedbackBackground.setIcon(ICON_FEEDBACK_BACKGROUND);
	feedbackBackground.setBounds(BOUNDS_FEEDBACK_TEXT_BACKGROUND);
	frame.getContentPane().add(feedbackBackground);

	feedbackText.setVisible(false);
	feedbackBackground.setVisible(false);

	inputBackground.setIcon(ICON_INPUT_BACKGROUND);
	inputBackground.setBounds(BOUNDS_INPUT_BAR_BACKGROUND);
	frame.getContentPane().add(inputBackground);
    }

    private static void createFolderTabs(final JFrame frame) {
	folder1 = new FolderTab(FolderName.FOLDER1, LOCATION_FOLDER1, folder1Name);
	frame.getContentPane().add(folder1);

	folder2 = new FolderTab(FolderName.FOLDER2, LOCATION_FOLDER2, folder2Name);
	frame.getContentPane().add(folder2);

	folder3 = new FolderTab(FolderName.FOLDER3, LOCATION_FOLDER3, folder3Name);
	frame.getContentPane().add(folder3);

	folder4 = new FolderTab(FolderName.FOLDER4, LOCATION_FOLDER4, folder4Name);
	frame.getContentPane().add(folder4);

	folder5 = new FolderTab(FolderName.FOLDER5, LOCATION_FOLDER5, folder5Name);
	frame.getContentPane().add(folder5);
    }

    private static void initLabels() {
	inputBackground = new JLabel("");
	feedbackText = new JLabel("");
	feedbackBackground = new JLabel("");
    }

    private static void processCtrlShortcuts(KeyEvent event) {
	if (isStartTimeShortcut(event)) {
	    
	    appendToTextPane(textPane, DELIMITER + interpreter.getDefaultParaSyn(ParameterType.START_TIME), ParameterType.START_TIME);
	    
	} else if (isModifyCommandShortcut(event)) {
	    
	    clearTextPane();
	    textPane.replaceSelection(interpreter.getDefaultCommandSyn(CommandType.MODIFY) + SINGLE_SPACE + DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID) + SINGLE_SPACE);
	    
	} else if (isIdShortcut(event)) {

	    appendToTextPane(textPane, DELIMITER + interpreter.getDefaultParaSyn(ParameterType.TASK_ID), ParameterType.TASK_ID);
	    
	} else if (isEndTimeShortcut(event)) {
	    
	    appendToTextPane(textPane, DELIMITER + interpreter.getDefaultParaSyn(ParameterType.END_TIME), ParameterType.END_TIME);
	    
	} else if (isPriorityShortcut(event)) {
	    
	    appendToTextPane(textPane, DELIMITER + interpreter.getDefaultParaSyn(ParameterType.PRIORITY), ParameterType.PRIORITY);
	    
	} else if (isReminderShortcut(event)) {
	    
	    appendToTextPane(textPane, DELIMITER + interpreter.getDefaultParaSyn(ParameterType.REMINDER_TIME), ParameterType.REMINDER_TIME);
	    
	} else if (isLocationShortcut(event)) {
	    
	    appendToTextPane(textPane, DELIMITER + interpreter.getDefaultParaSyn(ParameterType.LOCATION), ParameterType.LOCATION);
	    
	} else if (isFolderShortcut(event)) {
	    
	    appendToTextPane(textPane, DELIMITER + interpreter.getDefaultParaSyn(ParameterType.FOLDER), ParameterType.FOLDER);
	    
	} else if (isTextPaneFocusShortcut(event)) {
	    
	    textPane.requestFocus();
	    
	} else if (isFolderRightSwitchShortcut(event)) {
	 
	    executeFolderRightSwitch();

	} else if (isFolderLeftSwitchShortcut(event)) {
	    
	    executeFolderLeftSwitch();

	} else if (isPageUpShortcut(event)) {
	    
	    taskPane.getVerticalScrollBar().setValue((taskPane.getVerticalScrollBar().getValue()) - VIEWPORT_HEIGHT_DISPLAY_PANE);
	    
	} else if (isPageDownShortcut(event)) {
	    
	    taskPane.getVerticalScrollBar().setValue((taskPane.getVerticalScrollBar().getValue()) + VIEWPORT_HEIGHT_DISPLAY_PANE);
	}

    }

    private static void executeFolderLeftSwitch() {
	setCycleRef((getCycleRef() + NUM_FOLDERS - 1) % NUM_FOLDERS);
	FolderName nextFolder = getFolderCycle()[getCycleRef()];

	FolderTab.gotoFolder(nextFolder);
    }

    private static void executeFolderRightSwitch() {
	setCycleRef((getCycleRef() + NUM_FOLDERS + 1) % NUM_FOLDERS);

	FolderName nextFolder = getFolderCycle()[getCycleRef()];

	FolderTab.gotoFolder(nextFolder);
    }

    private static boolean isPageDownShortcut(KeyEvent event) {
	return isInputHistoryForwardShortcut(event);
    }

    private static boolean isPageUpShortcut(KeyEvent event) {
	return isInputHistoryBackShortcut(event);
    }

    private static boolean isFolderLeftSwitchShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_TAB && event.isShiftDown();
    }

    private static boolean isFolderRightSwitchShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_TAB && !event.isShiftDown();
    }

    private static boolean isTextPaneFocusShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_T;
    }

    private static boolean isFolderShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_F;
    }

    private static boolean isLocationShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_L;
    }

    private static boolean isReminderShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_R || event.getKeyCode() == KeyEvent.VK_A;
    }

    private static boolean isPriorityShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_P;
    }

    private static boolean isEndTimeShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_E || event.getKeyCode() == KeyEvent.VK_D;
    }

    private static boolean isIdShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_N || event.getKeyCode() == KeyEvent.VK_I;
    }

    private static boolean isModifyCommandShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_M && event.isShiftDown();
    }

    private static boolean isStartTimeShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_S || event.getKeyCode() == KeyEvent.VK_O;
    }

    private static void appendToTextPane(UserInputTextPane textPane, String newString, ParameterType parameterType) {
	
	if (textPane.getText().isEmpty()) {
	    return;
	}

	if (parameterAlreadyExists(textPane, newString)) {
	    textPane.setCaretPosition(textPane.getText().indexOf(newString)+newString.length() + 1);
	    return;
	}

	if (hasTrailingSpace(textPane)) {
	    newString = SINGLE_SPACE + newString;
	}

	StyleConstants.setForeground((MutableAttributeSet) parameterSetting, InputColorFilter.getParameterColor(parameterType));

	try {
	    textPane.appendParameter(newString, parameterSetting);
	    textPane.appendParameter(SINGLE_SPACE, normalSetting);
	    textPane.setCaretPosition(textPane.getText().length());
	} catch (BadLocationException e) {
	    logger.log(Level.WARNING, "" + e.getStackTrace());
	}

    }

    private static boolean hasTrailingSpace(UserInputTextPane textPane) {
	return textPane.getText().charAt(textPane.getText().length() - 1) != ' ';
    }

    private static boolean parameterAlreadyExists(UserInputTextPane textPane,
	    String newString) {
	return textPane.getText().indexOf(newString) >= 0;
    }

    private static void addMinimizeButton(final JFrame frame) {
	JButton btnMinimize = new JButton("");
	btnMinimize.setBackground(Color.BLACK);
	btnMinimize.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		frame.setState(JFrame.ICONIFIED);
	    }
	});
	btnMinimize.setIcon(ICON_MINIMIZE_BUTTON);
	btnMinimize.setBounds(BOUNDS_MINIMIZE_BUTTON);

	btnMinimize.setBorderPainted(false);
	btnMinimize.setContentAreaFilled(false);
	btnMinimize.setBorder(SETTINGS_EMPTY_BORDER);

	frame.getContentPane().add(btnMinimize);
    }

    private static void createSystemMessageLabel() {
	setSystemStatusMessage(new JLabel(""));
	getSystemStatusMessage().setHorizontalAlignment(SwingConstants.CENTER);
	getSystemStatusMessage().setBounds(BOUNDS_SYSTEM_MESSAGE);
	getSystemStatusMessage().setFont(latoReg12);
	getSystemStatusMessage().setForeground(Color.WHITE);

	mainFrame.getContentPane().add(getSystemStatusMessage());
    }

    private static void createEmptyListMessageLabel() {
	msgEmptyList = new JLabel(MESSAGE_EMPTY_LIST);
	msgEmptyList.setForeground(Color.WHITE);
	msgEmptyList.setHorizontalAlignment(SwingConstants.CENTER);
	msgEmptyList.setFont(lucidaReg22);
	msgEmptyList.setBounds(BOUNDS_EMPTY_LIST_MESSAGE);
	msgEmptyList.setVisible(false);
	mainFrame.getContentPane().add(msgEmptyList);
    }

    private static void createInputBar(final JFrame frame, JLabel inputBackground,
	    JLabel feedbackText, JLabel feedbackBackground) {
	
	createInputTextPane(); 
	initIndicators(frame);
	
	addInputColorFilter(frame, inputBackground, feedbackText, feedbackBackground);
	
	createInputScrollPane(frame);
    }

    private static void addInputColorFilter(final JFrame frame,
	    JLabel inputBackground, JLabel feedbackText,
	    JLabel feedbackBackground) {
	colorFilter = (InputColorFilter.getInstance(frame, textPane, interpreter, inputBackground, feedbackText, feedbackBackground));
	((AbstractDocument) textPane.getDocument()).setDocumentFilter(colorFilter);
    }

    private static void createInputScrollPane(final JFrame frame) {
	JScrollPane scrollPane = new JScrollPane(textPane);
	scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
	scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	scrollPane.setBounds(BOUNDS_SCROLL_PANE);
	scrollPane.setOpaque(false);
	scrollPane.setBorder(SETTINGS_EMPTY_BORDER);


	scrollPane.getViewport().setOpaque(false);
	scrollPane.setViewportView(textPane);
	frame.getContentPane().add(scrollPane);
    }

    private static void createInputTextPane() {
	textPane = new UserInputTextPane(new DefaultStyledDocument());
	textPane.setOpaque(false);
	textPane.setText(MESSAGE_WELCOME);
	textPane.setFont(mesloReg16);
	textPane.setForeground(Color.WHITE);
	textPane.setFocusTraversalKeysEnabled(false);
	textPane.setCaretColor(Color.WHITE);

	interpreter = new Interpreter();
	Interpreter.setIsGuiIdEnabled(true);
    }

    private static void initIndicators(final JFrame frame) {
	try {
	    upIndicator = new JLabel(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_ICON_UP_INDICATOR)))));
	} catch (IOException exception) {
	    logger.log(Level.SEVERE, BufferedGraphics.MESSAGE_GRAPHICS_LOAD_FAILED + exception.getStackTrace());
	    Assert.fail(BufferedGraphics.MESSAGE_GRAPHICS_LOAD_FAILED);
	}

	upIndicator.setBounds(BOUNDS_UP_INDICATOR);
	upIndicator.setVisible(false);
	frame.getContentPane().add(upIndicator);

	try {
	    downIndicator = new JLabel(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_ICON_DOWN_INDICATOR)))));
	} catch (IOException exception) {
	    logger.log(Level.SEVERE, BufferedGraphics.MESSAGE_GRAPHICS_LOAD_FAILED + exception.getStackTrace());
	    Assert.fail(BufferedGraphics.MESSAGE_GRAPHICS_LOAD_FAILED);
	}

	downIndicator.setBounds(BOUNDS_DOWN_INDICATOR);
	downIndicator.setVisible(false);
	frame.getContentPane().add(downIndicator);
    }

    private static void createShutdownHook() {
	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

	    public void run() {
		getController().executeCommands(interpreter.getDefaultCommandSyn(CommandType.QUIT));
	    }
	}));
    }

    private static void createTaskListPane(final JFrame frame) {
	JLayeredPane layeredPane = initLayeredPane();

	buildTaskPane(layeredPane);

	frame.getContentPane().add(layeredPane);
    }

    private static void buildTaskPane(JLayeredPane layeredPane) {
	taskPane = new JScrollPane();
	taskPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	taskPane.setBounds(BOUNDS_TASK_PANE);
	taskPane.setOpaque(false);
	taskPane.setBorder(SETTINGS_EMPTY_BORDER);
	taskPane.getViewport().setOpaque(false);
	taskPane.setOpaque(false);
	taskPane.setVisible(true);
	taskPane.setDoubleBuffered(true);

	addScrollBar();

	layeredPane.add(taskPane);
    }

    private static JLayeredPane initLayeredPane() {
	JLayeredPane layeredPane = new JLayeredPane();
	layeredPane.setBounds(BOUNDS_LAYERED_PANE);
	layeredPane.setOpaque(false);
	layeredPane.setBorder(SETTINGS_EMPTY_BORDER);
	return layeredPane;
    }

    private static void addScrollBar() {
	JScrollBar mainScrollBar = taskPane.getVerticalScrollBar();
	mainScrollBar.setPreferredSize(new Dimension(SCROLL_THUMB_WIDTH, Integer.MAX_VALUE));
	mainScrollBar.setUI(new ScrollBarUI());

	taskPane.getVerticalScrollBar().setUnitIncrement(MAX_SCROLL_SPEED_IN_PIXELS);
    }

    private static void addCloseButton(final JFrame frame) {
	JButton btnClose = new JButton(ICON_CLOSE_BUTTON);

	btnClose.setBackground(Color.BLACK);
	btnClose.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);
	    }
	});

	btnClose.setBorderPainted(false);
	btnClose.setContentAreaFilled(false);
	btnClose.setBorder(SETTINGS_EMPTY_BORDER);

	btnClose.setBounds(BOUNDS_CLOSE_BUTTON);
	frame.getContentPane().add(btnClose);
    }

    private static void initDefaultFolderState() {
	setCurrFolder(getDefaultFolder());
	setPrevFolder(getCurrFolder());

	setCycleRef(Integer.parseInt(getDefaultFolder().toString().charAt(6) + "" ) - 1); // char at index 6 in FolderName contains the folder id number
    }

    private static void loadFrameSettings() {
	mainFrame = new JFrame(TITLELESS_JFRAME);

	mainFrame.setBackground(COLOR_UI_BACKGROUND);
	mainFrame.getContentPane().setLayout(null);
	mainFrame.setUndecorated(true); 
	mainFrame.setSize(DIMENSIONS_MAIN_FRAME);
	mainFrame.setResizable(false); 
	mainFrame.setLocationRelativeTo(null); 
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void initiateSettingsPane(final JFrame frame) {
	new SettingsPane(frame, interpreter, cfg);
    }


    public static String getCurrentFolderName() {
	return cfg.getFolderName(getCurrFolder());
    }

    public static JLabel getSystemStatusMessage() {
	return systemStatusMessage;
    }

    public static void setSystemStatusMessage(JLabel systemStatusMessage) {
	MainInterface.systemStatusMessage = systemStatusMessage;
    }

    private static void showMainFrame() {
	mainFrame.setVisible(true);
	textPane.requestFocus();
    }

    public static int getCycleRef() {
	return cycleRef;
    }

    public static void setCycleRef(int cycleRef) {
	MainInterface.cycleRef = cycleRef;
    }

    public static FolderName[] getFolderCycle() {
	return folderCycle;
    }

    public static void setFolderCycle(FolderName folderCycle[]) {
	MainInterface.folderCycle = folderCycle;
    }

    public static FolderName getCurrFolder() {
	return currFolder;
    }

    public static void setCurrFolder(FolderName currFolder) {
	MainInterface.currFolder = currFolder;
    }

    public static FolderName getPrevFolder() {
	return prevFolder;
    }

    public static void setPrevFolder(FolderName prevFolder) {
	MainInterface.prevFolder = prevFolder;
    }

    public static FolderName getDefaultFolder() {
	return defaultFolder;
    }

    public static void setDefaultFolder(FolderName defaultFolder) {
	MainInterface.defaultFolder = defaultFolder;
    }

    public static int getCurrentHistoryState() {
	return currentHistoryState;
    }

    public static void setCurrentHistoryState(int inputNumRef) {
	MainInterface.currentHistoryState = inputNumRef;
    }

    public static int getInputHistorySize() {
	return inputHistorySize;
    }

    public static void setInputHistorySize(int inputHistorySize) {
	MainInterface.inputHistorySize = inputHistorySize;
    }

    public static Controller getController() {
	return controller;
    }

    public static void setController(Controller controller) {
	MainInterface.controller = controller;
    }

    private static void executeShortcut(KeyEvent event) {
	if (event.isControlDown()){
	    processCtrlShortcuts(event);
	}
	else if (event.isShiftDown()) {
	    processShiftOnlyShortcuts(event); 
	}
    }

    private static void processShiftOnlyShortcuts(KeyEvent event) {
	if (isInputHistoryBackShortcut(event)) {
	    executeInputHistoryBackwark();
	} else if (isInputHistoryForwardShortcut(event)) {
	    executeInputHistoryForward();
	}
    }

    private static void executeInputHistoryBackwark() {
	if (getCurrentHistoryState() >= 0) {
	    textPane.setText("");
	    textPane.replaceSelection(inputHistory.restoreFromHistory(savedUserInput.get(getCurrentHistoryState())));
	    setCurrentHistoryState(getCurrentHistoryState() - 1);
	}
    }

    private static void executeInputHistoryForward() {
	if (getCurrentHistoryState() + 2 < savedUserInput.size()) { // + 2 checks if there is one more forward state left
	    setCurrentHistoryState(getCurrentHistoryState() + 1);
	    textPane.setText("");
	    textPane.replaceSelection(inputHistory.restoreFromHistory(savedUserInput.get(getCurrentHistoryState()+1)));
	}
    }

    private static boolean isInputHistoryForwardShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_DOWN;
    }

    private static boolean isInputHistoryBackShortcut(KeyEvent event) {
	return event.getKeyCode() == KeyEvent.VK_UP;
    }

}



