package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.JLabel;

//@author A0105912N
public class ProgressBar extends JLabel
{

    private static final Color COLOR_PROGRESS_INDICATOR = Color.decode("#302a28");
    private static final int INITIAL_HEIGHT_BAR = 40;
    private static final int INTIAL_WIDTH_BAR = 888;
    private static final long serialVersionUID = 1L;

    private double xCoordinate, yCoordinate;
    private Double rectangle;  

    public ProgressBar(double xCoordinate,double yCoordinate)
    {
	linkMainInterfaceComponents(xCoordinate, yCoordinate);
	drawRectangle();
    }

    private void drawRectangle() {
	rectangle = new Rectangle2D.Double(this.xCoordinate , this.yCoordinate, INTIAL_WIDTH_BAR, INITIAL_HEIGHT_BAR);
    }

    private void linkMainInterfaceComponents(double xCoordinate,
	    double yCoordinate) {
	this.xCoordinate = xCoordinate;
	this.yCoordinate = yCoordinate;
    }

    @Override
    public void paintComponent(Graphics originalGraphics) 
    {
	super.paintComponent(originalGraphics);  
	Graphics2D newGraphics = (Graphics2D) originalGraphics;
	newGraphics.setColor(COLOR_PROGRESS_INDICATOR);
	newGraphics.fill(rectangle);
    }
}