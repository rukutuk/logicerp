/*
 * Panel1.java
 *
 * Created on February 8, 2007, 8:07 PM
 */

package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Color;
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
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.DoubleCellEditor;
import pohaci.gumunda.cgui.DoubleCellRenderer;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.Vchr_Salary;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryHoLoader;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21Ho;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21HoDet;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class PayrollTaxArt21HeadOfficePanel extends RevTransactionPanel
		implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private PayrollPmtTax21Ho m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	private buttomTabblePmtProject m_table;
	//private Unit unit;
	private UnitPicker m_unitpicker;
	
	public PayrollTaxArt21HeadOfficePanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initComponents();
		addingListener();
		initNumberFormats();
		disableEditMode();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid);
		setDefaultSignature();
		setEntity(new PayrollPmtTax21Ho());
		m_entityMapper = MasterMap.obtainMapperFor(PayrollPmtTax21Ho.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}

	private void initComponents() {
		setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid,
				IConstants.PAYROLL_PAYMENT_TAX21_HO));
		OriginatorComp = new AssignPanel(m_conn, m_sessionid, "Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid, "Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid, "Received by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon(
				"../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon(
				"../images/filter.gif"));
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon(
				"../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon(
				"../images/filter.gif"));
		java.awt.GridBagConstraints gridBagConstraints;
		m_unitpicker = new UnitPicker(m_conn,m_sessionid);
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
		jPanel1_2_2_1 = new javax.swing.JPanel();
		VoucherNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		VoucherDateLabel = new javax.swing.JLabel();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScroll = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarkLabel = new javax.swing.JLabel();
		RemarkScroll = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_2_1 = new javax.swing.JPanel();
		StatusLabel = new javax.swing.JLabel();
		SubmittedDateLabel = new javax.swing.JLabel();
		PaymentSourceLabel = new javax.swing.JLabel();
		AccountLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		UnitCodeLabel = new javax.swing.JLabel();
		PayToLabel = new javax.swing.JLabel();
		ChequeNoLabel = new javax.swing.JLabel();
		ChequeDueDateLabel = new javax.swing.JLabel();
		SubmittedDateText = new javax.swing.JTextField();
		//UnitCodeText = new javax.swing.JTextField();
		PaytoText = new javax.swing.JTextField();
		ChequeNoText = new javax.swing.JTextField();
		ChequeDueDateDate = new pohaci.gumunda.titis.application.DatePicker();
		PaymentSourceCombo = new javax.swing.JComboBox();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jPanel2_1 = new javax.swing.JPanel();
		BottomButtonPanel = new javax.swing.JPanel();
		AddTax21ComponentBtn = new javax.swing.JButton();
		DeleteTax21ComponentBtn = new javax.swing.JButton();
		// SaveTax21ComponentBtn = new javax.swing.JButton();
		Tax21PayableScrollPane = new javax.swing.JScrollPane();
		// Tax21PayableTable = new javax.swing.JTable();

		setLayout(new java.awt.BorderLayout());

		setPreferredSize(new java.awt.Dimension(700, 500));
		jPanel1.setLayout(new java.awt.BorderLayout());

		jPanel1.setPreferredSize(new java.awt.Dimension(650, 300));
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
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());

		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(325, 305));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(325, 200));
		VoucherNoLabel.setText("Voucher No");
		VoucherNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(VoucherNoLabel, gridBagConstraints);

		RefNoText.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);

		VoucherDateLabel.setText("Voucher Date*");
		VoucherDateLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(VoucherDateLabel, gridBagConstraints);

		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(DescriptionLabel, gridBagConstraints);

		DescriptionScroll.setPreferredSize(new java.awt.Dimension(200, 63));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(3);
		DescriptionScroll.setViewportView(DescriptionTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(DescriptionScroll, gridBagConstraints);

		RemarkLabel.setText("Remarks");
		RemarkLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(RemarkLabel, gridBagConstraints);

		RemarkScroll.setPreferredSize(new java.awt.Dimension(200, 63));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(3);
		RemarkScroll.setViewportView(RemarksTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(RemarkScroll, gridBagConstraints);

		TransactionDateDate.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(TransactionDateDate, gridBagConstraints);

		jPanel1_2_2.add(jPanel1_2_2_1, java.awt.BorderLayout.WEST);

		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);

		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(350, 200));
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

		PaymentSourceLabel.setText("Payment Source*");
		PaymentSourceLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PaymentSourceLabel, gridBagConstraints);

		AccountLabel.setText("Account");
		AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountLabel, gridBagConstraints);

		StatusText.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(StatusText, gridBagConstraints);

		UnitCodeLabel.setText("Unit Code*");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(UnitCodeLabel, gridBagConstraints);

		PayToLabel.setText("Pay to");
		PayToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PayToLabel, gridBagConstraints);

		ChequeNoLabel.setText("Cheque No.");
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

		SubmittedDateText.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(SubmittedDateText, gridBagConstraints);

		accountComp().setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(accountComp(), gridBagConstraints);

		/*UnitCodeText.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(UnitCodeText, gridBagConstraints);
*/
		m_unitpicker.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(m_unitpicker, gridBagConstraints);

		PaytoText.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PaytoText, gridBagConstraints);

		ChequeNoText.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeNoText, gridBagConstraints);

		ChequeDueDateDate.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_1.add(ChequeDueDateDate, gridBagConstraints);

		PaymentSourceCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Cash", "Bank" }));
		PaymentSourceCombo.setPreferredSize(new java.awt.Dimension(200, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PaymentSourceCombo, gridBagConstraints);

		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);

		jPanel1.add(jPanel1_2, java.awt.BorderLayout.CENTER);

		jPanel1_3.setLayout(new java.awt.BorderLayout());

		jPanel1_3.setPreferredSize(new java.awt.Dimension(700, 90));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());

		OriginatorComp.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(279, 110));
		jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);

		jPanel1_3_2.setLayout(new java.awt.BorderLayout());

		ApprovedComp.setLayout(new java.awt.GridBagLayout());

		ApprovedComp.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(279, 110));
		jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);

		jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());

		ReceivedComp.setLayout(new java.awt.GridBagLayout());

		ReceivedComp.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(279, 110));
		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);

		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);

		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);

		add(jPanel1, java.awt.BorderLayout.NORTH);

		jPanel2.setLayout(new java.awt.BorderLayout());

		jPanel2.setBorder(javax.swing.BorderFactory
				.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		jPanel2.setPreferredSize(new java.awt.Dimension(650, 250));
		jPanel2_1.setLayout(new java.awt.BorderLayout());

		jPanel2_1.setPreferredSize(new java.awt.Dimension(650, 35));
		BottomButtonPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.LEFT, 3, 5));

		BottomButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
		AddTax21ComponentBtn.setText("Add");
		AddTax21ComponentBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		AddTax21ComponentBtn.setPreferredSize(new java.awt.Dimension(50, 21));
		BottomButtonPanel.add(AddTax21ComponentBtn);

		DeleteTax21ComponentBtn.setText("Delete");
		DeleteTax21ComponentBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		DeleteTax21ComponentBtn
				.setPreferredSize(new java.awt.Dimension(50, 21));
		BottomButtonPanel.add(DeleteTax21ComponentBtn);

		/*
		 * SaveTax21ComponentBtn.setText("Save");
		 * SaveTax21ComponentBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		 * SaveTax21ComponentBtn.setPreferredSize(new java.awt.Dimension(50,
		 * 21)); BottomButtonPanel.add(SaveTax21ComponentBtn);
		 */

		jPanel2_1.add(BottomButtonPanel, java.awt.BorderLayout.WEST);

		jPanel2.add(jPanel2_1, java.awt.BorderLayout.NORTH);

		Tax21PayableScrollPane
				.setPreferredSize(new java.awt.Dimension(650, 215));
		/*
		 * Tax21PayableTable.setModel(new javax.swing.table.DefaultTableModel(
		 * new Object [][] {}, new String [] { "No", "Date", "Description",
		 * "Account","Ammount" } ));
		 */
		m_table = new buttomTabblePmtProject();
		Tax21PayableScrollPane.setViewportView(m_table);

		jPanel2.add(Tax21PayableScrollPane, java.awt.BorderLayout.CENTER);

		add(jPanel2, java.awt.BorderLayout.CENTER);

	}// </editor-fold>//GEN-END:initComponents

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
		/*accountComp().ad*/
		//accountComp().addPropertyChangeListener("object", this);
		m_unitpicker.addPropertyChangeListener("object",this);
		AddTax21ComponentBtn.addActionListener(this);
		DeleteTax21ComponentBtn.addActionListener(this);
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
		TransactionDateDate.setDate(null);
	}

	protected void clearAll() {
		super.clearAll();
		clearKomponen();
		disableEditMode();
		m_table.clearTable();
	}

	boolean cekaccount = false;
	protected void doNew() {
		cekaccount = true;
		super.doNew();
		isiDefaultAssignPanel();
		setEnableButtonBawah(true);
		m_table.clearTable();
	}

	public void doEdit() {
		super.doEdit();
		setEnableButtonBawah(true);
	}

	protected void doSubmit() {
		ArrayList validityMsgs = new ArrayList();
		if (AccountComp.getObject() == null)
			validityMsgs.add("Source account must be selected");
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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == PaymentSourceCombo) {
			revalidateAccount();
		} else if (e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0){
				new Vchr_Salary(m_entity,m_conn, m_sessionid);
			}else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		} else if (e.getSource() == m_searchRefNoBtn) {
			SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame
					.getMainFrame(), "Search Voucher", m_conn, m_sessionid,
					new PayrollPmtSlryHoLoader(m_conn, PayrollPmtTax21Ho.class));
			dlg.setVisible(true);
			if (dlg.selectedObj != null) {
				doLoad(dlg.selectedObj);
				LoadDetail(entity().getIndex());
			}
		} else if (e.getSource() == AddTax21ComponentBtn) {
			addTaxArt21Payable();
		} else if (e.getSource() == DeleteTax21ComponentBtn) {
			if (JOptionPane.showConfirmDialog(this, "Are you sure?",
					"Delete Confirmation Dialog", JOptionPane.YES_NO_OPTION) == 0) {
				int row = m_table.getSelectedRow();
				int maxRow = m_table.getRowCount() - 1;
				if (row > -1) {
					if (row < (maxRow - 1)) {
						m_table.removeData(row);
						m_table.updateSummary();
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"please select account you want to remove");
				}
			} else {
				return;
			}
		}
	}

	private void revalidateAccount() {
		String receive = (String) PaymentSourceCombo.getSelectedItem();
		System.out.println(receive);
		jPanel1_2_1.remove(accountComp());
		jPanel1_2_1.revalidate();
		jPanel1_2_1.repaint();
		if (receive.equals("Cash"))
			setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid,m_unitpicker.getUnit(),
					IConstants.PAYROLL_PAYMENT_TAX21_HO));

		else
			setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid,m_unitpicker.getUnit(),
					IConstants.PAYROLL_PAYMENT_TAX21_HO));

		accountComp().setPreferredSize(new java.awt.Dimension(200, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(accountComp(), gridBagConstraints);
		jPanel1_2_1.revalidate();
	}

	private void addTaxArt21Payable() {
		if (m_unitpicker.getUnit() == null) {
			JOptionPane.showMessageDialog(this,
					"Please select unit");
			return;
		}
		payrollTax21HoComp = new LookupTaxArt21PayablePicker(m_conn,m_sessionid, m_unitpicker.getUnit());
		payrollTax21HoComp.done();
		if (payrollTax21HoComp.getObject() != null) {
			int oldRow = m_lastRow;
			m_lastRow++;
			Vector temp1 = new Vector();
			TransactionDetail transDet = (TransactionDetail) payrollTax21HoComp.getObject();
			temp1.addElement(new Integer(m_lastRow));
			temp1.addElement(transDet.getTransaction().getPostedDate());
			temp1.addElement(transDet.getTransaction().getDescription());
			temp1.addElement(transDet.getAccount().getCode());
			temp1.addElement(new Double(transDet.getValue()));
			m_table.addData(temp1, transDet, oldRow);
		}
	}

	public void LoadDetail(long index) {
		GenericMapper mapper2 = MasterMap
				.obtainMapperFor(PayrollPmtTax21HoDet.class);
		mapper2.setActiveConn(m_conn);
		String value = new Long(index).toString();
		List detailList = mapper2
				.doSelectWhere(IDBConstants.ATTR_PAYROLL_PMT_TAX21_HO + "="
						+ value);
		m_table.clearTable();
		PayrollPmtTax21HoDet detail;
		Vector temp1;
		Account acc;
		for (int i = 0; i < detailList.size(); i++) {
			int oldRow = m_lastRow;
			m_lastRow++;
			temp1 = new Vector();
			detail = (PayrollPmtTax21HoDet) detailList.get(i);
			acc = (Account) detail.getAccount();
			TransactionDetail transactionDetail = getTransactionDetail(detail);
			Transaction transaction = detail.getTrans();
			transaction.setPostedDate(detail.getPostedDate());
			transaction.setDescription(detail.getDescription());
			transactionDetail.setTransaction(transaction);

			temp1.addElement(new Integer(i + 1));
			temp1.addElement(detail.getPostedDate());
			temp1.addElement(detail.getDescription());
			temp1.addElement(acc.getCode());
			temp1.addElement(new Double(detail.getAccValue()));
			m_table.addData(temp1, transactionDetail, oldRow);
		}
	}

	private TransactionDetail getTransactionDetail(PayrollPmtTax21HoDet detail) {
		GenericMapper mapper = MasterMap
				.obtainMapperFor(TransactionDetail.class);
		mapper.setActiveConn(m_conn);

		List list = mapper.doSelectWhere("TRANS="
				+ detail.getTrans().getIndex() + " AND ACCOUNT="
				+ detail.getAccount().getIndex());
		if (list.size() > 0)
			return (TransactionDetail) list.get(0);

		return null;
	}

	private void setEnableButtonBawah(boolean bool) {
		AddTax21ComponentBtn.setEnabled(bool);
		DeleteTax21ComponentBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}

	private void isiDefaultAssignPanel() {
		OriginatorComp.m_jobTextField
				.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField
				.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField
				.setText(getEmployeeJobTitle(defaultReceived));
	}

	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn,
				IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
				Signature.SIGN_ORIGINATOR);
		if (sign != null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn,
				IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
				Signature.SIGN_APPROVED);
		if (sign != null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn,
				IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
				Signature.SIGN_RECEIVED);
		if (sign != null)
			defaultReceived = sign.getFullEmployee();
	}

	protected StateTemplateEntity currentEntity() {
		return entity();
	}

	protected boolean cekValidity() {
		ArrayList validityMsgs = new ArrayList();
		if (TransactionDateDate.getDate() == null)
			validityMsgs.add("Transaction date must be selected");
		if (m_unitpicker.getUnit()==null)
			validityMsgs.add("Unit must be selected");
		/*if (AccountComp.getObject() == null)
			validityMsgs.add("Source account must be selected");*/
		if (DescriptionTextArea.getText().equals(""))
			validityMsgs.add("Description must be selected");
		detailAccountOperation();
		PayrollPmtTax21HoDet[] temp = entity().getPayrollPmtTax21HoDet();
		if (temp.length == 0)
			validityMsgs.add("Account Detail must be filled");

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

	protected void gui2entity() {
		String paySource = (String) PaymentSourceCombo.getSelectedItem();
		entity().setPaymentSource(paySource);
		Object objacc = AccountComp.getObject();
		if (objacc instanceof BankAccount) {
			BankAccount bank = (BankAccount) objacc;
			entity().setBankAccount(bank);
			entity().setCashAccount(null);			
		}
		else if (objacc instanceof CashAccount) {
			CashAccount cash = (CashAccount) objacc;
			entity().setCashAccount(cash);
			entity().setBankAccount(null);			
		}
		
		Object objunit = m_unitpicker.getObject();
		if (objunit instanceof Unit) {
			Unit unit = (Unit) objunit;
			entity().setUnit(unit);			
		}
		
		entity().setChequeNo(ChequeNoText.getText());
		entity().setPayto(PaytoText.getText());
		entity().setChequeDueDate(ChequeDueDateDate.getDate());
		entity().setTransactionDate(TransactionDateDate.getDate());
		entity().setRemarks(RemarksTextArea.getText());

		entity().transTemplateRead(this.OriginatorComp, this.ApprovedComp,
				this.ReceivedComp, this.RefNoText, this.DescriptionTextArea);
		entity().setCurrency(baseCurrency);
		entity().setExchangeRate(1);
		entity().setPayrollPmtTax21HoDet(detailAccountOperation());
	}

	private PayrollPmtTax21HoDet[] detailAccountOperation() {
		PayrollPmtTax21HoDet[] temp = new PayrollPmtTax21HoDet[m_table
				.getDataCount()];
		if (m_table.getRowCount() >= 0) {
			for (int i = 0; i < m_table.getDataCount(); i++) {
				temp[i] = new PayrollPmtTax21HoDet();
				TransactionDetail akun = (TransactionDetail) m_table
						.getListData().get(i);
				temp[i].setAccount(akun.getAccount());
				Object obj = m_table.getValueAt(i, 4);

				temp[i].setAccValue(((Double) obj).doubleValue());
				temp[i].setCurrency(entity().getCurrency());
				temp[i].setExchangeRate(entity().getExchangeRate());
				temp[i].setDescription(akun.getTransaction().getDescription());
				temp[i].setPostedDate(akun.getTransaction().getPostedDate());
				temp[i].setTrans(akun.getTransaction());
			}
			entity().setPayrollPmtTax21HoDet(temp);
		}
		return temp;
	}

	protected void entity2gui() {
		
		if (entity().getUnit()!=null)
			m_unitpicker.setObject(entity().getUnit());
		else 
			m_unitpicker.setObject(null);
		
		if (entity().isSourceBank()) {
			PaymentSourceCombo.setSelectedItem("Bank");
			AccountComp.setObject(entity().getBankAccount());
		} else {
			PaymentSourceCombo.setSelectedItem("Cash");
			AccountComp.setObject(entity().getCashAccount());
		}
		PaytoText.setText(entity().getPayto());
		ChequeNoText.setText(entity().getChequeNo());
		ChequeDueDateDate.setDate(entity().getChequeDueDate());
		TransactionDateDate.setDate(entity().getTransactionDate());
		RefNoText.setText(entity().getReferenceNo());
		
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
		if (entity().getSubmitDate() != null)
			SubmittedDateText.setText(dateformat.format(entity()
					.getSubmitDate()));
		else
			SubmittedDateText.setText("");

		if (entity().getTransactionDate() != null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else {
			TransactionDateDate.setDate(new Date());
		}
	}

	protected void disableEditMode() {
		StatusText.setEditable(false);
		SubmittedDateText.setEditable(false);
		PaymentSourceCombo.setEnabled(false);
		AccountComp.setEnabled(false);
		m_unitpicker.setEnabled(false);
		//UnitCodeText.setEditable(false);
		PaytoText.setEditable(false);
		ChequeNoText.setEditable(false);
		ChequeDueDateDate.setEditable(false);
		RefNoText.setEditable(false);
		TransactionDateDate.setEditable(false);
		DescriptionTextArea.setEditable(false);
		RemarksTextArea.setEditable(false);
		OriginatorComp.setEnabled(false);
		ApprovedComp.setEnabled(false);
		ReceivedComp.setEnabled(false);
		setEnableButtonBawah(false);
	}

	protected void enableEditMode() {
		PaymentSourceCombo.setEnabled(true);
		AccountComp.setEnabled(true);
		//UnitCodeText.setEditable(false);
		m_unitpicker.setEnabled(true);
		PaytoText.setEditable(true);
		ChequeNoText.setEditable(true);
		ChequeDueDateDate.setEditable(true);
		TransactionDateDate.setEditable(true);
		DescriptionTextArea.setEditable(true);
		RemarksTextArea.setEditable(true);
		OriginatorComp.setEnabled(true);
		ApprovedComp.setEnabled(true);
		ReceivedComp.setEnabled(true);
	}

	protected Object createNew() {
		PayrollPmtTax21Ho a = new PayrollPmtTax21Ho();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a;
	}

	protected void tableStopCellEditing() {
		m_table.stopCellEditing();
	}

	void setEntity(Object m_entity) {
		PayrollPmtTax21Ho oldEntity = this.m_entity;
		if (oldEntity != null) {
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PayrollPmtTax21Ho) m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())) {
			readEntityState();
		}
		if (evt.getSource()==m_unitpicker){
			revalidateAccount();
			m_table.clearTable();
		}
		/*if (evt.getSource() == accountComp()) {
			if (cekaccount){
				Object objunit = m_unitpicker.getObject();
				if (objunit==null)
					JOptionPane.showMessageDialog(this,"Please select unit");
				return;
			}
		}*/
			/*if (evt.getNewValue() == null) {
				UnitCodeText.setText("");
				unit = null;
			} else if (evt.getNewValue() instanceof BankAccount) {
				BankAccount b = (BankAccount) accountComp().getObject();
				UnitCodeText.setText(b.getUnit().getDescription());
				unit = b.getUnit();
			} else if (evt.getNewValue() instanceof CashAccount) {
				CashAccount b = (CashAccount) accountComp().getObject();
				UnitCodeText.setText(b.getUnit().getDescription());
				unit = b.getUnit();
			}*/
		
	}

	PayrollPmtTax21Ho entity() {
		return m_entity;
	}

	protected void setAccountComp(LookupPicker accountComp) {
		LookupPicker old = AccountComp;
		if (old != null) {
			old.removePropertyChangeListener("object", this);
		}
		AccountComp = accountComp;
		AccountComp.addPropertyChangeListener("object", this);
	}

	private LookupPicker accountComp() {
		return AccountComp;
	}

	protected void doSave() {
		if (!cekValidity())
			return;
		super.doSave();
		entityGetHisIndex();
		LoadDetail(entity().getIndex());
		if (!m_saveBtn.isEnabled())
			setEnableButtonBawah(false);
	}

	private void entityGetHisIndex() {
		AccountingSQLSAP sql = new AccountingSQLSAP();
		GenericMapper mapper2 = MasterMap
				.obtainMapperFor(PayrollPmtTax21HoDet.class);
		mapper2.setActiveConn(m_conn);
		try {
			if ((new Long(entity().getIndex()).toString().equalsIgnoreCase("0"))) {
				long index = sql.getMaxIndex(
						IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO, m_conn);
				entity().setIndex(index);
			}
			if (entity().getPayrollPmtTax21HoDet() != null) {
				PayrollPmtTax21HoDet[] temp = entity()
						.getPayrollPmtTax21HoDet();
				mapper2.doDeleteByColumn(
						IDBConstants.TABLE_PAYROLL_PMT_TAX21_HO, new Long(
								entity().getIndex()).toString());
				for (int i = 0; i < temp.length; i++) {
					temp[i].setPayrollPmtTax21Ho(entity());
					mapper2.doInsert(temp[i]);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected class MyTableModelListener implements TableModelListener {

		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			Object obj = e.getSource();
			if (obj instanceof DefaultTableModel) {
				DefaultTableModel model = (DefaultTableModel) obj;
				int maxRow = model.getRowCount();
				if (((row > -1) && (row < (maxRow - 1)))
						&& ((col == 3) || (col == 4))) {
					m_table.updateSummary();
				}
			}
		}
	}

	int m_lastRow;

	class buttomTabblePmtProject extends JTable {
		private static final long serialVersionUID = 1L;

		buttomTabbleRcvOthersModel model = new buttomTabbleRcvOthersModel();

		ArrayList listData = new ArrayList();

		protected buttomTabblePmtProject() {
			model.addColumn("No");
			model.addColumn("Date");
			model.addColumn("Description");
			model.addColumn("Account");
			model.addColumn("Amount");
			setModel(model);

			getColumnModel().getColumn(0).setPreferredWidth(40);
			getColumnModel().getColumn(0).setMaxWidth(40);
			clearTable();

			getModel().addTableModelListener(new MyTableModelListener());
		}

		public void updateSummary() {
			int maxRow = getRowCount() - 1;
			int maxTotalRow = maxRow - 2;

			/*
			 * Number debit = new Double(0); double totDebit = 0;
			 */

			double credit = 0;
			double totCredit = 0;

			for (int i = 0; i <= maxTotalRow; i++) {
				if (getValueAt(i, 4) != null) {
					credit = ((Double) getValueAt(i, 4)).doubleValue();
				}
				totCredit += credit;
			}

			/*
			 * for(int i=0; i<=maxTotalRow; i++){ if (getValueAt(i, 3)!=null){
			 * try { debit =
			 * (Number)m_numberFormatter1.stringToValue(getValueAt(i,
			 * 3).toString()); } catch (ParseException e) { e.printStackTrace(); } }
			 * totDebit += debit.doubleValue(); }
			 */
			setValueAt(new Double(totCredit), maxRow, 4);
			// setValueAt(new Double(totDebit), maxRow, 3);
		}

		public void updateCurrency(Currency curr) {
			for (int i = 0; i < getRowCount(); i++) {
				if (i != (getRowCount() - 2))
					setValueAt(new String(curr.toString()), i, 5);
			}
		}

		public boolean cekDebitCredit() {
			int maxRow = getRowCount() - 1;
			System.err.println(getValueAt(maxRow, 3).toString() + " = "
					+ getValueAt(maxRow, 4).toString());
			if (getValueAt(maxRow, 3).toString().equals(
					getValueAt(maxRow, 4).toString()))
				return true;
			return false;
		}

		public TableCellEditor getCellEditor(int row, int col) {
			// System.err.println("row :" + row + " ," + (getRowCount()-3));
			if (row <= getRowCount() - 3) {
				/*
				 * if (col == 3 &&
				 * ((Account)listData.get(row)).getBalanceAsString().equalsIgnoreCase("Debit"))
				 * return new DoubleCellEditor(); else
				 */
				if (col == 4)
					return new DoubleCellEditor();
			}

			return null;
		}

		public TableCellRenderer getCellRenderer(int row, int column) {
			if (column == 0)
				return new BaseTableCellRenderer();
			if (column > 0) {
				int maxRow = getRowCount() - 1;

				if (row >= (maxRow - 1) && (row <= maxRow)) {
					if (column == 2)
						return new StandardFormatCellRenderer(Font.BOLD,
								JLabel.RIGHT);
					if (column == 3)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,
								Font.BOLD);
					if (column == 4)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,
								Font.BOLD);
				} else {
					if (column == 3)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,
								Font.PLAIN);
					if (column == 4)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,
								Font.PLAIN);
				}
			}
			return new StandardFormatCellRenderer(Font.PLAIN, JLabel.CENTER);
		}

		public void clearTable() {
			listData.clear();
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
			model.addRow(new Object[] { null, null, null, null, null });
			model.addRow(new Object[] { null, null, null, "TOTAL",
					new Double(0) });
			m_lastRow = 0;
		}

		public void addData(Vector obj, Object det, int insertRow) {
			listData.add(det);
			model.insertRow(insertRow, obj);
			/*
			 * if (CurrComp.getObject()!=null)
			 * updateCurrency((Currency)CurrComp.getObject());
			 */
			updateSummary();
			updateNumbering();
		}

		public void removeData(int row) {
			listData.remove(row);
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.removeRow(row);
			m_lastRow--;
			updateNumbering();
		}

		private void updateNumbering() {
			for (int i = 0; i < m_lastRow; i++) {
				Integer val = (Integer) getValueAt(i, 0);
				if (val.intValue() != i) {
					setValueAt(new Integer(i + 1), i, 0);
				}
			}
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

	protected class FormattedDoubleCellRenderer extends DoubleCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int fontStyle = Font.PLAIN;

		private Color color = Color.BLACK;

		public FormattedDoubleCellRenderer(int alignment, int fontStyle) {
			super(alignment);
			this.fontStyle = fontStyle;
		}

		public FormattedDoubleCellRenderer(int alignment, int fontStyle,
				Color color) {
			super(alignment);
			this.fontStyle = fontStyle;
			this.color = color;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			super.getTableCellRendererComponent(table, value, false, hasFocus,
					row, column);

			Font font = getFont();
			setForeground(this.color);
			setFont(font.deriveFont(this.fontStyle));

			return this;
		}
	}

	protected class StandardFormatCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
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

	/**
	 * 
	 */
	int m_iRowIndex = -1;

	class buttomTabbleRcvOthersModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int col) {
			int maxRow = getRowCount();
			if ((col == 0 || col == 1 || col == 2) || row > (maxRow - 3))
				return false;
			return true;
		}
	}

	private LookupPicker AccountComp;

	private javax.swing.JLabel AccountLabel;

	private pohaci.gumunda.titis.application.DatePicker ChequeDueDateDate;

	private javax.swing.JLabel ChequeDueDateLabel;

	private javax.swing.JLabel ChequeNoLabel;

	private javax.swing.JTextField ChequeNoText;

	private javax.swing.JLabel DescriptionLabel;

	private javax.swing.JScrollPane DescriptionScroll;

	private javax.swing.JTextArea DescriptionTextArea;

	private javax.swing.JLabel PayToLabel;

	private javax.swing.JComboBox PaymentSourceCombo;

	private javax.swing.JLabel PaymentSourceLabel;

	private javax.swing.JTextField PaytoText;

	private javax.swing.JTextField RefNoText;

	private javax.swing.JLabel RemarkLabel;

	private javax.swing.JScrollPane RemarkScroll;

	private javax.swing.JTextArea RemarksTextArea;

	private javax.swing.JLabel StatusLabel;

	private javax.swing.JTextField StatusText;

	private javax.swing.JLabel SubmittedDateLabel;

	private javax.swing.JTextField SubmittedDateText;

	private javax.swing.JPanel TopButtonPanel;

	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;

	private javax.swing.JLabel UnitCodeLabel;

	//private javax.swing.JTextField UnitCodeText;

	private javax.swing.JLabel VoucherDateLabel;

	private javax.swing.JLabel VoucherNoLabel;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JPanel jPanel1_1;

	private javax.swing.JPanel jPanel1_2;

	private javax.swing.JPanel jPanel1_2_1;

	private javax.swing.JPanel jPanel1_2_2;

	private javax.swing.JPanel jPanel1_2_2_1;

	private javax.swing.JPanel jPanel1_3;

	private AssignPanel OriginatorComp;

	private javax.swing.JPanel jPanel1_3_2;

	private AssignPanel ApprovedComp;

	private javax.swing.JPanel jPanel1_3_2_2;

	private AssignPanel ReceivedComp;

	private javax.swing.JPanel jPanel2;

	private javax.swing.JPanel jPanel2_1;

	private javax.swing.JPanel BottomButtonPanel;

	private javax.swing.JScrollPane Tax21PayableScrollPane;

	//	private javax.swing.JTable Tax21PayableTable;
	private javax.swing.JButton AddTax21ComponentBtn;

	private javax.swing.JButton DeleteTax21ComponentBtn;

	private LookupTaxArt21PayablePicker payrollTax21HoComp;
	//private javax.swing.JButton SaveTax21ComponentBtn;
}
