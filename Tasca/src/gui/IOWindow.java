package gui;

import io.Exporter;
import io.Importer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;

import controller.Controller;


public class IOWindow extends JFrame {
    
    private int dragPosX = 0, dragPosY = 0;
    JLabel systemStatusMessage;
    
    private Controller controller;
    
    public IOWindow(JFrame mainFrame, JLabel systemStatusMessage, Controller controller) {
	super("TitleLessJFrame");
	this.systemStatusMessage = systemStatusMessage;
	this.controller = controller;
	
	MainInterface.getExportButton().setEnabled(false);
	
	loadGuiComponents(mainFrame);
	
	activateWindowDrag();
	
	setVisible(true);
	
    }

    public void loadGuiComponents(final JFrame mainFrame) {
	setSize(400, 200);
	setBackground(Color.decode("#272822"));
	
	getContentPane().setBackground(Color.decode("#272822"));


	getContentPane().setLayout(null);

	setUndecorated(true); 
	
	setAlwaysOnTop( true );
	setLocationByPlatform( true );
	
	setResizable(false); 
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	setLocationRelativeTo(mainFrame); 
	
	JButton btnClose = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Close button.png")));

	btnClose.setBackground(Color.BLACK);
	btnClose.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		executeQuit();
	    }
	});
	
	btnClose.setBorderPainted(false);
	btnClose.setContentAreaFilled(false);
	btnClose.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	
	btnClose.setBounds(373, 10, 17, 17);
	
	getContentPane().add(btnClose);
	
	JButton btnExport = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Export Pane Button.png")));
	
	btnExport.setBounds(55, 83, 81, 34);
	btnExport.setContentAreaFilled(false);
	btnExport.setBorder(BorderFactory.createEmptyBorder());
	btnExport.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		setAlwaysOnTop( false );
		exportFileFinder(mainFrame);
	    }
	});  
	
	getContentPane().add(btnExport);
	
	JLabel icon = new JLabel(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Export Pane Graphics.png")));
	icon.setBounds(160, 50, 80, 100);
	
	getContentPane().add(icon);
	
	
	JButton btnImport = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Import Pane Button.png")));
	btnImport.setBounds(264, 83, 81, 34);
	btnImport.setContentAreaFilled(false);
	btnImport.setBorder(BorderFactory.createEmptyBorder());
	btnImport.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		setAlwaysOnTop( false );
		importFileFinder(mainFrame);
	    }
	});  
	
	getContentPane().add(btnImport);
	
    }
    
    public void importFileFinder(JFrame mainFrame) {
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("iCal ICS File", "ics");
	chooser.setFileFilter(filter);
	int returnVal = chooser.showOpenDialog(mainFrame);
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	   System.out.println("You chose to open this file: " +
	        chooser.getSelectedFile().getAbsolutePath());
	   
	   new Importer(chooser.getSelectedFile().getAbsolutePath(), controller);
	   
	   systemStatusMessage.setText("\"" + chooser.getSelectedFile().getName() + "\" was successfully imported into TASCA");
	}
	
	MainInterface.updateTaskDisplay();
	executeQuit();
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
    
    public void executeQuit() {
	MainInterface.getExportButton().setEnabled(true);
	dispose();
    }

    public void exportFileFinder(final JFrame frame) {
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("iCal ICS File", "ics");
	
	chooser.setFileFilter(filter);
	
	int returnVal = chooser.showSaveDialog(frame);
	if(returnVal == JFileChooser.APPROVE_OPTION) {
	    String fileName = chooser.getSelectedFile().getName();

	    if (!isFilenameValid(fileName) ) {

		systemStatusMessage.setText("Export Failed: Invalid filename");
		executeQuit();
	    }
	    
	    
	    
	    String filePath = chooser.getSelectedFile().getAbsolutePath().replaceAll("/" + fileName, "");
	    fileName = clearFileName(fileName);
	    
	    System.out.println(fileName + " " + filePath);
	    
	    new Exporter(filePath, fileName, controller);
	    
	    systemStatusMessage.setText("Exported \"" + fileName + "\" sucessfully");
	}
	
	 executeQuit();
    }

    public String clearFileName(String fileName) {
	fileName = fileName.replace(".", "");
	fileName = fileName.replace(".ics", "");
	fileName = fileName.replace(",", "");
	return fileName;
    }

    public static boolean isFilenameValid(String text)
    {
        Pattern pattern = Pattern.compile(
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
            "$                                # Anchor to end of string.            ", 
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
        Matcher matcher = pattern.matcher(text);
        boolean isMatch = matcher.matches();
        return isMatch;
    }
}


