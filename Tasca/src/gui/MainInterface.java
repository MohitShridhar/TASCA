package gui;

import interpreter.Command;
import interpreter.CommandType;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
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

import storage.AllTasks;
import storage.Task;
import javax.swing.SwingConstants;


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
    private AttributeSet duplicateParameter, normalSetting, parameterSetting, commandSetting;
    private Interpreter interpreter;
    private JLabel background;
    private Map<CommandType, Color> commandColors = new HashMap<CommandType, Color>();
    private Map<ParameterType, Color> parameterColors = new HashMap<ParameterType, Color>();
    private boolean hasColor = false;
    
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
	commandColors.put(CommandType.DISPLAY_ALL_FLOAT, Color.MAGENTA.darker());
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
    
    public HighlightDocumentFilter(JTextPane textPane, Interpreter interpreter, JLabel background) {
        this.textPane = textPane;
        this.interpreter = interpreter;
        this.background = background;
        
        initColorMap();
        
        StyledDocument doc = textPane.getStyledDocument();
        
        duplicateParameter = new SimpleAttributeSet();
        StyleConstants.setItalic((MutableAttributeSet) duplicateParameter, true);
        
        normalSetting = new SimpleAttributeSet();
        StyleConstants.setForeground((MutableAttributeSet) normalSetting, Color.WHITE);
        StyleConstants.setBold((MutableAttributeSet) normalSetting, false);
        
        parameterSetting = new SimpleAttributeSet();
        StyleConstants.setBold((MutableAttributeSet) parameterSetting, false);
        
        commandSetting = new SimpleAttributeSet();
        StyleConstants.setBold((MutableAttributeSet) commandSetting, true);
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, text, attr);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
        
        updateBar(fb, interpreter);
    }
    

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
	
	 // TODO: intial spaces are considered when remove format 
	// TODO: Add duplicate parameters highlighting;
	
        String commandMatch = interpreter.getFirstWord(fb.getDocument().getText(0, fb.getDocument().getLength()));
        super.replace(fb, offset, length, text, attrs);
        addCommandColors(fb, offset, commandMatch); 
        
        String parameterMatches[] =  fb.getDocument().getText(0, fb.getDocument().getLength()).split("-");
        
        for (int i=1; i<parameterMatches.length; i++) {
            String paraMatch = parameterMatches[i];//interpreter.getFirstWord(parameterMatches[i]).trim();
           // addParameterColors(fb, offset, parameterMatches, i, paraMatch);
            
            // TODO: speed up/background threading
            
            addParaColors(paraMatch, fb.getDocument().getText(0, fb.getDocument().getLength()), interpreter);
                          
        }
        
        updateBar(fb, interpreter);
    }

    public void updateBar(FilterBypass fb, Interpreter interpreter) throws BadLocationException {
	String allText = fb.getDocument().getText(0, fb.getDocument().getLength());
	Boolean successParse = false;
	
	try {
	    interpreter.processUserInput(allText);
	    successParse = true;
	} catch(IllegalArgumentException | RewriteEmptyStreamException e) {
//	    System.out.println("Exception " + e);
	    successParse = false;
	}
        
        if (allText.trim().isEmpty() || allText == null) {
            background.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Empty Input Bar.gif")));
        } else if (successParse) {
            background.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Success Input Bar.gif")));
        } else {
            background.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Failed Input Bar.gif")));
        }
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

    public void addCommandColors(FilterBypass fb, int offset, String commandMatch)
	    throws BadLocationException {
	int startIndex = offset - commandMatch.length();
        
        //System.out.print("Start index: " + startIndex);
        if (startIndex >= 0) {
            
            String last = fb.getDocument().getText(startIndex, commandMatch.length()).trim();
            
//            if (startIndex < commandMatch.length()) {
//            	String firstWord = interpreter.getFirstWord(fb.getDocument().getText(0, fb.getDocument().getLength()));
//            }
            
            // last.equalsIgnoreCase(match)
            //System.out.println("Intepreter: " + interpreter.interpretCommand(last));
            String lastChar = fb.getDocument().getText(fb.getDocument().getLength()-1, 1);
            
            if (interpreter.interpretCommand(last) != CommandType.INVALID && startIndex <  commandMatch.length() + leadingSpacesCount(fb.getDocument().getText(0, fb.getDocument().getLength()-1)) && lastChar.equals(" ")) {
                //textPane.getHighlighter().addHighlight(startIndex, startIndex + match.length(), highlightPainter);
        	StyleConstants.setForeground((MutableAttributeSet) commandSetting, commandColors.get(interpreter.interpretCommand(last)));
                textPane.getStyledDocument().setCharacterAttributes(startIndex, startIndex + commandMatch.length(), commandSetting, true);
                hasColor = true;
            } else {
        	hasColor = false;
            }
            

        } else if (startIndex < 0 ) {
            textPane.getStyledDocument().setCharacterAttributes(0, commandMatch.length()+1, normalSetting, true);
            hasColor = false;
        }
    }
    
    
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

  // TODO: Fix minimize flickering
  // TODO: not imp: fix dual screen drag
    
  private static int posX=0,posY=0;
  private static JButton btnFolder2 = new JButton("");
  private static JButton btnFolder1 = new JButton("");
  private static JButton btnFolder3 = new JButton("");
  private static JButton btnFolder4 = new JButton("");
  private static JButton btnFolder5 = new JButton("");
  
  private static Folder currFolder, prevFolder;
  
  public static enum Folder {
      folder1, folder2, folder3, folder4, folder5
  }
  
  public MainInterface() {
      
  }

  
  private static void clearPreviousTab (Folder prevFolder) {
      switch (prevFolder) {
      case folder1:
	  btnFolder1.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case folder2:
	  btnFolder2.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case folder3:
	  btnFolder3.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case folder4:
	  btnFolder4.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case folder5:
	  btnFolder5.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
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
  
  private static void appendToPane(JTextPane tp, String msg, Color c)
  {
      StyleContext sc = StyleContext.getDefaultStyleContext();
      AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

      aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
      aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

      int len = tp.getDocument().getLength();
      tp.setCaretPosition(len);
      tp.setCharacterAttributes(aset, false);
      tp.replaceSelection(msg);
  }

    
  public static void main(String[] args)  {
      
     
      
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {

	      JFrame frame = new JFrame("TitleLessJFrame");
	      frame.getContentPane().setLayout(null);
	      frame.setUndecorated(true); 
	      frame.setSize(888, 500);
	      frame.setResizable(false); 
	      frame.setLocationRelativeTo(null); 
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      initGui(frame);
	      
	      frame.setVisible(true);
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

public static void initGui(final JFrame frame) {
    
    
    currFolder = Folder.folder1;
     prevFolder = Folder.folder1;
     
         
    Container mainContainer = frame.getContentPane();     
    
    
    JButton btnClose = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Close button.png")));
    
    btnClose.setBackground(Color.BLACK);
    btnClose.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
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
   
    
    MyTextPane textPane = new MyTextPane(new DefaultStyledDocument());
    textPane.setOpaque(false);
    textPane.setText("add task");
    textPane.setFont(new Font("Menlo", Font.PLAIN, 16));
    textPane.setForeground(Color.WHITE);
    
    // COLOR CODING--------------------------------------------------------
    Interpreter interpreter = new Interpreter();
    JLabel lblNewLabel = new JLabel("New label");
    HighlightDocumentFilter filter = (new HighlightDocumentFilter(textPane, interpreter, lblNewLabel));
    
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
    
    
    btnFolder1.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    	    prevFolder = currFolder;
    	    currFolder = Folder.folder1;
    	    
    	    // Clear previous:
    	    clearPreviousTab(prevFolder);
    	        	    
    	    // Update:
    	    btnFolder1.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
    	    frame.setComponentZOrder(btnFolder1, 0);
    	}
    });
    btnFolder1.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
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
    	    prevFolder = currFolder;
    	    currFolder = Folder.folder2;
    	    
    	    // Clear previous:
    	    clearPreviousTab(prevFolder);
    	    
    	    // Update:
    	    btnFolder2.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
    	    frame.setComponentZOrder(btnFolder2, 0);
    	}
    });
    btnFolder2.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
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
    	    prevFolder = currFolder;
	    currFolder = Folder.folder3;
	    
	    // Clear previous:
	    clearPreviousTab(prevFolder);

	    btnFolder3.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
	    frame.setComponentZOrder(btnFolder3, 0);

    	}
    });
    
   
    btnFolder3.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
    btnFolder3.setOpaque(false);
    btnFolder3.setFocusPainted(false);
    btnFolder3.setContentAreaFilled(false);
    btnFolder3.setBorderPainted(false);
    btnFolder3.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder3.setBounds(320, 4, 177, 28);
    frame.getContentPane().add(btnFolder3);
    
    
    
    btnFolder4.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    prevFolder = currFolder;
	    currFolder = Folder.folder4;
	    
	    // Clear previous:
	    clearPreviousTab(prevFolder);

	    btnFolder4.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
	    frame.setComponentZOrder(btnFolder4, 0);

    	}
    });
    
    
    btnFolder4.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
    btnFolder4.setOpaque(false);
    btnFolder4.setFocusPainted(false);
    btnFolder4.setContentAreaFilled(false);
    btnFolder4.setBorderPainted(false);
    btnFolder4.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder4.setBounds(479, 4, 177, 28);
    frame.getContentPane().add(btnFolder4);
    btnFolder5.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    prevFolder = currFolder;
	    currFolder = Folder.folder5;
	    
	    // Clear previous:
	    clearPreviousTab(prevFolder);

	    btnFolder5.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
	    frame.setComponentZOrder(btnFolder5, 0);
    	}
    });
    
    
    btnFolder5.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
    btnFolder5.setOpaque(false);
    btnFolder5.setFocusPainted(false);
    btnFolder5.setContentAreaFilled(false);
    btnFolder5.setBorderPainted(false);
    btnFolder5.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder5.setBounds(640, 4, 177, 28);
    
    frame.getContentPane().add(btnFolder5);
    
    JLabel feedbackText = new JLabel("Feed");
    feedbackText.setHorizontalAlignment(SwingConstants.CENTER);
    feedbackText.setForeground(Color.WHITE);
    feedbackText.setFont(new Font("Lato", Font.PLAIN, 16));
    feedbackText.setBounds(99, 374, 694, 16);
    frame.getContentPane().add(feedbackText);
    
    JLabel feedbackBackground = new JLabel("");
    feedbackBackground.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Untitled-19.png")));
    feedbackBackground.setBounds(77, 361, 750, 52);
    frame.getContentPane().add(feedbackBackground);
    
    
    
    lblNewLabel.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Empty Input Bar.gif")));
    lblNewLabel.setBounds(37, 412, 814, 46);
    frame.getContentPane().add(lblNewLabel);
    
    
    JLabel label = new JLabel(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/UI Background with Tabs.png")));
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
    
}	
}

   

