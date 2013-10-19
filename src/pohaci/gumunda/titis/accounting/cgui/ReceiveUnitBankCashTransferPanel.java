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
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.Rcpt_UnitCashBankTrans;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtUnitBankCashTrans;
import pohaci.gumunda.titis.accounting.entity.RcvUnitBankCashTrns;
import pohaci.gumunda.titis.accounting.entity.SalesReceivedLoader;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class ReceiveUnitBankCashTransferPanel 
extends RevTransactionPanel implements ActionListener,PropertyChangeListener{
	
	private static final long serialVersionUID = 1L;
	private RcvUnitBankCashTrns m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	public ReceiveUnitBankCashTransferPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(conn, sessionid);
		setDefaultSignature(); 
		setEntity(new RcvUnitBankCashTrns());
		m_entityMapper = MasterMap.obtainMapperFor(RcvUnitBankCashTrns.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	private void initComponents() {
		RcvToAccountText = new javax.swing.JTextField();
		RcvToUnitCodeComp = new javax.swing.JTextField();		
		CurrText = new javax.swing.JTextField();
		TransferComp = new LookupCashBankTransferPicker(m_conn,m_sessionid);
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
		jPanel1_2_2_1 = new javax.swing.JPanel();
		ReceiptNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		ReceiptDateLabel = new javax.swing.JLabel();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScroll = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		RemarksScroll = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_2_1 = new javax.swing.JPanel();
		StatusLabel = new javax.swing.JLabel();
		SubmittedDateLabel = new javax.swing.JLabel();
		ReceiveTo1Label = new javax.swing.JLabel();
		UnitCodeRcvToLabel = new javax.swing.JLabel();
		ReceiveToLabel = new javax.swing.JLabel();
		AccountRecvToLabel = new javax.swing.JLabel();
		ReceiveTo1Label1 = new javax.swing.JLabel();
		TransferFromLabel = new javax.swing.JLabel();
		AccountRcvFromLabel = new javax.swing.JLabel();
		UnitCodeRcvFromLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		jSeparator1 = new javax.swing.JSeparator();
		RcvToText = new javax.swing.JTextField();
		jSeparator2 = new javax.swing.JSeparator();
		RcvFromAccountText = new javax.swing.JTextField();
		RcvFromUnitCodeText = new javax.swing.JTextField();
		jPanel1_2_1_1 = new javax.swing.JPanel();
		CurrencyLabel = new javax.swing.JLabel();
		ExchRateLabel = new javax.swing.JLabel();
		ExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountLabel = new javax.swing.JLabel();
		AmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountBaseCurrLabel = new javax.swing.JLabel();
		AmountBaseText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountBaseCurrText = new javax.swing.JTextField();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		
		setLayout(new java.awt.BorderLayout());
		
		setPreferredSize(new java.awt.Dimension(700, 500));
		jPanel1.setLayout(new java.awt.BorderLayout());
		jPanel1.setPreferredSize(new java.awt.Dimension(670, 390));
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
		jPanel1_2.setPreferredSize(new java.awt.Dimension(700, 300));
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(415, 400));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());
		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(415, 325));
		
		ReceiptNoLabel.setText("Receipt No");
		ReceiptNoLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 1);
		jPanel1_2_2_1.add(ReceiptNoLabel, gridBagConstraints);
		
		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 1);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);
		
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
		
		DescriptionScroll.setPreferredSize(new java.awt.Dimension(290, 110));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScroll.setViewportView(DescriptionTextArea);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DescriptionScroll, gridBagConstraints);
		
		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RemarksLabel, gridBagConstraints);
		
		RemarksScroll.setPreferredSize(new java.awt.Dimension(290, 105));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScroll.setViewportView(RemarksTextArea);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 5, 1);
		jPanel1_2_2_1.add(RemarksScroll, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(TransactionDateDate, gridBagConstraints);
		jPanel1_2_2.add(jPanel1_2_2_1, java.awt.BorderLayout.WEST);
		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);
		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());
		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(440, 325));
		
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
		
		ReceiveTo1Label.setText("Receive To ");
		ReceiveTo1Label.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveTo1Label, gridBagConstraints);
		
		UnitCodeRcvToLabel.setText("Unit Code");
		UnitCodeRcvToLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(UnitCodeRcvToLabel, gridBagConstraints);
		
		ReceiveToLabel.setText("Receive To");
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveToLabel, gridBagConstraints);
		
		AccountRecvToLabel.setText("Account");
		AccountRecvToLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountRecvToLabel, gridBagConstraints);
		
		ReceiveTo1Label1.setText("Receive From");
		ReceiveTo1Label1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveTo1Label1, gridBagConstraints);
		
		TransferFromLabel.setText("Transfer From*");
		TransferFromLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(TransferFromLabel, gridBagConstraints);
		
		AccountRcvFromLabel.setText("Account");
		AccountRcvFromLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountRcvFromLabel, gridBagConstraints);
		
		UnitCodeRcvFromLabel.setText("Unit Code");
		UnitCodeRcvFromLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(UnitCodeRcvFromLabel, gridBagConstraints);
		
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
		
		jSeparator1.setPreferredSize(new java.awt.Dimension(290, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(jSeparator1, gridBagConstraints);
		
		RcvToUnitCodeComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(RcvToUnitCodeComp, gridBagConstraints);
		
		RcvToText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(RcvToText, gridBagConstraints);
		
		RcvToAccountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(RcvToAccountText, gridBagConstraints);
		
		jSeparator2.setPreferredSize(new java.awt.Dimension(290, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(jSeparator2, gridBagConstraints);
		
		TransferComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(TransferComp, gridBagConstraints);
		
		RcvFromAccountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(RcvFromAccountText, gridBagConstraints);
		
		RcvFromUnitCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(RcvFromUnitCodeText, gridBagConstraints);
		jPanel1_2_1_1.setLayout(new java.awt.GridBagLayout());
		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(410, 70));
		
		CurrencyLabel.setText("Currency");
		CurrencyLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(CurrencyLabel, gridBagConstraints);
		
		CurrText.setPreferredSize(new java.awt.Dimension(80, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1_1.add(CurrText, gridBagConstraints);
		
		ExchRateLabel.setText("Exch. Rate");
		ExchRateLabel.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1_2_1_1.add(ExchRateLabel, gridBagConstraints);
		
		ExchRateText.setPreferredSize(new java.awt.Dimension(141, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1_1.add(ExchRateText, gridBagConstraints);
		
		AmountLabel.setText("Amount");
		AmountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountLabel, gridBagConstraints);
		
		AmountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1_1.add(AmountText, gridBagConstraints);
		
		AmountBaseCurrLabel.setText("Amount Base Curr.");
		AmountBaseCurrLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountBaseCurrLabel, gridBagConstraints);
		
		AmountBaseText.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountBaseText, gridBagConstraints);
		
		AmountBaseCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1_1.add(AmountBaseCurrText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(jPanel1_2_1_1, gridBagConstraints);
		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);
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
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		TransferComp.addPropertyChangeListener("object",this);		
		AmountText.addPropertyChangeListener(this);
		ExchRateText.addPropertyChangeListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new Rcpt_UnitCashBankTrans(m_entity);
			else{
				JOptionPane.showMessageDialog(this,"Data is empty","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}			
		}		
		else if(e.getSource() == m_searchRefNoBtn) {
			String[] kolom = {"","UPPER("+IDBConstants.ATTR_REFERENCE_NO+")",IDBConstants.ATTR_TRANSACTION_DATE,IDBConstants.ATTR_EMP_RECEIVED,
					"UPPER("+IDBConstants.ATTR_RECEIVE_TO+")",IDBConstants.ATTR_RCV_UNIT_CODE,IDBConstants.ATTR_STATUS,IDBConstants.ATTR_SUBMIT_DATE};
			String orderby = " ORDER BY " + kolom[2] + " ASC," + kolom[1] + " ASC";
			SearchReceiptDialog dlg = new SearchReceiptDialog(GumundaMainFrame.getMainFrame(), "Search Receipt", m_conn, m_sessionid,kolom,orderby,
					new SalesReceivedLoader(m_conn,RcvUnitBankCashTrns.class),"Unit Bank/Cash" );
			dlg.setVisible(true);
			if (dlg.selectedObj != null)
			{   doLoad(dlg.selectedObj);
			}
		}
	}
	
	private void cleanUp() {
		StatusText.setText("");
		ExchRateText.setValue(null);
		AmountBaseCurrText.setText("");
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
		RcvToUnitCodeComp.setText("");
		RcvToAccountText.setText("");
		RcvToText.setText("");
		CurrText.setText("");
		AmountBaseText.setValue(null);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		
		if(evt.getSource()==TransferComp){
			if (evt.getNewValue() == null){
				RcvFromUnitCodeText.setText("");
				RcvFromAccountText.setText("");
			}else{
				PmtUnitBankCashTrans a = (PmtUnitBankCashTrans)TransferComp.getObject();
				RcvFromUnitCodeText.setText(a.getUnit().getDescription());
				if (a.getBankAccount() == null)
					RcvFromAccountText.setText(a.getCashAccount().getCode() + " " + a.getCashAccount().getName());
				else
					RcvFromAccountText.setText(a.getBankAccount().getCode() + " " + a.getBankAccount().getName());
				CashBankAccount cb = null;
				if(a.getPayTo().equalsIgnoreCase("bank")){
					RcvToText.setText("Bank");
					cb = a.getRcvBankAccount();
				}else{
					RcvToText.setText("Cash");
					cb = a.getRcvCashAccount();
				}				
				CurrText.setText(a.getCurrency().toString());
				ExchRateText.setValue(new Double(a.getExchangeRate()));
				RcvToUnitCodeComp.setText(cb.getUnit().toString());
				if(cb instanceof BankAccount){
					BankAccount b = (BankAccount)cb;
					RcvToAccountText.setText(b.getCode() + " " + b.getName());
				}else{
					CashAccount c = (CashAccount)cb;
					RcvToAccountText.setText(c.getCode() + " " + c.getName());
				}
				AmountText.setValue(new Double(a.getAmount()));
				countBaseCurrency();
			}
		}
		
		if(evt.getSource()==AmountText){
			countBaseCurrency();
		}
		
		if(evt.getSource()==ExchRateText){
			countBaseCurrency();
		}
	}
	
	private void countBaseCurrency() {
		Number amt = (Number)AmountText.getValue();
		Number exch = (Number)ExchRateText.getValue();
		if((amt!=null)&&(exch!=null)){
			double base = amt.doubleValue();
			PmtUnitBankCashTrans a = (PmtUnitBankCashTrans)TransferComp.getObject();
			if(a==null)
				return;
			if(a.getCurrency().getIndex()!=baseCurrency.getIndex()){
				base = amt.doubleValue() * exch.doubleValue();
			}
			AmountBaseText.setValue(new Double(base));
		}
	}
	
	private javax.swing.JLabel AccountRcvFromLabel;
	private javax.swing.JLabel AccountRecvToLabel;
	private javax.swing.JLabel AmountBaseCurrLabel;
	private javax.swing.JTextField AmountBaseCurrText;
	private javax.swing.JFormattedTextField AmountBaseText;
	private javax.swing.JLabel AmountLabel;
	private javax.swing.JFormattedTextField AmountText;
	private javax.swing.JTextField CurrText;
	private javax.swing.JLabel CurrencyLabel;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScroll;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JLabel ExchRateLabel;
	private javax.swing.JFormattedTextField ExchRateText;
	private javax.swing.JTextField RcvFromAccountText;
	private javax.swing.JTextField RcvFromUnitCodeText;
	private javax.swing.JTextField RcvToAccountText;
	private javax.swing.JTextField RcvToText;
	private javax.swing.JTextField RcvToUnitCodeComp;
	private javax.swing.JLabel ReceiptDateLabel;
	private javax.swing.JLabel ReceiptNoLabel;
	private javax.swing.JLabel ReceiveTo1Label;
	private javax.swing.JLabel ReceiveTo1Label1;
	private javax.swing.JLabel ReceiveToLabel;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScroll;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JTextField SubmittedDateText;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private LookupCashBankTransferPicker TransferComp;
	private javax.swing.JLabel TransferFromLabel;
	private javax.swing.JLabel UnitCodeRcvFromLabel;
	private javax.swing.JLabel UnitCodeRcvToLabel;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel TopButtonPanel;
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
	private javax.swing.JSeparator jSeparator2;
	private void enableAwal(){ 	
		setenableEditPanel(jPanel1_2_1,false);
		setenableEditPanel(jPanel1_2_1_1,false);
		setenableEditPanel(jPanel1_2_2,false);
		setenableEditPanel(jPanel1_2_2_1,false);
		CurrText.setEditable(false);
		this.ExchRateText.setEditable(false);
	}
	
	protected void enableEditMode()	{  
		this.RcvToUnitCodeComp.setEnabled(true);
		RcvToText.setEditable(false);
		RcvToAccountText.setEditable(false);
		this.TransferComp.setEnabled(true);
		ExchRateText.setEditable(false);
		this.AmountText.setEditable(false);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.TransactionDateDate.setEditable(true);	
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
	}
	
	protected void disableEditMode(){
		this.RcvToUnitCodeComp.setEnabled(false);
		RcvToText.setEditable(false);
		RcvToAccountText.setEditable(false);
		this.TransferComp.setEnabled(false);
		this.ExchRateText.setEditable(false);
		this.AmountText.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.TransactionDateDate.setEditable(false);	
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
	}	
	
	ArrayList validityMsgs = new ArrayList();	
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (RcvToUnitCodeComp.getText() == "")
			addInvalid("Receive to unit code must be selected");
		if (RcvToAccountText.getText() == "")
			addInvalid("Receive to account must be selected");
		if (TransferComp.getObject()==null)
			addInvalid("Transfer from must be selected");
		if (TransactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (DescriptionTextArea.getText().equals(""))
			addInvalid("Description Text Area must be filled");
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
	
	protected void gui2entity(){		
		PmtUnitBankCashTrans a= (PmtUnitBankCashTrans)TransferComp.getObject();		
		CashBankAccount cb = null;
		if (a != null) {
			if (a.getPayTo().equalsIgnoreCase("BANK")) {
				cb = a.getRcvBankAccount();
				entity().setReceiveTo("BANK");
				entity().setBankAccount((BankAccount) cb);
				entity().setCashAccount(null);
				entity().setUnit(cb.getUnit());
			} else {
				cb = a.getRcvCashAccount();
				entity().setReceiveTo("CASH");
				entity().setCashAccount((CashAccount) cb);
				entity().setBankAccount(null);
				entity().setUnit(cb.getUnit());
			}
			entity().setRcvUnitCode(cb.getUnit());
			entity().setTransferFrom(a.getIndex());
			entity().setTransferFrom1(a);
			entity().setCurrency(a.getCurrency());
		}
		Object obj = ExchRateText.getValue();
		Number amount = (Number) obj;
		entity().transTemplateRead(
				this.OriginatorComp,this.ApprovedComp,
				this.ReceivedComp,this.RefNoText.getText(),
				this.DescriptionTextArea.getText(), this.RemarksTextArea.getText(),
				this.TransactionDateDate.getDate(),amount.doubleValue());
		obj=AmountText.getValue();
		amount=(Number)obj;
		entity().setAmount(amount.doubleValue());
	}
	
	protected  void entity2gui(){
		TransferComp.findData(entity().getTransferFrom());
		if(TransferComp.getObject()==null){
			RcvToUnitCodeComp.setText("");
			RcvToText.setText("");
			RcvToAccountText.setText("");
			CurrText.setText("");
			AmountBaseText.setValue(new Double(0.0));
		}			
		if(entity().getExchangeRate()>0)
			ExchRateText.setValue(new Double(entity().getExchangeRate()));
		else
			ExchRateText.setValue(new Double(0.0));
		
		if(entity().getCurrency()==null){
			CurrText.setText(baseCurrency.getSymbol());
			if(entity().getExchangeRate()==0)
				ExchRateText.setValue(new Double(1.0));
		}		
		if(new Double(entity().getAmount()).compareTo(new Double(0))!=0)
			AmountText.setValue(new Double(entity().getAmount()));
		else
			AmountText.setValue(new Double(0.0));
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
		AmountBaseCurrText.setText(this.baseCurrency.getSymbol());
		StatusText.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else	
			SubmittedDateText.setText("");		
	}
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	RcvUnitBankCashTrns entity() {
		return m_entity;
	}
	
	void setEntity(Object m_entity) {
		RcvUnitBankCashTrns oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (RcvUnitBankCashTrns)m_entity;
		this.m_entity.addPropertyChangeListener(this);
		readEntityState();
	}
	
	protected Object createNew(){  
		RcvUnitBankCashTrns a  = new RcvUnitBankCashTrns();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}
	
	protected void doSave() {
		if (!cekValidity()) return;
		gui2entity();
		if (hasAnotherUnpostedTransaction()) return;
		super.doSave();
		AccountingSQLSAP sql=new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_RCV_UNIT_BANKCASH_TRNS, m_conn);
				entity().setIndex(index);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	private boolean hasAnotherUnpostedTransaction() {
		PmtUnitBankCashTrans trans = entity().getTransferFrom1();
		
		if(trans==null)
			return false;
		
		GenericMapper mapper = MasterMap.obtainMapperFor(RcvUnitBankCashTrns.class);
		mapper.setActiveConn(this.m_conn);
		
		String whereClausa = "TRANSFERFROM=" + trans.getIndex() + " AND STATUS<3";
		if(entity().getIndex()>0)
			whereClausa += " AND AUTOINDEX!=" + entity().getIndex();
		
		List list = mapper.doSelectWhere(whereClausa);
		
		if(list.size()==0)
			return false;
		
		JOptionPane.showMessageDialog(this, "There is a transaction for Payment Unit Bank/Cash Transfer: " + 
				trans.getReferenceNo() + " " +
				"that has not been posted yet.\n" +
				"Please post it before creating a new transaction.");
		return true;
	}
	
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
		AmountBaseCurrText.setText(this.baseCurrency.getSymbol());		
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	
	protected void clearForm() {
		clearTextField(jPanel1_2_2_1);
		clearTextField(jPanel1_2_2);		
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
		cleanUp();
		disableEditMode();
	}
	
	protected void doNew() {
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
		revalidatePaymentPicker();
	}

	private void revalidatePaymentPicker() {
		jPanel1_2_1.remove(TransferComp);
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();

		TransferComp.removePropertyChangeListener("object", this);
		TransferComp = new LookupCashBankTransferPicker(m_conn, m_sessionid);
		TransferComp.addPropertyChangeListener("object", this);

		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();

		TransferComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(TransferComp, gridBagConstraints);
		jPanel1_2_1.revalidate(); 
	}
}