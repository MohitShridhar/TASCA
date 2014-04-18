package gui;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import junit.framework.Assert;

import controller.Controller;

//@author A0105912N
public class BufferedGraphics {
    
    private static final String FILEPATH_KEYWORD_ITEM_BACKGROUND = "/GUI Graphics/Keyword Item Background.png";
    private static final String FILEPATH_SCROLL_THUMB = "/GUI Graphics/Scroll Thumb.png";
    private static final String FILEPATH_SCROLL_TRACK = "/GUI Graphics/Scroll Track.png";
    private static final String FILEPATH_UI_BACKGROUND = "/GUI Graphics/Task Item Background.gif";
    private static final String FILEPATH_INFO_ICON = "/GUI Graphics/Info Icon.png";
    private static final String FILEPATH_DATE_TOGGLE_ICON = "/GUI Graphics/Time Display Icon.png";
    private static final String FILEPATH_LOW_PRIORITY_ICON = "/GUI Graphics/Task Item Low Pri.png";
    private static final String FILEPATH_MEDIUM_PRIORITY_ICON = "/GUI Graphics/Task Item Med Pri.png";
    private static final String FILEPATH_HIGH_PRIORITY_ICON = "/GUI Graphics/Task Item High Pri.png";
    private static final String FILEPATH_DELETE_ICON = "/GUI Graphics/Delete Icon.png";
    private static final String FILEPATH_REMINDER_ICON = "/GUI Graphics/Task Item Reminder Icon.png";
    private static final String FILEPATH_UNCHECKED = "/GUI Graphics/Unchecked Icon.png";
    private static final String FILEPATH_CHECK_MARK = "/GUI Graphics/Task Item Check Mark.gif";
    private static final String FILEPATH_TASK_HIGHLIGHTER = "/GUI Graphics/Task Highlighter.png";
    
    public final static Logger logger = Controller.getLogger();
    public static final String MESSAGE_GRAPHICS_LOAD_FAILED = "Could not load important graphics";
    
    private ImageIcon checkMark;
    private ImageIcon reminderIcon;
    private ImageIcon highPri;
    private ImageIcon medPri;
    private ImageIcon lowPri;
    private ImageIcon background;
    private ImageIcon unchecked;
    private ImageIcon delete;
    private ImageIcon date;
    private ImageIcon info;
    private ImageIcon keywordItem;
    private ImageIcon taskHighlighter;
    private Image track;
    private Image thumb;
    
    public BufferedGraphics() {
	
	try {
	    bufferTaskItemUi();
	    loadMainInterfaceUi();
	    
	    bufferPriorityIcons();
	    bufferTimePropertyIcons();
	    
	    bufferScrollBarUi();
	    bufferSettingsPaneUi();
	   
	} catch (IOException e) {
	    logger.log(Level.SEVERE, MESSAGE_GRAPHICS_LOAD_FAILED + e.getStackTrace());
	    Assert.fail(MESSAGE_GRAPHICS_LOAD_FAILED);
	}
	
    }

    public void loadMainInterfaceUi() throws IOException {
	setBackground(new ImageIcon(ImageIO.read(MainInterface.class.getResource(FILEPATH_UI_BACKGROUND))));
    }

    public void bufferTaskItemUi() throws IOException {
	setCheckMark(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_CHECK_MARK)))));
	setUnchecked(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_UNCHECKED)))));
	setReminderIcon(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_REMINDER_ICON)))));
	setDelete(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_DELETE_ICON)))));
	setTaskHighlighter(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_TASK_HIGHLIGHTER)))));
    }

    public void bufferSettingsPaneUi() throws IOException {
	setKeywordItem(new ImageIcon(ImageIO.read(MainInterface.class.getResource(FILEPATH_KEYWORD_ITEM_BACKGROUND))));
    }

    public void bufferTimePropertyIcons() throws IOException {
	setDate(new ImageIcon(ImageIO.read(MainInterface.class.getResource(FILEPATH_DATE_TOGGLE_ICON))));
	setInfo(new ImageIcon(ImageIO.read(MainInterface.class.getResource(FILEPATH_INFO_ICON))));
    }

    public void bufferScrollBarUi() throws IOException {
	setTrack(ImageIO.read(MainInterface.class.getResource(FILEPATH_SCROLL_TRACK)));
	setThumb(ImageIO.read(MainInterface.class.getResource(FILEPATH_SCROLL_THUMB)));
    }

    public void bufferPriorityIcons() throws IOException {
	setHighPri(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_HIGH_PRIORITY_ICON)))));
	setMedPri(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_MEDIUM_PRIORITY_ICON)))));
	setLowPri(new ImageIcon(ImageIO.read((MainInterface.class.getResource(FILEPATH_LOW_PRIORITY_ICON)))));
    }

    public ImageIcon getCheckMark() {
	return checkMark;
    }

    private void setCheckMark(ImageIcon checkMark) {
	this.checkMark = checkMark;
    }

    public ImageIcon getReminderIcon() {
	return reminderIcon;
    }

    private void setReminderIcon(ImageIcon reminderIcon) {
	this.reminderIcon = reminderIcon;
    }

    public ImageIcon getHighPri() {
	return highPri;
    }

    private void setHighPri(ImageIcon highPri) {
	this.highPri = highPri;
    }

    public ImageIcon getMedPri() {
	return medPri;
    }

    private void setMedPri(ImageIcon medPri) {
	this.medPri = medPri;
    }

    public ImageIcon getLowPri() {
	return lowPri;
    }

    private void setLowPri(ImageIcon lowPri) {
	this.lowPri = lowPri;
    }

    public ImageIcon getBackground() {
	return background;
    }

    private void setBackground(ImageIcon background) {
	this.background = background;
    }

    public ImageIcon getUnchecked() {
	return unchecked;
    }

    private void setUnchecked(ImageIcon unchecked) {
	this.unchecked = unchecked;
    }

    public ImageIcon getDelete() {
	return delete;
    }

    private void setDelete(ImageIcon delete) {
	this.delete = delete;
    }

    public ImageIcon getDate() {
	return date;
    }

    private void setDate(ImageIcon date) {
	this.date = date;
    }

    public ImageIcon getInfo() {
	return info;
    }

    private void setInfo(ImageIcon info) {
	this.info = info;
    }

    public ImageIcon getKeywordItem() {
	return keywordItem;
    }

    private void setKeywordItem(ImageIcon keywordItem) {
	this.keywordItem = keywordItem;
    }

    public Image getTrack() {
	return track;
    }

    private void setTrack(Image track) {
	this.track = track;
    }

    public Image getThumb() {
	return thumb;
    }

    private void setThumb(Image thumb) {
	this.thumb = thumb;
    }

    public ImageIcon getTaskHighlighter() {
	return taskHighlighter;
    }

    private void setTaskHighlighter(ImageIcon taskHighlighter) {
	this.taskHighlighter = taskHighlighter;
    }
    
}
