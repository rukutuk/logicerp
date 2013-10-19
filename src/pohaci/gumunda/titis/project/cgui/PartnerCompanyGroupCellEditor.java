package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class PartnerCompanyGroupCellEditor extends DefaultCellEditor implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_owner;
  JButton m_broweBt = new JButton("Browse...");
  DefaultTableModel m_model = null;
  int m_row, m_col;

  Connection m_conn = null;
  long m_sessionid = -1;
  CompanyGroup[] m_group = new CompanyGroup[0];

  public PartnerCompanyGroupCellEditor(JFrame owner, Connection conn, long sessionid) {
    super(new JTextField());
    m_owner = owner;
    m_conn = conn;
    m_sessionid = sessionid;

    m_broweBt.addActionListener(this);
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column){
    m_model = (DefaultTableModel)table.getModel();
    m_row = row;
    m_col = column;
    return m_broweBt;
  }

  public CompanyGroup[] getCompanyGroup() {
    return m_group;
  }

  public void setCompanyGroup(CompanyGroup[] group) {
    m_group = group;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_broweBt){
      stopCellEditing();      
      PartnerCompanyGroupListDlg dlg = new PartnerCompanyGroupListDlg(m_owner, m_conn, m_sessionid);
      dlg.setVisible(true);
      if(dlg.getResponse() == JOptionPane.OK_OPTION) {
        m_group = dlg.getCompanyGroup();
        String str = "";
        for(int i = 0; i < m_group.length; i ++) {
          if(i == 0)
            str += m_group[i].toString();
          else
            str += ", " + m_group[i].toString();
        }
        m_model.setValueAt(str, m_row, m_col);
      }
    }
  }
}
