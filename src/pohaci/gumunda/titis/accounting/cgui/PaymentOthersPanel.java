package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.cgui.report.Vchr_Others;
import pohaci.gumunda.titis.accounting.dbapi.*;
import pohaci.gumunda.titis.accounting.entity.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.*;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class PaymentOthersPanel extends RevTransactionPanel 

implements ActionListener,PropertyChangeListener{	
	
	private static final long serialVersionUID = 1L;
	private PmtOthers m_entity;	
	protected Employee m_defaultOriginator,m_defaultApproved,m_defaultReceived;
	private BottomTabblePmtProject m_table;	
	private LookupPicker m_accountComp,m_paymentTypeComp;
	private JButton m_addOthersAccountBtn;
	private JTextField m_chequeNoText,m_payToText,m_refNoText,m_statusText,m_submittedDateText,m_unitText;
	private JButton m_deleteOthersAccountBtn;
	private LookupDepartmentPicker m_departmentComp;
	private JScrollPane m_descriptionSrollPane,m_remarksScrollPane,m_othersAccountScrollPane;
	private JTextArea m_descriptionTextArea,m_remarksTextArea;
	private JComboBox m_paymentSourceCombo;
	private pohaci.gumunda.titis.application.DatePicker m_transactionDateDate,m_chequeDueDateDate;
	private JPanel m_jPanel1,m_jPanel1_1,m_topButtonPanel,m_jPanel1_2,leftPanel,m_jPanel1_2_1_1,m_jPanel1_2_2,
	rightPanel,m_originatorCompPanel,m_approveCompPanel,m_receivedCompPanel,m_jPanel2,m_jPanel2_1,
	m_jPanel2_1_1;
	private AssignPanel m_originatorComp,m_approvedComp,m_receivedComp;
	private LookupReceiveTypeAnak m_paymentAccountComp;
	private JRadioButton m_singleRb = new JRadioButton("Single");
	private JRadioButton m_multipleRb = new JRadioButton("Multiple");
	
	public PaymentOthersPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		initTabel();
		enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid); 
		setDefaultSignature(); 
		setEntity(new PmtOthers());
		m_entityMapper = MasterMap.obtainMapperFor(PmtOthers.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	
	private void initComponents() {		
		JLabel voucherDateLbl = new JLabel("Voucher Date*");
		JLabel descriptionLbl = new JLabel("Description*");
		JLabel voucherNoLbl = new JLabel("Voucher No");
		JLabel remarksLbl = new JLabel("Remarks");
		JLabel submittedDateLbl = new JLabel("Submitted Date");
		JLabel paymentSourceLbl = new JLabel("Payment Source*");
		JLabel accountLbl = new JLabel("Account");
		JLabel chequeNoLbl = new JLabel("Cheque No");
		JLabel chequeDuDateLbl = new JLabel("Cheque Due Date");
		JLabel paymentTypeLbl = new JLabel("Payment Type*");
		JLabel payToLbl = new JLabel("Pay To*");
		JLabel unitLbl = new JLabel("Unit Code");
		JLabel statusLbl = new JLabel("Status");
		JLabel departmentLbl = new JLabel("Department");		
		m_chequeNoText = new JTextField();
		m_refNoText = new JTextField();
		m_statusText = new JTextField();
		m_submittedDateText = new JTextField();
		m_payToText = new JTextField();
		m_unitText = new JTextField();
		
		m_newBtn = new JButton("New");
		m_editBtn = new JButton("Edit");
		m_saveBtn = new JButton("Save");
		m_deleteBtn = new JButton("Delete");
		m_cancelBtn = new JButton("Cancel");
		m_submitBtn = new JButton("Submit");
		m_addOthersAccountBtn = new JButton();
		m_deleteOthersAccountBtn = new JButton();
		
		m_topButtonPanel = new JPanel();
		m_jPanel1 = new JPanel();
		m_jPanel1_1 = new JPanel();	
		m_jPanel1_2 = new JPanel();
		m_jPanel1_2_2 = new JPanel();
		rightPanel = new JPanel();
		m_jPanel1_2_1_1 = new JPanel();
		leftPanel = new JPanel();
		m_jPanel2 = new JPanel();
		m_jPanel2_1 = new JPanel();
		m_jPanel2_1_1 = new JPanel();
		m_originatorCompPanel = new JPanel();
		m_approveCompPanel = new JPanel();
		m_receivedCompPanel = new JPanel();
		
		m_descriptionSrollPane = new JScrollPane();
		m_remarksScrollPane = new JScrollPane();
		
		m_descriptionTextArea = new JTextArea();
		m_remarksTextArea = new JTextArea();
		
		m_transactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		
		m_paymentSourceCombo = new JComboBox();		
		m_chequeDueDateDate = new DatePicker();
		
		m_accountComp = new LookupCashAccountPicker(m_conn, m_sessionid);
		m_paymentTypeComp = new LookupReceiveCashBankType(m_conn, m_sessionid,"Others Payment");
		
		m_departmentComp = new LookupDepartmentPicker (m_conn, m_sessionid);
		
		m_originatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		m_approvedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		m_receivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");
		
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		
		m_othersAccountScrollPane = new JScrollPane();		
		m_table = new BottomTabblePmtProject();
		
		setLayout(new BorderLayout());
		GridBagConstraints gridBagConstraints;
		setPreferredSize(new Dimension(700, 600));
		m_jPanel1.setLayout(new BorderLayout());
		
		m_jPanel1.setPreferredSize(new Dimension(700, 400));
		m_jPanel1_1.setLayout(new BorderLayout());
		
		m_jPanel1_1.setPreferredSize(new Dimension(650, 35));
		m_topButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		
		m_topButtonPanel.setPreferredSize(new Dimension(650, 35));
		m_searchRefNoBtn.setPreferredSize(new Dimension(21, 21));
		m_topButtonPanel.add(m_searchRefNoBtn);
		
		m_printViewRefNoBtn.setPreferredSize(new Dimension(21, 21));
		m_topButtonPanel.add(m_printViewRefNoBtn);
		
		m_newBtn.setMargin(new Insets(2, 2, 2, 2));
		m_newBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_newBtn);
		
		m_editBtn.setMargin(new Insets(2, 2, 2, 2));
		m_editBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_editBtn);
		
		m_deleteBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deleteBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_deleteBtn);
		
		m_saveBtn.setMargin(new Insets(2, 2, 2, 2));
		m_saveBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_saveBtn);
		
		m_cancelBtn.setMargin(new Insets(2, 2, 2, 2));
		m_cancelBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_cancelBtn);
		
		m_submitBtn.setMargin(new Insets(2, 2, 2, 2));
		m_submitBtn.setPreferredSize(new Dimension(50, 20));
		m_topButtonPanel.add(m_submitBtn);
		
		m_jPanel1_1.add(m_topButtonPanel, BorderLayout.WEST);
		
		m_jPanel1.add(m_jPanel1_1, BorderLayout.NORTH);
		
		m_jPanel1_2.setLayout(new BorderLayout());
		
		m_jPanel1_2.setPreferredSize(new Dimension(700, 320));
		m_jPanel1_2_2.setLayout(new BorderLayout());
		
		m_jPanel1_2_2.setPreferredSize(new Dimension(415, 305));
		rightPanel.setLayout(new GridBagLayout());
		
		rightPanel.setPreferredSize(new Dimension(415, 400));
		
		voucherNoLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(voucherNoLbl, gridBagConstraints);
		
		m_refNoText.setPreferredSize(new Dimension(300, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;	
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(m_refNoText, gridBagConstraints);
		
		voucherDateLbl.setPreferredSize(new Dimension(100, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(voucherDateLbl, gridBagConstraints);
		
		descriptionLbl.setPreferredSize(new Dimension(85, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(descriptionLbl, gridBagConstraints);
		
		m_descriptionSrollPane.setPreferredSize(new Dimension(300, 90));
		m_descriptionTextArea.setColumns(20);
		m_descriptionTextArea.setLineWrap(true);
		m_descriptionTextArea.setRows(5);
		m_descriptionSrollPane.setViewportView(m_descriptionTextArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(m_descriptionSrollPane, gridBagConstraints);
		
		remarksLbl.setPreferredSize(new Dimension(90, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(remarksLbl, gridBagConstraints);
		
		m_remarksScrollPane.setPreferredSize(new Dimension(300, 80));
		m_remarksTextArea.setColumns(20);
		m_remarksTextArea.setLineWrap(true);
		m_remarksTextArea.setRows(5);
		m_remarksScrollPane.setViewportView(m_remarksTextArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(m_remarksScrollPane, gridBagConstraints);
		
		JPanel emptypanel = new JPanel();
		emptypanel.setPreferredSize(new Dimension(300, 30));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		rightPanel.add(emptypanel, gridBagConstraints);
		
		m_transactionDateDate.setPreferredSize(new Dimension(300, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		rightPanel.add(m_transactionDateDate, gridBagConstraints);
		
		m_jPanel1_2_2.add(rightPanel, BorderLayout.WEST);
		
		m_jPanel1_2.add(m_jPanel1_2_2, BorderLayout.CENTER);
		
		leftPanel.setLayout(new GridBagLayout());		
		leftPanel.setPreferredSize(new Dimension(420, 320));		
		statusLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(statusLbl, gridBagConstraints);
		
		submittedDateLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(submittedDateLbl, gridBagConstraints);
		
		paymentSourceLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(paymentSourceLbl, gridBagConstraints);
		
		accountLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(accountLbl, gridBagConstraints);
		
		chequeNoLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(chequeNoLbl, gridBagConstraints);
		
		chequeDuDateLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(chequeDuDateLbl, gridBagConstraints);
		
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
		
		m_paymentSourceCombo.setModel(new DefaultComboBoxModel(new String[] { "Cash","Bank" }));
		m_paymentSourceCombo.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_paymentSourceCombo, gridBagConstraints);
		
		m_accountComp.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_accountComp, gridBagConstraints);
		
		m_chequeNoText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_chequeNoText, gridBagConstraints);
		
		m_chequeDueDateDate.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_chequeDueDateDate, gridBagConstraints);
		
		paymentTypeLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(paymentTypeLbl, gridBagConstraints);		
		
		unitLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(unitLbl, gridBagConstraints);
		
		departmentLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(departmentLbl, gridBagConstraints);
		
		payToLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(payToLbl, gridBagConstraints);
		
		m_paymentTypeComp.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_paymentTypeComp, gridBagConstraints);
		
		m_jPanel1_2_1_1.setLayout(new GridBagLayout());
		
		m_jPanel1_2_1_1.setPreferredSize(new Dimension(200, 23));
		
		m_unitText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_unitText, gridBagConstraints);
		
		JPanel groupdeptPanel = new JPanel();
		ButtonGroup bg1 = new ButtonGroup();
	    bg1.add(m_singleRb);
	    bg1.add(m_multipleRb);
	    m_singleRb.setSelected(true);
	    
	    groupdeptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Department",
	            javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11)));
	    groupdeptPanel.add(m_singleRb);
	    groupdeptPanel.add(m_multipleRb);
	    
	    groupdeptPanel.setPreferredSize(new Dimension(290, 50));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(groupdeptPanel, gridBagConstraints);
	    
		m_departmentComp.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_departmentComp, gridBagConstraints);
		
		m_payToText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		leftPanel.add(m_payToText, gridBagConstraints);
		
		m_jPanel1_2.add(leftPanel, BorderLayout.WEST);
		
		m_jPanel1.add(m_jPanel1_2, BorderLayout.CENTER);
		
		m_originatorCompPanel.setLayout(new BorderLayout());
		
		m_originatorCompPanel.setPreferredSize(new Dimension(700, 90));
		m_originatorComp.setLayout(new GridBagLayout());
		
		m_originatorComp.setBorder(BorderFactory.createTitledBorder("Originator"));
		m_originatorComp.setOpaque(false);
		m_originatorComp.setPreferredSize(new Dimension(275,110));
		
		m_originatorCompPanel.add(m_originatorComp, BorderLayout.WEST);
		
		m_approveCompPanel.setLayout(new BorderLayout());
		
		m_approvedComp.setLayout(new GridBagLayout());
		
		m_approvedComp.setBorder(BorderFactory.createTitledBorder("Approved by"));
		m_approvedComp.setOpaque(false);
		m_approvedComp.setPreferredSize(new Dimension(275,110));
		
		m_approveCompPanel.add(m_approvedComp, BorderLayout.WEST);
		
		m_receivedCompPanel.setLayout(new BorderLayout());
		
		m_receivedComp.setLayout(new GridBagLayout());
		
		m_receivedComp.setBorder(BorderFactory.createTitledBorder("Received by"));
		m_receivedComp.setOpaque(false);
		m_receivedComp.setPreferredSize(new Dimension(275,110));
		
		m_receivedCompPanel.add(m_receivedComp, BorderLayout.WEST);
		
		m_approveCompPanel.add(m_receivedCompPanel, BorderLayout.CENTER);
		
		m_originatorCompPanel.add(m_approveCompPanel, BorderLayout.CENTER);
		
		m_jPanel1.add(m_originatorCompPanel, BorderLayout.SOUTH);
		
		add(m_jPanel1, BorderLayout.NORTH);
		
		m_jPanel2.setLayout(new BorderLayout());
		
		m_jPanel2_1.setLayout(new BorderLayout());
		
		m_jPanel2_1_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		
		m_jPanel2_1_1.setPreferredSize(new Dimension(650, 35));
		m_addOthersAccountBtn.setText("Add");
		m_addOthersAccountBtn.setMargin(new Insets(2, 2, 2, 2));
		m_addOthersAccountBtn.setPreferredSize(new Dimension(50, 21));
		m_jPanel2_1_1.add(m_addOthersAccountBtn);
		
		m_deleteOthersAccountBtn.setText("Delete");
		m_deleteOthersAccountBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deleteOthersAccountBtn.setPreferredSize(new Dimension(50, 21));
		m_jPanel2_1_1.add(m_deleteOthersAccountBtn);
		
		m_jPanel2_1.add(m_jPanel2_1_1, BorderLayout.WEST);
		
		m_jPanel2.add(m_jPanel2_1, BorderLayout.NORTH);
		
		m_othersAccountScrollPane.setPreferredSize(new Dimension(650, 215));
		m_othersAccountScrollPane.setViewportView(m_table);
		
		m_jPanel2.add(m_othersAccountScrollPane, BorderLayout.CENTER);
		m_jPanel2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED ));
		
		add(m_jPanel2, BorderLayout.CENTER);
		
	}
	
	private void addingListener(){
		m_paymentSourceCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);	
		m_paymentTypeComp.addPropertyChangeListener("object",this);
		accountComp().addPropertyChangeListener("object",this);
		m_addOthersAccountBtn.addActionListener(this);
		m_deleteOthersAccountBtn.addActionListener(this);
		m_singleRb.addActionListener(this);
		m_multipleRb.addActionListener(this);
		m_departmentComp.addPropertyChangeListener(this);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_printViewRefNoBtn){
			if (m_entity.getIndex()>0)
				new Vchr_Others(m_entity,m_conn,m_sessionid);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame.getMainFrame(), "Search Voucher", m_conn, m_sessionid,
					new   UnitBankCashTransferLoader(m_conn,PmtOthers.class),"Others",true );
			dlg.setVisible(true);
			if (dlg.selectedObj != null){
				doLoad(dlg.selectedObj);				
			}
		}
		else if(e.getSource()==m_addOthersAccountBtn){			
			if (m_paymentTypeComp.getObject()!=null){
				JournalStandard js = (JournalStandard)m_paymentTypeComp.getObject();
				m_paymentAccountComp =  new LookupReceiveTypeAnak(m_conn, m_sessionid,js,"Others Payment Type");
				m_paymentAccountComp.done();
				if(m_paymentAccountComp.getObject()!=null){
					int row = m_table.getRowCount()-2;
					Vector temp1=new Vector();
					JournalStandardAccount journal = (JournalStandardAccount)m_paymentAccountComp.getObject();
					Account acc=journal.getAccount();		
					m_nonDeleteList.add(new String("0"));
					m_posDebCreBalance.add(new String(""));
					temp1.addElement(null);
					temp1.addElement(acc.toStringWithCode());
					temp1.addElement(null);
					temp1.addElement(null);
					temp1.addElement(new Double(0));
					temp1.addElement(new Double(0));    	
					temp1.addElement(baseCurrency);	
					temp1.addElement(new Double(1));
					if (m_singleRb.isSelected())
						if (m_departmentComp.getObject() instanceof Organization) {
							Organization org = (Organization) m_departmentComp.getObject();
							temp1.addElement(org);
						}
					m_table.addData(temp1, journal,row);
				}
			}else{
				JOptionPane.showMessageDialog(this, "Please select receive type first",
						"Warning", JOptionPane.WARNING_MESSAGE);			
			}
		}
		else if (e.getSource()==m_deleteOthersAccountBtn){
			int row = m_table.getSelectedRow();
			int maxRow = m_table.getRowCount()-1;			
			if(row>-1){				
				String nil= ((String)m_nonDeleteList.get(row)).toString();
				if(row<(maxRow-1)){
					if (nil.equals("0")){
						m_nonDeleteList.remove(row);
						m_table.removeData(row);
						m_table.updateSummary();
					}
				}				
			}
		}
		else if (e.getSource()==m_singleRb){
			m_departmentComp.setEnabled(true);
			int maxRow = m_table.getRowCount()-2;
			for (int i =0;i<maxRow;i++){
				if (m_departmentComp.getObject() instanceof Organization) {
					Organization dept= (Organization) m_departmentComp.getObject();
					m_table.setValueAt(dept,i,8);
				}
			}
		}
		else if (e.getSource()==m_multipleRb){
			m_departmentComp.setEnabled(false);
			m_departmentComp.setObject(null);
			int maxRow = m_table.getRowCount()-2;
			for (int i =0;i<maxRow;i++){
				m_table.setValueAt("",i,8);					
			}
		}
	}
	
	public void doEdit() {
		super.doEdit();
		setEnableButtonBawah(true);
	}
	
	
	
	protected void doSubmit() {
		validityMsgs.clear();		
		if (entity().getBankAccount()== null && entity().getCashAccount()==null)
			addInvalid("Source account must be selected");
		if (!m_table.cekTotalDebitCredit()) 
			addInvalid("Transaction is unbalanced. Please check accounts");
			
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
		m_cekBalance = false;
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
		setEnableButtonBawah(true);
		this.m_chequeNoText.setEditable(false);
		this.m_chequeDueDateDate.setEditable(false);
	}
	
	protected void clearAll() {
		m_cekBalance = false;
		super.clearAll();
		clearForm();
		clearKomponen();
		disableEditMode();
	}
	
	protected void deleteChilds() {		
		super.deleteChilds();
		DeleteAnaknya(m_entity);		
	}
	
	public void clearKomponen(){
		m_statusText.setText("");
		m_originatorComp.setEmployee(null);
		m_approvedComp.setEmployee(null);
		m_receivedComp.setEmployee(null);
		m_originatorComp.m_jobTextField.setText("");
		m_approvedComp.m_jobTextField.setText("");
		m_receivedComp.m_jobTextField.setText("");
		m_originatorComp.setDate(null);
		m_approvedComp.setDate(null);
		m_receivedComp.setDate(null);
		m_transactionDateDate.setDate(null);
	}
	
	public void LoadDetail(long index){
		GenericMapper mapper2=MasterMap.obtainMapperFor(PmtOthersDetail.class);
		mapper2.setActiveConn(m_conn);		
		List rs=mapper2.doSelectWhere(IDBConstants.ATTR_PMT_OTHERS+"="+index);
		m_table.clearTable();
		PmtOthersDetail detail;
		Vector temp1;
		Account acc;		
		for(int i=0;i<rs.size();i++){
			temp1=new Vector();
			detail=(PmtOthersDetail)rs.get(i);			
			acc=(Account)detail.getAccount();
			
			JournalStandard js = (JournalStandard)m_paymentTypeComp.getObject();			
			GenericMapper mapper3=MasterMap.obtainMapperFor(JournalStandardAccount.class);
			mapper3.setActiveConn(m_conn);		
			List rs3=mapper3.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+js.getIndex() + " AND " + 
					IDBConstants.ATTR_ACCOUNT + "=" + acc.getIndex());			
			JournalStandardAccount jsAccount;
			temp1.addElement(null);
			temp1.addElement(acc.toStringWithCode());
			temp1.addElement(detail.getDescription());
			String SubsidiaryAccSet = getSubsidiaryByindex(acc);
			AccountingBusinessLogic accLogic = new AccountingBusinessLogic(m_conn);
			ProjectBusinessLogic proLogic = new ProjectBusinessLogic(m_conn);
			HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
			if (SubsidiaryAccSet.equals("Employee")){				
				SetEmployeeToTable(detail, temp1, hrmLogic);					
			}else if (SubsidiaryAccSet.equals("Partner")){
				SetPartnerToTable(detail, temp1, proLogic);				
			}else if (SubsidiaryAccSet.equals("Customer")){
				SetCustomerToTable(detail, temp1, proLogic);
			}else if (SubsidiaryAccSet.equals("Bank")){			
				SetBankAccountToTable(detail, temp1, accLogic);				
			}else if (SubsidiaryAccSet.equals("Cash")){
				SetCashAccountToTable(detail, temp1, accLogic);
			}else if (SubsidiaryAccSet.equals("Loan")){
				SetCompanyLoanToTable(detail, temp1, accLogic);
			}else if (SubsidiaryAccSet.equals("Project")){
				SetProjectToTable(detail, temp1, proLogic );
			}else{
				temp1.addElement("");
			}
			
			Account accBankCash = null;			
			if (entity().getBankAccount()!=null){
				accBankCash = entity().getBankAccount().getAccount(); 
			}else if (entity().getCashAccount()!=null){
				accBankCash = entity().getCashAccount().getAccount();
			}
			
			if (rs3.size()>0){
				jsAccount=(JournalStandardAccount)rs3.get(0);
				if (detail.getAccount().getIndex()==jsAccount.getAccount().getIndex()){					
					if (jsAccount.m_iscalculate){
						m_nonDeleteList.add(new String("1"));	
						m_cekBalance = true;
						if (jsAccount.getBalance()==0)
							m_posDebCreBalance.add(new String("0"));
						else
							m_posDebCreBalance.add(new String("1"));						
					}else if (accBankCash!=null){
						if (accBankCash.getIndex()==jsAccount.getAccount().getIndex()){
							m_posDebCreBalance.add(new String(""));
							m_nonDeleteList.add(new String("2"));						
						}else{
							m_nonDeleteList.add(new String("0"));
						}
					}else {
						m_posDebCreBalance.add(new String(""));
						m_nonDeleteList.add(new String("0"));
					}						
					if (jsAccount.getBalance()==0){
						temp1.addElement(new Double(detail.getAccValue()));
						temp1.addElement(new Double(0));
					}else if (jsAccount.getBalance()==1){
						temp1.addElement(new Double(0));
						temp1.addElement(new Double(detail.getAccValue()));
					}
					temp1.addElement(detail.getCurrency());			
					temp1.addElement(new Double(detail.getExchangeRate()));
					if (detail.getDepartment()!=null)
						temp1.addElement(detail.getDepartment());
					m_table.addData(temp1, jsAccount,i);	
				}
			}
		}
	}
	
	private void SetCompanyLoanToTable(PmtOthersDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
		CompanyLoan loan = null;
		try {
			loan = accLogic.getCompanyLoan(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (loan!=null)
			temp1.addElement(loan);				
		else
			temp1.addElement(null);
	}
	
	private void SetProjectToTable(PmtOthersDetail detail, Vector temp1, ProjectBusinessLogic  proLogic) {
		ProjectData  project = null;
		try {
			project = proLogic.getProjectDataByIndex(detail.getSubsidiAry());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (project!=null)
			temp1.addElement(project);				
		else
			temp1.addElement(null);
	}
	
	private void SetCashAccountToTable(PmtOthersDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
		CashAccount cAcc = null;
		try {
			cAcc = accLogic.getCashAccountByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cAcc!=null)
			temp1.addElement(cAcc);				
		else
			temp1.addElement(null);
	}
	
	private void SetBankAccountToTable(PmtOthersDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
		BankAccount bAcc = null;
		try {
			bAcc = accLogic.getBankAccountByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (bAcc!=null)
			temp1.addElement(bAcc);
		else
			temp1.addElement(null);
	}
	
	private void SetCustomerToTable(PmtOthersDetail detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Customer cust = null;
		cust = proLogic.getCustomerByIndex(detail.getSubsidiAry());
		temp1.addElement(cust);
	}
	
	private void SetPartnerToTable(PmtOthersDetail detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Partner partn = null;
		try {
			partn = proLogic.getPartnerByIndex(detail.getSubsidiAry());
		} catch (Exception e) {				
			e.printStackTrace();
		}
		if (partn!=null)
			temp1.addElement(partn);
		else
			temp1.addElement(null);
	}
	
	private void SetEmployeeToTable(PmtOthersDetail detail, Vector temp1, HRMBusinessLogic hrmLogic) {
		Employee emp = null;
		try {
			emp = hrmLogic.getEmployeeByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
		} catch (Exception e) {					
			e.printStackTrace();
		}
		if (emp!=null)
			temp1.addElement(emp);
		else
			temp1.addElement(null);
	}
	
	private void isiDefaultAssignPanel(){
		m_originatorComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultOriginator));
		m_approvedComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultApproved));
		m_receivedComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultReceived));
	}    
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	PmtOthers entity() {
		return m_entity;
	}
	
	protected void gui2entity() {
		String paySource = (String) m_paymentSourceCombo.getSelectedItem();
		entity().setPaymentSource(paySource);
		Object Objacc = m_accountComp.getObject();
		if (Objacc instanceof CashAccount) {
			CashAccount cash = (CashAccount) Objacc;
			entity().setCashAccount(cash);
			entity().setBankAccount(null);
			entity().setUnit(cash.getUnit());
			
		}else if (Objacc instanceof BankAccount) {
			BankAccount bank = (BankAccount) Objacc;
			entity().setBankAccount(bank);
			entity().setCashAccount(null);
			entity().setUnit(bank.getUnit());
		}else{
			entity().setBankAccount(null);
			entity().setCashAccount(null);
			Unit unit = DefaultUnit.createDefaultUnit(m_conn,m_sessionid);
			entity().setUnit(unit);
		}
		
		entity().setChequeNo(m_chequeNoText.getText());
		entity().setChequeDueDate(m_chequeDueDateDate.getDate());
		entity().setJournal((JournalStandard)m_paymentTypeComp.getObject());
		if (m_singleRb.isSelected())
			entity().setDepartmentgroup(0);
		else if (m_multipleRb.isSelected())
			entity().setDepartmentgroup(1);
			
		entity().setDepartment((Organization) m_departmentComp.getObject());
		entity().setPayTo(m_payToText.getText());
		entity().setCurrency(baseCurrency);
		entity().setExchangeRate(0);
		entity().setTransactionDate(m_transactionDateDate.getDate());
		entity().setRemarks(m_remarksTextArea.getText());
		entity().transTemplateRead(
				this.m_originatorComp, this.m_approvedComp,
				this.m_receivedComp, this.m_refNoText,
				this.m_descriptionTextArea
		);
		detailAccountOperation();
	}
	
	
	private void detailAccountOperation() {
		if (m_table.getRowCount()>=0){
			PmtOthersDetail[] temp=new PmtOthersDetail[m_table.getDataCount()];
			double total = 0; // i add this
			for (int i=0;i<m_table.getDataCount();i++){
				JournalStandardAccount journal = (JournalStandardAccount)m_table.getListData().get(i);
				Account acc = journal.getAccount();
				temp[i]=new PmtOthersDetail();
				temp[i].setAccount(acc);
				if (m_table.getValueAt(i,2)!=null)
					temp[i].setDescription(m_table.getValueAt(i,2).toString());
				else
					temp[i].setDescription("");				
				String SubsidiaryAccSet = getSubsidiaryByindex(acc);				
				Object value = m_table.getValueAt(i,3);
				if (SubsidiaryAccSet.equals("Employee")){	
					if (value instanceof Employee) {
						Employee emp = (Employee) value;
						temp[i].setSubsidiAry(emp.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
						//temp[i].setSubsidiAry(-1);
				}else if ((SubsidiaryAccSet.equals("Partner"))){
					if (value instanceof Partner) {
						Partner partn = (Partner) value;
						temp[i].setSubsidiAry(partn.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
						//temp[i].setSubsidiAry(-1);					
				}else if ((SubsidiaryAccSet.equals("Customer"))){
					if (value instanceof Customer) {
						Customer cust = (Customer) value;
						temp[i].setSubsidiAry(cust.getIndex());
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
						//temp[i].setSubsidiAry(-1);					
				}else if ((SubsidiaryAccSet.equals("Cash"))){
					if (value instanceof CashAccount) {
						CashAccount cash = (CashAccount) value;
						temp[i].setSubsidiAry(cash.getIndex());
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
						//temp[i].setSubsidiAry(-1);					
				}else if ((SubsidiaryAccSet.equals("Bank"))){
					if (value instanceof BankAccount) {
						BankAccount bank = (BankAccount) value;
						temp[i].setSubsidiAry(bank.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
						//temp[i].setSubsidiAry(-1);					
				}else if ((SubsidiaryAccSet.equals("Loan"))){
					if (value instanceof CompanyLoan) {
						CompanyLoan loan = (CompanyLoan) value;
						temp[i].setSubsidiAry(loan.getIndex());						
					}else{
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
						//temp[i].setSubsidiAry(-1);
					}
				}else if ((SubsidiaryAccSet.equals("Project"))){
					if (value instanceof ProjectData) {
						ProjectData project = (ProjectData) value;
						temp[i].setSubsidiAry(project.getIndex());						
					}else{
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
						//temp[i].setSubsidiAry(-1);
					}
				}else
					temp[i].setSubsidiAry(-1);				
				if (journal.getBalanceAsString().equalsIgnoreCase(Account.STR_DEBET)){
					temp[i].setAccValue(((Double)m_table.getValueAt(i,4)).doubleValue());
					if (!journal.isCalculate() && !journal.isHidden()) 
						total += ((Double)m_table.getValueAt(i,4)).doubleValue(); // i add this
				}else if (journal.getBalanceAsString().equalsIgnoreCase(Account.STR_CREDIT)){
					temp[i].setAccValue(((Double)m_table.getValueAt(i,5)).doubleValue());
					if (!journal.isCalculate() && !journal.isHidden()) 
						total -= ((Double)m_table.getValueAt(i,5)).doubleValue(); // i add this
				}
				Currency curr = null;
				if (m_table.getValueAt(i,6) instanceof Currency) {
					curr = (Currency) m_table.getValueAt(i,6);					
				}
				temp[i].setCurrency(curr);
				temp[i].setExchangeRate(((Double)m_table.getValueAt(i,7)).doubleValue());
				Object objdept = m_table.getValueAt(i,8);
				if (objdept instanceof Organization) {
					Organization org = (Organization) objdept;
					temp[i].setDepartment(org);
				}else{
					temp[i].setDepartment(null);
				}				
			}
			entity().setPmtOthersDetail(temp);
			entity().setTotal(total); // i add this
		}
	}
	
	protected void entity2gui() {
		m_paymentTypeComp.setObject(entity().getJournal()); 
		if(entity().getBankAccount()!=null){
			m_paymentSourceCombo.setSelectedItem("Bank");
			m_accountComp.setObject(entity().getBankAccount());
		}else if (entity().getCashAccount()!=null){
			m_paymentSourceCombo.setSelectedItem("Cash");
			m_accountComp.setObject(entity().getCashAccount());    		
		}else{
			m_accountComp.setObject(null);
			m_unitText.setText("");	
		}
		m_chequeNoText.setText(entity().getChequeNo());
		m_chequeDueDateDate.setDate(entity().getChequeDueDate());
		m_paymentTypeComp.setObject(entity().getJournal());
		if (entity().getDepartmentgroup()==0)
			m_singleRb.setSelected(true);
		else if (entity().getDepartmentgroup()==1)
			m_multipleRb.setSelected(true);
		m_departmentComp.setObject(entity().getDepartment());
		m_payToText.setText(entity().getPayTo());
		
		m_refNoText.setText(entity().getReferenceNo());
		if(entity().getTransactionDate()!=null)
			m_transactionDateDate.setDate(entity().getTransactionDate());
		else{
			m_transactionDateDate.setDate(new Date());
		}
		
		m_descriptionTextArea.setText(entity().getDescription());
		m_remarksTextArea.setText(entity().getRemarks());
		
		m_originatorComp.setEmployee(entity().getEmpOriginator());
		m_originatorComp.setDate(entity().getDateOriginator());
		
		m_approvedComp.setEmployee(entity().getEmpApproved());
		m_approvedComp.setDate(entity().getDateApproved());
		
		m_receivedComp.setEmployee(entity().getEmpReceived());
		m_receivedComp.setDate(entity().getDateReceived());
		
		m_originatorComp.setJobTitle(entity().getJobTitleOriginator());
		m_approvedComp.setJobTitle(entity().getJobTitleApproved());
		m_receivedComp.setJobTitle(entity().getJobTitleReceived());	
		m_statusText.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			m_submittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			m_submittedDateText.setText("");
		LoadDetail(entity().getIndex());
	}
	
	protected void enableEditMode(){
		this.m_accountComp.setEnabled(true);
		this.m_chequeNoText.setEditable(true);
		this.m_chequeDueDateDate.setEditable(true);
		this.m_paymentTypeComp.setEnabled(true);		
		this.m_transactionDateDate.setEditable(true);
		this.m_descriptionTextArea.setEnabled(true);
		this.m_remarksTextArea.setEnabled(true);
		this.m_originatorComp.setEnabled(true);
		this.m_approvedComp.setEnabled(true);
		this.m_receivedComp.setEnabled(true);
		m_payToText.setEditable(true);
		m_table.setEnabled(true);
		m_singleRb.setEnabled(true);
		m_multipleRb.setEnabled(true);
		if (m_singleRb.isSelected())
			this.m_departmentComp.setEnabled(true);
		else if (m_multipleRb.isSelected())
			this.m_departmentComp.setEnabled(false);
	}
	
	protected void disableEditMode(){
		this.m_paymentSourceCombo.setEnabled(false);
		this.m_accountComp.setEnabled(false);
		this.m_chequeNoText.setEditable(false);
		this.m_chequeDueDateDate.setEditable(false);
		this.m_paymentTypeComp.setEnabled(false);
		this.m_departmentComp.setEnabled(false);
		this.m_transactionDateDate.setEditable(false);
		this.m_descriptionTextArea.setEnabled(false);
		this.m_remarksTextArea.setEnabled(false);
		this.m_originatorComp.setEnabled(false);
		this.m_approvedComp.setEnabled(false);
		this.m_receivedComp.setEnabled(false);
		m_payToText.setEditable(false);
		m_table.setEnabled(false);
		m_singleRb.setEnabled(false);
		m_multipleRb.setEnabled(false);
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();		
		if (m_paymentTypeComp.getObject()== null)
			addInvalid("Payment Type must be selected");
		if (m_payToText.getText().equals(""))
			addInvalid("Pay To must be inserted");
		if (cekdepartmentdetail())
			addInvalid("Department detail is still empty");
		if (m_transactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (m_descriptionTextArea.getText().equals(""))
			addInvalid("Description must be inserted");
		detailAccountOperation();
		PmtOthersDetail[] temp=entity().getPmtOthersDetail();
		if(temp.length==0)
			addInvalid("Account Detail must be added");
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
	
	public boolean cekdepartmentdetail(){
		boolean cek = false;
		int maxRow = m_table.getRowCount() - 2;
		for (int i=0;i<maxRow;i++){
			if (m_table.getValueAt(i,8)==null)				
				cek =true;
			else if (m_table.getValueAt(i,8).equals(""))
				cek=true;
				
		}
		return cek;
	}
	
	private void addInvalid(String string) {
		validityMsgs.add(string);
	}
	
	protected Object createNew(){
		PmtOthers a  = new PmtOthers();
		a.setEmpOriginator(m_defaultOriginator);
		a.setEmpApproved(m_defaultApproved);
		a.setEmpReceived(m_defaultReceived);
		return a ;
	}
	
	void setEntity(Object m_entity) {
		PmtOthers oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PmtOthers)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}
	
	Currency m_currCashBank= null;	
	public void propertyChange(PropertyChangeEvent evt) {	
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		else if(evt.getSource() == m_departmentComp){
			if (m_singleRb.isSelected()){
				int maxRow = m_table.getRowCount()-2;
				for (int i =0;i<maxRow;i++){
					if (m_departmentComp.getObject() instanceof Organization) {
						Organization dept= (Organization) m_departmentComp.getObject();
						m_table.setValueAt(dept,i,8);
					}
				}
			}
			
		}
		else if (evt.getSource() ==m_paymentTypeComp){
			if (m_paymentTypeComp.getObject()!=null){
				JournalStandard js = (JournalStandard)m_paymentTypeComp.getObject();
				GenericMapper mapper2=MasterMap.obtainMapperFor(JournalStandardSetting.class);
				mapper2.setActiveConn(m_conn);
				List rs=mapper2.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+js.getIndex());				
				JournalStandardSetting jsSetting = (JournalStandardSetting)rs.get(0);
				leftPanel.remove(accountComp());			
				leftPanel.revalidate();
				leftPanel.repaint();
				if (jsSetting.getAttribute().equals(IConstants.ATTR_PMT_CASH)){	
					m_paymentSourceCombo.setSelectedItem("Cash");		
					setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));	
					m_chequeNoText.setEditable(false);
					m_chequeDueDateDate.setEditable(false);
				}else{
					m_paymentSourceCombo.setSelectedItem("Bank");		
					setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid,js));
					m_chequeNoText.setEditable(true);
					m_chequeDueDateDate.setEditable(true);
				}
				accountComp().setPreferredSize(new Dimension(290, 18));
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 1;
				gridBagConstraints.gridy = 4;
				gridBagConstraints.anchor = GridBagConstraints.WEST;
				gridBagConstraints.insets = new Insets(1, 3, 1, 1);
				leftPanel.add(accountComp(), gridBagConstraints);				
				leftPanel.revalidate();
				setEntitasGui(js.getIndex());
			}
		}
		else if (evt.getSource() == accountComp()){
			if (m_paymentTypeComp.getObject()!=null){
				BankAccount bankAccount = null;
				CashAccount cashAccount = null;
				Account acc = null;
				JournalStandard js = (JournalStandard)m_paymentTypeComp.getObject();												
				if (evt.getNewValue() == null){
					m_unitText.setText("");	
				} else if (evt.getNewValue() instanceof BankAccount){
					bankAccount = (BankAccount)accountComp().getObject();
					m_unitText.setText(bankAccount.getUnit().toString());
					m_currCashBank = bankAccount.getCurrency();
				} else if (evt.getNewValue() instanceof CashAccount){
					cashAccount = (CashAccount)accountComp().getObject();					
					m_unitText.setText(cashAccount.getUnit().toString());
					m_currCashBank = cashAccount.getCurrency();
				}				
				if (cashAccount!=null)		
					acc=cashAccount.getAccount();
				else if (bankAccount!=null)
					acc = bankAccount.getAccount();			
				if (acc!=null){					
					GenericMapper mapper2=MasterMap.obtainMapperFor(JournalStandardAccount.class);
					mapper2.setActiveConn(m_conn);
					List rs=mapper2.doSelectWhere(IDBConstants.ATTR_JOURNAL +"=" + js.getIndex() + " AND " +
							IDBConstants.ATTR_ACCOUNT+"="+acc.getIndex());
					JournalStandardAccount jsAccount= (JournalStandardAccount)rs.get(0);
					acc = jsAccount.getAccount();					
					Vector temp1=new Vector();					
					String[] data = (String[])m_nonDeleteList.toArray(new String[m_nonDeleteList.size()]);					
					boolean find = false;
					int rowFind = -1;
					for (int i=0;i<data.length;i++){
						if (data[i].equals("2")){
							find = true;	
							rowFind = i;
						}
					}
					if (find == true){						
						m_nonDeleteList.remove(rowFind);
						m_posDebCreBalance.remove(rowFind);						
						m_table.removeData(rowFind);						
						m_nonDeleteList.add(rowFind,new String("2"));
						m_posDebCreBalance.add(rowFind,new String(""));
						temp1.addElement(null);
						temp1.addElement(acc.toStringWithCode());
						temp1.addElement(null);
						if (bankAccount!=null)
							temp1.addElement(bankAccount);
						else
							temp1.addElement(cashAccount);						
						temp1.addElement(new Double(0));
						temp1.addElement(new Double(0));
						temp1.addElement(m_currCashBank);
						temp1.addElement(new Double(1));
						m_table.insertData(temp1, jsAccount,rowFind);						
					}else{
						m_nonDeleteList.add(new String("2"));
						m_posDebCreBalance.add(new String(""));
						int row = (m_table.getRowCount()-2);						
						temp1.addElement(null);
						temp1.addElement(acc.toStringWithCode());
						temp1.addElement(null);
						if (bankAccount!=null)
							temp1.addElement(bankAccount);
						else
							temp1.addElement(cashAccount);						
						temp1.addElement(new Double(0));
						temp1.addElement(new Double(0));
						temp1.addElement(m_currCashBank);
						temp1.addElement(new Double(1));
						m_table.addData(temp1, jsAccount,row);						
					}
				}
			}
		}
	}
	
	
	ArrayList m_nonDeleteList = new ArrayList(); 
	boolean m_cekBalance = false;  
	ArrayList m_posDebCreBalance = new ArrayList();  
	public void setEntitasGui(long jsIndex){
		m_table.clearTable();
		m_accountComp.setEnabled(true);
		m_unitText.setText("");		
		GenericMapper mapper2=MasterMap.obtainMapperFor(JournalStandardAccount.class);
		mapper2.setActiveConn(m_conn);
		List rs=mapper2.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+jsIndex);
		JournalStandardAccount jsAccount;
		for (int i=0;i<rs.size();i++){
			jsAccount = (JournalStandardAccount)rs.get(i);
			if (jsAccount.isCalculate()){
				m_cekBalance = true;									
				m_nonDeleteList.add(new String("1"));
				if (jsAccount.getBalance()==0)
					m_posDebCreBalance.add(new String("0"));
				else
					m_posDebCreBalance.add(new String("1"));
				
				Vector temp1=new Vector();
				Account acc = jsAccount.getAccount();				
				int row = (m_table.getRowCount()-2);
				
				temp1.addElement(null);
				temp1.addElement(acc.toStringWithCode());
				temp1.addElement(null);
				temp1.addElement(null);
				temp1.addElement(new Double(0));
				temp1.addElement(new Double(0));
				temp1.addElement(baseCurrency);
				temp1.addElement(new Double(1));
				m_table.addData(temp1, jsAccount,row);
			}
		}
	}
	
	protected void setAccountComp(LookupPicker accountComp) {
		LookupPicker old = m_accountComp;
		if (old!=null){
			old.removePropertyChangeListener("object",this);
		}
		m_accountComp = accountComp;
		m_accountComp.addPropertyChangeListener("object",this); 
	}	
	
	private LookupPicker accountComp() {
		return m_accountComp;
	}
	
	private void enableAwal(){ 	
		setenableEditPanel(leftPanel,false);
		setenableEditPanel(m_jPanel1_2_1_1,false);
		setenableEditPanel(rightPanel,false);
		setEnableButtonBawah(false);
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_OTHERS, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			m_defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_OTHERS, Signature.SIGN_APPROVED);
		if(sign!=null)
			m_defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_OTHERS, Signature.SIGN_RECEIVED);
		if(sign!=null)
			m_defaultReceived = sign.getFullEmployee();
	}
	
	public void initTabel(){
		m_table=new BottomTabblePmtProject();
		m_othersAccountScrollPane.setViewportView(m_table);
	}
	
	protected void doSave() {
		if (!cekValidity()) 
			return;	
		
		super.doSave();
		setEnableButtonBawah(false);
		AccountingSQLSAP sql=new AccountingSQLSAP();
		GenericMapper mapper2=MasterMap.obtainMapperFor(PmtOthersDetail.class);
		mapper2.setActiveConn(m_conn);
		try {
			if ((new Long(entity().getIndex()).toString().equalsIgnoreCase("0") )){	
				long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_OTHERS, m_conn);
				entity().setIndex(index);
			}
			System.out.println(entity().getIndex());
			if (entity().getPmtOthersDetail()!=null){
				PmtOthersDetail temp[]=entity().getPmtOthersDetail();
				mapper2.doDeleteByColumn(IDBConstants.ATTR_PMT_OTHERS, new Long(entity().getIndex()).toString());
				for (int i=0;i<temp.length;i++){
					temp[i].setPmtOthers((PmtOthers)entity());
					mapper2.doInsert(temp[i]);  
				}	
			}
			entity2gui();	
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	private void DeleteAnaknya(PmtOthers old){
		GenericMapper mapper2=MasterMap.obtainMapperFor(PmtOthersDetail.class);
		mapper2.setActiveConn(m_conn);
		mapper2.doDeleteByColumn(IDBConstants.ATTR_PAYMENT_OTHERS,new Long(old.getIndex()).toString());
	}
	
	public void setEnableButtonBawah(boolean bool){
		m_addOthersAccountBtn.setEnabled(bool);
		m_deleteOthersAccountBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}
	
	protected void clearForm() {
		clearTextField(m_originatorCompPanel);
		clearTextField(m_approveCompPanel);
		clearTextField(m_receivedCompPanel);
		m_table.clearTable();
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
	
	public String getSubsidiaryByindex(Account acc){
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		SubsidiaryAccountSetting accSet = null;
		try{
			accSet = logic.getSubsidiaryAccountSettingByIndex(m_sessionid, IDBConstants.MODUL_MASTER_DATA,acc.getIndex());
		}catch (Exception ex){			
		}
		if (accSet!=null)
			return accSet.getSubsidiaryAccount();
		else
			return "";
	}
	
	protected class MyTableModelListener implements TableModelListener{		
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			Object obj = e.getSource();
			if(obj instanceof DefaultTableModel){
				DefaultTableModel model = (DefaultTableModel) obj;
				int maxRow = model.getRowCount();							
				if(((row>-1)&&(row<(maxRow-1)))&& ((col==4) ||(col==5) ||  (col == 7)) && (row==m_table.getSelectedRow())){
					if (m_cekBalance){
						m_table.updateSummary();
					}else{
						m_table.updateSummary1();
					}
				}
			}
		}		
	}	
	
	class BottomTabblePmtProject extends JTable {
		private static final long serialVersionUID = 1L;
		buttomTabbleRcvOthersModel model = new buttomTabbleRcvOthersModel();
		ArrayList listData=new ArrayList();
		protected BottomTabblePmtProject() {
			model.addColumn("No");
			model.addColumn("Account");
			model.addColumn("Description");
			model.addColumn("Subsidiary");
			model.addColumn("Debit");
			model.addColumn("Credit");
			model.addColumn("Currency");
			model.addColumn("Exchange Rate");
			model.addColumn("Department");
			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(40);
			getColumnModel().getColumn(0).setMaxWidth(40);
			clearTable();
			
			getModel().addTableModelListener(new MyTableModelListener());
		}
		
		public void updateSummary1() {		
			int maxRow = getRowCount()-1;
			int maxTotalRow = maxRow - 2;			
			double totDebit = 0;			
			double totCredit = 0;
			for(int i=0; i<=maxTotalRow; i++){		
				if (getValueAt(i,6) instanceof Currency){
					Currency curr = (Currency)	getValueAt(i,6);				
					if (!curr.getSymbol().equals(baseCurrency.getSymbol())){	
						double excRate = ((Double)getValueAt(i,7)).doubleValue();					
						totDebit+=((Double)getValueAt(i,4)).doubleValue()*excRate;
						totCredit += ((Double)getValueAt(i, 5)).doubleValue() * excRate;
					}else{
						totDebit+=((Double)getValueAt(i,4)).doubleValue();
						totCredit += ((Double)getValueAt(i, 5)).doubleValue();
					}				
				}		
			}
			setValueAt(new Double(totCredit), maxRow, 5);						
			setValueAt(new Double(totDebit), maxRow, 4);
		}
		
		public void updateSummary() {
			int maxRow = getRowCount()-1;
			int maxTotalRow = maxRow - 2;			
			double totDebit = 0;			
			double totCredit = 0;			
			int posDbt = -1;
			int posCrd = -1;
			
			for (int j=0;j<m_posDebCreBalance.size();j++){			
				String nil= ((String)m_posDebCreBalance.get(j)).toString();			
				if (nil.equals("0")){					
					posDbt = j;
				}else if (nil.equals("1"))
					posCrd = j;
			}
			
			if (posDbt>=0){
				for(int i=(posDbt+1); i<=maxTotalRow; i++){
					totDebit = totDebit(totDebit, i);
				}
			}else{
				for(int i=0; i<=maxTotalRow; i++){
					totDebit = totDebit(totDebit, i);
				}
			}
			if (posCrd>=0){
				for(int i=(posCrd+1); i<=maxTotalRow; i++){
					totCredit = totCredit(totCredit, i);
				}
			}else{
				for(int i=0; i<=maxTotalRow; i++){
					totCredit = totCredit(totCredit, i);
				}
			}
			if (totDebit>totCredit){									
				double b = totDebit-totCredit;
				if (posDbt>=0)
					setValueAt(new Double(0), posDbt, 4);
				if (posCrd>=0)
					setValueAt(new Double(b), posCrd, 5);					
			}else if (totDebit<totCredit){
				double b = totCredit-totDebit;
				if (posDbt>=0)
					setValueAt(new Double(b), posDbt, 4);
				if (posCrd>=0)
					setValueAt(new Double(0), posCrd, 5);
			}else{
				if (posDbt>=0)
					setValueAt(new Double(0), posDbt, 4);
				if (posCrd>=0)
					setValueAt(new Double(0), posCrd, 5);					
			}
			totDebit =0;
			totCredit =0;
			for(int i=0; i<=maxTotalRow; i++){
				totDebit = totDebit(totDebit, i);
			}
			for(int i=0; i<=maxTotalRow; i++){
				totCredit = totCredit(totCredit, i);
			}
			setValueAt(new Double(totDebit), maxRow, 4);
			setValueAt(new Double(totCredit), maxRow, 5);	
			
		}
		
		private double totCredit(double totCredit, int i) {
			double excRate;
			if (getValueAt(i,6) instanceof Currency){					
				Currency curr = (Currency)	getValueAt(i,6);				
				if (!curr.getSymbol().equals(baseCurrency.getSymbol())){
					excRate = ((Double)getValueAt(i,7)).doubleValue();
					totCredit+=((Double)getValueAt(i,5)).doubleValue()*excRate;
				}else{
					totCredit+=((Double)getValueAt(i,5)).doubleValue();
				}
			}
			return totCredit;
		}
		
		private double totDebit(double totDebit, int i) {
			double excRate;
			if (getValueAt(i,6) instanceof Currency){					
				Currency curr = (Currency)	getValueAt(i,6);				
				if (!curr.getSymbol().equals(baseCurrency.getSymbol())){	
					excRate = ((Double)getValueAt(i,7)).doubleValue();
					totDebit+=((Double)getValueAt(i,4)).doubleValue()*excRate;
				}else{
					totDebit+=((Double)getValueAt(i,4)).doubleValue();
				}
			}
			return totDebit;
		}
		
		public boolean cekTotalDebitCredit(){
			int maxRow = getRowCount()-1;
			if (getValueAt(maxRow,4).toString().equals(getValueAt(maxRow,5).toString()))
				return true;
			return false;
		}
		
		public TableCellEditor getCellEditor(int row, int col) {
			String nil="0";
			if (m_nonDeleteList.size()>0)
				nil= ((String)m_nonDeleteList.get(row)).toString();
			Currency curr = null;
			if (getValueAt(row,6) instanceof Currency) {
				curr = (Currency) getValueAt(row,6);				
			}			
			if (col==2)
				return new BaseTableCellEditor();
			if (row <= getRowCount()-3 &&  (nil.equals("0") || nil.equals("2"))){
				JournalStandardAccount jsAccount = (JournalStandardAccount)listData.get(row);
				Account acc = jsAccount.getAccount();
				if (col ==3){						
					String SubsidiaryAccSet = getSubsidiaryByindex(acc);
					if (SubsidiaryAccSet != null) {
						if (!SubsidiaryAccSet.equals("")) {
							if (SubsidiaryAccSet.equals("Employee"))
								return new EmployeeCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
												.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Partner"))
								return new PartnerCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
												.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Customer"))
								return new CustomerCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
												.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Cash"))
								return new CashCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
												.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Bank"))
								return new BankCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
												.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Loan"))
								return new LoanCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
												.getMainFrame(), m_conn,
										m_sessionid);
							else if (SubsidiaryAccSet.equals("Project"))
								return new ProjectDataCellEditor(
										pohaci.gumunda.cgui.GumundaMainFrame
												.getMainFrame(), m_conn,
										m_sessionid);
						}
					} else
						return null;
				}
				else if (col == 4 && (jsAccount.getBalance()==0))					
					return new FormattedDoubleCellEditor(JLabel.RIGHT);
				else if (col ==5 && (jsAccount.getBalance()==1))
					return new FormattedDoubleCellEditor(JLabel.RIGHT);
				else if (col ==6 && nil.equals("0"))
					return new CurrencyCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),"Currency",m_conn,m_sessionid);
				else if (col==7 && !curr.getSymbol().equals(baseCurrency.getSymbol()))
					return new FormattedDoubleCellEditor(JLabel.RIGHT);
				else if (col ==8 && m_multipleRb.isSelected())
					return new OrganizationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
			}else{
				if (col ==8 && m_multipleRb.isSelected())
					return new OrganizationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
			}
			return null;
		}
		
		public TableCellRenderer getCellRenderer(int row, int column) {
			if(column==0)
				return new BaseTableCellRenderer();
			if(column>0){
				int maxRow =getRowCount()-1;				
				if(row>=(maxRow-1) &&(row<=maxRow)){
					if(column==3)
						return new FormattedStandardCellRenderer(Font.BOLD,JLabel.RIGHT);
					else if(column==4 || column==5)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
					else if (column==7)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);				
					
				}else{				
					JournalStandardAccount jsAccount = (JournalStandardAccount)listData.get(row);
					if (column == 3) 
						return new FormattedStandardCellRenderer(Font.PLAIN,JLabel.CENTER);
					else if (column == 4) 
						if (jsAccount.getBalance() == 0)
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					else if (column == 5) 
						if (jsAccount.getBalance() == 1)
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					else if (column == 6) 
						return new FormattedStandardCellRenderer(Font.PLAIN,JLabel.CENTER);
					else if (column == 7)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,Font.PLAIN);					
				}				
			}
			return new FormattedStandardCellRenderer(Font.PLAIN, JLabel.LEFT);
		}
		
		public void clearTable() {				
			listData.clear();
			m_nonDeleteList.clear();
			m_posDebCreBalance.clear();
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
			model.addRow(new Object[]{null, null, null, null,null, null, null});
			model.addRow(new Object[]{null, null, null, "TOTAL", new Double(0),new Double(0),null});			
		}
		
		public void addData(Vector obj,JournalStandardAccount acc,int insertRow){
			listData.add(acc);			
			model.insertRow(insertRow,obj);			
			updateSummary();
			updateNumbering();
		}
		
		public void insertData(Vector obj,JournalStandardAccount jsAccount,int insertRow){
			listData.add(insertRow,jsAccount);
			model.insertRow(insertRow,obj);
			updateSummary();
			updateNumbering();
		}
		
		public void removeData(int row) {
			listData.remove(row);
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);			
			updateNumbering();
		}		
		
		private void updateNumbering() {
			for(int i=0; i<getRowCount()-2; i++){				
				setValueAt(new Integer(i+1), i, 0);				
			}
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
	
	protected class StandardFormatCellRenderer extends DefaultTableCellRenderer {
		
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
	}
	
	
	class buttomTabbleRcvOthersModel extends DefaultTableModel{
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {			
			int maxRow = getRowCount();			
			if( (col == 0 || col==1) || row > (maxRow-3))
				return false;
			return true;
		}
		
		public void setValueAt(Object aValue, int row, int column) {
			if (column==6)
				if (aValue instanceof Currency){
					Currency curr = (Currency) aValue;
					if (curr.getIsBase())
						setValueAt(new Double(1.0),row,7);					
					super.setValueAt(new Currency(curr,Currency.SYMBOL),row,column);
					return;
				}
			super.setValueAt(aValue, row, column);
		}
	}
	
	protected void tableStopCellEditing() {
		m_table.stopCellEditing();
	}
	
}
