package gui;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class BufferedGraphics {
    
    public ImageIcon checkMark, reminderIcon, highPri, medPri, lowPri, background, unchecked;

    
    public BufferedGraphics() {
	
	try {
	    checkMark = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item Check Mark.gif"))));
	    unchecked = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Unchecked Icon.png"))));
	    reminderIcon = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item Reminder Icon.png"))));
	    highPri = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item High Pri.gif"))));
	    medPri = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item Med Pri.gif"))));
	    lowPri = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item Low Pri.gif"))));
	    background = new ImageIcon(ImageIO.read(MainInterface.class.getResource("/GUI Graphics/Task Item Background.gif")));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }
    
}
