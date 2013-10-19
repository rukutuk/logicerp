package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
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
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.cgui.report.ES_Project;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.*;
import pohaci.gumunda.titis.accounting.logic.BeginningBalanceBusinessLogic;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.*;


public class ExpenseProjectPanel extends RevTransactionPanel implements ActionListener, PropertyChangeListener,DocumentListener{
	private static final long serialVersionUID = 1L;
	private ExpenseSheet m_entity;
	public ExpenseSheetPanel guiParent;
	Employee defaultOriginator;
	Employee defaultApproved;
	Employee defaultReceived;
	
	public ExpenseProjectPanel (Connection conn, long sessionid,ExpenseSheetPanel parent) {
		guiParent=parent;
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();	
		initTabel();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid); 
		initExchangeRateHelper(m_conn, m_sessionid);
		setDefaultSignature(); 
		setEntity(new ExpenseSheet());
		m_entityMapper = MasterMap.obtainMapperFor(ExpenseSheet.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
		
		setEnableButtonBawah(false);
	}
	
	private void setDefaultExchangeRate(Currency currency) {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		ESExchRateText.setValue(new Double(rate));
	}
	
	private LookupESOwner ESOwnerComp;	
	private void initComponents() {
		setAccountComp(new LookupESCashAdvanceProject(m_conn,m_sessionid));
		ESOwnerComp = new LookupESOwner(m_conn,m_sessionid,LookupESOwner.PROJECT);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");        
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));		
		java.awt.GridBagConstraints gridBagConstraints;
		jPanel1 = new javax.swing.JPanel();
		jPanel1_1 = new javax.swing.JPanel();
		TopButtonPanel = new javax.swing.JPanel();
		m_newBtn = new javax.swing.JButton();
		m_editBtn = new javax.swing.JButton();
		m_saveBtn = new javax.swing.JButton();
		m_deleteBtn = new javax.swing.JButton();
		m_cancelBtn = new javax.swing.JButton();
		m_submitBtn = new javax.swing.JButton();
		jPanel1_2 = new javax.swing.JPanel();
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_1 = new javax.swing.JPanel();
		StatusLabel = new javax.swing.JLabel();
		SubmittedDateLabel = new javax.swing.JLabel();
		CashAdvanceLabel = new javax.swing.JLabel();	
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		jPanel1_2_1_1 = new javax.swing.JPanel();
		CurrencyLabel = new javax.swing.JLabel();
		AmountLabel = new javax.swing.JLabel();
		AmountBaseCurrentLabel = new javax.swing.JLabel();
		CashAdvanceCurrText = new javax.swing.JTextField();
		ExchRateLabel = new javax.swing.JLabel();
		CashAdvanceExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		CashAdvanceAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		CAAmountBaseCurrText = new javax.swing.JTextField();
		CAAmountBaseText = new javax.swing.JFormattedTextField(m_numberFormatter);
		ExpenseSheetGUI = new javax.swing.JPanel();
		StatusLabel12 = new javax.swing.JLabel();
		jSeparator2 = new javax.swing.JSeparator();
		jPanel1_2_1_2 = new javax.swing.JPanel();
		CurrencyLabel2 = new javax.swing.JLabel();
		AmountLabel2 = new javax.swing.JLabel();
		AmountBaseCurrentLabel2 = new javax.swing.JLabel();
		EsProjectTypeLabel = new javax.swing.JLabel();
		ExchRateLabel2 = new javax.swing.JLabel();
		ESExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		ESAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		ESAmountBaseCurrText = new javax.swing.JTextField();
		ESAmountBaseText = new javax.swing.JFormattedTextField(m_numberFormatter);
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScroll = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		RemarksScroll = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		jPanel1_2_2_1 = new javax.swing.JPanel();
		ExpSheetNoLabel = new javax.swing.JLabel();
		ExpSheetDateLabel = new javax.swing.JLabel();
		VoucherDateLabel = new javax.swing.JLabel();
		ProjectCodeLabel = new javax.swing.JLabel();
		CustomerLabel = new javax.swing.JLabel();
		WorkDescLabel = new javax.swing.JLabel();
		VoucherDateText = new javax.swing.JTextField();
		ProjectCodeText = new javax.swing.JTextField();
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
		PONoLabel = new javax.swing.JLabel();
		IPCNoLabel = new javax.swing.JLabel();
		ContractNoText = new javax.swing.JTextField();
		IPCNoText = new javax.swing.JTextField();
		RefNoText = new javax.swing.JTextField();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel2_1 = new javax.swing.JPanel();
		ButtomButtonPanel = new javax.swing.JPanel();
		AddESProjectAccountBtn = new javax.swing.JButton();
		DeleteESProjectAccountBtn = new javax.swing.JButton();
		ESProjectAccountScrollPane = new javax.swing.JScrollPane();
		m_table = new buttomTabbleESProject();
		ESCurrComp = new CurrencyPicker(m_conn,m_sessionid);
		CACurrComp = new CurrencyPicker(m_conn,m_sessionid);
		
		setLayout(new java.awt.BorderLayout());
		
		jPanel1.setLayout(new java.awt.BorderLayout());
		jPanel1.setPreferredSize(new java.awt.Dimension(725,460));
		jPanel1_1.setLayout(new java.awt.BorderLayout());
		
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
		
		jPanel1.add(jPanel1_1, java.awt.BorderLayout.NORTH);// panel button
		
		jPanel1_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2.setPreferredSize(new java.awt.Dimension(700, 300));
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(500, 305));
		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(440, 160));
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
		
		EsProjectTypeLabel.setText("ES Owner");
		EsProjectTypeLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;        
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(EsProjectTypeLabel, gridBagConstraints);
		
		CashAdvanceLabel.setText("Cash Advance*");
		CashAdvanceLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;       
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(CashAdvanceLabel, gridBagConstraints);
		
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
		ESOwnerComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ESOwnerComp, gridBagConstraints);
		
		accountComp().setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(accountComp(), gridBagConstraints);
		
		jPanel1_2_1_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(410, 69));
		CurrencyLabel.setText("Currency");
		CurrencyLabel.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_1_1.add(CurrencyLabel, gridBagConstraints);
		
		AmountLabel.setText("Amount");
		AmountLabel.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_1_1.add(AmountLabel, gridBagConstraints);
		
		AmountBaseCurrentLabel.setText("Amount Base Curr.");
		AmountBaseCurrentLabel.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_1_1.add(AmountBaseCurrentLabel, gridBagConstraints);
		
		CACurrComp.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(CACurrComp, gridBagConstraints);
		
		ExchRateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		ExchRateLabel.setText("Exch. Rate");
		ExchRateLabel.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(ExchRateLabel, gridBagConstraints);
		
		CashAdvanceExchRateText.setPreferredSize(new java.awt.Dimension(87, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(CashAdvanceExchRateText, gridBagConstraints);
		
		CashAdvanceAmountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(CashAdvanceAmountText, gridBagConstraints);
		
		CAAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1_1.add(CAAmountBaseCurrText, gridBagConstraints);
		
		CAAmountBaseText.setPreferredSize(new java.awt.Dimension(147, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(CAAmountBaseText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(jPanel1_2_1_1, gridBagConstraints);
		
		ExpenseSheetGUI.setLayout(new java.awt.GridBagLayout());
		
		ExpenseSheetGUI.setPreferredSize(new java.awt.Dimension(410, 15));
		StatusLabel12.setText("Expense Sheet");
		StatusLabel12.setPreferredSize(new java.awt.Dimension(75, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		ExpenseSheetGUI.add(StatusLabel12, gridBagConstraints);
		
		jSeparator2.setPreferredSize(new java.awt.Dimension(200, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		ExpenseSheetGUI.add(jSeparator2, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(ExpenseSheetGUI, gridBagConstraints);
		
		jPanel1_2_1_2.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_2.setPreferredSize(new java.awt.Dimension(410, 65));
		CurrencyLabel2.setText("Currency");
		CurrencyLabel2.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_1_2.add(CurrencyLabel2, gridBagConstraints);
		
		AmountLabel2.setText("Total Amount");
		AmountLabel2.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_1_2.add(AmountLabel2, gridBagConstraints);
		
		AmountBaseCurrentLabel2.setText("Amount Base Curr.");
		AmountBaseCurrentLabel2.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_1_2.add(AmountBaseCurrentLabel2, gridBagConstraints);
		
		ESCurrComp.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
		jPanel1_2_1_2.add(ESCurrComp, gridBagConstraints);
		
		ExchRateLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		ExchRateLabel2.setText("Exch. Rate*");
		ExchRateLabel2.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(ExchRateLabel2, gridBagConstraints);
		
		ESExchRateText.setPreferredSize(new java.awt.Dimension(90, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(ESExchRateText, gridBagConstraints);
		
		ESAmountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
		jPanel1_2_1_2.add(ESAmountText, gridBagConstraints);
		
		ESAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
		jPanel1_2_1_2.add(ESAmountBaseCurrText, gridBagConstraints);
		
		ESAmountBaseText.setPreferredSize(new java.awt.Dimension(147, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill=java.awt.GridBagConstraints.HORIZONTAL;
		
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_2.add(ESAmountBaseText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(jPanel1_2_1_2, gridBagConstraints);
		
		DescriptionLabel.setText("Description *");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1.add(DescriptionLabel, gridBagConstraints);
		
		DescriptionScroll.setPreferredSize(new java.awt.Dimension(290, 40));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScroll.setViewportView(DescriptionTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1.add(DescriptionScroll, gridBagConstraints);
		
		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1.add(RemarksLabel, gridBagConstraints);
		
		RemarksScroll.setPreferredSize(new java.awt.Dimension(290, 40));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScroll.setViewportView(RemarksTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1.add(RemarksScroll, gridBagConstraints);
		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(415, 160));
		ExpSheetNoLabel.setText("Exp. Sheet No");
		ExpSheetNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ExpSheetNoLabel, gridBagConstraints);
		
		ExpSheetDateLabel.setText("Exp. Sheet Date*");
		ExpSheetDateLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ExpSheetDateLabel, gridBagConstraints);
		
		VoucherDateLabel.setText("Voucher Date");
		VoucherDateLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(VoucherDateLabel, gridBagConstraints);
		
		ProjectCodeLabel.setText("Project Code");
		ProjectCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ProjectCodeLabel, gridBagConstraints);
		
		CustomerLabel.setText("Customer");
		CustomerLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(CustomerLabel, gridBagConstraints);
		
		WorkDescLabel.setText("Work Desc. ");
		WorkDescLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(WorkDescLabel, gridBagConstraints);
		
		VoucherDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(VoucherDateText, gridBagConstraints);
		
		ProjectCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ProjectCodeText, gridBagConstraints);
		
		CustomerText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(CustomerText, gridBagConstraints);
		
		WorkDescScroll.setPreferredSize(new java.awt.Dimension(290, 90));
		WorkDescTextArea.setColumns(20);
		WorkDescTextArea.setLineWrap(true);
		WorkDescTextArea.setRows(5);
		WorkDescScroll.setViewportView(WorkDescTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(WorkDescScroll, gridBagConstraints);
		
		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(UnitCodeLabel, gridBagConstraints);
		
		UnitCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(UnitCodeText, gridBagConstraints);
		
		ActivityCodeLabel.setText("Activity Code");
		ActivityCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ActivityCodeLabel, gridBagConstraints);
		
		ActivityCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ActivityCodeText, gridBagConstraints);
		
		DepartmentLabel.setText("Department");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(DepartmentLabel, gridBagConstraints);
		
		ORNoLabel.setText("O.R No");
		ORNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ORNoLabel, gridBagConstraints);
		
		DepartmentText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(DepartmentText, gridBagConstraints);
		
		ORNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ORNoText, gridBagConstraints);
		
		PONoLabel.setText("PO No");
		PONoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(PONoLabel, gridBagConstraints);
		
		IPCNoLabel.setText("IPC No");
		IPCNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(IPCNoLabel, gridBagConstraints);
		
		ContractNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(ContractNoText, gridBagConstraints);
		
		IPCNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(IPCNoText, gridBagConstraints);
		
		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
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
		
		jPanel1_3.setPreferredSize(new java.awt.Dimension(650, 100));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());
		
		OriginatorComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(279, 110));
		jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.setLayout(new java.awt.BorderLayout());
		
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		
		ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(279, 110));
		jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());
		
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		
		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Created by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(279, 110));
		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);
		
		add(jPanel1, java.awt.BorderLayout.NORTH);
		
		jPanel2.setLayout(new java.awt.BorderLayout());
		
		jPanel2.setPreferredSize(new java.awt.Dimension(200, 160));
		jPanel2_1.setLayout(new java.awt.BorderLayout());
		
		ButtomButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));
		
		ButtomButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
		AddESProjectAccountBtn.setText("Add");
		AddESProjectAccountBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		AddESProjectAccountBtn.setPreferredSize(new java.awt.Dimension(50, 21));
		ButtomButtonPanel.add(AddESProjectAccountBtn);
		
		DeleteESProjectAccountBtn.setText("Delete");
		DeleteESProjectAccountBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		DeleteESProjectAccountBtn.setPreferredSize(new java.awt.Dimension(50, 21));
		ButtomButtonPanel.add(DeleteESProjectAccountBtn);
		
		jPanel2_1.add(ButtomButtonPanel, java.awt.BorderLayout.WEST);
		
		jPanel2.add(jPanel2_1, java.awt.BorderLayout.NORTH);
		
		ESProjectAccountScrollPane.setPreferredSize(new java.awt.Dimension(650, 110));
		m_table.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {                
				},
				new String [] {
						"Expense Type", "Description", "Amount"
				}
		));
		ESProjectAccountScrollPane.setViewportView(m_table);		
		jPanel2.add(ESProjectAccountScrollPane, java.awt.BorderLayout.CENTER);		
		add(jPanel2, java.awt.BorderLayout.CENTER);		
	}
	
	protected void setAccountComp(LookupPicker accountComp) {
		LookupPicker old = AccountComp;
		if (old!=null){
			old.removePropertyChangeListener("object",this);
		}
		AccountComp = accountComp;
		AccountComp.addPropertyChangeListener("object",this);
	}
	
	private LookupPicker accountComp() {
		return AccountComp;
	}
	
	private void addingListener(){		
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);	
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		accountComp().addPropertyChangeListener("object",this);
		ESOwnerComp.addPropertyChangeListener("object",this);
		ESAmountText.getDocument().addDocumentListener(this);
		TransactionDateDate.addPropertyChangeListener(this);
		ESOwnerComp.hapusActionListenerBrowse();
		ESOwnerComp.m_browseBt.addActionListener(this);
		AccountComp.hapusActionListenerBrowse();
		AccountComp.m_browseBt.addActionListener(this);
		AddESProjectAccountBtn.addActionListener(this);
		DeleteESProjectAccountBtn.addActionListener(this);
		TransactionDateDate.addPropertyChangeListener("date", this);
	} 	
	
	private javax.swing.JLabel UnitCodeLabel;
	
	protected void doNew() {		
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
		setEnableButtonBawah(true);
		validateESNO();
		validateCashAdvance(null);
	}
	
	protected void doCancel() {	
		super.doCancel();
		setEnableButtonBawah(false);
		LoadESDetail(entity().getIndex());
		disableEditMode();
	}
	
	protected void doSubmit() {
		super.doSubmit();
		setEnableButtonBawah(false);
	}
	
	public void doEdit() {	
		super.doEdit();
		setEnableButtonBawah(true);
		if (entity()!=null){
			if (entity().getCurrency()!=null)
				if (!entity().getCurrency().getSymbol().equals(baseCurrency.getSymbol()))
					ESExchRateText.setEditable(true);
				else
					ESExchRateText.setEditable(false);
		}
	}
	
	protected void doDelete() {		
		super.doDelete();
		if (entity().getStatus()==StateTemplateEntity.State.NEW){ 	
			clearKomponen();
			clearForm();
			setEnableButtonBawah(false);				
		}
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_printViewRefNoBtn) {
			 if (m_entity.getIndex()>0)
				 new ES_Project(m_entity,m_conn);
			 else{
				 JOptionPane.showMessageDialog(this, "Data is empty",
						 "Warning", JOptionPane.WARNING_MESSAGE);
				 return;
			 }			
		 }
		 else if(e.getSource() == m_searchRefNoBtn) {  
			 String kriteria = "(" + IDBConstants.ATTR_PMT_CA_IOU_PROJECT_SETTLED + " IS NOT NULL  OR " +
			 IDBConstants.ATTR_PMT_CA_PROJECT + " IS NOT NULL OR " +
			 IDBConstants.ATTR_BEGINNING_BALANCE + " IS NOT NULL) " +
			 "ORDER BY " +IDBConstants.ATTR_TRANSACTION_DATE+" ASC, "+ IDBConstants.ATTR_REFERENCE_NO +" ASC";
			 SearchExpenseSheetDialog dlg = new SearchExpenseSheetDialog(GumundaMainFrame.getMainFrame(), 
					 "Search Expense Sheet", m_conn, m_sessionid,new ExpenseSheetLoader(m_conn),
					 IConstants.EXPENSE_SHEET_PROJECT, kriteria);	
			 dlg.find();
			 dlg.setVisible(true);
			 if (dlg.selectedObj != null){
				 doLoad(dlg.selectedObj);
			 }
		 }
		 else if (e.getSource()==ESOwnerComp.m_browseBt){
			 ESOwnerComp.done();
			 if(ESOwnerComp.getObject()!=null){
				 Employee emp=(Employee)ESOwnerComp.getObject();
				 validateCashAdvance(emp);		
			 }
		 }
		 else if (e.getSource()==AccountComp.m_browseBt){
			 if(ESOwnerComp.getObject()!=null){
				 AccountComp.done();
			 }
			 else
				 JOptionPane.showMessageDialog(this,"Please select employee on the left list");
		 }
		 else if(e.getSource()==AddESProjectAccountBtn){
			 ProjectAccountComp=new LookupEspenseSheetPicker(m_conn, m_sessionid,IConstants.EXPENSE_SHEET_PROJECT);
			 ProjectAccountComp.done();
			 if(ProjectAccountComp.getObject()!=null){   
				 Vector temp1=new Vector();
				 Account acc=(Account)ProjectAccountComp.getObject();
				 temp1.addElement(new Integer(m_table.getRowCount()+1));
				 temp1.addElement(acc.getName());
				 temp1.addElement("");
				 temp1.addElement(new Double(0));
				 m_table.addData(temp1, acc);
			 }
		 }
		 else if (e.getSource()==DeleteESProjectAccountBtn){
			 if (m_table.getRowCount()>0){
				 if (m_table.getSelectedRow()>=0)				
					 m_table.removeData(m_table.getSelectedRow());
			 }
		 }		
	}

	private void validateCashAdvance(Employee emp) {		
		 jPanel1_2_1.remove(accountComp());
		 jPanel1_2_1.revalidate();
		 jPanel1_2_1.repaint();
		 setAccountComp(new LookupESCashAdvanceProject(m_conn, m_sessionid,emp));
		 accountComp().setPreferredSize(new java.awt.Dimension(290, 18));
		 java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		 gridBagConstraints = new java.awt.GridBagConstraints();
		 gridBagConstraints.gridx = 1;
		 gridBagConstraints.gridy = 3;
		 gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		 gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		 jPanel1_2_1.add(accountComp(), gridBagConstraints);
		 jPanel1_2_1.revalidate();
		 setGuiEmpty();
	}
	
	private void validateESNO() {		
		 jPanel1_2_1.remove(ESOwnerComp);
		 jPanel1_2_1.revalidate();
		 jPanel1_2_1.repaint();
		 ESOwnerComp = new LookupESOwner(m_conn,m_sessionid,LookupESOwner.PROJECT);
		 ESOwnerComp.setPreferredSize(new java.awt.Dimension(290, 18));
		 java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		 gridBagConstraints = new java.awt.GridBagConstraints();
		 gridBagConstraints.gridx = 1;
		 gridBagConstraints.gridy = 2;
		 gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		 gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		 jPanel1_2_1.add(ESOwnerComp, gridBagConstraints);
		 jPanel1_2_1.revalidate();
		 ESOwnerComp.hapusActionListenerBrowse();
		 ESOwnerComp.m_browseBt.addActionListener(this);
	}
	
	private void setGuiEmpty(){						
		CACurrComp.setObject(null);
		CashAdvanceAmountText.setValue(null);
		CashAdvanceExchRateText.setValue(null);
		ESCurrComp.setObject(null);
		CashAdvanceExchRateText.setValue(null);		
		CAAmountBaseCurrText.setText(null);	
		CAAmountBaseText.setValue(null);
		setNullProject();		
	}
	
	private void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.EXPENSE_SHEET_PROJECT, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.EXPENSE_SHEET_PROJECT, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.EXPENSE_SHEET_PROJECT, Signature.SIGN_CREATED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	
	
	protected void entity2gui() {	
		ESOwnerComp.setObject(entity().getEsOwner());
		if (entity().getEsOwner()!=null)
			validateCashAdvance(entity().getEsOwner());
		if (entity().getPmtCaIouProjectSettled()!=null)
			accountComp().setObject(entity().getPmtCaIouProjectSettled());
		else if (entity().getPmtCaProject()!=null)    		
			accountComp().setObject(entity().getPmtCaProject());
		else if (entity().getBeginningBalance()!=null){
			accountComp().setObject(entity().getBeginningBalance());			
		}else
			accountComp().setObject(null);
		
		RefNoText.setText(entity().getReferenceNo());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getTransactionDate()!=null){
			TransactionDateDate.setDate(entity().getTransactionDate());
			VoucherDateText.setText(dateformat.format((entity().getTransactionDate())));
		}else{
			TransactionDateDate.setDate(new Date());
			VoucherDateText.setText(null);
		}
		
		if (entity().getProject()!=null){
			ProjectCodeText.setText(entity().getProject().toString());
			CustomerText.setText(entity().getProject().getCustomer().getName());
			WorkDescTextArea.setText(entity().getProject().getWorkDescription());
			UnitCodeText.setText(entity().getProject().getUnit().toString());
			ActivityCodeText.setText(entity().getProject().getActivity().getName());    		
			ORNoText.setText(entity().getProject().getORNo());
			IPCNoText.setText(entity().getProject().getIPCNo());
			ContractNoText.setText(entity().getProject().getPONo());
			DepartmentText.setText(entity().getProject().getDepartment().getName());    		
		}
		else{
			ProjectCodeText.setText("");
			CustomerText.setText("");
			WorkDescTextArea.setText("");
			UnitCodeText.setText("");
			ActivityCodeText.setText("");    		
			ORNoText.setText("");
			IPCNoText.setText("");
			ContractNoText.setText("");
			DepartmentText.setText("");  
		}
		
		if (entity().getCurrency()!=null){			
			ESCurrComp.setObject(entity().getCurrency());			
			if(entity().getExchangeRate()>0)
				ESExchRateText.setValue(new Double(entity().getExchangeRate()));
			else
				ESExchRateText.setValue(new Double(1));   
			if (entity().getAmount()>0)
				ESAmountText.setValue(new Double(entity().getAmount()));
			else
				ESAmountText.setValue(new Double(1));
		}else{
			ESCurrComp.setObject(null);
			ESExchRateText.setValue(null);
			ESAmountText.setValue(null);			
		}
		ESAmountBaseCurrText.setText(baseCurrency.getSymbol());		
		DescriptionTextArea.setText(entity().getDescription());
		RemarksTextArea.setText(entity().getRemarks());		
		OriginatorComp.setEmployee(entity().getEmpOriginator());
		OriginatorComp.setJobTitle(entity().getJobTitleOriginator());
		OriginatorComp.setDate(entity().getDateOriginator());    	
		
		ApprovedComp.setEmployee(entity().getEmpApproved());
		ApprovedComp.setJobTitle(entity().getJobTitleApproved());
		ApprovedComp.setDate(entity().getDateApproved());
		
		ReceivedComp.setEmployee(entity().getEmpReceived());
		ReceivedComp.setDate(entity().getDateReceived());
		ReceivedComp.setJobTitle(entity().getJobTitleReceived());
		
		StatusText.setText(entity().statusInString());    	
		
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");
		LoadESDetail(entity().getIndex());
	}
	
	protected void gui2entity() {
		Object objk = AccountComp.getObject();
		
		if (objk instanceof PmtCAIOUProjectSettled) {
			PmtCAIOUProjectSettled a = (PmtCAIOUProjectSettled) objk;
			entity().setPmtCaIouProjectSettled(a);
			entity().setEsProjectType("IOU");
			entity().setProject(a.getPmtcaiouproject().getProject());
			entity().setPmtCaProject(null);
			entity().setBeginningBalance(null);
		}
		else if (objk instanceof PmtCAProject) {
			PmtCAProject a = (PmtCAProject) objk;						
			entity().setPmtCaProject(a);
			entity().setEsProjectType("General");			
			entity().setProject(a.getProject());
			entity().setPmtCaIouProjectSettled(null);
			entity().setBeginningBalance(null);
		}else if (objk instanceof BeginningCashAdvance) {
			BeginningCashAdvance a = (BeginningCashAdvance) objk;
			entity().setBeginningBalance(a);
			entity().setEsProjectType("Beginning Balance");
			entity().setProject(a.getProject());
			entity().setPmtCaIouOthersSettled(null);
			entity().setPmtCaProject(null);
		}		
		entity().setDepartmentgroup(-1);
		entity().setDepartment(entity().getProject().getDepartment());
		Currency cur = (Currency)ESCurrComp.getObject();
		entity().setCurrency(cur);
		Number obj= new Double(0.0);
		entity().setAmount(obj.doubleValue());
		obj=(Number)ESExchRateText.getValue();
		entity().setExchangeRate(obj.doubleValue());
		entity().setTransactionDate(TransactionDateDate.getDate());
		entity().setRemarks(RemarksTextArea.getText());
		Employee temp=(Employee)ESOwnerComp.getObject();
		entity().setEsOwner(temp);
		EmployeePicker helpEmp=new EmployeePicker(m_conn,m_sessionid);
		entity().setUnit(helpEmp.findUnitEmployee(temp));
		
		entity().transTemplateRead(
				this.OriginatorComp, this.ApprovedComp,
				this.ReceivedComp, this.RefNoText,
				this.DescriptionTextArea
		);
		detailAccountOperation();		
	}
	
	ExpenseSheet entity() {
		return m_entity;
	}
	
	protected StateTemplateEntity currentEntity() {		
		return entity();
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity() {
		validityMsgs.clear();
		if (accountComp().getObject()==null)
			addInvalid("CashAdvanceCompe must be selected");		
		if (DescriptionTextArea.equals(""))
			addInvalid("Description must be selected");		
		if (TransactionDateDate.getDate()==null)
			addInvalid("Description must be selected");
		if(ESExchRateText.getText().equals(""))
			addInvalid("Exchange Rate must be filled");
		if(DescriptionTextArea.getText().equals(""))
			addInvalid("Description must be filled");
		detailAccountOperation();
		ExpenseSheetDetail[] temp=entity().getExpenseSheetDetail();
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
	
	protected boolean cekValidity2() {
		if (entity().getStatus()==0) 
			return true;	
		gui2entity();
		long indexCA=0;
		String kolom="";
		if (entity().getPmtCaIouOthersSettled()!=null){
			indexCA=entity().getPmtCaIouOthersSettled().getIndex();
			kolom=IDBConstants.ATTR_PMT_CA_IOU_OTHERS_SETTLED;
		}
		else if (entity().getPmtCaIouProjectSettled()!=null){
			indexCA=entity().getPmtCaIouProjectSettled().getIndex();
			kolom=IDBConstants.ATTR_PMT_CA_IOU_PROJECT_SETTLED;
		}		
		else if (entity().getPmtCaOthers()!=null){
			indexCA=entity().getPmtCaOthers().getIndex();
			kolom=IDBConstants.ATTR_PMT_CA_OTHERS;
		}
		else if (entity().getPmtCaProject()!=null){
			indexCA=entity().getPmtCaProject().getIndex();
			kolom=IDBConstants.ATTR_PMT_CA_PROJECT;
		}
		else if (entity().getBeginningBalance()!=null){
			indexCA=entity().getBeginningBalance().getIndex();
			kolom =IDBConstants.ATTR_BEGINNING_BALANCE;		
		}			
		
		GenericMapper mapnya = MasterMap.obtainMapperFor(ExpenseSheet.class);
		mapnya.setActiveConn(m_conn);
		String strWhere = kolom+"="+indexCA;
		Object[] listData=mapnya.doSelectWhere(strWhere).toArray();
		if (listData.length!=0){
			ExpenseSheet temp=(ExpenseSheet)listData[0];
			String status=temp.statusInString();
			String no=temp.getReferenceNo();
			if (temp.getStatus()!=0)
				addInvalid("Warning this Cash Advance has been used and "+status+
						"\nThe Expense sheet reference no is "+no+
						"\nYou cannot save this Expense Sheet!!\n"
				);
			else
				addInvalid("Warning this Cash Advance has been used and "+status+
						"\nPlease edit existing expense sheet data"+
						"\nYou cannot save this Expense Sheet!!\n"
				);
			
		}		
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
	
	
	protected void disableEditMode() {
		this.TransactionDateDate.setEditable(false);		
		this.accountComp().setEnabled(false);
		this.ESOwnerComp.setEnabled(false);
		this.DescriptionTextArea.setEditable(false);
		this.RemarksTextArea.setEditable(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
		this.StatusText.setEditable(false);
		this.SubmittedDateText.setEditable(false);
		this.CashAdvanceCurrText.setEditable(false);
		this.CashAdvanceExchRateText.setEditable(false);
		this.CAAmountBaseText.setEditable(false);
		this.CAAmountBaseCurrText.setEditable(false);
		this.CashAdvanceAmountText.setEditable(false);
		this.ESAmountText.setEditable(false);
		this.ESAmountBaseCurrText.setEditable(false);
		this.ESAmountBaseText.setEditable(false);
		this.WorkDescTextArea.setEditable(false);  
		this.WorkDescTextArea.setBackground(Color.lightGray);
		
		
		this.VoucherDateText.setEditable(false);
		this.ProjectCodeText.setEditable(false);
		this.CustomerText.setEditable(false);
		this.ORNoText.setEditable(false);
		this.UnitCodeText.setEditable(false);
		this.ActivityCodeText.setEditable(false);
		this.DepartmentText.setEditable(false);
		this.IPCNoText.setEditable(false);
		this.RefNoText.setEditable(false);
		this.ContractNoText.setEditable(false);
		this.ESCurrComp.setEnabled(false);
		this.CACurrComp.setEnabled(false);
		this.ESExchRateText.setEditable(false);
		this.ESAmountBaseText.setEditable(false);
		this.ESAmountText.setEditable(false);
	}
	
	protected void enableEditMode() {
		this.ESOwnerComp.setEnabled(true);
		this.accountComp().setEnabled(true);	
		this.ESAmountText.setEditable(true);
		this.ESAmountBaseCurrText.setEditable(false);
		this.ESAmountBaseText.setEditable(true);
		this.TransactionDateDate.setEditable(true);
		
		this.DescriptionTextArea.setEditable(true);
		this.RemarksTextArea.setEditable(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
		this.ESAmountBaseCurrText.setText(baseCurrency.getSymbol());
		this.CAAmountBaseCurrText.setText(baseCurrency.getSymbol());
		
		this.ESCurrComp.setEnabled(false);
		this.CACurrComp.setEnabled(false);
		this.ESExchRateText.setEditable(false);
		this.ESAmountBaseText.setEditable(false);
		this.ESAmountText.setEditable(false);
	}
	
	protected Object createNew() {
		ExpenseSheet a = new ExpenseSheet();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a;
	}
	
	
	void setEntity(Object m_entity) {
		ExpenseSheet oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (ExpenseSheet)m_entity; 	
		this.m_entity.addPropertyChangeListener(this);
		if(this.m_entity.getBeginningBalance()!=null){
			setBeginningBalance();
		}
	}
	
	private void setBeginningBalance() {
		BeginningCashAdvance tempClass = this.m_entity.getBeginningBalance();
		EmployeePicker helpEmp=new EmployeePicker(m_conn,m_sessionid);			
		Unit unit = helpEmp.findUnitEmployee(tempClass.getEmployee());
		if (unit==null){				
			UnitPicker unitpicker = new UnitPicker();
			unitpicker.init(m_conn, m_sessionid);				
			unit = unitpicker.getDefaultUnit();
		}
		BeginningBalanceBusinessLogic logic = new BeginningBalanceBusinessLogic(m_conn, m_sessionid);
		tempClass.setTrans(logic.findTransaction(unit));
		tempClass.showReferenceNo(true);		
		this.m_entity.setBeginningBalance(tempClass);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {		
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		if ("date".equals(evt.getPropertyName())){
			if (evt.getSource() == TransactionDateDate) {
				Currency curr = (Currency) ESCurrComp.getObject();
				setDefaultExchangeRate(curr);
			}
		}
		if ("object".equals(evt.getPropertyName())) {
			if (evt.getSource() == accountComp()) {
				Object obj = accountComp().getObject();
				if (obj == null) {
					ProjectCodeText.setText("");
					CACurrComp.setObject(null);
					CashAdvanceAmountText.setValue(null);
					ESCurrComp.setObject(null);
					CashAdvanceExchRateText.setValue(null);
					CAAmountBaseCurrText.setText(null);
					CAAmountBaseText.setValue(null);
				}

				else if (obj instanceof PmtCAIOUProjectSettled) {
					PmtCAIOUProjectSettled a = (PmtCAIOUProjectSettled) obj;
					CACurrComp.setObject(a.getCurrency());
					CashAdvanceAmountText.setValue(new Double(a.getAmount()));
					ESCurrComp.setObject(a.getCurrency());
					if (a.getCurrency().getSymbol().equalsIgnoreCase(
							baseCurrency.getSymbol())) {
						ESExchRateText.setValue(new Double(1));
						ESExchRateText.setEditable(false);
					} else{
						ESExchRateText.setEditable(true);
						setDefaultExchangeRate(a.getCurrency());
					}
					CAAmountBaseCurrText.setText(baseCurrency.getSymbol());
					CashAdvanceExchRateText.setValue(new Double(1));
					CAAmountBaseText.setValue(new Double(a.getAmount()
							* ((Double) CashAdvanceExchRateText.getValue())
									.doubleValue()));

					if (a.getPmtcaiouproject().getProject() != null) {
						ProjectCodeText.setText(a.getPmtcaiouproject()
								.getProject().toString());
						CustomerText.setText(a.getPmtcaiouproject()
								.getProject().getCustomer().getName());
						WorkDescTextArea.setText(a.getPmtcaiouproject()
								.getProject().getWorkDescription());
						UnitCodeText.setText(a.getPmtcaiouproject()
								.getProject().getUnit().toString());
						DepartmentText.setText(a.getPmtcaiouproject()
								.getProject().getDepartment().toString());
						ORNoText.setText(a.getPmtcaiouproject().getProject()
								.getORNo());
						IPCNoText.setText(a.getPmtcaiouproject().getProject()
								.getIPCNo());
						ContractNoText.setText(a.getPmtcaiouproject()
								.getProject().getPONo());
					} else {
						setNullProject();
					}
				}

				else if (obj instanceof PmtCAProject) {
					PmtCAProject a = (PmtCAProject) obj;
					CACurrComp.setObject(a.getCurrency());
					CashAdvanceAmountText.setValue(new Double(a.getAmount()));
					ESCurrComp.setObject(a.getCurrency());
					if (a.getCurrency().getSymbol().equalsIgnoreCase(
							baseCurrency.getSymbol())) {
						ESExchRateText.setValue(new Double(1));
						ESExchRateText.setEditable(false);
					} else {
						ESExchRateText.setEditable(true);
						setDefaultExchangeRate(a.getCurrency());
					}
					if (a.getExchangeRate() != 0)
						CashAdvanceExchRateText.setValue(new Double(a
								.getExchangeRate()));
					else
						CashAdvanceExchRateText.setValue(new Double(1));
					CAAmountBaseCurrText.setText(baseCurrency.getSymbol());
					CAAmountBaseText.setValue(new Double(a.getAmount()
							* ((Number) CashAdvanceExchRateText.getValue())
									.doubleValue()));
					if (a.getProject() != null) {
						ProjectCodeText.setText(a.getProject().toString());
						CustomerText.setText(a.getProject().getCustomer()
								.getName());
						WorkDescTextArea.setText(a.getProject()
								.getWorkDescription());
						UnitCodeText.setText(a.getProject().getUnit()
								.toString());
						DepartmentText.setText(a.getProject().getDepartment()
								.toString());
						ORNoText.setText(a.getProject().getORNo());
						IPCNoText.setText(a.getProject().getIPCNo());
						ContractNoText.setText(a.getProject().getPONo());
					} else {
						setNullProject();
					}
				}

				else if (obj instanceof BeginningCashAdvance) {
					BeginningCashAdvance a = (BeginningCashAdvance) obj;
					CACurrComp.setObject(a.getCurrency());
					CashAdvanceAmountText.setValue(new Double(a.getAccValue()));
					ESCurrComp.setObject(a.getCurrency());
					if (a.getCurrency().getSymbol().equals(
							baseCurrency.getSymbol())) {
						ESExchRateText.setValue(new Double(1));
						ESExchRateText.setEditable(false);
					} else {
						ESExchRateText.setEditable(true);
						setDefaultExchangeRate(a.getCurrency());
					}
					CashAdvanceExchRateText.setValue(new Double(a
							.getExchangeRate()));
					CAAmountBaseCurrText.setText(baseCurrency.getSymbol());
					CAAmountBaseText.setValue(new Double(a.getAccValue()
							* ((Number) CashAdvanceExchRateText.getValue())
									.doubleValue()));

					if (a.getProject() != null) {
						ProjectCodeText.setText(a.getProject().toString());
						CustomerText.setText(a.getProject().getCustomer()
								.getName());
						WorkDescTextArea.setText(a.getProject()
								.getWorkDescription());
						UnitCodeText.setText(a.getProject().getUnit()
								.toString());
						DepartmentText.setText(a.getProject().getDepartment()
								.toString());
						ORNoText.setText(a.getProject().getORNo());
						IPCNoText.setText(a.getProject().getIPCNo());
						ContractNoText.setText(a.getProject().getPONo());
					} else {
						setNullProject();
					}
				}
				// end if instance of
			}// end if evt.getsource
			else if (evt.getSource() == ESCurrComp) {
				Currency a = (Currency) ESCurrComp.getObject();
				if (a != null)
					if (!a.getSymbol().equalsIgnoreCase(
							baseCurrency.getSymbol())){
						ESExchRateText.setEditable(true);
						setDefaultExchangeRate(a);
					}
					else {
						ESExchRateText.setValue(new Double(1));
					}
			}
		}
	}

	private void setNullProject() {
		ProjectCodeText.setText("");
		CustomerText.setText("");
		WorkDescTextArea.setText("");
		UnitCodeText.setText("");
		DepartmentText.setText("");
		ORNoText.setText("");
		IPCNoText.setText("");
		ContractNoText.setText("");
	}
	
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
	}
	
	protected void doSave() {
		if (!cekValidity()||!cekValidity2()) 
			return;
		TableCellEditor cell=m_table.getCellEditor();
		if (cell!=null)
			cell.stopCellEditing();
		super.doSave();
		AccountingSQLSAP sql=new AccountingSQLSAP();
		GenericMapper mapper2=MasterMap.obtainMapperFor(ExpenseSheetDetail.class);
		mapper2.setActiveConn(m_conn);
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_EXPENSE_SHEET, m_conn);
				entity().setIndex(index);
			}
			if (entity().getEsProjectType()!=null){
				ExpenseSheetDetail temp[]=entity().getExpenseSheetDetail();
				mapper2.doDeleteByColumn(IDBConstants.ATTR_EXPENSE_SHEET,new Long(entity().getIndex()).toString());
				for (int i=0;i<temp.length;i++){
					temp[i].setExpenseSheet((ExpenseSheet)entity());
					mapper2.doInsert(temp[i]);  
				}
				LoadESDetail(entity().getIndex());
			}			
			if(!m_saveBtn.isEnabled())
				 setEnableButtonBawah(false);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	private void toBaseCurrency(){
		try{
			double amount,xcrate;
			if (!ESAmountText.getText().equalsIgnoreCase(""))
				amount=((Number)ESAmountText.getValue()).doubleValue();
			else
				amount=0;
			if (!ESExchRateText.getText().equalsIgnoreCase(""))
				xcrate=((Number)ESExchRateText.getValue()).doubleValue();
			else
				xcrate=0;			
			if (ESCurrComp.getObject()!=null){
				if (((Currency)ESCurrComp.getObject()).getSymbol().equals(baseCurrency.getSymbol()))
					ESAmountBaseText.setValue(new Double(amount));
				else
					ESAmountBaseText.setValue(new Double(amount*xcrate));
			}			
		}
		catch (Exception e){			
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
	
	public void clearAll(){
		super.clearAll();
		clearForm();
		clearKomponen();
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
		ESAmountBaseCurrText.setText("");
		CAAmountBaseCurrText.setText("");
		accountComp().setObject(null);
		CACurrComp.setObject(null);
		ESCurrComp.setObject(null);
		CashAdvanceExchRateText.setValue(null);
		ESExchRateText.setValue(null);
		CAAmountBaseText.setValue(null);
		CashAdvanceAmountText.setValue(null);
		ESAmountBaseText.setValue(null);
		StatusText.setText("");		
	}
	
	protected void clearForm() {
		ESAmountBaseText.setValue(null);
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
	
	protected class MyTableModelListener implements TableModelListener{
		public void tableChanged(TableModelEvent e) {
			int col = e.getColumn();
			Object obj = e.getSource();
			if(obj instanceof DefaultTableModel)
				if (col==3)				
					m_table.updateSummary();				
		}
	}
	
	class buttomTabbleESProject extends JTable {
		private static final long serialVersionUID = 1L;
		buttomTabbleESProjectModel model = new buttomTabbleESProjectModel();
		ArrayList listData=new ArrayList();
		public buttomTabbleESProject() {
			model.addColumn("No");
			model.addColumn("Expense Sheet");
			model.addColumn("Description");
			model.addColumn("Amount");			
			setModel(model);    
			getColumnModel().getColumn(0).setPreferredWidth(40);
			getColumnModel().getColumn(0).setMaxWidth(40);
			getModel().addTableModelListener(new MyTableModelListener());
		}
		
		public TableCellEditor getCellEditor(int row, int col) {
			if(col == 3) {
				return new FormattedDoubleCellEditor(JTextField.RIGHT);
			}else if(col==2){
				return new BaseTableCellEditor();
			}			
			return null;
		}
		
		public void updateSummary() {	
			double val = 0;
			for (int i = 0; i < getRowCount(); i++) {				
				if (getValueAt(i, 3) != null) {
					val += ((Double) getValueAt(i, 3)).doubleValue();					
				}
			}
			ESAmountText.setValue(new Double(val));
		}
		
		public void clearTable() {		
			int count = getRowCount();
			for(int i = 0; i < count; i ++){				
				model.removeRow(0);
			}
			listData.clear();
		}	
		
		public void addData(Vector obj,Account acc){		
			listData.add(acc);
			model.addRow(obj);			
			updateSummary();
		}
		
		public void removeData(int selectedRow) {		
			model.removeRow(selectedRow);
			listData.remove(selectedRow);
			this.setModel(model);
			for(int i=0;i<this.getRowCount();i++){
				this.setValueAt(new Integer(i+1), i, 0);
			} 
			updateSummary();
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
		
		public TableCellRenderer getCellRenderer(int row, int column) {
			if(column==3)
				return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
			return super.getCellRenderer(row, column);
		}
	}
	
		
	class buttomTabbleESProjectModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;		
		public boolean isCellEditable(int row, int col) {
			if(col == 0 || col==1)
				return false;
			return true;
		}
	}

	public void setEnableButtonBawah(boolean bool){		
		AddESProjectAccountBtn.setEnabled(bool);
		DeleteESProjectAccountBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}

	public void initTabel(){
		m_table=new buttomTabbleESProject();
		ESProjectAccountScrollPane.setViewportView(m_table);	
	}
	
	public void LoadESDetail(long a){
		GenericMapper mapper2=MasterMap.obtainMapperFor(ExpenseSheetDetail.class);
		mapper2.setActiveConn(m_conn);
		List rs=mapper2.doSelectWhere(IDBConstants.ATTR_EXPENSE_SHEET+"="+a);
		m_table.clearTable();
		ExpenseSheetDetail temp;
		Vector temp1;
		Account acc;
		double tempamount=0;		
		for(int i=0;i<rs.size();i++){
			temp1=new Vector();
			temp=(ExpenseSheetDetail)rs.get(i);
			acc=(Account)temp.getAccount();
			temp1.addElement(new Integer(i+1));
			temp1.addElement(acc.getName());
			temp1.addElement(temp.getDescription());
			try{
				temp1.addElement(new Double(temp.getaccValue()));
				tempamount=tempamount+temp.getaccValue();				
			}
			catch(Exception e){ 
				temp1.addElement(new Double(temp.getaccValue()));
			}
			ESAmountText.setValue(new Double(tempamount));
			m_table.addData(temp1, acc);
		}		
	}
	
	private void detailAccountOperation() {
		if (m_table.getDataCount()>=0){
			ExpenseSheetDetail[] temp=new ExpenseSheetDetail[m_table.getDataCount()];
			double temp2=0;
			for (int i=0;i<m_table.getDataCount();i++){
				temp[i]=new ExpenseSheetDetail();			
				temp[i].setAccount((Account)m_table.getListData().get(i));
				temp[i].setDescription(m_table.getValueAt(i,2).toString());
				try{
					Double temp1=(Double)m_table.getValueAt(i,3);
					temp[i].setaccValue(temp1.doubleValue());
					temp2=temp2+temp1.doubleValue();
				}
				catch (Exception e){
					temp[i].setaccValue(1);			
				}
				temp[i].setCurrency(entity().getCurrency());
				temp[i].setExchangeRate(entity().getExchangeRate());			
			}
			entity().setExpenseSheetDetail(temp);
			entity().setAmount(temp2);
		}
	}
	
	private LookupPicker AccountComp;
	private javax.swing.JLabel ActivityCodeLabel;
	private javax.swing.JTextField ActivityCodeText;
	private javax.swing.JButton AddESProjectAccountBtn;
	private javax.swing.JLabel AmountBaseCurrentLabel;
	private javax.swing.JLabel AmountBaseCurrentLabel2;
	private javax.swing.JLabel AmountLabel;
	private javax.swing.JLabel AmountLabel2;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel ButtomButtonPanel;
	private javax.swing.JTextField CAAmountBaseCurrText;
	private javax.swing.JFormattedTextField CAAmountBaseText;
	private javax.swing.JFormattedTextField CashAdvanceAmountText;
	private javax.swing.JTextField CashAdvanceCurrText;
	private javax.swing.JFormattedTextField CashAdvanceExchRateText;
	private javax.swing.JLabel CashAdvanceLabel;
	private javax.swing.JTextField ContractNoText;
	private javax.swing.JLabel CurrencyLabel;
	private javax.swing.JLabel CurrencyLabel2;
	private javax.swing.JLabel CustomerLabel;
	private javax.swing.JTextField CustomerText;	
	private javax.swing.JLabel EsProjectTypeLabel;
	private javax.swing.JButton DeleteESProjectAccountBtn;
	private javax.swing.JLabel DepartmentLabel;
	private javax.swing.JTextField DepartmentText;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScroll;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JTextField ESAmountBaseCurrText;
	private javax.swing.JFormattedTextField ESAmountBaseText;
	private javax.swing.JFormattedTextField ESAmountText;
	private javax.swing.JFormattedTextField ESExchRateText;
	private javax.swing.JScrollPane ESProjectAccountScrollPane;
	private buttomTabbleESProject m_table;    
	private javax.swing.JLabel ExchRateLabel;
	private javax.swing.JLabel ExchRateLabel2;
	private javax.swing.JLabel ExpSheetDateLabel;
	private javax.swing.JLabel ExpSheetNoLabel;
	private javax.swing.JPanel ExpenseSheetGUI;
	private javax.swing.JLabel IPCNoLabel;
	private javax.swing.JTextField IPCNoText;    
	private javax.swing.JLabel ORNoLabel;
	private javax.swing.JTextField ORNoText;
	private AssignPanel OriginatorComp;
	private javax.swing.JLabel PONoLabel;
	private javax.swing.JLabel ProjectCodeLabel;
	private javax.swing.JTextField ProjectCodeText;
	private AssignPanel ReceivedComp;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScroll;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JLabel StatusLabel12;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JTextField SubmittedDateText;
	private javax.swing.JPanel TopButtonPanel;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JTextField UnitCodeText;
	private javax.swing.JLabel VoucherDateLabel;
	private javax.swing.JTextField VoucherDateText;
	private javax.swing.JLabel WorkDescLabel;
	private javax.swing.JScrollPane WorkDescScroll;
	private javax.swing.JTextArea WorkDescTextArea;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel jPanel1_2;
	private javax.swing.JPanel jPanel1_2_2;
	private javax.swing.JPanel jPanel1_2_2_1;
	private javax.swing.JPanel jPanel1_2_1;
	private javax.swing.JPanel jPanel1_2_1_1;
	private javax.swing.JPanel jPanel1_2_1_2;
	private javax.swing.JPanel jPanel1_3;
	private javax.swing.JPanel jPanel1_3_2;
	private javax.swing.JPanel jPanel1_3_2_2;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel2_1;
	private LookupEspenseSheetPicker ProjectAccountComp;
	private javax.swing.JSeparator jSeparator2;
	private CurrencyPicker ESCurrComp,CACurrComp;   
}
