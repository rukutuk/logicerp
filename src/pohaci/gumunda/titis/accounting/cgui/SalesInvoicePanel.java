package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.Document;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.cgui.report.Invoice;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.SalesAdvance;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.SalesInvoiceLoader;
import pohaci.gumunda.titis.accounting.entity.SalesItem;
import pohaci.gumunda.titis.accounting.entity.SalesItemDetail;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.ProjectContract;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.ProjectDataPicker;

public class SalesInvoicePanel extends RevTransactionPanel implements ActionListener, PropertyChangeListener, DocumentListener{
	private static final long serialVersionUID = 1L;
	private SalesInvoice m_entity;
	private Document VATPercentDoc;
	private Document ExchRateDoc;	
	private Employee defaultAuthorized;	
	private Hashtable detailDescList;
	int m_editedIndex=-1;
	
	public SalesInvoicePanel(Connection conn, long sessionid) {
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
		setEntity(new SalesInvoice());
		m_entityMapper = MasterMap.obtainMapperFor(SalesInvoice.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();        
		setNullList();
	}
	
	private void setNullList() {
		detailDescList = new Hashtable();
	}
	
	private void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.SALES_INVOICE, Signature.SIGN_AUTHORIZED);
		if(sign!=null)
			defaultAuthorized = sign.getFullEmployee();
	}
	
	private void enableAwal() {
		disableSomeComponents();
	}
	
	private void disableSomeComponents() {
		StatusText.setEditable(false);
		SubmittedDateText.setEditable(false);
		CurrComp.setEditable(false);
		cashBankCombo.setEditable(false);
		DownPaymentCurrText.setEditable(false);
		VATCurrText.setEditable(false);
		//VATAmountText.setEditable(false);
		SubtotalCurrText.setEditable(false);
		SubtotalAmountText.setEditable(false);
		VATBaseCurrText.setEditable(false);
		VATBaseAmountText.setEditable(false);
		TotalCurrText.setEditable(false);
		TotalAmountText.setEditable(false);
		
		RefNoText.setEditable(false);
		CustomerText.setEditable(false);
		WorkDescTextArea.setEditable(false);
		UnitCodeText.setEditable(false);
		ActivityCodeText.setEditable(false);
		DepartmentText.setEditable(false);
		ORNoText.setEditable(false);
		ContractNoText.setEditable(false);
		IPCNoText.setEditable(false);
	}
	
	private void initComponents() {
		AccountComp = new LookupBankAccountPicker(m_conn, m_sessionid);
		ProjectCodeComp = new ProjectDataPicker(m_conn, m_sessionid);
		AuthorizationComp = new AssignPanel(m_conn, m_sessionid,"Authorization");
		DownPaymentComp = new LookupDownPaymentPicker(m_conn, m_sessionid);
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
		extLeftRightPanel = new javax.swing.JPanel();
		jPanel1_2_2 = new javax.swing.JPanel();
		leftPanel = new javax.swing.JPanel();
		currencyPanel = new javax.swing.JPanel();
		CurrComp = new javax.swing.JTextField();
		ExchRateLabel = new javax.swing.JLabel();
		ExchRateText = new JFormattedTextField(m_numberFormatter);
		downPaymentPanel = new javax.swing.JPanel();
		DownPaymentCurrText = new javax.swing.JTextField();
		vatPanel = new javax.swing.JPanel();
		VATPercentText = new JFormattedTextField(m_numberFormatter);
		CurrencyLabel6 = new javax.swing.JLabel();
		VATCurrText = new javax.swing.JTextField();
		VATAmountText = new JFormattedTextField(m_numberFormatter);
		inBaseCurrPanel = new javax.swing.JPanel();
		SubTotalLabel = new javax.swing.JLabel();
		VATLabel2 = new javax.swing.JLabel();
		SubtotalCurrText = new javax.swing.JTextField();
		VATBaseCurrText = new javax.swing.JTextField();
		SubtotalAmountText = new JFormattedTextField(m_numberFormatter);
		VATBaseAmountText = new JFormattedTextField(m_numberFormatter);
		TotalLabel = new javax.swing.JLabel();
		TotalCurrText = new javax.swing.JTextField();
		TotalAmountText = new JFormattedTextField(m_numberFormatter);
		SalesInBaseCurrentLabel = new javax.swing.JLabel();
		jSeparator1 = new javax.swing.JSeparator();
		StatusLabel = new javax.swing.JLabel();
		SubmittedDateLabel = new javax.swing.JLabel();
		AccountLabel = new javax.swing.JLabel();
		DownPaymentLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScroll = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		CurrencyLabel = new javax.swing.JLabel();
		VATLabel = new javax.swing.JLabel();
		rightpanel = new javax.swing.JPanel();
		InvoiceNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		InvoiceDateLabel = new javax.swing.JLabel();
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
		BriefDescLabel = new javax.swing.JLabel();
		BriefDescScroll = new javax.swing.JScrollPane();
		BriefDescTextArea = new javax.swing.JTextArea();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel2 = new javax.swing.JPanel();
		jPanel2_1 = new javax.swing.JPanel();
		ButtomButtonPanel = new javax.swing.JPanel();
		AddInvoiceItemBtn = new javax.swing.JButton();
		DeleteInvoiceItemBtn = new javax.swing.JButton();
		DetailDescription = new javax.swing.JButton();
		InvoiceItemScrollPane = new javax.swing.JScrollPane();
		cashBankCombo = new JComboBox(new String[]{"Cash","Bank"});
		
		AttentionTxt = new JTextField();
		
		InvoiceItemTable = new ItemTable();
		
		setLayout(new java.awt.BorderLayout());
		
		setPreferredSize(new java.awt.Dimension(700, 675));
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		jPanel1.setPreferredSize(new java.awt.Dimension(650, 470));
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
		
		extLeftRightPanel.setLayout(new java.awt.BorderLayout());
		
		extLeftRightPanel.setPreferredSize(new java.awt.Dimension(650, 370));
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(325, 335));
		leftPanel.setLayout(new java.awt.GridBagLayout());
		
		leftPanel.setPreferredSize(new java.awt.Dimension(440, 230));
		
		currencyPanel.setLayout(new java.awt.GridBagLayout());		
		currencyPanel.setPreferredSize(new java.awt.Dimension(290, 20));
		CurrComp.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		currencyPanel.add(CurrComp, gridBagConstraints);
		
		ExchRateLabel.setText("Exch. Rate");
		ExchRateLabel.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		currencyPanel.add(ExchRateLabel, gridBagConstraints);
		
		ExchRateText.setPreferredSize(new java.awt.Dimension(172, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		currencyPanel.add(ExchRateText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(currencyPanel, gridBagConstraints);
		
		downPaymentPanel.setLayout(new java.awt.GridBagLayout());		
		downPaymentPanel.setPreferredSize(new java.awt.Dimension(290, 20));
		DownPaymentCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		downPaymentPanel.add(DownPaymentCurrText, gridBagConstraints);
		
		DownPaymentComp.setPreferredSize(new java.awt.Dimension(237, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		downPaymentPanel.add(DownPaymentComp, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(downPaymentPanel, gridBagConstraints);
		
		vatPanel.setLayout(new java.awt.GridBagLayout());		
		vatPanel.setPreferredSize(new java.awt.Dimension(	290, 23));
		VATPercentText.setPreferredSize(new java.awt.Dimension(30, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		vatPanel.add(VATPercentText, gridBagConstraints);
		
		CurrencyLabel6.setText("%");
		CurrencyLabel6.setPreferredSize(new java.awt.Dimension(20, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		vatPanel.add(CurrencyLabel6, gridBagConstraints);
		
		VATCurrText.setPreferredSize(new java.awt.Dimension(44, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 3);
		vatPanel.add(VATCurrText, gridBagConstraints);
		
		VATAmountText.setPreferredSize(new java.awt.Dimension(188, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		vatPanel.add(VATAmountText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(vatPanel, gridBagConstraints);
		
		inBaseCurrPanel.setLayout(new java.awt.GridBagLayout());		
		inBaseCurrPanel.setPreferredSize(new java.awt.Dimension(410, 80));
		SubTotalLabel.setText("Subtotal");
		SubTotalLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		inBaseCurrPanel.add(SubTotalLabel, gridBagConstraints);
		
		VATLabel2.setText("VAT");
		VATLabel2.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		inBaseCurrPanel.add(VATLabel2, gridBagConstraints);
		
		SubtotalCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		inBaseCurrPanel.add(SubtotalCurrText, gridBagConstraints);
		
		VATBaseCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		inBaseCurrPanel.add(VATBaseCurrText, gridBagConstraints);
		
		SubtotalAmountText.setPreferredSize(new java.awt.Dimension(240, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		inBaseCurrPanel.add(SubtotalAmountText, gridBagConstraints);
		
		VATBaseAmountText.setPreferredSize(new java.awt.Dimension(240, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		inBaseCurrPanel.add(VATBaseAmountText, gridBagConstraints);
		
		TotalLabel.setText("Total");
		TotalLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		inBaseCurrPanel.add(TotalLabel, gridBagConstraints);
		
		TotalCurrText.setPreferredSize(new java.awt.Dimension(48, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		inBaseCurrPanel.add(TotalCurrText, gridBagConstraints);
		
		TotalAmountText.setPreferredSize(new java.awt.Dimension(240, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		inBaseCurrPanel.add(TotalAmountText, gridBagConstraints);
		
		SalesInBaseCurrentLabel.setText("Sales in Base Currency");
		SalesInBaseCurrentLabel.setPreferredSize(new java.awt.Dimension(115, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		inBaseCurrPanel.add(SalesInBaseCurrentLabel, gridBagConstraints);
		
		jSeparator1.setPreferredSize(new java.awt.Dimension(190, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		inBaseCurrPanel.add(jSeparator1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		leftPanel.add(inBaseCurrPanel, gridBagConstraints);
		
		AuthorizationComp.setLayout(new java.awt.GridBagLayout());		
		AuthorizationComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Authorization"));
		AuthorizationComp.setOpaque(false);
		AuthorizationComp.setPreferredSize(new java.awt.Dimension(410, 95));		
		AuthorizationComp.m_employeePicker.setPreferredSize(new Dimension(295,18));
		AuthorizationComp.m_jobTextField.setPreferredSize(new Dimension(235,18));
		AuthorizationComp.m_datePicker.setPreferredSize(new Dimension(235,18));
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		leftPanel.add(AuthorizationComp, gridBagConstraints);
		
		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(StatusLabel, gridBagConstraints);
		
		SubmittedDateLabel.setText("Submitted Date");
		SubmittedDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(SubmittedDateLabel, gridBagConstraints);
		
		JLabel receiptFromLabel = new JLabel();
		receiptFromLabel.setText("Receive To");
		receiptFromLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(receiptFromLabel, gridBagConstraints);
		
		AccountLabel.setText("Account*");
		AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(AccountLabel, gridBagConstraints);
		
		DownPaymentLabel.setText("Down Payment");
		DownPaymentLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(DownPaymentLabel, gridBagConstraints);
		
		cashBankCombo.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(cashBankCombo, gridBagConstraints);
		
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(AccountComp, gridBagConstraints);
		
		StatusText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(StatusText, gridBagConstraints);
		
		SubmittedDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(SubmittedDateText, gridBagConstraints);
		
		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(DescriptionLabel, gridBagConstraints);
		
		DescriptionScroll.setPreferredSize(new java.awt.Dimension(290, 50));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScroll.setViewportView(DescriptionTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(new JLabel("Attention"), gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(DescriptionScroll, gridBagConstraints);       
		
		AttentionTxt.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;        
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(AttentionTxt, gridBagConstraints);
		
		CurrencyLabel.setText("Currency*");
		CurrencyLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(CurrencyLabel, gridBagConstraints);
		
		VATLabel.setText("VAT");
		VATLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(VATLabel, gridBagConstraints);
		
		extLeftRightPanel.add(leftPanel, java.awt.BorderLayout.WEST);
		rightpanel.setLayout(new java.awt.GridBagLayout());		
		rightpanel.setPreferredSize(new java.awt.Dimension(425, 200));
		InvoiceNoLabel.setText("Invoice No");
		InvoiceNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(InvoiceNoLabel, gridBagConstraints);
		
		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(RefNoText, gridBagConstraints);
		
		InvoiceDateLabel.setText("Invoice Date*");
		InvoiceDateLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(InvoiceDateLabel, gridBagConstraints);
		
		ProjectCodeLabel.setText("Project Code*");
		ProjectCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(ProjectCodeLabel, gridBagConstraints);
		
		CustomerLabel.setText("Customer");
		CustomerLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(CustomerLabel, gridBagConstraints);
		
		WorkDescLabel.setText("Work Desc. ");
		WorkDescLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(WorkDescLabel, gridBagConstraints);
		
		ProjectCodeComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(ProjectCodeComp, gridBagConstraints);
		
		CustomerText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(CustomerText, gridBagConstraints);
		
		WorkDescScroll.setPreferredSize(new java.awt.Dimension(290, 55));
		WorkDescTextArea.setColumns(20);
		WorkDescTextArea.setLineWrap(true);
		WorkDescTextArea.setRows(5);
		WorkDescScroll.setViewportView(WorkDescTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(WorkDescScroll, gridBagConstraints);
		
		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(UnitCodeLabel, gridBagConstraints);
		
		UnitCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(UnitCodeText, gridBagConstraints);
		
		ActivityCodeLabel.setText("Activity Code");
		ActivityCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(ActivityCodeLabel, gridBagConstraints);
		
		ActivityCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(ActivityCodeText, gridBagConstraints);
		
		DepartmentLabel.setText("Department");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(DepartmentLabel, gridBagConstraints);
		
		ORNoLabel.setText("O.R No");
		ORNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(ORNoLabel, gridBagConstraints);
		
		DepartmentText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(DepartmentText, gridBagConstraints);
		
		ORNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(ORNoText, gridBagConstraints);
		
		SOPOContractNo.setText("SO/PO/Contract No");
		SOPOContractNo.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(SOPOContractNo, gridBagConstraints);
		
		IPCNoLabel.setText("IPC No");
		IPCNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(IPCNoLabel, gridBagConstraints);
		
		ContractNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(ContractNoText, gridBagConstraints);
		
		IPCNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(IPCNoText, gridBagConstraints);
		
		RemarkLabel.setText("Remarks");
		RemarkLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(RemarkLabel, gridBagConstraints);
		
		RemarkScroll.setPreferredSize(new java.awt.Dimension(290, 65));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarkScroll.setViewportView(RemarksTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(RemarkScroll, gridBagConstraints);
		
		BriefDescLabel.setText("Brief Desc.");
		BriefDescLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(BriefDescLabel, gridBagConstraints);
		
		BriefDescScroll.setPreferredSize(new java.awt.Dimension(290, 65));
		BriefDescTextArea.setColumns(20);
		BriefDescTextArea.setLineWrap(true);
		BriefDescTextArea.setRows(5);
		BriefDescScroll.setViewportView(BriefDescTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightpanel.add(BriefDescScroll, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		rightpanel.add(TransactionDateDate, gridBagConstraints);
		
		jPanel1_2_2.add(rightpanel, java.awt.BorderLayout.WEST);
		
		extLeftRightPanel.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(extLeftRightPanel, java.awt.BorderLayout.CENTER);
		
		add(jPanel1, java.awt.BorderLayout.NORTH);
		
		jPanel2.setLayout(new java.awt.BorderLayout());
		
		jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		jPanel2.setPreferredSize(new java.awt.Dimension(650, 250));
		jPanel2_1.setLayout(new java.awt.BorderLayout());
		
		jPanel2_1.setPreferredSize(new java.awt.Dimension(650, 35));
		ButtomButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));
		
		ButtomButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
		AddInvoiceItemBtn.setText("Add");
		AddInvoiceItemBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		AddInvoiceItemBtn.setPreferredSize(new java.awt.Dimension(50, 21));
		ButtomButtonPanel.add(AddInvoiceItemBtn);
		
		DeleteInvoiceItemBtn.setText("Delete");
		DeleteInvoiceItemBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		DeleteInvoiceItemBtn.setPreferredSize(new java.awt.Dimension(50, 21));
		ButtomButtonPanel.add(DeleteInvoiceItemBtn);
		
		/*DetailDescription.setText("Detail Desc");
		 DetailDescription.setMargin(new java.awt.Insets(2, 2, 2, 2));
		 DetailDescription.setPreferredSize(new java.awt.Dimension(75, 21));
		 ButtomButtonPanel.add(DetailDescription);*/
		
		jPanel2_1.add(ButtomButtonPanel, java.awt.BorderLayout.WEST);
		
		jPanel2.add(jPanel2_1, java.awt.BorderLayout.NORTH);
		
		InvoiceItemScrollPane.setPreferredSize(new java.awt.Dimension(650, 215));
		
		InvoiceItemScrollPane.setViewportView(InvoiceItemTable);
		
		jPanel2.add(InvoiceItemScrollPane, java.awt.BorderLayout.CENTER);
		
		add(jPanel2, java.awt.BorderLayout.CENTER);
	}
	
	private void addingListener(){    	
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);	    	
		ProjectCodeComp.addPropertyChangeListener(this);    	
		DownPaymentComp.addPropertyChangeListener(this);
		VATPercentText.addPropertyChangeListener("value", this);
		VATAmountText.addPropertyChangeListener("value", this);
		ExchRateText.addPropertyChangeListener("value", this);    	
		AddInvoiceItemBtn.addActionListener(this);
		DeleteInvoiceItemBtn.addActionListener(this);
		DetailDescription.addActionListener(this);
		TransactionDateDate.addPropertyChangeListener("date", this);
		cashBankCombo.addActionListener(this);
	}  
	
	/*private int countDefaultDigit(){
		SalesItem[] items = entity().getSalesItem();
		int max = 0;
		NumberRounding nr0 = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND_DOWN, 0);
		NumberRounding nr4 = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND_DOWN, 4);
		for (int i=0; i<items.length; i++) {
			BigDecimal qtyD = new BigDecimal(items[i].getQty());
			int x = qtyD.scale();
			qtyD.setScale(2, BigDecimal.ROUND_FLOOR);
			BigInteger qt = qtyD.unscaledValue();
			BigDecimal intL = new BigDecimal(qtyD.unscaledValue());
			
			double qty = items[i].getQty();
			long intValue = (long) Math.floor(qty);
			int x = (int) Math.rint(qty);
			double decimals = qty - intValue; // cari bagian decimalnya saja
			decimals = nr4.round(decimals);
			
			double decimals = qtyD.doubleValue() - intL.doubleValue();
			
			int countDecimal = countDigits(decimals);
			
			max = Math.max(max, countDecimal);
		}
		
		return max;
	}
	*/
/*	private int countDigits(double qty) {
		String qtyS = String.valueOf(qty);
		return 0;
	}
*/
	/*private int countDigit(double decimals) {
		BigDecimal TEN_POWER[] = {BigDecimal.valueOf(1), BigDecimal.valueOf(10), 
					BigDecimal.valueOf(100), BigDecimal.valueOf(1000), BigDecimal.valueOf(10000)};
		
		BigDecimal decimalsBig = new BigDecimal(decimals);
		decimalsBig.setScale()
			
		NumberRounding nr0 = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND_DOWN, 0);
		for (int i=0; i<=4; i++) {
			BigDecimal val = decimalsBig.multiply(TEN_POWER[i]);
			
			NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND_DOWN, 4-i);
			BigInteger intVal = val.toBigInteger();
			
		}
		return 4;
	}*/

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_printViewRefNoBtn) {    		  
			if (m_entity.getIndex()>0){
				int defaultDigit = 0;
				SalesInvoiceRptDlg dlg = new SalesInvoiceRptDlg(defaultDigit);
				dlg.setVisible(true); 
				
				if (dlg.getResponse() == JOptionPane.OK_OPTION) {
					int digit = dlg.getDigit();
					String moneyLanguage = dlg.getLanguage(); 
					
					if (digit < defaultDigit) {
						int resp = JOptionPane.showConfirmDialog(this, 
								"The Data contains quantity which decimal digit longer than you set.\n" +
								"It may cause that amount is not consistent with its quantity.\n" +
								"Are you sure to continue print out Invoice?", 
								"Confirmation", JOptionPane.YES_NO_OPTION);
						if (resp == JOptionPane.OK_OPTION)
							new Invoice(m_entity, m_conn, digit,moneyLanguage);
					}else
						new Invoice(m_entity, m_conn, digit,moneyLanguage);
				}
			}else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}		
		}
		if(e.getSource() == m_searchRefNoBtn) {
			SearchInvoiceDialog dlg = new SearchInvoiceDialog(GumundaMainFrame.getMainFrame(), "Search Invoice", m_conn, m_sessionid,new SalesInvoiceLoader(m_conn,SalesInvoice.class));
			dlg.setVisible(true);
			if (dlg.selectedObj != null) {
				doLoad(dlg.selectedObj);
			}
		}
		if(e.getSource()==AddInvoiceItemBtn){
			InvoiceItemTable.addNewRow();
			m_editedIndex = (InvoiceItemTable.getRowCount()-6)-1;
			InvoiceItemTable.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
		}
		if(e.getSource()==DeleteInvoiceItemBtn){
			int row = -1;
			row = InvoiceItemTable.getSelectedRow();
			int maxRow = InvoiceItemTable.getRowCount()-1;
			if(row>-1){
				if(row<(maxRow-5)){
					InvoiceItemTable.deleteRow(row);
				}
			}	
		}
		if(e.getSource()==DetailDescription){
			int row = -1;
			row = InvoiceItemTable.getSelectedRow();
			int maxRow = InvoiceItemTable.getRowCount()-1;
			if(row>-1){
				if(row<(maxRow-5)){
					
					//Integer no = (Integer)InvoiceItemTable.getValueAt(row, 0);
					int no = InvoiceItemTable.getSelectedRow();
					
					ArrayList list = (ArrayList) detailDescList.get(new Integer(no));
					
					SalesInvoiceItemDetailDescDialog dlg =
						new SalesInvoiceItemDetailDescDialog(GumundaMainFrame.getMainFrame(),
								m_conn, m_sessionid, list);
					dlg.setVisible(true);
					
					if(dlg.getResponse()==JOptionPane.OK_OPTION){
						detailDescList.put(new Integer(no), dlg.getDetailDescList());
					}
				}
			}
		}
		if (e.getSource() == cashBankCombo) {
			changeBankAccount();
		}
	}   
	
	private LookupPicker AccountComp;
	private javax.swing.JLabel AccountLabel;
	private javax.swing.JLabel ActivityCodeLabel;
	private javax.swing.JTextField ActivityCodeText;
	private javax.swing.JButton AddInvoiceItemBtn;
	private javax.swing.JLabel BriefDescLabel;
	private javax.swing.JScrollPane BriefDescScroll;
	private javax.swing.JTextArea BriefDescTextArea;
	private javax.swing.JPanel ButtomButtonPanel;
	private javax.swing.JTextField ContractNoText;
	private javax.swing.JTextField CurrComp;
	private javax.swing.JLabel CurrencyLabel;
	private javax.swing.JLabel CurrencyLabel6;
	private javax.swing.JLabel CustomerLabel;
	private javax.swing.JTextField CustomerText;
	private javax.swing.JButton DeleteInvoiceItemBtn;
	private javax.swing.JLabel DepartmentLabel;
	private javax.swing.JTextField DepartmentText;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScroll;
	private javax.swing.JTextArea DescriptionTextArea;
	private JComboBox cashBankCombo;
	private javax.swing.JButton DetailDescription;
	private LookupDownPaymentPicker DownPaymentComp;
	private javax.swing.JTextField DownPaymentCurrText;
	private javax.swing.JLabel DownPaymentLabel;
	private javax.swing.JLabel ExchRateLabel;
	private javax.swing.JFormattedTextField ExchRateText;
	private javax.swing.JLabel IPCNoLabel;
	private javax.swing.JTextField IPCNoText;
	private javax.swing.JLabel InvoiceDateLabel;
	private javax.swing.JLabel InvoiceNoLabel;
	private javax.swing.JLabel ORNoLabel;
	private javax.swing.JTextField ORNoText;
	private javax.swing.JScrollPane InvoiceItemScrollPane;
	private ItemTable InvoiceItemTable;
	private ProjectDataPicker ProjectCodeComp;
	private javax.swing.JLabel ProjectCodeLabel;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarkLabel;
	private javax.swing.JScrollPane RemarkScroll;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel SOPOContractNo;
	private JFormattedTextField SubtotalAmountText;
	private javax.swing.JTextField SubtotalCurrText;
	private javax.swing.JTextField TotalCurrText;
	private JFormattedTextField TotalAmountText;
	private JFormattedTextField VATBaseAmountText;
	private javax.swing.JTextField VATBaseCurrText;
	private javax.swing.JLabel SalesInBaseCurrentLabel;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubTotalLabel;
	private javax.swing.JTextField SubmittedDateText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JPanel TopButtonPanel;
	private javax.swing.JLabel TotalLabel;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JLabel UnitCodeLabel;
	private javax.swing.JTextField UnitCodeText;
	private javax.swing.JFormattedTextField VATAmountText;
	private javax.swing.JTextField VATCurrText;
	private javax.swing.JLabel VATLabel;
	private javax.swing.JLabel VATLabel2;
	private javax.swing.JFormattedTextField VATPercentText;
	private javax.swing.JLabel WorkDescLabel;
	private javax.swing.JScrollPane WorkDescScroll;
	private javax.swing.JTextArea WorkDescTextArea;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel extLeftRightPanel;
	private javax.swing.JPanel jPanel1_2_2;
	private javax.swing.JPanel leftPanel;
	private javax.swing.JPanel currencyPanel;
	private javax.swing.JPanel downPaymentPanel;
	private javax.swing.JPanel vatPanel;
	private javax.swing.JPanel inBaseCurrPanel;
	private AssignPanel AuthorizationComp;
	private javax.swing.JPanel rightpanel;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel2_1;
	private javax.swing.JSeparator jSeparator1;
	JTextField AttentionTxt;
	
	ArrayList validityMsgs = new ArrayList();
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	private SalesInvoice entity() {
		return m_entity;
	}
	
	protected boolean cekValidity() {
		validityMsgs.clear();
		if (AccountComp.getObject()== null)
			addInvalid("Bank Account must be selected");
		if (TransactionDateDate.getDate() == null)
			addInvalid("Invoice date must be inserted");
		if (ProjectCodeComp.getObject() == null)
			addInvalid("Project Code must be selected");
		/*else{
		 ProjectData projectData = (ProjectData) ProjectCodeComp.getObject();
		 ProjectContract contract = projectData.getProjectContract();
		 if(contract==null)
		 addInvalid("Selected Project Code has no Project Contract");
		 }*/
		if (DescriptionTextArea.getText().equals(""))
			addInvalid("Description must be selected");
		SalesItem[] si = InvoiceItemTable.getSalesItem();
		if(si.length==0)
			addInvalid("Item must be inserted");
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
		ProjectData project = (ProjectData) ProjectCodeComp.getObject();		
		if(project!=null){
			project = getRealProject(project.getIndex());			
			//project.setProjectContract();
			entity().setProject(project);
			entity().setActivity(project.getActivity());
			ProjectContract projContract = getProjectContract(project);
			Currency curr = null;
			if (projContract!=null)
				curr = projContract.getCurrency();
			else 
				curr = baseCurrency;
			
			entity().setCurrency(curr);
			entity().setCustomer(project.getCustomer());
			entity().setDepartment(project.getDepartment());
			entity().setSalesCurr(curr);
			entity().setUnit(project.getUnit());
			entity().setVatCurr(curr);
			entity().setDownPaymentCurr(curr);
		}
		entity().setReceiveFrom((String)cashBankCombo.getSelectedItem());
		if (cashBankCombo.getSelectedItem().equals("Cash")){
			entity().setCashAccount((CashAccount) AccountComp.getObject());
			entity().setBankAccount(null);
		}else {
			entity().setCashAccount(null);
			entity().setBankAccount((BankAccount)AccountComp.getObject());	
		}
		entity().setBriefDesc(BriefDescTextArea.getText());		
		entity().setEmpAuthorize(AuthorizationComp.getEmployee());
		entity().setDateAuthorize(AuthorizationComp.getDate());
		entity().setDescription(DescriptionTextArea.getText());
		entity().setAttention(AttentionTxt.getText());
		Object obj = DownPaymentComp.getObject();
		if(obj!=null){
			if (obj instanceof SalesAdvance){
				SalesAdvance sa = (SalesAdvance) obj;
				entity().setSalesAdvance(sa);
				entity().setBeginningBalance(null);
				entity().setDownPaymentAmount(sa.getSalesAdvAmount());
			} else {
				BeginningAccountReceivable bar = (BeginningAccountReceivable) obj;
				entity().setSalesAdvance(null);
				entity().setBeginningBalance(bar);
				entity().setDownPaymentAmount(bar.getAccValue());
			}
		}
		entity().setEmpAuthorize(AuthorizationComp.getEmployee());		
		if(ExchRateText.getValue()!=null){
			double val = ((Number)ExchRateText.getValue()).doubleValue();
			entity().setExchangeRate(val);
			entity().setSalesExchRate(val);
			entity().setVatExchRate(val);
		}		
		entity().setJobTitleAuthorize(AuthorizationComp.getJobTitle());		
		entity().setRemarks(RemarksTextArea.getText());		
		Number vat = (Number)VATAmountText.getValue();
		double vatAmt = 0;
		if(vat!=null)
			vatAmt = vat.doubleValue();
		entity().setVatAmount(vatAmt);		
		if(VATPercentText.getValue()!=null)
			entity().setVatPercent(((Number)VATPercentText.getValue()).doubleValue());
		entity().setSalesItem(InvoiceItemTable.getSalesItem());		
		entity().setTransactionDate(TransactionDateDate.getDate());		
		entity().setSalesAmount(InvoiceItemTable.getSales());	
		
	}
	
	protected void entity2gui() {
		InvoiceItemTable.clear();
		detailDescList.clear();
		StatusText.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");
		RefNoText.setText(entity().getReferenceNo());
		
		if(entity().getTransactionDate()!=null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else
			TransactionDateDate.setDate(new Date());
		
		ProjectCodeComp.setObject(entity().getProject());
		
		if (entity().getReceiveFrom()!=null)
			cashBankCombo.setSelectedItem(entity().getReceiveFrom());
		else
			cashBankCombo.setSelectedItem("Cash");
		
		AccountComp.setObject(entity().igetCashBankAccount());
		
		if (entity().getSalesAdvance()!=null)
			DownPaymentComp.setObject(entity().getSalesAdvance());
		else if (entity().getBeginningBalance()!=null)
			DownPaymentComp.setObject(entity().getBeginningBalance());
		
		ExchRateText.setValue(new Double(entity().getSalesExchRate()));
		
		VATPercentText.setValue(new Double(entity().getVatPercent()));	
		
		AuthorizationComp.setEmployee(entity().getEmpAuthorize());
		AuthorizationComp.setJobTitle(entity().getJobTitleAuthorize());
		AuthorizationComp.setDate(entity().getDateAuthorize());		
		DescriptionTextArea.setText(entity().getDescription());
		RemarksTextArea.setText(entity().getRemarks());
		BriefDescTextArea.setText(entity().getBriefDesc());
		AttentionTxt.setText(entity().getAttention());
		
		SubtotalCurrText.setText(baseCurrency.getSymbol());
		VATBaseCurrText.setText(baseCurrency.getSymbol());
		TotalCurrText.setText(baseCurrency.getSymbol());		
		
		if(entity().getSalesItem().length>0){
			SalesItem[] salesItems = entity().getSalesItem();
			for(int i=0; i<salesItems.length; i++){
				SalesItem salesItem = salesItems[i];				
				if(salesItem!=null){
					InvoiceItemTable.addRow(salesItem,i);					
					if(salesItem.igetSalesItemDetails()!=null){
						if(salesItem.igetSalesItemDetails().length>0){
							SalesItemDetail[] dets = salesItem.igetSalesItemDetails();							
							List detailDescs = new ArrayList();
							int max = dets.length;
							for(int j=0; j<max; j++){
								ArrayList list = new ArrayList();
								list.add(dets[j].getNumber());
								list.add(dets[j].getSpecification());
								list.add(dets[j].getDescription());
								list.add(new Double(dets[j].getQty()));
								list.add(new Double(dets[j].getUnitPrice()));
								list.add(new Double(dets[j].getAmount()));								
								Object[] rowData = (Object[]) list.toArray(new Object[list.size()]);
								detailDescs.add(rowData);								
								detailDescList.put(new Integer(i),detailDescs);
							}	
						}
					}
				}
			}
		}
		InvoiceItemTable.updateSummary();
		VATAmountText.setValue(new Double(entity().getVatAmount()));
		InvoiceItemTable.updateTotalSummary();
		
		
		updateSalesInBaseCurrency();
	}
	
	private SalesItem[] getSalesItemByInvoice(long index) {
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesItem.class);
		mapper.setActiveConn(m_conn);
		
		List list = mapper.doSelectWhere(IDBConstants.ATTR_SALES_INVOICE + "=" + index);
		
		SalesItem[] salesItems = (SalesItem[]) list.toArray(new SalesItem[list.size()]);
		
		return salesItems;
	}
	
	private void inEditMode(boolean edit) {
		cashBankCombo.setEnabled(edit);
		AccountComp.setEnabled(edit);
		VATPercentText.setEditable(edit);
		AuthorizationComp.setEnabled(edit);
		DescriptionTextArea.setEditable(edit);
		TransactionDateDate.setEditable(edit);
		ProjectCodeComp.setEnabled(edit);
		RemarksTextArea.setEditable(edit);
		BriefDescTextArea.setEditable(edit);		
		DownPaymentComp.setEnabled(edit);		
		AddInvoiceItemBtn.setEnabled(edit);
		DeleteInvoiceItemBtn.setEnabled(edit);
		DetailDescription.setEnabled(edit);
		InvoiceItemTable.setEnabled(edit);
		AttentionTxt.setEditable(edit);
		
		ExchRateText.setEditable(edit);
		
		VATAmountText.setEditable(edit);
	}
	
	protected void disableEditMode() {
		
		boolean edit = false;
		
		inEditMode(edit);
	}
	
	
	protected void enableEditMode() {
		boolean edit = true;
		
		inEditMode(edit);
	}
	
	protected Object createNew() {
		SalesInvoice o = new SalesInvoice();
		o.setEmpAuthorize(defaultAuthorized);
		return o;
	}
	
	void setEntity(Object m_entity) {
		SalesInvoice oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (SalesInvoice)m_entity;		
		if(entity().getSalesItem()==null){
			SalesItem[] salesItems = getSalesItemByInvoice(entity().getIndex());			
			if(salesItems!=null){
				if(salesItems.length>0){
					for(int i=0; i<salesItems.length; i++){
						SalesItem si = salesItems[i];						
						SalesItemDetail[] details = getSalesItemDetailsByItem(si.getIndex());						
						si.isetSalesItemDetails(details);
					}
				}
			}
			
			this.m_entity.setSalesItem(salesItems);
		}		
		this.m_entity.addPropertyChangeListener(this);
	}
	
	private SalesItemDetail[] getSalesItemDetailsByItem(long index) {
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesItemDetail.class);
		mapper.setActiveConn(m_conn);		
		List list = mapper.doSelectWhere(IDBConstants.ATTR_SALES_ITEM + "=" + index);		
		SalesItemDetail[] details = (SalesItemDetail[]) list.toArray(new SalesItemDetail[list.size()]);		
		return details;
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())) {
			readEntityState();
		}
		if ("value".equals(evt.getPropertyName())) {
			if (evt.getSource() == VATPercentText) {
				updateVAT();
			}
			if (evt.getSource() == ExchRateText) {
				updateSalesInBaseCurrency();
			}
			
			if (evt.getSource() == VATAmountText) {
				InvoiceItemTable.updateTotalSummary();
			}
			
			
		}
		if ("object".equals(evt.getPropertyName())){
			if (evt.getSource() == ProjectCodeComp) {
				onProjectChanged(evt);
				changeBankAccount();
				enableExchRate();
			}
			if (evt.getSource() == DownPaymentComp) {
				InvoiceItemTable.updateSummary();
				updateVAT();			
				InvoiceItemTable.updateTotalSummary();
			}
			
		} 
		if ("date".equals(evt.getPropertyName())){
			if (evt.getSource() == TransactionDateDate) {
				enableExchRate();
			}
		}
	}

	private void changeBankAccount() {
		ProjectData project = (ProjectData) ProjectCodeComp.getObject();
		Currency curr = baseCurrency;
		if(project!=null){
			project = getRealProject(project.getIndex());					
			project.setProjectContract(getProjectContract(project));			
			if(project.getProjectContract()!=null)
				curr = project.getProjectContract().getCurrency();			
		}
		
		leftPanel.remove(AccountComp);
		leftPanel.revalidate();
		leftPanel.repaint();		
		if (cashBankCombo.getSelectedItem().equals("Cash"))
			AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid, curr);
		else 
			AccountComp = new LookupBankAccountPicker(m_conn, m_sessionid, curr);
		
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(AccountComp, gridBagConstraints);
		leftPanel.revalidate();
	}
	
	private void onProjectChanged(PropertyChangeEvent evt) {
		if(evt.getNewValue()!=null){
			ProjectData project = (ProjectData) ProjectCodeComp.getObject();			
			project = getRealProject(project.getIndex());					
			CustomerText.setText(project.getCustname());
			WorkDescTextArea.setText(project.getWorkDescription());
			UnitCodeText.setText(project.getUnit().toString());
			if (project.getActivity()!=null)
				ActivityCodeText.setText(project.getActivity().toString());
			else 
				ActivityCodeText.setText("");
			if (project.getDepartment()!=null)
				DepartmentText.setText(project.getDepartment().toString());
			else 	 
				DepartmentText.setText("");
			ORNoText.setText(project.getORNo());
			ContractNoText.setText(project.getPONo());
			IPCNoText.setText(project.getIPCNo());
			
			ProjectContract projContract = getProjectContract(project);
			
			if (projContract != null) {
				project.setProjectContract(projContract);	
				Currency curr = project.getProjectContract().getCurrency();
				CurrComp.setText(curr.getSymbol());
				DownPaymentCurrText.setText(curr.getSymbol());
				VATCurrText.setText(curr.getSymbol());
				setDefaultExchangeRate(curr);
			}else{
				JOptionPane
				 .showMessageDialog(
				 this,
				 "Project contract of the selected project has not been defined yet!\n"
				 + "System will use base currency as its transactional currency.",
				 "Warning!", JOptionPane.INFORMATION_MESSAGE);
				 
				Currency curr = baseCurrency;
				CurrComp.setText(curr.getSymbol());
				DownPaymentCurrText.setText(curr.getSymbol());
				VATCurrText.setText(curr.getSymbol());
				setDefaultExchangeRate(curr);
			}
			refreshDownPaymentComp(project);
			DownPaymentComp.setObject(null);
			
		}else{
			CustomerText.setText("");
			WorkDescTextArea.setText("");
			UnitCodeText.setText("");
			ActivityCodeText.setText("");
			DepartmentText.setText("");
			ORNoText.setText("");
			ContractNoText.setText("");
			IPCNoText.setText("");
			
			CurrComp.setText("");
		}
	}
	
	private void setDefaultExchangeRate(Currency curr) {
		if(entity().getStatus()>StateTemplateEntity.State.SAVED)
			return;
		
		Date date = TransactionDateDate.getDate();
		
		if (curr == null)
			curr = baseCurrency;
		
		double rate = getDefaultExchangeRate(curr, date);
		ExchRateText.setValue(new Double(rate));
	}
	
	private void enableExchRate() {
		String symbol = CurrComp.getText();
		if(symbol.equals(baseCurrency.getSymbol())){
			ExchRateText.setValue(new Double(1.0));
			ExchRateText.setEditable(false);
		}else{
			//ExchRateText.setValue(new Double(1.0));
			Currency curr = null;
			ProjectData project = (ProjectData) ProjectCodeComp.getObject();
			if (project != null) {
				project = getRealProject(project.getIndex());
				ProjectContract projContract = getProjectContract(project);
				if (projContract != null) {
					curr = projContract.getCurrency();
				}
			}
			setDefaultExchangeRate(curr);
			ExchRateText.setEditable(true);
		}
	}
	
	private void refreshDownPaymentComp(ProjectData projectData) {
		DownPaymentComp.refreshData(projectData);
	}
	
	private ProjectData getRealProject(long index) {
		GenericMapper mapper = MasterMap.obtainMapperFor(ProjectData.class);
		mapper.setActiveConn(m_conn);
		
		ProjectData project = (ProjectData) mapper.doSelectByIndex(new Long(index));		
		return project;
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
	
	public void insertUpdate(DocumentEvent e) {
		documentChange(e);
	}
	
	public void removeUpdate(DocumentEvent e) {
		documentChange(e);
	}
	
	public void changedUpdate(DocumentEvent e) {
		documentChange(e);
	}
	
	private void documentChange(DocumentEvent e) {		
		if(e.getDocument()==VATPercentDoc){
			InvoiceItemTable.updateSummary();
			updateVAT();
		}
		if(e.getDocument()==ExchRateDoc){
			updateSalesInBaseCurrency();
		}
	}
	
	private void updateVAT() {		
		double subtotal = InvoiceItemTable.getSubTotal();
		Number vatPercentNbr = (Number) VATPercentText.getValue();
		double vatPercent = 0;
		if(vatPercentNbr!=null)
			vatPercent = vatPercentNbr.doubleValue();
		double vat = (vatPercent/100) * subtotal;
		
		VATAmountText.setValue(new Double(vat));		
	}
	
	public void updateSalesInBaseCurrency() {
		double subtotal = InvoiceItemTable.getSubTotal();
		double vat = InvoiceItemTable.getVAT();
		double total = InvoiceItemTable.getTotal();
		
		ProjectData project = (ProjectData) ProjectCodeComp.getObject();
		
		if(project!=null){
			project.setProjectContract(getProjectContract(project));
			Currency currency = baseCurrency;
			if(project.getProjectContract()!=null)
				currency = project.getProjectContract().getCurrency();
			
			if(currency.getIndex()==baseCurrency.getIndex()){
				SubtotalAmountText.setValue(new Double(subtotal));
				VATBaseAmountText.setValue(new Double(vat));
				TotalAmountText.setValue(new Double(total));
			}else{
				Number nbr = (Number) ExchRateText.getValue();
				if(nbr!=null){
					double rate = nbr.doubleValue();
					double subtotalBase = subtotal * rate;
					double vatBase = vat * rate;
					double totalBase = total * rate;
					
					SubtotalAmountText.setValue(new Double(subtotalBase));
					VATBaseAmountText.setValue(new Double(vatBase));
					TotalAmountText.setValue(new Double(totalBase));
				}
			}
		}
	}
	
	protected void doSave() {
		if (!cekValidity()) return;
		gui2entity();
		if (hasAnotherUnpostedTransaction()) return;		
		super.doSave();
		//	AccountingSQLSAP sql = new AccountingSQLSAP();
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesItem.class);
		mapper.setActiveConn(this.m_conn);
		
		mapper.doDeleteByColumn(IDBConstants.ATTR_SALES_INVOICE, new Long(entity().getIndex()).toString());
		SalesItem[] salesItems = entity().getSalesItem();
		for(int i=0; i<salesItems.length; i++){
			SalesItem salesItem = salesItems[i];
			salesItem.setSalesInvoice(entity().getIndex());
			
			mapper.doInsert(salesItem);
			//long indexSI = sql.getMaxIndex(IDBConstants.TABLE_SALES_ITEM, m_conn);
			
			SalesItemDetail[] details = salesItem.igetSalesItemDetails();
			
			if(details!=null){
				if(details.length>0){
					GenericMapper mapper2 = MasterMap.obtainMapperFor(SalesItemDetail.class);
					mapper2.setActiveConn(this.m_conn);
					
					//mapper2.doDeleteByColumn(IDBConstants.ATTR_SALES_ITEM, new Long(indexSI).toString());						
					for(int j=0; j<details.length; j++){
						SalesItemDetail det = details[j];
						det.setSalesItem(salesItem.getIndex());
						
						mapper2.doInsert(det);
					}
				}
			}
		}
		
	}
	
	private boolean hasAnotherUnpostedTransaction() {
		SalesAdvance salesAdvanceUsed = entity().getSalesAdvance();
		
		if(salesAdvanceUsed==null)
			return false;
		
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesInvoice.class);
		mapper.setActiveConn(this.m_conn);
		
		String whereClausa = "SALESADVANCE=" + salesAdvanceUsed.getIndex() + " AND STATUS<3";
		if(entity().getIndex()>0)
			whereClausa += " AND AUTOINDEX!=" + entity().getIndex();
		
		List list = mapper.doSelectWhere(whereClausa);
		
		if(list.size()==0)
			return false;
		
		JOptionPane.showMessageDialog(this, "There is a transaction for Sales Advance: " + 
				salesAdvanceUsed.getReferenceNo() + " " +
				"that has not been posted yet.\n" +
		"Please post it before creating a new transaction.");
		return true;
	}
	
	protected class MyTableModelListener implements TableModelListener{
		
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			Object obj = e.getSource();
			if(obj instanceof DefaultTableModel){
				DefaultTableModel model = (DefaultTableModel) obj;
				int maxRow = model.getRowCount();
				if(((row>-1)&&(row<maxRow-5))&&((col==3)||(col==4)||(col==5) )){					
					Double qty = (Double) model.getValueAt(row, 3);
					Double unitPrice = (Double) model.getValueAt(row, 4);
					if((qty!=null)&&(unitPrice!=null)){
						Double persAmt = (Double) model.getValueAt(row, 5);
						double amount = qty.doubleValue() * unitPrice.doubleValue();
						if (persAmt!=null)
							if (persAmt.doubleValue()>0)
								amount = amount*persAmt.doubleValue();
						model.setValueAt(new Double(amount), row, 7);
					}
				}else if((row>-1)&&(row<maxRow-5)&&(col==7)){
					InvoiceItemTable.updateSummary();
					updateVAT();
					InvoiceItemTable.updateTotalSummary();
					
				}
			}
		}
		
	}
	
	protected class ItemTable extends JTable{	
		private static final long serialVersionUID = 1L;
		int lastRow;		
		protected ItemTable(){
			PersonalTableModel model = new PersonalTableModel();
			model.addColumn("No");
			model.addColumn("Description");
			model.addColumn("Specification");
			model.addColumn("Qty");
			model.addColumn("Unit Price");
			model.addColumn("Detail Qty");
			model.addColumn("Detail Spec");
			model.addColumn("Amount");			
			setModel(model);			
			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMinWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);			
			getSelectionModel().addListSelectionListener(model);
			clear();
			getModel().addTableModelListener(new MyTableModelListener());
		}
		
		class PersonalTableModel extends DefaultTableModel implements ListSelectionListener {			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;


			public boolean isCellEditable(int row, int column) {
				if(column>=0)
					if(isSummaryRow(row)){						
						return false;
					}else {
						return true;
					}				
				return false;
			}
			
			
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()){
					int iRowIndex = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();
					if (iRowIndex >=getRowCount()-6)
						getSelectionModel().clearSelection();
				}
			}
		}
		
		public SalesItem[] getSalesItem(){
			int maxRow = getRowCount()-1;
			int maxSalesRow = maxRow - 5;			
			ArrayList list = new ArrayList();
			for(int i=0;i <maxSalesRow; i++){
				//Integer no = (Integer) getValueAt(i, 0);				
				SalesItem salesItem = new SalesItem();
				salesItem.setNumber((String) getValueAt(i, 0));
				salesItem.setDescription((String) getValueAt(i, 1));
				salesItem.setSpecification((String) getValueAt(i, 2));
				Double qty = (Double)getValueAt(i, 3);
				if(qty!=null)
					salesItem.setQty(qty.doubleValue());
				Double unitPrice = (Double)getValueAt(i, 4);
				if(unitPrice!=null)
					salesItem.setUnitPrice(unitPrice.doubleValue());
				Double persAmt = (Double)getValueAt(i, 5);
				if (persAmt!=null)
					salesItem.setPersonAmount(persAmt.doubleValue());
				salesItem.setPersonDesc((String) getValueAt(i, 6));
				Double amt = (Double)getValueAt(i, 7);
				if(amt!=null)
					salesItem.setAmount(amt.doubleValue());				
				List detailList = (List) detailDescList.get(new Integer(i));
				List detailDescList = new ArrayList();
				if(detailList!=null){
					Iterator iterator = detailList.iterator();
					while(iterator.hasNext()){
						SalesItemDetail det = new SalesItemDetail();
						Object[] objs = (Object[]) iterator.next();
						det.setNumber((String) objs[0]);
						det.setDescription((String) objs[1]);
						det.setSpecification((String) objs[2]);
						det.setQty(((Double)objs[3]).doubleValue());
						det.setUnitPrice(((Double)objs[4]).doubleValue());
						det.setAmount(((Double)objs[5]).doubleValue());
						detailDescList.add(det);
					}					
					SalesItemDetail[] details = (SalesItemDetail[]) detailDescList
					.toArray(new SalesItemDetail[detailDescList.size()]);					
					salesItem.isetSalesItemDetails(details);
				}
				list.add(salesItem);
			}			
			return (SalesItem[]) list.toArray(new SalesItem[list.size()]);
		}
		
		public double getSales() {
			int maxRow = getRowCount()-1;
			Double subTotal = (Double)getValueAt(maxRow - 4, 7);
			return subTotal.doubleValue();
		}
		
		public double getSubTotal() {
			int maxRow = getRowCount()-1;
			Double subTotal = (Double)getValueAt(maxRow - 2, 7);
			return subTotal.doubleValue();
		}
		
		public double getTotal() {
			int maxRow = getRowCount()-1;
			Double total = (Double)getValueAt(maxRow, 7);
			return total.doubleValue();
		}
		
		public double getVAT() {
			int maxRow = getRowCount()-1;
			Double vat = (Double)getValueAt(maxRow - 1, 7);
			return vat.doubleValue();
		}
		
		public void fillVAT(double vat) {
			int maxRow = getRowCount()-1;
			setValueAt(new Double(vat), maxRow-1, 7);
			updateSummary();
		}
		
		public void fillDownPayment(double downPayment) {
			int maxRow = getRowCount()-1;
			setValueAt(new Double(downPayment), maxRow-3, 7);
			updateSummary();
		}
		
		public void updateSummary() {
			int maxRow = getRowCount()-1;
			int maxSalesRow = maxRow - 6;
			double sales = 0;
			for(int i=0; i<=maxSalesRow; i++){
				Double amt = (Double) getValueAt(i, 7);
				if(amt!=null)
					sales += amt.doubleValue();
			}
			setValueAt(new Double(sales),maxRow - 4, 7);
			double dp = DownPaymentComp.getAmount();
			setValueAt(new Double(dp), maxRow - 3, 7);
			double subtotal = sales - dp;
			setValueAt(new Double(subtotal), maxRow - 2, 7);
		}
		
		public void updateTotalSummary() {
			int maxRow = getRowCount()-1;
			double subtotal = getSubTotal();
			double vat = 0;
			Number vatNbr = (Number)VATAmountText.getValue();
			if(vatNbr!=null)
				vat = vatNbr.doubleValue();
			
			NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
			subtotal = nr.round(subtotal);
			vat = nr.round(vat);
			
			setValueAt(new Double(vat), maxRow - 1, 7);
			double total = subtotal + vat;
			total = nr.round(total);
			setValueAt(new Double(total), maxRow,7);
			updateSalesInBaseCurrency();
		}
		
		public void deleteRow(int row) {
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);
			lastRow--;
			deleteDetailDescList(row);
			updateNumbering();
			updateSummary();
			updateVAT();
		}
		
		private void deleteDetailDescList(int row) {
			int no = 0;	
			Hashtable detaillist = new Hashtable();
			int length = detailDescList.size();
			for (int i=0;i<length;i++){
				ArrayList iter = (ArrayList)detailDescList.get(new Integer(i));
				if (i==row)
					detailDescList.remove(new Integer(row));					
				else{					
					detaillist.put(new Integer(no++),iter);
				}
			}
			detailDescList.clear();
			detailDescList = detaillist;
		}
		
		private void updateNumbering() {
			for(int i=0; i<lastRow; i++){
				int value = InvoiceItemTable.getSelectedRow();			
				Integer val = new Integer(value);
				if(val.intValue()!=i){
					Object obj = detailDescList.get(val);
					ArrayList list = (ArrayList) obj;
					if(obj!=null){
						detailDescList.remove(val);
						detailDescList.put(new Integer(i), list);
					}
				}
			}
		}
		
		public void clear(){			
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
			model.addRow(new Object[]{null,  null, null, null,null, null,  null});
			model.addRow(new Object[]{null, null, null, null, null, null, "SUBTOTAL", new Double(0)});
			model.addRow(new Object[]{null, null, null, null, null, null, "DOWN PAYMENT", new Double(0)});
			model.addRow(new Object[]{null, null, null, null, null, null, "TOTAL", new Double(0)});
			model.addRow(new Object[]{null, null, null, null, null, null, "VAT", new Double(0)});
			model.addRow(new Object[]{null, null, null, null, null, null, "GRAND TOTAL", new Double(0)});
			lastRow = 0;
		}
		
		public void addNewRow(){
			int oldRow = lastRow;
			DefaultTableModel model = (DefaultTableModel) getModel();
			lastRow++;
			model.insertRow(oldRow, new Object[]{null,null,null,null,null,null,null,null});
		}
		
		public void addRow(SalesItem salesItem,int no){
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.insertRow(lastRow++, new Object[]{salesItem.getNumber(), 
					salesItem.getDescription(), 
					salesItem.getSpecification(),
					new Double(salesItem.getQty()), 
					new Double(salesItem.getUnitPrice()),
					new Double(salesItem.getPersonAmount()),
					salesItem.getPersonDesc(),
					new Double(salesItem.getAmount())});
		}
		
		public TableCellEditor getCellEditor(int row, int column) {
			if(column==3 || column ==4 || column==5 || column==7)
				return new FormattedDoubleCellEditor("###,##0.0000;(###,##0.0000)", JLabel.RIGHT);
			return super.getCellEditor(row, column);
		}
		
		public TableCellRenderer getCellRenderer(int row, int column) {
			if(column>=0){
				if(isSummaryRow(row)){
					if(column==6)
						return new FormattedStandardCellRenderer(Font.BOLD, JLabel.RIGHT);
					if(column==7)
						return new FormattedDoubleCellRenderer("###,##0.00;(###,##0.00)", JLabel.RIGHT, Font.BOLD);
				}else{
					if((column==3)||(column==4))
						return new FormattedDoubleCellRenderer("###,##0.0000;(###,##0.0000)", JLabel.RIGHT, Font.PLAIN);
					if(column==5)
						return new FormattedDoubleCellRenderer("###,##0.00;(###,##0.00)", JLabel.RIGHT, Font.PLAIN);
					if(column==7)
						return new FormattedDoubleCellRenderer("###,##0.00;(###,##0.00)", JLabel.RIGHT, Font.BOLD);
				}
			}
			return new FormattedStandardCellRenderer(Font.PLAIN, JLabel.LEFT);
		}
		
		
		private boolean isSummaryRow(int row) {
			int maxRow = getRowCount()-1;
			return ((row>=(maxRow-5))&&(row<=maxRow));
		}
		
		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}
	}
	
	/*protected class FormattedDoubleCellEditor extends AbstractCellEditor implements TableCellEditor{
	 private static final long serialVersionUID = 1L;
	 private int alignment = JLabel.LEFT;
	 private JFormattedTextField textField;
	 
	 public FormattedDoubleCellEditor(int alignment){
	 this.alignment = alignment;			
	 EmptyNumberFormatter formatter = new EmptyNumberFormatter(new Double(0));
	 formatter.setAllowsInvalid(false);
	 DecimalFormat decformat = (DecimalFormat) NumberFormat.getInstance();
	 decformat.applyPattern("#,##0.00");
	 decformat.setMaximumFractionDigits(2);
	 formatter.setFormat(decformat);
	 textField = new JFormattedTextField(formatter);
	 }
	 
	 public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {		
	 textField.setValue(value);
	 textField.setHorizontalAlignment(alignment);			
	 return textField;
	 }
	 
	 public Object getCellEditorValue() {
	 return textField.getValue();
	 }
	 }*/
	
	/*protected class StandardFormatCellRenderer extends DefaultTableCellRenderer {		
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
	 }*/
	
	
	protected void clearAll() {
		super.clearAll();
		clearComponents();
		disableEditMode();
	}
	
	private void clearComponents() {
		StatusText.setText("");
		SubmittedDateText.setText("");		
		ExchRateText.setValue(new Double(0.0));
		DownPaymentCurrText.setText("");
		VATCurrText.setText("");		
		SubtotalCurrText.setText("");
		VATBaseCurrText.setText("");
		TotalCurrText.setText("");		
		SubtotalAmountText.setValue(new Double(0.0));
		VATBaseAmountText.setValue(new Double(0.0));
		TotalAmountText.setValue(new Double(0.0));		
		AuthorizationComp.setEmployee(null);
		AuthorizationComp.setJobTitle("");
		AuthorizationComp.setDate(null);		
		RefNoText.setText("");
		TransactionDateDate.setDate(null);
	}
	
	protected void doNew() {
		clearComponents();
		changeBankAccount();
		super.doNew();
		DownPaymentComp.setObject(null);
		refreshDownPaymentComp(null);
		ExchRateText.setEditable(false);
		isiDefaultAssignPanel();
	}
	
	private void isiDefaultAssignPanel() {
		AuthorizationComp.m_jobTextField.setText(getEmployeeJobTitle(defaultAuthorized));
	}
	
	protected void tableStopCellEditing() {
		InvoiceItemTable.stopCellEditing();
	}
	
	protected void deleteChilds() {
		GenericMapper mapper;
		
		SalesItem[] items = entity().getSalesItem();
		for(int i=0; i<items.length; i++){
			SalesItem item = items[i];
			
			mapper = MasterMap.obtainMapperFor(SalesItemDetail.class);
			mapper.setActiveConn(this.m_conn);
			
			mapper.doDeleteByColumn("SALESITEM", String.valueOf(item.getIndex()));
			
			mapper = MasterMap.obtainMapperFor(SalesItem.class);
			mapper.doDelete(item);
		}
	}
}
