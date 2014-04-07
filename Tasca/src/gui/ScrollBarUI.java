package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.metal.MetalScrollBarUI;

//@author A0105912N
class ScrollBarUI extends MetalScrollBarUI {
    
    private static final Color COLOR_UI_BACKGROUND = Color.decode("#272822");
    private static final int OFFSET_HEIGHT_THUMB = 1;
    private static final int OFFSET_WIDTH_THUMB = 2;
    private Image imageThumb, imageTrack;

    ScrollBarUI() {
	loadUiGraphics();
    }

    public void loadUiGraphics() {
	BufferedGraphics graphics = new BufferedGraphics();
	imageThumb = graphics.getThumb();
	imageTrack = graphics.getTrack();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
	g.translate(thumbBounds.x, thumbBounds.y);
	g.setColor(COLOR_UI_BACKGROUND);
	
	g.drawRect(0, 0, thumbBounds.width - OFFSET_WIDTH_THUMB, thumbBounds.height - OFFSET_HEIGHT_THUMB);
	
	AffineTransform transform = AffineTransform.getScaleInstance(calculateThumbWidth(thumbBounds),calculateThumbHeight(thumbBounds));
	((Graphics2D)g).drawImage(imageThumb, transform, null);
	g.translate( -thumbBounds.x, -thumbBounds.y ); 
    }

    public double calculateThumbHeight(Rectangle thumbBounds) {
	return (double)thumbBounds.height/imageThumb.getHeight(null);
    }

    public double calculateThumbWidth(Rectangle thumbBounds) {
	return (double)thumbBounds.width/imageThumb.getWidth(null);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
	g.translate(trackBounds.x, trackBounds.y);
	((Graphics2D)g).drawImage(imageTrack,AffineTransform.getScaleInstance(1,(double)trackBounds.height/imageTrack.getHeight(null)),null);
	g.translate( -trackBounds.x, -trackBounds.y );
    }

    //Remove ugly arrows buttons:
    @Override
    protected JButton createDecreaseButton(int orientation) {
	return createZeroButton();
    }

    @Override    
    protected JButton createIncreaseButton(int orientation) {
	return createZeroButton();
    }

    private JButton createZeroButton() {
	JButton jbutton = new JButton();
	jbutton.setPreferredSize(new Dimension(0, 0));
	jbutton.setMinimumSize(new Dimension(0, 0));
	jbutton.setMaximumSize(new Dimension(0, 0));
	return jbutton;
    }

}