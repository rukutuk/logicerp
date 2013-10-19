package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Component;
import java.awt.Dimension;
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

import pohaci.gumunda.titis.accounting.cgui.report.Vcr_IOUSettledProject;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProjectSettled;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class PaymentCAIOUProjectSettledPanel extends RevTransactionPanel 
implements ActionListener,PropertyChangeListener{
	
	private static final long serialVersionUID = 1L;
	private PaymentCAIOUProjectCashReceivePanel CashReceiveGUI; 
	private PmtCAIOUProjectSettled m_entity;
	private PmtCAIOUProject entityparent;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	JDialog m_owner;
	String m_msg = "Unbalance transaction";
	
	public PaymentCAIOUProjectSettledPanel(JDialog owner,Connection conn, long sessionid,PmtCAIOUProject obj,PaymentCAIOUProjectCashReceivePanel receivenya) {
		m_owner = owner;
		entityparent=obj;    	
		m_conn = conn;
		m_sessionid = sessionid;		
		initNumberFormats();
		initBaseCurrency(conn, sessionid); 
		initComponents();
		enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();		
		setDefaultSignature(); 
		setEntity(new PmtCAIOUProjectSettled());
		m_entityMapper = MasterMap.obtainMapperFor(PmtCAIOUProjectSettled.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
		CashReceiveGUI=receivenya;	
		PertamaMuncul(entityparent);
			
	}
		
	public void PertamaMuncul(PmtCAIOUProject temp){
		try {
			GenericMapper mapnya;
			Object[] listData;
			mapnya= MasterMap.obtainMapperFor(PmtCAIOUProjectSettled.class);
			mapnya.setActiveConn(m_conn);
			listData=mapnya.doSelectWhere("PMTCAIOUPROJECT ="+temp.getIndex()).toArray();
			if (listData.length==0){ 
				setEnabledSaveCancel(true);
				setEnabledNonSaveCancel(false);
				clearForm();
				doNew();				
				isiDefaultAssignPanel();
				enableEditMode();
				HitungInstallment(temp);			
			}else{
				doLoad((PmtCAIOUProjectSettled)listData[0]);
				checkReceived();
				warning();
				HitungInstallment(temp);
				TotalSettledtCurrText.setText("Rp");
				TotalInsCurrText.setText("Rp");
				EndingBalanceCurrText.setText("Rp");
			}  
		}
		catch (Exception e){		
		}	
	}
	
	private void warning() {
		if (!((entity().getStatus() <= StateTemplateEntity.State.SAVED) && (CashReceiveGUI
				.entity().getStatus() <= StateTemplateEntity.State.SAVED))) {
			String msg = "You can only view the transaction.";
			
			if (!((entity().getStatus() > StateTemplateEntity.State.SAVED) && (CashReceiveGUI
					.entity().getStatus() > StateTemplateEntity.State.SAVED))) {
				msg += "\nPlease notify that either submitted transaction\nor receive transaction is not submitted.";
			}
			JOptionPane.showMessageDialog(this, msg);
		}
	}

	private void checkReceived() {
		enableButton(m_editBtn, CashReceiveGUI.m_editBtn);
		enableButton(m_deleteBtn, CashReceiveGUI.m_deleteBtn);
		enableButton(m_saveBtn, CashReceiveGUI.m_saveBtn);
		enableButton(m_cancelBtn, CashReceiveGUI.m_cancelBtn);
		enableButton(m_submitBtn, CashReceiveGUI.m_submitBtn);
	}

	private void enableButton(JButton btn, JButton btn2) {
		btn.setEnabled(btn.isEnabled() && btn2.isEnabled());
	}
	
	private void initComponents() {
		AccountComp = new LookupBankAccountPicker(m_conn, m_sessionid, baseCurrency);
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by");
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		TopButtonPanel = new javax.swing.JPanel();
		java.awt.GridBagConstraints gridBagConstraints;		
		jPanel1 = new javax.swing.JPanel();
		jPanel1_1 = new javax.swing.JPanel();
		TopButtonPanel = new javax.swing.JPanel();
		ReceiveBtn = new javax.swing.JButton();
		m_editBtn = new javax.swing.JButton();
		m_newBtn= new JButton();
		m_searchRefNoBtn= new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		m_saveBtn = new javax.swing.JButton();
		m_deleteBtn = new javax.swing.JButton();
		m_cancelBtn = new javax.swing.JButton();
		m_submitBtn = new javax.swing.JButton();
		jPanel1_2 = new javax.swing.JPanel();
		jPanel1_2_1 = new javax.swing.JPanel();
		PayToLabel = new javax.swing.JLabel();
		PaymentSourceCombo = new javax.swing.JComboBox();
		ChequeNoLabel = new javax.swing.JLabel();
		ChequeNoText = new javax.swing.JTextField();
		jPanel1_2_1_2 = new javax.swing.JPanel();
		AmountReceiveLabel1 = new javax.swing.JLabel();
		RecvBaseCurrentLabel1 = new javax.swing.JLabel();
		TotalSettledtCurrText = new javax.swing.JTextField();
		EndingBalanceCurrText = new javax.swing.JTextField();
		TotalSettledText = new javax.swing.JFormattedTextField(m_numberFormatter);
		EndingBalanceAmountText = new javax.swing.JTextField();
		AmountReceiveLabel2 = new javax.swing.JLabel();
		TotalInsCurrText = new javax.swing.JTextField();
		TotalInsAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		StatusLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateLabel = new javax.swing.JLabel();
		SubmittedDateText = new javax.swing.JTextField();
		ChequeDueDateLabel = new javax.swing.JLabel();
		AccountLabel = new javax.swing.JLabel();
		ChequeDueDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_2_1 = new javax.swing.JPanel();
		IOweUNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		IOweUDateLabel = new javax.swing.JLabel();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		DescriptionLabel = new javax.swing.JLabel();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		
		setLayout(new java.awt.BorderLayout());
		
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		jPanel1.setPreferredSize(new java.awt.Dimension(650, 330));
		jPanel1_1.setLayout(new java.awt.BorderLayout());
		
		TopButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));
		
		TopButtonPanel.setPreferredSize(new java.awt.Dimension(650, 35));
		m_printViewRefNoBtn.setPreferredSize(new java.awt.Dimension(21, 21));
		TopButtonPanel.add(m_printViewRefNoBtn);
		
		ReceiveBtn.setText("Receive");
		ReceiveBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		ReceiveBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		
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
		PayToLabel.setText("Payment Source*");
		PayToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PayToLabel, gridBagConstraints);
		
		PaymentSourceCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bank"}));
		PaymentSourceCombo.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PaymentSourceCombo, gridBagConstraints);
		
		ChequeNoLabel.setText("Cheque No");
		ChequeNoLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeNoLabel, gridBagConstraints);
		
		ChequeNoText.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeNoText, gridBagConstraints);
		
		jPanel1_2_1_2.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_1_2.setPreferredSize(new java.awt.Dimension(412, 65));
		AmountReceiveLabel1.setText("I Owe U Settled *");
		AmountReceiveLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		jPanel1_2_1_2.add(AmountReceiveLabel1, gridBagConstraints);
		
		RecvBaseCurrentLabel1.setText("Ending Balance");
		RecvBaseCurrentLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		jPanel1_2_1_2.add(RecvBaseCurrentLabel1, gridBagConstraints);
		
		TotalSettledtCurrText.setPreferredSize(new java.awt.Dimension(52, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 3);
		jPanel1_2_1_2.add(TotalSettledtCurrText, gridBagConstraints);
		
		EndingBalanceCurrText.setPreferredSize(new java.awt.Dimension(52, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 3);
		jPanel1_2_1_2.add(EndingBalanceCurrText, gridBagConstraints);
		
		TotalSettledText.setPreferredSize(new java.awt.Dimension(235, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(TotalSettledText, gridBagConstraints);
		
		EndingBalanceAmountText.setPreferredSize(new java.awt.Dimension(235, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(EndingBalanceAmountText, gridBagConstraints);
		
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
		
		TotalInsAmountText.setPreferredSize(new java.awt.Dimension(235, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		jPanel1_2_1_2.add(TotalInsAmountText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
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
		
		ChequeDueDateLabel.setText("Cheque Due Date");
		ChequeDueDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeDueDateLabel, gridBagConstraints);
		
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
		
		ChequeDueDateDate.setPreferredSize(new java.awt.Dimension(290, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeDueDateDate, gridBagConstraints);
		
		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);
		
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(415, 305));
		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(415, 400));
		IOweUNoLabel.setText("Voucher No *");
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
		
		IOweUDateLabel.setText("Voucher Date*");
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
		
		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 75));
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
		
		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(DescriptionLabel, gridBagConstraints);
		
		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(290, 75));
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
	
	private void addingListener(){		
		m_printViewRefNoBtn.addActionListener(this);
		ReceiveBtn.addActionListener(this);
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this); 
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		//TotalSettledText.getDocument().addDocumentListener(this);		
		TotalSettledText.addPropertyChangeListener("value", this);
	}  
	
	protected void doDelete() {
		super.doDelete();
		// ubah status bapaknya
		// i add this
		setParentStatus();
		CashReceiveGUI.doDelete();
	}
	
	private void setParentStatus() {
		// sebelum diubah status cek apakah yang make dia sudah habis atau tidak
		boolean isAllDeleted = true;
		
		// installment:
		List list = getUsedList(PmtCAIOUProjectInstall.class);
		isAllDeleted = isAlreadyDeleted(isAllDeleted, list);
		
		// settlement:
		list = getUsedList(PmtCAIOUProjectSettled.class);
		isAllDeleted = isAlreadyDeleted(isAllDeleted, list);
		
		// received:
		list = getUsedList(PmtCAIOUProjectReceive.class);
		isAllDeleted = isAlreadyDeleted(isAllDeleted, list);
		
		if (isAllDeleted) {
			entityparent.setStatus(StateTemplateEntity.State.SAVED);
			GenericMapper mapnya = MasterMap.obtainMapperFor(PmtCAIOUProject.class);
			mapnya.setActiveConn(m_conn);
			mapnya.doUpdate(entityparent);
		}
			
	}

	private List getUsedList(Class clazz) {
		GenericMapper mapper = MasterMap.obtainMapperFor(clazz);
		mapper.setActiveConn(m_conn);
		List list = mapper.doSelectWhere(IDBConstants.TABLE_PMT_CA_IOU_PROJECT + "=" + entityparent.getIndex());
		return list;
	}

	private boolean isAlreadyDeleted(boolean isAllDeleted, List list) {
		if (list.size()==0) 
			isAllDeleted &= true; // kosong
		else
			isAllDeleted &= false;
		return isAllDeleted;
	}
	
	protected void doCancel() {
		super.doCancel();
		CashReceiveGUI.doCancel();
	}
	
	protected void doNew() {
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
	}
	
	protected void doSubmit() {
		validityMsgs.clear();
		if (AccountComp.getObject()==null)
			addInvalid("Account comp mus be selected");		
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
		CashReceiveGUI.doSubmit();
	}
	
	public void doEdit() {	
		super.doEdit();
		CashReceiveGUI.doEdit();
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex()>0){
				new Vcr_IOUSettledProject(m_owner,entityparent,m_entity);
			}else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}			
		}else if (e.getSource()==ReceiveBtn){  		  
			m_owner.setTitle("Cash Advance Project - I Owe You Cash Receive");
			PaymentCAIOUProjectCashReceivePanel PaymentCAIOUProjectPanel = new PaymentCAIOUProjectCashReceivePanel(m_owner,m_conn, m_sessionid,entityparent);		  
			m_owner.add(PaymentCAIOUProjectPanel);
			m_owner.setModal(true);
			m_owner.setSize(new Dimension(800,450));
			m_owner.setVisible(true);  
		}
	}
	
	public double TotalInstallment;
	public void HitungInstallment(PmtCAIOUProject temp){
		try {
			double amountInstall;
			GenericMapper mapnya;
			Object[] listData;
			mapnya= MasterMap.obtainMapperFor(PmtCAIOUProjectInstall.class);
			mapnya.setActiveConn(m_conn);
			listData=mapnya.doSelectWhere("PMTCAIOUProject ="+temp.getIndex()).toArray();
			amountInstall=0;   
			for(int i = 0; i < listData.length; i ++){
				PmtCAIOUProjectInstall data=(PmtCAIOUProjectInstall)listData[i];
				if (data.getStatus()==3)
					amountInstall=amountInstall+data.getAmount();				
			}
			TotalInstallment=amountInstall;
			TotalInsAmountText.setValue(new Double(amountInstall));	
		}
		catch (Exception e){			
		}		
	}
	
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
		OriginatorComp.m_datePicker.setDate(new Date());
		ApprovedComp.m_datePicker.setDate(new Date());
		ReceivedComp.m_datePicker.setDate(new Date());
		TotalSettledtCurrText.setText("Rp");
		TotalInsCurrText.setText("Rp");
		EndingBalanceCurrText.setText("Rp");
	}
	

	protected void gui2entity() {
		String paySource = (String) PaymentSourceCombo.getSelectedItem();
		entity().setPaymentsource(paySource);
		Object objAcc = AccountComp.getObject();
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
		}
		else{
			entity().setCashAccount(null);
			entity().setBankAccount(null);
			Unit unit = DefaultUnit.createDefaultUnit(m_conn,m_sessionid);
			entity().setUnit(unit);
		}
		Object obj = TotalSettledText.getValue();
		Number amount = (Number) obj;
		entity().transTemplateRead(
				this.OriginatorComp,this.ApprovedComp,
				this.ReceivedComp,this.RefNoText.getText(),
				this.DescriptionTextArea.getText(), this.RemarksTextArea.getText(),
				this.TransactionDateDate.getDate(),new Double(1).doubleValue());
		
		obj = TotalSettledText.getValue();
		amount = (Number) obj;
		if (amount!=null)
			entity().setAmount(amount.doubleValue());
		entity().setPmtcaiouproject(entityparent);
		entity().setCurrency(baseCurrency);
		entity().setChequeduedate(ChequeDueDateDate.getDate());
		entity().setChequeno(ChequeNoText.getText());
		
	}
	
	protected void entity2gui() {
		AccountComp.setObject(entity().getBankAccount());
		TotalSettledText.setValue(new Double(entity().getAmount()));
		RefNoText.setText(entity().getReferenceNo());
		if(entity().getTransactionDate()!=null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else
			TransactionDateDate.setDate(new Date());		
		
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
		if(entity().getChequeduedate()!=null)
			ChequeDueDateDate.setDate(entity().getTransactionDate());
		else
			ChequeDueDateDate.setDate(new Date());
		ChequeNoText.setText(entity().getChequeno());		
	}
	
	protected Object createNew(){
		PmtCAIOUProjectSettled a  = new PmtCAIOUProjectSettled();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}
	
	
	protected boolean cekBalance(){
		boolean cek = false;
		double amountSetled = ((Number)TotalSettledText.getValue()).doubleValue();		
		if (amountSetled>=TotalInstallment)
			cek=true;		
		return cek;
	}
	
	protected void doSave() {
		System.err.println("save");
		if ((!cekValidity()) ||(!CashReceiveGUI.cekValidity())) return;
		if (!cekBalance()){
			JOptionPane.showMessageDialog(this,m_msg);
			return;
		}
		super.doSave();
		AccountingSQLSAP sql=new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_CA_IOU_OTHERS_SETTLED, m_conn);
				entity().setIndex(index);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		CashReceiveGUI.doSave();
	}
	
	
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	void setEntity(Object m_entity) {
		PmtCAIOUProjectSettled oldEntity = this.m_entity;
		if (oldEntity!=null)
			oldEntity.removePropertyChangeListener(this);		
		this.m_entity = (PmtCAIOUProjectSettled)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}
	
	PmtCAIOUProjectSettled entity() {
		return m_entity;
	}
	
	protected void enableEditMode() {
		this.AccountComp.setEnabled(true);
		this.ChequeNoText.setEditable(true);
		this.TotalSettledText.setEditable(true);
		this.TransactionDateDate.setEditable(true);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
		//this.ChequeDueDateDate.setEnabled(true);
		this.ChequeDueDateDate.setEditable(true);
	}
	
	protected void disableEditMode() {
		this.AccountComp.setEnabled(false);
		this.ChequeNoText.setEditable(false);
		this.TotalSettledText.setEditable(false);
		this.TransactionDateDate.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
		//this.ChequeDueDateDate.setEnabled(false);
		this.ChequeDueDateDate.setEditable(false);
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		/*if (AccountComp.getObject()==null)
			addInvalid("Account comp mus be selected");*/		
		if (TransactionDateDate.getDate() == null)
			addInvalid("Voucher date must be selected");		
		if (DescriptionTextArea.getText().compareTo("")==0)
			addInvalid("Description Text Area must be filled");
		if(TotalSettledText.getText().equals(""))
			addInvalid("Total settled must be insert");
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
		if ("value".equals(evt.getPropertyName())){
			if (evt.getSource()==TotalSettledText){
				toBaseCurrency();
			}
		}
	}
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_PROJECT_SETTLEMENT, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	private void enableAwal(){ 	
		setenableEditPanel(jPanel1_2_1,false);
		setenableEditPanel(jPanel1_2_1_2, false);
		setenableEditPanel(jPanel1_2_2_1,false);
		PaymentSourceCombo.setEnabled(false);
	}

	private void toBaseCurrency() {
		try{
			double amount,endingAmount;
			if (!TotalSettledText.getText().equalsIgnoreCase(""))
				amount=((Number)TotalSettledText.getValue()).doubleValue();
			else
				amount=0;
			endingAmount=amount-TotalInstallment;
			EndingBalanceAmountText.setText(m_numberFormatter.valueToString(new Double(endingAmount)));
			
		}catch (Exception e){
			System.out.println("Error");
		}
		
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
	public void clearKomponen(){
		TotalInsAmountText.setText("");
		TotalSettledText.setText("");
		EndingBalanceAmountText.setText("");
		TotalInsCurrText.setText("");
		TotalSettledtCurrText.setText("");
		EndingBalanceCurrText.setText("");
		TransactionDateDate.setDate(null);
		OriginatorComp.setEmployee(null);
		ApprovedComp.setEmployee(null);
		ReceivedComp.setEmployee(null);
		OriginatorComp.m_jobTextField.setText("");
		ApprovedComp.m_jobTextField.setText("");
		ReceivedComp.m_jobTextField.setText("");
		OriginatorComp.setDate(null);
		ApprovedComp.setDate(null);
		ReceivedComp.setDate(null);
		ChequeDueDateDate.setDate(null);
		
	}
	
	private LookupBankAccountPicker AccountComp;
	private javax.swing.JLabel AccountLabel;
	private javax.swing.JLabel AmountReceiveLabel1;
	private javax.swing.JLabel AmountReceiveLabel2;
	private pohaci.gumunda.titis.application.DatePicker ChequeDueDateDate;
	private javax.swing.JLabel ChequeDueDateLabel;
	private javax.swing.JLabel ChequeNoLabel;
	private javax.swing.JTextField ChequeNoText;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JTextField EndingBalanceAmountText;
	private javax.swing.JTextField EndingBalanceCurrText;
	private javax.swing.JLabel IOweUDateLabel;
	private javax.swing.JLabel IOweUNoLabel;
	private javax.swing.JComboBox PaymentSourceCombo;
	private javax.swing.JLabel PayToLabel;
	private javax.swing.JButton ReceiveBtn;
	private javax.swing.JLabel RecvBaseCurrentLabel1;
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
	private javax.swing.JFormattedTextField TotalSettledText;
	private javax.swing.JTextField TotalSettledtCurrText;
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
	
}

