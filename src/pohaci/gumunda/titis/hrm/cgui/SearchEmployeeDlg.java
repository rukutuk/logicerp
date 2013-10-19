package pohaci.gumunda.titis.hrm.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.cgui.report.RptFieldAllowances;
import pohaci.gumunda.titis.project.cgui.report.RptPersonalUtilization;
import pohaci.gumunda.titis.accounting.cgui.PayrollPaychequeVerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.PayrollTaxArt21VerificationPanel;
import pohaci.gumunda.titis.accounting.cgui.UnitCellEditor;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.Misc;

public class SearchEmployeeDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	JFrame m_mainframe;
	EmployeeListPanel m_panel;
	SearchTable m_table;
	JRadioButton m_andRadioBt, m_orRadioBt;
	JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
	JButton m_findBt, m_closeBt, m_clearBt;
	JComboBox m_statusComboBox;
	Connection m_conn = null;
	long m_sessionid = -1;
	RptFieldAllowances m_RptFieldAllowances;
	RptPersonalUtilization m_RptPersonalUtilization;
	PayrollInsuranceAllowanceSubmitPanel m_PayrollInsuranceAllowanceSubmitPanel;
	PayrollOtherAllowanceSubmitPanel m_PayrollOtherAllowanceSubmitPanel;
	PayrollPaychequeSubmitPanel m_PayrollPaychequeSubmitPanel;
	PayrollOvertimeSubmitPanel m_PayrollOvertimeSubmitPanel;
	PayrollTransportationAllowanceSubmitPanel m_PayrollTransportationAllowanceSubmitPanel;
	PayrollMealAllowanceSubmitPanel m_PayrollMealAllowanceSubmitPanel;
	PayrollTaxArt21SubmitPanel m_PayrollTaxArt21SubmitPanel;
	PayrollTaxArt21VerificationPanel m_PayrollTaxArt21VerificationPanel;
	PayrollPaychequeVerificationPanel m_payrollPaychequeVerificationPanel;
	String m_field = "";
	String m_criteria = "";  
	
	// konstrukter dipanggil pada employee
	public SearchEmployeeDlg(JFrame owner,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 480);
		m_mainframe = owner;
		m_field = "employee";
		m_panel = null;
		m_conn = conn;
		m_sessionid = sessionid;    
		constructComponent();
		
	}
	
	// konstrukter dipanggil pada employee
	public SearchEmployeeDlg(JFrame owner, EmployeeListPanel panel,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 500);
		m_mainframe = owner;
		m_field = "employee";
		m_panel = panel;
		m_conn = conn;
		m_sessionid = sessionid;    
		constructComponent();
		
	}
	// konstruktro dipanggil pada RptFieldAllowances
	public SearchEmployeeDlg(JFrame owner,  RptFieldAllowances RptFieldAllowances,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 370);
		m_mainframe = owner;		
		m_RptFieldAllowances = RptFieldAllowances;
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}
	
	// konstruktor dipanggil pada RptPersonalUtilization
	public SearchEmployeeDlg(JFrame owner,  RptPersonalUtilization RptPersonalUtilization,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 370);
		m_mainframe = owner;		
		m_RptPersonalUtilization = RptPersonalUtilization;
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();	
	}
	
	public SearchEmployeeDlg(JFrame owner,  PayrollInsuranceAllowanceSubmitPanel PayrollInsuranceAllowanceSubmitPanel,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 320);
		m_mainframe = owner;		
		m_PayrollInsuranceAllowanceSubmitPanel = PayrollInsuranceAllowanceSubmitPanel;
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}
	
	public SearchEmployeeDlg(JFrame owner,  PayrollOtherAllowanceSubmitPanel PayrollOtherAllowanceSubmitPanel,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 320);//// 280 diganti jadi 310 biar pas
		m_mainframe = owner;		
		m_PayrollOtherAllowanceSubmitPanel = PayrollOtherAllowanceSubmitPanel;
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}
	
	//konstruktor dipanggil pada PayrollPaychequeSubmitPanel
	public SearchEmployeeDlg(JFrame owner,  PayrollPaychequeSubmitPanel PayrollPaychequeSubmitPanel,
			Connection conn, long sessionid ) {
		super(owner, "Search Employee", false);
		setSize(350, 320);
		m_mainframe = owner;		
		m_PayrollPaychequeSubmitPanel = PayrollPaychequeSubmitPanel;
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}
	
	//konstruktor dipanggil pada PayrollPaychequeSubmitPanel
	public SearchEmployeeDlg(JFrame owner,  PayrollOvertimeSubmitPanel PayrollOvertimeSubmitPanel,
			Connection conn, long sessionid ) {
		super(owner, "Search Employee", false);
		setSize(350, 320);
		m_mainframe = owner;		
		m_PayrollOvertimeSubmitPanel = PayrollOvertimeSubmitPanel;
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}
	
	//konstruktor dipanggil pada PayrollTransportation
	public SearchEmployeeDlg(JFrame owner,  PayrollTransportationAllowanceSubmitPanel PayrollTransportationAllowanceSubmitPanel,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 320);
		m_mainframe = owner;		
		m_PayrollTransportationAllowanceSubmitPanel = PayrollTransportationAllowanceSubmitPanel;
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}
	
	//konstruktor dipanggil pada MealAlowance
	public SearchEmployeeDlg(JFrame owner,  PayrollMealAllowanceSubmitPanel PayrollMealAllowanceSubmitPanel,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 320);
		m_mainframe = owner;		
		m_PayrollMealAllowanceSubmitPanel = PayrollMealAllowanceSubmitPanel;
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}
	
	//konstruktor dipanggil pada MealAlowance
	public SearchEmployeeDlg(JFrame owner,  PayrollTaxArt21SubmitPanel PayrollTaxArt21SubmitPanel,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 320);
		m_mainframe = owner;		
		m_PayrollTaxArt21SubmitPanel = PayrollTaxArt21SubmitPanel;
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}
	
//	konstruktor dipanggil pada PayrollTaxArt21VerificationPanel
	public SearchEmployeeDlg(JFrame owner,  PayrollTaxArt21VerificationPanel PayrollTaxArt21VerificationPanel,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 320);
		m_mainframe = owner;		
		m_PayrollTaxArt21VerificationPanel = PayrollTaxArt21VerificationPanel;		
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}
	
//	konstruktor dipanggil pada PayrollTaxArt21VerificationPanel
	public SearchEmployeeDlg(JFrame owner,  PayrollPaychequeVerificationPanel PayrollPaychequeVerificationPanel,
			Connection conn, long sessionid) {
		super(owner, "Search Employee", false);
		setSize(350, 320);
		m_mainframe = owner;		
		m_payrollPaychequeVerificationPanel = PayrollPaychequeVerificationPanel;		
		m_conn = conn;
		m_sessionid = sessionid;	
		constructComponent();
	}

	public void setBorder(JPanel panel,String theme){
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), theme ,
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
	}
	
	JPanel criteriaPanel() {
		int height = getHeightPanel(m_field);		  
		
		m_table = new SearchTable(m_field);
		m_statusComboBox = new JComboBox(new Object[]{"Active","Retired","All"});
		m_andRadioBt = new JRadioButton("and");
		m_andRadioBt.setSelected(true);
		m_orRadioBt = new JRadioButton("or");
		
		m_containsRadioBt = new JRadioButton("Text Contains Criteria");
		m_containsRadioBt.setSelected(true);
		m_matchRadioBt = new JRadioButton("Match Case");
		m_wholeRadioBt = new JRadioButton("Find Whole Words only");
		m_findBt = new JButton("Find");
		m_findBt.addActionListener(this);
		m_closeBt = new JButton("Close");
		m_closeBt.addActionListener(this);
		m_clearBt = new JButton("Clear");
		m_clearBt.addActionListener(this);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(m_andRadioBt);
		bg.add(m_orRadioBt);
		
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(m_containsRadioBt);
		bg2.add(m_matchRadioBt);
		bg2.add(m_wholeRadioBt);
		
		JPanel centerPanel = new JPanel();
		JPanel criteriaPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel operatorPanel = new JPanel();
		JPanel optionPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gridBagConstraints;    
		
		scrollPane.setPreferredSize(new Dimension(100, height));
		scrollPane.getViewport().add(m_table);    
		
		operatorPanel.setLayout(new GridLayout(2, 1));
		operatorPanel.add(m_andRadioBt);
		operatorPanel.add(m_orRadioBt);
		setBorder(operatorPanel,"Search Operator");
		
		optionPanel.setLayout(new GridLayout(3, 1));
		optionPanel.add(m_containsRadioBt);
		optionPanel.add(m_matchRadioBt);
		optionPanel.add(m_wholeRadioBt);
		setBorder(optionPanel,"Option");
		
		criteriaPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		gridBagConstraints.weightx = 1.0;
		//criteriaPanel.add(new JLabel("Search Criterion"), gridBagConstraints);
		setBorder(criteriaPanel,"Search Criterion");
		
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(1, 0, 5, 0);
		criteriaPanel.add(scrollPane, gridBagConstraints);
		
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		//criteriaPanel.add(new JLabel("Search Operator"), gridBagConstraints);
		
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new Insets(1, 0, 5, 0);
		criteriaPanel.add(operatorPanel, gridBagConstraints);
		
		gridBagConstraints.gridy = 4;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		
		gridBagConstraints.gridy = 5;
		criteriaPanel.add(optionPanel, gridBagConstraints);
		
		gridBagConstraints.gridy = 6;
		criteriaPanel.add(buttonPanel, gridBagConstraints);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(m_findBt);
		buttonPanel.add(m_closeBt);
		buttonPanel.add(m_clearBt);
		
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		centerPanel.add(criteriaPanel, BorderLayout.NORTH);
		//centerPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		return centerPanel;
	}
	
	int getHeightPanel(String field){
		int value;
		if (field.equals("employee"))
			value = 248;
		else if (m_RptFieldAllowances!=null)
			value = 105;
		else if (m_RptPersonalUtilization!=null)
			value = 105;		
		else 
			value = 53;
		return value;
	}
	
	void constructComponent() {
		getContentPane().add(criteriaPanel(), BorderLayout.CENTER);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}
	
	public void setVisible( boolean flag ){
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());
		
		super.setVisible(flag);
	}
	
	void find() {
		String query = "";
		try {
			query = m_table.getCriterion();
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
			return;
		}
		
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			m_panel.reset(logic.getEmployeeByCriteria(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA, query));
			if (m_panel.m_panel_rptEmployeProfile!=null)
				m_panel.m_panel_rptEmployeProfile.m_employee = null;
			
			else if (m_panel.m_panel_rptPaycheq!=null)
				m_panel.m_panel_rptPaycheq.m_employee = null;
		}
		catch(Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	String getOperator(){
		String operator;
		if(m_andRadioBt.isSelected())
			operator = " AND ";
		else
			operator = " OR ";
		return operator;
	}
	
	void PayrollPaychequeSubmitPanel(){
		String[] attr = m_table.getTwoRow();	 
		m_PayrollPaychequeSubmitPanel.view(false,attr,getOperator());
		//m_PayrollPaychequeSubmitPanel.SetEmpSub(attr,getOperator());
	}
	
	void PayrollMealAllowanceSubmitPanel(){
		String[] attr = m_table.getTwoRow();	
		m_PayrollMealAllowanceSubmitPanel.view(false,attr,getOperator());	 
	}
	
	void PayrollOvertimeSubmitPanel(){
		String[] attr = m_table.getTwoRow();
		m_PayrollOvertimeSubmitPanel.view(false,attr,getOperator());	  
	}
	
	void PayrollTransportationAllowanceSubmitPanel(){
		String[] attr = m_table.getTwoRow();
		m_PayrollTransportationAllowanceSubmitPanel.view(false,attr,getOperator());	
	}
	void PayrollInsuranceAllowanceSubmitPanel(){
		String[] attr = m_table.getTwoRow();	 
		m_PayrollInsuranceAllowanceSubmitPanel.view(false,attr,getOperator());
	}  
	
	void PayrollOtherAllowanceSubmitPanel(){
		String[] attr = m_table.getTwoRow();
		m_PayrollOtherAllowanceSubmitPanel.view(false,attr,getOperator());
	}
	
	void RptFieldAllowances(){	  
		String[] attr = m_table.getFiveRow();
		m_RptFieldAllowances.view(false,attr,getOperator());	  
	}  
	
	void RptPersonalUtilization(){
		String[] attr = m_table.getFiveRow();	
		m_RptPersonalUtilization.view(false,attr,getOperator());
	}
	
	void PayrollTaxArt21SubmitPanel(){
		String[] attr = m_table.getTwoRow();	
		m_PayrollTaxArt21SubmitPanel.view(false,attr,getOperator());
	}  
	
	void PayrollTaxArt21VerificationPanel(){
		String[] attr = m_table.getTwoRow();	
		m_PayrollTaxArt21VerificationPanel.searchPresentingSubmittedData(attr,getOperator());
	}  
	
	void PayrollPaychequesVerivicationPanel(){
		String[] attr = m_table.getTwoRow();	
		//m_payrollVerificationPanel.d
		m_payrollPaychequeVerificationPanel.doSearch(attr,getOperator());
		m_payrollPaychequeVerificationPanel.SetEmpSub(attr,getOperator());
		
		//m_payrollVerificationPanel.searchPresentingSubmittedData(attr,getOperator());
	}  
	
	void clear() {
		m_table.clear();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_findBt) {
			if (m_field.equals("employee")){
				find();
				
			} else if (m_PayrollPaychequeSubmitPanel!=null){
				PayrollPaychequeSubmitPanel();
			} else if (m_PayrollMealAllowanceSubmitPanel!=null){
				PayrollMealAllowanceSubmitPanel();
			} else if (m_PayrollOvertimeSubmitPanel!=null){
				PayrollOvertimeSubmitPanel();
			} else if (m_PayrollTransportationAllowanceSubmitPanel!=null){
				PayrollTransportationAllowanceSubmitPanel();
			} else if (m_RptFieldAllowances!=null){    	  
				RptFieldAllowances();    	  
			} else if (m_RptPersonalUtilization!=null){
				RptPersonalUtilization();
			} else if (m_PayrollInsuranceAllowanceSubmitPanel!=null){
				PayrollInsuranceAllowanceSubmitPanel();
			} else if (m_PayrollOtherAllowanceSubmitPanel!=null){
				PayrollOtherAllowanceSubmitPanel();
			} else if (m_PayrollTaxArt21SubmitPanel!=null){
				PayrollTaxArt21SubmitPanel();
			} else if (m_PayrollTaxArt21VerificationPanel!=null){
				PayrollTaxArt21VerificationPanel();
			} else if (m_payrollPaychequeVerificationPanel!=null){
				PayrollPaychequesVerivicationPanel();
			}
		}
		else if(e.getSource() == m_closeBt) {
			if(m_panel != null)
				m_panel.m_show = false;
			dispose();
		}
		else if(e.getSource() == m_clearBt) {
			m_table.clear();
		}
	}
	
	class SearchTable extends JTable {	
		private static final long serialVersionUID = 1L;
		
		public SearchTable( String field) {
			SearchTableModel model = new SearchTableModel();
			model.addColumn("Attribute");
			model.addColumn("Description");
			if (field.equals("employee")){
				model.addRow(new Object[]{"Employee No", ""});
				model.addRow(new Object[]{"Name", ""});      
				model.addRow(new Object[]{"Education", ""});      
				model.addRow(new Object[]{"Job Title", ""});
				model.addRow(new Object[]{"Department", ""});
				model.addRow(new Object[]{"Unit Code", ""});          	  
				model.addRow(new Object[]{"Work Agreement", ""});    	  
				model.addRow(new Object[]{"Sex", ""});
				model.addRow(new Object[]{"City", ""});
				model.addRow(new Object[]{"Province", ""});
				model.addRow(new Object[]{"Phone", ""});
				model.addRow(new Object[]{"Marital Status", ""});    	  
				model.addRow(new Object[]{"Qualification", ""});	      
				model.addRow(new Object[]{"Status", "Active"});	      
			}else if (m_PayrollPaychequeSubmitPanel!=null){
				getTwoAddRow(model);		  
			}else if (m_PayrollMealAllowanceSubmitPanel!=null){
				getTwoAddRow(model);		  
			}else if (m_PayrollOvertimeSubmitPanel!=null){
				getTwoAddRow(model);		  
			}else if (m_PayrollTransportationAllowanceSubmitPanel!=null){
				getTwoAddRow(model);		  
			}else if (m_PayrollInsuranceAllowanceSubmitPanel!=null){
				getTwoAddRow(model);     
			}else if (m_PayrollOtherAllowanceSubmitPanel!=null){
				getTwoAddRow(model);
			}else if (m_RptFieldAllowances!=null){
				getFiveAddRow(model);
			}else if (m_RptPersonalUtilization!=null){
				getFiveAddRow(model);
			}else if (m_PayrollTaxArt21SubmitPanel!=null){
				getTwoAddRow(model);		  
			}else if (m_PayrollTaxArt21VerificationPanel!=null){
				getTwoAddRow(model);
			}else if (m_payrollPaychequeVerificationPanel!=null){
				getTwoAddRow(model);
			}
			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(100);
			getColumnModel().getColumn(0).setMaxWidth(100);
			getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
		}

		private void getFiveAddRow(SearchTableModel model) {
			model.addRow(new Object[]{"Employee No", ""});
			model.addRow(new Object[]{"Name", ""}); 
			model.addRow(new Object[]{"Job Title", ""});
			model.addRow(new Object[]{"Work Agreement", ""});  
			model.addRow(new Object[]{"Qualification", ""});
		}

		private void getTwoAddRow(SearchTableModel model) {
			model.addRow(new Object[]{"Name", ""}); 
			model.addRow(new Object[]{"Job Title", ""});
		}
		
		public TableCellEditor getCellEditor(int row, int col) {      
			if (m_field.equals("employee")){
				if(row == 2 && col == 1)
					return new EducationCellEditor(GumundaMainFrame.getMainFrame(),
							"Education", m_conn, m_sessionid);    	
				else if(row == 3 && col == 1)
					return new JobTitleCellEditor(GumundaMainFrame.getMainFrame(),
							"Job Title", m_conn, m_sessionid);
				else if(row == 4 && col == 1)
					return new OrganizationCellEditor(GumundaMainFrame.getMainFrame(),
							m_conn, m_sessionid);    	
				else if(row == 5 && col == 1)
					return new UnitCellEditor(GumundaMainFrame.getMainFrame(),
							"Unit Code", m_conn, m_sessionid);
				else if(row == 6 && col == 1)
					return new WorkAgreementCellEditor(GumundaMainFrame.getMainFrame(), 
							"Work Agreement", m_conn, m_sessionid);
				else if(row == 7 && col == 1)
					return new SexTypeCellEditor(GumundaMainFrame.getMainFrame(),
							"Sex Type", m_conn, m_sessionid);
				else if(row == 11 && col == 1)
					return new MaritalStatusCellEditor(GumundaMainFrame.getMainFrame(),
							"Marital Status", m_conn, m_sessionid);
				else if(row == 12 && col == 1)
					return new QualificationCellEditor(GumundaMainFrame.getMainFrame(),
							"Qualification", m_conn, m_sessionid);
				else if(row == 13 && col == 1)
					return new DefaultCellEditor(m_statusComboBox);
			}	
			return new BaseTableCellEditor();
		}
		
		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}
		
		public void clear() {
			stopCellEditing();
			int row = getRowCount();
			for(int i = 0; i < row; i ++){
				setValueAt("", i, 1);
				if(i==13)
					setValueAt("Active",i,1);
				else
					setValueAt("", i, 1);
			}			
		}
		
		public String getCriteria(String attribute, String value) {
			String criteria = "";
			if(m_containsRadioBt.isSelected())
				criteria = Misc.getCriteria(attribute, value, Misc.CONTAINTS_CRITERIA);
			else if(m_matchRadioBt.isSelected())
				criteria = Misc.getCriteria(attribute, value, Misc.MATCH_CASE);
			else
				criteria = Misc.getCriteria(attribute, value, Misc.FIND_WHOLE_WORDS_ONLY);
			return criteria;
		}
		
		public String[] getTwoRow(){
			String[] attr = new String[2];
			stopCellEditing();
			attr[0] = (String)getValueAt(0, 1);
			attr[1] = (String)getValueAt(1, 1);    	            
			return attr;
		}    
		
		public String[] getFiveRow(){
			String[] attr = new String[5];
			stopCellEditing();
			attr[0] = (String)getValueAt(0, 1);
			attr[1] = (String)getValueAt(1, 1);    	            
			attr[2] = (String)getValueAt(2, 1);
			attr[3] = (String)getValueAt(3, 1);
			attr[4] = (String)getValueAt(4, 1);
			return attr;
		}		
				
		public String getCriterion() throws Exception {
			stopCellEditing();
			String operator, criteria = "";
			//String equality = " = ";
			int row = 0;
			
			String queryselect = "SELECT DISTINCT emp." +
			IDBConstants.ATTR_AUTOINDEX + ", emp." +
			IDBConstants.ATTR_EMPLOYEE_NO + ", emp." +
			IDBConstants.ATTR_FIRST_NAME + ", emp." +
			IDBConstants.ATTR_MIDLE_NAME + ", emp." +
			IDBConstants.ATTR_LAST_NAME + ", emp." +
			IDBConstants.ATTR_NICK_NAME + ", emp." +
			IDBConstants.ATTR_BIRTH_PLACE + ", emp." +
			IDBConstants.ATTR_BIRTH_DATE + ", emp." +
			IDBConstants.ATTR_NATIONALITY + ", emp." +
			IDBConstants.ATTR_ADDRESS + ", emp." +
			IDBConstants.ATTR_CITY + ", emp." +
			IDBConstants.ATTR_POST_CODE + ", emp." +
			IDBConstants.ATTR_PROVINCE + ", emp." +
			IDBConstants.ATTR_COUNTRY + ", emp." +
			IDBConstants.ATTR_PHONE + ", emp." +
			IDBConstants.ATTR_MOBILE_PHONE1 + ", emp." +
			IDBConstants.ATTR_MOBILE_PHONE2 + ", emp." +
			IDBConstants.ATTR_FAX + ", emp." +
			IDBConstants.ATTR_EMAIL + ", emp." +
			IDBConstants.ATTR_SEX + ", emp." +
			IDBConstants.ATTR_MARITAL + ", emp." +
			IDBConstants.ATTR_RELIGION + ", emp." +
			IDBConstants.ATTR_ART_21;
			
			String querytable = " FROM " +
			IDBConstants.TABLE_EMPLOYEE + " emp " +
			"LEFT JOIN (SELECT e." +
			IDBConstants.ATTR_EMPLOYEE + ", e." +
			IDBConstants.ATTR_JOB_TITLE + ", e." +
			IDBConstants.ATTR_DEPARTMENT + ", e." +
			IDBConstants.ATTR_UNIT + ", e." +
			IDBConstants.ATTR_WORK_AGREEMENT + ", e." +
			IDBConstants.ATTR_TMT + " " +
			"FROM " + IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + " e, "+ 
			"(SELECT " + 
			IDBConstants.ATTR_EMPLOYEE + ", " +
			"MAX(" + IDBConstants.ATTR_TMT + ") " + IDBConstants.ATTR_TMT + " " +
			"FROM (SELECT * FROM " + IDBConstants.TABLE_EMPLOYEE_EMPLOYMENT + " " +
			"WHERE " + IDBConstants.ATTR_TMT + "<DATE(NOW())) employ GROUP BY " +
			IDBConstants.ATTR_EMPLOYEE + ") lastemp " +
			"WHERE e." + IDBConstants.ATTR_EMPLOYEE + "=" +
			"lastemp." + IDBConstants.ATTR_EMPLOYEE + " AND " +
			"e." + IDBConstants.ATTR_TMT + "=" +
			"lastemp." + IDBConstants.ATTR_TMT + ") employ ON " +
			"emp." + IDBConstants.ATTR_AUTOINDEX + 
			"=employ." + IDBConstants.ATTR_EMPLOYEE + " " +
			"LEFT JOIN (SELECT " + 
			IDBConstants.ATTR_EMPLOYEE + ", " +
			IDBConstants.ATTR_GRADE + ", " + 
			"MAX(" + IDBConstants.ATTR_TO + ") ATTRTO " +
			"FROM " + IDBConstants.TABLE_EMPLOYEE_EDUCATION + " "+
			"GROUP BY " +
			IDBConstants.ATTR_EMPLOYEE + ", " +
			IDBConstants.ATTR_GRADE + ") edu ON " +
			"emp." + IDBConstants.ATTR_AUTOINDEX + 
			"=edu." + IDBConstants.ATTR_EMPLOYEE + " " +
			"LEFT JOIN " + IDBConstants.TABLE_EMPLOYEE_QUALIFICATION + " qua ON " +
			"emp." + IDBConstants.ATTR_AUTOINDEX +
			"=qua." + IDBConstants.ATTR_EMPLOYEE;
			
			if(m_andRadioBt.isSelected())
				operator = " AND ";
			else
				operator = " OR ";
			
			// employee no
			String value = (String)getValueAt(row++, 1);
			if(!value.equals(""))
				criteria = "WHERE " + getCriteria("emp." + IDBConstants.ATTR_EMPLOYEE_NO, value);
			
			// name      
			value = (String)getValueAt(row++, 1);     
			
			if(!value.equals("")){
				if(criteria.equals(""))
					criteria = "WHERE " + getCriteria("emp." + IDBConstants.ATTR_FIRST_NAME + 
							"& ' ' & emp." + IDBConstants.ATTR_MIDLE_NAME + 
							"& ' ' & emp." + IDBConstants.ATTR_LAST_NAME, value);
				else
					criteria += operator + getCriteria(" emp." + IDBConstants.ATTR_FIRST_NAME + 
							"& ' ' & emp." + IDBConstants.ATTR_MIDLE_NAME + 
							"& ' ' & emp." + IDBConstants.ATTR_LAST_NAME, value);
			}
			
			// education
			Object obj = (getValueAt(row++, 1));
			if(obj instanceof Education){
				if(criteria.equals(""))
					criteria = " WHERE edu." + IDBConstants.ATTR_GRADE +
					"=" + ((Education)obj).getIndex();
				else
					criteria += operator + " edu." + IDBConstants.ATTR_GRADE +
					"=" + ((Education)obj).getIndex();
			}
			
			//job title
			obj = (getValueAt(row++, 1));
			if(obj instanceof JobTitle){
				if(criteria.equals(""))
					criteria = " WHERE employ." + IDBConstants.ATTR_JOB_TITLE +
					"=" + ((JobTitle)obj).getIndex();
				else
					criteria += operator + " employ." + IDBConstants.ATTR_JOB_TITLE +
					"=" + ((JobTitle)obj).getIndex();
			}
			
			// department
			obj = (getValueAt(row++, 1));
			if(obj instanceof Organization){
				if(criteria.equals(""))
					criteria = " WHERE employ." + IDBConstants.ATTR_DEPARTMENT +
					"=" + ((Organization)obj).getIndex();
				else
					criteria += operator + " employ." + IDBConstants.ATTR_DEPARTMENT +
					"=" + ((Organization)obj).getIndex();
			}
			
			//  unit code
			obj = (getValueAt(row++, 1));
			if(obj instanceof Unit){
				if(criteria.equals(""))
					criteria = " WHERE employ." + IDBConstants.ATTR_UNIT +
					"=" + ((Unit)obj).getIndex();
				else
					criteria += operator + " employ." + IDBConstants.ATTR_UNIT +
					"=" + ((Unit)obj).getIndex();
			}
			
			// work agreement
			obj = (getValueAt(row++, 1));
			if(obj instanceof WorkAgreement){
				if(criteria.equals(""))
					criteria = " WHERE employ." + IDBConstants.ATTR_WORK_AGREEMENT +
					"=" + ((WorkAgreement)obj).getIndex();
				else
					criteria += operator + " employ." + IDBConstants.ATTR_WORK_AGREEMENT +
					"=" + ((WorkAgreement)obj).getIndex();
			}
			
			//sex			
			obj = (getValueAt(row++, 1));
			if(obj instanceof SexType){
				if(criteria.equals(""))
					criteria = "WHERE emp." + IDBConstants.ATTR_SEX + "=" + 
					((SexType)obj).getIndex();
				else 
					criteria += operator + " emp." + IDBConstants.ATTR_SEX + "=" + 
					((SexType)obj).getIndex();
			}
			
			//city
			value = (String)getValueAt(row++, 1);
			if(!value.equals("")){
				if(criteria.equals(""))
					criteria = "WHERE " + getCriteria("emp." + IDBConstants.ATTR_CITY, value);
				else
					criteria += operator + getCriteria(" emp." + IDBConstants.ATTR_CITY, value);
			}
			
			//province
			value = (String)getValueAt(row++, 1);
			if(!value.equals("")){
				if(criteria.equals(""))
					criteria = "WHERE " + getCriteria("emp." + IDBConstants.ATTR_PROVINCE, value);
				else
					criteria += operator + getCriteria(" emp." + IDBConstants.ATTR_PROVINCE, value);
			}
			
			//phone
			value = (String)getValueAt(row++, 1);
			if(!value.equals("")){
				if(criteria.equals(""))
					criteria = "WHERE " + getCriteria("emp." + IDBConstants.ATTR_PHONE, value);
				else
					criteria += operator + getCriteria(" emp." + IDBConstants.ATTR_PHONE, value);
			}
			
			obj = getValueAt(row++, 1);
			if(obj instanceof SimpleEmployeeAttribute){
				if(criteria.equals(""))
					criteria = "WHERE emp." + IDBConstants.ATTR_MARITAL + "=" +
					((SimpleEmployeeAttribute)obj).getIndex();
				else
					criteria += operator + " emp." + IDBConstants.ATTR_MARITAL + "=" +
					((SimpleEmployeeAttribute)obj).getIndex();
			}
			
			//qualification
			obj = getValueAt(row++, 1);
			if(obj instanceof Qualification){
				if(criteria.equals(""))
					criteria = "WHERE qua." + IDBConstants.ATTR_QUALIFICATION + "=" +
					((Qualification)obj).getIndex();
				else
					criteria += operator + " qua." + IDBConstants.ATTR_QUALIFICATION+ "=" +
					((Qualification)obj).getIndex();
			}
			
			value = (String)getValueAt(row++, 1);
			if(criteria.equals("")) {
				if(!value.equals("All")) {
					if(value.equals("Active"))
						criteria += "WHERE NOT EXISTS (SELECT " + IDBConstants.ATTR_EMPLOYEE +
						" FROM " + IDBConstants.TABLE_EMPLOYEE_RETIREMENT + " ret " +
						" WHERE " + IDBConstants.ATTR_TMT + "<=DATE(NOW()) AND" +
						" ret." + IDBConstants.ATTR_EMPLOYEE + 
						"=emp." + IDBConstants.ATTR_AUTOINDEX + ")";
					else //retired
						criteria += "WHERE EXISTS (SELECT " + IDBConstants.ATTR_EMPLOYEE +
						" FROM " + IDBConstants.TABLE_EMPLOYEE_RETIREMENT + " ret " +
						" WHERE " + IDBConstants.ATTR_TMT + "<=DATE(NOW()) AND" +
						" ret." + IDBConstants.ATTR_EMPLOYEE + 
						"=emp." + IDBConstants.ATTR_AUTOINDEX + ")";
				}
			}
			else {
				if(!value.equals("All")) {
					if(value.equals("Active"))
						criteria += operator + " NOT EXISTS (SELECT " + IDBConstants.ATTR_EMPLOYEE +
						" FROM " + IDBConstants.TABLE_EMPLOYEE_RETIREMENT + " ret " +
						" WHERE " + IDBConstants.ATTR_TMT + "<DATE(NOW()) AND" +
						" ret." + IDBConstants.ATTR_EMPLOYEE + 
						"=emp." + IDBConstants.ATTR_AUTOINDEX + ")";
					else
						criteria += operator + " EXISTS (SELECT " + IDBConstants.ATTR_EMPLOYEE +
						" FROM " + IDBConstants.TABLE_EMPLOYEE_RETIREMENT + " ret " +
						" WHERE " + IDBConstants.ATTR_TMT + "<DATE(NOW()) AND" +
						" ret." + IDBConstants.ATTR_EMPLOYEE + 
						"=emp." + IDBConstants.ATTR_AUTOINDEX + ")";
				}
			}
			
			String sort = " ORDER BY " + IDBConstants.ATTR_EMPLOYEE_NO;
			
			String query = queryselect + querytable + " " + criteria + sort;
			return query;
		}
	}
	
	class SearchTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {
			if(col == 0)
				return false;
			return true;
		}
		
		public void setValueAt(Object obj, int row, int col) {
			if(obj instanceof Education)
				super.setValueAt(new Education((Education)obj, EmployeeAttribute.CODE), row, col);
			else if(obj instanceof JobTitle)
				super.setValueAt(new JobTitle((JobTitle)obj, EmployeeAttribute.DESCRIPTION), row, col);
			else if(obj instanceof Organization)
				super.setValueAt(new Organization((Organization)obj, EmployeeAttribute.DESCRIPTION), row, col);
			else if(obj instanceof Unit)
				super.setValueAt(new Unit((Unit)obj, Unit.CODE_DESCRIPTION), row, col);
			else if(obj instanceof WorkAgreement)
				super.setValueAt(new WorkAgreement((WorkAgreement)obj, EmployeeAttribute.DESCRIPTION), row, col);
			else if(obj instanceof SexType)
				super.setValueAt(new SexType((SexType)obj, EmployeeAttribute.DESCRIPTION), row, col);
			else if(obj instanceof Qualification)
				super.setValueAt(new Qualification((Qualification)obj, EmployeeAttribute.CODE), row, col);
			else
				super.setValueAt(obj, row, col);
		}
	}
}
;
