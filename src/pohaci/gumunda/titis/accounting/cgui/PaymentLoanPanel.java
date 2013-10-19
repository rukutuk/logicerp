
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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningLoan;
import pohaci.gumunda.titis.accounting.cgui.report.Vchr_loan;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtLoan;
import pohaci.gumunda.titis.accounting.entity.RcvLoan;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.UnitBankCashTransferLoader;
import pohaci.gumunda.titis.accounting.logic.BeginningBalanceBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.DoubleFormatted;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class PaymentLoanPanel extends RevTransactionPanel
implements ActionListener,PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	private PmtLoan m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;

	public PaymentLoanPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid);
		initExchangeRateHelper(m_conn, m_sessionid);
		setDefaultSignature();
		setEntity(new PmtLoan());
		m_entityMapper = MasterMap.obtainMapperFor(PmtLoan.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}

	private void initComponents() {
		detail=new LookupLoanPaymentDetail(m_conn, m_sessionid);
		AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid);
		PayToComp = new LookupLoanPicker(m_conn, m_sessionid);
		LoanReceiptComp = new LookupLoanReceiptPicker(m_conn, m_sessionid);
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
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanelKanan = new javax.swing.JPanel();
		VoucherNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		VoucherDateLabel = new javax.swing.JLabel();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		jPanelLoanPayment = new javax.swing.JPanel();
		LoanPaymentLabel = new javax.swing.JLabel();
		jSeparator2 = new javax.swing.JSeparator();
		CurrencyLoanPaymentLabel = new javax.swing.JLabel();
		ExchRateLoanPaymentLabel = new javax.swing.JLabel();
		LoanPaymentExchRateTextJF = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountLoanPaymentLabel = new javax.swing.JLabel();
		LoanPaymentAmountTextJF = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountBaseCurrLoanPaymentLabel = new javax.swing.JLabel();
		LoanPaymentAmountBaseTextJF = new javax.swing.JFormattedTextField(m_numberFormatter3);
		LoanPaymentAmountBaseCurrText = new javax.swing.JTextField();
		TotalReceiveLabel1 = new javax.swing.JLabel();
		TotalPaymentAmountTextJF = new javax.swing.JFormattedTextField(m_numberFormatter3);
		TotalPaymentCurrText = new javax.swing.JTextField();
		TotalReceiveLabel2 = new javax.swing.JLabel();
		EndingBalanceCurrText = new javax.swing.JTextField();
		EndingBalanceAmountTextJF = new javax.swing.JFormattedTextField(m_numberFormatter);
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
		PaymentSourceCombo = new javax.swing.JComboBox();
		ChequeNoText = new javax.swing.JTextField();
		PayToText = new javax.swing.JTextField();
		InitialLoanLabel = new javax.swing.JLabel();
		AccumulatedReceiveLabel = new javax.swing.JLabel();
		jPanelLoanReceipt = new javax.swing.JPanel();
		LoanReceiveLabel = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		CurrencyLoanReceiveLabel = new javax.swing.JLabel();
		ExchRateLoanReceiveLabel = new javax.swing.JLabel();
		LoanReceiptExchRateTextJF = new javax.swing.JFormattedTextField(m_numberFormatter3);
		AmountLoanReceiveLabel = new javax.swing.JLabel();
		LoanReceiptAmountTextJF = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountBaseCurrLoanReceiveLabel = new javax.swing.JLabel();
		LoanReceiptAmounBaseTextJF =  new javax.swing.JFormattedTextField(m_numberFormatter3);
		LoanReceiptAmountBaseCurrText = new javax.swing.JFormattedTextField(m_numberFormatter3);
		TotalReceiveLabel = new javax.swing.JLabel();
		AccumulatedPmtAmountTextJF = new javax.swing.JFormattedTextField(m_numberFormatter);
		AccumlatedPmtCurrText = new javax.swing.JTextField();
		ReceiptDateLabel = new javax.swing.JLabel();
		ReceiptNoLabel = new javax.swing.JLabel();
		ReceiptDateText = new javax.swing.JTextField();
		LoanPaymentDetailButton = new javax.swing.JButton();
		AccumulatedReceiveLabel1 = new javax.swing.JLabel();
		ChequeDueDateDate = new DatePicker();
		InitialLoanTextJF = new javax.swing.JFormattedTextField(m_numberFormatter);
		jPaneOriginatorComp = new javax.swing.JPanel();
		jPanelApprovedComp = new javax.swing.JPanel();
		jPaneReceivedComp = new javax.swing.JPanel();
		paymentCurrComp=new CurrencyPicker(m_conn,m_sessionid);
		receiptCurrComp=new CurrencyPicker(m_conn,m_sessionid);
		setLayout(new java.awt.BorderLayout());
		setPreferredSize(new java.awt.Dimension(700, 420));
		jPanel1.setLayout(new java.awt.BorderLayout());
		jPanel1.setPreferredSize(new java.awt.Dimension(670, 450));
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
		m_saveBtn.setText("Save");
		m_saveBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_saveBtn.setPreferredSize(new java.awt.Dimension(50, 20));

		TopButtonPanel.add(m_deleteBtn);
		m_cancelBtn.setText("Cancel");
		m_cancelBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_cancelBtn.setPreferredSize(new java.awt.Dimension(50, 20));

		TopButtonPanel.add(m_saveBtn);
		m_deleteBtn.setText("Delete");
		m_deleteBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_deleteBtn.setPreferredSize(new java.awt.Dimension(50, 20));

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

		VoucherNoLabel.setText("Voucher No");
		VoucherNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);

		jPanelKanan.add(VoucherNoLabel, gridBagConstraints);
		RefNoText.setPreferredSize(new java.awt.Dimension(295, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(RefNoText, gridBagConstraints);

		VoucherDateLabel.setText("Voucher Date");
		VoucherDateLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(VoucherDateLabel, gridBagConstraints);

		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(DescriptionLabel, gridBagConstraints);

		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(295, 75));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(DescriptionScrollPane, gridBagConstraints);

		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(RemarksLabel, gridBagConstraints);

		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(295, 75));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScrollPane.setViewportView(RemarksTextArea);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKanan.add(RemarksScrollPane, gridBagConstraints);

		jPanelLoanPayment.setLayout(new java.awt.GridBagLayout());
		jPanelLoanPayment.setPreferredSize(new java.awt.Dimension(402, 120));

		LoanPaymentLabel.setText("Loan Payment");
		LoanPaymentLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);

		jPanelLoanPayment.add(LoanPaymentLabel, gridBagConstraints);
		jSeparator2.setPreferredSize(new java.awt.Dimension(290, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(jSeparator2, gridBagConstraints);

		CurrencyLoanPaymentLabel.setText("Currency*");
		CurrencyLoanPaymentLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(CurrencyLoanPaymentLabel, gridBagConstraints);

		paymentCurrComp.setPreferredSize(new java.awt.Dimension(80, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(paymentCurrComp, gridBagConstraints);

		ExchRateLoanPaymentLabel.setText("Exch. Rate*");
		ExchRateLoanPaymentLabel.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(ExchRateLoanPaymentLabel, gridBagConstraints);

		LoanPaymentExchRateTextJF.setPreferredSize(new java.awt.Dimension(141, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(LoanPaymentExchRateTextJF, gridBagConstraints);

		AmountLoanPaymentLabel.setText("Amount*");
		AmountLoanPaymentLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(AmountLoanPaymentLabel, gridBagConstraints);

		LoanPaymentAmountTextJF.setPreferredSize(new java.awt.Dimension(294, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(LoanPaymentAmountTextJF, gridBagConstraints);

		AmountBaseCurrLoanPaymentLabel.setText("Amount Base Curr.");
		AmountBaseCurrLoanPaymentLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(AmountBaseCurrLoanPaymentLabel, gridBagConstraints);

		LoanPaymentAmountBaseTextJF.setPreferredSize(new java.awt.Dimension(205, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(LoanPaymentAmountBaseTextJF, gridBagConstraints);

		LoanPaymentAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(LoanPaymentAmountBaseCurrText, gridBagConstraints);

		TotalReceiveLabel1.setText("Total Payment");
		TotalReceiveLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(TotalReceiveLabel1, gridBagConstraints);

		TotalPaymentAmountTextJF.setPreferredSize(new java.awt.Dimension(205, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(TotalPaymentAmountTextJF, gridBagConstraints);

		TotalPaymentCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(TotalPaymentCurrText, gridBagConstraints);

		TotalReceiveLabel2.setText("Ending Balance");
		TotalReceiveLabel2.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(TotalReceiveLabel2, gridBagConstraints);

		EndingBalanceCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(EndingBalanceCurrText, gridBagConstraints);

		EndingBalanceAmountTextJF.setPreferredSize(new java.awt.Dimension(205, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanPayment.add(EndingBalanceAmountTextJF, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanelKanan.add(jPanelLoanPayment, gridBagConstraints);
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(295, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelKanan.add(TransactionDateDate, gridBagConstraints);

		jPanel1_2_2.add(jPanelKanan, java.awt.BorderLayout.WEST);
		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);
		jPanelKiri.setLayout(new java.awt.GridBagLayout());
		jPanelKiri.setPreferredSize(new java.awt.Dimension(410, 400));
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

		ReceiveToLabel.setText("Payment Source*");
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(ReceiveToLabel, gridBagConstraints);

		AccountLabel1.setText("Account");
		AccountLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccountLabel1, gridBagConstraints);

		CompanyLoanLabel.setText("Cheque No");
		CompanyLoanLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(CompanyLoanLabel, gridBagConstraints);

		AccountLabel2.setText("Cheque Due Date");
		AccountLabel2.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccountLabel2, gridBagConstraints);

		StatusText.setMargin(new java.awt.Insets(1, 2, 1, 1));
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

		PaymentSourceCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Cash","Bank" }));
		PaymentSourceCombo.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(PaymentSourceCombo, gridBagConstraints);

		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccountComp, gridBagConstraints);

		ChequeNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(ChequeNoText, gridBagConstraints);

		PayToText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(PayToText, gridBagConstraints);

		InitialLoanLabel.setText("Pay to*");
		InitialLoanLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(InitialLoanLabel, gridBagConstraints);

		AccumulatedReceiveLabel.setText("Initial Loan");
		AccumulatedReceiveLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccumulatedReceiveLabel, gridBagConstraints);

		jPanelLoanReceipt.setLayout(new java.awt.GridBagLayout());
		jPanelLoanReceipt.setPreferredSize(new java.awt.Dimension(410, 140));

		LoanReceiveLabel.setText("Loan Receipt");
		LoanReceiveLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(LoanReceiveLabel, gridBagConstraints);

		jSeparator1.setPreferredSize(new java.awt.Dimension(290, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelLoanReceipt.add(jSeparator1, gridBagConstraints);

		CurrencyLoanReceiveLabel.setText("Currency");
		CurrencyLoanReceiveLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(CurrencyLoanReceiveLabel, gridBagConstraints);

		receiptCurrComp.setPreferredSize(new java.awt.Dimension(75, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(receiptCurrComp, gridBagConstraints);

		ExchRateLoanReceiveLabel.setText("Exch. Rate");
		ExchRateLoanReceiveLabel.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(ExchRateLoanReceiveLabel, gridBagConstraints);

		LoanReceiptExchRateTextJF.setPreferredSize(new java.awt.Dimension(137, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelLoanReceipt.add(LoanReceiptExchRateTextJF, gridBagConstraints);

		AmountLoanReceiveLabel.setText("Amount");
		AmountLoanReceiveLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(AmountLoanReceiveLabel, gridBagConstraints);

		LoanReceiptAmountTextJF.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelLoanReceipt.add(LoanReceiptAmountTextJF, gridBagConstraints);

		AmountBaseCurrLoanReceiveLabel.setText("Amount Base Curr.");
		AmountBaseCurrLoanReceiveLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(AmountBaseCurrLoanReceiveLabel, gridBagConstraints);

		LoanReceiptAmounBaseTextJF.setPreferredSize(new java.awt.Dimension(201, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelLoanReceipt.add(LoanReceiptAmounBaseTextJF, gridBagConstraints);

		LoanReceiptAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(LoanReceiptAmountBaseCurrText, gridBagConstraints);

		TotalReceiveLabel.setText("Accumulated Payment");
		TotalReceiveLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(TotalReceiveLabel, gridBagConstraints);

		AccumulatedPmtAmountTextJF.setPreferredSize(new java.awt.Dimension(163, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelLoanReceipt.add(AccumulatedPmtAmountTextJF, gridBagConstraints);

		AccumlatedPmtCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(AccumlatedPmtCurrText, gridBagConstraints);

		ReceiptDateLabel.setText("Receipt Date");
		ReceiptDateLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(ReceiptDateLabel, gridBagConstraints);

		LoanReceiptComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelLoanReceipt.add(LoanReceiptComp, gridBagConstraints);

		ReceiptNoLabel.setText("Receipt No*");
		ReceiptNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelLoanReceipt.add(ReceiptNoLabel, gridBagConstraints);

		ReceiptDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelLoanReceipt.add(ReceiptDateText, gridBagConstraints);

		LoanPaymentDetailButton.setText("...");
		LoanPaymentDetailButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
		LoanPaymentDetailButton.setPreferredSize(new java.awt.Dimension(34, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
		jPanelLoanReceipt.add(LoanPaymentDetailButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanelKiri.add(jPanelLoanReceipt, gridBagConstraints);

		AccumulatedReceiveLabel1.setText("Pay to Account");
		AccumulatedReceiveLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccumulatedReceiveLabel1, gridBagConstraints);

		ChequeDueDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(ChequeDueDateDate, gridBagConstraints);

		PayToComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(PayToComp, gridBagConstraints);

		InitialLoanTextJF.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(InitialLoanTextJF, gridBagConstraints);

		jPanel1_2.add(jPanelKiri, java.awt.BorderLayout.WEST);
		jPanel1.add(jPanel1_2, java.awt.BorderLayout.CENTER);
		jPaneOriginatorComp.setLayout(new java.awt.BorderLayout());
		jPaneOriginatorComp.setPreferredSize(new java.awt.Dimension(700, 90));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());
		OriginatorComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(275,110));

		jPaneOriginatorComp.add(OriginatorComp, java.awt.BorderLayout.WEST);
		jPanelApprovedComp.setLayout(new java.awt.BorderLayout());
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(275,110));

		jPanelApprovedComp.add(ApprovedComp, java.awt.BorderLayout.WEST);
		jPaneReceivedComp.setLayout(new java.awt.BorderLayout());
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(275,110));

		jPaneReceivedComp.add(ReceivedComp, java.awt.BorderLayout.WEST);
		jPanelApprovedComp.add(jPaneReceivedComp, java.awt.BorderLayout.CENTER);
		jPaneOriginatorComp.add(jPanelApprovedComp, java.awt.BorderLayout.CENTER);
		jPanel1.add(jPaneOriginatorComp, java.awt.BorderLayout.SOUTH);
		add(jPanel1, java.awt.BorderLayout.NORTH);
	}

	private void addingListener(){
		PaymentSourceCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		LoanPaymentAmountTextJF.addPropertyChangeListener( this);
		LoanPaymentExchRateTextJF.addPropertyChangeListener( this);
		LoanReceiptAmountTextJF.getDocument().addDocumentListener(new ReceiptAmountOnly());
		LoanReceiptExchRateTextJF.getDocument().addDocumentListener(new ReceiptAmountOnly());
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		PayToComp.addPropertyChangeListener("object", this);
		LoanReceiptComp.addPropertyChangeListener("object", this);
		LoanPaymentDetailButton.addActionListener(this);
		AccountComp.addPropertyChangeListener(this);
		TransactionDateDate.addPropertyChangeListener("date", this);
		paymentCurrComp.addPropertyChangeListener("object", this);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		else if (evt.getSource()== paymentCurrComp){
			Currency curr = (Currency) paymentCurrComp.getObject();
			setDefaultExchangeRate(curr);
		}
		else if (evt.getSource() == PayToComp){
			if (PayToComp.getObject()!=null){
				setGuiCompanyLoan();
				receiptCurrComp.setObject(null);
				LoanReceiptExchRateTextJF.setValue(null);
				LoanReceiptAmountTextJF.setValue(null);
				detail.setRcvLoan(null);
				LoanReceiptAmountBaseCurrText.setText(null);
				AccumlatedPmtCurrText.setText(null);
				AccumulatedPmtAmountTextJF.setValue(null);
				paymentCurrComp.setObject(null);
				LoanPaymentExchRateTextJF.setValue(null);
				LoanPaymentAmountTextJF.setValue(null);
				LoanPaymentAmountBaseCurrText.setText(null);
				TotalPaymentCurrText.setText(null);
				EndingBalanceCurrText.setText(null);
				EndingBalanceAmountTextJF.setText(null);
			}else{
				PayToText.setText("");
				InitialLoanTextJF.setText("");
			}
		}
		else if (evt.getSource() == LoanReceiptComp ){
			if (LoanReceiptComp != null ){
				Object obj = LoanReceiptComp.getObject();
				if (obj instanceof RcvLoan) {
					RcvLoan tempClass = (RcvLoan) obj;
					if(tempClass!=null && tempClass.getExchangeRate()>0){
						LoanReceiptExchRateTextJF.setValue(new Double(tempClass.getExchangeRate()));
					}else{
						LoanReceiptExchRateTextJF.setValue(new Double(1));
					}

					detail.setRcvLoan(tempClass);
					LoanReceiptAmountBaseCurrText.setText(baseCurrency.getSymbol());
					Currency currency = tempClass.getCurrency();
					receiptCurrComp.setObject(tempClass.getCurrency());
					AccumlatedPmtCurrText.setText(currency.getSymbol());
					AccumulatedPmtAmountTextJF.setValue(new Double(detail.getAccumulatedPayment()));
					ReceiptDateText.setText(tempClass.getTransactionDate().toString());
					LoanReceiptAmountTextJF.setValue(new Double(tempClass.getAmount()));

					paymentCurrComp.setObject(tempClass.getCurrency());
					if(currency.getSymbol().equals(baseCurrency.getSymbol())){
						//LoanPaymentExchRateTextJF.setValue(new Double(1));
						LoanPaymentExchRateTextJF.setEditable(false);
					}else{
						//LoanPaymentExchRateTextJF.setValue(new Double(entity().getExchangeRate()));
					}
					setDefaultExchangeRate(tempClass.getCurrency());

					LoanPaymentAmountTextJF.setValue(new Double(entity().getAmount()));
					LoanPaymentAmountBaseCurrText.setText(baseCurrency.getSymbol());
					TotalPaymentCurrText.setText(currency.getSymbol());
					EndingBalanceCurrText.setText(currency.getSymbol());

					new PaymentAmountOnly().toBaseCurrency();
				}else if (obj instanceof BeginningLoan) {
					BeginningLoan tempClass = (BeginningLoan) obj;
					String curr = tempClass.getCurrency().getSymbol();

					receiptCurrComp.setObject(tempClass.getCurrency());
					LoanReceiptExchRateTextJF.setValue(new Double(tempClass.getExchangeRate()));
					LoanReceiptAmountTextJF.setValue(new Double(tempClass.getAccValue()));

					detail.setRcvLoan(tempClass);
					LoanReceiptAmountBaseCurrText.setText(baseCurrency.getSymbol());
					AccumlatedPmtCurrText.setText(curr);
					AccumulatedPmtAmountTextJF.setValue(new Double(detail.getAccumulatedPayment()));

					paymentCurrComp.setObject(tempClass.getCurrency());
					if (curr.equals(baseCurrency.getSymbol())){
						//LoanPaymentExchRateTextJF.setValue(new Double(1));
						LoanPaymentExchRateTextJF.setEditable(false);
					}else{
						//LoanPaymentExchRateTextJF.setValue(new Double(entity().getExchangeRate()));
					}

					setDefaultExchangeRate(tempClass.getCurrency());

					LoanPaymentAmountTextJF.setValue(new Double(entity().getAmount()));
					LoanPaymentAmountBaseCurrText.setText(baseCurrency.getSymbol());
					TotalPaymentCurrText.setText(curr);
					EndingBalanceCurrText.setText(curr);
					new PaymentAmountOnly().toBaseCurrency();
				}

			}else{
				detail.setRcvLoan(null);
				AccumulatedPmtAmountTextJF.setValue(new Double(0));
				LoanReceiptAmountTextJF.setValue(new Double(0) );
				ReceiptDateText.setText("");
				LoanReceiptExchRateTextJF.setValue(null);
				LoanReceiptAmountBaseCurrText.setText("");
				receiptCurrComp.setObject(null);
				new PaymentAmountOnly().toBaseCurrency();
			}
		}
		else if (evt.getSource() == LoanPaymentAmountTextJF || evt.getSource() == LoanPaymentExchRateTextJF ){
			new PaymentAmountOnly().toBaseCurrency();
		}
		else if (evt.getSource() == AccountComp ){
			CashBankAccount cashBankAccount=(CashBankAccount)AccountComp.getObject();
			if(cashBankAccount!=null){
				//paymentCurrComp.setObject(cashBankAccount.getCurrency());
				if (cashBankAccount.getCurrency().getSymbol().equalsIgnoreCase(this.baseCurrency.getSymbol())){
					LoanPaymentExchRateTextJF.setEnabled(false);
					//LoanPaymentExchRateTextJF.setValue(new Double(1));
					//LoanReceiptExchRateTextJF.setValue(new Double(1));
				}else{
					LoanPaymentExchRateTextJF.setEnabled(true);
					//setDefaultExchangeRate(cashBankAccount.getCurrency());
				}
			}else{
				//paymentCurrComp.setObject(null);
			}
		}
		else if (evt.getSource() == TransactionDateDate) {
			if ("date".equals(evt.getPropertyName())) {
				CashBankAccount cashBankAccount = (CashBankAccount) AccountComp.getObject();
				if (cashBankAccount!=null){
					Currency curr = cashBankAccount.getCurrency();
					setDefaultExchangeRate(curr);
				}
			}
		}
	}

	private void setDefaultExchangeRate(Currency currency) {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		LoanPaymentExchRateTextJF.setValue(new Double(rate));
	}

	private void setGuiCompanyLoan() {
		Object obj = PayToComp.getObject();
		CompanyLoan tempClass = (CompanyLoan) obj;
		changeLoanReceipt(tempClass);
		PayToComp.setObject(tempClass);
		LoanReceiptComp.refreshData(tempClass);
		new ReceiptAmountOnly().toBaseCurrency();
		new PaymentAmountOnly().toBaseCurrency();
		detail.clearTable();
		detail.setCompanyLoan(tempClass,entity());
		PayToText.setText(tempClass.getAccount().getCode());
		InitialLoanTextJF.setText(tempClass.getCurrency().getSymbol()+" "
				+ new DoubleFormatted(tempClass.getInitial()));
		revalidateAccount();
		AccountComp.setObject(null);
	}

	protected void clearForm() {
		clearTextField(jPanelKanan);
		clearTextField(jPanelLoanPayment);
		clearTextField(jPanelLoanReceipt);
		clearTextField(jPanelKiri);
		clearTextField(jPanel1_2_2);
		clearTextField(jPanelApprovedComp);
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

	protected void clearAll() {
		super.clearAll();
		clearKomponen();
		clearForm();
		disableEditMode();
	}

	protected void doSubmit() {
		validityMsgs.clear();
		if (AccountComp.getObject()==null)
			validityMsgs.add("Account source must be selected");
		if (validityMsgs.size()>0){
			StringBuffer result = new StringBuffer();
			Iterator iter = validityMsgs.iterator();
			while (iter.hasNext()){
				String o = iter.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this,result);
			return;
		}
		super.doSubmit();
	}

	protected void doNew() {
		super.doNew();
		isiDefaultAssignPanel();
		clearForm();
		detail.setRcvLoan(null);
	}

	public void clearKomponen(){
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



	public void actionPerformed(ActionEvent e){
		if(e.getSource() == PaymentSourceCombo) {
			revalidateAccount();
		}
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new Vchr_loan(m_entity,paymentCurrComp.getObject());
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			doSearch();
		}else if(e.getSource() == LoanPaymentDetailButton) {
			detail.procesData();
			detail.done1();
		}
		else if (e.getSource()==m_newBtn){
			this.ChequeNoText.setEditable(false);
			this.ChequeDueDateDate.setEditable(false);
		}
	}


	private void doSearch() {
		SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame.getMainFrame(), "Search Voucher", m_conn, m_sessionid,
				new UnitBankCashTransferLoader(m_conn,PmtLoan.class), "Loan");
		dlg.setVisible(true);
		Object obj = dlg.selectedObj;
		if ( obj!= null){
			doLoad(obj);
			LoanPaymentDetailButton.setEnabled(true);
		}
	}

	private void revalidateAccount() {
		Object obj = PayToComp.getObject();
		Currency currency = null;
		if (obj instanceof CompanyLoan) {
			CompanyLoan tempClass = (CompanyLoan) obj;
			currency = tempClass.getCurrency();
		}else if (obj instanceof BeginningLoan) {
			BeginningLoan tempClass = (BeginningLoan) obj;
			currency = tempClass.getCompanyLoan().getCurrency();
		}

		String receive = (String)PaymentSourceCombo.getSelectedItem();
		jPanelKiri.remove(AccountComp);
		jPanelKiri.revalidate();
		jPanelKiri.repaint();
		AccountComp.removePropertyChangeListener(this);
		String attribute = "";
		if(receive.equalsIgnoreCase("CASH")){
			AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid,currency);
			ChequeNoText.setText("");
			ChequeDueDateDate.setDate(null);
			this.ChequeNoText.setEditable(false);
			this.ChequeDueDateDate.setEditable(false);
			attribute = IConstants.ATTR_PMT_CASH;
		}else if (receive.equalsIgnoreCase("BANK")){
			AccountComp = new LookupBankAccountPicker(m_conn, m_sessionid,currency);
			ChequeNoText.setText("");
			ChequeDueDateDate.setDate(null);
			this.ChequeNoText.setEditable(true);
			this.ChequeDueDateDate.setEditable(true);
			attribute = IConstants.ATTR_PMT_BANK;
		}
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanelKiri.add(AccountComp, gridBagConstraints);
		jPanelKiri.revalidate();

		if(LoanReceiptComp.getObject()==null)
			return;

		if(!(LoanReceiptComp.getObject() instanceof RcvLoan))
			return;

		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(m_conn, m_sessionid, IDBConstants.MODUL_ACCOUNTING);
		List list = helper.getJournalStandardSettingWithAccount(IConstants.PAYMENT_LOAN, attribute);
		JournalStandardSetting setting = (JournalStandardSetting) list.get(0);
		JournalStandard journalStandard = setting.getJournalStandard();

		//boolean find = false;
		RcvLoan rcvLoan = (RcvLoan) LoanReceiptComp.getObject();
		JournalStandardAccount[] accounts = journalStandard.getJournalStandardAccount();
		for(int i=0; i<accounts.length; i++){
			JournalStandardAccount account = accounts[i];
			if(account.getAccount().getIndex()==rcvLoan.getCompanyLoan().getAccount().getIndex()){
				//find = true;
				break;
			}
		}
		/*if(!find)
			AccountComp.setEnabled(false);
		else
			AccountComp.setEnabled(true);*/
	}

	private void changeLoanReceipt(CompanyLoan companyLoan) {
		jPanelLoanReceipt.remove(LoanReceiptComp);
		jPanelLoanReceipt.revalidate();
		jPanelLoanReceipt.repaint();
		LoanReceiptComp.removePropertyChangeListener(this);
		LoanReceiptComp = new LookupLoanReceiptPicker(m_conn, m_sessionid,companyLoan);
		LoanReceiptComp.addPropertyChangeListener("object", this);
		LoanReceiptComp.setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanelLoanReceipt.add(LoanReceiptComp, gridBagConstraints);
		jPanelLoanReceipt.revalidate();
	}

	protected void doSave() {
		if (!cekValidity()) return;
		double totalPayment=((Number)TotalPaymentAmountTextJF.getValue()).doubleValue();
		double amountReceipt= ((Number)LoanReceiptAmounBaseTextJF.getValue()).doubleValue();
		if(totalPayment>amountReceipt){
			JOptionPane.showMessageDialog(this,
			" Your Total Payment exceeding the total Loan Amount Receipt. Please Check your loan payment amount");
			return;
		}
		super.doSave();
		AccountingSQLSAP sql=new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_LOAN, m_conn);
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

	private javax.swing.JLabel AccountLabel1;
	private javax.swing.JLabel AccountLabel2;
	private LookupPicker AccountComp;
	private LookupLoanPicker PayToComp;
	private javax.swing.JTextField AccumlatedPmtCurrText;
	private javax.swing.JFormattedTextField AccumulatedPmtAmountTextJF;
	private javax.swing.JLabel AccumulatedReceiveLabel;
	private javax.swing.JLabel AccumulatedReceiveLabel1;
	private javax.swing.JLabel AmountBaseCurrLoanPaymentLabel;
	private javax.swing.JLabel AmountBaseCurrLoanReceiveLabel;
	private javax.swing.JLabel AmountLoanPaymentLabel;
	private javax.swing.JLabel AmountLoanReceiveLabel;
	private DatePicker ChequeDueDateDate;
	private javax.swing.JTextField ChequeNoText;
	private javax.swing.JLabel CompanyLoanLabel;
	private javax.swing.JLabel CurrencyLoanPaymentLabel;
	private javax.swing.JLabel CurrencyLoanReceiveLabel;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JFormattedTextField EndingBalanceAmountTextJF;
	private javax.swing.JTextField EndingBalanceCurrText;
	private javax.swing.JLabel ExchRateLoanPaymentLabel;
	private javax.swing.JLabel ExchRateLoanReceiveLabel;
	private javax.swing.JLabel InitialLoanLabel;
	private javax.swing.JFormattedTextField InitialLoanTextJF;
	private javax.swing.JTextField LoanPaymentAmountBaseCurrText;
	private javax.swing.JFormattedTextField LoanPaymentAmountBaseTextJF;//
	private javax.swing.JFormattedTextField LoanPaymentAmountTextJF;//
	private javax.swing.JButton LoanPaymentDetailButton;
	private javax.swing.JFormattedTextField LoanPaymentExchRateTextJF;
	private javax.swing.JLabel LoanPaymentLabel;
	private javax.swing.JFormattedTextField LoanReceiptAmounBaseTextJF;
	private javax.swing.JTextField LoanReceiptAmountBaseCurrText;
	private javax.swing.JFormattedTextField LoanReceiptAmountTextJF;
	private LookupLoanReceiptPicker LoanReceiptComp;
	private javax.swing.JFormattedTextField LoanReceiptExchRateTextJF;
	private javax.swing.JLabel LoanReceiveLabel;
	private javax.swing.JTextField PayToText;
	private javax.swing.JComboBox PaymentSourceCombo;
	private javax.swing.JLabel ReceiptDateLabel;
	private javax.swing.JTextField ReceiptDateText;
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
	private javax.swing.JFormattedTextField TotalPaymentAmountTextJF;
	private javax.swing.JTextField TotalPaymentCurrText;
	private javax.swing.JLabel TotalReceiveLabel;
	private javax.swing.JLabel TotalReceiveLabel1;
	private javax.swing.JLabel TotalReceiveLabel2;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JLabel VoucherDateLabel;
	private javax.swing.JLabel VoucherNoLabel;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel TopButtonPanel;
	private javax.swing.JPanel jPanel1_2;
	private javax.swing.JPanel jPanelKiri;
	private javax.swing.JPanel jPanelLoanReceipt;
	private javax.swing.JPanel jPanelLoanPayment;
	private javax.swing.JPanel jPanel1_2_2;
	private javax.swing.JPanel jPanelKanan;
	private javax.swing.JPanel jPaneOriginatorComp;
	private AssignPanel OriginatorComp;
	private javax.swing.JPanel jPanelApprovedComp;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel jPaneReceivedComp;
	private AssignPanel ReceivedComp;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private CurrencyPicker  paymentCurrComp;
	private CurrencyPicker  receiptCurrComp;
	private LookupLoanPaymentDetail detail;

	protected StateTemplateEntity currentEntity() {
		return entity();
	}

	PmtLoan entity() {
		return m_entity;
	}

	protected void gui2entity() {
		Object objk = LoanReceiptComp.getObject();
		if(objk instanceof RcvLoan){
			RcvLoan tempClass = (RcvLoan)objk;
			entity().setLoanReceipt(tempClass);
			entity().setBeginningBalance(null);
			Currency currency = tempClass.getCurrency();
			entity().setCurrency(currency);
		}
		else if (objk instanceof BeginningLoan) {
			BeginningLoan tempClass = (BeginningLoan) objk;
			entity().setBeginningBalance(tempClass);
			entity().setLoanReceipt(null);
			entity().setCurrency(tempClass.getCurrency());
		}

		String paySource = (String) PaymentSourceCombo.getSelectedItem();
		entity().setPaymentSource(paySource);
		Object objAcc = AccountComp.getObject();
		if (objAcc instanceof BankAccount) {
			BankAccount bank = (BankAccount) objAcc;
			entity().setBankAccount(bank);
			entity().setCashAccount(null);
			entity().setUnit(bank.getUnit());
		}else if (objAcc instanceof CashAccount) {
			CashAccount cash = (CashAccount) objAcc;
			entity().setCashAccount(cash);
			entity().setBankAccount(null);
			entity().setUnit(cash.getUnit());

		}else{
			entity().setCashAccount(null);
			entity().setBankAccount(null);
			Unit unit = DefaultUnit.createDefaultUnit(m_conn,m_sessionid);
			entity().setUnit(unit);
		}

		entity().setChequeNo(ChequeNoText.getText());
		entity().setChequeDueDate(ChequeDueDateDate.getDate());
		entity().setPayTo(((CompanyLoan)PayToComp.getObject()));

		entity().setExchangeRate(((Number)LoanPaymentExchRateTextJF.getValue()).doubleValue());

		Object obj = LoanPaymentAmountTextJF.getValue();
		Number amount = (Number) obj;
		entity().setAmount(amount.doubleValue());

		entity().setTransactionDate(TransactionDateDate.getDate());
		entity().setRemarks(RemarksTextArea.getText());
		entity().transTemplateRead(
				this.OriginatorComp, this.ApprovedComp,
				this.ReceivedComp, this.RefNoText,
				this.DescriptionTextArea
		);
	}

	protected void entity2gui() {
		if (entity().getLoanReceipt()!=null){
			PayToComp.setObject(entity().getLoanReceipt().getCompanyLoan());
			LoanReceiptComp.setObject(entity().getLoanReceipt());
			receiptCurrComp.setObject(baseCurrency);
		}else if (entity().getBeginningBalance()!=null){
			PayToComp.setObject(entity().getBeginningBalance().getCompanyLoan());
			LoanReceiptComp.setObject(entity().getBeginningBalance());
		}else{
			paymentCurrComp.setObject(null);
			PayToComp.setObject(null);
			LoanReceiptComp.setObject(null);
			receiptCurrComp.setObject(null);
		}

		if (entity().getExchangeRate() > 0)
			LoanPaymentExchRateTextJF.setValue(new Double(entity().getExchangeRate()));

		RefNoText.setText(entity().getReferenceNo());
		if(entity().getTransactionDate()!=null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else{
			TransactionDateDate.setDate(new Date());
		}

		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");

		if (entity().getPaymentSource()!=null)
			PaymentSourceCombo.setSelectedItem(entity().getPaymentSource());

		if(entity().getBankAccount()!= null)
			AccountComp.setObject(entity().getBankAccount());
		else if (entity().getCashAccount()!=null)
			AccountComp.setObject(entity().getCashAccount());

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
		//if (entity().getPayTo()!=null)
			detail.setCompanyLoan(entity().getPayTo(),entity());
		/*else
			detail.setCompanyLoan(null);*/

		/*if(entity().getStatus()==StateTemplateEntity.State.POSTED){
			double sum = detail.getAccumulatedPayment()-((Number)LoanPaymentAmountTextJF.getValue()).doubleValue();
			AccumulatedPmtAmountTextJF.setValue(new Double(sum));
		}else{
			AccumulatedPmtAmountTextJF.setValue(new Double(detail.getAccumulatedPayment()));
		}*/

		ChequeNoText.setText(entity().getChequeNo());
		ChequeDueDateDate.setDate(entity().getChequeDueDate());
	}

	protected void enableEditMode(){
		this.PaymentSourceCombo.setEnabled(true);
		this.AccountComp.setEnabled(true);
		this.LoanPaymentAmountBaseCurrText.setEnabled(true);
		this.ChequeNoText.setEditable(true);
		this.ChequeDueDateDate.setEditable(true);
		this.paymentCurrComp.setEnabled(true);
		this.receiptCurrComp.setEnabled(true);
		this.LoanPaymentExchRateTextJF.setEditable(true);
		this.LoanPaymentAmountTextJF.setEditable(true);
		this.TransactionDateDate.setEditable(true);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
		paymentCurrComp.setEnabled(false);
		receiptCurrComp.setEnabled(false);
		detail.setEnabled(true);
		LoanReceiptComp.setEnabled(true);
		PayToComp.setEnabled(true);
		LoanPaymentDetailButton.setEnabled(true);
	}

	protected void disableEditMode(){
		this.PaymentSourceCombo.setEnabled(false);
		this.AccountComp.setEnabled(false);
		this.LoanReceiptAmountBaseCurrText.setEnabled(false);
		this.LoanPaymentAmountBaseCurrText.setEnabled(false);
		this.ChequeNoText.setEditable(false);
		this.ChequeDueDateDate.setEditable(false);
		this.paymentCurrComp.setEnabled(false);
		this.receiptCurrComp.setEnabled(false);
		this.LoanPaymentExchRateTextJF.setEditable(false);
		this.LoanReceiptExchRateTextJF.setEditable(false);
		this.LoanReceiptExchRateTextJF.setEnabled(false);
		this.LoanPaymentAmountTextJF.setEditable(false);
		this.LoanReceiptAmountTextJF.setEditable(false);
		this.TransactionDateDate.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
		LoanReceiptComp.setEnabled(false);
		PayToComp.setEnabled(false);
		LoanPaymentDetailButton.setEnabled(false);
	}

	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (TransactionDateDate.getDate() == null)
			validityMsgs.add("Transaction date must be selected");
		if (PayToComp.getObject() == null)
			validityMsgs.add("Pay to must be inserted");
		if (DescriptionTextArea.getText().equals(""))
			validityMsgs.add("Description must be inserted");
		if (LoanPaymentAmountTextJF.getText().equals(""))
			validityMsgs.add("Amount value must be inserted");
		if (LoanReceiptComp.getObject()==null)
			validityMsgs.add("Receipt No. must be inserted");
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

	protected Object createNew(){
		PmtLoan a  = new PmtLoan();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}

	void setEntity(Object m_entity) {
		PmtLoan oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PmtLoan)m_entity;
		this.m_entity.addPropertyChangeListener(this);
		if(this.m_entity.getBeginningBalance()!=null){
			setBeginningBalance();
		}
	}

	private void setBeginningBalance() {
		BeginningLoan bb = this.m_entity.getBeginningBalance();
		BeginningBalanceBusinessLogic logic = new BeginningBalanceBusinessLogic(m_conn, m_sessionid);
		bb.setTrans(logic.findTransaction(bb.getCompanyLoan().getUnit()));
		bb.showReferenceNo(true);
		this.m_entity.setBeginningBalance(bb);
	}

	private void enableAwal(){
		setenableEditPanel(jPanelKiri,false);
		setenableEditPanel(jPanelLoanReceipt,false);
		setenableEditPanel(jPanelKanan,false);
		setenableEditPanel(jPanelLoanPayment,false);
		setenableEditPanel(jPanelKanan,false);
		setenableEditPanel(jPanel1_2_2,false);
		paymentCurrComp.setEnabled(false);
		receiptCurrComp.setEnabled(false);
	}

	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_LOAN, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_LOAN, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_LOAN, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}

	class PaymentAmountOnly implements DocumentListener{
		private   void toBaseCurrency() {
			try{
				double amount,crate;
				if (LoanPaymentAmountTextJF.getValue()!=null)
					amount=((Number)LoanPaymentAmountTextJF.getValue()).doubleValue();
				else
					amount=0;
				if(LoanPaymentExchRateTextJF.getValue()!=null)
					crate=((Number)LoanPaymentExchRateTextJF.getValue()).doubleValue();
				else
					crate=1;
				Double paymentAmountBase=new Double(amount*crate);
				LoanPaymentAmountBaseTextJF.setValue(paymentAmountBase);
				double total=amount+detail.getAccumulatedPayment();
				TotalPaymentAmountTextJF.setValue(new Double(total));
				if(LoanReceiptAmountTextJF.getValue() !=null){
					double loan=((Number)LoanReceiptAmountTextJF.getValue()).doubleValue();
					double balance =loan - total;
					EndingBalanceAmountTextJF.setValue(new Double(balance));
				}
			}catch (Exception e){
				e.printStackTrace();
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

	}

	class ReceiptAmountOnly implements DocumentListener{
		private void toBaseCurrency() {
			try{
				double amount,crate;
				if (LoanReceiptAmountTextJF.getValue()!=null)
					amount=((Number)LoanReceiptAmountTextJF.getValue()).doubleValue();
				else
					amount=0;
				if(LoanReceiptExchRateTextJF.getValue()!=null)
					crate=((Number)LoanReceiptExchRateTextJF.getValue()).doubleValue();
				else
					crate=1;
				LoanReceiptAmounBaseTextJF.setValue(new Double(amount*crate));
			}catch (Exception e){
				e.printStackTrace();
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
	}
}
