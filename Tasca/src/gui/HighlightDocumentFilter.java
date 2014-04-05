package gui;

import interpreter.CommandType;
import interpreter.Interpreter;
import interpreter.ParameterType;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.antlr.runtime.tree.RewriteEmptyStreamException;

import controller.Controller;

public class HighlightDocumentFilter extends DocumentFilter {

    private static final ImageIcon GRAPHIC_FAILED_INPUT_BAR = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Failed Input Bar.gif"));
    private static final ImageIcon GRAPHIC_SUCCESS_INPUT_BAR = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Success Input Bar.gif"));
    private static final ImageIcon GRAPHIC_EMPTY_INPUT_BAR = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Empty Input Bar.gif"));
    private static final int VISIBLE_RECT_HEIGHT = 23;
    private static final int INDICATOR_UPDATE_PERIOD = 100;
    private DefaultHighlightPainter highlightPainter = new DefaultHighlightPainter(Color.YELLOW);
    private static JTextPane textPane;
    private JFrame mainFrame;
    private AttributeSet duplicateParameter, normalSetting, parameterSetting, commandSetting;
    private static Interpreter interpreter;
    private JLabel background, feedbackBackground, feedbackText;
    
    private static Map<CommandType, Color> commandColors = new HashMap<CommandType, Color>();
    private static Map<ParameterType, Color> parameterColors = new HashMap<ParameterType, Color>();
    private boolean hasColor = false;
    private String userInput = null;
    
    private StyledDocument doc;
    
    ActionListener taskUpdateIndicator = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            setIndicators();
        }
    };
    
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
	commandColors.put(CommandType.UNMARK, Color.getHSBColor(0.97f, 0.66f, 0.94f));
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
	
	
    }
    
    public Controller getController() {
	return MainInterface.controller;
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
        StyleConstants.setFontFamily((MutableAttributeSet) commandSetting, "Meslo LG S");
        StyleConstants.setFontSize((MutableAttributeSet) commandSetting, 16);
        StyleConstants.setBold((MutableAttributeSet) commandSetting, true);
        
        new Timer(INDICATOR_UPDATE_PERIOD, taskUpdateIndicator).start();
        
        
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
        		
        		HighlightDocumentFilter.interpreter.setCurrentFolder(MainInterface.getCurrentFolderName());        		
        		quit = MainInterface.controller.executeCommands(userInput, MainInterface.getCurrentFolderName());
        		
        		if (!quit) {
        		    MainInterface.updateTaskDisplay();
        		    MainInterface.clearTextPane();
        		    MainInterface.inputNumRef = MainInterface.inputHistorySize;
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

    public void setIndicators() {
	
//	System.out.println("Preferred size: " + textPane.getPreferredSize() + " Visible rect: " + textPane.getVisibleRect());
	
	int preferredHeight = textPane.getPreferredSize().height;
	int visibleYPos = textPane.getVisibleRect().y;

	if (preferredHeight == VISIBLE_RECT_HEIGHT) {
	    MainInterface.setUpIndicator(false);
	    MainInterface.setDownIndicator(false);
	} else if (preferredHeight > VISIBLE_RECT_HEIGHT && visibleYPos == 0 && preferredHeight - visibleYPos != VISIBLE_RECT_HEIGHT) {
	    MainInterface.setUpIndicator(false);
	    MainInterface.setDownIndicator(true);
	} else if (preferredHeight > VISIBLE_RECT_HEIGHT && visibleYPos > 0 && preferredHeight - visibleYPos != VISIBLE_RECT_HEIGHT) {
	    MainInterface.setUpIndicator(true);
	    MainInterface.setDownIndicator(true);
	} else if (preferredHeight > VISIBLE_RECT_HEIGHT && visibleYPos > 0 && preferredHeight - visibleYPos == VISIBLE_RECT_HEIGHT) {
	    MainInterface.setUpIndicator(true);
	    MainInterface.setDownIndicator(false);
	}

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
            background.setIcon(GRAPHIC_EMPTY_INPUT_BAR);
	    feedbackText.setVisible(false);
	    feedbackBackground.setVisible(false);
        } else if (successParse) {
            background.setIcon(GRAPHIC_SUCCESS_INPUT_BAR);
        } else {
            background.setIcon(GRAPHIC_FAILED_INPUT_BAR);
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
//	System.out.println(paraMatch);
	
	if (hasNextSpace(interpreter.getFirstWord("-" + paraMatch), allInput) && allInput.charAt(allInput.indexOf(paraMatch)-1) == '-') {
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
    
    public static Color getParameterColor(ParameterType parameterType) {
	return parameterColors.get(parameterType);
    }
    
    
}
