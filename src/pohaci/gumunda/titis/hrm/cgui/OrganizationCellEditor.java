package pohaci.gumunda.titis.hrm.cgui;

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
import javax.swing.table.DefaultTableModel;

public class OrganizationCellEditor extends DefaultCellEditor implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame m_owner;
	JButton m_browseBt = new JButton("Browse...");
	DefaultTableModel m_model = null;
	int m_row, m_col;
	
	public Connection m_conn = null;
	public long m_sessionid = -1;
	Object m_object = null;

  public OrganizationCellEditor(JFrame owner, Connection conn, long sessionid) {
	  super(new JTextField());
	  m_owner = owner;
	  m_conn = conn;
	  m_sessionid = sessionid;
	  m_browseBt.addActionListener(this);
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
		  boolean isSelected, int row, int column){
	  m_model = (DefaultTableModel)table.getModel();
	  m_row = row;
	  m_col = column;
	  return m_browseBt;
  }

  void done() {
	  OrganizationTreeDlg dlg = new OrganizationTreeDlg(m_owner, m_conn, m_sessionid,false);
	  dlg.setVisible(true);
	  if(dlg.getResponse() == JOptionPane.OK_OPTION) {
		  Object object = dlg.getOrganization();
		  m_model.setValueAt(object, m_row, m_col);
	  }
  }

  public void actionPerformed(ActionEvent e) {
	  if(e.getSource() == m_browseBt){
		  stopCellEditing();
		  done();
	  }
  }
}