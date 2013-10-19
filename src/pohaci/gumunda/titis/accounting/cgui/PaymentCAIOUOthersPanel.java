/*
 * PaymentCAProjectPanel.java
 *
 * Created on June 25, 2007, 8:54 PM
 */

package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.report.IOweU_Others;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.CashAdvanceIOweULoader;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.FrameMain;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.EmployeePicker;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.OrganizationPicker;
public class PaymentCAIOUOthersPanel extends RevTransactionPanel implements ActionListener,PropertyChangeListener{
	
	private static final long serialVersionUID = 1L;
	private PmtCAIOUOthers m_entity;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	
	public PaymentCAIOUOthersPanel(Connection conn, long sessionid) {
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
		setEntity(new PmtCAIOUOthers());
		m_entityMapper = MasterMap.obtainMapperFor(PmtCAIOUOthers.class);
		m_entityMapper.setActiveConn(m_conn);        
		stateButtonAwal();
	}
	
	private void initComponents() {
		AccountComp= new LookupCashAccountPicker(m_conn, m_sessionid);
		PayToComp = new EmployeePicker(m_conn, m_sessionid);
		UnitCodeComp = new LookupUnitPicker(m_conn, m_sessionid);
		DepartmentComp = new OrganizationPicker(m_conn, m_sessionid,false);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved 1 by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Approved 2 by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		TopButtonPanel = new javax.swing.JPanel();
		java.awt.GridBagConstraints gridBagConstraints;
		AccountLabel = new JLabel();
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
		PayToLabel = new javax.swing.JLabel();
		JobTitleLabel = new javax.swing.JLabel();
		JobTitleText = new javax.swing.JTextField();
		jPanel1_2_1_2 = new javax.swing.JPanel();
		AmountReceiveLabel1 = new javax.swing.JLabel();
		RecvBaseCurrentLabel1 = new javax.swing.JLabel();
		TotalAmountText  = new javax.swing.JFormattedTextField(m_numberFormatter);
		EndingBalanceCurrText = new javax.swing.JTextField();
		TotalAmountCurrText= new javax.swing.JTextField();
		EndingBalanceAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountReceiveLabel2 = new javax.swing.JLabel();
		TotalInsCurrText = new javax.swing.JTextField();
		TotalInsAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		jPanel1_2_1_1 = new javax.swing.JPanel();
		InstalmentsBtn = new javax.swing.JButton();
		SettledBtn = new javax.swing.JButton();
		UnitCodeLabel = new javax.swing.JLabel();
		DepartmentLabel = new javax.swing.JLabel();
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_2_1 = new javax.swing.JPanel();
		IOweUNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		IOweUDateLabel = new javax.swing.JLabel();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		setLayout(new java.awt.BorderLayout());
		
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		jPanel1.setPreferredSize(new java.awt.Dimension(650, 350));
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
		
		jPanel1_1.add(TopButtonPanel, java.awt.BorderLayout.WEST);
		
		jPanel1.add(jPanel1_1, java.awt.BorderLayout.NORTH);
		
		jPanel1_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2.setPreferredSize(new java.awt.Dimension(740, 320));
		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(420, 320));
		
		AccountLabel.setText("Account");
		AccountLabel.setPreferredSize(new java.awt.Dimension(113, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountLabel, gridBagConstraints);
		
		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);
		
		PayToLabel.setText("Pay To*");
		PayToLabel.setPreferredSize(new java.awt.Dimension(113, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PayToLabel, gridBagConstraints);
		
		PayToComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PayToComp, gridBagConstraints);
		
		JobTitleLabel.setText("Job Title");
		JobTitleLabel.setPreferredSize(new java.awt.Dimension(113, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(JobTitleLabel, gridBagConstraints);
		
		JobTitleText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(JobTitleText, gridBagConstraints);
		
		jPanel1_2_1_2.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_2.setPreferredSize(new java.awt.Dimension(410, 65));
		AmountReceiveLabel1.setText("Total Amount *");
		AmountReceiveLabel1.setPreferredSize(new java.awt.Dimension(120, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(AmountReceiveLabel1, gridBagConstraints);
		
		RecvBaseCurrentLabel1.setText("Ending Balance");
		RecvBaseCurrentLabel1.setPreferredSize(new java.awt.Dimension(120, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(RecvBaseCurrentLabel1, gridBagConstraints);
		
		TotalAmountCurrText.setPreferredSize(new java.awt.Dimension(82, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 3);
		jPanel1_2_1_2.add(TotalAmountCurrText, gridBagConstraints);
		
		EndingBalanceCurrText.setPreferredSize(new java.awt.Dimension(82, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 3);
		jPanel1_2_1_2.add(EndingBalanceCurrText, gridBagConstraints);
		
		TotalAmountText.setPreferredSize(new java.awt.Dimension(205, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(TotalAmountText, gridBagConstraints);
		
		EndingBalanceAmountText.setPreferredSize(new java.awt.Dimension(205, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(EndingBalanceAmountText, gridBagConstraints);
		
		AmountReceiveLabel2.setText("Total Installment");
		AmountReceiveLabel2.setPreferredSize(new java.awt.Dimension(120, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(AmountReceiveLabel2, gridBagConstraints);
		
		TotalInsCurrText.setPreferredSize(new java.awt.Dimension(82, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 3);
		jPanel1_2_1_2.add(TotalInsCurrText, gridBagConstraints);
		
		TotalInsAmountText.setPreferredSize(new java.awt.Dimension(205, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(TotalInsAmountText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1.add(jPanel1_2_1_2, gridBagConstraints);
		
		jPanel1_2_1_1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
		
		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(410, 30));
		InstalmentsBtn.setText("Installments");
		InstalmentsBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		InstalmentsBtn.setPreferredSize(new java.awt.Dimension(70, 20));
		jPanel1_2_1_1.add(InstalmentsBtn);
		
		SettledBtn.setText("Settled");
		SettledBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		SettledBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		jPanel1_2_1_1.add(SettledBtn);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		jPanel1_2_1.add(jPanel1_2_1_1, gridBagConstraints);
		
		UnitCodeLabel.setText("Unit Code");
		UnitCodeLabel.setPreferredSize(new java.awt.Dimension(60,15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(UnitCodeLabel, gridBagConstraints);
		
		UnitCodeComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(UnitCodeComp, gridBagConstraints);
		
		DepartmentLabel.setText("Department*");
		DepartmentLabel.setPreferredSize(new java.awt.Dimension(60,15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DepartmentLabel, gridBagConstraints);
		
		DepartmentComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DepartmentComp, gridBagConstraints);
		
		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(415, 305));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(415, 400));
		IOweUNoLabel.setText("I Owe U No *");
		IOweUNoLabel.setPreferredSize(new java.awt.Dimension(77,15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		//  gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(IOweUNoLabel, gridBagConstraints);
		
		RefNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);
		
		IOweUDateLabel.setText("I Owe U Date*");
		IOweUDateLabel.setPreferredSize(new java.awt.Dimension(77,15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(IOweUDateLabel, gridBagConstraints);
		
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(TransactionDateDate, gridBagConstraints);
		
		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DescriptionLabel, gridBagConstraints);
		
		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 70));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
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
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		jPanel1_2_2_1.add(RemarksScrollPane, gridBagConstraints);
		
		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 8, 1, 1);
		jPanel1_2_2_1.add(RemarksLabel, gridBagConstraints);
		
		jPanel1_2_2.add(jPanel1_2_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(jPanel1_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.setLayout(new java.awt.BorderLayout());
		
		jPanel1_3.setPreferredSize(new java.awt.Dimension(700, 100));
		OriginatorComp.setLayout(new java.awt.GridBagLayout());
		
		OriginatorComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(275,110));
		
		jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.setLayout(new java.awt.BorderLayout());
		
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		
		ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved 1 by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(275,110));
		jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());
		
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		
		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved 2 by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(285,110));
		jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);
		
		jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);
		
		jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);
		
		jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);
		
		add(jPanel1, java.awt.BorderLayout.NORTH);
		
	}
	
	private void addingListener(){
		InstalmentsBtn.addActionListener(this);
		AccountComp.addPropertyChangeListener("object",this);
		SettledBtn.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);	
		payToComp().addPropertyChangeListener(this);
		TotalAmountText.addPropertyChangeListener(this);
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this); 
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);    	
	}  
	
	public void setNo(){
		try{
			entity().setIOUNo(m_sessionid, m_conn);			
		} catch (Exception e){    	
		}    	
	}
		
	protected void doCancel() {
		super.doCancel();
		if (entity().getStatus()==0){
			this.InstalmentsBtn.setEnabled(true);
			this.SettledBtn.setEnabled(cekSettledOk(entity()));	
		}  
	}

	public void doEdit() {
		super.doEdit();
		this.InstalmentsBtn.setEnabled(false);
		this.SettledBtn.setEnabled(false);
	}

	protected void doNew() {
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0){
				new IOweU_Others(m_entity,PayToComp);
			}else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		
		else if(e.getSource() == m_searchRefNoBtn) {
			SearchIOweYouDialog dlg = new SearchIOweYouDialog(GumundaMainFrame.getMainFrame(),
					"Search I Owe U", m_conn, m_sessionid,
					new CashAdvanceIOweULoader(m_conn,PmtCAIOUOthers.class),
			"Cash Advance I Owe U Others");
			dlg.setVisible(true);
			if (dlg.selectedObj!= null){
				PmtCAIOUOthers temp=(PmtCAIOUOthers) dlg.selectedObj;
				doLoadObject(temp);    			
			}
		}
		
		else if (e.getSource()==InstalmentsBtn){
			loadInstallment(null); 			
		}
		
		else if (e.getSource()==SettledBtn){
			loadSettlement();
		}    	
	}

	protected void loadSettlement() {
		JDialog dialog= new JDialog((FrameMain)GumundaMainFrame.getMainFrame());
		//dialog.add(arg0)
		dialog.setTitle("Cash Advance Others - I Owe You Settled");
		JPanel paneltemp=new JPanel();
		PaymentCAIOUOthersCashReceivePanel compB=new PaymentCAIOUOthersCashReceivePanel(dialog,m_conn, m_sessionid,entity());
		PaymentCAIOUOthersSettledPanel compA= new PaymentCAIOUOthersSettledPanel(dialog,m_conn, m_sessionid,entity(),compB,payToComp());
		paneltemp.setLayout(new java.awt.BorderLayout());
		paneltemp.add(compA,java.awt.BorderLayout.NORTH);
		paneltemp.add(compB,java.awt.BorderLayout.CENTER);		
		dialog.getContentPane().add(paneltemp);
		dialog.setModal(true);
		dialog.setSize(new Dimension(900,750));
		dialog.setVisible(true);
	}

	protected void loadInstallment(Object obj) {
		JDialog dialog= new JDialog((FrameMain)GumundaMainFrame.getMainFrame());
		dialog.setTitle("Cash Advance Others - I Owe You Installment");
		PaymentCAIOUOthersInstPanel compA=new PaymentCAIOUOthersInstPanel(dialog,m_conn, m_sessionid,entity());
		// bonus
		if(obj!=null)
			compA.loadObject(obj);
	    if (cekHasSettled(entity())){
	    	JOptionPane.showMessageDialog(this, "This I Owe U has been settled, you can only view the installment!!");
	    	compA.m_newBtn.setVisible(false);
	    }
	    else
	    	compA.m_newBtn.setVisible(true);
		dialog.getContentPane().add(compA);
		dialog.setModal(true);
		dialog.setSize(new Dimension(950,380));
		dialog.setVisible(true);
	}

	protected void doLoadObject(StateTemplateEntity temp) {
		HitungInstallment((PmtCAIOUOthers) temp);
		doLoad(temp);
		TotalAmountCurrText.setText(entity().getCurrency().getSymbol());
		TotalInsCurrText.setText(entity().getCurrency().getSymbol());
		EndingBalanceCurrText.setText(entity().getCurrency().getSymbol());
		this.InstalmentsBtn.setEnabled(true);
		this.SettledBtn.setEnabled(cekSettledOk(entity()));
	}
	
	public double TotalInstallment;
	public void HitungInstallment(PmtCAIOUOthers temp){
		try {
			double amountInstall;
			GenericMapper mapnya;
			Object[] listData;
			mapnya= MasterMap.obtainMapperFor(PmtCAIOUOthersInstall.class);
			mapnya.setActiveConn(m_conn);
			listData=mapnya.doSelectWhere("PMTCAIOUOTHERS ="+temp.getIndex()).toArray();
			amountInstall=0;   
			for(int i = 0; i < listData.length; i ++){
				PmtCAIOUOthersInstall data=(PmtCAIOUOthersInstall)listData[i];
				if (data.getStatus()==3)
				{amountInstall=amountInstall+data.getAmount();
				}
			}
			TotalInstallment=amountInstall;
			TotalInsAmountText.setValue(new Double(TotalInstallment));
		}catch (Exception e){    	
		}    	
	}
	
	public boolean cekSettledOk(PmtCAIOUOthers temp) {
		try {
			GenericMapper mapnya2;
			Object[] listData;
			mapnya2 = MasterMap.obtainMapperFor(PmtCAIOUOthersInstall.class);
			mapnya2.setActiveConn(m_conn);
			String strWhere = "PMTCAIOUOTHERS =" + temp.getIndex() + " and status!=3";
			System.err.println(strWhere);
			listData = mapnya2.doSelectWhere(strWhere).toArray();		
			if (listData.length > 0){
				boolean isThere = true;
				List list = getUsedList(PmtCAIOUOthersReceive.class, temp.getIndex());
				isThere &= (list.size()>0);
				
				list = getUsedList(PmtCAIOUOthersSettled.class, temp.getIndex());
				isThere &= (list.size()>0);
				
				if (isThere){
					JOptionPane.showMessageDialog(this, 
							"Although installment status is not posted yet.\n" +
							"Please notify that there are settlement and receive transaction!");
					return true;
				}
				
				return false;
			}else{
				strWhere = "PMTCAIOUOTHERS =" + temp.getIndex() + " and status=3";
				listData = mapnya2.doSelectWhere(strWhere).toArray();
				if (listData.length > 0)
					return true;
				else
					return false;
			}
		} catch (Exception e) {
			return false;
		}

	}
	
	private List getUsedList(Class clazz, long parentIndex) {
		GenericMapper mapper = MasterMap.obtainMapperFor(clazz);
		mapper.setActiveConn(m_conn);
		List list = mapper.doSelectWhere(IDBConstants.TABLE_PMT_CA_IOU_OTHERS + "=" + parentIndex);
		return list;
	}
	
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
		OriginatorComp.m_datePicker.setDate(new Date());
		ApprovedComp.m_datePicker.setDate(new Date());
		ReceivedComp.m_datePicker.setDate(new Date());
		TotalAmountCurrText.setText(baseCurrency.getSymbol());
		TotalInsCurrText.setText(baseCurrency.getSymbol());
		EndingBalanceCurrText.setText(baseCurrency.getSymbol());
		TotalAmountText.setValue(new Double(0));
		TotalInsAmountText.setValue(new Double(0));
		EndingBalanceAmountText.setValue(new Double(0));
		TotalInstallment=0;
	}
	
	private javax.swing.JLabel AccountLabel;
	private LookupCashAccountPicker AccountComp;
	private javax.swing.JLabel AmountReceiveLabel1;
	private javax.swing.JLabel AmountReceiveLabel2;
	private javax.swing.JLabel DepartmentLabel;
	private OrganizationPicker DepartmentComp;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JFormattedTextField EndingBalanceAmountText;
	private javax.swing.JTextField EndingBalanceCurrText;
	private javax.swing.JLabel IOweUDateLabel;
	private javax.swing.JLabel IOweUNoLabel;
	private javax.swing.JButton InstalmentsBtn;
	private javax.swing.JLabel JobTitleLabel;
	private javax.swing.JTextField JobTitleText;
	private EmployeePicker PayToComp;
	private javax.swing.JLabel PayToLabel;
	private javax.swing.JLabel RecvBaseCurrentLabel1;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScrollPane;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JButton SettledBtn;
	private javax.swing.JFormattedTextField TotalAmountText;
	private javax.swing.JTextField TotalAmountCurrText;
	private javax.swing.JFormattedTextField TotalInsAmountText;
	private javax.swing.JTextField TotalInsCurrText;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JLabel UnitCodeLabel;
	private LookupUnitPicker UnitCodeComp;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel TopButtonPanel;
	private javax.swing.JPanel jPanel1_2;
	private javax.swing.JPanel jPanel1_2_1;
	private javax.swing.JPanel jPanel1_2_1_1;
	private javax.swing.JPanel jPanel1_2_1_2;
	private javax.swing.JPanel jPanel1_2_2;
	private javax.swing.JPanel jPanel1_2_2_1;
	private javax.swing.JPanel jPanel1_3;
	private AssignPanel OriginatorComp;
	private javax.swing.JPanel jPanel1_3_2;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel jPanel1_3_2_2;
	private AssignPanel ReceivedComp;
	
	protected void gui2entity() {
		entity().setCashAccount((CashAccount)AccountComp.getObject());
		entity().setUnit((Unit)UnitCodeComp.getObject());
		entity().setDepartment((Organization)DepartmentComp.getObject());
		entity().setPayTo(PayToComp.getEmployee());
		Object obj = TotalAmountText.getValue();
		Number amount = (Number) obj;
		entity().transTemplateRead(
				this.OriginatorComp,this.ApprovedComp,
				this.ReceivedComp,this.RefNoText.getText(),
				this.DescriptionTextArea.getText(), this.RemarksTextArea.getText(),
				this.TransactionDateDate.getDate(),amount.doubleValue());
		
		obj = TotalAmountText.getValue();
		amount = (Number) obj;
		if (amount!=null)
			entity().setAmount(amount.doubleValue());
		setNo();
		
		Object objAcc = AccountComp.getObject();
		if (objAcc instanceof CashAccount) {
			CashAccount cash = (CashAccount) objAcc;
			entity().setCurrency(cash.getCurrency());
		}else
			entity().setCurrency(baseCurrency);
	}
	
	protected void entity2gui() {
		AccountComp.setObject(entity().getCashAccount());
		payToComp().setEmployee(entity().getPayTo());
		payToComp().findEmployee(entity().getPayTo());
		JobTitleText.setText(payToComp().getJobTitle());
		UnitCodeComp.setObject(entity().getUnit());
		DepartmentComp.setObject(entity().getDepartment());
		TotalAmountText.setValue(new Double(entity().getAmount()));
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
	}
	
	protected Object createNew(){
		PmtCAIOUOthers a  = new PmtCAIOUOthers();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}
	
	protected void doSave() {
		if (!cekValidity()) 
			return;
		super.doSave();
		AccountingSQLSAP sql=new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_CA_IOU_OTHERS, m_conn);
				entity().setIndex(index);
			}
			if (entity().getStatus()==0){
				this.InstalmentsBtn.setEnabled(true);
				this.SettledBtn.setEnabled(cekSettledOk(entity()));
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	void setEntity(Object m_entity) {
		PmtCAIOUOthers oldEntity = this.m_entity;
		if (oldEntity!=null){
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PmtCAIOUOthers)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}
	
	PmtCAIOUOthers entity() {
		return m_entity;
	}
	
	protected void enableEditMode(){
		this.AccountComp.setEnabled(true);
		this.PayToComp.setEditable(true);
		this.UnitCodeComp.setEnabled(false);
		this.DepartmentComp.setEnabled(true);
		this.TotalAmountText.setEditable(true);
		this.TransactionDateDate.setEditable(true);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
		this.SettledBtn.setEnabled(false);
		this.InstalmentsBtn.setEnabled(false);		
	}
	
	protected void disableEditMode(){
		this.AccountComp.setEnabled(false);
		this.PayToComp.setEditable(false);
		this.UnitCodeComp.setEnabled(false);
		this.DepartmentComp.setEnabled(false);
		this.TotalAmountText.setEditable(false);
		this.TransactionDateDate.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
		this.InstalmentsBtn.setEnabled(false);
		this.SettledBtn.setEnabled(false);
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		/*if (AccountComp.getObject()==null)
			addInvalid("Account Comp must be selected");*/
		if (PayToComp.getEmployee()==null)
			addInvalid("Pay To Comp must be selected");
		if (TotalAmountText.getValue()== null)
			addInvalid("Amount must be filled");
		/*if (UnitCodeComp.getObject() == null)
			addInvalid("Unit Code must be selected");*/
		if (DepartmentComp.getObject()==null)
			addInvalid("Departement must be selected");
		if (DescriptionTextArea.getText().compareTo("")==0)
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
	
	public void propertyChange(PropertyChangeEvent evt) {	
		if ("state".equals(evt.getPropertyName()))
			readEntityState();
		else if (evt.getSource() == payToComp())
			JobTitleText.setText(payToComp().getJobTitle());				
		else if (evt.getSource()==AccountComp){
			if (AccountComp.getObject()!=null){
				CashAccount a=(CashAccount)AccountComp.getObject();
				UnitCodeComp.setObject(a.getUnit());
			}
		}
		else if (evt.getSource()==TotalAmountText){
			double amount,endingAmount;			
			if (!TotalAmountText.getText().equalsIgnoreCase(""))
				amount=((Number)TotalAmountText.getValue()).doubleValue();
			else
				amount=0;
			endingAmount=amount-TotalInstallment;
			EndingBalanceAmountText.setValue(new Double(endingAmount));			
		}
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS, Signature.SIGN_APPROVED1);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS, Signature.SIGN_APPROVED2);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	
	private void enableAwal(){ 	
		setenableEditPanel(jPanel1_2_1,false);
		setenableEditPanel(jPanel1_2_1_1,false);
		setenableEditPanel(jPanel1_2_2_1,false);
		setenableEditPanel(jPanel1_2_1_2, false);
	}
	

	private EmployeePicker payToComp() {
		return PayToComp;
	}
	
	protected void clearForm() {
		clearTextField(jPanel1_2_2_1);
		clearTextField(jPanel1_2_2);
		clearTextField(jPanel1_2_1);
		clearTextField(jPanel1_2_1);
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
	
	public void clearAll(){
		super.clearAll();
		clearForm();
		clearKomponen();
		disableEditMode();
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
		TotalAmountCurrText.setText("");
		TotalInsCurrText.setText("");
		EndingBalanceCurrText.setText("");
		TotalAmountText.setValue(null);
		TotalInsAmountText.setValue(null);
		EndingBalanceAmountText.setValue(null);
		TransactionDateDate.setDate(null);
	}	 
	public boolean cekHasSettled(PmtCAIOUOthers temp){
		try {
			GenericMapper mapnya;
			Object[] listData;
			mapnya= MasterMap.obtainMapperFor(PmtCAIOUOthersSettled.class);
			mapnya.setActiveConn(m_conn);
			//listData=mapnya.doSelectWhere("PMTCAIOUOTHERS="+temp.getIndex()+" and status=3").toArray();
			listData=mapnya.doSelectWhere("PMTCAIOUOTHERS="+temp.getIndex()).toArray();
			
			if (listData.length>0)
				return true;
			else
				return false;			
		}
		catch (Exception e){
			return false;		
		}		
	}
}
