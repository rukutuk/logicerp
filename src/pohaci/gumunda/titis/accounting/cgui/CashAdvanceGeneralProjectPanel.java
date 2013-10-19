package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pohaci.gumunda.titis.accounting.cgui.report.Vchr_CAProject;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.entity.UnitBankCashTransferLoader;
import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.hrm.cgui.*;
import pohaci.gumunda.titis.project.cgui.*;

public class CashAdvanceGeneralProjectPanel extends RevTransactionPanel
implements ActionListener, PropertyChangeListener, DocumentListener {
	private static final long serialVersionUID = 1L;
	
	JTextField m_statusTextField, m_submitTextField;
	
	JComboBox m_sourceComboBox;
	
	LookupPicker m_accountPicker;
	
	JTextField m_chequenoTextField;
	
	DatePicker m_chequedatePicker;
	
	CurrencyPicker m_currencyPicker;
	
	JFormattedTextField m_exchangeNumberField;
	
	EmployeePicker m_paytoPicker;
	
	JTextField m_jobTextField;
	
	JFormattedTextField m_amountNumbeField;
	
	AmountPanel m_baseAmount;
	
	// UnitPicker m_unitPicker;
	// OrganizationPicker m_departmentPicker;
	JTextArea m_descriptionTextArea;
	
	JTextField m_vouchernoTextField, m_customerTextField, m_unitTextField,
	m_activityTextField, m_departmentTextField, m_ornoTextField,
	m_contractTextField, m_ipcTextField;
	
	DatePicker m_voucherdatePicker;
	
	ProjectDataPicker m_projectPicker;
	
	JTextArea m_workTextArea, m_remarksTextArea;
	
	AssignPanel m_originatorAssign, m_approvedAssign, m_receivedAssign;
	
	boolean m_new = false, m_edit = false;
	
	private PmtCAProject m_entity;
	
	protected Employee defaultOriginator;
	
	protected Employee defaultApproved;
	
	protected Employee defaultReceived;
	
	private JPanel formleftPanel;
	
	// private JPanel formPanel;
	private JPanel buttonPanel;
	
	private JPanel formrightPanel;
	
	// private JPanel assignPanel;
	private JPanel currencyPanel;
	
	public CashAdvanceGeneralProjectPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		constructComponent();
		enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid);
		setDefaultSignature();
		setEntity(new PmtCAProject());
		m_entityMapper = MasterMap.obtainMapperFor(PmtCAProject.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	// Tambahan panel untuk perbaikan form
	private JPanel panelUtama = new JPanel();
	
	private JPanel panelAtas = new JPanel();// pake Border layout nich akan
	// berisi buttonpanel
	
	private JPanel panelTengah = new JPanel();
	
	private JPanel panelTengahKanan = new JPanel();
	
	private JPanel panelBawah = new JPanel();
	
	private JPanel panelBawahKiri = new JPanel();
	
	private JPanel panelBawahKanan = new JPanel();
	
	void constructComponent() {
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon(
		"../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon(
		"../images/filter.gif"));
		
		m_searchRefNoBtn.setPreferredSize(new java.awt.Dimension(21, 21));
		m_printViewRefNoBtn.setPreferredSize(new java.awt.Dimension(21, 21));
		
		m_newBtn = new JButton("New");
		m_submitBtn = new JButton("Submit");
		m_cancelBtn = new JButton("Cancel");
		m_saveBtn = new JButton("Save");
		m_deleteBtn = new JButton("Delete");
		m_editBtn = new JButton("Edit");
		
		m_statusTextField = new JTextField();
		m_statusTextField.setEditable(false);
		m_submitTextField = new JTextField();
		m_submitTextField.setEditable(false);
		m_sourceComboBox = new JComboBox();
		m_sourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Cash", "Bank" }));
		m_accountPicker = new LookupCashAccountPicker(m_conn, m_sessionid);
		m_chequenoTextField = new JTextField();
		m_chequedatePicker = new DatePicker();
		m_currencyPicker = new CurrencyPicker(m_conn, m_sessionid);
		m_exchangeNumberField = new javax.swing.JFormattedTextField(
				m_numberFormatter);
		m_paytoPicker = new EmployeePicker(m_conn, m_sessionid);
		m_jobTextField = new JTextField();
		m_amountNumbeField = new javax.swing.JFormattedTextField(
				m_numberFormatter);
		m_baseAmount = new AmountPanel();
		// m_unitPicker = new UnitPicker(m_conn, m_sessionid);
		// m_departmentPicker = new OrganizationPicker(m_conn, m_sessionid);
		m_descriptionTextArea = new JTextArea();
		
		m_vouchernoTextField = new JTextField();
		m_customerTextField = new JTextField();
		m_workTextArea = new JTextArea();
		m_unitTextField = new JTextField();
		m_activityTextField = new JTextField();
		m_departmentTextField = new JTextField();
		m_ornoTextField = new JTextField();
		m_contractTextField = new JTextField();
		m_ipcTextField = new JTextField();
		m_voucherdatePicker = new DatePicker();
		m_projectPicker = new ProjectDataPicker(m_conn, m_sessionid);
		m_remarksTextArea = new JTextArea();
		
		m_originatorAssign = new AssignPanel(m_conn, m_sessionid, "Originator");
		m_approvedAssign = new AssignPanel(m_conn, m_sessionid, "Approved by");
		m_receivedAssign = new AssignPanel(m_conn, m_sessionid, "Received by");
		
		buttonPanel = new JPanel();
		formleftPanel = new JPanel();
		formrightPanel = new JPanel();
		// formPanel = new JPanel();
		// assignPanel = new JPanel();
		currencyPanel = new JPanel();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(m_searchRefNoBtn);
		buttonPanel.add(m_printViewRefNoBtn);
		buttonPanel.add(m_newBtn);
		buttonPanel.add(m_editBtn);
		buttonPanel.add(m_deleteBtn);
		buttonPanel.add(m_saveBtn);
		buttonPanel.add(m_cancelBtn);
		buttonPanel.add(m_submitBtn);
		// Ini persiapan panel Utama
		setLayout(new java.awt.BorderLayout());
		
		setPreferredSize(new java.awt.Dimension(650, 430));
		panelUtama.setLayout(new java.awt.BorderLayout());
		
		panelUtama.setPreferredSize(new java.awt.Dimension(700, 500));
		
		// Pengaturan panel atas sekaligus penambahan panel button dan
		// pamasangan panelAtas ke panel utama
		panelAtas.setLayout(new java.awt.BorderLayout());
		panelAtas.setPreferredSize(new java.awt.Dimension(700, 30));
		panelAtas.add(buttonPanel, java.awt.BorderLayout.WEST);
		panelUtama.add(panelAtas, java.awt.BorderLayout.NORTH);
		// ==================================================
		// currencypanel
		currencyPanel.setLayout(new GridBagLayout());
		m_currencyPicker.setPreferredSize(new java.awt.Dimension(50, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		currencyPanel.add(m_currencyPicker, gridBagConstraints);
		/*
		 * Misc.setGridBagConstraints(currencyPanel, m_currency,
		 * gridBagConstraints, 1, 1, GridBagConstraints.WEST, 1, 1, 0.0, 0.0,
		 * new Insets(1, 1, 3, 1));
		 */
		Misc.setGridBagConstraints(currencyPanel, new JLabel("Exch. Rate*"),
				gridBagConstraints, 2, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(currencyPanel, m_exchangeNumberField,
				gridBagConstraints, 3, 1, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		// form left
		formleftPanel.setLayout(new GridBagLayout());
		
		formleftPanel.setPreferredSize(new Dimension(420, 400));
		gridBagConstraints = new GridBagConstraints();
		Misc.setGridBagConstraints(formleftPanel, new JLabel("Status"),
				gridBagConstraints, 0, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_statusTextField,
				gridBagConstraints, 2, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel, formrightPanel,
				gridBagConstraints, 3, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 15, 1.0, 0.0, new Insets(1, 3, 3,
						1));
		
		Misc.setGridBagConstraints(formleftPanel, new JLabel("Submit"),
				gridBagConstraints, 0, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_submitTextField,
				gridBagConstraints, 2, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel,
				new JLabel("Payment Source"), gridBagConstraints, 0, 2,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1,
						3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_sourceComboBox,
				gridBagConstraints, 2, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel, new JLabel("Account"),
				gridBagConstraints, 0, 3, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 3, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_accountPicker,
				gridBagConstraints, 2, 3, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, new Insets(1, 1, 3, 1));
		
		Misc.setGridBagConstraints(formleftPanel, new JLabel("Cheque No."),
				gridBagConstraints, 0, 4, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 4, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_chequenoTextField,
				gridBagConstraints, 2, 4, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel,
				new JLabel("Cheque Due Date"), gridBagConstraints, 0, 5,
				GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1,
						3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 5, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_chequedatePicker,
				gridBagConstraints, 2, 5, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel, new JLabel("Pay to*"),
				gridBagConstraints, 0, 6, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 6, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_paytoPicker,
				gridBagConstraints, 2, 6, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel, new JLabel("Job Title"),
				gridBagConstraints, 0, 7, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 7, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_jobTextField,
				gridBagConstraints, 2, 7, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel, new JLabel(
		"Cash Advance Others"), gridBagConstraints, 0, 8,
		GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1,
				3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JSeparator(),
				gridBagConstraints, 1, 8, GridBagConstraints.HORIZONTAL, 2, 1,
				0.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel, new JLabel("     Currency*"),
				gridBagConstraints, 0, 9, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 9, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, currencyPanel,
				gridBagConstraints, 2, 9, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel, new JLabel("     Amount*"),
				gridBagConstraints, 0, 10, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 1, 3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 10, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_amountNumbeField,
				gridBagConstraints, 2, 10, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formleftPanel, new JLabel(
		"     Amount Base Curr"), gridBagConstraints, 0, 11,
		GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1, 1,
				3, 1));
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 11, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, m_baseAmount,
				gridBagConstraints, 2, 11, GridBagConstraints.HORIZONTAL, 1, 1,
				1.0, 0.0, null);
		
		/*
		 * Misc.setGridBagConstraints(formleftPanel, new JLabel("Unit Code*"),
		 * gridBagConstraints, 0, 12, GridBagConstraints.HORIZONTAL, 1, 1, 0.0,
		 * 0.0, new Insets(1, 1, 3, 1));
		 * Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
		 * gridBagConstraints, 1, 12, GridBagConstraints.HORIZONTAL, 1, 1, 0.0,
		 * 0.0, null); Misc.setGridBagConstraints(formleftPanel, m_unitPicker,
		 * gridBagConstraints, 2, 12, GridBagConstraints.HORIZONTAL, 1, 1, 1.0,
		 * 0.0, null);
		 * 
		 * Misc.setGridBagConstraints(formleftPanel, new JLabel("Department"),
		 * gridBagConstraints, 0, 13, GridBagConstraints.HORIZONTAL, 1, 1, 0.0,
		 * 0.0, new Insets(1, 1, 3, 1));
		 * Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
		 * gridBagConstraints, 1, 13, GridBagConstraints.HORIZONTAL, 1, 1, 0.0,
		 * 0.0, null); Misc.setGridBagConstraints(formleftPanel,
		 * m_departmentPicker, gridBagConstraints, 2, 13,
		 * GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);
		 */
		
		m_descriptionTextArea.setPreferredSize(new Dimension(50, 150));
		m_descriptionTextArea.setLineWrap(true);
		m_descriptionTextArea.setRows(5);
		JScrollPane SPDesc = new JScrollPane(m_descriptionTextArea);
		SPDesc.setPreferredSize(new Dimension(50, 50));
		gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		Misc.setGridBagConstraints(formleftPanel, new JLabel("Description*"),
				gridBagConstraints, 0, 14, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, new JLabel(" "),
				gridBagConstraints, 1, 14, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formleftPanel, SPDesc, gridBagConstraints,
				2, 14, GridBagConstraints.HORIZONTAL, 1, 1, 1.0, 0.0, null);
		
		panelTengah.setLayout(new java.awt.BorderLayout());
		panelTengah.setPreferredSize(new java.awt.Dimension(880, 380));
		
		// panelT
		panelTengah.add(formleftPanel, BorderLayout.WEST);
		
		// form right
		formrightPanel.setLayout(new GridBagLayout());
		formrightPanel.setPreferredSize(new Dimension(405, 400));
		
		// rightPanel
		// formrightPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		Misc.setGridBagConstraints(formrightPanel, new JLabel("Voucher No."),
				gridBagConstraints, 0, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 0, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_vouchernoTextField,
				gridBagConstraints, 2, 0, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formrightPanel, new JLabel("Voucher Date*"),
				gridBagConstraints, 0, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 1, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_voucherdatePicker,
				gridBagConstraints, 2, 1, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formrightPanel, new JLabel("Project Code*"),
				gridBagConstraints, 0, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 2, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_projectPicker,
				gridBagConstraints, 2, 2, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formrightPanel, new JLabel("Customer"),
				gridBagConstraints, 0, 3, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 3, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_customerTextField,
				gridBagConstraints, 2, 3, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		m_workTextArea.setPreferredSize(new Dimension(50, 10));
		m_workTextArea.setLineWrap(true);
		m_workTextArea.setRows(5);
		JScrollPane SPWork = new JScrollPane(m_workTextArea);
		SPWork.setPreferredSize(new Dimension(50, 50));
		
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		Misc.setGridBagConstraints(formrightPanel, new JLabel("Work Desc."),
				gridBagConstraints, 0, 4, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 4, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, SPWork, gridBagConstraints,
				2, 4, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formrightPanel, new JLabel("Unit Code"),
				gridBagConstraints, 0, 5, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 5, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_unitTextField,
				gridBagConstraints, 2, 5, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formrightPanel, new JLabel("Activity"),
				gridBagConstraints, 0, 6, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 6, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_activityTextField,
				gridBagConstraints, 2, 6, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formrightPanel, new JLabel("Department"),
				gridBagConstraints, 0, 7, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 7, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_departmentTextField,
				gridBagConstraints, 2, 7, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formrightPanel, new JLabel("O.R No"),
				gridBagConstraints, 0, 8, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 8, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_ornoTextField,
				gridBagConstraints, 2, 8, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formrightPanel, new JLabel(
		"SO/PO/Contract No."), gridBagConstraints, 0, 9,
		GridBagConstraints.HORIZONTAL, 1, 1, 0.0, 0.0, new Insets(1,
				15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 9, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_contractTextField,
				gridBagConstraints, 2, 9, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		Misc.setGridBagConstraints(formrightPanel, new JLabel("IPC No."),
				gridBagConstraints, 0, 10, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 10, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, m_ipcTextField,
				gridBagConstraints, 2, 10, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		m_remarksTextArea.setPreferredSize(new Dimension(50, 80));
		m_remarksTextArea.setLineWrap(true);
		m_remarksTextArea.setRows(5);
		JScrollPane SPRemark = new JScrollPane(m_remarksTextArea);
		SPRemark.setPreferredSize(new Dimension(50, 50));
		Misc.setGridBagConstraints(formrightPanel, new JLabel("Remarks"),
				gridBagConstraints, 0, 11, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, new Insets(1, 15, 3, 1));
		Misc.setGridBagConstraints(formrightPanel, new JLabel(" "),
				gridBagConstraints, 1, 11, GridBagConstraints.HORIZONTAL, 1, 1,
				0.0, 0.0, null);
		Misc.setGridBagConstraints(formrightPanel, SPRemark,
				gridBagConstraints, 2, 11, GridBagConstraints.HORIZONTAL,
				GridBagConstraints.REMAINDER, 1, 1.0, 0.0, null);
		
		panelTengahKanan.setLayout(new java.awt.BorderLayout());
		panelTengahKanan.setPreferredSize(new java.awt.Dimension(325, 400));
		panelTengahKanan.add(formrightPanel, BorderLayout.WEST);
		panelTengah.add(panelTengahKanan, BorderLayout.CENTER);
		panelUtama.add(panelTengah, java.awt.BorderLayout.CENTER);
		
		// assignpanel tempat assgin panelnya
		panelBawah.setLayout(new java.awt.BorderLayout());
		panelBawah.setPreferredSize(new java.awt.Dimension(880, 110));
		panelBawahKiri.setLayout(new java.awt.BorderLayout());
		panelBawahKanan.setLayout(new java.awt.BorderLayout());
		m_originatorAssign.setPreferredSize(new Dimension(275, 110));
		m_approvedAssign.setPreferredSize(new Dimension(275, 110));
		m_receivedAssign.setPreferredSize(new Dimension(275, 110));
		panelBawah.add(m_originatorAssign, BorderLayout.WEST);
		panelBawahKiri.add(m_approvedAssign, BorderLayout.WEST);
		panelBawahKanan.add(m_receivedAssign, BorderLayout.WEST);
		panelBawahKiri.add(panelBawahKanan, BorderLayout.CENTER);
		panelBawah.add(panelBawahKiri, BorderLayout.CENTER);
		panelUtama.add(panelBawah, BorderLayout.SOUTH);		
		add(panelUtama, BorderLayout.NORTH);
	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}
	
	public void clearAll() {
		super.clearAll();
		clearForm();
		clearKomponen();
		disableEditMode();
	}
	
	public void clearKomponen() {
		m_exchangeNumberField.setValue(new Double(0));
		m_baseAmount.currencyTextField.setText("");
		m_statusTextField.setText("");
		m_originatorAssign.setEmployee(null);
		m_approvedAssign.setEmployee(null);
		m_receivedAssign.setEmployee(null);
		m_originatorAssign.m_jobTextField.setText("");
		m_approvedAssign.m_jobTextField.setText("");
		m_receivedAssign.m_jobTextField.setText("");
		m_originatorAssign.setDate(null);
		m_approvedAssign.setDate(null);
		m_receivedAssign.setDate(null);
		m_voucherdatePicker.setDate(null);
		m_currencyPicker.setObject(null);
		m_jobTextField.setText("");
		
	}
	
	
	protected void doNew() {
		super.doNew();
		isiDefaultAssignPanel();
		m_chequenoTextField.setEditable(false);
		m_chequedatePicker.setEditable(false);
	}
	
	
	
	protected void doSubmit() {
		validityMsgs.clear();
		if (m_accountPicker.getObject() == null)
			addInvalid("Source account must be selected");
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
	}
	
	public void doEdit() {
		super.doEdit();
		enableDisableExchrate();
	}
	
	public void enableDisableExchrate(){
		Object currobj = m_currencyPicker.getObject(); 
		if (currobj instanceof Currency) {
			Currency curr = (Currency) currobj;
			if (curr.getSymbol().equals(baseCurrency.getSymbol()))
				m_exchangeNumberField.setEditable(false);
			else
				m_exchangeNumberField.setEditable(true);
		}
		else
			m_exchangeNumberField.setEditable(false);
	}
	
	private void enableExchRate() {
		Object currobj = m_currencyPicker.getObject();
		if (currobj instanceof Currency) {
			Currency curr = (Currency) currobj;
			if(curr.getSymbol().equals(baseCurrency.getSymbol())){
				m_exchangeNumberField.setValue(new Double(1.0));
				m_exchangeNumberField.setEditable(false);
			}
			else{
				m_exchangeNumberField.setEditable(true);
				/*//ExchRateText.setValue(new Double(1.0));
				 curr = null;
				 ProjectData project = (ProjectData) m_projectPicker.getObject();
				 if (project != null) {
				 project = getRealProject(project.getIndex());
				 ProjectContract projContract = getProjectContract(project);
				 if (projContract != null) {
				 curr = projContract.getCurrency();
				 }
				 setDefaultExchangeRate(curr);
				 m_exchangeNumberField.setEditable(true);
				 }*/				
			}
		}
	}
	
	/*	private void setDefaultExchangeRate(Currency curr) {
	 if(entity().getStatus()>StateTemplateEntity.State.SAVED)
	 return;
	 
	 Date date = m_voucherdatePicker.getDate();
	 
	 if (curr == null)
	 curr = baseCurrency;
	 
	 initExchangeRateHelper(m_conn, m_sessionid);
	 double rate = getDefaultExchangeRate(curr, date);
	 m_exchangeNumberField.setValue(new Double(rate));
	 
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
	 */	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_searchRefNoBtn) {
			SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame
					.getMainFrame(), "Search Voucher", m_conn, m_sessionid,
					new UnitBankCashTransferLoader(m_conn, PmtCAProject.class),
			"Cash Advance General Project");
			dlg.setVisible(true);
			if (dlg.selectedObj != null) {
				doLoad(dlg.selectedObj);
			}
		} else if (e.getSource() == m_sourceComboBox) {			
			revalidateAccount((Currency)m_currencyPicker.getObject());
			//m_currencyPicker.setObject(baseCurrency);
			//enableExchRate();
		} else if (e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex() > 0) {
				new Vchr_CAProject(m_entity, m_conn, m_sessionid);
			} else {
				JOptionPane.showMessageDialog(this, "Data is empty", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
	}
	
	private void revalidateAccount(Currency curr) {
		String receive = (String) m_sourceComboBox.getSelectedItem();
		formleftPanel.remove(m_accountPicker);
		if (receive.equals("Cash")) {
			setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid,curr));
			m_chequedatePicker.setDate(null);
			m_chequenoTextField.setText("");
			m_chequenoTextField.setEditable(false);
			m_chequedatePicker.setEditable(false);
		} else {
			setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid,curr));
			m_chequedatePicker.setDate(null);
			m_chequenoTextField.setText("");
			m_chequenoTextField.setEditable(true);
			m_chequedatePicker.setEditable(true);
		}
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		Misc.setGridBagConstraints(formleftPanel, m_accountPicker,
				gridBagConstraints, 2, 3, GridBagConstraints.HORIZONTAL, 1,
				1, 1.0, 0.0, null);
		formleftPanel.add(m_accountPicker, gridBagConstraints);
		formleftPanel.revalidate();
	}
	
	private void isiDefaultAssignPanel() {
		m_originatorAssign.m_jobTextField
		.setText(getEmployeeJobTitle(defaultOriginator));
		m_approvedAssign.m_jobTextField
		.setText(getEmployeeJobTitle(defaultApproved));
		m_receivedAssign.m_jobTextField
		.setText(getEmployeeJobTitle(defaultReceived));
	}
	
	protected void clearForm() {
		clearTextField(formrightPanel);
		clearTextField(formleftPanel);
		m_workTextArea.setText("");
		m_projectPicker.setObject(null);
		
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
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	PmtCAProject entity() {
		return m_entity;
	}
	
	protected void enableEditMode() {
		this.m_workTextArea.setEditable(false);
		this.m_sourceComboBox.setEnabled(true);
		this.m_accountPicker.setEnabled(true);
		this.m_chequenoTextField.setEditable(true);
		this.m_chequedatePicker.setEditable(true);
		this.m_paytoPicker.setEnabled(true);
		this.m_amountNumbeField.setEditable(true);
		this.m_voucherdatePicker.setEditable(true);
		this.m_descriptionTextArea.setEnabled(true);
		this.m_remarksTextArea.setEnabled(true);
		this.m_originatorAssign.setEnabled(true);
		this.m_approvedAssign.setEnabled(true);
		this.m_receivedAssign.setEnabled(true);
		m_currencyPicker.setEnabled(true);
		m_paytoPicker.setEditable(true);
		m_projectPicker.setEnabled(true);
		//m_exchangeNumberField.setEditable(true);
		
	}
	
	protected void disableEditMode() {
		this.m_sourceComboBox.setEnabled(false);
		this.m_accountPicker.setEnabled(false);
		this.m_chequenoTextField.setEditable(false);
		this.m_chequedatePicker.setEditable(false);
		this.m_paytoPicker.setEnabled(false);
		this.m_amountNumbeField.setEditable(false);
		this.m_voucherdatePicker.setEditable(false);
		this.m_descriptionTextArea.setEnabled(false);
		this.m_remarksTextArea.setEnabled(false);
		this.m_originatorAssign.setEnabled(false);
		this.m_approvedAssign.setEnabled(false);
		this.m_receivedAssign.setEnabled(false);
		this.m_workTextArea.setEditable(false);
		m_paytoPicker.setEditable(false);
		m_currencyPicker.setEnabled(false);
		m_baseAmount.amountNumberField.setEditable(false);
		m_projectPicker.setEnabled(false);
		m_exchangeNumberField.setEditable(false);
		//enableDisableExchrate();
	}
	
	ArrayList validityMsgs = new ArrayList();
	
	protected boolean cekValidity() {
		validityMsgs.clear();
		if (m_voucherdatePicker.getDate() == null)
			addInvalid("Voucher date must be selected");
		/*if (m_accountPicker.getObject() == null)
		 addInvalid("Source account must be selected");*/
		if (m_paytoPicker.getEmployee() == null)
			addInvalid("PayTo must be selected");
		if (m_amountNumbeField.getText().equals(""))
			addInvalid("Amount must be inserted");
		if (m_exchangeNumberField.getText().equals(""))
			addInvalid("Exchage Rate must be inserted");
		if (m_projectPicker.getObject() == null)
			addInvalid("Project must be selected");
		if (m_descriptionTextArea.getText().equals(""))
			addInvalid("Description must be inserted");
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
	
	protected Object createNew() {
		PmtCAProject a = new PmtCAProject();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a;
	}
	
	protected void doSave() {
		if (!cekValidity())
			return;
		super.doSave();
		AccountingSQLSAP sql = new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")) {
				long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_CA_PROJECT,
						m_conn);
				entity().setIndex(index);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void setEntity(Object m_entity) {
		PmtCAProject oldEntity = this.m_entity;
		if (oldEntity != null) {
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PmtCAProject) m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}
	
	protected void gui2entity() {
		String paySource = (String) m_sourceComboBox.getSelectedItem();
		entity().setPaymentSource(paySource);
		
		Object objaccount = m_accountPicker.getObject();
		if (objaccount instanceof CashAccount) {
			CashAccount cash= (CashAccount) objaccount;
			entity().setCashAccount(cash);
			entity().setBankAccount(null);
		}
		else if (objaccount instanceof BankAccount) {
			BankAccount bank = (BankAccount) objaccount;
			entity().setBankAccount(bank);
			entity().setCashAccount(null);
		}
		
		entity().setChequeNo(m_chequenoTextField.getText());
		entity().setChequedueDate(m_chequedatePicker.getDate());
		entity().setPayTo(m_paytoPicker.getEmployee());
		entity().setProject((ProjectData) m_projectPicker.getObject());
		entity().setUnit(entity().getProject().getUnit());
		
		/* String payTo = (String) PayToCombo.getSelectedItem();
		 if(payTo.equalsIgnoreCase("BANK")){ entity().setPayTo("BANK");
		 entity().setRcvBankAccount((BankAccount) RcvAccountComp.getObject());
		 entity().setRcvCashAccount(null); }else{ entity().setPayTo("CASH");
		 entity().setRcvCashAccount((CashAccount) RcvAccountComp.getObject());
		 entity().setRcvBankAccount(null); }*/
		
		entity().setCurrency((Currency) m_currencyPicker.getObject());
		Object obj = m_exchangeNumberField.getValue();
		Number amount = (Number) obj;
		entity().setExchangeRate(amount.doubleValue());
		
		obj = m_amountNumbeField.getValue();
		amount = (Number) obj;
		entity().setAmount(amount.doubleValue());
		entity().setTransactionDate(m_voucherdatePicker.getDate());
		
		entity().setRemarks(m_remarksTextArea.getText());
		
		entity().transTemplateRead(this.m_originatorAssign,
				this.m_approvedAssign, this.m_receivedAssign,
				this.m_vouchernoTextField, this.m_descriptionTextArea);
	}
	
	protected void entity2gui() {
		
		if (entity().getPaymentSource()!=null)
			m_sourceComboBox.setSelectedItem(entity().getPaymentSource());
		
		if (entity().getCurrency() != null) {
			m_currencyPicker.setObject(entity().getCurrency());
		} else {
			m_currencyPicker.setObject(null);
		}		
		
		if (entity().getBankAccount()!=null)
			m_accountPicker.setObject(entity().getBankAccount());
		else if (entity().getCashAccount()!=null)
			m_accountPicker.setObject(entity().getCashAccount());
		
		m_chequenoTextField.setText(entity().getChequeNo());
		m_chequedatePicker.setDate(entity().getChequedueDate());
		m_projectPicker.setObject(entity().getProject());
		m_paytoPicker.setEmployee(entity().getPayTo());
		if (entity().getPayTo() != null)
			m_jobTextField.setText(entity().getPayTo().getJobTitleName());		
		m_amountNumbeField.setText(new Double(entity().getAmount()).toString());
		
		m_baseAmount.setCurrency(baseCurrency);
		
		if (entity().getExchangeRate() > 0) {
			m_exchangeNumberField.setValue(new Double(entity().getExchangeRate()));
		} else {
			m_exchangeNumberField.setValue(new Double(1));
		}
		
		m_amountNumbeField.setValue(new Double(entity().getAmount()));
		
		m_vouchernoTextField.setText(entity().getReferenceNo());
		if (entity().getTransactionDate() != null)
			m_voucherdatePicker.setDate(entity().getTransactionDate());
		else {
			m_voucherdatePicker.setDate(new Date());
		}
		
		m_descriptionTextArea.setText(entity().getDescription());
		m_remarksTextArea.setText(entity().getRemarks());
		
		m_originatorAssign.setEmployee(entity().getEmpOriginator());
		m_originatorAssign.setDate(entity().getDateOriginator());
		
		m_approvedAssign.setEmployee(entity().getEmpApproved());
		m_approvedAssign.setDate(entity().getDateApproved());
		
		m_receivedAssign.setEmployee(entity().getEmpReceived());
		m_receivedAssign.setDate(entity().getDateReceived());
		
		m_originatorAssign.setJobTitle(entity().getJobTitleOriginator());
		m_approvedAssign.setJobTitle(entity().getJobTitleApproved());
		m_receivedAssign.setJobTitle(entity().getJobTitleReceived());
		
		m_statusTextField.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if (entity().getSubmitDate() != null)
			m_submitTextField.setText(dateformat.format(entity()
					.getSubmitDate()));
		else
			m_submitTextField.setText("");
		
	}
	
	private void enableAwal() {
		setenableEditPanel(formrightPanel, false);
		m_jobTextField.setEditable(false);
		m_vouchernoTextField.setEditable(false);
		m_exchangeNumberField.setEditable(false);
		//m_currency.setEnabled(false);
		m_workTextArea.setEnabled(false);// (false);
		/*
		 * setenableEditPanel(jPanel1_2_1_1,false);
		 * setenableEditPanel(jPanel1_2_2_1,false);
		 */
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn,
				IConstants.PAYMENT_CASHADVANCE_PROJECT,
				Signature.SIGN_ORIGINATOR);
		if (sign != null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature
		.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_PROJECT,
				Signature.SIGN_APPROVED);
		if (sign != null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature
		.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_PROJECT,
				Signature.SIGN_RECEIVED);
		if (sign != null)
			defaultReceived = sign.getFullEmployee();
	}
	
	private void addingListener() {
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_saveBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_paytoPicker.addPropertyChangeListener(this);
		m_exchangeNumberField.getDocument().addDocumentListener(this);
		m_amountNumbeField.getDocument().addDocumentListener(this);
		m_sourceComboBox.addActionListener(this);
		m_projectPicker.addPropertyChangeListener("object", this);
		//m_accountPicker.addPropertyChangeListener(this);
		m_voucherdatePicker.addPropertyChangeListener("date", this);
		m_currencyPicker.addPropertyChangeListener("object",this);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())) {
			readEntityState();
		}
		if (evt.getSource() == m_paytoPicker) {
			if (m_paytoPicker.getEmployee() != null)
				m_jobTextField.setText(m_paytoPicker.getJobTitle());
		} 
		else if (evt.getSource() == m_projectPicker) {
			if (evt.getNewValue() != null) {
				ProjectData temp = (ProjectData) m_projectPicker.getObject();
				m_customerTextField.setText(temp.getCustname());
				m_workTextArea.setText(temp.getWorkDescription());
				m_unitTextField.setText(temp.getUnit().getDescription());
				m_activityTextField.setText(temp.getActivity().getName());
				m_departmentTextField.setText(temp.getDepartment().getName());
				m_ornoTextField.setText(temp.getORNo());
				m_contractTextField.setText(temp.getPONo());
				m_ipcTextField.setText(temp.getIPCNo());
				
				/*ProjectContract projContract = getProjectContract(temp);
				 if (projContract != null) {
				 temp.setProjectContract(projContract);	
				 Currency curr = temp.getProjectContract().getCurrency();
				 m_currency.setObject(curr);
				 enableExchRate();
				 }else{
				 Currency curr = baseCurrency;
				 m_currency.setObject(curr);
				 enableExchRate();
				 }*/
			} else {
				m_customerTextField.setText(null);
				m_workTextArea.setText(null);
				m_unitTextField.setText(null);
				m_activityTextField.setText(null);
				m_departmentTextField.setText(null);
				m_ornoTextField.setText(null);
				m_contractTextField.setText(null);
				m_ipcTextField.setText(null);
			}
		} 
		/*else if (evt.getSource() == m_accountPicker) {
		 if (evt.getNewValue() != null) {
		 if (evt.getNewValue() instanceof BankAccount) {
		 BankAccount b = (BankAccount) m_accountPicker.getObject();
		 m_currencyPicker.setObject(b.getCurrency());
		 enableExchRate();
		 } else if (evt.getNewValue() instanceof CashAccount) {
		 CashAccount b = (CashAccount) m_accountPicker.getObject();
		 m_currencyPicker.setObject(b.getCurrency());
		 }
		 if (m_accountPicker.getObject() != null)
		 if (((Currency) m_currencyPicker.getObject()).getSymbol().equalsIgnoreCase(this.baseCurrency.getSymbol())) {
		 m_exchangeNumberField.setEditable(false);
		 m_exchangeNumberField.setValue(new Double(1));
		 } else
		 m_exchangeNumberField.setEditable(true);
		 
		 }
		 }*/
		else if (evt.getSource() ==m_currencyPicker){
			revalidateAccount((Currency)m_currencyPicker.getObject());
			enableExchRate();
		}
		/*else if("date".equals(evt.getPropertyName())){
		 if (m_voucherdatePicker == evt.getSource()){
		 enableExchRate();
		 }
		 }*/
	}
	
	private void toBaseCurrency() {
		try {
			double amount, crate;
			if (!m_amountNumbeField.getText().equalsIgnoreCase(""))
				amount = ((Number) m_numberFormatter
						.stringToValue(m_amountNumbeField.getText()))
						.doubleValue();
			else
				amount = 0;
			String xchRate = m_exchangeNumberField.getText();
			if (!xchRate.equalsIgnoreCase(""))
				crate = ((Number) m_numberFormatter
						.stringToValue(m_exchangeNumberField.getText()))
						.doubleValue();
			else
				crate = 1;
			m_baseAmount.setValue(amount * crate);
		}
		
		catch (Exception e) {
			System.out.println("Error");
		}	
	}
	
	public void insertUpdate(DocumentEvent e) {
		toBaseCurrency();
	}
	
	public void removeUpdate(DocumentEvent e) {
		toBaseCurrency();
	}
	
	public void changedUpdate(DocumentEvent e) {
		toBaseCurrency();
	}
	
	protected void setAccountComp(LookupPicker accountComp) {
		LookupPicker old = m_accountPicker;
		if (old != null) {
			old.removePropertyChangeListener("object", this);
		}
		m_accountPicker = accountComp;
		m_accountPicker.addPropertyChangeListener("object", this);
	}
}