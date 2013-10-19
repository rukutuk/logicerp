/*
 * PaymentCAProjectPanel.java
 *
 * Created on June 25, 2007, 8:54 PM
 */

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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pohaci.gumunda.titis.accounting.cgui.report.Rcpt_IOUSettledOthers;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class PaymentCAIOUOthersCashReceivePanel extends RevTransactionPanel
implements ActionListener,PropertyChangeListener,DocumentListener{
	private static final long serialVersionUID = 1L;
	private PmtCAIOUOthersReceive m_entity;
	private PmtCAIOUOthers entityparent;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	JDialog m_owner;
	public PaymentCAIOUOthersCashReceivePanel(JDialog owner,Connection conn, long sessionid,PmtCAIOUOthers obj) {
		m_owner = owner;
		entityparent=obj;
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
		setEntity(new PmtCAIOUOthersReceive());
		m_entityMapper = MasterMap.obtainMapperFor(PmtCAIOUOthersReceive.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
		PertamaMuncul(entityparent);
	}

	public void PertamaMuncul(PmtCAIOUOthers temp){
		try {
			GenericMapper mapnya;
			Object[] listData;
			mapnya= MasterMap.obtainMapperFor(PmtCAIOUOthersReceive.class);
			mapnya.setActiveConn(m_conn);
			listData=mapnya.doSelectWhere("PMTCAIOUOTHERS ="+temp.getIndex()).toArray();
			if (listData.length==0){
				// data baru...
				setEnabledSaveCancel(true);
				setEnabledNonSaveCancel(false);
				clearForm();
				doNew();
				isiDefaultAssignPanel();
				enableEditMode();
				HitungInstallment(temp);
				AccountComp.setObject(entityparent.getCashAccount());
			}else{
				doLoad((PmtCAIOUOthersReceive)listData[0]);
				HitungInstallment(temp);
				TotalInsCurrText.setText("Rp");
			}
		}
		catch (Exception e){
		}
	}

	private void initComponents() {
		AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		TopButtonPanel = new javax.swing.JPanel();
		java.awt.GridBagConstraints gridBagConstraints;
		jPanel1 = new javax.swing.JPanel();
		jPanel1_1 = new javax.swing.JPanel();
		TopButtonPanel = new javax.swing.JPanel();
		m_editBtn = new javax.swing.JButton();
		m_newBtn= new JButton();
		m_saveBtn = new javax.swing.JButton();
		m_deleteBtn = new javax.swing.JButton();
		m_cancelBtn = new javax.swing.JButton();
		m_submitBtn = new javax.swing.JButton();
		jPanel1_2 = new javax.swing.JPanel();
		jPanel1_2_1 = new javax.swing.JPanel();
		jPanel1_2_1_2 = new javax.swing.JPanel();
		AmountReceiveLabel2 = new javax.swing.JLabel();
		TotalInsCurrText = new javax.swing.JTextField();
		TotalInsAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		StatusLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateLabel = new javax.swing.JLabel();
		SubmittedDateText = new javax.swing.JTextField();
		AccountLabel = new javax.swing.JLabel();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_2_1 = new javax.swing.JPanel();
		IOweUNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		IOweUDateLabel = new javax.swing.JLabel();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();

		setLayout(new java.awt.BorderLayout());

		jPanel1.setLayout(new java.awt.BorderLayout());

		jPanel1.setPreferredSize(new java.awt.Dimension(650, 300));
		jPanel1_1.setLayout(new java.awt.BorderLayout());

		TopButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));

		TopButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
		m_printViewRefNoBtn.setPreferredSize(new java.awt.Dimension(21, 21));
		TopButtonPanel.add(m_printViewRefNoBtn);

		m_editBtn.setText("Edit");
		m_editBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_editBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		//TopButtonPanel.add(m_editBtn);

		m_deleteBtn.setText("Delete");
		m_deleteBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_deleteBtn.setPreferredSize(new java.awt.Dimension(50, 20));
//		TopButtonPanel.add(m_deleteBtn);

		m_saveBtn.setText("Save");
		m_saveBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_saveBtn.setPreferredSize(new java.awt.Dimension(50, 20));

		m_cancelBtn.setText("Cancel");
		m_cancelBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_cancelBtn.setPreferredSize(new java.awt.Dimension(50, 20));

		m_submitBtn.setText("Submit");
		m_submitBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_submitBtn.setPreferredSize(new java.awt.Dimension(50, 20));

		jPanel1_1.add(TopButtonPanel, java.awt.BorderLayout.WEST);

		jPanel1.add(jPanel1_1, java.awt.BorderLayout.NORTH);

		jPanel1_2.setLayout(new java.awt.BorderLayout());

		jPanel1_2.setPreferredSize(new java.awt.Dimension(650, 320));
		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(420, 320));
		jPanel1_2_1_2.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_1_2.setPreferredSize(new java.awt.Dimension(412, 21));
		AmountReceiveLabel2.setText("Total Instalment");
		AmountReceiveLabel2.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		jPanel1_2_1_2.add(AmountReceiveLabel2, gridBagConstraints);

		TotalInsCurrText.setPreferredSize(new java.awt.Dimension(52, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 3);
		jPanel1_2_1_2.add(TotalInsCurrText, gridBagConstraints);

		TotalInsAmountText.setPreferredSize(new java.awt.Dimension(235, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(TotalInsAmountText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1.add(jPanel1_2_1_2, gridBagConstraints);

		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(StatusLabel, gridBagConstraints);

		StatusText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(StatusText, gridBagConstraints);

		SubmittedDateLabel.setText("Submitted Date");
		SubmittedDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(SubmittedDateLabel, gridBagConstraints);

		SubmittedDateText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(SubmittedDateText, gridBagConstraints);

		AccountLabel.setText("Account");
		AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountLabel, gridBagConstraints);

		AccountComp.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountComp, gridBagConstraints);

		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DescriptionLabel, gridBagConstraints);

		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 75));
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DescriptionScrollPane, gridBagConstraints);

		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);

		jPanel1_2_2.setLayout(new java.awt.BorderLayout());

		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(415, 305));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());

		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(415, 400));
		IOweUNoLabel.setText("Receipt No *");
		IOweUNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
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

		IOweUDateLabel.setText("Receipt Date *");
		IOweUDateLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
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

		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(290, 115));
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

	/*
	 private void initComponents() {
	 //	 Instansiasi Component
	  AccountComp = new LookupCashAccountPicker(m_conn, m_sessionid);
	  OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
	  ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
	  ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");
	  m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("images/filter2.gif"));
	  m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("images/filter.gif"));
	  TopButtonPanel = new javax.swing.JPanel();
	  //
	   java.awt.GridBagConstraints gridBagConstraints;

	   jPanel1 = new javax.swing.JPanel();
	   jPanel1_1 = new javax.swing.JPanel();
	   TopButtonPanel = new javax.swing.JPanel();
	   //Ini tetap harus ditambahkan
	    m_newBtn=new JButton();
	    m_editBtn = new javax.swing.JButton();
	    m_saveBtn = new javax.swing.JButton();
	    m_deleteBtn = new javax.swing.JButton();
	    m_cancelBtn = new javax.swing.JButton();
	    m_submitBtn = new javax.swing.JButton();
	    jPanel1_2 = new javax.swing.JPanel();
	    jPanel1_2_1 = new javax.swing.JPanel();
	    jPanel1_2_1_2 = new javax.swing.JPanel();
	    AmountReceiveLabel2 = new javax.swing.JLabel();
	    TotalInsCurrText = new javax.swing.JTextField();
	    TotalInsAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
	    StatusLabel = new javax.swing.JLabel();
	    StatusText = new javax.swing.JTextField();
	    SubmittedDateLabel = new javax.swing.JLabel();
	    SubmittedDateText = new javax.swing.JTextField();
	    AccountLabel = new javax.swing.JLabel();
	    DescriptionLabel = new javax.swing.JLabel();
	    DescriptionScrollPane = new javax.swing.JScrollPane();
	    DescriptionTextArea = new javax.swing.JTextArea();
	    jPanel1_2_2 = new javax.swing.JPanel();
	    jPanel1_2_2_1 = new javax.swing.JPanel();
	    IOweUNoLabel = new javax.swing.JLabel();
	    RefNoText = new javax.swing.JTextField();
	    IOweUDateLabel = new javax.swing.JLabel();
	    TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
	    RemarksScrollPane = new javax.swing.JScrollPane();
	    RemarksTextArea = new javax.swing.JTextArea();
	    RemarksLabel = new javax.swing.JLabel();
	    jPanel1_3 = new javax.swing.JPanel();
	    jPanel1_3_2 = new javax.swing.JPanel();
	    jPanel1_3_2_2 = new javax.swing.JPanel();

	    setLayout(new java.awt.BorderLayout());

	    jPanel1.setLayout(new java.awt.BorderLayout());

	    jPanel1.setPreferredSize(new java.awt.Dimension(650, 300));
	    jPanel1_1.setLayout(new java.awt.BorderLayout());

	    TopButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));

	    TopButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
	    m_printViewRefNoBtn.setPreferredSize(new java.awt.Dimension(21, 21));
	    TopButtonPanel.add(m_printViewRefNoBtn);

	    m_editBtn.setText("Edit");
	    m_editBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
	    m_editBtn.setPreferredSize(new java.awt.Dimension(50, 20));
	    //TopButtonPanel.add(m_editBtn);

	     m_deleteBtn.setText("Delete");
	     m_deleteBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
	     m_deleteBtn.setPreferredSize(new java.awt.Dimension(50, 20));
	     //        TopButtonPanel.add(m_deleteBtn);

	      m_saveBtn.setText("Save");
	      m_saveBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
	      m_saveBtn.setPreferredSize(new java.awt.Dimension(50, 20));
	      //      TopButtonPanel.add(m_saveBtn);

	       m_cancelBtn.setText("Cancel");
	       m_cancelBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
	       m_cancelBtn.setPreferredSize(new java.awt.Dimension(50, 20));
	       //    TopButtonPanel.add(m_cancelBtn);

	        m_submitBtn.setText("Submit");
	        m_submitBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
	        m_submitBtn.setPreferredSize(new java.awt.Dimension(50, 20));
	        //  TopButtonPanel.add(m_submitBtn);

	         jPanel1_1.add(TopButtonPanel, java.awt.BorderLayout.WEST);

	         jPanel1.add(jPanel1_1, java.awt.BorderLayout.NORTH);

	         jPanel1_2.setLayout(new java.awt.BorderLayout());

	         jPanel1_2.setPreferredSize(new java.awt.Dimension(650, 320));
	         jPanel1_2_1.setLayout(new java.awt.GridBagLayout());

	         jPanel1_2_1.setPreferredSize(new java.awt.Dimension(420, 320));
	         jPanel1_2_1_2.setLayout(new java.awt.GridBagLayout());

	         jPanel1_2_1_2.setPreferredSize(new java.awt.Dimension(322, 21));
	         AmountReceiveLabel2.setText("Total Instalment");
	         AmountReceiveLabel2.setPreferredSize(new java.awt.Dimension(110, 15));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 0;
	         gridBagConstraints.gridy = 1;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
	         jPanel1_2_1_2.add(AmountReceiveLabel2, gridBagConstraints);

	         TotalInsCurrText.setPreferredSize(new java.awt.Dimension(52, 18));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 1;
	         gridBagConstraints.gridy = 1;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 3);
	         jPanel1_2_1_2.add(TotalInsCurrText, gridBagConstraints);

	         TotalInsAmountText.setPreferredSize(new java.awt.Dimension(145, 18));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 2;
	         gridBagConstraints.gridy = 1;
	         gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
	         jPanel1_2_1_2.add(TotalInsAmountText, gridBagConstraints);

	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 0;
	         gridBagConstraints.gridy = 2;
	         gridBagConstraints.gridwidth = 2;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
	         jPanel1_2_1.add(jPanel1_2_1_2, gridBagConstraints);

	         StatusLabel.setText("Status");
	         StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 0;
	         gridBagConstraints.gridy = 0;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_1.add(StatusLabel, gridBagConstraints);

	         StatusText.setPreferredSize(new java.awt.Dimension(200, 18));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 1;
	         gridBagConstraints.gridy = 0;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_1.add(StatusText, gridBagConstraints);

	         SubmittedDateLabel.setText("Submitted Date");
	         SubmittedDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 0;
	         gridBagConstraints.gridy = 1;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_1.add(SubmittedDateLabel, gridBagConstraints);

	         SubmittedDateText.setPreferredSize(new java.awt.Dimension(200, 18));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 1;
	         gridBagConstraints.gridy = 1;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_1.add(SubmittedDateText, gridBagConstraints);

	         AccountLabel.setText("Account*");
	         AccountLabel.setPreferredSize(new java.awt.Dimension(110, 15));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 0;
	         gridBagConstraints.gridy = 3;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_1.add(AccountLabel, gridBagConstraints);

	         AccountComp.setPreferredSize(new java.awt.Dimension(200, 18));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 1;
	         gridBagConstraints.gridy = 3;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_1.add(AccountComp, gridBagConstraints);

	         DescriptionLabel.setText("Description*");
	         DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 0;
	         gridBagConstraints.gridy = 4;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_1.add(DescriptionLabel, gridBagConstraints);

	         DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(200, 75));
	         DescriptionTextArea.setColumns(20);
	         DescriptionTextArea.setLineWrap(true);
	         DescriptionTextArea.setRows(5);
	         DescriptionScrollPane.setViewportView(DescriptionTextArea);

	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 1;
	         gridBagConstraints.gridy = 4;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_1.add(DescriptionScrollPane, gridBagConstraints);

	         jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);

	         jPanel1_2_2.setLayout(new java.awt.BorderLayout());

	         jPanel1_2_2.setPreferredSize(new java.awt.Dimension(325, 305));
	         jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());

	         jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(325, 400));
	         IOweUNoLabel.setText("Receipt No *");
	         IOweUNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 0;
	         gridBagConstraints.gridy = 0;
	         gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_2_1.add(IOweUNoLabel, gridBagConstraints);

	         RefNoText.setPreferredSize(new java.awt.Dimension(200, 18));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 1;
	         gridBagConstraints.gridy = 0;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_2_1.add(RefNoText, gridBagConstraints);

	         IOweUDateLabel.setText("Receipt Date *");
	         IOweUDateLabel.setPreferredSize(new java.awt.Dimension(95, 15));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 0;
	         gridBagConstraints.gridy = 1;
	         gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_2_1.add(IOweUDateLabel, gridBagConstraints);

	         TransactionDateDate.setPreferredSize(new java.awt.Dimension(200, 18));
	         gridBagConstraints = new java.awt.GridBagConstraints();
	         gridBagConstraints.gridx = 1;
	         gridBagConstraints.gridy = 1;
	         gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
	         gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
	         jPanel1_2_2_1.add(TransactionDateDate, gridBagConstraints);

	         RemarksScrollPane.setPreferredSize(new java.awt.Dimension(200, 115));
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
	         OriginatorComp.setPreferredSize(new java.awt.Dimension(275, 110));
	         jPanel1_3.add(OriginatorComp, java.awt.BorderLayout.WEST);

	         jPanel1_3_2.setLayout(new java.awt.BorderLayout());

	         ApprovedComp.setLayout(new java.awt.GridBagLayout());

	         ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved by"));
	         ApprovedComp.setOpaque(false);
	         ApprovedComp.setPreferredSize(new java.awt.Dimension(275, 110));

	         jPanel1_3_2.add(ApprovedComp, java.awt.BorderLayout.WEST);

	         jPanel1_3_2_2.setLayout(new java.awt.BorderLayout());

	         ReceivedComp.setLayout(new java.awt.GridBagLayout());

	         ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Received by"));
	         ReceivedComp.setOpaque(false);
	         ReceivedComp.setPreferredSize(new java.awt.Dimension(275, 110));
	         jPanel1_3_2_2.add(ReceivedComp, java.awt.BorderLayout.WEST);

	         jPanel1_3_2.add(jPanel1_3_2_2, java.awt.BorderLayout.CENTER);

	         jPanel1_3.add(jPanel1_3_2, java.awt.BorderLayout.CENTER);

	         jPanel1.add(jPanel1_3, java.awt.BorderLayout.SOUTH);

	         add(jPanel1, java.awt.BorderLayout.NORTH);

	         }// </editor-fold>//GEN-END:initComponents
	         */
	private void addingListener(){
		m_printViewRefNoBtn.addActionListener(this);
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource() == m_saveBtn) {
			//doSave();
		}else if (e.getSource() == m_cancelBtn) {

		}else if (e.getSource() == m_newBtn) {

			//super.doNew();
			clearForm();
			isiDefaultAssignPanel();
		}else if (e.getSource() == m_submitBtn) {
			System.out.println("Manggil submit");
		}else if (e.getSource() == m_editBtn){
		}else if (e.getSource() == m_deleteBtn){
		}else if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0){
				new Rcpt_IOUSettledOthers(m_owner,entityparent,m_entity);
			}else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			//JasperVoucherCashNonProject jasper =
			//new JasperVoucherCashNonProject(new Object[0]);
		}
	}

	public double TotalInstallment;
	public void HitungInstallment(PmtCAIOUOthers temp)
	{ try {
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
		TotalInsAmountText.setValue(new Double(amountInstall));
	}
	catch (Exception e)
	{

	}

	}
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
		OriginatorComp.m_datePicker.setDate(new Date());
		ApprovedComp.m_datePicker.setDate(new Date());
		ReceivedComp.m_datePicker.setDate(new Date());
		//Ini di hard code lho...
		//	TotalSettledtCurrText.setText("Rp");
		TotalInsCurrText.setText(entityparent.getCurrency().getSymbol());
		//EndingBalanceCurrText.setText("Rp");
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private LookupCashAccountPicker AccountComp;
	private javax.swing.JLabel AccountLabel;
	private javax.swing.JLabel AmountReceiveLabel2;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JLabel IOweUDateLabel;
	private javax.swing.JLabel IOweUNoLabel;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScrollPane;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JTextField SubmittedDateText;
	private javax.swing.JFormattedTextField TotalInsAmountText;
	private javax.swing.JTextField TotalInsCurrText;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel1_1;
	private javax.swing.JPanel TopButtonPanel;
	private javax.swing.JPanel jPanel1_2;
	private javax.swing.JPanel jPanel1_2_1;
	private javax.swing.JPanel jPanel1_2_1_2;
	private javax.swing.JPanel jPanel1_2_2;
	private javax.swing.JPanel jPanel1_2_2_1;
	private javax.swing.JPanel jPanel1_3;
	private AssignPanel OriginatorComp;
	private javax.swing.JPanel jPanel1_3_2;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel jPanel1_3_2_2;
	private AssignPanel ReceivedComp;
	// End of variables declaration//GEN-END:variables

	// End of variables declaration//GEN-END:variables
	protected void gui2entity() {
		entity().setReceiveTo("CASH");
		entity().setCashAccount((CashAccount)AccountComp.getObject());
		entity().setUnit(entity().getCashAccount().getUnit());

		Object obj = TotalInsAmountText.getValue();
		Number amount = (Number) obj;
		entity().transTemplateRead(
				this.OriginatorComp,this.ApprovedComp,
				this.ReceivedComp,this.RefNoText.getText(),
				this.DescriptionTextArea.getText(), this.RemarksTextArea.getText(),
				this.TransactionDateDate.getDate(),new Double(1).doubleValue());

		obj = TotalInsAmountText.getValue();
		amount = (Number) obj;
		if (amount!=null)
			entity().setAmount(amount.doubleValue());
		entity().setPmtcaiouothers(entityparent);
		entity().setCurrency(baseCurrency);
	}

	protected void entity2gui() {
		AccountComp.setObject(entity().getCashAccount());
		TotalInsAmountText.setValue(new Double(entity().getAmount()));
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

		//Ubahan 24 Mei 2007
		OriginatorComp.setJobTitle(entity().getJobTitleOriginator());
		ApprovedComp.setJobTitle(entity().getJobTitleApproved());
		ReceivedComp.setJobTitle(entity().getJobTitleReceived());
		//===============*/
		StatusText.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");
	}

	protected Object createNew()
	{
		PmtCAIOUOthersReceive a  = new PmtCAIOUOthersReceive();
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
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0"))
			{	long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_CA_IOU_OTHERS_RECEIVE, m_conn);
			entity().setIndex(index);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	protected void doSubmit() {
		validityMsgs.clear();
		if (AccountComp.getObject()==null)
			addInvalid("Account comp mus be selected");
		if (validityMsgs.size()>0){
			StringBuffer result = new StringBuffer();
			Iterator iter = validityMsgs.iterator();
			while (iter.hasNext())			{
				String o = iter.next().toString();
				result.append(o);
				result.append("\r\n");
			}
			JOptionPane.showMessageDialog(this,result);
			return;
		}
		super.doSubmit();
	}

	protected void doDelete() {
		super.doDelete();
		// tambah tambah
		setParentStatus();
	}

	private void setParentStatus() {
		// sebelum diubah status cek apakah yang make dia sudah habis atau tidak
		boolean isAllDeleted = true;

		// installment:
		List list = getUsedList(PmtCAIOUOthersInstall.class);
		isAllDeleted = isAlreadyDeleted(isAllDeleted, list);

		// settlement:
		list = getUsedList(PmtCAIOUOthersSettled.class);
		isAllDeleted = isAlreadyDeleted(isAllDeleted, list);

		// received:
		list = getUsedList(PmtCAIOUOthersReceive.class);
		isAllDeleted = isAlreadyDeleted(isAllDeleted, list);

		if (isAllDeleted) {
			entityparent.setStatus(StateTemplateEntity.State.SAVED);
			GenericMapper mapnya = MasterMap.obtainMapperFor(PmtCAIOUOthers.class);
			mapnya.setActiveConn(m_conn);
			mapnya.doUpdate(entityparent);
		}

	}

	private List getUsedList(Class clazz) {
		GenericMapper mapper = MasterMap.obtainMapperFor(clazz);
		mapper.setActiveConn(m_conn);
		List list = mapper.doSelectWhere(IDBConstants.TABLE_PMT_CA_IOU_OTHERS + "=" + entityparent.getIndex());
		return list;
	}

	private boolean isAlreadyDeleted(boolean isAllDeleted, List list) {
		if (list.size()==0)
			isAllDeleted &= true; // kosong
		else
			isAllDeleted &= false;
		return isAllDeleted;
	}

	protected StateTemplateEntity currentEntity() {
		return entity();
	}

	void setEntity(Object m_entity) {
		PmtCAIOUOthersReceive oldEntity = this.m_entity;
		if (oldEntity!=null)
		{
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PmtCAIOUOthersReceive)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}

	PmtCAIOUOthersReceive entity() {
		return m_entity;
	}

	protected void enableEditMode()
	{  // this.AccountComp.setEnabled(true);
		//this.TotalInsAmountText.setEditable(true);
		this.TransactionDateDate.setEditable(true);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);


	}
	protected void disableEditMode()
	{ 	// this.AccountComp.setEnabled(false);
		//this.TotalInsAmountText.setEditable(false);
		this.TransactionDateDate.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);

	}
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity()
	{
		validityMsgs.clear();
		/*if (AccountComp.getObject()==null)
			addInvalid("Account comp mus be selected");*/

		if (TransactionDateDate.getDate() == null)
			addInvalid("Voucher date must be selected");

		if (DescriptionTextArea.getText().compareTo("")==0)
			addInvalid("Description Text Area must be filled");
		if (validityMsgs.size()>0)
		{
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
	private void addInvalid(String string) {
		validityMsgs.add(string);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName()))
		{
			readEntityState();
		}

	}
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_RECEIVE, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_RECEIVE, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_RECEIVE, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	private void enableAwal(){
		setenableEditPanel(jPanel1_2_1,false);
		//	setenableEditPanel(jPanel1_2_1_1,false);
		setenableEditPanel(jPanel1_2_2_1,false);
		this.AccountComp.setEnabled(false);
		this.TotalInsAmountText.setEditable(false);
		this.TotalInsCurrText.setEditable(false);
		//	PaymentSourceCombo.setEnabled(false);
		//CurrComp.setEnabled(false);
		//WorkDescText.setEnabled(false);
	}
	private double toBaseCurrency() {
		try{
			double amount;
			if (!TotalInsAmountText.getText().equalsIgnoreCase(""))
				amount=((Number)m_numberFormatter.stringToValue(TotalInsAmountText.getText())).doubleValue();
			else
				amount=0;
			return amount;
		}catch (Exception e)
		{
			System.out.println("Error");
			return 0;
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

	//private EmployeePicker payToComp() {
	//return PayToComp;
//	}
	protected void clearForm() {
		clearTextField(jPanel1_2_2_1);
		clearTextField(jPanel1_2_2);
		clearTextField(jPanel1_2_1);
		clearTextField(jPanel1_2_1);
	}
	protected void clearTextField(JPanel temppanel) {
		Component[] componentList=temppanel.getComponents();
		JTextField temptext;
		for (int i=0;i<componentList.length;i++)
		{ if (componentList[i] instanceof JTextField)
		{ temptext=(JTextField)componentList[i];
		temptext.setText("");
		}
		}
	}

	public void clearAll(){
		super.clearAll();
		clearForm();
		clearKomponen();
	}
	//Ini ntar harus diisi lho
	public void clearKomponen(){

	}
}

