package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class KeywordItem extends JLayeredPane {
    

    private static final int MAX_COLUMNS = 10;
    private static final Color COLOR_UI_BACKGROUND = Color.decode("#443e3e");
    private static final String TEXT_DEFAULT_KEYWORD_LABEL = "Keyword";
    private static final Border SETTINGS_EMPTY_BORDER = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    private static final long serialVersionUID = 1L;
    private static BufferedGraphics graphics = new BufferedGraphics();
    private JTextField synonymsField;
    
    public KeywordItem(String keyword, String synonyms) {
	super();
	
	loadFrameSettings();
	
	addSynonymsTextField(synonyms);
	addKeywordLabel(keyword);
	
	addItemBackground();
    }

    private void addItemBackground() {
	JLabel background = new JLabel();
	background.setSize(396, 41); // Size in pixels
	background.setIcon(graphics.getKeywordItem());
	this.add(background);
    }

    private void addKeywordLabel(String keyword) {
	JLabel keywordLabel = new JLabel(TEXT_DEFAULT_KEYWORD_LABEL);
	keywordLabel.setHorizontalAlignment(SwingConstants.CENTER);
	keywordLabel.setForeground(Color.WHITE);
	keywordLabel.setFont(MainInterface.latoBold13);
	keywordLabel.setBounds(0, 0, 123, 41); // Coordinates and size in pixels
	keywordLabel.setText(keyword);
	add(keywordLabel);
    }

    private void addSynonymsTextField(String synonyms) {
	synonymsField = new JTextField();
	synonymsField.setBounds(135, 8, 227, 24); // Coordinates and size in pixels
	synonymsField.setFont(MainInterface.latoReg13);
	synonymsField.setForeground(Color.WHITE);
	synonymsField.setBackground(COLOR_UI_BACKGROUND);
	synonymsField.setBorder(SETTINGS_EMPTY_BORDER);
	synonymsField.setText(synonyms);
	synonymsField.setCaretColor(Color.WHITE);
	synonymsField.setColumns(MAX_COLUMNS);
	add(synonymsField);
    }

    private void loadFrameSettings() {
	this.setSize(396, 41); // Size in pixels
	this.setBorder(SETTINGS_EMPTY_BORDER);
    }
    
    public String getInputText() {
	return synonymsField.getText();
    }
}
