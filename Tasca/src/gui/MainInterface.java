package gui;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;




public class MainInterface {

  // TODO: Fix minimize flickering
  // TODO: not imp: fix dual screen drag
    
  private static int posX=0,posY=0;
  private static JButton btnFolder2 = new JButton("");
  private static JButton btnFolder1 = new JButton("");
  private static JButton btnFolder3 = new JButton("");
  private static JButton btnFolder4 = new JButton("");
  private static JButton btnFolder5 = new JButton("");
  
  private static Folder currFolder, prevFolder;
  
  public static enum Folder {
      folder1, folder2, folder3, folder4, folder5
  }
  
  public MainInterface() {

  }

  
  private static void clearPreviousTab (Folder prevFolder) {
      switch (prevFolder) {
      case folder1:
	  btnFolder1.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case folder2:
	  btnFolder2.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case folder3:
	  btnFolder3.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case folder4:
	  btnFolder4.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      case folder5:
	  btnFolder5.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
	  break;
      default:
	  break;
      }
  }
  
  private static class MyTextPane extends JTextPane {

      
      public MyTextPane() {
          super();

          SimpleAttributeSet set = new SimpleAttributeSet(getParagraphAttributes());
 //         StyleConstants.setLineSpacing(set , 2);
          StyleConstants.setForeground(set, Color.WHITE);
          
          // Set the attributes before adding text
          setCharacterAttributes(set, true);
//          setParagraphAttributes(set, true);
          setText("add new");
//          setForeground(Color.WHITE);
          
          
          setOpaque(false);

          // this is needed if using Nimbus L&F - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960
          setBackground(Color.BLACK);
          
      }
      
      @Override
      protected void paintComponent(Graphics g) {
          // set background green - but can draw image here too
	  try {
	    Image img = ImageIO.read(MainInterface.class.getResourceAsStream("/GUI Graphics/Empty Input Bar.gif"));
	    g.drawImage(img, 0, 0, 814,//(int) getSize().getWidth(),
                   46, this); //(int) getSize().getHeight(), this);
	    
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}


          super.paintComponent(g);
      }


      
  }
    
  public static void main(String[] args) {
     
     currFolder = Folder.folder1;
     prevFolder = Folder.folder1;
      
    final JFrame frame = new JFrame("TitleLessJFrame");
    
    Container mainContainer = frame.getContentPane(); 
    frame.getContentPane().setLayout(null);
    frame.setUndecorated(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(888, 500);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
    
    
    JButton btnClose = new JButton(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Close button.png")));
    
    btnClose.setBackground(Color.BLACK);
    btnClose.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    	    frame.dispose();
    	    System.exit(0);
    	}
    });
    

    
    // Text pane:
    
//    MyTextPane textPane = new MyTextPane();
//    textPane.setText("add task");
//    
//    
//    	
//    textPane.setFont(new Font("Menlo", Font.PLAIN, 16));
//    
//    DefaultCaret caret = (DefaultCaret)textPane.getCaret();
//    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
//    
//    textPane.setLocation(37, 412);
//    textPane.setSize(814, 46);
//    textPane.setMargin(new Insets(13, 10, 13, 25));
//    textPane.setCaretPosition(textPane.getDocument().getLength());
// 
//    
//    JScrollPane scrollPane = new JScrollPane(textPane);
//    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
//    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//    scrollPane.setLocation(37, 412);
//    scrollPane.setSize(814, 46);
//    scrollPane.setPreferredSize(new Dimension(814, 46));
//    scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//    
//    
//    scrollPane.setViewportView(textPane);   
//    
//    frame.getContentPane().add(scrollPane);
    
    JTextPane textPane = new JTextPane();
    textPane.setOpaque(false);
    textPane.setText("add task");
    textPane.setFont(new Font("Menlo", Font.PLAIN, 16));
    textPane.setForeground(Color.WHITE);
    //textPane.setMargin(new Insets(13, 10, 13, 25));
    
    JScrollPane scrollPane = new JScrollPane(textPane);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setLocation(50, 425);
    scrollPane.setSize(760, 19);
    scrollPane.setOpaque(false);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    scrollPane.getViewport().setOpaque(false);
    
    scrollPane.setViewportView(textPane);
    frame.getContentPane().add(scrollPane);   
    
    
    btnClose.setBounds(862, 7, 17, 17);
    frame.getContentPane().add(btnClose);
    
    JButton btnMinimize = new JButton("");
    btnMinimize.setBackground(Color.BLACK);
    btnMinimize.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    frame.setState(JFrame.ICONIFIED);
    	}
    });
    btnMinimize.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Minimize Button.png")));
    btnMinimize.setBounds(836, 7, 18, 18);
    frame.getContentPane().add(btnMinimize);
    
    
    btnFolder1.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    	    prevFolder = currFolder;
    	    currFolder = Folder.folder1;
    	    
    	    // Clear previous:
    	    clearPreviousTab(prevFolder);
    	        	    
    	    // Update:
    	    btnFolder1.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
    	    frame.setComponentZOrder(btnFolder1, 0);
    	}
    });
    btnFolder1.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
    btnFolder1.setBounds(0, 4, 177, 28);
    btnFolder1.setOpaque(false);
    btnFolder1.setFocusPainted(false);
    btnFolder1.setBorderPainted(false);
    btnFolder1.setContentAreaFilled(false);
    btnFolder1.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    
    frame.getContentPane().add(btnFolder1);
    
    btnFolder2.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    prevFolder = currFolder;
    	    currFolder = Folder.folder2;
    	    
    	    // Clear previous:
    	    clearPreviousTab(prevFolder);
    	    
    	    // Update:
    	    btnFolder2.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
    	    frame.setComponentZOrder(btnFolder2, 0);
    	}
    });
    btnFolder2.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
    btnFolder2.setOpaque(false);
    btnFolder2.setFocusPainted(false);
    btnFolder2.setContentAreaFilled(false);
    btnFolder2.setBorderPainted(false);
    btnFolder2.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder2.setBounds(159, 4, 177, 28);
    frame.getContentPane().add(btnFolder2);
    btnFolder3.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    prevFolder = currFolder;
	    currFolder = Folder.folder3;
	    
	    // Clear previous:
	    clearPreviousTab(prevFolder);

	    btnFolder3.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
	    frame.setComponentZOrder(btnFolder3, 0);

    	}
    });
    
   
    btnFolder3.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
    btnFolder3.setOpaque(false);
    btnFolder3.setFocusPainted(false);
    btnFolder3.setContentAreaFilled(false);
    btnFolder3.setBorderPainted(false);
    btnFolder3.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder3.setBounds(320, 4, 177, 28);
    frame.getContentPane().add(btnFolder3);
    
    
    
    btnFolder4.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    prevFolder = currFolder;
	    currFolder = Folder.folder4;
	    
	    // Clear previous:
	    clearPreviousTab(prevFolder);

	    btnFolder4.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
	    frame.setComponentZOrder(btnFolder4, 0);

    	}
    });
    
    
    btnFolder4.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
    btnFolder4.setOpaque(false);
    btnFolder4.setFocusPainted(false);
    btnFolder4.setContentAreaFilled(false);
    btnFolder4.setBorderPainted(false);
    btnFolder4.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder4.setBounds(479, 4, 177, 28);
    frame.getContentPane().add(btnFolder4);
    btnFolder5.addMouseListener(new MouseAdapter() {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    	    prevFolder = currFolder;
	    currFolder = Folder.folder5;
	    
	    // Clear previous:
	    clearPreviousTab(prevFolder);

	    btnFolder5.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab Clicked.gif")));	    
	    frame.setComponentZOrder(btnFolder5, 0);
    	}
    });
    
    
    btnFolder5.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Tab NotClicked.gif")));
    btnFolder5.setOpaque(false);
    btnFolder5.setFocusPainted(false);
    btnFolder5.setContentAreaFilled(false);
    btnFolder5.setBorderPainted(false);
    btnFolder5.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    btnFolder5.setBounds(640, 4, 177, 28);
    
    frame.getContentPane().add(btnFolder5);
    
    JLabel lblNewLabel = new JLabel("New label");
    lblNewLabel.setIcon(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/Empty Input Bar.gif")));
    lblNewLabel.setBounds(37, 412, 814, 46);
    frame.getContentPane().add(lblNewLabel);
    
    
    
    JLabel label = new JLabel(new ImageIcon(MainInterface.class.getResource("/GUI Graphics/UI Background with Tabs.png")));
    label.setBackground(Color.BLACK);
    label.setBounds(0, 0, 888, 500);
    mainContainer.add(label);
    
    frame.getContentPane().setBackground(Color.WHITE);

    
    frame.addMouseListener(new MouseAdapter()
    {
       public void mousePressed(MouseEvent e)
       {
          posX=e.getX();
          posY=e.getY();
       }
    });
    
    frame.addMouseMotionListener(new MouseAdapter()
    {
         public void mouseDragged(MouseEvent evt)
         {
    		//sets frame position when mouse dragged			
    		frame.setLocation (evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
    					
         }
    });
    
  }	
}

   

