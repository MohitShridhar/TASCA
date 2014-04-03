package gui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class KeywordItem extends JLayeredPane {
    
    private static BufferedGraphics graphics = new BufferedGraphics();
    private JTextField synonymsField;
    
    public KeywordItem(String keyword, String synonyms) {
	super();
	
	this.setSize(396, 41);
	this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	
	synonymsField = new JTextField();
	synonymsField.setBounds(135, 8, 227, 24);
	synonymsField.setFont(MainInterface.latoReg13);//new Font("Lato", Font.PLAIN, 13));
	synonymsField.setForeground(Color.WHITE);
	synonymsField.setBackground(Color.decode("#443e3e"));
	synonymsField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	synonymsField.setText(synonyms);
	synonymsField.setCaretColor(Color.WHITE);
	add(synonymsField);
	synonymsField.setColumns(10);
	
	JLabel keywordLabel = new JLabel("Keyword");
	keywordLabel.setHorizontalAlignment(SwingConstants.CENTER);
	keywordLabel.setForeground(Color.WHITE);
	keywordLabel.setFont(MainInterface.latoBold13);//new Font("Lato", Font.BOLD, 13));
	keywordLabel.setBounds(0, 0, 123, 41);
	keywordLabel.setText(keyword);
	add(keywordLabel);
	
	
	
	JLabel background = new JLabel();
	background.setSize(396, 41);
	background.setIcon(graphics.keywordItem);
	this.add(background);
    }
    
    public String getInputText() {
	return synonymsField.getText();
    }
}
