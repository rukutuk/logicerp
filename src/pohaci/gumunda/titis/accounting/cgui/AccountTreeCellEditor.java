package pohaci.gumunda.titis.accounting.cgui;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import pohaci.gumunda.titis.accounting.entity.Account;

public class AccountTreeCellEditor extends DefaultCellEditor implements ActionListener {
	private static final long serialVersionUID = 1L;
	public JFrame m_owner;
	public JButton m_btEdit = new JButton("Account...");
	public DefaultTableModel m_tmodel = null;
	public int m_row, m_col;
	public Connection m_conn = null;
	public long m_sessionid = -1;
	private boolean isGroupSelectionEnable = false;
	
	public AccountTreeCellEditor(JFrame owner, Connection conn, long sessionid) {
		super(new JTextField());
		m_owner = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		m_btEdit.addActionListener(this);
	}
	
	public AccountTreeCellEditor(JFrame owner, Connection conn, long sessionid, boolean isGroupSelectionEnable) {
		super(new JTextField());
		m_owner = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		m_btEdit.addActionListener(this);
		this.isGroupSelectionEnable = isGroupSelectionEnable;
	}
	
	public Component getTableCellEditorComponent(JTable table,
			Object value,
			boolean isSelected,
			int row,
			int column){
		m_tmodel = (DefaultTableModel)table.getModel();
		m_row = row;
		m_col = column;
		return m_btEdit;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_btEdit){
			stopCellEditing();
			AccountTreeDlg dlg = new AccountTreeDlg(m_owner, null, m_conn, m_sessionid, isGroupSelectionEnable);
			dlg.setVisible(true);
			if(dlg.getResponse() == JOptionPane.OK_OPTION) {
				Account account = dlg.getAccount();
				if(account != null) {
					m_tmodel.setValueAt(account, m_row, m_col);
				}
			}
		}
	}
}
