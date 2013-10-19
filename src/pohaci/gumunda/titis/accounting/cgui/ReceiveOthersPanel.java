package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.cgui.report.Rcpt_Others;
import pohaci.gumunda.titis.accounting.dbapi.*;
import pohaci.gumunda.titis.accounting.entity.*;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;
import pohaci.gumunda.titis.application.db.*;
import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.project.cgui.*;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class ReceiveOthersPanel extends RevTransactionPanel 
implements ActionListener, PropertyChangeListener {	
	private static final long serialVersionUID = 1L;
	private RcvOthers m_entity;
	protected Employee m_defaultOriginator,m_defaultApproved,m_defaultReceived;
	private LookupPicker m_accountComp,m_rcvTypeComp;
	private JButton m_addRcvTypeAccountBtn,m_deleteRcvTypeAccountBtn;
	private LookupDepartmentPicker m_departmentComp;
	private JScrollPane m_descriptionSrollPane,m_rcvTypeAccountSrollPane,m_remarksScrollPane;
	private JTextArea m_descriptionTextArea,m_remarksTextArea;
	private JFormattedTextField m_exchRateText;
	private JTextField m_rcvFromText,m_refNoText,m_statusText,m_submittedDateText,m_unitCodeText;	
	private BottomTabbleRcvOthers m_table;
	private JComboBox m_rcvToCombo;
	private pohaci.gumunda.titis.application.DatePicker m_transactionDateDate;
	private JPanel m_bottomButtonPanel,m_jPanel1,m_jPanel1_1,m_topButtonPanel,m_jPanel1_2,m_jPanel1_2_1,
	m_jPanel1_2_1_1,m_jPanel1_2_2,m_jPanel1_2_2_1,m_jPanel1_3,m_jPanel1_3_2,m_jPanel1_3_2_2,
	m_jPanel2,m_jPanel2_1;
	private AssignPanel m_originatorComp,m_approvedComp,m_receivedComp;	
	private LookupReceiveTypeAnak m_rcvAccountComp;
	
	public ReceiveOthersPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		initTabel();
		enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(conn, sessionid);
		setDefaultSignature(); 
		setEntity(new RcvOthers());
		m_entityMapper = MasterMap.obtainMapperFor(RcvOthers.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}	
	
	private void initComponents() {		
		JLabel receiptNoLbl = new JLabel("Receipt No");
		JLabel receiptDateLbl = new JLabel("Receipt Date*");
		JLabel descriptionLbl = new JLabel("Description*");
		JLabel statusLbl = new JLabel("Status");
		JLabel submittedDateLbl = new JLabel("Submitted Date");
		JLabel receiveToLbl = new JLabel("Receive To*");
		JLabel accountLbl = new JLabel("Account*");
		JLabel receiveTypeLbl = new JLabel("Receive Type*");
		JLabel receiveFromLbl = new JLabel("Receive From*");
		JLabel unitCodeLbl = new JLabel("Unit Code*");
		JLabel departmentLbl = new JLabel("Department*");
		JLabel remarksLbl = new JLabel("Remarks");
		JLabel exchRateLbl1 = new JLabel("Exch. Rate");
		
		m_unitCodeText=new JTextField();	
		m_statusText = new JTextField();		
		m_submittedDateText = new JTextField();		
		m_rcvFromText = new JTextField();
		m_refNoText = new JTextField();
		
		m_newBtn = new JButton("New");
		m_editBtn = new JButton("Edit");
		m_saveBtn = new JButton("Save");
		m_deleteBtn = new JButton("Delete");
		m_cancelBtn = new JButton("Cancel");
		m_submitBtn = new JButton("Submit");
		m_addRcvTypeAccountBtn = new JButton("Add");
		m_deleteRcvTypeAccountBtn = new JButton("Delete");
		
		m_descriptionTextArea = new JTextArea();
		m_remarksTextArea = new JTextArea();
		
		m_descriptionSrollPane = new JScrollPane();		
		m_remarksScrollPane = new JScrollPane();
		m_rcvTypeAccountSrollPane = new JScrollPane();
		
		m_topButtonPanel = new JPanel();
		m_bottomButtonPanel = new JPanel();
		m_jPanel1 = new JPanel();
		m_jPanel1_1 = new JPanel();
		m_jPanel1_2 = new JPanel();
		m_jPanel1_2_2 = new JPanel();
		m_jPanel1_2_2_1 = new JPanel();
		m_jPanel1_3 = new JPanel();
		m_jPanel1_3_2 = new JPanel();
		m_jPanel1_3_2_2 = new JPanel();
		m_jPanel2 = new JPanel();
		m_jPanel2_1 = new JPanel();
		m_jPanel1_2_1 = new JPanel();
		m_jPanel1_2_1_1 = new JPanel();
		
		m_originatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		m_approvedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		m_receivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");
		
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		
		m_rcvToCombo = new JComboBox(new String[]{"Cash","Bank"});		
		
		m_transactionDateDate = new pohaci.gumunda.titis.application.DatePicker();		
		m_exchRateText=new  JFormattedTextField(m_numberFormatter);		
		
		m_departmentComp= new LookupDepartmentPicker(m_conn, m_sessionid);
		m_accountComp = new LookupCashAccountPicker(m_conn, m_sessionid);
		m_rcvTypeComp  = new LookupReceiveCashBankType(m_conn, m_sessionid,"Receive Others");
		
		m_table = new BottomTabbleRcvOthers();
				
		
		setLayout(new BorderLayout());
		GridBagConstraints gridBagConstraints;
		setPreferredSize(new Dimension(700, 600));
		
		m_jPanel1.setLayout(new BorderLayout());		
		m_jPanel1.setPreferredSize(new Dimension(650, 300));
		
		m_jPanel1_1.setLayout(new BorderLayout());		
		m_jPanel1_1.setPreferredSize(new Dimension(650, 35));
		
		m_topButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));		
		m_topButtonPanel.setPreferredSize(new Dimension(650, 35));
		m_topButtonPanel.add(m_searchRefNoBtn);
		
		m_searchRefNoBtn.setPreferredSize(new Dimension(21, 21));
		
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
		
		m_jPanel1_2.setPreferredSize(new Dimension(650, 300));
		m_jPanel1_2_2.setLayout(new BorderLayout());
		
		m_jPanel1_2_2.setPreferredSize(new Dimension(415, 300));
		m_jPanel1_2_2_1.setLayout(new GridBagLayout());
		
		m_jPanel1_2_2_1.setPreferredSize(new Dimension(415, 300));

		receiptNoLbl.setPreferredSize(new Dimension(85, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_2_1.add(receiptNoLbl, gridBagConstraints);
		
		receiptDateLbl.setPreferredSize(new Dimension(85, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_2_1.add(receiptDateLbl, gridBagConstraints);
		
		descriptionLbl.setPreferredSize(new Dimension(85, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_2_1.add(descriptionLbl, gridBagConstraints);
		
		m_descriptionSrollPane.setPreferredSize(new Dimension(290, 50));
		m_descriptionTextArea.setColumns(20);
		m_descriptionTextArea.setLineWrap(true);
		m_descriptionTextArea.setRows(5);
		m_descriptionSrollPane.setViewportView(m_descriptionTextArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_2_1.add(m_descriptionSrollPane, gridBagConstraints);
		
		remarksLbl.setPreferredSize(new Dimension(85, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_2_1.add(remarksLbl, gridBagConstraints);
		
		m_remarksScrollPane.setPreferredSize(new Dimension(290, 65));
		m_remarksTextArea.setColumns(20);
		m_remarksTextArea.setLineWrap(true);
		m_remarksTextArea.setRows(5);
		m_remarksScrollPane.setViewportView(m_remarksTextArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_2_1.add(m_remarksScrollPane, gridBagConstraints);
		
		m_refNoText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_jPanel1_2_2_1.add(m_refNoText, gridBagConstraints);
		
		m_transactionDateDate.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		m_jPanel1_2_2_1.add(m_transactionDateDate, gridBagConstraints);
		
		m_jPanel1_2_2.add(m_jPanel1_2_2_1, BorderLayout.WEST);
		
		m_jPanel1_2.add(m_jPanel1_2_2, BorderLayout.CENTER);
		
		m_jPanel1_2_1.setLayout(new GridBagLayout());
		
		m_jPanel1_2_1.setPreferredSize(new Dimension(440, 300));

		statusLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(statusLbl, gridBagConstraints);
		
		submittedDateLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(submittedDateLbl, gridBagConstraints);
		
		receiveToLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(receiveToLbl, gridBagConstraints);
		
		accountLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(accountLbl, gridBagConstraints);
		
		receiveTypeLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(receiveTypeLbl, gridBagConstraints);
		
		receiveFromLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(receiveFromLbl, gridBagConstraints);
		
		m_statusText.setPreferredSize(new Dimension(290, 18));
		//m_statusText.setBackground(Color.WHITE);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_statusText, gridBagConstraints);
		
		m_submittedDateText.setPreferredSize(new Dimension(290, 18));
		//m_submittedDateText.setBackground(Color.WHITE);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_submittedDateText, gridBagConstraints);
		
		//m_rcvToCombo.setBackground(Color.WHITE);		
		m_rcvToCombo.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_rcvToCombo, gridBagConstraints);
		
		m_accountComp.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_accountComp, gridBagConstraints);
		
		m_rcvTypeComp.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_rcvTypeComp, gridBagConstraints);
		
		m_rcvFromText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_rcvFromText, gridBagConstraints);
		
		unitCodeLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(unitCodeLbl, gridBagConstraints);
		
		departmentLbl.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(departmentLbl, gridBagConstraints);
		
		m_jPanel1_2_1_1.setLayout(new GridBagLayout());
		
		m_jPanel1_2_1_1.setPreferredSize(new Dimension(200, 23));
		
		exchRateLbl1.setPreferredSize(new Dimension(60, 21));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 0);
		m_jPanel1_2_1_1.add(exchRateLbl1, gridBagConstraints);
		
		m_exchRateText.setPreferredSize(new Dimension(85, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 0, 1, 0);
		m_jPanel1_2_1_1.add(m_exchRateText, gridBagConstraints);
		
		m_unitCodeText.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_unitCodeText, gridBagConstraints);
		
		m_departmentComp.setPreferredSize(new Dimension(290, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		m_jPanel1_2_1.add(m_departmentComp, gridBagConstraints);
		
		m_jPanel1_2.add(m_jPanel1_2_1, BorderLayout.WEST);
		
		m_jPanel1.add(m_jPanel1_2, BorderLayout.CENTER);
		
		m_jPanel1_3.setLayout(new BorderLayout());
		
		m_jPanel1_3.setPreferredSize(new Dimension(700, 90));
		m_originatorComp.setLayout(new GridBagLayout());
		
		m_originatorComp.setBorder(BorderFactory.createTitledBorder("Originator"));
		m_originatorComp.setOpaque(false);
		m_originatorComp.setPreferredSize(new Dimension(275, 110));
		
		m_jPanel1_3.add(m_originatorComp, BorderLayout.WEST);
		
		m_jPanel1_3_2.setLayout(new BorderLayout());
		
		m_approvedComp.setLayout(new GridBagLayout());
		
		m_approvedComp.setBorder(BorderFactory.createTitledBorder("Approved by"));
		m_approvedComp.setOpaque(false);
		m_approvedComp.setPreferredSize(new Dimension(275, 110));
		
		m_jPanel1_3_2.add(m_approvedComp, BorderLayout.WEST);
		
		m_jPanel1_3_2_2.setLayout(new BorderLayout());
		
		m_jPanel1_3_2_2.setPreferredSize(new Dimension(215, 110));
		m_receivedComp.setLayout(new GridBagLayout());
		
		m_receivedComp.setBorder(BorderFactory.createTitledBorder("Received by"));
		m_receivedComp.setOpaque(false);
		m_receivedComp.setPreferredSize(new Dimension(275, 110));
		
		m_jPanel1_3_2_2.add(m_receivedComp, BorderLayout.WEST);
		
		m_jPanel1_3_2.add(m_jPanel1_3_2_2, BorderLayout.CENTER);
			
		m_jPanel1_3.add(m_jPanel1_3_2, BorderLayout.CENTER);
		
		m_jPanel1.add(m_jPanel1_3, BorderLayout.SOUTH);
		
		add(m_jPanel1, BorderLayout.NORTH);
		
		m_jPanel2.setLayout(new BorderLayout());
		
		m_jPanel2.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		m_jPanel2.setPreferredSize(new Dimension(650, 250));
		m_jPanel2_1.setLayout(new BorderLayout());
		
		m_jPanel2_1.setPreferredSize(new Dimension(650, 35));
		m_bottomButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		
		m_bottomButtonPanel.setPreferredSize(new Dimension(650, 35));
		m_addRcvTypeAccountBtn.setMargin(new Insets(2, 2, 2, 2));
		m_addRcvTypeAccountBtn.setPreferredSize(new Dimension(50, 20));
		m_bottomButtonPanel.add(m_addRcvTypeAccountBtn);
		
		m_deleteRcvTypeAccountBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deleteRcvTypeAccountBtn.setPreferredSize(new Dimension(50, 20));
		m_bottomButtonPanel.add(m_deleteRcvTypeAccountBtn);
		m_jPanel2_1.add(m_bottomButtonPanel, BorderLayout.WEST);		
		m_jPanel2.add(m_jPanel2_1, BorderLayout.NORTH);		
		m_rcvTypeAccountSrollPane.setPreferredSize(new Dimension(650, 215));
		m_rcvTypeAccountSrollPane.setViewportView(m_table);		
		m_jPanel2.add(m_rcvTypeAccountSrollPane, BorderLayout.CENTER);		
		add(m_jPanel2, BorderLayout.CENTER);		
	}
	
	private void addingListener(){
		m_addRcvTypeAccountBtn.addActionListener(this);
		m_deleteRcvTypeAccountBtn.addActionListener(this);
		accountComp().addPropertyChangeListener("object",this);
		m_rcvTypeComp.addPropertyChangeListener("object",this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
	}	
	
	public void actionPerformed(ActionEvent e) {		
		if(e.getSource()==m_addRcvTypeAccountBtn){
			if (m_rcvTypeComp.getObject()!=null){
				JournalStandard js = (JournalStandard)m_rcvTypeComp.getObject();
				m_rcvAccountComp=new LookupReceiveTypeAnak(m_conn, m_sessionid,js,"Others Receive Type");
				m_rcvAccountComp.done();
				if(m_rcvAccountComp.getObject()!=null){ 
					Vector temp1=new Vector();
					JournalStandardAccount journal = (JournalStandardAccount)m_rcvAccountComp.getObject();										
					Account acc=journal.getAccount();
					m_nonDeleteList.add(new String("0"));
					m_posDebCreBalance.add(new String(""));
					int row = (m_table.getRowCount()-2);
					temp1.addElement(null);
					temp1.addElement(acc.toStringWithCode());
					temp1.addElement(null);
					temp1.addElement(null);
					temp1.addElement(new Double(0));
					temp1.addElement(new Double(0));
					temp1.addElement(baseCurrency);
					temp1.addElement(new Double(1));
					m_table.addData(temp1, journal,row);
				}					
			}else{
				JOptionPane.showMessageDialog(this, "Please select receive type first",
						"Warning", JOptionPane.WARNING_MESSAGE);					
			}
		}
		else if (e.getSource()==m_deleteRcvTypeAccountBtn){			
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
		
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new Rcpt_Others(m_entity,m_conn);
			else{
				JOptionPane.showMessageDialog(this,"Data is empty","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {			
			String[] kolom = {"","UPPER(REFERENCENO)","TRANSACTIONDATE","EMPRECEIVED","UPPER(RECEIVETO)","UNIT","STATUS","SUBMITDATE"};
			String orderby = " ORDER BY " + kolom[2] + " ASC," + kolom[1] + " ASC";
			SearchReceiptDialog dlg = new SearchReceiptDialog(GumundaMainFrame.getMainFrame(), "Search Receipt", m_conn, m_sessionid,kolom,orderby,
					new SalesReceivedLoader(m_conn,RcvOthers.class) ,"Others");				
			dlg.setVisible(true);
			if (dlg.selectedObj != null){   
				doLoad(dlg.selectedObj);				
			}						
			
		}
	}
	
	public void doEdit() {
		super.doEdit();
		setEnableButtonBawah(true);
	}
	
	protected void doNew() {
		m_cekBalance = false;
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
	}
	

	protected void deleteChilds() {
		super.deleteChilds();
		DeleteAnaknya(m_entity);
	}
	
	
	protected void clearAll() {
		m_cekBalance = false;
		super.clearAll();
		clearForm();		
		clearKomponen();
		disableEditMode();
	}
	
	public void clearKomponen(){
		m_statusText.setText("");
		m_unitCodeText.setText("");
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
	
	boolean m_cekButtonAddBawah = false;
	public void LoadOthersDetail(long a){
		GenericMapper mapper2=MasterMap.obtainMapperFor(RcvOthersDetail.class);
		mapper2.setActiveConn(m_conn);		
		List rs=mapper2.doSelectWhere(IDBConstants.ATTR_RECEIVE_OTHERS+"="+a);
		m_table.clearTable();
		RcvOthersDetail detail;
		Vector temp1;
		Account acc;		
		for(int i=0;i<rs.size();i++){
			temp1=new Vector();
			detail=(RcvOthersDetail)rs.get(i);			
			acc=(Account)detail.getAccount();
			
			JournalStandard js = (JournalStandard)m_rcvTypeComp.getObject();
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
				SetCompanyLoanToTable(detail, temp1, accLogic );
			}else if (SubsidiaryAccSet.equals("Project")){
				SetProjectToTable(detail, temp1, proLogic );
			}else{
				temp1.addElement("");
			}
			
			Account accBankCash = null;			
			if (entity().getBankAccount()!=null){
				accBankCash = entity().getBankAccount().getAccount(); 
			}else {
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
					}else if (accBankCash.getIndex()==jsAccount.getAccount().getIndex()){
						m_posDebCreBalance.add(new String(""));
						m_nonDeleteList.add(new String("2"));						
						m_cekButtonAddBawah = true;
					}else {
						m_posDebCreBalance.add(new String(""));
						m_nonDeleteList.add(new String("0"));
					}						
					if (jsAccount.getBalance()==0){
						temp1.addElement(new Double(detail.getaccValue()));
						temp1.addElement(new Double(0));
					}else if (jsAccount.getBalance()==1){
						temp1.addElement(new Double(0));
						temp1.addElement(new Double(detail.getaccValue()));
					}
					temp1.addElement(detail.getCurrency());			
					temp1.addElement(new Double(detail.getExchangerate()));
					m_table.addData(temp1, jsAccount,i);	
				}
			}			
		}		
	}
	
	private void SetCashAccountToTable(RcvOthersDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCompanyLoanToTable(RcvOthersDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetBankAccountToTable(RcvOthersDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCustomerToTable(RcvOthersDetail detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Customer cust = null;
		cust = proLogic.getCustomerByIndex(detail.getSubsidiAry());
		temp1.addElement(cust);
	}
	
	private void SetPartnerToTable(RcvOthersDetail detail, Vector temp1, ProjectBusinessLogic proLogic) {
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
	
	private void SetEmployeeToTable(RcvOthersDetail detail, Vector temp1, HRMBusinessLogic hrmLogic) {
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
	

	private void SetProjectToTable(RcvOthersDetail detail, Vector temp1, ProjectBusinessLogic  proLogic) {
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
	
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}		
		else if (evt.getSource()== m_rcvTypeComp){	
			if (m_rcvTypeComp.getObject()!=null){
				JournalStandard js = (JournalStandard)m_rcvTypeComp.getObject();
				GenericMapper mapper2=MasterMap.obtainMapperFor(JournalStandardSetting.class);
				mapper2.setActiveConn(m_conn);
				List rs=mapper2.doSelectWhere(IDBConstants.ATTR_JOURNAL+"="+js.getIndex());				
				JournalStandardSetting jsSetting = (JournalStandardSetting)rs.get(0);
				m_jPanel1_2_1.remove(accountComp());			
				m_jPanel1_2_1.revalidate();
				m_jPanel1_2_1.repaint();
				if (jsSetting.getAttribute().equals(IConstants.ATTR_RCV_CASH)){
					m_rcvToCombo.setSelectedItem("Cash");		
					setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));	
				}else{
					m_rcvToCombo.setSelectedItem("Bank");		
					setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid,js));
				}
				accountComp().setPreferredSize(new Dimension(290, 18));
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 1;
				gridBagConstraints.gridy = 4;
				gridBagConstraints.anchor = GridBagConstraints.WEST;
				gridBagConstraints.insets = new Insets(1, 3, 1, 1);
				m_jPanel1_2_1.add(accountComp(), gridBagConstraints);				
				m_jPanel1_2_1.revalidate();
				setEntitasGui(js.getIndex());
			}
		}
		else if (evt.getSource() == accountComp()){
			if (m_rcvTypeComp.getObject()!=null){
				BankAccount bankAccount = null;
				CashAccount cashAccount = null;
				Account acc = null;			
				Currency curr= null;				
				JournalStandard js = (JournalStandard)m_rcvTypeComp.getObject();							
				if (evt.getNewValue() == null){
					m_unitCodeText.setText("");	
				} else if (evt.getNewValue() instanceof BankAccount){
					bankAccount = (BankAccount)accountComp().getObject();
					m_unitCodeText.setText(bankAccount.getUnit().toString());
					curr = bankAccount.getCurrency();
				} else if (evt.getNewValue() instanceof CashAccount){
					cashAccount = (CashAccount)accountComp().getObject();					
					m_unitCodeText.setText(cashAccount.getUnit().toString());
					curr = cashAccount.getCurrency();					
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
						temp1.addElement(curr);
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
						temp1.addElement(curr);
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
		m_unitCodeText.setText("");		
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
		setenableEditPanel(m_jPanel1_2_1,false);
		setenableEditPanel(m_jPanel1_2_1_1,false);
		setenableEditPanel(m_jPanel1_2_2,false);
		setenableEditPanel(m_jPanel1_2_2_1,false);
		setEnableButtonBawah(false);
	}
	
	protected void enableEditMode()	{  
		this.m_accountComp.setEnabled(true);
		this.m_departmentComp.setEnabled(true);
		this.m_rcvTypeComp.setEnabled(true);
		this.m_rcvFromText.setEditable(true);
		this.m_descriptionTextArea.setEnabled(true);
		this.m_remarksTextArea.setEnabled(true);
		this.m_transactionDateDate.setEditable(true);	
		this.m_originatorComp.setEnabled(true);
		this.m_approvedComp.setEnabled(true);
		this.m_receivedComp.setEnabled(true);
		setEnableButtonBawah(true);
	}
	
	protected void disableEditMode(){
		this.m_rcvToCombo.setEnabled(false);
		this.m_accountComp.setEnabled(false);
		this.m_departmentComp.setEnabled(false);
		this.m_rcvTypeComp.setEnabled(false);
		this.m_rcvFromText.setEditable(false);
		this.m_exchRateText.setEditable(false);
		this.m_descriptionTextArea.setEnabled(false);
		this.m_remarksTextArea.setEnabled(false);
		this.m_transactionDateDate.setEditable(false);	
		this.m_originatorComp.setEnabled(false);
		this.m_approvedComp.setEnabled(false);
		this.m_receivedComp.setEnabled(false);
		setEnableButtonBawah(false);
	}	
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity()	{
		validityMsgs.clear();
		if (m_transactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (m_accountComp.getObject()== null)
			addInvalid("Source account must be selected");
		if (m_descriptionTextArea.getText().compareTo("")==0)
			addInvalid("Description Text Area must be filled");
		if (m_unitCodeText.getText().equals(""))
			addInvalid("Unit code Area must be filled");
		if (m_departmentComp.getObject()==null)
			addInvalid("Department must be selected");
		if (m_rcvTypeComp.getObject()==null)
			addInvalid("Receive type must be selected");
		if (m_rcvFromText.getText().equalsIgnoreCase(""))
			addInvalid("Receive from must be filed");
		detailAccountOperation();
		RcvOthersDetail[] temp=entity().getRcvOthersDetail();		
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
	
	private void addInvalid(String string) {
		validityMsgs.add(string);
	}
	
	protected void gui2entity() {
		String paySource = (String) m_rcvToCombo.getSelectedItem();
		if(paySource.equalsIgnoreCase("BANK")){
			entity().setReceiveTo("BANK");
			entity().setBankAccount((BankAccount) m_accountComp.getObject());
			entity().setCashAccount(null);
			entity().setUnit(entity().getBankAccount().getUnit());
		}else{
			entity().setReceiveTo("CASH");
			entity().setCashAccount((CashAccount) m_accountComp.getObject());
			entity().setBankAccount(null);
			entity().setUnit(entity().getCashAccount().getUnit());
		}
		entity().setReceiveFrom(m_rcvFromText.getText());		
		entity().setJournal((JournalStandard)m_rcvTypeComp.getObject());		
		entity().setDepartment((Organization)m_departmentComp.getObject());
		entity().setCurrency(baseCurrency);
		entity().setExchangeRate(0);
		entity().setTransactionDate(m_transactionDateDate.getDate());
		entity().transTemplateRead(
				this.m_originatorComp, this.m_approvedComp,
				this.m_receivedComp, this.m_refNoText,
				this.m_descriptionTextArea
		);
		detailAccountOperation();
	}
	
	private void detailAccountOperation() {
		if (m_table.getDataCount()>=0){
			RcvOthersDetail[] temp=new RcvOthersDetail[m_table.getDataCount()];
			for (int i=0;i<m_table.getDataCount();i++){
				JournalStandardAccount journal = (JournalStandardAccount)m_table.getListData().get(i);
				Account acc = journal.getAccount();
				temp[i]=new RcvOthersDetail();
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
				
				if (journal.getBalance()==0){
					temp[i].setaccValue(((Double)m_table.getValueAt(i,4)).doubleValue());				
				}else if (journal.getBalance()==1){
					temp[i].setaccValue(((Double)m_table.getValueAt(i,5)).doubleValue());
				}
				
				Currency curr = null;
				if (m_table.getValueAt(i,6) instanceof Currency) {
					curr = (Currency) m_table.getValueAt(i,6);					
				}				
				temp[i].setCurrency(curr);				
				double excRate = ((Double)m_table.getValueAt(i,7)).doubleValue();
				
				temp[i].setExchangerate(excRate);
			}
			entity().setRcvOthersDetail(temp);
		}
	}
	
	protected  void entity2gui(){
		m_rcvTypeComp.setObject(entity().getJournal()); 
		if(entity().isSourceBank()){
			m_rcvToCombo.setSelectedItem("Bank");
			m_accountComp.setObject(entity().getBankAccount());
		}else {
			m_rcvToCombo.setSelectedItem("Cash");
			m_accountComp.setObject(entity().getCashAccount());    		
		}
		m_departmentComp.setObject(entity().getDepartment());
		m_rcvFromText.setText(entity().getReceiveFrom());
		m_departmentComp.setObject(entity().getDepartment());
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
		LoadOthersDetail(entity().getIndex());
	}
	
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	RcvOthers entity() {
		return m_entity;
	}
	
	void setEntity(Object m_entity) {
		RcvOthers oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (RcvOthers)m_entity;
		this.m_entity.addPropertyChangeListener(this);
		readEntityState();
	}
	
	protected Object createNew(){  
		RcvOthers a  = new RcvOthers();
		a.setEmpOriginator(m_defaultOriginator);
		a.setEmpApproved(m_defaultApproved);
		a.setEmpReceived(m_defaultReceived);
		return a ;
	}
	
	protected void doSave() {
		if (!cekValidity()) return;	
		if (!m_table.cekTotalDebitCredit()) {
			JOptionPane.showMessageDialog(this, "Transaction is unbalanced. Please check accounts",
					"Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		super.doSave();		
		setEnableButtonBawah(false);		
		AccountingSQLSAP sql=new AccountingSQLSAP();
		GenericMapper mapper2=MasterMap.obtainMapperFor(RcvOthersDetail.class);
		mapper2.setActiveConn(m_conn);
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_RCV_OTHERS, m_conn);
				entity().setIndex(index);
			}
			if (entity().getRcvOthersDetail()!=null){
				RcvOthersDetail temp[]=entity().getRcvOthersDetail();
				mapper2.doDeleteByColumn(IDBConstants.ATTR_RECEIVE_OTHERS,new Long(entity().getIndex()).toString());
				for (int i=0;i<temp.length;i++){
					temp[i].setReceiveothers((RcvOthers)entity());					
					mapper2.doInsert(temp[i]);  
				}	
			}	
			entity2gui();
		} catch (SQLException e) {	
			e.printStackTrace();
		}	
	}
	
	private void DeleteAnaknya(RcvOthers old){
		GenericMapper mapper2=MasterMap.obtainMapperFor(RcvOthersDetail.class);
		mapper2.setActiveConn(m_conn);
		mapper2.doDeleteByColumn(IDBConstants.ATTR_RECEIVE_OTHERS,new Long(old.getIndex()).toString());
	}
	
	private void isiDefaultAssignPanel(){
		m_originatorComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultOriginator));
		m_approvedComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultApproved));
		m_receivedComp.m_jobTextField.setText(getEmployeeJobTitle(m_defaultReceived));	
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_OTHERS, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			m_defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_OTHERS, Signature.SIGN_APPROVED);
		if(sign!=null)
			m_defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_OTHERS, Signature.SIGN_RECEIVED);
		if(sign!=null)
			m_defaultReceived = sign.getFullEmployee();
	}
	
	protected void clearForm() {
		clearTextField(m_jPanel1_2_2_1);
		clearTextField(m_jPanel1_2_2);
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
	
	public void setEnableButtonBawah(boolean bool){
		m_addRcvTypeAccountBtn.setEnabled(bool);		
		m_deleteRcvTypeAccountBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}
	
	public void initTabel(){
		m_table=new BottomTabbleRcvOthers();
		m_rcvTypeAccountSrollPane.setViewportView(m_table);
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
				if(((row>-1)&&(row<(maxRow-1)))&& ((col==4)||(col==5) /*||(col==6)*/|| (col == 7)) && (row==m_table.getSelectedRow())){
					if (m_cekBalance)
						m_table.updateSummary();
					else
						m_table.updateSummary1();						
				}
			}
		}		
	}	
	
	protected class BottomTabbleRcvOthers extends JTable {        
		private static final long serialVersionUID = 1L;
		buttomTabbleRcvOthersModel model = new buttomTabbleRcvOthersModel();
		ArrayList listData=new ArrayList();
		
		protected BottomTabbleRcvOthers() {
			model.addColumn("No");
			model.addColumn("Account");
			model.addColumn("Description");
			model.addColumn("Subsidiary");
			model.addColumn("Debit");
			model.addColumn("Credit");
			model.addColumn("Currency");
			model.addColumn("Exchange Rate");
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
			String nil= ((String)m_nonDeleteList.get(row)).toString();
			String currSymbol = "";
			Currency curr = null;
			if (getValueAt(row,6) instanceof Currency) {
				curr = (Currency) getValueAt(row,6);	
				currSymbol = curr.getSymbol();
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
				else if (col==7 && !currSymbol.equals(baseCurrency.getSymbol()) && !currSymbol.equals(""))
					return new FormattedDoubleCellEditor(JLabel.RIGHT);		
			}
			return null;
		}
		
		public TableCellRenderer getCellRenderer(int row, int column) {
			if (column == 0)
				return new BaseTableCellRenderer();
			if (column > 0) {
				int maxRow = getRowCount() - 1;
				if (row >= (maxRow - 1) && (row <= maxRow)) {
					if (column == 3)
						return new FormattedStandardCellRenderer(Font.BOLD,JLabel.RIGHT);
					else if (column == 4 || column == 5)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,Font.BOLD);
					else if (column == 7) {
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,Font.PLAIN);
					}
				} else {
					JournalStandardAccount jsAccount = (JournalStandardAccount)listData.get(row);
					if (column == 3) {
						return new FormattedStandardCellRenderer(Font.PLAIN,JLabel.CENTER);
					} else if (column == 4) {
						if (jsAccount.getBalance() == 0)
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					} else if (column == 5) {
						if (jsAccount.getBalance() == 1)
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					} else if (column == 6) {
						return new FormattedStandardCellRenderer(Font.PLAIN,JLabel.CENTER);
					} else if (column == 7) {
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,Font.PLAIN);
					}
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
		
		public void addData(Vector obj,JournalStandardAccount jsAccount,int insertRow){
			listData.add(jsAccount);
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
			for(int i=0; i<(getRowCount()-2); i++){
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
	
	class buttomTabbleRcvOthersModel extends DefaultTableModel{	
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {			
			int maxRow = getRowCount();			
			if( (col == 0 || col==1 ) || row > (maxRow-3))
				return false;
			return true;
		}
		
		public void setValueAt(Object aValue, int row, int column) {
			if (column==6)
				if (aValue instanceof Currency){
					Currency curr = (Currency) aValue;
					if (curr.getIsBase()){
						setValueAt(new Double(1.0),row,7);
					}
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
