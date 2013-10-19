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
import java.sql.Connection;
import javax.swing.*;

import pohaci.gumunda.titis.accounting.entity.Unit;

public class AttributeListDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  Connection m_conn = null;
  long m_sessionid = -1;

  JFrame m_mainframe;
  JList m_list;
  JButton m_okBt, m_cancelBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  Object[] m_object = new Object[0];
  String m_title = "";
  Unit m_unit = null;

  public AttributeListDlg(JFrame owner, String title, Connection conn, long sessionid) {
    super(owner, title, true);
    setSize(350, 350);
    m_mainframe = owner;
    m_title = title;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
  }

  void constructComponent() {
    m_list = new JList(new DefaultListModel());
    m_list.addMouseListener(new MouseAdapter()  {
		public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() >= 2) {
             onOK();
            }
			}
		});
    m_okBt = new JButton("OK");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(new JScrollPane(m_list), BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  void onOK() {
	  Object[] obj = m_list.getSelectedValues();
	  System.err.println("");
	  if(obj.length == 0) {
		  JOptionPane.showMessageDialog(this, m_title + " unselected!");
		  return;
	  }
	  java.util.Vector vresult = new java.util.Vector();    
	  for(int i = 0; i < obj.length; i ++){    
		  vresult.addElement(obj[i]);
	  }    
	  m_object = new Object[vresult.size()];
	  vresult.copyInto(m_object);
	  m_iResponse = JOptionPane.OK_OPTION;
	  dispose();
  }

  public JList getAttributeList() {	  
    return m_list;
  }
  
  
  public Object[] getObject() {	
    return m_object;
  }

  public int getResponse() {
    return m_iResponse;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_okBt) {
      onOK();
    }
    else if(e.getSource() == m_cancelBt) {
    	m_iResponse = JOptionPane.CANCEL_OPTION;
      dispose();
    }
  }
}
