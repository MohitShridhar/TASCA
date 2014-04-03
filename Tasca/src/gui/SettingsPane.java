package gui;

import interpreter.Config;
import interpreter.FolderName;
import interpreter.Interpreter;
import interpreter.ParameterType;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Checkbox;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;



public class SettingsPane extends JFrame {
    
    private static final int MAX_FOLDER_NAME_LENGTH = 16;
    private int dragPosX = 0, dragPosY = 0;
    private Checkbox activeFeedbackCheckbox;
    private Config cfg;
    private String folder1Name, folder2Name, folder3Name, folder4Name, folder5Name, defaultFolder;
    private JComboBox defaultFolderSelector;
    private Properties props;
    private JTextField folder1Field;
    private JTextField folder2Field;
    private JTextField folder3Field;
    private JTextField folder4Field;
    private JTextField folder5Field;
    private Interpreter interpreter;
    private JLabel warningLabel;
    private JLabel lblParameterKeywords;
    private JScrollPane commandKeywordPane, paraKeywordPane;
    
    private ArrayList<KeywordItem> commandKeywordItems = new ArrayList<KeywordItem>();
    private ArrayList<KeywordItem> paraKeywordItems = new ArrayList<KeywordItem>();

    private String[][] commandDatabase = new String[][]{
	    {"Add","add","How to use <>"},
	    {"Delete","delete","How to use <>"},
	    {"Clear Completed","clearCompleted","How to use <>"},
	    {"Clear", "clear","How to use <>"},
	    {"Modify", "modify","How to use <>"},
	    {"Mark", "mark","How to use <>"},
	    {"Unmark", "unmark","How to use <>"},
	    {"Search", "search","How to use <>"},
	    {"Now", "now","How to use <>"},
	    {"Today", "today","How to use <>"},
	    {"Tomorrow", "tomorrow","How to use <>"},
	    {"Week", "week","How to use <>"},
	    {"Month", "month","How to use <>"},
	    {"Undo", "undo","How to use <>"},
	    {"Redo", "redo","How to use <>"},
	    {"Show All", "displayAll","How to use <>"},
	    {"Non-Timed Tasks", "displayFloat","How to use <>"},
	    {"Show in Time", "display","How to use <>"},
	    {"Export", "export","How to use <>"},
	    {"Import", "import","How to use <>"},
	    {"Quit", "quit","How to use <>"}
    };
    
    private String[][] parameterDatabase = new String[][] {
	    {"Start Time","startTime","How to use <>"},
	    {"End Time", "endTime","How to use <>"},
	    {"Reminder Time", "reminderTime","How to use <>"},
	    {"Priority", "priority","How to use <>"},
	    {"Location", "location","How to use <>"},
	    {"Folder", "folder","How to use <>"},
	    {"Task ID", "taskID","How to use <>"}
    };
    
    public SettingsPane(JFrame mainFrame, Interpreter interpreter, Config cfg) {
	super("TitleLessJFrame");
	loadFrameSettings(mainFrame);
	
	MainInterface.getBtnSettings().setEnabled(false);
	
	activateWindowDrag();
	
	this.cfg = cfg;
	loadFolderSettings();
	
	loadGuiComponents();
	
	loadKeywordDatabase();
	
	setVisible(true);
	
	this.interpreter = interpreter;

    }
    
    private int getCurrentDefaultFolder(String folderId) {
	if (folderId.equals("folder1")) {
	    return 0;
	} else if (folderId.equals("folder2")) {
	    return 1;
	} else if (folderId.equals("folder3")) {
	    return 2;
	} else if (folderId.equals("folder4")) {
	    return 3;
	} else {
	    return 4;
	}
	
    }
    
    private void loadFolderSettings() {
	props = cfg.getConfigFile();
	
	defaultFolder = props.getProperty("default");
	folder1Name = props.getProperty("folder1");
	folder2Name = props.getProperty("folder2");
	folder3Name = props.getProperty("folder3");
	folder4Name = props.getProperty("folder4");
	folder5Name = props.getProperty("folder5");
	
	
    }
   

    public void activateWindowDrag() {
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
	    		//sets frame position when mouse dragged			
	    		setLocation (evt.getXOnScreen()-dragPosX,evt.getYOnScreen()-dragPosY);
	    					
	         }
	    });
    }
    
    private void loadKeywordDatabase() {
	
	commandKeywordItems.clear();
	
	JPanel commandPanel = new JPanel(new GridLayout(commandDatabase.length, 0, 0, 13));

	commandPanel.setBackground(Color.decode("#272822"));
	
	// Load command keywords:
	for (int i=0; i<commandDatabase.length; i++) {
	    KeywordItem keywordBar = new KeywordItem(commandDatabase[i][0], cfg.getProperty(commandDatabase[i][1]));

	    keywordBar.setPreferredSize(new Dimension(396, 41));
	    keywordBar.setToolTipText(commandDatabase[i][2]);
	    keywordBar.setVisible(true);
	    commandPanel.add(keywordBar);
	    
	    commandKeywordItems.add(keywordBar);
	}
	
	commandKeywordPane.setBounds(34, 195, 396, 155);
	commandKeywordPane.setViewportView(commandPanel);
	commandKeywordPane.setVisible(true);
	
	
	// Load parameter keywords:
	paraKeywordItems.clear();
	
	JPanel paraPanel = new JPanel(new GridLayout(parameterDatabase.length, 0, 0, 13));
	
	paraPanel.setBackground(Color.decode("#272822"));
	
	for (int i=0; i<parameterDatabase.length; i++) {
	    KeywordItem keywordBar = new KeywordItem(parameterDatabase[i][0], cfg.getProperty(parameterDatabase[i][1]));
	    
	    keywordBar.setPreferredSize(new Dimension(396, 41));
	    keywordBar.setToolTipText(parameterDatabase[i][2]);
	    keywordBar.setVisible(true);
	    paraPanel.add(keywordBar);
	    
	    paraKeywordItems.add(keywordBar);
	}
	
	
	paraKeywordPane.setBounds(490, 195, 396, 155);
	paraKeywordPane.setViewportView(paraPanel);
	paraKeywordPane.setVisible(true);
	
    }
    
    public void loadGuiComponents() {
	JButton btnCancel = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Settings Cancel Button.png")));
	
	btnCancel.setBounds(0, 366, 81, 34);
	btnCancel.setContentAreaFilled(false);
	btnCancel.setBorder(BorderFactory.createEmptyBorder());
	btnCancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		executeQuit();
	    }
	});    
	
	folder1Field = new JTextField();
	folder1Field.setFont(MainInterface.latoReg15); //(new Font("Lato", Font.PLAIN, 15));
	folder1Field.setForeground(Color.WHITE);
	folder1Field.setBackground(Color.decode("#443e3e"));
	folder1Field.setHorizontalAlignment(SwingConstants.CENTER);
	folder1Field.setBounds(171, 116, 141, 28);
	folder1Field.setText(folder1Name);
	folder1Field.setCaretColor(Color.WHITE);
	getContentPane().add(folder1Field);
	folder1Field.setColumns(10);
	
	folder2Field = new JTextField();
	folder2Field.setHorizontalAlignment(SwingConstants.CENTER);
	folder2Field.setForeground(Color.WHITE);
	folder2Field.setFont(MainInterface.latoReg15);//new Font("Lato", Font.PLAIN, 15));
	folder2Field.setColumns(10);
	folder2Field.setBackground(new Color(68, 62, 62));
	folder2Field.setBounds(308, 116, 140, 28);
	folder2Field.setText(folder2Name);
	folder2Field.setCaretColor(Color.WHITE);
	getContentPane().add(folder2Field);
	
	folder3Field = new JTextField();
	folder3Field.setHorizontalAlignment(SwingConstants.CENTER);
	folder3Field.setForeground(Color.WHITE);
	folder3Field.setFont(MainInterface.latoReg15);//new Font("Lato", Font.PLAIN, 15));
	folder3Field.setColumns(10);
	folder3Field.setBackground(new Color(68, 62, 62));
	folder3Field.setBounds(444, 116, 138, 28);
	folder3Field.setText(folder3Name);
	folder3Field.setCaretColor(Color.WHITE);
	getContentPane().add(folder3Field);
	
	folder4Field = new JTextField();
	folder4Field.setHorizontalAlignment(SwingConstants.CENTER);
	folder4Field.setForeground(Color.WHITE);
	folder4Field.setFont(MainInterface.latoReg15);//new Font("Lato", Font.PLAIN, 15));
	folder4Field.setColumns(10);
	folder4Field.setBackground(new Color(68, 62, 62));
	folder4Field.setBounds(578, 116, 140, 28);
	folder4Field.setText(folder4Name);
	folder4Field.setCaretColor(Color.WHITE);
	getContentPane().add(folder4Field);
	
	folder5Field = new JTextField();
	folder5Field.setHorizontalAlignment(SwingConstants.CENTER);
	folder5Field.setForeground(Color.WHITE);
	folder5Field.setFont(MainInterface.latoReg15);//new Font("Lato", Font.PLAIN, 15));
	folder5Field.setColumns(10);
	folder5Field.setBackground(new Color(68, 62, 62));
	folder5Field.setBounds(714, 116, 138, 28);
	folder5Field.setText(folder5Name);
	folder5Field.setCaretColor(Color.WHITE);
	getContentPane().add(folder5Field);
	
	activeFeedbackCheckbox = new Checkbox("New check box");
	activeFeedbackCheckbox.setFocusable(false);
	activeFeedbackCheckbox.setState(MainInterface.isActiveFeedbackEnabled());
	activeFeedbackCheckbox.setBackground(Color.decode("#272822"));
	activeFeedbackCheckbox.setBounds(170, 40, 25, 25);
	getContentPane().add(activeFeedbackCheckbox);
	
	getContentPane().add(btnCancel);
	
	commandKeywordPane = new JScrollPane();
	commandKeywordPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	commandKeywordPane.setBounds(45, 195, 396, 155);
	commandKeywordPane.setOpaque(false);
	commandKeywordPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	commandKeywordPane.getViewport().setOpaque(false);
	commandKeywordPane.setOpaque(false);
	commandKeywordPane.setVisible(true);
	commandKeywordPane.setDoubleBuffered(true);

	JScrollBar mainScrollBarCommand = commandKeywordPane.getVerticalScrollBar();
	mainScrollBarCommand.setPreferredSize(new Dimension(16, Integer.MAX_VALUE));
	mainScrollBarCommand.setUI(new MyScrollbarUI());

	commandKeywordPane.getVerticalScrollBar().setUnitIncrement(1);
	
	getContentPane().add(commandKeywordPane);
	
	paraKeywordPane = new JScrollPane();
	paraKeywordPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	paraKeywordPane.setBounds(490, 195, 396, 155);
	paraKeywordPane.setOpaque(false);
	paraKeywordPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	paraKeywordPane.getViewport().setOpaque(false);
	paraKeywordPane.setOpaque(false);
	paraKeywordPane.setVisible(true);
	paraKeywordPane.setDoubleBuffered(true);

	JScrollBar mainScrollBarPara = paraKeywordPane.getVerticalScrollBar();
	mainScrollBarPara.setPreferredSize(new Dimension(16, Integer.MAX_VALUE));
	mainScrollBarPara.setUI(new MyScrollbarUI());

	paraKeywordPane.getVerticalScrollBar().setUnitIncrement(1);
	
	getContentPane().add(paraKeywordPane);
	
	
	
	JButton btnSave = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Settings Save Button.png")));

	btnSave.setBounds(839, 366, 81, 34);
	btnSave.setContentAreaFilled(false);
	btnSave.setBorder(BorderFactory.createEmptyBorder());
	btnSave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		saveChanges();
	    }
	});    

	getContentPane().add(btnSave);
	
	defaultFolderSelector = new JComboBox();
	defaultFolderSelector.setFocusable(false);
	defaultFolderSelector.setFont(MainInterface.latoReg13);//new Font("Lato", Font.PLAIN, 13));
	defaultFolderSelector.setModel(new DefaultComboBoxModel(new String[] {"folder1", "folder2", "folder3", "folder4", "folder5"}));
	defaultFolderSelector.setSelectedIndex(getCurrentDefaultFolder(defaultFolder));
	defaultFolderSelector.setMaximumRowCount(5);
	defaultFolderSelector.setBounds(75, 116, 97, 27);
	getContentPane().add(defaultFolderSelector);
	
	lblParameterKeywords = new JLabel("PARAMETERS");
	lblParameterKeywords.setHorizontalAlignment(SwingConstants.CENTER);
	lblParameterKeywords.setForeground(Color.WHITE);
	lblParameterKeywords.setFont(MainInterface.latoReg14);//new Font("Lato", Font.PLAIN, 14));
	lblParameterKeywords.setBounds(588, 167, 201, 16);
	getContentPane().add(lblParameterKeywords);
	
	JLabel lblActiveInputFeedback = new JLabel("Active Input Feedback");
	lblActiveInputFeedback.setForeground(Color.WHITE);
	lblActiveInputFeedback.setFont(MainInterface.latoReg13);//new Font("Lato", Font.PLAIN, 13));
	lblActiveInputFeedback.setHorizontalAlignment(SwingConstants.CENTER);
	lblActiveInputFeedback.setBounds(21, 45, 157, 16);
	getContentPane().add(lblActiveInputFeedback);
	
	JLabel folderSelectionBackground = new JLabel(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Settings Folder Display.png")));
	folderSelectionBackground.setBounds(70, 81, 780, 60);
	getContentPane().add(folderSelectionBackground);
	
	warningLabel = new JLabel("");
	warningLabel.setForeground(Color.WHITE);
	warningLabel.setFont(MainInterface.latoBold16);//new Font("Lato", Font.BOLD, 16));
	warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
	warningLabel.setBounds(110, 364, 700, 28);
	getContentPane().add(warningLabel);
	
	JLabel commandKeywordsLabel = new JLabel("COMMANDS");
	commandKeywordsLabel.setForeground(Color.WHITE);
	commandKeywordsLabel.setHorizontalAlignment(SwingConstants.CENTER);
	commandKeywordsLabel.setFont(MainInterface.latoReg14);//new Font("Lato", Font.PLAIN, 14));
	commandKeywordsLabel.setBounds(130, 165, 201, 16);
	getContentPane().add(commandKeywordsLabel);
		
    }
    
    public void loadFrameSettings(JFrame mainFrame) {
	setBackground(Color.decode("#272822"));
	
	getContentPane().setBackground(Color.decode("#272822"));
	setAlwaysOnTop( true );
	setLocationByPlatform( true );
	
	getContentPane().setLayout(null);
	
	JLabel title = new JLabel("PREFERENCES");
	title.setForeground(Color.WHITE);
	title.setFont(MainInterface.latoBold20);//new Font("Lato", Font.BOLD, 20));
	title.setHorizontalAlignment(SwingConstants.CENTER);
	title.setBounds(369, 17, 182, 25);
	getContentPane().add(title);
	setUndecorated(true); 
	setSize(920, 400);
	
	setLocationRelativeTo(mainFrame); 
	
	setResizable(false); 
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void executeQuit() {
	MainInterface.getBtnSettings().setEnabled(true);
	cfg = new Config();
	interpreter = new Interpreter(true);
	
	dispose();
    }
    
    private void checkKeywordDatabase() throws IllegalArgumentException {
	Properties checkPropDuplicate = new Properties();
	
	for (int i=0; i<commandKeywordItems.size(); i++) {
	    checkPropDuplicate.setProperty(commandDatabase[i][1], commandKeywordItems.get(i).getInputText());
	}
	
	for (int i=0; i<paraKeywordItems.size(); i++) {
	    checkPropDuplicate.setProperty(parameterDatabase[i][1], paraKeywordItems.get(i).getInputText());
	}
	
	new Interpreter(checkPropDuplicate);
	
	props = checkPropDuplicate;
    }
    
    private void saveKeywordDatabase() throws IllegalArgumentException {
	try {
	    checkKeywordDatabase();
	} catch (IllegalArgumentException e) {
	    throw new IllegalArgumentException(e.getMessage());
	}
	
    }
    
    public void saveChanges() {
	MainInterface.setIsActiveFeedbackEnabled(activeFeedbackCheckbox.getState());
	try {
		saveKeywordDatabase();
		saveFolderSettings();
		
		// If successful:
		writeToConfigFile();
		
		MainInterface.loadFolderNames();
		MainInterface.setFolderLabels();
		MainInterface.updateTaskDisplay();
		
		executeQuit();
	} catch (IllegalArgumentException e) {
	    warningLabel.setText("SAVE FAILED: " + e.getMessage());
	}
    }
    
    private boolean isValidNameSize(String name) {
	return name.length() < MAX_FOLDER_NAME_LENGTH;
    }
    
    private void checkValidityOfFolderNames() throws IllegalArgumentException {
	if (!isValidNameSize(folder1Field.getText()) || folder1Field.getText().trim().isEmpty()) {
	    throw new IllegalArgumentException("Please specific a shorter name for Folder 1 or check that it's not empty");
	} else if (!isValidNameSize(folder2Field.getText()) || folder2Field.getText().trim().isEmpty()) {
	    throw new IllegalArgumentException("Please specific a shorter name for Folder 2 or check that it's not empty");
	} else if (!isValidNameSize(folder3Field.getText()) || folder3Field.getText().trim().isEmpty()) {
	    throw new IllegalArgumentException("Please specific a shorter name for Folder 3 or check that it's not empty");
	} else if (!isValidNameSize(folder4Field.getText()) || folder4Field.getText().trim().isEmpty()) {
	    throw new IllegalArgumentException("Please specific a shorter name for Folder 4 or check that it's not empty");
	} else if (!isValidNameSize(folder5Field.getText()) || folder5Field.getText().trim().isEmpty()) {
	    throw new IllegalArgumentException("Please specific a shorter name for Folder 5 or check that it's not empty");
	} 
	
	return;
    }
    
    private void saveFolderProps() {
	defaultFolder = (String) defaultFolderSelector.getSelectedItem();
	props.setProperty("default", defaultFolder);
	
	props.setProperty("folder1", folder1Field.getText());
	props.setProperty("folder2", folder2Field.getText());
	props.setProperty("folder3", folder3Field.getText());
	props.setProperty("folder4", folder4Field.getText());
	props.setProperty("folder5", folder5Field.getText());
    }
    
    private void checkForDuplicateNames() throws IllegalArgumentException {
	ArrayList<String> currentNames = new ArrayList<String>();
	
	if (!currentNames.contains(folder1Field.getText().toLowerCase())) {
	    currentNames.add(folder1Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException("Folder 1's name is a duplicate");
	}
	
	if (!currentNames.contains(folder2Field.getText().toLowerCase())) {
	    currentNames.add(folder2Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException("Folder 2's name is a duplicate");
	}
	
	if (!currentNames.contains(folder3Field.getText().toLowerCase())) {
	    currentNames.add(folder3Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException("Folder 3's name is a duplicate");
	}
	
	if (!currentNames.contains(folder4Field.getText().toLowerCase())) {
	    currentNames.add(folder4Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException("Folder 4's name is a duplicate");
	}
	
	if (!currentNames.contains(folder5Field.getText().toLowerCase())) {
	    currentNames.add(folder5Field.getText().toLowerCase());
	} else {
	    throw new IllegalArgumentException("Folder 5's name is a duplicate");
	}
	
    }
    
    public void saveFolderSettings() throws IllegalArgumentException {
	
	// Check that new folder names are not too long.
	try {
		checkValidityOfFolderNames();
		checkForDuplicateNames();
		
	} catch (IllegalArgumentException e) {
	    throw new IllegalArgumentException(e.getMessage());
	}
	
	saveFolderProps();
	
    }

    public void writeToConfigFile() {
	try {
	    props.store(new FileOutputStream("Config.cfg"), null);
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
}
