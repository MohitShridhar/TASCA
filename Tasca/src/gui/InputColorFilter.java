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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.antlr.runtime.tree.RewriteEmptyStreamException;

import controller.Controller;

//@author A0105912N
/**
 * @author MohitSridhar
 *
 */
/**
 * @author MohitSridhar
 *
 */
/**
 * @author MohitSridhar
 *
 */
/**
 * @author MohitSridhar
 *
 */
public class InputColorFilter extends DocumentFilter {
    
    private static InputColorFilter filterInstance = null;
    
    private static final String WARNING_PARSING_NOT_SUCCESSFUL = "Interpreter parsing not successful";
    private static final char SINGLE_SPACE = ' ';
    private static final char DELIMETER_CHAR_FORM = '-';
    private static final String DELIMITER_STRING_FORM = "-";
    private static final String EXPRESSION_NEW_LINE = "\\n";
    private static final int FONT_SIZE_COMMAND = 16;
    private static final String FONT_TYPE_COMMAND = "Meslo LG S";
    
    private static Logger logger = Controller.getLogger();
    
    private static final ImageIcon GRAPHIC_FAILED_INPUT_BAR = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Failed Input Bar.gif"));
    private static final ImageIcon GRAPHIC_SUCCESS_INPUT_BAR = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Success Input Bar.gif"));
    private static final ImageIcon GRAPHIC_EMPTY_INPUT_BAR = new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Empty Input Bar.gif"));
    
    private static final int VISIBLE_INPUT_RECT_HEIGHT = 23;
    private static final int INDICATOR_UPDATE_PERIOD = 100; // in milliseconds
    
    private static JTextPane textPane;
    private JFrame mainFrame;
    private AttributeSet normalSetting, parameterSetting, commandSetting;
    private static Interpreter interpreter;
    private JLabel background, feedbackBackground, feedbackText;
    
    private static Map<CommandType, Color> commandColors = new HashMap<CommandType, Color>();
    private static Map<ParameterType, Color> parameterColors = new HashMap<ParameterType, Color>();
    private String userInputString = null;
   
    
    static {
	
	// Command Colors:
	
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
	
	
	// Parameter Colors:
	
	parameterColors.put(ParameterType.START_TIME, Color.getHSBColor(0.675f, 0.44f, 0.98f));
	parameterColors.put(ParameterType.END_TIME, Color.getHSBColor(0.97f, 0.66f, 0.94f));
	parameterColors.put(ParameterType.REMINDER_TIME, Color.getHSBColor(0.06f, 0.71f, 0.87f));
	parameterColors.put(ParameterType.PRIORITY, Color.getHSBColor(0.95f, 0.67f, 0.98f));
	parameterColors.put(ParameterType.LOCATION, Color.CYAN.brighter());
	parameterColors.put(ParameterType.FOLDER, Color.PINK);
	parameterColors.put(ParameterType.TASK_ID, Color.YELLOW.brighter());
	parameterColors.put(ParameterType.INVALID, Color.white);
    }
    
    ActionListener taskUpdateIndicator = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            setIndicators();
        }
    };
    
    /*
     * Singleton: Only one color filter is required because only one input textpane is used
     */
    public static InputColorFilter getInstance(JFrame frame, final JTextPane textPane, Interpreter interpreter, JLabel background, JLabel feedbackText, JLabel feedbackBackground) {
	if (filterInstance == null) {
	    filterInstance = new InputColorFilter(frame, textPane, interpreter, background, feedbackText, feedbackBackground);
	}
	
	return filterInstance;
    }
    
    private InputColorFilter(JFrame frame, final JTextPane textPane, Interpreter interpreter, JLabel background, JLabel feedbackText, JLabel feedbackBackground) {
	
        linkMainInterfaceComponents(frame, textPane, interpreter, background,feedbackText, feedbackBackground);      
        buildAllAttributeSets();

        addListenerForInputExecution(textPane);
        startIndicatorTimer();
    }
    
    private void linkMainInterfaceComponents(JFrame frame,
	    final JTextPane textPane, Interpreter interpreter,
	    JLabel background, JLabel feedbackText, JLabel feedbackBackground) {
	
	InputColorFilter.textPane = textPane;
        InputColorFilter.interpreter = interpreter;
        
        this.mainFrame = frame;
        this.background = background;
        this.feedbackBackground = feedbackBackground;
        this.feedbackText = feedbackText;
    }

    private void startIndicatorTimer() {
	new Timer(INDICATOR_UPDATE_PERIOD, taskUpdateIndicator).start();
    }

    private void addListenerForInputExecution(final JTextPane textPane) {
	textPane.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        	    if (e.getKeyCode() == KeyEvent.VK_ENTER)
        	    {
        		processUserAction();
        	    }
        	}
	});
    }

    private void processUserAction() {
	userInputString = getUserInput();

	if (userInputString != null) {
	    boolean quit = false;
	    quit = executeCommand();
	    processNextAction(quit);
	}
    }

    private void buildAllAttributeSets() {
	normalSetting = new SimpleAttributeSet();
        StyleConstants.setBold((MutableAttributeSet) normalSetting, false);
        StyleConstants.setForeground((MutableAttributeSet) normalSetting, Color.WHITE);
        
        parameterSetting = new SimpleAttributeSet();
        StyleConstants.setBold((MutableAttributeSet) parameterSetting, false);
        
        commandSetting = new SimpleAttributeSet();
        StyleConstants.setFontFamily((MutableAttributeSet) commandSetting, FONT_TYPE_COMMAND);
        StyleConstants.setFontSize((MutableAttributeSet) commandSetting, FONT_SIZE_COMMAND);
        StyleConstants.setBold((MutableAttributeSet) commandSetting, true);
    }
    
    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, text.replaceAll(EXPRESSION_NEW_LINE, ""), attr);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);   
        updateSuccessBar(fb);
    }
    
    /*
     * The color coding system is activated every time the text field is update/changed
     */
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
	
        super.replace(fb, offset, length, text.replaceAll(EXPRESSION_NEW_LINE, ""), attrs);
	
        String allInputText = getAllTextFromDocument(fb).toLowerCase();
	String commandMatch = getCurrentCommand(allInputText);

        addCommandColor(commandMatch, allInputText);
        
        String allParameterMatches[] =  allInputText.split(DELIMITER_STRING_FORM);
        
        for (int i=1; i<allParameterMatches.length; i++) { // Iteration starts from i=1 as the first element in the array contains the command; not the first parameter 
            String parameterMatch = allParameterMatches[i].toLowerCase();
            addParameterColor(parameterMatch, allInputText);
        }
     
        updateSuccessBar(fb);
    }

    private String getAllTextFromDocument(FilterBypass fb)
	    throws BadLocationException {
	return fb.getDocument().getText(0, fb.getDocument().getLength());
    }

    private String getCurrentCommand(String allInputText) {
	return interpreter.getFirstWord(allInputText).toLowerCase();
    }

    private void setIndicators() {
	
//	For debugging:
//	System.out.println("Preferred size: " + textPane.getPreferredSize() + " Visible rect: " + textPane.getVisibleRect());
	
	int preferredHeight = textPane.getPreferredSize().height;
	int visibleYPos = textPane.getVisibleRect().y;

	if (isSingleLine(preferredHeight)) {
	    setIndicators(false, false);
	} else if (isMultipleLinesAtTop(preferredHeight, visibleYPos)) {
	    setIndicators(false, true);
	} else if (isMultipleLinesInMiddle(preferredHeight, visibleYPos)) {
	    setIndicators(true, true);
	} else if (isMultipleLinesAtBottom(preferredHeight, visibleYPos)) {
	    setIndicators(true, false);
	}

    }

    private boolean isMultipleLinesAtBottom(int preferredHeight, int visibleYPos) {
	return preferredHeight > VISIBLE_INPUT_RECT_HEIGHT && visibleYPos > 0 && preferredHeight - visibleYPos == VISIBLE_INPUT_RECT_HEIGHT;
    }

    private boolean isMultipleLinesInMiddle(int preferredHeight, int visibleYPos) {
	return preferredHeight > VISIBLE_INPUT_RECT_HEIGHT && visibleYPos > 0 && preferredHeight - visibleYPos != VISIBLE_INPUT_RECT_HEIGHT;
    }

    private boolean isMultipleLinesAtTop(int preferredHeight, int visibleYPos) {
	return preferredHeight > VISIBLE_INPUT_RECT_HEIGHT && visibleYPos == 0 && preferredHeight - visibleYPos != VISIBLE_INPUT_RECT_HEIGHT;
    }

    private boolean isSingleLine(int preferredHeight) {
	return preferredHeight == VISIBLE_INPUT_RECT_HEIGHT;
    }

    private void setIndicators(boolean up, boolean down) {
	MainInterface.setUpIndicator(up);
	MainInterface.setDownIndicator(down);
    }

    private void updateSuccessBar(FilterBypass fb) throws BadLocationException {
	String allInputText = getAllTextFromDocument(fb);
	Boolean isParseSuccessful = false;
	Boolean isInputEmpty = checkEmptyInput(allInputText);
	
	userInputString = null;

	isParseSuccessful = checkForInterpreterExceptions(allInputText);
        setInputBarBackground(isParseSuccessful, isInputEmpty);
    }

    private void setInputBarBackground(Boolean isParseSuccessful, Boolean isInputEmpty) {
	if (isInputEmpty) {
            background.setIcon(GRAPHIC_EMPTY_INPUT_BAR);
	    feedbackText.setVisible(false);
	    feedbackBackground.setVisible(false);
        } else if (isParseSuccessful) {
            background.setIcon(GRAPHIC_SUCCESS_INPUT_BAR);
        } else {
            background.setIcon(GRAPHIC_FAILED_INPUT_BAR);
        }
    }

    private Boolean checkForInterpreterExceptions(String allText) {
	Boolean isParseSuccessful;
	try {
	    interpreter.processUserInput(allText);
	    
	    isParseSuccessful = true;
	    deactiveExceptionFeedbackBar();
	    userInputString = allText;
	    
	} catch(IllegalArgumentException | RewriteEmptyStreamException e) {
	    isParseSuccessful = false;
	    activateExceptionFeedbackBar(e);
	    
	    logger.log(Level.WARNING, WARNING_PARSING_NOT_SUCCESSFUL);
	}
	
	return isParseSuccessful;
    }

    private void activateExceptionFeedbackBar(RuntimeException e) {
	if (MainInterface.getIsActiveFeedbackEnabled()) {
	    feedbackBackground.setVisible(true);
	    feedbackText.setVisible(true);
	    feedbackText.setText(e.getMessage());
	}
    }

    private void deactiveExceptionFeedbackBar() {
	if (MainInterface.getIsActiveFeedbackEnabled()) {
	    feedbackText.setVisible(false);
	    feedbackBackground.setVisible(false);
	}
    }

    public String getUserInput(){
	return userInputString;
    }

    private boolean checkEmptyInput(String allText) {
	return allText.trim().isEmpty() || allText == null;
    }
    
    
    private void addParameterColor(String paraMatch, String allInput) {
	
	int startIndex = allInput.indexOf(paraMatch) - 1; // startIndex is offset by -1 so the delimiter '-' behind the parameter is also colored
	
	int paraLength = interpreter.getFirstWord(paraMatch).length();
	
	if (readyColorCoding(paraMatch, allInput)) {
	    
	    findParameterColorProperties(paraMatch);
	    
	    colorParameter(startIndex, paraLength);
	    unformatSpaceAfterParameter(startIndex, paraLength); 
	}
		
    }

    private void unformatSpaceAfterParameter(int startIndex,
	    int paraLength) {
	textPane.getStyledDocument().setCharacterAttributes(startIndex + paraLength + 1, startIndex + paraLength + 1, normalSetting, true);
    }

    private void colorParameter(int startIndex, int paraLength) {
	textPane.getStyledDocument().setCharacterAttributes(startIndex, startIndex + paraLength, parameterSetting, true);
    }

    private void findParameterColorProperties(String paraMatch) {
	StyleConstants.setForeground((MutableAttributeSet) parameterSetting, parameterColors.get(interpreter.interpretParameter(interpreter.getFirstWord(paraMatch))));
    }

    private boolean readyColorCoding(String paraMatch, String allInput) {
	return hasTrailingSpace(interpreter.getFirstWord(DELIMITER_STRING_FORM + paraMatch), allInput) && allInput.charAt(allInput.indexOf(paraMatch)-1) == DELIMETER_CHAR_FORM;
    }
    
    private void addCommandColor (String commMatch, String allInput) {
	int startIndex = allInput.indexOf(commMatch);
	CommandType commandType = interpreter.interpretCommand(commMatch);
	
	if (commandType == CommandType.INVALID) {
	    textPane.getStyledDocument().setCharacterAttributes(startIndex, startIndex + commMatch.length(), normalSetting, true);
	}
	
	else if (hasTrailingSpace(commMatch, allInput)) {
	    StyleConstants.setForeground((MutableAttributeSet) commandSetting, commandColors.get(commandType));
	    textPane.getStyledDocument().setCharacterAttributes(startIndex, startIndex + commMatch.length(), commandSetting, true);
	    textPane.getStyledDocument().setCharacterAttributes(startIndex + commMatch.length() + 1, startIndex + commMatch.length() + 1, normalSetting, true);
	}
	
    }
    
    private static boolean hasTrailingSpace(String sub, String main) {
	
	int startIndex = main.indexOf(sub);
	int endIndex = startIndex + sub.length();
	
	if (endIndex > main.length() - 1) {
	    return false;
	} 
	
	if (main.charAt(endIndex) != SINGLE_SPACE) {
	    return false;
	}
	
	return true;
    }
    
    private boolean executeCommand() {
	boolean quit;
	
	InputColorFilter.interpreter.setCurrentFolder(MainInterface.getCurrentFolderName());        		
	quit = MainInterface.getController().executeCommands(userInputString, MainInterface.getCurrentFolderName());
	return quit;
    }

    private void processNextAction(boolean quit) {
	if (!quit) {
	    MainInterface.updateTaskDisplay();
	    MainInterface.clearTextPane();
	    MainInterface.setCurrentHistoryState(MainInterface.getInputHistorySize());
	} else {
	    mainFrame.dispose();
	    System.exit(0);        			
	}
    }

    public static Color getParameterColor(ParameterType parameterType) {
	return parameterColors.get(parameterType);
    }
    
}
