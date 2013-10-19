package pohaci.gumunda.titis.accounting.cgui.report;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.view.JRViewer;
import pohaci.gumunda.titis.accounting.cgui.UnitPicker;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.application.JMonthChooser;
import pohaci.gumunda.titis.application.JSpinField;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.OrganizationPicker;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.report.AccountbebanTree;

public class DepartmentCostPanel extends JPanel implements ActionListener,TreeSelectionListener {
	private static final long serialVersionUID = 1L;
	Connection m_conn;
	long m_sessionid;
	AccountbebanTree m_tree;
	JRViewer m_jrv;
	JPanel m_rightPanel = new JPanel();
	UnitPicker m_unitpicker;
	OrganizationPicker m_department;
	JButton m_preview;
	JToggleButton m_btnExcel;
	Account m_account;
	JMonthChooser m_monthcb;
	JSpinField m_yearField;
	HRMBusinessLogic m_hrmlogic;
	
	public DepartmentCostPanel(Connection conn,long sessionid){
		m_conn = conn;
		m_sessionid = sessionid;
		m_hrmlogic = new HRMBusinessLogic(m_conn);
		setJasper();
		constructComponent();
	}
	
	public void constructComponent(){
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		m_tree= new AccountbebanTree(m_conn,m_sessionid);
		m_tree.addTreeSelectionListener(this);
		
		m_preview = new JButton("Preview");
		m_preview.addActionListener(this);
		m_btnExcel = new JToggleButton(new ImageIcon("../images/excell.gif"));
		m_btnExcel.addActionListener(this);
		
		JLabel unitLbl = new JLabel("Unit");
		JLabel departmentLbl = new JLabel("Department");
		
		m_unitpicker  = new UnitPicker(m_conn,m_sessionid);
		m_department = new OrganizationPicker(m_conn,m_sessionid,true);
		
		JPanel compPanel  = new JPanel();
		compPanel.setPreferredSize(new Dimension(100,110));
		compPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(3, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(unitLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		compPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 2;
		compPanel.add(m_unitpicker, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(departmentLbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		compPanel.add(new JLabel("  "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridwidth = 2;
		compPanel.add(m_department, gridBagConstraints);
		
		JLabel periodlbl = new JLabel("Period");
		m_monthcb = new JMonthChooser(JMonthChooser.NO_SPINNER);
		
		m_yearField = new JSpinField();
		m_yearField.setMaximum(9999);
		m_yearField.setValue(new java.util.GregorianCalendar()
				.get(java.util.Calendar.YEAR));
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(1, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(periodlbl, gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		compPanel.add(new JLabel(" "), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.insets = new Insets(3, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(m_monthcb, gridBagConstraints);
		
		gridBagConstraints.gridx = 3;
		gridBagConstraints.insets = new Insets(3, 6, 2, 6);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		compPanel.add(m_yearField, gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		compPanel.add(m_preview, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		compPanel.add(m_btnExcel, gridBagConstraints);	
		
		JPanel leftTopPanel = new JPanel();
		leftTopPanel.setLayout(new BorderLayout());
		leftTopPanel.add(compPanel,BorderLayout.NORTH);		
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(leftTopPanel,BorderLayout.NORTH);
		leftPanel.add(new JScrollPane(m_tree), BorderLayout.CENTER);
		
		m_rightPanel.setLayout(new BorderLayout());
		m_rightPanel.add(m_jrv,BorderLayout.CENTER);
		
		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(m_rightPanel);
		splitPane.setDividerLocation(250);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		add(splitPane, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_preview){
			if (!cekValidity())
				return;
			getJasper(false);
		}
		else if (e.getSource() == m_btnExcel){
			if (!cekValidity())
				return;
			getJasper(true);
		}
	}
	
	public void setJasper(){
		DepartmentCost beginjasper = new DepartmentCost();
		setPanel(beginjasper);
	}
	
	private void setPanel(DepartmentCost jasper) {
		if (m_jrv != null)
			m_rightPanel.remove(m_jrv);
		m_jrv = jasper.getPrintView();
		m_rightPanel.add(m_jrv, BorderLayout.CENTER);
		m_rightPanel.validate();
	}
	
	private void addInvalid(String string) {
		validityMsgs.add(string);
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (m_account== null)
			addInvalid("Account must selected");
		if (m_department.getObject()==null)
			addInvalid("Department must selected");
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
	
	int m_page = 0;
	public void getDepartment(Organization org,DepartmentCost beginjasper,boolean isExcel){
		try {
			Organization[] suborg = m_hrmlogic.getSubOrganization(m_sessionid, IDBConstants.MODUL_MASTER_DATA, org.getIndex());			
			for(int i = 0; i < suborg.length; i++){
				Organization[] nextsuborg = m_hrmlogic.getSubOrganization(m_sessionid, IDBConstants.MODUL_MASTER_DATA, suborg[i].getIndex());				
				if (nextsuborg.length==0){
					DepartmentCost addjasper=new DepartmentCost(m_conn,m_sessionid,suborg[i],m_unitpicker.getUnit(),m_account,m_monthcb,m_yearField,isExcel);
					int size = addjasper.getJasperPrint().getPages().size();
					for (int j=0;j<size;j++){
						JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(j);
						beginjasper.getJasperPrint().addPage(m_page++,page);
					}
				}else{
					getDepartment(suborg[i],beginjasper,isExcel);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	private void getJasper(boolean isExcel) {
		try {
			m_page = 0;
			DepartmentCost beginjasper = null;
			Organization org = (Organization) m_department.getObject();
			if (org.getDescription().equals("Department")){					
				Organization[] orgroot = m_hrmlogic.getSuperOrganization(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
				beginjasper=new DepartmentCost(m_conn,m_sessionid,orgroot[0],m_unitpicker.getUnit(),m_account,m_monthcb,m_yearField,isExcel);
				for(int i = 0; i < orgroot.length; i++){
					Organization[] suborg = m_hrmlogic.getSubOrganization(m_sessionid, IDBConstants.MODUL_MASTER_DATA, orgroot[i].getIndex());
					if (suborg.length==0){
						DepartmentCost addjasper=new DepartmentCost(m_conn,m_sessionid,orgroot[i],m_unitpicker.getUnit(),m_account,m_monthcb,m_yearField,isExcel);
						int size = addjasper.getJasperPrint().getPages().size();
						for (int j=0;j<size;j++){
							JRPrintPage page = (JRPrintPage)addjasper.getJasperPrint().getPages().get(j);
							beginjasper.getJasperPrint().addPage(m_page++,page);
						}
					}else{
						getDepartment(orgroot[i],beginjasper,isExcel);
					}
				}					
				beginjasper.getJasperPrint().removePage(m_page);
			}
			else{
				beginjasper=new DepartmentCost(m_conn,m_sessionid,org,m_unitpicker.getUnit(),m_account,m_monthcb,m_yearField,isExcel);
				Organization[] suborg = m_hrmlogic.getSubOrganization(m_sessionid, IDBConstants.MODUL_MASTER_DATA, org.getIndex());
				if (suborg.length>0)
					beginjasper.getJasperPrint().removePage(m_page);
				getDepartment(org,beginjasper,isExcel);
			}
			
			if (isExcel)
				beginjasper.exportToExcel(beginjasper.getJasperPrint());
			setTopanel(beginjasper);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void setTopanel(DepartmentCost beginjasper) {
		if (m_jrv != null)
			m_rightPanel.remove(m_jrv);
		m_jrv = new JRViewer(beginjasper.getJasperPrint());
		m_jrv.repaint();
		m_jrv.revalidate();	
		m_rightPanel.add(m_jrv, BorderLayout.CENTER);
		m_rightPanel.validate();
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
		}
		else if (obj instanceof String) {	
		}
	}
	
	public void setAccount(Account acc){
		m_account =acc;		
	}
	
}
