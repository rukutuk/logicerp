package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.cgui.report.ES_Others;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheetDetail;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheetLoader;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.accounting.entity.PmtCAOthers;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.logic.BeginningBalanceBusinessLogic;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.OrganizationCellEditor;

public class ExpenseOthersPanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener
,DocumentListener{
	private static final long serialVersionUID = 1L;
	
	private ExpenseSheet m_entity;
	public ExpenseSheetPanel guiParent;
	Employee defaultOriginator;
	Employee defaultApproved;
	Employee defaultReceived;
	private JRadioButton m_singleRb = new JRadioButton("Single");
	private JRadioButton m_multipleRb = new JRadioButton("Multiple");
	public ExpenseOthersPanel(Connection conn, long sessionid,ExpenseSheetPanel parent) {
		guiParent=parent;
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();   
		initTabel();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(conn, sessionid); 
		initExchangeRateHelper(conn, sessionid);
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
	private LookupDepartmentPicker DepartmentComp;
	private JLabel CashAdvanceLabel3;
	private void initComponents() {
		setAccountComp(new LookupESCashAdvanceOthers(m_conn,m_sessionid));
		ESOwnerComp = new LookupESOwner(m_conn,m_sessionid,LookupESOwner.OTHERS);
		DepartmentComp = new LookupDepartmentPicker(m_conn, m_sessionid);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Created by");        
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		CashAdvanceLabel3=new JLabel();
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
		leftLeftPanel = new javax.swing.JPanel();
		leftPanel = new javax.swing.JPanel();
		ExpSheetNoLabel1 = new javax.swing.JLabel();
		ExpSheetDateLabel1 = new javax.swing.JLabel();
		UnitCodeLabel1 = new javax.swing.JLabel();
		UnitCodeText = new javax.swing.JTextField();
		DescriptionLabel1 = new javax.swing.JLabel();
		RemarksLabel1 = new javax.swing.JLabel();
		DescriptionScroll1 = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksScroll1 = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		RefNoText = new javax.swing.JTextField();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		DepartmentLabel = new javax.swing.JLabel();
		//DepartmentText = new javax.swing.JTextField();
		jPanel1_2_1 = new javax.swing.JPanel();
		StatusLabel1 = new javax.swing.JLabel();
		SubmittedDateLabel1 = new javax.swing.JLabel();
		CashAdvanceLabel1 = new javax.swing.JLabel();
		CashAdvancePanel1 = new javax.swing.JPanel();
		StatusLabel13 = new javax.swing.JLabel();
		jSeparator3 = new javax.swing.JSeparator();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		VoucherDateText = new javax.swing.JTextField();
		jPanel1_2_2_1_3 = new javax.swing.JPanel();
		CurrencyLabel = new javax.swing.JLabel();
		AmountLabel = new javax.swing.JLabel();
		AmountBaseCurrentLabel = new javax.swing.JLabel();
		CACurrComp = new CurrencyPicker(m_conn,m_sessionid);
		ExchRateLabel = new javax.swing.JLabel();
		CashAdvanceExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		CashAdvanceAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		CAAmountBaseCurrText = new javax.swing.JFormattedTextField();
		CAAmountBaseText = new javax.swing.JFormattedTextField(m_numberFormatter);
		ExpenseSheet1 = new javax.swing.JPanel();
		StatusLabel14 = new javax.swing.JLabel();
		jSeparator4 = new javax.swing.JSeparator();
		jPanel1_2_2_1_4 = new javax.swing.JPanel();
		CurrencyLabel3 = new javax.swing.JLabel();
		AmountLabel3 = new javax.swing.JLabel();
		AmountBaseCurrentLabel3 = new javax.swing.JLabel();
		ESCurrComp = new CurrencyPicker(m_conn,m_sessionid);
		ExchRateLabel3 = new javax.swing.JLabel();
		ESExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		ESAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		ESAmountBaseCurrText = new javax.swing.JTextField();
		ESAmountBaseText = new javax.swing.JFormattedTextField(m_numberFormatter);
		CashAdvanceLabel2 = new javax.swing.JLabel();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel2_1 = new javax.swing.JPanel();
		ButtomButtonPanel = new javax.swing.JPanel();
		AddESOthersAccountBtn = new javax.swing.JButton();
		DeleteESOthersAccountBtn = new javax.swing.JButton();
		ESOthersAccountScrollPane = new javax.swing.JScrollPane();
		m_table = new buttomTabbleESOthers();
		
		setLayout(new java.awt.BorderLayout());
		
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		jPanel1.setPreferredSize(new java.awt.Dimension(725, 410));
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
		
		jPanel1.add(jPanel1_1, java.awt.BorderLayout.NORTH);
		
		jPanel1_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2.setPreferredSize(new java.awt.Dimension(880, 320));
		leftLeftPanel.setLayout(new java.awt.BorderLayout());
		
		leftLeftPanel.setPreferredSize(new java.awt.Dimension(415, 300));
		leftPanel.setLayout(new java.awt.GridBagLayout());
		
		leftPanel.setPreferredSize(new java.awt.Dimension(415, 200));
		ExpSheetNoLabel1.setText("Exp. Sheet No");
		ExpSheetNoLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(ExpSheetNoLabel1, gridBagConstraints);
		
		ExpSheetDateLabel1.setText("Exp. Sheet Date*");
		ExpSheetDateLabel1.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(ExpSheetDateLabel1, gridBagConstraints);
		
		UnitCodeLabel1.setText("Unit Code");
		UnitCodeLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(UnitCodeLabel1, gridBagConstraints);
		
		DepartmentLabel.setText("Department");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(DepartmentLabel, gridBagConstraints);
		
		DescriptionLabel1.setText("Description *");
		DescriptionLabel1.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(DescriptionLabel1, gridBagConstraints);
		
		RemarksLabel1.setText("Remarks");
		RemarksLabel1.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(RemarksLabel1, gridBagConstraints);
		
		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(RefNoText, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(TransactionDateDate, gridBagConstraints);
		
		UnitCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(UnitCodeText, gridBagConstraints);
		
		JPanel groupdeptPanel = new JPanel();
		groupdeptPanel.setPreferredSize(new java.awt.Dimension(290, 50));
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(m_singleRb);
		bg1.add(m_multipleRb);
		m_singleRb.setSelected(true);
		
		groupdeptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Department",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11)));
		groupdeptPanel.add(m_singleRb);
		groupdeptPanel.add(m_multipleRb);
		
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(groupdeptPanel, gridBagConstraints);
		
		//DepartmentText.setPreferredSize(new java.awt.Dimension(290, 18))
		DepartmentComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(DepartmentComp, gridBagConstraints);
		//leftPanel.add(DepartmentText, gridBagConstraints);
		
		DescriptionScroll1.setPreferredSize(new java.awt.Dimension(290, 50));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(3);
		DescriptionScroll1.setViewportView(DescriptionTextArea);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;	
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(DescriptionScroll1, gridBagConstraints);
		
		RemarksScroll1.setPreferredSize(new java.awt.Dimension(290, 50));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(3);
		RemarksScroll1.setViewportView(RemarksTextArea);		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		leftPanel.add(RemarksScroll1, gridBagConstraints);
		
		leftLeftPanel.add(leftPanel, java.awt.BorderLayout.WEST);
		
		jPanel1_2.add(leftLeftPanel, java.awt.BorderLayout.CENTER);
		
		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(440, 200));
		StatusLabel1.setText("Status");
		StatusLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(StatusLabel1, gridBagConstraints);
		
		SubmittedDateLabel1.setText("Submitted Date");
		SubmittedDateLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(SubmittedDateLabel1, gridBagConstraints);
		
		CashAdvanceLabel1.setText("Voucher Date");
		CashAdvanceLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(CashAdvanceLabel1, gridBagConstraints);
		
		CashAdvancePanel1.setLayout(new java.awt.GridBagLayout());
		
		CashAdvancePanel1.setPreferredSize(new java.awt.Dimension(410, 15));
		StatusLabel13.setText("Cash Advance");
		StatusLabel13.setPreferredSize(new java.awt.Dimension(75, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		CashAdvancePanel1.add(StatusLabel13, gridBagConstraints);
		
		jSeparator3.setPreferredSize(new java.awt.Dimension(200, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		CashAdvancePanel1.add(jSeparator3, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(CashAdvancePanel1, gridBagConstraints);
		
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
		
		VoucherDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(VoucherDateText, gridBagConstraints);
		
		jPanel1_2_2_1_3.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_2_1_3.setPreferredSize(new java.awt.Dimension(410, 69));
		CurrencyLabel.setText("Currency");
		CurrencyLabel.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_2_1_3.add(CurrencyLabel, gridBagConstraints);
		
		AmountLabel.setText("Amount");
		AmountLabel.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_2_1_3.add(AmountLabel, gridBagConstraints);
		
		AmountBaseCurrentLabel.setText("Amount Base Curr.");
		AmountBaseCurrentLabel.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_2_1_3.add(AmountBaseCurrentLabel, gridBagConstraints);
		
		CACurrComp.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_2_1_3.add(CACurrComp, gridBagConstraints);
		
		ExchRateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		ExchRateLabel.setText("Exch. Rate");
		ExchRateLabel.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_2_1_3.add(ExchRateLabel, gridBagConstraints);
		
		CashAdvanceExchRateText.setPreferredSize(new java.awt.Dimension(87, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		jPanel1_2_2_1_3.add(CashAdvanceExchRateText, gridBagConstraints);
		
		CashAdvanceAmountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_2_1_3.add(CashAdvanceAmountText, gridBagConstraints);
		
		CAAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1_3.add(CAAmountBaseCurrText, gridBagConstraints);
		
		CAAmountBaseText.setPreferredSize(new java.awt.Dimension(147, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		jPanel1_2_2_1_3.add(CAAmountBaseText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(jPanel1_2_2_1_3, gridBagConstraints);
		
		ExpenseSheet1.setLayout(new java.awt.GridBagLayout());
		
		ExpenseSheet1.setPreferredSize(new java.awt.Dimension(410, 15));
		StatusLabel14.setText("Expense Sheet");
		StatusLabel14.setPreferredSize(new java.awt.Dimension(75, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		ExpenseSheet1.add(StatusLabel14, gridBagConstraints);
		
		jSeparator4.setPreferredSize(new java.awt.Dimension(200, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		ExpenseSheet1.add(jSeparator4, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(ExpenseSheet1, gridBagConstraints);
		
		jPanel1_2_2_1_4.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_2_1_4.setPreferredSize(new java.awt.Dimension(410, 65));
		CurrencyLabel3.setText("Currency*");
		CurrencyLabel3.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_2_1_4.add(CurrencyLabel3, gridBagConstraints);
		
		AmountLabel3.setText("Amount");
		AmountLabel3.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_2_1_4.add(AmountLabel3, gridBagConstraints);
		
		AmountBaseCurrentLabel3.setText("Amount Base Curr.");
		AmountBaseCurrentLabel3.setPreferredSize(new java.awt.Dimension(105, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
		jPanel1_2_2_1_4.add(AmountBaseCurrentLabel3, gridBagConstraints);
		
		ESCurrComp.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
		jPanel1_2_2_1_4.add(ESCurrComp, gridBagConstraints);
		
		ExchRateLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		ExchRateLabel3.setText("Exch. Rate*");
		ExchRateLabel3.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_2_1_4.add(ExchRateLabel3, gridBagConstraints);
		
		ESExchRateText.setPreferredSize(new java.awt.Dimension(90, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		jPanel1_2_2_1_4.add(ESExchRateText, gridBagConstraints);
		
		ESAmountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
		jPanel1_2_2_1_4.add(ESAmountText, gridBagConstraints);
		
		ESAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
		jPanel1_2_2_1_4.add(ESAmountBaseCurrText, gridBagConstraints);
		
		ESAmountBaseText.setPreferredSize(new java.awt.Dimension(147, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
		jPanel1_2_2_1_4.add(ESAmountBaseText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(jPanel1_2_2_1_4, gridBagConstraints);
		
		CashAdvanceLabel3.setText("ES Owner*");
		CashAdvanceLabel3.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(CashAdvanceLabel3, gridBagConstraints);
		
		ESOwnerComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ESOwnerComp, gridBagConstraints);
		
		CashAdvanceLabel2.setText("Cash Advance*");
		CashAdvanceLabel2.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(CashAdvanceLabel2, gridBagConstraints);
		
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);
		
		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1.add(jPanel1_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.setLayout(new java.awt.BorderLayout());
		
		jPanel1_3.setPreferredSize(new java.awt.Dimension(650, 100));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());
		
		OriginatorComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(284, 110));
		jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.setLayout(new java.awt.BorderLayout());
		
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		
		ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(284, 110));
		jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());
		
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		
		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(284, 110));
		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);
		
		add(jPanel1, java.awt.BorderLayout.NORTH);
		
		jPanel2.setLayout(new java.awt.BorderLayout());
		
		jPanel2_1.setLayout(new java.awt.BorderLayout());
		
		ButtomButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));
		
		ButtomButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
		AddESOthersAccountBtn.setText("Add");
		AddESOthersAccountBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		AddESOthersAccountBtn.setPreferredSize(new java.awt.Dimension(50, 21));
		ButtomButtonPanel.add(AddESOthersAccountBtn);
		
		DeleteESOthersAccountBtn.setText("Delete");
		DeleteESOthersAccountBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		DeleteESOthersAccountBtn.setPreferredSize(new java.awt.Dimension(50, 21));
		ButtomButtonPanel.add(DeleteESOthersAccountBtn);	
		
		jPanel2_1.add(ButtomButtonPanel, java.awt.BorderLayout.WEST);
		
		jPanel2.add(jPanel2_1, java.awt.BorderLayout.NORTH);
		
		ESOthersAccountScrollPane.setPreferredSize(new java.awt.Dimension(650, 215));
		m_table.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {},
				new String [] {"Expense Type", "Description", "Amount"}
		));
		ESOthersAccountScrollPane.setViewportView(m_table);
		
		jPanel2.add(ESOthersAccountScrollPane, java.awt.BorderLayout.CENTER);
		
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
		ESAmountText.getDocument().addDocumentListener(this);
		TransactionDateDate.addPropertyChangeListener(this);	
		ESOwnerComp.hapusActionListenerBrowse();
		ESOwnerComp.m_browseBt.addActionListener(this);
		AccountComp.hapusActionListenerBrowse();
		AccountComp.m_browseBt.addActionListener(this);
		AddESOthersAccountBtn.addActionListener(this);
		DeleteESOthersAccountBtn.addActionListener(this);
		TransactionDateDate.addPropertyChangeListener("date", this);
		DepartmentComp.addPropertyChangeListener(this);
		m_singleRb.addActionListener(this);
		m_multipleRb.addActionListener(this);
	}
	
	protected void doNew() {
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
		setEnableButtonBawah(true);
		validateEsOwner();
		validateCashAdvance(null);
	}	
	
	public void validateEsOwner(){
		jPanel1_2_1.remove(ESOwnerComp);
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();			
		ESOwnerComp = new LookupESOwner(m_conn,m_sessionid,LookupESOwner.OTHERS);		
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
	
	protected void doDelete() {	
		super.doDelete();
		if (entity().getStatus()==StateTemplateEntity.State.NEW){ 	
			clearKomponen();
			clearForm();
			setEnableButtonBawah(false);			
		}	
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
	
	public void actionPerformed(ActionEvent e){		
		if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new ES_Others(m_entity,m_conn);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}	
		}
		else if (e.getSource()==m_singleRb){
			DepartmentComp.setEnabled(true);
			int maxRow = m_table.getRowCount() ;
			for (int i=0;i<maxRow;i++){
				if (DepartmentComp.getObject() instanceof Organization) {
					Organization org = (Organization) DepartmentComp.getObject();
					m_table.setValueAt(org,i,4);					
				}else
					m_table.setValueAt("",i,4);
			}
		}
		else if (e.getSource()==m_multipleRb){
			DepartmentComp.setEnabled(false);
			DepartmentComp.setObject(null);
			int maxRow = m_table.getRowCount();
			for (int i=0;i<maxRow;i++){
				m_table.setValueAt("",i,4);
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			String kriteria = "("+IDBConstants.ATTR_PMT_CA_IOU_OTHERS_SETTLED+" IS NOT NULL  OR "+
			IDBConstants.ATTR_PMT_CA_OTHERS+ " IS NOT NULL OR "+
			IDBConstants.ATTR_BEGINNING_BALANCE+" IS NOT NULL) " +
			"ORDER BY " + IDBConstants.ATTR_TRANSACTION_DATE + " ASC," + IDBConstants.ATTR_REFERENCE_NO + " ASC";
			SearchExpenseSheetDialog dlg = new SearchExpenseSheetDialog(GumundaMainFrame.getMainFrame(), "Search Expense Sheet", 
					m_conn, m_sessionid,new ExpenseSheetLoader(m_conn),IConstants.EXPENSE_SHEET_OTHERS,"" +kriteria);		
			dlg.setVisible(true);
			if (dlg.selectedObj != null){
				doLoad(dlg.selectedObj);
			}
		}
		else if (e.getSource()==ESOwnerComp.m_browseBt){
			ESOwnerComp.done();
			if(ESOwnerComp.getObject()!=null){
				Employee temp=(Employee)ESOwnerComp.getObject();
				validateCashAdvance(temp);
				
			}
		}
		
		else if (e.getSource()==AccountComp.m_browseBt){
			if(ESOwnerComp.getObject()!=null){
				AccountComp.done();
			}
			else
				JOptionPane.showMessageDialog(this,"Please select employee on the left list");
		}
		else if(e.getSource()==AddESOthersAccountBtn){
			ESothersComp=new LookupEspenseSheetPicker(m_conn, m_sessionid,IConstants.EXPENSE_SHEET_OTHERS);
			ESothersComp.done();
			if(ESothersComp.getObject()!=null){   
				Vector temp1=new Vector();
				Account acc=(Account)ESothersComp.getObject();
				temp1.addElement(new Integer(m_table.getRowCount()+1));
				temp1.addElement(acc.getName());
				temp1.addElement("");
				temp1.addElement(new Double(0));
				temp1.addElement(DepartmentComp.getObject());
				m_table.addData(temp1, acc);
			}
		}
		else if (e.getSource()==DeleteESOthersAccountBtn){		
			if (m_table.getRowCount()>0){
				if (m_table.getSelectedRow()>=0)				
					m_table.removeData(m_table.getSelectedRow());
			}
		}
	}
	
	private void validateCashAdvance(Employee temp) {
		jPanel1_2_1.remove(accountComp());
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();			
		setAccountComp(new LookupESCashAdvanceOthers(m_conn, m_sessionid,temp));  
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
	
	private void setGuiEmpty(){
		CACurrComp.setObject(null);
		CashAdvanceAmountText.setValue(null);
		ESCurrComp.setObject(null);		
		CashAdvanceExchRateText.setValue(null);
		CAAmountBaseCurrText.setText(null);	
		CAAmountBaseText.setValue(null);
		ESExchRateText.setValue(null);
		ESAmountBaseCurrText.setText(null);
		ESAmountBaseText.setText(null);
		UnitCodeText.setText(null);
		//DepartmentText.setText(null);
	}
	
	private void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.EXPENSE_SHEET_OTHERS, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.EXPENSE_SHEET_OTHERS, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.EXPENSE_SHEET_OTHERS, Signature.SIGN_CREATED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	
	ExpenseSheet entity() {
		return m_entity;
	}
	
	protected StateTemplateEntity currentEntity() {		
		return entity();
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity() {		
		System.err.println(DescriptionTextArea.getText());
		if (AccountComp.getObject()==null)
			addInvalid("CashAdvanceCompe must be selected");
		if(ESExchRateText.getText().equals(""))
			addInvalid("Exchange Rate must be filled");
		if(DescriptionTextArea.getText().equals(""))
			addInvalid("Description must be filled");
		if (cekdepartmentdetail())
			validityMsgs.add("Department detail is still empty");
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
	
	public boolean cekdepartmentdetail(){
		boolean cek = false;
		int maxRow = m_table.getRowCount();
		for (int i=0;i<maxRow;i++){
			if (m_table.getValueAt(i,4)==null)				
				cek =true;
			else if (m_table.getValueAt(i,4).equals(""))
				cek=true;
		}
		return cek;
	}
	
	
	private void addInvalid(String string) {
		validityMsgs.add(string);
	}
	
	protected boolean cekValidity2() {
		validityMsgs.clear();
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
			kolom=IDBConstants.ATTR_BEGINNING_BALANCE;
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
	
	protected void gui2entity() {		
		Object objk = AccountComp.getObject();
		if (objk instanceof PmtCAIOUOthersSettled) {
			PmtCAIOUOthersSettled a = (PmtCAIOUOthersSettled) objk;
			entity().setPmtCaIouOthersSettled(a);
			entity().setEsProjectType("IOU");
			entity().setProject(null);
			entity().setPmtCaProject(null);
			entity().setBeginningBalance(null);		
		}else if (objk instanceof PmtCAOthers) {
			PmtCAOthers a = (PmtCAOthers) objk;					
			entity().setPmtCaOthers(a);
			entity().setEsProjectType("General");
			entity().setPmtCaIouProjectSettled(null);
			entity().setProject(null);
			entity().setBeginningBalance(null);		
		}else if (objk instanceof BeginningCashAdvance) {
			BeginningCashAdvance a = (BeginningCashAdvance) objk;
			entity().setBeginningBalance(a);
			entity().setEsProjectType("");
			entity().setPmtCaProject(null);
			entity().setPmtCaIouOthersSettled(null);
			entity().setProject(null);		
		}
		
		Object objDept = DepartmentComp.getObject();
		if (objDept instanceof Organization) {
			Organization dept = (Organization) objDept;
			entity().setDepartment(dept);			
		}else
			entity().setDepartment(null);
		if (m_singleRb.isSelected())
			entity().setDepartmentgroup(0);
		else if (m_multipleRb.isSelected())
			entity().setDepartmentgroup(1);
		
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
	
	protected void entity2gui() {
		UnitCodeText.setText("");
		//DepartmentText.setText("");  
		
		ESOwnerComp.setObject(entity().getEsOwner());
		if (entity().getEsOwner()!=null)
			validateCashAdvance(entity().getEsOwner());
		if (entity().getPmtCaIouOthersSettled()!=null)
			AccountComp.setObject(entity().getPmtCaIouOthersSettled());
		else if (entity().getPmtCaOthers()!=null)    		
			AccountComp.setObject(entity().getPmtCaOthers());
		else if (entity().getBeginningBalance()!=null)
			AccountComp.setObject(entity().getBeginningBalance());		
		else
			accountComp().setObject(null);
		if (entity().getDepartment()!=null)
			DepartmentComp.setObject(entity().getDepartment());
		else
			DepartmentComp.setObject(null);
		
		RefNoText.setText(entity().getReferenceNo());
		if(entity().getTransactionDate()!=null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else{
			TransactionDateDate.setDate(new Date());
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
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");
		LoadESDetail(entity().getIndex());
		
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
		this.CashAdvanceExchRateText.setEditable(false);
		this.CAAmountBaseText.setEditable(false);
		this.CAAmountBaseCurrText.setEditable(false);
		this.ESAmountText.setEditable(false);
		this.ESAmountBaseCurrText.setEditable(false);
		this.ESAmountBaseText.setEditable(false);
		this.VoucherDateText.setEditable(false);
		this.UnitCodeText.setEditable(false);
		//this.DepartmentText.setEditable(false);
		this.DepartmentComp.setEnabled(false);
		this.RefNoText.setEditable(false);
		this.ESCurrComp.setEnabled(false);
		this.CACurrComp.setEnabled(false);
		this.ESExchRateText.setEditable(false);
		this.ESAmountBaseText.setEditable(false);
		this.ESAmountText.setEditable(false);
		CashAdvanceAmountText.setEditable(false);
		m_singleRb.setEnabled(false);
		m_multipleRb.setEnabled(false);
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
		this.DepartmentComp.setEnabled(true);
		this.ESAmountBaseCurrText.setText(baseCurrency.getSymbol());
		this.CAAmountBaseCurrText.setText(baseCurrency.getSymbol());
		
		this.ESCurrComp.setEnabled(false);
		this.CACurrComp.setEnabled(false);
		this.ESExchRateText.setEditable(false);
		this.ESAmountBaseText.setEditable(false);
		this.ESAmountText.setEditable(false);
		
		CashAdvanceAmountText.setEditable(false);
		m_singleRb.setEnabled(true);
		m_multipleRb.setEnabled(true);
	}
	
	protected Object createNew() {
		ExpenseSheet a  = new ExpenseSheet();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
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
		if ("date".equals(evt.getPropertyName())) {
			if (evt.getSource() == TransactionDateDate) {
				Currency curr = (Currency) ESCurrComp.getObject();
				setDefaultExchangeRate(curr);
			}
		}
		if (evt.getSource() == DepartmentComp){
			if (m_singleRb.isSelected()){
				int maxRow = m_table.getRowCount() ;
				for (int i=0;i<maxRow;i++){
					if (DepartmentComp.getObject() instanceof Organization) {
						Organization org = (Organization) DepartmentComp.getObject();
						m_table.setValueAt(org,i,4);
					}
				}
			}
		}
		if ("object".equals(evt.getPropertyName())) {
			if (evt.getSource() == accountComp()) {
				if (ESOwnerComp.getObject() != null) {
					Object obj = accountComp().getObject();
					if (obj == null) {
						CACurrComp.setObject(null);
						CashAdvanceAmountText.setValue(null);
						ESCurrComp.setObject(null);
						CashAdvanceExchRateText.setValue(null);
						CAAmountBaseCurrText.setText(null);
						CAAmountBaseText.setValue(null);
					}
					
					else if (obj instanceof PmtCAIOUOthersSettled) {
						PmtCAIOUOthersSettled a = (PmtCAIOUOthersSettled) obj;
						CACurrComp.setObject(a.getCurrency());
						CashAdvanceAmountText
						.setValue(new Double(a.getAmount()));
						if (a.getExchangeRate() != 0)
							CashAdvanceExchRateText.setValue(new Double(a
									.getExchangeRate()));
						else
							CashAdvanceExchRateText.setValue(new Double(1));
						CAAmountBaseCurrText.setText(baseCurrency.getSymbol());
						
						double value = a.getAmount()
						* ((Double) CashAdvanceExchRateText.getValue())
						.doubleValue();
						CAAmountBaseText.setValue(new Double(value));
						
						ESCurrComp.setObject(a.getCurrency());
						if (a.getCurrency().getSymbol().equalsIgnoreCase(
								baseCurrency.getSymbol())){
							ESExchRateText.setValue(new Double(1));
							ESExchRateText.setEditable(false);
						}
						else {
							ESExchRateText.setEditable(true);
							setDefaultExchangeRate(a.getCurrency());
						}
						
						ESAmountBaseCurrText.setText(baseCurrency.getSymbol());
						
						Number val = (Number) ESAmountText.getValue();
						value = 0;
						if (val != null)
							value = val.doubleValue();
						
						if (ESExchRateText.getValue() != null) {
							value = value
							* ((Double) ESExchRateText.getValue())
							.doubleValue();
							ESAmountBaseText.setValue(new Double(value));
						}
						
						if (a.getPmtcaiouothers() != null) {
							UnitCodeText.setText(a.getPmtcaiouothers()
									.getUnit().toString());
							if (entity().getDepartment()==null){
								if (a.getPmtcaiouothers().getDepartment() != null)
									DepartmentComp.setObject(a.getPmtcaiouothers().getDepartment());
								//DepartmentText.setText(a.getPmtcaiouothers().getDepartment().toString());
								else
									DepartmentComp.setObject(null);
								//DepartmentText.setText("");
							}
						} else {
							UnitCodeText.setText("");
							if (entity().getDepartment()==null)
								DepartmentComp.setObject(null);
							//DepartmentText.setText("");
						}
					}
					
					else if (obj instanceof PmtCAOthers) {
						PmtCAOthers a = (PmtCAOthers) obj;
						CACurrComp.setObject(a.getCurrency());
						CashAdvanceAmountText
						.setValue(new Double(a.getAmount()));
						if (a.getExchangeRate() != 0)
							CashAdvanceExchRateText.setValue(new Double(a
									.getExchangeRate()));
						else
							CashAdvanceExchRateText.setValue(new Double(1));
						CAAmountBaseCurrText.setText(baseCurrency.getSymbol());
						double value = a.getAmount()
						* ((Double) CashAdvanceExchRateText.getValue())
						.doubleValue();
						CAAmountBaseText.setValue(new Double(value));
						UnitCodeText.setText(a.getUnit().toString());
						if (a.getDepartment() != null)
							DepartmentComp.setObject(a.getDepartment());
						//DepartmentText.setText(a.getDepartment().toString());
						else
							//DepartmentText.setText("");
							DepartmentComp.setObject(null);
						
						ESCurrComp.setObject(a.getCurrency());
						if (a.getCurrency().getSymbol().equalsIgnoreCase(
								baseCurrency.getSymbol())){
							ESExchRateText.setValue(new Double(1));
							ESExchRateText.setEditable(false);
						}
						else {
							ESExchRateText.setEditable(true);
							setDefaultExchangeRate(a.getCurrency());
						}
						
						ESAmountBaseCurrText.setText(baseCurrency.getSymbol());
						
						Number val = (Number) ESAmountText.getValue();
						value = 0;
						if (val != null)
							value = val.doubleValue();
						
						if (ESExchRateText.getValue() != null) {
							value = value
							* ((Double) ESExchRateText.getValue())
							.doubleValue();
							ESAmountBaseText.setValue(new Double(value));
						}
						
					}
					
					else if (obj instanceof BeginningCashAdvance) {
						BeginningCashAdvance a = (BeginningCashAdvance) obj;
						CACurrComp.setObject(a.getCurrency());
						CashAdvanceAmountText.setValue(new Double(a
								.getAccValue()));
						if (a.getExchangeRate() != 0)
							CashAdvanceExchRateText.setValue(new Double(a
									.getExchangeRate()));
						else
							CashAdvanceExchRateText.setValue(new Double(1));
						CAAmountBaseCurrText.setText(baseCurrency.getSymbol());
						double value = a.getAccValue()
						* ((Number) CashAdvanceExchRateText.getValue())
						.doubleValue();
						CAAmountBaseText.setValue(new Double(value));
						
						// --->
						UnitCodeText.setText(a.getTrans().getUnit().toString());
						
						ESCurrComp.setObject(a.getCurrency());
						if (a.getCurrency().getSymbol().equalsIgnoreCase(
								baseCurrency.getSymbol())) {
							ESExchRateText.setValue(new Double(1));
							ESExchRateText.setEditable(false);
						}
						else {
							ESExchRateText.setEditable(true);
							setDefaultExchangeRate(a.getCurrency());
						}
						
						ESAmountBaseCurrText.setText(baseCurrency.getSymbol());
						
						Number val = (Number) ESAmountText.getValue();
						value = 0;
						if (val != null)
							value = val.doubleValue();
						
						if (ESExchRateText.getValue() != null) {
							value = value
							* ((Double) ESExchRateText.getValue())
							.doubleValue();
							ESAmountBaseText.setValue(new Double(value));
						}
					}
					
				}
				// end if instance of			
			}// end if evt.getsource
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
		CashAdvanceExchRateText.setValue(null);
		ESExchRateText.setValue(null);
		CAAmountBaseText.setValue(null);
		ESAmountBaseText.setValue(null);		
	}
	
	protected void clearForm() {
		clearTextField(leftPanel);
		clearTextField(leftLeftPanel);
		clearTextField(jPanel1_2_1);
		clearTextField(jPanel1_2_1);
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
	
	class buttomTabbleESOthers extends JTable {
		
		private static final long serialVersionUID = 1L;
		buttomTabbleESOthersModel model = new buttomTabbleESOthersModel();
		ArrayList listData=new ArrayList();
		
		public buttomTabbleESOthers() {		
			model.addColumn("No");
			model.addColumn("Expense Sheet");
			model.addColumn("Description");
			model.addColumn("Amount");
			model.addColumn("Department");
			setModel(model);    
			getColumnModel().getColumn(0).setPreferredWidth(40);
			getColumnModel().getColumn(0).setMaxWidth(40);
			getModel().addTableModelListener(new MyTableModelListener());			
		}
		
		public TableCellEditor getCellEditor(int row, int col) {
			if(col == 3) 
				return new FormattedDoubleCellEditor(JTextField.RIGHT);
			else if(col==2)
				return new BaseTableCellEditor();
			else if(col==4 && m_multipleRb.isSelected())
				return new OrganizationCellEditor(GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
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
			for(int i = 0; i < count; i ++)				
				model.removeRow(0);			
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
			for(int i=0;i<this.getRowCount();i++)
				this.setValueAt(new Integer(i+1), i, 0);
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
	
	class buttomTabbleESOthersModel extends DefaultTableModel {		
		private static final long serialVersionUID = 1L;		
		public boolean isCellEditable(int row, int col) {
			System.out.println("Milih Colom ke:"+col);
			if(col == 0 || col==1)
				return false;
			return true;
		}
	}
	
	public void setEnableButtonBawah(boolean bool){		
		AddESOthersAccountBtn.setEnabled(bool);
		DeleteESOthersAccountBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}
	
	public TableCellRenderer FormattedDoubleCellRenderer(int right, int plain) {	
		return null;
	}
	
	public void initTabel(){
		m_table=new buttomTabbleESOthers();
		ESOthersAccountScrollPane.setViewportView(m_table);	
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
			if (temp.getDepartment()!=null)
				temp1.addElement(temp.getDepartment());
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
					Number temp1=(Double)m_table.getValueAt(i,3);
					temp[i].setaccValue(temp1.doubleValue());
					temp2=temp2+temp1.doubleValue();
				}
				catch (Exception e){
					temp[i].setaccValue(1);			
				}
				temp[i].setCurrency(entity().getCurrency());
				temp[i].setExchangeRate(entity().getExchangeRate());
				Object objdept = m_table.getValueAt(i,4);
				if (objdept instanceof Organization) {
					Organization org = (Organization) objdept;
					temp[i].setDepartment(org);
				}else{
					temp[i].setDepartment(null);
				}
			}
			entity().setExpenseSheetDetail(temp);
			entity().setAmount(temp2);
		}
	}
	
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
	}
	
	protected void doSave() {
		if (!cekValidity2())
			return;
		if (!cekValidity())
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
	
	private LookupPicker AccountComp;
	private javax.swing.JButton AddESOthersAccountBtn;
	private javax.swing.JLabel AmountBaseCurrentLabel;
	private javax.swing.JLabel AmountBaseCurrentLabel3;
	private javax.swing.JLabel AmountLabel;
	private javax.swing.JLabel AmountLabel3;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel ButtomButtonPanel;
	private javax.swing.JFormattedTextField CAAmountBaseCurrText;
	private javax.swing.JFormattedTextField CAAmountBaseText;
	private javax.swing.JFormattedTextField CashAdvanceAmountText;
	private CurrencyPicker CACurrComp;
	private javax.swing.JFormattedTextField CashAdvanceExchRateText;
	private javax.swing.JLabel CashAdvanceLabel1;
	private javax.swing.JLabel CashAdvanceLabel2;
	private javax.swing.JPanel CashAdvancePanel1;
	private javax.swing.JLabel CurrencyLabel;
	private javax.swing.JLabel CurrencyLabel3;
	private javax.swing.JButton DeleteESOthersAccountBtn;
	private javax.swing.JLabel DepartmentLabel;
	//private javax.swing.JTextField DepartmentText;
	private javax.swing.JLabel DescriptionLabel1;
	private javax.swing.JScrollPane DescriptionScroll1;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JTextField ESAmountBaseCurrText;
	private javax.swing.JFormattedTextField ESAmountBaseText;
	private javax.swing.JFormattedTextField ESAmountText;
	private CurrencyPicker ESCurrComp;
	private javax.swing.JFormattedTextField ESExchRateText;
	private javax.swing.JScrollPane ESOthersAccountScrollPane;
	private buttomTabbleESOthers m_table;
	private javax.swing.JLabel ExchRateLabel;
	private javax.swing.JLabel ExchRateLabel3;
	private javax.swing.JLabel ExpSheetDateLabel1;
	private javax.swing.JLabel ExpSheetNoLabel1;
	private javax.swing.JPanel ExpenseSheet1;
	private AssignPanel OriginatorComp;
	private AssignPanel ReceivedComp;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel1;
	private javax.swing.JScrollPane RemarksScroll1;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel StatusLabel1;
	private javax.swing.JLabel StatusLabel13;
	private javax.swing.JLabel StatusLabel14;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel1;
	private javax.swing.JTextField SubmittedDateText;
	private javax.swing.JPanel TopButtonPanel;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JLabel UnitCodeLabel1;
	private javax.swing.JTextField UnitCodeText;
	private javax.swing.JTextField VoucherDateText;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel jPanel1_2;
	private javax.swing.JPanel jPanel1_2_1;
	private javax.swing.JPanel leftLeftPanel;
	private javax.swing.JPanel leftPanel;
	private javax.swing.JPanel jPanel1_2_2_1_3;
	private javax.swing.JPanel jPanel1_2_2_1_4;
	private javax.swing.JPanel jPanel1_3;
	private javax.swing.JPanel jPanel1_3_2;
	private javax.swing.JPanel jPanel1_3_2_2;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel2_1;
	private javax.swing.JSeparator jSeparator3;
	private javax.swing.JSeparator jSeparator4;    
	private LookupEspenseSheetPicker ESothersComp;	
}
