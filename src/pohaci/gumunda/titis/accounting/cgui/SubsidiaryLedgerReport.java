package pohaci.gumunda.titis.accounting.cgui;

import javax.swing.*;

import net.sf.jasperreports.view.JRViewer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import pohaci.gumunda.titis.accounting.cgui.report.Subsidiary_ledger;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class SubsidiaryLedgerReport extends JPanel  implements ActionListener/*,ListSelectionListener*/  {
	private static final long serialVersionUID = 1L;
	SubsidiaryLedgerListPanel m_list;
	JRViewer m_jrv;
	Connection m_conn = null;
	long m_sessionid = -1;	
	PeriodSubsidiaryLedger m_periodStartEnd;
	JPanel m_centerPanel = new JPanel();
	JPanel m_listPanel = new JPanel();
	JButton m_btnView;
	JToggleButton m_btnExcel;
	private JComboBox m_subsidiaryCombo;
	SubsidiaryAccountSetting[] m_subsidiaryAccounts;
	boolean m_excel = false;
	
	public SubsidiaryLedgerReport(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		setJasper();
		initData();
		constructComponent();
	}	
	
	void constructComponent() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);  
		
		JLabel subsidiaryLabel = new JLabel("Subsidiary Ledger");		
		m_btnView = new JButton("View");
		m_btnView.addActionListener(this);		
		m_btnExcel = new JToggleButton(new ImageIcon("../images/excell.gif"));
		m_btnExcel.addActionListener(this);
		
		JPanel button2Panel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel addUnitPanel = new JPanel();
		JPanel endPanel = new JPanel();	
		JPanel panelSisa = new JPanel();
		
		m_periodStartEnd = new PeriodSubsidiaryLedger("Period");		
		
		m_list = new SubsidiaryLedgerListPanel(m_conn, m_sessionid);	
		if (m_subsidiaryAccounts[0]!=null)
			setListBySubsidiaryAccount(m_subsidiaryAccounts[0]);		
		
		addUnitPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(2, 4, 1, 1);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		addUnitPanel.add(subsidiaryLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		addUnitPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		addUnitPanel.add(m_subsidiaryCombo, gridBagConstraints);
		
		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(m_btnView);
		buttonPanel.add(m_btnExcel);
		
		endPanel.setPreferredSize(new Dimension(350,120));
		endPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		endPanel.add(addUnitPanel, gridBagConstraints);
		gridBagConstraints.gridy = 1;
		endPanel.add(m_periodStartEnd, gridBagConstraints);
		
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.RELATIVE;
		endPanel.add(buttonPanel, gridBagConstraints);
		
		panelSisa.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0; 
		gridBagConstraints.gridy = 0;		
		panelSisa.add(endPanel, gridBagConstraints); // centerpanel diincludkan kompenent menurut gridbag
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER; // mengisi sisa
		panelSisa.add(new JPanel(), gridBagConstraints);
		
		m_listPanel.setLayout(new BorderLayout());
		m_listPanel.add(m_list, BorderLayout.CENTER);
		
		m_centerPanel.setLayout(new BorderLayout());
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.add(button2Panel, BorderLayout.SOUTH);
		
		splitPane.setLeftComponent(m_listPanel);
		splitPane.setRightComponent(m_centerPanel);
		splitPane.setDividerLocation(200);
		
		setLayout(new BorderLayout());
		add(panelSisa, BorderLayout.NORTH);
		add(splitPane, BorderLayout.CENTER);
	}
	
	void initData(){
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		m_subsidiaryCombo = new JComboBox();
		m_subsidiaryCombo.addActionListener(this);
		try {
			m_subsidiaryAccounts = logic.getAllSubsidiaryAccountSetting(m_sessionid, IDBConstants.MODUL_ACCOUNTING);
			for(int i = 0; i < m_subsidiaryAccounts.length;  i ++) {				
				m_subsidiaryCombo.addItem(m_subsidiaryAccounts[i]);			
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
	}
	
	public void setJasper(){		
		Subsidiary_ledger jasper = new Subsidiary_ledger();
		m_jrv = jasper.getPrintView();
	}
	
	private void setPanel(Subsidiary_ledger jasper) {
		if (m_jrv != null)
			m_centerPanel.remove(m_jrv);
		m_jrv = jasper.getPrintView();
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.validate();
	}
	
	/*private void setPanel2(Subsidiary_ledger jasper) {
		if (m_jrv != null)
			m_centerPanel.remove(m_jrv);
		m_jrv = new JRViewer(jasper.getJasperPrint());
		m_jrv.repaint();
		m_jrv.revalidate();
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.validate();
	}*/
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_btnView){
			if (m_list.getSelected() instanceof BankAccount) {
				BankAccount bank = (BankAccount) m_list.getSelected();
				System.err.println("value print :" + bank.getIndex());				
			}
			
			if (!cekValidity()) 
				return;
			m_excel = false;
			getJasper();
		}
		else if(e.getSource() == m_btnExcel){
			if (m_list.getSelected() instanceof BankAccount) {
				BankAccount bank = (BankAccount) m_list.getSelected();
				System.err.println("value print :" + bank.getIndex());				
			}
			
			if (!cekValidity()) 
				return;
			m_excel = true;
			getJasper();
		}
		else if(e.getSource()==m_subsidiaryCombo){
			SubsidiaryAccountSetting subsidiariAcc = (SubsidiaryAccountSetting)m_subsidiaryCombo.getSelectedItem();
			setListBySubsidiaryAccount(subsidiariAcc);
		}
	}
	
	private void getJasper() {
		SubsidiaryAccountSetting subsidiariAcc = (SubsidiaryAccountSetting)m_subsidiaryCombo.getSelectedItem();
		Subsidiary_ledger jasper = new Subsidiary_ledger(m_conn,m_sessionid,subsidiariAcc,m_periodStartEnd,m_list.getSelected(),m_excel);
		if (!m_excel)
			setPanel(jasper);
	}

	void setListBySubsidiaryAccount(SubsidiaryAccountSetting subsidiariAcc){
		try{
			AccountingBusinessLogic accLogic = new AccountingBusinessLogic(m_conn);
			ProjectBusinessLogic proLogic = new ProjectBusinessLogic(m_conn);
			HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);	
			System.err.println(subsidiariAcc.getSubsidiaryAccount());
			Object obj=null;			
			if (subsidiariAcc.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.BANK)){
				String query = "SELECT * FROM " + IDBConstants.TABLE_BANK_ACCOUNT + " WHERE " + 
				IDBConstants.ATTR_ACCOUNT + "=" + subsidiariAcc.getAccount().getIndex();				
				BankAccount[] bankAcc = accLogic.getListAllBankAccount(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
				obj = bankAcc;
			}else if (subsidiariAcc.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.CASH)){
				String query = "SELECT * FROM " + IDBConstants.TABLE_CASH_ACCOUNT + " WHERE " +
				IDBConstants.ATTR_ACCOUNT + "=" + subsidiariAcc.getAccount().getIndex();;
				CashAccount[] cashAcc = accLogic.getListAllCashAccount(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
				obj = cashAcc;
			}else if (subsidiariAcc.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.CUSTOMER)){
				Customer[] cust = proLogic.getAllCustomer(m_sessionid,IDBConstants.MODUL_ACCOUNTING);
				obj = cust;
			}else if (subsidiariAcc.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.EMPLOYEE)){				
				Employee[] emp = hrmLogic.getAllEmployee(m_sessionid,IDBConstants.MODUL_ACCOUNTING);
				obj = emp;
			}else if (subsidiariAcc.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.LOAN)){
				String query = "SELECT * FROM " + IDBConstants.TABLE_COMPANY_LOAN + " WHERE " + 
				IDBConstants.ATTR_ACCOUNT + "=" + subsidiariAcc.getAccount().getIndex();
				CompanyLoan[] loan = accLogic.getListAllCompanyLoan(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
				obj = loan;
			}else if (subsidiariAcc.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.PARTNER)){
				Partner[] partner = proLogic.getAllPartner(m_sessionid,IDBConstants.MODUL_ACCOUNTING);
				obj = partner;
			}else if (subsidiariAcc.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.PROJECT)){
				long account = subsidiariAcc.getAccount().getIndex();
				ProjectData[] projects = proLogic.getProjectDataByAccount(m_sessionid, IDBConstants.MODUL_MASTER_DATA, account);
				obj = projects;
			}
			if (m_list!=null)
				m_listPanel.remove(m_list);
			m_list = new SubsidiaryLedgerListPanel(m_conn, m_sessionid,obj);
			m_listPanel.add(m_list, BorderLayout.CENTER);
			m_listPanel.validate();
			
		}catch(Exception ex){			
		}
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();		
		if (m_periodStartEnd.m_startDate.getDate()==null)
			addInvalid("Start Date must be selected");
		if (m_periodStartEnd.m_endDate.getDate()==null)
			addInvalid("End Date must be selected");	
		if (m_list.getSelected()==null)
			addInvalid("Subsidiary must be selected");
		if (validityMsgs.size()>0){
			StringBuffer result = new StringBuffer();
			Iterator iter = validityMsgs.iterator();
			while (iter.hasNext()){
				String o = iter.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this,result);
			return false;
		}
		return true;
	}
	
	private void addInvalid(String string) {
		validityMsgs.add(string);
	}

	/*public void valueChanged(ListSelectionEvent e) {
		System.err.println("value selected :" + m_list.getSelected());
	}*/
	
}