package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.cgui.report.MJ_Standard;
import pohaci.gumunda.titis.accounting.dbapi.*;
import pohaci.gumunda.titis.accounting.entity.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;
import pohaci.gumunda.titis.application.db.*;
import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.*;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class MJStandardPanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MemJournalStrd m_entity;	
	private Employee m_defaultOriginator,m_defaultApproved;
	private JButton m_addMJStandardAccountBtn,m_deleteMJStandardAccountBtn;
	private AssignPanel m_approvedComp,m_originatorComp;
	private LookupDepartmentPicker m_departmentComp;
	private JScrollPane m_descScroll,m_MJStandAccountScroll,m_remarksScroll;
	private JTextArea m_descTxtArea,m_remarksTxtArea;	
	private JTextField m_refNoText,m_statusText,m_submittedDateText;	
	private LookupMJStandardPicker m_transactionCodeComp;
	private pohaci.gumunda.titis.application.DatePicker m_transactionDateDate;
	private UnitPicker m_unitCodeComp;
	private JPanel m_buttomButtonPanel,m_jPanel1,m_jPanel1_1,m_jPanel1_2,m_jPanel1_2_1,m_jPanel1_3,
	m_jPanel1_3_2,m_jPanel2,m_jPanel2_1,m_topButtonPanel;
	public MemJournalStrdTable m_table;
	private LookupReceiveTypeAnak m_memJournalStrdComp;
	private JRadioButton m_singleRb = new JRadioButton("Single");
	private JRadioButton m_multipleRb = new JRadioButton("Multiple");
	
	public MJStandardPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid); 
		setDefaultSignature(); 
		setEntity(new MemJournalStrd());
		m_entityMapper = MasterMap.obtainMapperFor(MemJournalStrd.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	private void initComponents() {
		JLabel statusLbl = new JLabel("Status");
		JLabel submittedDateLbl = new JLabel("Submitted Date");
		JLabel transactionLbl = new JLabel("Transaction Code*");
		JLabel transactionDateLbl = new JLabel("Transaction Date*");
		JLabel memorialJournalNoLbl = new JLabel("Memorial Journal No");
		JLabel descriptionLbl = new JLabel("Description*");
		JLabel unitLbl = new JLabel("Unit Code*");		
		JLabel remarksLbl=new JLabel("Remarks");
		JLabel departmentLbl = new JLabel("Department*");
		
		m_newBtn = new JButton("New");
		m_editBtn = new JButton("Edit");
		m_saveBtn = new JButton("Save");
		m_deleteBtn = new JButton("Delete");
		m_cancelBtn = new JButton("Cancel");
		m_submitBtn = new JButton("Submit");
		m_addMJStandardAccountBtn = new JButton("Add");
		m_deleteMJStandardAccountBtn = new JButton("Delete");					
		
		m_statusText = new JTextField();
		m_submittedDateText = new JTextField();
		m_refNoText = new JTextField();
		
		m_remarksScroll=new JScrollPane();
		m_descScroll = new JScrollPane();
		m_MJStandAccountScroll = new JScrollPane();
		
		m_remarksTxtArea= new JTextArea();
		m_descTxtArea = new JTextArea();
		
		m_transactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		
		m_topButtonPanel = new JPanel();
		m_jPanel1 = new JPanel();
		m_jPanel1_1 = new JPanel();  
		m_jPanel1_2 = new JPanel();
		m_jPanel1_2_1 = new JPanel();
		m_jPanel1_3 = new JPanel();
		m_jPanel1_3_2 = new JPanel();
		m_jPanel2 = new JPanel();
		m_jPanel2_1 = new JPanel();		
		m_buttomButtonPanel = new JPanel();
		
		m_transactionCodeComp = new LookupMJStandardPicker (m_conn,m_sessionid);
		m_departmentComp= new LookupDepartmentPicker(m_conn,m_sessionid);
		m_unitCodeComp = new UnitPicker(m_conn,m_sessionid);
		m_originatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		m_approvedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		
		m_table = new MemJournalStrdTable();
		
		setLayout(new BorderLayout());
		GridBagConstraints gridBagConstraints;
		
		setPreferredSize(new Dimension(500, 675));
		m_jPanel1.setLayout(new BorderLayout());
		
		m_jPanel1.setPreferredSize(new Dimension(650, 480));
		m_jPanel1_1.setLayout(new BorderLayout());
		
		m_jPanel1_1.setPreferredSize(new Dimension(650, 35));
		m_topButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		
		m_topButtonPanel.setPreferredSize(new Dimension(650, 35));
		m_searchRefNoBtn.setPreferredSize(new Dimension(21, 21));
		m_topButtonPanel.add(m_searchRefNoBtn);
		
		m_printViewRefNoBtn.setPreferredSize(new Dimension(21, 21));
		m_topButtonPanel.add(m_printViewRefNoBtn);
		
		m_newBtn.setMargin(new Insets(2, 2, 2, 2));
		m_newBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_newBtn);
		
		m_editBtn.setMargin(new Insets(2, 2, 2, 2));
		m_editBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_editBtn);
		
		m_deleteBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deleteBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_deleteBtn);
		
		m_saveBtn.setMargin(new Insets(2, 2, 2, 2));
		m_saveBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_saveBtn);
		
		m_cancelBtn.setMargin(new Insets(2, 2, 2, 2));
		m_cancelBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_cancelBtn);
		
		m_submitBtn.setMargin(new Insets(2, 2, 2, 2));
		m_submitBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_submitBtn);
		
		m_jPanel1_1.add(m_topButtonPanel, BorderLayout.WEST);
		
		m_jPanel1.add(m_jPanel1_1, BorderLayout.NORTH);
		
		m_jPanel1_2.setLayout(new BorderLayout());
		
		m_jPanel1_2.setPreferredSize(new Dimension(650, 350));
		m_jPanel1_2_1.setLayout(new GridBagLayout());
		
		m_jPanel1_2_1.setPreferredSize(new Dimension(570, 200));
		statusLbl.setText("Status");
		statusLbl.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 35, 1, 1);
		m_jPanel1_2_1.add(statusLbl, gridBagConstraints);
		
		submittedDateLbl.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 35, 1, 1);
		m_jPanel1_2_1.add(submittedDateLbl, gridBagConstraints);
		
		transactionLbl.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 35, 1, 1);
		m_jPanel1_2_1.add(transactionLbl, gridBagConstraints);
		
		transactionDateLbl.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 35, 1, 1);
		m_jPanel1_2_1.add(transactionDateLbl, gridBagConstraints);
		
		memorialJournalNoLbl.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 35, 1, 1);
		m_jPanel1_2_1.add(memorialJournalNoLbl, gridBagConstraints);
		
		descriptionLbl.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 35, 1, 1);
		m_jPanel1_2_1.add(descriptionLbl, gridBagConstraints);
		
		unitLbl.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 35, 1, 1);
		m_jPanel1_2_1.add(unitLbl, gridBagConstraints);
		
		departmentLbl.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 35, 1, 1);
		m_jPanel1_2_1.add(departmentLbl, gridBagConstraints);
		
		m_statusText.setPreferredSize(new Dimension(370, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_statusText, gridBagConstraints);
		
		m_submittedDateText.setPreferredSize(new Dimension(370, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_submittedDateText, gridBagConstraints);
		
		m_transactionCodeComp.setPreferredSize(new Dimension(370, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_transactionCodeComp, gridBagConstraints);
		
		m_transactionDateDate.setPreferredSize(new Dimension(370, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_jPanel1_2_1.add(m_transactionDateDate, gridBagConstraints);
		
		m_refNoText.setPreferredSize(new Dimension(370, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_refNoText, gridBagConstraints);
		
		m_descScroll.setPreferredSize(new Dimension(370, 64));
		m_descTxtArea.setColumns(20);
		m_descTxtArea.setLineWrap(true);
		m_descTxtArea.setRows(5);
		m_descScroll.setViewportView(m_descTxtArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_descScroll, gridBagConstraints);
		
		m_unitCodeComp.setPreferredSize(new Dimension(370, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_unitCodeComp, gridBagConstraints);
		
		JPanel groupdeptPanel = new JPanel();
		groupdeptPanel.setPreferredSize(new java.awt.Dimension(370, 50));
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(m_singleRb);
		bg1.add(m_multipleRb);
		m_singleRb.setSelected(true);
		
		groupdeptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Department",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11)));
		groupdeptPanel.add(m_singleRb);
		groupdeptPanel.add(m_multipleRb);
		
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		m_jPanel1_2_1.add(groupdeptPanel, gridBagConstraints);
		
		m_departmentComp.setPreferredSize(new Dimension(370, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_departmentComp, gridBagConstraints);
		
		
		remarksLbl.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 35, 1, 1);
		m_jPanel1_2_1.add(remarksLbl, gridBagConstraints);
		
		m_remarksScroll.setPreferredSize(new Dimension(370, 64));
		m_remarksTxtArea.setColumns(20);
		m_remarksTxtArea.setLineWrap(true);
		m_remarksTxtArea.setRows(5);
		m_remarksScroll.setViewportView(m_remarksTxtArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		
		m_jPanel1_2_1.add(m_remarksScroll, gridBagConstraints);
		
		m_jPanel1_2.add(m_jPanel1_2_1, BorderLayout.WEST);
		
		m_jPanel1.add(m_jPanel1_2, BorderLayout.CENTER);
		
		m_jPanel1_3.setLayout(new BorderLayout());
		
		m_jPanel1_3.setPreferredSize(new Dimension(650, 110));
		m_originatorComp.setLayout(new GridBagLayout());
		
		m_originatorComp.setBorder(BorderFactory.createTitledBorder("Originator"));
		m_originatorComp.setOpaque(false);
		m_originatorComp.setPreferredSize(new Dimension(275, 110));
		m_jPanel1_3.add(m_originatorComp, BorderLayout.WEST);
		
		m_jPanel1_3_2.setLayout(new BorderLayout());
		
		m_jPanel1_3_2.setPreferredSize(new Dimension(430, 110));
		m_approvedComp.setLayout(new GridBagLayout());
		
		m_approvedComp.setBorder(BorderFactory.createTitledBorder("Approved by"));
		m_approvedComp.setOpaque(false);
		m_approvedComp.setPreferredSize(new Dimension(275, 110));
		m_jPanel1_3_2.add(m_approvedComp, BorderLayout.WEST);
		
		m_jPanel1_3.add(m_jPanel1_3_2, BorderLayout.CENTER);
		
		m_jPanel1.add(m_jPanel1_3, BorderLayout.SOUTH);
		
		add(m_jPanel1, BorderLayout.NORTH);
		
		m_jPanel2.setLayout(new BorderLayout());
		
		m_jPanel2.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		m_jPanel2.setPreferredSize(new Dimension(650, 250));
		m_jPanel2_1.setLayout(new BorderLayout());
		
		m_jPanel2_1.setPreferredSize(new Dimension(650, 35));
		m_buttomButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		
		m_buttomButtonPanel.setPreferredSize(new Dimension(650, 35));
		
		m_addMJStandardAccountBtn.setMargin(new Insets(2, 2, 2, 2));
		m_addMJStandardAccountBtn.setPreferredSize(new Dimension(50, 21));
		m_buttomButtonPanel.add(m_addMJStandardAccountBtn);
		
		m_deleteMJStandardAccountBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deleteMJStandardAccountBtn.setPreferredSize(new Dimension(50, 21));
		m_buttomButtonPanel.add(m_deleteMJStandardAccountBtn);
		
		m_jPanel2_1.add(m_buttomButtonPanel, BorderLayout.WEST);		
		m_jPanel2.add(m_jPanel2_1, BorderLayout.NORTH);
		m_MJStandAccountScroll.setPreferredSize(new Dimension(650, 200));		
		m_MJStandAccountScroll.setViewportView(m_table);		
		m_jPanel2.add(m_MJStandAccountScroll, BorderLayout.CENTER);
		
		add(m_jPanel2, BorderLayout.CENTER);
		
	}
	
	private void enableAwal(){ 	
		setenableEditPanel(m_jPanel1_2_1,false);		
		setEnableButtonBawah(false);
	}
	
	public void setEnableButtonBawah(boolean bool){
		m_addMJStandardAccountBtn.setEnabled(bool);
		m_deleteMJStandardAccountBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}
	
	protected void clearForm() {		
		m_table.clearTable();
	}
	
	private void addingListener(){
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);		
		m_addMJStandardAccountBtn.addActionListener(this);
		m_deleteMJStandardAccountBtn.addActionListener(this);
		m_transactionCodeComp.addPropertyChangeListener("object",this);
		m_departmentComp.addPropertyChangeListener(this);
		m_singleRb.addActionListener(this);
		m_multipleRb.addActionListener(this);
	}  
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new MJ_Standard(m_entity,m_conn,baseCurrency);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if (e.getSource()==m_singleRb){
			m_departmentComp.setEnabled(true);
			int maxRow = m_table.getRowCount() ;
			for (int i=0;i<maxRow;i++){
				if (m_departmentComp.getObject() instanceof Organization) {
					Organization org = (Organization) m_departmentComp.getObject();
					m_table.setValueAt(org,i,8);					
				}else{
					m_table.setValueAt("",i,8);
				}
			}
		}
		else if (e.getSource()==m_multipleRb){
			m_departmentComp.setEnabled(false);
			m_departmentComp.setObject(null);
			int maxRow = m_table.getRowCount();
			for (int i=0;i<maxRow;i++){
				m_table.setValueAt("",i,8);
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {      	
			SearchMemorialJournalDialog dlg = new SearchMemorialJournalDialog(GumundaMainFrame.getMainFrame(), "Search Memorial Journal","Standard", m_conn, m_sessionid,new MemJournalStrdLoader(m_conn,MemJournalStrd.class));
			dlg.setVisible(true);
			if (dlg.selectedObj != null){    			
				doLoad(dlg.selectedObj);				
			}
		}
		else if (e.getSource() == m_addMJStandardAccountBtn){
			if (m_transactionCodeComp.getObject()!=null){
				JournalStandard js = (JournalStandard)m_transactionCodeComp.getObject();
				m_memJournalStrdComp = new LookupReceiveTypeAnak(m_conn,m_sessionid,js,"Memorial Journal Standard");
				m_memJournalStrdComp.done();
				if (m_memJournalStrdComp.getObject()!=null){
					int row = m_table.getRowCount()-2;
					Vector temp1=new Vector();
					JournalStandardAccount journal = (JournalStandardAccount)m_memJournalStrdComp.getObject();
					MemJournalStrdDet mjDetail = new MemJournalStrdDet();					
					mjDetail.setAccount(journal.getAccount());
					if(journal.getBalance()==0)
						mjDetail.setBalanceCode(Account.STR_DEBET);
					else
						mjDetail.setBalanceCode(Account.STR_CREDIT);	
					Account acc = mjDetail.getAccount();
					
					temp1.addElement(null);
					temp1.addElement(acc.getCode());
					temp1.addElement(acc.getName());
					temp1.addElement(null);
					temp1.addElement(new Double(0));
					temp1.addElement(new Double(0));
					temp1.addElement(baseCurrency);
					temp1.addElement(new Double(1));
					temp1.addElement(m_departmentComp.getObject());
					m_table.addData(temp1, mjDetail, row);
				}
			}else{
				JOptionPane.showMessageDialog(this, "Please select transaction code first",
						"Warning", JOptionPane.WARNING_MESSAGE);	
			}
		}else if (e.getSource() == m_deleteMJStandardAccountBtn){
			int row = m_table.getSelectedRow();
			int maxRow = m_table.getRowCount()-1;
			if(row>-1){
				if(row<(maxRow-1)){
					m_table.removeData(row);
					m_table.updateSummary();
				}
			}
		}
	} 
	
	public void doEdit() {		
		super.doEdit();
	}
	
	protected void doNew() {
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
	}
	
	protected void deleteChilds() {
		DeleteAnaknya(m_entity);
		super.deleteChilds();		
	}
	
	private void DeleteAnaknya(MemJournalStrd old){
		GenericMapper mapper2=MasterMap.obtainMapperFor(MemJournalStrdDet.class);
		mapper2.setActiveConn(m_conn);   
		mapper2.doDeleteByColumn(IDBConstants.ATTR_MEMORIAL_JOURNAL_STANDARD,new Long(old.getIndex()).toString());
	}
	
	protected void clearAll() {		
		super.clearAll();
		clearForm();
		clearKomponen();
		disableEditMode();
	}
	
	public void clearKomponen(){
		m_statusText.setText("");		
		m_originatorComp.setEmployee(null);
		m_approvedComp.setEmployee(null);
		m_originatorComp.m_jobTextField.setText("");
		m_approvedComp.m_jobTextField.setText("");
		m_originatorComp.setDate(null);
		m_approvedComp.setDate(null);			
		m_transactionDateDate.setDate(null);
	}
	
	public void LoadDetail(long index){
		GenericMapper mapper2=MasterMap.obtainMapperFor(MemJournalStrdDet.class);
		mapper2.setActiveConn(m_conn);
		List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_MEMORIAL_JOURNAL_STANDARD+"="+index);
		m_table.clearTable();
		MemJournalStrdDet detail;
		Vector temp1;
		Account acc;
		for(int i=0;i<detailList.size();i++){
			int row = m_table.getRowCount()-2;
			temp1=new Vector();
			detail=(MemJournalStrdDet)detailList.get(i);			
			acc=(Account)detail.getAccount();
			temp1.addElement(new Integer(i+1));
			temp1.addElement(acc.getCode());
			temp1.addElement(acc.getName());			
			String SubsidiaryAccSet = getSubsidiaryByindex(acc);
			AccountingBusinessLogic accLogic = new AccountingBusinessLogic(m_conn);
			ProjectBusinessLogic proLogic = new ProjectBusinessLogic(m_conn);
			HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			if (SubsidiaryAccSet.equals("Employee")){				
				SetEmployeeToTable(detail, temp1, hrmLogic);					
			}else if (SubsidiaryAccSet.equals("Partner")){
				SetPartnerToTable(detail, temp1, proLogic);				
			}else if (SubsidiaryAccSet.equals("Customer")){
				SetCustomerToTable(detail, temp1, proLogic);
			}else if (SubsidiaryAccSet.equals("Bank")){			
				SetBankAccountToTable(detail, temp1, accLogic);				
			}else if (SubsidiaryAccSet.equals("Cash")){
				SetCashAccountToTable(detail, temp1, accLogic);
			}else if (SubsidiaryAccSet.equals("Loan")){
				SetLoanToTable(detail, temp1, accLogic);
			}else if (SubsidiaryAccSet.equals("Project")){
				SetProjectToTable(detail, temp1, proLogic );
			}else{
				temp1.addElement("");
			}
			if (detail.getBalanceCode().equalsIgnoreCase("Debit")){
				temp1.addElement(new Double(detail.getAccValue()));
				temp1.addElement(new Double(0));
			}else if (detail.getBalanceCode().equalsIgnoreCase("Credit")){
				temp1.addElement(new Double(0));
				temp1.addElement(new Double(detail.getAccValue()));
			}
			temp1.addElement(detail.getCurrency());
			temp1.addElement(new Double(detail.getExchangeRate()));	
			if (detail.getDepartment()!=null)
				temp1.addElement(detail.getDepartment());
			m_table.addData(temp1, detail, row);
		}
	}
	
	private void SetLoanToTable(MemJournalStrdDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
		CompanyLoan loan = null;
		try {
			loan = accLogic.getCompanyLoan(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (loan!=null)
			temp1.addElement(loan);				
		else
			temp1.addElement(null);
	}
	
	private void SetProjectToTable(MemJournalStrdDet detail, Vector temp1, ProjectBusinessLogic  proLogic) {
		ProjectData  project = null;
		try {
			project = proLogic.getProjectDataByIndex(detail.getSubsidiAry());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (project!=null)
			temp1.addElement(project);				
		else
			temp1.addElement(null);
	}
	
	private void SetCashAccountToTable(MemJournalStrdDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
		CashAccount cAcc = null;
		try {
			cAcc = accLogic.getCashAccountByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cAcc!=null)
			temp1.addElement(cAcc);				
		else
			temp1.addElement(null);
	}
	
	private void SetBankAccountToTable(MemJournalStrdDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
		BankAccount bAcc = null;
		try {
			bAcc = accLogic.getBankAccountByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (bAcc!=null)
			temp1.addElement(bAcc);
		else
			temp1.addElement(null);
	}
	
	private void SetCustomerToTable(MemJournalStrdDet detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Customer cust = null;
		cust = proLogic.getCustomerByIndex(detail.getSubsidiAry());
		temp1.addElement(cust);
	}
	
	private void SetPartnerToTable(MemJournalStrdDet detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Partner partn = null;
		try {
			partn = proLogic.getPartnerByIndex(detail.getSubsidiAry());
		} catch (Exception e) {				
			e.printStackTrace();
		}
		if (partn!=null)
			temp1.addElement(partn);
		else
			temp1.addElement(null);
	}
	
	private void SetEmployeeToTable(MemJournalStrdDet detail, Vector temp1, HRMBusinessLogic hrmLogic) {
		Employee emp = null;
		try {
			emp = hrmLogic.getEmployeeByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
		} catch (Exception e) {					
			e.printStackTrace();
		}
		if (emp!=null)
			temp1.addElement(emp);
		else
			temp1.addElement(null);
	}
	
	public String getSubsidiaryByindex(Account acc){
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		SubsidiaryAccountSetting accSet = null;
		try{
			accSet = logic.getSubsidiaryAccountSettingByIndex(m_sessionid, IDBConstants.MODUL_MASTER_DATA,acc.getIndex());
		}catch (Exception ex){			
		}
		if (accSet!=null)
			return accSet.getSubsidiaryAccount();
		else
			return "";
	}
	
	private void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.MJ_STANDARD, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			m_defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.MJ_STANDARD, Signature.SIGN_APPROVED);
		if(sign!=null)
			m_defaultApproved = sign.getFullEmployee();		
	}
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity() {		
		validityMsgs.clear();
		if (m_unitCodeComp.getObject()==null)
			addInvalid("Unit code must be selected");		
		if (m_transactionCodeComp.getObject()==null)
			addInvalid("Transaction code must be selected");		
		if (m_descTxtArea.getText().equals(""))
			addInvalid("Description must be selected");
		if (cekdepartmentdetail())
			validityMsgs.add("Department detail is still empty");
		detailAccountOperation();
		MemJournalStrdDet[] temp=entity().getMemJournalStrdDet();		
		if(temp.length==0)
			addInvalid("Account Detail must be added");
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
	
	public boolean cekdepartmentdetail(){
		boolean cek = false;
		int maxRow = m_table.getRowCount()-2;
		for (int i=0;i<maxRow;i++){
			if (m_table.getValueAt(i,8)==null)				
				cek =true;
			else if (m_table.getValueAt(i,8).equals(""))
				cek=true;
		}
		return cek;
	}
	
	private void addInvalid(String string) {
		validityMsgs.add(string);
	}
	
	protected void gui2entity() {		
		entity().setUnit((Unit)m_unitCodeComp.getObject());
		Object objDept = m_departmentComp.getObject();
		if (objDept instanceof Organization) {
			Organization dept = (Organization) objDept;
			entity().setDepartment(dept);			
		}else
			entity().setDepartment(null);
		if (m_singleRb.isSelected())
			entity().setDepartmentgroup(0);
		else if (m_multipleRb.isSelected())
			entity().setDepartmentgroup(1);
		
		entity().setTransactionCode((JournalStandard)m_transactionCodeComp.getObject());
		entity().setTransactionDate(m_transactionDateDate.getDate());		
		entity().transTemplateRead(
				this.m_originatorComp, this.m_approvedComp,
				null, this.m_refNoText,
				this.m_descTxtArea
		);
		detailAccountOperation();
		entity().setRemarks(m_remarksTxtArea.getText());
	}
	
	private void detailAccountOperation() {
		if (m_table.getRowCount()>=0){
			MemJournalStrdDet[] temp=new MemJournalStrdDet[m_table.getDataCount()];
			for (int i=0;i<m_table.getDataCount();i++){
				MemJournalStrdDet det = (MemJournalStrdDet) m_table.getListData().get(i);
				Account acc = det.getAccount();				
				temp[i]=new MemJournalStrdDet();
				temp[i].setAccount(acc);		
				String SubsidiaryAccSet = getSubsidiaryByindex(acc);
				Object value = m_table.getValueAt(i,3);
				if (SubsidiaryAccSet.equals("Employee")){	
					if (value instanceof Employee) {
						Employee emp = (Employee) value;
						temp[i].setSubsidiAry(emp.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Partner"))){
					if (value instanceof Partner) {
						Partner partn = (Partner) value;
						temp[i].setSubsidiAry(partn.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Customer"))){
					if (value instanceof Customer) {
						Customer cust = (Customer) value;
						temp[i].setSubsidiAry(cust.getIndex());
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Cash"))){
					if (value instanceof CashAccount) {
						CashAccount cash = (CashAccount) value;
						temp[i].setSubsidiAry(cash.getIndex());
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Bank"))){
					if (value instanceof BankAccount) {
						BankAccount bank = (BankAccount) value;
						temp[i].setSubsidiAry(bank.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Loan"))){
					if (value instanceof CompanyLoan) {
						CompanyLoan loan = (CompanyLoan) value;
						temp[i].setSubsidiAry(loan.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Project"))){
					if (value instanceof ProjectData) {
						ProjectData project = (ProjectData) value;
						temp[i].setSubsidiAry(project.getIndex());						
					}else{
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
						//temp[i].setSubsidiAry(-1);
					}
				}else
					temp[i].setSubsidiAry(-1);
				temp[i].setBalanceCode(det.getBalanceCode());
				if (det.getBalanceCode().equalsIgnoreCase(Account.STR_DEBET)){
					temp[i].setAccValue(((Double)m_table.getValueAt(i,4)).doubleValue());
				}else if (det.getBalanceCode().equalsIgnoreCase(Account.STR_CREDIT)){
					temp[i].setAccValue(((Double)m_table.getValueAt(i,5)).doubleValue());
				}
				
				if (m_table.getValueAt(i,6) instanceof Currency)
					temp[i].setCurrency((Currency)m_table.getValueAt(i,6));
				else
					temp[i].setCurrency(null);				
				
				temp[i].setExchangeRate(((Double)m_table.getValueAt(i,7)).doubleValue());
				
				Object objdept = m_table.getValueAt(i,8);
				if (objdept instanceof Organization) {
					Organization org = (Organization) objdept;
					temp[i].setDepartment(org);
				}else{
					temp[i].setDepartment(null);
				}
			}
			entity().setMemJournalStrdDet(temp);
		}
	}
	
	protected void entity2gui() {		
		m_unitCodeComp.setObject(entity().getUnit());
		m_transactionCodeComp.setObject(entity().getTransactionCode());
		m_departmentComp.setObject(entity().getDepartment());
		m_transactionCodeComp.setObject(entity().getTransactionCode());		
		m_refNoText.setText(entity().getReferenceNo());
		if(entity().getTransactionDate()!=null)
			m_transactionDateDate.setDate(entity().getTransactionDate());
		else{
			m_transactionDateDate.setDate(new Date());
		}		
		m_descTxtArea.setText(entity().getDescription());
		m_originatorComp.setEmployee(entity().getEmpOriginator());
		m_originatorComp.setDate(entity().getDateOriginator());
		m_originatorComp.setJobTitle(entity().getJobTitleOriginator());
		m_approvedComp.setEmployee(entity().getEmpApproved());
		m_approvedComp.setDate(entity().getDateApproved());    	
		m_approvedComp.setJobTitle(entity().getJobTitleApproved());    
		m_statusText.setText(entity().statusInString());
		
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			m_submittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			m_submittedDateText.setText("");	
		LoadDetail(entity().getIndex());
		m_remarksTxtArea.setText(entity().getRemarks());
	}
	
	protected void disableEditMode() {
		m_statusText.setEditable(false);
		//m_statusText.setBackground(#0);
		m_submittedDateText.setEditable(false);
		m_refNoText.setEditable(false);
		m_transactionCodeComp.setEnabled(false);
		m_transactionDateDate.setEditable(false);		
		m_descTxtArea.setEditable(false);
		m_unitCodeComp.setEnabled(false);
		m_departmentComp.setEnabled(false);
		m_originatorComp.setEnabled(false);
		m_approvedComp.setEnabled(false);
		m_singleRb.setEnabled(false);
		m_multipleRb.setEnabled(false);
		setEnableButtonBawah(false);	
	}
	
	protected void enableEditMode() {
		m_transactionCodeComp.setEnabled(true);
		m_transactionDateDate.setEditable(true);
		m_descTxtArea.setEditable(true);
		m_unitCodeComp.setEnabled(true);
		m_departmentComp.setEnabled(true);
		m_originatorComp.setEnabled(true);
		m_approvedComp.setEnabled(true);
		m_singleRb.setEnabled(true);
		m_multipleRb.setEnabled(true);
		setEnableButtonBawah(true);		
	}
	
	protected Object createNew() {
		MemJournalStrd a  = new MemJournalStrd();
		a.setEmpOriginator(m_defaultOriginator);
		a.setEmpApproved(m_defaultApproved);		
		return a;
	}
	
	void setEntity(Object m_entity) {		
		MemJournalStrd oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (MemJournalStrd)m_entity;		
		this.m_entity.addPropertyChangeListener(this);
		
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}		
		if (evt.getSource() == m_departmentComp){
			if (m_singleRb.isSelected()){
				int maxRow = m_table.getRowCount()-2;
				for (int i=0;i<maxRow;i++){
					if (m_departmentComp.getObject() instanceof Organization) {
						Organization org = (Organization) m_departmentComp.getObject();
						m_table.setValueAt(org,i,8);
					}
				}
			}
		}
		if (evt.getSource() ==m_transactionCodeComp){
			m_table.clearTable();
		}
	}
	
	private void isiDefaultAssignPanel(){
		m_originatorComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultOriginator));
		m_approvedComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultApproved));		
	}
	
	MemJournalStrd entity() {
		return m_entity;	
	}
	
	protected void doSave() {
		if (!cekValidity()) return;
		if (!m_table.cekDebitCredit()) {
			JOptionPane.showMessageDialog(this, " Transaction is unbalanced. Please check accounts",
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		super.doSave();		
		setEnableButtonBawah(false);
		AccountingSQLSAP sql=new AccountingSQLSAP();
		GenericMapper mapper2=MasterMap.obtainMapperFor(MemJournalStrdDet.class);
		mapper2.setActiveConn(m_conn);
		try {
			if ((new Long(entity().getIndex()).toString().equalsIgnoreCase("0") )){	
				long index = sql.getMaxIndex(IDBConstants.TABLE_MEMORIAL_JOURNAL_STANDARD, m_conn);
				entity().setIndex(index);			
			}
			if (entity().getMemJournalStrdDet()!=null){
				MemJournalStrdDet temp[]=entity().getMemJournalStrdDet();
				mapper2.doDeleteByColumn(IDBConstants.ATTR_MEMORIAL_JOURNAL_STANDARD, new Long(entity().getIndex()).toString());
				for (int i=0;i<temp.length;i++){
					temp[i].setMemJournalStrd((MemJournalStrd)entity());
					mapper2.doInsert(temp[i]);  
				}	
			}
			entity2gui();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected class MyTableModelListener implements TableModelListener{
		
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			Object obj = e.getSource();
			if(obj instanceof DefaultTableModel){
				DefaultTableModel model = (DefaultTableModel) obj;
				int maxRow = model.getRowCount();
				if(((row>-1)&&(row<(maxRow-1)))&& ((col==4)||(col==5) ||(col==6)|| (col == 7))){				
					m_table.updateSummary();
				}
			}
		}
		
	}	
	
	class MemJournalStrdTable extends JTable {
		private static final long serialVersionUID = 1L;
		buttomTabbleRcvOthersModel model = new buttomTabbleRcvOthersModel();
		ArrayList listData=new ArrayList();
		protected MemJournalStrdTable() {
			model.addColumn("No");
			model.addColumn("Account");
			model.addColumn("Account Name");
			model.addColumn("Subsidiary");
			model.addColumn("Debit");
			model.addColumn("Credit");
			model.addColumn("Curr");
			model.addColumn("Exch Rate");
			model.addColumn("Department");
			setModel(model);    			
			getColumnModel().getColumn(0).setPreferredWidth(40);
			getColumnModel().getColumn(0).setMaxWidth(40);
			clearTable();			
			getModel().addTableModelListener(new MyTableModelListener());
		}
		
		public void updateSummary() {			
			int maxRow = getRowCount()-1;
			int maxTotalRow = maxRow - 2;			
			double debit = 0;			
			double totDebit = 0;			
			double credit = 0;	
			double totCredit = 0;			
			double excRate = 0;
			for(int i=0; i<=maxTotalRow; i++){
				debit = ((Double)getValueAt(i,4)).doubleValue();
				credit = ((Double)getValueAt(i, 5)).doubleValue();
				excRate = ((Double)getValueAt(i,7)).doubleValue();				
				Object currObj = getValueAt(i,6);	
				if (currObj instanceof Currency){					
					Currency curr = (Currency)	currObj;				
					if (curr.getSymbol().equals("$")){						
						totDebit += (debit*excRate);
						totCredit += (credit*excRate);						
					}else{							
						totDebit += debit;
						totCredit += credit;						
					}
				}else{
					totDebit += debit;
					totCredit += credit;
				}
			}			
			setValueAt(new Double(totCredit), maxRow, 5);						
			setValueAt(new Double(totDebit), maxRow, 4);			
		}
		
		
		public void clearTable() {				
			listData.clear();
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
			model.addRow(new Object[]{null, null, null, null, null, null,null});
			model.addRow(new Object[]{null, null, "TOTAL", null,new Double(0),new Double(0),baseCurrency,null,null});			
		}		
		
		
		public void addData(Vector obj, MemJournalStrdDet det,int insertRow){			
			listData.add(det);
			model.insertRow(insertRow,obj);
			updateSummary();
			updateNumbering();
		}
		
		public void removeData(int row) {
			listData.remove(row);
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);	
			updateNumbering();
			updateSummary();
		}
		
		public TableCellEditor getCellEditor(int row, int col) {
			String currSymbol = "";
			Currency curr = null;
			if (getValueAt(row,6) instanceof Currency) {
				curr = (Currency) getValueAt(row,6);
				currSymbol = curr.getSymbol();
			}		
			
			if (row <= getRowCount()-3){
				MemJournalStrdDet det = (MemJournalStrdDet) listData.get(row);
				Account acc = det.getAccount();				
				if (col ==3){						
					String SubsidiaryAccSet = getSubsidiaryByindex(acc);					
					if (SubsidiaryAccSet != null) {
						if (!SubsidiaryAccSet.equals("")) {
							
							if (SubsidiaryAccSet.equals("Employee"))
								return new EmployeeCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
										.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Partner"))
								return new PartnerCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
										.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Customer"))
								return new CustomerCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
										.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Cash"))
								return new CashCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
										.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Bank"))
								return new BankCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
										.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Loan"))
								return new LoanCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
										.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Project"))
								return new ProjectDataCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
										.getMainFrame(), m_conn,
										m_sessionid);
						}
					} else
						return null;
				}
				else if (col == 4 && det.getBalanceCode().equalsIgnoreCase(Account.STR_DEBET))					
					//return new DoubleCellEditor();
					return new FormattedDoubleCellEditor(JLabel.RIGHT);
				else if (col==5 && det.getBalanceCode().equalsIgnoreCase(Account.STR_CREDIT))
					//return new DoubleCellEditor();
					return new FormattedDoubleCellEditor(JLabel.RIGHT);
				else if (col==6){
					return new CurrencyCellEditor(GumundaMainFrame.getMainFrame(),"Currency",m_conn,m_sessionid);
				}else if (col==7 && (!currSymbol.equals(baseCurrency.getSymbol()) && !currSymbol.equals("")))
					//return new DoubleCellEditor();
					return new FormattedDoubleCellEditor(JLabel.RIGHT);
				else if(col==8 && m_multipleRb.isSelected())
					return new OrganizationCellEditor(GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
			}
			return null;
		}
		
		public TableCellRenderer getCellRenderer(int row, int column) {
			if(column==0)
				return new BaseTableCellRenderer();
			if(column>0){
				int maxRow =getRowCount()-1;
				
				if(row>=(maxRow-1) &&(row<=maxRow)){
					if(column==2)
						return new FormattedStandardCellRenderer(Font.BOLD,JLabel.RIGHT);
					else if(column==4 || column==5 || column==7)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
					else if (column == 6)
						return new FormattedStandardCellRenderer(Font.BOLD, JLabel.CENTER);
				}else{			
					MemJournalStrdDet det = (MemJournalStrdDet) listData.get(row);
					if (column==4){
						if (det.getBalanceCode().equalsIgnoreCase(Account.STR_DEBET))
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					}						
					else if (column==5){
						if (det.getBalanceCode().equalsIgnoreCase(Account.STR_CREDIT))
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
						else 
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					}						
					else if(column==7)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);					
				}		
			}
			return new FormattedStandardCellRenderer(Font.PLAIN, JLabel.CENTER);
		}
		
		private void updateNumbering() {
			for(int i=0; i<getRowCount()-2; i++){
				setValueAt(new Integer(i+1), i, 0);							
				
			}
		}
		
		public boolean cekDebitCredit(){
			int maxRow = getRowCount()-1;			
			if (getValueAt(maxRow,4).toString().equals(getValueAt(maxRow,5).toString()))
				return true;
			return false;
		}
		
		public int getDataCount(){
			return listData.size();
		}
		
		public ArrayList getListData(){
			return listData;
		}
		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}
	}
	
	/*protected class FormattedDoubleCellRenderer extends DoubleCellRenderer {
	 private static final long serialVersionUID = 1L;
	 private int fontStyle = Font.PLAIN;
	 private Color color = Color.BLACK;		
	 public FormattedDoubleCellRenderer(int alignment, int fontStyle) {
	 super(alignment);
	 this.fontStyle = fontStyle;
	 }		
	 public FormattedDoubleCellRenderer(int alignment, int fontStyle, Color color) {
	 super(alignment);
	 this.fontStyle = fontStyle;
	 this.color = color;
	 }		
	 public Component getTableCellRendererComponent(JTable table,
	 Object value, boolean isSelected, boolean hasFocus, int row,
	 int column) {
	 super.getTableCellRendererComponent(table, value, false,
	 hasFocus, row, column);			
	 Font font = getFont();
	 setForeground(this.color);
	 setFont(font.deriveFont(this.fontStyle));
	 
	 return this;
	 }
	 }
	 
	 protected class StandardFormatCellRenderer extends DefaultTableCellRenderer {
	 private static final long serialVersionUID = 1L;
	 private int fontStyle = Font.PLAIN;		
	 private int horizontalAlignment = JLabel.LEFT;		
	 private Color fontColor = Color.BLACK;	
	 private Color backColor = Color.WHITE;		
	 public StandardFormatCellRenderer(int fontStyle, int horizontalAlignment) {
	 this.fontStyle = fontStyle;
	 this.horizontalAlignment = horizontalAlignment;
	 }
	 
	 public StandardFormatCellRenderer(int fontStyle,
	 int horizontalAlignment, Color fontColor) {
	 this.fontStyle = fontStyle;
	 this.horizontalAlignment = horizontalAlignment;
	 this.fontColor = fontColor;
	 }
	 
	 public StandardFormatCellRenderer(int fontStyle,
	 int horizontalAlignment, Color fontColor, Color backColor) {
	 this.fontStyle = fontStyle;
	 this.horizontalAlignment = horizontalAlignment;
	 this.fontColor = fontColor;
	 this.backColor = backColor;
	 }
	 
	 public Component getTableCellRendererComponent(JTable table,
	 Object value, boolean isSelected, boolean hasFocus, int row,
	 int column) {
	 setText((value == null) ? "" : value.toString());
	 setHorizontalAlignment(this.horizontalAlignment);
	 setBackground(this.backColor);
	 
	 Font font = getFont();
	 setForeground(this.fontColor);
	 setFont(font.deriveFont(this.fontStyle));
	 return this;
	 }
	 }*/
	
	class buttomTabbleRcvOthersModel extends DefaultTableModel{		
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {			
			int maxRow = getRowCount();			
			if( (col == 0 || col==1 || col==2 ) || row > (maxRow-3))
				return false;
			return true;
		}
		
		public void setValueAt(Object aValue, int row, int column) {
			if (column==6)
				if (aValue instanceof Currency){
					Currency curr = (Currency) aValue;
					if (curr.getIsBase()){
						setValueAt(new Double(1.0),row,7);
					}
					super.setValueAt(new Currency(curr,Currency.SYMBOL),row,column);
					return;
				}
			super.setValueAt(aValue, row, column);
		}
	}
	
	protected void tableStopCellEditing() {
		m_table.stopCellEditing();
	}
}
