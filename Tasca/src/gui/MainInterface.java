package gui;

import interpreter.Command;
import interpreter.CommandType;
import interpreter.Config;
import interpreter.FolderName;
import interpreter.Interpreter;
import interpreter.ParameterType;
import io.Exporter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;

import org.antlr.runtime.tree.RewriteEmptyStreamException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import controller.Controller;


import storage.AllTasks;
import storage.FloatingTask;
import storage.Reminder;
import storage.Task;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import logic.Logic;


class MyTextPane extends JTextPane {
    public MyTextPane() {
	super();
    }
    
    public MyTextPane(StyledDocument doc) {
	super(doc);
    }
    
    @Override
    public void replaceSelection(String content) {
	    getInputAttributes().removeAttribute(StyleConstants.Foreground);
	    getInputAttributes().removeAttribute(StyleConstants.Bold);
	    super.replaceSelection(content);
    }
}

public class MainInterface {

  private static final int INPUT_HISTORY_SIZE = 30;

// TODO: Add more help error messages. And integrate ID & Folder & time validity checkers. Implement all user exceptions
  // TODO: Save before OS Quit 
  public static Controller controller = new Controller();
   
  private static final int NUM_FOLDERS = 5;  
    
  private static int posX=0,posY=0;
  
  private static MyTextPane textPane = null;
  
  private static List<InputHistory.Memento> savedUserInput = new ArrayList<InputHistory.Memento>();
  private static InputHistory inputHistory = new InputHistory();
  
  private static JButton btnFolder2 = new JButton("");
  private static JButton btnFolder1 = new JButton("");
  private static JButton btnFolder3 = new JButton("");
  private static JButton btnFolder4 = new JButton("");
  private static JButton btnFolder5 = new JButton("");
  
  private static JLabel folder1Label = null;
  private static JLabel folder2Label = null;
  private static JLabel folder3Label = null;
  private static JLabel folder4Label = null;
  private static JLabel folder5Label = null;
  
  private static String userInput = null;
  private static HighlightDocumentFilter filter = null;
  
  public static int inputNumRef = -1;
  public static int inputHistorySize = -1;
  
  private static JFrame frame = null;
  private static JFrame mainFrame;
  
  private static ImageIcon tabNotClicked;
  private static ImageIcon tabClicked;
  
  public static Font menloReg16 = null;
  public static Font latoReg15 = null;
  public static Font latoReg14 = null;
  public static Font latoReg12 = null;
  public static Font latoBold13 = null;
  public static Font lucidaReg22 = null;
  public static Font latoBold20 = null;
  public static Font latoBold16 = null;
  public static Font latoReg13 = null;
  
  
  public static Font menloReg = null;
  public static Font latoReg = null;
  
  private static String folder1Name, folder2Name, folder3Name, folder4Name, folder5Name;
  
  public static boolean activeFeedbackEnabled = true;
  
  private static FolderName currFolder, prevFolder, defaultFolder;
  
  private static FolderName folderCycle[] = {FolderName.FOLDER1, FolderName.FOLDER2, FolderName.FOLDER3, FolderName.FOLDER4, FolderName.FOLDER5};
  private static int cycleRef = 1;
  
  private static HashMap componentMap;


private static LinkedList<Reminder> currentTimedTasks;
private static LinkedList<FloatingTask> currentFloatingTasks;

private static JScrollPane taskPane;

private static JScrollPane twin;

private static Interpreter interpreter;

private static Config cfg;

private static JLabel emptyTaskListMsg;

public static JLabel systemStatusMessage;

private static JButton btnSettings;

private static JButton btnExport;
  
//  public static enum FolderName {
//      folder1, folder2, folder3, folder4, folder5
//      
//  }
//  
  public MainInterface() {
      
//      try {
//	menloReg = Font.createFont(Font.TRUETYPE_FONT, MainInterface.class.getResourceAsStream("/GUI Graphics/Fonts/Menlo.ttf"));
//	latoReg = Font.createFont(Font.TRUETYPE_FONT, MainInterface.class.getResourceAsStream("/GUI Graphics/Fonts/Lato-Reg.ttf"));
//    } catch (FontFormatException | IOException e1) {
//	// TODO Auto-generated catch block
//	e1.printStackTrace();
//    }
      
      try {
	     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MainInterface.class.getResourceAsStream("/GUI Graphics/Fonts/Menlo.ttf")));
	     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MainInterface.class.getResourceAsStream("/GUI Graphics/Fonts/Lato-Reg.ttf")));
	     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, MainInterface.class.getResourceAsStream("/GUI Graphics/Fonts/Lucida Grande.ttf")));
	     
	} catch (IOException|FontFormatException e) {
	     //Handle exception
	    e.printStackTrace();
	}
      
      
      
      menloReg16 = new Font("Menlo", Font.PLAIN, 16);
      
      latoBold20 = new Font("Lato", Font.BOLD, 20);
      latoBold16 = new Font("Lato", Font.BOLD, 16);
      latoBold13 = new Font("Lato", Font.BOLD, 13);
      
      latoReg15 = new Font("Lato", Font.PLAIN, 15);
      latoReg14 = new Font("Lato", Font.PLAIN, 14);
      latoReg13 = new Font("Lato", Font.PLAIN, 13);
      latoReg12 = new Font("Lato", Font.PLAIN, 12);
      
      lucidaReg22 = new Font("Lucida Grande", Font.PLAIN, 22);
      
      
      
  }
  
  public static boolean isActiveFeedbackEnabled() {
      return activeFeedbackEnabled;
  }
  
  public static void setIsActiveFeedbackEnabled(boolean isEnabled) {
      activeFeedbackEnabled = isEnabled;
  }

  public static Component getComponentByName(String name) {
      if (componentMap.containsKey(name)) {
	  return (Component) componentMap.get(name);
      }
      else return null;
  }
  
  public static String getUserInput() {
      return filter.getUserInput();
  }
  
  private static void clearPreviousTab (FolderName prevFolder) {
      switch (prevFolder) {
      case FOLDER1:
	  btnFolder1.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case FOLDER2:
	  btnFolder2.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case FOLDER3:
	  btnFolder3.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case FOLDER4:
	  btnFolder4.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case FOLDER5:
	  btnFolder5.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case DEFAULT:
	  //TODO: Need to manage default case
	  break;      
      default:
	  break;
      }
  }
  
//  private static class MyTextPane extends JTextPane {
//      
//      
//      public MyTextPane() {
//          super();
//
//          SimpleAttributeSet set = new SimpleAttributeSet(getParagraphAttributes());
// //         StyleConstants.setLineSpacing(set , 2);
//          StyleConstants.setForeground(set, Color.WHITE);
//          
//          // Set the attributes before adding text
//          setCharacterAttributes(set, true);
////          setParagraphAttributes(set, true);
//          setText("add new");
////          setForeground(Color.WHITE);
//          
//          
//          setOpaque(false);
//
//          // this is needed if using Nimbus L&F - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960
//          setBackground(Color.BLACK);
//          
//      }
//      
//      @Override
//      protected void paintComponent(Graphics g) {
//          // set background green - but can draw image here too
//	  try {
//	    Image img = ImageIO.read(MainInterface.class.getResourceAsStream("/GUI Graphics/Empty Input Bar.gif"));
//	    g.drawImage(img, 0, 0, 814,//(int) getSize().getWidth(),
//                   46, this); //(int) getSize().getHeight(), this);
//	    
//	} catch (IOException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	}
//
//
//          super.paintComponent(g);
//      }
//
//
//      
//  }
  

    
  public static void main(String[] args)  {
      
     
      
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {

	      mainFrame = new JFrame("TitleLessJFrame");
	      
	      mainFrame.setBackground(UIManager.getColor("Label.disabledShadow"));
	      mainFrame.getContentPane().setLayout(null);
	      mainFrame.setUndecorated(true); 
	      mainFrame.setSize(888, 500);
	      mainFrame.setResizable(false); 
	      mainFrame.setLocationRelativeTo(null); 
	      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      initGui(mainFrame);
	      
	      
	      mainFrame.setVisible(true);
	      
//	      new SettingsPane(mainFrame);
	      
          }
      });
      
      
//      java.awt.EventQueue.invokeLater(new Runnable() {
//	  
//	  
//	  @Override
//	  public void run() {
//	      
//	      
//	      JFrame frame = new JFrame("TitleLessJFrame");
//	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	      initGui(frame);
//	      frame.setVisible(true);
//	      
//	  }
//      });
//     

  }
  

  private static LinkedList<Reminder> folderSortTimedTasks(LinkedList<Reminder> original) {
      LinkedList<Reminder> sortedList = new LinkedList<Reminder>();
      
      for (int i=0; i<original.size(); i++) {
	  FolderName folder = cfg.getFolderId(original.get(i).getTask().getFolder());
	  
	  if (isCurrentFolder(folder)) {
	      sortedList.add(original.get(i));
	  }
      }
      
      return sortedList;
  }


public static boolean isCurrentFolder(FolderName folder) {
    return folder == currFolder || (folder == FolderName.DEFAULT && defaultFolder == currFolder);
}
  
  private static LinkedList<FloatingTask> folderSortFloatingTasks(LinkedList<FloatingTask> original) {
      LinkedList<FloatingTask> sortedList = new LinkedList<FloatingTask>();
      
      for (int i=0; i<original.size(); i++) {
	  FolderName folder = cfg.getFolderId(original.get(i).getFolder());
	  
	  if (isCurrentFolder(folder)) {
	      sortedList.add(original.get(i));
	  }
      }
      
      return sortedList;
  }
  
  
 public static void updateTaskDisplay() {
     int scrollPos = getScrollPos();
     
     currentTimedTasks = controller.getCurrentSystemState().getTimedList();
     currentFloatingTasks = controller.getCurrentSystemState().getFloatingList();
     
     LinkedList<Reminder> folderSortedTimedTasks = folderSortTimedTasks(currentTimedTasks); 
     LinkedList<FloatingTask> folderSortedFloatingTasks = folderSortFloatingTasks(currentFloatingTasks);
     
     if (folderSortedTimedTasks.size() + folderSortedFloatingTasks.size() == 0) {
	 taskPane.setVisible(false);
	 emptyTaskListMsg.setVisible(true);
	 
	 return;
     } else {
	 emptyTaskListMsg.setVisible(false);
     }
     
     JPanel tempPanel = new JPanel(new GridLayout(folderSortedTimedTasks.size() + folderSortedFloatingTasks.size(), 0, 0, 13));
     
     tempPanel.setBackground(Color.decode("#272822"));
     
     Interpreter.clearGuiIdMap(); 
     
     int i, j;
     
     // Iterate through timed tasks:
     for (i=0; i< folderSortedTimedTasks.size(); i++) {
	 TaskItem taskBar = new TaskItem(textPane, controller, i+1, interpreter);
	 
	 taskBar.loadTimedTaskDetails(folderSortedTimedTasks.get(i).getTask(), i+1, folderSortedTimedTasks.get(i).getReminderTime());
	 Interpreter.addGuiId(i+1, folderSortedTimedTasks.get(i).getTask().getTaskID());
	 
	 
	 taskBar.setPreferredSize(new Dimension(888, 40));
	 taskBar.setVisible(true);
	 tempPanel.add(taskBar);
	 	 
     }
     
     // Iterate through floating tasks:
     for (j = 0; j< folderSortedFloatingTasks.size(); j++) {
	 TaskItem taskBar = new TaskItem(textPane, controller, j+1+i, interpreter);
	 
	 taskBar.loadFloatingTaskDetails(folderSortedFloatingTasks.get(j), j+1+i);
	 
	 Interpreter.addGuiId(j+1+i, folderSortedFloatingTasks.get(j).getTaskID());
	 
	 taskBar.setPreferredSize(new Dimension(888, 40));
	 taskBar.setVisible(true);
	 tempPanel.add(taskBar);
     }
     
     double preferredHeight = tempPanel.getPreferredSize().getHeight(); 
     
     if (preferredHeight < 262) {
     	taskPane.setSize(tempPanel.getPreferredSize());

     } else {
	taskPane.setBounds(0, 80, 888, 262);
     }
     
     
     taskPane.setViewportView(tempPanel);
     
     taskPane.setVisible(true);
     
     setScollPos(scrollPos);
     
     systemStatusMessage.setText(controller.getSystemMessageString());
          
 }
 
 public static void clearTextPane() {
     	
     if (savedUserInput.size() > INPUT_HISTORY_SIZE) {
	 savedUserInput.clear();
	 inputNumRef = -1;
     }
     
     if (!textPane.getText().trim().isEmpty()) {
	 inputHistory.set(textPane.getText());
	 savedUserInput.add(inputHistory.saveToMemento());
	 inputNumRef++;
	 inputHistorySize++;
     }

     textPane.setText("");
 }
 
  
 public static void loadFolderNames(){
     cfg = new Config ();
     
     folder1Name = cfg.getFolderName(FolderName.FOLDER1);
     folder2Name = cfg.getFolderName(FolderName.FOLDER2);
     folder3Name = cfg.getFolderName(FolderName.FOLDER3);
     folder4Name = cfg.getFolderName(FolderName.FOLDER4);
     folder5Name = cfg.getFolderName(FolderName.FOLDER5);
     
     defaultFolder = cfg.getDefaultFolder();
 }
 
 // TODO: Replace folder buttons with Common inheritance class
 public static void folder1Activate() {
     prevFolder = currFolder;
     currFolder = FolderName.FOLDER1;

     // Clear previous:
     clearPreviousTab(prevFolder);

     // Update:
     btnFolder1.setIcon(tabClicked);
     
     updateTaskDisplay();
     //frame.setComponentZOrder(btnFolder1, 0);
     //frame.setComponentZOrder(folder1Label, 0);
 }
 
 public static void folder2Activate() {
     prevFolder = currFolder;
     currFolder = FolderName.FOLDER2;

     // Clear previous:
     clearPreviousTab(prevFolder);

     // Update:
     btnFolder2.setIcon(tabClicked);
     
     updateTaskDisplay();
     //frame.setComponentZOrder(btnFolder2, 0);
     //    	    frame.setComponentZOrder(folder2Label, 0);
 }
 
 public static void folder3Activate() {
     prevFolder = currFolder;
     currFolder = FolderName.FOLDER3;

     // Clear previous:
     clearPreviousTab(prevFolder);

     btnFolder3.setIcon(tabClicked);	
     
     updateTaskDisplay();
     //frame.setComponentZOrder(btnFolder3, 0);
     //	    frame.setComponentZOrder(folder3Label, 0);    
 }
  

 public static void folder4Activate() {
     prevFolder = currFolder;
     currFolder = FolderName.FOLDER4;

     // Clear previous:
     clearPreviousTab(prevFolder);

     btnFolder4.setIcon(tabClicked);
     
     updateTaskDisplay();
     //frame.setComponentZOrder(btnFolder4, 0);
     //	    frame.setComponentZOrder(folder4Label,0);
 }
 
 public static void folder5Activate() {
     prevFolder = currFolder;
     currFolder = FolderName.FOLDER5;

     // Clear previous:
     clearPreviousTab(prevFolder);

     btnFolder5.setIcon(tabClicked);	    
     updateTaskDisplay();
     //frame.setComponentZOrder(btnFolder5, 0);
     //	    frame.setComponentZOrder(folder5Label, 0);
 }

 public static int getScrollPos() {
     return taskPane.getVerticalScrollBar().getValue();
 }
 
 public static void setScollPos(int pos) {
     taskPane.getVerticalScrollBar().setValue(pos);
 }
 
 
 public static JButton getBtnSettings() {
     return btnSettings;
 }
 
 public static JButton getExportButton() {
     return btnExport;
 }
 
 public static void setFolderLabels() {
     folder1Label.setText(folder1Name);
     folder2Label.setText(folder2Name);
     folder3Label.setText(folder3Name);
     folder4Label.setText(folder4Name);
     folder5Label.setText(folder5Name);
     
 }
 
public static void initGui(final JFrame frame) {
    
    new MainInterface();
    
    // Load folder names:
    loadFolderNames();
    intiateFolderState();
    
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

        public void run() {
            controller.executeCommands("quit");
        }
    }));
       
         
    Container mainContainer = frame.getContentPane();     
    
    
    JButton btnClose = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Close button.png")));
    
    btnClose.setBackground(Color.BLACK);
    btnClose.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    	    //frame.dispose();
    	    System.exit(0);
    	}
    });
    
    // Text pane:
    
//    MyTextPane textPane = new MyTextPane();
//    textPane.setText("add task");
//    
//    
//    	
//    textPane.setFont(new Font("Menlo", Font.PLAIN, 16));
//    
//    DefaultCaret caret = (DefaultCaret)textPane.getCaret();
//    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
//    
//    textPane.setLocation(37, 412);
//    textPane.setSize(814, 46);
//    textPane.setMargin(new Insets(13, 10, 13, 25));
//    textPane.setCaretPosition(textPane.getDocument().getLength());
// 
//    
//    JScrollPane scrollPane = new JScrollPane(textPane);
//    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
//    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//    scrollPane.setLocation(37, 412);
//    scrollPane.setSize(814, 46);
//    scrollPane.setPreferredSize(new Dimension(814, 46));
//    scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//    
//    
//    scrollPane.setViewportView(textPane);   
//    
//    frame.getContentPane().add(scrollPane);
   
    /* Task Image Buffer */
    	
    
    /* Task Image Buffer */
    
    /* Task test */
    
    

//	TaskItem taskBar = new TaskItem(tasks.get(0));
    
    JLayeredPane layeredPane = new JLayeredPane();
    layeredPane.setBounds(0, 0, 888, 262 + 80);
    layeredPane.setOpaque(false);
    layeredPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    
    taskPane = new JScrollPane();
    taskPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    taskPane.setBounds(0, 80, 888, 262);
    taskPane.setOpaque(false);
    taskPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    taskPane.getViewport().setOpaque(false);
    taskPane.setOpaque(false);
    taskPane.setVisible(true);
    taskPane.setDoubleBuffered(true);
    //taskPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    
    JScrollBar mainScrollBar = taskPane.getVerticalScrollBar();
    mainScrollBar.setPreferredSize(new Dimension(16, Integer.MAX_VALUE));
    mainScrollBar.setUI(new MyScrollbarUI());
    
    taskPane.getVerticalScrollBar().setUnitIncrement(1);
    
    
   
    
//    twin = new JScrollPane();
//    twin.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//    twin.setBounds(0, 80, 21, 262);
//    twin.setOpaque(false);
//    twin.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//    twin.getViewport().setOpaque(false);
//    twin.setVisible(true);
//    JScrollBar twinScrollBar = twin.getVerticalScrollBar();
//    twinScrollBar.setPreferredSize(new Dimension(21, Integer.MAX_VALUE));
//    twinScrollBar.setUI(new MyScrollbarUI()); 
//    layeredPane.add(twin);
    
//    twin.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
////    twin.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
//    JScrollBar twinScrollBar = twin.getVerticalScrollBar();
//    twinScrollBar.setPreferredSize(new Dimension(21, Integer.MAX_VALUE));
//    twinScrollBar.setModel(mainScrollBar.getModel());    
//    
//    
//    layeredPane.add(twin);
    
    
    layeredPane.add(taskPane);
    
    frame.getContentPane().add(layeredPane);

    
    /* Task test */
    
    textPane = new MyTextPane(new DefaultStyledDocument());
    textPane.setOpaque(false);
    textPane.setText("memora vivere");
    textPane.setFont(menloReg16);
    textPane.setForeground(Color.WHITE);
    textPane.setFocusTraversalKeysEnabled(false);
    
    interpreter = new Interpreter();
    Interpreter.setIsGuiIdEnabled(true); 
    
    JLabel lblNewLabel = new JLabel("New label");
    JLabel feedbackText = new JLabel("Feed");
    JLabel feedbackBackground = new JLabel("");
    
    filter = (new HighlightDocumentFilter(frame, textPane, interpreter, lblNewLabel, feedbackText, feedbackBackground));
    
    ((AbstractDocument) textPane.getDocument()).setDocumentFilter(filter);
   
    
    //textPane.setMargin(new Insets(13, 10, 13, 25));
    
    JScrollPane scrollPane = new JScrollPane(textPane);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setLocation(50, 425);
    scrollPane.setSize(745, 19);
    scrollPane.setOpaque(false);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    
    
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setViewportView(textPane);
    frame.getContentPane().add(scrollPane);   
    
    emptyTaskListMsg = new JLabel("Just do it. Later.");
    emptyTaskListMsg.setForeground(Color.WHITE);
    emptyTaskListMsg.setHorizontalAlignment(SwingConstants.CENTER);
    emptyTaskListMsg.setFont(lucidaReg22);
    emptyTaskListMsg.setBounds(214, 200, 460, 28);
    emptyTaskListMsg.setVisible(false);
    
    systemStatusMessage = new JLabel("");
    systemStatusMessage.setHorizontalAlignment(SwingConstants.CENTER);
    systemStatusMessage.setBounds(0, 484, 888, 16);
    systemStatusMessage.setFont(latoReg12);
    systemStatusMessage.setForeground(Color.WHITE);
    
    mainFrame.getContentPane().add(systemStatusMessage);
    mainFrame.getContentPane().add(emptyTaskListMsg);
    
    
    btnClose.setBounds(862, 7, 17, 17);
    frame.getContentPane().add(btnClose);
    
    JButton btnMinimize = new JButton("");
    btnMinimize.setBackground(Color.BLACK);
    btnMinimize.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    frame.setState(JFrame.ICONIFIED);
    	}
    });
    btnMinimize.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Minimize Button.png")));
    btnMinimize.setBounds(836, 7, 18, 18);
    frame.getContentPane().add(btnMinimize);
    
    try {
	tabNotClicked = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif"))));
	tabClicked = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif"))));
    } catch (IOException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
    }
    
    
    folder1Label = new JLabel(folder1Name);
    folder1Label.setHorizontalAlignment(SwingConstants.CENTER);
    
    btnFolder1.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    	    cycleRef = 0;
    	    folder1Activate();

    	}
    });
    
    
    folder1Label.setFont(latoBold13);  // new Font("Lato", Font.BOLD, 13)
    folder1Label.setForeground(Color.WHITE);
    folder1Label.setBounds(-2, 4, 177, 28);
    mainFrame.getContentPane().add(folder1Label);
    
    
    if (defaultFolder == FolderName.FOLDER1) {
    	btnFolder1.setIcon(tabClicked);
    } else {
	btnFolder1.setIcon(tabNotClicked);
    }
    
    btnFolder1.setBounds(-2, 4, 177, 28);
    btnFolder1.setOpaque(false);
    btnFolder1.setFocusPainted(false);
    btnFolder1.setBorderPainted(false);
    btnFolder1.setContentAreaFilled(false);
    btnFolder1.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    
    frame.getContentPane().add(btnFolder1);
    
    btnFolder2.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    cycleRef = 1;
    	    folder2Activate();
    	}
    });
    
    
    folder2Label = new JLabel(folder2Name);
    folder2Label.setHorizontalAlignment(SwingConstants.CENTER);
    folder2Label.setForeground(Color.WHITE);
    folder2Label.setFont(latoBold13);
    folder2Label.setBounds(159, 4, 177, 28);
    mainFrame.getContentPane().add(folder2Label);
    
    if (defaultFolder == FolderName.FOLDER2) {
    	btnFolder2.setIcon(tabClicked);
    } else {
	btnFolder2.setIcon(tabNotClicked);
    }
    
    btnFolder2.setOpaque(false);
    btnFolder2.setFocusPainted(false);
    btnFolder2.setContentAreaFilled(false);
    btnFolder2.setBorderPainted(false);
    btnFolder2.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder2.setBounds(159, 4, 177, 28);
    frame.getContentPane().add(btnFolder2);
    btnFolder3.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    cycleRef = 2;
    	    folder3Activate();

    	}

    });
    
    folder3Label = new JLabel(folder3Name);
    folder3Label.setHorizontalAlignment(SwingConstants.CENTER);
    folder3Label.setForeground(Color.WHITE);
    folder3Label.setFont(latoBold13);
    folder3Label.setBounds(318, 4, 177, 28);
    mainFrame.getContentPane().add(folder3Label);
    
   
    if (defaultFolder == FolderName.FOLDER3) {
    	btnFolder3.setIcon(tabClicked);
    } else {
	btnFolder3.setIcon(tabNotClicked);
    }
    
    btnFolder3.setOpaque(false);
    btnFolder3.setFocusPainted(false);
    btnFolder3.setContentAreaFilled(false);
    btnFolder3.setBorderPainted(false);
    btnFolder3.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder3.setBounds(318, 4, 177, 28);
    frame.getContentPane().add(btnFolder3);
    
    
    
    btnFolder4.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    cycleRef = 3;
    	    folder4Activate();

    	}


    });
   
    
    folder4Label = new JLabel(folder4Name);
    folder4Label.setHorizontalAlignment(SwingConstants.CENTER);
    folder4Label.setForeground(Color.WHITE);
    folder4Label.setFont(latoBold13);
    folder4Label.setBounds(477, 4, 177, 28);
    mainFrame.getContentPane().add(folder4Label);
    
    folder5Label = new JLabel(folder5Name);
    folder5Label.setHorizontalAlignment(SwingConstants.CENTER);
    folder5Label.setForeground(Color.WHITE);
    folder5Label.setFont(latoBold13);
    folder5Label.setBounds(636, 4, 177, 28);
    mainFrame.getContentPane().add(folder5Label);
    
    
    if (defaultFolder == FolderName.FOLDER4) {
    	btnFolder4.setIcon(tabClicked);
    } else {
	btnFolder4.setIcon(tabNotClicked);
    }
    
    btnFolder4.setOpaque(false);
    btnFolder4.setFocusPainted(false);
    btnFolder4.setContentAreaFilled(false);
    btnFolder4.setBorderPainted(false);
    btnFolder4.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder4.setBounds(477, 4, 177, 28);
    frame.getContentPane().add(btnFolder4);
    btnFolder5.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    cycleRef = 4;
    	    folder5Activate();
    	}
    });
    
    
    if (defaultFolder == FolderName.FOLDER5) {
    	btnFolder5.setIcon(tabClicked);
    } else {
	btnFolder5.setIcon(tabNotClicked);
    }
    
    btnFolder5.setOpaque(false);
    btnFolder5.setFocusPainted(false);
    btnFolder5.setContentAreaFilled(false);
    btnFolder5.setBorderPainted(false);
    btnFolder5.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder5.setBounds(636, 4, 177, 28);
    
    frame.getContentPane().add(btnFolder5);
    
    
    feedbackText.setHorizontalAlignment(SwingConstants.CENTER);
    feedbackText.setForeground(Color.WHITE);
    feedbackText.setFont(latoReg15);
    feedbackText.setBounds(99, 373, 694, 18);
    frame.getContentPane().add(feedbackText);
    
    feedbackBackground.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Error Feedback Background.png")));
    feedbackBackground.setBounds(77, 361, 750, 52);
    frame.getContentPane().add(feedbackBackground);
    
    feedbackText.setVisible(false);
    feedbackBackground.setVisible(false);
    
    lblNewLabel.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Empty Input Bar.gif")));
    lblNewLabel.setBounds(37, 412, 814, 46);
    frame.getContentPane().add(lblNewLabel);
    
    btnSettings = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Settings Icon.png")));
    btnSettings.setBounds(505, 365, 27, 27);
    btnSettings.setContentAreaFilled(false);
    btnSettings.setBorder(BorderFactory.createEmptyBorder());
    btnSettings.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    new SettingsPane(frame, interpreter, cfg);
	}
    });     
    mainContainer.add(btnSettings);

    btnExport = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Export Icon.png")));
    btnExport.setBounds(351, 365, 26, 26);
    btnExport.setContentAreaFilled(false);
    btnExport.setBorder(BorderFactory.createEmptyBorder());
    btnExport.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    // TODO: Implement export feature
	    new IOWindow(frame, systemStatusMessage, controller);
	}
    });   

    mainContainer.add(btnExport);
    
    JLabel label = new JLabel(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/UI Background.png")));
    label.setBackground(Color.BLACK);
    label.setBounds(0, 0, 888, 500);
    mainContainer.add(label);
    
    frame.getContentPane().setBackground(Color.WHITE);
    
 
    
    frame.addMouseListener(new MouseAdapter()
    {
       public void mousePressed(MouseEvent e)
       {
          posX=e.getX();
          posY=e.getY();
       }
    });
    
    frame.addMouseMotionListener(new MouseAdapter()
    {
         public void mouseDragged(MouseEvent evt)
         {
    		//sets frame position when mouse dragged			
    		frame.setLocation (evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
    					
         }
    });
    
    KeyboardFocusManager.getCurrentKeyboardFocusManager()
    .addKeyEventDispatcher(new KeyEventDispatcher() {
	private long lastPressProcessed = 0;
	
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if(System.currentTimeMillis() - lastPressProcessed > 170) {
        	if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_TAB && !e.isShiftDown()) {
        	    cycleRef = (cycleRef + NUM_FOLDERS + 1) % NUM_FOLDERS;

        	    FolderName nextFolder = folderCycle[cycleRef];

        	    cycleTabsSwitchCase(nextFolder);

        	} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_TAB && e.isShiftDown()) {
        	    cycleRef = (cycleRef + NUM_FOLDERS - 1) % NUM_FOLDERS;
        	    FolderName nextFolder = folderCycle[cycleRef];

        	    cycleTabsSwitchCase(nextFolder);
        	} else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_UP) {
            	    if (inputNumRef >= 0) {
            		textPane.setText(inputHistory.restoreFromMemento(savedUserInput.get(inputNumRef)));
            		inputNumRef--;
            	    }
        	} else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_DOWN) {
        	    if (inputNumRef + 2 < savedUserInput.size()) {
        		inputNumRef++;
        		textPane.setText(inputHistory.restoreFromMemento(savedUserInput.get(inputNumRef+1)));
        	    }
        	}
        	
        	lastPressProcessed = System.currentTimeMillis();
            }
	    
	    return false;
        }
        
	public void cycleTabsSwitchCase(FolderName nextFolder) {
	    switch (nextFolder) {
	    case FOLDER1:
	        folder1Activate();
	        break;
	    case FOLDER2:
	        folder2Activate();
	        break;
	    case FOLDER3:
	        folder3Activate();
	        break;
	    case FOLDER4:
	        folder4Activate();
	        break;
	    case FOLDER5:
	        folder5Activate();
	        break;
	        
	    default:
	        break;
	    }
	}
  });
  
    //updateTaskDisplay();
    
   
    
//    textPane.addKeyListener(new java.awt.event.KeyAdapter() {
//	
//	public void keyPressed(java.awt.event.KeyEvent evt) {
//	    if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_TAB && !evt.isShiftDown()) {
//		cycleRef = (cycleRef + NUM_FOLDERS + 1) % NUM_FOLDERS;
//		
//		FolderName nextFolder = folderCycle[cycleRef];
//		
//		cycleTabsSwitchCase(nextFolder);
//		
//	    } else if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_TAB && evt.isShiftDown()) {
//		cycleRef = (cycleRef + NUM_FOLDERS - 1) % NUM_FOLDERS;
//		FolderName nextFolder = folderCycle[cycleRef];
//		
//		cycleTabsSwitchCase(nextFolder);
//	    }
//	}
//
//	public void cycleTabsSwitchCase(FolderName nextFolder) {
//	    switch (nextFolder) {
//	    case FOLDER1:
//	        folder1Activate();
//	        break;
//	    case FOLDER2:
//	        folder2Activate();
//	        break;
//	    case FOLDER3:
//	        folder3Activate();
//	        break;
//	    case FOLDER4:
//	        folder4Activate();
//	        break;
//	    case FOLDER5:
//	        folder5Activate();
//	        break;
//	        
//	    default:
//	        break;
//	    }
//	}
//    });
}

public static void intiateFolderState() {
    currFolder = defaultFolder;
    prevFolder = currFolder;
    
    cycleRef = Integer.parseInt( defaultFolder.toString().charAt(6) + "" ) - 1;
}



}



