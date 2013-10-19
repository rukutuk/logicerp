package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEmpReceivable;
import pohaci.gumunda.titis.accounting.cgui.report.Rcpt_EmpReceivable;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.RcvEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.SalesReceivedLoader;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.BeginningBalanceBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import java.awt.Component;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class ReceiveEmployeeReceivablePanel 
extends RevTransactionPanel implements ActionListener, PropertyChangeListener{
	
	private static final long serialVersionUID = 1L;
	private RcvEmpReceivable m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	
	public ReceiveEmployeeReceivablePanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(conn, sessionid);
		initExchangeRateHelper(conn, sessionid);
		setDefaultSignature(); 
		setEntity(new RcvEmpReceivable());
		m_entityMapper = MasterMap.obtainMapperFor(RcvEmpReceivable.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}

	private javax.swing.JLabel labelReceiveFrom=new javax.swing.JLabel("Receive From");
	private LookupEmpRcvOwner EmpRcvOwnerComp;
	
	private void setDefaultExchangeRate(Currency currency) {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		EmpARRcvExchRateText.setValue(new Double(rate));
	}

	private void initComponents() {
		EmpRcvOwnerComp=new LookupEmpRcvOwner(m_conn, m_sessionid);
		AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid);
		EmpARRcvCurrComp = new CurrencyPicker(m_conn, m_sessionid);
		EmpReceivableComp = new LookupEmployeeRcvPicker(m_conn, m_sessionid, null);
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
		InitialLoanLabel = new javax.swing.JLabel();
		AccumulatedReceiveLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		RcvToCombo = new javax.swing.JComboBox();
		jPanel1_2_1_2 = new javax.swing.JPanel();
		InitialLoanCurrText = new javax.swing.JTextField();
		EmpReceivableLabel = new javax.swing.JLabel();
		InitialLoanAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
        InitialLoanXRateText=new javax.swing.JFormattedTextField(m_numberFormatter);
        InitialLoanXRateLabel=new JLabel();
		jPanel1_2_1_3 = new javax.swing.JPanel();
		AccumulatedRcvCurrText = new javax.swing.JTextField();
		AccumulatedRcvAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AccumulatedRcvBtn= new JButton();
		jPanel1_2_1_1 = new javax.swing.JPanel();
		ExpSheetDiffLabel = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		CurrencyLabel = new javax.swing.JLabel();
		ExchRateLabel = new javax.swing.JLabel();
		EmpARRcvExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountLabel = new javax.swing.JLabel();
		EmpARRcvAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountBaseCurrLabel = new javax.swing.JLabel();
		EmpARRcvAmountBaseText = new javax.swing.JFormattedTextField(m_numberFormatter);
		EmpARRcvAmountBaseCurrText = new javax.swing.JTextField();
		TotalReceiveLabel = new javax.swing.JLabel();
		TotRcvAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		TotRcvCurrText = new javax.swing.JTextField();
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_2_1 = new javax.swing.JPanel();
		ReceiptNoLabel = new javax.swing.JLabel();
		ReceiptDateLabel = new javax.swing.JLabel();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		RefNoText = new javax.swing.JTextField();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		
		setLayout(new java.awt.BorderLayout());
		
		setPreferredSize(new java.awt.Dimension(950, 400));
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		jPanel1.setPreferredSize(new java.awt.Dimension(700, 400));
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
		
		jPanel1_2.setPreferredSize(new java.awt.Dimension(700, 380));
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
		
		ReceiveToLabel.setText("Receive To*");
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveToLabel, gridBagConstraints);
		
		AccountLabel.setText("Account*");
		AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountLabel, gridBagConstraints);
		
		InitialLoanLabel.setText("Initial Loan");
		InitialLoanLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(InitialLoanLabel, gridBagConstraints);
		
		AccumulatedReceiveLabel.setText("Accumulated Receive");
		AccumulatedReceiveLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccumulatedReceiveLabel, gridBagConstraints);
		
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
		
		RcvToCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash","Bank"}));
		RcvToCombo.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(RcvToCombo, gridBagConstraints);
		
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);
		
		labelReceiveFrom.setText("Employee Name");
		labelReceiveFrom.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(labelReceiveFrom, gridBagConstraints);

		EmpRcvOwnerComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(EmpRcvOwnerComp, gridBagConstraints);
		
		EmpReceivableLabel.setText("Emp. Receivable");
		EmpReceivableLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(EmpReceivableLabel, gridBagConstraints);
		
		EmpReceivableComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(EmpReceivableComp, gridBagConstraints);
		
		jPanel1_2_1_2.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_2.setPreferredSize(new java.awt.Dimension(290, 20));
		InitialLoanCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(InitialLoanCurrText, gridBagConstraints);
		
		InitialLoanAmountText.setPreferredSize(new java.awt.Dimension(120, 18));//237, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		//gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(InitialLoanAmountText, gridBagConstraints);
		
		InitialLoanXRateLabel.setText("Exch. Rate");
		InitialLoanXRateLabel.setPreferredSize(new java.awt.Dimension(62, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_1_2.add(InitialLoanXRateLabel, gridBagConstraints);
		
		
		InitialLoanXRateText.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(InitialLoanXRateText, gridBagConstraints);
			
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(jPanel1_2_1_2, gridBagConstraints);
		
		jPanel1_2_1_3.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_3.setPreferredSize(new java.awt.Dimension(290, 20));
		AccumulatedRcvCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_3.add(AccumulatedRcvCurrText, gridBagConstraints);
		
		AccumulatedRcvAmountText.setPreferredSize(new java.awt.Dimension(187, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_3.add(AccumulatedRcvAmountText, gridBagConstraints);
		
		AccumulatedRcvBtn.setText("Detail");
		AccumulatedRcvBtn.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_3.add(AccumulatedRcvBtn, gridBagConstraints);
		
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(jPanel1_2_1_3, gridBagConstraints);
		
		jPanel1_2_1_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(410, 100));
		ExpSheetDiffLabel.setText("Empoyee AR Receive");
		ExpSheetDiffLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ExpSheetDiffLabel, gridBagConstraints);
		
		jSeparator1.setPreferredSize(new java.awt.Dimension(200, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(jSeparator1, gridBagConstraints);
		
		CurrencyLabel.setText("Currency*");
		CurrencyLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(CurrencyLabel, gridBagConstraints);
		
		ExchRateLabel.setText("Exch. Rate*");
		ExchRateLabel.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ExchRateLabel, gridBagConstraints);
		
		EmpARRcvExchRateText.setPreferredSize(new java.awt.Dimension(137, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(EmpARRcvExchRateText, gridBagConstraints);
		
		AmountLabel.setText("Amount*");
		AmountLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountLabel, gridBagConstraints);
		
		EmpARRcvAmountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(EmpARRcvAmountText, gridBagConstraints);
		
		AmountBaseCurrLabel.setText("Amount Base Curr.");
		AmountBaseCurrLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountBaseCurrLabel, gridBagConstraints);
		
		EmpARRcvAmountBaseText.setPreferredSize(new java.awt.Dimension(201, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(EmpARRcvAmountBaseText, gridBagConstraints);
		
		EmpARRcvAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(EmpARRcvAmountBaseCurrText, gridBagConstraints);
		
		TotalReceiveLabel.setText("Total Receive");
		TotalReceiveLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(TotalReceiveLabel, gridBagConstraints);
		
		TotRcvAmountText.setPreferredSize(new java.awt.Dimension(201, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(TotRcvAmountText, gridBagConstraints);
		
		TotRcvCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(TotRcvCurrText, gridBagConstraints);
		
		//  EmpARRcvCurrCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		EmpARRcvCurrComp.setPreferredSize(new java.awt.Dimension(80, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		jPanel1_2_1_1.add(EmpARRcvCurrComp, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(jPanel1_2_1_1, gridBagConstraints);
		
		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(325, 400));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(415, 400));
		ReceiptNoLabel.setText("Receipt No");
		ReceiptNoLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ReceiptNoLabel, gridBagConstraints);
		
		ReceiptDateLabel.setText("Receipt Date*");
		ReceiptDateLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ReceiptDateLabel, gridBagConstraints);
		
		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DescriptionLabel, gridBagConstraints);
		
		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 115));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DescriptionScrollPane, gridBagConstraints);
		
		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RemarksLabel, gridBagConstraints);
		
		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(290, 100));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScrollPane.setViewportView(RemarksTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 5, 1);
		jPanel1_2_2_1.add(RemarksScrollPane, gridBagConstraints);
		
		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(TransactionDateDate, gridBagConstraints);
		
		jPanel1_2_2.add(jPanel1_2_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(jPanel1_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.setLayout(new java.awt.BorderLayout());
		
		jPanel1_3.setPreferredSize(new java.awt.Dimension(700, 90));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());
		
		OriginatorComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(275, 110));
		
		jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.setLayout(new java.awt.BorderLayout());
		
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		
		ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(275, 110));
		
		jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_3_2_2.setPreferredSize(new java.awt.Dimension(275, 110));
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		
		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(275, 110));
		
		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);
		
		//jSplitPane1.setRightComponent(jPanel1);
		
		add(jPanel1, java.awt.BorderLayout.NORTH);
		
	}
	
	protected void setAccountComp(LookupPicker accountComp) {
		LookupPicker old = AccountComp;
		if (old!=null){
			old.removePropertyChangeListener("object",this);
		}
		AccountComp = accountComp;
		AccountComp.addPropertyChangeListener("object",this);
	}
	
	private LookupPicker accountComp() {
		return AccountComp;
	}
	
	private void addingListener(){
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);	
		RcvToCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		accountComp().addPropertyChangeListener("object",this);
		/*EmpReceivableComp.hapusActionListenerBrowse();
		EmpReceivableComp.m_browseBt.addActionListener(this);*/
		EmpReceivableComp.addPropertyChangeListener("object",this);
		EmpARRcvAmountText.addPropertyChangeListener(this);
		EmpARRcvExchRateText.addPropertyChangeListener(this);
		//EmpARRcvExchRateText.getDocument().addDocumentListener(this);
		AccumulatedRcvBtn.addActionListener(this);
		EmpRcvOwnerComp.addPropertyChangeListener("object",this);
		TransactionDateDate.addPropertyChangeListener("date", this);
	}
	
	
	protected void doNew() {
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
	}

	public void actionPerformed(ActionEvent e) {	
		if(e.getSource() == RcvToCombo) {
			changeAccountComp();						
		}
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0){
				Employee emp = (Employee)EmpRcvOwnerComp.getObject();
				new Rcpt_EmpReceivable(m_entity,m_conn,m_sessionid,emp);		
			}else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			String[] kolom = {"","UPPER(REFERENCENO)","TRANSACTIONDATE","EMPRECEIVED","UPPER(RECEIVETO)","UNIT","STATUS","SUBMITDATE"};
			String orderby = " ORDER BY " + kolom[2] + " ASC," + kolom[1] + " ASC";
			SearchReceiptDialog dlg = new SearchReceiptDialog(GumundaMainFrame.getMainFrame(), "Search Receipt", m_conn, m_sessionid,kolom,orderby,
					new SalesReceivedLoader(m_conn,RcvEmpReceivable.class) ,"Employee Receivable");
			dlg.setVisible(true);
			if (dlg.selectedObj != null){
				doLoad(dlg.selectedObj);			
			}
		}	
	
		else if (e.getSource()==AccumulatedRcvBtn){ 
			if (EmpReceivableComp.getObject()!=null){
				Object obj = EmpReceivableComp.getObject();
				LookupEmpReceivableDetailPicker dlg = null;
				dlg = new LookupEmpReceivableDetailPicker(m_conn,m_sessionid,obj);	
				dlg.done1();
				dlg.setVisible(true);
				
			}else{
				JOptionPane.showMessageDialog(this,"Please select Voucher on employee receivable first");
			}
		}
	}

	public void changeAccountComp() {
		Object obj = EmpReceivableComp.getObject();
		jPanel1_2_1.remove(accountComp());
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();
		String receive = (String)RcvToCombo.getSelectedItem();
		
		if (obj instanceof PmtEmpReceivable) {
			PmtEmpReceivable temp = (PmtEmpReceivable) obj;								
			if(receive.equals("Cash"))
				if (temp!=null) 
					setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid,temp.getCurrency()));
				else
					setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));			
			else
				if(temp!=null)
					setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid,temp.getCurrency()));
				else
					setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid));
			
		}else if (obj instanceof BeginningEmpReceivable) {
			BeginningEmpReceivable temp = (BeginningEmpReceivable) obj;
			if(receive.equals("Cash"))
				if (temp!=null) 
					setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid,temp.getCurrency()));
				else
					setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));			
			else
				if(temp!=null)
					setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid,temp.getCurrency()));
				else
					setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid));			
		} 
		
		accountComp().setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(accountComp(), gridBagConstraints);
		jPanel1_2_1.revalidate();
		if (obj == null){
			if (receive.equals("Bank"))
				JOptionPane.showMessageDialog(this,"Emp. Receivable must selected");
			RcvToCombo.setSelectedItem("Cash");			
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}	
		if ("value".equals(evt.getPropertyName())) {
			if (evt.getSource() == EmpARRcvAmountText)
				setBaseCurr();
			else if (evt.getSource() == EmpARRcvExchRateText)
				setBaseCurr();
		}
		if ("object".equals(evt.getPropertyName())) {
			if (evt.getSource() == AccountComp) {
				if (evt.getNewValue() == null) {
					EmpARRcvCurrComp.setObject(null);
				} else {
					if (evt.getNewValue() instanceof CashAccount)
						EmpARRcvCurrComp.setObject(((CashAccount) AccountComp
								.getObject()).getCurrency());
					else if (evt.getNewValue() instanceof BankAccount)
						EmpARRcvCurrComp.setObject(((BankAccount) AccountComp
								.getObject()).getCurrency());
					if (((Currency) EmpARRcvCurrComp.getObject()).getSymbol()
							.equalsIgnoreCase(this.baseCurrency.getSymbol())) {
						EmpARRcvExchRateText.setEditable(false);
						EmpARRcvExchRateText.setValue(new Double(1));
					} else {
						EmpARRcvExchRateText.setEditable(true);
						setDefaultExchangeRate((Currency) EmpARRcvCurrComp.getObject());
					}
				}
			} else if (evt.getSource() == EmpReceivableComp) {
				if (EmpReceivableComp.getObject() != null) {
					Object obj = EmpReceivableComp.getObject();
					if (obj instanceof PmtEmpReceivable) {
						PmtEmpReceivable tempClass = (PmtEmpReceivable) obj;
						InitialLoanAmountText.setValue(new Double(tempClass
								.getAmount()));
						InitialLoanXRateText.setValue(new Double(tempClass
								.getExchangeRate()));
						InitialLoanCurrText.setText(tempClass.getCurrency()
								.getSymbol());
						EmpARRcvCurrComp.setObject(tempClass.getCurrency());
						LookupEmpReceivableDetailPicker dlg = new LookupEmpReceivableDetailPicker(
								m_conn, m_sessionid, tempClass);
						double temp = dlg.getAccumulatedReceived(tempClass);
						AccumulatedRcvCurrText.setText(tempClass.getCurrency()
								.getSymbol());
						AccumulatedRcvAmountText.setValue(new Double(temp));
						TotRcvCurrText.setText(tempClass.getCurrency()
								.getSymbol());
						TotRcvAmountText.setValue(AccumulatedRcvAmountText
								.getValue());
						if (EmpARRcvCurrComp.getObject() != null)
							if (((Currency) EmpARRcvCurrComp.getObject())
									.getSymbol().equalsIgnoreCase(
											this.baseCurrency.getSymbol())) {
								EmpARRcvExchRateText.setEditable(false);
								EmpARRcvExchRateText.setValue(new Double(1));
							} else {
								EmpARRcvExchRateText.setEditable(true);
								setDefaultExchangeRate((Currency) EmpARRcvCurrComp.getObject());
							}
						changeAccountComp();

					} else if (obj instanceof BeginningEmpReceivable) {
						BeginningEmpReceivable tempClass = (BeginningEmpReceivable) obj;
						InitialLoanAmountText.setValue(new Double(tempClass
								.getAccValue()));
						InitialLoanXRateText.setValue(new Double(tempClass
								.getExchangeRate()));
						InitialLoanCurrText.setText(tempClass.getCurrency()
								.getSymbol());
						AccumulatedRcvCurrText.setText(tempClass.getCurrency()
								.getSymbol());
						LookupEmpReceivableDetailPicker dlg = new LookupEmpReceivableDetailPicker(
								m_conn, m_sessionid, tempClass);
						double temp = dlg.getAccumulatedReceived(tempClass);
						AccumulatedRcvAmountText.setValue(new Double(temp));
						EmpARRcvCurrComp.setObject(tempClass.getCurrency());
						EmpARRcvExchRateText.setValue(new Double(tempClass
								.getExchangeRate()));

						if (((Currency) EmpARRcvCurrComp.getObject())
								.getSymbol().equalsIgnoreCase(
										this.baseCurrency.getSymbol())) {
							EmpARRcvExchRateText.setEditable(false);
						} else {
							EmpARRcvExchRateText.setEditable(true);
							setDefaultExchangeRate((Currency) EmpARRcvCurrComp.getObject());
						}
						TotRcvCurrText.setText(tempClass.getCurrency()
								.getSymbol());
						changeAccountComp();
					}
				} else {
					InitialLoanAmountText.setValue(null);
					InitialLoanXRateText.setValue(null);
					InitialLoanCurrText.setText(null);
					AccumulatedRcvCurrText.setText(null);
					AccumulatedRcvAmountText.setValue(null);
				}
			} else if (evt.getSource() == EmpRcvOwnerComp) {
				Object obj = EmpRcvOwnerComp.getObject();
				Employee emp = (Employee) obj;
				jPanel1_2_1.remove(EmpReceivableComp);
				jPanel1_2_1.revalidate();
				jPanel1_2_1.repaint();
				setEmpReceivableComp(new LookupEmployeeRcvPicker(m_conn,
						m_sessionid, emp));
				EmpReceivableComp.setPreferredSize(new java.awt.Dimension(290,
						18));
				java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = 1;
				gridBagConstraints.gridy = 3;
				gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
				gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
				jPanel1_2_1.add(EmpReceivableComp, gridBagConstraints);
				jPanel1_2_1.revalidate();
				setNewEmployee();
			}
		}
		if ("date".equals(evt.getPropertyName())) {
			if (evt.getSource() == TransactionDateDate) {
				setDefaultExchangeRate((Currency) EmpARRcvCurrComp.getObject());
			}
		}
	}

	private void setBaseCurr() {
		double amount=0,crate=1,acumulate=0;
		if (EmpARRcvAmountText.getValue()!=null)
			amount=((Number)EmpARRcvAmountText.getValue()).doubleValue();			
		if(EmpARRcvExchRateText.getValue()!=null)
			crate=((Number)EmpARRcvExchRateText.getValue()).doubleValue();			
		EmpARRcvAmountBaseText.setValue(new Double(amount*crate));

		if (AccumulatedRcvAmountText.getValue()!=null){
			acumulate = ((Number)AccumulatedRcvAmountText.getValue()).doubleValue();
		}
		double total = acumulate+amount*crate;
		TotRcvAmountText.setValue(new Double(total));
	}
	
	private void setNewEmployee(){ 
		InitialLoanAmountText.setValue(null);
		InitialLoanXRateText.setValue(null);
		InitialLoanCurrText.setText(null);
		AccumulatedRcvCurrText.setText(null);
		AccumulatedRcvAmountText.setValue(null);		
		EmpARRcvCurrComp.setObject(null);
		EmpARRcvAmountText.setText(null);
		EmpARRcvExchRateText.setValue(null);
		TotRcvCurrText.setText(null);			
	}
	
	private LookupPicker AccountComp;
	private javax.swing.JLabel AccountLabel;
	private static javax.swing.JFormattedTextField AccumulatedRcvAmountText;
	private static javax.swing.JTextField AccumulatedRcvCurrText;
	private javax.swing.JButton AccumulatedRcvBtn;
	private javax.swing.JLabel AccumulatedReceiveLabel;
	private javax.swing.JLabel AmountBaseCurrLabel;
	private javax.swing.JLabel AmountLabel;
	private javax.swing.JLabel CurrencyLabel;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JTextField EmpARRcvAmountBaseCurrText;
	private javax.swing.JFormattedTextField EmpARRcvAmountBaseText;
	private javax.swing.JFormattedTextField EmpARRcvAmountText;
	private CurrencyPicker EmpARRcvCurrComp;
	private javax.swing.JFormattedTextField EmpARRcvExchRateText;
	private javax.swing.JLabel EmpReceivableLabel;
	//Ini awalnya LookupEmpARListPicker
	private LookupEmployeeRcvPicker EmpReceivableComp;
	private javax.swing.JLabel ExchRateLabel;
	private javax.swing.JLabel ExpSheetDiffLabel;
	private static javax.swing.JFormattedTextField InitialLoanAmountText;
	private static javax.swing.JFormattedTextField InitialLoanXRateText;
	private javax.swing.JLabel InitialLoanXRateLabel;
	private static javax.swing.JTextField InitialLoanCurrText;
	private javax.swing.JLabel InitialLoanLabel;
	private javax.swing.JComboBox RcvToCombo;
	private javax.swing.JLabel ReceiptDateLabel;
	private javax.swing.JLabel ReceiptNoLabel;
	private javax.swing.JLabel ReceiveToLabel;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScrollPane;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JTextField SubmittedDateText;
	private javax.swing.JFormattedTextField TotRcvAmountText;
	private javax.swing.JTextField TotRcvCurrText;
	private javax.swing.JLabel TotalReceiveLabel;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
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
	private javax.swing.JPanel jPanel1_3;
	private AssignPanel OriginatorComp;
	private javax.swing.JPanel jPanel1_3_2;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel jPanel1_3_2_2;
	private AssignPanel ReceivedComp;
	private javax.swing.JSeparator jSeparator1;
	//private javax.swing.JSplitPane jSplitPane1;
	//private EmployeeListPanel employeeListPanel;
	
	private void enableAwal(){ 	
		setenableEditPanel(jPanel1_2_1,false);
		setenableEditPanel(jPanel1_2_1_1,false);
		setenableEditPanel(jPanel1_2_2,false);
		setenableEditPanel(jPanel1_2_2_1,false);
		setenableEditPanel(jPanel1_2_1_3,false);
		setenableEditPanel(jPanel1_2_1_2,false);
		EmpARRcvCurrComp.setEnabled(false);
	}
	
	protected void enableEditMode(){
		EmpARRcvCurrComp.setObject(entity().getCurrency());
		RcvToCombo.setEnabled(true);
		AccountComp.setEnabled(true);
		EmpReceivableComp.setEnabled(true);
		if (EmpARRcvCurrComp.getObject()!=null)
			if (((Currency)EmpARRcvCurrComp.getObject()).getSymbol().equalsIgnoreCase(this.baseCurrency.getSymbol()) ){ 
				EmpARRcvExchRateText.setEditable(false);
				EmpARRcvExchRateText.setValue(new Double(1));
			}else
				EmpARRcvExchRateText.setEditable(true);
		TransactionDateDate.setEditable(true);
		EmpARRcvAmountText.setEditable(true);
		AccumulatedRcvBtn.setEnabled(true);
		DescriptionTextArea.setEnabled(true);
		RemarksTextArea.setEnabled(true);	
		OriginatorComp.setEnabled(true);
		ApprovedComp.setEnabled(true);
		ReceivedComp.setEnabled(true);
		//employeeListPanel.setEnabledList(false);
		EmpRcvOwnerComp.setEnabled(true);
	}
	
	protected void disableEditMode(){
		RcvToCombo.setEnabled(false);
		AccountComp.setEnabled(false);
		EmpReceivableComp.setEnabled(false);
		EmpARRcvExchRateText.setEditable(false);
		TransactionDateDate.setEditable(false);
		EmpARRcvAmountText.setEditable(false);
		AccumulatedRcvBtn.setEnabled(false);
		DescriptionTextArea.setEnabled(false);
		RemarksTextArea.setEnabled(false);	
		OriginatorComp.setEnabled(false);
		ApprovedComp.setEnabled(false);
		ReceivedComp.setEnabled(false);
		EmpRcvOwnerComp.setEnabled(false);
		//employeeListPanel.setEnabledList(true);
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (TransactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (AccountComp.getObject()== null)
			addInvalid("Source account must be selected");
		if (EmpReceivableComp.getObject() == null)
			addInvalid("Expense Sheet No must be selected");
		if (EmpARRcvAmountText.getText().compareTo("")==0)
			addInvalid("Amount Expense Sheet Difference must be filled");
		if (DescriptionTextArea.getText().compareTo("")==0)
			addInvalid("Description Text Area must be filled");
		if  (EmpARRcvExchRateText.getText().equals(""))
			addInvalid("Exchange rate must be filled");
		//Object obj = EmpARRcvExchRateText.getValue();
		//Number amount = (Number) obj;
	
//		if ()
			
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
		String paySource = (String) RcvToCombo.getSelectedItem();
		if(paySource.equalsIgnoreCase("BANK")){
			entity().setPaymentSource("BANK");
			entity().setBankAccount((BankAccount) AccountComp.getObject());
			entity().setCashAccount(null);
			entity().setUnit(entity().getBankAccount().getUnit());
		}else{
			entity().setPaymentSource("CASH");
			entity().setCashAccount((CashAccount) AccountComp.getObject());
			entity().setBankAccount(null);
			entity().setUnit(entity().getCashAccount().getUnit());
		}
		
		Object obj = EmpReceivableComp.getObject();
		if (obj instanceof PmtEmpReceivable) {
			PmtEmpReceivable tempClass = (PmtEmpReceivable) obj;
			entity().setEmpReceivable(tempClass);
			entity().setBeginningBalance(null);
		}else if (obj instanceof BeginningEmpReceivable) {
			BeginningEmpReceivable tempClass = (BeginningEmpReceivable) obj;
			entity().setBeginningBalance(tempClass);			
			entity().setEmpReceivable(null);
		}		
		
		Object object = EmpARRcvExchRateText.getValue();
		Number amount = (Number) object;
		entity().transTemplateRead(
				this.OriginatorComp,this.ApprovedComp,
				this.ReceivedComp,this.RefNoText.getText(),
				this.DescriptionTextArea.getText(), this.RemarksTextArea.getText(),
				this.TransactionDateDate.getDate(),amount.doubleValue());
		
		obj = EmpARRcvAmountText.getValue();
		amount = (Number) obj;
		if (amount!=null)
			entity().setAmount(amount.doubleValue());
		entity().setCurrency((Currency)EmpARRcvCurrComp.getObject());
	}
	
	
	protected  void entity2gui(){
		if (entity().getEmpReceivable() != null) {		
			EmpRcvOwnerComp.setObject(entity().getEmpReceivable().getPayTo());
			EmpReceivableComp.setObject(entity().getEmpReceivable());			
		} else if (entity().getBeginningBalance()!=null) {
			EmpRcvOwnerComp.setObject(entity().getBeginningBalance().getEmployee());	
			EmpReceivableComp.setObject(entity().getBeginningBalance());
		}else{
			EmpRcvOwnerComp.setObject(null);
			EmpReceivableComp.setObject(null);						
		}
				
		if (entity().isBank()){
			BankAccount bankAcct = entity().getBankAccount();
			RcvToCombo.setSelectedItem("Bank");
			accountComp().setObject(bankAcct);
		}else {
			RcvToCombo.setSelectedItem("Cash");
			CashAccount cashAcct = entity().getCashAccount();
			accountComp().setObject(cashAcct);
		}
		
		OriginatorComp.setEmployee(entity().getEmpOriginator());
		RefNoText.setText(entity().getReferenceNo());
		SubmittedDateText.setText(entity().getSubmitDate() == null ? "" :  entity().getSubmitDate().toString());
		EmpARRcvAmountText.setValue(new Double(entity().getAmount()));
		DescriptionTextArea.setText(entity().getDescription());
		RemarksTextArea.setText(entity().getRemarks());
		EmpARRcvCurrComp.setObject(entity().getCurrency());
		if(entity().getExchangeRate()>0)
			EmpARRcvExchRateText.setValue(new Double(entity().getExchangeRate()));
		else
			EmpARRcvExchRateText.setValue(new Double(1));
		RefNoText.setText(entity().getReferenceNo());
		if(entity().getTransactionDate()!=null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else{
			TransactionDateDate.setDate(new Date());
		}
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
		
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");
		EmpARRcvAmountBaseCurrText.setText(this.baseCurrency.getSymbol());	
		if (entity().getStatus()==3){
			double awal/*,akhir*/;
			try{
				if (!AccumulatedRcvAmountText.getText().equalsIgnoreCase(""))
					awal=((Number)AccumulatedRcvAmountText.getValue()).doubleValue();
				else
					awal=0;
			/*	if (!EmpARRcvAmountText.getText().equalsIgnoreCase(""))
					akhir=((Number)m_numberFormatter.stringToValue(EmpARRcvAmountText.getText())).doubleValue();
				else
					akhir=0;*/
				
				TotRcvAmountText.setValue(new Double(awal));
			}
			catch (Exception e){
				System.out.println("Error");
			}
		}
	}
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	RcvEmpReceivable entity() {
		return m_entity;
	}
	
	RcvEmpReceivable oldEntity;
	void setEntity(Object m_entity) {	
		oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (RcvEmpReceivable)m_entity;
		this.m_entity.addPropertyChangeListener(this);
		readEntityState();
		
		if(this.m_entity.getBeginningBalance()!=null){
			setBeginningBalance();
		}
	}
	private void setBeginningBalance() {
		BeginningEmpReceivable bar = this.m_entity.getBeginningBalance();
		EmployeePicker helpEmp=new EmployeePicker(m_conn,m_sessionid);			
		Unit unit = helpEmp.findUnitEmployee(bar.getEmployee());
		if (unit==null){				
			UnitPicker unitpicker = new UnitPicker();
			unitpicker.init(m_conn, m_sessionid);				
			unit = unitpicker.getDefaultUnit();
		}
		BeginningBalanceBusinessLogic logic = new BeginningBalanceBusinessLogic(m_conn, m_sessionid);
		bar.setTrans(logic.findTransaction(unit));		
		bar.showReferenceNo(true);		
		this.m_entity.setBeginningBalance(bar);
	}
	
	protected void setEmpReceivableComp(LookupPicker accountComp) {
		LookupPicker old = EmpReceivableComp;
		if (old!=null){
			old.removePropertyChangeListener("object",this);
		}
		EmpReceivableComp = (LookupEmployeeRcvPicker) accountComp;
		EmpReceivableComp.addPropertyChangeListener("object",this);
	}	
	
	protected Object createNew(){  
		RcvEmpReceivable a  = new RcvEmpReceivable();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}
		
	protected boolean cekBalance(){
		double a,b,c;
		a=((Number)InitialLoanAmountText.getValue()).doubleValue();
		b=((Number)AccumulatedRcvAmountText.getValue()).doubleValue();
		c=((Number)EmpARRcvAmountText.getValue()).doubleValue();
		if (b+c>a){
			JOptionPane.showMessageDialog(this,"This Receive will exceed the Employee Payment. Please Check your Account Employee Receivable Payment amount");
			return false;
		}
		else
			return true;
	}

	//Ini untuk mengecek transaksi yang gantung??
	protected boolean cekGantung(){
		Object obj  = EmpReceivableComp.getObject();
		GenericMapper mapnya = MasterMap.obtainMapperFor(RcvEmpReceivable.class);
		String strWhere ="";
		if (obj instanceof PmtEmpReceivable) {			
			PmtEmpReceivable tempClass = (PmtEmpReceivable) obj;
			strWhere = "EMPRECEIVABLE="+tempClass.getIndex()+" AND STATUS<>3 AND " + IDBConstants.ATTR_AUTOINDEX + "<>" + m_entity.getIndex();;
		}else if (obj instanceof BeginningEmpReceivable) {
			BeginningEmpReceivable tempClass = (BeginningEmpReceivable) obj;
			strWhere = "BEGINNINGBALANCE="+tempClass.getIndex()+" AND STATUS<>3 AND " + IDBConstants.ATTR_AUTOINDEX + "<>" + m_entity.getIndex();
			System.err.println(strWhere);
		}
		Object[] listData=mapnya.doSelectWhere(strWhere).toArray();
		if (listData.length!=0){
			JOptionPane.showMessageDialog(this,"There's still a transaction for this receipt hasn't posted yet. \nPlease Posting the transaction first before you create a new transaction");
			return false;
		}
		else
			return true;
				
	}
	
	protected void doSave() {
		if (!cekValidity()) return;
		if(!cekBalance()) return;
		if (!cekGantung()) return;		
		super.doSave();		
		Object obj = EmpReceivableComp.getObject();
		if (obj instanceof PmtEmpReceivable) {
			GenericMapper map = MasterMap.obtainMapperFor(PmtEmpReceivable.class);
			map.setActiveConn(m_conn);
			PmtEmpReceivable tempClass = (PmtEmpReceivable) obj;
			tempClass.setEndingBalance(tempClass.getEndingBalance()-entity().getAmount());
			short tempstate= entity().getStatus();
			map.doUpdate(obj);
			entity().setStatus(tempstate);
		}		
		
		AccountingSQLSAP sql=new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_RCV_EMP_RECEIVABLE, m_conn);
				entity().setIndex(index);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	protected void doDelete(){
		super.doDelete();
		if (entity().getStatus()==0){ 
			GenericMapper map = MasterMap.obtainMapperFor(PmtEmpReceivable.class);
			map.setActiveConn(m_conn);
			PmtEmpReceivable obj=(PmtEmpReceivable)oldEntity.getEmpReceivable();
			obj.setEndingBalance(obj.getEndingBalance()+oldEntity.getAmount());
			short tempstate= entity().getStatus();
			map.doUpdate(obj);
			entity().setStatus(tempstate);			
		}
	}
	
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
		OriginatorComp.m_datePicker.setDate(new Date());
		ApprovedComp.m_datePicker.setDate(new Date());
		ReceivedComp.m_datePicker.setDate(new Date());
		EmpARRcvAmountBaseCurrText.setText(this.baseCurrency.getSymbol());		
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_EMPLOYEE_RECEIVABLE, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_EMPLOYEE_RECEIVABLE, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_EMPLOYEE_RECEIVABLE, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	
	protected void clearForm() {
		clearTextField(jPanel1_2_2_1);
		clearTextField(jPanel1_2_1_2);
		clearTextField(jPanel1_2_1_3);
		clearTextField(jPanel1_2_2);	
		clearTextField(jPanel1_2_1);
		clearTextField(jPanel1_2_1_1);		
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
		
	public void clearKomponen(){
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
		EmpARRcvCurrComp.setObject(null);
		EmpARRcvAmountBaseText.setValue(new Double(0));
		EmpARRcvAmountBaseCurrText.setText("");
		TransactionDateDate.setDate(null);
		EmpARRcvExchRateText.setValue(new Double(0));
		TotRcvCurrText.setText("");
		TotRcvAmountText.setValue(new Double(0));
		AccumulatedRcvAmountText.setValue(new Double(0));
		InitialLoanAmountText.setValue(new Double(0));
		InitialLoanXRateText.setValue(new Double(0));
		InitialLoanCurrText.setText("");
		AccumulatedRcvCurrText.setText("");
	}
	
	public void clearAll(){
		super.clearAll();
		clearForm();
		clearKomponen();
		disableEditMode();
	}

}
