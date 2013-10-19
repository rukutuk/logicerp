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
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.Vchr_EmpReceiv;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtEmpReceivable;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.UnitBankCashTransferLoader;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;

public class PaymentEmployeeReceivablePanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	private PmtEmpReceivable m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	public PaymentEmployeeReceivablePanel(Connection conn, long sessionid) {
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
		setEntity(new PmtEmpReceivable());
		m_entityMapper = MasterMap.obtainMapperFor(PmtEmpReceivable.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}

	private void initComponents() {
		AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid);
		PayToComp = new EmployeePicker(m_conn, m_sessionid);
		CurrComp = new CurrencyPicker(m_conn, m_sessionid);
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
		PayToLabel = new javax.swing.JLabel();
		EmpReceivableDetailComp = new javax.swing.JButton();
		PaymentSourceLabel = new javax.swing.JLabel();
		AccountLabel = new javax.swing.JLabel();
		ChequeNoLabel = new javax.swing.JLabel();
		ChequeDueDateLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		PaymentSourceCombo = new javax.swing.JComboBox();
		ChequeNoText = new javax.swing.JTextField();
		ChequeDueDateDate = new DatePicker();
		jPanel1_2_1_1 = new javax.swing.JPanel();
		ExpSheetDiffLabel4 = new javax.swing.JLabel();
		jSeparator6 = new javax.swing.JSeparator();
		CurrencyLabel4 = new javax.swing.JLabel();
		ExchRateLabel4 = new javax.swing.JLabel();
		ExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountLabel4 = new javax.swing.JLabel();
		AmountText=new JFormattedTextField(m_numberFormatter);
		AmountBaseCurrLabel4 = new javax.swing.JLabel();
		AmountBaseText = new JFormattedTextField(m_numberFormatter);;
		//AmountBaseCurrText = new JFormattedTextField(m_numberFormatter);
		AmountBaseCurrText =  new JTextField();
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_2_1 = new javax.swing.JPanel();
		VoucherNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		VoucherDateLabel = new javax.swing.JLabel();
		JobTittleLabel = new javax.swing.JLabel();
		UnitCodeLabel = new javax.swing.JLabel();
		DepartmentLabel = new javax.swing.JLabel();
		DescriptionLabel = new javax.swing.JLabel();
		RemarksLabel = new javax.swing.JLabel();
		JobTitleText = new javax.swing.JTextField();
		UnitCodeText = new javax.swing.JTextField();
		DepartmentText = new javax.swing.JTextField();
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();

		setLayout(new java.awt.BorderLayout());

		setPreferredSize(new java.awt.Dimension(650, 430));
		jPanel1.setLayout(new java.awt.BorderLayout());

		jPanel1.setPreferredSize(new java.awt.Dimension(700, 390));
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

		jPanel1_2.setPreferredSize(new java.awt.Dimension(880, 380));
		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(430, 400));
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

		PayToLabel.setText("Pay To*");
		PayToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PayToLabel, gridBagConstraints);

		EmpReceivableDetailComp.setText("Employee Receivable Detail");
		EmpReceivableDetailComp.setPreferredSize(new java.awt.Dimension(290, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 3);
		jPanel1_2_1.add(EmpReceivableDetailComp, gridBagConstraints);

		PaymentSourceLabel.setText("Payment Source*");
		PaymentSourceLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PaymentSourceLabel, gridBagConstraints);

		AccountLabel.setText("Account");
		AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountLabel, gridBagConstraints);

		ChequeNoLabel.setText("Cheque No");
		ChequeNoLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeNoLabel, gridBagConstraints);

		ChequeDueDateLabel.setText("Cheque Due Date");
		ChequeDueDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeDueDateLabel, gridBagConstraints);

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

		PayToComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PayToComp, gridBagConstraints);

		PaymentSourceCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash","Bank" }));
		PaymentSourceCombo.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PaymentSourceCombo, gridBagConstraints);

		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);

		ChequeNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeNoText, gridBagConstraints);

		ChequeDueDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeDueDateDate, gridBagConstraints);

		jPanel1_2_1_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(407, 80));
		ExpSheetDiffLabel4.setText("Emp. Receivable Pmt");
		ExpSheetDiffLabel4.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ExpSheetDiffLabel4, gridBagConstraints);

		jSeparator6.setPreferredSize(new java.awt.Dimension(200, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(jSeparator6, gridBagConstraints);

		CurrencyLabel4.setText("Currency*");
		CurrencyLabel4.setPreferredSize(new java.awt.Dimension(103, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(CurrencyLabel4, gridBagConstraints);

		CurrComp.setPreferredSize(new java.awt.Dimension(80, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(CurrComp, gridBagConstraints);

		ExchRateLabel4.setText("Exch. Rate*");
		ExchRateLabel4.setPreferredSize(new java.awt.Dimension(60, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ExchRateLabel4, gridBagConstraints);

		ExchRateText.setPreferredSize(new java.awt.Dimension(137, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(ExchRateText, gridBagConstraints);

		AmountLabel4.setText("Amount*");
		AmountLabel4.setPreferredSize(new java.awt.Dimension(103, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountLabel4, gridBagConstraints);

		AmountText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(AmountText, gridBagConstraints);

		AmountBaseCurrLabel4.setText("Amount Base Curr.");
		AmountBaseCurrLabel4.setPreferredSize(new java.awt.Dimension(103, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountBaseCurrLabel4, gridBagConstraints);

		AmountBaseText.setPreferredSize(new java.awt.Dimension(201, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(AmountBaseText, gridBagConstraints);

		AmountBaseCurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountBaseCurrText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1.add(jPanel1_2_1_1, gridBagConstraints);

		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);

		jPanel1_2_2.setLayout(new java.awt.BorderLayout());

		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(325, 400));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(325, 400));
		VoucherNoLabel.setText("Voucher No");
		VoucherNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(VoucherNoLabel, gridBagConstraints);

		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);

		VoucherDateLabel.setText("Voucher Date*");
		VoucherDateLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(VoucherDateLabel, gridBagConstraints);

		JobTittleLabel.setText("Job Title");
		JobTittleLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(JobTittleLabel, gridBagConstraints);

		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(UnitCodeLabel, gridBagConstraints);

		DepartmentLabel.setText("Department");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DepartmentLabel, gridBagConstraints);

		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DescriptionLabel, gridBagConstraints);

		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RemarksLabel, gridBagConstraints);

		JobTitleText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(JobTitleText, gridBagConstraints);

		UnitCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(UnitCodeText, gridBagConstraints);

		DepartmentText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DepartmentText, gridBagConstraints);

		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 85));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DescriptionScrollPane, gridBagConstraints);

		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(290, 70));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScrollPane.setViewportView(RemarksTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RemarksScrollPane, gridBagConstraints);

		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(TransactionDateDate, gridBagConstraints);

		jPanel1_2_2.add(jPanel1_2_2_1, java.awt.BorderLayout.CENTER);

		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel1_2, java.awt.BorderLayout.CENTER);

		jPanel1_3.setLayout(new java.awt.BorderLayout());

		jPanel1_3.setPreferredSize(new java.awt.Dimension(700, 90));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());

		OriginatorComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(275,110));


		jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);

		jPanel1_3_2.setLayout(new java.awt.BorderLayout());

		ApprovedComp.setLayout(new java.awt.GridBagLayout());

		ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(275,110));


		jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);

		jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());

		ReceivedComp.setLayout(new java.awt.GridBagLayout());

		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(275,110));


		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);

		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);

		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);

		add(jPanel1, java.awt.BorderLayout.NORTH);

	}
	private void addingListener(){
		PaymentSourceCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		//accountComp().addPropertyChangeListener(this);
		payToComp().addPropertyChangeListener(this);
		AmountText.addPropertyChangeListener(this);
		ExchRateText.addPropertyChangeListener(this);
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		EmpReceivableDetailComp.addActionListener(this);
		TransactionDateDate.addPropertyChangeListener("date", this);
		CurrComp.addPropertyChangeListener("object",this);
	}



	protected void doDelete() {
		super.doDelete();
		if (entity().getStatus()==-1){
			clearKomponen();
			clearForm();
		}
	}



	protected void doSubmit() {
		validityMsgs.clear();
		if (AccountComp.getObject()== null)
			addInvalid("Source account must be selected");
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
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
		ChequeNoText.setEditable(false);
		ChequeDueDateDate.setEditable(false);
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == PaymentSourceCombo) {
			Object objCurr = CurrComp.getObject();
			if (objCurr instanceof Currency) {
				Currency curr = (Currency) objCurr;
				validateAccount(curr);
			}
		}
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new Vchr_EmpReceiv(m_conn,m_sessionid,m_entity,payToComp());
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}else if(e.getSource() == m_searchRefNoBtn) {
			SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame.getMainFrame(), "Search Voucher", m_conn, m_sessionid,
					new   UnitBankCashTransferLoader(m_conn,PmtEmpReceivable.class),"Employee Receivable" );
			dlg.setVisible(true);
			if (dlg.selectedObj != null){
				PmtEmpReceivable temp=(PmtEmpReceivable) dlg.selectedObj;
				System.out.println(temp.getTrans());
				doLoad(dlg.selectedObj);
			}
		}
		else if (e.getSource()==EmpReceivableDetailComp){
			if (PayToComp.getEmployee()!=null){
				Employee obj=PayToComp.getEmployee();
				LookupEmployeeRcvPicker dlg=new LookupEmployeeRcvPicker(m_conn, m_sessionid, obj);
				dlg.done1();
			}else{
				JOptionPane.showMessageDialog(this,"Please select pay to employee first");
			}
		}
	}

	private void validateAccount(Currency curr) {
		String receive = (String)PaymentSourceCombo.getSelectedItem();
		System.out.println(receive);
		jPanel1_2_1.remove(accountComp());
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();
		if(receive.equals("Cash")){
			setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid,curr));
			ChequeNoText.setText("");
			ChequeDueDateDate.setDate(null);
			this.ChequeNoText.setEditable(false);
			this.ChequeDueDateDate.setEditable(false);
		}else{
			setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid,curr));
			ChequeNoText.setText("");
			ChequeDueDateDate.setDate(null);
			this.ChequeNoText.setEditable(true);
			this.ChequeDueDateDate.setEditable(true);
		}
		accountComp().setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(accountComp(), gridBagConstraints);
		jPanel1_2_1.revalidate();
	}

	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
		OriginatorComp.m_datePicker.setDate(new Date());
		ApprovedComp.m_datePicker.setDate(new Date());
		ReceivedComp.m_datePicker.setDate(new Date());
		CurrComp.setObject(this.baseCurrency);
	}

	private LookupPicker AccountComp;
	private javax.swing.JLabel AccountLabel;
	private javax.swing.JLabel AmountBaseCurrLabel4;
	//private javax.swing.JFormattedTextField AmountBaseCurrText;
	private javax.swing.JTextField AmountBaseCurrText;
	private javax.swing.JFormattedTextField AmountBaseText;
	private javax.swing.JLabel AmountLabel4;
	private javax.swing.JFormattedTextField AmountText;
	private DatePicker ChequeDueDateDate;
	private javax.swing.JLabel ChequeDueDateLabel;
	private javax.swing.JLabel ChequeNoLabel;
	private javax.swing.JTextField ChequeNoText;
	private CurrencyPicker CurrComp;
	private javax.swing.JLabel CurrencyLabel4;
	private javax.swing.JLabel DepartmentLabel;
	private javax.swing.JTextField DepartmentText;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JButton EmpReceivableDetailComp;
	private javax.swing.JLabel ExchRateLabel4;
	private javax.swing.JFormattedTextField ExchRateText;
	private javax.swing.JLabel ExpSheetDiffLabel4;
	private javax.swing.JTextField JobTitleText;
	private javax.swing.JLabel JobTittleLabel;
	private EmployeePicker PayToComp;
	private javax.swing.JLabel PayToLabel;
	private javax.swing.JComboBox PaymentSourceCombo;
	private javax.swing.JLabel PaymentSourceLabel;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScrollPane;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JTextField SubmittedDateText;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JLabel UnitCodeLabel;
	private javax.swing.JTextField UnitCodeText;
	private javax.swing.JLabel VoucherDateLabel;
	private javax.swing.JLabel VoucherNoLabel;
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
	private javax.swing.JSeparator jSeparator6;


	protected void gui2entity() {
		String paySource = (String) PaymentSourceCombo.getSelectedItem();
		entity().setPaymentSource(paySource);
		Object objAcc = accountComp().getObject();
		if (objAcc instanceof BankAccount) {
			BankAccount bank = (BankAccount) objAcc;
			entity().setBankAccount(bank);
			entity().setCashAccount(null);
			entity().setUnit(bank.getUnit());
		}
		else if (objAcc instanceof CashAccount) {
			CashAccount cash = (CashAccount) objAcc;
			entity().setCashAccount(cash);
			entity().setBankAccount(null);
			entity().setUnit(cash.getUnit());
		}else {
			entity().setCashAccount(null);
			entity().setBankAccount(null);
			Unit unit = DefaultUnit.createDefaultUnit(m_conn,m_sessionid);
			entity().setUnit(unit);
		}

		entity().setChequeNo(ChequeNoText.getText());
		entity().setChequeDueDate(ChequeDueDateDate.getDate());
		entity().setPayTo(PayToComp.getEmployee());

		entity().setCurrency((Currency)CurrComp.getObject());
		if (ExchRateText.getValue()!=null)
			entity().setExchangeRate(((Number)ExchRateText.getValue()).doubleValue());

		Object obj = AmountText.getValue();
		Number amount = (Number) obj;
		entity().setAmount(amount.doubleValue());
		entity().setTransactionDate(TransactionDateDate.getDate());

		entity().setRemarks(RemarksTextArea.getText());

		entity().transTemplateRead(
				this.OriginatorComp, this.ApprovedComp,
				this.ReceivedComp, this.RefNoText,
				this.DescriptionTextArea
		);
		entity().setEndingBalance(entity().getAmount());
	}

	protected void entity2gui() {
		if(entity().getCurrency()!=null)
			CurrComp.setObject(entity().getCurrency());
		else
			CurrComp.setObject(baseCurrency);

		String receive = (String)PaymentSourceCombo.getSelectedItem();
		PaymentSourceCombo.setSelectedItem(receive);
		if(entity().getBankAccount()!=null)
			AccountComp.setObject(entity().getBankAccount());
		else if (entity().getCashAccount()!=null)
			AccountComp.setObject(entity().getCashAccount());

		ChequeNoText.setText(entity().getChequeNo());
		ChequeDueDateDate.setDate(entity().getChequeDueDate());
		PayToComp.findEmployee(entity().getPayTo());
		AmountBaseCurrText.setText(baseCurrency.getSymbol());

		if(entity().getExchangeRate()>0)
			ExchRateText.setValue(new Double(entity().getExchangeRate()));
		else
			ExchRateText.setValue(new Double(1));
		AmountText.setValue(new Double(entity().getAmount()));
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
		StatusText.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");
	}

	protected Object createNew(){
		PmtEmpReceivable a  = new PmtEmpReceivable();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}

	protected void doSave() {
		if (!cekValidity()) return;
		super.doSave();
		AccountingSQLSAP sql=new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_EMP_RECEIVABLE, m_conn);
				entity().setIndex(index);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	protected StateTemplateEntity currentEntity() {
		return entity();
	}

	void setEntity(Object m_entity) {
		PmtEmpReceivable oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PmtEmpReceivable)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}

	PmtEmpReceivable entity() {
		return m_entity;
	}

	protected void enableEditMode(){
		CurrComp.setEnabled(true);
		this.PaymentSourceCombo.setEnabled(true);
		this.accountComp().setEnabled(true);
		this.AmountBaseCurrText.setEnabled(true);
		this.ChequeNoText.setEditable(true);
		this.ChequeDueDateDate.setEditable(true);
		this.PayToComp.setEditable(true);

		if (CurrComp.getObject()!=null){
			if (((Currency)CurrComp.getObject()).getSymbol().equalsIgnoreCase(this.baseCurrency.getSymbol())){
				ExchRateText.setEditable(false);
				ExchRateText.setValue(new Double(1));
			}else
				ExchRateText.setEditable(true);
		}
		this.AmountText.setEditable(true);
		this.TransactionDateDate.setEditable(true);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
		this.EmpReceivableDetailComp.setEnabled(true);
	}

	protected void disableEditMode(){
		CurrComp.setEnabled(false);
		this.PaymentSourceCombo.setEnabled(false);
		this.accountComp().setEnabled(false);
		this.AmountBaseCurrText.setEnabled(false);
		this.ChequeNoText.setEditable(false);
		this.ChequeDueDateDate.setEditable(false);
		this.PayToComp.setEditable(false);
		this.ExchRateText.setEditable(false);
		this.AmountText.setEditable(false);
		this.TransactionDateDate.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
		this.EmpReceivableDetailComp.setEnabled(false);
	}

	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (AmountBaseText.getText().compareTo("")==0)
			addInvalid("Amount must be filled");
		if (TransactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (CurrComp.getObject() == null)
			addInvalid("Currency must be selected");
		if (DescriptionTextArea.getText().compareTo("")==0)
			addInvalid("Description Text Area must be filled");
		if (PayToComp.getEmployee()==null)
			addInvalid("Pay to must be selected");
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

	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		else if (evt.getSource()==AmountText){
			//setBaseCurr();
			toBaseCurrency();
		}
		else if (evt.getSource()==ExchRateText){
			//setBaseCurr();
			toBaseCurrency();
		}
		/*else if (evt.getSource() ==AccountComp){
			if (evt.getNewValue() instanceof BankAccount){
				BankAccount b = (BankAccount)AccountComp.getObject();
				//CurrComp.setObject(b.getCurrency());
			} else if (evt.getNewValue() instanceof CashAccount){
				CashAccount b = (CashAccount)AccountComp.getObject();
				CurrComp.setObject(b.getCurrency());
			}
			if (CurrComp.getObject()!=null)
				if (((Currency)CurrComp.getObject()).getSymbol().equalsIgnoreCase(this.baseCurrency.getSymbol()) ){
					ExchRateText.setEditable(false);
					ExchRateText.setValue(new Double(1));}
				else{
					setDefaultExchangeRate();
					ExchRateText.setEditable(true);
				}

		}*/
		else if(evt.getPropertyName().equals("employee")){
			JobTitleText.setText(payToComp().getJobTitle());
			UnitCodeText.setText(payToComp().getUnit());
			DepartmentText.setText(payToComp().getDepartment());
		}
		if ("value".equals(evt.getPropertyName())){
			if ((evt.getSource()==AmountText)||(evt.getSource()==ExchRateText)){
				toBaseCurrency();
			}
		}
		if ("date".equals(evt.getPropertyName())){
			if (evt.getSource()==TransactionDateDate) {
				setDefaultExchangeRate();
			}
		}
		if (evt.getSource()==CurrComp){
			Object objCurr= CurrComp.getObject();
			if (objCurr instanceof Currency) {
				Currency curr = (Currency) objCurr;
				if (curr.getSymbol().equals(baseCurrency.getSymbol()))
					ExchRateText.setEditable(false);
				else
					ExchRateText.setEditable(true);
				setDefaultExchangeRate();
				validateAccount(curr);
			}
		}
	}

	private void setDefaultExchangeRate() {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate((Currency)CurrComp.getObject(),date);
		ExchRateText.setValue(new Double(rate));
	}

	/*private void setBaseCurr() {
		double amount=0,crate=1;
		if (AmountText.getValue()!=null)
			amount=((Number)AmountText.getValue()).doubleValue();
		if(ExchRateText.getValue()!=null)
			crate=((Number)ExchRateText.getValue()).doubleValue();
		AmountBaseCurrText.setValue(new Double(amount*crate));

	}*/

	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_EMPLOYEE_RECEIVABLE, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_EMPLOYEE_RECEIVABLE, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_EMPLOYEE_RECEIVABLE, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}

	private void enableAwal(){
		setenableEditPanel(jPanel1_2_1,false);
		setenableEditPanel(jPanel1_2_1_1,false);
		setenableEditPanel(jPanel1_2_2_1,false);
	}

	private void toBaseCurrency() {
		try{
			double amount,crate;
			if (!AmountText.getText().equalsIgnoreCase(""))
				amount=((Number)AmountText.getValue()).doubleValue();
			else
				amount=0;
			String xchRate=ExchRateText.getText();
			if(!xchRate.equalsIgnoreCase(""))
				crate=((Number)ExchRateText.getValue()).doubleValue();
			else
				crate=1;
			AmountBaseText.setValue(new Double(amount*crate));

		}catch (Exception e){
			System.out.println("Error");
		}
	}

	/*public void insertUpdate(DocumentEvent e) {
		toBaseCurrency();
	}

	public void removeUpdate(DocumentEvent e) {
		toBaseCurrency();
	}

	public void changedUpdate(DocumentEvent e) {
		toBaseCurrency();
	}*/

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

	private EmployeePicker payToComp() {
		return PayToComp;
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

	public void clearKomponen(){
		StatusText.setText("");
		OriginatorComp.setEmployee(null);
		ApprovedComp.setEmployee(null);
		ReceivedComp.setEmployee(null);
		OriginatorComp.m_jobTextField.setText("");
		ApprovedComp.m_jobTextField.setText("");
		ReceivedComp.m_jobTextField.setText("");
		OriginatorComp.setDate(null);
		ApprovedComp.setDate(null);
		ReceivedComp.setDate(null);
		CurrComp.setObject(null);
		ExchRateText.setValue(new Double(0));
		AmountBaseCurrText.setText("");
		TransactionDateDate.setDate(null);
	}

	public void clearAll(){
		super.clearAll();
		clearForm();
		clearKomponen();
		disableEditMode();
	}

}
