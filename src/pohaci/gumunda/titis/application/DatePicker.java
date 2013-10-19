package pohaci.gumunda.titis.application;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import pohaci.gumunda.cgui.DatePickerDlg;

public class DatePicker extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JTextField m_dateTextField = new JTextField();
  JButton m_browseBt = new JButton("...");

  java.util.Date m_date = null;
  java.text.SimpleDateFormat m_dateformat = new java.text.SimpleDateFormat("dd-MM-yyyy");

  public DatePicker() {
    m_browseBt.setPreferredSize(new Dimension(22, 18));
    m_browseBt.addActionListener(this);
   
    m_dateTextField = new JTextField(){
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public boolean isFocusTraversable() {
			return false;
		}
    };
    m_dateTextField.setRequestFocusEnabled(false);

    setLayout(new BorderLayout(3, 1));
    add(m_dateTextField, BorderLayout.CENTER);
    add(m_browseBt, BorderLayout.EAST);
   // setDate(new java.util.Date());
  }

  public void setEditable(boolean editable) {
    m_dateTextField.setEnabled(editable);
    m_browseBt.setEnabled(editable);
  }

  public java.util.Date getDate() {
    return m_date;
  }

  public void setDate(java.util.Date date) {
    m_date = date;
    if(date != null)    	
      m_dateTextField.setText(m_dateformat.format(m_date));
    else
      m_dateTextField.setText("");
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_browseBt) {
      DatePickerDlg datepicker = new DatePickerDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
      datepicker.setVisible(true);
      java.util.Date old_date = m_date; // i add this line
      m_date = datepicker.getPickedDate();
      if(m_date != null)    	  
        m_dateTextField.setText(m_dateformat.format(m_date));
      else 
    	  m_dateTextField.setText(" ");
      firePropertyChange("date", old_date, m_date); // i add this
        
    }
  }


}
