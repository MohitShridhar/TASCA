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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
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


class HighlightDocumentFilter extends DocumentFilter {

    private DefaultHighlightPainter highlightPainter = new DefaultHighlightPainter(Color.YELLOW);
    private JTextPane textPane;
    private JFrame mainFrame;
    private AttributeSet duplicateParameter, normalSetting, parameterSetting, commandSetting;
    private Interpreter interpreter;
    private JLabel background, feedbackBackground, feedbackText;
    
    private Map<CommandType, Color> commandColors = new HashMap<CommandType, Color>();
    private Map<ParameterType, Color> parameterColors = new HashMap<ParameterType, Color>();
    private boolean hasColor = false;
    private String userInput = null;
    
    private Controller controller = new Controller();
    
    private StyledDocument doc;
    
    private void initColorMap() {
	
	// Command Color Coding:
	commandColors.put(CommandType.ADD, Color.green.brighter());
	commandColors.put(CommandType.MODIFY, Color.yellow.darker());
	commandColors.put(CommandType.DISPLAY_NOW, Color.cyan.darker());
	commandColors.put(CommandType.DISPLAY_TODAY, Color.cyan.darker());
	commandColors.put(CommandType.DISPLAY_TOMORROW, Color.cyan.darker());
	commandColors.put(CommandType.DISPLAY_WEEK, Color.cyan.darker());
	commandColors.put(CommandType.DISPLAY_MONTH, Color.cyan.darker());
	commandColors.put(CommandType.DISPLAY_ALL, Color.cyan);
	commandColors.put(CommandType.DISPLAY_IN_TIME, Color.cyan.darker());
	commandColors.put(CommandType.DISPLAY_ALL_FLOAT, Color.MAGENTA.brighter());
	commandColors.put(CommandType.DELETE, Color.getHSBColor(0.97f, 0.66f, 0.94f));
	commandColors.put(CommandType.DELETE_ALL_COMPLETED, Color.getHSBColor(0.97f, 0.66f, 0.94f));
	commandColors.put(CommandType.SEARCH, Color.getHSBColor(0.52f, 0.9f, 0.92f));
	commandColors.put(CommandType.MARK, Color.YELLOW.brighter());
	commandColors.put(CommandType.QUIT, Color.getHSBColor(0.97f, 0.66f, 0.94f));
	commandColors.put(CommandType.CLEAR, Color.getHSBColor(0.97f, 0.66f, 0.94f));
	commandColors.put(CommandType.UNDO, Color.orange);
	commandColors.put(CommandType.REDO, Color.PINK);
	commandColors.put(CommandType.EXPORT, Color.yellow.brighter());
	commandColors.put(CommandType.IMPORT, Color.getHSBColor(0.5f, 0.85f, 0.94f));
	commandColors.put(CommandType.INVALID, Color.white);
	
	
	// Parameter Color Coding:
	
	parameterColors.put(ParameterType.START_TIME, Color.getHSBColor(0.675f, 0.44f, 0.98f));
	parameterColors.put(ParameterType.END_TIME, Color.getHSBColor(0.97f, 0.66f, 0.94f));
	parameterColors.put(ParameterType.REMINDER_TIME, Color.getHSBColor(0.06f, 0.71f, 0.87f));
	parameterColors.put(ParameterType.PRIORITY, Color.getHSBColor(0.95f, 0.67f, 0.98f));
	parameterColors.put(ParameterType.LOCATION, Color.CYAN.brighter());
	parameterColors.put(ParameterType.FOLDER, Color.PINK);
	parameterColors.put(ParameterType.TASK_ID, Color.YELLOW.brighter());
	parameterColors.put(ParameterType.INVALID, Color.white);
	
	// START_TIME, END_TIME, REMINDER_TIME, PRIORITY, LOCATION, FOLDER, TASK_ID,
    }
    
    public Controller getController() {
	return controller;
    }
    
    public HighlightDocumentFilter(JFrame frame, final JTextPane textPane, Interpreter interpreter, JLabel background, JLabel feedbackText, JLabel feedbackBackground) {
        this.textPane = textPane;
        this.mainFrame = frame;
        this.interpreter = interpreter;
        this.background = background;
        this.feedbackBackground = feedbackBackground;
        this.feedbackText = feedbackText;      
        
        initColorMap();
        
        StyledDocument doc = textPane.getStyledDocument();
        
        duplicateParameter = new SimpleAttributeSet();
        StyleConstants.setItalic((MutableAttributeSet) duplicateParameter, true);
        
        normalSetting = new SimpleAttributeSet();
        StyleConstants.setBold((MutableAttributeSet) normalSetting, false);
        StyleConstants.setForeground((MutableAttributeSet) normalSetting, Color.WHITE);
        
        parameterSetting = new SimpleAttributeSet();
        StyleConstants.setBold((MutableAttributeSet) parameterSetting, false);
        
        commandSetting = new SimpleAttributeSet();
        StyleConstants.setBold((MutableAttributeSet) commandSetting, true);
        
        textPane.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        	    if (e.getKeyCode() == KeyEvent.VK_ENTER)
        	    {
        		processUserAction();
        	    }
        	}

        	public void processUserAction() {
        	    userInput = getUserInput();

        	    if (userInput != null) {
        		boolean quit = false;
        		
        		quit = controller.executeCommands(userInput);
        		
        		if (!quit) {
        		    LinkedList<Task> tasks = Logic.displayLL();
        		    TaskItem task = (TaskItem) MainInterface.getComponentByName("taskBar");
        		    task.loadDetails(tasks.get(0));
        		    task.setVisible(true);
        		    
        		    textPane.setText("");
        		} else {
        		    mainFrame.dispose();
        		    System.exit(0);        			
        		}
        	    }
        	}
        });
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, text.replaceAll("\\n", ""), attr);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
        
        updateBar(fb, interpreter);
    }
    

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
	
	 // TODO: intial spaces are considered when remove format 
	// TODO: Add duplicate parameters highlighting
        super.replace(fb, offset, length, text.replaceAll("\\n", ""), attrs);
        
	
        String allText = fb.getDocument().getText(0, fb.getDocument().getLength()).toLowerCase();
	String commandMatch = interpreter.getFirstWord(allText).toLowerCase();

        //addCommandColors(fb, offset, commandMatch); 
        addCommColors(commandMatch, allText, interpreter);
        
        String parameterMatches[] =  allText.split("-");
        
        for (int i=1; i<parameterMatches.length; i++) {
            String paraMatch = parameterMatches[i].toLowerCase();//interpreter.getFirstWord(parameterMatches[i]).trim();
           // addParameterColors(fb, offset, parameterMatches, i, paraMatch);
            
            // TODO: speed up/background threading
            
            addParaColors(paraMatch, allText, interpreter);
                          
        }
        
        updateBar(fb, interpreter);
    }

    

    public void updateBar(FilterBypass fb, Interpreter interpreter) throws BadLocationException {
	String allText = fb.getDocument().getText(0, fb.getDocument().getLength());
	Boolean successParse = false;
	String exceptionMsg = null;	
	Boolean emptyInput = checkEmptyInput(allText);
	
	
	userInput = null;

	try {
	    interpreter.processUserInput(allText);;
	    successParse = true;
	    
	    if (MainInterface.activeFeedbackEnabled) {
		feedbackText.setVisible(false);
		feedbackBackground.setVisible(false);
	    }
	    
	    userInput = allText;
	    
	    
	} catch(IllegalArgumentException | RewriteEmptyStreamException e) {
//	    System.out.println("Exception " + e);
	    successParse = false;

	    if (MainInterface.activeFeedbackEnabled) {
		feedbackBackground.setVisible(true);
		feedbackText.setVisible(true);
		feedbackText.setText(e.getMessage());
	    }

	}
        
        if (emptyInput) {
            background.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Empty Input Bar.gif")));
	    feedbackText.setVisible(false);
	    feedbackBackground.setVisible(false);
        } else if (successParse) {
            background.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Success Input Bar.gif")));
        } else {
            background.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Failed Input Bar.gif")));
        }
    }
    
    public String getUserInput(){
	return userInput;
    }
    
    public boolean checkEmptyInput(String allText) {
	return allText.trim().isEmpty() || allText == null;
    }
    
    
    private void addParaColors(String paraMatch, String allInput, Interpreter interpreter) {
	
	int startIndex = allInput.indexOf(paraMatch) - 1;
	
	
//	System.out.println("Para: " + paraMatch + " Start index " + startIndex + " end index " + endIndex );
//	System.out.println("Has next space: " +  hasNextSpace("-" + interpreter.getFirstWord(paraMatch), allInput));
	
//	if (para == ParameterType.INVALID && interpreter.interpretParameter(interpreter.getFirstWord(paraMatch)) != ParameterType.INVALID && !hasNextSpace("-" + interpreter.getFirstWord(paraMatch), allInput)) {
//	    System.out.println("Remove index start: " + (startIndex + interpreter.getFirstWord(paraMatch).length() + 1));
//	    
//	    textPane.getStyledDocument().setCharacterAttributes(startIndex + interpreter.getFirstWord(paraMatch).length() + 1, endIndex, normalSetting, true);
//	    return;
//	} 
//	else if (para == ParameterType.INVALID) {
//	    textPane.getStyledDocument().setCharacterAttributes(startIndex, endIndex, normalSetting, true);
//	    return;
//	}
	
	int paraLength = interpreter.getFirstWord(paraMatch).length();
	
//	System.out.println("Start index: " + startIndex + " End index: " +  (startIndex + paraLength));
	
	if (hasNextSpace(interpreter.getFirstWord("-" + paraMatch), allInput)) {
	    StyleConstants.setForeground((MutableAttributeSet) parameterSetting, parameterColors.get(interpreter.interpretParameter(interpreter.getFirstWord(paraMatch))));
	    textPane.getStyledDocument().setCharacterAttributes(startIndex, startIndex + paraLength, parameterSetting, true);
	    textPane.getStyledDocument().setCharacterAttributes(startIndex + paraLength + 1, startIndex + paraLength + 1, normalSetting, true);
	}

		
    }
    
    private void addCommColors (String commMatch, String allInput, Interpreter interpreter) {
	int startIndex = allInput.indexOf(commMatch);
	CommandType commandType = interpreter.interpretCommand(commMatch);
	
	if (commandType == CommandType.INVALID) {
	    StyleConstants.setForeground((MutableAttributeSet) commandSetting, commandColors.get(commandType));
	    textPane.getStyledDocument().setCharacterAttributes(startIndex, startIndex + commMatch.length(), normalSetting, true);
	}
	
	else if (hasNextSpace(commMatch, allInput)) {
	    
	    StyleConstants.setForeground((MutableAttributeSet) commandSetting, commandColors.get(commandType));
	    textPane.getStyledDocument().setCharacterAttributes(startIndex, startIndex + commMatch.length(), commandSetting, true);
	    textPane.getStyledDocument().setCharacterAttributes(startIndex + commMatch.length() + 1, startIndex + commMatch.length() + 1, normalSetting, true);
	}
	
    }

//    public void addParameterColors(FilterBypass fb, int offset,
//	    String[] parameterMatches, int i, String paraMatch)
//	    throws BadLocationException {
//	int startIndex = offset - paraMatch.length();
//	
//	if (startIndex >= 0) {
//
//	    String last = fb.getDocument().getText(startIndex, paraMatch.length()).trim();
//	    
//	    String lastChar = fb.getDocument().getText(fb.getDocument().getLength(), 1);
//	    
//	    System.out.println("last: " +  last  + " Command: " + interpreter.interpretParameter(last) + "start index: " + startIndex);
//	    //System.out.println("ParaMatch: " + paraMatch);
//	    System.out.println("Has next space for " + last + ": " + hasNextSpace(interpreter.getFirstWord(last), fb.getDocument().getText(0, fb.getDocument().getLength())));
//	    
//	    if (interpreter.interpretParameter(last) != ParameterType.INVALID && startIndex <  paraMatch.length() + previousParaLen(parameterMatches, i) && hasNextSpace(interpreter.getFirstWord(last), fb.getDocument().getText(0, fb.getDocument().getLength()))) {//&& lastChar.equals(" ")) {
//		
//		System.out.println("Start index: " + startIndex);
//	        StyleConstants.setForeground((MutableAttributeSet) parameterSetting, parameterColors.get(interpreter.interpretParameter(last)));
//	        textPane.getStyledDocument().setCharacterAttributes(startIndex-1, startIndex + paraMatch.length(), parameterSetting, true);
//	    } 
////	    else {
////		textPane.getStyledDocument().setCharacterAttributes(previousParaLen(parameterMatches, i), paraMatch.length()+1, normalSetting, true);
////	    }
//
//	} 
//    }
    
    private boolean hasNextSpace(String sub, String main) {
	
	int startIndex = main.indexOf(sub);
	int endIndex = startIndex + sub.length();
	
	if (endIndex > main.length() - 1) {
	    return false;
	} 
	
	if (main.charAt(endIndex) != ' ') {
	    return false;
	}
	
	return true;
    }
    
    private int previousParaLen(String[]  paraMatches, int num) {
	int length = 0;
	
	for (int i=0; i<num; i++) {
	    length += paraMatches[i].length();
	}
	
	System.out.println("Length: "+ length);
	
	return length + num - 1;
    }

//Superseeded:
    
//    public void addCommandColors(FilterBypass fb, int offset, String commandMatch)
//	    throws BadLocationException {
//	int startIndex = offset - commandMatch.length();
//        
//        //System.out.print("Start index: " + startIndex);
//        if (startIndex >= 0) {
//            
//            String last = fb.getDocument().getText(startIndex, commandMatch.length()).trim();
//            
////            if (startIndex < commandMatch.length()) {
////            	String firstWord = interpreter.getFirstWord(fb.getDocument().getText(0, fb.getDocument().getLength()));
////            }
//            
//            // last.equalsIgnoreCase(match)
//            //System.out.println("Intepreter: " + interpreter.interpretCommand(last));
//            String lastChar = fb.getDocument().getText(fb.getDocument().getLength()-1, 1);
//            
//            if (interpreter.interpretCommand(last) != CommandType.INVALID && startIndex <  commandMatch.length() + leadingSpacesCount(fb.getDocument().getText(0, fb.getDocument().getLength()-1)) && lastChar.equals(" ")) {
//                //textPane.getHighlighter().addHighlight(startIndex, startIndex + match.length(), highlightPainter);
//        	StyleConstants.setForeground((MutableAttributeSet) commandSetting, commandColors.get(interpreter.interpretCommand(last)));
//                textPane.getStyledDocument().setCharacterAttributes(startIndex, startIndex + commandMatch.length(), commandSetting, true);
//                hasColor = true;
//            } else {
//        	hasColor = false;
//            }
//            
//
//        } else if (startIndex < 0 ) {
//            textPane.getStyledDocument().setCharacterAttributes(0, commandMatch.length()+1, normalSetting, true);
//            hasColor = false;
//        }
//    }
    
    
    private int leadingSpacesCount(String string) {
	int count = 0;
	
	for (int i=0; i<string.length(); i++) {
	    if (string.charAt(i) == ' ') {
		count++;
	    } else {
		return count;
	    }
	}
	
	return count;
    }
    

}


public class MainInterface {

  // TODO: Add more help error messages. And integrate ID & Folder & time validity checkers. Implement all user exceptions
  // TODO: Save before OS Quit 
    
  private static final int NUM_FOLDERS = 5;  
    
  private static int posX=0,posY=0;
  
  private static MyTextPane textPane = null;
  
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
  
  private static JFrame frame = null;
  private static JFrame mainFrame;
  
  private static ImageIcon tabNotClicked;
  private static ImageIcon tabClicked;
  
  public static Font menloReg16 = null;
  public static Font latoReg15 = null;
  public static Font latoBold13 = null;
  
  public static Font menloReg = null;
  public static Font latoReg = null;
  
  private static String folder1Name, folder2Name, folder3Name, folder4Name, folder5Name;
  
  public static boolean activeFeedbackEnabled = true;
  
  private static FolderName currFolder, prevFolder, defaultFolder;
  
  private static FolderName folderCycle[] = {FolderName.FOLDER1, FolderName.FOLDER2, FolderName.FOLDER3, FolderName.FOLDER4, FolderName.FOLDER5};
  private static int cycleRef = 1;
  
  private static HashMap componentMap;

private static TaskItem taskBar;
  
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
	     
	     
	} catch (IOException|FontFormatException e) {
	     //Handle exception
	    e.printStackTrace();
	}
      
      
      
      menloReg16 = new Font("Menlo", Font.PLAIN, 16);
      latoReg15 = new Font("Lato", Font.PLAIN, 15);
      latoBold13 = new Font("Lato", Font.BOLD, 13);
      
      
  }

  private static void createComponentMap(JFrame mainFrame) {
      componentMap = new HashMap<String,Component>();
      Component[] components = mainFrame.getContentPane().getComponents();
      for (int i=0; i < components.length; i++) {
	  componentMap.put(components[i].getName(), components[i]);
	  System.out.println(components[i].getName());
      }
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
  
  
 private static void loadFolderNames(){
     Config cfg = new Config ();
     
     folder1Name = cfg.getFolderName(FolderName.FOLDER1);
     folder2Name = cfg.getFolderName(FolderName.FOLDER2);
     folder3Name = cfg.getFolderName(FolderName.FOLDER3);
     folder4Name = cfg.getFolderName(FolderName.FOLDER4);
     folder5Name = cfg.getFolderName(FolderName.FOLDER5);
     
     defaultFolder = cfg.getDefaultFolder();
     currFolder = defaultFolder;
     prevFolder = currFolder;
     
     cycleRef = Integer.parseInt( defaultFolder.toString().charAt(6) + "" ) - 1;
 }
 
 // TODO: Replace folder buttons with Common inheritance class
 public static void folder1Activate() {
     prevFolder = currFolder;
     currFolder = FolderName.FOLDER1;

     // Clear previous:
     clearPreviousTab(prevFolder);

     // Update:
     btnFolder1.setIcon(tabClicked);
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
     //frame.setComponentZOrder(btnFolder2, 0);
     //    	    frame.setComponentZOrder(folder2Label, 0);
 }
 
 public static void folder3Activate() {
     prevFolder = currFolder;
     currFolder = FolderName.FOLDER3;

     // Clear previous:
     clearPreviousTab(prevFolder);

     btnFolder3.setIcon(tabClicked);	    
     //frame.setComponentZOrder(btnFolder3, 0);
     //	    frame.setComponentZOrder(folder3Label, 0);    
 }
  

 public static void folder4Activate() {
     prevFolder = currFolder;
     currFolder = FolderName.FOLDER4;

     // Clear previous:
     clearPreviousTab(prevFolder);

     btnFolder4.setIcon(tabClicked);	    
     //frame.setComponentZOrder(btnFolder4, 0);
     //	    frame.setComponentZOrder(folder4Label,0);
 }
 
 public static void folder5Activate() {
     prevFolder = currFolder;
     currFolder = FolderName.FOLDER5;

     // Clear previous:
     clearPreviousTab(prevFolder);

     btnFolder5.setIcon(tabClicked);	    
     //frame.setComponentZOrder(btnFolder5, 0);
     //	    frame.setComponentZOrder(folder5Label, 0);
 }

public static void initGui(final JFrame frame) {
    
    new MainInterface();
    
    // Load folder names:
    loadFolderNames();
       
         
    Container mainContainer = frame.getContentPane();     
    
    
    JButton btnClose = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Close button.png")));
    
    btnClose.setBackground(Color.BLACK);
    btnClose.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    	    Controller controller = filter.getController();
    	    controller.executeCommands("quit");
    	    frame.dispose();
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
    
    taskBar = new TaskItem();
    taskBar.setLocation(82, 81);
    taskBar.setVisible(false);
    taskBar.setName("taskBar");
    frame.getContentPane().add(taskBar); 

    
    /* Task test */
    
    
    
    textPane = new MyTextPane(new DefaultStyledDocument());
    textPane.setOpaque(false);
    textPane.setText("memora vivere");
    textPane.setFont(menloReg16);
    textPane.setForeground(Color.WHITE);
    textPane.setFocusTraversalKeysEnabled(false);
    
    // COLOR CODING--------------------------------------------------------
    Interpreter interpreter = new Interpreter();
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
    
    
    folder1Label.setFont(latoBold13);
    folder1Label.setForeground(Color.WHITE);
    folder1Label.setBounds(56, 10, 61, 16);
    mainFrame.getContentPane().add(folder1Label);
    
    
    if (defaultFolder == FolderName.FOLDER1) {
    	btnFolder1.setIcon(tabClicked);
    } else {
	btnFolder1.setIcon(tabNotClicked);
    }
    
    btnFolder1.setBounds(0, 4, 177, 28);
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
    folder2Label.setFont(new Font("Lato", Font.BOLD, 13));
    folder2Label.setBounds(215, 10, 61, 16);
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
    folder3Label.setFont(new Font("Lato", Font.BOLD, 13));
    folder3Label.setBounds(374, 10, 61, 16);
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
    folder4Label.setFont(new Font("Lato", Font.BOLD, 13));
    folder4Label.setBounds(533, 10, 61, 16);
    mainFrame.getContentPane().add(folder4Label);
    
    folder5Label = new JLabel(folder5Name);
    folder5Label.setHorizontalAlignment(SwingConstants.CENTER);
    folder5Label.setForeground(Color.WHITE);
    folder5Label.setFont(new Font("Lato", Font.BOLD, 13));
    folder5Label.setBounds(694, 10, 61, 16);
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
    
    
    JLabel label = new JLabel(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/UI Background.png")));
    label.setBackground(Color.BLACK);
    label.setBounds(0, 0, 888, 500);
    mainContainer.add(label);
    
    createComponentMap(frame);
    
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
}

   

