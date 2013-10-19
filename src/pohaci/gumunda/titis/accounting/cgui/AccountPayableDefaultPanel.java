package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;

import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;

public class AccountPayableDefaultPanel extends JPanel implements ActionListener {
	//test nunung
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	
	AccountPicker m_accountPicker;
	JButton m_editBt, m_saveBt, m_cancelBt;
	Account m_account = null;
	
	public AccountPayableDefaultPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		constructComponent();
		initComponents();
		initData();
	}
	
	void constructComponent(){
		m_accountPicker = new AccountPicker(m_conn, m_sessionid);
		m_editBt = new JButton("Edit");
		m_editBt.addActionListener(this);
		m_saveBt = new JButton("Save");
		m_saveBt.addActionListener(this);
		m_cancelBt = new JButton("Cancel");
		m_cancelBt.addActionListener(this);
		
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		buttonPanel.add(m_editBt);
		buttonPanel.add(m_saveBt);
		buttonPanel.add(m_cancelBt);
		
		centerPanel.setLayout(new GridBagLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		Misc.setGridBagConstraints(centerPanel, new JLabel("Account"), gridBagConstraints, 0, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
		Misc.setGridBagConstraints(centerPanel, new JLabel(" "), gridBagConstraints, 1, 0,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, null);
		Misc.setGridBagConstraints(centerPanel, m_accountPicker, gridBagConstraints, 2, 0,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		Misc.setGridBagConstraints(centerPanel, buttonPanel, gridBagConstraints, 0, 1,
				GridBagConstraints.HORIZONTAL, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		Misc.setGridBagConstraints(centerPanel, new JPanel(), gridBagConstraints,
				0, 2, GridBagConstraints.BOTH, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, null);
		setLayout(new BorderLayout());
		add(centerPanel, BorderLayout.CENTER);
	}
	
	void initComponents() {
		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_accountPicker.setEnabled(false);
	}
	
	void initData() {
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			m_account = logic.getAccountPayableDefault(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
			m_accountPicker.setAccount(m_account);
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Information", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	void edit() {
		m_editBt.setEnabled(false);
		m_saveBt.setEnabled(true);
		m_cancelBt.setEnabled(true);
		m_accountPicker.setEnabled(true);
	}
	
	void save() {
		Account account = (Account)m_accountPicker.getAccount();
		try {
			AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
			logic.createAccountPayableDefault(m_sessionid, IDBConstants.MODUL_MASTER_DATA, account);
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_accountPicker.setEnabled(false);
	}
	
	void cancel() {
		m_editBt.setEnabled(true);
		m_saveBt.setEnabled(false);
		m_cancelBt.setEnabled(false);
		m_accountPicker.setEnabled(false);
		m_accountPicker.setAccount(m_account);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_editBt) {
			edit();
		}
		else if(e.getSource() == m_saveBt) {
			save();
		}
		else if(e.getSource() == m_cancelBt) {
			cancel();
		}
	}
}