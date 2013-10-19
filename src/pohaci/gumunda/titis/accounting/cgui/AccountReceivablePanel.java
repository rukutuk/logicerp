package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import pohaci.gumunda.titis.project.cgui.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.AccountReceivable;

public class AccountReceivablePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	AccountReceivableTable m_table;
	JButton m_addBt, m_editBt, m_deleteBt, m_saveBt, m_cancelBt;
	
	int m_editedIndex = -1;
	boolean m_new = false, m_edit = false;
	AccountReceivable m_receivable = null;
	
	public AccountReceivablePanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
		initData();
	}
	
	void constructComponent() {
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel buttonPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		
		m_table = new AccountReceivableTable();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		m_addBt = new JButton("Add");
		m_addBt.addActionListener(this);
		buttonPanel.add(m_addBt);
		m_editBt = new JButton("Edit");
		m_editBt.addActionListener(this);
		buttonPanel.add(m_editBt);
		m_deleteBt = new JButton("Delete");
		m_deleteBt.addActionListener(this);
		buttonPanel.add(m_deleteBt);
		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		buttonPanel.add(m_saveBt);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);
		buttonPanel.add(m_cancelBt);
		
		m_addBt.setEnabled(true);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_deleteBt.setEnabled(false);
		
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		tabbedPane.addTab("Customer", centerPanel);
		tabbedPane.addTab("Default", new AccountReceivableDefaultPanel(m_conn, m_sessionid));
		
		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_addBt) 
			add();
		else if(e.getSource() == m_editBt) 
			edit();
		else if(e.getSource() == m_saveBt) 
			save();
		else if(e.getSource() == m_cancelBt) 
			cancel();
		else if(e.getSource() == m_deleteBt) 
			delete();
		
	}
	
	void initData() {
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		model.setRowCount(0);
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			AccountReceivable[] receivable = logic.getAllAccountReceivable(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			for(int i = 0; i < receivable.length;  i ++) {
				model.addRow(new Object[]{
						receivable[i].getCustomer(), receivable[i].getAccount()
				});
			}
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	void add() {
		m_new = true;
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		model.addRow(new Object[]{});
		m_editedIndex = model.getRowCount() - 1;
		m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
		m_addBt.setEnabled(false);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		m_deleteBt.setEnabled(false);
	}
	
	void edit() {
		m_edit = true;
		m_editedIndex = m_table.getSelectedRow();
		m_receivable = new AccountReceivable((Customer)m_table.getValueAt(m_editedIndex, 0),
				(Account)m_table.getValueAt(m_editedIndex, 1));
		m_addBt.setEnabled(false);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		m_deleteBt.setEnabled(false);
	}
	
	void delete() {
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		m_editedIndex = m_table.getSelectedRow();
		Customer customer = (Customer)model.getValueAt(m_editedIndex, 0);
		Account account = (Account)model.getValueAt(m_editedIndex, 1);
		try{
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			logic.deleteAccountReceivable(m_sessionid, IDBConstants.MODUL_MASTER_DATA, customer.getIndex(), account.getIndex());
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		model.removeRow(m_editedIndex);
	}
	
	void save() {
		m_table.stopCellEditing();
		java.util.ArrayList list = new java.util.ArrayList();
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		Customer customer = null;
		Account account = null;
		Object objValue = model.getValueAt(m_editedIndex, 0);
		if(objValue instanceof Customer)
			customer = (Customer)objValue;
		else
			list.add("Customer");
		objValue = model.getValueAt(m_editedIndex, 1);
		if(objValue instanceof Account)
			account = (Account)objValue;
		else
			list.add("Account");
		String strexc = "Please insert :\n";
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if(exception.length > 0) {
			for(int i = 0; i < exception.length; i ++)
				strexc += exception[i] + "\n";
			JOptionPane.showMessageDialog(this, strexc);
			return;
		}
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			if(m_new)
				logic.createAccountReceivable(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
						customer.getIndex(), account.getIndex());
			else
				logic.updateAccountReceivable(m_sessionid, IDBConstants.MODUL_MASTER_DATA,
						customer.getIndex(), account.getIndex());
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		m_new = false;
		m_edit = false;
		m_addBt.setEnabled(true);
		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_deleteBt.setEnabled(true);
	}
	
	void cancel() {
		m_table.stopCellEditing();
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		if(m_new){
			m_new = false;
			model.removeRow(m_editedIndex);
		}
		else if(m_edit){
			m_edit = false;
			model.removeRow(m_editedIndex);
			model.insertRow(m_editedIndex, new Object[]{m_receivable.getCustomer(),m_receivable.getAccount()});
			m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
			m_addBt.setEnabled(true);
			m_editBt.setEnabled(true);
			m_saveBt.setEnabled(false);
			m_cancelBt.setEnabled(false);
			m_deleteBt.setEnabled(true);
		}
	}
	
	class AccountReceivableTable extends JTable {
		private static final long serialVersionUID = 1L;
		public AccountReceivableTable() {
			AccountReceivableTableModel model = new AccountReceivableTableModel();
			model.addColumn("Customer");
			model.addColumn("Account");
			setModel(model);
			getSelectionModel().addListSelectionListener(model);
		}
		
		public TableCellEditor getCellEditor(int row, int col) {
			if(col == 0)
				return new CustomerCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn, m_sessionid);
			if(col == 1)
				return new AccountTreeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn, m_sessionid);
			return super.getCellEditor(row, col);
		}
		
		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}
	}
	
	class AccountReceivableTableModel extends DefaultTableModel implements ListSelectionListener {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col) {
			if((m_new || m_edit)&& row == m_editedIndex) {
				if(m_edit) {
					if(col == 0)
						return false;
				}
				return true;
			}
			return false;
		}
		
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()){
				int iRowIndex = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();
				if(m_new || m_edit){
					if(iRowIndex != m_editedIndex)
						m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
				}
				else{
					if(iRowIndex != -1){
						m_addBt.setEnabled(true);
						m_editBt.setEnabled(true);
						m_saveBt.setEnabled(false);
						m_cancelBt.setEnabled(false);
						m_deleteBt.setEnabled(true);
					}
					else{
						m_addBt.setEnabled(true);
						m_editBt.setEnabled(false);
						m_saveBt.setEnabled(false);
						m_cancelBt.setEnabled(false);
						m_deleteBt.setEnabled(false);
					}
				}
			}
		}
	}
}