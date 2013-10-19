package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.cgui.report.JasperPurchaseReceipt;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceiptItem;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceiptLoader;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.application.db.*;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.*;

public class PurchaseReceiptPanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener{	
	private static final long serialVersionUID = 1L;
	ArrayList validityMsgs = new ArrayList();
	private PurchaseReceipt m_entity;
	protected Employee m_defaultOriginator,m_defaultApproved,m_defaultReceived;	
	private JTextField m_activityCodeText,m_contractNoText,m_customerText,m_departmentText,m_IPCNoText,m_invoiceNoText,
	m_ORNoText,m_purchaseContractNoText,m_refNoText,m_statusText,m_subTotalAmountText,m_subTotalCurrText,m_submittedDateText,
	m_totaCurrText,m_totalAmountText,m_unitCodeText,m_VATAmountText,m_VATCurrText;	
	
	private JScrollPane m_bankAccountSroll,m_descriptionScroll,m_burchaseItemScrollPane,m_remarkScroll,m_workDescScroll;
	private JTextArea m_bankAccountTextArea,m_descriptionTextArea,m_remarksTextArea,m_workDescTextArea;	
	private JButton m_addPurchaseItemBtn,m_deletePurchaseItemBtn;
	private DatePicker m_dueDateDate,m_invoiceDateDate,m_purchaseContractDateDate;
	private JFormattedTextField m_exchRateText,m_invoiceVATAmountText,m_invoiceVATPercentText;	
	private ProjectDataPicker m_projectComp;	
	private PartnerDataPicker m_supplierComp;
	private JComboBox m_supplierTypeCombo,m_projectTypeCombo;	
	private pohaci.gumunda.titis.application.DatePicker m_transactionDateDate;
	private JPanel m_invoicePanel,panelUtama,extTopButtonPanel,leftRightPanel,extRightPanel,extLeftPanel,leftPanel,
	exchratePanel,vatPanel,rightPanel,totalPanel,originatorPanel,approvedPanel,
	receivePanel,bottomPanel,butonbawahPanel,m_purchaseInBaseCurrencyPanel,m_purchaseOrderPanel,m_bottomButtonPanel,
	m_topButtonPanel;
	private AssignPanel m_originatorComp,m_approvedComp,m_receivedComp;
	private JSeparator m_jSeparator1,m_jSeparator2,m_jSeparator3;	
	private CurrencyPicker m_currComp,m_invoiceVATCurrComp;
	private boolean m_edit,m_new =false;
	private boolean m_first =true;
	ItemTable m_table;
	public boolean isFirst() { return m_first; }
	
	public PurchaseReceiptPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid);
		initExchangeRateHelper(conn, sessionid);
		setDefaultSignature(); 
		setEntity(new PurchaseReceipt());
		m_entityMapper = MasterMap.obtainMapperFor(PurchaseReceipt.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	private void initComponents() {
		JLabel m_activityCodeLbl,m_bankAccountLbl,m_currencyLbl,m_currencyLbl7,m_customerLbl,
		m_departmentLbl,m_descriptionLbl,m_dueDateLbl,m_exchRateLbl,m_IPCNoLbl,m_invoiceDateLbl,
		m_invoiceNoLbl,m_POContactNoLbl,m_POContractDateLbl,m_projectCodeLbl,m_purchRcptDateLbl,m_purchRcptNoLbl,
		m_ORNoLbl,m_remarkLbl,m_SOPOContractNoLbl,m_statusLbl,m_statusLbl11,m_statusLbl15,m_suplierLbl,
		m_statusLbl16,m_subTotalLbl,m_submittedDateLbl,m_supplierTypeLbl,m_totalLbl,m_unitCodeLbl,
		m_VATLbl,m_VATLbl2,m_workDescLbl,projectTypeLbl;
		
		m_projectComp = new ProjectDataPicker(m_conn, m_sessionid);
		m_originatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		m_approvedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		m_receivedComp  = new AssignPanel(m_conn, m_sessionid,"Received by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		m_topButtonPanel = new JPanel();
		
		GridBagConstraints gridBagConstraints;
		panelUtama = new JPanel();
		extTopButtonPanel = new JPanel();
		m_newBtn = new JButton();
		m_editBtn = new JButton();
		m_saveBtn = new JButton();
		m_deleteBtn = new JButton();
		m_cancelBtn = new JButton();
		m_submitBtn = new JButton();
		leftRightPanel = new JPanel();
		extLeftPanel = new JPanel();
		leftPanel = new JPanel();
		m_statusLbl = new JLabel();
		m_submittedDateLbl = new JLabel();
		projectTypeLbl = new JLabel("Project Type");
		m_invoicePanel = new JPanel();
		m_statusLbl11 = new JLabel();
		m_jSeparator1 = new JSeparator();
		m_invoiceNoLbl = new JLabel();
		m_invoiceDateLbl = new JLabel();
		m_suplierLbl = new JLabel();
		m_supplierTypeLbl = new JLabel();
		m_currencyLbl = new JLabel();
		m_VATLbl = new JLabel();
		m_bankAccountLbl = new JLabel();
		m_dueDateLbl = new JLabel();
		m_purchaseOrderPanel = new JPanel();
		m_statusLbl15 = new JLabel();
		m_jSeparator2 = new JSeparator();
		m_POContactNoLbl = new JLabel();
		m_POContractDateLbl = new JLabel();
		m_descriptionLbl = new JLabel();
		m_statusText = new JTextField();
		m_submittedDateText = new JTextField();
		m_projectTypeCombo =  new JComboBox(new String[]{"CURRENT","IN PROGRESS"});
		m_invoiceNoText = new JTextField();
		m_invoiceDateDate = new DatePicker();
		m_supplierComp =new PartnerDataPicker(m_conn, m_sessionid);
		exchratePanel = new JPanel();
		m_currComp = new CurrencyPicker(m_conn,m_sessionid);
		m_exchRateLbl = new JLabel();
		m_exchRateText = new JFormattedTextField(m_numberFormatter);
		vatPanel = new JPanel();
		m_invoiceVATPercentText = new JFormattedTextField(m_numberFormatter);
		m_currencyLbl7 = new JLabel();
		m_invoiceVATCurrComp = new CurrencyPicker(m_conn,m_sessionid);
		m_invoiceVATAmountText = new JFormattedTextField(m_numberFormatter);
		m_bankAccountSroll = new JScrollPane();
		m_bankAccountTextArea = new JTextArea();
		m_dueDateDate = new DatePicker();
		m_purchaseContractNoText = new JTextField();
		m_purchaseContractDateDate = new DatePicker();
		m_descriptionScroll = new JScrollPane();
		m_descriptionTextArea = new JTextArea();
		m_supplierTypeCombo = new JComboBox();
		extRightPanel = new JPanel();
		rightPanel = new JPanel();
		m_purchRcptNoLbl = new JLabel();
		m_purchRcptDateLbl = new JLabel();
		m_projectCodeLbl = new JLabel();
		m_customerLbl = new JLabel();
		m_workDescLbl = new JLabel();
		m_customerText = new JTextField();
		m_workDescScroll = new JScrollPane();
		m_workDescTextArea = new JTextArea();
		m_unitCodeLbl = new JLabel();
		m_unitCodeText = new JTextField();
		m_activityCodeLbl = new JLabel();
		m_activityCodeText = new JTextField();
		m_departmentLbl = new JLabel();
		m_ORNoLbl = new JLabel();
		m_departmentText = new JTextField();
		m_ORNoText = new JTextField();
		m_SOPOContractNoLbl = new JLabel();
		m_IPCNoLbl = new JLabel();
		m_contractNoText = new JTextField();
		m_IPCNoText = new JTextField();
		m_purchaseInBaseCurrencyPanel = new JPanel();
		m_statusLbl16 = new JLabel();
		m_jSeparator3 = new JSeparator();
		totalPanel = new JPanel();
		m_subTotalLbl = new JLabel();
		m_VATLbl2 = new JLabel();
		m_subTotalCurrText = new JTextField();
		m_VATCurrText = new JTextField();
		m_subTotalAmountText = new JTextField();
		m_VATAmountText = new JTextField();
		m_totalLbl = new JLabel();
		m_totaCurrText = new JTextField();
		m_totalAmountText = new JTextField();
		m_remarkLbl = new JLabel();
		m_remarkScroll = new JScrollPane();
		m_remarksTextArea = new JTextArea();
		m_refNoText = new JTextField();
		m_transactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		originatorPanel = new JPanel();
		approvedPanel = new JPanel();
		receivePanel = new JPanel();
		bottomPanel = new JPanel();
		m_bottomButtonPanel = new JPanel();
		butonbawahPanel = new JPanel();
		m_addPurchaseItemBtn = new JButton();
		m_deletePurchaseItemBtn = new JButton();
		m_burchaseItemScrollPane = new JScrollPane();		
		m_table = new ItemTable();
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(700, 750));
		
		panelUtama.setLayout(new BorderLayout());
		panelUtama.setPreferredSize(new Dimension(725, 530));
		extTopButtonPanel.setLayout(new BorderLayout());
		extTopButtonPanel.setPreferredSize(new Dimension(650, 35));
		
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
		extTopButtonPanel.add(m_topButtonPanel, BorderLayout.WEST);
		
		panelUtama.add(extTopButtonPanel, BorderLayout.NORTH);
		
		leftRightPanel.setLayout(new BorderLayout());
		leftRightPanel.setPreferredSize(new Dimension(700, 330));
		extLeftPanel.setLayout(new BorderLayout());
		extLeftPanel.setPreferredSize(new Dimension(430, 315));
		
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setPreferredSize(new Dimension(420, 210));		
		m_statusLbl.setText("Status");
		m_statusLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_statusLbl, gridBagConstraints);
		
		m_submittedDateLbl.setText("Submitted Date");
		m_submittedDateLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_submittedDateLbl, gridBagConstraints);
		
		m_invoicePanel.setLayout(new GridBagLayout());
		m_invoicePanel.setPreferredSize(new Dimension(410, 20));
		m_statusLbl11.setText("Invoice");
		m_statusLbl11.setPreferredSize(new Dimension(50, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_invoicePanel.add(m_statusLbl11, gridBagConstraints);
		
		m_jSeparator1.setPreferredSize(new Dimension(340, 5));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_invoicePanel.add(m_jSeparator1, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		leftPanel.add(m_invoicePanel, gridBagConstraints);
		
		m_invoiceNoLbl.setText("Invoice No*");
		m_invoiceNoLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_invoiceNoLbl, gridBagConstraints);
		
		m_invoiceDateLbl.setText("Invoice Date*");
		m_invoiceDateLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_invoiceDateLbl, gridBagConstraints);
		
		m_suplierLbl.setText("Supplier*");
		m_suplierLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_suplierLbl, gridBagConstraints);
		
		m_supplierTypeLbl.setText("Supplier Type*");
		m_supplierTypeLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_supplierTypeLbl, gridBagConstraints);
		
		m_currencyLbl.setText("Currency*");
		m_currencyLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_currencyLbl, gridBagConstraints);
		
		m_VATLbl.setText("VAT");
		m_VATLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_VATLbl, gridBagConstraints);
		
		m_bankAccountLbl.setText("Bank Account");
		m_bankAccountLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_bankAccountLbl, gridBagConstraints);
		
		m_dueDateLbl.setText("Due Date*");
		m_dueDateLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_dueDateLbl, gridBagConstraints);
		
		m_purchaseOrderPanel.setLayout(new GridBagLayout());
		m_purchaseOrderPanel.setPreferredSize(new Dimension(320, 20));
		m_statusLbl15.setText("Purchase Order");
		m_statusLbl15.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_purchaseOrderPanel.add(m_statusLbl15, gridBagConstraints);
		
		m_jSeparator2.setPreferredSize(new Dimension(240, 5));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_purchaseOrderPanel.add(m_jSeparator2, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		leftPanel.add(m_purchaseOrderPanel, gridBagConstraints);
		
		m_POContactNoLbl.setText("PO/Contract No.*");
		m_POContactNoLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_POContactNoLbl, gridBagConstraints);
		
		m_POContractDateLbl.setText("PO/Contract Date*");
		m_POContractDateLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_POContractDateLbl, gridBagConstraints);
		
		m_descriptionLbl.setText("Description*");
		m_descriptionLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 14;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_descriptionLbl, gridBagConstraints);
		
		m_statusText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_statusText, gridBagConstraints);
		
		m_submittedDateText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_submittedDateText, gridBagConstraints);
		
		m_invoiceNoText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_invoiceNoText, gridBagConstraints);
		
		m_invoiceDateDate.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_invoiceDateDate, gridBagConstraints);
		
		m_supplierComp.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_supplierComp, gridBagConstraints);
		
		exchratePanel.setLayout(new GridBagLayout());
		exchratePanel.setPreferredSize(new Dimension(200, 23));		
		m_currComp.setPreferredSize(new Dimension(78, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		exchratePanel.add(m_currComp, gridBagConstraints);
		
		m_exchRateLbl.setText("Exch. Rate");
		m_exchRateLbl.setPreferredSize(new Dimension(60, 21));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		exchratePanel.add(m_exchRateLbl, gridBagConstraints);
		
		m_exchRateText.setPreferredSize(new Dimension(140, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		exchratePanel.add(m_exchRateText, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		leftPanel.add(exchratePanel, gridBagConstraints);
		
		vatPanel.setLayout(new GridBagLayout());
		vatPanel.setPreferredSize(new Dimension(200, 23));		
		m_invoiceVATPercentText.setPreferredSize(new Dimension(45, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		vatPanel.add(m_invoiceVATPercentText, gridBagConstraints);
		
		m_currencyLbl7.setText("%");
		m_currencyLbl7.setPreferredSize(new Dimension(20, 21));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		vatPanel.add(m_currencyLbl7, gridBagConstraints);
		
		m_invoiceVATCurrComp.setPreferredSize(new Dimension(75, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 3);
		vatPanel.add(m_invoiceVATCurrComp, gridBagConstraints);
		
		m_invoiceVATAmountText.setPreferredSize(new Dimension(147, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		vatPanel.add(m_invoiceVATAmountText, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		leftPanel.add(vatPanel, gridBagConstraints);
		
		m_bankAccountSroll.setPreferredSize(new Dimension(290, 54));
		m_bankAccountTextArea.setColumns(20);
		m_bankAccountTextArea.setLineWrap(true);
		m_bankAccountTextArea.setRows(5);
		m_bankAccountSroll.setViewportView(m_bankAccountTextArea);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_bankAccountSroll, gridBagConstraints);
		
		m_dueDateDate.setPreferredSize(new Dimension(290, 21));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_dueDateDate, gridBagConstraints);
		
		m_purchaseContractNoText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_purchaseContractNoText, gridBagConstraints);
		
		m_purchaseContractDateDate.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_purchaseContractDateDate, gridBagConstraints);
		
		m_descriptionScroll.setPreferredSize(new Dimension(290, 70));
		m_descriptionTextArea.setColumns(20);
		m_descriptionTextArea.setLineWrap(true);
		m_descriptionTextArea.setRows(5);
		m_descriptionScroll.setViewportView(m_descriptionTextArea);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 14;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_descriptionScroll, gridBagConstraints);
		
		m_supplierTypeCombo.setModel(new DefaultComboBoxModel(new String[] {"PKP", "NON PKP" }));
		m_supplierTypeCombo.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		leftPanel.add(m_supplierTypeCombo, gridBagConstraints);
		
		extLeftPanel.add(leftPanel, BorderLayout.WEST);
		leftRightPanel.add(extLeftPanel, BorderLayout.WEST);
		extRightPanel.setLayout(new BorderLayout());
		
		rightPanel.setLayout(new GridBagLayout());
		rightPanel.setPreferredSize(new Dimension(415, 220));		
		m_purchRcptNoLbl.setText("Purch. Receipt No.");
		m_purchRcptNoLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_purchRcptNoLbl, gridBagConstraints);
		
		m_purchRcptDateLbl.setText("Purch. Rcpt Date*");
		m_purchRcptDateLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_purchRcptDateLbl, gridBagConstraints);
		
		m_projectCodeLbl.setText("Project Code*");
		m_projectCodeLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_projectCodeLbl, gridBagConstraints);
		
		m_customerLbl.setText("Customer");
		m_customerLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_customerLbl, gridBagConstraints);
		
		m_workDescLbl.setText("Work Desc. ");
		m_workDescLbl.setPreferredSize(new Dimension(85, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_workDescLbl, gridBagConstraints);
		
		m_projectComp.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_projectComp, gridBagConstraints);
		
		m_customerText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_customerText, gridBagConstraints);
		
		m_workDescScroll.setPreferredSize(new Dimension(290, 43));
		m_workDescTextArea.setColumns(20);
		m_workDescTextArea.setLineWrap(true);
		m_workDescTextArea.setRows(5);
		m_workDescScroll.setViewportView(m_workDescTextArea);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_workDescScroll, gridBagConstraints);
		
		m_unitCodeLbl.setText("Unit Code");
		m_unitCodeLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_unitCodeLbl, gridBagConstraints);
		
		m_unitCodeText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_unitCodeText, gridBagConstraints);
		
		m_activityCodeLbl.setText("Activity Code");
		m_activityCodeLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_activityCodeLbl, gridBagConstraints);
		
		m_activityCodeText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_activityCodeText, gridBagConstraints);
		
		m_departmentLbl.setText("Department");
		m_departmentLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_departmentLbl, gridBagConstraints);
		
		m_ORNoLbl.setText("O.R No");
		m_ORNoLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_ORNoLbl, gridBagConstraints);
		
		m_departmentText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_departmentText, gridBagConstraints);
		
		m_ORNoText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_ORNoText, gridBagConstraints);
		
		m_SOPOContractNoLbl.setText("SO/PO/Contract No");
		m_SOPOContractNoLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_SOPOContractNoLbl, gridBagConstraints);
		
		m_IPCNoLbl.setText("IPC No");
		m_IPCNoLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_IPCNoLbl, gridBagConstraints);
		
		projectTypeLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(projectTypeLbl, gridBagConstraints);
		
		m_contractNoText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_contractNoText, gridBagConstraints);
		
		m_IPCNoText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_IPCNoText, gridBagConstraints);
		
		m_projectTypeCombo.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_projectTypeCombo, gridBagConstraints);
		
		
		m_purchaseInBaseCurrencyPanel.setLayout(new GridBagLayout());
		m_purchaseInBaseCurrencyPanel.setPreferredSize(new Dimension(410, 20));
		m_statusLbl16.setText("Purchase In Base Currency");
		m_statusLbl16.setPreferredSize(new Dimension(140, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_purchaseInBaseCurrencyPanel.add(m_statusLbl16, gridBagConstraints);
		
		m_jSeparator3.setPreferredSize(new Dimension(190, 5));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_purchaseInBaseCurrencyPanel.add(m_jSeparator3, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		rightPanel.add(m_purchaseInBaseCurrencyPanel, gridBagConstraints);
		
		totalPanel.setLayout(new GridBagLayout());
		totalPanel.setPreferredSize(new Dimension(400, 60));		
		m_subTotalLbl.setText("SubTotal");
		m_subTotalLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		totalPanel.add(m_subTotalLbl, gridBagConstraints);
		
		m_VATLbl2.setText("VAT");
		m_VATLbl2.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		totalPanel.add(m_VATLbl2, gridBagConstraints);
		
		m_subTotalCurrText.setPreferredSize(new Dimension(46, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 4);
		totalPanel.add(m_subTotalCurrText, gridBagConstraints);
		
		m_VATCurrText.setPreferredSize(new Dimension(46, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 4);
		totalPanel.add(m_VATCurrText, gridBagConstraints);
		
		m_subTotalAmountText.setPreferredSize(new Dimension(240, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		totalPanel.add(m_subTotalAmountText, gridBagConstraints);
		
		m_VATAmountText.setPreferredSize(new Dimension(240, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		totalPanel.add(m_VATAmountText, gridBagConstraints);
		
		m_totalLbl.setText("Total");
		m_totalLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		totalPanel.add(m_totalLbl, gridBagConstraints);
		
		m_totaCurrText.setPreferredSize(new Dimension(46, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 4);
		totalPanel.add(m_totaCurrText, gridBagConstraints);
		
		m_totalAmountText.setPreferredSize(new Dimension(240, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		totalPanel.add(m_totalAmountText, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 13;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(totalPanel, gridBagConstraints);
		
		m_remarkLbl.setText("Remarks");
		m_remarkLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 14;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_remarkLbl, gridBagConstraints);
		
		m_remarkScroll.setPreferredSize(new Dimension(290, 43));
		m_remarksTextArea.setColumns(20);
		m_remarksTextArea.setLineWrap(true);
		m_remarksTextArea.setRows(5);
		m_remarkScroll.setViewportView(m_remarksTextArea);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 14;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_remarkScroll, gridBagConstraints);
		
		m_refNoText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(m_refNoText, gridBagConstraints);
		
		m_transactionDateDate.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_transactionDateDate, gridBagConstraints);
		
		extRightPanel.add(rightPanel, BorderLayout.WEST);
		leftRightPanel.add(extRightPanel, BorderLayout.CENTER);
		panelUtama.add(leftRightPanel, BorderLayout.CENTER);
		
		originatorPanel.setLayout(new BorderLayout());
		originatorPanel.setPreferredSize(new Dimension(700, 90));
		
		m_originatorComp.setLayout(new GridBagLayout());
		m_originatorComp.setBorder(BorderFactory.createTitledBorder("Originator"));
		m_originatorComp.setOpaque(false);
		m_originatorComp.setPreferredSize(new Dimension(280, 110));
		originatorPanel.add(m_originatorComp, BorderLayout.WEST);
		approvedPanel.setLayout(new BorderLayout());
		
		m_approvedComp.setLayout(new GridBagLayout());
		m_approvedComp.setBorder(BorderFactory.createTitledBorder("Approved by"));
		m_approvedComp.setOpaque(false);
		m_approvedComp.setPreferredSize(new Dimension(280, 110));
		approvedPanel.add(m_approvedComp, BorderLayout.WEST);
		receivePanel.setLayout(new BorderLayout());
		receivePanel.setPreferredSize(new Dimension(275,110));
		
		m_receivedComp.setLayout(new GridBagLayout());
		m_receivedComp.setBorder(BorderFactory.createTitledBorder("Received by"));
		m_receivedComp.setOpaque(false);
		m_receivedComp.setPreferredSize(new Dimension(280, 110));
		receivePanel.add(m_receivedComp, BorderLayout.WEST);
		approvedPanel.add(receivePanel, BorderLayout.CENTER);
		originatorPanel.add(approvedPanel, BorderLayout.CENTER);
		
		panelUtama.add(originatorPanel, BorderLayout.SOUTH);
		add(panelUtama, BorderLayout.NORTH);	
		
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		bottomPanel.setPreferredSize(new Dimension(650, 250));
		
		m_bottomButtonPanel.setLayout(new BorderLayout());
		m_bottomButtonPanel.setPreferredSize(new Dimension(650, 35));
		
		butonbawahPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		butonbawahPanel.setPreferredSize(new Dimension(650, 35));
		
		m_addPurchaseItemBtn.setText("Add");
		m_addPurchaseItemBtn.setMargin(new Insets(2, 2, 2, 2));
		m_addPurchaseItemBtn.setPreferredSize(new Dimension(50, 21));
		butonbawahPanel.add(m_addPurchaseItemBtn);
		
		m_deletePurchaseItemBtn.setText("Delete");
		m_deletePurchaseItemBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deletePurchaseItemBtn.setPreferredSize(new Dimension(50, 21));
		butonbawahPanel.add(m_deletePurchaseItemBtn);
		
		m_bottomButtonPanel.add(butonbawahPanel, BorderLayout.WEST);
		bottomPanel.add(m_bottomButtonPanel, BorderLayout.NORTH);
		m_burchaseItemScrollPane.setPreferredSize(new Dimension(650, 215));	
		bottomPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.CENTER);		
	}
	
	private void addingListener(){
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		m_supplierTypeCombo.addActionListener(this);
		m_projectComp.addPropertyChangeListener("object",this);
		m_currComp.addPropertyChangeListener("object",this);		
		m_exchRateText.addPropertyChangeListener(this);
		m_invoiceVATPercentText.addPropertyChangeListener(this);
		m_addPurchaseItemBtn.addActionListener(this);
		m_deletePurchaseItemBtn.addActionListener(this);
		m_transactionDateDate.addPropertyChangeListener("date", this);
	}
	
	public void actionPerformed(ActionEvent e){	
		if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new JasperPurchaseReceipt(m_entity,m_conn);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if (e.getSource() == m_addPurchaseItemBtn) {
			m_first = false;
			m_table.addRow(new Object[]{null, null, null, null, null});
		}
		else if(e.getSource()==m_deletePurchaseItemBtn){			
			int row = -1;
			row = m_table.getSelectedRow();
			int maxRow = m_table.getRowCount()-1;
			if(row>-1){
				if(row<(maxRow-3)){
					m_table.deleteRow(row);
					m_table.updateSummary();	
					m_table.updateNumbering();
				}
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {   	
			SearchPurchaseReceiptDialog dlg = new SearchPurchaseReceiptDialog(GumundaMainFrame.getMainFrame(), "Search Purchase Receipt", m_conn, m_sessionid,new PurchaseReceiptLoader(m_conn));
			dlg.setVisible(true);
			if (dlg.selectedObj != null){    			
				doLoad(dlg.selectedObj);				
				m_first = false;
			}			
		}
		else if (e.getSource()==m_supplierTypeCombo){
			if (!m_supplierTypeCombo.getSelectedItem().equals("PKP")){
				m_invoiceVATPercentText.setText(new Double(0).toString());
				m_invoiceVATPercentText.setEditable(false);
			}else
				m_invoiceVATPercentText.setEditable(true);
		}
	}
	
	protected void clearAll() {
		super.clearAll();
		clearKomponen();	
		disableEditMode();
	}
	
	protected void deleteChilds() {
		DeleteAnaknya(m_entity);
		super.deleteChilds();
	}
	
	private void DeleteAnaknya(PurchaseReceipt old){
		GenericMapper mapper2=MasterMap.obtainMapperFor(PurchaseReceiptItem.class);
		mapper2.setActiveConn(m_conn);   
		mapper2.doDeleteByColumn(IDBConstants.ATTR_PURCHASE_RECEIPT,new Long(old.getIndex()).toString());
	}
	
	
	public void doEdit() {
		m_edit = true;
		super.doEdit();
	}
	
	protected void doNew() {		
		m_new = true;
		super.doNew();
		isiDefaultAssignPanel();
	}

	class VatPercentRateOnly implements DocumentListener{
		private void toBaseCurrency(){
			String excrate = m_invoiceVATPercentText.getText();					
			if (excrate==null) 
				excrate = "";
			if (!excrate.equals("")){
				m_table.updateSummary();						
			}	
		}		
		public void changedUpdate(DocumentEvent e) {
			toBaseCurrency();			
		}		
		public void insertUpdate(DocumentEvent e) {
			toBaseCurrency();			
		}		
		public void removeUpdate(DocumentEvent e) {
			toBaseCurrency();			
		}		
	}
	
	class ExRateOnly implements DocumentListener{
		private void toBaseCurrency(){
			if (m_currComp.getObject()!=null){
				if (!((Currency)m_currComp.getObject()).getSymbol().equals(baseCurrency.getSymbol())){
					Object excrate = m_exchRateText.getValue();			
					if (excrate!=null) 
					{
						m_table.updateSummary();						
					}					
				}
			}	
		}		
		public void changedUpdate(DocumentEvent e) {
			toBaseCurrency();			
		}	
		public void insertUpdate(DocumentEvent e) {
			toBaseCurrency();			
		}		
		public void removeUpdate(DocumentEvent e) {
			toBaseCurrency();			
		}		
	}
	
	
	public void clearKomponen(){
		m_statusText.setText("");
		m_invoiceDateDate.setDate(null);
		m_currComp.setObject(null);
		m_dueDateDate.setDate(null);
		m_purchaseContractDateDate.setDate(null);		
		m_currComp.setObject(null);
		m_subTotalCurrText.setText("");
		m_VATCurrText.setText("");
		m_totaCurrText.setText("");
		m_originatorComp.setEmployee(null);
		m_approvedComp.setEmployee(null);
		m_receivedComp.setEmployee(null);
		m_originatorComp.m_jobTextField.setText("");
		m_approvedComp.m_jobTextField.setText("");
		m_receivedComp.m_jobTextField.setText("");
		m_originatorComp.setDate(null);
		m_approvedComp.setDate(null);
		m_receivedComp.setDate(null);
		m_exchRateText.setValue(new Double(0));
		m_transactionDateDate.setDate(null);
	}
	
	public void loadProjectDetail(long a){
		GenericMapper mapper2=MasterMap.obtainMapperFor(PurchaseReceiptItem.class);
		mapper2.setActiveConn(m_conn);
		List rs=mapper2.doSelectWhere(IDBConstants.ATTR_PURCHASE_RECEIPT+"="+a);
		m_table.clear();		
		PurchaseReceiptItem temp;
		if (rs.size()>0){
			for(int i=0;i<rs.size();i++){			
				temp=(PurchaseReceiptItem)rs.get(i);
				m_table.addRow(new Object[]{temp.getDescription(), temp.getSpecification(), new Double(temp.getQty()), 
						new Double(temp.getUnitPrice()),new Double(temp.getAmount())});
			}
			m_first = false;
			m_table.updateSummary();
		}
	}
	
	public void setEnableButtonBawah(boolean bool){
		m_addPurchaseItemBtn.setEnabled(bool);
		m_deletePurchaseItemBtn.setEnabled(bool);		
	}
	
	private void isiDefaultAssignPanel(){
		m_originatorComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultOriginator));
		m_approvedComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultApproved));
		m_receivedComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultReceived));
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PURCHASE_RECEIPT, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			m_defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PURCHASE_RECEIPT, Signature.SIGN_APPROVED);
		if(sign!=null)
			m_defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PURCHASE_RECEIPT, Signature.SIGN_RECEIVED);
		if(sign!=null)
			m_defaultReceived = sign.getFullEmployee();
	}
	
	PurchaseReceipt entity() {
		return m_entity;
	}
	
	protected StateTemplateEntity currentEntity() {		
		return entity();
	}
	
	
	protected boolean cekValidity() {
		validityMsgs.clear();
		if (m_invoiceNoText.getText().equals(""))
			addInvalid("Invoince no must be added");		
		if (m_invoiceDateDate.getDate()==null)
			addInvalid("Invoince date must be selected");
		if (m_supplierComp.getObject()==null)
			addInvalid("Supplier type must be selected");
		if (m_currComp.getObject()==null)
			addInvalid("Currency type must be selected");
		if (m_purchaseContractNoText.getText().equals(""))
			addInvalid("PO/Contract No  must be added");
		if (m_purchaseContractDateDate.getDate()==null)
			addInvalid("PO/Contract date must be added");
		if (m_descriptionTextArea.getText().equals(""))
			addInvalid("Description must be added");
		if (m_transactionDateDate.getDate()==null)
			addInvalid("Purchase Rcpt Date must be selected");
		if (m_projectComp.getObject()==null)
			addInvalid("Project Code must be selected");
		detailAccountOperation();
		PurchaseReceiptItem[] temp=entity().getPurchaseDetail();		
		if(temp.length==0)
			addInvalid("Account Detail must be added");
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
	
	protected void gui2entity() {
		entity().setInvoice(m_invoiceNoText.getText());
		if (m_invoiceDateDate.getDate()!=null)
			entity().setInvoiceDate(m_invoiceDateDate.getDate());
		else
			entity().setInvoiceDate(new Date());
		entity().setSupplier((Partner)m_supplierComp.getObject());
		entity().setSupplierType(m_supplierTypeCombo.getSelectedItem().toString());
		
		entity().setApCurr((Currency)m_currComp.getObject());		
		
		if (m_exchRateText.getValue()==null)
			entity().setApexChRate(0.0);
		else
			entity().setApexChRate(((Number)m_exchRateText.getValue()).doubleValue());
		
		entity().setVatCurr((Currency)m_invoiceVATCurrComp.getObject());
		
		entity().setVatPercent(((Number)m_invoiceVATPercentText.getValue()).doubleValue());
		Number nbr = new Double(0);
		try {
			nbr = (Number)m_numberFormatter.stringToValue(m_invoiceVATAmountText.getText());
			entity().setVatAmount(nbr.doubleValue());	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		entity().setBankAccount(m_bankAccountTextArea.getText());
		entity().setDuedate(m_dueDateDate.getDate());		
		entity().setContractNo(m_purchaseContractNoText.getText());
		entity().setContractDate(m_purchaseContractDateDate.getDate());		
		entity().setProject((ProjectData)m_projectComp.getObject());
		entity().setReferenceNo(m_refNoText.getText());
		entity().setTransactionDate(m_transactionDateDate.getDate());
		entity().setProjectType((String)m_projectTypeCombo.getSelectedItem());
		entity().setRemarks(m_remarksTextArea.getText());		
		entity().transTemplateRead(
				this.m_originatorComp, this.m_approvedComp,
				this.m_receivedComp, this.m_refNoText,
				this.m_descriptionTextArea
		);
		
		Double amt = m_table.getSubtotal();
		entity().setAmount(amt.doubleValue());
		entity().setUnit(((ProjectData)m_projectComp.getObject()).getUnit());
		detailAccountOperation();	
	}
	
	private void detailAccountOperation() {
		if (m_table.getDataCount()>=0){
			PurchaseReceiptItem[] temp = new PurchaseReceiptItem[m_table.getDataCount()];
			for (int i =0;i<m_table.getDataCount();i++){	
				temp[i]=new PurchaseReceiptItem();
				
				if (m_table.getValueAt(i,1)!=null){
					String desc = (String) m_table.getValueAt(i,1);
					temp[i].setDescription(desc);
				}else
					temp[i].setDescription("");
				
				if (m_table.getValueAt(i,2)!=null){
					String spec = (String) m_table.getValueAt(i,2);
					temp[i].setSpecification(spec);
				}else
					temp[i].setSpecification("");
				
				if (m_table.getValueAt(i,3)!=null)
					temp[i].setQty(Double.parseDouble(m_table.getValueAt(i,3).toString()));
				else
					temp[i].setQty(0.0);
				
				if (m_table.getValueAt(i,4)!=null)
					temp[i].setUnitPrice(Double.parseDouble(m_table.getValueAt(i,4).toString()));
				else
					temp[i].setUnitPrice(0.0);
				
				if (m_table.getValueAt(i,5)!=null)
					temp[i].setAmount(Double.parseDouble(m_table.getValueAt(i,5).toString()));
				else
					temp[i].setAmount(0.0);			
			}
			entity().setPurchaseDetail(temp);
		}
	}
	
	protected void entity2gui() {
		m_statusText.setText(entity().statusInString());
		m_invoiceNoText.setText(entity().getInvoice());
		if (entity().getInvoiceDate()!=null)
			m_invoiceDateDate.setDate(entity().getInvoiceDate());
		else
			m_invoiceDateDate.setDate(new Date());
		
		if (entity().getSupplier()!=null)
			m_supplierComp.setObject(entity().getSupplier());		
		else
			m_supplierComp.setObject(null);
		
		m_invoiceVATPercentText.setValue(new Double(0));
		if (entity().getSupplierType()!=null){
			if (entity().getSupplierType().equalsIgnoreCase("PKP")){
				m_supplierTypeCombo.setSelectedItem("PKP");
				m_invoiceVATPercentText.setValue(new Double(entity().getVatPercent()));
			}else{
				m_supplierTypeCombo.setSelectedItem("NON PKP");
				m_invoiceVATPercentText.setValue(new Double(0));
			}
		}else{
			m_supplierTypeCombo.setSelectedItem("PKP");
			m_invoiceVATPercentText.setValue(new Double(0));
		}
		
		if (entity().getApCurr()!=null){
			m_currComp.setObject(entity().getApCurr());
			if (entity().getApCurr().getSymbol().equals(baseCurrency.getSymbol())){
				m_exchRateText.setValue(new Double(1.0));
				// aneh:
				//m_exchRateText.setEditable(false);
			}else{
				m_exchRateText.setValue(new Double(entity().getApexChRate()));
				// aneh:
				//m_exchRateText.setEditable(true);
			}
		}else{			
			m_currComp.setObject(null);
			m_exchRateText.setValue(null);
			m_invoiceVATCurrComp.setObject(null);
			m_invoiceVATAmountText.setValue(null);
			m_subTotalCurrText.setText("");
			m_subTotalAmountText.setText("");
			m_VATCurrText.setText("");
			m_VATAmountText.setText("");
			m_totaCurrText.setText("");
			m_totalAmountText.setText("");
		}	
		if (entity().getVatCurr()!=null)
			m_invoiceVATCurrComp.setObject(entity().getVatCurr());
		m_invoiceVATAmountText.setValue(new Double(entity().getVatAmount()));
		m_bankAccountTextArea.setText(entity().getBankAccount());
		if (entity().getDuedate()!=null)
			m_dueDateDate.setDate(entity().getDuedate());
		else
			m_dueDateDate.setDate(new Date());	
		m_purchaseContractNoText.setText(entity().getContractNo());
		if (entity().getContractDate()!=null)
			m_purchaseContractDateDate.setDate(entity().getContractDate());
		else
			m_purchaseContractDateDate.setDate(new Date());
		m_refNoText.setText(entity().getReferenceNo());
		m_descriptionTextArea.setText(entity().getDescription());
		if (entity().getProjectType()==null)
			m_projectTypeCombo.setSelectedItem("CURRENT");
		else
			m_projectTypeCombo.setSelectedItem(entity().getProjectType());
		m_remarksTextArea.setText(entity().getRemarks());
		if (entity().getTransactionDate()!=null)
			m_transactionDateDate.setDate(entity().getTransactionDate());
		else
			m_transactionDateDate.setDate(new Date());
		if (entity().getProject()!=null){
			m_projectComp.setObject(entity().getProject());
			m_customerText.setText("");
			if (entity().getProject().getCustomer()!=null)
				m_customerText.setText(entity().getProject().getCustomer().toString());
			m_workDescTextArea.setText(entity().getProject().getWorkDescription());
			m_unitCodeText.setText(entity().getProject().getUnit().toString());
			if (entity().getProject().getAct()!=null)
				m_activityCodeText.setText(entity().getProject().getAct().toString());
			else
				m_activityCodeText.setText("");
			m_departmentText.setText(entity().getProject().getDepartment().toString());
			m_ORNoText.setText(entity().getProject().getORNo());
			m_contractNoText.setText(entity().getProject().getPONo());
			m_IPCNoText.setText(entity().getProject().getIPCNo());			
		}else{
			m_projectComp.setObject(null);
			m_customerText.setText("");
			m_workDescTextArea.setText("");
			m_unitCodeText.setText("");
			m_activityCodeText.setText("");
			m_departmentText.setText("");
			m_ORNoText.setText("");
			m_contractNoText.setText("");
			m_IPCNoText.setText("");
		}		
		
		m_subTotalCurrText.setText(baseCurrency.getSymbol());
		m_VATCurrText.setText(baseCurrency.getSymbol());
		m_totaCurrText.setText(baseCurrency.getSymbol());
						
		m_originatorComp.setEmployee(entity().getEmpOriginator());
		m_originatorComp.setJobTitle(entity().getJobTitleOriginator());
		m_originatorComp.setDate(entity().getDateOriginator());
		
		m_approvedComp.setEmployee(entity().getEmpApproved());
		m_approvedComp.setJobTitle(entity().getJobTitleApproved());
		m_approvedComp.setDate(entity().getDateOriginator());
		
		m_receivedComp.setEmployee(entity().getEmpReceived());
		m_receivedComp.setJobTitle(entity().getJobTitleReceived());
		m_receivedComp.setDate(entity().getDateReceived());
		
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			m_submittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			m_submittedDateText.setText("");
		loadProjectDetail(entity().getIndex());	
		
	}
	
	protected void disableEditMode() {
		m_statusText.setEditable(false);
		m_submittedDateText.setEditable(false);
		m_invoiceNoText.setEditable(false);
		m_invoiceDateDate.setEditable(false);
		m_supplierComp.setEnabled(false);
		m_supplierTypeCombo.setEnabled(false);
		m_currComp.setEnabled(false);
		m_exchRateText.setEditable(false);	
		m_invoiceVATPercentText.setEditable(false);
		m_invoiceVATCurrComp.setEnabled(false);
		m_invoiceVATAmountText.setEditable(false);
		m_bankAccountTextArea.setEditable(false);
		m_dueDateDate.setEditable(false);
		m_purchaseContractNoText.setEditable(false);
		m_purchaseContractDateDate.setEditable(false);
		m_descriptionTextArea.setEditable(false);
		m_refNoText.setEditable(false);
		m_transactionDateDate.setEditable(false);
		m_projectComp.setEnabled(false);
		m_customerText.setEditable(false);
		m_workDescTextArea.setEditable(false);
		m_unitCodeText.setEditable(false);
		m_activityCodeText.setEditable(false);
		m_departmentText.setEditable(false);
		m_ORNoText.setEditable(false);
		m_contractNoText.setEditable(false);
		m_IPCNoText.setEditable(false);
		m_projectTypeCombo.setEnabled(false);
		m_subTotalCurrText.setEditable(false);
		m_subTotalAmountText.setEditable(false);		
		m_VATAmountText.setEditable(false);
		m_VATCurrText.setEditable(false);		
		m_totaCurrText.setEditable(false);
		m_totalAmountText.setEditable(false);
		m_remarksTextArea.setEditable(false);
		m_originatorComp.setEnabled(false);
		m_approvedComp.setEnabled(false);
		m_receivedComp.setEnabled(false);		
		m_addPurchaseItemBtn.setEnabled(false);
		m_deletePurchaseItemBtn.setEnabled(false);	
		m_table.setEnabled(false);
	}
	
	protected void enableEditMode() {
		
		m_invoiceNoText.setEditable(true);
		m_invoiceDateDate.setEditable(true);
		m_supplierComp.setEnabled(true);
		m_supplierTypeCombo.setEnabled(true);
		m_currComp.setEnabled(true);
		//m_exchRateText.setEditable(true);
		m_invoiceVATPercentText.setEditable(true);
		m_bankAccountTextArea.setEditable(true);
		m_dueDateDate.setEditable(true);
		m_purchaseContractNoText.setEditable(true);
		m_purchaseContractDateDate.setEditable(true);
		m_descriptionTextArea.setEditable(true);
		m_transactionDateDate.setEditable(true);
		m_projectComp.setEnabled(true);
		m_projectTypeCombo.setEnabled(true);
		m_remarksTextArea.setEditable(true);
		m_originatorComp.setEnabled(true);
		m_approvedComp.setEnabled(true);
		m_receivedComp.setEnabled(true);	
		m_addPurchaseItemBtn.setEnabled(true);
		m_deletePurchaseItemBtn.setEnabled(true);
		m_table.setEnabled(true);
	}
	
	protected Object createNew() {
		PurchaseReceipt a  = new PurchaseReceipt();
		a.setEmpOriginator(m_defaultOriginator);
		a.setEmpApproved(m_defaultApproved);
		a.setEmpReceived(m_defaultReceived);
		
		return a ;
	}
	
	void setEntity(Object m_entity) {
		PurchaseReceipt oldEntity = this.m_entity;
		if (oldEntity!=null)
		{
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PurchaseReceipt)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}	
	
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		if (evt.getSource()==m_projectComp){			
			if (evt.getNewValue() == null){
			}else {				
				ProjectData a = (ProjectData)m_projectComp.getObject();				
				m_customerText.setText(a.getCustname());
				m_workDescTextArea.setText(a.getWorkDescription());
				m_unitCodeText.setText("");
				if (a.getUnit()!=null)
					m_unitCodeText.setText(a.getUnit().toString());
				m_activityCodeText.setText("");
				if (a.getActivity()!=null)
					m_activityCodeText.setText(a.getActivity().toString());
				m_departmentText.setText("");
				if (a.getDepartment()!=null)
					m_departmentText.setText(a.getDepartment().toString());
				m_ORNoText.setText(a.getORNo());
				m_contractNoText.setText(a.getPONo());
				m_IPCNoText.setText(a.getIPCNo());				
				
			}
		}
		if (evt.getSource()==m_currComp){
			if (evt.getNewValue() == null){				
			}else{			
				m_invoiceVATCurrComp.setObject((Currency)m_currComp.getObject());
				if (((Currency)m_currComp.getObject()).getSymbol().equals(baseCurrency.getSymbol())){
					m_exchRateText.setValue(new Double(1));
					m_exchRateText.setEditable(false);					
				}else{
					Double exch = new Double(entity().getApexChRate());
					if(exch.doubleValue()==0.0D){
						setDefaultExchangeRate();
					}else
						m_exchRateText.setValue(exch);
					m_exchRateText.setEditable(true);
				}
				m_table.updateSummary();
			}
		}
		
		if(evt.getSource()==m_invoiceVATPercentText){
			Number vat = (Number)m_invoiceVATPercentText.getValue();
			if(vat!=null)
				m_table.updateSummary();
		}
		if(evt.getSource()==m_exchRateText){
			if (m_currComp.getObject()!=null){
				Number exch = (Number)m_exchRateText.getValue();
				if(exch!=null)
					m_table.updateSummary();
			}
		}
		if(evt.getSource()==m_transactionDateDate){
			if ("date".endsWith(evt.getPropertyName())){
				setDefaultExchangeRate();
			}
		}
	}

	private void setDefaultExchangeRate() {
		Date date = m_transactionDateDate.getDate();
		double val = getDefaultExchangeRate((Currency)m_currComp.getObject(), date);
		m_exchRateText.setValue(new Double(val));
	}
	
	protected void doSave() {
		if (!cekValidity()) return;
		super.doSave();
		setEnableButtonBawah(false);
		m_edit = false;
		m_new =false;		
		AccountingSQLSAP sql = new AccountingSQLSAP();
		GenericMapper mapper2=MasterMap.obtainMapperFor(PurchaseReceiptItem.class);
		mapper2.setActiveConn(m_conn);
		try {			
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_PURCHASE_RECEIPT, m_conn);
				entity().setIndex(index);
			}			
			if (entity().getPurchaseDetail()!=null){
				PurchaseReceiptItem temp[]=entity().getPurchaseDetail();
				mapper2.doDeleteByColumn(IDBConstants.ATTR_PURCHASE_RECEIPT,new Long(entity().getIndex()).toString());
				for (int i=0;i<temp.length;i++){
					temp[i].setPurchaseReceipt(	(PurchaseReceipt)entity());
					mapper2.doInsert(temp[i]);  
				}	
			}
			entity2gui();
			m_first = false;
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
				if(((row>-1)&&(row<maxRow))&&((col==3)||(col==4))){		
					Number qtyN = (Number) model.getValueAt(row, 3);
					Double qty = (Double)qtyN;
					Double unitPrice = (Double) model.getValueAt(row, 4);
					if((qty!=null)&&(unitPrice!=null)){
						double amount = qty.doubleValue() * unitPrice.doubleValue();
						model.setValueAt(new Double(amount), row, 5);
						m_table.updateSummary();
					}
				}
			}
		}
		
	}
	
	protected class ItemTable extends JTable {
		private static final long serialVersionUID = 1L;
		ArrayList listData=new ArrayList();
		protected ItemTable(){
			PersonalTableModel model = new PersonalTableModel();
			model.addColumn("No");
			model.addColumn("Description");
			model.addColumn("Specification");
			model.addColumn("Qty");
			model.addColumn("Unit Price");
			model.addColumn("Amount");
			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(30);
			getColumnModel().getColumn(0).setMaxWidth(30);		
			clear();			
			getModel().addTableModelListener(new MyTableModelListener());
			getSelectionModel().addListSelectionListener(model);
		}		
		
		public Double getSubtotal() {
			int maxRow = getRowCount()-1;
			return (Double)getValueAt(maxRow - 2, 5);
		}
		
		public void updateSummary() {
			int maxRow = getRowCount()-1;
			int maxSalesRow = maxRow - 4;				
			
			double subtotal = 0;
			
			for(int i=0; i<=maxSalesRow; i++){
				double amt = 0;
				if (getValueAt(i, 5)!=null)						
					amt = ((Double) getValueAt(i, 5)).doubleValue();
				subtotal += amt;
			}			
			setValueAt(new Double(subtotal), maxRow - 2, 5);
			
			String vatPercentStr = m_invoiceVATPercentText.getText();
			Number nbr = new Double(0);
			try {
				nbr = (Number)m_numberFormatter.stringToValue(vatPercentStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			double vatPercent = 0;
			if(!m_invoiceVATPercentText.getText().equals("")){
				vatPercent = nbr.doubleValue();
			}				
			double vat = (vatPercent / 100) * subtotal;
			setValueAt(new Double(vat), maxRow - 1, 5);			
			
			double total = subtotal + vat;
			setValueAt(new Double(total), maxRow, 5);			
			
			DecimalFormat formatDesimal = new DecimalFormat("#,##0.00");
			m_invoiceVATAmountText.setValue(new Double(vat));
			
			if (m_currComp.getObject()!=null){
				if (((Currency)m_currComp.getObject()).getSymbol().equals(baseCurrency.getSymbol())){
					m_subTotalAmountText.setText(formatDesimal.format(subtotal));
					m_VATAmountText.setText(formatDesimal.format(vat));	
					m_totalAmountText.setText(formatDesimal.format(total));	
				}else{
					if (m_exchRateText.getValue()!=null){							
						m_subTotalAmountText.setText(formatDesimal.format(subtotal*((Number)m_exchRateText.getValue()).doubleValue()));
						m_VATAmountText.setText(formatDesimal.format(vat*((Number)m_exchRateText.getValue()).doubleValue()));			
						m_totalAmountText.setText(formatDesimal.format(total*((Number)m_exchRateText.getValue()).doubleValue()));
					}
				}
			}		
		}
		
		public void deleteRow(int row) {
			listData.remove(row);
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);			
		}
		
		private void updateNumbering() {
			for(int i=0; i<(getRowCount()-4); i++){								
				setValueAt(new Integer(i+1), i, 0);				
			}
		}
		
		public void clear(){
			listData.clear();
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
			model.addRow(new Object[]{null, null, null, null, null, null});			
			model.addRow(new Object[]{null, null, null, null, "SUBTOTAL", new Double(0)});
			model.addRow(new Object[]{null, null, null, null, "VAT", new Double(0)});
			model.addRow(new Object[]{null, null, null, null, "TOTAL", new Double(0)});
		}
		
		public void addRow(Object[] data){
			listData.add(data);
			DefaultTableModel model = (DefaultTableModel) getModel();
			int row = getRowCount()-5;
			model.insertRow(row+1,new Object[]{String.valueOf(row+2), data[0], data[1],data[2], data[3], data[4]});
			this.getSelectionModel().setSelectionInterval(row, row);
			updateNumbering();
		}
		
		public TableCellEditor getCellEditor(int row, int column) {
			if(column==3)
				//return new DblCellEditor();
				return new FormattedDoubleCellEditor(JLabel.RIGHT);
			if(column==4)
				//return new DblCellEditor();
				return new FormattedDoubleCellEditor(JLabel.RIGHT);
			return super.getCellEditor(row, column);
		}
		
		public TableCellRenderer getCellRenderer(int row, int column) {
			if(column==0)
				return new BaseTableCellRenderer();
			if(column>0){
				if(isSummaryRow(row)){
					if(column==4)					
						return new FormattedStandardCellRenderer(Font.BOLD, JLabel.RIGHT);
					if(column==5)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
				}else{
					if(column==3)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					if(column>=4)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
				}
			}
			return new FormattedStandardCellRenderer(Font.PLAIN, JLabel.LEFT);
		}
		
		public boolean isCellEditable(int row, int column) {
			if(column==0)
				return false;
			if(column>0)
				if(isSummaryRow(row))
					return false;
				else if(column==5)
					return false;				
			if (m_edit || m_new){
				return true;
			}
			return false;
		}
		
		private boolean isSummaryRow(int row) {
			int maxRow = getRowCount()-1;			
			return ((row>=(maxRow-3))&&(row<=maxRow));
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
	
	class PersonalTableModel extends DefaultTableModel implements ListSelectionListener {
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {		    
			return false;
		}
		
		public void valueChanged(ListSelectionEvent e) {
			if(!e.getValueIsAdjusting()){
				//int iRowIndex = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();
			}
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
	}*/


	protected void tableStopCellEditing() {
		m_table.stopCellEditing();
	}
	
}
