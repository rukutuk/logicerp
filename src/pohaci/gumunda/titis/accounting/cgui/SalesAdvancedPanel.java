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
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.Rcpt_SalesAdv;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.SalesAdvance;
import pohaci.gumunda.titis.accounting.entity.SalesReceivedLoader;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.ProjectContract;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.ProjectDataPicker;

public class SalesAdvancedPanel extends RevTransactionPanel implements ActionListener, PropertyChangeListener, DocumentListener{
	private static final long serialVersionUID = 1L;
	private SalesAdvance m_entity;
	Employee defaultOriginator;
	Employee defaultApproved;
	Employee defaultReceived;	
	ArrayList validityMsgs = new ArrayList();
	private Document VATPercentDoc;
	private Document Tax23PercentDoc;
	private Document SalesAdvExchRateDoc;
	private Document VATExchRateDoc;
	private Document Tax23ExchRateDoc;
	
	public SalesAdvancedPanel(Connection conn, long sessionid) {
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
		setEntity(new SalesAdvance());
		m_entityMapper = MasterMap.obtainMapperFor(SalesAdvance.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}

	private void setDefaultExchangeRate(Currency currency) {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		SalesAdvExchRateText.setValue(new Double(rate));
	}
	
	private void enableAwal() {
		disableSomeComponents();
	}
	
	private void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.SALES_ADVANCE, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.SALES_ADVANCE, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.SALES_ADVANCE, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	
	private void disableSomeComponents() {
		StatusText.setEditable(false);
		SubmittedDateText.setEditable(false);
		RcvToText.setEditable(false);
		SalesAdvCurrComp.setEditable(false);
		VATCurrText.setEditable(false);
		VATAmountText.setEditable(false);
		Tax23CurrText.setEditable(false);
		AmountRcvCurrText.setEditable(false);
		AmountRcvText.setEditable(false);
		RcvBaseCurrText.setEditable(false);
		RcvBaseAmountText.setEditable(false);
		RefNoText.setEditable(false);
		CustomerText.setEditable(false);
		WorkDescTextArea.setEditable(false);
		UnitCodeText.setEditable(false);
		ActivityCodeText.setEditable(false);
		DepartmentText.setEditable(false);
		ORNoText.setEditable(false);
		ContractNoText.setEditable(false);
		IPCNoText.setEditable(false);
		
		VATExchRateText.setEditable(false);
		Tax23ExchRateText.setEditable(false);
	}
	
	private void initComponents() {
		AccountComp = new LookupBankAccountPicker(m_conn, m_sessionid);
		ProjectCodeComp = new ProjectDataPicker(m_conn, m_sessionid);
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
		CustomerStatusLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		RcvToText = new javax.swing.JTextField();
		jPanel1_2_1_1 = new javax.swing.JPanel();
		SalesAdvAmountLabel = new javax.swing.JLabel();
		VATLabel = new javax.swing.JLabel();
		TaxArt23Label = new javax.swing.JLabel();
		AmountReceiveLabel = new javax.swing.JLabel();
		RecvBaseCurrentLabel = new javax.swing.JLabel();
		ExchRateLabel = new javax.swing.JLabel();
		Tax23ExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		Tax23PercentText = new javax.swing.JFormattedTextField(m_numberFormatter);
		VATPercentText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountRcvCurrText = new javax.swing.JTextField();
		RcvBaseCurrText = new javax.swing.JTextField();
		CurrencyLabel6 = new javax.swing.JLabel();
		CurrencyLabel7 = new javax.swing.JLabel();
		Tax23CurrText = new javax.swing.JTextField();
		VATCurrText = new javax.swing.JTextField();
		AmountRcvText = new javax.swing.JFormattedTextField(m_numberFormatter);
		RcvBaseAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		SalesAdvAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		VATAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		SalesAdvCurrComp = new javax.swing.JTextField();
		VATExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		ExchRateLabel1 = new javax.swing.JLabel();
		SalesAdvExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		ExchRateLabel2 = new javax.swing.JLabel();
		Tax23AmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		SalesAdvanceReceiveLabel = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScroll = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		CustomerStatusCombo = new javax.swing.JComboBox();
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_2_1 = new javax.swing.JPanel();
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
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		
		setLayout(new java.awt.BorderLayout());
		
		setPreferredSize(new java.awt.Dimension(750, 500));
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		jPanel1.setPreferredSize(new java.awt.Dimension(650, 450));
		jPanel1_1.setLayout(new java.awt.BorderLayout());
		
		jPanel1_1.setPreferredSize(new java.awt.Dimension(650, 35));
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
		
		jPanel1_2.setPreferredSize(new java.awt.Dimension(650, 200));
		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(490, 200));
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
		
		ReceiveToLabel.setText("Receive To");
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveToLabel, gridBagConstraints);
		
		AccountLabel.setText("Account*");
		AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountLabel, gridBagConstraints);
		
		CustomerStatusLabel.setText("Customer Status");
		CustomerStatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(CustomerStatusLabel, gridBagConstraints);
		
		StatusText.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(StatusText, gridBagConstraints);
		
		SubmittedDateText.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(SubmittedDateText, gridBagConstraints);
		
		RcvToText.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(RcvToText, gridBagConstraints);
		
		AccountComp.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);
		
		jPanel1_2_1_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(470, 130));
		SalesAdvAmountLabel.setText("Sales Advance*");
		SalesAdvAmountLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(SalesAdvAmountLabel, gridBagConstraints);
		
		TaxArt23Label.setText("Tax Art 23");
		TaxArt23Label.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(TaxArt23Label, gridBagConstraints);
		
		VATLabel.setText("VAT");
		VATLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(VATLabel, gridBagConstraints);
		
		AmountReceiveLabel.setText("Amount Receive");
		AmountReceiveLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(AmountReceiveLabel, gridBagConstraints);
		
		RecvBaseCurrentLabel.setText("Rcv. Base Curr");
		RecvBaseCurrentLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 0);
		jPanel1_2_1_1.add(RecvBaseCurrentLabel, gridBagConstraints);
		
		ExchRateLabel.setText("Exch. Rate*");
		ExchRateLabel.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(ExchRateLabel, gridBagConstraints);
		
		Tax23ExchRateText.setPreferredSize(new java.awt.Dimension(142, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(Tax23ExchRateText, gridBagConstraints);
		
		VATPercentText.setPreferredSize(new java.awt.Dimension(30, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(VATPercentText, gridBagConstraints);
		
		Tax23PercentText.setPreferredSize(new java.awt.Dimension(30, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(Tax23PercentText, gridBagConstraints);
		
		AmountRcvCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(AmountRcvCurrText, gridBagConstraints);
		
		RcvBaseCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(RcvBaseCurrText, gridBagConstraints);
		
		CurrencyLabel6.setText("%");
		CurrencyLabel6.setPreferredSize(new java.awt.Dimension(20, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(CurrencyLabel6, gridBagConstraints);
		
		CurrencyLabel7.setText("%");
		CurrencyLabel7.setPreferredSize(new java.awt.Dimension(20, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(CurrencyLabel7, gridBagConstraints);
		
		VATCurrText.setPreferredSize(new java.awt.Dimension(30, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(VATCurrText, gridBagConstraints);
		
		Tax23CurrText.setPreferredSize(new java.awt.Dimension(30, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(Tax23CurrText, gridBagConstraints);
		
		AmountRcvText.setPreferredSize(new java.awt.Dimension(208, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(AmountRcvText, gridBagConstraints);
		
		RcvBaseAmountText.setPreferredSize(new java.awt.Dimension(208, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(RcvBaseAmountText, gridBagConstraints);
		
		SalesAdvAmountText.setPreferredSize(new java.awt.Dimension(5, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(SalesAdvAmountText, gridBagConstraints);
		
		VATAmountText.setPreferredSize(new java.awt.Dimension(58, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 0);
		jPanel1_2_1_1.add(VATAmountText, gridBagConstraints);
		
		SalesAdvCurrComp.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(SalesAdvCurrComp, gridBagConstraints);
		
		VATExchRateText.setPreferredSize(new java.awt.Dimension(142, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(VATExchRateText, gridBagConstraints);
		
		ExchRateLabel1.setText("Exch. Rate*");
		ExchRateLabel1.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(ExchRateLabel1, gridBagConstraints);
		
		SalesAdvExchRateText.setPreferredSize(new java.awt.Dimension(142, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(SalesAdvExchRateText, gridBagConstraints);
		
		ExchRateLabel2.setText("Exch. Rate*");
		ExchRateLabel2.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(ExchRateLabel2, gridBagConstraints);
		
		Tax23AmountText.setPreferredSize(new java.awt.Dimension(58, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 0);
		jPanel1_2_1_1.add(Tax23AmountText, gridBagConstraints);
		
		SalesAdvanceReceiveLabel.setText("Sales Adv. Receive");
		SalesAdvanceReceiveLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
		jPanel1_2_1_1.add(SalesAdvanceReceiveLabel, gridBagConstraints);
		
		jSeparator1.setPreferredSize(new java.awt.Dimension(260, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(jSeparator1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(jPanel1_2_1_1, gridBagConstraints);
		
		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DescriptionLabel, gridBagConstraints);
		
		DescriptionScroll.setPreferredSize(new java.awt.Dimension(350, 85));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScroll.setViewportView(DescriptionTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DescriptionScroll, gridBagConstraints);
		
		CustomerStatusCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "WAPU", "NONWAPU" }));
		CustomerStatusCombo.setPreferredSize(new java.awt.Dimension(350, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(CustomerStatusCombo, gridBagConstraints);
		
		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(440, 305));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(440, 200));
		ReceiptNoLabel.setText("Receipt No");
		ReceiptNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ReceiptNoLabel, gridBagConstraints);
		
		RefNoText.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);
		
		ReceiptDateLabel.setText("Receipt Date*");
		ReceiptDateLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ReceiptDateLabel, gridBagConstraints);
		
		ProjectCodeLabel.setText("Project Code*");
		ProjectCodeLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ProjectCodeLabel, gridBagConstraints);
		
		CustomerLabel.setText("Customer");
		CustomerLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(CustomerLabel, gridBagConstraints);
		
		WorkDescLabel.setText("Work Desc.");
		WorkDescLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(WorkDescLabel, gridBagConstraints);
		
		ProjectCodeComp.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ProjectCodeComp, gridBagConstraints);
		
		CustomerText.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(CustomerText, gridBagConstraints);
		
		WorkDescScroll.setPreferredSize(new java.awt.Dimension(320, 60));
		WorkDescTextArea.setColumns(20);
		WorkDescTextArea.setLineWrap(true);
		WorkDescTextArea.setRows(5);
		WorkDescScroll.setViewportView(WorkDescTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(WorkDescScroll, gridBagConstraints);
		
		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(UnitCodeLabel, gridBagConstraints);
		
		UnitCodeText.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(UnitCodeText, gridBagConstraints);
		
		ActivityCodeLabel.setText("Activity Code");
		ActivityCodeLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ActivityCodeLabel, gridBagConstraints);
		
		ActivityCodeText.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ActivityCodeText, gridBagConstraints);
		
		DepartmentLabel.setText("Department");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(DepartmentLabel, gridBagConstraints);
		
		ORNoLabel.setText("O.R No");
		ORNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ORNoLabel, gridBagConstraints);
		
		DepartmentText.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(DepartmentText, gridBagConstraints);
		
		ORNoText.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ORNoText, gridBagConstraints);
		
		SOPOContractNo.setText("SO/PO/Contract No");
		SOPOContractNo.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(SOPOContractNo, gridBagConstraints);
		
		IPCNoLabel.setText("IPC No");
		IPCNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(IPCNoLabel, gridBagConstraints);
		
		ContractNoText.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ContractNoText, gridBagConstraints);
		
		IPCNoText.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(IPCNoText, gridBagConstraints);
		
		RemarkLabel.setText("Remarks");
		RemarkLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(RemarkLabel, gridBagConstraints);
		
		RemarkScroll.setPreferredSize(new java.awt.Dimension(320,50));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarkScroll.setViewportView(RemarksTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(RemarkScroll, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(320, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
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
		OriginatorComp.setPreferredSize(new java.awt.Dimension(308, 110));
		
		jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.setLayout(new java.awt.BorderLayout());
		
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		
		ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(308, 110));
		
		jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_3_2_2.setPreferredSize(new java.awt.Dimension(215, 110));
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		
		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(306, 110));
		
		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);
		
		add(jPanel1, java.awt.BorderLayout.NORTH);
		
	}
	
	private void addingListener(){
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		AccountComp.addPropertyChangeListener(this);
		ProjectCodeComp.addPropertyChangeListener(this);    	
		SalesAdvAmountText.addPropertyChangeListener(this);    	
		VATPercentText.addPropertyChangeListener(this);
		Tax23PercentText.addPropertyChangeListener(this);
		Tax23AmountText.addPropertyChangeListener(this);    	
		AmountRcvText.addPropertyChangeListener(this);    	
		SalesAdvExchRateText.addPropertyChangeListener(this);
		VATExchRateText.addPropertyChangeListener(this);
		Tax23ExchRateText.addPropertyChangeListener(this);
		CustomerStatusCombo.addActionListener(this);
		TransactionDateDate.addPropertyChangeListener("date", this);
	}  
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_printViewRefNoBtn) {			
			if (m_entity.getIndex()>0){
				new Rcpt_SalesAdv(m_entity);
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
					new SalesReceivedLoader(m_conn,SalesAdvance.class),"Sales Advance");
			
			dlg.setVisible(true);
			if (dlg.selectedObj != null) {
				doLoad(dlg.selectedObj);
			}
		}
		else if(e.getSource() == CustomerStatusCombo) {
			onChangeCustomerStatusCombo();
		}
	} 
	
	protected void doNew() {
		super.doNew();
		onChangeCustomerStatusCombo();
		enableExchangeRate();
		isiDefaultAssignPanel();
	}

	private void enableExchangeRate() {
		// cara tolol
		// khawatir ama performance...
		String salesAdvCurr = SalesAdvCurrComp.getText();
		String rcvCurr = AmountRcvCurrText.getText();
		
		if((!salesAdvCurr.equals(""))&&(!rcvCurr.equals(""))){
			if(!salesAdvCurr.equalsIgnoreCase(rcvCurr)){
				SalesAdvExchRateText.setEditable(true);
				//setDefaultExchangeRate(currency);
			}else{
				if(!salesAdvCurr.equalsIgnoreCase(baseCurrency.getSymbol())){
					SalesAdvExchRateText.setEditable(true);
					//setDefaultExchangeRate(currency);
				}else{
					SalesAdvExchRateText.setEditable(false);
					SalesAdvExchRateText.setValue(new Double(1.0));
				}
			}
		}else{
			SalesAdvExchRateText.setEditable(false);
		}
	}

	private void isiDefaultAssignPanel() {
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
	}

	private void onChangeCustomerStatusCombo() {
		if(CustomerStatusCombo.getSelectedItem().equals("WAPU")){
			// ga ada ppn
			VATPercentText.setValue(new Double(0));
			VATPercentText.setEditable(false);
			VATAmountText.setValue(new Double(0));
		}else{
			VATPercentText.setEditable(true);
		}
	}

	private LookupBankAccountPicker AccountComp;
	private javax.swing.JLabel AccountLabel;
	private javax.swing.JLabel ActivityCodeLabel;
	private javax.swing.JTextField ActivityCodeText;
	private javax.swing.JTextField AmountRcvCurrText;
	private javax.swing.JFormattedTextField AmountRcvText;
	private javax.swing.JLabel AmountReceiveLabel;
	private javax.swing.JTextField ContractNoText;
	private javax.swing.JLabel CurrencyLabel6;
	private javax.swing.JLabel CurrencyLabel7;
	private javax.swing.JLabel CustomerLabel;
	private javax.swing.JComboBox CustomerStatusCombo;
	private javax.swing.JLabel CustomerStatusLabel;
	private javax.swing.JTextField CustomerText;
	private javax.swing.JLabel DepartmentLabel;
	private javax.swing.JTextField DepartmentText;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScroll;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JLabel ExchRateLabel;
	private javax.swing.JLabel ExchRateLabel1;
	private javax.swing.JLabel ExchRateLabel2;
	private javax.swing.JLabel IPCNoLabel;
	private javax.swing.JTextField IPCNoText;;
	private javax.swing.JLabel ORNoLabel;
	private javax.swing.JTextField ORNoText;  
	private ProjectDataPicker ProjectCodeComp;
	private javax.swing.JLabel ProjectCodeLabel;
	private javax.swing.JFormattedTextField RcvBaseAmountText;
	private javax.swing.JTextField RcvBaseCurrText;
	private javax.swing.JTextField RcvToText;
	private javax.swing.JLabel ReceiptDateLabel;
	private javax.swing.JLabel ReceiptNoLabel;
	private javax.swing.JLabel ReceiveToLabel;
	private javax.swing.JLabel RecvBaseCurrentLabel;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarkLabel;
	private javax.swing.JScrollPane RemarkScroll;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel SOPOContractNo;
	private javax.swing.JLabel SalesAdvAmountLabel;
	private javax.swing.JFormattedTextField SalesAdvAmountText;
	private javax.swing.JTextField SalesAdvCurrComp;
	private javax.swing.JFormattedTextField SalesAdvExchRateText;
	private javax.swing.JLabel SalesAdvanceReceiveLabel;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JTextField SubmittedDateText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JFormattedTextField Tax23AmountText;
	private javax.swing.JTextField Tax23CurrText;
	private javax.swing.JFormattedTextField Tax23ExchRateText;
	private javax.swing.JLabel TaxArt23Label;
	private javax.swing.JFormattedTextField Tax23PercentText;
	private javax.swing.JPanel TopButtonPanel;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JLabel UnitCodeLabel;
	private javax.swing.JTextField UnitCodeText;
	private javax.swing.JFormattedTextField VATAmountText;
	private javax.swing.JFormattedTextField VATExchRateText;
	private javax.swing.JLabel VATLabel;
	private javax.swing.JFormattedTextField VATPercentText;
	private javax.swing.JTextField VATCurrText;
	private javax.swing.JLabel WorkDescLabel;
	private javax.swing.JScrollPane WorkDescScroll;
	private javax.swing.JTextArea WorkDescTextArea;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel jPanel1_2;
	private javax.swing.JPanel jPanel1_2_1;
	private javax.swing.JPanel jPanel1_2_1_1;
	private javax.swing.JPanel jPanel1_2_2;
	private javax.swing.JPanel jPanel1_2_2_1;
	private javax.swing.JPanel jPanel1_3;
	private AssignPanel OriginatorComp;
	private javax.swing.JPanel jPanel1_3_2;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel jPanel1_3_2_2;
	private AssignPanel ReceivedComp;
	private javax.swing.JSeparator jSeparator1;
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	private SalesAdvance entity() {
		return m_entity;
	}
	
	protected boolean cekValidity() {
		validityMsgs.clear();
		if (TransactionDateDate.getDate() == null)
			addInvalid("Receipt date must be inserted");
		if (AccountComp.getObject()== null)
			addInvalid("Bank Account must be selected");
		if (SalesAdvAmountText.getValue() == null)
			addInvalid("Sales Advance Amount must be inserted");
		if (DescriptionTextArea.getText().equals(""))
			addInvalid("Description must be selected");
		if (ProjectCodeComp.getObject() == null)
			addInvalid("Project Code must be selected");
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
		entity().setReceiveTo("BANK");		
		BankAccount bankAccount = (BankAccount)AccountComp.getObject();
		entity().setBankAccount(bankAccount);
		entity().setCustomerStatus((String)CustomerStatusCombo.getSelectedItem());
		Number val = (Number)SalesAdvAmountText.getValue();
		if(val!=null)
			entity().setSalesAdvAmount(val.doubleValue());
		
		val = (Number)SalesAdvExchRateText.getValue();
		if(val!=null)
			entity().setSalesAdvExchRate(val.doubleValue());
		
		val = (Number)VATPercentText.getValue();
		if(val!=null)
			entity().setVatPercent(val.doubleValue());			
		val = (Number)VATAmountText.getValue();
		if(val!=null)
			entity().setVatAmount(val.doubleValue());			
		val = (Number)VATExchRateText.getValue();
		if(val!=null)
			entity().setVatExchRate(val.doubleValue());
		
		val = (Number)Tax23PercentText.getValue();
		if(val!=null)
			entity().setTax23Percent(val.doubleValue());	
		val = (Number)Tax23AmountText.getValue();
		if(val!=null)
			entity().setTax23Amount(val.doubleValue());
		val = (Number)Tax23ExchRateText.getValue();
		if(val!=null)
			entity().setTax23ExchRate(val.doubleValue());
		entity().setTransactionDate(TransactionDateDate.getDate());
		entity().setProject((ProjectData)ProjectCodeComp.getObject());		
		if(entity().getProject()!=null){
			ProjectData project = getRealProject(entity().getProject().getIndex());
			ProjectContract projContract = getProjectContract(project);
			Currency curr = null;
			if (projContract!=null){
				project.setProjectContract(projContract);			
				curr = project.getProjectContract().getCurrency();
			}else{
				curr = baseCurrency;
			}
			entity().setCurrency(curr);
			entity().setSalesAdvCurr(curr);
			entity().setVatCurr(curr);
			entity().setTax23Curr(curr);
			entity().setUnit(bankAccount.getUnit());
		}		
		entity().transTemplateRead(OriginatorComp, ApprovedComp, ReceivedComp, RefNoText, DescriptionTextArea);
		entity().setRemarks(RemarksTextArea.getText());
	}
	
	protected void entity2gui() {
		StatusText.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");    	
		if(entity().getReceiveTo()==null)
			RcvToText.setText("BANK");
		else
			RcvToText.setText(entity().getReceiveTo());    	
		ProjectCodeComp.setObject(entity().getProject());    	
		AccountComp.setObject(entity().getBankAccount());
		String custStatus = entity().getCustomerStatus();
		if(custStatus==null)
			custStatus = "WAPU";
		CustomerStatusCombo.setSelectedItem(custStatus);    	   	
		if(entity().getSalesAdvCurr()!=null){
			SalesAdvCurrComp.setText(entity().getSalesAdvCurr().getSymbol());
		}
		if(entity().getVatCurr()!=null)
			VATCurrText.setText(entity().getVatCurr().getSymbol());
		if(entity().getTax23Curr()!=null)
			Tax23CurrText.setText(entity().getTax23Curr().getSymbol());
		if(entity().getBankAccount()!=null)
			AmountRcvCurrText.setText(entity().getBankAccount().getCurrency().getSymbol());
		RcvBaseCurrText.setText(baseCurrency.getSymbol());    	
		SalesAdvAmountText.setValue(new Double(entity().getSalesAdvAmount()));
		double val = entity().getSalesAdvExchRate();
		if(val==0)
			val = 1.0;
		SalesAdvExchRateText.setValue(new Double(val));    	
		VATPercentText.setValue(new Double(entity().getVatPercent()));
		VATAmountText.setValue(new Double(entity().getVatAmount()));
		val = entity().getVatExchRate();
		if(val==0)
			val = 1.0;
		VATExchRateText.setValue(new Double(val));    	
		Tax23PercentText.setValue(new Double(entity().getTax23Percent()));
		Tax23AmountText.setValue(new Double(entity().getTax23Amount()));
		val = entity().getTax23ExchRate();
		if(val==0)
			val = 1.0;
		Tax23ExchRateText.setValue(new Double(val));    	
		DescriptionTextArea.setText(entity().getDescription());    	
		RefNoText.setText(entity().getReferenceNo());
		if(entity().getTransactionDate()!=null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else
			TransactionDateDate.setDate(new Date());    	
		RemarksTextArea.setText(entity().getRemarks());    	
		OriginatorComp.setEmployee(entity().getEmpOriginator());
		OriginatorComp.setJobTitle(entity().getJobTitleOriginator());
		OriginatorComp.setDate(entity().getDateOriginator());    	
		ApprovedComp.setEmployee(entity().getEmpApproved());
		ApprovedComp.setJobTitle(entity().getJobTitleApproved());
		ApprovedComp.setDate(entity().getDateApproved());    	
		ReceivedComp.setEmployee(entity().getEmpReceived());
		ReceivedComp.setJobTitle(entity().getJobTitleReceived());
		ReceivedComp.setDate(entity().getDateReceived());
	}
	
	protected void disableEditMode() {
		AccountComp.setEnabled(false);
		CustomerStatusCombo.setEnabled(false);
		SalesAdvAmountText.setEditable(false);
		SalesAdvExchRateText.setEditable(false);
		VATPercentText.setEditable(false);
		//VATExchRateText.setEditable(false);
		Tax23PercentText.setEditable(false);
		Tax23AmountText.setEditable(false);
		//Tax23ExchRateText.setEditable(false);
		DescriptionTextArea.setEditable(false);
		TransactionDateDate.setEditable(false);
		ProjectCodeComp.setEnabled(false);
		RemarksTextArea.setEditable(false);
		OriginatorComp.setEnabled(false);
		ApprovedComp.setEnabled(false);
		ReceivedComp.setEnabled(false);
		
		update();
	}
	
	private void update() {
		if(entity()!=null)
			AccountComp.setObject(entity().getBankAccount());
	}

	protected void enableEditMode() {
		AccountComp.setEnabled(true);
		CustomerStatusCombo.setEnabled(true);
		SalesAdvAmountText.setEditable(true);
		SalesAdvExchRateText.setEditable(true);	
		VATPercentText.setEditable(true);
		//VATExchRateText.setEditable(true);
		Tax23PercentText.setEditable(true);
		Tax23AmountText.setEditable(true);
		//Tax23ExchRateText.setEditable(true);
		DescriptionTextArea.setEditable(true);
		TransactionDateDate.setEditable(true);
		ProjectCodeComp.setEnabled(true);
		RemarksTextArea.setEditable(true);
		OriginatorComp.setEnabled(true);
		ApprovedComp.setEnabled(true);
		ReceivedComp.setEnabled(true);
	}
	
	protected Object createNew() {
		SalesAdvance o = new SalesAdvance();
		o.setEmpOriginator(defaultOriginator);
		o.setEmpApproved(defaultApproved);
		o.setEmpReceived(defaultReceived);
		return o;
	}
	
	void setEntity(Object m_entity) {
		SalesAdvance oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (SalesAdvance)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		if ("object".equals(evt.getPropertyName())) {
			if (evt.getSource() == ProjectCodeComp) {
				ProjectData project = (ProjectData) ProjectCodeComp.getObject();
				if (project != null) {
					project = getRealProject(project.getIndex());
					CustomerText.setText(project.getCustname());
					WorkDescTextArea.setText(project.getWorkDescription());
					UnitCodeText.setText(project.getUnit().toString());
					ActivityCodeText.setText(project.getActivity().toString());
					DepartmentText.setText(project.getDepartment().toString());
					ORNoText.setText(project.getORNo());
					ContractNoText.setText(project.getPONo());
					IPCNoText.setText(project.getIPCNo());

					ProjectContract projContract = getProjectContract(project);
					if (projContract != null) {
						project.setProjectContract(projContract);
						setCurrency(project.getProjectContract().getCurrency());
						setAccount(project.getProjectContract().getCurrency());
						AccountComp.setObject(entity().getBankAccount());

						enableExchangeRate(project.getProjectContract()
								.getCurrency());
					} else {
						/*
						 * JOptionPane.showMessageDialog(this,"Project contract
						 * of the selected project has not been defined yet!\n" +
						 * "System will use base currency as its transactional
						 * currency.", "Warning!",
						 * JOptionPane.INFORMATION_MESSAGE);
						 */
						setCurrency(baseCurrency);
						setAccount(baseCurrency);
					}

					countAmount();
					countAmountInBaseCurrency();
				} else {
					CustomerText.setText("");
					WorkDescTextArea.setText("");
					UnitCodeText.setText("");
					ActivityCodeText.setText("");
					DepartmentText.setText("");
					ORNoText.setText("");
					ContractNoText.setText("");
					IPCNoText.setText("");
				}
			} else if (evt.getSource() == AccountComp) {
				// setRcvCurrency();
				enableExchangeRate();
				countAmount();
				countAmountInBaseCurrency();
			}
		}
		if ("value".equals(evt.getPropertyName())) {
			if (evt.getSource() == SalesAdvAmountText) {
				countVAT();
				countTax();
				countAmount();
			}

			else if (evt.getSource() == Tax23AmountText) {
				countAmount();
			} else if (evt.getSource() == AmountRcvText) {
				countAmountInBaseCurrency();
			} else if (evt.getSource() == SalesAdvExchRateText) {
				countAmountInBaseCurrency();
				setExchangeRate();
			} else if (evt.getSource() == VATPercentText) {
				countVAT();
				countAmount();
			} else if (evt.getSource() == Tax23PercentText) {
				countTax();
				countAmount();
			} else if ((evt.getSource() == SalesAdvExchRateText)
					|| (evt.getSource() == VATExchRateText)
					|| (evt.getSource() == Tax23ExchRateText)) {
				countAmount();
				countAmountInBaseCurrency();
			}
		}
		if ("date".equals(evt.getPropertyName())) {
			if (evt.getSource() == TransactionDateDate) {
				ProjectData project = (ProjectData) ProjectCodeComp.getObject();
				if (project != null) {
					project = getRealProject(project.getIndex());
					ProjectContract projContract = getProjectContract(project);
					if (projContract != null) {
						project.setProjectContract(projContract);
						enableExchangeRate(project.getProjectContract()
								.getCurrency());
					}
				}
			}
		}
	}
	
	private void setAccount(Currency currency) {
		jPanel1_2_1.remove(AccountComp);
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();
		
		setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid, currency));
		
		AccountComp.setPreferredSize(new java.awt.Dimension(350, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);
		jPanel1_2_1.revalidate(); 
	}
	
	protected void setAccountComp(LookupBankAccountPicker accountComp) {
		LookupPicker old = AccountComp;
		if (old!=null){
			old.removePropertyChangeListener("object",this);
		}
		AccountComp = accountComp;
		AccountComp.addPropertyChangeListener("object",this);
	}

	private void enableExchangeRate(Currency currency) {
		// cara tolol
		// khawatir ama performance...
		String salesAdvCurr = SalesAdvCurrComp.getText();
		String rcvCurr = AmountRcvCurrText.getText();
		
		if((!salesAdvCurr.equals(""))&&(!rcvCurr.equals(""))){
			if(!salesAdvCurr.equalsIgnoreCase(rcvCurr)){
				SalesAdvExchRateText.setEditable(true);
				setDefaultExchangeRate(currency);
			}else{
				if(!salesAdvCurr.equalsIgnoreCase(baseCurrency.getSymbol())){
					SalesAdvExchRateText.setEditable(true);
					setDefaultExchangeRate(currency);
				}else{
					SalesAdvExchRateText.setEditable(false);
					SalesAdvExchRateText.setValue(new Double(1.0));
				}
			}
		}else{
			SalesAdvExchRateText.setEditable(false);
		}
	}

	private void setExchangeRate() {
		Number exch = (Number)SalesAdvExchRateText.getValue();
		if(exch!=null){
			Tax23ExchRateText.setValue(new Double(exch.doubleValue()));
			VATExchRateText.setValue(new Double(exch.doubleValue()));
		}
	}

	private ProjectContract getProjectContract(ProjectData project) {
		GenericMapper mapper = MasterMap.obtainMapperFor(ProjectContract.class);
		mapper.setActiveConn(m_conn);		
		String whereClause = "PROJECT=" + project.getIndex();		
		List projectContract = mapper.doSelectWhere(whereClause);
		if(projectContract.size()>0)
			return (ProjectContract) projectContract.get(0);		
		return null;
	}
	
	private ProjectData getRealProject(long index) {
		GenericMapper mapper = MasterMap.obtainMapperFor(ProjectData.class);
		mapper.setActiveConn(m_conn);		
		ProjectData project = (ProjectData) mapper.doSelectByIndex(new Long(index));		
		return project;
	}
	
	private void setCurrency(Currency curr) {
		String symbol = curr.getSymbol();
		SalesAdvCurrComp.setText(symbol);
		VATCurrText.setText(symbol);
		Tax23CurrText.setText(symbol);
		AmountRcvCurrText.setText(symbol);
		//AmountRcvCurrText.setText(symbol);
	}
	
	private void countAmountInBaseCurrency() {
		String amtCurr = AmountRcvCurrText.getText();
		String baseCurr = baseCurrency.getSymbol();
		
		Number amt = (Number)AmountRcvText.getValue();
		Number exch = (Number)SalesAdvExchRateText.getValue();
		
		if((amt!=null)&&(exch!=null)&&(!amtCurr.equals(""))&&(!baseCurr.equals(""))){
			if(amtCurr.equals(baseCurr)){
				RcvBaseAmountText.setValue(new Double(amt.doubleValue()));
			}else{
				RcvBaseAmountText.setValue(new Double(amt.doubleValue() * exch.doubleValue()));
			}
		}		
	}
	
	private void countAmount() {
		Number amount = (Number)SalesAdvAmountText.getValue();
		Number tax = (Number)Tax23AmountText.getValue();
		Number vat = (Number)VATAmountText.getValue();		
		if((amount!=null)&&(tax!=null)&&(vat!=null)){
			double val = amount.doubleValue() +
			vat.doubleValue() -
			tax.doubleValue();
			
			String salesAdvCurr = SalesAdvCurrComp.getText();
			String rcvCurr = AmountRcvCurrText.getText();
			if((!salesAdvCurr.equals(""))&&(!rcvCurr.equals(""))){
				if(!salesAdvCurr.equals(rcvCurr)){
					if(salesAdvCurr.equals(baseCurrency.getSymbol())){
						Number exch = (Number)SalesAdvExchRateText.getValue();
						if(exch!=null)
							val /= exch.doubleValue();
					}else{
						Number exch = (Number)SalesAdvExchRateText.getValue();
						if(exch!=null)
							val *= exch.doubleValue();
					}
				}else{
					
				}
			}
			AmountRcvText.setValue(new Double(val));
		}
	}
	
	private void countTax() {
		Number amount = (Number)SalesAdvAmountText.getValue();
		Number tax = (Number)Tax23PercentText.getValue();		
		if((amount!=null)&&(tax!=null)){
			double val = amount.doubleValue() * (tax.doubleValue() / 100);
			Tax23AmountText.setValue(new Double(val));
		}
	}
	
	private void countVAT() {
		Number amount = (Number)SalesAdvAmountText.getValue();
		Number vat = null;
		if(!VATPercentText.getText().equals(""))
			if(VATPercentText.getValue()!=null)
				vat = new Double(((Number)VATPercentText.getValue()).doubleValue());
			else
				vat = new Double(0);		
		if((amount!=null)&&(vat!=null)){
			double val = amount.doubleValue() * (vat.doubleValue() / 100);
			VATAmountText.setValue(new Double(val));
		}
	}
	
	protected void doSave() {
		if (!cekValidity()) return;
		super.doSave();
		/*AccountingSQLSAP sql = new AccountingSQLSAP();
		try {
			long index = sql.getMaxIndex(IDBConstants.TABLE_SALES_ADVANCE, m_conn);
			entity().setIndex(index);			
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		disableEditMode();
	}
	
	public void insertUpdate(DocumentEvent e) {
		doUpdate(e);
	}
	
	public void removeUpdate(DocumentEvent e) {
		doUpdate(e);
	}
	
	public void changedUpdate(DocumentEvent e) {
		doUpdate(e);
	}
	
	private void doUpdate(DocumentEvent e) {
		if(e.getDocument()==VATPercentDoc){
			countVAT();
			
			countAmount();
		}
		if(e.getDocument()==Tax23PercentDoc){
			countTax();
			
			countAmount();
		}
		if((e.getDocument()==SalesAdvExchRateDoc) ||
				(e.getDocument()==VATExchRateDoc) ||
				(e.getDocument()==Tax23ExchRateDoc)) {
			countAmountInBaseCurrency();
		}
	}

	protected void clearAll() {
		super.clearAll();
		clearKomponen();
		disableEditMode();
		
	}

	private void clearKomponen() {
		StatusText.setText("");
		SubmittedDateText.setText("");
		RcvToText.setText("");
		SalesAdvCurrComp.setText("");
		SalesAdvExchRateText.setValue(new Double(0));
		VATCurrText.setText("");
		VATExchRateText.setValue(new Double(0));
		Tax23CurrText.setText("");
		Tax23ExchRateText.setValue(new Double(0));
		SalesAdvCurrComp.setText("");
		RcvBaseCurrText.setText("");
		RcvBaseAmountText.setValue(new Double(0));
		AmountRcvCurrText.setText("");
		
		OriginatorComp.setEmployee(null);
		OriginatorComp.setJobTitle("");
		OriginatorComp.setDate(null);
		
		ApprovedComp.setEmployee(null);
		ApprovedComp.setJobTitle("");
		ApprovedComp.setDate(null);
		
		ReceivedComp.setEmployee(null);
		ReceivedComp.setJobTitle("");
		ReceivedComp.setDate(null);
		
		TransactionDateDate.setDate(null);
	}

	public void doEdit() {
		super.doEdit();
		entity2gui(); // again
	}
}
