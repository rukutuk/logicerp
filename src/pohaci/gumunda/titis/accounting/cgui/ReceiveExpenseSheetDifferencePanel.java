package pohaci.gumunda.titis.accounting.cgui;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEsDiff;
import pohaci.gumunda.titis.accounting.cgui.report.Rcpt_ESDiff;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.RcvESDiff;
import pohaci.gumunda.titis.accounting.entity.SalesReceivedLoader;
import pohaci.gumunda.titis.accounting.logic.BeginningBalanceBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.BeginningESBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class ReceiveExpenseSheetDifferencePanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener{
	
	private static final long serialVersionUID = 1L;
	private RcvESDiff m_entity;
	Employee defaultOriginator;
	Employee defaultApproved;
	Employee defaultReceived;
	public ReceiveExpenseSheetDifferencePanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		enableAwal();
		disableEditMode();
		addingListenerParents();
		addingListener();
		initBaseCurrency(conn, sessionid);
		initExchangeRateHelper(conn, sessionid);
		setDefaultSignature(); 
		setEntity(new RcvESDiff());
		m_entityMapper = MasterMap.obtainMapperFor(RcvESDiff.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	} 
	
	private void setDefaultExchangeRate(Currency currency) {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		ESDiffExchRateText.setValue(new Double(rate));
	}
	
	private LookupExpRcvOwner ReceiveFromComp;
	private javax.swing.JLabel ReceiveFromLabel;
	
	private void initComponents() {
		ReceiveFromComp=new LookupExpRcvOwner(m_conn, m_sessionid);
		ReceiveFromLabel= new JLabel();
		AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid);
		ESNoComp = new LookupExpenseSheetNoPicker(m_conn, m_sessionid);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		TopButtonPanel = new javax.swing.JPanel();
		java.awt.GridBagConstraints gridBagConstraints;
		jPanel1 = new javax.swing.JPanel();
		jPanel1_1 = new javax.swing.JPanel();
		m_newBtn = new javax.swing.JButton();
		m_editBtn = new javax.swing.JButton();
		m_saveBtn = new javax.swing.JButton();
		m_deleteBtn = new javax.swing.JButton();
		m_cancelBtn = new javax.swing.JButton();
		m_submitBtn = new javax.swing.JButton();
		jPanel1_2 = new javax.swing.JPanel();
		jPanel1_2_1 = new javax.swing.JPanel();
		StatusLabel = new javax.swing.JLabel();
		SubmittedDateLabel = new javax.swing.JLabel();
		ReceiveToLabel = new javax.swing.JLabel();
		AccountLabel = new javax.swing.JLabel();
		ExpenseSheetNoLabel = new javax.swing.JLabel();
		ExpenseSheetTypeLabel = new javax.swing.JLabel();
		ExpenseSheetDateLabel = new javax.swing.JLabel();
		UnitCodeLabel = new javax.swing.JLabel();
		DepartmentLabel = new javax.swing.JLabel();
		TotalExpensesLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		RcvToCombo = new javax.swing.JComboBox();
		ESTypeText = new javax.swing.JTextField();
		ESDateText = new DatePicker();
		UnitCodeText = new javax.swing.JTextField();
		DepartmentText = new javax.swing.JTextField();
		jPanel1_2_1_2 = new javax.swing.JPanel();
		TotExpensesCurrText = new javax.swing.JTextField();
		TotExpensesAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		jPanel1_2_1_1 = new javax.swing.JPanel();
		ExpSheetDiffLabel = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		CurrencyLabel1 = new javax.swing.JLabel();
		ESDiffCurrText = new CurrencyPicker(m_conn,m_sessionid);
		ExchRateLabel1 = new javax.swing.JLabel();
		ESDiffExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountLabel1 = new javax.swing.JLabel();
		ESDiffAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountBaseCurrLabel1 = new javax.swing.JLabel();
		ESDiffAmountBaseText = new javax.swing.JFormattedTextField(m_numberFormatter);
		ESDiffAmountBaseCurrText = new javax.swing.JTextField();
		//EndingBalanceLabel = new javax.swing.JLabel();
		/*EndBalanceAmountText = new javax.swing.JTextField();
		 EndBalanceCurrText = new javax.swing.JTextField();*/
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_2_1 = new javax.swing.JPanel();
		ReceiptNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		ReceiptDateLabel = new javax.swing.JLabel();
		CashAdvNoLabel = new javax.swing.JLabel();
		CashAdvDateLabel = new javax.swing.JLabel();
		CashAdvAmountLabel = new javax.swing.JLabel();
		CashAdvNoText = new javax.swing.JTextField();
		CashAdvDateText = new javax.swing.JTextField();
		jPanel1_2_2_1_2 = new javax.swing.JPanel();
		CashAdvAmountCurrText = new javax.swing.JTextField();
		CashAdvAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_2_1_3 = new javax.swing.JPanel();
		ExpSheetDiffLabel1 = new javax.swing.JLabel();
		jSeparator3 = new javax.swing.JSeparator();
		CurrencyLabel2 = new javax.swing.JLabel();
		ESDiffRcvCurrText1 = new javax.swing.JTextField();
		ExchRateLabel2 = new javax.swing.JLabel();
		ESDiffRcvExchRateText1 = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountLabel2 = new javax.swing.JLabel();
		ESDiffRcvAmountText1 = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountBaseCurrLabel2 = new javax.swing.JLabel();
		ESDiffRcvAmountBaseText1 = new javax.swing.JFormattedTextField(m_numberFormatter);
		ESDiffRcvAmountBaseCurrText1 = new javax.swing.JTextField();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		
		setLayout(new java.awt.BorderLayout());
		
		setPreferredSize(new java.awt.Dimension(700, 500));		
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		jPanel1.setPreferredSize(new java.awt.Dimension(650, 450));
		jPanel1_1.setLayout(new java.awt.BorderLayout());
		
		jPanel1_1.setPreferredSize(new java.awt.Dimension(700, 35));
		TopButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));
		
		TopButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
		m_searchRefNoBtn.setPreferredSize(new java.awt.Dimension(21, 21));
		TopButtonPanel.add(m_searchRefNoBtn);
		
		m_printViewRefNoBtn.setPreferredSize(new java.awt.Dimension(21, 21));
		TopButtonPanel.add(m_printViewRefNoBtn);
		
		m_newBtn.setText("New");
		m_newBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_newBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		TopButtonPanel.add(m_newBtn);
		
		m_editBtn.setText("Edit");
		m_editBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_editBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		TopButtonPanel.add(m_editBtn);		
		
		m_deleteBtn.setText("Delete");
		m_deleteBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_deleteBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		TopButtonPanel.add(m_deleteBtn);
		
		m_saveBtn.setText("Save");
		m_saveBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_saveBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		TopButtonPanel.add(m_saveBtn);
		
		m_cancelBtn.setText("Cancel");
		m_cancelBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_cancelBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		TopButtonPanel.add(m_cancelBtn);
		
		m_submitBtn.setText("Submit");
		m_submitBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_submitBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		TopButtonPanel.add(m_submitBtn);
		
		jPanel1_1.add(TopButtonPanel, java.awt.BorderLayout.WEST);
		
		jPanel1.add(jPanel1_1, java.awt.BorderLayout.NORTH);
		
		jPanel1_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2.setPreferredSize(new java.awt.Dimension(650, 380));
		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(440, 400));
		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(StatusLabel, gridBagConstraints);
		
		SubmittedDateLabel.setText("Submitted Date");
		SubmittedDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(SubmittedDateLabel, gridBagConstraints);
		
		ReceiveFromLabel.setText("Receive From*");
		ReceiveFromLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveFromLabel, gridBagConstraints);
		
		ReceiveToLabel.setText("Receive To*");
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveToLabel, gridBagConstraints);
		
		AccountLabel.setText("Account*");
		AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountLabel, gridBagConstraints);
		
		ExpenseSheetNoLabel.setText("Expense Sheet No.*");
		ExpenseSheetNoLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ExpenseSheetNoLabel, gridBagConstraints);
		
		ExpenseSheetTypeLabel.setText("Expense Sheet Type ");
		ExpenseSheetTypeLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ExpenseSheetTypeLabel, gridBagConstraints);
		
		ExpenseSheetDateLabel.setText("Expense Sheet Date");
		ExpenseSheetDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ExpenseSheetDateLabel, gridBagConstraints);
		
		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(UnitCodeLabel, gridBagConstraints);
		
		DepartmentLabel.setText("Department");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DepartmentLabel, gridBagConstraints);
		
		TotalExpensesLabel.setText("Total Expenses");
		TotalExpensesLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(TotalExpensesLabel, gridBagConstraints);
		
		StatusText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(StatusText, gridBagConstraints);
		
		SubmittedDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(SubmittedDateText, gridBagConstraints);
		
		ReceiveFromComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveFromComp, gridBagConstraints);
		
		RcvToCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash", "Bank"}));
		RcvToCombo.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(RcvToCombo, gridBagConstraints);
		
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);
		
		ESNoComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ESNoComp, gridBagConstraints);
		
		ESTypeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ESTypeText, gridBagConstraints);
		
		ESDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ESDateText, gridBagConstraints);
		
		UnitCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(UnitCodeText, gridBagConstraints);
		
		DepartmentText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DepartmentText, gridBagConstraints);
		
		jPanel1_2_1_2.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_2.setPreferredSize(new java.awt.Dimension(290, 18));
		TotExpensesCurrText.setPreferredSize(new java.awt.Dimension(45, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
		jPanel1_2_1_2.add(TotExpensesCurrText, gridBagConstraints);
		
		TotExpensesAmountText.setPreferredSize(new java.awt.Dimension(147, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
		jPanel1_2_1_2.add(TotExpensesAmountText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(jPanel1_2_1_2, gridBagConstraints);
		
		jPanel1_2_1_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(410, 100));
		ExpSheetDiffLabel.setText("Exp. Sheet Diff Rcv");
		ExpSheetDiffLabel.setPreferredSize(new java.awt.Dimension(107, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ExpSheetDiffLabel, gridBagConstraints);
		
		jSeparator1.setPreferredSize(new java.awt.Dimension(200, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(jSeparator1, gridBagConstraints);
		
		CurrencyLabel1.setText("Currency");
		CurrencyLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(CurrencyLabel1, gridBagConstraints);
		
		ESDiffCurrText.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ESDiffCurrText, gridBagConstraints);
		
		ExchRateLabel1.setText("Exch. Rate*");
		ExchRateLabel1.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ExchRateLabel1, gridBagConstraints);
		
		ESDiffExchRateText.setPreferredSize(new java.awt.Dimension(78, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ESDiffExchRateText, gridBagConstraints);
		
		AmountLabel1.setText("Amount");
		AmountLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountLabel1, gridBagConstraints);
		
		ESDiffAmountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ESDiffAmountText, gridBagConstraints);
		
		AmountBaseCurrLabel1.setText("Amount Base Curr.");
		AmountBaseCurrLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountBaseCurrLabel1, gridBagConstraints);
		
		ESDiffAmountBaseText.setPreferredSize(new java.awt.Dimension(140, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ESDiffAmountBaseText, gridBagConstraints);
		
		ESDiffAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(55, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ESDiffAmountBaseCurrText, gridBagConstraints);
		
		/*EndingBalanceLabel.setText("Ending Balance");
		 EndingBalanceLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		 gridBagConstraints = new java.awt.GridBagConstraints();
		 gridBagConstraints.gridx = 0;
		 gridBagConstraints.gridy = 4;
		 gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		 gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		 jPanel1_2_1_1.add(EndingBalanceLabel, gridBagConstraints);*/
		
		/*EndBalanceAmountText.setPreferredSize(new java.awt.Dimension(140, 18));
		 gridBagConstraints = new java.awt.GridBagConstraints();
		 gridBagConstraints.gridx = 2;
		 gridBagConstraints.gridy = 4;
		 gridBagConstraints.gridwidth = 2;
		 gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		 gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		 gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		 jPanel1_2_1_1.add(EndBalanceAmountText, gridBagConstraints);
		 
		 EndBalanceCurrText.setPreferredSize(new java.awt.Dimension(55, 18));
		 gridBagConstraints = new java.awt.GridBagConstraints();
		 gridBagConstraints.gridx = 1;
		 gridBagConstraints.gridy = 4;
		 gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		 gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		 jPanel1_2_1_1.add(EndBalanceCurrText, gridBagConstraints);
		 */
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(jPanel1_2_1_1, gridBagConstraints);
		
		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(415, 400));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(415, 400));
		ReceiptNoLabel.setText("Receipt No");
		ReceiptNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ReceiptNoLabel, gridBagConstraints);
		
		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);
		
		ReceiptDateLabel.setText("Receipt Date*");
		ReceiptDateLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ReceiptDateLabel, gridBagConstraints);
		
		CashAdvNoLabel.setText("Cash Adv. No.");
		CashAdvNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(CashAdvNoLabel, gridBagConstraints);
		
		CashAdvDateLabel.setText("Cash Adv. Date");
		CashAdvDateLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(CashAdvDateLabel, gridBagConstraints);
		
		CashAdvAmountLabel.setText("Cash Adv. Amount");
		CashAdvAmountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(CashAdvAmountLabel, gridBagConstraints);
		
		CashAdvNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(CashAdvNoText, gridBagConstraints);
		
		CashAdvDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(CashAdvDateText, gridBagConstraints);
		
		jPanel1_2_2_1_2.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_2_1_2.setPreferredSize(new java.awt.Dimension(290, 18));
		CashAdvAmountCurrText.setPreferredSize(new java.awt.Dimension(45, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
		jPanel1_2_2_1_2.add(CashAdvAmountCurrText, gridBagConstraints);
		
		CashAdvAmountText.setPreferredSize(new java.awt.Dimension(243, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
		jPanel1_2_2_1_2.add(CashAdvAmountText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(jPanel1_2_2_1_2, gridBagConstraints);
		
		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DescriptionLabel, gridBagConstraints);
		
		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 60));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DescriptionScrollPane, gridBagConstraints);
		
		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RemarksLabel, gridBagConstraints);
		
		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(290, 78));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScrollPane.setViewportView(RemarksTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RemarksScrollPane, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(TransactionDateDate, gridBagConstraints);
		
		jPanel1_2_1_3.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_3.setPreferredSize(new java.awt.Dimension(410, 78));
		ExpSheetDiffLabel1.setText("Exp. Sheet Diff");
		ExpSheetDiffLabel1.setPreferredSize(new java.awt.Dimension(107, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_3.add(ExpSheetDiffLabel1, gridBagConstraints);
		
		jSeparator3.setPreferredSize(new java.awt.Dimension(200, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_3.add(jSeparator3, gridBagConstraints);
		
		CurrencyLabel2.setText("Currency");
		CurrencyLabel2.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1_3.add(CurrencyLabel2, gridBagConstraints);
		
		ESDiffRcvCurrText1.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_3.add(ESDiffRcvCurrText1, gridBagConstraints);
		
		ExchRateLabel2.setText("Exch. Rate*");
		ExchRateLabel2.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_3.add(ExchRateLabel2, gridBagConstraints);
		
		ESDiffRcvExchRateText1.setPreferredSize(new java.awt.Dimension(77, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_3.add(ESDiffRcvExchRateText1, gridBagConstraints);
		
		AmountLabel2.setText("Amount");
		AmountLabel2.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1_3.add(AmountLabel2, gridBagConstraints);
		
		ESDiffRcvAmountText1.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_3.add(ESDiffRcvAmountText1, gridBagConstraints);
		
		AmountBaseCurrLabel2.setText("Amount Base Curr.");
		AmountBaseCurrLabel2.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1_3.add(AmountBaseCurrLabel2, gridBagConstraints);
		
		ESDiffRcvAmountBaseText1.setPreferredSize(new java.awt.Dimension(140, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_3.add(ESDiffRcvAmountBaseText1, gridBagConstraints);
		
		ESDiffRcvAmountBaseCurrText1.setPreferredSize(new java.awt.Dimension(55, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_3.add(ESDiffRcvAmountBaseCurrText1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_2_1.add(jPanel1_2_1_3, gridBagConstraints);
		
		jPanel1_2_2.add(jPanel1_2_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(jPanel1_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.setLayout(new java.awt.BorderLayout());
		
		jPanel1_3.setPreferredSize(new java.awt.Dimension(700, 90));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());		
		
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(282, 110));
		jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.setLayout(new java.awt.BorderLayout());
		
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(282, 110));
		jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_3_2_2.setPreferredSize(new java.awt.Dimension(275,110));
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(282, 110));
		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);		
		
		add(jPanel1, java.awt.BorderLayout.NORTH);		
	}
	
	private void addingListener(){
		RcvToCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);	
		ESNoComp().addPropertyChangeListener("object",this);
		ReceiveFromComp.hapusActionListenerBrowse();
		ReceiveFromComp.m_browseBt.addActionListener(this);
		ESDiffExchRateText.addPropertyChangeListener(this);
		TransactionDateDate.addPropertyChangeListener("date", this);
	}
	
	
	protected void doNew() {
		super.doNew();	
		isiDefaultAssignPanel();
		clearForm();
		TransactionDateDate.setDate(new Date());
		revalidateReceiveFrom();
		revalidateReceiveFromByIndex(null);
	}
	
	
	public void actionPerformed(ActionEvent e) {		 	
		if(e.getSource() == RcvToCombo) {
			changeAccountComp();
		}
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new Rcpt_ESDiff(m_entity);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			String[] kolom = {"","UPPER(REFERENCENO)","TRANSACTIONDATE","EMPRECEIVED","UPPER(RECEIVETO)","UNIT","STATUS","SUBMITDATE"};
			String orderby = " ORDER BY " + kolom[2] + " ASC," + kolom[1] + " ASC";
			SearchReceiptDialog dlg = new SearchReceiptDialog(GumundaMainFrame.getMainFrame(), "Search Receipt", m_conn, m_sessionid, kolom,orderby,
					new SalesReceivedLoader(m_conn,RcvESDiff.class),"Expense Sheet Difference");
			dlg.setVisible(true);
			if (dlg.selectedObj != null){   
				doLoad(dlg.selectedObj);			
			}
		}
		if (e.getSource()==ReceiveFromComp.m_browseBt){
			ReceiveFromComp.done();
			Object obj =ReceiveFromComp.getObject(); 
			if(obj!=null){
				Employee emp = (Employee)obj;
				//long index = ;	
				/*if (obj instanceof ExpenseSheet) {
				 ExpenseSheet es = (ExpenseSheet) obj;
				 index=es.getEsOwner().getIndex();
				 
				 }else if (obj instanceof BeginningEsDiff) {
				 BeginningEsDiff temp1 = (BeginningEsDiff) obj;
				 index=temp1.getEmployee().getIndex();					
				 }				*/
				revalidateReceiveFromByIndex(emp);				
			}
		}
	}
	
	private void revalidateReceiveFrom() {
		jPanel1_2_1.remove(ReceiveFromComp);
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();
		ReceiveFromComp=new LookupExpRcvOwner(m_conn, m_sessionid);		  
		ReceiveFromComp.setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints = new java.awt.GridBagConstraints();		
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveFromComp, gridBagConstraints);
		jPanel1_2_1.revalidate();		
		
		LookupExpRcvOwner old = ReceiveFromComp;		
		if (old!=null){
			ReceiveFromComp.hapusActionListenerBrowse();			
		}
		ReceiveFromComp.m_browseBt.addActionListener(this);		
	}
	
	
	private void revalidateReceiveFromByIndex(Employee employee) {
		jPanel1_2_1.remove(ESNoComp());
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();
		setESNoComp(new LookupExpenseSheetNoPicker(m_conn, m_sessionid,employee,LookupExpenseSheetNoPicker.RECEIVE));  
		ESNoComp().setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ESNoComp(), gridBagConstraints);
		jPanel1_2_1.revalidate();
		
		setGuiPayTo();
	}
	
	private void changeAccountComp() {
		Object obj = ESNoComp.getObject();
		String receive = (String)RcvToCombo.getSelectedItem();
		System.out.println(receive);
		jPanel1_2_1.remove(AccountComp);
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();
		if (obj instanceof ExpenseSheet) {
			ExpenseSheet temp = (ExpenseSheet) obj;
			if(receive.equals("Cash"))
				if (temp!=null) 
					AccountComp= new LookupCashAccountPicker(m_conn, m_sessionid,temp.getCurrency());
				else
					AccountComp= new LookupCashAccountPicker(m_conn, m_sessionid);			
			else
				if(temp!=null)
					AccountComp= new LookupBankAccountPicker(m_conn, m_sessionid,temp.getCurrency());
				else
					AccountComp= new LookupBankAccountPicker(m_conn, m_sessionid);
			
		}
		else if (obj instanceof BeginningEsDiff) {
			BeginningEsDiff temp = (BeginningEsDiff) obj;
			if(receive.equals("Cash"))
				if (temp!=null) 
					AccountComp= new LookupCashAccountPicker(m_conn, m_sessionid,temp.getCurrency());
				else
					AccountComp= new LookupCashAccountPicker(m_conn, m_sessionid);			
			else
				if(temp!=null)
					AccountComp= new LookupBankAccountPicker(m_conn, m_sessionid,temp.getCurrency());
				else
					AccountComp= new LookupBankAccountPicker(m_conn, m_sessionid);
		}
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);
		jPanel1_2_1.revalidate();			
	}
	
	private void enableAwal(){ 	
		setenableEditPanel(jPanel1_2_1,false);
		setenableEditPanel(jPanel1_2_1_1,false);
		setenableEditPanel(jPanel1_2_1_2,false);
		setenableEditPanel(jPanel1_2_2_1,false);
		setenableEditPanel(jPanel1_2_2_1_2,false);
		setenableEditPanel(jPanel1_2_1_3,false);
		ESDiffCurrText.setEnabled(false);
	}
	
	protected void enableEditMode()	{  
		ESDateText.setEditable(false);
		RcvToCombo.setEnabled(true);
		AccountComp.setEnabled(true);
		ESNoComp.setEnabled(true);
		TransactionDateDate.setEditable(true);
		ESDiffAmountText.setEnabled(true);
		DescriptionTextArea.setEnabled(true);
		RemarksTextArea.setEnabled(true);	
		OriginatorComp.setEnabled(true);
		ApprovedComp.setEnabled(true);
		ReceivedComp.setEnabled(true);
		ReceiveFromComp.setEnabled(true);
		if(entity().getCurrency()!=null)
			if(!entity().getCurrency().getSymbol().equalsIgnoreCase(baseCurrency.getSymbol()))
				ESDiffExchRateText.setEditable(true);
		ESDiffAmountText.setEditable(false);
	}
	
	protected void disableEditMode(){
		ESDateText.setEditable(false);
		RcvToCombo.setEnabled(false);
		AccountComp.setEnabled(false);
		ESNoComp.setEnabled(false);
		TransactionDateDate.setEditable(false);
		ESDiffAmountText.setEnabled(false);
		DescriptionTextArea.setEnabled(false);
		RemarksTextArea.setEnabled(false);	
		OriginatorComp.setEnabled(false);
		ApprovedComp.setEnabled(false);
		ReceivedComp.setEnabled(false);
		ReceiveFromComp.setEnabled(false);
		ESDiffExchRateText.setEditable(false);
		ESDiffAmountText.setEditable(false);
		
	}	
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (ReceiveFromComp.getObject()==null)
			addInvalid("Receive from must be selected");
		if (AccountComp.getObject()== null)
			addInvalid("Account component must be selected");
		if (ESNoComp.getObject() == null)
			addInvalid("Expense Sheet No must be selected");
		if (ESDiffAmountText.getText().compareTo("")==0)
			addInvalid("Amount Expense Sheet Difference must be filled");
		if (TransactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (DescriptionTextArea.getText().compareTo("")==0)
			addInvalid("Description Text Area must be filled");
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
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	RcvESDiff entity() {
		return m_entity;
	}
	
	String m_bank = "BANK";
	String m_cash = "CASH";
	protected void gui2entity() { 
		double xcrate;
		Number obj=(Number)ESDiffExchRateText.getValue();
		if (obj==null)
			xcrate=1;
		else
			xcrate=obj.doubleValue();
		entity().transTemplateRead(
				this.OriginatorComp,this.ApprovedComp,
				this.ReceivedComp,this.RefNoText.getText(),
				this.DescriptionTextArea.getText(), this.RemarksTextArea.getText(),
				this.TransactionDateDate.getDate(),xcrate);
		Object selItem = RcvToCombo.getSelectedItem();
		Object object =AccountComp.getObject();
		if (m_bank.equalsIgnoreCase((String) selItem)){
			entity().setPaymentSource(m_bank);
			entity().setBankAccount((BankAccount) object);
			entity().setUnit(((BankAccount) object).getUnit());
			entity().setCashAccount(null);
		}else{   
			entity().setPaymentSource(m_cash);
			entity().setCashAccount((CashAccount) object);
			entity().setUnit(((CashAccount) object).getUnit());
			entity().setBankAccount(null);
		}		
		Object obj1 = ESNoComp.getObject();
		if (obj1 instanceof ExpenseSheet) {
			ExpenseSheet temp = (ExpenseSheet) obj1;
			entity().setEsNo(temp);			
		}else if (obj1 instanceof BeginningEsDiff) {
			BeginningEsDiff temp = (BeginningEsDiff) obj1;
			entity().setBeginningBalance(temp);
		}		
		Object amtobj = ESDiffAmountText.getValue();
		Number amount = (Number) amtobj;
		if (amount!=null)
			entity().setAmount(amount.doubleValue());
		entity().setCurrency((Currency)ESDiffCurrText.getObject());
		
	}
	
	protected  void entity2gui(){
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getTransactionDate()!=null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else{
			TransactionDateDate.setDate(new Date());
		}		
		
		if (entity().getEsNo()!=null){			
			changeAccountComp();
			revalidateReceiveFromByIndex(entity().getEsNo().getEsOwner());
			ESNoComp().setObject(entity().getEsNo());
			ReceiveFromComp.setObject(entity().getEsNo().getEsOwner());
		}else if (entity().getBeginningBalance()!=null){
			changeAccountComp();
			revalidateReceiveFromByIndex(entity().getBeginningBalance().getEmployee());	
			ESNoComp().setObject(entity().getBeginningBalance());	
			ReceiveFromComp.setObject(entity().getBeginningBalance().getEmployee());				
		}else{
			ESNoComp().setObject(null);
			ReceiveFromComp.setObject(null);
		}
		
		if (entity().isBank()){			
			RcvToCombo.setSelectedItem(m_bank);		
			BankAccount bankAcct = entity().getBankAccount();			
			AccountComp.setObject(bankAcct);
		}else{
			RcvToCombo.setSelectedItem(m_cash);
			CashAccount cashAcct = entity().getCashAccount();
			AccountComp.setObject(cashAcct);
		}
		
		RefNoText.setText(entity().getReferenceNo());		
		String strDate = entity().getSubmitDate() == null ? "" :  dateformat.format(entity().getSubmitDate());
		SubmittedDateText.setText(strDate);
		Double amount = new Double(entity().getAmount());
		ESDiffAmountText.setValue(amount);
		DescriptionTextArea.setText(entity().getDescription());
		RemarksTextArea.setText(entity().getRemarks());
		DescriptionTextArea.setText(entity().getDescription());
		RemarksTextArea.setText(entity().getRemarks());
		OriginatorComp.setEmployee(entity().getEmpOriginator());
		OriginatorComp.setDate(entity().getDateOriginator());    	
		ApprovedComp.setEmployee(entity().getEmpApproved());
		ApprovedComp.setDate(entity().getDateApproved());    	
		ReceivedComp.setEmployee(entity().getEmpReceived());
		ReceivedComp.setDate(entity().getDateReceived());
		OriginatorComp.setJobTitle(entity().getJobTitleOriginator());
		ApprovedComp.setJobTitle(entity().getJobTitleApproved());
		ReceivedComp.setJobTitle(entity().getJobTitleReceived());
		StatusText.setText(entity().statusInString());
	}
	
	RcvESDiff oldEntity;
	void setEntity(Object m_entity) {	
		oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (RcvESDiff)m_entity;
		this.m_entity.addPropertyChangeListener(this);
		readEntityState();
		if(this.m_entity.getBeginningBalance()!=null){
			setBeginningBalance();
		}
	}
	
	private void setBeginningBalance() {
		BeginningEsDiff tempClass = this.m_entity.getBeginningBalance();
		BeginningBalanceBusinessLogic logic = new BeginningBalanceBusinessLogic(m_conn, m_sessionid);
		EmployeePicker helpEmp=new EmployeePicker(m_conn,m_sessionid);		
		Unit unit = helpEmp.findUnitEmployee(tempClass.getEmployee());
		if (unit==null){				
			UnitPicker unitpicker = new UnitPicker();
			unitpicker.init(m_conn, m_sessionid);				
			unit = unitpicker.getDefaultUnit();
		}
		if (unit!=null){
			tempClass.setTrans(logic.findTransaction(unit));
			tempClass.showReferenceNo(true);		
			this.m_entity.setBeginningBalance(tempClass);
		}
	}
	
	protected Object createNew(){ 
		RcvESDiff a= new RcvESDiff();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a;		
	}
	
	
	protected void clearAll() {
		super.clearAll();
		clearForm();
		disableEditMode();		
	}
	
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
	}
	
	private LookupPicker ESNoComp() {
		return ESNoComp;
	}
	
	protected void setESNoComp(LookupPicker accountComp) {
		LookupPicker old = ESNoComp;
		if (old!=null){
			old.removePropertyChangeListener("object",this);
		}
		ESNoComp = accountComp;
		ESNoComp.addPropertyChangeListener("object",this);
	}	
	
	public void propertyChange(PropertyChangeEvent evt) {		
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		if ("object".equals(evt.getPropertyName())) {
			if (evt.getSource() == ESNoComp) {
				Object obj = ESNoComp.getObject();
				if (obj != null) {
					if (obj instanceof ExpenseSheet) {
						setGuiExpenseSheet(obj);
					} else if (obj instanceof BeginningEsDiff) {
						setGuiBeginningBalance(obj);
					}
					changeAccountComp();
				}
			}
		}
		if ("value".equals(evt.getPropertyName())) {
			if (evt.getSource() == ESDiffExchRateText) {
				inBaseCurrency();
			}
		}
		if ("date".equals(evt.getPropertyName())) {
			if (evt.getSource() == TransactionDateDate) {
				Object obj = ESNoComp.getObject();
				if (obj != null) {
					if (obj instanceof ExpenseSheet) {
						setGuiExpenseSheet(obj);
					} else if (obj instanceof BeginningEsDiff) {
						setGuiBeginningBalance(obj);
					}
				}
			}
		}
	}
	
	private void inBaseCurrency() {
		Number exchRateNbr = (Number)ESDiffExchRateText.getValue();
		double exchRate = 1;
		if(exchRateNbr!=null)
			 exchRate = exchRateNbr.doubleValue();
		Number amountNbr = (Number)ESDiffAmountText.getValue();
		double amount = 0;
		if(amountNbr!=null)
			amount = amountNbr.doubleValue();
		double base = amount * exchRate;
		ESDiffAmountBaseText.setValue(new Double(base));
	}

	private void setGuiPayTo() {
		UnitCodeText.setText(null);
		DepartmentText.setText(null);
		ESTypeText.setText(null);		
		TotExpensesCurrText.setText(null);
		TotExpensesAmountText.setText(null);
		ESDateText.setDate(null);
		ESDiffCurrText.setObject(null);
		ESDiffExchRateText.setValue(null);		
		ESDiffAmountText.setValue(null);
		ESDiffAmountBaseCurrText.setText(null);
		ESDiffAmountBaseText.setValue(null);		
		ESDiffRcvCurrText1.setText(null);
		ESDiffRcvExchRateText1.setValue(null);
		ESDiffRcvAmountText1.setValue(null);
		ESDiffRcvAmountBaseCurrText1.setText(null);
		ESDiffRcvAmountBaseText1.setValue(null);	
	}
	
	private void setGuiBeginningBalance(Object obj) {		
		BeginningEsDiff temp = (BeginningEsDiff) obj;
		EmployeePicker helpEmp=new EmployeePicker(m_conn,m_sessionid);
		Unit unit = helpEmp.findUnitEmployee(temp.getEmployee());
		if (unit==null){				
			UnitPicker unitpicker = new UnitPicker();
			unitpicker.init(this.m_conn, this.m_sessionid);				
			unit = unitpicker.getDefaultUnit();			
		}		
		if (unit!=null)
			UnitCodeText.setText(unit.toString());
		DepartmentText.setText(helpEmp.getDepartment());
		ESTypeText.setText("Beginning Balance");
		ESDateText.setDate(temp.getTrans().getTransDate());
		ESDiffCurrText.setObject(temp.getCurrency());
		if (!temp.getCurrency().getSymbol().equalsIgnoreCase(baseCurrency.getSymbol())){
			ESDiffExchRateText.setEditable(true);
			if (entity().getExchangeRate()==0)
				setDefaultExchangeRate(temp.getCurrency());
			else
				ESDiffExchRateText.setValue(new Double(entity().getExchangeRate()));			
		}else{
			ESDiffExchRateText.setEditable(false);
			ESDiffExchRateText.setValue(new Double(1));
		}
		
		//ESDiffExchRateText.setValue(new Double(temp.getExchangeRate()));
		double value = temp.getAccValue()*temp.getExchangeRate();
		ESDiffAmountText.setValue(new Double(temp.getAccValue()));
		ESDiffAmountBaseCurrText.setText(baseCurrency.getSymbol());
		ESDiffAmountBaseText.setValue(new Double(value));		
		ESDiffRcvCurrText1.setText(temp.getCurrency().getSymbol());
		ESDiffRcvExchRateText1.setValue(new Double(temp.getExchangeRate()));
		ESDiffRcvAmountText1.setValue(new Double(temp.getAccValue()));
		ESDiffRcvAmountBaseCurrText1.setText(baseCurrency.getSymbol());
		ESDiffRcvAmountBaseText1.setValue(new Double(value));
		
		
	}
	
	private void setGuiExpenseSheet(Object obj) {
		ExpenseSheet temp = (ExpenseSheet) obj;
		AccountComp.setObject(null);
		ESTypeText.setText(temp.getEsProjectType());
		ESDateText.setDate(temp.getTransactionDate());		
		if(temp.getProject()!=null){
			ProjectData temp1=temp.getProject();
			UnitCodeText.setText(temp1.getUnit().toString());
			if (temp1.getDepartment()!=null)
				DepartmentText.setText(temp1.getDepartment().getDescription());
			else
				DepartmentText.setText("");
		} 
		else { 
			if (temp.getPmtCaOthers()!=null){
				UnitCodeText.setText(temp.getPmtCaOthers().getUnit().toString()); 
				if (temp.getPmtCaOthers().getDepartment()!=null)
					DepartmentText.setText(temp.getPmtCaOthers().getDepartment().getDescription());   
				else 
					DepartmentText.setText("");
			}
			else if (temp.getPmtCaIouOthersSettled()!=null){ 
				UnitCodeText.setText(temp.getPmtCaIouOthersSettled().getPmtcaiouothers().getUnit().toString());
				if (temp.getPmtCaIouOthersSettled().getPmtcaiouothers().getDepartment()!=null)
					DepartmentText.setText(temp.getPmtCaIouOthersSettled().getPmtcaiouothers().getDepartment().getDescription());
				else
					DepartmentText.setText("");
			}
			else if (temp.getBeginningBalance()!=null){
				BeginningCashAdvance bca = temp.getBeginningBalance();
				BeginningESBusinessLogic logic = new BeginningESBusinessLogic(m_conn, m_sessionid);
				logic.getTransaction(bca);
				UnitCodeText.setText(bca.getTrans().getUnit().toString());
				DepartmentText.setText("");
			}
		}
		TotExpensesAmountText.setValue(new Double(temp.getAmount()));
		TotExpensesCurrText.setText(temp.getCurrency().getSymbol());
		PmtCAProject pmt1=null;
		PmtCAIOUProjectSettled pmt2=null;
		PmtCAOthers pmt3=null;
		PmtCAIOUOthersSettled pmt4=null;
		String cashadv = null,tgl = null;
		double amount1 = 0,amount2,xcrateCA = 0,xcrateEX;
		amount2=temp.getAmount();
		xcrateEX=temp.getExchangeRate();
		String a=temp.getEsProjectType();
		if (a.equalsIgnoreCase("General")){
			if (temp.getPmtCaProject()!=null){
				pmt1=temp.getPmtCaProject();
				amount1=pmt1.getAmount();
				cashadv=pmt1.getReferenceNo();
				tgl=pmt1.getTransactionDate().toString();
				xcrateCA=pmt1.getExchangeRate();
			}else if (temp.getPmtCaOthers()!=null) {
				pmt3=temp.getPmtCaOthers();
				amount1=pmt3.getAmount();
				cashadv=pmt3.getReferenceNo();
				tgl=pmt3.getTransactionDate().toString();
				xcrateCA=pmt3.getExchangeRate();}
		}
		else if (a.equalsIgnoreCase("IOU")) {
			if (temp.getPmtCaIouProjectSettled()!=null){
				pmt2=temp.getPmtCaIouProjectSettled();
				amount1=pmt2.getAmount();
				cashadv=pmt2.getReferenceNo();
				tgl=pmt2.getTransactionDate().toString();
				xcrateCA=pmt2.getExchangeRate();
			}else if (temp.getPmtCaIouOthersSettled()!=null){
				pmt4=temp.getPmtCaIouOthersSettled();
				amount1=pmt4.getAmount();
				cashadv=pmt4.getReferenceNo();
				tgl=pmt4.getTransactionDate().toString();
				xcrateCA=pmt4.getExchangeRate();
			}					
		} 
		else if (a.equalsIgnoreCase("Beginning Balance")){
			if (temp.getBeginningBalance()!=null){
				BeginningCashAdvance bca = temp.getBeginningBalance();
				amount1 = bca.getAccValue();
				if(bca.getTrans()==null){
					BeginningESBusinessLogic logic = new BeginningESBusinessLogic(m_conn, m_sessionid);
					logic.getTransaction(bca);
				}
				cashadv = bca.getTrans().getReference();
				tgl = bca.getTrans().getTransDate().toString();
				xcrateCA = bca.getExchangeRate();
			}
		}
		CashAdvNoText.setText(cashadv);
		CashAdvDateText.setText(tgl);
		CashAdvAmountText.setValue(new Double(amount1));
		CashAdvAmountCurrText.setText(temp.getCurrency().getSymbol());
		ESDiffRcvCurrText1.setText(temp.getCurrency().getSymbol());
		if (xcrateCA==0)
			xcrateCA=1;
		if (xcrateEX==0)
			xcrateEX=1;
		ESDiffExchRateText.setValue(new Double(xcrateEX)); // benarkah?
		ESDiffRcvExchRateText1.setValue(new Double(xcrateEX));		
		double selisih=amount1-amount2;
		double selisihbase=selisih*xcrateEX;
		ESDiffRcvAmountText1.setValue(new Double(selisih));
		ESDiffRcvAmountBaseCurrText1.setText(baseCurrency.getSymbol());
		ESDiffAmountBaseCurrText.setText(baseCurrency.getSymbol()); // benarkah?
		ESDiffRcvAmountBaseText1.setValue(new Double(selisihbase));		
		ESDiffAmountBaseText.setValue(new Double(selisihbase)); // benarkah?
		ESDiffCurrText.setObject(temp.getCurrency());
		if (!temp.getCurrency().getSymbol().equalsIgnoreCase(baseCurrency.getSymbol())){
			ESDiffExchRateText.setEditable(true);
			if (entity().getExchangeRate()==0)
				setDefaultExchangeRate(temp.getCurrency());
			else
				ESDiffExchRateText.setValue(new Double(entity().getExchangeRate()));			
		}
		ESDiffAmountText.setValue(new Double(selisih));
		
		inBaseCurrency();
	}
	
	public void clearForm(){
		clearTextField(jPanel1_2_2_1);
		clearTextField(jPanel1_2_1_2);
		clearTextField(jPanel1_2_1_3);
		clearTextField(jPanel1_2_2);	
		clearTextField(jPanel1_2_1_1);
		ReceiveFromComp.setObject(null);
		StatusText.setText("");
		OriginatorComp.setEmployee(null);
		ApprovedComp.setEmployee(null);
		ReceivedComp.setEmployee(null);
		OriginatorComp.m_jobTextField.setText("");
		ApprovedComp.m_jobTextField.setText("");
		ReceivedComp.m_jobTextField.setText("");
		OriginatorComp.setDate(null);
		ApprovedComp.setDate(null);
		ReceivedComp.setDate(null);
		ESTypeText.setText(null);
		ESDateText.setDate(null);
		UnitCodeText.setText(null);
		DepartmentText.setText(null);
		ESDiffCurrText.setObject(null);
		CashAdvAmountCurrText.setText(null);
		CashAdvAmountText.setValue(null);	
		TransactionDateDate.setDate(null);
	}
	
	protected void clearTextField(JPanel temppanel) { 
		Component[] componentList=temppanel.getComponents();
		JTextField temptext;
		for (int i=0;i<componentList.length;i++){
			if (componentList[i] instanceof JTextField){
				temptext=(JTextField)componentList[i];
				temptext.setText(""); 
			}
		} 	
	}
	
	protected void doSave() {
		if (!cekValidity()) return;
		super.doSave();
		AccountingSQLSAP sql=new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_RCV_ES_DIFF, m_conn);
				entity().setIndex(index);
			}			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}	
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_EXPENSE_SHEET_DIFFERENCE, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_EXPENSE_SHEET_DIFFERENCE, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_EXPENSE_SHEET_DIFFERENCE, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	
	private LookupPicker AccountComp;
	private javax.swing.JLabel AccountLabel;
	private javax.swing.JLabel AmountBaseCurrLabel1;
	private javax.swing.JLabel AmountBaseCurrLabel2;
	private javax.swing.JLabel AmountLabel1;
	private javax.swing.JLabel AmountLabel2;
	private javax.swing.JTextField CashAdvAmountCurrText;
	private javax.swing.JLabel CashAdvAmountLabel;
	private javax.swing.JFormattedTextField CashAdvAmountText;
	private javax.swing.JLabel CashAdvDateLabel;
	private javax.swing.JTextField CashAdvDateText;
	private javax.swing.JLabel CashAdvNoLabel;
	private javax.swing.JTextField CashAdvNoText;
	private javax.swing.JLabel CurrencyLabel1;
	private javax.swing.JLabel CurrencyLabel2;
	private javax.swing.JLabel DepartmentLabel;
	private javax.swing.JTextField DepartmentText;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private DatePicker ESDateText;
	private javax.swing.JTextField ESDiffAmountBaseCurrText;
	private javax.swing.JFormattedTextField ESDiffAmountBaseText;
	private javax.swing.JFormattedTextField ESDiffAmountText;
	private CurrencyPicker ESDiffCurrText;
	private javax.swing.JFormattedTextField ESDiffExchRateText;
	private javax.swing.JTextField ESDiffRcvAmountBaseCurrText1;
	private javax.swing.JFormattedTextField ESDiffRcvAmountBaseText1;
	private javax.swing.JFormattedTextField ESDiffRcvAmountText1;
	private javax.swing.JTextField ESDiffRcvCurrText1;
	private javax.swing.JFormattedTextField ESDiffRcvExchRateText1;
	private LookupPicker ESNoComp;
	private javax.swing.JTextField ESTypeText;
	/*private javax.swing.JTextField EndBalanceAmountText;
	 private javax.swing.JTextField EndBalanceCurrText;*/
	//private javax.swing.JLabel EndingBalanceLabel;
	private javax.swing.JLabel ExchRateLabel1;
	private javax.swing.JLabel ExchRateLabel2;
	private javax.swing.JLabel ExpSheetDiffLabel;
	private javax.swing.JLabel ExpSheetDiffLabel1;
	private javax.swing.JLabel ExpenseSheetDateLabel;
	private javax.swing.JLabel ExpenseSheetNoLabel;
	private javax.swing.JLabel ExpenseSheetTypeLabel;
	private javax.swing.JLabel ReceiptDateLabel;
	private javax.swing.JLabel ReceiptNoLabel;
	private javax.swing.JComboBox RcvToCombo;
	private javax.swing.JLabel ReceiveToLabel;
	private javax.swing.JTextField RefNoText;;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScrollPane;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JTextField SubmittedDateText;
	private javax.swing.JFormattedTextField TotExpensesAmountText;
	private javax.swing.JTextField TotExpensesCurrText;
	private javax.swing.JLabel TotalExpensesLabel;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JLabel UnitCodeLabel;
	private javax.swing.JTextField UnitCodeText;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel TopButtonPanel;
	private javax.swing.JPanel jPanel1_2;
	private javax.swing.JPanel jPanel1_2_1;
	private javax.swing.JPanel jPanel1_2_1_1;
	private javax.swing.JPanel jPanel1_2_1_2;
	private javax.swing.JPanel jPanel1_2_1_3;
	private javax.swing.JPanel jPanel1_2_2;
	private javax.swing.JPanel jPanel1_2_2_1;
	private javax.swing.JPanel jPanel1_2_2_1_2;
	private javax.swing.JPanel jPanel1_3;
	private AssignPanel OriginatorComp;
	private javax.swing.JPanel jPanel1_3_2;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel jPanel1_3_2_2;
	private AssignPanel ReceivedComp;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator3;
	
}
