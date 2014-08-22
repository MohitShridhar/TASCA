package gui;

import interpreter.FolderName;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import controller.Controller;

import junit.framework.Assert;

//@author A0105912N
public class FolderTab extends JLayeredPane {
    
    public final static Logger logger = Controller.getLogger();
    
    private static final int INVALID_FOLDER_REF = -1;
    private static final int NUM_FOLDERS = 5;
    
    private static final String FILEPATH_IMAGE_CLICKED = "/GUI Graphics/Tab Clicked.gif";
    private static final String FILEPATH_IMAGE_NOT_CLICKED = "/GUI Graphics/Tab NotClicked.gif";
    
    private static final Dimension DIMENSIONS_FOLDER_TAB = new Dimension(177, 28);
    
    private static final Border SETTINGS_EMPTY_BORDER = BorderFactory.createEmptyBorder(0,0,0,0);
    private static final long serialVersionUID = 1L;

    private static ImageIcon tabNotClickedIcon, tabClickedIcon;
    private JButton folderBtn;
    	
    private FolderName folderName = FolderName.DEFAULT;
    
    private static Map<FolderName, FolderTab> folderNameMap = new HashMap<FolderName, FolderTab>();
    private JLabel folderLabel;
    
    static {
	
	try {
	    tabNotClickedIcon = new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_IMAGE_NOT_CLICKED))));
	    tabClickedIcon = new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_IMAGE_CLICKED))));
	} catch (IOException e) {
	    logger.log(Level.SEVERE, BufferedGraphics.MESSAGE_GRAPHICS_LOAD_FAILED + e.getStackTrace());
	    Assert.fail(BufferedGraphics.MESSAGE_GRAPHICS_LOAD_FAILED);
	}
    }
    
    
    public FolderTab(FolderName folderName, Point location, String folderLabelText) {
	
	loadTabSettings(location);
	linkMainInterfaceComponents(folderName);
	
	addFolderLabel(folderLabelText);
	addFolderButton(folderName);
	
    }

    private void addFolderButton(FolderName folderName) {
	
	folderBtn = new JButton("");
	folderBtn.setSize(DIMENSIONS_FOLDER_TAB);
	folderBtn.setOpaque(false);
	folderBtn.setFocusPainted(false);
	folderBtn.setBorderPainted(false);
	folderBtn.setContentAreaFilled(false);
	folderBtn.setBorder(SETTINGS_EMPTY_BORDER);
	
	attachMouseListener();
	initTabState(folderName);
	
	add(folderBtn);
    }

    private void attachMouseListener() {
	folderBtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		folderActivate();
	    }
	});
    }

    private void initTabState(FolderName folderName) {
	if (MainInterface.getDefaultFolder() == folderName) {
	    folderBtn.setIcon(tabClickedIcon);
	} else {
	    folderBtn.setIcon(tabNotClickedIcon);
	}
    }

    private void addFolderLabel(String folderLabelText) {
	folderLabel = new JLabel();
	folderLabel.setHorizontalAlignment(SwingConstants.CENTER);
	folderLabel.setFont(MainInterface.latoReg14); 
	folderLabel.setForeground(Color.WHITE);
	folderLabel.setSize(DIMENSIONS_FOLDER_TAB);
	folderLabel.setText(folderLabelText);
	add(folderLabel);
    }

    private void linkMainInterfaceComponents(FolderName folderName) {
	this.folderName = folderName;
	folderNameMap.put(folderName, this);
    }

    private void loadTabSettings(Point location) {
	this.setBorder(SETTINGS_EMPTY_BORDER);
	this.setLocation(location);
	this.setSize(DIMENSIONS_FOLDER_TAB);
    }

    public void folderActivate() {
	
	updateFolderReferences();
	
	if (MainInterface.getCurrFolder() != MainInterface.getPrevFolder()) {
	    executeTabSwitch();
	    MainInterface.updateTaskDisplay();
	}

    }

    private void executeTabSwitch() {
	
	folderBtn.setIcon(tabClickedIcon);
	folderNameMap.get(MainInterface.getPrevFolder()).clearTab();
	
    }
    
    private void updateFolderReferences() {
	
	MainInterface.setCycleRef(searchFolderRef(MainInterface.getFolderCycle(), folderName));
	MainInterface.setPrevFolder(MainInterface.getCurrFolder());
	MainInterface.setCurrFolder(folderName);
	
    }
    
    private static int searchFolderRef(FolderName[] folderCycle, FolderName currFolder) {
	
	int cycleRef = INVALID_FOLDER_REF;
	
	for (cycleRef=0; cycleRef<NUM_FOLDERS; cycleRef++) {
	    if (folderCycle[cycleRef] == currFolder) {
		return cycleRef;
	    }
	}
	
	assert(cycleRef != INVALID_FOLDER_REF);
	
	return cycleRef;
    }
    
    private void clearTab() {
	folderBtn.setIcon(tabNotClickedIcon);
    }
    
    public void setLabelText(String labelText) {
	folderLabel.setText(labelText);
    }
    
    public static void gotoFolder(FolderName nextFolder) {
	folderNameMap.get(nextFolder).folderActivate();
    }
    
 }
