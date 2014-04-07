package gui;

import java.awt.Color;
import java.awt.Rectangle;


import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//@author A0105912N
public class FolderNameField extends JTextField {
    

    private static final int MAX_COLUMNS = 10;
    private static final Color COLOR_UI_BACKGROUND = Color.decode("#443e3e");
    private static final long serialVersionUID = 1L;

    public FolderNameField(Rectangle bounds, String name, JFrame mainInterfaceFrame) {
	super();
	
	loadSettings(bounds, name);
	
	mainInterfaceFrame.getContentPane().add(this);
    }

    public void loadSettings(Rectangle bounds, String name) {
	
	setFont(MainInterface.latoReg15); 
	setForeground(Color.WHITE);
	setBackground(COLOR_UI_BACKGROUND);
	setHorizontalAlignment(SwingConstants.CENTER);
	setBounds(bounds);
	
	setText(name);
	setCaretColor(Color.WHITE);
	
	setColumns(MAX_COLUMNS);
    }
}
