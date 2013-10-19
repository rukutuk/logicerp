package pohaci.gumunda.titis.accounting.cgui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.view.JRViewer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import pohaci.gumunda.titis.accounting.cgui.report.General_Ledger;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.AccountTree;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.*;

public class GenLedgerPanel extends JPanel  implements ActionListener, TreeSelectionListener  {
	private static final long serialVersionUID = 1L;
	AccountTree m_list;
	Account m_account=null;
	UnitPicker m_unitPicker;	
	JRViewer m_jrv;
	Connection m_conn = null;
	long m_sessionid = -1;	
	PeriodStartEnd m_periodStartEnd;
	JPanel m_centerPanel = new JPanel();
	JButton m_btnView;
	JToggleButton m_btnexcell;
	boolean m_root = false;
	
	public GenLedgerPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		setJasper(); 
		constructComponent();
	}	
	
	void constructComponent() {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);  
		
		JLabel unitLabel = new JLabel("Unit code");
		m_btnView = new JButton("View");
		m_btnView.addActionListener(this);
		m_btnexcell = new JToggleButton(new ImageIcon("../images/excell.gif"));
		m_btnexcell.addActionListener(this);
		
		JPanel listPanel = new JPanel();
		JPanel button2Panel = new JPanel();
		
		JPanel addUnitPanel = new JPanel();
		JPanel endPanel = new JPanel();
		
		m_periodStartEnd = new PeriodStartEnd("Period");
		m_unitPicker = new UnitPicker(m_conn,m_sessionid);
		
		
		m_list = new AccountTree(m_conn, m_sessionid);
		m_list.addTreeSelectionListener(this);
		
		addUnitPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(2, 4, 1, 1);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		addUnitPanel.add(unitLabel, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		addUnitPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		addUnitPanel.add(m_unitPicker, gridBagConstraints);
		
		gridBagConstraints.gridx = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(2, 4, 3, 4);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		addUnitPanel.add(m_btnView, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth=2;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.fill = GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		addUnitPanel.add(m_btnexcell, gridBagConstraints);
		
		endPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		endPanel.add(m_periodStartEnd, gridBagConstraints);
		gridBagConstraints.gridy = 1;
		endPanel.add(addUnitPanel, gridBagConstraints);
		
		listPanel.setLayout(new BorderLayout());
		listPanel.add(endPanel, BorderLayout.NORTH);
		listPanel.add(new JScrollPane(m_list), BorderLayout.CENTER);
		
		m_centerPanel.setLayout(new BorderLayout());
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.add(button2Panel, BorderLayout.SOUTH);
		
		splitPane.setLeftComponent(listPanel);
		splitPane.setRightComponent(m_centerPanel);
		splitPane.setDividerLocation(200);
		
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
	}
	
	AccountingBusinessLogic m_logic;
	public void setJasper(){
			General_Ledger jasper = new General_Ledger(m_conn,m_sessionid,m_account,m_periodStartEnd,m_unitPicker,m_excel);
			if (!m_excel)
				setPanel(jasper);			
		
	}
	
	int m_no = 0;
	private void getSubAccount(Account account,General_Ledger jasper){
		try {
			Account[] subaccount = m_logic.getSubAccount(m_sessionid, IDBConstants.MODUL_ACCOUNTING, account.getIndex());
			for (int i=0;i<subaccount.length;i++){	
				if (!subaccount[i].isGroup()){
					General_Ledger addjasper = new General_Ledger(m_conn,m_sessionid,subaccount[i],m_periodStartEnd,m_unitPicker,m_excel);
					int size = addjasper.getJasperPrint().getPages().size();
					for (int j=0;j<size;j++){
						JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(j);
						jasper.getJasperPrint().addPage(m_no++,page);
					}
				} else if (subaccount[i].isGroup()){
					getSubAccount(subaccount[i],jasper);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean m_excel;
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_btnView){
			if (!cekValidity()) 
				return;			
			if (m_account!=null){
				m_excel = false;
				if (m_account.isGroup()){			
					m_logic = new AccountingBusinessLogic(m_conn);					
					General_Ledger jasper = new General_Ledger(m_conn,m_sessionid,m_account,m_periodStartEnd,m_unitPicker,m_excel);					
					getSubAccount(m_account,jasper);
					jasper.getJasperPrint().removePage(m_no);
					m_no=0;
					setPanel1(jasper);
				}else{					
					setJasper();
				}
			}
		}
		else if (e.getSource()==m_btnexcell){
			if (!cekValidity()) 
				return;			
			if (m_account!=null){
				m_excel = true;
				if (m_account.isGroup()){			
					m_logic = new AccountingBusinessLogic(m_conn);					
					General_Ledger jasper = new General_Ledger(m_conn,m_sessionid,m_account,m_periodStartEnd,m_unitPicker,m_excel);					
					getSubAccount(m_account,jasper);
					jasper.getJasperPrint().removePage(m_no);
					jasper.exportexcel(jasper.getJasperPrint());
					m_no=0;
				}else{
					General_Ledger jasper = new General_Ledger(m_conn,m_sessionid,m_account,m_periodStartEnd,m_unitPicker,m_excel);
					jasper.exportexcel(jasper.getJasperPrint());
					//setJasper();
				}
			}
		}
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (m_account== null && m_root==false)
			addInvalid("Account must selected");
		if (m_periodStartEnd.m_startDate.getDate()==null)
			addInvalid("Start Date must selected");
		if (m_periodStartEnd.m_endDate.getDate()==null)
			addInvalid("End Date must selected");		
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
	
	DefaultMutableTreeNode m_node = null;
	public void valueChanged(TreeSelectionEvent e) {
		TreePath path = e.getNewLeadSelectionPath();
		if(path != null) {
			m_node = (DefaultMutableTreeNode)path.getLastPathComponent();
			setSelectedObject(m_node.getUserObject());
		}
	}
	
	void setSelectedObject(Object obj) {
		if (obj instanceof Account) {
			Account account = (Account) obj;
			setAccount(account);
			m_root = false;
		}
		if (obj instanceof String) {			
			m_root = true;
		}
	}
	
	private void setPanel(General_Ledger jasper) {
		if (m_jrv != null)
			m_centerPanel.remove(m_jrv);
		m_jrv = jasper.getPrintView();
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.validate();
	}
	
	private void setPanel1(General_Ledger jasper) {
		if (m_jrv != null)
			m_centerPanel.remove(m_jrv);
		m_jrv = new JRViewer(jasper.getJasperPrint());
		m_jrv.repaint();
		m_jrv.revalidate();
		m_centerPanel.add(m_jrv, BorderLayout.CENTER);
		m_centerPanel.validate();
	}
	
	
	public void setAccount(Account acc){
		m_account =acc;		
	}
	
}