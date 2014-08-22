package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

//@author A0105912N
public class KeywordItem extends JLayeredPane {
    
    private static final Dimension BOUNDS_ITEM = new Dimension(396, 41);
    private static final Rectangle BOUNDS_SYNONYMS_TEXT_FIELD = new Rectangle(135, 8, 227, 24);
    private static final Rectangle BOUNDS_KEYWORD_LABEL = new Rectangle(0, 0, 123, 41);
    private static final Dimension DIMENSIONS_ITEM_BACKGROUND = BOUNDS_ITEM;
    
    private static final int MAX_COLUMNS = 10;
    private static final Color COLOR_UI_BACKGROUND = Color.decode("#443e3e");
    private static final String TEXT_DEFAULT_KEYWORD_LABEL = "Keyword";
    private static final Border SETTINGS_EMPTY_BORDER = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    private static final long serialVersionUID = 1L;
    private static BufferedGraphics graphics = new BufferedGraphics();
    private JTextField synonymsField;
    
    public KeywordItem(String keyword, String synonyms) {
	super();
	
	loadItemSettings();
	
	addSynonymsTextField(synonyms);
	addKeywordLabel(keyword);
	
	addItemBackground();
    }

    private void addItemBackground() {
	JLabel background = new JLabel();
	background.setSize(DIMENSIONS_ITEM_BACKGROUND);
	background.setIcon(graphics.getKeywordItem());
	this.add(background);
    }

    private void addKeywordLabel(String keyword) {
	JLabel keywordLabel = new JLabel(TEXT_DEFAULT_KEYWORD_LABEL);
	keywordLabel.setHorizontalAlignment(SwingConstants.CENTER);
	keywordLabel.setForeground(Color.WHITE);
	keywordLabel.setFont(MainInterface.latoBold13);
	keywordLabel.setBounds(BOUNDS_KEYWORD_LABEL);
	keywordLabel.setText(keyword);
	add(keywordLabel);
    }

    private void addSynonymsTextField(String synonyms) {
	synonymsField = new JTextField();
	synonymsField.setBounds(BOUNDS_SYNONYMS_TEXT_FIELD); 
	synonymsField.setFont(MainInterface.latoReg13);
	synonymsField.setForeground(Color.WHITE);
	synonymsField.setBackground(COLOR_UI_BACKGROUND);
	synonymsField.setBorder(SETTINGS_EMPTY_BORDER);
	synonymsField.setText(synonyms);
	synonymsField.setCaretColor(Color.WHITE);
	synonymsField.setColumns(MAX_COLUMNS);
	add(synonymsField);
    }

    private void loadItemSettings() {
	this.setSize(BOUNDS_ITEM); 
	this.setBorder(SETTINGS_EMPTY_BORDER);
    }
    
    public String getInputText() {
	return synonymsField.getText();
    }
}
