package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountPayable;
import pohaci.gumunda.titis.accounting.cgui.StateTemplateEntity.State;
import pohaci.gumunda.titis.accounting.cgui.report.Vchr_APPayment;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmtLoader;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.logic.BeginningAccountPayableBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.BeginningBalanceBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.PurchaseAccountPayablePaymentBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.PurchaseReceiptBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class PurchaseAccountPayablePaymentPanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener,DocumentListener{
	private static final long serialVersionUID = 1L;
	private PurchaseApPmt m_entity;
	protected Employee m_defaultOriginator,m_defaultApproved,m_defaultReceived;
	DecimalFormat m_formatDesimal = null;
	private JFormattedTextField m_APAmountText,m_APBaseAmountText,m_APPaymentAmountText,m_APPaymentExchRateText,
	m_APPaymentTax23AmountText,m_APPaymentTax23PercentText,m_accumulatedPaymentAmountText,m_beginningBalanceAmountText,
	m_endBalaceAmountText,m_exchRateText,m_purchaseAmountText,m_totalPaymentAmountText, m_BegBalanceBasePurcRecAmountText,m_EndBalanceBaseAccPayAmountText;
	private JTextField m_APBaseCurrText,m_APCurrText,/*m_APPaymentCurrText,*/m_APPaymentTax23CurrText,m_activityText,
	m_accumulatedPaymentCurrText,m_beginningBalanceCurrText,m_chequeNoText,m_contractNoText,m_currText,
	m_customerText,	m_departmentText,m_endBalanceCurrText,m_IPCNoText,m_invoiceDateText,m_invoiceNoText,m_ORNoText,
	m_paytoText,m_projectText,m_purchRcptDateText,m_purchaseCurrText,m_refNoText,m_statusText,m_submittedDateText,
	m_totalPaymentCurrText,m_unitText,m_BegBalanceBaseCurrPurcRecText,m_EndBalanceBaseCurrAccPayText;
	private LookupPicker m_accountComp,detailAmount;
	private LookupPurchasePicker m_purchRcptNoComp;
	CurrencyPicker m_currpayment;
	private JLabel m_accountLbl,m_activityCodeLbl,m_amountBaseCurrentLbl,m_amountLbl,m_chequeDueDateLbl,
	m_chequeNumberLbl,m_currencyLbl,m_currencyLbl1,m_currencyLbl6,m_customerLbl,m_departmentLbl,
	m_descriptionLbl,m_endingBalanceLbl,m_exchRateLbl,m_exchRateLbl1,m_IPCNoLbl,m_invoiceDateLbl,
	m_invoiceNoLbl,m_ORNoLbl,m_payToLbl,m_paymentSourceLbl,m_projectLbl,m_purchRcptDateLbl,m_purchRcptNoLbl,
	m_SOPOContractNoLbl,m_statusLbl,m_separatorPurcRecLbl,m_subTotalLbl1,m_submittedDateLbl,m_remarkLbl,m_separatorAccPayLbl,
	m_accPayLbl,m_supplierBankLbl,m_taxArt23Lbl,m_BBLbl,m_totalPaymentLbl,m_unitCodeLbl,m_VATLbl3,
	m_VoucherDateLbl,m_voucherNoLbl,m_workDescLbl,m_BegBalanceBaseCurrPurcRecLbl,m_EndBalanceBaseCurrAccPayLbl;
	private JTextArea m_descrptionTextArea,m_remarksTextArea,m_supplierBankTextArea,m_workDescTextArea;
	private JButton m_detailComp;
	private JComboBox m_paymentSourceCombo;
	private JScrollPane m_remarkScroll,m_supplierBankScroll,m_decriptionScroll,m_workDescScroll;
	private pohaci.gumunda.titis.application.DatePicker m_transactionDateDate,m_chequeDueDateDate;
	private JPanel m_purchaseOrderPanel,m_purchaseReceiptPanel,m_topButtonPanel,m_jPanel1,m_jPanel1_1,
	m_jPanel1_2,m_jPanel1_2_1,m_accPayPanel,m_jPanel1_2_2,m_leftPanel,m_jPanel1_2_2_1_1,m_rightPanel,
	m_leftTopPanel,m_addOriginatorPanel,m_addApprovedPanel,m_receivedAddPanel;
	private AssignPanel m_originatorPanel,m_approvedPanel,m_receivedPanel;
	private JSeparator m_jSeparator1,m_jSeparator2;

	public PurchaseAccountPayablePaymentPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid);
		initExchangeRateHelper(m_conn, m_sessionid);
		setDefaultSignature();
		setEntity(new PurchaseApPmt());
		m_entityMapper = MasterMap.obtainMapperFor(PurchaseApPmt.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}

	private void setDefaultExchangeRate(Currency currency) {
		Date date = m_transactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		m_APPaymentExchRateText.setValue(new Double(rate));
	}

	private void initComponents() {
		m_formatDesimal = new DecimalFormat("#,##0.00");
		setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid));
		m_purchRcptNoComp = new LookupPurchasePicker(m_conn, m_sessionid);
		m_originatorPanel = new AssignPanel(m_conn, m_sessionid,"Originator");
		m_approvedPanel = new AssignPanel(m_conn, m_sessionid,"Approved by");
		m_receivedPanel = new AssignPanel(m_conn, m_sessionid,"Received by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		m_topButtonPanel = new JPanel();

		GridBagConstraints gridBagConstraints;
		m_jPanel1 = new JPanel();
		m_jPanel1_1 = new JPanel();
		m_newBtn = new JButton();
		m_editBtn = new JButton();
		m_saveBtn = new JButton();
		m_deleteBtn = new JButton();
		m_cancelBtn = new JButton();
		m_submitBtn = new JButton();
		m_jPanel1_2 = new JPanel();
		m_jPanel1_2_2 = new JPanel();
		m_leftPanel = new JPanel();
		m_statusLbl = new JLabel();
		m_submittedDateLbl = new JLabel();
		m_paymentSourceLbl = new JLabel();
		m_accountLbl = new JLabel();
		m_chequeNumberLbl = new JLabel();
		m_chequeDueDateLbl = new JLabel();
		m_purchaseReceiptPanel = new JPanel();
		m_separatorPurcRecLbl = new JLabel();
		m_jSeparator1 = new JSeparator();
		m_purchRcptNoLbl = new JLabel();
		m_purchRcptDateLbl = new JLabel();
		m_invoiceNoLbl = new JLabel();
		m_invoiceDateLbl = new JLabel();
		m_payToLbl = new JLabel();
		m_supplierBankLbl = new JLabel();
		m_currencyLbl = new JLabel();
		m_EndBalanceBaseCurrAccPayLbl = new JLabel();
		m_BegBalanceBaseCurrPurcRecLbl = new JLabel();

		m_statusText = new JTextField();
		m_submittedDateText = new JTextField();
		m_chequeNoText = new JTextField();
		m_chequeDueDateDate = new DatePicker();
		m_purchRcptDateText = new JTextField();
		m_invoiceNoText = new JTextField();
		m_invoiceDateText = new JTextField();
		m_paytoText = new JTextField();
		m_BegBalanceBaseCurrPurcRecText = new JTextField();
		m_EndBalanceBaseCurrAccPayText = new JTextField();
		m_BegBalanceBasePurcRecAmountText = new JFormattedTextField(m_numberFormatter);
		m_EndBalanceBaseAccPayAmountText = new JFormattedTextField(m_numberFormatter);


		m_supplierBankScroll = new JScrollPane();
		m_supplierBankTextArea = new JTextArea();
		m_jPanel1_2_2_1_1 = new JPanel();
		m_currText = new JTextField();
		m_exchRateLbl1 = new JLabel();
		m_exchRateText = new JFormattedTextField(m_numberFormatter);
		m_leftTopPanel = new JPanel();
		m_subTotalLbl1 = new JLabel();
		m_VATLbl3 = new JLabel();
		m_purchaseCurrText = new JTextField();
		m_APCurrText = new JTextField();
		m_purchaseAmountText = new JFormattedTextField(m_numberFormatter);
		m_APAmountText = new JFormattedTextField(m_numberFormatter);
		m_accPayLbl = new JLabel();
		m_accumulatedPaymentCurrText = new JTextField();
		m_accumulatedPaymentAmountText = new JFormattedTextField(m_numberFormatter);
		m_BBLbl = new JLabel();
		m_beginningBalanceCurrText = new JTextField();
		m_beginningBalanceAmountText = new JFormattedTextField(m_numberFormatter);
		m_detailComp = new JButton();
		m_purchaseOrderPanel = new JPanel();
		m_separatorAccPayLbl = new JLabel();
		m_jSeparator2 = new JSeparator();
		m_accPayPanel = new JPanel();
		m_currencyLbl1 = new JLabel();
		m_amountLbl = new JLabel();
		m_taxArt23Lbl = new JLabel();
		m_totalPaymentLbl = new JLabel();
		m_amountBaseCurrentLbl = new JLabel();
		m_currpayment = new CurrencyPicker(m_conn,m_sessionid);/*new JTextField();*/
		m_exchRateLbl = new JLabel();
		m_APPaymentExchRateText = new JFormattedTextField(m_numberFormatter);
		m_APPaymentAmountText = new JFormattedTextField(m_numberFormatter);
		m_APPaymentTax23PercentText = new JFormattedTextField(m_numberFormatter);
		m_totalPaymentCurrText = new JTextField();
		m_APBaseCurrText = new JTextField();
		m_currencyLbl6 = new JLabel();
		m_APPaymentTax23CurrText = new JTextField();
		m_totalPaymentAmountText = new JFormattedTextField(m_numberFormatter);
		m_APBaseAmountText = new JFormattedTextField(m_numberFormatter);
		m_APPaymentTax23AmountText = new JFormattedTextField(m_numberFormatter);
		m_endingBalanceLbl = new JLabel();
		m_endBalanceCurrText = new JTextField();
		m_endBalaceAmountText = new JFormattedTextField(m_numberFormatter);
		m_paymentSourceCombo = new JComboBox();
		m_jPanel1_2_1 = new JPanel();
		m_rightPanel = new JPanel();
		m_voucherNoLbl = new JLabel();
		m_VoucherDateLbl = new JLabel();
		m_projectLbl = new JLabel();
		m_customerLbl = new JLabel();
		m_workDescLbl = new JLabel();
		m_projectText = new JTextField();
		m_customerText = new JTextField();
		m_workDescScroll = new JScrollPane();
		m_workDescTextArea = new JTextArea();
		m_unitCodeLbl = new JLabel();
		m_unitText = new JTextField();
		m_activityCodeLbl = new JLabel();
		m_activityText = new JTextField();
		m_departmentLbl = new JLabel();
		m_ORNoLbl = new JLabel();
		m_departmentText = new JTextField();
		m_ORNoText = new JTextField();
		m_SOPOContractNoLbl = new JLabel();
		m_IPCNoLbl = new JLabel();
		m_contractNoText = new JTextField();
		m_IPCNoText = new JTextField();
		m_descriptionLbl = new JLabel();
		m_remarkLbl = new JLabel();
		m_decriptionScroll = new JScrollPane();
		m_descrptionTextArea = new JTextArea();
		m_remarkScroll = new JScrollPane();
		m_remarksTextArea = new JTextArea();
		m_refNoText = new JTextField();
		m_transactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		m_addOriginatorPanel = new JPanel();
		m_addApprovedPanel = new JPanel();
		m_receivedAddPanel = new JPanel();

		setLayout(new BorderLayout());

		setPreferredSize(new Dimension(725, 700));
		m_jPanel1.setLayout(new BorderLayout());

		m_jPanel1.setPreferredSize(new Dimension(725, 690));
		m_jPanel1_1.setLayout(new BorderLayout());

		m_jPanel1_1.setPreferredSize(new Dimension(650, 35));
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
		m_jPanel1_2.setPreferredSize(new Dimension(700, 320));
		m_jPanel1_2_2.setLayout(new BorderLayout());
		m_jPanel1_2_2.setPreferredSize(new Dimension(450, 305));

		m_leftPanel.setLayout(new GridBagLayout());
		m_leftPanel.setPreferredSize(new Dimension(440, 2000));

		m_statusLbl.setText("Status");
		m_statusLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_statusLbl, gridBagConstraints);

		m_submittedDateLbl.setText("Submitted Date");
		m_submittedDateLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_submittedDateLbl, gridBagConstraints);

		m_paymentSourceLbl.setText("Payment Source*");
		m_paymentSourceLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_paymentSourceLbl, gridBagConstraints);

		m_accountLbl.setText("Account");
		m_accountLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_accountLbl, gridBagConstraints);

		m_chequeNumberLbl.setText("Cheque Number");
		m_chequeNumberLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 13; // 4
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_chequeNumberLbl, gridBagConstraints);

		m_chequeDueDateLbl.setText("Cheque Due Date");
		m_chequeDueDateLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 14; // 5
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_chequeDueDateLbl, gridBagConstraints);

		m_purchaseReceiptPanel.setLayout(new GridBagLayout());
		m_purchaseReceiptPanel.setPreferredSize(new Dimension(410, 20));

		m_separatorPurcRecLbl.setText("Purchase Receipt");
		m_separatorPurcRecLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_purchaseReceiptPanel.add(m_separatorPurcRecLbl, gridBagConstraints);

		m_jSeparator1.setPreferredSize(new Dimension(300, 5));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_purchaseReceiptPanel.add(m_jSeparator1, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2; // 6
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		m_leftPanel.add(m_purchaseReceiptPanel, gridBagConstraints);

		m_purchRcptNoLbl.setText("Purch. Rcpt No*");
		m_purchRcptNoLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3; // 7
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_purchRcptNoLbl, gridBagConstraints);

		m_purchRcptDateLbl.setText("Purch. Rcpt Date");
		m_purchRcptDateLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4; // 8
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_purchRcptDateLbl, gridBagConstraints);

		m_invoiceNoLbl.setText("Invoice No");
		m_invoiceNoLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5; // 9
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_invoiceNoLbl, gridBagConstraints);

		m_invoiceDateLbl.setText("Invoice Date");
		m_invoiceDateLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6; // 10
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_invoiceDateLbl, gridBagConstraints);

		m_payToLbl.setText("Pay to*");
		m_payToLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7; // 11
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_payToLbl, gridBagConstraints);

		m_supplierBankLbl.setText("Supplier Bank");
		m_supplierBankLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8; // 12
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_supplierBankLbl, gridBagConstraints);

		m_currencyLbl.setText("Currency");
		m_currencyLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9; //13
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_currencyLbl, gridBagConstraints);

		m_statusText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_statusText, gridBagConstraints);

		m_submittedDateText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_submittedDateText, gridBagConstraints);

		accountComp().setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 12; // 3
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(accountComp(), gridBagConstraints);

		m_chequeNoText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 13; // 4
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_chequeNoText, gridBagConstraints);

		m_chequeDueDateDate.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 14; // 5
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_chequeDueDateDate, gridBagConstraints);

		m_purchRcptNoComp.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3; // 7
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_purchRcptNoComp, gridBagConstraints);

		m_purchRcptDateText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4; // 8
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_purchRcptDateText, gridBagConstraints);

		m_invoiceNoText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5; // 9
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_invoiceNoText, gridBagConstraints);

		m_invoiceDateText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6; // 10
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_invoiceDateText, gridBagConstraints);

		m_paytoText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7; // 11
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_paytoText, gridBagConstraints);

		m_supplierBankScroll.setPreferredSize(new Dimension(310, 50));
		m_supplierBankTextArea.setColumns(20);
		m_supplierBankTextArea.setLineWrap(true);
		m_supplierBankTextArea.setRows(5);
		m_supplierBankScroll.setViewportView(m_supplierBankTextArea);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8; // 12
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_leftPanel.add(m_supplierBankScroll, gridBagConstraints);

		m_jPanel1_2_2_1_1.setLayout(new GridBagLayout());
		m_jPanel1_2_2_1_1.setPreferredSize(new Dimension(220, 25));

		m_jPanel1_2_2_1_1.setLayout(new GridBagLayout());
		m_jPanel1_2_2_1_1.setPreferredSize(new Dimension(310, 25));

		m_currText.setPreferredSize(new Dimension(76, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 2);
		m_jPanel1_2_2_1_1.add(m_currText, gridBagConstraints);

		m_exchRateLbl1.setHorizontalAlignment(SwingConstants.CENTER);
		m_exchRateLbl1.setText("Exch. Rate");
		m_exchRateLbl1.setPreferredSize(new Dimension(112, 21));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		m_jPanel1_2_2_1_1.add(m_exchRateLbl1, gridBagConstraints);

		m_exchRateText.setPreferredSize(new Dimension(110, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		m_jPanel1_2_2_1_1.add(m_exchRateText, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9; // 13
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(0, 3, 0, 0);
		m_leftPanel.add(m_jPanel1_2_2_1_1, gridBagConstraints);

		m_leftTopPanel.setLayout(new GridBagLayout());
		m_leftTopPanel.setPreferredSize(new Dimension(430, 110));

		m_subTotalLbl1.setText("Purch. (before VAT)");
		m_subTotalLbl1.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_subTotalLbl1, gridBagConstraints);

		m_VATLbl3.setText("Account Payable");
		m_VATLbl3.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_VATLbl3, gridBagConstraints);

		m_accPayLbl.setText("Accumulated Payment");
		m_accPayLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_accPayLbl, gridBagConstraints);

		m_BBLbl.setText("Beginning Balance");
		m_BBLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_BBLbl, gridBagConstraints);

		m_BegBalanceBaseCurrPurcRecLbl.setText("Beg. Bal. Base Curr");
		m_BegBalanceBaseCurrPurcRecLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_BegBalanceBaseCurrPurcRecLbl, gridBagConstraints);

		m_purchaseCurrText.setPreferredSize(new Dimension(77, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 4);
		m_leftTopPanel.add(m_purchaseCurrText, gridBagConstraints);

		m_purchaseAmountText.setPreferredSize(new Dimension(230, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_purchaseAmountText, gridBagConstraints);

		m_APCurrText.setPreferredSize(new Dimension(77, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 4);
		m_leftTopPanel.add(m_APCurrText, gridBagConstraints);

		m_APAmountText.setPreferredSize(new Dimension(230, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_APAmountText, gridBagConstraints);

		m_accumulatedPaymentCurrText.setPreferredSize(new Dimension(77, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 4);
		m_leftTopPanel.add(m_accumulatedPaymentCurrText, gridBagConstraints);

		m_accumulatedPaymentAmountText.setPreferredSize(new Dimension(160, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_accumulatedPaymentAmountText, gridBagConstraints);

		m_detailComp.setText("Detail");
		m_detailComp.setPreferredSize(new Dimension(67, 21));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftTopPanel.add(m_detailComp, gridBagConstraints);

		m_beginningBalanceCurrText.setPreferredSize(new Dimension(77, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 4);
		m_leftTopPanel.add(m_beginningBalanceCurrText, gridBagConstraints);

		m_beginningBalanceAmountText.setPreferredSize(new Dimension(230, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_beginningBalanceAmountText, gridBagConstraints);

		m_BegBalanceBaseCurrPurcRecText.setPreferredSize(new Dimension(77, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 4);
		m_leftTopPanel.add(m_BegBalanceBaseCurrPurcRecText, gridBagConstraints);

		m_BegBalanceBasePurcRecAmountText.setPreferredSize(new Dimension(230, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_leftTopPanel.add(m_BegBalanceBasePurcRecAmountText, gridBagConstraints);



		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10; // 14
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_leftTopPanel, gridBagConstraints);

		m_purchaseOrderPanel.setLayout(new GridBagLayout());
		m_purchaseOrderPanel.setPreferredSize(new Dimension(410, 15));

		m_separatorAccPayLbl.setText("Account Payable Payment");
		m_separatorAccPayLbl.setPreferredSize(new Dimension(130, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_purchaseOrderPanel.add(m_separatorAccPayLbl, gridBagConstraints);

		m_jSeparator2.setPreferredSize(new Dimension(210, 5));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_purchaseOrderPanel.add(m_jSeparator2, gridBagConstraints);

		m_descriptionLbl.setText("Description*");
		m_descriptionLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 16;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_leftPanel.add(m_descriptionLbl, gridBagConstraints);

		m_decriptionScroll.setPreferredSize(new Dimension(310, 100));
		m_descrptionTextArea.setColumns(20);
		m_descrptionTextArea.setLineWrap(true);
		m_descrptionTextArea.setRows(5);
		m_decriptionScroll.setViewportView(m_descrptionTextArea);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 16;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_leftPanel.add(m_decriptionScroll, gridBagConstraints);

		m_paymentSourceCombo.setModel(new DefaultComboBoxModel(new String[] { "Cash", "Bank" }));
		m_paymentSourceCombo.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11; // 2
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_leftPanel.add(m_paymentSourceCombo, gridBagConstraints);

		m_jPanel1_2_2.add(m_leftPanel, BorderLayout.WEST);
		m_jPanel1_2.add(m_jPanel1_2_2, BorderLayout.WEST);
		m_jPanel1_2_1.setLayout(new BorderLayout());
		m_rightPanel.setLayout(new GridBagLayout());
		m_rightPanel.setPreferredSize(new Dimension(440, 200));

		m_voucherNoLbl.setText("Voucher No");
		m_voucherNoLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_voucherNoLbl, gridBagConstraints);

		m_VoucherDateLbl.setText("Voucher Date*");
		m_VoucherDateLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_VoucherDateLbl, gridBagConstraints);

		m_projectLbl.setText("Project Code*");
		m_projectLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_projectLbl, gridBagConstraints);

		m_customerLbl.setText("Customer");
		m_customerLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_customerLbl, gridBagConstraints);

		m_workDescLbl.setText("Work Desc. ");
		m_workDescLbl.setPreferredSize(new Dimension(85, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_workDescLbl, gridBagConstraints);

		m_projectText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_projectText, gridBagConstraints);

		m_customerText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_customerText, gridBagConstraints);

		m_workDescScroll.setPreferredSize(new Dimension(310, 63));
		m_workDescTextArea.setColumns(20);
		m_workDescTextArea.setLineWrap(true);
		m_workDescTextArea.setRows(5);
		m_workDescScroll.setViewportView(m_workDescTextArea);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_workDescScroll, gridBagConstraints);

		m_unitCodeLbl.setText("Unit Code");
		m_unitCodeLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_unitCodeLbl, gridBagConstraints);

		m_unitText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_unitText, gridBagConstraints);

		m_activityCodeLbl.setText("Activity Code");
		m_activityCodeLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_activityCodeLbl, gridBagConstraints);

		m_activityText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_activityText, gridBagConstraints);

		m_departmentLbl.setText("Department");
		m_departmentLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_departmentLbl, gridBagConstraints);

		m_ORNoLbl.setText("O.R No");
		m_ORNoLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_ORNoLbl, gridBagConstraints);

		m_departmentText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_departmentText, gridBagConstraints);

		m_ORNoText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_ORNoText, gridBagConstraints);

		m_SOPOContractNoLbl.setText("SO/PO/Contract No");
		m_SOPOContractNoLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_SOPOContractNoLbl, gridBagConstraints);

		m_IPCNoLbl.setText("IPC No");
		m_IPCNoLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_IPCNoLbl, gridBagConstraints);

		m_contractNoText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_contractNoText, gridBagConstraints);

		m_IPCNoText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_IPCNoText, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		m_rightPanel.add(m_purchaseOrderPanel, gridBagConstraints);

		m_accPayPanel.setLayout(new GridBagLayout());
		m_accPayPanel.setPreferredSize(new Dimension(425, 150));

		m_currencyLbl1.setText("Currency*");
		m_currencyLbl1.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_currencyLbl1, gridBagConstraints);

		m_amountLbl.setText("Amount*");
		m_amountLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_amountLbl, gridBagConstraints);

		m_taxArt23Lbl.setText("Tax Art 23");
		m_taxArt23Lbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_taxArt23Lbl, gridBagConstraints);

		m_totalPaymentLbl.setText("Total Payment");
		m_totalPaymentLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_totalPaymentLbl, gridBagConstraints);

		m_amountBaseCurrentLbl.setText("Amount Base Curr.");
		m_amountBaseCurrentLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_amountBaseCurrentLbl, gridBagConstraints);

		m_currpayment.setPreferredSize(new Dimension(50, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_currpayment, gridBagConstraints);

		m_exchRateLbl.setHorizontalAlignment(SwingConstants.CENTER);
		m_exchRateLbl.setText("Exch. Rate");
		m_exchRateLbl.setPreferredSize(new Dimension(60, 21));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_exchRateLbl, gridBagConstraints);

		m_APPaymentExchRateText.setPreferredSize(new Dimension(170, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_APPaymentExchRateText, gridBagConstraints);

		m_APPaymentAmountText.setPreferredSize(new Dimension(300, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_APPaymentAmountText, gridBagConstraints);

		m_APPaymentTax23PercentText.setPreferredSize(new Dimension(30, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_APPaymentTax23PercentText, gridBagConstraints);

		m_totalPaymentCurrText.setPreferredSize(new Dimension(46, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 2);
		m_accPayPanel.add(m_totalPaymentCurrText, gridBagConstraints);

		m_APBaseCurrText.setPreferredSize(new Dimension(46, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 2);
		m_accPayPanel.add(m_APBaseCurrText, gridBagConstraints);

		m_currencyLbl6.setHorizontalAlignment(SwingConstants.CENTER);
		m_currencyLbl6.setText("%");
		m_currencyLbl6.setPreferredSize(new Dimension(20, 21));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_currencyLbl6, gridBagConstraints);

		m_APPaymentTax23CurrText.setPreferredSize(new Dimension(56, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 2);
		m_accPayPanel.add(m_APPaymentTax23CurrText, gridBagConstraints);

		m_totalPaymentAmountText.setPreferredSize(new Dimension(165, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_totalPaymentAmountText, gridBagConstraints);

		m_APBaseAmountText.setPreferredSize(new Dimension(165, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_APBaseAmountText, gridBagConstraints);

		m_APPaymentTax23AmountText.setPreferredSize(new Dimension(80, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_APPaymentTax23AmountText, gridBagConstraints);

		m_endingBalanceLbl.setText("Ending Balance");
		m_endingBalanceLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_endingBalanceLbl, gridBagConstraints);

		m_endBalanceCurrText.setPreferredSize(new Dimension(46, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 2);
		m_accPayPanel.add(m_endBalanceCurrText, gridBagConstraints);

		m_endBalaceAmountText.setPreferredSize(new Dimension(165, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_endBalaceAmountText, gridBagConstraints);

		m_EndBalanceBaseCurrAccPayLbl.setText("End Bal. Base Curr");
		m_EndBalanceBaseCurrAccPayLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_EndBalanceBaseCurrAccPayLbl, gridBagConstraints);

		m_EndBalanceBaseCurrAccPayText.setPreferredSize(new Dimension(46, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 2);
		m_accPayPanel.add(m_EndBalanceBaseCurrAccPayText, gridBagConstraints);

		m_EndBalanceBaseAccPayAmountText.setPreferredSize(new Dimension(165, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_accPayPanel.add(m_EndBalanceBaseAccPayAmountText, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_rightPanel.add(m_accPayPanel, gridBagConstraints);


		m_remarkLbl.setText("Remarks");
		m_remarkLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_remarkLbl, gridBagConstraints);

		m_remarkScroll.setPreferredSize(new Dimension(310, 100));
		m_remarksTextArea.setColumns(20);
		m_remarksTextArea.setLineWrap(true);
		m_remarksTextArea.setRows(5);
		m_remarkScroll.setViewportView(m_remarksTextArea);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_remarkScroll, gridBagConstraints);

		m_refNoText.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_rightPanel.add(m_refNoText, gridBagConstraints);

		m_transactionDateDate.setPreferredSize(new Dimension(310, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_rightPanel.add(m_transactionDateDate, gridBagConstraints);

		m_jPanel1_2_1.add(m_rightPanel, BorderLayout.WEST);

		m_jPanel1_2.add(m_jPanel1_2_1, BorderLayout.CENTER);

		m_jPanel1.add(m_jPanel1_2, BorderLayout.CENTER);

		m_addOriginatorPanel.setLayout(new BorderLayout());
		m_addOriginatorPanel.setPreferredSize(new Dimension(700, 90));
		m_originatorPanel.setLayout(new GridBagLayout());
		m_originatorPanel.setBorder(BorderFactory.createTitledBorder("Originator"));
		m_originatorPanel.setOpaque(false);
		m_originatorPanel.setPreferredSize(new Dimension(294, 110));
		m_addOriginatorPanel.add(m_originatorPanel, BorderLayout.WEST);

		m_addApprovedPanel.setLayout(new BorderLayout());
		m_approvedPanel.setLayout(new GridBagLayout());
		m_approvedPanel.setBorder(BorderFactory.createTitledBorder("Approved by"));
		m_approvedPanel.setOpaque(false);
		m_approvedPanel.setPreferredSize(new Dimension(294, 110));
		m_addApprovedPanel.add(m_approvedPanel, BorderLayout.WEST);

		m_receivedAddPanel.setLayout(new BorderLayout());
		m_receivedAddPanel.setPreferredSize(new Dimension(234, 110));
		m_receivedPanel.setLayout(new GridBagLayout());
		m_receivedPanel.setBorder(BorderFactory.createTitledBorder("Received by"));
		m_receivedPanel.setOpaque(false);
		m_receivedPanel.setPreferredSize(new Dimension(293, 110));

		m_receivedAddPanel.add(m_receivedPanel, BorderLayout.WEST);
		m_addApprovedPanel.add(m_receivedAddPanel, BorderLayout.CENTER);
		m_addOriginatorPanel.add(m_addApprovedPanel, BorderLayout.CENTER);

		m_jPanel1.add(m_addOriginatorPanel, BorderLayout.SOUTH);

		add(m_jPanel1, BorderLayout.NORTH);

	}

	private void addingListener(){
		m_paymentSourceCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		accountComp().addPropertyChangeListener("object",this);
		m_purchRcptNoComp.addPropertyChangeListener("object",this);
		m_APPaymentAmountText.addPropertyChangeListener("value",this);
		//m_APPaymentAmountText.getDocument().addDocumentListener(this);
		//m_APPaymentTax23PercentText.getDocument().addDocumentListener(this);
		m_currpayment.addPropertyChangeListener("object",this);
		m_APPaymentTax23PercentText.addPropertyChangeListener("value",this);
		m_APPaymentTax23AmountText.addPropertyChangeListener("value",this);
		m_detailComp.addActionListener(this);
		m_APPaymentExchRateText.addPropertyChangeListener("value",this);
		m_transactionDateDate.addPropertyChangeListener("date", this);
	}


	public void actionPerformed(ActionEvent e){

		if(e.getSource() == m_paymentSourceCombo) {
			Object objaccount =m_currpayment.getObject();
			revalidateAccount((Currency)objaccount);
		}
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0){
				new Vchr_APPayment(m_entity,m_conn);
			}else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame.getMainFrame(), "Search Voucher",
					m_conn, m_sessionid,new PurchaseApPmtLoader(m_conn),"Account Payable Payment");
			dlg.setVisible(true);
			if (dlg.selectedObj != null){
				m_isitacamount = false;
				doLoad(dlg.selectedObj);
			}
		}
		else if (e.getSource() == m_detailComp){
			if (m_purchRcptNoComp.getObject()!=null){
				Object obj = m_purchRcptNoComp.getObject();
				detailAmount= new LookupAPPaymentDetailPicker(m_conn, m_sessionid,obj, entity());
				detailAmount.done1();
			}
		}
	}

	private void revalidateAccount(Currency curr) {
		String receive = (String)m_paymentSourceCombo.getSelectedItem();
		//if (m_purchRcptNoComp.getObject() instanceof PurchaseReceipt) {
		/*PurchaseReceipt pr = (PurchaseReceipt) m_purchRcptNoComp.getObject();
		 LookupPicker lookup = null;
		 if(receive.equals("Cash")){
		 if(pr!=null){
		 lookup = new LookupCashAccountPicker(m_conn, m_sessionid, pr.getApCurr());
		 } else {
		 lookup = new LookupCashAccountPicker(m_conn, m_sessionid);
		 }
		 m_chequeNoText.setEditable(false);
		 m_chequeDueDateDate.setEditable(false);
		 }else{
		 if(pr!=null){
		 lookup = new LookupBankAccountPicker(m_conn, m_sessionid, pr.getApCurr());
		 } else {
		 lookup = new LookupBankAccountPicker(m_conn, m_sessionid);
		 }
		 m_chequeNoText.setEditable(true);
		 m_chequeDueDateDate.setEditable(true);
		 }*/
		/*	LookupPicker lookup = null;
		 if(receive.equals("Cash")){
		 lookup = new LookupCashAccountPicker(m_conn, m_sessionid,curr);
		 m_chequeNoText.setEditable(false);
		 m_chequeDueDateDate.setEditable(false);
		 } else {
		 lookup = new LookupBankAccountPicker(m_conn, m_sessionid,curr);
		 m_chequeNoText.setEditable(true);
		 m_chequeDueDateDate.setEditable(true);
		 }
		 revalidateAccountComp(lookup);
		 }else if (m_purchRcptNoComp.getObject() instanceof BeginningAccountPayable) {*/
		//BeginningAccountPayable pr = (BeginningAccountPayable)m_purchRcptNoComp.getObject();
		/*LookupPicker lookup = null;
		 if(receive.equals(m_cash)){
		 if(pr!=null)
		 lookup = new LookupCashAccountPicker(m_conn,m_sessionid,pr.getCurrency());
		 else
		 lookup = new LookupCashAccountPicker(m_conn,m_sessionid);
		 m_chequeNoText.setEditable(false);
		 m_chequeDueDateDate.setEditable(false);
		 }else{
		 if(pr!=null)
		 lookup = new LookupBankAccountPicker(m_conn,m_sessionid,pr.getCurrency());
		 else
		 lookup = new LookupBankAccountPicker(m_conn,m_sessionid);
		 m_chequeNoText.setEditable(true);
		 m_chequeDueDateDate.setEditable(true);
		 }*/
		LookupPicker lookup = null;
		if(receive.equalsIgnoreCase("CASH")){
			lookup = new LookupCashAccountPicker(m_conn, m_sessionid,curr);
			m_chequeNoText.setEditable(false);
			m_chequeDueDateDate.setEditable(false);
		} else if(receive.equalsIgnoreCase("BANK")){
			lookup = new LookupBankAccountPicker(m_conn, m_sessionid,curr);
			m_chequeNoText.setEditable(true);
			m_chequeDueDateDate.setEditable(true);
		}
		revalidateAccountComp(lookup);
		/*}	else{

		 }*/
	}

	boolean m_isitacamount = false;
	protected void doNew() {
		clearKomponen();
		revalidateAccount((Currency)m_currpayment.getObject());
		super.doNew();
		isiDefaultAssignPanel();
		revalidatePurchasePicker();
		m_isitacamount = true;
	}


	protected void doSubmit() {
		validityMsgs.clear();
		if (m_accountComp.getObject()==null)
			addInvalid("Account must be selected");
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

	public void doEdit() {
		super.doEdit();
		m_isitacamount=true;
	}

	protected void revalidatePurchasePicker() {

		m_leftPanel.remove(m_purchRcptNoComp);
		m_leftPanel.revalidate();
		m_leftPanel.repaint();
		m_purchRcptNoComp.removePropertyChangeListener("object", this);
		m_purchRcptNoComp = new LookupPurchasePicker(m_conn, m_sessionid);
		m_purchRcptNoComp.addPropertyChangeListener("object", this);

		m_purchRcptNoComp.setPreferredSize(new java.awt.Dimension(310, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		m_leftPanel.add(m_purchRcptNoComp, gridBagConstraints);
		m_leftPanel.revalidate();
	}

	protected void clearAll() {
		super.clearAll();
		clearKomponen();
		disableEditMode();
	}

	public void clearKomponen(){
		m_statusText.setText("");
		m_originatorPanel.setEmployee(null);
		m_approvedPanel.setEmployee(null);
		m_receivedPanel.setEmployee(null);
		m_originatorPanel.m_jobTextField.setText("");
		m_approvedPanel.m_jobTextField.setText("");
		m_receivedPanel.m_jobTextField.setText("");
		m_originatorPanel.setDate(null);
		m_approvedPanel.setDate(null);
		m_receivedPanel.setDate(null);
		m_exchRateText.setValue(new Double(0));
		m_transactionDateDate.setDate(null);

		m_beginningBalanceAmountText.setValue(new Double(0.0));
		//m_APPaymentCurrText.setText("");
		m_currpayment.setObject(null);
		m_totalPaymentAmountText.setValue(null);
		m_APBaseAmountText.setValue(null);
		m_APBaseCurrText.setText("");
		m_endBalaceAmountText.setValue(null);

		m_BegBalanceBaseCurrPurcRecText.setText("");
		m_BegBalanceBasePurcRecAmountText.setValue(new Double(0.0));

		m_EndBalanceBaseCurrAccPayText.setText("");
		m_EndBalanceBaseAccPayAmountText.setValue(new Double(0.0));
	}
	
	private void hitungTaxArt23() {
		Number amt = (Number)m_APPaymentAmountText.getValue();
		double amount = 0;
		Number nbr = new Double(0.0);
		nbr = amt;
		if(amt!=null)
			amount = nbr.doubleValue();
		double tax23percent = 0;
		if(m_APPaymentTax23PercentText.getValue()!=null){
			tax23percent = ((Number)m_APPaymentTax23PercentText.getValue()).doubleValue();
		}
		
		double hitung = (tax23percent/100) * amount;
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		
		m_APPaymentTax23AmountText.setValue(new Double(hitung));
		
		double value = amount - hitung;
		value = nr.round(value);
		m_totalPaymentAmountText.setValue(new Double(value));
	}

	public void hitungAccountPayablePayment(){
		Number amt = (Number)m_APPaymentAmountText.getValue();
		double amount = 0;
		Number nbr = new Double(0.0);
		nbr = amt;
		if(amt!=null)
			amount = nbr.doubleValue();
		double tax23percent = 0;
		if(m_APPaymentTax23PercentText.getValue()!=null){
			tax23percent = ((Number)m_APPaymentTax23PercentText.getValue()).doubleValue();
		}
		/*double vatPercent = 0;
		 if (m_purchRcptNoComp.getObject() instanceof PurchaseReceipt) {
		 PurchaseReceipt pr = (PurchaseReceipt) m_purchRcptNoComp.getObject();
		 if(pr!=null){
		 if(pr.getVatPercent()>0)
		 vatPercent = pr.getVatPercent();
		 }

		 } else if (m_purchRcptNoComp.getObject() instanceof BeginningAccountPayable) {
		 //BeginningAccountPayable pr = (BeginningAccountPayable) m_purchRcptNoComp.getObject();
		  vatPercent = 0;
		  }*/

		//double hitung = (tax23percent/100) * (amount /(1 + (vatPercent/100)));
		double hitung = (tax23percent/100) * amount;
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);

		/*if (m_isitacamount) 
			m_APPaymentTax23AmountText.setValue(new Double(hitung));
		else*/
			//m_APPaymentTax23AmountText.setValue(new Double(entity().getTax23Amount()));
		if (m_APPaymentTax23AmountText.getValue() != null)
		hitung = ((Number)m_APPaymentTax23AmountText.getValue()).doubleValue();

		double value = amount - hitung;
		value = nr.round(value);
		m_totalPaymentAmountText.setValue(new Double(value));

		if (m_totalPaymentCurrText.getText().equals(baseCurrency.getSymbol()))
			m_APBaseAmountText.setValue(m_totalPaymentAmountText.getValue());
		else{
			Number total = (Number)m_totalPaymentAmountText.getValue();
			Number exch = (Number)m_APPaymentExchRateText.getValue();
			if((total!=null)&&(exch!=null)){
				double val = total.doubleValue()*exch.doubleValue();

				m_APBaseAmountText.setValue(new Double(val));
			}else
				m_APBaseAmountText.setValue(new Double(0.0));
		}

		Object obj = m_purchRcptNoComp.getObject();
		double acc = getAcumulateAmount(obj);
		if(acc>0){
			if (entity().getStatus() == State.POSTED) {
				String receiptCurrency = m_currText.getText();
				String paymentCurrency = "";
				Object objcurr = m_currpayment.getObject();
				if (objcurr instanceof Currency) {
					Currency curr = (Currency) objcurr;
					paymentCurrency = curr.getSymbol();
				}


				if ((!receiptCurrency.equals(""))
						&& (!paymentCurrency.equals(""))) {
					if (!receiptCurrency.equals(paymentCurrency)) {
						if (paymentCurrency.equals(baseCurrency.getSymbol())) {
							// receipt = $; payment = Rp
							Number receiptExchangeRateNbr = (Number) m_exchRateText
							.getValue();
							double receiptExchangeRate = 0;
							if (receiptExchangeRateNbr != null)
								receiptExchangeRate = receiptExchangeRateNbr
								.doubleValue();
							amount = amount / receiptExchangeRate;
						} else {
							// receipt = Rp; payment = $
							Number paymentExchangeRateNbr = (Number) m_APPaymentExchRateText
							.getValue();
							double paymentExchangeRate = 0;
							if (paymentExchangeRateNbr != null)
								paymentExchangeRate = paymentExchangeRateNbr
								.doubleValue();
							amount = amount * paymentExchangeRate;
						}
					}
				}

				amount = nr.round(amount);
				acc -= amount;
				m_accumulatedPaymentAmountText.setValue(new Double(acc));
			}
		}

		/*Number accPmt = (Number)m_accumulatedPaymentAmountText.getValue();
		 if(accPmt!=null){
		 if(entity().getStatus()==State.POSTED){
		 double acc = accPmt.doubleValue();
		 if(acc>0){
		 String receiptCurrency = m_currText.getText();
		 String paymentCurrency = m_APPaymentCurrText.getText();

		 if((!receiptCurrency.equals(""))&&(!paymentCurrency.equals(""))){
		 if(!receiptCurrency.equals(paymentCurrency)){
		 if(paymentCurrency.equals(baseCurrency.getSymbol())){
		 // receipt = $; payment = Rp
		  Number receiptExchangeRateNbr = (Number) m_exchRateText.getValue();
		  double receiptExchangeRate = 0;
		  if(receiptExchangeRateNbr!=null)
		  receiptExchangeRate = receiptExchangeRateNbr.doubleValue();
		  amount = amount / receiptExchangeRate;
		  }else{
		  // receipt = Rp; payment = $
		   Number paymentExchangeRateNbr = (Number) m_APPaymentExchRateText.getValue();
		   double paymentExchangeRate = 0;
		   if(paymentExchangeRateNbr!=null)
		   paymentExchangeRate = paymentExchangeRateNbr.doubleValue();
		   amount = amount * paymentExchangeRate;
		   }
		   }
		   }

		   amount = nr.round(amount);
		   acc -= amount;
		   m_accumulatedPaymentAmountText.setValue(new Double(acc));
		   }
		   }
		   }*/

		double beginingbalance = 0;
		Number ap = (Number)m_APAmountText.getValue();
		Number accPay = (Number)m_accumulatedPaymentAmountText.getValue();
		if((ap!=null)&&(accPay!=null))
			beginingbalance = ap.doubleValue() - accPay.doubleValue();
		m_beginningBalanceAmountText.setValue(new Double(beginingbalance));
		Number beg = (Number)m_beginningBalanceAmountText.getValue();
		if(beg!=null)
			beginingbalance = beg.doubleValue();

		countEndingBalance();

		setBegBalanceBaseCurrency();
		setEndBalanceBaseCurrency();
	}

	private void countEndingBalance() {
		Number amtNbr = (Number) m_APPaymentAmountText.getValue();
		Number beg = (Number)m_beginningBalanceAmountText.getValue();

		double amount = 0;
		if(amtNbr!=null)
			amount = amtNbr.doubleValue();
		double beginingBalance = 0;
		if(beg!=null)
			beginingBalance = beg.doubleValue();



		String currPmt = "";
		Object objcurr = m_currpayment.getObject();
		if (objcurr instanceof Currency) {
			Currency curr = (Currency) objcurr;
			currPmt  = curr.getSymbol();
		}
		String currBeg = m_beginningBalanceCurrText.getText();

		if(!currPmt.equals(currBeg)){
			if(currPmt.equals(baseCurrency.getSymbol())){
				// pmt = Rp, beg = $
				Number exchRateBegNbr = (Number) m_exchRateText.getValue();
				double exchRate = 1;
				if(exchRateBegNbr!=null)
					exchRate = exchRateBegNbr.doubleValue();
				amount = amount / exchRate;
			}else{
				// pmt = $, beg = Rp
				Number exchRateBegNbr = (Number) m_APPaymentExchRateText.getValue();
				double exchRate = 1;
				if(exchRateBegNbr!=null)
					exchRate = exchRateBegNbr.doubleValue();
				amount = amount * exchRate;
			}
		}

		double data = beginingBalance-amount;

		if(data!=0)
			m_endBalaceAmountText.setValue(new Double(data));
		else
			m_endBalaceAmountText.setValue(new Double(0.0));
	}

	public void hitungAccountPayablePaymentWithTaxArt21(){

		Number amt = (Number)m_APPaymentAmountText.getValue();
		double amount = 0;
		if (amt!=null)
			amount = amt.doubleValue();
		Number tax = (Number)m_APPaymentTax23AmountText.getValue();
		double taxamt = 0;
		if (tax!=null)
			taxamt = tax.doubleValue();

		if (amount!=0 && taxamt!=0)
			m_totalPaymentAmountText.setValue(new Double(amount-taxamt));

		Number exc = (Number)m_APPaymentExchRateText.getValue();
		double excrate = 0;
		if (exc!=null)
			excrate = exc.doubleValue();

		Number totPay = (Number)m_totalPaymentAmountText.getValue();
		double totPayAmount = 0 ;
		if (totPay!=null)
			totPayAmount = totPay.doubleValue();
		m_APBaseAmountText.setValue(new Double(totPayAmount*excrate));



		/*	Number taxArt21amt = (Number)m_APPaymentTax23AmountText.getValue();
		 double totPayment = amt.doubleValue()-taxArt21amt.doubleValue();
		 m_totalPaymentAmountText.setValue(new Double(totPayment));*/
		//m_APBaseAmountText.setValue(new Double(totPayment*excrate));
	}

	private void isiDefaultAssignPanel(){
		m_originatorPanel.m_jobTextField.setText(getEmployeeJobTitle(m_defaultOriginator));
		m_approvedPanel.m_jobTextField.setText(getEmployeeJobTitle(m_defaultApproved));
		m_receivedPanel.m_jobTextField.setText(getEmployeeJobTitle(m_defaultReceived));
	}

	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PURCHASE_AP_PAYMENT, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			m_defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PURCHASE_AP_PAYMENT, Signature.SIGN_APPROVED);
		if(sign!=null)
			m_defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PURCHASE_AP_PAYMENT, Signature.SIGN_RECEIVED);
		if(sign!=null)
			m_defaultReceived = sign.getFullEmployee();
	}

	PurchaseApPmt entity() {
		return m_entity;
	}

	protected StateTemplateEntity currentEntity() {
		return entity();
	}

	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity() {
		validityMsgs.clear();
		/*if (m_accountComp.getObject()==null)
		 addInvalid("Account must be selected");*/
		if (m_purchRcptNoComp.getObject()==null)
			addInvalid("purch. Rcpt No must be selected");
		if (m_paytoText.getText().equals(""))
			addInvalid("pay to must be added");
//		if (m_APPaymentCurrText.getText().equals(""))
		if (m_currpayment.getObject()==null)
			addInvalid("Currency Account Payable Payment must be added");
		if (m_APPaymentAmountText.getText().equals(""))
			addInvalid("Amount Account Payable Payment must be added");
		/*if (m_APPaymentTax23PercentText.getText().equals(""))
		 addInvalid("Tax art 23 percent Account Payable Payment must be added");*/
		if (m_transactionDateDate.getDate()==null)
			addInvalid("Voucher date must be selected");
		if (m_projectText.getText().equals(""))
			addInvalid("Project code must be added");
		if (m_descrptionTextArea.getText().equals(""))
			addInvalid("Description must be added");
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

	String m_bank = "Bank";
	String m_cash = "Cash";
	protected void gui2entity() {
		String paySource = (String) m_paymentSourceCombo.getSelectedItem();
		entity().setPaymentSource(paySource);

		Object objcurr= m_currpayment.getObject();
		entity().setAppMtCurr(null);
		entity().setTax23Curr(null);
		if (objcurr instanceof Currency) {
			Currency curr = (Currency) objcurr;
			entity().setCurrency(curr);
			if (curr != null) {
				entity().setAppMtCurr(curr);
				entity().setTax23Curr(curr);
			}
		}



		CashBankAccount objAcc = (CashBankAccount) accountComp().getObject();

		if (objAcc instanceof BankAccount) {
			BankAccount bank = (BankAccount) objAcc;
			entity().setBankAccount(bank);
			entity().setCashAccount(null);
			entity().setUnit(objAcc.getUnit());
			//entity().setAppMtCurr(bank.getCurrency());
			//entity().setTax23Curr(bank.getCurrency());
		}
		else if (objAcc instanceof CashAccount) {
			CashAccount cash = (CashAccount) objAcc;
			entity().setCashAccount(cash);
			entity().setBankAccount(null);
			entity().setUnit(objAcc.getUnit());
			//entity().setAppMtCurr(cash.getCurrency());
			//entity().setTax23Curr(cash.getCurrency());

		}else{
			entity().setCashAccount(null);
			entity().setBankAccount(null);
			//entity().setAppMtCurr(null);
			//entity().setTax23Curr(null);
		}




		entity().setChequeNo(m_chequeNoText.getText());
		entity().setChequeDueDate(m_chequeDueDateDate.getDate());

		Number exch = (Number)m_APPaymentExchRateText.getValue();
		if(exch!=null)
			entity().setAppMtExchRate(exch.doubleValue());

		if (m_purchRcptNoComp.getObject()!=null){
			Object obj = m_purchRcptNoComp.getObject();
			if (obj instanceof PurchaseReceipt) {
				PurchaseReceipt a = (PurchaseReceipt) obj;
				entity().setBeginningBalance(null);
				entity().setPurchaseReceipt(a);
				//entity().setAppMtCurr(a.getApCurr());

				//entity().setTax23Curr(a.getCurrency());
			} else if (obj instanceof BeginningAccountPayable) {
				BeginningAccountPayable a = (BeginningAccountPayable) obj;
				entity().setPurchaseReceipt(null);
				entity().setBeginningBalance(a);
				//entity().setAppMtCurr(a.getCurrency());

				//entity().setTax23Curr(a.getCurrency());
			}
		}

		if(m_APPaymentTax23PercentText.getValue()!=null)
			entity().setTax23Percent(((Number)m_APPaymentTax23PercentText.getValue()).doubleValue());
		if(m_APPaymentTax23AmountText.getValue()!=null)
			entity().setTax23Amount(((Number)m_APPaymentTax23AmountText.getValue()).doubleValue());
		if(m_APPaymentAmountText.getValue()!=null)
			entity().setAppMtAmount(((Number)m_APPaymentAmountText.getValue()).doubleValue());
		entity().setTransactionDate(m_transactionDateDate.getDate());
		entity().setRemarks(m_remarksTextArea.getText());
		entity().transTemplateRead(
				this.m_originatorPanel, this.m_approvedPanel,
				this.m_receivedPanel, this.m_refNoText,
				this.m_descrptionTextArea
		);
	}

	protected void entity2gui() {
		m_statusText.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");

		if(entity().getSubmitDate()!=null)
			m_submittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			m_submittedDateText.setText("");
		m_chequeNoText.setText(entity().getChequeNo());
		m_chequeDueDateDate.setDate(entity().getChequeDueDate());
		m_refNoText.setText(entity().getReferenceNo());

		
		
		if (entity().getPurchaseReceipt()!=null){
			m_purchRcptNoComp.setObject(entity().getPurchaseReceipt());
			
			hitungAccountPayablePayment();
		}else if (entity().getBeginningBalance()!=null){
			m_purchRcptNoComp.setObject(entity().getBeginningBalance());
			
			hitungAccountPayablePayment();
		}else{
			m_purchRcptNoComp.setObject(null);
			setGuiEmpty();
		}

		/*if (entity().getCurrency()!=null)
			m_currpayment.setObject(entity().getCurrency());
		else
			m_currpayment.setObject(null);*/
		if (entity().getAppMtCurr()!=null)
			m_currpayment.setObject(entity().getAppMtCurr());
		else
			m_currpayment.setObject(null);

		if (entity().getAppMtExchRate()>0)
			m_APPaymentExchRateText.setValue(new Double(entity().getAppMtExchRate()));
		else
			m_APPaymentExchRateText.setValue(new Double(1));

		if (entity().getPaymentSource()!=null){
			m_paymentSourceCombo.setSelectedItem(entity().getPaymentSource());
		}



		m_APPaymentExchRateText.setValue(new Double(entity().getAppMtExchRate()));
		m_APPaymentAmountText.setValue(new Double(entity().getAppMtAmount()));
		m_APPaymentTax23PercentText.setValue(new Double(entity().getTax23Percent()));
		m_APBaseCurrText.setText(baseCurrency.getSymbol());
		m_APPaymentTax23AmountText.setValue(new Double(entity().getTax23Amount()));

		if (entity().getBankAccount()!=null){
			m_accountComp.setObject(entity().getBankAccount());
		}else if (entity().getCashAccount()!=null){
			m_accountComp.setObject(entity().getCashAccount());
		}

		if (entity().getTransactionDate()!=null)
			m_transactionDateDate.setDate(entity().getTransactionDate());
		else
			m_transactionDateDate.setDate(new Date());

		m_descrptionTextArea.setText(entity().getDescription());
		m_remarksTextArea.setText(entity().getRemarks());

		m_originatorPanel.setEmployee(entity().getEmpOriginator());
		m_originatorPanel.setDate(entity().getDateOriginator());
		m_approvedPanel.setEmployee(entity().getEmpApproved());
		m_approvedPanel.setDate(entity().getDateApproved());

		m_receivedPanel.setEmployee(entity().getEmpReceived());
		m_receivedPanel.setDate(entity().getDateReceived());

		m_originatorPanel.setJobTitle(entity().getJobTitleOriginator());
		m_approvedPanel.setJobTitle(entity().getJobTitleApproved());
		m_receivedPanel.setJobTitle(entity().getJobTitleReceived());
	}

	private void setGuiEmpty() {
		m_purchRcptNoComp.setObject(null);
		m_purchRcptDateText.setText("");
		m_purchRcptDateText.setText("");
		m_invoiceNoText.setText("");
		m_paytoText.setText("");
		m_supplierBankTextArea.setText("");
		m_currText.setText("");
		m_exchRateText.setText("");
		m_purchaseCurrText.setText("");
		m_purchaseAmountText.setText("");
		m_APCurrText.setText("");
		m_APAmountText.setValue(new Double(0.0));
		m_accumulatedPaymentCurrText.setText("");
		m_accumulatedPaymentAmountText.setValue(new Double(0.0));
		m_beginningBalanceCurrText.setText("");
		m_beginningBalanceAmountText.setValue(new Double(0.0));
		m_APPaymentAmountText.setValue(new Double(0.0));
		m_APPaymentExchRateText.setValue(new Double(0.0));
		m_APPaymentTax23PercentText.setValue(new Double(0.0));
		m_APPaymentTax23CurrText.setText("");
		m_APPaymentTax23AmountText.setValue(new Double(0.0));
		m_totalPaymentCurrText.setText("");
		m_totalPaymentAmountText.setValue(new Double(0.0));
		m_APBaseAmountText.setText("");
		m_endBalanceCurrText.setText("");
		m_endBalaceAmountText.setValue(new Double(0.0));
	}


	protected void disableEditMode() {
		m_statusText.setEditable(false);
		m_submittedDateText.setEditable(false);
		m_paymentSourceCombo.setEnabled(false);
		m_accountComp.setEnabled(false);
		m_chequeNoText.setEditable(false);
		m_chequeDueDateDate.setEditable(false);
		m_purchRcptNoComp.setEnabled(false);
		m_purchRcptDateText.setEditable(false);
		m_invoiceNoText.setEditable(false);
		m_invoiceDateText.setEditable(false);
		m_paytoText.setEditable(false);
		m_supplierBankTextArea.setEditable(false);
		m_currText.setEditable(false);
		m_exchRateText.setEditable(false);
		m_purchaseCurrText.setEditable(false);
		m_purchaseAmountText.setEditable(false);
		m_APCurrText.setEditable(false);
		m_APAmountText.setEditable(false);
		m_accumulatedPaymentCurrText.setEditable(false);
		m_accumulatedPaymentAmountText.setEditable(false);
		m_detailComp.setEnabled(false);
		m_beginningBalanceCurrText.setEditable(false);
		m_beginningBalanceAmountText.setEditable(false);
		//m_APPaymentCurrText.setEditable(false);

		m_currpayment.setEnabled(false);
		m_APPaymentExchRateText.setEditable(false);
		m_APPaymentAmountText.setEditable(false);
		m_APPaymentTax23PercentText.setEditable(false);
		m_APPaymentTax23CurrText.setEditable(false);
		m_APPaymentTax23AmountText.setEditable(false);
		m_totalPaymentCurrText.setEditable(false);
		m_totalPaymentAmountText.setEditable(false);
		m_APBaseCurrText.setEditable(false);
		m_APBaseAmountText.setEditable(false);
		m_endBalanceCurrText.setEditable(false);
		m_endBalaceAmountText.setEditable(false);
		m_refNoText.setEditable(false);
		m_transactionDateDate.setEditable(false);
		m_projectText.setEditable(false);
		m_customerText.setEditable(false);
		m_workDescTextArea.setEditable(false);
		m_unitText.setEditable(false);
		m_activityText.setEditable(false);
		m_departmentText.setEditable(false);
		m_ORNoText.setEditable(false);
		m_contractNoText.setEditable(false);
		m_IPCNoText.setEditable(false);
		m_descrptionTextArea.setEditable(false);
		m_remarksTextArea.setEditable(false);
		m_originatorPanel.setEnabled(false);
		m_approvedPanel.setEnabled(false);
		m_receivedPanel.setEnabled(false);

		m_BegBalanceBaseCurrPurcRecText.setEditable(false);
		m_BegBalanceBasePurcRecAmountText.setEditable(false);

		m_EndBalanceBaseCurrAccPayText.setEditable(false);
		m_EndBalanceBaseAccPayAmountText.setEditable(false);

		//jangan dihapus dulu
		/*CurrText.setText("1");
		 ExchRateText.setText("2");

		 PurchaseCurrText.setText("3");
		 PurchaseAmountText.setText("4");

		 APCurrText.setText("5");
		 APAmountText.setText("6");

		 AccumulatedPaymentCurrText.setText("7");
		 AccumulatedPaymentAmountText.setText("8");

		 BeginningBalanceCurrText.setText("9");
		 BeginningBalanceAmountText.setText("10");

		 APPaymentCurrText.setText("11");
		 APPaymentExchRateText.setText("12");

		 APPaymentAmountText.setText("13");

		 APPaymentTax23PercentText.setText("14");
		 APPaymentTax23CurrText.setText("15");
		 APPaymentTax23AmountText.setText("16");

		 TotalPaymentCurrText.setText("17");
		 TotalPaymentAmountText.setText("18");

		 APBaseCurrText.setText("19");
		 APBaseAmountText.setText("20");

		 EndBalanceCurrText.setText("21");
		 EndBalaceAmountText.setText("22");*/
	}

	protected void enableEditMode() {
		m_paymentSourceCombo.setEnabled(true);
		m_accountComp.setEnabled(true);
		m_purchRcptNoComp.setEnabled(true);
		m_detailComp.setEnabled(true);
		m_APPaymentAmountText.setEditable(true);
		m_APPaymentTax23PercentText.setEditable(true);
		m_APPaymentTax23AmountText.setEditable(true);
		m_transactionDateDate.setEditable(true);
		m_descrptionTextArea.setEditable(true);
		m_remarksTextArea.setEditable(true);
		m_originatorPanel.setEnabled(true);
		m_approvedPanel.setEnabled(true);
		m_receivedPanel.setEnabled(true);
		m_currpayment.setEnabled(true);

		/*if (accountComp().getObject()!=null) {
			CashBankAccount cba = (CashBankAccount) accountComp().getObject();
			if (cba.getCurrency().getIndex()==baseCurrency.getIndex())
				m_APPaymentExchRateText.setEditable(false);
			else
				m_APPaymentExchRateText.setEditable(true);
		}*/
		Currency curr = (Currency) m_currpayment.getObject();
		if (curr != null) {
			if (curr.getIndex() == baseCurrency.getIndex()) {
				m_APPaymentExchRateText.setEditable(false);
			} else {
				m_APPaymentExchRateText.setEditable(true);
			}
		}
	}

	protected Object createNew() {
		PurchaseApPmt a  = new PurchaseApPmt();
		a.setEmpOriginator(m_defaultOriginator);
		a.setEmpApproved(m_defaultApproved);
		a.setEmpReceived(m_defaultReceived);
		return a ;
	}

	void setEntity(Object m_entity) {
		PurchaseApPmt oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PurchaseApPmt)m_entity;
		this.m_entity.addPropertyChangeListener(this);
		if(this.m_entity.getBeginningBalance()!=null){
			setBeginningBalance();
		}
	}

	private void setBeginningBalance() {
		BeginningAccountPayable bar = this.m_entity.getBeginningBalance();
		BeginningBalanceBusinessLogic logic = new BeginningBalanceBusinessLogic(m_conn, m_sessionid);
		bar.setTrans(logic.findTransaction(bar.getProject().getUnit()));
		bar.showReferenceNo(true);
		this.m_entity.setBeginningBalance(bar);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		if ("object".equals(evt.getPropertyName())) {
			if (evt.getSource() == m_purchRcptNoComp) {
				purchaseReceiptOnChange(evt);
				Object objaccount =m_currpayment.getObject();
				if (objaccount instanceof Currency) {
					Currency curr = (Currency) objaccount;
					revalidateAccount(curr);
				}
				hitungAccountPayablePayment();
			}
			else if (evt.getSource() == accountComp()){
				setPaymentCurrency();
				hitungAccountPayablePayment();
			}
			else if (evt.getSource()==m_currpayment){
				Object objcurr= m_currpayment.getObject();
				if (objcurr instanceof Currency) {
					Currency curr = (Currency) objcurr;
					m_currpayment.setObject(curr);
					m_APPaymentTax23CurrText.setText(curr.getSymbol());
					m_totalPaymentCurrText.setText(curr.getSymbol());
					if(curr.getSymbol().equals(baseCurrency.getSymbol())){
						//m_APPaymentExchRateText.setValue(new Double(1));
						m_APPaymentExchRateText.setEditable(false);
					} else {
						m_APPaymentExchRateText.setEditable(true);
						/*if (entity().getAppMtExchRate()>0)
							m_APPaymentExchRateText.setValue(new Double(entity().getAppMtExchRate()));
						else
							m_APPaymentExchRateText.setValue(new Double(1));*/
					}
					setDefaultExchangeRate(curr);
					revalidateAccount(curr);
				}
			}
		}
		if ("value".equals(evt.getPropertyName())) {
			if (evt.getSource() == m_APPaymentAmountText) {
				if (evt.getNewValue() != evt.getOldValue()) {
					hitungTaxArt23();
					hitungAccountPayablePayment();
				}
			} else if (evt.getSource() == m_APPaymentExchRateText) {
				hitungTaxArt23();
				hitungAccountPayablePayment();
			} else if (evt.getSource() == m_APPaymentTax23PercentText) {
				hitungTaxArt23();
				hitungAccountPayablePayment();
			} else if (evt.getSource() == m_APPaymentTax23AmountText) {
				hitungAccountPayablePaymentWithTaxArt21();
			}
		}
		if ("date".equals(evt.getPropertyName())) {
			if (evt.getSource() == m_transactionDateDate) {
				CashBankAccount cba = (CashBankAccount) accountComp().getObject();
				if (cba!=null) {
					Currency curr = cba.getCurrency();
					setDefaultExchangeRate(curr);
				}
			}
		}
	}


	

	private void setEndBalanceBaseCurrency() {
		Object obj = m_purchRcptNoComp.getObject();

		if (obj != null) {
			Currency prCurr = baseCurrency;
			double prExchRate = 0;

			if (obj instanceof PurchaseReceipt) {
				PurchaseReceipt pr = (PurchaseReceipt) obj;

				prCurr = pr.getApCurr();
				prExchRate = pr.getApexChRate();
			} else {
				BeginningAccountPayable bb = (BeginningAccountPayable) obj;

				prCurr = bb.getCurrency();
				prExchRate = bb.getExchangeRate();
			}
			m_EndBalanceBaseCurrAccPayText.setText(baseCurrency.getSymbol());

			Number endBalNbr = (Number) m_endBalaceAmountText.getValue();
			if (prCurr.getSymbol().equals(baseCurrency.getSymbol())) {
				m_EndBalanceBaseAccPayAmountText.setValue(endBalNbr);
			} else {
				double endBal = endBalNbr.doubleValue();
				double val = endBal * prExchRate;
				m_EndBalanceBaseAccPayAmountText.setValue(new Double(val));
			}
		}

	}

	private void setBegBalanceBaseCurrency() {
		Object obj = m_purchRcptNoComp.getObject();

		if (obj != null) {
			Currency prCurr = baseCurrency;
			double prExchRate = 0;

			if (obj instanceof PurchaseReceipt) {
				PurchaseReceipt pr = (PurchaseReceipt) obj;

				prCurr = pr.getApCurr();
				prExchRate = pr.getApexChRate();
			} else {
				BeginningAccountPayable bb = (BeginningAccountPayable) obj;

				prCurr = bb.getCurrency();
				prExchRate = bb.getExchangeRate();
			}

			m_BegBalanceBaseCurrPurcRecText.setText(baseCurrency
					.getSymbol());

			Number begBalNbr = (Number) m_beginningBalanceAmountText
			.getValue();
			if (prCurr.getSymbol().equals(baseCurrency.getSymbol())) {
				m_BegBalanceBasePurcRecAmountText.setValue(begBalNbr);
			} else {
				double begBal = begBalNbr.doubleValue();
				double val = begBal * prExchRate;
				m_BegBalanceBasePurcRecAmountText.setValue(new Double(val));
			}
		}
	}

	private void setPaymentCurrency() {
		CashBankAccount cb = (CashBankAccount) accountComp().getObject();
		if(cb!=null){
			//Currency curr = cb.getCurrency();

			/*m_APPaymentCurrText.setObject("a");
			m_APPaymentTax23CurrText.setText("a");
			m_totalPaymentCurrText.setText("a");*/


		}
	}

	private void purchaseReceiptOnChange(PropertyChangeEvent evt) {
		Object obj = m_purchRcptNoComp.getObject();
		if ( obj instanceof PurchaseReceipt) {
			PurchaseReceipt a = (PurchaseReceipt) m_purchRcptNoComp.getObject();
			if (a.getTransactionDate()!=null)
				m_purchRcptDateText.setText(a.getTransactionDate().toString());
			else
				m_purchRcptDateText.setText("");
			m_invoiceNoText.setText(a.getInvoice());
			m_invoiceDateText.setText(a.getInvoiceDate().toString());
			if (a.getSupplier()!=null)
				m_paytoText.setText(a.getSupplier().toString());
			else
				m_paytoText.setText("");
			m_supplierBankTextArea.setText(a.getBankAccount());
			if (a.getApCurr()!=null){
				String curr = a.getApCurr().toString();
				m_currText.setText(curr);
				if (curr.equals(baseCurrency.getSymbol()))
					m_exchRateText.setValue(new Double(1));
				else
					m_exchRateText.setValue(new Double(a.getApexChRate()));

				m_purchaseCurrText.setText(curr);
				m_purchaseAmountText.setValue(new Double(a.getAmount()));
				m_APCurrText.setText(curr);
				m_APAmountText.setValue(new Double(a.getAmount()+a.getVatAmount()));
				m_accumulatedPaymentCurrText.setText(curr);
				double accAmt = getAcumulateAmount(a);
				if(accAmt>0)
					m_accumulatedPaymentAmountText.setValue(new Double(accAmt));
				else
					m_accumulatedPaymentAmountText.setValue(new Double(0.0));
				m_beginningBalanceCurrText.setText(curr);

/*
				m_currpayment.setObject(curr);
				if (curr.equals(baseCurrency.getSymbol())){
					m_APPaymentExchRateText.setValue(new Double(1));
					m_APPaymentExchRateText.setEditable(false);
				}else{
					setDefaultExchangeRate(a.getApCurr());
					m_APPaymentExchRateText.setEditable(true);
				}
*/
				m_APPaymentAmountText.setValue(null);
				m_APPaymentTax23AmountText.setValue(null);
				m_APPaymentTax23PercentText.setValue(null);
				m_totalPaymentAmountText.setValue(null);
				m_endBalaceAmountText.setValue(null);

				//m_APPaymentTax23CurrText.setText(curr);
				//m_totalPaymentCurrText.setText(curr);
				m_endBalanceCurrText.setText(curr);
			}
			if (a != null) {
				ProjectData b = a.getProject();
				setGuiProject(b);
			}
			if(m_paymentSourceCombo.getSelectedItem().equals("Cash")){
				m_accountComp.setObject(entity().getCashAccount());
			}else{
				m_accountComp.setObject(entity().getBankAccount());
			}
		}
		else if (obj instanceof BeginningAccountPayable){
			BeginningAccountPayable a = (BeginningAccountPayable) m_purchRcptNoComp.getObject();
			if (a.getTrans().getTransDate()!=null)
				m_purchRcptDateText.setText(a.getTrans().getTransDate().toString());
			else
				m_purchRcptDateText.setText("");
			m_invoiceNoText.setText(a.getTrans().getReference());
			m_invoiceDateText.setText("");
			if (a.getPartner()!=null)
				m_paytoText.setText(a.getPartner().toString());
			else
				m_paytoText.setText("");
			m_supplierBankTextArea.setText("");
			if (a.getCurrency()!=null){
				String curr = a.getCurrency().toString();
				m_currText.setText(curr);
				if (curr.equals(baseCurrency.getSymbol()))
					m_exchRateText.setValue(new Double(1));
				else
					m_exchRateText.setValue(new Double(a.getExchangeRate()));
				m_purchaseCurrText.setText("");
				m_purchaseAmountText.setValue(new Double(0));
				m_APCurrText.setText(curr);
				m_APAmountText.setValue(new Double(a.getAccValue()));
				m_accumulatedPaymentCurrText.setText(curr);
				double accAmt = getAcumulateAmount(a);
				if(accAmt>0)
					m_accumulatedPaymentAmountText.setValue(new Double(accAmt));
				else
					m_accumulatedPaymentAmountText.setValue(new Double(0.0));
				m_beginningBalanceCurrText.setText(curr);
				/*m_currpayment.setObject(curr);
				if (curr.equals(baseCurrency.getSymbol())){
					m_APPaymentExchRateText.setValue(new Double(1));
					m_APPaymentExchRateText.setEditable(false);
				}else{
					setDefaultExchangeRate(a.getCurrency());
					m_APPaymentExchRateText.setEditable(true);
				}*/
				m_APPaymentAmountText.setValue(null);
				m_APPaymentTax23AmountText.setValue(null);
				m_APPaymentTax23PercentText.setValue(null);
				m_totalPaymentAmountText.setValue(null);
				m_endBalaceAmountText.setValue(null);

				/*m_APPaymentTax23CurrText.setText(curr);
				m_totalPaymentCurrText.setText(curr);*/
				m_endBalanceCurrText.setText(curr);
			}
			if (a != null){
				ProjectData b = a.getProject();
				setGuiProject(b);
			}
			if(m_paymentSourceCombo.getSelectedItem().equals("Cash"))
				m_accountComp.setObject(entity().getCashAccount());
			else
				m_accountComp.setObject(entity().getBankAccount());
		}
		else {
			m_invoiceNoText.setText("");
			m_invoiceDateText.setText("");
			m_currText.setText("");
			m_exchRateText.setText("");
			m_projectText.setText("");
			m_workDescTextArea.setText("");
			m_ORNoText.setText("");
			m_contractNoText.setText("");
			m_IPCNoText.setText("");
			m_customerText.setText("");
			m_unitText.setText("");
			m_activityText.setText("");
			m_departmentText.setText("");
		}

	}

	private void setGuiProject(ProjectData b) {
		m_projectText.setText(b.toString());
		m_workDescTextArea.setText(b.getWorkDescription());
		m_ORNoText.setText(b.getORNo());
		m_contractNoText.setText(b.getPONo());
		m_IPCNoText.setText(b.getIPCNo());
		m_customerText.setText("");
		if (b.getCustomer() != null)
			m_customerText.setText(b.getCustomer().toString());
		m_unitText.setText("");
		if (b.getUnit() != null)
			m_unitText.setText(b.getUnit().toString());
		m_activityText.setText("");
		if (b.getActivity() != null)
			m_activityText.setText(b.getActivity().toString());
		m_departmentText.setText("");
		if (b.getDepartment() != null)
			m_departmentText.setText(b.getDepartment().toString());
	}

	protected double getAcumulateAmount(Object obj) {
		double amt = 0;
		if (obj != null) {
			if (obj instanceof PurchaseReceipt) {
				PurchaseReceipt receipt = (PurchaseReceipt) obj;
				PurchaseReceiptBusinessLogic logic = new PurchaseReceiptBusinessLogic(
						m_conn, m_sessionid);
				amt = logic.getAccumulatedPayment(receipt);
			} else {
				BeginningAccountPayable receipt = (BeginningAccountPayable) obj;
				BeginningAccountPayableBusinessLogic logic = new BeginningAccountPayableBusinessLogic(
						m_conn, m_sessionid);
				amt = logic.getAccumulatedPayable(receipt);
			}
		}

		return amt;
		/*try{
		 AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		 PurchaseApPmt[] purchaseApPmt = null;
		 String query = "";
		 if (obj instanceof PurchaseReceipt) {
		 PurchaseReceipt tempClass = (PurchaseReceipt) obj;
		 query = "SELECT * FROM " + IDBConstants.TABLE_PURCHASE_AP_PMT + " where purchasereceipt=" + tempClass.getIndex() + " and status=3";
		 }else if (obj instanceof BeginningAccountPayable) {
		 BeginningAccountPayable tempClass = (BeginningAccountPayable) obj;
		 query = "SELECT * FROM " + IDBConstants.TABLE_PURCHASE_AP_PMT + " where beginningbalance=" + tempClass.getIndex() + " and status=3";

		 }
		 purchaseApPmt = logic.getAllPurchaseApPmtByReceipt(m_sessionid,IDBConstants.MODUL_ACCOUNTING,query);
		 double data = 0;
		 for (int i =0; i<purchaseApPmt.length;i++){
		 data += purchaseApPmt[i].getAppMtAmount();
		 }
		 return data;
		 }catch(Exception e){
		 }
		 return 0;*/
	}

	protected void revalidateAccountComp(LookupPicker accountComp) {
		accountComp().removePropertyChangeListener("object", this);
		accountComp.addPropertyChangeListener("object",this);

		m_leftPanel.remove(accountComp());
		m_leftPanel.revalidate();
		m_leftPanel.repaint();
		if (accountComp!=null)
			setAccountComp(accountComp);
		accountComp().setPreferredSize(new java.awt.Dimension(310, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		m_leftPanel.add(accountComp(), gridBagConstraints);
		m_leftPanel.revalidate();
	}

	private void setAccountComp(LookupPicker accountComp) {
		LookupPicker old = m_accountComp;
		if (old!=null){
			old.removePropertyChangeListener("object",this);
		}
		m_accountComp = accountComp;
		m_accountComp.addPropertyChangeListener("object",this);
	}

	private LookupPicker accountComp(){
		return m_accountComp;
	}

	protected void doSave() {
		if (!cekValidity()) return;
		gui2entity();
		if (hasAnotherUnpostedTransaction()) return;
		if (checkBalance()) return;
		m_isitacamount = false;
		super.doSave();

	}

	private boolean checkBalance() {
		boolean isOutstanding = false;
		if (entity().getPurchaseReceipt()!=null){
			PurchaseReceiptBusinessLogic logic = new PurchaseReceiptBusinessLogic(m_conn, m_sessionid);
			PurchaseReceipt receipt = entity().getPurchaseReceipt();
			if(receipt==null)
				return false;
			double totalPaid = logic.getAccumulatedPayment(receipt);
			double purchase = receipt.getAmount();
			double vat = receipt.getVatAmount();
			double ap = purchase + vat;
			PurchaseApPmt pmt = (PurchaseApPmt)entity();
			double payment = pmt.getAppMtAmount();

			Currency pmtCurr = pmt.getAppMtCurr();
			Currency apCurr = receipt.getApCurr();
			double apExchRate = receipt.getApexChRate();
			if(!pmtCurr.getSymbol().equals(baseCurrency.getSymbol())){
				payment = payment * apExchRate;
			}
			if(!apCurr.getSymbol().equals(baseCurrency.getSymbol())){
				ap = ap * apExchRate;
				totalPaid = totalPaid * apExchRate;
			}
			totalPaid += payment;

			double balance = ap - totalPaid;
			isOutstanding = balance < 0;

		}else if (entity().getBeginningBalance()!=null){
			/*GenericMapper maper = MasterMap.obtainMapperFor(PurchaseApPmt.class);
			 maper.setActiveConn(m_conn);
			 String strWhere = IDBConstants.ATTR_STATUS + "=3 AND " + IDBConstants.ATTR_BEGINNING_BALANCE + "=" + entity().getBeginningBalance().getIndex();
			 List list = maper.doSelectWhere(strWhere);
			 Iterator iterator = list.iterator();
			 double totAmount = 0;
			 while(iterator.hasNext()){
			 PurchaseApPmt purcAppPmt = (PurchaseApPmt) iterator.next();
			 totAmount += purcAppPmt.getAppMtAmount();
			 }	*/

			BeginningAccountPayableBusinessLogic logic = new BeginningAccountPayableBusinessLogic(m_conn, m_sessionid);
			BeginningAccountPayable bb = entity().getBeginningBalance();
			double totalPaid = logic.getAccumulatedPayable(bb);
			double ap = bb.getAccValue();

			PurchaseApPmt pmt = (PurchaseApPmt)entity();
			double payment = pmt.getAppMtAmount();

			Currency pmtCurr = pmt.getAppMtCurr();
			Currency apCurr = bb.getCurrency();
			double apExchRate = bb.getExchangeRate();
			if (pmtCurr!=null)
				if(!pmtCurr.getSymbol().equals(baseCurrency.getSymbol())){
					payment = payment * apExchRate;
				}
			if(!apCurr.getSymbol().equals(baseCurrency.getSymbol())){
				ap = ap * apExchRate;
				totalPaid = totalPaid * apExchRate;
			}
			totalPaid += payment;

			double balance = ap - totalPaid;

			//isOutstanding = (totAmount+entity().getAppMtAmount()) > (entity().getBeginningBalance().getAccValue()+(entity().getBeginningBalance().getAccValue()*0.1));
			isOutstanding = balance < 0;

		}

		if (isOutstanding){
			JOptionPane.showMessageDialog(this,"This payment will exceed the Purchase Receipt. Please Check your Account Payable Payment amount");
		}
		return isOutstanding;
	}

	private boolean hasAnotherUnpostedTransaction() {
		boolean result = false;
		PurchaseAccountPayablePaymentBusinessLogic logic = new PurchaseAccountPayablePaymentBusinessLogic(m_conn, m_sessionid);
		try {
			result = logic.hasUnpostedTransaction(entity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result){
			String referenceno = "";
			if (entity().getPurchaseReceipt()!=null)
				referenceno = entity().getPurchaseReceipt().getReferenceNo();
			else if (entity().getBeginningBalance()!=null)
				referenceno = entity().getBeginningBalance().getTrans().getReference();
			JOptionPane.showMessageDialog(this, "There is a transaction for Invoice: " + referenceno +
					" " + "that has not been posted yet.\n" +
			"Please post it before creating a new transaction.");
		}
		return result;
	}

	private void tobase(){
		if (!m_APPaymentAmountText.getText().equals("")){
			//hitungAccountPayablePayment();
		}
		if (!m_APPaymentTax23PercentText.getText().equals("")){
			hitungAccountPayablePayment();
		}
	}

	public void changedUpdate(DocumentEvent e) {
		tobase();
	}

	public void insertUpdate(DocumentEvent e) {
		tobase();
	}

	public void removeUpdate(DocumentEvent e) {
		tobase();
	}
}
