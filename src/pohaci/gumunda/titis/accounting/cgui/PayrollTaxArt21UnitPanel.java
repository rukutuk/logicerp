/*
 * Panel1.java
 *
 * Created on February 8, 2007, 8:07 PM
 */

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

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.BorderFactory;
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
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21Unit;
import pohaci.gumunda.titis.accounting.entity.PayrollPmtTax21UnitDet;
import pohaci.gumunda.titis.accounting.entity.Unit;
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

public class PayrollTaxArt21UnitPanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	private PayrollPmtTax21Unit m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	private JRadioButton m_singleRb = new JRadioButton("Single");
	private JRadioButton m_multipleRb = new JRadioButton("Multiple");
	private LookupDepartmentPicker m_departmentComp;
	private List journalStandardList = new ArrayList();
	private List rowList = new ArrayList();
	
	private buttomTabblePmtProject m_table;
	public PayrollTaxArt21UnitPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		getJournalStandardSetting();
		initNumberFormats();
		initComponents();
		//enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid); 
		setDefaultSignature(); 
		setEntity(new PayrollPmtTax21Unit());
		m_entityMapper = MasterMap.obtainMapperFor(PayrollPmtTax21Unit.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	private void getJournalStandardSetting() {
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(m_conn, m_sessionid, 
					IDBConstants.MODUL_ACCOUNTING);
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.PAYROLL_PAYMENT_TAX21_UNIT);
		
		Iterator iterator = journalStdList.iterator();
		journalStandardList = new ArrayList();
		while(iterator.hasNext()){
			JournalStandardSetting jss = (JournalStandardSetting) iterator.next();
			
			journalStandardList.add(jss.getJournalStandard());
		}
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	
	private void initComponents() {
		setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid, IConstants.PAYROLL_PAYMENT_TAX21_UNIT));
		//UnitCodeComp = new LookupUnitPicker(m_conn, m_sessionid);
		m_departmentComp = new LookupDepartmentPicker (m_conn, m_sessionid);
		m_unitpicker = new UnitPicker(m_conn,m_sessionid);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");        
		m_searchRefNoBtn = new JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new JButton(new ImageIcon("../images/filter.gif"));        
		//====================================================
		GridBagConstraints gridBagConstraints;
		JPanel panelUtamaAtas = new JPanel();
		JPanel TopButtonPanel = new JPanel();
		JPanel jPanel1_1_1 = new JPanel();        
		m_newBtn = new JButton();
		m_editBtn = new JButton();
		m_deleteBtn = new JButton();
		m_saveBtn = new JButton();
		m_cancelBtn = new JButton();
		m_submitBtn = new JButton();
		JPanel panelAtas = new JPanel();
		JPanel jPanel1_2_2 = new JPanel();
		panelKomponenKANAN = new JPanel();
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
		panelKomponenKIRI = new JPanel();
		JLabel StatusLabel = new JLabel();
		JLabel SubmittedDateLabel = new JLabel();
		JLabel UnitCodeLabel = new JLabel();
		JLabel PaymentSourceLabel = new JLabel();
		JLabel AccountLabel = new JLabel();
		StatusText = new JTextField();
		JLabel PayToLabel = new JLabel();
		JLabel ChequeNoLabel = new JLabel();
		JLabel ChequeDueDateLabel = new JLabel();
		SubmittedDateText = new JTextField();
		PaytoText = new JTextField();
		ChequeNoText = new JTextField();
		PaymentSourceCombo = new JComboBox();
		ChequeDueDateDate = new pohaci.gumunda.titis.application.DatePicker();
		JPanel panel4OriginatorComp = new JPanel();
		JPanel panel4ApprovedComp = new JPanel();
		JPanel panel4ReceivedComp = new JPanel();
		JPanel panelUtamaBawah = new JPanel();
		JPanel panel4Add_Delete = new JPanel();
		JPanel ButtomButtonPanel = new JPanel();
		AddTax21ComponentBtn = new JButton();
		DeleteTax21ComponentBtn = new JButton();
		//SaveTax21ComponentBtn = new JButton();
		scrollPane = new JScrollPane();
		//Tax21ComponentTable = new JTable();
		
		setLayout(new BorderLayout());
		
		setPreferredSize(new Dimension(700, 500));
		panelUtamaAtas.setLayout(new BorderLayout());
		
		panelUtamaAtas.setPreferredSize(new Dimension(650, 400));
		TopButtonPanel.setLayout(new BorderLayout());
		
		TopButtonPanel.setPreferredSize(new Dimension(650, 35));
		jPanel1_1_1.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		
		jPanel1_1_1.setPreferredSize(new Dimension(650, 35));
		
		m_searchRefNoBtn.setPreferredSize(new Dimension(21, 21));
		jPanel1_1_1.add(m_searchRefNoBtn);
		
		m_printViewRefNoBtn.setPreferredSize(new Dimension(21, 21));
		jPanel1_1_1.add(m_printViewRefNoBtn);
		
		m_newBtn.setText("New");
		m_newBtn.setMargin(new Insets(2, 2, 2, 2));
		m_newBtn.setPreferredSize(new Dimension(50, 21));
		jPanel1_1_1.add(m_newBtn);
		
		m_editBtn.setText("Edit");
		m_editBtn.setMargin(new Insets(2, 2, 2, 2));
		m_editBtn.setPreferredSize(new Dimension(50, 21));
		jPanel1_1_1.add(m_editBtn);
		
		m_deleteBtn.setText("Delete");
		m_deleteBtn.setMargin(new Insets(2, 2, 2, 2));
		m_deleteBtn.setPreferredSize(new Dimension(50, 21));
		jPanel1_1_1.add(m_deleteBtn);
		
		m_saveBtn.setText("Save");
		m_saveBtn.setMargin(new Insets(2, 2, 2, 2));
		m_saveBtn.setPreferredSize(new Dimension(50, 21));
		jPanel1_1_1.add(m_saveBtn);
		
		m_cancelBtn.setText("Cancel");
		m_cancelBtn.setMargin(new Insets(2, 2, 2, 2));
		m_cancelBtn.setPreferredSize(new Dimension(50, 21));
		jPanel1_1_1.add(m_cancelBtn);
		
		m_submitBtn.setText("Submit");
		m_submitBtn.setMargin(new Insets(2, 2, 2, 2));
		m_submitBtn.setPreferredSize(new Dimension(50, 21));
		jPanel1_1_1.add(m_submitBtn);
		
		TopButtonPanel.add(jPanel1_1_1, BorderLayout.WEST);
		
		panelUtamaAtas.add(TopButtonPanel, BorderLayout.NORTH);
		
		panelAtas.setLayout(new BorderLayout());
		
		panelAtas.setPreferredSize(new Dimension(650, 320));
		jPanel1_2_2.setLayout(new BorderLayout());
		
		jPanel1_2_2.setPreferredSize(new Dimension(325, 305));
		panelKomponenKANAN.setLayout(new GridBagLayout());
		
		panelKomponenKANAN.setPreferredSize(new Dimension(325, 200));
		VoucherNoLabel.setText("Voucher No");
		VoucherNoLabel.setPreferredSize(new Dimension(95, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKANAN.add(VoucherNoLabel, gridBagConstraints);
		
		RefNoText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKANAN.add(RefNoText, gridBagConstraints);
		
		VoucherDateLabel.setText("Voucher Date*");
		VoucherDateLabel.setPreferredSize(new Dimension(95, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKANAN.add(VoucherDateLabel, gridBagConstraints);
		
		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new Dimension(95, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKANAN.add(DescriptionLabel, gridBagConstraints);
		
		DescriptionScroll.setPreferredSize(new Dimension(200, 63));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScroll.setViewportView(DescriptionTextArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKANAN.add(DescriptionScroll, gridBagConstraints);
		
		RemarkLabel.setText("Remarks");
		RemarkLabel.setPreferredSize(new Dimension(95, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKANAN.add(RemarkLabel, gridBagConstraints);
		
		RemarkScroll.setPreferredSize(new Dimension(200, 63));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarkScroll.setViewportView(RemarksTextArea);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKANAN.add(RemarkScroll, gridBagConstraints);
		
		JPanel emptypanel = new JPanel();
		emptypanel.setPreferredSize(new Dimension(200, 70));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKANAN.add(emptypanel, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKANAN.add(TransactionDateDate, gridBagConstraints);
		
		jPanel1_2_2.add(panelKomponenKANAN, BorderLayout.WEST);
		
		panelAtas.add(jPanel1_2_2, BorderLayout.CENTER);
		
		panelKomponenKIRI.setLayout(new GridBagLayout());
		
		panelKomponenKIRI.setPreferredSize(new Dimension(350, 200));
		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(StatusLabel, gridBagConstraints);
		
		SubmittedDateLabel.setText("Submitted Date");
		SubmittedDateLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(SubmittedDateLabel, gridBagConstraints);
		
		PaymentSourceLabel.setText("Payment Source*");
		PaymentSourceLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(PaymentSourceLabel, gridBagConstraints);
		
		AccountLabel.setText("Account");
		AccountLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(AccountLabel, gridBagConstraints);
		
		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(UnitCodeLabel, gridBagConstraints);
		
		StatusText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(StatusText, gridBagConstraints);
		
		PayToLabel.setText("Pay to");
		PayToLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(PayToLabel, gridBagConstraints);
		
		ChequeNoLabel.setText("Cheque No.");
		ChequeNoLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(ChequeNoLabel, gridBagConstraints);
		
		ChequeDueDateLabel.setText("Cheque Due Date");
		ChequeDueDateLabel.setPreferredSize(new Dimension(110, 15));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(ChequeDueDateLabel, gridBagConstraints);
		
		SubmittedDateText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(SubmittedDateText, gridBagConstraints);
		
		PaymentSourceCombo.setModel(new DefaultComboBoxModel(new String[] { "Cash", "Bank" }));
		PaymentSourceCombo.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(PaymentSourceCombo, gridBagConstraints);
		
		accountComp().setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(accountComp(), gridBagConstraints);
		
		m_unitpicker.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(m_unitpicker, gridBagConstraints);
		
		PaytoText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(PaytoText, gridBagConstraints);
		
		ChequeNoText.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(ChequeNoText, gridBagConstraints);
		
		ChequeDueDateDate.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKIRI.add(ChequeDueDateDate, gridBagConstraints);
		
		JPanel groupdeptPanel = new JPanel();
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(m_singleRb);
		bg1.add(m_multipleRb);
		m_singleRb.setSelected(true);
		
		groupdeptPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Department",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11)));
		groupdeptPanel.add(m_singleRb);
		groupdeptPanel.add(m_multipleRb);
		
		groupdeptPanel.setPreferredSize(new Dimension(200, 60));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKIRI.add(groupdeptPanel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKIRI.add(new JLabel("Department *"), gridBagConstraints);
		
		m_departmentComp.setPreferredSize(new Dimension(200, 18));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 3);
		panelKomponenKIRI.add(m_departmentComp, gridBagConstraints);
		
		panelAtas.add(panelKomponenKIRI, BorderLayout.WEST);
		
		panelUtamaAtas.add(panelAtas, BorderLayout.CENTER);
		
		panel4OriginatorComp.setLayout(new BorderLayout());
		
		panel4OriginatorComp.setPreferredSize(new Dimension(700, 90));
		OriginatorComp.setLayout(new GridBagLayout());
		
		OriginatorComp.setBorder(BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new Dimension(279, 110));
		panel4OriginatorComp.add(OriginatorComp, BorderLayout.WEST);
		
		panel4ApprovedComp.setLayout(new BorderLayout());
		
		ApprovedComp.setLayout(new GridBagLayout());
		
		ApprovedComp.setBorder(BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new Dimension(279, 110));
		panel4ApprovedComp.add(ApprovedComp, BorderLayout.WEST);
		
		panel4ReceivedComp.setLayout(new BorderLayout());
		
		ReceivedComp.setLayout(new GridBagLayout());
		
		ReceivedComp.setBorder(BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new Dimension(279, 110));
		panel4ReceivedComp.add(ReceivedComp, BorderLayout.WEST);
		
		panel4ApprovedComp.add(panel4ReceivedComp, BorderLayout.CENTER);
		
		panel4OriginatorComp.add(panel4ApprovedComp, BorderLayout.CENTER);
		
		panelUtamaAtas.add(panel4OriginatorComp, BorderLayout.SOUTH);
		
		add(panelUtamaAtas, BorderLayout.NORTH);
		
		panelUtamaBawah.setLayout(new BorderLayout());
		
		panelUtamaBawah.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		panelUtamaBawah.setPreferredSize(new Dimension(650, 250));
		panel4Add_Delete.setLayout(new BorderLayout());
		
		panel4Add_Delete.setPreferredSize(new Dimension(650, 35));
		ButtomButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 5));
		
		ButtomButtonPanel.setPreferredSize(new Dimension(650, 35));
		AddTax21ComponentBtn.setText("Add");
		AddTax21ComponentBtn.setMargin(new Insets(2, 2, 2, 2));
		AddTax21ComponentBtn.setPreferredSize(new Dimension(50, 21));
		ButtomButtonPanel.add(AddTax21ComponentBtn);
		
		DeleteTax21ComponentBtn.setText("Delete");
		DeleteTax21ComponentBtn.setMargin(new Insets(2, 2, 2, 2));
		DeleteTax21ComponentBtn.setPreferredSize(new Dimension(50, 21));
		ButtomButtonPanel.add(DeleteTax21ComponentBtn);
		
		/*SaveTax21ComponentBtn.setText("Save");
		 SaveTax21ComponentBtn.setMargin(new Insets(2, 2, 2, 2));
		 SaveTax21ComponentBtn.setPreferredSize(new Dimension(50, 21));
		 ButtomButtonPanel.add(SaveTax21ComponentBtn);*/
		
		panel4Add_Delete.add(ButtomButtonPanel, BorderLayout.WEST);
		
		panelUtamaBawah.add(panel4Add_Delete, BorderLayout.NORTH);
		
		scrollPane.setPreferredSize(new Dimension(650, 215));
		/* Tax21ComponentTable.setModel(new table.DefaultTableModel(
		 new Object [][] {                
		 },
		 new String [] {
		 "Account", "Description", "Debit", "Credit","Currency"
		 }
		 ));*/
		m_table=new buttomTabblePmtProject();
		scrollPane.setViewportView(m_table);
		
		panelUtamaBawah.add(scrollPane, BorderLayout.CENTER);
		
		add(panelUtamaBawah, BorderLayout.CENTER);
		
	}
	
	private void addingListener(){
		PaymentSourceCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		accountComp().addPropertyChangeListener("object",this);
		//m_unitpicker.addPropertyChangeListener("object",this);
		AddTax21ComponentBtn.addActionListener(this);
		DeleteTax21ComponentBtn.addActionListener(this);
		m_singleRb.addActionListener(this);
		m_multipleRb.addActionListener(this);
		m_departmentComp.addPropertyChangeListener(this);
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
		//ExchRateText.setValue(new Double(0));
		TransactionDateDate.setDate(null);
	}
	
	protected void clearAll() {
		super.clearAll();
		clearKomponen();
		disableEditMode();
		m_table.clearTable();
	}
	
	protected void clearForm() {
		clearTextField(panelKomponenKIRI);
		clearTextField(panelKomponenKANAN);
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
	
	protected void doNew() {
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
		setEnableButtonBawah(true);
		m_table.clearTable();
		StatusText.setText(entity().statusInString());
		rowList = new ArrayList();
	}
	
	public void doEdit() {
		super.doEdit();
		setEnableButtonBawah(true);
	}
	
	
	protected void doSubmit() {
		ArrayList validityMsgs = new ArrayList();
		if (AccountComp.getObject()== null)
			validityMsgs.add("Source account must be selected");
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
		setEnableButtonBawah(false);
	}
	
	public void actionPerformed(ActionEvent e)    {    	
		if(e.getSource() == PaymentSourceCombo) {
			m_table.clearTable();
			rowList.clear();
			revalidateaccount();
			
		}
		else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0)
				new Vchr_Salary(m_entity,m_conn, m_sessionid);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		else if(e.getSource() == m_searchRefNoBtn) {
			SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame.getMainFrame(), "Search Voucher", m_conn, m_sessionid,new PayrollPmtSlryHoLoader(m_conn,PayrollPmtTax21Unit.class));    	
			dlg.setVisible(true);
			if (dlg.selectedObj != null){				
				doLoad(dlg.selectedObj);
				LoadDetail(entity().getIndex());
			}	
		}
		else if (e.getSource() == AddTax21ComponentBtn){
			addTax21Cost();
		}
		else if (e.getSource()==DeleteTax21ComponentBtn){
			int row = m_table.getSelectedRow();
			Boolean isDeleted = (Boolean) rowList.get(row);
			if (isDeleted.booleanValue()) {
				if (JOptionPane
						.showConfirmDialog(this, "Are you sure?",
								"Delete Confirmation Dialog",
								JOptionPane.YES_NO_OPTION) == 0) {
					deleteTax21Cost();
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
	
	private void revalidateaccount() {
		String receive = (String)PaymentSourceCombo.getSelectedItem();    		
		panelKomponenKIRI.remove(accountComp());
		panelKomponenKIRI.revalidate();
		panelKomponenKIRI.repaint();
		if(receive.equals("Cash"))
			setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid, m_unitpicker.getUnit(),IConstants.PAYROLL_PAYMENT_TAX21_UNIT));    		
		else if (receive.equals("Bank"))
			setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid, m_unitpicker.getUnit(),IConstants.PAYROLL_PAYMENT_TAX21_UNIT));
		
		accountComp().setPreferredSize(new Dimension(200, 18));
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(1, 3, 1, 1);
		panelKomponenKIRI.add(accountComp(), gridBagConstraints);
		panelKomponenKIRI.revalidate();
	}
	
	private void deleteTax21Cost() {
		int row = m_table.getSelectedRow();
		int maxRow = m_table.getRowCount() - 1;
		if (row > -1) {
			if (row < (maxRow - 1)) {
				rowList.remove(row);
				m_table.removeData(row);
				m_table.updateSummary();
			}
		} else {
			JOptionPane.showMessageDialog(null,
			"Please select account you want to remove");
		}
	}
	
	private void addTax21Cost() {
		payrollTaxArt21UnitComp = new LookupTaxArt21UnitPicker(m_conn,m_sessionid);
		payrollTaxArt21UnitComp.done();
		
		if(payrollTaxArt21UnitComp.getObject()!=null){
			int oldRow = m_lastRow;
			rowList.add(m_lastRow, new Boolean(true));
			Vector vector=new Vector();
			JournalStandardAccount acc=(JournalStandardAccount)payrollTaxArt21UnitComp.getObject();
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
		GenericMapper mapper2=MasterMap.obtainMapperFor(PayrollPmtTax21UnitDet.class);
		mapper2.setActiveConn(m_conn); 
		String value = new Long(index).toString();
		List detailList=mapper2.doSelectWhere(IDBConstants.ATTR_PAYROLL_PMT_TAX21_UNIT+"="+value);
		m_table.clearTable();
		PayrollPmtTax21UnitDet detail;
		Vector temp1;
		Account acc;
		rowList = new ArrayList();
		for(int i=0;i<detailList.size();i++){
			int oldRow = m_lastRow;
			temp1=new Vector();
			detail=(PayrollPmtTax21UnitDet)detailList.get(i);
			acc=(Account)detail.getAccount();
			JournalStandardAccount journalStandardAccount = null;
			try {
				AccountingBusinessLogic accLogic = new AccountingBusinessLogic(m_conn);
				JournalStandardSetting[] jss = accLogic.getJournalStandardSetting(
						m_sessionid, IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT,
						IConstants.PAYROLL_PAYMENT_TAX21_UNIT);
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
				
				
				/*String SubsidiaryAccSet = getSubsidiaryByindex(acc);
				 AccountingBusinessLogic accLogic = new AccountingBusinessLogic(m_conn);*/
				
				/*			CashBankAccount cba = null;
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
	
	private void SetCashAccountToTable(PayrollPmtTax21UnitDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCompanyLoanToTable(PayrollPmtTax21UnitDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetProjectToTable(PayrollPmtTax21UnitDet detail, Vector temp1, ProjectBusinessLogic  proLogic) {
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
	
	
	private void SetBankAccountToTable(PayrollPmtTax21UnitDet detail, Vector temp1, AccountingBusinessLogic accLogic) {
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
	
	private void SetCustomerToTable(PayrollPmtTax21UnitDet detail, Vector temp1, ProjectBusinessLogic proLogic) {
		Customer cust = null;
		cust = proLogic.getCustomerByIndex(detail.getSubsidiAry());
		temp1.addElement(cust);
	}
	
	private void SetPartnerToTable(PayrollPmtTax21UnitDet detail, Vector temp1, ProjectBusinessLogic proLogic) {
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
	
	private void SetEmployeeToTable(PayrollPmtTax21UnitDet detail, Vector temp1, HRMBusinessLogic hrmLogic) {
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
	
	/*private CashBankAccount SetCashAccountToTable(PayrollPmtTax21UnitDet detail, AccountingBusinessLogic accLogic) {
	 CashAccount cAcc = null;
	 try {
	 cAcc = accLogic.getCashAccountByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 return cAcc;
	 }
	 
	 private CashBankAccount SetBankAccountToTable(PayrollPmtTax21UnitDet detail, AccountingBusinessLogic accLogic) {
	 BankAccount bAcc = null;
	 try {
	 bAcc = accLogic.getBankAccountByIndex(m_sessionid,IDBConstants.MODUL_ACCOUNTING,detail.getSubsidiAry());
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 return bAcc;
	 }
	 */
	private String getSubsidiaryByindex(Account acc) {
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
	
	private void setEnableButtonBawah(boolean bool){
		AddTax21ComponentBtn.setEnabled(bool);
		DeleteTax21ComponentBtn.setEnabled(bool);
		m_table.setEnabled(bool);
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
	
	PayrollPmtTax21Unit entity() {
		return m_entity;
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
		if (DescriptionTextArea.getText().equals("")){
			addInvalid("Description must be selected");
		}
		detailAccountOperation();
		PayrollPmtTax21UnitDet[] temp = entity().getPayrollPmtTax21UnitDet();
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
		}
		else if (objacc instanceof CashAccount) {
			CashAccount cash = (CashAccount) objacc;
			entity().setCashAccount(cash);
			entity().setBankAccount(null);
		}
		
		Object objunit = m_unitpicker.getObject();
		if (objunit instanceof Unit) {
			Unit unit= (Unit) objunit;
			entity().setUnit(unit);			
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
		PayrollPmtTax21UnitDet[] temp = new PayrollPmtTax21UnitDet[m_table.getDataCount()];
		if (m_table.getRowCount()>=0){
			
			for (int i=0;i<m_table.getDataCount();i++){
				JournalStandardAccount journal = (JournalStandardAccount)m_table.getListData().get(i);
				Account acc = journal.getAccount();//(Account)m_table.getListData().get(i);
				temp[i]=new PayrollPmtTax21UnitDet();
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
						temp[i].setSubsidiAry(-1);					
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
			entity().setPayrollPmtTax21UnitDet(temp);
		}
	}
	
	protected void entity2gui() {
		if(entity().getBankAccount()!=null){
			PaymentSourceCombo.setSelectedItem("Bank");
			AccountComp.setObject(entity().getBankAccount());
		}else if (entity().getCashAccount()!=null){
			PaymentSourceCombo.setSelectedItem("Cash");
			AccountComp.setObject(entity().getCashAccount());
		}else{
			AccountComp.setObject(null);
		}
		
		if (entity().getUnit()!=null)
			m_unitpicker.setObject(entity().getUnit());
		else
			m_unitpicker.setObject(null);
		
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
		m_departmentComp.setEnabled(false);
		m_singleRb.setEnabled(false);
		m_multipleRb.setEnabled(false);
		StatusText.setEditable(false);
		SubmittedDateText.setEditable(false);
		PaymentSourceCombo.setEnabled(false);
		AccountComp.setEnabled(false);		
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
		m_unitpicker.setEnabled(false);
		setEnableButtonBawah(false);
	}
	
	protected void enableEditMode() {
		m_singleRb.setEnabled(true);
		m_multipleRb.setEnabled(true);
		if (m_singleRb.isSelected())
			m_departmentComp.setEnabled(true);
		else 
			m_departmentComp.setEnabled(false);
		PaymentSourceCombo.setEnabled(true);
		AccountComp.setEnabled(true);		
		PaytoText.setEditable(true);
		ChequeNoText.setEditable(true);
		ChequeDueDateDate.setEditable(true);
		TransactionDateDate.setEditable(true);
		DescriptionTextArea.setEditable(true);
		RemarksTextArea.setEditable(true);
		OriginatorComp.setEnabled(true);
		ApprovedComp.setEnabled(true);
		ReceivedComp.setEnabled(true);
		//m_unitpicker.setEnabled(true);
		setEnableButtonBawah(true);
	}
	
	protected Object createNew() {
		PayrollPmtTax21Unit a  = new PayrollPmtTax21Unit();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}
	
	void setEntity(Object m_entity) {
		PayrollPmtTax21Unit oldEntity = this.m_entity;
		if (oldEntity!=null)
		{
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PayrollPmtTax21Unit)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}
		
		if (evt.getSource() == accountComp()){
			Object objacc = accountComp().getObject();
			if (objacc instanceof BankAccount) {
				BankAccount bank = (BankAccount) objacc;
				m_unitpicker.setObject(bank.getUnit());
				addIntoTable();		
			}else if (objacc instanceof CashAccount) {
				CashAccount cash = (CashAccount) objacc;
				m_unitpicker.setObject(cash.getUnit());
				addIntoTable();		
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
	
	private CashBankAccount getAccount() {
		CashBankAccount acc = (CashBankAccount) accountComp().getObject();
		return acc;
	}
	
	private JournalStandardAccount getSelectedJournalStandardAccount(Account account) {
		// MAKSA NEEH...
		if(journalStandardList.size()==0)
			return null;
		
		Iterator iterator = journalStandardList.iterator();
		while(iterator.hasNext()){
			JournalStandard js = (JournalStandard) iterator.next();
			JournalStandardAccount[] jsas = js.getJournalStandardAccount();
			for(int i=0; i<jsas.length; i++){
				if(jsas[i].getAccount().getIndex()==account.getIndex())
					return jsas[i];
			}
			
		}
		
		return null;
	}
	
	private void addIntoTable() {
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
		
		JournalStandardAccount jsa = getSelectedJournalStandardAccount(account.getAccount());
		
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
		}else{
			rowList.remove(findRow);
			m_table.removeData(findRow);
			rowList.add(findRow, new Boolean(false));
			m_table.addData(vector, jsa, findRow);
		}
	}
	
	protected void tableStopCellEditing() {
		m_table.stopCellEditing();
	}
	
	protected void setAccountComp(LookupPicker accountComp) {
		LookupPicker old = AccountComp;
		if (old!=null)
		{
			old.removePropertyChangeListener("object",this);
		}
		AccountComp = accountComp;
		AccountComp.addPropertyChangeListener("object",this);
	}
	
	private LookupPicker accountComp() {
		return AccountComp;
	}
	
	boolean save=false;
	protected void doSave() {
		save=true;
		if (!cekValidity()) return;		
		super.doSave();
		entityGetHisIndex();
		LoadDetail(entity().getIndex());
		if(!m_saveBtn.isEnabled())
			setEnableButtonBawah(false);
	}
	
	private void entityGetHisIndex() {
		AccountingSQLSAP sql=new AccountingSQLSAP();
		GenericMapper mapper2=MasterMap.obtainMapperFor(PayrollPmtTax21UnitDet.class);
		mapper2.setActiveConn(m_conn);
		try {
			if ((new Long(entity().getIndex()).toString().equalsIgnoreCase("0") ))
			{	
				long index = sql.getMaxIndex(IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT, m_conn);
				entity().setIndex(index);
			}
			
			if (entity().getPayrollPmtTax21UnitDet()!=null){
				PayrollPmtTax21UnitDet[] temp=entity().getPayrollPmtTax21UnitDet();
				mapper2.doDeleteByColumn(IDBConstants.TABLE_PAYROLL_PMT_TAX21_UNIT, 
						new Long(entity().getIndex()).toString());
				for (int i=0;i<temp.length;i++){
					temp[i].setPayrollPmtTax21Unit((PayrollPmtTax21Unit)entity());
					mapper2.doInsert(temp[i]);  
				}	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	class buttomTabblePmtProject extends JTable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		buttomTabbleRcvOthersModel model = new buttomTabbleRcvOthersModel();
		ArrayList listData=new ArrayList();
		protected buttomTabblePmtProject() {
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
		
		public boolean isBalance(){
			int maxRow = getRowCount()-1;
			if (getValueAt(maxRow,4).toString().equals(getValueAt(maxRow,5).toString()))
				return true;
			return false;
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
			System.err.println(getValueAt(maxRow,3).toString() + " = " + getValueAt(maxRow,4).toString());
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
				if (col == 4 && journal.getBalanceAsString().equalsIgnoreCase("Debit"))					
					return new FormattedDoubleCellEditor(JTextField.LEFT);	
				if (col == 5 && journal.getBalanceAsString().equalsIgnoreCase("Credit"))					
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
						if(journal.getBalanceAsString().equalsIgnoreCase("Debit"))
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.BOLD);	
						else
							return new FormattedDoubleCellRenderer(JLabel.RIGHT, Font.PLAIN);
					if(column==5 ) 
						if(journal.getBalanceAsString().equalsIgnoreCase("Credit"))
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
		
		public void addData(Vector obj,JournalStandardAccount jsa,int insertRow){
			listData.add(insertRow, jsa);			
			model.insertRow(insertRow,obj);
			/*if (CurrComp.getObject()!=null)
			 updateCurrency((Currency)CurrComp.getObject());*/
			m_lastRow++;
			updateSummary();
			updateNumbering();
		}
		
		public void addData(Vector obj,Account acc){
			listData.add(acc);
			//buttomTabblePmtProjectModel model=(buttomTabblePmtProjectModel)accountTable.getModel();
			model.addRow(obj);
			//System.out.println(this.getParent().getClass());
			//JScrollPane temp=(JScrollPane)this.getParent();
			//this.setModel(model);
			//((JScrollPane)this.getParent()).setViewportView(this);   
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
	class buttomTabbleRcvOthersModel extends DefaultTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {			
			int maxRow = getRowCount();	
			if (row < maxRow - 1) {
				/*if (row < rowList.size()) {
				 Boolean isDeleted = (Boolean) rowList.get(row);
				 if (!isDeleted.booleanValue())
				 return false;
				 }*/
				if ((col == 0 || col == 1 || col == 2) || row > (maxRow - 3))
					return false;
				return true;
			}
			return false;
		}
	}
	
	private LookupPicker AccountComp;
	private pohaci.gumunda.titis.application.DatePicker ChequeDueDateDate;
	private javax.swing.JScrollPane DescriptionScroll;
	private JTextArea DescriptionTextArea;
	private JComboBox PaymentSourceCombo;
	private JScrollPane RemarkScroll;
	private JTextArea RemarksTextArea;
	private JTextField StatusText,SubmittedDateText,RefNoText,PaytoText,ChequeNoText;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private UnitPicker m_unitpicker;
	private JPanel panelKomponenKIRI,panelKomponenKANAN;
	private AssignPanel OriginatorComp,ApprovedComp,ReceivedComp;
	private JScrollPane scrollPane;
	private JButton AddTax21ComponentBtn,DeleteTax21ComponentBtn;
	private LookupTaxArt21UnitPicker payrollTaxArt21UnitComp;
	
}
