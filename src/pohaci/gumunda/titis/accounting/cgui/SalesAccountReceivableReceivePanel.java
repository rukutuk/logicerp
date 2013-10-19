package pohaci.gumunda.titis.accounting.cgui;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.cgui.report.Rcpt_AR;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.SalesReceived;
import pohaci.gumunda.titis.accounting.entity.SalesReceivedLoader;
import pohaci.gumunda.titis.accounting.logic.BeginningAccountReceivableBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.BeginningBalanceBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.SalesInvoiceBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.SalesReceivedBusinessLogic;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class SalesAccountReceivableReceivePanel extends RevTransactionPanel
		implements ActionListener, PropertyChangeListener, DocumentListener {
	private static final long serialVersionUID = 1L;

	private SalesReceived m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	private Document ARRcvAmountDoc;
	private Document Tax23AmountDoc;
	private Document Tax23PercentDoc;
	private Document ARRcvExchRateDoc;
	private Document Tax23ExchRateDoc;
	private JTextField transGainLossCurrText;
	private JFormattedTextField transGainLossAmountText;

	public SalesAccountReceivableReceivePanel(Connection conn, long sessionid) {
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
		setEntity(new SalesReceived());
		m_entityMapper = MasterMap.obtainMapperFor(SalesReceived.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}

	private void setDefaultExchangeRate(Currency currency) {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		ARRcvExchRateText.setValue(new Double(rate));
	}

	private void enableAwal() {
		disableSomeComponents();
	}

	private void initComponents() {
		retentionCurrText = new JTextField();
		retentionText = new JFormattedTextField(m_numberFormatter);
		AccountComp = new LookupBankAccountPicker(m_conn, m_sessionid);
		ProjectCodeText = new JTextField();
		OriginatorComp = new AssignPanel(m_conn, m_sessionid, "Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid, "Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid, "Received by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon(
				"../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon(
				"../images/filter.gif"));
		TopButtonPanel = new javax.swing.JPanel();
		java.awt.GridBagConstraints gridBagConstraints;
		mainPanel = new javax.swing.JPanel();
		extTopButtonPanel = new javax.swing.JPanel();
		TopButtonPanel = new javax.swing.JPanel();
		m_newBtn = new javax.swing.JButton();
		m_editBtn = new javax.swing.JButton();
		m_saveBtn = new javax.swing.JButton();
		m_deleteBtn = new javax.swing.JButton();
		m_cancelBtn = new javax.swing.JButton();
		m_submitBtn = new javax.swing.JButton();
		extLeftRightPanel = new javax.swing.JPanel();
		leftPanel = new javax.swing.JPanel();
		StatusLabel = new javax.swing.JLabel();
		SubmittedDateLabel = new javax.swing.JLabel();
		ReceiveToLabel = new javax.swing.JLabel();
		InvoiceNoLabel = new javax.swing.JLabel();
		InvoiceDateLabel = new javax.swing.JLabel();
		CustomerStatusLabel = new javax.swing.JLabel();
		AccountLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		RcvToText = new javax.swing.JTextField();
		InvoiceNoComp = new LookupInvoicePicker(m_conn, m_sessionid);
		InvoiceDateText = new javax.swing.JTextField();
		DownPaymentLabel = new javax.swing.JLabel();
		salesPanel = new javax.swing.JPanel();
		SalesCurrText = new javax.swing.JTextField();
		// SalesAmountText = new javax.swing.JTextField();
		SalesAmountText = new JFormattedTextField(m_numberFormatter);
		accountReceivablePanel = new javax.swing.JPanel();
		ARCurrText = new javax.swing.JTextField();
		// ARAmountText = new javax.swing.JTextField();
		ARAmountText = new JFormattedTextField(m_numberFormatter);
		DownPaymentLabel1 = new javax.swing.JLabel();
		accumalateReceivePanel = new javax.swing.JPanel();
		AccReceiveCurrText = new javax.swing.JTextField();
		// AccReceiveAmountText = new javax.swing.JTextField();
		AccReceiveAmountText = new JFormattedTextField(m_numberFormatter);
		DetailBtn = new javax.swing.JButton();
		DownPaymentLabel2 = new javax.swing.JLabel();
		begBalPanel = new javax.swing.JPanel();
		BeginBalanceCurrText = new javax.swing.JTextField();
		// BeginBalanceAmountText = new javax.swing.JTextField();
		BeginBalanceAmountText = new JFormattedTextField(m_numberFormatter);
		DownPaymentLabel3 = new javax.swing.JLabel();
		CurrencyLabel1 = new javax.swing.JLabel();
		jPanel1_2_2_1_1 = new javax.swing.JPanel();
		CurrComp = new javax.swing.JTextField();
		ExchRateLabel1 = new javax.swing.JLabel();
		// ExchRateText = new javax.swing.JTextField();
		ExchRateText = new JFormattedTextField(m_numberFormatter);
		accReceivableReceivePanel = new javax.swing.JPanel();
		SalesAmountLabel1 = new javax.swing.JLabel();
		TaxArt23Label1 = new javax.swing.JLabel();
		// Yulan
		VATLabel1 = new javax.swing.JLabel();
		AmountReceiveLabel1 = new javax.swing.JLabel();
		RecvBaseCurrentLabel1 = new javax.swing.JLabel();
		BegBalBaseCurrLabel = new javax.swing.JLabel();
		begBalBaseCurrPanel = new javax.swing.JPanel();
		EndBalBaseCurrLabel = new javax.swing.JLabel();
		ExchRateLabel4 = new javax.swing.JLabel();
		// Tax23ExchRateText = new javax.swing.JTextField();
		Tax23ExchRateText = new JFormattedTextField(m_numberFormatter);
		// Yulan
		VATExchRateText = new JFormattedTextField(m_numberFormatter);
		ExchRateLabel6 = new javax.swing.JLabel();
		// Tax23PercentText = new javax.swing.JTextField();
		Tax23PercentText = new JFormattedTextField(m_numberFormatter);
		TotalRcvCurrText = new javax.swing.JTextField();
		EndBalanceCurrText = new javax.swing.JTextField();
		BegBalBaseCurrText = new javax.swing.JTextField();
		EndBalBaseCurrText = new javax.swing.JTextField();
		CurrencyLabel8 = new javax.swing.JLabel();
		Tax23CurrText = new javax.swing.JTextField();
		// Yulan
		VATCurrText = new javax.swing.JTextField();
		// TotalRcvAmountText = new javax.swing.JTextField();
		TotalRcvAmountText = new JFormattedTextField(m_numberFormatter);
		// EndBalanceAmountText = new javax.swing.JTextField();
		EndBalanceAmountText = new JFormattedTextField(m_numberFormatter);
		BegBalBaseCurrAmountText = new JFormattedTextField(m_numberFormatter);
		EndBalBaseCurrAmountText = new JFormattedTextField(m_numberFormatter);
		ARSalesAmountText = new JFormattedTextField(m_numberFormatter);
		ARRcvCurrText = new javax.swing.JTextField();
		ExchRateLabel5 = new javax.swing.JLabel();
		// ARRcvExchRateText = new javax.swing.JTextField();
		ARRcvExchRateText = new JFormattedTextField(m_numberFormatter);
		// Tax23AmountText = new javax.swing.JTextField();
		Tax23AmountText = new JFormattedTextField(m_numberFormatter);
		VATAmountText = new JFormattedTextField(m_numberFormatter);
		AmountReceiveLabel2 = new javax.swing.JLabel();
		AmountBaseCurrText = new javax.swing.JTextField();
		// AmountBaseText = new javax.swing.JTextField();
		AmountBaseText = new JFormattedTextField(m_numberFormatter);
		separatorAccRecPanel = new javax.swing.JPanel();
		SalesAdvanceReceiveLabel = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		separatorInvPanel = new javax.swing.JPanel();
		SalesAdvanceReceiveLabel3 = new javax.swing.JLabel();
		jSeparator4 = new javax.swing.JSeparator();
		CustomerStatusCombo = new javax.swing.JComboBox();
		CashBankCombo = new JComboBox();
		extRightPanel = new javax.swing.JPanel();
		rightPanel = new javax.swing.JPanel();
		ReceiptNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		ReceiptDateLabel = new javax.swing.JLabel();
		ProjectCodeLabel = new javax.swing.JLabel();
		CustomerLabel = new javax.swing.JLabel();
		WorkDescLabel = new javax.swing.JLabel();
		CustomerText = new javax.swing.JTextField();
		WorkDescScroll = new javax.swing.JScrollPane();
		WorkDescTextArea = new javax.swing.JTextArea();
		UnitCodeLabel = new javax.swing.JLabel();
		UnitCodeText = new javax.swing.JTextField();
		ActivityCodeLabel = new javax.swing.JLabel();
		ActivityCodeText = new javax.swing.JTextField();
		DepartmentLabel = new javax.swing.JLabel();
		ORNoLabel = new javax.swing.JLabel();
		DepartmentText = new javax.swing.JTextField();
		ORNoText = new javax.swing.JTextField();
		SOPOContractNo = new javax.swing.JLabel();
		IPCNoLabel = new javax.swing.JLabel();
		ContractNoText = new javax.swing.JTextField();
		IPCNoText = new javax.swing.JTextField();
		RemarkLabel = new javax.swing.JLabel();
		RemarkScroll = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScroll = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();

		bottomPanel = new javax.swing.JPanel();
		ApprovedPanel = new javax.swing.JPanel();
		receivedPanel = new javax.swing.JPanel();
		setLayout(new java.awt.BorderLayout());

		setPreferredSize(new java.awt.Dimension(750, 670));
		mainPanel.setLayout(new java.awt.BorderLayout());

		mainPanel.setPreferredSize(new java.awt.Dimension(600, 690));
		extTopButtonPanel.setLayout(new java.awt.BorderLayout());
		extTopButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
		TopButtonPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.LEFT, 3, 5));

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

		extTopButtonPanel.add(TopButtonPanel, java.awt.BorderLayout.WEST);

		mainPanel.add(extTopButtonPanel, java.awt.BorderLayout.NORTH);

		extLeftRightPanel.setLayout(new java.awt.BorderLayout());

		extLeftRightPanel.setPreferredSize(new java.awt.Dimension(650, 400));
		leftPanel.setLayout(new java.awt.GridBagLayout());

		leftPanel.setPreferredSize(new java.awt.Dimension(490, 430));

		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		ReceiveToLabel.setText("Receive To");
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(110, 15));

		SubmittedDateLabel.setText("Submitted Date");
		SubmittedDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));

		// ============
		salesPanel.setLayout(new java.awt.GridBagLayout());
		salesPanel.setPreferredSize(new java.awt.Dimension(350, 20));

		SalesCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		salesPanel.add(SalesCurrText, gridBagConstraints);

		SalesAmountText.setPreferredSize(new java.awt.Dimension(297, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		salesPanel.add(SalesAmountText, gridBagConstraints);

		// ============
		accountReceivablePanel.setLayout(new java.awt.GridBagLayout());
		accountReceivablePanel
				.setPreferredSize(new java.awt.Dimension(350, 20));

		ARCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accountReceivablePanel.add(ARCurrText, gridBagConstraints);

		ARAmountText.setPreferredSize(new java.awt.Dimension(297, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accountReceivablePanel.add(ARAmountText, gridBagConstraints);

		// ============
		accumalateReceivePanel.setLayout(new java.awt.GridBagLayout());
		accumalateReceivePanel
				.setPreferredSize(new java.awt.Dimension(350, 20));
		AccReceiveCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		accumalateReceivePanel.add(AccReceiveCurrText, gridBagConstraints);

		AccReceiveAmountText.setPreferredSize(new java.awt.Dimension(233, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		accumalateReceivePanel.add(AccReceiveAmountText, gridBagConstraints);
		DetailBtn.setText("...");
		DetailBtn.setPreferredSize(new java.awt.Dimension(60, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		accumalateReceivePanel.add(DetailBtn, gridBagConstraints);

		// ============
		begBalPanel.setLayout(new java.awt.GridBagLayout());
		begBalPanel.setPreferredSize(new java.awt.Dimension(350, 20));
		BeginBalanceCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		begBalPanel.add(BeginBalanceCurrText, gridBagConstraints);

		BeginBalanceAmountText
				.setPreferredSize(new java.awt.Dimension(297, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		begBalPanel.add(BeginBalanceAmountText, gridBagConstraints);

		// ============
		begBalBaseCurrPanel.setLayout(new java.awt.GridBagLayout());
		begBalBaseCurrPanel.setPreferredSize(new java.awt.Dimension(350, 20));
		BegBalBaseCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		begBalBaseCurrPanel.add(BegBalBaseCurrText, gridBagConstraints);

		BegBalBaseCurrAmountText.setPreferredSize(new java.awt.Dimension(297,
				18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		begBalBaseCurrPanel.add(BegBalBaseCurrAmountText, gridBagConstraints);

		// ============
		jPanel1_2_2_1_1.setLayout(new java.awt.GridBagLayout());
		jPanel1_2_2_1_1.setPreferredSize(new java.awt.Dimension(350, 20));
		CurrComp.setPreferredSize(new java.awt.Dimension(60, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jPanel1_2_2_1_1.add(CurrComp, gridBagConstraints);

		ExchRateLabel1.setText("Exch. Rate*");
		ExchRateLabel1.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1_1.add(ExchRateLabel1, gridBagConstraints);

		ExchRateText.setPreferredSize(new java.awt.Dimension(222, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_2_1_1.add(ExchRateText, gridBagConstraints);

		// ============
		accReceivableReceivePanel.setLayout(new java.awt.GridBagLayout());
		accReceivableReceivePanel.setPreferredSize(new java.awt.Dimension(465,
				220));

		SalesAmountLabel1.setText("Sales*");
		SalesAmountLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(SalesAmountLabel1, gridBagConstraints);

		VATLabel1.setText("VAT*");
		VATLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 1;
		accReceivableReceivePanel.add(VATLabel1, gridBagConstraints);

		TaxArt23Label1.setText("Tax Art 22/23*");
		TaxArt23Label1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 2;
		accReceivableReceivePanel.add(TaxArt23Label1, gridBagConstraints);

		JLabel bankchargeslbl = new JLabel("Bank Charges");
		bankchargeslbl.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 3;
		accReceivableReceivePanel.add(bankchargeslbl, gridBagConstraints);

		JLabel retentionlbl = new JLabel("Retention");
		retentionlbl.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 4;
		accReceivableReceivePanel.add(retentionlbl, gridBagConstraints);

		JLabel translationLbl = new JLabel("Translation");
		translationLbl.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 5;
		accReceivableReceivePanel.add(translationLbl, gridBagConstraints);

		AmountReceiveLabel1.setText("Total Receive");
		AmountReceiveLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 6;
		accReceivableReceivePanel.add(AmountReceiveLabel1, gridBagConstraints);

		AmountReceiveLabel2.setText("Amount Base Curr.");
		AmountReceiveLabel2.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 7;
		accReceivableReceivePanel.add(AmountReceiveLabel2, gridBagConstraints);

		RecvBaseCurrentLabel1.setText("Ending Balance");
		RecvBaseCurrentLabel1.setPreferredSize(new java.awt.Dimension(115, 15));
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		accReceivableReceivePanel
				.add(RecvBaseCurrentLabel1, gridBagConstraints);

		EndBalBaseCurrLabel.setText("End. Bal. Base Curr");
		EndBalBaseCurrLabel.setPreferredSize(new java.awt.Dimension(115, 15));
		gridBagConstraints.gridy = 9;
		accReceivableReceivePanel.add(EndBalBaseCurrLabel, gridBagConstraints);

		// Exch Rate buat VAT
		ExchRateLabel6.setText("Exch. Rate*");
		ExchRateLabel6.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 0;
		accReceivableReceivePanel.add(ExchRateLabel6, gridBagConstraints);

		ExchRateLabel4.setText("Exch. Rate*");
		ExchRateLabel4.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		accReceivableReceivePanel.add(ExchRateLabel4, gridBagConstraints);

		VATExchRateText.setPreferredSize(new java.awt.Dimension(55, 18));
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(VATExchRateText, gridBagConstraints);

		ExchRateLabel4.setText("Exch. Rate*");
		ExchRateLabel4.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		accReceivableReceivePanel.add(ExchRateLabel4, gridBagConstraints);

		Tax23ExchRateText.setPreferredSize(new java.awt.Dimension(55, 18));
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(Tax23ExchRateText, gridBagConstraints);

		Tax23PercentText.setPreferredSize(new java.awt.Dimension(30, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(Tax23PercentText, gridBagConstraints);

		VATCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(VATCurrText, gridBagConstraints);

		// Bank Charges
		BankchargesCurrText = new JTextField();
		BankchargesCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(BankchargesCurrText, gridBagConstraints);

		bankchargesAmountText = new JFormattedTextField(m_numberFormatter);
		bankchargesAmountText.setPreferredSize(new java.awt.Dimension(18, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel
				.add(bankchargesAmountText, gridBagConstraints);

		JLabel bankchargesExchRateLbl = new JLabel("Exch. Rate*");
		bankchargesExchRateLbl.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		accReceivableReceivePanel.add(bankchargesExchRateLbl,
				gridBagConstraints);

		bankChargeExchRateText = new JFormattedTextField(m_numberFormatter);
		bankChargeExchRateText.setPreferredSize(new java.awt.Dimension(55, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(bankChargeExchRateText,
				gridBagConstraints);

		retentionCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(retentionCurrText, gridBagConstraints);

		retentionText = new JFormattedTextField(m_numberFormatter);
		retentionText.setPreferredSize(new java.awt.Dimension(18, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(retentionText, gridBagConstraints);

		// Translation
		transGainLossCurrText = new JTextField();
		transGainLossCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel
				.add(transGainLossCurrText, gridBagConstraints);

		transGainLossAmountText = new JFormattedTextField(m_numberFormatter);
		transGainLossAmountText
				.setPreferredSize(new java.awt.Dimension(18, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(transGainLossAmountText,
				gridBagConstraints);

		/*
		 * JLabel transGainLossExchRateLbl=new JLabel("Exch. Rate*");
		 * transGainLossExchRateLbl.setPreferredSize(new java.awt.Dimension(60,
		 * 21)); gridBagConstraints = new java.awt.GridBagConstraints();
		 * gridBagConstraints.gridx = 5; gridBagConstraints.gridy = 4;
		 * gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		 * gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		 * accReceivableReceivePanel.add(transGainLossExchRateLbl,
		 * gridBagConstraints);
		 *
		 * transGainlossExchRateText = new
		 * JFormattedTextField(m_numberFormatter);
		 * transGainlossExchRateText.setPreferredSize(new java.awt.Dimension(55,
		 * 18)); gridBagConstraints = new java.awt.GridBagConstraints();
		 * gridBagConstraints.gridx = 6; gridBagConstraints.gridy = 4;
		 * gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		 * gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		 * gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		 * accReceivableReceivePanel.add(transGainlossExchRateText,
		 * gridBagConstraints);
		 */

		// total
		TotalRcvCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		accReceivableReceivePanel.add(TotalRcvCurrText, gridBagConstraints);

		EndBalanceCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		accReceivableReceivePanel.add(EndBalanceCurrText, gridBagConstraints);

		EndBalBaseCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		accReceivableReceivePanel.add(EndBalBaseCurrText, gridBagConstraints);

		CurrencyLabel8.setText("%");
		CurrencyLabel8.setPreferredSize(new java.awt.Dimension(18, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 0);
		accReceivableReceivePanel.add(CurrencyLabel8, gridBagConstraints);

		// Yulan
		VATAmountText.setPreferredSize(new java.awt.Dimension(18, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(VATAmountText, gridBagConstraints);

		Tax23CurrText.setPreferredSize(new java.awt.Dimension(30, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(Tax23CurrText, gridBagConstraints);

		TotalRcvAmountText.setPreferredSize(new java.awt.Dimension(270, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(TotalRcvAmountText, gridBagConstraints);

		EndBalanceAmountText.setPreferredSize(new java.awt.Dimension(270, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(EndBalanceAmountText, gridBagConstraints);

		EndBalBaseCurrAmountText.setPreferredSize(new java.awt.Dimension(270,
				18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(EndBalBaseCurrAmountText,
				gridBagConstraints);

		ARSalesAmountText.setPreferredSize(new java.awt.Dimension(60, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(ARSalesAmountText, gridBagConstraints);

		ARRcvCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		accReceivableReceivePanel.add(ARRcvCurrText, gridBagConstraints);

		ExchRateLabel5.setText("Exch. Rate*");
		ExchRateLabel5.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		accReceivableReceivePanel.add(ExchRateLabel5, gridBagConstraints);

		ARRcvExchRateText.setPreferredSize(new java.awt.Dimension(55, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(ARRcvExchRateText, gridBagConstraints);

		Tax23AmountText.setPreferredSize(new java.awt.Dimension(58, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 0);
		accReceivableReceivePanel.add(Tax23AmountText, gridBagConstraints);

		// ////////////////////////////

		AmountBaseCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		accReceivableReceivePanel.add(AmountBaseCurrText, gridBagConstraints);

		AmountBaseText.setPreferredSize(new java.awt.Dimension(300, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		accReceivableReceivePanel.add(AmountBaseText, gridBagConstraints);

		separatorAccRecPanel.setLayout(new java.awt.GridBagLayout());
		separatorAccRecPanel.setPreferredSize(new java.awt.Dimension(465, 20));
		SalesAdvanceReceiveLabel.setText("Account Receivable Receive");
		SalesAdvanceReceiveLabel.setPreferredSize(new java.awt.Dimension(160,
				15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		separatorAccRecPanel.add(SalesAdvanceReceiveLabel, gridBagConstraints);

		jSeparator1.setPreferredSize(new java.awt.Dimension(300, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		separatorAccRecPanel.add(jSeparator1, gridBagConstraints);

		separatorInvPanel.setLayout(new java.awt.GridBagLayout());
		separatorInvPanel.setPreferredSize(new java.awt.Dimension(375, 20));
		SalesAdvanceReceiveLabel3.setText("Invoice");
		SalesAdvanceReceiveLabel3.setPreferredSize(new java.awt.Dimension(50,
				15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		separatorInvPanel.add(SalesAdvanceReceiveLabel3, gridBagConstraints);

		jSeparator4.setPreferredSize(new java.awt.Dimension(310, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		separatorInvPanel.add(jSeparator4, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(StatusLabel, gridBagConstraints);

		leftPanel.add(SubmittedDateLabel, gridBagConstraints);

		gridBagConstraints.gridy = 2;
		leftPanel.add(ReceiveToLabel, gridBagConstraints);

		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		leftPanel.add(separatorInvPanel, gridBagConstraints);

		InvoiceNoLabel.setText("Invoice No. *");
		InvoiceNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
		leftPanel.add(InvoiceNoLabel, gridBagConstraints);

		InvoiceDateLabel.setText("Invoice Date");
		InvoiceDateLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 5;
		leftPanel.add(InvoiceDateLabel, gridBagConstraints);

		CustomerStatusLabel.setText("Customer Status");
		CustomerStatusLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 6;
		leftPanel.add(CustomerStatusLabel, gridBagConstraints);

		JLabel CashBankLabel = new JLabel("Receive From *");
		CashBankLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 7;
		leftPanel.add(CashBankLabel, gridBagConstraints);

		AccountLabel.setText("Account*");
		AccountLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 8;
		leftPanel.add(AccountLabel, gridBagConstraints);

		CurrencyLabel1.setText("Currency");
		CurrencyLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints.gridy = 9;
		leftPanel.add(CurrencyLabel1, gridBagConstraints);

		DownPaymentLabel.setText("Sales (before VAT)");
		DownPaymentLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		leftPanel.add(DownPaymentLabel, gridBagConstraints);

		DownPaymentLabel1.setText("Account Receivable");
		DownPaymentLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints.gridy = 11;
		leftPanel.add(DownPaymentLabel1, gridBagConstraints);

		DownPaymentLabel1.setText("Account Receivable");
		DownPaymentLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints.gridy = 11;
		leftPanel.add(DownPaymentLabel1, gridBagConstraints);

		DownPaymentLabel2.setText("Accumulated Receive");
		DownPaymentLabel2.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints.gridy = 12;
		leftPanel.add(DownPaymentLabel2, gridBagConstraints);

		DownPaymentLabel3.setText("Beginning Balance");
		DownPaymentLabel3.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints.gridy = 13;
		leftPanel.add(DownPaymentLabel3, gridBagConstraints);

		BegBalBaseCurrLabel.setText("Beg. Bal. Base Curr");
		gridBagConstraints.gridy = 14;
		leftPanel.add(BegBalBaseCurrLabel, gridBagConstraints);

		gridBagConstraints.gridy = 15;
		gridBagConstraints.gridwidth = 2;
		leftPanel.add(separatorAccRecPanel, gridBagConstraints);

		gridBagConstraints.gridy = 16;
		leftPanel.add(accReceivableReceivePanel, gridBagConstraints);

		StatusText.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		leftPanel.add(StatusText, gridBagConstraints);

		SubmittedDateText.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints.gridy = 1;
		leftPanel.add(SubmittedDateText, gridBagConstraints);

		RcvToText.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints.gridy = 2;
		leftPanel.add(RcvToText, gridBagConstraints);

		InvoiceNoComp.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints.gridy = 4;
		leftPanel.add(InvoiceNoComp, gridBagConstraints);

		InvoiceDateText.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints.gridy = 5;
		leftPanel.add(InvoiceDateText, gridBagConstraints);

		CustomerStatusCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "WAPU", "NONWAPU" }));
		CustomerStatusCombo.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints.gridy = 6;
		leftPanel.add(CustomerStatusCombo, gridBagConstraints);

		CashBankCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Cash", "Bank" }));
		CashBankCombo.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints.gridy = 7;
		leftPanel.add(CashBankCombo, gridBagConstraints);

		AccountComp.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints.gridy = 8;
		leftPanel.add(AccountComp, gridBagConstraints);

		gridBagConstraints.gridy = 9;
		leftPanel.add(jPanel1_2_2_1_1, gridBagConstraints);

		gridBagConstraints.gridy = 10;
		leftPanel.add(salesPanel, gridBagConstraints);

		gridBagConstraints.gridy = 11;
		leftPanel.add(accountReceivablePanel, gridBagConstraints);

		gridBagConstraints.gridy = 12;
		leftPanel.add(accumalateReceivePanel, gridBagConstraints);

		gridBagConstraints.gridy = 13;
		leftPanel.add(begBalPanel, gridBagConstraints);

		gridBagConstraints.gridy = 14;
		leftPanel.add(begBalBaseCurrPanel, gridBagConstraints);

		extLeftRightPanel.add(leftPanel, java.awt.BorderLayout.WEST);

		extRightPanel.setLayout(new java.awt.BorderLayout());
		extRightPanel.setPreferredSize(new java.awt.Dimension(325, 305));
		rightPanel.setLayout(new java.awt.GridBagLayout());

		rightPanel.setPreferredSize(new java.awt.Dimension(430, 200));
		ReceiptNoLabel.setText("Receipt No");
		ReceiptNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(ReceiptNoLabel, gridBagConstraints);

		RefNoText.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(RefNoText, gridBagConstraints);

		ReceiptDateLabel.setText("Receipt Date*");
		ReceiptDateLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(ReceiptDateLabel, gridBagConstraints);

		ProjectCodeLabel.setText("Project Code*");
		ProjectCodeLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(ProjectCodeLabel, gridBagConstraints);

		CustomerLabel.setText("Customer");
		CustomerLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(CustomerLabel, gridBagConstraints);

		WorkDescLabel.setText("Work Desc.");
		WorkDescLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(WorkDescLabel, gridBagConstraints);

		ProjectCodeText.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(ProjectCodeText, gridBagConstraints);

		CustomerText.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(CustomerText, gridBagConstraints);

		WorkDescScroll.setPreferredSize(new java.awt.Dimension(310, 64));
		WorkDescTextArea.setColumns(20);
		WorkDescTextArea.setLineWrap(true);
		WorkDescTextArea.setRows(5);
		WorkDescScroll.setViewportView(WorkDescTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(WorkDescScroll, gridBagConstraints);

		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(UnitCodeLabel, gridBagConstraints);

		UnitCodeText.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(UnitCodeText, gridBagConstraints);

		ActivityCodeLabel.setText("Activity Code");
		ActivityCodeLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(ActivityCodeLabel, gridBagConstraints);

		ActivityCodeText.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(ActivityCodeText, gridBagConstraints);

		DepartmentLabel.setText("Department");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(DepartmentLabel, gridBagConstraints);

		DepartmentText.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(DepartmentText, gridBagConstraints);

		ORNoLabel.setText("O.R No");
		ORNoLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(ORNoLabel, gridBagConstraints);

		ORNoText.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(ORNoText, gridBagConstraints);

		SOPOContractNo.setText("SO/PO Contract No");
		SOPOContractNo.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(SOPOContractNo, gridBagConstraints);

		ContractNoText.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(ContractNoText, gridBagConstraints);

		IPCNoLabel.setText("IPC No");
		IPCNoLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(IPCNoLabel, gridBagConstraints);

		IPCNoText.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(IPCNoText, gridBagConstraints);

		RemarkLabel.setText("Remarks");
		RemarkLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(RemarkLabel, gridBagConstraints);

		RemarkScroll.setPreferredSize(new java.awt.Dimension(310, 69));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(7);
		RemarkScroll.setViewportView(RemarksTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(RemarkScroll, gridBagConstraints);

		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(DescriptionLabel, gridBagConstraints);

		DescriptionScroll.setPreferredSize(new java.awt.Dimension(310, 80));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(7);
		DescriptionScroll.setViewportView(DescriptionTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(DescriptionScroll, gridBagConstraints);

		TransactionDateDate.setPreferredSize(new java.awt.Dimension(310, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		rightPanel.add(TransactionDateDate, gridBagConstraints);

		JPanel panelKosong = new JPanel();
		panelKosong.setPreferredSize(new java.awt.Dimension(310, 100));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(panelKosong, gridBagConstraints);

		extRightPanel.add(rightPanel, java.awt.BorderLayout.WEST);

		extLeftRightPanel.add(extRightPanel, java.awt.BorderLayout.CENTER);

		mainPanel.add(extLeftRightPanel, java.awt.BorderLayout.CENTER);

		bottomPanel.setLayout(new java.awt.BorderLayout());
		bottomPanel.setPreferredSize(new java.awt.Dimension(700, 90));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());
		OriginatorComp.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(305, 110));
		bottomPanel.add(OriginatorComp, java.awt.BorderLayout.WEST);

		ApprovedPanel.setLayout(new java.awt.BorderLayout());
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		ApprovedComp.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(305, 110));
		ApprovedPanel.add(ApprovedComp, java.awt.BorderLayout.WEST);

		receivedPanel.setLayout(new java.awt.BorderLayout());
		receivedPanel.setPreferredSize(new java.awt.Dimension(275, 110));
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		ReceivedComp.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(305, 110));
		receivedPanel.add(ReceivedComp, java.awt.BorderLayout.WEST);

		ApprovedPanel.add(receivedPanel, java.awt.BorderLayout.CENTER);
		bottomPanel.add(ApprovedPanel, java.awt.BorderLayout.CENTER);
		mainPanel.add(bottomPanel, java.awt.BorderLayout.SOUTH);

		add(mainPanel, java.awt.BorderLayout.NORTH);
	}

	private void addingListener() {
		m_searchRefNoBtn.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		AccountComp.addPropertyChangeListener("object", this);
		InvoiceNoComp.addPropertyChangeListener("object", this);

		ARSalesAmountText.addPropertyChangeListener("value", this);
		Tax23AmountText.addPropertyChangeListener("value", this);
		Tax23PercentText.addPropertyChangeListener("value", this);
		ARRcvExchRateText.addPropertyChangeListener("value", this);
		VATAmountText.addPropertyChangeListener("value", this);

		retentionText.addPropertyChangeListener("value", this);
		bankchargesAmountText.addPropertyChangeListener("value", this);
		transGainLossAmountText.addPropertyChangeListener("value", this);

		TotalRcvAmountText.addPropertyChangeListener("value", this);

		DetailBtn.addActionListener(this);

		CustomerStatusCombo.addActionListener(this);

		TransactionDateDate.addPropertyChangeListener("date", this);

		CashBankCombo.addActionListener(this);

		// CustomerStatusCombo.addPropertyChangeListener("object",this);

		/*
		 * ARRcvAmountDoc = ARRcvAmountText.getDocument();
		 * ARRcvAmountDoc.addDocumentListener(this); Tax23AmountDoc =
		 * Tax23AmountText.getDocument();
		 * Tax23AmountDoc.addDocumentListener(this); Tax23PercentDoc =
		 * Tax23PercentText.getDocument();
		 * Tax23PercentDoc.addDocumentListener(this); ARRcvExchRateDoc =
		 * ARRcvExchRateText.getDocument();
		 * ARRcvExchRateDoc.addDocumentListener(this); Tax23ExchRateDoc =
		 * ARRcvExchRateText.getDocument();
		 * Tax23ExchRateDoc.addDocumentListener(this);
		 */
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex() > 0) {
				// Object obj = m_entity;
				new Rcpt_AR(m_entity);
			} else {
				JOptionPane.showMessageDialog(this, "Data is empty", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		} else if (e.getSource() == m_searchRefNoBtn) {
			String[] kolom = { "", "UPPER(REFERENCENO)", "TRANSACTIONDATE",
					"EMPRECEIVED", "UPPER(RECEIVETO)", "UNIT", "STATUS",
					"SUBMITDATE" };
			String orderby = " ORDER BY " + kolom[2] + " ASC," + kolom[1]
					+ " ASC";
			SearchSalesArReceiptDialog dlg = new SearchSalesArReceiptDialog(GumundaMainFrame
					.getMainFrame(), "Search Receipt", m_conn, m_sessionid,
					kolom, orderby, new SalesReceivedLoader(m_conn,
							SalesReceived.class), "Sales Account Receivable");
			dlg.setVisible(true);
			if (dlg.selectedObj != null) {
				doLoad(dlg.selectedObj);
				m_ceksave = true;
			}
		} else if (e.getSource() == DetailBtn) {
			if (InvoiceNoComp.getObject() != null) {
				LookupPicker details = new LookupSalesARReceivedPicker(m_conn,
						m_sessionid, InvoiceNoComp.getObject(), entity());
				details.done1();
			}
		}
		if (e.getSource() == CustomerStatusCombo) {
			VATAmountText.setEditable(true);
			/*
			 * if(CustomerStatusCombo.getSelectedItem().equals("NONWAPU")){
			 * VATAmountText.setValue(null); VATAmountText.setEditable(false); }
			 */
			updateTax();
			updateTotalReceived();
		}
		/*
		 * if(e.getSource() == AccountComp){ updateTotalReceived();
		 * updateAmountInBaseCurrency(); updateEndingBalance(); }
		 */
		if (e.getSource() == CashBankCombo) {
			setAccount();
		}
	}

	/*private LookupPicker accountComp() {
		return AccountComp;
	}*/

	private void isiDefaultAssignPanel() {
		OriginatorComp.m_jobTextField
				.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField
				.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField
				.setText(getEmployeeJobTitle(defaultReceived));
	}

	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn,
				IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
				Signature.SIGN_ORIGINATOR);
		if (sign != null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn,
				IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
				Signature.SIGN_APPROVED);
		if (sign != null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn,
				IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
				Signature.SIGN_RECEIVED);
		if (sign != null)
			defaultReceived = sign.getFullEmployee();
	}

	SalesReceived entity() {
		return m_entity;
	}

	protected StateTemplateEntity currentEntity() {
		return entity();
	}

	ArrayList validityMsgs = new ArrayList();

	protected boolean cekValidity() {
		validityMsgs.clear();
		if (TransactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (InvoiceNoComp.getObject() == null)
			addInvalid("Invoice must be selected");
		if (AccountComp.getObject() == null)
			addInvalid("Account must be selected");
		if (ExchRateText.getValue() == null)
			addInvalid("Exchange rate must be inserted");
		if (DescriptionTextArea.getText().equals(""))
			addInvalid("Description must be inserted");
		/*if (ARSalesAmountText.getValue() == null)
			addInvalid("Account Receivable Amount must be inserted");
		else {
			double amt = ((Number) ARSalesAmountText.getValue()).doubleValue();
			if (amt == 0)
				addInvalid("Account Receivable Amount must be inserted");
		}*/
		if (ARRcvExchRateText.getValue() == null)
			addInvalid("Account Receivable Exchange Rate must be inserted");
		else {
			double amt = ((Number) ARRcvExchRateText.getValue()).doubleValue();
			if (amt == 0)
				addInvalid("Account Receivable Exchange Rate must be inserted");
		}
		/*
		 * if(Tax23AmountText.getValue()==null) addInvalid("Tax 23 Amount must
		 * be inserted"); else{ double amt =
		 * ((Number)Tax23AmountText.getValue()).doubleValue(); if(amt==0)
		 * addInvalid("Tax 23 Amount must be inserted"); }
		 * if(Tax23PercentText.getValue()==null) addInvalid("Tax 23 Percent must
		 * be inserted"); else{ double amt =
		 * ((Number)Tax23PercentText.getValue()).doubleValue(); if(amt==0)
		 * addInvalid("Tax 23 Percent must be inserted"); }
		 */
		if (Tax23ExchRateText.getValue() == null)
			addInvalid("Tax 23 ExchRate must be inserted");

		if (validityMsgs.size() > 0) {
			StringBuffer result = new StringBuffer();
			Iterator iter = validityMsgs.iterator();
			while (iter.hasNext()) {
				String o = iter.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this, result);
			return false;
		}
		return true;
	}

	private void addInvalid(String string) {
		validityMsgs.add(string);
	}

	protected void gui2entity() {
		if (InvoiceNoComp.getObject() != null) {
			CashBankAccount cb = (CashBankAccount) AccountComp.getObject();
			Object obj = InvoiceNoComp.getObject();
			if (obj instanceof SalesInvoice) {
				SalesInvoice invoice = (SalesInvoice) obj;
				entity().setInvoice(invoice);
				entity().setBeginningBalance(null);
				entity().setCurrency(invoice.getSalesCurr());
				// entity().setSalesARCurr(invoice.getSalesCurr());
				// if(!invoice.getSalesCurr().getSymbol().equals(cb.getCurrency().getSymbol()))
				entity().setSalesARCurr(cb.getCurrency());
				entity().setVatCurr(invoice.getVatCurr());
				entity().setTax23Curr(invoice.getSalesCurr());
				entity().setBankChargesCurr(cb.getCurrency());
				entity().setRetentionCurr(cb.getCurrency());
			} else if (obj instanceof BeginningAccountReceivable) {
				BeginningAccountReceivable bar = (BeginningAccountReceivable) obj;
				entity().setBeginningBalance(bar);
				entity().setInvoice(null);
				entity().setCurrency(bar.getCurrency());
				// entity().setSalesARCurr(bar.getCurrency());
				entity().setSalesARCurr(cb.getCurrency());
				entity().setVatCurr(bar.getCurrency());
				entity().setTax23Curr(bar.getCurrency());
				entity().setBankChargesCurr(cb.getCurrency());
				entity().setRetentionCurr(cb.getCurrency());
			}
		}
		entity().setTranslationCurr(baseCurrency);

		entity().setReceiveTo((String) CashBankCombo.getSelectedItem());
		if (CashBankCombo.getSelectedItem().equals("Cash")) {
			entity().setCashAccount((CashAccount) AccountComp.getObject());
			entity().setBankAccount(null);
			entity().setUnit(((CashAccount) AccountComp.getObject()).getUnit());
		} else {
			entity().setCashAccount(null);
			entity().setBankAccount((BankAccount) AccountComp.getObject());
			entity().setUnit(((BankAccount) AccountComp.getObject()).getUnit());
		}

		entity().setCustomerStatus(
				(String) CustomerStatusCombo.getSelectedItem());

		Number amt = (Number) ARRcvExchRateText.getValue();
		if (amt != null) {
			entity().setExchangeRate(amt.doubleValue());
			entity().setSalesARExchRate(amt.doubleValue());
		}

		amt = (Number) ARSalesAmountText.getValue();
		if (amt != null)
			entity().setSalesARAmount(amt.doubleValue());

		// if(CustomerStatusCombo.getSelectedItem().equals("NONWAPU")){
		// VATAmountText.setValue(new Double(0));
		// }
		amt = (Number) VATAmountText.getValue();
		if (amt != null)
			entity().setVatAmount(amt.doubleValue());

		amt = (Number) VATExchRateText.getValue();
		if (amt != null)
			entity().setVatExchRate(amt.doubleValue());

		amt = (Number) Tax23PercentText.getValue();
		if (amt != null)
			entity().setTax23Percent(amt.doubleValue());

		amt = (Number) Tax23AmountText.getValue();
		if (amt != null)
			entity().setTax23Amount(amt.doubleValue());
		/*
		 * double tax23Amount =
		 * (entity().getTax23Percent()*(entity().getSalesARAmount()-entity().getVATAmount()));
		 * if(tax23Amount!=0) entity().setTax23Amount(tax23Amount);
		 */

		amt = (Number) Tax23ExchRateText.getValue();
		if (amt != null)
			entity().setTax23ExchRate(amt.doubleValue());

		amt = (Number) bankchargesAmountText.getValue();
		if (amt != null)
			entity().setBankChargesAmount(amt.doubleValue());

		amt = (Number) retentionText.getValue();
		if (amt != null)
			entity().setRetentionAmount(amt.doubleValue());

		amt = (Number) transGainLossAmountText.getValue();
		if (amt != null)
			entity().setTranslationAmount(amt.doubleValue());

		amt = (Number) bankChargeExchRateText.getValue();
		if (amt != null)
			entity().setBankChargesExchRate(amt.doubleValue());

		entity().setTransactionDate(TransactionDateDate.getDate());
		entity().setCustomerStatus(
				CustomerStatusCombo.getSelectedItem().toString());
		entity().setRemarks(RemarksTextArea.getText());
		entity().transTemplateRead(this.OriginatorComp, this.ApprovedComp,
				this.ReceivedComp, this.RefNoText, this.DescriptionTextArea);
	}

	protected void entity2gui() {
		StatusText.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");

		if (entity().getSubmitDate() != null)
			SubmittedDateText.setText(dateformat.format(entity()
					.getSubmitDate()));
		else
			SubmittedDateText.setText("");

		if (entity().getInvoice() != null)
			InvoiceNoComp.setObject(entity().getInvoice());
		else if (entity().getBeginningBalance() != null)
			InvoiceNoComp.setObject(entity().getBeginningBalance());
		else
			InvoiceNoComp.setObject(null);

		String custStatus = entity().getCustomerStatus();
		if (custStatus == null)
			custStatus = "WAPU";
		CustomerStatusCombo.setSelectedItem(custStatus);

		if (entity().getReceiveTo() != null)
			CashBankCombo.setSelectedItem(entity().getReceiveTo());
		else
			CashBankCombo.setSelectedItem("Cash");

		if (entity().getBankAccount() != null)
			AccountComp.setObject(entity().getBankAccount());
		else
			AccountComp.setObject(entity().getCashAccount());

		VATAmountText.setValue(new Double(entity().getVatAmount()));

		ARSalesAmountText.setValue(new Double(entity().getSalesARAmount()));

		ARRcvExchRateText.setValue(new Double(entity().getSalesARExchRate()));
		Tax23ExchRateText.setValue(new Double(entity().getTax23ExchRate()));

		VATExchRateText.setValue(new Double(entity().getTax23ExchRate()));
		Tax23PercentText.setValue(new Double(entity().getTax23Percent()));

		Tax23AmountText.setValue(new Double(entity().getTax23Amount()));

		bankchargesAmountText.setValue(new Double(entity()
				.getBankChargesAmount()));
		bankChargeExchRateText.setValue(new Double(entity()
				.getBankChargesExchRate()));

		retentionText.setValue(new Double(entity().getRetentionAmount()));

		transGainLossAmountText.setValue(new Double(entity()
				.getTranslationAmount()));

		RefNoText.setText(entity().getReferenceNo());
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

		Date date = new Date();
		if (entity().getTransactionDate() != null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else
			TransactionDateDate.setDate(date);
	}

	protected void disableEditMode() {
		inEditMode(false);
	}

	private void inEditMode(boolean edit) {
		InvoiceNoComp.setEnabled(edit);
		CustomerStatusCombo.setEnabled(edit);
		CashBankCombo.setEnabled(edit);
		AccountComp.setEnabled(edit);
		TransactionDateDate.setEditable(edit);
		DescriptionTextArea.setEditable(edit);
		RemarksTextArea.setEditable(edit);
		OriginatorComp.setEnabled(edit);
		ApprovedComp.setEnabled(edit);
		ReceivedComp.setEnabled(edit);
		ARSalesAmountText.setEditable(edit);
		ARRcvExchRateText.setEditable(edit);
		Tax23PercentText.setEditable(edit);
		Tax23AmountText.setEditable(edit);
		VATAmountText.setEditable(edit);
		// Tax23ExchRateText.setEditable(edit);
		DetailBtn.setEnabled(edit);

		bankchargesAmountText.setEditable(edit);
		retentionText.setEditable(edit);
		transGainLossAmountText.setEditable(edit);

	}

	protected void enableEditMode() {
		inEditMode(true);

		/*CashBankAccount cb = null;
		Object obj = AccountComp.getObject();
		if (obj instanceof CashBankAccount)
			cb = (CashBankAccount) obj;*/
		/*if (cb != null)
			if (cb.getCurrency().getSymbol().equals(
					cb.getCurrency().getSymbol()))
				ARRcvExchRateText.setEditable(false);
			else
				ARRcvExchRateText.setEditable(true);*/
	}

	private void disableSomeComponents() {
		StatusText.setEditable(false);
		SubmittedDateText.setEditable(false);
		RcvToText.setEditable(false);
		InvoiceDateText.setEditable(false);
		CurrComp.setEditable(false);
		ExchRateText.setEditable(false);
		SalesCurrText.setEditable(false);
		SalesAmountText.setEditable(false);
		ARCurrText.setEditable(false);
		ARAmountText.setEditable(false);
		AccReceiveCurrText.setEditable(false);
		AccReceiveAmountText.setEditable(false);
		BeginBalanceCurrText.setEditable(false);
		BeginBalanceAmountText.setEditable(false);
		ARRcvCurrText.setEditable(false);
		Tax23CurrText.setEditable(false);
		TotalRcvCurrText.setEditable(false);
		TotalRcvAmountText.setEditable(false);
		AmountBaseText.setEditable(false);
		AmountBaseCurrText.setEditable(false);

		BankchargesCurrText.setEditable(false);
		bankchargesAmountText.setEditable(false);
		bankChargeExchRateText.setEditable(false);

		retentionCurrText.setEditable(false);
		retentionText.setEditable(false);

		transGainLossCurrText.setEditable(false);
		transGainLossAmountText.setEditable(false);

		EndBalanceCurrText.setEditable(false);
		EndBalanceAmountText.setEditable(false);
		RefNoText.setEditable(false);
		ProjectCodeText.setEditable(false);
		CustomerText.setEditable(false);
		WorkDescTextArea.setEditable(false);
		UnitCodeText.setEditable(false);
		ActivityCodeText.setEditable(false);
		DepartmentText.setEditable(false);
		ORNoText.setEditable(false);
		ContractNoText.setEditable(false);
		IPCNoText.setEditable(false);

		Tax23ExchRateText.setEditable(false);
		Tax23AmountText.setEditable(false);
		VATCurrText.setEditable(false);
		VATExchRateText.setEditable(false);

		BegBalBaseCurrAmountText.setEditable(false);
		EndBalBaseCurrAmountText.setEditable(false);
		EndBalBaseCurrText.setEditable(false);
		BegBalBaseCurrText.setEditable(false);

	}

	protected Object createNew() {
		SalesReceived a = new SalesReceived();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a;
	}

	void setEntity(Object m_entity) {
		SalesReceived oldEntity = this.m_entity;
		if (oldEntity != null) {
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (SalesReceived) m_entity;
		this.m_entity.addPropertyChangeListener(this);

		// set beginning balance
		if (this.m_entity.getBeginningBalance() != null) {
			setBeginningBalance();
		}
	}

	private void setBeginningBalance() {
		BeginningAccountReceivable bar = this.m_entity.getBeginningBalance();

		BeginningBalanceBusinessLogic logic = new BeginningBalanceBusinessLogic(
				m_conn, m_sessionid);

		bar.setTrans(logic.findTransaction(bar.getProject().getUnit()));
		bar.showReferenceNo(true);

		this.m_entity.setBeginningBalance(bar);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())) {
			readEntityState();
		}
		if ("object".equals(evt.getPropertyName())) {
			if (evt.getSource() == AccountComp) {
				if (AccountComp.getObject() == null) {
				} else {
					CashBankAccount cb = (CashBankAccount) AccountComp
							.getObject();
					Currency currency = cb.getCurrency();

					ARRcvCurrText.setText(currency.getSymbol());
					Tax23CurrText.setText(currency.getSymbol());
					VATCurrText.setText(currency.getSymbol());
					TotalRcvCurrText.setText(currency.getSymbol());
					BankchargesCurrText.setText(currency.getSymbol());

					retentionCurrText.setText(currency.getSymbol());

					transGainLossCurrText.setText(baseCurrency.getSymbol());

					ARRcvExchRateText.setValue(new Double(1D));

					if (currency.getSymbol().equals(baseCurrency.getSymbol())) { // sama
						ARRcvExchRateText.setEditable(false);
					} else {
						ARRcvExchRateText.setEditable(true);
						setDefaultExchangeRate(currency);
					}

					updateTotalReceived();
					updateAmountInBaseCurrency();
					updateEndingBalance();
				}
			}
			if (evt.getSource() == InvoiceNoComp) {

				if (InvoiceNoComp.getObject() != null) {
					Object obj = InvoiceNoComp.getObject();
					if (obj instanceof SalesInvoice) {
						SalesInvoice si = (SalesInvoice) obj;
						setSalesInvoice(si);
						updateProject(si);

						// TODO: ini untuk sementara, harusnya ngubah juga di
						// sales invoice

						CashBankCombo.setSelectedItem(si.getReceiveFrom());
						setAccount();
						setDefaultAccount(si.igetCashBankAccount());
					} else if (obj instanceof BeginningAccountReceivable) {
						BeginningAccountReceivable bar = (BeginningAccountReceivable) obj;
						setBeginningBalance(bar);
						updateProject(bar);

						setAccount();
						setDefaultAccount(null);
					}
					updateAmountInBaseCurrency();
					enableExchangeRate();
					updateEndingBalance();
				} else {
					setNullSalesInvoice();
					setNullProject();
					setNullAccount();
					enableExchangeRate();
				}
			}
		}
		if ("value".equals(evt.getPropertyName())) {
			if (evt.getSource() == ARSalesAmountText) {
				updateTax();
				updateTotalReceived();
				updateAmountInBaseCurrency();
				updateEndingBalance();
			}
			if (evt.getSource() == Tax23AmountText) {
				updateTotalReceived();
			}
			if (evt.getSource() == Tax23PercentText) {
				updateTax();
			}
			if (evt.getSource() == ARRcvExchRateText) {
				updateExchangeRate();
				updateAmountInBaseCurrency();
				updateEndingBalance();
			}
			if (evt.getSource() == TotalRcvAmountText) {
				updateAmountInBaseCurrency();
				updateEndingBalance();
			}
			if (evt.getSource() == bankchargesAmountText) {
				updateTotalReceived();
			}
			if (evt.getSource() == transGainLossAmountText) {
				//updateTotalReceived();
				updateEndingBalance();
			}
			if (evt.getSource() == VATAmountText) {
				updateTax();
				updateTotalReceived();
				updateAmountInBaseCurrency();
				updateEndingBalance();
			}
			if (evt.getSource() == retentionText) {
				updateTotalReceived();
				updateEndingBalance();
			}
			/*
			 * if (evt.getSource() == CustomerStatusCombo) {
			 * VATAmountText.setEditable(true); if
			 * (CustomerStatusCombo.getSelectedItem().equals("NONWAPU")) {
			 * VATAmountText.setValue(null); VATAmountText.setEditable(false); }
			 * updateTax(); updateTotalReceived(); }
			 */
		}
		if ("date".equals(evt.getPropertyName())) {
			if (evt.getSource() == TransactionDateDate) {
				CashBankAccount cba = (CashBankAccount) AccountComp.getObject();
				if (cba != null) {
					Currency curr = cba.getCurrency();
					setDefaultExchangeRate(curr);
				}
			}
		}
	}

	private void updateProject(Object obj) {
		ProjectData project = null;
		if (obj instanceof SalesInvoice)
			project = ((SalesInvoice) obj).getProject();
		else if (obj instanceof BeginningAccountReceivable)
			project = ((BeginningAccountReceivable) obj).getProject();
		if (project != null) {
			ProjectCodeText.setText(project.getCode());
			CustomerText.setText(project.getCustname());
			WorkDescTextArea.setText(project.getWorkDescription());
			UnitCodeText.setText(project.getUnit().toString());
			ActivityCodeText.setText(project.getActivity().toString());
			DepartmentText.setText(project.getDepartment().toString());
			ORNoText.setText(project.getORNo());
			ContractNoText.setText(project.getPONo());
			IPCNoText.setText(project.getIPCNo());
		}
	}

	private void setDefaultAccount(CashBankAccount bankAccount) {
		AccountComp.setObject(bankAccount);
	}

	private void enableExchangeRate() {
		Object obj = InvoiceNoComp.getObject();
		if (obj == null)
			return;

		Currency curr = null;
		if (obj instanceof SalesInvoice) {
			SalesInvoice si = (SalesInvoice) obj;
			curr = si.getSalesCurr();
		} else if (obj instanceof BeginningAccountReceivable) {
			BeginningAccountReceivable bar = (BeginningAccountReceivable) obj;
			curr = bar.getCurrency();
		}

		if (curr == null)
			return;

		if (curr.getIndex() == baseCurrency.getIndex()) {
			ARRcvExchRateText.setEditable(false);
			ARRcvExchRateText.setValue(new Double(1.0));
		} else {
			ARRcvExchRateText.setEditable(true);
			setDefaultExchangeRate(curr);
		}
	}

	protected void clearAll() {
		super.clearAll();
		clearComponent();
		disableEditMode();
	}

	private void clearComponent() {
		StatusText.setText("");
		SubmittedDateText.setText("");
		RcvToText.setText("");

		AmountBaseCurrText.setText("");
		AmountBaseText.setValue(new Double(0.0));

		RefNoText.setText("");
		TransactionDateDate.setDate(null);

		EndBalanceCurrText.setText("");
		EndBalanceAmountText.setValue(new Double(0.0));

		BegBalBaseCurrText.setText("");
		BegBalBaseCurrAmountText.setValue(new Double(0.0));

		EndBalBaseCurrText.setText("");
		EndBalBaseCurrAmountText.setValue(new Double(0.0));

		TotalRcvAmountText.setValue(new Double(0.0));

		retentionCurrText.setText("");
		transGainLossCurrText.setText("");
	}

	private void updateExchangeRate() {
		Tax23ExchRateText.setValue(ARRcvExchRateText.getValue());
		VATExchRateText.setValue(ARRcvExchRateText.getValue());
		bankChargeExchRateText.setValue(ARRcvExchRateText.getValue());
	}

	private void setNullAccount() {
		setAccount();
	}

	private void doUpdate(DocumentEvent evt) {
		if (evt.getDocument() == ARRcvAmountDoc) {
			if (ARSalesAmountText.getValue() != null) {
				// updateTotalReceived();
				// updateEndingBalance();
				// updateAmountInBaseCurrency();
			}
		}
		if (evt.getDocument() == Tax23AmountDoc) {
			if (Tax23AmountText.getValue() != null) {
				// updateTotalReceived();
				// updateEndingBalance();
				// updateAmountInBaseCurrency();
			}
		}
		if (evt.getDocument() == Tax23PercentDoc) {
			if (!Tax23PercentText.getText().equals("")) {
				// updateTax();
			}
		}
		if ((evt.getDocument() == ARRcvExchRateDoc)
				|| (evt.getDocument() == Tax23ExchRateDoc)) {
			// updateAmountInBaseCurrency();
		}
	}

	private void setNullProject() {
		ProjectCodeText.setText("");
		CustomerText.setText("");
		WorkDescTextArea.setText("");
		UnitCodeText.setText("");
		ActivityCodeText.setText("");
		DepartmentText.setText("");
		ORNoText.setText("");
		ContractNoText.setText("");
		IPCNoText.setText("");
	}

	private void setNullSalesInvoice() {
		InvoiceDateText.setText("");
		CurrComp.setText("");
		ExchRateText.setText("");
		SalesCurrText.setText("");
		ARCurrText.setText("");
		AccReceiveCurrText.setText("");
		BeginBalanceCurrText.setText("");
		ARRcvCurrText.setText("");
		Tax23CurrText.setText("");
		BankchargesCurrText.setText("");
		VATCurrText.setText("");
		TotalRcvCurrText.setText("");
		EndBalanceCurrText.setText("");
		SalesAmountText.setText("");
		ARAmountText.setText("");
		AccReceiveAmountText.setText("");
		BeginBalanceAmountText.setValue(new Double(0.0));
	}

	private void setBeginningBalance(BeginningAccountReceivable bar) {
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		InvoiceDateText.setText(dateformat
				.format(bar.getTrans().getTransDate()));
		CurrComp.setText(bar.getCurrency().getSymbol());
		ExchRateText.setValue(new Double(bar.getExchangeRate()));
		setAccumulatedReceived(bar);

		double ar = bar.getAccValue();
		double accReceived = 0;
		if (AccReceiveAmountText.getValue() != null)
			accReceived = ((Number) AccReceiveAmountText.getValue())
					.doubleValue();
		double begBalance = ar - accReceived;
		String symbol = bar.getCurrency().getSymbol();
		SalesCurrText.setText(symbol);
		ARCurrText.setText(symbol);
		AccReceiveCurrText.setText(symbol);
		BeginBalanceCurrText.setText(symbol);
		ARRcvCurrText.setText(symbol);
		VATCurrText.setText(symbol);
		Tax23CurrText.setText(symbol);
		BankchargesCurrText.setText(symbol);

		TotalRcvCurrText.setText(symbol);
		EndBalanceCurrText.setText(symbol);
		SalesAmountText.setValue(null);
		ARAmountText.setValue(new Double(ar));
		AccReceiveAmountText.setValue(new Double(accReceived));
		BeginBalanceAmountText.setValue(new Double(begBalance));

		BegBalBaseCurrText.setText(baseCurrency.getSymbol());
		AmountBaseCurrText.setText(baseCurrency.getSymbol());
		EndBalBaseCurrText.setText(baseCurrency.getSymbol());

		double exchRate = ((Number) ExchRateText.getValue()).doubleValue();
		BegBalBaseCurrAmountText.setValue(new Double(begBalance * exchRate));
	}

	private void setAccumulatedReceived(BeginningAccountReceivable bar) {
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesReceived.class);
		mapper.setActiveConn(m_conn);

		List list = mapper.doSelectWhere("BEGINNINGBALANCE=" + bar.getIndex()
				+ " AND STATUS=3 AND AUTOINDEX != " + entity().getIndex());

		Iterator iter = list.iterator();

		double amt = 0;
		while (iter.hasNext()) {
			SalesReceived rcv = (SalesReceived) iter.next();

			/*if (rcv.getIndex() != entity().getIndex())
				amt += rcv.getSalesARAmount();

			if (!rcv.getBeginningBalance().getCurrency().getSymbol().equals(
					rcv.getSalesARCurr().getSymbol())) {
				if (rcv.getSalesARCurr().getSymbol().equals(
						baseCurrency.getSymbol()))
					amt += ((rcv.getSalesARAmount() + rcv.getVatAmount() - rcv.getRetentionAmount()) / rcv.getBeginningBalance()
							.getExchangeRate());
				else
					amt += ((rcv.getSalesARAmount() + rcv.getVatAmount() - rcv.getRetentionAmount()) * rcv.getSalesARExchRate());
			}*/

			if (!rcv.getBeginningBalance().getCurrency().getSymbol().equals(
					rcv.getSalesARCurr().getSymbol())) {
				if (rcv.getSalesARCurr().getSymbol().equals(
						baseCurrency.getSymbol()))
					amt += ((rcv.getSalesARAmount() + rcv.getVatAmount() - rcv
							.getRetentionAmount()) / rcv.getBeginningBalance()
							.getExchangeRate());
				else
					amt += ((rcv.getSalesARAmount() + rcv.getVatAmount() - rcv
							.getRetentionAmount()) * rcv.getSalesARExchRate());
			} else
				amt += (rcv.getSalesARAmount() + rcv.getVatAmount() - rcv
						.getRetentionAmount());
		}

		NumberRounding nr = new NumberRounding(
				NumberRounding.NUMBERROUNDING_ROUND, 2);
		amt = nr.round(amt);

		/*
		 * BeginningAccountReceivableBusinessLogic logic = new
		 * BeginningAccountReceivableBusinessLogic(m_conn, m_sessionid); double
		 * amt = logic.getAccumulatedReceived(bar);
		 */
		AccReceiveAmountText.setValue(new Double(amt));
	}

	private void setSalesInvoice(SalesInvoice si) {
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		InvoiceDateText.setText(dateformat.format(si.getTransactionDate()));
		CurrComp.setText(si.getSalesCurr().getSymbol());
		ExchRateText.setValue(new Double(si.getSalesExchRate()));
		setAccumulatedReceived(si);
		double sales = si.getSalesAmount();
		double dp = si.getDownPaymentAmount();
		sales = sales - dp;
		double vat = si.getVatAmount();
		double ar = sales + vat;

		double accReceived = 0;
		if (AccReceiveAmountText.getValue() != null)
			accReceived = ((Number) AccReceiveAmountText.getValue())
					.doubleValue();
		double begBalance = ar - accReceived;

		NumberRounding nr = new NumberRounding(
				NumberRounding.NUMBERROUNDING_ROUND, 2);
		begBalance = nr.round(begBalance);

		String symbol = si.getSalesCurr().getSymbol();
		SalesCurrText.setText(symbol);
		ARCurrText.setText(symbol);
		AccReceiveCurrText.setText(symbol);
		BeginBalanceCurrText.setText(symbol);
		ARRcvCurrText.setText(symbol);
		VATCurrText.setText(symbol);
		Tax23CurrText.setText(symbol);
		BankchargesCurrText.setText(symbol);

		TotalRcvCurrText.setText(symbol);
		EndBalanceCurrText.setText(symbol);
		SalesAmountText.setValue(new Double(sales));
		ARAmountText.setValue(new Double(ar));
		AccReceiveAmountText.setValue(new Double(accReceived));
		BeginBalanceAmountText.setValue(new Double(begBalance));

		BegBalBaseCurrText.setText(baseCurrency.getSymbol());
		AmountBaseCurrText.setText(baseCurrency.getSymbol());
		EndBalBaseCurrText.setText(baseCurrency.getSymbol());

		double exchRate = ((Number) ExchRateText.getValue()).doubleValue();
		double begBalRp = begBalance * exchRate;
		BegBalBaseCurrAmountText.setValue(new Double(begBalRp));
		// updateTotalReceived();
		// updateAmountInBaseCurrency();
		// updateEndingBalance();
	}

	private void setAccumulatedReceived(SalesInvoice si) {
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesReceived.class);
		mapper.setActiveConn(m_conn);

		List list = mapper.doSelectWhere("INVOICE=" + si.getIndex()
				+ " AND STATUS=3 AND AUTOINDEX != " + entity().getIndex());

		Iterator iter = list.iterator();

		double amt = 0;
		while (iter.hasNext()) {
			SalesReceived rcv = (SalesReceived) iter.next();

			if (!rcv.getInvoice().getSalesCurr().getSymbol().equals(
					rcv.getSalesARCurr().getSymbol())) {
				if (rcv.getSalesARCurr().getSymbol().equals(
						baseCurrency.getSymbol()))
					amt += ((rcv.getSalesARAmount() + rcv.getVatAmount() - rcv
							.getRetentionAmount()) / rcv.getInvoice()
							.getSalesExchRate());
				else
					amt += ((rcv.getSalesARAmount() + rcv.getVatAmount() - rcv
							.getRetentionAmount()) * rcv.getSalesARExchRate());
			} else
				amt += (rcv.getSalesARAmount() + rcv.getVatAmount() - rcv
						.getRetentionAmount());

			double translation = rcv.getTranslationAmount();
			if (rcv.getInvoice().getSalesCurr().getIndex()==baseCurrency.getIndex()) {

			} else {
				double invExchRate = rcv.getInvoice().getSalesExchRate();
				translation = (translation / invExchRate);
				NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
				translation = nr.round(translation);
			}

			amt -= translation;
		}

		NumberRounding nr = new NumberRounding(
				NumberRounding.NUMBERROUNDING_ROUND, 2);
		amt = nr.round(amt);



		/*
		 * SalesInvoiceBusinessLogic logic = new
		 * SalesInvoiceBusinessLogic(m_conn, m_sessionid); double amt =
		 * logic.getAccumulatedReceived(si);
		 */
		AccReceiveAmountText.setValue(new Double(amt));
	}

	private void updateEndingBalance() {
		/*
		 * Number arRcvNbr = (Number)ARSalesAmountText.getValue(); Number begNbr =
		 * (Number)BeginBalanceAmountText.getValue();
		 *
		 * if((arRcvNbr==null)||(begNbr==null)) return;
		 *
		 * double arRcv = arRcvNbr.doubleValue(); double beg =
		 * begNbr.doubleValue();
		 *
		 * double end = 0;
		 *
		 * Object obj = InvoiceNoComp.getObject(); double invExchRate = 1; if
		 * (obj != null) {
		 *
		 * invExchRate = ((Number) ExchRateText.getValue()) .doubleValue();
		 * double rcvExchRate = ((Number) ARRcvExchRateText.getValue())
		 * .doubleValue();
		 *
		 * CashBankAccount cb = (CashBankAccount) AccountComp.getObject();
		 * Currency curr = null;
		 *
		 * if (obj instanceof SalesInvoice) { SalesInvoice si = (SalesInvoice)
		 * obj;
		 *
		 * curr = si.getSalesCurr(); } else if (obj instanceof
		 * BeginningAccountReceivable) { BeginningAccountReceivable bar =
		 * (BeginningAccountReceivable) obj;
		 *
		 * curr = bar.getCurrency(); }
		 *
		 * if (cb != null) { if (curr.getIndex() != cb.getCurrency().getIndex()) {
		 * if (cb.getCurrency().getIndex() == baseCurrency.getIndex()) { arRcv =
		 * arRcv / invExchRate; } else arRcv = arRcv * rcvExchRate; } } } //
		 * NumberRounding nr = new //
		 * NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2); //arRcv =
		 * nr.round(arRcv);
		 *
		 * end = beg - arRcv; EndBalanceAmountText.setValue(new Double(end));
		 * EndBalBaseCurrAmountText.setValue(new Double(end*invExchRate));
		 */

		Number begNbr = (Number) BeginBalanceAmountText.getValue();
		Number salesNbr = (Number) ARSalesAmountText.getValue();
		Number vatNbr = (Number) VATAmountText.getValue();
		Number retentionNbr = (Number) retentionText.getValue();
		//!NEW
		Number translationNbr = (Number)  transGainLossAmountText.getValue();

		if ((begNbr == null) || (salesNbr == null) || (vatNbr == null)
				|| (retentionNbr == null) || (translationNbr == null))
			return;

		double beg = begNbr.doubleValue();
		double sales = salesNbr.doubleValue();
		double vat = vatNbr.doubleValue();
		double retention = retentionNbr.doubleValue();
		//!NEW
		double translation = translationNbr.doubleValue();

		double arRcv = sales + vat - retention;

		Object obj = InvoiceNoComp.getObject();
		double invExchRate = 1;
		if (obj != null) {

			invExchRate = ((Number) ExchRateText.getValue()).doubleValue();
			double rcvExchRate = ((Number) ARRcvExchRateText.getValue())
					.doubleValue();

			CashBankAccount cb = (CashBankAccount) AccountComp.getObject();
			Currency curr = null;

			if (obj instanceof SalesInvoice) {
				SalesInvoice si = (SalesInvoice) obj;

				curr = si.getSalesCurr();
			} else if (obj instanceof BeginningAccountReceivable) {
				BeginningAccountReceivable bar = (BeginningAccountReceivable) obj;

				curr = bar.getCurrency();
			}

			if (cb != null) {
				if (curr.getIndex() != cb.getCurrency().getIndex()) {
					if (cb.getCurrency().getIndex() == baseCurrency.getIndex()) {
						// inv $, rcv Rp
						arRcv = arRcv / invExchRate;
					} else {
						// inv Rp, rcv $
						arRcv = arRcv * rcvExchRate;
					}
				}

				// TRANSLATION (+) = gain
				// TRANSLATION (-) = loss
				if (curr.getIndex() == baseCurrency.getIndex()) {
					// inv Rp
					arRcv = arRcv - translation;
				} else {
					// inv $
					arRcv = arRcv - (translation / invExchRate);
				}
			}
		}

		double end = beg - arRcv;

		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		end = nr.round(end);

		EndBalanceAmountText.setValue(new Double(end));
		EndBalBaseCurrAmountText.setValue(new Double(end * invExchRate));
	}

	private void updateAmountInBaseCurrency() {
		Number totRcv = (Number) TotalRcvAmountText.getValue();
		Number arExchRateNbr = (Number) ARRcvExchRateText.getValue();

		if ((totRcv == null) || (arExchRateNbr == null))
			return;

		Object obj = InvoiceNoComp.getObject();
		if (obj == null)
			return;

		CashBankAccount cb = (CashBankAccount) AccountComp.getObject();
		if (cb == null)
			return;

		double inBase = 0;

		if (cb.getCurrency().getSymbol() == baseCurrency.getSymbol())
			inBase = totRcv.doubleValue();
		else
			inBase = totRcv.doubleValue() * arExchRateNbr.doubleValue();

		AmountBaseText.setValue(new Double(inBase));

	}

	private void updateTotalReceived() {
		if ((ARSalesAmountText.getValue() != null)
				&& (Tax23AmountText.getValue() != null)
				&& (InvoiceNoComp.getObject() != null)) {

			// wapu kah?
			// Object obj = InvoiceNoComp.getObject();
			/*
			 * double ppnPercent = 0;
			 * if(CustomerStatusCombo.getSelectedItem().toString().equalsIgnoreCase("WAPU")){
			 * if(obj instanceof SalesInvoice){ SalesInvoice inv =
			 * (SalesInvoice) obj; ppnPercent = inv.getVatPercent(); // kalau
			 * wapu ada nilainya.... }else if(obj instanceof
			 * BeginningAccountReceivable){ ppnPercent = 10; } }
			 */

			double sales = ((Number) ARSalesAmountText.getValue())
					.doubleValue();

			Number vatNbr = (Number) VATAmountText.getValue();
			double vat = 0;
			if (vatNbr != null)
				vat = vatNbr.doubleValue();

			double tax = ((Number) Tax23AmountText.getValue()).doubleValue();

			double bankcharges = ((Number) bankchargesAmountText.getValue())
					.doubleValue();

			//double trans = ((Number) transGainLossAmountText.getValue())
			//		.doubleValue();

			double retention = ((Number) retentionText.getValue())
					.doubleValue();

			double rcv = 0;
			if (CustomerStatusCombo.getSelectedItem().toString()
					.equalsIgnoreCase("WAPU")) {
				rcv = sales - retention - tax - bankcharges;
			} else {
				rcv = sales + vat - retention - tax - bankcharges;
			}

			CashBankAccount cb = (CashBankAccount) AccountComp.getObject();
			if (cb != null) {
				if (cb.getCurrency().getIndex() != baseCurrency.getIndex()) {
					/*double er = 1;
					Number exchRate = (Number) ARRcvExchRateText.getValue();
					if (exchRate != null)
						er = exchRate.doubleValue();
					//trans /= er; // INI GA DIPAKE

					NumberRounding nr = new NumberRounding(
							NumberRounding.NUMBERROUNDING_ROUND, 2);
					//trans = nr.round(trans); // INI GA DIPAKE
*/				}
			}

			//rcv += trans;

			TotalRcvAmountText.setValue(new Double(rcv));
		}
	}

	private void updateTax() {
		Number arRcv = (Number) ARSalesAmountText.getValue();
		// String taxPercent = Tax23PercentText.getText();
		Number taxPercent = (Number) Tax23PercentText.getValue();

		Object obj = InvoiceNoComp.getObject();

		if ((arRcv != null) && (taxPercent != null) && (obj != null)) {

			// wapu kah?
			/*
			 * double ppnPercent = 0;
			 * if(CustomerStatusCombo.getSelectedItem().toString().equalsIgnoreCase("WAPU")){
			 * if(obj instanceof SalesInvoice){ SalesInvoice inv =
			 * (SalesInvoice)obj; ppnPercent = inv.getVatPercent(); // kalau
			 * wapu ada nilainya.... }else if(obj instanceof
			 * BeginningAccountReceivable){ ppnPercent = 10; //terpaksa
			 * dipaksakan } }
			 *
			 * double sales = arRcv.doubleValue() / (1 + (ppnPercent / 100));
			 */

			double sales = arRcv.doubleValue();
			// arRcv.Number amt = (Number) VATAmountText.getValue();
			// double vat = 0;
			// if(amt!=null)
			// vat = amt.doubleValue();
			double taxP = taxPercent.doubleValue();
			double tax = (sales) * (taxP / 100);
			if (m_ceksave)
				Tax23AmountText.setValue(new Double(tax));

		}
	}

	boolean m_ceksave = false;

	protected void doSave() {
		m_ceksave = false;
		if (!cekValidity())
			return;
		gui2entity();
		if (hasAnotherUnpostedTransaction())
			return;

		if (checkBalance())
			return;
		super.doSave();
	}

	private boolean hasAnotherUnpostedTransaction() {
		SalesInvoice salesInvoiceUsed = entity().getInvoice();
		if (salesInvoiceUsed == null)
			return false;

		SalesReceivedBusinessLogic logic = new SalesReceivedBusinessLogic(
				m_conn, m_sessionid);
		boolean result = false;
		try {
			result = logic.hasUnpostedTransaction(entity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result)
			JOptionPane
					.showMessageDialog(
							this,
							"There is a transaction for Invoice: "
									+ salesInvoiceUsed.getReferenceNo()
									+ " "
									+ "that has not been posted yet.\n"
									+ "Please post it before creating a new transaction.");

		return result;
	}

	private boolean checkBalance() {
		SalesInvoiceBusinessLogic logic = new SalesInvoiceBusinessLogic(m_conn,
				m_sessionid);
		BeginningAccountReceivableBusinessLogic bbLogic = new BeginningAccountReceivableBusinessLogic(m_conn, m_sessionid);


		// beginning balance
		SalesInvoice salesInvoice = entity().getInvoice();
		BeginningAccountReceivable bar = entity().getBeginningBalance();

		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		double totalReceived = 0;
		if (salesInvoice!=null)
			totalReceived = logic.getAccumulatedReceived(salesInvoice);
		else
			totalReceived = bbLogic.getAccumulatedReceived(bar);

		double begBalance = 0;
		if (salesInvoice!=null) {
			double sales = salesInvoice.getSalesAmount();
			double dp = salesInvoice.getDownPaymentAmount();
			sales = sales - dp;
			double vat = salesInvoice.getVatAmount();
			double ar = sales + vat;
			begBalance = ar - totalReceived;
		} else {
			double ar = bar.getAccValue();
			begBalance = ar - totalReceived;
		}


		//double exchRate = ((Number) ExchRateText.getValue()).doubleValue();
		/*
		 * BankAccount cb = (BankAccount) AccountComp.getObject();
		 * if(!salesInvoice.getSalesCurr().getSymbol().equals(cb.getCurrency().getSymbol())){
		 * if (cb.getCurrency().getSymbol().equals(baseCurrency.getSymbol()) )
		 * ar = received.getSalesARAmount() * exchRate; else ar =
		 * received.getSalesARAmount()/exchRate; }
		 */

		//SalesInvoice inv = entity().getInvoice();

		double rcvSales= entity().getSalesARAmount();
		double rcvVat = entity().getVatAmount();
		double rcvRetention = entity().getRetentionAmount();
		double rcvTranslation = entity().getTranslationAmount();

		double arRcv = rcvSales + rcvVat - rcvRetention;

		double invExchRate = 1;

			if (salesInvoice!=null)
				invExchRate = salesInvoice.getSalesExchRate();
			else
				invExchRate = bar.getExchangeRate();

			double rcvExchRate = entity().getSalesARExchRate();

			CashBankAccount cb = (CashBankAccount) AccountComp.getObject();
			Currency curr = null;

			if (salesInvoice!=null) {
				curr = salesInvoice.getSalesCurr();
			} else {
				curr = bar.getCurrency();
			}

			if (cb != null) {
				if (curr.getIndex() != cb.getCurrency().getIndex()) {
					if (cb.getCurrency().getIndex() == baseCurrency.getIndex()) {
						// inv $, rcv Rp
						arRcv = arRcv / invExchRate;
					} else {
						// inv Rp, rcv $
						arRcv = arRcv * rcvExchRate;
					}
				}

				// TRANSLATION (+) = gain
				// TRANSLATION (-) = loss
				if (curr.getIndex() == baseCurrency.getIndex()) {
					// inv Rp
					arRcv = arRcv - rcvTranslation;
				} else {
					// inv $
					arRcv = arRcv - (rcvTranslation / invExchRate);
				}
			}

		double balance = begBalance - arRcv;
		balance = nr.round(balance);

		boolean isOutstanding = balance < 0;

		if (isOutstanding) {
			JOptionPane.showMessageDialog(this,
					"Total received exceeds total account receivables!");
		}
		return isOutstanding;
	}

	private LookupPicker AccountComp;

	private javax.swing.JLabel AccountLabel;

	// private javax.swing.JTextField ARAmountText;
	private javax.swing.JFormattedTextField ARAmountText;

	private JFormattedTextField ARSalesAmountText;

	// private javax.swing.JTextField ARRcvExchRateText;
	private javax.swing.JFormattedTextField ARRcvExchRateText;

	// private javax.swing.JTextField AccReceiveAmountText;
	private JFormattedTextField AccReceiveAmountText;

	private javax.swing.JLabel ActivityCodeLabel;

	// private javax.swing.JTextField AmountBaseText;
	private javax.swing.JFormattedTextField AmountBaseText;

	private javax.swing.JLabel AmountReceiveLabel1;

	private javax.swing.JLabel AmountReceiveLabel2;

	private javax.swing.JLabel CurrencyLabel1;

	private javax.swing.JLabel CurrencyLabel8;

	private javax.swing.JLabel CustomerLabel;

	private javax.swing.JComboBox CustomerStatusCombo, CashBankCombo;

	private javax.swing.JLabel CustomerStatusLabel;

	private javax.swing.JLabel DepartmentLabel;

	private javax.swing.JLabel DescriptionLabel;

	private javax.swing.JScrollPane DescriptionScroll;

	private javax.swing.JTextArea DescriptionTextArea;

	private javax.swing.JButton DetailBtn;

	private javax.swing.JLabel DownPaymentLabel;

	private javax.swing.JLabel DownPaymentLabel1;

	private javax.swing.JLabel DownPaymentLabel2;

	private javax.swing.JLabel DownPaymentLabel3;

	// private javax.swing.JTextField BeginBalanceAmountText;
	private javax.swing.JFormattedTextField BeginBalanceAmountText;

	// private javax.swing.JTextField EndBalanceAmountText;
	private javax.swing.JFormattedTextField EndBalanceAmountText;

	private javax.swing.JTextField EndBalanceCurrText;

	private javax.swing.JLabel ExchRateLabel1;

	private javax.swing.JLabel ExchRateLabel4;

	private javax.swing.JLabel ExchRateLabel5;

	private javax.swing.JLabel ExchRateLabel6;

	// private javax.swing.JTextField ExchRateText;
	private JFormattedTextField ExchRateText;

	private javax.swing.JLabel IPCNoLabel;

	private javax.swing.JLabel InvoiceDateLabel;

	private LookupInvoicePicker InvoiceNoComp;

	private javax.swing.JLabel InvoiceNoLabel;;

	private javax.swing.JLabel ORNoLabel;

	private javax.swing.JLabel ProjectCodeLabel;

	private javax.swing.JLabel ReceiptDateLabel;

	private javax.swing.JLabel ReceiptNoLabel;

	private javax.swing.JLabel ReceiveToLabel;

	private javax.swing.JLabel RecvBaseCurrentLabel1;

	private javax.swing.JLabel RemarkLabel;

	private javax.swing.JScrollPane RemarkScroll;

	private javax.swing.JTextArea RemarksTextArea;

	private javax.swing.JLabel SOPOContractNo;

	private javax.swing.JLabel SalesAmountLabel1;

	private javax.swing.JLabel SalesAdvanceReceiveLabel;

	private javax.swing.JLabel SalesAdvanceReceiveLabel3;

	// private javax.swing.JTextField SalesAmountText;
	private JFormattedTextField SalesAmountText;

	private javax.swing.JLabel StatusLabel;

	private javax.swing.JLabel SubmittedDateLabel;

	private javax.swing.JLabel TaxArt23Label1;

	private javax.swing.JLabel VATLabel1;

	private javax.swing.JLabel BegBalBaseCurrLabel;

	private javax.swing.JLabel EndBalBaseCurrLabel;

	private javax.swing.JTextField ActivityCodeText;

	private javax.swing.JTextField AmountBaseCurrText;

	private javax.swing.JTextField AccReceiveCurrText;

	private javax.swing.JTextField ARRcvCurrText;

	private javax.swing.JTextField ARCurrText;

	private javax.swing.JTextField BeginBalanceCurrText;

	private javax.swing.JTextField ContractNoText;

	private javax.swing.JTextField CustomerText;

	private javax.swing.JTextField RefNoText;

	private javax.swing.JTextField ORNoText;

	private javax.swing.JTextField ProjectCodeText;

	private javax.swing.JTextField RcvToText;

	private javax.swing.JTextField IPCNoText;

	private javax.swing.JTextField CurrComp;

	private javax.swing.JTextField InvoiceDateText;

	private JFormattedTextField Tax23AmountText;

	private JFormattedTextField VATAmountText;

	private javax.swing.JTextField Tax23CurrText;

	private javax.swing.JTextField VATCurrText;

	private javax.swing.JTextField BegBalBaseCurrText;

	private javax.swing.JTextField EndBalBaseCurrText;

	private javax.swing.JTextField SalesCurrText;

	private javax.swing.JTextField StatusText;

	private javax.swing.JTextField SubmittedDateText;

	private javax.swing.JTextField DepartmentText;

	JTextField retentionCurrText;

	JFormattedTextField retentionText;

	JTextField BankchargesCurrText;

	JFormattedTextField bankchargesAmountText;

	JFormattedTextField bankChargeExchRateText;

	// private javax.swing.JTextField Tax23ExchRateText;
	private javax.swing.JFormattedTextField Tax23ExchRateText;

	private javax.swing.JFormattedTextField VATExchRateText;

	// private javax.swing.JTextField Tax23PercentText;
	private javax.swing.JFormattedTextField Tax23PercentText;

	// private javax.swing.JTextField TotalRcvAmountText;
	private javax.swing.JFormattedTextField TotalRcvAmountText;

	private javax.swing.JFormattedTextField BegBalBaseCurrAmountText;

	private javax.swing.JFormattedTextField EndBalBaseCurrAmountText;

	private javax.swing.JPanel TopButtonPanel;

	private javax.swing.JTextField TotalRcvCurrText;

	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;

	private javax.swing.JLabel UnitCodeLabel;

	private javax.swing.JTextField UnitCodeText;

	private javax.swing.JLabel WorkDescLabel;

	private javax.swing.JScrollPane WorkDescScroll;

	private javax.swing.JTextArea WorkDescTextArea;

	private javax.swing.JPanel mainPanel;

	private javax.swing.JPanel extTopButtonPanel;

	private javax.swing.JPanel extLeftRightPanel;

	private javax.swing.JPanel leftPanel;

	private javax.swing.JPanel accReceivableReceivePanel;

	private javax.swing.JPanel extRightPanel;

	private javax.swing.JPanel rightPanel;

	private javax.swing.JPanel jPanel1_2_2_1_1;

	private javax.swing.JPanel salesPanel;

	private javax.swing.JPanel accountReceivablePanel;

	private javax.swing.JPanel accumalateReceivePanel;

	private javax.swing.JPanel begBalPanel;

	private javax.swing.JPanel separatorAccRecPanel;

	private javax.swing.JPanel separatorInvPanel;

	private javax.swing.JPanel begBalBaseCurrPanel;

	private javax.swing.JPanel bottomPanel;

	private AssignPanel OriginatorComp;

	private javax.swing.JPanel ApprovedPanel;

	private AssignPanel ApprovedComp;

	private javax.swing.JPanel receivedPanel;

	private AssignPanel ReceivedComp;

	private javax.swing.JSeparator jSeparator1;

	private javax.swing.JSeparator jSeparator4;

	public void insertUpdate(DocumentEvent e) {
		doUpdate(e);
	}

	public void removeUpdate(DocumentEvent e) {
		doUpdate(e);
	}

	public void changedUpdate(DocumentEvent e) {
		doUpdate(e);
	}

	private void setAccount() {
		leftPanel.remove(AccountComp);
		leftPanel.revalidate();
		leftPanel.repaint();

		if (CashBankCombo.getSelectedItem().equals("Cash")) {
			setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));
		} else {
			setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid));
		}

		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();

		AccountComp.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(AccountComp, gridBagConstraints);
		leftPanel.revalidate();
	}

	protected void setAccountComp(LookupPicker accountComp) {
		LookupPicker old = AccountComp;
		if (old != null) {
			old.removePropertyChangeListener("object", this);
		}
		AccountComp = accountComp;
		AccountComp.addPropertyChangeListener("object", this);
	}

	protected void doNew() {
		m_ceksave = true;
		clearComponent();
		super.doNew();

		isiDefaultAssignPanel();
		revalidateInvoiceLookup();
	}

	private void revalidateInvoiceLookup() {
		InvoiceNoComp.refreshData();
	}

	public void doEdit() {
		super.doEdit();

		/*
		 * BankAccount cb = null; Object obj = AccountComp.getObject(); if (obj
		 * instanceof BankAccount) cb = (BankAccount) obj; if(cb != null)
		 * if(cb.getCurrency().getSymbol().equals(cb.getCurrency().getSymbol()))
		 * ARRcvExchRateText.setEditable(false);
		 */

		/*
		 * VATAmountText.setEditable(true);
		 * if(CustomerStatusCombo.getSelectedItem().equals("NONWAPU")){
		 * VATAmountText.setValue(null); VATAmountText.setEditable(false); }
		 */
		m_ceksave = true;
	}

	protected void doCancel() {
		m_ceksave = false;
		super.doCancel();
	}

	/* (non-Javadoc)
	 * @see pohaci.gumunda.titis.accounting.cgui.RevTransactionPanel#checkBeforeSubmit()
	 */
	protected boolean checkBeforeSubmit() {
		StateTemplateEntity currentEntity = currentEntity();
		if (currentEntity instanceof SalesReceived) {
			SalesReceived entity = (SalesReceived) currentEntity;

			if ((entity.getSalesARCurr().getIndex()!=baseCurrency.getIndex())&&(entity.getSalesARExchRate()==1)) {
				JOptionPane.showMessageDialog(this, "Please check your Exchange Rate!");
				return false;
			}
		} else
			return false;

		return true;
	}
}
