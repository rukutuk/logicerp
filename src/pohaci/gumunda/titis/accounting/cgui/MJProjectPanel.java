package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.MJ_NSProject;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.*;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;


public class MJProjectPanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener  {
	private static final long serialVersionUID = 1L;
	private MemJournalNonStrd m_entity;
	Employee m_defaultOriginator,m_defaultApproved;
	private JButton m_addMJNonStndrdProjAccntBtn,m_deleteMJNonStndrdProjAccntBtn;
	private AssignPanel m_approvedComp,m_originatorComp;	
	private JLabel m_descriptionLbl,m_transactionCodeLbl,m_memorialJournalNoLbl,m_statusLbl,
	m_submittedDateLbl,m_transactionDateLbl;
	private JScrollPane m_descriptionScroll;
	private JTextArea m_descriptionTextArea;
	private LookupJournalTypePicker m_journaTypeCombo;
	private JScrollPane m_MJNonStndrdProjAccnScrollPane;	
	private JTextField m_statusText,m_submittedDateText,m_refNoText;
	private pohaci.gumunda.titis.application.DatePicker m_transactionDateDate;
	private JPanel m_topButtonPanel,m_buttomButtonPanel,m_jPanel1,m_jPanel1_1,m_jPanel1_2,
	m_jPanel1_2_1,m_jPanel1_3,m_jPanel1_3_2,m_jPanel2,m_jPanel2_1;
	private MasterProjectDataPanel m_projectPanel; 
	private MemJournalNonStrdTableProject m_table;	
	
	public MJProjectPanel(Connection conn, long sessionid) {
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
		setEntity(new MemJournalNonStrd());
		m_entityMapper = MasterMap.obtainMapperFor(MemJournalNonStrd.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	private void initComponents() {
		m_journaTypeCombo = new LookupJournalTypePicker(m_conn, m_sessionid);
		m_originatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		m_approvedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));		
		GridBagConstraints gridBagConstraints;		
		m_jPanel1 = new JPanel();
		m_jPanel1_1 = new JPanel();
		m_topButtonPanel = new JPanel();
		m_newBtn = new JButton();
		m_editBtn = new JButton();
		m_saveBtn = new JButton();
		m_deleteBtn = new JButton();
		m_cancelBtn = new JButton();
		m_submitBtn = new JButton();
		m_jPanel1_2 = new JPanel();
		m_jPanel1_2_1 = new JPanel();
		m_statusLbl = new JLabel();
		m_submittedDateLbl = new JLabel();
		m_transactionCodeLbl = new JLabel();
		m_transactionDateLbl = new JLabel();
		m_memorialJournalNoLbl = new JLabel();
		m_descriptionLbl = new JLabel();
		m_statusText = new JTextField();
		m_submittedDateText = new JTextField();
		m_refNoText = new JTextField();
		m_descriptionScroll = new JScrollPane();
		m_descriptionTextArea = new JTextArea();
		m_transactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		m_projectPanel = new MasterProjectDataPanel(110,250,m_conn,m_sessionid);        
		m_jPanel1_3 = new JPanel();
		m_jPanel1_3_2 = new JPanel();
		m_jPanel2 = new JPanel();
		m_jPanel2_1 = new JPanel();
		m_buttomButtonPanel = new JPanel();
		m_addMJNonStndrdProjAccntBtn = new JButton();
		m_deleteMJNonStndrdProjAccntBtn = new JButton();
		m_MJNonStndrdProjAccnScrollPane = new JScrollPane();
		m_table = new MemJournalNonStrdTableProject();	
		
		setLayout(new BorderLayout());
		
		m_jPanel1.setLayout(new BorderLayout());
		
		m_jPanel1.setPreferredSize(new Dimension(650, 400));
		m_jPanel1_1.setLayout(new BorderLayout());
		
		m_topButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		
		m_topButtonPanel.setPreferredSize(new Dimension(650, 35));
		m_searchRefNoBtn.setPreferredSize(new Dimension(21, 21));
		m_topButtonPanel.add(m_searchRefNoBtn);
		
		m_printViewRefNoBtn.setPreferredSize(new Dimension(21, 21));
		m_topButtonPanel.add(m_printViewRefNoBtn);
		
		m_newBtn.setText("New");
		m_newBtn.setMargin(new Insets(2, 2, 2, 2));
		m_newBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_newBtn);
		
		m_editBtn.setText("Edit");
		m_editBtn.setMargin(new Insets(2, 2, 2, 2));
		m_editBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_editBtn);
		
		m_deleteBtn.setText("Delete");
		m_deleteBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deleteBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_deleteBtn);
		
		m_saveBtn.setText("Save");
		m_saveBtn.setMargin(new Insets(2, 2, 2, 2));
		m_saveBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_saveBtn);       
		
		m_cancelBtn.setText("Cancel");
		m_cancelBtn.setMargin(new Insets(2, 2, 2, 2));
		m_cancelBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_cancelBtn);
		
		m_submitBtn.setText("Submit");
		m_submitBtn.setMargin(new Insets(2, 2, 2, 2));
		m_submitBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_submitBtn);
		
		m_jPanel1_1.add(m_topButtonPanel, BorderLayout.WEST);
		
		m_jPanel1.add(m_jPanel1_1, BorderLayout.NORTH);
		
		m_jPanel1_2.setLayout(new BorderLayout());
		
		m_jPanel1_2.setPreferredSize(new Dimension(950, 320));
		m_jPanel1_2_1.setLayout(new GridBagLayout());
		
		m_jPanel1_2_1.setPreferredSize(new Dimension(800, 200));
		m_statusLbl.setText("Status");
		m_statusLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 5, 1, 1);
		m_jPanel1_2_1.add(m_statusLbl, gridBagConstraints);
		
		m_submittedDateLbl.setText("Submitted Date");
		m_submittedDateLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 5, 1, 1);
		m_jPanel1_2_1.add(m_submittedDateLbl, gridBagConstraints);
		
		m_transactionCodeLbl.setText("Journal Type");
		m_transactionCodeLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 5, 1, 1);
		m_jPanel1_2_1.add(m_transactionCodeLbl, gridBagConstraints);
		
		m_transactionDateLbl.setText("Transaction Date*");
		m_transactionDateLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 5, 1, 1);
		m_jPanel1_2_1.add(m_transactionDateLbl, gridBagConstraints);
		
		m_memorialJournalNoLbl.setText("Memorial Journal No");
		m_memorialJournalNoLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 5, 1, 1);
		m_jPanel1_2_1.add(m_memorialJournalNoLbl, gridBagConstraints);
		
		m_descriptionLbl.setText("Description*");
		m_descriptionLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 5, 1, 1);
		m_jPanel1_2_1.add(m_descriptionLbl, gridBagConstraints);
		
		m_statusText.setPreferredSize(new Dimension(250, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_statusText, gridBagConstraints);
		
		m_submittedDateText.setPreferredSize(new Dimension(250, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_submittedDateText, gridBagConstraints);
		
		m_refNoText.setPreferredSize(new Dimension(250, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_refNoText, gridBagConstraints);
		
		m_descriptionScroll.setPreferredSize(new Dimension(250, 120));
		m_descriptionTextArea.setColumns(20);
		m_descriptionTextArea.setLineWrap(true);
		m_descriptionTextArea.setRows(5);
		m_descriptionScroll.setViewportView(m_descriptionTextArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_descriptionScroll, gridBagConstraints);
		
		m_transactionDateDate.setPreferredSize(new Dimension(250, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_jPanel1_2_1.add(m_transactionDateDate, gridBagConstraints);
		
		m_projectPanel.setPreferredSize(new Dimension(400, 225));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		//gridBagConstraints.gridwidth=2;
		gridBagConstraints.gridheight = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 1, 1, 0);
		m_jPanel1_2_1.add(m_projectPanel, gridBagConstraints);
		
		m_journaTypeCombo.setPreferredSize(new Dimension(250, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_journaTypeCombo, gridBagConstraints);
		
		m_jPanel1_2.add(m_jPanel1_2_1, BorderLayout.WEST);
		
		m_jPanel1.add(m_jPanel1_2, BorderLayout.CENTER);
		
		m_jPanel1_3.setLayout(new BorderLayout());
		
		m_jPanel1_3.setPreferredSize(new Dimension(650, 110));
		m_originatorComp.setLayout(new GridBagLayout());
		
		m_originatorComp.setBorder(BorderFactory.createTitledBorder("Originator"));
		m_originatorComp.setOpaque(false);
		m_originatorComp.setPreferredSize(new Dimension(280, 110));
		m_jPanel1_3.add(m_originatorComp, BorderLayout.WEST);
		
		m_jPanel1_3_2.setLayout(new BorderLayout());
		
		m_jPanel1_3_2.setPreferredSize(new Dimension(430, 110));
		m_approvedComp.setLayout(new GridBagLayout());
		
		m_approvedComp.setBorder(BorderFactory.createTitledBorder("Approved by"));
		m_approvedComp.setOpaque(false);
		m_approvedComp.setPreferredSize(new Dimension(280, 110));
		m_jPanel1_3_2.add(m_approvedComp, BorderLayout.WEST);
		
		m_jPanel1_3.add(m_jPanel1_3_2, BorderLayout.CENTER);
		
		m_jPanel1.add(m_jPanel1_3, BorderLayout.SOUTH);
		
		add(m_jPanel1, BorderLayout.NORTH);
		
		m_jPanel2.setLayout(new BorderLayout());
		
		m_jPanel2_1.setLayout(new BorderLayout());
		
		m_buttomButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		
		m_buttomButtonPanel.setPreferredSize(new Dimension(650, 35));
		m_addMJNonStndrdProjAccntBtn.setText("Add");
		m_addMJNonStndrdProjAccntBtn.setMargin(new Insets(2, 2, 2, 2));
		m_addMJNonStndrdProjAccntBtn.setPreferredSize(new Dimension(50, 21));
		m_buttomButtonPanel.add(m_addMJNonStndrdProjAccntBtn);
		
		m_deleteMJNonStndrdProjAccntBtn.setText("Delete");
		m_deleteMJNonStndrdProjAccntBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deleteMJNonStndrdProjAccntBtn.setPreferredSize(new Dimension(50, 21));
		m_buttomButtonPanel.add(m_deleteMJNonStndrdProjAccntBtn);
		
		m_jPanel2_1.add(m_buttomButtonPanel, BorderLayout.WEST);
		
		m_jPanel2.add(m_jPanel2_1, BorderLayout.NORTH);
		
		m_MJNonStndrdProjAccnScrollPane.setPreferredSize(new Dimension(650, 215));        
		m_MJNonStndrdProjAccnScrollPane.setViewportView(m_table);
		
		m_jPanel2.add(m_MJNonStndrdProjAccnScrollPane, BorderLayout.CENTER);
		
		add(m_jPanel2, BorderLayout.CENTER);
	}
	
	private void enableAwal(){ 	
		setenableEditPanel(m_jPanel1_2_1,false);		
		setEnableButtonBawah(false);
	}
	
	private void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.MJ_NONSTANDARD_PROJECT, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			m_defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.MJ_NONSTANDARD_PROJECT, Signature.SIGN_APPROVED);
		if(sign!=null)
			m_defaultApproved = sign.getFullEmployee();		
	}
	
	public void setEnableButtonBawah(boolean bool){
		m_addMJNonStndrdProjAccntBtn.setEnabled(bool);
		m_deleteMJNonStndrdProjAccntBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}
	
	protected void clearForm() {		
		m_table.clearTable();
	}
	
	private void isiDefaultAssignPanel(){
		m_originatorComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultOriginator));
		m_approvedComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultApproved));		
	}
	
	private void addingListener(){
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		m_projectPanel.ProjectCodeComp.addPropertyChangeListener("object",this);
		m_addMJNonStndrdProjAccntBtn.addActionListener(this);
		m_deleteMJNonStndrdProjAccntBtn.addActionListener(this);
	}  
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new MJ_NSProject(m_entity,m_conn,baseCurrency);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			SearchMemorialJournalDialog dlg = new SearchMemorialJournalDialog(GumundaMainFrame.getMainFrame(), 
					"Search Memorial Journal","Non Standard Journal Project", m_conn, m_sessionid,
					new MemJournalStrdLoader(m_conn,MemJournalNonStrd.class),"1");
			dlg.setVisible(true);
			if (dlg.selectedObj != null){    			
				doLoad(dlg.selectedObj);				
			}
		}
		else if (e.getSource() == m_addMJNonStndrdProjAccntBtn){
			AccountTreeDlg dlg = new AccountTreeDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
					"Account", m_conn, m_sessionid);
			dlg.setVisible(true);
			if (dlg.getResponse()==JOptionPane.OK_OPTION){
				Account acc = dlg.getAccount();
				Vector temp1=new Vector();
				int row = m_table.getRowCount()-2;
				temp1.addElement(null);
				temp1.addElement(acc.getCode());
				temp1.addElement(acc.getName());
				temp1.addElement(null);
				temp1.addElement(new Double(0));
				temp1.addElement(new Double(0));
				temp1.addElement(baseCurrency);
				temp1.addElement(new Double(1));	
				m_table.addData(temp1, acc,row);	
			}
		}
		else if (e.getSource() == m_deleteMJNonStndrdProjAccntBtn){
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
		setEnableButtonBawah(true);
	}
	
	protected void doNew() {
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
		setEnableButtonBawah(true);
	}
	
	protected void deleteChilds() {
		DeleteAnaknya(m_entity);
		super.deleteChilds();
	}
	
	private void DeleteAnaknya(MemJournalNonStrd old){
		GenericMapper mapper2=MasterMap.obtainMapperFor(MemJournalNonStrdDet.class);
		mapper2.setActiveConn(m_conn);   
		mapper2.doDeleteByColumn(IDBConstants.ATTR_MEMORIAL_JOURNAL_NONSTANDARD,new Long(old.getIndex()).toString());
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
	
	MemJournalNonStrd entity() {
		return m_entity;
	}
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity() {
		validityMsgs.clear();
		if (m_transactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");		
		if (m_descriptionTextArea.getText().equals(""))
			addInvalid("Description must be selected");		
		if (m_projectPanel.ProjectCodeComp.getObject()==null)	
			addInvalid("Project code must be selected");		
		detailAccountOperation();
		MemJournalNonStrdDet[] temp=entity().getMemJournalNonStrdDets();		
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
	
	private void addInvalid(String string) {
		validityMsgs.add(string);
	}
	
	protected void gui2entity() {		
		entity().setTransactionDate(m_transactionDateDate.getDate());
		entity().setJournal((Journal)m_journaTypeCombo.getObject());
		entity().setProject((ProjectData)m_projectPanel.ProjectCodeComp.getObject());
		entity().setUnit((Unit)m_projectPanel.UnitCodeComp.getObject());		
		entity().setDepartment((Organization)m_projectPanel.DepartmentComp.getObject());
		entity().setDepartmentgroup(0);
		entity().transTemplateRead(
				this.m_originatorComp, this.m_approvedComp,
				null, this.m_refNoText,
				this.m_descriptionTextArea
		);
		entity().setSubmitType("1");
		detailAccountOperation();
		
	}
	
	private void detailAccountOperation() {
		if (m_table.getRowCount()>=0){
			MemJournalNonStrdDet[] temp=new MemJournalNonStrdDet[m_table.getDataCount()];
			for (int i=0;i<m_table.getDataCount();i++){
				Account acc = (Account)m_table.getListData().get(i);
				temp[i]=new MemJournalNonStrdDet();
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
				
				double debit =((Double)m_table.getValueAt(i,4)).doubleValue();
				double credit = ((Double)m_table.getValueAt(i,5)).doubleValue();
				
				if (debit> 0){
					temp[i].setAccValue(debit);						
					temp[i].setBalanceCode(Account.STR_DEBET);
				}else{
					temp[i].setAccValue(credit);
					temp[i].setBalanceCode(Account.STR_CREDIT);						
				}
				
				if (m_table.getValueAt(i,6) instanceof Currency )
					temp[i].setCurrency((Currency)m_table.getValueAt(i,6));
				else
					temp[i].setCurrency(null);				
				
				temp[i].setExchangeRate(((Double)m_table.getValueAt(i,7)).doubleValue());
			}
			entity().setMemJournalNonStrdDet(temp);
		}
	}
	
	protected void entity2gui() {
		
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");		
		if(entity().getTransactionDate()!=null)
			m_transactionDateDate.setDate(entity().getTransactionDate());
		else{
			m_transactionDateDate.setDate(new Date());
		}    	
		m_journaTypeCombo.setObject(entity().getJournal());
		m_descriptionTextArea.setText(entity().getDescription());
		
		m_projectPanel.ProjectCodeComp.setObject(entity().getProject());		
		if (entity().getProject()==null){    		    		
			m_projectPanel.CustomerText.setText("");
			m_projectPanel.WorkDescTextArea.setText("");
			m_projectPanel.UnitCodeComp.setObject("");
			m_projectPanel.DepartmentComp.setObject("");    		
		}else{
			m_projectPanel.DepartmentComp.setObject("");
			if (entity().getDepartment()!=null)
				m_projectPanel.DepartmentComp.setObject(entity().getDepartment());			
			m_projectPanel.UnitCodeComp.setObject("");
			if (entity().getUnit()!=null)
				m_projectPanel.UnitCodeComp.setObject(entity().getUnit());
			m_projectPanel.ActivityCodeText.setText(entity().getProject().getActivity().toString());
		}
		m_originatorComp.setEmployee(entity().getEmpOriginator());
		m_originatorComp.setJobTitle(entity().getJobTitleOriginator());
		m_originatorComp.setDate(entity().getDateOriginator());		
		m_approvedComp.setEmployee(entity().getEmpApproved());
		m_approvedComp.setJobTitle(entity().getJobTitleApproved());
		m_approvedComp.setDate(entity().getDateApproved());		
		m_statusText.setText(entity().statusInString());  
		m_refNoText.setText(entity().getReferenceNo());		
		if(entity().getSubmitDate()!=null)
			m_submittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			m_submittedDateText.setText("");
		LoadDetail(entity().getIndex());
	}
	
	protected void disableEditMode() {
		m_statusText.setEditable(false);
		m_submittedDateText.setEditable(false);    	
		m_refNoText.setEditable(false);
		m_journaTypeCombo.setEnabled(false);
		m_transactionDateDate.setEditable(false);
		m_descriptionTextArea.setEditable(false);
		m_originatorComp.setEnabled(false);
		m_approvedComp.setEnabled(false);
		m_projectPanel.ProjectCodeComp.setEnabled(false);		
		m_projectPanel.CustomerText.setEditable(false);
		m_projectPanel.WorkDescScroll.setEnabled(false);
		m_projectPanel.WorkDescTextArea.setEditable(false);
		m_projectPanel.UnitCodeComp.setEnabled(false);
		m_projectPanel.ActivityCodeText.setEditable(false);		
		m_projectPanel.DepartmentComp.setEnabled(false);
		m_projectPanel.ORNoText.setEditable(false);
		m_projectPanel.ContractNoText.setEditable(false);
		m_projectPanel.IPCNoText.setEditable(false);
	}
	
	protected void enableEditMode() {
		m_journaTypeCombo.setEnabled(true);
		m_transactionDateDate.setEditable(true);
		m_descriptionTextArea.setEditable(true);
		m_originatorComp.setEnabled(true);
		m_approvedComp.setEnabled(true);
		m_projectPanel.ProjectCodeComp.setEnabled(true);
		m_projectPanel.CustomerText.setEditable(true);
		m_projectPanel.WorkDescScroll.setEnabled(true);
		m_projectPanel.WorkDescTextArea.setEditable(true);
		m_projectPanel.UnitCodeComp.setEnabled(true);
		m_projectPanel.DepartmentComp.setEnabled(true);
	}
	
	protected Object createNew() {
		MemJournalNonStrd a  = new MemJournalNonStrd();
		a.setEmpOriginator(m_defaultOriginator);
		a.setEmpApproved(m_defaultApproved);		
		return a ;
	}
	
	void setEntity(Object m_entity) {
		MemJournalNonStrd oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (MemJournalNonStrd)m_entity;
		this.m_entity.addPropertyChangeListener(this);
		
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		if (evt.getSource() == m_projectPanel.ProjectCodeComp){
			System.err.println("masuk sini");
			if (evt.getNewValue() == null){
				m_projectPanel.CustomerText.setText("");
				m_projectPanel.WorkDescTextArea.setText("");
				m_projectPanel.UnitCodeComp.setObject(null);
				m_projectPanel.DepartmentComp.setObject(null);
				m_projectPanel.ActivityCodeText.setText("");
				m_projectPanel.ORNoText.setText("");
				m_projectPanel.ContractNoText.setText("");
				m_projectPanel.IPCNoText.setText("");
			}else{
				ProjectData project = (ProjectData)m_projectPanel.ProjectCodeComp.getObject();
				m_projectPanel.CustomerText.setText(project.getCustname());
				m_projectPanel.WorkDescTextArea.setText(project.getWorkDescription());
				m_projectPanel.UnitCodeComp.setObject(project.getUnit());
				m_projectPanel.DepartmentComp.setObject(project.getDepartment());
				m_projectPanel.ActivityCodeText.setText(project.getAct());
				m_projectPanel.ORNoText.setText(project.getORNo());
				m_projectPanel.ContractNoText.setText(project.getPONo());
				m_projectPanel.IPCNoText.setText(project.getIPCNo());
				
			}
		}
		
	}
	
	public void LoadDetail(long index){
		GenericMapper mapper2=MasterMap.obtainMapperFor(MemJournalNonStrdDet.class);
		mapper2.setActiveConn(m_conn); 
		String value = new Long(index).toString();
		List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_MEMORIAL_JOURNAL_NONSTANDARD+"="+value);
		m_table.clearTable();
		MemJournalNonStrdDet detail;
		Vector temp1;	
		Account acc;
		for(int i=0;i<detailList.size();i++){
			int row = m_table.getRowCount()-2;
			temp1=new Vector();
			detail=(MemJournalNonStrdDet)detailList.get(i);
			acc=(Account)detail.getAccount();
			temp1.addElement(null);
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
				SetLoanAccountToTable(detail, temp1, accLogic);
			}else if (SubsidiaryAccSet.equals("Project")){
				SetProjectToTable(detail, temp1, proLogic );
			}else{
				temp1.addElement("");
			}			
			if (detail.getBalanceCode().equalsIgnoreCase(Account.STR_DEBET)){
				temp1.addElement(new Double(detail.getAccValue()));
				temp1.addElement(new Double(0));
			}else if (detail.getBalanceCode().equalsIgnoreCase(Account.STR_CREDIT)){
				temp1.addElement(new Double(0));
				temp1.addElement(new Double(detail.getAccValue()));
			}
			temp1.addElement(detail.getCurrency());
			temp1.addElement(new Double(detail.getExchangeRate()));
			m_table.addData(temp1, acc,row);
		}
	}
	
	private void SetLoanAccountToTable(MemJournalNonStrdDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetProjectToTable(MemJournalNonStrdDet detail, Vector temp1, ProjectBusinessLogic  proLogic) {
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
	private void SetCashAccountToTable(MemJournalNonStrdDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetBankAccountToTable(MemJournalNonStrdDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCustomerToTable(MemJournalNonStrdDet detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Customer cust = null;
		cust = proLogic.getCustomerByIndex(detail.getSubsidiAry());
		temp1.addElement(cust);
	}
	
	private void SetPartnerToTable(MemJournalNonStrdDet detail, Vector temp1, ProjectBusinessLogic proLogic) {
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
	
	private void SetEmployeeToTable(MemJournalNonStrdDet detail, Vector temp1, HRMBusinessLogic hrmLogic) {
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
	
	protected void doSave() {
		if (!cekValidity()) return;
		if (m_table.cekDebitCredit()){
			JOptionPane.showMessageDialog(this, "Must debit value only or credit only",
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!m_table.cekTotalDebitCredit()) {
			JOptionPane.showMessageDialog(this, " Transaction is unbalanced. Please check accounts",
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		super.doSave();
		AccountingSQLSAP sql = new AccountingSQLSAP();		
		setEnableButtonBawah(false);	
		GenericMapper mapper2=MasterMap.obtainMapperFor(MemJournalNonStrdDet.class);
		mapper2.setActiveConn(m_conn);
		try {
			if ((new Long(entity().getIndex()).toString().equalsIgnoreCase("0") )){
				long index = sql.getMaxIndex(IDBConstants.TABLE_MEMORIAL_JOURNAL_NONSTANDARD, m_conn);
				entity().setIndex(index);			
			}			
			if (entity().getMemJournalNonStrdDets()!=null){
				MemJournalNonStrdDet temp[]=entity().getMemJournalNonStrdDets();
				mapper2.doDeleteByColumn(IDBConstants.ATTR_MEMORIAL_JOURNAL_NONSTANDARD, new Long(entity().getIndex()).toString());
				for (int i=0;i<temp.length;i++){
					temp[i].setMemJournalNonStrd((MemJournalNonStrd)entity());
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
	
	class MemJournalNonStrdTableProject extends JTable {
		private static final long serialVersionUID = 1L;
		buttomTabbleRcvOthersModel model = new buttomTabbleRcvOthersModel();
		ArrayList listData=new ArrayList();
		protected MemJournalNonStrdTableProject() {
			model.addColumn("No");
			model.addColumn("Account");
			model.addColumn("Account Name");
			model.addColumn("Subsidiary");
			model.addColumn("Debit");
			model.addColumn("Credit");
			model.addColumn("Curr");
			model.addColumn("Exch Rate");
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
			model.addRow(new Object[]{null, null, null, null, null, null});
			model.addRow(new Object[]{null, null, "TOTAL", null,new Double(0),new Double(0),baseCurrency,null});
		}
		
		
		public void addData(Vector obj,Account acc,int insertRow){			
			listData.add(acc);
			model.insertRow(insertRow,obj);
			updateSummary();
			updateNumbering();
		}
		
		public void removeData(int row) {
			listData.remove(row);
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);
			updateSummary();
			updateNumbering();
		}
		
		public TableCellEditor getCellEditor(int row, int col) {			
			String currSymbol = "";
			Currency curr = null;
			if (getValueAt(row,6) instanceof Currency) {
				curr = (Currency) getValueAt(row,6);	
				currSymbol = curr.getSymbol();
			}
			
			if (row <= getRowCount()-3){
				Account acc = (Account)listData.get(row);
				if (col ==3){						
					String SubsidiaryAccSet = getSubsidiaryByindex(acc);
					System.err.println("SubsidiaryAccSet :" + SubsidiaryAccSet);
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
					}else
						return null;
				}else if (col == 4 || col==5)					
					//return new DoubleCellEditor();
					return new FormattedDoubleCellEditor(JLabel.RIGHT);
				else if (col==6){
					return new CurrencyCellEditor(GumundaMainFrame.getMainFrame(),"Currency",m_conn,m_sessionid);
				}else if (col==7 && !currSymbol.equals(baseCurrency.getSymbol()) && !currSymbol.equals(""))
					//return new DoubleCellEditor();
					return new FormattedDoubleCellEditor(JLabel.RIGHT);
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
					if(column==4 || column==5 || column==7)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);	
					if (column ==6)
						return new FormattedStandardCellRenderer(Font.BOLD, JLabel.CENTER);
				}else{				
					if(column==4 || column==5 || column==7)
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
			int maxTotalRow = maxRow - 2;
			double debit = 0;	
			double credit = 0;	
			for(int i=0; i<=maxTotalRow; i++){										
				debit = ((Double)getValueAt(i,4)).doubleValue();
				credit = ((Double)getValueAt(i, 5)).doubleValue();
				if (debit>0 && credit>0)
					return true;		
			}				
			return false;
		}
		
		public boolean cekTotalDebitCredit(){
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
