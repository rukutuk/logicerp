package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.border.BevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.DoubleCellRenderer;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.Vchr_NonProject;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCost;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCostDetail;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.accounting.entity.UnitBankCashTransferLoader;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.FormattedDoubleCellEditor;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeeCellEditor;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.OrganizationCellEditor;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.PartnerCellEditor;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.cgui.ProjectDataCellEditor;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class PaymentOperationalCostPanel  extends RevTransactionPanel 
implements ActionListener,PropertyChangeListener,ItemListener{
	private static final long serialVersionUID = 1L;
	private PmtOperationalCost m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	private ButtomTablePmtProject m_table;
	private JRadioButton m_singleRb = new JRadioButton("Single");
	private JRadioButton m_multipleRb = new JRadioButton("Multiple");
	public PaymentOperationalCostPanel(Connection conn, long sessionid) {
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
		initExchangeRateHelper(m_conn, m_sessionid);
		setDefaultSignature(); 
		setEntity(new PmtOperationalCost());
		m_entityMapper = MasterMap.obtainMapperFor(PmtOperationalCost.class);
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
		PayToTxt = new JTextField();
		CurrComp = new CurrencyPicker(m_conn, m_sessionid);
		UnitCodeComp = new UnitPicker(m_conn, m_sessionid);
		DepartmentComp = new LookupDepartmentPicker(m_conn, m_sessionid);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by"); 
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		TopButtonPanel = new javax.swing.JPanel();
		BottomButtonPanel = new javax.swing.JPanel();
		java.awt.GridBagConstraints gridBagConstraints;
		PanelAtas = new javax.swing.JPanel();
		jPanel1_1 = new javax.swing.JPanel();
		m_newBtn = new javax.swing.JButton();
		m_editBtn = new javax.swing.JButton();
		m_saveBtn = new javax.swing.JButton();
		m_deleteBtn = new javax.swing.JButton();
		m_cancelBtn = new javax.swing.JButton();
		m_submitBtn = new javax.swing.JButton();
		PanelKiri = new javax.swing.JPanel();
		PanelKiri_2 = new javax.swing.JPanel();
		jPanel1_2_2 = new javax.swing.JPanel();
		PanelKanan = new javax.swing.JPanel();
		VoucherNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		VoucherDateLabel = new javax.swing.JLabel();
		UnitCodeLabel = new javax.swing.JLabel();
		DepartmentLabel = new javax.swing.JLabel();
		DescriptionLabel = new javax.swing.JLabel();
		RemarksLabel = new javax.swing.JLabel();
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
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
		ReceiveFromLabel1 = new javax.swing.JLabel();
		PanelCurrencyDKK = new javax.swing.JPanel();
		ExchRateLabel1 = new javax.swing.JLabel();
		ExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		PanelOriginatorComp = new javax.swing.JPanel();
		PanelApprovedComp = new javax.swing.JPanel();
		PanelReceivedComp = new javax.swing.JPanel();
		PanelBawah = new javax.swing.JPanel();
		BottomButtonPanel_2 = new javax.swing.JPanel();
		AddOprCostAccountBtn = new javax.swing.JButton();
		DeleteOprCostAccountBtn = new javax.swing.JButton();
		OprCostAccountScrollPane = new javax.swing.JScrollPane();
		setLayout(new java.awt.BorderLayout());
		setPreferredSize(new java.awt.Dimension(700, 675));
		
		PanelAtas.setLayout(new java.awt.BorderLayout());
		PanelAtas.setOpaque(false);
		PanelAtas.setPreferredSize(new java.awt.Dimension(650, 360));
		jPanel1_1.setLayout(new java.awt.BorderLayout());
		jPanel1_1.setPreferredSize(new java.awt.Dimension(650, 35));
		
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
		PanelAtas.add(jPanel1_1, java.awt.BorderLayout.NORTH);
		PanelKiri_2.setLayout(new java.awt.BorderLayout());
		PanelKiri_2.setPreferredSize(new java.awt.Dimension(740, 320));
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(415, 305));
		PanelKanan.setLayout(new java.awt.GridBagLayout());
		
		PanelKanan.setPreferredSize(new java.awt.Dimension(415, 400));
		VoucherNoLabel.setText("Voucher No");
		VoucherNoLabel.setPreferredSize(new java.awt.Dimension(90, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(VoucherNoLabel, gridBagConstraints);
		
		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(RefNoText, gridBagConstraints);
		
		VoucherDateLabel.setText("Voucher Date*");
		VoucherDateLabel.setPreferredSize(new java.awt.Dimension(90, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(VoucherDateLabel, gridBagConstraints);
		
		UnitCodeLabel.setText("Unit Code*");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(90, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(UnitCodeLabel, gridBagConstraints);
		
		DepartmentLabel.setText("Department");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(90, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(DepartmentLabel, gridBagConstraints);
		
		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(90, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(RemarksLabel, gridBagConstraints);
		
		UnitCodeComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(UnitCodeComp, gridBagConstraints);
		
		JPanel groupdeptPanel = new JPanel();
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(m_singleRb);
		bg1.add(m_multipleRb);
		m_singleRb.setSelected(true);
		
		groupdeptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Department",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11)));
		groupdeptPanel.add(m_singleRb);
		groupdeptPanel.add(m_multipleRb);
		
		groupdeptPanel.setPreferredSize(new java.awt.Dimension(290, 50));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(groupdeptPanel, gridBagConstraints);
		
		DepartmentComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(DepartmentComp, gridBagConstraints);
		
		
		/*gridBagConstraints = new java.awt.GridBagConstraints();
		 gridBagConstraints.gridx = 1;
		 gridBagConstraints.gridy = 5;
		 gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		 gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		 PanelKanan.add(DescriptionScrollPane, gridBagConstraints);*/
		
		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(290, 40));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(1);
		RemarksScrollPane.setViewportView(RemarksTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(RemarksScrollPane, gridBagConstraints);
		
		JPanel emptypanel = new JPanel();
		emptypanel.setPreferredSize(new java.awt.Dimension(290, 30));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKanan.add(emptypanel, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		PanelKanan.add(TransactionDateDate, gridBagConstraints);
		
		jPanel1_2_2.add(PanelKanan, java.awt.BorderLayout.WEST);
		
		PanelKiri_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);
		
		PanelKiri.setLayout(new java.awt.GridBagLayout());
		
		PanelKiri.setPreferredSize(new java.awt.Dimension(435, 320));
		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(StatusLabel, gridBagConstraints);
		
		SubmittedDateLabel.setText("Submitted Date");
		SubmittedDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(SubmittedDateLabel, gridBagConstraints);
		
		ReceiveToLabel.setText("Payment Source*");
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(ReceiveToLabel, gridBagConstraints);
		
		AccountLabel.setText("Account");
		AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(AccountLabel, gridBagConstraints);
		
		ChequeNoLabel.setText("Cheque No");
		ChequeNoLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(ChequeNoLabel, gridBagConstraints);
		
		ChequeDueDateLabel.setText("Cheque Due Date");
		ChequeDueDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(ChequeDueDateLabel, gridBagConstraints);
		
		StatusText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(StatusText, gridBagConstraints);
		
		SubmittedDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(SubmittedDateText, gridBagConstraints);
		
		PaymentSourceCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash","Bank" }));
		PaymentSourceCombo.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(PaymentSourceCombo, gridBagConstraints);
		
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(AccountComp, gridBagConstraints);
		
		ChequeNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(ChequeNoText, gridBagConstraints);
		
		ChequeDueDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(ChequeDueDateDate, gridBagConstraints);
		
		PayToLabel.setText("Pay To*");
		PayToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(PayToLabel, gridBagConstraints);
		
		PayToTxt.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(PayToTxt, gridBagConstraints);
		
		
		ReceiveFromLabel1.setText("Currency*");
		ReceiveFromLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(ReceiveFromLabel1, gridBagConstraints);
		
		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(90, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(DescriptionLabel, gridBagConstraints);
		
		
		PanelCurrencyDKK.setLayout(new java.awt.GridBagLayout());
		
		PanelCurrencyDKK.setPreferredSize(new java.awt.Dimension(290, 23));
		ExchRateLabel1.setText("Exch. Rate*");
		ExchRateLabel1.setPreferredSize(new java.awt.Dimension(70, 21));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		PanelCurrencyDKK.add(ExchRateLabel1, gridBagConstraints);
		
		ExchRateText.setPreferredSize(new java.awt.Dimension(135, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		PanelCurrencyDKK.add(ExchRateText, gridBagConstraints);
		
		CurrComp.setPreferredSize(new java.awt.Dimension(80, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		PanelCurrencyDKK.add(CurrComp, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(PanelCurrencyDKK, gridBagConstraints);
		
		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 40));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(1);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(DescriptionScrollPane, gridBagConstraints);
		
		PanelKiri_2.add(PanelKiri, java.awt.BorderLayout.WEST);
		PanelAtas.add(PanelKiri_2, java.awt.BorderLayout.CENTER);
		PanelOriginatorComp.setLayout(new java.awt.BorderLayout());
		PanelOriginatorComp.setPreferredSize(new java.awt.Dimension(700, 90));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());
		OriginatorComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(275, 110));
		PanelOriginatorComp.add(OriginatorComp, java.awt.BorderLayout.WEST);
		PanelOriginatorComp.add(PanelApprovedComp, java.awt.BorderLayout.CENTER);
		PanelAtas.add(PanelOriginatorComp, java.awt.BorderLayout.SOUTH);
		add(PanelAtas, java.awt.BorderLayout.NORTH);
		
		PanelApprovedComp.setLayout(new java.awt.BorderLayout());
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(275, 110));
		PanelApprovedComp.add(ApprovedComp, java.awt.BorderLayout.WEST);
		PanelApprovedComp.add(PanelReceivedComp, java.awt.BorderLayout.CENTER);
		
		PanelReceivedComp.setLayout(new java.awt.BorderLayout());
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(275, 110));
		PanelReceivedComp.add(ReceivedComp, java.awt.BorderLayout.WEST);
		
		PanelBawah.setLayout(new java.awt.BorderLayout());
		BottomButtonPanel_2.setLayout(new java.awt.BorderLayout());
		BottomButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));
		BottomButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
		AddOprCostAccountBtn.setText("Add");
		AddOprCostAccountBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		AddOprCostAccountBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		BottomButtonPanel.add(AddOprCostAccountBtn);
		
		DeleteOprCostAccountBtn.setText("Delete");
		DeleteOprCostAccountBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		DeleteOprCostAccountBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		BottomButtonPanel.add(DeleteOprCostAccountBtn);
		BottomButtonPanel_2.add(BottomButtonPanel, java.awt.BorderLayout.WEST);
		
		PanelBawah.add(BottomButtonPanel_2, java.awt.BorderLayout.NORTH);
		m_table = new ButtomTablePmtProject();
		OprCostAccountScrollPane.setPreferredSize(new java.awt.Dimension(650, 215));
		m_table.setModel(new javax.swing.table.DefaultTableModel(
				new Object [][] {
				},
				new String [] {
						"Account", "Desciption", "Amount"
				}
		));
		OprCostAccountScrollPane.setViewportView(m_table);
		PanelBawah.add(OprCostAccountScrollPane, java.awt.BorderLayout.CENTER);
		PanelBawah.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED ));
		add(PanelBawah, java.awt.BorderLayout.CENTER);
	}
	
	private void addingListener(){
		PaymentSourceCombo.addActionListener(this);
		PaymentSourceCombo.addItemListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);	
		//PayToTxt.addPropertyChangeListener(this);
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this); 
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		AddOprCostAccountBtn.addActionListener(this);
		DeleteOprCostAccountBtn.addActionListener(this);
		AccountComp.addPropertyChangeListener("object",this);
		TransactionDateDate.addPropertyChangeListener("date", this);
		CurrComp.addPropertyChangeListener("object",this);
		m_singleRb.addActionListener(this);
		m_multipleRb.addActionListener(this);
		DepartmentComp.addPropertyChangeListener(this);
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
		TransactionDateDate.setDate(null);
		CurrComp.setObject(null);
	}
	
	protected void clearAll() {
		super.clearAll();
		clearForm();
		clearKomponen();
		disableEditMode();
		setEnableButtonBawah(false);
		m_table.clearTable();
	}
	
	protected void doNew() {
		revalidateAccountcomp((Currency)CurrComp.getObject());
		super.doNew();
		isiDefaultAssignPanel();
		setEnableButtonBawah(true);
		m_table.clearTable();
		ChequeNoText.setEditable(false);
		ChequeDueDateDate.setEditable(false);
	}
	
	
	public void doEdit() {
		super.doEdit();
		Object currObject = CurrComp.getObject();
		if (currObject instanceof Currency) 
			propertyCurrency(currObject);
		setEnableButtonBawah(true);
	}
	
	protected void doSubmit() {
		validityMsgs.clear();
		if (!m_table.cekTotalDebitCredit()) 
			validityMsgs.add("Transaction is unbalanced. Please check accounts");
		if (entity().getBankAccount()== null && entity().getCashAccount()==null)
			validityMsgs.add("Source account must be selected");			
		if (validityMsgs.size()>0){
			StringBuffer result = new StringBuffer();
			Iterator iter = validityMsgs.iterator();
			while (iter.hasNext())
			{
				String o = iter.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this,result);
			return;
		}		
		
		super.doSubmit();
		setEnableButtonBawah(false);
	}
	
	public void actionPerformed(ActionEvent e){
		if (e.getSource()==m_singleRb){
			DepartmentComp.setEnabled(true);
			int maxRow = m_table.getRowCount() - 2;
			for (int i=0;i<maxRow;i++){
				if (DepartmentComp.getObject() instanceof Organization) {
					Organization org = (Organization) DepartmentComp.getObject();
					m_table.setValueAt(org,i,6);
					
				}
			}
		}
		else if (e.getSource()==m_multipleRb){
			DepartmentComp.setEnabled(false);
			DepartmentComp.setObject(null);
			int maxRow = m_table.getRowCount() - 2;
			for (int i=0;i<maxRow;i++){
				m_table.setValueAt("",i,6);
			}
		}
		else if(e.getSource() == PaymentSourceCombo) {
			revalidateAccountcomp((Currency)CurrComp.getObject());
			m_table.clearTable();
		}
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new Vchr_NonProject(m_entity,m_conn,m_sessionid);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame.getMainFrame(), "Search Voucher", m_conn, m_sessionid,
					new UnitBankCashTransferLoader(m_conn,PmtOperationalCost.class),"Operational Cost",true);    	
			dlg.setVisible(true);
			if (dlg.selectedObj != null){    			
				doLoad(dlg.selectedObj);
			}
		}else if(e.getSource()==AddOprCostAccountBtn){
			LookupOperationalCostTypePicker ProjectAccountComp = null;
			String receive = (String)PaymentSourceCombo.getSelectedItem();
			if(receive.equals("Cash"))
				ProjectAccountComp=new LookupOperationalCostTypePicker(m_conn, m_sessionid,IConstants.ATTR_PMT_CASH);
			else
				ProjectAccountComp=new LookupOperationalCostTypePicker(m_conn, m_sessionid,IConstants.ATTR_PMT_BANK);
			ProjectAccountComp.done();
			if(ProjectAccountComp.getObject()!=null){  
				int row = m_table.getRowCount()-2;
				m_nonDeleteList.add(new String("0"));	
				Vector vector=new Vector();
				JournalStandardAccount jsAccount = (JournalStandardAccount)ProjectAccountComp.getObject();
				Account acc=jsAccount.getAccount();
				vector.addElement(new Integer(m_table.getRowCount()+1));
				vector.addElement(acc.toStringWithCode());
				vector.addElement("");
				vector.addElement(null);
				vector.addElement(new Double(0));
				vector.addElement(new Double(0));
				if (m_singleRb.isSelected())
					if (DepartmentComp.getObject() instanceof Organization) {
						Organization org = (Organization) DepartmentComp.getObject();
						vector.addElement(org);
					}
					else
						vector.addElement(null);
				m_table.addData(vector, jsAccount, row);
			}
		}
		else if (e.getSource()==DeleteOprCostAccountBtn){			
			int row = m_table.getSelectedRow();
			int maxRow = m_table.getRowCount()-1;			
			if(row>-1){				
				String nil= ((String)m_nonDeleteList.get(row)).toString();
				if(row<(maxRow-1)){
					if (nil.equals("0")){
						if (JOptionPane.showConfirmDialog(this,"Are you sure?","Delete Confirmation Dialog",
								JOptionPane.YES_NO_OPTION)==0){
							m_nonDeleteList.remove(row);
							m_table.removeData(row);
							m_table.updateSummary();
						}else{
							return;
						}
					}
				}				
			}	
		}
	}
	
	private void revalidateAccountcomp(Currency curr) {
		String receive = (String)PaymentSourceCombo.getSelectedItem();
		System.out.println(receive);
		PanelKiri.remove(AccountComp);
		PanelKiri.revalidate();
		PanelKiri.repaint();
		if(receive.equals("Cash")){
			AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid,curr);
			AccountComp.addPropertyChangeListener(this);
			ChequeNoText.setText("");
			ChequeDueDateDate.setDate(null);
			ChequeNoText.setEditable(false);
			ChequeDueDateDate.setEditable(false);
		}else{
			AccountComp = new LookupBankAccountPicker(m_conn, m_sessionid,curr);
			AccountComp.addPropertyChangeListener(this);
			ChequeNoText.setText("");
			ChequeDueDateDate.setDate(null);
			ChequeNoText.setEditable(true);
			ChequeDueDateDate.setEditable(true);
		}
		AccountComp.addPropertyChangeListener(this);
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		PanelKiri.add(AccountComp, gridBagConstraints);
		PanelKiri.revalidate();		
	}
	
	public void LoadDetail(long index){
		//Object obj =  AccountComp.getObject();
		String paySource = (String) PaymentSourceCombo.getSelectedItem();
		JournalStandardAccount[]  jsAcc = getJournalStandardAccount(paySource);
		GenericMapper mapper2=MasterMap.obtainMapperFor(PmtOperationalCostDetail.class);
		mapper2.setActiveConn(m_conn); 
		String value = new Long(index).toString();
		List rs=mapper2.doSelectWhere(IDBConstants.ATTR_PMT_OPERASIONAL_COST+"="+value);
		m_table.clearTable();
		PmtOperationalCostDetail detail;
		Vector vektor;
		Account acc;
		for(int i=0;i<rs.size();i++){
			vektor = new Vector();
			detail = (PmtOperationalCostDetail) rs.get(i);
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
						vektor.addElement(new Double(detail.getAccValue()));
						vektor.addElement(new Double(0));
					}
					else if (jsAccount.m_balance==1){				
						vektor.addElement(new Double(0));
						vektor.addElement(new Double(detail.getAccValue()));
					}
					if (detail.getDepartment()!=null)
						vektor.addElement(detail.getDepartment());
					m_table.addData(vektor, jsAccount, i);
				}
			}
		}
	}
	
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
	}
	
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	PmtOperationalCost entity() {
		return m_entity;
	}
	
	protected void gui2entity() {
		entity().setCurrency((Currency)CurrComp.getObject());
		String paySource = (String) PaymentSourceCombo.getSelectedItem();
		entity().setPaymentSource(paySource);
		Object accObject = AccountComp.getObject();
		if (accObject!=null){
			if (accObject instanceof BankAccount) {
				BankAccount bank = (BankAccount) accObject;
				entity().setBankAccount(bank);
				entity().setCashAccount(null);
				entity().setUnit(bank.getUnit());
				
			}else if (accObject instanceof CashAccount) {
				CashAccount cash = (CashAccount) accObject;
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
		
		entity().setChequeNo(ChequeNoText.getText());
		entity().setChequeDueDate(ChequeDueDateDate.getDate());
		if (m_singleRb.isSelected())
			entity().setDepartmentgroup(0);
		else if (m_multipleRb.isSelected())
			entity().setDepartmentgroup(1);
		entity().setDepartment((Organization) DepartmentComp.getObject());
		entity().setPayTo(PayToTxt.getText());
		entity().setTotal( m_table.getTotal()  );
		entity().setExchangeRate(((Number)ExchRateText.getValue()).doubleValue());
		entity().setTransactionDate(TransactionDateDate.getDate());
		entity().setRemarks(RemarksTextArea.getText());
		entity().transTemplateRead(
				this.OriginatorComp, this.ApprovedComp,
				this.ReceivedComp, this.RefNoText,
				this.DescriptionTextArea
		);
		detailAccountOperation();
	}
	
	private void detailAccountOperation() {
		if (m_table.getRowCount()>=0){
			PmtOperationalCostDetail[] temp=new PmtOperationalCostDetail[m_table.getDataCount()];
			for (int i=0;i<m_table.getDataCount();i++){
				temp[i]=new PmtOperationalCostDetail();
				JournalStandardAccount jsAcc = (JournalStandardAccount)m_table.getListData().get(i);
				temp[i].setAccount(jsAcc.getAccount());
				temp[i].setDescription(m_table.getValueAt(i,2).toString());
				Account acc =  jsAcc.getAccount();
				String SubsidiaryAccSet = getSubsidiaryByindex(acc);				
				Object value = m_table.getValueAt(i,3);
				if (SubsidiaryAccSet.equals("Employee")){	
					if (value instanceof Employee) {
						Employee emp = (Employee) value;
						temp[i].setSubsidiAry(emp.getIndex());						
					}else
						validityMsgs.add("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Partner"))){
					if (value instanceof Partner) {
						Partner partn = (Partner) value;
						temp[i].setSubsidiAry(partn.getIndex());						
					}else
						validityMsgs.add("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Customer"))){
					if (value instanceof Customer) {
						Customer cust = (Customer) value;
						temp[i].setSubsidiAry(cust.getIndex());
					}else
						validityMsgs.add("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Cash"))){
					if (value instanceof CashAccount) {
						CashAccount cash = (CashAccount) value;
						temp[i].setSubsidiAry(cash.getIndex());
					}else
						validityMsgs.add("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Bank"))){
					if (value instanceof BankAccount) {
						BankAccount bank = (BankAccount) value;
						temp[i].setSubsidiAry(bank.getIndex());						
					}else
						validityMsgs.add("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Loan"))){
					if (value instanceof CompanyLoan) {
						CompanyLoan loan = (CompanyLoan) value;
						temp[i].setSubsidiAry(loan.getIndex());						
					}else{
						validityMsgs.add("Subsidiary " + acc.getName() +  " must selected");
					}
				}else if ((SubsidiaryAccSet.equals("Project"))){
					if (value instanceof ProjectData) {
						ProjectData project = (ProjectData) value;
						temp[i].setSubsidiAry(project.getIndex());						
					}else{
						validityMsgs.add("Subsidiary " + acc.getName() +  " must selected");
					}
				}else
					temp[i].setSubsidiAry(-1);
				
				try {
					Number val = null;
					if (jsAcc.m_balance==0)
						val= (Double) m_table.getValueAt(i, 4);
					else if (jsAcc.m_balance==1)
						val= (Double) m_table.getValueAt(i, 5);
					temp[i].setAccValue(val.doubleValue());
				} catch (Exception e) {
				}
				temp[i].setCurrency(entity().getCurrency());
				temp[i].setExchangeRate(entity().getExchangeRate());
				Object objdept = m_table.getValueAt(i,6);
				if (objdept instanceof Organization) {
					Organization org = (Organization) objdept;
					temp[i].setDepartment(org);
				}else{
					temp[i].setDepartment(null);
				}
			}
			entity().setPmtOperationalCostDetail(temp);
		}
	}
	
	protected void entity2gui() {
		if(entity().getCurrency()!=null){
			CurrComp.setObject(entity().getCurrency());
		}else{
			CurrComp.setObject(null);
		}
		if (entity().getPaymentSource()!=null)
			PaymentSourceCombo.setSelectedItem(entity().getPaymentSource());
		else
			PaymentSourceCombo.setSelectedItem("Bank");
		if(entity().getBankAccount()!=null){
			AccountComp.setObject(entity().getBankAccount());
		}else if (entity().getCashAccount()!=null){
			AccountComp.setObject(entity().getCashAccount());    		
		}else{
			AccountComp.setObject(null);
		}
		ChequeNoText.setText(entity().getChequeNo());
		ChequeDueDateDate.setDate(entity().getChequeDueDate());
		//Employee payTo=entity().getPayTo();
		PayToTxt.setText(entity().getPayTo());		
		//JobTitleText.setText(entity().getPayTo().getJobTitleName());
		UnitCodeComp.setObject(entity().getUnit());
		if (entity().getDepartmentgroup()==0)
			m_singleRb.setSelected(true);
		else if (entity().getDepartmentgroup()==1)
			m_multipleRb.setSelected(true);
		DepartmentComp.setObject(entity().getDepartment());
		
		if(entity().getExchangeRate()>0){
			ExchRateText.setValue(new Double(entity().getExchangeRate()));
		}else{
			ExchRateText.setValue(new Double(1));
		}
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
		/*if (AccountComp.getObject()!=null)*/
		LoadDetail(entity().getIndex());
	}

	protected void enableEditMode(){
		this.PaymentSourceCombo.setEnabled(true);
		this.AccountComp.setEnabled(true);
		this.ChequeNoText.setEditable(true);
		this.ChequeDueDateDate.setEditable(true);
		this.TransactionDateDate.setEditable(true);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
		UnitCodeComp.setEnabled(true);
		PayToTxt.setEditable(true);		
		CurrComp.setEnabled(true);
		m_singleRb.setEnabled(true);
		m_multipleRb.setEnabled(true);
		if (m_singleRb.isSelected())
			DepartmentComp.setEnabled(true);
		else if (m_multipleRb.isSelected())
			DepartmentComp.setEnabled(false);
	}
	
	protected void disableEditMode(){
		this.PaymentSourceCombo.setEnabled(false);
		this.AccountComp.setEnabled(false);
		this.ChequeNoText.setEditable(false);
		this.ChequeDueDateDate.setEditable(false);
		this.TransactionDateDate.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
		ExchRateText.setEditable(false);
		UnitCodeComp.setEnabled(false);
		PayToTxt.setEditable(false);
		DepartmentComp.setEnabled(false);
		CurrComp.setEnabled(false);
		m_singleRb.setEnabled(false);
		m_multipleRb.setEnabled(false);
	}
	
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (PayToTxt.getText().equals(""))
			validityMsgs.add("Pay to must be inseted");
		if (TransactionDateDate.getDate() == null)
			validityMsgs.add("Transaction date must be selected");
		if (CurrComp.getObject()==null)
			validityMsgs.add("Currency to must be selected");
		if (UnitCodeComp.getObject() == null)
			validityMsgs.add("Unit Code must be selected");
		if (cekdepartmentdetail())
			validityMsgs.add("Department detail is still empty");
		if (DescriptionTextArea.getText().equals(""))
			validityMsgs.add("Description must be inserted");
		detailAccountOperation();
		PmtOperationalCostDetail[] temp=entity().getPmtOperationalCostDetail();
		if(temp.length==0)
			validityMsgs.add("Account Detail must be added");
		if (validityMsgs.size()>0){
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
	
	public boolean cekdepartmentdetail(){
		boolean cek = false;
		int maxRow = m_table.getRowCount() - 2;
		for (int i=0;i<maxRow;i++){
			if (m_table.getValueAt(i,6)==null)				
				cek =true;
			else if (m_table.getValueAt(i,6).equals(""))
				cek=true;
		}
		return cek;
	}
	
	protected Object createNew()
	{
		PmtOperationalCost a  = new PmtOperationalCost();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}
	
	void setEntity(Object m_entity) {
		PmtOperationalCost oldEntity = this.m_entity;
		if (oldEntity!=null)
		{
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PmtOperationalCost)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		else if (evt.getSource() == DepartmentComp){
			if (m_singleRb.isSelected()){
				int maxRow = m_table.getRowCount() - 2;
				for (int i=0;i<maxRow;i++){
					if (DepartmentComp.getObject() instanceof Organization) {
						Organization org = (Organization) DepartmentComp.getObject();
						m_table.setValueAt(org,i,6);
					}
				}
			}
		}
		else if(evt.getSource() == CurrComp){
			Object currObject = CurrComp.getObject();
			if (currObject instanceof Currency) {
				propertyCurrency(currObject);
				revalidateAccountcomp((Currency)currObject);				
			}
			
		}
		else if(evt.getSource() == AccountComp){
			if ("object".equals(evt.getPropertyName())) {
				if (AccountComp.getObject() != null) {
					/*			Currency currency = ((CashBankAccount) AccountComp
					 .getObject()).getCurrency();
					 CurrComp.setObject(currency);
					 if (currency.getSymbol().equals(baseCurrency.getSymbol())) {
					 ExchRateText.setEditable(false);
					 ExchRateText.setValue(new Double(1));
					 } else {
					 ExchRateText.setEditable(true);
					 setDefaultExchangeRate(currency);
					 }
					 */			
					addToTable(evt.getNewValue());
				}
			}
		}
		else if (evt.getSource()==TransactionDateDate) {
			if ("date".equals(evt.getPropertyName())){
				CashBankAccount cba = (CashBankAccount) AccountComp.getObject();
				if (cba != null) {
					Currency curr = cba.getCurrency();
					setDefaultExchangeRate(curr);
				}
			}
		}
	}
	
	private void propertyCurrency(Object currObject) {
		Currency curr = (Currency) currObject;
		if (curr.getSymbol().equals(baseCurrency.getSymbol())){
			ExchRateText.setValue(new Double (1));
			ExchRateText.setEditable(false);				
		} else {
			ExchRateText.setEditable(true);	
			/*if (entity().getExchangeRate()==0){				
			 ExchRateText.setValue(new Double (1));
			 }*/
		}		
	}
	
	
	
	public JournalStandardAccount[] getJournalStandardAccount(Object obj){			
		String attr = "";		
		if (obj instanceof CashAccount) {					
			attr = IConstants.ATTR_PMT_CASH;
		}else if (obj instanceof BankAccount) {	
			attr = IConstants.ATTR_PMT_BANK;
		} else if (obj instanceof String) {
			String cashBank = (String) obj;
			if (cashBank.equalsIgnoreCase("CASH")){
				attr = IConstants.ATTR_PMT_CASH;
			}else{				
				attr = IConstants.ATTR_PMT_BANK;
			}
		}
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(m_conn,m_sessionid, 
					IDBConstants.MODUL_ACCOUNTING);
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYMENT_OPERASIONAL_COST,
					attr);		
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
			System.err.println("row : " + m_lastRow);
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
			vector.addElement(new Double(0.0D));
			if (m_singleRb.isSelected()){
				if (DepartmentComp.getObject() instanceof Organization) {
					Organization org = (Organization) DepartmentComp.getObject();
					vector.addElement(org);
				}
			}else
				vector.addElement(null);
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
			vector.addElement(new Double(0.0D));
			if (m_singleRb.isSelected()){
				if (DepartmentComp.getObject() instanceof Organization) {
					Organization org = (Organization) DepartmentComp.getObject();
					vector.addElement(org);
				}
			}
			else
				vector.addElement(null);
			m_table.addData(vector, jsAccount, row);
		}
	}
	
	private void enableAwal(){ 	
		setenableEditPanel(PanelKiri,false);
		setenableEditPanel(PanelCurrencyDKK,false);
		setenableEditPanel(PanelKanan,false);
		setEnableButtonBawah(false);
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_OPERASIONAL_COST, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_OPERASIONAL_COST, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_OPERASIONAL_COST, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	
	public void initTabel(){
		m_table=new ButtomTablePmtProject();
		OprCostAccountScrollPane.setViewportView(m_table);
	}
	
	protected void doSave() {
		if (!cekValidity()) return;
		
		super.doSave();
		saveDetail();	
		entity2gui();
		disableEditMode();
		if(!m_saveBtn.isEnabled())
			setEnableButtonBawah(false);
	}
	
	protected void tableStopCellEditing() {
		m_table.stopCellEditing();
	}
	
	private void saveDetail() {
		AccountingSQLSAP sql=new AccountingSQLSAP();
		GenericMapper mapper2=MasterMap.obtainMapperFor(PmtOperationalCostDetail.class);
		mapper2.setActiveConn(m_conn);
		try {
			if ((new Long(entity().getIndex()).toString().equalsIgnoreCase("0") ))	{	
				long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_OPERATIONAL_COST, m_conn);
				entity().setIndex(index);
			}
			
			if (entity().getPmtOperationalCostDetail()!=null){
				PmtOperationalCostDetail[] temp=entity().getPmtOperationalCostDetail();
				mapper2.doDeleteByColumn(IDBConstants.TABLE_PMT_OPERATIONAL_COST, 
						new Long(entity().getIndex()).toString());
				for (int i=0;i<temp.length;i++){
					temp[i].setPmtOperationalCost(entity());
					mapper2.doInsert(temp[i]);  
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setEnableButtonBawah(boolean bool){
		AddOprCostAccountBtn.setEnabled(bool);
		DeleteOprCostAccountBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}
	
	protected void clearForm() {
		clearTextField(PanelCurrencyDKK);
		clearTextField(PanelKiri);
		clearTextField(PanelKanan);
	}
	
	protected void clearTextField(JPanel temppanel) { 
		Component[] componentList=temppanel.getComponents();
		JTextField temptext;
		for (int i=0;i<componentList.length;i++)
		{ 
			if (componentList[i] instanceof JTextField)
			{ 
				temptext=(JTextField)componentList[i];
				temptext.setText(""); 
			}
		} 	
	}
	
	private void SetCashAccountToTable(PmtOperationalCostDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCompanyLoanToTable(PmtOperationalCostDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetProjectToTable(PmtOperationalCostDetail detail, Vector temp1, ProjectBusinessLogic  proLogic) {
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
	
	
	private void SetBankAccountToTable(PmtOperationalCostDetail detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCustomerToTable(PmtOperationalCostDetail detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Customer cust = null;
		cust = proLogic.getCustomerByIndex(detail.getSubsidiAry());
		temp1.addElement(cust);
	}
	
	private void SetPartnerToTable(PmtOperationalCostDetail detail, Vector temp1, ProjectBusinessLogic proLogic) {
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
	
	private void SetEmployeeToTable(PmtOperationalCostDetail detail, Vector temp1, HRMBusinessLogic hrmLogic) {
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
				if (((row > -1) && (row < (maxRow - 1))) && ((col == 4) || (col==5))) {
					String nil= ((String)m_nonDeleteList.get(row)).toString();
					if (nil.equals("0"))
						m_table.updateSummary();
				}
			}
		}
	}
	
	class ButtomTablePmtProject extends JTable {
		private static final long serialVersionUID = 1L;
		ButtomTablePmtProjectModel model = new ButtomTablePmtProjectModel();
		ArrayList listData=new ArrayList();
		double total =0;
		
		public ButtomTablePmtProject() {
			model.addColumn("No");
			model.addColumn("Account");
			model.addColumn("Description");
			model.addColumn("Subsidiary");
			model.addColumn("Debit");
			model.addColumn("Credit");
			model.addColumn("Department");
			
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
			double ttl = 0;
			
			int pos=-1;
			for (int i = 0; i <= maxTotalRow; i++) {
				JournalStandardAccount jsAccount = (JournalStandardAccount)listData.get(i);
				if (jsAccount.isHidden()) 
					pos = getPosisiNonDelete();
				if (i!=pos){
					if (getValueAt(i, 4) != null){
						debet = ((Double) getValueAt(i, 4)).doubleValue();
						totDebet += debet;
						ttl +=debet;
					}
				}				
				
				//if (jsAccount.isCalculate())				
				if (i!=pos){
					if (getValueAt(i, 5) != null) {
						credit = ((Double) getValueAt(i, 5)).doubleValue();	
						totCredit +=credit;
						ttl -=credit;
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
			setTotal(ttl);
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
					else if (col == 6 && m_multipleRb.isSelected())
						return new OrganizationCellEditor(GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
				}else{
					if (col == 6 && m_multipleRb.isSelected())
						return new OrganizationCellEditor(GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
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
					}
				}
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
						return new StandardFormatCellRenderer(Font.BOLD,
								JLabel.RIGHT);
					else if (column == 4 || column == 5 )
						return new FormattedDoubleCellRenderer(JLabel.RIGHT,
								Font.BOLD);
				} else {
					JournalStandardAccount jsAccount = (JournalStandardAccount)listData.get(row);
					if (column == 2)
						return new StandardFormatCellRenderer(Font.PLAIN,
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
			
			return new StandardFormatCellRenderer(Font.PLAIN, JLabel.LEFT);
		}
		
		public void clearTable() {
			listData.clear();
			m_nonDeleteList.clear();
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
			model.addRow(new Object[] { null,null, null, null, null,null });
			model.addRow(new Object[] { null,null, null, "TOTAL", new Double(0),new Double(0) });
			m_lastRow = 0;
		}	
		
		public void addData(Vector obj,JournalStandardAccount jsAccont,int insertRow){
			listData.add(jsAccont);
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
	
	class ButtomTablePmtProjectModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {
			if(col == 0 || col==1)
				return false;
			return true;
		}
	}
	
	public Object[] getSelectedObjects() {
		return null;
	}
	
	public void itemStateChanged(ItemEvent e) {
		m_table.clearTable();
	}
	
	class StringFormatted{
		private String valueText;
		
		public  StringFormatted(String valueText){
			this.valueText=valueText;
		}
		
		public double doubleValue(){
			if(valueText.equals(""))
				return 0;
			String tanpaKoma = purify(valueText,",");
			new Double(tanpaKoma).doubleValue();
			return new Double(tanpaKoma).doubleValue();
		}
		
		private String purify(String string,String pengotor){
			if(string.indexOf(pengotor)!=-1)
				return string.replaceAll(pengotor,"");
			else 
				return string;
		}
	}
	
	protected static class FormattedDoubleCellRenderer extends DoubleCellRenderer {
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
	}
	
	protected static class StandardFormatCellRenderer extends DefaultTableCellRenderer {
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
	
	private LookupPicker AccountComp;
	private javax.swing.JLabel AccountLabel;
	private javax.swing.JButton AddOprCostAccountBtn;
	private DatePicker ChequeDueDateDate;
	private javax.swing.JLabel ChequeDueDateLabel;
	private javax.swing.JLabel ChequeNoLabel;
	private javax.swing.JTextField ChequeNoText;
	private CurrencyPicker CurrComp;
	private javax.swing.JButton DeleteOprCostAccountBtn;
	private LookupDepartmentPicker DepartmentComp;
	private javax.swing.JLabel DepartmentLabel;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JLabel ExchRateLabel1;
	private javax.swing.JFormattedTextField ExchRateText;
	private javax.swing.JScrollPane OprCostAccountScrollPane;
	private javax.swing.JTextField PayToTxt;
	private javax.swing.JLabel PayToLabel;
	private javax.swing.JComboBox PaymentSourceCombo;
	private javax.swing.JLabel ReceiveFromLabel1;
	private javax.swing.JLabel ReceiveToLabel;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScrollPane;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JTextField SubmittedDateText;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private UnitPicker UnitCodeComp;
	private javax.swing.JLabel UnitCodeLabel;
	private javax.swing.JLabel VoucherDateLabel;
	private javax.swing.JLabel VoucherNoLabel;
	private javax.swing.JPanel PanelAtas;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel TopButtonPanel;
	private javax.swing.JPanel PanelKiri_2;
	private javax.swing.JPanel PanelKiri;
	private javax.swing.JPanel PanelCurrencyDKK;
	private javax.swing.JPanel jPanel1_2_2;
	private javax.swing.JPanel PanelKanan;
	private javax.swing.JPanel PanelOriginatorComp;
	private AssignPanel OriginatorComp;
	private javax.swing.JPanel PanelApprovedComp;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel PanelReceivedComp;
	private AssignPanel ReceivedComp;
	private javax.swing.JPanel PanelBawah;
	private javax.swing.JPanel BottomButtonPanel_2;
	private javax.swing.JPanel BottomButtonPanel;
	public int m_lastRow;
}
