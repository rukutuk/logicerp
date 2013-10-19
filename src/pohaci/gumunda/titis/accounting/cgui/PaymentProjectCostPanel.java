package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Component;
import java.awt.Font;
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

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.Vchr_project;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCost;
import pohaci.gumunda.titis.accounting.entity.PmtProjectCostDetail;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.UnitBankCashTransferLoader;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.FormattedDoubleCellRenderer;
import pohaci.gumunda.titis.application.FormattedStandardCellRenderer;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeeCellEditor;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.PartnerCellEditor;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.ProjectDataCellEditor;
import pohaci.gumunda.titis.project.cgui.ProjectDataPicker;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class PaymentProjectCostPanel extends RevTransactionPanel implements
ActionListener, PropertyChangeListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private PmtProjectCost m_entity;

	protected Employee defaultOriginator;

	protected Employee defaultApproved;

	protected Employee defaultReceived;

	public PaymentProjectCostPanel(Connection conn, long sessionid) {
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
		initExchangeRateHelper(conn, sessionid);
		setDefaultSignature();
		setEntity(new PmtProjectCost());
		m_entityMapper = MasterMap.obtainMapperFor(PmtProjectCost.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}

	private void setDefaultExchangeRate(Currency currency) {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		ExchRateText.setValue(new Double(rate));
	}

	private void initComponents() {
		AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid);
		PayToComp = new JTextField();
		CurrComp = new CurrencyPicker(m_conn, m_sessionid);
		UnitCodeText = new JTextField();
		DepartmentText = new JTextField();
		ProjectCodeComp = new ProjectDataPicker(m_conn, m_sessionid);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid, "Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid, "Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid, "Received by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon(
		"../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon(
		"../images/filter.gif"));
		TopButtonPanel = new javax.swing.JPanel();
		BottomButtonPanel = new javax.swing.JPanel();
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
		ChequeNoLabel = new javax.swing.JLabel();
		ChequeDueDateLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		PaymentSourceCombo = new javax.swing.JComboBox();
		ChequeNoText = new javax.swing.JTextField();
		ChequeDueDateDate = new DatePicker();
		PayToLabel = new javax.swing.JLabel();
		CurrencyLabel = new javax.swing.JLabel();
		DescriptionLabel = new javax.swing.JLabel();
		jPanel1_2_1_1 = new javax.swing.JPanel();
		ExchRateLabel1 = new javax.swing.JLabel();
		ExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_2_1 = new javax.swing.JPanel();
		VoucherNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		VoucherDateLabel = new javax.swing.JLabel();
		ProjectCodeLabel = new javax.swing.JLabel();
		CustomerLabel = new javax.swing.JLabel();
		WorkDescLabel = new javax.swing.JLabel();
		CustomerText = new javax.swing.JTextField();
		WorkDescScrollPane = new javax.swing.JScrollPane();
		WorkDescText = new javax.swing.JTextArea();
		UnitCodeLabel = new javax.swing.JLabel();
		ActivityCodeLabel = new javax.swing.JLabel();
		ActivityCodeText = new javax.swing.JTextField();
		DepartmentLabel = new javax.swing.JLabel();
		ORNoLabel = new javax.swing.JLabel();
		ORNoText = new javax.swing.JTextField();
		SOPOContractNo = new javax.swing.JLabel();
		IPCNoLabel = new javax.swing.JLabel();
		ContractNoText = new javax.swing.JTextField();
		IPCNoText = new javax.swing.JTextField();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_3 = new javax.swing.JPanel();
		jPanel1_3_4_1 = new javax.swing.JPanel();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel2_1 = new javax.swing.JPanel();
		AddRcvTypeAccountBtn = new javax.swing.JButton();
		DeleteRcvTypeAccountBtn = new javax.swing.JButton();
		ProjectCostAccountSrollPane = new javax.swing.JScrollPane();
		m_table = new BottomTablePmtProject();

		setLayout(new java.awt.BorderLayout());

		setPreferredSize(new java.awt.Dimension(700, 675));
		jPanel1.setLayout(new java.awt.BorderLayout());

		jPanel1.setPreferredSize(new java.awt.Dimension(850, 500));
		jPanel1_1.setLayout(new java.awt.BorderLayout());

		jPanel1_1.setPreferredSize(new java.awt.Dimension(650, 35));
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

		jPanel1_1.add(TopButtonPanel, java.awt.BorderLayout.WEST);

		jPanel1.add(jPanel1_1, java.awt.BorderLayout.NORTH);

		jPanel1_2.setLayout(new java.awt.BorderLayout());

		jPanel1_2.setPreferredSize(new java.awt.Dimension(650, 320));
		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(420, 320));
		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
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

		ReceiveToLabel.setText("Payment Source*");
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ReceiveToLabel, gridBagConstraints);

		AccountLabel.setText("Account");
		AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountLabel, gridBagConstraints);

		ChequeNoLabel.setText("Cheque No");
		ChequeNoLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeNoLabel, gridBagConstraints);

		ChequeDueDateLabel.setText("Cheque Due Date");
		ChequeDueDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
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

		PaymentSourceCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Cash", "Bank" }));
		PaymentSourceCombo.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PaymentSourceCombo, gridBagConstraints);

		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);

		ChequeNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeNoText, gridBagConstraints);

		ChequeDueDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeDueDateDate, gridBagConstraints);

		PayToLabel.setText("Pay To*");
		PayToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PayToLabel, gridBagConstraints);

		PayToComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PayToComp, gridBagConstraints);

		CurrencyLabel.setText("Currency*");
		CurrencyLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(CurrencyLabel, gridBagConstraints);

		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DescriptionLabel, gridBagConstraints);

		jPanel1_2_1_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(290, 23));
		ExchRateLabel1.setText("Exch. Rate*");
		ExchRateLabel1.setPreferredSize(new java.awt.Dimension(60, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_1_1.add(ExchRateLabel1, gridBagConstraints);

		ExchRateText.setPreferredSize(new java.awt.Dimension(170, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_1.add(ExchRateText, gridBagConstraints);

		CurrComp.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		jPanel1_2_1_1.add(CurrComp, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(jPanel1_2_1_1, gridBagConstraints);

		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 95));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DescriptionScrollPane, gridBagConstraints);

		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);

		jPanel1_2_2.setLayout(new java.awt.BorderLayout());

		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(325, 305));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(415, 400));
		VoucherNoLabel.setText("Voucher No");
		VoucherNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(VoucherNoLabel, gridBagConstraints);

		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);

		VoucherDateLabel.setText("Voucher Date*");
		VoucherDateLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(VoucherDateLabel, gridBagConstraints);

		ProjectCodeLabel.setText("Project Code*");
		ProjectCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ProjectCodeLabel, gridBagConstraints);

		CustomerLabel.setText("Customer");
		CustomerLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(CustomerLabel, gridBagConstraints);

		WorkDescLabel.setText("Work Desc. ");
		WorkDescLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(WorkDescLabel, gridBagConstraints);

		ProjectCodeComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ProjectCodeComp, gridBagConstraints);

		CustomerText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(CustomerText, gridBagConstraints);

		WorkDescScrollPane.setPreferredSize(new java.awt.Dimension(200, 63));
		WorkDescText.setColumns(20);
		WorkDescText.setLineWrap(true);
		WorkDescText.setRows(5);
		WorkDescScrollPane.setViewportView(WorkDescText);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(WorkDescScrollPane, gridBagConstraints);

		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(UnitCodeLabel, gridBagConstraints);

		UnitCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(UnitCodeText, gridBagConstraints);

		ActivityCodeLabel.setText("Activity Code");
		ActivityCodeLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ActivityCodeLabel, gridBagConstraints);

		ActivityCodeText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ActivityCodeText, gridBagConstraints);

		DepartmentLabel.setText("Department");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DepartmentLabel, gridBagConstraints);

		ORNoLabel.setText("O.R No");
		ORNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ORNoLabel, gridBagConstraints);

		DepartmentText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DepartmentText, gridBagConstraints);

		ORNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ORNoText, gridBagConstraints);

		SOPOContractNo.setText("SO/PO/Contract No");
		SOPOContractNo.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(SOPOContractNo, gridBagConstraints);

		IPCNoLabel.setText("IPC No");
		IPCNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(IPCNoLabel, gridBagConstraints);

		ContractNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ContractNoText, gridBagConstraints);

		IPCNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(IPCNoText, gridBagConstraints);

		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(TransactionDateDate, gridBagConstraints);

		jPanel1_2_2.add(jPanel1_2_2_1, java.awt.BorderLayout.WEST);

		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel1_2, java.awt.BorderLayout.CENTER);

		jPanel1_3.setLayout(new java.awt.BorderLayout());

		jPanel1_3.setPreferredSize(new java.awt.Dimension(700, 200));
		jPanel1_3_3.setLayout(new java.awt.BorderLayout());

		jPanel1_3_3.setPreferredSize(new java.awt.Dimension(100, 100));
		jPanel1_3_4_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_3_4_1.setPreferredSize(new java.awt.Dimension(825, 50));
		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(700, 85));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScrollPane.setViewportView(RemarksTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_3_4_1.add(RemarksScrollPane, gridBagConstraints);

		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(110, 15));

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 1);

		jPanel1_3_4_1.add(RemarksLabel, gridBagConstraints);
		jPanel1_3_3.add(jPanel1_3_4_1, java.awt.BorderLayout.WEST);
		jPanel1_3.add(jPanel1_3_3, java.awt.BorderLayout.NORTH);

		OriginatorComp.setLayout(new java.awt.GridBagLayout());
		OriginatorComp.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(275, 110));

		jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);
		jPanel1_3_2.setLayout(new java.awt.BorderLayout());

		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		ApprovedComp.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(275, 110));

		jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);
		jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());

		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		ReceivedComp.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(275, 110));

		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);
		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);
		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);
		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);

		add(jPanel1, java.awt.BorderLayout.NORTH);

		jPanel2.setLayout(new java.awt.BorderLayout());
		jPanel2_1.setLayout(new java.awt.BorderLayout());

		BottomButtonPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.LEFT, 3, 5));
		BottomButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));

		AddRcvTypeAccountBtn.setText("Add");
		AddRcvTypeAccountBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		AddRcvTypeAccountBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		BottomButtonPanel.add(AddRcvTypeAccountBtn);

		DeleteRcvTypeAccountBtn.setText("Delete");
		DeleteRcvTypeAccountBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		DeleteRcvTypeAccountBtn
		.setPreferredSize(new java.awt.Dimension(50, 20));
		BottomButtonPanel.add(DeleteRcvTypeAccountBtn);
		jPanel2_1.add(BottomButtonPanel, java.awt.BorderLayout.WEST);
		jPanel2.add(jPanel2_1, java.awt.BorderLayout.NORTH);
		ProjectCostAccountSrollPane.setPreferredSize(new java.awt.Dimension(
				650, 215));
		m_table
		.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {}, new String[] { "Account",
						"Desciption", "Amount" }));
		ProjectCostAccountSrollPane.setViewportView(m_table);
		jPanel2.add(ProjectCostAccountSrollPane, java.awt.BorderLayout.CENTER);
		add(jPanel2, java.awt.BorderLayout.CENTER);
	}

	private void addingListener() {
		PaymentSourceCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		CurrComp.addPropertyChangeListener("object", this);
		ProjectCodeComp.addPropertyChangeListener("object", this);
		AddRcvTypeAccountBtn.addActionListener(this);
		DeleteRcvTypeAccountBtn.addActionListener(this);
		//AccountComp.addPropertyChangeListener("object",this);
		TransactionDateDate.addPropertyChangeListener("date", this);
	}

	protected void setAccountComp(LookupPicker accountComp) {
		AccountComp = accountComp;
		AccountComp.addPropertyChangeListener(this);
	}

	private LookupPicker accountComp() {
		return AccountComp;
	}

	protected void doCancel() {
		super.doCancel();
		setEnableButtonBawah(false);
		if (AccountComp.getObject()!=null)
			LoadProjectDetail(entity().getIndex());
	}

	protected void doDelete() {
		super.doDelete();
		if (entity().getStatus() == StateTemplateEntity.State.NEW) {
			clearKomponen();
			clearForm();
			setEnableButtonBawah(false);
		}
	}

	public void doEdit() {
		super.doEdit();
		Object currObject = CurrComp.getObject();
		if (currObject != null)
			propertyCurrecy(currObject);
		setEnableButtonBawah(true);
	}

	protected void doNew() {
		revalidateAccountComp((Currency)CurrComp.getObject());
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
		setEnableButtonBawah(true);
		ChequeNoText.setEditable(false);
		ChequeDueDateDate.setEditable(false);
	}

	protected void doSubmit() {
		validityMsgs.clear();
		if (entity().getBankAccount()==null && entity().getCashAccount()==null)
			addInvalid("Source account must be selected");
		if (!m_table.cekTotalDebitCredit())
			addInvalid("Transaction is unbalanced. Please check accounts");
		if (validityMsgs.size() > 0) {
			StringBuffer result = new StringBuffer();
			Iterator iter = validityMsgs.iterator();
			while (iter.hasNext()) {
				String o = iter.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this, result);
			return;
		}
		super.doSubmit();
		setEnableButtonBawah(false);
	}

	boolean m_load = false;
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == AddRcvTypeAccountBtn) {
			String receive = (String) PaymentSourceCombo.getSelectedItem();
			if (receive.equals("Cash"))
				ProjectAccountComp = new LookupAccountPicker1(m_conn,m_sessionid, IConstants.ATTR_PMT_CASH);
			else
				ProjectAccountComp = new LookupAccountPicker1(m_conn,m_sessionid, IConstants.ATTR_PMT_BANK);
			ProjectAccountComp.done();
			if (ProjectAccountComp.getObject() != null) {
				int row = m_table.getRowCount()-2;
				m_nonDeleteList.add(new String("0"));
				Vector vector = new Vector();
				JournalStandardAccount jsAccount = (JournalStandardAccount)ProjectAccountComp.getObject();
				Account acc=jsAccount.getAccount();
				vector.addElement(new Integer(m_table.getRowCount() + 1));
				vector.addElement(acc.toStringWithCode());
				vector.addElement("");
				vector.addElement(null);
				vector.addElement(new Double(0));
				vector.addElement(new Double(0));
				m_table.addData(vector, jsAccount, row);
			}
		}
		else if (e.getSource() == DeleteRcvTypeAccountBtn) {
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
		else if (e.getSource() == PaymentSourceCombo) {
			Object currObj= CurrComp.getObject();
			/*if (currObj!=null){*/
			revalidateAccountComp((Currency)currObj);
			m_table.clearTable();
			/*}else{
			 JOptionPane.showMessageDialog(this, "Data is empty", "Warning",
			 JOptionPane.WARNING_MESSAGE);
			 }*/
		} else if (e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex() > 0)
				new Vchr_project(m_entity, m_conn, m_sessionid);
			else {
				JOptionPane.showMessageDialog(this, "Data is empty", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		} else if (e.getSource() == m_searchRefNoBtn) {
			SearchVoucherDialog dlg = new SearchVoucherDialog(
					GumundaMainFrame.getMainFrame(),
					"Search Voucher",
					m_conn,
					m_sessionid,
					new UnitBankCashTransferLoader(m_conn, PmtProjectCost.class),
					"Project Cost",true);
			dlg.setVisible(true);
			if (dlg.selectedObj != null) {
				m_load = true;
				doLoad(dlg.selectedObj);
			}

			/*SearchVoucherFrame dlg = new SearchVoucherFrame(
					GumundaMainFrame.getMainFrame(),
					"Search Voucher",
					m_conn,
					m_sessionid,
					new UnitBankCashTransferLoader(m_conn, PmtProjectCost.class),
					"Project Cost",true);
			dlg.setVisible(true);
			if (dlg.selectedObj != null) {
				m_load = true;
				doLoad(dlg.selectedObj);
			}*/
		}

	}

	private void revalidateAccountComp(Currency curr) {
		String receive = (String) PaymentSourceCombo.getSelectedItem();
		jPanel1_2_1.remove(accountComp());
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();
		if (receive.equals("Cash")) {
			setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid,curr));
			ChequeNoText.setText("");
			ChequeDueDateDate.setDate(null);
			ChequeNoText.setEditable(false);
			ChequeDueDateDate.setEditable(false);
		} else if (receive.equals("Bank")){
			setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid,curr));
			ChequeNoText.setText("");
			ChequeDueDateDate.setDate(null);
			ChequeNoText.setEditable(true);
			ChequeDueDateDate.setEditable(true);
		}
		accountComp().setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(accountComp(), gridBagConstraints);
		jPanel1_2_1.revalidate();
	}

	public void LoadProjectDetail(long a) {
		String receive = (String) PaymentSourceCombo.getSelectedItem();
		JournalStandardAccount[]  jsAcc = getJournalStandardAccount(receive);
		GenericMapper mapper2 = MasterMap.obtainMapperFor(PmtProjectCostDetail.class);
		mapper2.setActiveConn(m_conn);
		String strWhere = IDBConstants.ATTR_PMT_PROJECT_COST+ "=" + a;
		List rs = mapper2.doSelectWhere(strWhere);
		m_table.clearTable();
		PmtProjectCostDetail detail;
		Vector vektor;
		Account acc;
		for (int i = 0; i < rs.size(); i++) {
			vektor = new Vector();
			detail = (PmtProjectCostDetail) rs.get(i);
			acc = (Account) detail.getAccount();
			JournalStandardAccount jsAccount = null;
			for (int j=0;j<jsAcc.length;j++){
				if (acc.getIndex()==jsAcc[j].getAccount().getIndex()){
					jsAccount = jsAcc[j];
					//if (jsAccount.isCalculate()){
					if (jsAccount.isHidden()){
						m_nonDeleteList.add(new String("1"));
					}else{
						m_nonDeleteList.add(new String("0"));
					}
					vektor.addElement(new Integer(i + 1));
					vektor.addElement(acc.toStringWithCode());
					vektor.addElement(detail.getDescription());
					String SubsidiaryAccSet = getSubsidiaryByindex(acc);
					AccountingBusinessLogic accLogic = new AccountingBusinessLogic(m_conn);
					ProjectBusinessLogic proLogic = new ProjectBusinessLogic(m_conn);
					HRMBusinessLogic hrmLogic = new HRMBusinessLogic(m_conn);
					if (SubsidiaryAccSet.equals("Employee")){
						SetEmployeeToTable(detail, vektor, hrmLogic);
					}else if (SubsidiaryAccSet.equals("Partner")){
						SetPartnerToTable(detail, vektor, proLogic);
					}else if (SubsidiaryAccSet.equals("Customer")){
						SetCustomerToTable(detail, vektor, proLogic);
					}else if (SubsidiaryAccSet.equals("Bank")){
						SetBankAccountToTable(detail, vektor, accLogic);
					}else if (SubsidiaryAccSet.equals("Cash")){
						SetCashAccountToTable(detail, vektor, accLogic);
					}else if (SubsidiaryAccSet.equals("Loan")){
						SetCompanyLoanToTable(detail, vektor, accLogic);
					}else if (SubsidiaryAccSet.equals("Project")){
						SetProjectToTable(detail, vektor, proLogic );
					}else{
						vektor.addElement("");
					}
					if (jsAccount.m_balance==0){
						vektor.addElement(new Double(detail.getaccValue()));
						vektor.addElement(new Double(0));
					}
					else if (jsAccount.m_balance==1){
						vektor.addElement(new Double(0));
						vektor.addElement(new Double(detail.getaccValue()));
					}
					m_table.addData(vektor, jsAccount, i);
				}
			}
		}
	}



	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())) {
			readEntityState();
		}
		if (evt.getSource() == CurrComp) {
			Object currObject = CurrComp.getObject();
			if (currObject == null){
				ExchRateText.setEditable(false);
			}else {
				propertyCurrecy(currObject);
				setDefaultExchangeRate((Currency) currObject);
				revalidateAccountComp((Currency)currObject);
			}

			/*Object currObj= CurrComp.getObject();
			 if (currObj!=null){

			 }*/

		} else if (evt.getSource() == ProjectCodeComp) {
			if (evt.getNewValue() != null) {
				ProjectData temp = (ProjectData) ProjectCodeComp.getObject();
				Customer cust = temp.getCustomer();
				if (cust != null)
					CustomerText.setText(cust.getName());
				else
					CustomerText.setText("");
				WorkDescText.setText(temp.getWorkDescription());
				UnitCodeText.setText(temp.getUnit().getDescription());
				ActivityCodeText.setText(temp.getActivity().getName());
				DepartmentText.setText(temp.getDepartment().getName());
				ORNoText.setText(temp.getORNo());
				ContractNoText.setText(temp.getPONo());
				IPCNoText.setText(temp.getIPCNo());
			} else {
				CustomerText.setText("");
				WorkDescText.setText("");
				UnitCodeText.setText("");
				ActivityCodeText.setText("");
				DepartmentText.setText("");
				ORNoText.setText("");
				ContractNoText.setText("");
				IPCNoText.setText("");
			}
		}

		else if (evt.getSource() == AccountComp) {
			if ("object".equals(evt.getPropertyName())) {
				if (AccountComp.getObject() != null) {
					/*	Currency currency = ((CashBankAccount) AccountComp
					 .getObject()).getCurrency();
					 CurrComp.setObject(currency);
					 if (currency.getSymbol().equals(baseCurrency.getSymbol())) {
					 ExchRateText.setEnabled(false);
					 ExchRateText.setValue(new Double(1));
					 } else {
					 ExchRateText.setEnabled(true);
					 setDefaultExchangeRate(currency);
					 }*/

					addToTable(evt.getNewValue());
				}
			}
		}
		else if (evt.getSource() == TransactionDateDate) {
			if ("date".equals(evt.getPropertyName())) {
				CashBankAccount cba = (CashBankAccount) AccountComp.getObject();
				if (cba != null) {
					Currency curr = cba.getCurrency();
					setDefaultExchangeRate(curr);
				}
			}
		}
	}

	private void propertyCurrecy(Object currObject) {
		if (((Currency) currObject).getSymbol().equalsIgnoreCase("RP")){
			ExchRateText.setValue(new Double(1));
			ExchRateText.setEditable(false);
		}else{
			ExchRateText.setEditable(true);
			/*//if (entity().getExchangeRate()>0)
			 ExchRateText.setValue(new Double(1));*/

		}
	}



	public JournalStandardAccount[] getJournalStandardAccount(Object obj){
		String attr = "";
		if (obj instanceof CashAccount) {
			attr = IConstants.ATTR_PMT_CASH;
		}else if (obj instanceof BankAccount) {
			attr = IConstants.ATTR_PMT_BANK;
		}else if (obj instanceof String) {
			String receive = (String) obj;
			if (receive.equalsIgnoreCase("CASH"))
				attr = IConstants.ATTR_PMT_CASH;
			else
				attr = IConstants.ATTR_PMT_BANK;
		}
		JournalStandardSettingPickerHelper helper =new JournalStandardSettingPickerHelper(m_conn,m_sessionid,IDBConstants.MODUL_ACCOUNTING);
		List journalStdList = helper.getJournalStandardSettingWithAccount(IConstants.PAYMENT_PROJECT_COST,attr);

		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		JournalStandardAccount[] jsAcc = journal.getJournalStandardAccount();
		return jsAcc;
	}

	private JournalStandardAccount getJournalStandardAcc(Object obj, Account acc) {
		JournalStandardAccount[] jsAcc = getJournalStandardAccount(obj);
		JournalStandardAccount jsaccount  = null;
		for (int i=0;i<jsAcc.length;i++){
			if (jsAcc[i].getAccount().getIndex() == acc.getIndex())
				jsaccount =jsAcc[i];
		}
		return jsaccount;
	}


	ArrayList m_nonDeleteList = new ArrayList();
	private void addToTable(Object cashbankacc){
		Object obj =  AccountComp.getObject();
		Account acc =((CashBankAccount)obj).getAccount();
		JournalStandardAccount jsAccount = getJournalStandardAcc(obj, acc);

		String[] data = (String[])m_nonDeleteList.toArray(new String[m_nonDeleteList.size()]);
		boolean find = false;
		int rowFind = -1;
		for (int i=0;i<data.length;i++){
			if (data[i].equals("1")){
				find = true;
				rowFind = i;
			}
		}

		if (find){
			m_nonDeleteList.remove(rowFind);
			m_table.removeData(rowFind);
			m_nonDeleteList.add(rowFind,new String("1"));
			Vector vector = new Vector();
			vector.addElement(new Integer(m_table
					.getRowCount() + 1));
			vector.addElement(acc.toStringWithCode());
			vector.addElement("");
			if (cashbankacc instanceof BankAccount) {
				BankAccount bank = (BankAccount) cashbankacc;
				vector.addElement(bank);
			}else if (cashbankacc instanceof CashAccount) {
				CashAccount cash = (CashAccount) cashbankacc;
				vector.addElement(cash);
			}
			//vector.addElement(null);
			vector.addElement(new Double(0));
			vector.addElement(new Double(0));
			m_table.insertData(vector, jsAccount,rowFind);
		}else{
			m_nonDeleteList.add(new String("1"));
			int row = (m_table.getRowCount()-2);
			Vector vector = new Vector();
			vector.addElement(new Integer(m_table
					.getRowCount() + 1));
			vector.addElement(acc.toStringWithCode());
			vector.addElement("");
			if (cashbankacc instanceof BankAccount) {
				BankAccount bank = (BankAccount) cashbankacc;
				vector.addElement(bank);
			}else if (cashbankacc instanceof CashAccount) {
				CashAccount cash = (CashAccount) cashbankacc;
				vector.addElement(cash);
			}
			vector.addElement(new Double(0));
			vector.addElement(new Double(0));
			m_table.addData(vector, jsAccount, row);
		}
	}




	private void enableAwal() {
		setenableEditPanel(jPanel1_2_1, false);
		setenableEditPanel(jPanel1_2_1_1, false);
		setenableEditPanel(jPanel1_2_2, false);
		setenableEditPanel(jPanel1_2_2_1, false);
		WorkDescText.setEditable(false);
		setEnableButtonBawah(false);
		CurrComp.setEnabled(false);

	}

	protected void enableEditMode() {
		this.PaymentSourceCombo.setEnabled(true);
		CurrComp.setEnabled(true);
		accountComp().setEnabled(true);
		this.ChequeNoText.setEditable(true);
		this.ChequeDueDateDate.setEditable(true);
		this.PayToComp.setEditable(true);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.TransactionDateDate.setEditable(true);
		this.ProjectCodeComp.setEnabled(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
	}

	protected void disableEditMode() {
		this.PaymentSourceCombo.setEnabled(false);
		accountComp().setEnabled(false);
		CurrComp.setEnabled(false);
		this.ChequeNoText.setEditable(false);
		this.ChequeDueDateDate.setEditable(false);
		this.PayToComp.setEditable(false);
		this.ExchRateText.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.TransactionDateDate.setEditable(false);
		this.ProjectCodeComp.setEnabled(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
	}

	ArrayList validityMsgs = new ArrayList();

	protected boolean cekValidity() {
		validityMsgs.clear();
		if (TransactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (CurrComp.getObject() == null)
			addInvalid("currency must be selected");
		if (PayToComp.getText().equals(""))
			addInvalid("Pay to field must be selected");
		if (ProjectCodeComp.getObject() == null)
			addInvalid("Project code must be selected");
		if (DescriptionTextArea.getText().compareTo("") == 0)
			addInvalid("Description Text Area must be filled");
		if (ExchRateText.getText().compareTo("") == 0)
			addInvalid("Exchange rate must be filled");

		detailAccountOperation();// agar temp terisi gitoo loh, ya abiz
		// gimana !!?
		PmtProjectCostDetail[] temp = entity().getPmtProjectCostDetail();
		 if (temp.length == 0)
		 addInvalid("Account Detail must be added");
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
		String paySource = (String) PaymentSourceCombo.getSelectedItem();
		Object objAccount = AccountComp.getObject();
		entity().setPaymentSource(paySource);
		if (objAccount!=null) {
			if (objAccount instanceof BankAccount){
				BankAccount bank= (BankAccount) objAccount;
				//entity().setPaymentSource("BANK");
				entity().setBankAccount(bank);
				entity().setCashAccount(null);
				entity().setUnit(bank.getUnit());
			}else if (objAccount instanceof CashAccount) {
				CashAccount cash = (CashAccount) objAccount;
				//entity().setPaymentSource("CASH");
				entity().setCashAccount(cash);
				entity().setBankAccount(null);
				entity().setUnit(cash.getUnit());
			}
		}else{
			entity().setCashAccount(null);
			entity().setBankAccount(null);
			Unit unit = DefaultUnit.createDefaultUnit(m_conn,m_sessionid);
			entity().setUnit(unit);
		}
		entity().setChequeno(ChequeNoText.getText());
		entity().setChequeDueDate(ChequeDueDateDate.getDate());
		entity().setProject((ProjectData) ProjectCodeComp.getObject());
		entity().setProject(((ProjectData) ProjectCodeComp.getObject()));
		entity().setPayTo(PayToComp.getText());
		entity().setTotal(m_table.getTotal());
		entity().setCurrency((Currency) CurrComp.getObject());
		Object obj = ExchRateText.getValue();
		Number amount = (Number) obj;
		entity().transTemplateRead(this.OriginatorComp, this.ApprovedComp,
				this.ReceivedComp, this.RefNoText.getText(),
				this.DescriptionTextArea.getText(),
				this.RemarksTextArea.getText(),
				this.TransactionDateDate.getDate(), amount.doubleValue());
		detailAccountOperation();
	}	
	
	private void detailAccountOperation() {
		if (m_table.getDataCount() >= 0) {
			PmtProjectCostDetail[] temp = new PmtProjectCostDetail[m_table.getDataCount()];
			for (int i = 0; i < m_table.getDataCount(); i++) {
				temp[i] = new PmtProjectCostDetail();
				JournalStandardAccount jsAcc = (JournalStandardAccount)m_table.getListData().get(i);
				temp[i].setAccount(jsAcc.getAccount());
				temp[i].setDescription(m_table.getValueAt(i, 2).toString());

				Account acc = jsAcc.getAccount();
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
				try {
					Number val = null;
					if (jsAcc.m_balance==0)
						val= (Double) m_table.getValueAt(i, 4);
					else if (jsAcc.m_balance==1)
						val= (Double) m_table.getValueAt(i, 5);
					temp[i].setaccValue(val.doubleValue());
				} catch (Exception e) {
				}
				temp[i].setCurrency(entity().getCurrency());
				temp[i].setExchangeRate(entity().getExchangeRate());
			}
			entity().setProjectDetail(temp);
		}
	}

	// 5. Entity2Gui
	protected void entity2gui() {
		CurrComp.setObject(entity().getCurrency());

		if (entity().getPaymentSource() != null) {
			if (entity().getPaymentSource().equalsIgnoreCase("BANK"))
				PaymentSourceCombo.setSelectedItem("Bank");
			else
				PaymentSourceCombo.setSelectedItem("Cash");
		}

		if (entity().getBankAccount()!=null){
			//PaymentSourceCombo.setSelectedItem("Bank");
			AccountComp.setObject(entity().getBankAccount());
		}else if (entity().getCashAccount()!=null){
			//PaymentSourceCombo.setSelectedItem("Cash");
			AccountComp.setObject(entity().getCashAccount());
		}else{
			AccountComp.setObject(null);
		}

		ChequeNoText.setText(entity().getChequeno());
		ChequeDueDateDate.setDate(entity().getChequeDueDate());
		PayToComp.setText(entity().getPayTo());
		RefNoText.setText(entity().getReferenceNo());
		if (entity().getTransactionDate() != null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else {
			TransactionDateDate.setDate(new Date());
		}
		if (entity().getProject() != null)
			ProjectCodeComp.setObject(entity().getProject());

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

		if (entity().getExchangeRate() > 0)
			ExchRateText.setValue(new Double(entity().getExchangeRate()));// ));//entity().getExchangeRate());
		else
			ExchRateText.setValue(new Double(1));

		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if (entity().getSubmitDate() != null)
			SubmittedDateText.setText(dateformat.format(entity()
					.getSubmitDate()));
		else
			SubmittedDateText.setText("");
		LoadProjectDetail(entity().getIndex());
	}

	protected StateTemplateEntity currentEntity() {
		return entity();
	}

	PmtProjectCost entity() {
		return m_entity;
	}

	void setEntity(Object m_entity) {
		PmtProjectCost oldEntity = this.m_entity;
		if (oldEntity != null) {
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PmtProjectCost) m_entity;
		this.m_entity.addPropertyChangeListener(this);
		System.out.println("Ini status awal booo" + entity().getStatus());
		readEntityState();
	}

	protected Object createNew() {
		PmtProjectCost a = new PmtProjectCost();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a;
	}

	protected void doSave() {
		if (!cekValidity())
			return;
		super.doSave();
		saveDetail();
		entity2gui();
		disableEditMode();
		if (!m_saveBtn.isEnabled())
			setEnableButtonBawah(false);
	}

	private void saveDetail() {
		AccountingSQLSAP sql = new AccountingSQLSAP();
		GenericMapper mapper2 = MasterMap.obtainMapperFor(PmtProjectCostDetail.class);
		mapper2.setActiveConn(m_conn);
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")) {
				long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_PROJECT_COST, m_conn);
				entity().setIndex(index);
			}
			if (entity().getProjectDetail() != null) {
				PmtProjectCostDetail temp[] = entity().getProjectDetail();
				mapper2.doDeleteByColumn(IDBConstants.ATTR_PMT_PROJECT_COST,new Long(entity().getIndex()).toString());
				for (int i = 0; i < temp.length; i++) {
					temp[i].setPmtProjectCost((PmtProjectCost) entity());
					mapper2.doInsert(temp[i]);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void isiDefaultAssignPanel() {
		OriginatorComp.m_jobTextField
		.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField
		.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField
		.setText(getEmployeeJobTitle(defaultReceived));
		OriginatorComp.setDate(new Date());
		ApprovedComp.setDate(new Date());
		ReceivedComp.setDate(new Date());
	}

	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_PROJECT_COST,
				Signature.SIGN_ORIGINATOR);
		if (sign != null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_PROJECT_COST,
				Signature.SIGN_APPROVED);
		if (sign != null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_PROJECT_COST,
				Signature.SIGN_RECEIVED);
		if (sign != null)
			defaultReceived = sign.getFullEmployee();
	}

	protected void clearForm() {
		WorkDescText.setText("");
		ProjectCodeComp.setObject(null);
		m_table.clearTable();
		clearTextField(jPanel1_2_2_1);
		clearTextField(jPanel1_2_2);

	}

	protected void clearTextField(JPanel temppanel) {
		Component[] componentList = temppanel.getComponents();
		JTextField temptext;
		for (int i = 0; i < componentList.length; i++) {
			if (componentList[i] instanceof JTextField) {
				temptext = (JTextField) componentList[i];
				temptext.setText("");
			}
		}
	}

	public void setEnableButtonBawah(boolean bool) {
		AddRcvTypeAccountBtn.setEnabled(bool);
		DeleteRcvTypeAccountBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}

	public void initTabel() {
		m_table = new BottomTablePmtProject();
		ProjectCostAccountSrollPane.setViewportView(m_table);
	}

	public void clearKomponen() {
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
		ExchRateText.setValue(new Double(0));
		TransactionDateDate.setDate(null);
	}

	public void clearAll() {
		super.clearAll();
		clearForm();
		clearKomponen();
		disableEditMode();
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

	private void SetCashAccountToTable(PmtProjectCostDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCompanyLoanToTable(PmtProjectCostDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetProjectToTable(PmtProjectCostDetail detail, Vector temp1, ProjectBusinessLogic  proLogic) {
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
	

	private void SetBankAccountToTable(PmtProjectCostDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
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

	private void SetCustomerToTable(PmtProjectCostDetail detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Customer cust = null;
		cust = proLogic.getCustomerByIndex(detail.getSubsidiAry());
		temp1.addElement(cust);
	}

	private void SetPartnerToTable(PmtProjectCostDetail detail, Vector temp1, ProjectBusinessLogic proLogic) {
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

	private void SetEmployeeToTable(PmtProjectCostDetail detail, Vector temp1, HRMBusinessLogic hrmLogic) {
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

	protected class MyTableModelListener implements TableModelListener {
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			Object obj = e.getSource();
			if (obj instanceof DefaultTableModel) {
				DefaultTableModel model = (DefaultTableModel) obj;
				int maxRow = model.getRowCount();

				if (((row > -1) && (row < (maxRow - 1))) && ((col == 4) || (col==5))) {
					String nil= ((String)m_nonDeleteList.get(row)).toString();
					if (nil.equals("0"))
						m_table.updateSummary();
				}
			}
		}
	}

	int m_lastRow;

	class BottomTablePmtProject extends JTable {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		BottomTabblePmtProjectModel model = new BottomTabblePmtProjectModel();

		ArrayList listData = new ArrayList();
		double total = 0;

		public BottomTablePmtProject() {
			model.addColumn("No");
			model.addColumn("Account");
			model.addColumn("Description");
			model.addColumn("Subsidiary");
			model.addColumn("Debit");
			model.addColumn("Credit");
			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(40);
			getColumnModel().getColumn(0).setMaxWidth(40);

			clearTable();

			getModel().addTableModelListener(new MyTableModelListener());
		}

		public int getPosisiNonDelete(){
			int pos = -1;
			for (int i=0;i<m_nonDeleteList.size();i++){
				String nil= ((String)m_nonDeleteList.get(i)).toString();
				if (nil.equals("1"))
					pos=i;
			}
			return pos;
		}

		public void updateSummary() {
			int maxRow = getRowCount() - 1;
			int maxTotalRow = maxRow - 2;
			double debet = 0;
			double totDebet = 0;
			double credit = 0;
			double totCredit = 0;
			double ttl=0;

			int pos=-1;
			for (int i = 0; i <= maxTotalRow; i++) {
				JournalStandardAccount jsAccount = (JournalStandardAccount)listData.get(i);
				if (jsAccount.isHidden())
					pos = getPosisiNonDelete();
				
				if (i!=pos){
					if (getValueAt(i, 4) != null) {
						debet = ((Double) getValueAt(i, 4)).doubleValue();
						totDebet += debet;
						ttl+=debet;
					}			
				}
				
				if (i!=pos){
					if (getValueAt(i, 5) != null) {
						credit = ((Double) getValueAt(i, 5)).doubleValue();
						totCredit +=credit;
						ttl-=credit;
					}
				}
			}
			setValueAt(new Double(totDebet), maxRow, 4);
			if (pos>=0){
				double balance=0;
				if (totDebet>totCredit)
					balance = totDebet-totCredit;									
				setValueAt(new Double(balance), pos, 5);
				setValueAt(new Double(totCredit+balance), maxRow, 5);
			}else{
				setValueAt(new Double(totCredit), maxRow, 5);
			}
			setTotal( ttl);
		}
		
		public void setTotal(double ttl) {			
			total=ttl;
		}
		public double getTotal() {			
			return total;
		}

		public TableCellEditor getCellEditor(int row, int col) {
			if (row < getRowCount() - 2) {
				String nil= ((String)m_nonDeleteList.get(row)).toString();
				if (nil.equals("0")){
					JournalStandardAccount jsAccount = (JournalStandardAccount)listData.get(row);
					if (col == 2)
						return new BaseTableCellEditor();
					else if ( col == 4 && jsAccount.getBalance()==0)
						return new FormattedDoubleCellEditor(JLabel.RIGHT);
					else if ( col == 5 && jsAccount.getBalance()==1)
						return new FormattedDoubleCellEditor(JLabel.RIGHT);

				}
			}
			if (col==3){
				JournalStandardAccount jsAccount = (JournalStandardAccount)listData.get(row);
				Account acc = jsAccount.getAccount();
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
				}else
					return null;
			}
			return null;
		}

		public TableCellRenderer getCellRenderer(int row, int column) {
			if (column == 0)
				return new BaseTableCellRenderer();
			if (column > 0) {
				int maxRow = getRowCount() - 1;
				if(row>=(maxRow-1) &&(row<=maxRow)){
					if (column == 2)
						return new FormattedStandardCellRenderer(Font.BOLD,
								JLabel.RIGHT);
					else if (column == 4 || column == 5 )
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,
								Font.BOLD);
				} else {
					JournalStandardAccount jsAccount = (JournalStandardAccount)listData.get(row);
					if (column == 2)
						return new FormattedStandardCellRenderer(Font.PLAIN,
								JLabel.LEFT);
					else if (column == 4){
						if (jsAccount.getBalance()==0)
							return new FormattedDoubleCellRenderer(JLabel.RIGHT,Font.BOLD);
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT,Font.PLAIN);
					}else if (column == 5){
						if (jsAccount.getBalance()==1)
							return new FormattedDoubleCellRenderer(JLabel.RIGHT,Font.BOLD);
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT,Font.PLAIN);
					}
				}
			}
			return new FormattedStandardCellRenderer(Font.PLAIN, JLabel.LEFT);
		}

		public void clearTable() {
			listData.clear();
			m_nonDeleteList.clear();
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
			model.addRow(new Object[] { null, null, null,null, null,null });
			model.addRow(new Object[] { null, null, null,"TOTAL", new Double(0),new Double(0) });
			m_lastRow = 0;
		}

		public void addData(Vector obj, JournalStandardAccount jsAccount, int insertRow) {
			listData.add(jsAccount);
			model.insertRow(insertRow, obj);
			updateSummary();
			updateNumbering();
		}

		public void insertData(Vector obj,JournalStandardAccount jsAccount,int insertRow){
			listData.add(insertRow,jsAccount);
			model.insertRow(insertRow,obj);
			updateSummary();
			updateNumbering();
		}

		private void updateNumbering() {
			for(int i=0; i<getRowCount()-2; i++){
				setValueAt(new Integer(i+1), i, 0);
			}
		}

		public void removeData(int row) {
			listData.remove(row);
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);
			m_lastRow--;
			updateSummary();
			updateNumbering();
		}

		public boolean cekTotalDebitCredit(){
			int maxRow = getRowCount()-1;
			if (getValueAt(maxRow,4).toString().equals(getValueAt(maxRow,5).toString()))
				return true;
			return false;
		}

		public int getDataCount() {
			return listData.size();
		}

		public ArrayList getListData() {
			return listData;
		}

		public void stopCellEditing() {
			TableCellEditor editor;
			if ((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}

	}

	class BottomTabblePmtProjectModel extends DefaultTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		//int maxRow = getRowCount();

		public boolean isCellEditable(int row, int col) {
			int maxRow = getRowCount();
			if( col == 0 || col==1 || row > (maxRow-3))
				return false;
			return true;
		}
	}

	protected void tableStopCellEditing() {
		m_table.stopCellEditing();
	}

	private LookupPicker AccountComp;
	private javax.swing.JLabel AccountLabel;
	private javax.swing.JLabel ActivityCodeLabel;
	private javax.swing.JTextField ActivityCodeText;
	private javax.swing.JButton AddRcvTypeAccountBtn;
	private DatePicker ChequeDueDateDate;
	private javax.swing.JLabel ChequeDueDateLabel;
	private javax.swing.JLabel ChequeNoLabel;
	private javax.swing.JTextField ChequeNoText;
	private javax.swing.JTextField ContractNoText;
	private CurrencyPicker CurrComp;
	private javax.swing.JLabel CurrencyLabel;
	private javax.swing.JLabel CustomerLabel;
	private javax.swing.JTextField CustomerText;
	private javax.swing.JButton DeleteRcvTypeAccountBtn;
	private javax.swing.JLabel DepartmentLabel;
	private javax.swing.JTextField DepartmentText;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JLabel ExchRateLabel1;
	private javax.swing.JFormattedTextField ExchRateText;
	private javax.swing.JLabel IPCNoLabel;
	private javax.swing.JTextField IPCNoText;
	private javax.swing.JLabel ORNoLabel;
	private javax.swing.JTextField ORNoText;
	private javax.swing.JTextField PayToComp;
	private javax.swing.JLabel PayToLabel;
	private javax.swing.JComboBox PaymentSourceCombo;
	private ProjectDataPicker ProjectCodeComp;
	private javax.swing.JLabel ProjectCodeLabel;
	private javax.swing.JScrollPane ProjectCostAccountSrollPane;
	private BottomTablePmtProject m_table;
	private javax.swing.JLabel ReceiveToLabel;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScrollPane;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel SOPOContractNo;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JTextField SubmittedDateText;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JLabel UnitCodeLabel;
	private javax.swing.JTextField UnitCodeText;
	private javax.swing.JLabel VoucherDateLabel;
	private javax.swing.JLabel VoucherNoLabel;
	private javax.swing.JLabel WorkDescLabel;
	private javax.swing.JScrollPane WorkDescScrollPane;
	private javax.swing.JTextArea WorkDescText;
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
	private javax.swing.JPanel jPanel1_3_3;
	private javax.swing.JPanel jPanel1_3_4_1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel2_1;
	private javax.swing.JPanel BottomButtonPanel;
	private LookupAccountPicker1 ProjectAccountComp;
}
