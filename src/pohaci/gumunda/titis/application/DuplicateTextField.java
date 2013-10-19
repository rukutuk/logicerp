package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DuplicateTextField extends JFrame implements ActionListener, FocusListener{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JTextField tf1, tf2;

  public DuplicateTextField() {
    JPanel ptext = new JPanel();
    ptext.setLayout(new GridLayout(2, 1));
    tf1 = new JTextField();
    tf1.addFocusListener(this);
    tf1.addActionListener(this);
    tf2 = new JTextField();
    tf2.addActionListener(this);

    addFocusListener(this);
    tf1.getDocument().addDocumentListener(new MyDocumentListener());
    int textlength = tf1.getDocument().getLength();
    tf1.setSelectionEnd(textlength);

    ptext.add(tf1);
    ptext.add(tf2);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(ptext, BorderLayout.NORTH);
    setSize(300, 200);
    setVisible(true);
  }


  public void focusGained(FocusEvent e) {
    tf1.selectAll();
    int textlength = tf1.getDocument().getLength();
    tf1.setSelectionEnd(textlength);
  }
  public void focusLost(FocusEvent e) {
  }

  class MyDocumentListener implements DocumentListener {
    //String newline = System.getProperty("line.separator");
    public void insertUpdate(DocumentEvent e) {
      tf1.selectAll();
      int textlength = tf1.getDocument().getLength();
      tf1.setSelectionEnd(textlength);
      updateLog(e);
    }
    public void removeUpdate(DocumentEvent e) {
      updateLog(e);
    }
    public void changedUpdate(DocumentEvent e) {
    }
    public void updateLog(DocumentEvent e) {
      //Document doc = (Document)e.getDocument();
      //int changeLength = e.getLength();
      String s1 = tf1.getText();
      tf2.setText(s1);
    }
  }

  public void actionPerformed(ActionEvent e){
    String s1 = tf1.getText();
    tf2.setText(s1);
    tf1.selectAll();
  }

  public void processWindowEvent(WindowEvent e){
    if(e.getID() == WindowEvent.WINDOW_CLOSING ) System.exit(0);
  }

  public static void main(String[] s){
    new DuplicateTextField();
  }
}