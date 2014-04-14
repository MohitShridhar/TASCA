package gui;

import interpreter.Config;
import interpreter.Interpreter;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import controller.Controller;


//@author A0105912N
public class SettingsPane extends JFrame {

    private static final Rectangle BOUNDS_DEFAULT_COLLATES_LABEL = new Rectangle(640, 45, 201, 16);
    private static final Rectangle BOUNDS_MAIN_TITLE = new Rectangle(369, 17, 182, 25);
    private static final Rectangle BOUNDS_CANCEL_BUTTON = new Rectangle(0, 366, 81, 34);
    private static final Rectangle BOUNDS_ACTIVE_FEEDBACK_CHECKBOX = new Rectangle(170, 40, 25, 25);
    private static final Rectangle BOUNDS_COMM_PANE = new Rectangle(45, 195, 396, 155);
    private static final Rectangle BOUNDS_PARA_PANE = new Rectangle(490, 195, 396, 155);
    private static final Rectangle BOUNDS_SAVE_BUTTON = new Rectangle(839, 366, 81, 34);
    private static final Rectangle BOUNDS_DEFAULT_FOLDER_SELECTOR = new Rectangle(75, 116, 97, 27);
    private static final Rectangle BOUNDS_FOLDER_LABEL = new Rectangle(70, 81, 780, 60);
    private static final Rectangle BOUNDS_FEEDBACK_CHECKBOX_LABEL = new Rectangle(21, 45, 157, 16);
    private static final Rectangle BOUNDS_COMM_TITLE = new Rectangle(130, 165, 201, 16);
    private static final Rectangle BOUNDS_PARA_TITLE = new Rectangle(588, 167, 201, 16);
    private static final Rectangle BOUNDS_WARNING_LABEL = new Rectangle(110, 364, 700, 28);
    private static final Rectangle BOUNDS_COMMAND_KEYWORD_PANE = new Rectangle(34, 195, 396, 155);
    private static final Rectangle BOUNDS_PARA_KEYWORD_PANE = BOUNDS_PARA_PANE;
    
    private static final Logger logger = Controller.getLogger();
    
    private static final Dimension DIMENSIONS_KEYWORD_BAR_PREFERRED_SIZE = new Dimension(396, 41);
    private static final Dimension DIMENSIONS_SETTINGS_PANE = new Dimension(920, 400);

    private static final String FILENAME_CONFIG_FILE = "Config.cfg";
    private static final String BOOLEAN_REF_DEFAULT_COLLATED = "isDefaultCollated";
    private static final String BOOLEAN_REF_FEEDBACK_ENABLED = "isActiveFeedbackEnabled";
    
    private static final String MESSAGE_INVALID_FOLDER_NAME = "Please specify a shorter name for Folder %1$s or check that it's not empty";
    private static final String MESSAGE_DUPLICATE_FOLDER_NAME = "Folder %1$s's name is a duplicate";
    private static final String MESSAGE_SAVE_FAILED = "SAVE FAILED: ";
    private static final String MESSAGE_CONFIG_WRITE_FAILED = "Could not write config file";
    
    private static final ImageIcon ICON_CANCEL_BUTTON = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Settings Cancel Button.png"));
    private static final ImageIcon LABEL_FOLDER_SELECTION = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Settings Folder Display.png"));
    private static final ImageIcon ICON_SAVE_BUTTON = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Settings Save Button.png"));
    
    private static final String TITLE_PREFERENCES = "PREFERENCES";
    private static final String TITLE_ACTIVE_FEEDBACK = "Active Input Feedback";
    private static final String TITLE_DEFAULT_COLLATES = "Default Folder collates tasks";
    private static final String TITLE_COMMANDS = "COMMANDS";
    private static final String TITLE_PARAMETERS = "PARAMETERS";

    private static final int MAX_ROW_COUNT = 5;
    private static final int SCROLL_SPEED_PIXELS = 1;
    private static final int SCROLL_THUMB_WIDTH = 16;
    
    private static final Border SETTINGS_EMPTY_BORDER = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    
    private static final Rectangle BOUNDS_FOLDER5_FIELD = new Rectangle(714, 116, 138, 28);
    private static final Rectangle BOUNDS_FOLDER4_FIELD = new Rectangle(578, 116, 140, 28);
    private static final Rectangle BOUNDS_FOLDER3_FIELD = new Rectangle(444, 116, 138, 28);
    private static final Rectangle BOUNDS_FOLDER2_FIELD = new Rectangle(308, 116, 140, 28);
    private static final Rectangle BOUNDS_FOLDER1_FIELD = new Rectangle(171, 116, 141, 28);
    
    private static final int TOOLTIP_REF = 2;
    private static final int CONFIG_NAME_REF = 1;
    private static final int DISPLAY_NAME_REF = 0;
    
    private static final int VERTICAL_GAP_BETWEEN_KEYWORD_ITEMS = 13;
    
    private static final Color COLOR_UI_BACKGROUND = Color.decode("#272822");
    
    private static final int ID_INT_FOLDER5 = 5;
    private static final int ID_INT_FOLDER4 = 4;
    private static final int ID_INT_FOLDER3 = 3;
    private static final int ID_INT_FOLDER2 = 2;
    private static final int ID_INT_FOLDER1 = 1;
    private static final int MAX_FOLDER_NAME_LENGTH = 16;
    
    private static final int ARRAY_ID_FOLDER5 = 4;
    private static final int ARRAY_ID_FOLDER4 = 3;
    private static final int ARRAY_ID_FOLDER3 = 2;
    private static final int ARRAY_ID_FOLDER2 = 1;
    private static final int ARRAY_ID_FOLDER1 = 0;
    
    private static final String DEFAULT_FOLDER_ID_STRING = "default";
    private static final String FOLDER5_ID_STRING = "folder5";
    private static final String FOLDER4_ID_STRING = "folder4";
    private static final String FOLDER3_ID_STRING = "folder3";
    private static final String FOLDER2_ID_STRING = "folder2";
    private static final String FOLDER1_ID_STRING = "folder1";
    
    private static final String TITLELESS_JFRAME = "TitleLessJFrame";
    private static final long serialVersionUID = 1L;

    
    private int dragPosX = 0, dragPosY = 0;
    private Checkbox activeFeedbackCheckbox, defaultCollatedCheckbox;
    private Config cfg;
    private String folder1Name, folder2Name, folder3Name, folder4Name, folder5Name, defaultFolder;
    
    private JComboBox<Object> defaultFolderSelector;
    private Properties props;
    
    private FolderNameField folder1Field;
    private FolderNameField folder2Field;
    private FolderNameField folder3Field;
    private FolderNameField folder4Field;
    private FolderNameField folder5Field;
    
    @SuppressWarnings("unused") // Compiler problem
    private Interpreter interpreter;
    private JLabel warningLabel;
    private JScrollPane commandKeywordPane, paraKeywordPane;
    
    private ArrayList<KeywordItem> commandKeywordItems = new ArrayList<KeywordItem>();
    private ArrayList<KeywordItem> paraKeywordItems = new ArrayList<KeywordItem>();

    private String[][] commandDatabase = new String[][]{
	    {"Add","add","<html>Create a new task<br> eg: add EE2031 assignment -start tues -end thurs -priority high -location home -remind wed -folder home &nbsp;<br>Note: all properties are optional</html>"},
	    {"Delete","delete","<html> Remove a task <br> eg: delete assignment (if you are unsure about the item ID, search for the description) <br> delete -id 12 (if you know the ID) </html>"},
	    {"Clear Completed","clearCompleted","<html> Remove all tasks which have been checked off </html>"},
	    {"Clear", "clear","<html> Remove all tasks in the current folder </html>"},
	    {"Modify", "modify","<html> Change the properties of an existing task <br> eg: modify new description for task -id 3 -pri med -loc new location -remind thurs ... &nbsp;</html>"},
	    {"Mark", "mark","<html> Check off a completed task </html>"},
	    {"Unmark", "unmark","<html> Uncheck a task if it wasn't actually completed </html>"},
	    {"Search", "search","<html> Search for a task using its description <br> eg: search EE2024 </html>"},
	    {"Now", "now","<html> Displays all tasks you should be currently doing </html>"},
	    {"Today", "today","<html> Displays all tasks that start or end today </html>"},
	    {"Tomorrow", "tomorrow","<html> Displays all tasks for tomorrow </html>"},
	    {"Week", "week","<html> Displays all tasks for the week </html>"},
	    {"Month", "month","<html> Displays all tasks for the month </html>"},
	    {"Undo", "undo","<html> Undo the previous action </html>"},
	    {"Redo", "redo","<html> Redo the next action </html>"},
	    {"Show All", "displayAll","<html>  Displays all tasks pertaining to the current folder </html>"},
	    {"Non-Timed Tasks", "displayFloat","<html> Displayes all non-timed tasks pertaining to the current folder </html>"},
	    {"Show in Time", "display","<html> Displays all tasks between the specified start and end time <br> eg: display -start tues -end thurs </html>"},
	    {"Export", "export","<html> Export all tasks to an ICS file at the specified filepath <br> eg: export /Users/MohitSridhar/Tasca_Export_Folder </html>"},
	    {"Import", "import","<html> Import all tasks from an ICS file <br> eg: import /Users/MohitSridhar/Tasca_Export_Folder/Tasca_Tasks.ics </html>"},
	    {"Quit", "quit","<html> Exit the application </html>"}
    };
    
    private String[][] parameterDatabase = new String[][] {
	    {"Start Time","startTime","<html> 1978-01-28 | 1984/04/02 | 1/02/1980 2/28/79 (American date) <br> The 31st of April in the year 2008 | Fri, 21 Nov 1997 | Jan 21, '97 | Sun, Nov 21 | jan 1st | february twenty-eighth <br> next thursday | last tues <br> 10 min from now </html>"},
	    {"End Time", "endTime","<html> 1978-01-28 | 1984/04/02 | 1/02/1980 2/28/79 (American date) <br> The 31st of April in the year 2008 | Fri, 21 Nov 1997 | Jan 21, '97 | Sun, Nov 21 | jan 1st | february twenty-eighth <br> next thursday | last tues <br> 10 min from now </html>"},
	    {"Reminder Time", "reminderTime","<html> Set reminder time <br> 1978-01-28 | 1984/04/02 | 1/02/1980 2/28/79 (American date) <br> The 31st of April in the year 2008 | Fri, 21 Nov 1997 | Jan 21, '97 | Sun, Nov 21 | jan 1st | february twenty-eighth <br> next thursday | last tues <br> 10 min from now </html>"},
	    {"Priority", "priority","<html> To specify the priority. It can have four states: high, low, medium or none </html>"},
	    {"Location", "location","<html> To specify the location description </html>"},
	    {"Folder", "folder","<html> To specify the folder name </html>"},
	    {"Task ID", "taskID","<html> Number reference to the task <br> </html>"}
    };
    
    public SettingsPane(JFrame mainFrame, Interpreter interpreter, Config cfg) {
	super(TITLELESS_JFRAME);
	loadFrameSettings(mainFrame);
	linkMainInterfaceComponents(interpreter, cfg);
	
	activateWindowDrag();
	
	loadFolderDatabase();
	loadGuiComponents();
	loadKeywordDatabase();
	setVisible(true);
    }

    private void linkMainInterfaceComponents(Interpreter interpreter, Config cfg) {
	MainInterface.getSettingsBtn().setEnabled(false);
	
	this.cfg = cfg;
	this.interpreter = interpreter;
    }
    
    private int getCurrentDefaultFolder(String folderId) {
	if (folderId.equals(FOLDER1_ID_STRING)) {
	    return ARRAY_ID_FOLDER1;
	} else if (folderId.equals(FOLDER2_ID_STRING)) {
	    return ARRAY_ID_FOLDER2;
	} else if (folderId.equals(FOLDER3_ID_STRING)) {
	    return ARRAY_ID_FOLDER3;
	} else if (folderId.equals(FOLDER4_ID_STRING)) {
	    return ARRAY_ID_FOLDER4;
	} else {
	    return ARRAY_ID_FOLDER5;
	}
    }
    
    private void loadFolderDatabase() {
	props = cfg.getConfigFile();
	
	defaultFolder = props.getProperty(DEFAULT_FOLDER_ID_STRING);
	folder1Name = props.getProperty(FOLDER1_ID_STRING);
	folder2Name = props.getProperty(FOLDER2_ID_STRING);
	folder3Name = props.getProperty(FOLDER3_ID_STRING);
	folder4Name = props.getProperty(FOLDER4_ID_STRING);
	folder5Name = props.getProperty(FOLDER5_ID_STRING);
    }


    private void activateWindowDrag() {
	addMouseListener(new MouseAdapter()
	{
	    public void mousePressed(MouseEvent e)
	    {
		dragPosX=e.getX();
		dragPosY=e.getY();
	    }
	});

	addMouseMotionListener(new MouseAdapter()
	{
	    public void mouseDragged(MouseEvent evt)
	    {		
		setLocation (evt.getXOnScreen()-dragPosX,evt.getYOnScreen()-dragPosY);

	    }
	});
    }

    private void loadKeywordDatabase() {
	
	loadCommands();
	loadParameters();
	
    }

    private void loadParameters() {
	
	paraKeywordItems.clear();
	
	JPanel paraPanel = new JPanel(new GridLayout(parameterDatabase.length, 0, 0, VERTICAL_GAP_BETWEEN_KEYWORD_ITEMS));
	
	paraPanel.setBackground(COLOR_UI_BACKGROUND);
	
	for (int i=0; i<parameterDatabase.length; i++) {
	    addParameterKeywordItem(paraPanel, i);
	}

	loadParameterKeywordPaneSettings(paraPanel);
    }

    private void loadCommands() {
	
	commandKeywordItems.clear();
	
	JPanel commandPanel = new JPanel(new GridLayout(commandDatabase.length, 0, 0, VERTICAL_GAP_BETWEEN_KEYWORD_ITEMS));

	commandPanel.setBackground(COLOR_UI_BACKGROUND);
	
	for (int i=0; i<commandDatabase.length; i++) {
	    addCommandKeywordItem(commandPanel, i);
	}
	
	loadCommandKeywordPaneSettings(commandPanel);
    }

    private void loadParameterKeywordPaneSettings(JPanel paraPanel) {
	paraKeywordPane.setBounds(BOUNDS_PARA_KEYWORD_PANE);
	paraKeywordPane.setViewportView(paraPanel);
	paraKeywordPane.setVisible(true);
    }

    private void addParameterKeywordItem(JPanel paraPanel, int i) {
	KeywordItem keywordBar = new KeywordItem(parameterDatabase[i][DISPLAY_NAME_REF], cfg.getProperty(parameterDatabase[i][CONFIG_NAME_REF]));
	
	keywordBar.setPreferredSize(DIMENSIONS_KEYWORD_BAR_PREFERRED_SIZE);
	keywordBar.setToolTipText(parameterDatabase[i][TOOLTIP_REF]); 
	keywordBar.setVisible(true);
	paraPanel.add(keywordBar);
	
	paraKeywordItems.add(keywordBar);
    }

    private void loadCommandKeywordPaneSettings(JPanel commandPanel) {
	commandKeywordPane.setBounds(BOUNDS_COMMAND_KEYWORD_PANE);
	commandKeywordPane.setViewportView(commandPanel);
	commandKeywordPane.setVisible(true);
    }

    private void addCommandKeywordItem(JPanel commandPanel, int i) {
	KeywordItem keywordBar = new KeywordItem(commandDatabase[i][DISPLAY_NAME_REF], cfg.getProperty(commandDatabase[i][CONFIG_NAME_REF]));

	keywordBar.setPreferredSize(DIMENSIONS_KEYWORD_BAR_PREFERRED_SIZE);
	keywordBar.setToolTipText(commandDatabase[i][TOOLTIP_REF]);
	keywordBar.setVisible(true);
	commandPanel.add(keywordBar);
	
	commandKeywordItems.add(keywordBar);
    }
    
    private void loadGuiComponents() {
	
	addCancelButton();    
	addSaveButton();
	
	addFolderNameFields();
	addCheckBoxes();
	
	addCommandKeywordPane();
	addParaKeywordPane();
	
	addDefaultFolderSelector();
	
	addWarningLabel();
	addTitles();
		
    }

    private void addWarningLabel() {
	warningLabel = new JLabel("");
	warningLabel.setForeground(Color.WHITE);
	warningLabel.setFont(MainInterface.latoBold16);
	warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
	warningLabel.setBounds(BOUNDS_WARNING_LABEL);
	getContentPane().add(warningLabel);
    }

    private void addTitles() {
	
	JLabel parameterKeywordsTitle = new JLabel(TITLE_PARAMETERS);
	parameterKeywordsTitle.setHorizontalAlignment(SwingConstants.CENTER);
	parameterKeywordsTitle.setForeground(Color.WHITE);
	parameterKeywordsTitle.setFont(MainInterface.latoReg14);
	parameterKeywordsTitle.setBounds(BOUNDS_PARA_TITLE);
	getContentPane().add(parameterKeywordsTitle);
	
	
	JLabel commandKeywordsTitle = new JLabel(TITLE_COMMANDS);
	commandKeywordsTitle.setForeground(Color.WHITE);
	commandKeywordsTitle.setHorizontalAlignment(SwingConstants.CENTER);
	commandKeywordsTitle.setFont(MainInterface.latoReg14);
	commandKeywordsTitle.setBounds(BOUNDS_COMM_TITLE);
	getContentPane().add(commandKeywordsTitle);
	
	JLabel activeInputFeedbackTitle = new JLabel(TITLE_ACTIVE_FEEDBACK);
	activeInputFeedbackTitle.setForeground(Color.WHITE);
	activeInputFeedbackTitle.setFont(MainInterface.latoReg13);
	activeInputFeedbackTitle.setHorizontalAlignment(SwingConstants.CENTER);
	activeInputFeedbackTitle.setBounds(BOUNDS_FEEDBACK_CHECKBOX_LABEL);
	getContentPane().add(activeInputFeedbackTitle);
	
	JLabel defaultCollatedTitle = new JLabel(TITLE_DEFAULT_COLLATES);
	defaultCollatedTitle.setForeground(Color.WHITE);
	defaultCollatedTitle.setFont(MainInterface.latoReg13);
	defaultCollatedTitle.setHorizontalAlignment(SwingConstants.RIGHT);
	defaultCollatedTitle.setBounds(BOUNDS_DEFAULT_COLLATES_LABEL);
	getContentPane().add(defaultCollatedTitle);
	
	JLabel folderSelectionBackgroundLabel = new JLabel(LABEL_FOLDER_SELECTION);
	folderSelectionBackgroundLabel.setBounds(BOUNDS_FOLDER_LABEL);
	getContentPane().add(folderSelectionBackgroundLabel);
    }

    private void addDefaultFolderSelector() {
	
	defaultFolderSelector = new JComboBox<Object>();
	defaultFolderSelector.setFocusable(false);
	defaultFolderSelector.setFont(MainInterface.latoReg13);
	defaultFolderSelector.setModel(new DefaultComboBoxModel<Object>(new String[] {FOLDER1_ID_STRING, FOLDER2_ID_STRING, FOLDER3_ID_STRING, FOLDER4_ID_STRING, FOLDER5_ID_STRING}));
	defaultFolderSelector.setSelectedIndex(getCurrentDefaultFolder(defaultFolder));
	defaultFolderSelector.setMaximumRowCount(MAX_ROW_COUNT);
	defaultFolderSelector.setBounds(BOUNDS_DEFAULT_FOLDER_SELECTOR);
	getContentPane().add(defaultFolderSelector);
	
    }

    private void addSaveButton() {
	JButton btnSave = new JButton(ICON_SAVE_BUTTON);

	btnSave.setBounds(BOUNDS_SAVE_BUTTON);
	btnSave.setContentAreaFilled(false);
	btnSave.setBorder(SETTINGS_EMPTY_BORDER);
	btnSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		saveChanges();
	    }
	});    

	getContentPane().add(btnSave);
    }

    private void addParaKeywordPane() {
	
	paraKeywordPane = new JScrollPane();
	paraKeywordPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	paraKeywordPane.setBounds(BOUNDS_PARA_PANE);
	paraKeywordPane.setOpaque(false);
	paraKeywordPane.setBorder(SETTINGS_EMPTY_BORDER);
	paraKeywordPane.getViewport().setOpaque(false);
	paraKeywordPane.setOpaque(false);
	paraKeywordPane.setVisible(true);
	paraKeywordPane.setDoubleBuffered(true);

	addScrollBar(paraKeywordPane);

	paraKeywordPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED_PIXELS);
	
	getContentPane().add(paraKeywordPane);
    }

    private void addCommandKeywordPane() {
	
	commandKeywordPane = new JScrollPane();
	commandKeywordPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	commandKeywordPane.setBounds(BOUNDS_COMM_PANE);
	commandKeywordPane.setOpaque(false);
	commandKeywordPane.setBorder(SETTINGS_EMPTY_BORDER);
	commandKeywordPane.getViewport().setOpaque(false);
	commandKeywordPane.setOpaque(false);
	commandKeywordPane.setVisible(true);
	commandKeywordPane.setDoubleBuffered(true);

	addScrollBar(commandKeywordPane);
	commandKeywordPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED_PIXELS);
	
	getContentPane().add(commandKeywordPane);
    }

    private void addScrollBar(JScrollPane scrollPane) {
	JScrollBar mainScrollBar = scrollPane.getVerticalScrollBar();
	mainScrollBar.setPreferredSize(new Dimension(SCROLL_THUMB_WIDTH, Integer.MAX_VALUE));
	mainScrollBar.setUI(new ScrollBarUI());
    }

    private void addCheckBoxes() {
	
	activeFeedbackCheckbox = new Checkbox("");
	activeFeedbackCheckbox.setFocusable(false);
	activeFeedbackCheckbox.setState(cfg.getIsActiveFeedbackEnabled());
	activeFeedbackCheckbox.setBackground(COLOR_UI_BACKGROUND);
	activeFeedbackCheckbox.setBounds(BOUNDS_ACTIVE_FEEDBACK_CHECKBOX);
	getContentPane().add(activeFeedbackCheckbox);
	
	
	defaultCollatedCheckbox = new Checkbox("");
	defaultCollatedCheckbox.setFocusable(false);
	defaultCollatedCheckbox.setState(cfg.getIsDefaultCollated());
	defaultCollatedCheckbox.setBackground(COLOR_UI_BACKGROUND);
	defaultCollatedCheckbox.setBounds(new Rectangle(846, 40, 25, 25));
	getContentPane().add(defaultCollatedCheckbox);
    }

    private void addFolderNameFields() {
	folder1Field = new FolderNameField(BOUNDS_FOLDER1_FIELD, folder1Name, this);
	folder2Field = new FolderNameField(BOUNDS_FOLDER2_FIELD, folder2Name, this);
	folder3Field = new FolderNameField(BOUNDS_FOLDER3_FIELD, folder3Name, this);
	folder4Field = new FolderNameField(BOUNDS_FOLDER4_FIELD, folder4Name, this);
	folder5Field = new FolderNameField(BOUNDS_FOLDER5_FIELD, folder5Name, this);
    }

    private void addCancelButton() {
	JButton btnCancel = new JButton(ICON_CANCEL_BUTTON);
	
	btnCancel.setBounds(BOUNDS_CANCEL_BUTTON);
	btnCancel.setContentAreaFilled(false);
	btnCancel.setBorder(BorderFactory.createEmptyBorder());
	btnCancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		executeQuit();
	    }
	});
	
	getContentPane().add(btnCancel);
    }
    
    private void loadFrameSettings(JFrame mainFrame) {
	setBackground(COLOR_UI_BACKGROUND);
	
	getContentPane().setBackground(COLOR_UI_BACKGROUND);
	setAlwaysOnTop(true);
	setLocationByPlatform(true);
	
	getContentPane().setLayout(null);
	
	addMainTitle();
	
	setLocationRelativeTo(mainFrame); 
	
	setResizable(false); 
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addMainTitle() {
	JLabel title = new JLabel(TITLE_PREFERENCES);
	title.setForeground(Color.WHITE);
	title.setFont(MainInterface.latoBold20);//new Font("Lato", Font.BOLD, 20));
	title.setHorizontalAlignment(SwingConstants.CENTER);
	title.setBounds(BOUNDS_MAIN_TITLE);
	getContentPane().add(title);
	setUndecorated(true); 
	setSize(DIMENSIONS_SETTINGS_PANE);
    }


    private void executeQuit() {
	MainInterface.getSettingsBtn().setEnabled(true);
	
	cfg = new Config();
	interpreter = new Interpreter(true);
	
	dispose();
    }
    
    private void checkKeywordDatabase() throws IllegalArgumentException {
	Properties checkPropDuplicate = new Properties();
	
	for (int i=0; i<commandKeywordItems.size(); i++) {
	    checkPropDuplicate.setProperty(commandDatabase[i][CONFIG_NAME_REF], commandKeywordItems.get(i).getInputText());
	}
	
	for (int i=0; i<paraKeywordItems.size(); i++) {
	    checkPropDuplicate.setProperty(parameterDatabase[i][CONFIG_NAME_REF], paraKeywordItems.get(i).getInputText());
	}
	
	new Interpreter(checkPropDuplicate);
	
	props = checkPropDuplicate;
    }
    
    private void saveKeywordDatabase() throws IllegalArgumentException {
	try {
	    checkKeywordDatabase();
	} catch (IllegalArgumentException e) {
	    logger.log(Level.WARNING, e.getMessage());
	    throw new IllegalArgumentException(e.getMessage());
	}
	
    }
    
    private void saveChanges() {

	
	try {
		saveKeywordDatabase();
		saveFolderSettings();
		saveCheckBoxes();
		
		// If no exceptions were found:
		writeToConfigFile();
		
		refreshMainInterface();
		executeQuit();
	} catch (IllegalArgumentException exceptionFeedback) {
	    warningLabel.setText(MESSAGE_SAVE_FAILED + exceptionFeedback.getMessage());
	}
    }

    private void saveCheckBoxes() {
	props.setProperty(BOOLEAN_REF_FEEDBACK_ENABLED, Boolean.toString(activeFeedbackCheckbox.getState()));
	props.setProperty(BOOLEAN_REF_DEFAULT_COLLATED, Boolean.toString(defaultCollatedCheckbox.getState()));
    }

    private void refreshMainInterface() {
	MainInterface.loadAllFolderLabels();
	MainInterface.updateAllFolderLabels();
	MainInterface.updateTaskDisplay();
    }
    
    private boolean isValidNameSize(String name) {
	return name.length() < MAX_FOLDER_NAME_LENGTH;
    }
    
    private void checkValidityOfFolderNames() throws IllegalArgumentException {
	if (isValidFolderName(folder1Field)) {
	    throw new IllegalArgumentException(String.format(MESSAGE_INVALID_FOLDER_NAME, ID_INT_FOLDER1));
	} else if (isValidFolderName(folder2Field)) {
	    throw new IllegalArgumentException(String.format(MESSAGE_INVALID_FOLDER_NAME, ID_INT_FOLDER2));
	} else if (isValidFolderName(folder3Field)) {
	    throw new IllegalArgumentException(String.format(MESSAGE_INVALID_FOLDER_NAME, ID_INT_FOLDER3));
	} else if (isValidFolderName(folder4Field)) {
	    throw new IllegalArgumentException(String.format(MESSAGE_INVALID_FOLDER_NAME, ID_INT_FOLDER4));
	} else if (isValidFolderName(folder5Field)) {
	    throw new IllegalArgumentException(String.format(MESSAGE_INVALID_FOLDER_NAME, ID_INT_FOLDER5));
	} 
	
	return;
    }

    private boolean isValidFolderName(FolderNameField nameField) {
	return !isValidNameSize(nameField.getText()) || nameField.getText().trim().isEmpty();
    }
    
    private void saveFolderProps() {
	defaultFolder = (String) defaultFolderSelector.getSelectedItem();
	props.setProperty(DEFAULT_FOLDER_ID_STRING, defaultFolder);
	
	props.setProperty(FOLDER1_ID_STRING, folder1Field.getText());
	props.setProperty(FOLDER2_ID_STRING, folder2Field.getText());
	props.setProperty(FOLDER3_ID_STRING, folder3Field.getText());
	props.setProperty(FOLDER4_ID_STRING, folder4Field.getText());
	props.setProperty(FOLDER5_ID_STRING, folder5Field.getText());
    }
    
    private void checkForDuplicateNames() throws IllegalArgumentException {
	ArrayList<String> currentNames = new ArrayList<String>();
	
	if (duplicateNameExists(currentNames, folder1Field)) {
	    currentNames.add(folder1Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException(String.format(MESSAGE_DUPLICATE_FOLDER_NAME, ID_INT_FOLDER1));
	}
	
	if (duplicateNameExists(currentNames, folder2Field)) {
	    currentNames.add(folder2Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException(String.format(MESSAGE_DUPLICATE_FOLDER_NAME, ID_INT_FOLDER2));
	}
	
	if (duplicateNameExists(currentNames, folder3Field)) {
	    currentNames.add(folder3Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException(String.format(MESSAGE_DUPLICATE_FOLDER_NAME, ID_INT_FOLDER3));
	}
	
	if (duplicateNameExists(currentNames, folder4Field)) {
	    currentNames.add(folder4Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException(String.format(MESSAGE_DUPLICATE_FOLDER_NAME, ID_INT_FOLDER4));
	}
	
	if (duplicateNameExists(currentNames, folder5Field)) {
	    currentNames.add(folder5Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException(String.format(MESSAGE_DUPLICATE_FOLDER_NAME, ID_INT_FOLDER5));
	}
	
    }

    private boolean duplicateNameExists(ArrayList<String> currentNames, FolderNameField folderName) {
	return !currentNames.contains(folderName.getText().toLowerCase());
    }
    
    private void saveFolderSettings() throws IllegalArgumentException {
	
	try {
		checkValidityOfFolderNames();
		checkForDuplicateNames();
		
	} catch (IllegalArgumentException e) {
	    throw new IllegalArgumentException(e.getMessage());
	}
	
	saveFolderProps();
	
    }

    private void writeToConfigFile() {
	try {
	    props.store(new FileOutputStream(FILENAME_CONFIG_FILE), null);
	} catch (FileNotFoundException e) {
	    logger.log(Level.SEVERE, MESSAGE_CONFIG_WRITE_FAILED + e.getStackTrace());
	} catch (IOException e) {
	    logger.log(Level.SEVERE,MESSAGE_CONFIG_WRITE_FAILED + e.getStackTrace());
	}
    }
    
}
