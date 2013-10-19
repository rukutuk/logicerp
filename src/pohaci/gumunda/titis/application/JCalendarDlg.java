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

public class JCalendarDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  JDayChooser m_daychooser;
  JCalendar m_calendar;
  JButton m_okBt, m_cancelBt;

  int m_iResponse = JOptionPane.NO_OPTION;

  public JCalendarDlg(JFrame owner) {
    super(owner, "Date", true);
    m_mainframe = owner;
    setSize(300, 300);
    setResizable(false);
    constructComponent();
  }

  void constructComponent() {
    m_daychooser = new JDayChooser();
    m_calendar = new JCalendar(JMonthChooser.RIGHT_SPINNER, m_daychooser);
    m_okBt = new JButton("OK");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(new JScrollPane(m_calendar), BorderLayout.CENTER);
    getContentPane().add(buttonPanel,  BorderLayout.SOUTH);
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  public int getResponse() {
    return m_iResponse;
  }

  public java.util.Date getSelectedDate() {
    return m_daychooser.getDate();
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_okBt) {
      m_iResponse = JOptionPane.OK_OPTION;
      dispose();
    }
    else if(e.getSource() == m_cancelBt) {
      dispose();
    }
  }
}