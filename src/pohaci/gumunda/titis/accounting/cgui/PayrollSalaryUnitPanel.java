package pohaci.gumunda.titis.accounting.cgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.DoubleCellRenderer;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.Vchr_Salary;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryHoLoader;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryUnit;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtSlryUnitDet;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
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

public class PayrollSalaryUnitPanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	private PayrollPmtSlryUnit m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	private BottomTablePmtProject m_table;
	private LookupDepartmentPicker m_departmentComp;
	private JRadioButton m_singleRb = new JRadioButton("Single");
	private JRadioButton m_multipleRb = new JRadioButton("Multiple");
	private List rowList = new ArrayList();
	
	public PayrollSalaryUnitPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initComponents();
		addingListener();
		initNumberFormats();
		initComponents();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid); 
		setDefaultSignature(); 
		setEntity(new PayrollPmtSlryUnit());
		m_entityMapper = MasterMap.obtainMapperFor(PayrollPmtSlryUnit.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	
	private void initComponents() {	
		setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid, getJournalStandardSetting(IConstants.ATTR_PMT_BANK)));
		UnitCodeText = new JTextField();
		m_departmentComp = new LookupDepartmentPicker (m_conn, m_sessionid);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");        
		m_searchRefNoBtn = new JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new JButton(new ImageIcon("../images/filter.gif"));
		m_searchRefNoBtn = new JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new JButton(new ImageIcon("../images/filter.gif"));
		GridBagConstraints gridBagConstraints;
		JPanel PanelUtama = new JPanel();
		JPanel TopButtonPanel_2 = new JPanel();
		JPanel TopButtonPanel = new JPanel();
		m_newBtn = new JButton("New");
		m_editBtn = new JButton("Edit");
		m_saveBtn = new JButton("Save");
		m_deleteBtn = new JButton("Delete");
		m_cancelBtn = new JButton("Cancel");
		m_submitBtn = new JButton("Submit");
		JPanel PanelKiri_2 = new JPanel();
		JPanel PanelKanan_2 = new JPanel();
		PanelKanan = new JPanel();
		JLabel VoucherNoLabel = new JLabel();
		RefNoText = new JTextField();
		JLabel VoucherDateLabel = new JLabel();
		JLabel DescriptionLabel = new JLabel();
		DescriptionScroll = new JScrollPane();
		DescriptionTextArea = new JTextArea();
		JLabel RemarkLabel = new JLabel();
		RemarkScroll = new JScrollPane();
		RemarksTextArea = new JTextArea();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		PanelKiri = new JPanel();
		JLabel StatusLabel = new JLabel();
		JLabel SubmittedDateLabel = new JLabel();
		JLabel UnitCodeLabel = new JLabel();
		JLabel PaymentSourceLabel = new JLabel();
		AccountLabel = new JLabel();
		StatusText = new JTextField();
		JLabel PayToLabel = new JLabel();
		JLabel ChequeNoLabel = new JLabel();
		JLabel ChequeDueDateLabel = new JLabel();
		SubmittedDateText = new JTextField();
		PaytoText = new JTextField();
		ChequeNoText = new JTextField();
		PaymentSourceCombo = new JComboBox();
		ChequeDueDateDate = new pohaci.gumunda.titis.application.DatePicker();
		JPanel PanelOriginatorComp = new JPanel();
		JPanel PanelApprovedComp = new JPanel();
		JPanel PanelReceivedComp = new JPanel();
		JPanel PanelTable = new JPanel();
		JPanel ButtomButtonPanel_2 = new JPanel();
		JPanel ButtomButtonPanel = new JPanel();
		AddPayrollComponentPayableBtn = new JButton();
		DeletePayrollComponentPayableBtn = new JButton();
		PayrollComponentScrollPane = new JScrollPane();
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(650, 400));
		PanelUtama.setLayout(new BorderLayout());
		PanelUtama.setPreferredSize(new Dimension(650, 400));
		
		TopButtonPanel_2.setLayout(new BorderLayout());
		TopButtonPanel_2.setPreferredSize(new Dimension(650, 35));
		
		TopButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		TopButtonPanel.setPreferredSize(new Dimension(650, 35));
		m_searchRefNoBtn.setPreferredSize(new Dimension(21, 21));
		TopButtonPanel.add(m_searchRefNoBtn);
		m_printViewRefNoBtn.setPreferredSize(new Dimension(21, 21));
		TopButtonPanel.add(m_printViewRefNoBtn);
		
		m_newBtn.setMargin(new Insets(2, 2, 2, 2));
		m_newBtn.setPreferredSize(new Dimension(50, 20));
		TopButtonPanel.add(m_newBtn);
		
		m_editBtn.setMargin(new Insets(2, 2, 2, 2));
		m_editBtn.setPreferredSize(new Dimension(50, 20));
		TopButtonPanel.add(m_editBtn);
		
		m_deleteBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deleteBtn.setPreferredSize(new Dimension(50, 20));
		TopButtonPanel.add(m_deleteBtn);
		
		m_saveBtn.setMargin(new Insets(2, 2, 2, 2));
		m_saveBtn.setPreferredSize(new Dimension(50, 20));
		TopButtonPanel.add(m_saveBtn);
		
		m_cancelBtn.setMargin(new Insets(2, 2, 2, 2));
		m_cancelBtn.setPreferredSize(new Dimension(50, 20));
		TopButtonPanel.add(m_cancelBtn);
		
		m_submitBtn.setMargin(new Insets(2, 2, 2, 2));
		m_submitBtn.setPreferredSize(new Dimension(50, 20));
		TopButtonPanel.add(m_submitBtn);
		
		TopButtonPanel_2.add(TopButtonPanel, BorderLayout.WEST);
		PanelUtama.add(TopButtonPanel_2, BorderLayout.NORTH);
		PanelKiri_2.setLayout(new BorderLayout());
		PanelKiri_2.setPreferredSize(new Dimension(650, 400));
		
		PanelKanan_2.setLayout(new BorderLayout());
		PanelKanan_2.setPreferredSize(new Dimension(325, 400));
		
		PanelKanan.setLayout(new GridBagLayout());
		PanelKanan.setPreferredSize(new Dimension(325, 200));		
		VoucherNoLabel.setText("Voucher No");
		VoucherNoLabel.setPreferredSize(new Dimension(95, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKanan.add(VoucherNoLabel, gridBagConstraints);
		
		RefNoText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKanan.add(RefNoText, gridBagConstraints);
		
		VoucherDateLabel.setText("Voucher Date*");
		VoucherDateLabel.setPreferredSize(new Dimension(95, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKanan.add(VoucherDateLabel, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKanan.add(TransactionDateDate, gridBagConstraints);
		
		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new Dimension(95, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKanan.add(DescriptionLabel, gridBagConstraints);
		
		DescriptionScroll.setPreferredSize(new Dimension(200, 63));
		DescriptionTextArea.setColumns(60);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(3);
		DescriptionScroll.setViewportView(DescriptionTextArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKanan.add(DescriptionScroll, gridBagConstraints);
		
		RemarkLabel.setText("Remarks");
		RemarkLabel.setPreferredSize(new Dimension(95, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKanan.add(RemarkLabel, gridBagConstraints);
		
		RemarkScroll.setPreferredSize(new Dimension(200, 63));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(3);
		RemarkScroll.setViewportView(RemarksTextArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKanan.add(RemarkScroll, gridBagConstraints);
		
		JPanel emptypanel = new JPanel();
		emptypanel.setPreferredSize(new Dimension(200, 70));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKanan.add(emptypanel, gridBagConstraints);
		
		
		PanelKanan_2.add(PanelKanan, BorderLayout.WEST);		
		PanelKiri_2.add(PanelKanan_2, BorderLayout.CENTER);		
		PanelKiri.setLayout(new GridBagLayout());
		
		PanelKiri.setPreferredSize(new Dimension(350, 200));
		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(StatusLabel, gridBagConstraints);
		
		StatusText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(StatusText, gridBagConstraints);
		
		SubmittedDateLabel.setText("Submitted Date");
		SubmittedDateLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(SubmittedDateLabel, gridBagConstraints);
		
		SubmittedDateText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(SubmittedDateText, gridBagConstraints);
		
		PaymentSourceLabel.setText("Payment Source*");
		PaymentSourceLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(PaymentSourceLabel, gridBagConstraints);
		
		PaymentSourceCombo.setModel(new DefaultComboBoxModel(new String[] { "Bank", "Cash" }));
		PaymentSourceCombo.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(PaymentSourceCombo, gridBagConstraints);
		
		AccountLabel.setText("Account");
		AccountLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(AccountLabel, gridBagConstraints);
		
		accountComp().setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(accountComp(), gridBagConstraints);
		
		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(UnitCodeLabel, gridBagConstraints);
		
		UnitCodeText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(UnitCodeText, gridBagConstraints);
		
		PayToLabel.setText("Pay To");
		PayToLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(PayToLabel, gridBagConstraints);
		
		PaytoText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(PaytoText, gridBagConstraints);
		
		ChequeNoLabel.setText("Cheque No.");
		ChequeNoLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(ChequeNoLabel, gridBagConstraints);
		
		ChequeNoText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(ChequeNoText, gridBagConstraints);
		
		ChequeDueDateLabel.setText("Cheque Due Date");
		ChequeDueDateLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(ChequeDueDateLabel, gridBagConstraints);
		
		ChequeDueDateDate.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKiri.add(ChequeDueDateDate, gridBagConstraints);
		
		JPanel groupdeptPanel = new JPanel();
		ButtonGroup bg1 = new ButtonGroup();
		
		bg1.add(m_singleRb);
		bg1.add(m_multipleRb);
		m_singleRb.setSelected(true);
		
		groupdeptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Department",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11)));
		groupdeptPanel.add(m_singleRb);
		groupdeptPanel.add(m_multipleRb);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(new Label(" "), gridBagConstraints);
		
		groupdeptPanel.setPreferredSize(new Dimension(200, 70));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKiri.add(groupdeptPanel, gridBagConstraints);
		
		JLabel departmentlbl = new JLabel("Department *");
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(departmentlbl, gridBagConstraints);
		
		m_departmentComp.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		PanelKiri.add(m_departmentComp, gridBagConstraints);
		
		PanelKiri_2.add(PanelKiri, BorderLayout.WEST);
		PanelUtama.add(PanelKiri_2, BorderLayout.CENTER);
		
		PanelOriginatorComp.setLayout(new BorderLayout());
		PanelOriginatorComp.setPreferredSize(new Dimension(700, 90));
		
		OriginatorComp.setLayout(new GridBagLayout());		
		OriginatorComp.setBorder(BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new Dimension(279, 110));
		PanelOriginatorComp.add(OriginatorComp, BorderLayout.WEST);
		PanelOriginatorComp.add(PanelApprovedComp, BorderLayout.CENTER);
		
		PanelApprovedComp.setLayout(new BorderLayout());
		ApprovedComp.setLayout(new GridBagLayout());
		ApprovedComp.setBorder(BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new Dimension(279, 110));
		PanelApprovedComp.add(ApprovedComp, BorderLayout.WEST);
		PanelApprovedComp.add(PanelReceivedComp, BorderLayout.CENTER);
		
		PanelReceivedComp.setLayout(new BorderLayout());
		ReceivedComp.setLayout(new GridBagLayout());
		ReceivedComp.setBorder(BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new Dimension(279, 110));
		PanelReceivedComp.add(ReceivedComp, BorderLayout.WEST);
		
		PanelUtama.add(PanelOriginatorComp, BorderLayout.SOUTH);
		add(PanelUtama, BorderLayout.NORTH);
		PanelTable.setLayout(new BorderLayout());
		PanelTable.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		PanelTable.setPreferredSize(new Dimension(650, 250));
		ButtomButtonPanel_2.setLayout(new BorderLayout());
		ButtomButtonPanel_2.setPreferredSize(new Dimension(650, 35));
		
		ButtomButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		ButtomButtonPanel.setPreferredSize(new Dimension(650, 35));
		AddPayrollComponentPayableBtn.setText("Add");
		AddPayrollComponentPayableBtn.setMargin(new Insets(2, 2, 2, 2));
		AddPayrollComponentPayableBtn.setPreferredSize(new Dimension(50, 21));
		ButtomButtonPanel.add(AddPayrollComponentPayableBtn);
		
		DeletePayrollComponentPayableBtn.setText("Delete");
		DeletePayrollComponentPayableBtn.setMargin(new Insets(2, 2, 2, 2));
		DeletePayrollComponentPayableBtn.setPreferredSize(new Dimension(50, 21));
		ButtomButtonPanel.add(DeletePayrollComponentPayableBtn);
		
		ButtomButtonPanel_2.add(ButtomButtonPanel, BorderLayout.WEST);
		PanelTable.add(ButtomButtonPanel_2, BorderLayout.NORTH);
		PayrollComponentScrollPane.setPreferredSize(new Dimension(650, 215));
		m_table=new BottomTablePmtProject();
		PayrollComponentScrollPane.setViewportView( m_table);
		PanelTable.add(PayrollComponentScrollPane, BorderLayout.CENTER);
		add(PanelTable, BorderLayout.CENTER);
	}
	
	private void addingListener(){
		PaymentSourceCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);	
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		accountComp().addPropertyChangeListener("object",this);
		AddPayrollComponentPayableBtn.addActionListener(this);
		m_departmentComp.addPropertyChangeListener(this);
		DeletePayrollComponentPayableBtn.addActionListener(this);
		m_singleRb.addActionListener(this);
		m_multipleRb.addActionListener(this);
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
	}
	
	protected void clearAll() {	
		super.clearAll();
		clearForm();
		clearKomponen();
		m_table.clearTable();
		disableEditMode();
	}
	
	protected void doNew() {
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
		m_table.clearTable();
		StatusText.setText(entity().statusInString());
		rowList = new ArrayList();
	}
	
	public void doEdit() {
		super.doEdit();
		if (rowList.size()>0)
			setEnableButtonBawah(true);
	}
	
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == PaymentSourceCombo) {
			UnitCodeText.setText("");			
			rowList.clear();
			m_table.clearTable();
			updatePaymentSource();			
			
		}
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity!=null)
				new Vchr_Salary(m_entity,m_conn, m_sessionid);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			showSearchVoucherDialog();
		}
		else if (e.getSource() == AddPayrollComponentPayableBtn){
			ShowPayrollComponentPayable();
		}
		else if (e.getSource()==DeletePayrollComponentPayableBtn){
			int row = m_table.getSelectedRow();
			Boolean isDeleted = (Boolean) rowList.get(row);
			if (isDeleted.booleanValue()) {
				if (JOptionPane
						.showConfirmDialog(this, "Are you sure?",
								"Delete Confirmation Dialog",
								JOptionPane.YES_NO_OPTION) == 0) {
					deletePayrollComponentPayable();
				} else {
					return;
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
				}else{
					m_table.setValueAt("",i,8);					
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
	
	private void deletePayrollComponentPayable() {
		int row = m_table.getSelectedRow();
		int maxRow = m_table.getRowCount()-1;
		if(row>-1){
			if(row<(maxRow-1)){
				rowList.remove(row);
				m_table.removeData(row);
				m_table.updateSummary();    				
			}
		}else{
			JOptionPane.showMessageDialog(null,"Please select account you want to remove");
		}
	}
	
	private JournalStandard getJournalStandardSetting(String attribute) {
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(m_conn, m_sessionid, 
					IDBConstants.MODUL_ACCOUNTING);
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYROLL_PAYMENT_SALARY_UNIT, attribute);
		
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		return setting.getJournalStandard();
	}
	
	
	private void updatePaymentSource() {
		PanelKiri.remove(accountComp());
		PanelKiri.revalidate();
		PanelKiri.repaint();
		String receive = (String)PaymentSourceCombo.getSelectedItem();
		if (receive.equals("Cash"))
			setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));
		else
			setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid, getJournalStandardSetting(IConstants.ATTR_PMT_BANK)));
		accountComp().setPreferredSize(new Dimension(200, 18));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		PanelKiri.add(accountComp(), gridBagConstraints);
		PanelKiri.revalidate();
	}
	private void showSearchVoucherDialog() {
		SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame.getMainFrame(), "Search Voucher", m_conn, m_sessionid,new PayrollPmtSlryHoLoader(m_conn,PayrollPmtSlryUnit.class));    	 
		dlg.setVisible(true);
		if (dlg.selectedObj != null){    			
			doLoad(dlg.selectedObj);
		}
	}
	
	private void ShowPayrollComponentPayable() {
		String attribute ="";
		String paySource = (String)PaymentSourceCombo.getSelectedItem();
		if (accountComp().getObject() instanceof BankAccount) 
			attribute = IConstants.ATTR_PMT_BANK;			
		else if (accountComp().getObject() instanceof CashAccount) 
			attribute = IConstants.ATTR_PMT_CASH;			
		else if (paySource.equalsIgnoreCase("CASH"))
			attribute = IConstants.ATTR_PMT_CASH;
		else if (paySource.equalsIgnoreCase("BANK"))
			attribute = IConstants.ATTR_PMT_BANK;
		
		JournalStandardAccount[] jsacc = getJournalStandardSetting(attribute).getJournalStandardAccount();
		PayrollSalaryHOComp = new LookupPayrollCompPicker(m_conn, m_sessionid,jsacc);
		PayrollSalaryHOComp.done();
		
		if(PayrollSalaryHOComp.getObject()!=null){
			int oldRow = m_lastRow;
			rowList.add(m_lastRow, new Boolean(true));
			Vector vector=new Vector();
			JournalStandardAccount acc=(JournalStandardAccount)PayrollSalaryHOComp.getObject();
			vector.addElement(new Integer(oldRow+1));
			vector.addElement(acc.getAccount().getCode());
			vector.addElement(acc.getAccount().getName());
			vector.addElement(null);
			vector.addElement(new Double(0));
			vector.addElement(new Double(0));  
			vector.addElement(baseCurrency);  
			vector.addElement(new Double(1));  
			if (m_singleRb.isSelected())
				if (m_departmentComp.getObject() instanceof Organization) {
					Organization org = (Organization) m_departmentComp.getObject();
					vector.addElement(org);
				}
			m_table.addData(vector, acc, oldRow);
		}
	}
	
	public void LoadDetail(long index){
		GenericMapper mapper2=MasterMap.obtainMapperFor(PayrollPmtSlryUnitDet.class);
		mapper2.setActiveConn(m_conn); 
		String value = new Long(index).toString();
		List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_PAYROLL_PMT_SALARY_UNIT+"="+value);
		m_table.clearTable();
		PayrollPmtSlryUnitDet detail;
		Vector temp1;
		Account acc;
		rowList = new ArrayList();
		for(int i=0;i<detailList.size();i++){
			int oldRow = m_lastRow;
			temp1=new Vector();
			detail=(PayrollPmtSlryUnitDet)detailList.get(i);
			acc=(Account)detail.getAccount();
			JournalStandardAccount journalStandardAccount = null;
			try {
				AccountingBusinessLogic accLogic = new AccountingBusinessLogic(m_conn);
				JournalStandardSetting[] jss = accLogic.getJournalStandardSetting(
						m_sessionid, IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT,
						IConstants.PAYROLL_PAYMENT_SALARY_UNIT);
				JournalStandard journalStandard;
				JournalStandardAccount[] JSAccount;
				
				for (int j = 0; j < jss.length; j++) {
					journalStandard = (JournalStandard) jss[j].getJournalStandard();
					JSAccount = accLogic.getJournalStandardAccount(m_sessionid,
							IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT, journalStandard
							.getIndex());
					for (int f = 0; f < JSAccount.length; f++) {
						Account account = JSAccount[f].getAccount();
						if(acc.getIndex() == account.getIndex()){
							journalStandardAccount= JSAccount[f];
							break;
						}
						
					}
				}
				
				
				/*CashBankAccount cba = null;
				 if (SubsidiaryAccSet.equals("Bank")){			
				 cba = (CashBankAccount) SetBankAccountToTable(detail, accLogic);				
				 }else if (SubsidiaryAccSet.equals("Cash")){
				 cba = (CashBankAccount) SetCashAccountToTable(detail, accLogic);
				 }*/
				
				if(journalStandardAccount.isHidden())
					rowList.add(i, new Boolean(false));
				else
					rowList.add(i, new Boolean(true));
				
				temp1.addElement(new Integer(i+1));
				temp1.addElement(acc.getCode());
				temp1.addElement(acc.getName());
				String SubsidiaryAccSet = getSubsidiaryByindex(acc);
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
				if (detail.getBalanceCode()==0){//debir
					temp1.addElement(new Double(detail.getAccValue()));
					temp1.addElement(new Double(0));
				}else if (detail.getBalanceCode()==1){//kredit
					temp1.addElement(new Double(0));
					temp1.addElement(new Double(detail.getAccValue()));
				}
				temp1.addElement(detail.getCurrency());
				temp1.addElement(new Double(detail.getExchangeRate()));
				if (detail.getDepartment()!=null)
					temp1.addElement(detail.getDepartment());
				
				m_table.addData(temp1, journalStandardAccount,oldRow);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	private void SetCashAccountToTable(PayrollPmtSlryUnitDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCompanyLoanToTable(PayrollPmtSlryUnitDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetProjectToTable(PayrollPmtSlryUnitDet detail, Vector temp1, ProjectBusinessLogic  proLogic) {
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
	
	
	private void SetBankAccountToTable(PayrollPmtSlryUnitDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCustomerToTable(PayrollPmtSlryUnitDet detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Customer cust = null;
		cust = proLogic.getCustomerByIndex(detail.getSubsidiAry());
		temp1.addElement(cust);
	}
	
	private void SetPartnerToTable(PayrollPmtSlryUnitDet detail, Vector temp1, ProjectBusinessLogic proLogic) {
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
	
	private void SetEmployeeToTable(PayrollPmtSlryUnitDet detail, Vector temp1, HRMBusinessLogic hrmLogic) {
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
	
	
	/*private Object SetCashAccountToTable(PayrollPmtSlryUnitDet detail, AccountingBusinessLogic accLogic) {
	 CashAccount cAcc = null;
	 try {
	 cAcc = accLogic.getCashAccountByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 return cAcc;
	 }
	 
	 private Object SetBankAccountToTable(PayrollPmtSlryUnitDet detail, AccountingBusinessLogic accLogic) {
	 BankAccount bAcc = null;
	 try {
	 bAcc = accLogic.getBankAccountByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 return bAcc;
	 }*/
	
	private void setEnableButtonBawah(boolean bool){
		AddPayrollComponentPayableBtn.setEnabled(bool);
		DeletePayrollComponentPayableBtn.setEnabled(bool);
		m_table.setEnabled(bool);
	}
	
	protected void clearForm() {
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
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
	}
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	protected boolean cekValidity() {		
		validityMsgs.clear();
		if (TransactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (cekdepartmentdetail())
			addInvalid("Department detail is still empty");
		if (DescriptionTextArea.getText().equals(""))
			addInvalid("Description	 must be selected");	
		detailAccountOperation();
		PayrollPmtSlryUnitDet[] temp = entity().getPayrollPmtSlryUnitDet();
		if (temp.length==0)
			addInvalid("Account Detail must be filled");
		
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
	ArrayList validityMsgs = new ArrayList();
	private void addInvalid(String string) {
		validityMsgs.add(string);
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
	protected void gui2entity() {
		String paySource = (String) PaymentSourceCombo.getSelectedItem();
		entity().setPaymentSource(paySource);
		Object objacc = AccountComp.getObject();
		if (objacc instanceof BankAccount) {
			BankAccount bank = (BankAccount) objacc;
			entity().setBankAccount(bank);
			entity().setCashAccount(null);
			entity().setUnit(bank.getUnit());
		}
		else if (objacc instanceof CashAccount) {
			CashAccount cash = (CashAccount) objacc;
			entity().setCashAccount(cash);
			entity().setBankAccount(null);
			entity().setUnit(cash.getUnit());
		} 
		entity().setChequeNo(ChequeNoText.getText());		
		entity().setPayto(PaytoText.getText());
		
		if (m_singleRb.isSelected())
			entity().setDepartmentgroup(0);
		else if (m_multipleRb.isSelected())
			entity().setDepartmentgroup(1);
		entity().setDepartment((Organization) m_departmentComp.getObject());
		
		entity().setChequeDueDate(ChequeDueDateDate.getDate());		
		entity().setTransactionDate(TransactionDateDate.getDate());		
		entity().setRemarks(RemarksTextArea.getText());
		
		entity().transTemplateRead(
				this.OriginatorComp, this.ApprovedComp,
				this.ReceivedComp, this.RefNoText,
				this.DescriptionTextArea
		);
		entity().setCurrency(baseCurrency);
		
		detailAccountOperation();
	}
	
	private void detailAccountOperation() {
		PayrollPmtSlryUnitDet[] temp=new PayrollPmtSlryUnitDet[m_table.getDataCount()];
		if (m_table.getRowCount()>=0){
			
			for (int i=0;i<m_table.getDataCount();i++){
				JournalStandardAccount journal = (JournalStandardAccount)m_table.getListData().get(i);
				Account acc = journal.getAccount();//(Account)m_table.getListData().get(i);
				temp[i]=new PayrollPmtSlryUnitDet();
				temp[i].setAccount(acc);
				
				String SubsidiaryAccSet = getSubsidiaryByindex(acc);				
				Object value = m_table.getValueAt(i,3);
				if (SubsidiaryAccSet.equals("Employee")){	
					if (value instanceof Employee) {
						Employee emp = (Employee) value;
						temp[i].setSubsidiAry(emp.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Partner"))){
					if (value instanceof Partner) {
						Partner partn = (Partner) value;
						temp[i].setSubsidiAry(partn.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Customer"))){
					if (value instanceof Customer) {
						Customer cust = (Customer) value;
						temp[i].setSubsidiAry(cust.getIndex());
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");
				}else if ((SubsidiaryAccSet.equals("Cash"))){
					if (value instanceof CashAccount) {
						CashAccount cash = (CashAccount) value;
						temp[i].setSubsidiAry(cash.getIndex());
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");					
				}else if ((SubsidiaryAccSet.equals("Bank"))){
					if (value instanceof BankAccount) {
						BankAccount bank = (BankAccount) value;
						temp[i].setSubsidiAry(bank.getIndex());						
					}else
						addInvalid("Subsidiary " + acc.getName() +  " must selected");				
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
				
				if (journal.getBalanceAsString().equalsIgnoreCase("Debit")){
					temp[i].setAccValue( ((Double) m_table.getValueAt(i, 4) ).doubleValue());
					temp[i].setBalanceCode(0);
				}else if (journal.getBalanceAsString().equalsIgnoreCase("Credit")){
					temp[i].setAccValue( ((Double) m_table.getValueAt(i, 5) ).doubleValue());
					temp[i].setBalanceCode(1);
				}
				temp[i].setCurrency((Currency)m_table.getValueAt(i,6));
				if(m_table.getValueAt(i, 6)!=null)
					temp[i].setExchangeRate( ((Double) m_table.getValueAt(i, 7) ).doubleValue());
				
				Object objdept = m_table.getValueAt(i,8);
				if (objdept instanceof Organization) {
					Organization org = (Organization) objdept;
					temp[i].setDepartment(org);
				}else{
					temp[i].setDepartment(null);
				}
			}
			entity().setPayrollPmtSlryUnitDet(temp);
		}
	}
	
	protected void entity2gui() {
		if (entity().getBankAccount()!=null){
			PaymentSourceCombo.setSelectedItem("Bank");
			AccountComp.setObject(entity().getBankAccount());
		}else if (entity().getCashAccount()!=null){
			PaymentSourceCombo.setSelectedItem("Cash");
			AccountComp.setObject(entity().getCashAccount());
		}else {
			PaymentSourceCombo.setSelectedItem("Bank");
			AccountComp.setObject(null);
		}
		
		PaytoText.setText(entity().getPayto());
		
		if (entity().getDepartmentgroup()==0)
			m_singleRb.setSelected(true);
		else if (entity().getDepartmentgroup()==1)
			m_multipleRb.setSelected(true);
		m_departmentComp.setObject(entity().getDepartment());
		
		ChequeNoText.setText(entity().getChequeNo());
		ChequeDueDateDate.setDate(entity().getChequeDueDate());
		TransactionDateDate.setDate(entity().getTransactionDate());
		RefNoText.setText(entity().getReferenceNo());
		if(entity().getUnit()!=null)
			UnitCodeText.setText(entity().getUnit().toString());
		
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
		
		if(entity().getTransactionDate()!=null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else{
			TransactionDateDate.setDate(new Date());
		}
		
		LoadDetail(entity().getIndex());
	}
	
	protected void disableEditMode() {
		StatusText.setEditable(false);
		m_departmentComp.setEnabled(false);
		m_singleRb.setEnabled(false);
		m_multipleRb.setEnabled(false);
		SubmittedDateText.setEditable(false);
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
		setEnableButtonBawah(false);
		RefNoText.setEditable(false);
		//UnitCodeComp.setEnabled(false);
		UnitCodeText.setEditable(false);
		this.PaytoText.setEditable(false);
	}
	
	protected void enableEditMode() {
		m_singleRb.setEnabled(true);
		m_multipleRb.setEnabled(true);
		if (m_singleRb.isSelected())
			m_departmentComp.setEnabled(true);
		else 
			m_departmentComp.setEnabled(false);
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
		setEnableButtonBawah(true);
		//setEnableButtonBawah(true);
		//UnitCodeComp.setEnabled(true);
		UnitCodeText.setEditable(false);
		this.PaytoText.setEditable(true);
	}
	
	protected Object createNew() {
		PayrollPmtSlryUnit a  = new PayrollPmtSlryUnit();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}
	
	void setEntity(Object m_entity) {
		PayrollPmtSlryUnit oldEntity = this.m_entity;
		if (oldEntity!=null)
			oldEntity.removePropertyChangeListener(this);		
		this.m_entity = (PayrollPmtSlryUnit)m_entity;
		this.m_entity.addPropertyChangeListener(this);		
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName()))
			readEntityState();
		if (evt.getSource() == accountComp()){
			if (evt.getNewValue() == null){				
			} else if (evt.getNewValue() instanceof BankAccount){
				JournalStandardAccount jsa = getSelectedJournalStandardAccount();
				addIntoTable(jsa);
				if (rowList.size()>0)	
					setEnableButtonBawah(true);
			} else if (evt.getNewValue() instanceof CashAccount){
				JournalStandardAccount jsa = getSelectedJournalStandardAccount();
				addIntoTable(jsa);
				if (rowList.size()>0)	
					setEnableButtonBawah(true);
			}
		}
		if (evt.getSource()==m_departmentComp){
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
	}
	
	private void addIntoTable(JournalStandardAccount jsa ) {
		CashBankAccount account = getAccount();		
		Vector vector = new Vector();
		vector.addElement(new Integer(1));
		vector.addElement(account.getAccount().getCode());
		vector.addElement(account.getAccount().getName());
		vector.addElement(account);
		vector.addElement(new Double(0));
		vector.addElement(new Double(0));  
		vector.addElement(account.getCurrency());  
		vector.addElement(new Double(1));
		if (m_singleRb.isSelected())
			if (m_departmentComp.getObject() instanceof Organization) {
				Organization org = (Organization) m_departmentComp.getObject();
				vector.addElement(org);
			}
		
		Boolean[] isDeleted = (Boolean[]) rowList.toArray(new Boolean[rowList.size()]);
		int findRow = -1;
		for(int i=0; i<isDeleted.length; i++){
			if(!isDeleted[i].booleanValue()){
				findRow = i;
				break;
			}
		}	
		if(findRow==-1){
			rowList.add(new Boolean(false));
			int row = m_lastRow;
			m_table.addData(vector, jsa, row);
		}else {
			rowList.remove(findRow);
			m_table.removeData(findRow);
			rowList.add(findRow, new Boolean(false));
			m_table.addData(vector, jsa, findRow);
		}
	}
	
	private JournalStandardAccount getSelectedJournalStandardAccount() {
		if (accountComp().getObject() instanceof BankAccount) {
			BankAccount acc= (BankAccount) accountComp().getObject();
			UnitCodeText.setText(acc.getUnit().toString());	
			
			JournalStandardAccount[] jsas = getJournalStandardSetting(IConstants.ATTR_PMT_BANK).getJournalStandardAccount();
			for(int i=0; i<jsas.length; i++){
				if(jsas[i].getAccount().getIndex()==acc.getAccount().getIndex())
					return jsas[i];
			}				
		}else if (accountComp().getObject() instanceof CashAccount) {
			CashAccount acc = (CashAccount) accountComp().getObject();
			UnitCodeText.setText(acc.getUnit().toString());
			JournalStandardAccount[] jsas = getJournalStandardSetting(IConstants.ATTR_PMT_CASH).getJournalStandardAccount();			
			for(int i=0; i<jsas.length; i++){
				if(jsas[i].getAccount().getIndex()==acc.getAccount().getIndex())
					return jsas[i];
			}	
		}		
		return null;
	}
	
	private CashBankAccount getAccount() {
		CashBankAccount acc = (CashBankAccount) accountComp().getObject();
		return acc;
	}
	
	PayrollPmtSlryUnit entity() {
		return m_entity;
	}
	
	protected void setAccountComp(LookupPicker accountComp) {
		LookupPicker old = AccountComp;
		if (old!=null)
			old.removePropertyChangeListener("object",this);		
		AccountComp = accountComp;
		AccountComp.addPropertyChangeListener("object",this);
	}
	
	private LookupPicker accountComp() {
		return AccountComp;
	}
	
	protected void doSubmit() {
		ArrayList validityMsgs = new ArrayList();
		validityMsgs.clear();
		if (accountComp().getObject() == null)
			validityMsgs.add("Account source must be selected");
		if(!m_table.isBalance())
			validityMsgs.add("It probably results in an unbalance transaction. Please check transaction amount and exchange rate!");			
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
	
	
	protected void doSave() {
		if (!cekValidity()) return;
		
		super.doSave();
		entityGetHisIndex();		
		LoadDetail(entity().getIndex());
		if(!m_saveBtn.isEnabled())
			setEnableButtonBawah(false);
	}
	
	private void entityGetHisIndex() {
		AccountingSQLSAP sql=new AccountingSQLSAP();
		GenericMapper mapper2=MasterMap.obtainMapperFor(PayrollPmtSlryUnitDet.class);
		mapper2.setActiveConn(m_conn);
		try {
			if ((new Long(entity().getIndex()).toString().equalsIgnoreCase("0") ))
			{	
				long index = sql.getMaxIndex(IDBConstants.TABLE_PAYROLL_PMT_SALARY_UNIT, m_conn);
				entity().setIndex(index);
			}
			
			if (entity().getPayrollPmtSlryUnitDet()!= null){
				PayrollPmtSlryUnitDet[] temp=entity().getPayrollPmtSlryUnitDet();
				mapper2.doDeleteByColumn(IDBConstants.ATTR_PAYROLL_PMT_SALARY_UNIT, 
						new Long(entity().getIndex()).toString());
				for (int i=0;i<temp.length;i++){
					temp[i].setPayrollPmtSlryUnit((PayrollPmtSlryUnit)entity());
					mapper2.doInsert(temp[i]);  
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getSubsidiaryByindex(Account acc){
		AccountingBusinessLogic logic = new AccountingBusinessLogic(m_conn);
		SubsidiaryAccountSetting accSet = null;
		try{
			accSet = logic.getSubsidiaryAccountSettingByIndex(m_sessionid,
					IDBConstants.MODUL_MASTER_DATA,acc.getIndex());
		}catch (Exception ex){			
		}
		if (accSet!=null)
			return accSet.getSubsidiaryAccount();
		else
			return "";
	}
	
	protected void tableStopCellEditing() {
		m_table.stopCellEditing();
	}
	
	protected class MyTableModelListener implements TableModelListener{
		
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			Object obj = e.getSource();
			if(obj instanceof DefaultTableModel){
				DefaultTableModel model = (DefaultTableModel) obj;
				int maxRow = model.getRowCount();
				if(((row>-1)&&(row<(maxRow-1)))&& (col==4||col==5 || col == 7) && (row==m_table.getSelectedRow())){	
					Boolean isDeleted = (Boolean) rowList.get(row);
					if(isDeleted.booleanValue())
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
		BottomTableRcvOthersModel model = new BottomTableRcvOthersModel();
		ArrayList listData=new ArrayList();
		protected BottomTablePmtProject() {
			model.addColumn("No");
			model.addColumn("Account");
			model.addColumn("Description");
			model.addColumn("Subsidiary");
			model.addColumn("Debit");
			model.addColumn("Credit");
			model.addColumn("Currency");
			model.addColumn("ExchangeRate");
			model.addColumn("Department");
			setModel(model);    
			
			getColumnModel().getColumn(0).setPreferredWidth(40);
			getColumnModel().getColumn(0).setMaxWidth(40);
			clearTable();
			
			getModel().addTableModelListener(new MyTableModelListener());
		}
		
		public void updateSummary(){
			int maxRow = getRowCount()-1;		
			double totDebit = getTotDebit(true);	
			double totCredit = getTotCredit(true);
			
			double balance = totDebit - totCredit;
			int calcRow = -1;
			Boolean[] isDeleted =  (Boolean[]) rowList.toArray(new Boolean[rowList.size()]);
			for(int i=0; i<isDeleted.length; i++){
				if(!isDeleted[i].booleanValue()){
					calcRow = i;
					break;
				}
			}
			
			if (calcRow > -1) {
				JournalStandardAccount journal = (JournalStandardAccount) listData
				.get(calcRow);
				if(journal.getBalance()==0)
					setValueAt(new Double(balance), calcRow, 4);
				else
					setValueAt(new Double(balance), calcRow, 5);
			}
			
			totDebit = getTotDebit(false);
			totCredit = getTotCredit(false);
			
			setValueAt(new Double(totCredit), maxRow, 5);						
			setValueAt(new Double(totDebit), maxRow, 4);
		}
		
		private double getTotCredit(boolean check) {
			int maxRow = getRowCount()-1;
			int maxTotalRow = maxRow - 2;		
			double totCredit = 0;	
			for(int i=0; i<=maxTotalRow; i++){
				Boolean isDeleted = new Boolean(true);
				if(check)
					isDeleted = (Boolean) rowList.get(i);
				if (isDeleted.booleanValue()) {
					if (getValueAt(i, 6) instanceof Currency) {
						Currency curr = (Currency) getValueAt(i, 6);
						if (!curr.getSymbol().equals(baseCurrency.getSymbol())) {
							double excRate = ((Double) getValueAt(i, 7))
							.doubleValue();
							totCredit += ((Double) getValueAt(i, 5))
							.doubleValue()
							* excRate;
						} else {
							totCredit += ((Double) getValueAt(i, 5))
							.doubleValue();
						}
					}
				}
			}	
			return totCredit;
		}
		
		private double getTotDebit(boolean check) {
			int maxRow = getRowCount()-1;
			int maxTotalRow = maxRow - 2;			
			double totDebit = 0;			
			for (int i = 0; i <= maxTotalRow; i++) {
				Boolean isDeleted = new Boolean(true);
				if(check)
					isDeleted = (Boolean) rowList.get(i);
				if (isDeleted.booleanValue()) {
					if (getValueAt(i, 6) instanceof Currency) {
						Currency curr = (Currency) getValueAt(i, 6);
						if (!curr.getSymbol().equals(baseCurrency.getSymbol())) {
							double excRate = ((Double) getValueAt(i, 7))
							.doubleValue();
							totDebit += ((Double) getValueAt(i, 4))
							.doubleValue()
							* excRate;
						} else {
							totDebit += ((Double) getValueAt(i, 4))
							.doubleValue();
						}
					}
				}
			}
			return totDebit;
		}
		
		public void updateCurrency(Currency curr){			
			for(int i=0; i<getRowCount(); i++){
				if (i!=(getRowCount()-2))
					setValueAt(new String(curr.toString()), i, 6);	
			}			
		}
		
		public boolean cekDebitCredit(){
			int maxRow = getRowCount()-1;		
			int maxTotalRow = maxRow - 2;
			double debit = 0;
			double credit = 0;	
			for(int i=0; i<=maxTotalRow; i++){	
				debit = ((Double)getValueAt(i,3)).doubleValue();
				credit = ((Double)getValueAt(i,4)).doubleValue();
				if (debit>0 && credit>0)
					return true;
			}				
			return false;
		}
		
		
		public boolean cekTotalDebitCredit(){
			int maxRow = getRowCount()-1;
			System.err.println(getValueAt(maxRow,3).toString() + " = " + getValueAt(maxRow,4).toString());
			if (getValueAt(maxRow,3).toString().equals(getValueAt(maxRow,4).toString()))
				return true;
			return false;
		}
		
		public boolean isBalance(){
			int maxRow = getRowCount()-1;
			if (getValueAt(maxRow,4).toString().equals(getValueAt(maxRow,5).toString()))
				return true;
			return false;
		}
		
		public TableCellEditor getCellEditor(int row, int col) {
			
			String currSymbol = "";
			Currency curr = null;
			if (getValueAt(row,6) instanceof Currency) {
				curr = (Currency) getValueAt(row,6);	
				currSymbol = curr.getSymbol();
			}
			Boolean isDeleted = (Boolean) rowList.get(row);
			if (row <= getRowCount()-3 && isDeleted.booleanValue()){	
				JournalStandardAccount journal = (JournalStandardAccount)listData.get(row);				
				if (col == 4 && journal.getBalanceAsString().equalsIgnoreCase(JournalStandardAccount.STR_DEBET))					
					return new FormattedDoubleCellEditor(JTextField.LEFT);	
				if (col == 5 && journal.getBalanceAsString().equalsIgnoreCase(JournalStandardAccount.STR_CREDIT))					
					return new FormattedDoubleCellEditor(JTextField.LEFT);
				if (col == 6 ){
					/*Boolean isDeleted = (Boolean) rowList.get(row);
					 if(isDeleted.booleanValue())*/
					return new CurrencyCellEditor( pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
							"", m_conn ,m_sessionid);
				}
				if (col == 7 )
					if( !currSymbol.equals(baseCurrency.getSymbol()) && !currSymbol.equals(""))					
						return new FormattedDoubleCellEditor(JTextField.RIGHT);
				if (col ==8 && m_multipleRb.isSelected())
					return new OrganizationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
			}
			else if (row <= getRowCount()-3 && !isDeleted.booleanValue()){
				if (col ==8 && m_multipleRb.isSelected())
					return new OrganizationCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
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
			return null;
		}
		
		public TableCellRenderer getCellRenderer(int row, int column) {
			
			if(column==0)
				return new BaseTableCellRenderer();
			if(column>0){
				int maxRow = getRowCount()-1;
				if(row>=(maxRow-1) &&(row<=maxRow)){
					if(column==3)
						return new StandardFormatCellRenderer(Font.BOLD,JLabel.RIGHT);
					if(column==4 || column==5)
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);					
				}else{				
					JournalStandardAccount journal = (JournalStandardAccount)listData.get(row);
					if(column==4)
						if(journal.getBalanceAsString().equalsIgnoreCase(JournalStandardAccount.STR_DEBET))
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);	
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					if(column==5 ) 
						if(journal.getBalanceAsString().equalsIgnoreCase(JournalStandardAccount.STR_CREDIT))
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);	
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);	
					if(column==6 )
						return new StandardFormatCellRenderer(Font.PLAIN, JLabel.CENTER);
					if(column==7 )
						return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					
				}			
			}
			return new StandardFormatCellRenderer(Font.PLAIN, JLabel.CENTER);
		}
		
		public void clearTable() {				
			listData.clear();
			DefaultTableModel model = (DefaultTableModel) getModel();
			model.setRowCount(0);
			model.addRow(new Object[]{null, null, null, null,null, null, null});
			model.addRow(new Object[]{null, null, null, "TOTAL", new Double(0),new Double(0),null});			
			m_lastRow = 0;
		}
		
		public void addData(Vector obj,JournalStandardAccount acc,int insertRow){
			listData.add(insertRow, acc);
			model.insertRow(insertRow,obj);
			/*if (CurrComp.getObject()!=null)
			 updateCurrency((Currency)CurrComp.getObject());*/
			m_lastRow++;
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
			for(int i=0; i<m_lastRow; i++){
				Integer val = (Integer)getValueAt(i, 0);				
				if(val.intValue()!=i+1){
					setValueAt(new Integer(i+1), i, 0);
				}
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
	
	class BottomTableRcvOthersModel extends DefaultTableModel{
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {			
			int maxRow = getRowCount();	
			if (row < maxRow - 1) {
				/*if (row < rowList.size()) {
				 Boolean isDeleted = (Boolean) rowList.get(row);
				 if (!isDeleted.booleanValue()){
				 //return false;
				  }
				  }*/
				if ((col == 0 || col == 1 || col == 2) || row > (maxRow - 3))
					return false;
				return true;
			}
			return false;
		}
		
		public void setValueAt(Object aValue, int row, int column) {
			if (column==5)
				if (aValue instanceof Currency){
					Currency curr = (Currency) aValue;
					if (curr.getIsBase()){
						setValueAt(new Double(1.0),row,6);
					}				
				}
			super.setValueAt(aValue, row, column);
		}
	}
	
	private LookupPicker AccountComp;
	private JLabel AccountLabel;
	private pohaci.gumunda.titis.application.DatePicker ChequeDueDateDate;
	private JScrollPane DescriptionScroll;
	private JTextArea DescriptionTextArea;
	private JComboBox PaymentSourceCombo;
	private JTextField PaytoText,RefNoText,StatusText,SubmittedDateText,ChequeNoText;
	private JScrollPane RemarkScroll;
	private JTextArea RemarksTextArea;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private JTextField UnitCodeText;
	private JPanel PanelKiri,PanelKanan;
	private AssignPanel OriginatorComp,ApprovedComp,ReceivedComp;
	private JScrollPane PayrollComponentScrollPane;
	private JButton AddPayrollComponentPayableBtn,DeletePayrollComponentPayableBtn;
	private LookupPayrollCompPicker PayrollSalaryHOComp;
}
