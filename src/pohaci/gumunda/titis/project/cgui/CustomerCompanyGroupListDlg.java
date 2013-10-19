package pohaci.gumunda.titis.project.cgui;

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

import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class CustomerCompanyGroupListDlg extends JDialog implements ActionListener {
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
  CompanyGroup[] m_group = new CompanyGroup[0];

  public CustomerCompanyGroupListDlg(JFrame owner, Connection conn, long sessionid) {
    super(owner, "Company Group", true);
    setSize(350, 350);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    initData();
  }

  void constructComponent() {
    m_list = new JList(new DefaultListModel());
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

  void initData() {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_list.setListData(logic.getAllCustomerCompanyGroup(m_sessionid, IDBConstants.MODUL_MASTER_DATA));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);

    }
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
    if(obj.length == 0) {
      JOptionPane.showMessageDialog(this, "No unit selected!");
      return;
    }

    java.util.Vector vresult = new java.util.Vector();
    for(int i = 0; i < obj.length; i ++)
      vresult.addElement((CompanyGroup)obj[i]);

    m_group = new CompanyGroup[vresult.size()];
    vresult.copyInto(m_group);
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public CompanyGroup[] getCompanyGroup() {
    return m_group;
  }

  public int getResponse() {
    return m_iResponse;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_okBt) {
      onOK();
    }
    else if(e.getSource() == m_cancelBt) {
      dispose();
    }
  }
}