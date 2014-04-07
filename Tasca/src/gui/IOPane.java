package gui;

import io.Exporter;
import io.Importer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;


public class IOPane extends JFrame {
    
    private static final String INVALID_FILENAME_REGEX = 
	    
    "# Match a valid Windows filename (unspecified file system).          \n" +
    "^                                # Anchor to start of string.        \n" +
    "(?!                              # Assert filename is not: CON, PRN, \n" +
    "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n" +
    "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n" +
    "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n" +
    "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n" +
    "  (?:\\.[^.]*)?                  # followed by optional extension    \n" +
    "  $                              # and end of string                 \n" +
    ")                                # End negative lookahead assertion. \n" +
    "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n" +
    "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n" +
    "$                                # Anchor to end of string.            ";
    
    
    private static final String MESSAGE_EXPORT_FAILED = "Export Failed: Invalid filename";
    private static final String MESSAGE_EXPORT_SUCCESSFUL = "Exported \"%1$s\" sucessfully";
    private static final String MESSAGE_SUCCESSFUL_IMPORT = " was successfully imported into TASCA";
    
    private static final Border SETTINGS_EMPTY_BORDER = BorderFactory.createEmptyBorder(0,0,0,0);

    private static final String CHAR_QUOTATION_MARKS = "\"";
    
    private static final String FILETYPE_NAME_ICS = "iCal ICS File";
    private static final String FILETYPE_ICS = "ics";
    private static final String UI_BACKGROUND_COLOR_HEX = "#272822";
    
    private static final ImageIcon ICON_CLOSE = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Close button.png"));
    private static final ImageIcon ICON_EXPORT = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Export Pane Button.png"));
    private static final ImageIcon ICON_PANE_GRAPHIC = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Export Pane Graphics.png"));
    private static final ImageIcon ICON_IMPORT = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Import Pane Button.png"));
    
    private static final long serialVersionUID = 1L;

    private static final String TITLELESS_JFRAME = "TitleLessJFrame";
    private int dragPosX = 0, dragPosY = 0;
    JLabel systemStatusMessage;
    
    private Controller controller;
    
    public IOPane(JFrame mainFrame, JLabel systemStatusMessage, Controller controller) {
	super(TITLELESS_JFRAME);
	linkMainInterfaceComponents(systemStatusMessage, controller);
	
	loadGuiComponents(mainFrame);
	activateWindowDrag();
	setVisible(true);
    }

    private void linkMainInterfaceComponents(JLabel systemStatusMessage,
	    Controller controller) {
	this.systemStatusMessage = systemStatusMessage;
	this.controller = controller;
    }

    private void loadGuiComponents(final JFrame mainInterfaceFrame) {
	MainInterface.getExportBtn().setEnabled(false);
	
	loadFrameSettings(mainInterfaceFrame); 
	
	addCloseButton();
	addExportPaneGraphic();
	
	addImportButton(mainInterfaceFrame);
	addExportButton(mainInterfaceFrame);
    }

    private void addImportButton(final JFrame mainInterfaceFrame) {
	JButton btnImport = new JButton(ICON_IMPORT);
	btnImport.setBounds(264, 83, 81, 34); // Coordinates and Size in pixels
	btnImport.setContentAreaFilled(false);
	btnImport.setBorder(BorderFactory.createEmptyBorder());
	btnImport.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		setAlwaysOnTop( false );
		activateImportFileFinder(mainInterfaceFrame);
	    }
	});  
	
	getContentPane().add(btnImport);
    }

    private void addExportPaneGraphic() {
	JLabel icon = new JLabel(ICON_PANE_GRAPHIC);
	icon.setBounds(160, 50, 80, 100); // Coordinates and Size in pixels
	
	getContentPane().add(icon);
    }

    private void loadFrameSettings(final JFrame mainInterfaceFrame) {
	setSize(400, 200); // Size in pixels
	setBackground(Color.decode(UI_BACKGROUND_COLOR_HEX));
	
	getContentPane().setBackground(Color.decode(UI_BACKGROUND_COLOR_HEX));
	getContentPane().setLayout(null);

	setUndecorated(true); 
	setAlwaysOnTop( true );
	setLocationByPlatform( true );
	
	setResizable(false); 
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(mainInterfaceFrame);
    }

    private void addExportButton(final JFrame mainFrame) {
	JButton btnExport = new JButton(ICON_EXPORT);
	
	btnExport.setBounds(55, 83, 81, 34); // Coordinates and Size in pixels
	btnExport.setContentAreaFilled(false);
	btnExport.setBorder(BorderFactory.createEmptyBorder());
	btnExport.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		setAlwaysOnTop( false );
		activateExportFileFinder(mainFrame);
	    }
	});  
	
	getContentPane().add(btnExport);
    }

    private void addCloseButton() {
	JButton btnClose = new JButton(ICON_CLOSE);

	btnClose.setBackground(Color.BLACK);
	btnClose.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		executeQuit();
	    }
	});
	
	btnClose.setBorderPainted(false);
	btnClose.setContentAreaFilled(false);
	btnClose.setBorder(SETTINGS_EMPTY_BORDER);
	
	btnClose.setBounds(373, 10, 17, 17); // Coordinates and Size in pixels
	
	getContentPane().add(btnClose);
    }
    
    private void activateImportFileFinder(JFrame mainFrame) {
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter(FILETYPE_NAME_ICS, FILETYPE_ICS);
	
	int userSelection = createOpenDialog(mainFrame, chooser, filter);
	
	if(userSelection == JFileChooser.APPROVE_OPTION) {
	   new Importer(chooser.getSelectedFile().getAbsolutePath(), controller);
	   systemStatusMessage.setText(CHAR_QUOTATION_MARKS + chooser.getSelectedFile().getName() + CHAR_QUOTATION_MARKS + MESSAGE_SUCCESSFUL_IMPORT);
	}
	
	MainInterface.updateTaskDisplay();
	executeQuit();
    }

    private int createOpenDialog(JFrame mainFrame, JFileChooser chooser,
	    FileNameExtensionFilter filter) {
	chooser.setFileFilter(filter);
	int returnVal = chooser.showOpenDialog(mainFrame);
	return returnVal;
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

    private void executeQuit() {
	MainInterface.getExportBtn().setEnabled(true);
	dispose();
    }

    private void activateExportFileFinder(final JFrame frame) {
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter(FILETYPE_NAME_ICS, FILETYPE_ICS);

	int userSelection = createSaveDialog(frame, chooser, filter);

	if(userSelection == JFileChooser.APPROVE_OPTION) {
	    String fileName = chooser.getSelectedFile().getName();

	    if (!isFilenameValid(fileName) ) {
		systemStatusMessage.setText(MESSAGE_EXPORT_FAILED);
		executeQuit();
	    }

	    saveToDestination(chooser, fileName);
	}

	executeQuit();
    }

    private void saveToDestination(JFileChooser chooser, String fileName) {
	String filePath = chooser.getSelectedFile().getAbsolutePath().replaceAll("/" + fileName, "");
	fileName = cleanFileName(fileName);
	
	new Exporter(filePath, fileName, controller);
	systemStatusMessage.setText(String.format(MESSAGE_EXPORT_SUCCESSFUL, fileName));
    }

    private int createSaveDialog(final JFrame frame, JFileChooser chooser,
	    FileNameExtensionFilter filter) {
	chooser.setFileFilter(filter);
	
	int returnVal = chooser.showSaveDialog(frame);
	return returnVal;
    }

    private String cleanFileName(String fileName) {
	fileName = fileName.replace(".", ""); 
	fileName = fileName.replace(".ics", ""); // .ics will be generated by iCal4j 
	fileName = fileName.replace(",", "");
	return fileName;
    }

    private static boolean isFilenameValid(String text)
    {
        Pattern pattern = Pattern.compile(INVALID_FILENAME_REGEX, 
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
        
        Matcher matcher = pattern.matcher(text);
        boolean isMatch = matcher.matches();
        return isMatch;
    }
}


