package gui;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

//@author A0105912N
class UserInputTextPane extends JTextPane {

    private static final long serialVersionUID = 1L;

    public UserInputTextPane() {
	super();
    }

    public UserInputTextPane(StyledDocument doc) {
	super(doc);
    }

    @Override
    public void replaceSelection(String content) {
	removeTextAttributes();
	super.replaceSelection(content);
	setFocus();
    }

    private void setFocus() {
	this.requestFocus();
    }

    private void removeTextAttributes() {
	getInputAttributes().removeAttribute(StyleConstants.Foreground);
	getInputAttributes().removeAttribute(StyleConstants.Bold);
	getInputAttributes().removeAttribute(StyleConstants.FontFamily);
    }
    
    public void appendParameter(String str, SimpleAttributeSet parameterSetting) throws BadLocationException
    {
	StyledDocument document = (StyledDocument) this.getDocument();
	document.insertString(document.getLength(), str, parameterSetting);
    }
}