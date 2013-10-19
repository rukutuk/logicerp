package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Component;
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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.Rcpt_Loan;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.RcvLoan;
import pohaci.gumunda.titis.accounting.entity.SalesReceivedLoader;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class ReceiveLoanPanel extends RevTransactionPanel
implements ActionListener,DocumentListener,PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	private RcvLoan m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;

	public ReceiveLoanPanel(Connection conn, long sessionid) {
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
		setEntity(new RcvLoan());
		m_entityMapper = MasterMap.obtainMapperFor(RcvLoan.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}

	private void setDefaultExchangeRate(Currency currency) {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		LoanRcvExchRateTextJF.setValue(new Double(rate));
	}

	private void initComponents() {
		LoanRcvCurrCombo = new CurrencyPicker(m_conn, m_sessionid);
		AccountComp = new LookupBankAccountPicker(m_conn, m_sessionid);
		CompanyLoanComp = new LookupLoanPicker(m_conn, m_sessionid);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");
		Details = new LookupCompanyLoanRcvPicker(m_conn, m_sessionid);
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
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanelKanan = new javax.swing.JPanel();
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
		jPanelKiri = new javax.swing.JPanel();
		StatusLabel = new javax.swing.JLabel();
		SubmittedDateLabel = new javax.swing.JLabel();
		ReceiveToLabel = new javax.swing.JLabel();
		AccountLabel1 = new javax.swing.JLabel();
		CompanyLoanLabel = new javax.swing.JLabel();
		AccountLabel2 = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		RcvToCombo = new javax.swing.JComboBox();
		LoanAccountText = new javax.swing.JTextField();
		InitialLoanLabel = new javax.swing.JLabel();
		AccumulatedReceiveLabel = new javax.swing.JLabel();
		initialLoanPanel = new javax.swing.JPanel();
		InitialLoanCurrText = new javax.swing.JTextField();
		InitialLoanAmountTextJF = new javax.swing.JFormattedTextField(m_numberFormatter);
		jPanelAccumulatedRcv = new javax.swing.JPanel();
		AccumulatedRcvCurrText = new javax.swing.JTextField();
		AccumulatedRcvAmountTextJF = new javax.swing.JFormattedTextField(m_numberFormatter);
		jPanel1_2_1_1 = new javax.swing.JPanel();
		ExpSheetDiffLabel = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		CurrencyLabel = new javax.swing.JLabel();
		ExchRateLabel = new javax.swing.JLabel();
		LoanRcvExchRateTextJF = new javax.swing.JFormattedTextField(m_numberFormatter3);
		AmountLabel = new javax.swing.JLabel();
		LoanRcvAmountTextJF = new javax.swing.JFormattedTextField(m_numberFormatter3);
		AmountBaseCurrLabel = new javax.swing.JLabel();
		LoanRcvAmountBaseText = new javax.swing.JTextField();
		LoanRcvAmountBaseCurrText = new javax.swing.JTextField();
		TotalReceiveLabel = new javax.swing.JLabel();
		TotRcvCurrText = new javax.swing.JTextField();
		TotRcvAmountTextJF = new javax.swing.JFormattedTextField(m_numberFormatter);
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();

		setLayout(new java.awt.BorderLayout());
		setPreferredSize(new java.awt.Dimension(700, 420));
		jPanel1.setLayout(new java.awt.BorderLayout());
		jPanel1.setPreferredSize(new java.awt.Dimension(670, 400));
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
		jPanel1_2.setPreferredSize(new java.awt.Dimension(700, 375));
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(415, 400));

		jPanelKanan.setLayout(new java.awt.GridBagLayout());
		jPanelKanan.setPreferredSize(new java.awt.Dimension(415, 400));
		ReceiptNoLabel.setText("Receipt No");
		ReceiptNoLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);

		jPanelKanan.add(ReceiptNoLabel, gridBagConstraints);
		ReceiptDateLabel.setText("Receipt Date*");
		ReceiptDateLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(ReceiptDateLabel, gridBagConstraints);

		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(DescriptionLabel, gridBagConstraints);

		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 110));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(DescriptionScrollPane, gridBagConstraints);

		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(RemarksLabel, gridBagConstraints);

		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(290, 110));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScrollPane.setViewportView(RemarksTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 3, 1);
		jPanelKanan.add(RemarksScrollPane, gridBagConstraints);

		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelKanan.add(RefNoText, gridBagConstraints);

		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelKanan.add(TransactionDateDate, gridBagConstraints);

		jPanel1_2_2.add(jPanelKanan, java.awt.BorderLayout.WEST);

		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);

		jPanelKiri.setLayout(new java.awt.GridBagLayout());

		jPanelKiri.setPreferredSize(new java.awt.Dimension(440, 400));
		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(StatusLabel, gridBagConstraints);

		SubmittedDateLabel.setText("Submitted Date");
		SubmittedDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(SubmittedDateLabel, gridBagConstraints);

		ReceiveToLabel.setText("Receive To");
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(ReceiveToLabel, gridBagConstraints);

		AccountLabel1.setText("Account*");
		AccountLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccountLabel1, gridBagConstraints);

		CompanyLoanLabel.setText("Company Loan*");
		CompanyLoanLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(CompanyLoanLabel, gridBagConstraints);

		AccountLabel2.setText("Loan Account");
		AccountLabel2.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccountLabel2, gridBagConstraints);

		StatusText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(StatusText, gridBagConstraints);

		SubmittedDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(SubmittedDateText, gridBagConstraints);

		RcvToCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bank","Cash"}));
		RcvToCombo.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(RcvToCombo, gridBagConstraints);

		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccountComp, gridBagConstraints);

		CompanyLoanComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(CompanyLoanComp, gridBagConstraints);

		LoanAccountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(LoanAccountText, gridBagConstraints);

		InitialLoanLabel.setText("Initial Loan");
		InitialLoanLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(InitialLoanLabel, gridBagConstraints);

		AccumulatedReceiveLabel.setText("Accumulated Receive");
		AccumulatedReceiveLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccumulatedReceiveLabel, gridBagConstraints);

		initialLoanPanel.setLayout(new java.awt.GridBagLayout());
		initialLoanPanel.setPreferredSize(new java.awt.Dimension(290, 20));
		InitialLoanCurrText.setPreferredSize(new java.awt.Dimension(78, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		initialLoanPanel.add(InitialLoanCurrText, gridBagConstraints);

		InitialLoanAmountTextJF.setPreferredSize(new java.awt.Dimension(207, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		initialLoanPanel.add(InitialLoanAmountTextJF, gridBagConstraints);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(initialLoanPanel, gridBagConstraints);

		jPanelAccumulatedRcv.setLayout(new java.awt.GridBagLayout());
		jPanelAccumulatedRcv.setPreferredSize(new java.awt.Dimension(290, 20));
		AccumulatedRcvCurrText.setPreferredSize(new java.awt.Dimension(78, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanelAccumulatedRcv.add(AccumulatedRcvCurrText, gridBagConstraints);

		AccumulatedRcvAmountTextJF.setPreferredSize(new java.awt.Dimension(167, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanelAccumulatedRcv.add(AccumulatedRcvAmountTextJF, gridBagConstraints);

		JLabel label=new JLabel();
		label.setPreferredSize(new java.awt.Dimension(3, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanelAccumulatedRcv.add(label, gridBagConstraints);


		Details.setPreferredSize(new java.awt.Dimension(35, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanelAccumulatedRcv.add(Details, gridBagConstraints);


		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(jPanelAccumulatedRcv, gridBagConstraints);

		jPanel1_2_1_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(410, 100));
		ExpSheetDiffLabel.setText("Loan Receive");
		ExpSheetDiffLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ExpSheetDiffLabel, gridBagConstraints);

		jSeparator1.setPreferredSize(new java.awt.Dimension(290, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(jSeparator1, gridBagConstraints);

		CurrencyLabel.setText("Currency*");
		CurrencyLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(CurrencyLabel, gridBagConstraints);

		ExchRateLabel.setText("Exch. Rate*");
		ExchRateLabel.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ExchRateLabel, gridBagConstraints);

		LoanRcvExchRateTextJF.setPreferredSize(new java.awt.Dimension(137, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(LoanRcvExchRateTextJF, gridBagConstraints);

		AmountLabel.setText("Amount *");
		AmountLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountLabel, gridBagConstraints);

		LoanRcvAmountTextJF.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(LoanRcvAmountTextJF, gridBagConstraints);

		AmountBaseCurrLabel.setText("Amount Base Curr.");
		AmountBaseCurrLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountBaseCurrLabel, gridBagConstraints);

		LoanRcvAmountBaseText.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(LoanRcvAmountBaseText, gridBagConstraints);

		LoanRcvAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(LoanRcvAmountBaseCurrText, gridBagConstraints);

		TotalReceiveLabel.setText("Total Received");
		TotalReceiveLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(TotalReceiveLabel, gridBagConstraints);

		LoanRcvCurrCombo.setPreferredSize(new java.awt.Dimension(80, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		jPanel1_2_1_1.add(LoanRcvCurrCombo, gridBagConstraints);

		TotRcvCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(TotRcvCurrText, gridBagConstraints);

		TotRcvAmountTextJF.setPreferredSize(new java.awt.Dimension(201, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(TotRcvAmountTextJF, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanelKiri.add(jPanel1_2_1_1, gridBagConstraints);
		jPanel1_2.add(jPanelKiri, java.awt.BorderLayout.WEST);
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
		jPanel1_3_2_2.setPreferredSize(new java.awt.Dimension(215, 110));

		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(275, 110));
		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);
		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);
		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);
		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);
		add(jPanel1, java.awt.BorderLayout.NORTH);
	}

	private void addingListener(){
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		CompanyLoanComp.addPropertyChangeListener("object", this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		AccountComp.addPropertyChangeListener("object", this);
		LoanRcvAmountTextJF.getDocument().addDocumentListener(this);
		LoanRcvExchRateTextJF.getDocument().addDocumentListener(this);
		TransactionDateDate.addPropertyChangeListener("date", this);
		RcvToCombo.addActionListener(this);
	}

	protected void doNew() {
		super.doNew();
		isiDefaultAssignPanel();
	}

	protected void clearAll() {
		super.clearAll();
		clearForm();
		Details.setCompanyLoan(null);
		clearKomponen();
		disableEditMode();
	}

	public void clearKomponen(){
		StatusText.setText("");
		CompanyLoanComp.setObject(null);
		OriginatorComp.setEmployee(null);
		ApprovedComp.setEmployee(null);
		ReceivedComp.setEmployee(null);
		OriginatorComp.m_jobTextField.setText("");
		ApprovedComp.m_jobTextField.setText("");
		ReceivedComp.m_jobTextField.setText("");
		OriginatorComp.setDate(null);
		ApprovedComp.setDate(null);
		ReceivedComp.setDate(null);
		TransactionDateDate.setDate(null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==RcvToCombo){
			CompanyLoan companyLoan=(CompanyLoan)CompanyLoanComp.getObject();
			if (companyLoan!=null){
				if (companyLoan.getCurrency()!=null){
					if (((String)RcvToCombo.getSelectedItem()).equals("Bank")){
						setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid, companyLoan.getCurrency()));
					}
					else if (((String)RcvToCombo.getSelectedItem()).equals("Cash")){
						setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid, companyLoan.getCurrency()));
					}
				}
			}else{
				if (((String)RcvToCombo.getSelectedItem()).equals("Bank")){
					setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid));
				}
				else if (((String)RcvToCombo.getSelectedItem()).equals("Cash")){
					setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));
				}
			}
		}
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new Rcpt_Loan(m_entity,m_conn);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			String[] kolom = {"","UPPER(REFERENCENO)","TRANSACTIONDATE","EMPRECEIVED","UPPER(RECEIVETO)","UNIT","STATUS","SUBMITDATE"};
			String orderby = " ORDER BY " + kolom[2] + " ASC," + kolom[1] + " ASC";
			SearchReceiptDialog dlg = new SearchReceiptDialog(GumundaMainFrame.getMainFrame(), "Search Receipt", m_conn, m_sessionid,kolom,orderby,
					new SalesReceivedLoader(m_conn,RcvLoan.class),"Loan" );
			dlg.setVisible(true);
			if (dlg.selectedObj != null)
			{
				doLoad(dlg.selectedObj);
				Details.setEnabled(true);
				toBaseCurrency();
			}
		}

	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		if ("object".equals(evt.getPropertyName())) {
			if (evt.getSource() == AccountComp) {
				if ((BankAccount) AccountComp.getObject() != null) {
					entity().setUnit(
							((BankAccount) AccountComp.getObject()).getUnit());
				}
			}
			if (evt.getSource() == CompanyLoanComp){
				if (evt.getNewValue() == null){
				}
				else{
					CompanyLoan companyLoan=(CompanyLoan)CompanyLoanComp.getObject();
					componentCompanyLoanDependence(companyLoan);
				}
			}

		}
		if ("date".equals(evt.getPropertyName())) {
			if (evt.getSource() == TransactionDateDate) {
				CompanyLoan companyLoan=(CompanyLoan)CompanyLoanComp.getObject();
				if (companyLoan!=null)
					setDefaultExchangeRate(companyLoan.getCurrency());
			}
		}
	}

	// semoga bisa
	protected void setAccountComp(LookupPicker accountComp) {

		jPanelKiri.remove(AccountComp);
		jPanelKiri.revalidate();
		jPanelKiri.repaint();

		LookupPicker old = AccountComp;
		if (old!=null){
			old.removePropertyChangeListener("AccountComp",this);
		}
		AccountComp = accountComp;
		AccountComp.addPropertyChangeListener("AccountComp",this);

		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));



		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		//gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccountComp, gridBagConstraints);
		jPanelKiri.revalidate();

		AccountComp.setEnabled(true);
	}

	private void componentCompanyLoanDependence(CompanyLoan companyLoan) {
		if(companyLoan!=null){
			CompanyLoanComp.setObject(companyLoan);
			AccountComp.setObject(null);
			Details.setCompanyLoan(companyLoan);
			AccumulatedRcvAmountTextJF.setValue(new Double(Details.getAccumulatedAmount()));
			//AccountComp.setFilterText(companyLoan.getCurrency().getSymbol());

			LoanRcvCurrCombo.setObject(companyLoan.getCurrency());
			LoanAccountText.setText(companyLoan.getAccount().getName());
			InitialLoanAmountTextJF.setValue( new Double(companyLoan.getInitial()));
			InitialLoanCurrText.setText(companyLoan.getCurrency().getSymbol());
			AccumulatedRcvCurrText.setText(companyLoan.getCurrency().getSymbol());
			TotRcvCurrText.setText(companyLoan.getCurrency().getSymbol());
			if (companyLoan.getCurrency().getSymbol().equalsIgnoreCase(this.baseCurrency.getSymbol()) )//.equalsIgnoreCase(this.baseCurrency.getSymbol()))
			{
				LoanRcvExchRateTextJF.setEditable(false);
				LoanRcvExchRateTextJF.setValue(new Double(1));
			}
			else{
				LoanRcvExchRateTextJF.setEditable(true);
				setDefaultExchangeRate(companyLoan.getCurrency());
			}
			if (((String)RcvToCombo.getSelectedItem()).equals("Cash")){
				setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid,companyLoan.getCurrency()));
			}else if (((String)RcvToCombo.getSelectedItem()).equals("Bank")){
				setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid,companyLoan.getCurrency()));
			}
		}else{
			CompanyLoanComp.setObject(null);
			Details.setCompanyLoan(companyLoan);
			AccumulatedRcvAmountTextJF.setValue(new Double(Details.getAccumulatedAmount()));
			//AccountComp.setFilterText("");
			setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid));
			LoanRcvCurrCombo.setObject(null);
			LoanAccountText.setText("");
			InitialLoanAmountTextJF.setValue( null);
			InitialLoanCurrText.setText("");
			AccumulatedRcvCurrText.setText("");
			TotRcvCurrText.setText("");

			LoanRcvExchRateTextJF.setEditable(false);
			LoanRcvExchRateTextJF.setValue(new Double(1));
		}
	}
	private LookupPicker AccountComp;
	private javax.swing.JLabel AccountLabel1;
	private javax.swing.JLabel AccountLabel2;
	private javax.swing.JFormattedTextField AccumulatedRcvAmountTextJF;
	private javax.swing.JTextField AccumulatedRcvCurrText;
	private javax.swing.JLabel AccumulatedReceiveLabel;
	private javax.swing.JLabel AmountBaseCurrLabel;
	private javax.swing.JLabel AmountLabel;
	private LookupLoanPicker CompanyLoanComp;
	private javax.swing.JLabel CompanyLoanLabel;
	private javax.swing.JLabel CurrencyLabel;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private LookupCompanyLoanRcvPicker Details;
	private javax.swing.JLabel ExchRateLabel;
	private javax.swing.JLabel ExpSheetDiffLabel;
	private javax.swing.JFormattedTextField InitialLoanAmountTextJF;
	private javax.swing.JTextField InitialLoanCurrText;
	private javax.swing.JLabel InitialLoanLabel;
	private javax.swing.JTextField LoanAccountText;
	private javax.swing.JTextField LoanRcvAmountBaseCurrText;
	private javax.swing.JTextField LoanRcvAmountBaseText;
	private JFormattedTextField LoanRcvAmountTextJF;
	private CurrencyPicker LoanRcvCurrCombo;
	private javax.swing.JFormattedTextField LoanRcvExchRateTextJF;
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
	private javax.swing.JFormattedTextField TotRcvAmountTextJF;
	private javax.swing.JTextField TotRcvCurrText;
	private javax.swing.JLabel TotalReceiveLabel;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel TopButtonPanel;
	private javax.swing.JPanel jPanel1_2;
	private javax.swing.JPanel jPanelKiri;
	private javax.swing.JPanel jPanel1_2_1_1;
	private javax.swing.JPanel jPanelAccumulatedRcv;
	private javax.swing.JPanel initialLoanPanel;
	private javax.swing.JPanel jPanel1_2_2;
	private javax.swing.JPanel jPanelKanan;
	private javax.swing.JPanel jPanel1_3;
	private AssignPanel OriginatorComp;
	private javax.swing.JPanel jPanel1_3_2;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel jPanel1_3_2_2;
	private AssignPanel ReceivedComp;
	private javax.swing.JSeparator jSeparator1;
	private void enableAwal(){
		setenableEditPanel(jPanelKiri,false);
		setenableEditPanel(jPanel1_2_1_1,false);
		setenableEditPanel(jPanel1_2_2,false);
		setenableEditPanel(jPanelKanan,false);
		setenableEditPanel(initialLoanPanel,false);
		setenableEditPanel(jPanelAccumulatedRcv,false);
		LoanRcvCurrCombo.setEnabled(false);
		RcvToCombo.setEnabled(false);
	}

	protected void enableEditMode()	{
		RcvToCombo.setEnabled(true);
		this.AccountComp.setEnabled(true);
		this.CompanyLoanComp.setEnabled(true);
		this.LoanRcvAmountTextJF.setEditable(true);
		if (CompanyLoanComp.getObject()!=null)
		{ if (((CompanyLoan)CompanyLoanComp.getObject()).getCurrency().getSymbol().equalsIgnoreCase(this.baseCurrency.getSymbol()))//.equalsIgnoreCase(this.baseCurrency.getSymbol()))
		   { LoanRcvExchRateTextJF.setEditable(false);
		    LoanRcvExchRateTextJF.setValue(new Double(1));
		   }
		 else
			 LoanRcvExchRateTextJF.setEditable(true);
		}
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.TransactionDateDate.setEditable(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
		this.Details.setEnabled(true);
	}

	protected void disableEditMode(){
		RcvToCombo.setEnabled(false);
		this.AccountComp.setEnabled(false);
		this.CompanyLoanComp.setEnabled(false);
		this.LoanRcvAmountTextJF.setEditable(false);
		this.LoanRcvExchRateTextJF.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.TransactionDateDate.setEditable(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
		this.Details.setEnabled(false);
	}
	ArrayList validityMsgs = new ArrayList();

	protected boolean cekValidity(){
		validityMsgs.clear();
		if (TransactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (AccountComp.getObject()== null)
			addInvalid("Source account must be selected");
		if (CompanyLoanComp.getObject()==null)
			addInvalid("Company Loan must be selected");
		if (LoanRcvAmountTextJF.getValue()==null)
			addInvalid("Loan Receive amount must be filled");
		if (DescriptionTextArea.getText().compareTo("")==0)
			addInvalid("Description Text Area must be filled");
		if (validityMsgs.size()>0)
		{
			StringBuffer result = new StringBuffer();
			Iterator iter = validityMsgs.iterator();
			while (iter.hasNext())
			{
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

	protected void gui2entity(){
		String paySource = (String) RcvToCombo.getSelectedItem();
		entity().setReceiveTo(paySource);
		if (AccountComp.getObject() instanceof BankAccount) {
			BankAccount bank = (BankAccount) AccountComp.getObject();
			entity().setBankAccount(bank);
			entity().setUnit(bank.getUnit());
			entity().setCashAccount(null);
		}
		else if (AccountComp.getObject() instanceof CashAccount) {
			CashAccount cash = (CashAccount) AccountComp.getObject();
			entity().setCashAccount(cash);
			entity().setUnit(cash.getUnit());
			entity().setBankAccount(null);
		}
		entity().setCompanyLoan((CompanyLoan)CompanyLoanComp.getObject());
		entity().setCurrency((Currency)LoanRcvCurrCombo.getObject());
		entity().setExchangeRate(((Number)LoanRcvExchRateTextJF.getValue()).doubleValue());
		Object obj = LoanRcvExchRateTextJF.getValue();
		Number amount = (Number) obj;
		entity().transTemplateRead(
				this.OriginatorComp,this.ApprovedComp,
				this.ReceivedComp,this.RefNoText.getText(),
				this.DescriptionTextArea.getText(), this.RemarksTextArea.getText(),
				this.TransactionDateDate.getDate(),amount.doubleValue());
		obj=LoanRcvAmountTextJF.getValue();
		amount=(Number)obj;
		entity().setAmount(amount.doubleValue());

	}

	protected  void entity2gui()
	{
		if(entity().getReceiveTo()==null){
		}
		else if (entity().getReceiveTo().equals("Bank")){
			RcvToCombo.setSelectedItem("Bank");
		}
		else if(entity().getReceiveTo().equals("Cash")){
			RcvToCombo.setSelectedItem("Cash");
		}
		componentCompanyLoanDependence(entity().getCompanyLoan());
		CompanyLoanComp.setObject(entity().getCompanyLoan());
		if(new Double(entity().getAmount()).compareTo(new Double(0))!=0)
			LoanRcvAmountTextJF.setValue(new Double(entity().getAmount()));
		else
			LoanRcvAmountTextJF.setValue(null);
		if(entity().getExchangeRate()>0)
			LoanRcvExchRateTextJF.setValue(new Double(entity().getExchangeRate()));
		else
			LoanRcvExchRateTextJF.setValue(null);
		RefNoText.setText(entity().getReferenceNo());
		if(entity().getTransactionDate()!=null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else{
			TransactionDateDate.setDate(new Date());
		}
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
		LoanRcvAmountBaseCurrText.setText(this.baseCurrency.getSymbol());
		StatusText.setText(entity().statusInString());

		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");

		/*AccountingSQLSAP logic =new AccountingSQLSAP();
		try {
			long index=logic.getMaxIndex("RcvLoan",m_conn);
			Details.setRcvFilterIndex(index);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/

		if(entity().getReceiveTo()==null){
		}
		else if (entity().getReceiveTo().equals("Bank")){
			AccountComp.setObject(entity().getBankAccount());
		}
		else if(entity().getReceiveTo().equals("Cash")){
			AccountComp.setObject(entity().getCashAccount());
		}

		Details.setRcvFilter(this.entity());

		if(entity().getStatus()==StateTemplateEntity.State.POSTED){
			double sum = Details.getAccumulatedAmount()-((Number)LoanRcvAmountTextJF.getValue()).doubleValue();
			 AccumulatedRcvAmountTextJF.setValue(new Double(sum));
		}else{
			 AccumulatedRcvAmountTextJF.setValue(new Double(Details.getAccumulatedAmount()));
		}
	}

	protected StateTemplateEntity currentEntity() {
		return entity();
	}

	RcvLoan entity() {
		return m_entity;
	}
	void setEntity(Object m_entity) {
		RcvLoan oldEntity = this.m_entity;
		if (oldEntity!=null)
		{
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (RcvLoan)m_entity;
		this.m_entity.addPropertyChangeListener(this);
		readEntityState();
	}
	protected Object createNew()
	{
		RcvLoan a  = new RcvLoan();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}

	protected void doSave() {
		if (!cekValidity()) return;
		double initial = ((Number)InitialLoanAmountTextJF.getValue()).doubleValue();
		double totalRec = ((Number)TotRcvAmountTextJF.getValue()).doubleValue();
		if(totalRec>initial)
			if (JOptionPane.showConfirmDialog(this,"Total Received is bigger than initial loan, are you sure to save this receive?",
					"Delete Confirmation Dialog",JOptionPane.YES_NO_OPTION)!=0)
				return;
		super.doSave();
		AccountingSQLSAP sql=new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0"))
			{
				long index = sql.getMaxIndex(IDBConstants.TABLE_RCV_LOAN, m_conn);
				entity().setIndex(index);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
	}

	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_LOAN, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_LOAN, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_LOAN, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}

	protected void clearForm() {
		clearTextField(jPanelKanan);
		clearTextField(initialLoanPanel);
		clearTextField(jPanel1_2_2);
		clearTextField(jPanelKiri);
		clearTextField(jPanelAccumulatedRcv);
		clearTextField(jPanel1_2_1_1);
	}

	protected void clearTextField(JPanel temppanel) {
		Component[] componentList=temppanel.getComponents();
		JTextField temptext;
		for (int i=0;i<componentList.length;i++)
		{
			if (componentList[i] instanceof JTextField)
			{
				temptext=(JTextField)componentList[i];
				temptext.setText("");
			}
		}
	}

	public void insertUpdate(DocumentEvent e) {
		toBaseCurrency();
	}

	public void removeUpdate(DocumentEvent e) {
		toBaseCurrency();
	}

	public void changedUpdate(DocumentEvent e) {
		toBaseCurrency();
	}

	private void toBaseCurrency() {
		try{
			double amount,crate = 1;
			if (LoanRcvAmountTextJF.getValue()!=null)
				amount=((Number)LoanRcvAmountTextJF.getValue()).doubleValue();
			else
				amount=0;
			Object xchRate=LoanRcvExchRateTextJF.getValue();
			if(xchRate!=null)
				crate=( (Number)LoanRcvExchRateTextJF.getValue() ).doubleValue();
			else
				crate=1;

			LoanRcvAmountBaseText.setText(m_numberFormatter.valueToString(new Double(amount*crate)));
			double total = amount + ((Number)AccumulatedRcvAmountTextJF.getValue()).doubleValue();
			TotRcvAmountTextJF.setValue(new Double(total));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
