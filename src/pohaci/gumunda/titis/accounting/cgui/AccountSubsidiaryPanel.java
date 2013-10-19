package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceTransactionCode;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;

public class AccountSubsidiaryPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	SubsidiaryTable m_table;
	JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;
	Connection m_conn = null;
	long m_sessionid = -1;
	boolean m_new = false, m_edit = false;
	int m_editedIndex = -1;
	SubsidiaryAccountSetting[] m_subsidiaryAccounts;
	SubsidiaryAccountSetting m_subsidiaryAccount;
	JComboBox m_subsidiaryAccountCombo, m_bbTransCode;
	
	public AccountSubsidiaryPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
		initData();
	}
	void constructComponent() {		
		JPanel buttonPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		
		m_table = new SubsidiaryTable();
		m_subsidiaryAccountCombo = new JComboBox(new Object[]{"Cash","Bank","Employee","Customer","Partner","Loan","Project"});
		m_bbTransCode = new JComboBox(BeginningBalanceTransactionCode.getList());
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		m_addBt = new JButton("Add");
		m_addBt.addActionListener(this);
		buttonPanel.add(m_addBt);
		m_editBt = new JButton("Edit");
		m_editBt.addActionListener(this);
		buttonPanel.add(m_editBt);
		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		buttonPanel.add(m_saveBt);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);;
		buttonPanel.add(m_cancelBt);
		m_deleteBt = new JButton("Delete");
		m_deleteBt.addActionListener(this);;
		buttonPanel.add(m_deleteBt);
		
		m_addBt.setEnabled(true);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_deleteBt.setEnabled(false);
		
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		setLayout(new BorderLayout());
		add(centerPanel, BorderLayout.CENTER);
	}
	
	void initData() {
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		model.setRowCount(0);
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			m_subsidiaryAccounts = logic.getAllSubsidiaryAccountSetting(m_sessionid, IDBConstants.MODUL_ACCOUNTING);
			for(int i = 0; i < m_subsidiaryAccounts.length;  i ++) {
				model.addRow(new Object[]{
						m_subsidiaryAccounts[i], m_subsidiaryAccounts[i].getSubsidiaryAccount(),
						new BeginningBalanceTransactionCode(m_subsidiaryAccounts[i].getTranscode())
				});
			}
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	void onNew() {
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
	
	void onEdit() {
		m_edit = true;
		m_editedIndex = m_table.getSelectedRow();
		m_addBt.setEnabled(false);
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		m_deleteBt.setEnabled(false);
	}
	
	void onSave() {
		m_table.stopCellEditing();
		java.util.ArrayList list = new java.util.ArrayList();
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		Account account = null;
		String SubsidiaryAccount="";
		int col = 0;
		Object object = model.getValueAt(m_editedIndex, col++);	 
		if(object instanceof Account)
			account = (Account)object;
		else{
			if (m_subsidiaryAccounts[m_editedIndex]!=null)
				account = m_subsidiaryAccounts[m_editedIndex].getAccount();
			else
				list.add("Account");
		}	    
		
		object = model.getValueAt(m_editedIndex, col++);
		BeginningBalanceTransactionCode tc = (BeginningBalanceTransactionCode) model.getValueAt(m_editedIndex,col++);
		if (object!=null)
			SubsidiaryAccount = object.toString();
		else
			list.add("Subsidiary account");	    
		
		String strexc = "Please insert :\n";
		String[] exception = new String[list.size()];
		list.toArray(exception);
		if(exception.length > 0) {
			for(int i = 0; i < exception.length; i ++)
				strexc += exception[i] + "\n";
			JOptionPane.showMessageDialog(this, strexc);
			return;
		}
		
		SubsidiaryAccountSetting accSetting = new SubsidiaryAccountSetting();
		accSetting.setAccount(account);
		accSetting.setSubsidiaryAccount(SubsidiaryAccount);
		accSetting.setTranscode(tc.getIdx());
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			if(m_new){	    	 
				logic.createSubsidiaryAccountSetting(m_sessionid, IDBConstants.MODUL_MASTER_DATA,accSetting);
				m_subsidiaryAccounts = logic.getAllSubsidiaryAccountSetting(m_sessionid, IDBConstants.MODUL_ACCOUNTING);
			}
			else if(m_edit){	    	 
				accSetting.setAutoindex(m_subsidiaryAccounts[m_editedIndex].getAutoindex());
				logic.updateSubsidiaryAccountSetting(m_sessionid, IDBConstants.MODUL_ACCOUNTING, accSetting);
				m_table.updateSubsidiaryAccountSetting(accSetting, m_editedIndex);
			}
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
	
	void onCancel() {
		m_table.stopCellEditing();
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		if(m_new){
			m_new = false;
			model.removeRow(m_editedIndex);
		}
		else if(m_edit){
			m_edit = false;	     
			m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
			m_addBt.setEnabled(true);
			m_editBt.setEnabled(true);
			m_saveBt.setEnabled(false);
			m_cancelBt.setEnabled(false);
			m_deleteBt.setEnabled(true);
		}
	}
	
	void onDelete() {
		DefaultTableModel model = (DefaultTableModel)m_table.getModel();
		m_editedIndex = m_table.getSelectedRow();
		long index = m_subsidiaryAccounts[m_editedIndex].getAutoindex();
		try{
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			logic.deleteSubsidiaryAccountSetting(m_sessionid, IDBConstants.MODUL_MASTER_DATA, index);
			m_subsidiaryAccounts = logic.getAllSubsidiaryAccountSetting(m_sessionid, IDBConstants.MODUL_ACCOUNTING);
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		model.removeRow(m_editedIndex);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_addBt) {
			onNew();
		}
		else if(e.getSource() == m_editBt) {
			onEdit();
		}
		else if(e.getSource() == m_saveBt) {
			onSave();
		}
		else if(e.getSource() == m_cancelBt) {
			onCancel();
		}
		else if(e.getSource() == m_deleteBt) {
			onDelete();
		}
	}
	
	class SubsidiaryTable extends JTable {
		private static final long serialVersionUID = 1L;
		
		public SubsidiaryTable() {
			BankLoanTableModel model = new BankLoanTableModel();
			model.addColumn("Account");
			model.addColumn("Subsidiary Account");
			model.addColumn("BB Transaction Code");
			setModel(model);
			
			getSelectionModel().addListSelectionListener(model);
		}
		
		public TableCellEditor getCellEditor(int row, int col) {
			if (col==0)
				return new AccountTreeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
						m_conn, m_sessionid);
			else if(col == 1)
				return new DefaultCellEditor(m_subsidiaryAccountCombo);	
			else if (col == 2)
				return new DefaultCellEditor(m_bbTransCode);
			return new BaseTableCellEditor();
		}
		
		
		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}
		
		public void updateSubsidiaryAccountSetting(SubsidiaryAccountSetting accSetting, int row) {
			DefaultTableModel model = (DefaultTableModel)m_table.getModel();
			model.removeRow(row);
			model.insertRow(row, new Object[]{
					accSetting, accSetting.getSubsidiaryAccount(), new BeginningBalanceTransactionCode(accSetting.getTranscode())
			});
			
			this.getSelectionModel().setSelectionInterval(row, row);
		}
	}
	
	class BankLoanTableModel extends DefaultTableModel implements ListSelectionListener {
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {
			if((m_new || m_edit)&& row == m_editedIndex)
				return true;
			return false;
		}
		
		public void setValueAt(Object obj, int row, int col) {
			if(obj instanceof CreditorList) {
				super.setValueAt(new CreditorList((CreditorList)obj, CreditorList.CODE), row, col);
			}
			else
				super.setValueAt(obj, row, col);
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
