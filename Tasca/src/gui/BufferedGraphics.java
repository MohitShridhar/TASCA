package gui;

import java.awt.Image;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class BufferedGraphics {
    
    public ImageIcon checkMark, reminderIcon, highPri, medPri, lowPri, background, unchecked, delete, date, info;
    public Image track, thumb;
    
    public BufferedGraphics() {
	
	try {
	    checkMark = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item Check Mark.gif"))));
	    unchecked = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Unchecked Icon.png"))));
	    reminderIcon = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item Reminder Icon.png"))));
	    delete = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Delete Icon.png"))));
	    
	    highPri = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item High Pri.png"))));
	    medPri = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item Med Pri.png"))));
	    lowPri = new ImageIcon(ImageIO.read((MainInterface.class.getResource("/GUI Graphics/Task Item Low Pri.png"))));
	    
	    date = new ImageIcon(ImageIO.read(MainInterface.class.getResource("/GUI Graphics/Time Display Icon.png")));
	    info = new ImageIcon(ImageIO.read(MainInterface.class.getResource("/GUI Graphics/Info Icon.png")));
	    
	    background = new ImageIcon(ImageIO.read(MainInterface.class.getResource("/GUI Graphics/Task Item Background.gif")));
	    
	    track = ImageIO.read(MainInterface.class.getResource("/GUI Graphics/Scroll Track.png"));
	    thumb = ImageIO.read(MainInterface.class.getResource("/GUI Graphics/Scroll Thumb.png"));
	    
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }
    
}
