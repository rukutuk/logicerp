package pohaci.gumunda.titis.accounting.cgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.StateTemplateEntity.State;
import pohaci.gumunda.titis.accounting.cgui.report.Vchr_UnitCashBankTrans;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.PmtUnitBankCashTrans;
import pohaci.gumunda.titis.accounting.entity.UnitBankCashTransferLoader;
import pohaci.gumunda.titis.application.DatePicker;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class PaymentUnitBankCashTransferPanel extends RevTransactionPanel
		implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	private PmtUnitBankCashTrans m_entity;

	protected Employee defaultOriginator;

	protected Employee defaultApproved;

	protected Employee defaultReceived;

	public PaymentUnitBankCashTransferPanel(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		initNumberFormats();
		initComponents();
		enableAwal();
		disableEditMode();
		addingListener();
		addingListenerParents();
		initBaseCurrency(m_conn, m_sessionid);
		initExchangeRateHelper(m_conn, m_sessionid);
		setDefaultSignature();
		setEntity(new PmtUnitBankCashTrans());
		m_entityMapper = MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	private void setDefaultExchangeRate(Currency currency) {
		Date date = TransactionDateDate.getDate();
		double rate = getDefaultExchangeRate(currency, date);
		ExchRateText.setValue(new Double(rate));
	}

	protected void entity2gui() {
		// CurrencyAmountBaseCurrText.setText("");
		// AmountBaseCurrentText.setValue(new Double(0.0));
		if (entity().isSourceBank()) {
			PaymentSourceCombo.setSelectedItem("Bank");
			AccountComp.setObject(entity().getBankAccount());
		} else {
			PaymentSourceCombo.setSelectedItem("Cash");
			AccountComp.setObject(entity().getCashAccount());
		}
		ChequeNoText.setText(entity().getChequeNo());
		ChequeDueDateDate.setDate(entity().getChequeDueDate());

		CurrencyAmountBaseCurrText.setText(baseCurrency.getSymbol());
		CurrencyAmountBaseCurrText2.setText(baseCurrency.getSymbol());

		/*
		 * if(entity().getCurrency()!=null)
		 * CurrText.setText(entity().getCurrency().getSymbol()); else
		 * CurrText.setText(baseCurrency.getSymbol());
		 */

		

		if (entity().isReceivingBank()) {
			PayToCombo.setSelectedItem("Bank");
			RcvAccountComp.setObject(entity().getRcvBankAccount());
		} else {
			PayToCombo.setSelectedItem("Cash");
			RcvAccountComp.setObject(entity().getRcvCashAccount());
		}

		RefNoText.setText(entity().getReferenceNo());
		if (entity().getTransactionDate() != null)
			TransactionDateDate.setDate(entity().getTransactionDate());
		else
			TransactionDateDate.setDate(new Date());
		
		if (entity().getExchangeRate() > 0)
			ExchRateText.setValue(new Double(entity().getExchangeRate()));
		else
			ExchRateText.setValue(new Double(1));

		AmountText.setValue(new Double(entity().getAmount()));

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
	}

	protected void gui2entity() {
		String paySource = (String) PaymentSourceCombo.getSelectedItem();
		CashBankAccount cb = null;
		if (paySource.equalsIgnoreCase("BANK")) {
			entity().setPaymentSource("BANK");
			entity().setBankAccount((BankAccount) AccountComp.getObject());
			entity().setCashAccount(null);
			entity().setUnit(entity().getBankAccount().getUnit());
			cb = (BankAccount) AccountComp.getObject();
		} else {
			entity().setPaymentSource("CASH");
			entity().setCashAccount((CashAccount) AccountComp.getObject());
			entity().setBankAccount(null);
			entity().setUnit(entity().getCashAccount().getUnit());
			cb = (CashAccount) AccountComp.getObject();
		}
		entity().setChequeNo(ChequeNoText.getText());
		entity().setChequeDueDate(ChequeDueDateDate.getDate());
		String payTo = (String) PayToCombo.getSelectedItem();
		if (payTo.equalsIgnoreCase("BANK")) {
			entity().setPayTo("BANK");
			entity().setRcvBankAccount((BankAccount) RcvAccountComp.getObject());
			entity().setRcvCashAccount(null);
		} else {
			entity().setPayTo("CASH");
			entity().setRcvCashAccount((CashAccount) RcvAccountComp.getObject());
			entity().setRcvBankAccount(null);
		}
		entity().setCurrency(cb.getCurrency());
		Number amt = (Number) ExchRateText.getValue();
		if (amt != null)
			entity().setExchangeRate(amt.doubleValue());
		Object obj = AmountText.getValue();
		Number amount = (Number) obj;
		entity().setAmount(amount.doubleValue());
		entity().setTransactionDate(TransactionDateDate.getDate());
		entity().setRemarks(RemarksTextArea.getText());
		entity().transTemplateRead(this.OriginatorComp, this.ApprovedComp,
				this.ReceivedComp, this.RefNoText, this.DescriptionTextArea);
	}

	private void initComponents() {
		setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));
		setRcvAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));
		CurrText = new javax.swing.JTextField();
		OriginatorComp = new AssignPanel(m_conn, m_sessionid, "Originator");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid, "Approved by");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid, "Received by");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon(
				"../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon(
				"../images/filter.gif"));
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
		jPanel1_2_2 = new javax.swing.JPanel();
		jPanel1_2_2_1 = new javax.swing.JPanel();
		VoucherNoLabel = new javax.swing.JLabel();
		RefNoText = new javax.swing.JTextField();
		VoucherDateLabel = new javax.swing.JLabel();
		DescriptionLabel = new javax.swing.JLabel();
		DescriptionScrollPane = new javax.swing.JScrollPane();
		DescriptionTextArea = new javax.swing.JTextArea();
		RemarksLabel = new javax.swing.JLabel();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();
		jPanel1_2_1 = new javax.swing.JPanel();
		StatusLabel = new javax.swing.JLabel();
		SubmittedDateLabel = new javax.swing.JLabel();
		TransferFromLabel = new javax.swing.JLabel();
		PaymentSourceLabel = new javax.swing.JLabel();
		AccountLabel = new javax.swing.JLabel();
		UnitCodeFromLabel = new javax.swing.JLabel();
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		jSeparator1 = new javax.swing.JSeparator();
		PaymentSourceCombo = new javax.swing.JComboBox();
		FromUnitCodeText = new javax.swing.JTextField();
		ChequeNoLabel = new javax.swing.JLabel();
		ChequeDueDateLabel = new javax.swing.JLabel();
		ChequeNoText = new javax.swing.JTextField();
		TransferToLabel = new javax.swing.JLabel();
		PayToLabel = new javax.swing.JLabel();
		ChequeDueDateDate = new DatePicker();
		jSeparator2 = new javax.swing.JSeparator();
		ReceiveAccountLabel = new javax.swing.JLabel();
		PayToCombo = new javax.swing.JComboBox();
		UnitCodeToLabel = new javax.swing.JLabel();
		ToUnitCodeText = new javax.swing.JTextField();
		jPanel1_2_1_1 = new javax.swing.JPanel();
		// CurrencyLabel = new javax.swing.JLabel();
		ExchRateLabel = new javax.swing.JLabel();
		ExchRateText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountLabel = new javax.swing.JLabel();
		AmountText = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountBaseCurrLabel = new javax.swing.JLabel();
		CurrencyAmountBaseCurrText = new javax.swing.JTextField();
		AmountBaseCurrentText = new javax.swing.JFormattedTextField(
				m_numberFormatter);
		// Inisialisasi komponen baru
		jPanel1_2_2_2_1 = new javax.swing.JPanel();
		AmountLabel2 = new javax.swing.JLabel();
		AmountText2 = new javax.swing.JFormattedTextField(m_numberFormatter);
		AmountBaseCurrLabel2 = new javax.swing.JLabel();
		AmountBaseCurrentText2 = new javax.swing.JFormattedTextField(
				m_numberFormatter);
		CurrencyAmountBaseCurrText2 = new JTextField();
		CurrText2 = new JTextField();
		ExchRateLabel2 = new JLabel();
		ExchRateText2 = new javax.swing.JFormattedTextField(m_numberFormatter);

		jPanel1_3 = new javax.swing.JPanel();
		jPanel1_3_2 = new javax.swing.JPanel();
		jPanel1_3_2_2 = new javax.swing.JPanel();
		setLayout(new java.awt.BorderLayout());
		setPreferredSize(new java.awt.Dimension(700, 475));

		jPanel1.setLayout(new java.awt.BorderLayout());
		jPanel1.setPreferredSize(new java.awt.Dimension(670, 430));
		jPanel1_1.setLayout(new java.awt.BorderLayout());
		jPanel1_1.setPreferredSize(new java.awt.Dimension(700, 35));
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
		jPanel1_2.setPreferredSize(new java.awt.Dimension(700, 300));
		jPanel1_2_2.setLayout(new java.awt.BorderLayout());
		jPanel1_2_2.setPreferredSize(new java.awt.Dimension(415, 400));

		jPanel1_2_2_1.setLayout(new java.awt.GridBagLayout());
		jPanel1_2_2_1.setPreferredSize(new java.awt.Dimension(420, 325));

		VoucherNoLabel.setText("     Voucher No*");
		VoucherNoLabel.setPreferredSize(new java.awt.Dimension(125, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(VoucherNoLabel, gridBagConstraints);

		RefNoText.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RefNoText, gridBagConstraints);

		VoucherDateLabel.setText("Voucher Date*");
		VoucherDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(VoucherDateLabel, gridBagConstraints);

		RemarksLabel.setText("Remarks");
		RemarksLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RemarksLabel, gridBagConstraints);

		RemarksScrollPane.setOpaque(false);
		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(280, 120));
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScrollPane.setViewportView(RemarksTextArea);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(RemarksScrollPane, gridBagConstraints);

		TransactionDateDate.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
		jPanel1_2_2_1.add(TransactionDateDate, gridBagConstraints);

		jPanel1_2_2.add(jPanel1_2_2_1, java.awt.BorderLayout.WEST);
		jPanel1_2.add(jPanel1_2_2, java.awt.BorderLayout.CENTER);

		jPanel1_2_1.setLayout(new java.awt.GridBagLayout());
		jPanel1_2_1.setPreferredSize(new java.awt.Dimension(410, 325));

		DescriptionLabel.setText("Description*");
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DescriptionLabel, gridBagConstraints);

		DescriptionScrollPane.setOpaque(false);
		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(290, 80));// 120
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(DescriptionScrollPane, gridBagConstraints);

		StatusLabel.setText("Status");
		StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(StatusLabel, gridBagConstraints);

		SubmittedDateLabel.setText("Submitted Date");
		SubmittedDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(SubmittedDateLabel, gridBagConstraints);

		TransferFromLabel.setText("Transfer From");
		TransferFromLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(TransferFromLabel, gridBagConstraints);

		PaymentSourceLabel.setText("Payment Source*");
		PaymentSourceLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PaymentSourceLabel, gridBagConstraints);

		AccountLabel.setText("Account*");
		AccountLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(AccountLabel, gridBagConstraints);

		UnitCodeFromLabel.setText("Unit Code");
		UnitCodeFromLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(UnitCodeFromLabel, gridBagConstraints);

		StatusText.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(StatusText, gridBagConstraints);

		SubmittedDateText.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(SubmittedDateText, gridBagConstraints);

		jSeparator1.setPreferredSize(new java.awt.Dimension(290, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(jSeparator1, gridBagConstraints);

		PaymentSourceCombo.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Cash", "Bank" }));
		PaymentSourceCombo.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(PaymentSourceCombo, gridBagConstraints);

		accountComp().setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(accountComp(), gridBagConstraints);

		FromUnitCodeText.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(FromUnitCodeText, gridBagConstraints);

		ChequeNoLabel.setText("Cheque No");
		ChequeNoLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeNoLabel, gridBagConstraints);

		ChequeDueDateLabel.setText("Cheque Due Date");
		ChequeDueDateLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeDueDateLabel, gridBagConstraints);

		ChequeNoText.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeNoText, gridBagConstraints);
		// ===
		TransferToLabel.setText("     Transfer To");
		TransferToLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(TransferToLabel, gridBagConstraints);
		// ==
		PayToLabel.setText("Pay To*");
		PayToLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(PayToLabel, gridBagConstraints);

		ChequeDueDateDate.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(ChequeDueDateDate, gridBagConstraints);
		// ==
		jSeparator2.setPreferredSize(new java.awt.Dimension(280, 5));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weightx = 1;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(jSeparator2, gridBagConstraints);

		ReceiveAccountLabel.setText("Receive Account*");
		ReceiveAccountLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ReceiveAccountLabel, gridBagConstraints);
		// ==
		PayToCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"Cash", "Bank" }));
		PayToCombo.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(PayToCombo, gridBagConstraints);
		// ==
		rcvAccountComp().setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(rcvAccountComp(), gridBagConstraints);
		// ==
		UnitCodeToLabel.setText("Unit Code");
		UnitCodeToLabel.setPreferredSize(new java.awt.Dimension(100, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(UnitCodeToLabel, gridBagConstraints);
		// ==
		ToUnitCodeText.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_1.add(ToUnitCodeText, gridBagConstraints);

		jPanel1_2_1_1.setLayout(new java.awt.GridBagLayout());
		jPanel1_2_1_1.setPreferredSize(new java.awt.Dimension(300, 60));

		/*
		 * CurrencyLabel.setText("Currency"); CurrencyLabel.setPreferredSize(new
		 * java.awt.Dimension(110, 15)); gridBagConstraints = new
		 * java.awt.GridBagConstraints(); gridBagConstraints.gridx = 0;
		 * gridBagConstraints.gridy = 0; gridBagConstraints.anchor =
		 * java.awt.GridBagConstraints.WEST; gridBagConstraints.insets = new
		 * java.awt.Insets(1, 3, 1, 1); jPanel1_2_1_1.add(CurrencyLabel,
		 * gridBagConstraints);
		 */

		ExchRateLabel.setText("Exch. Rate*");
		ExchRateLabel.setPreferredSize(new java.awt.Dimension(103, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(ExchRateLabel, gridBagConstraints);

		ExchRateText.setPreferredSize(new java.awt.Dimension(280, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_1_1.add(ExchRateText, gridBagConstraints);

		AmountLabel.setText("Amount");
		AmountLabel.setPreferredSize(new java.awt.Dimension(103, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountLabel, gridBagConstraints);

		AmountText.setPreferredSize(new java.awt.Dimension(180, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountText, gridBagConstraints);

		// CurrComp.setPreferredSize(new java.awt.Dimension(80, 18));
		CurrText.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		// jPanel1_2_1_1.add(CurrComp, gridBagConstraints);
		jPanel1_2_1_1.add(CurrText, gridBagConstraints);

		AmountBaseCurrLabel.setText("  Amount Base Curr.");
		AmountBaseCurrLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountBaseCurrLabel, gridBagConstraints);

		CurrencyAmountBaseCurrText.setPreferredSize(new java.awt.Dimension(85,
				18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_1_1.add(CurrencyAmountBaseCurrText, gridBagConstraints);

		AmountBaseCurrentText.setPreferredSize(new java.awt.Dimension(180, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1_1.add(AmountBaseCurrentText, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_1.add(jPanel1_2_1_1, gridBagConstraints);

		// ==============
		jPanel1_2_2_2_1.setLayout(new java.awt.GridBagLayout());
		jPanel1_2_2_2_1.setPreferredSize(new java.awt.Dimension(412, 60));

		ExchRateLabel2.setText("         Exch. Rate*");
		ExchRateLabel2.setPreferredSize(new java.awt.Dimension(128, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_2_1.add(ExchRateLabel2, gridBagConstraints);

		ExchRateText2.setPreferredSize(new java.awt.Dimension(110, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
		jPanel1_2_2_2_1.add(ExchRateText2, gridBagConstraints);

		AmountLabel2.setText("Amount");
		AmountLabel2.setPreferredSize(new java.awt.Dimension(103, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_2_1.add(AmountLabel2, gridBagConstraints);

		AmountText2.setText("");
		AmountText2.setPreferredSize(new java.awt.Dimension(180, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		// gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_2_1.add(AmountText2, gridBagConstraints);

		CurrText2.setPreferredSize(new java.awt.Dimension(85, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_2_2_1.add(CurrText2, gridBagConstraints);

		AmountBaseCurrLabel2.setText("Amount Base Curr.");
		AmountBaseCurrLabel2.setPreferredSize(new java.awt.Dimension(103, 15));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_2_1.add(AmountBaseCurrLabel2, gridBagConstraints);

		AmountBaseCurrentText2
				.setPreferredSize(new java.awt.Dimension(180, 18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		// gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		jPanel1_2_2_2_1.add(AmountBaseCurrentText2, gridBagConstraints);

		CurrencyAmountBaseCurrText2.setPreferredSize(new java.awt.Dimension(85,
				18));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		jPanel1_2_2_2_1.add(CurrencyAmountBaseCurrText2, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
		jPanel1_2_2_1.add(jPanel1_2_2_2_1, gridBagConstraints);

		// ============
		jPanel1_2.add(jPanel1_2_1, java.awt.BorderLayout.WEST);
		jPanel1.add(jPanel1_2, java.awt.BorderLayout.CENTER);
		jPanel1_3.setLayout(new java.awt.BorderLayout());
		jPanel1_3.setPreferredSize(new java.awt.Dimension(700, 90));

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
	}

	private void addingListener() {
		PaymentSourceCombo.addActionListener(this);
		PayToCombo.addActionListener(this);
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this);
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);
		accountComp().addPropertyChangeListener("object", this);
		rcvAccountComp().addPropertyChangeListener("object", this);
		AmountText.addPropertyChangeListener("value",this);
		ExchRateText.addPropertyChangeListener("value",this);
		AmountText2.addPropertyChangeListener("value",this);
		ExchRateText2.addPropertyChangeListener("value",this);
		TransactionDateDate.addPropertyChangeListener("date", this);
	}

	protected void doNew() {
		super.doNew();
		isiDefaultAssignPanel();
		this.ChequeNoText.setEditable(false);
		this.ChequeDueDateDate.setEditable(false);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == PaymentSourceCombo) {
			String receive = (String) PaymentSourceCombo.getSelectedItem();
			jPanel1_2_1.remove(accountComp());
			jPanel1_2_1.revalidate();
			jPanel1_2_1.repaint();
			if (receive.equals("Cash")) {
				setAccountComp(new LookupCashAccountPicker(m_conn, m_sessionid));
				ChequeNoText.setText("");
				ChequeDueDateDate.setDate(null);
				this.ChequeNoText.setEditable(false);
				this.ChequeDueDateDate.setEditable(false);
			} else {
				setAccountComp(new LookupBankAccountPicker(m_conn, m_sessionid));
				ChequeNoText.setText("");
				ChequeDueDateDate.setDate(null);
				this.ChequeNoText.setEditable(true);
				this.ChequeDueDateDate.setEditable(true);
			}
			accountComp().setPreferredSize(new java.awt.Dimension(280, 18));

			java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 4;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
			jPanel1_2_1.add(accountComp(), gridBagConstraints);
			jPanel1_2_1.revalidate();
		} else if (e.getSource() == PayToCombo) {
			String receive = (String) PayToCombo.getSelectedItem();
			System.out.println(receive);
			jPanel1_2_2_1.remove(rcvAccountComp());
			jPanel1_2_2_1.revalidate();
			jPanel1_2_2_1.repaint();
			if (receive.equals("Cash"))
				setRcvAccountComp(new LookupCashAccountPicker(m_conn,
						m_sessionid));
			else
				setRcvAccountComp(new LookupBankAccountPicker(m_conn,
						m_sessionid));

			// rcvAccountComp().setPreferredSize(new java.awt.Dimension(310,
			// 18));
			java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
			/*
			 * gridBagConstraints.gridx = 1; gridBagConstraints.gridy = 10;
			 * gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			 * gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
			 */
			rcvAccountComp().setPreferredSize(new java.awt.Dimension(280, 18));
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 4;
			// gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
			jPanel1_2_2_1.add(rcvAccountComp(), gridBagConstraints);

			// jPanel1_2_2_1.add(rcvAccountComp(), gridBagConstraints);
			jPanel1_2_2_1.revalidate();
		} else if (e.getSource() == m_printViewRefNoBtn) {
			if (m_entity.getIndex() > 0)
				new Vchr_UnitCashBankTrans(m_entity, m_conn, m_sessionid);
			else {
				JOptionPane.showMessageDialog(this, "Data is empty", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		} else if (e.getSource() == m_searchRefNoBtn) {
			SearchVoucherDialog dlg = new SearchVoucherDialog(GumundaMainFrame
					.getMainFrame(), "Search Voucher", m_conn, m_sessionid,
					new UnitBankCashTransferLoader(m_conn,
							PmtUnitBankCashTrans.class),
					"Unit Bank/Cash Transfer");
			dlg.setVisible(true);
			if (dlg.selectedObj != null) {
				doLoad(dlg.selectedObj);
			}
		}
	}

	protected void clearAll() {
		super.clearAll();
		cleanUp();
		disableEditMode();
	}

	private void cleanUp() {
		StatusText.setText("");
		ExchRateText.setValue(null);
		CurrencyAmountBaseCurrText.setText("");
		CurrencyAmountBaseCurrText2.setText("");
		CurrText.setText("");
		CurrText2.setText("");
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
		AmountBaseCurrentText.setValue(new Double(0.0));
	}

	protected Object createNew() {
		PmtUnitBankCashTrans a = new PmtUnitBankCashTrans();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a;
	}

	protected StateTemplateEntity currentEntity() {
		return entity();
	}

	void setEntity(Object m_entity) {
		PmtUnitBankCashTrans oldEntity = this.m_entity;
		if (oldEntity != null) {
			oldEntity.removePropertyChangeListener(this);
		}
		this.m_entity = (PmtUnitBankCashTrans) m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}

	PmtUnitBankCashTrans entity() {
		return m_entity;
	}

	private LookupPicker AccountComp;

	private javax.swing.JLabel AccountLabel;

	private javax.swing.JLabel AmountBaseCurrLabel;

	private javax.swing.JFormattedTextField AmountBaseCurrentText;

	private javax.swing.JLabel AmountLabel;

	private javax.swing.JFormattedTextField AmountText;

	private DatePicker ChequeDueDateDate;

	private javax.swing.JLabel ChequeDueDateLabel;

	private javax.swing.JLabel ChequeNoLabel;

	private javax.swing.JTextField ChequeNoText;

	private javax.swing.JTextField CurrText;

	private javax.swing.JTextField CurrencyAmountBaseCurrText;

	// private javax.swing.JLabel CurrencyLabel;;
	private javax.swing.JLabel DescriptionLabel;

	private javax.swing.JScrollPane DescriptionScrollPane;

	private javax.swing.JTextArea DescriptionTextArea;

	private javax.swing.JLabel ExchRateLabel;

	private javax.swing.JFormattedTextField ExchRateText;

	private javax.swing.JTextField FromUnitCodeText;

	private javax.swing.JComboBox PayToCombo;

	private javax.swing.JLabel PayToLabel;

	private javax.swing.JComboBox PaymentSourceCombo;

	private javax.swing.JLabel PaymentSourceLabel;

	private LookupPicker RcvAccountComp;

	private javax.swing.JLabel ReceiveAccountLabel;

	private javax.swing.JTextField RefNoText;

	private javax.swing.JLabel RemarksLabel;

	private javax.swing.JScrollPane RemarksScrollPane;

	private javax.swing.JTextArea RemarksTextArea;

	private javax.swing.JLabel StatusLabel;

	private javax.swing.JTextField StatusText;

	private javax.swing.JLabel SubmittedDateLabel;

	private javax.swing.JTextField SubmittedDateText;

	private javax.swing.JTextField ToUnitCodeText;

	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;

	private javax.swing.JLabel TransferFromLabel;

	private javax.swing.JLabel TransferToLabel;

	private javax.swing.JLabel UnitCodeFromLabel;

	private javax.swing.JLabel UnitCodeToLabel;

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

	private javax.swing.JSeparator jSeparator1;

	private javax.swing.JSeparator jSeparator2;

	// Ini komponen Tambahan
	private javax.swing.JPanel jPanel1_2_2_2_1;

	private javax.swing.JLabel AmountBaseCurrLabel2;

	private javax.swing.JFormattedTextField AmountBaseCurrentText2;

	private javax.swing.JLabel AmountLabel2;

	private javax.swing.JFormattedTextField AmountText2;

	private javax.swing.JTextField CurrText2;

	private javax.swing.JTextField CurrencyAmountBaseCurrText2;

	private javax.swing.JLabel ExchRateLabel2;

	private javax.swing.JFormattedTextField ExchRateText2;

	public void propertyChange(PropertyChangeEvent evt) {
		if ("state".equals(evt.getPropertyName())) {
			readEntityState();
		}
		if ("object".equals(evt.getPropertyName())) {
			if (evt.getSource() == accountComp()) {
				if (evt.getNewValue() == null) {
					FromUnitCodeText.setText("");
					CurrText.setText(baseCurrency.getSymbol());
				} else if (evt.getNewValue() instanceof BankAccount) {
					BankAccount b = (BankAccount) accountComp().getObject();
					FromUnitCodeText.setText(b.getUnit().getDescription());
					CurrText.setText(b.getCurrency().getSymbol());

					if (!((entity().getStatus() == State.SUBMITTED)
							|| (entity().getStatus() == State.POSTED) || (entity()
							.getStatus() == State.VERIFIED))) {
						changeExchangeRate();
						changeReceivingAmount();
						toBaseCurrency();
					}
				} else if (evt.getNewValue() instanceof CashAccount) {
					CashAccount b = (CashAccount) accountComp().getObject();
					FromUnitCodeText.setText(b.getUnit().getDescription());
					CurrText.setText(b.getCurrency().getSymbol());

					if (!((entity().getStatus() == State.SUBMITTED)
							|| (entity().getStatus() == State.POSTED) || (entity()
							.getStatus() == State.VERIFIED))) {
						changeExchangeRate();
						changeReceivingAmount();
						toBaseCurrency();
					}
				}
			}
			if (evt.getSource() == rcvAccountComp()) {
				if (evt.getNewValue() == null) {
					ToUnitCodeText.setText("");
					CurrText2.setText(baseCurrency.getSymbol());
				} else if (evt.getNewValue() instanceof BankAccount) {
					BankAccount b = (BankAccount) rcvAccountComp().getObject();
					ToUnitCodeText.setText(b.getUnit().getDescription());
					CurrText2.setText(b.getCurrency().getSymbol());

					if (!((entity().getStatus() == State.SUBMITTED)
							|| (entity().getStatus() == State.POSTED) || (entity()
							.getStatus() == State.VERIFIED))) {
						changeExchangeRate();
						changeReceivingAmount();
						toRcvBaseCurrency();
					}
				} else if (evt.getNewValue() instanceof CashAccount) {
					CashAccount b = (CashAccount) rcvAccountComp().getObject();
					ToUnitCodeText.setText(b.getUnit().getDescription());
					CurrText2.setText(b.getCurrency().getSymbol());

					if (!((entity().getStatus() == State.SUBMITTED)
							|| (entity().getStatus() == State.POSTED) || (entity()
							.getStatus() == State.VERIFIED))) {
						changeExchangeRate();
						changeReceivingAmount();
						toRcvBaseCurrency();
					}
				}
			}
		}
		if ("value".equals(evt.getPropertyName())) {
			if ((evt.getSource() == AmountText)
					|| (evt.getSource() == ExchRateText)) {
				toBaseCurrency();
				changeReceivingAmount();
			}
			if (evt.getSource() == ExchRateText) {
				ExchRateText2.setValue(ExchRateText.getValue());
			}
			if ((evt.getSource() == AmountText2)
					|| (evt.getSource() == ExchRateText2)) {
				toRcvBaseCurrency();
			}
		}
		if ("date".equals(evt.getPropertyName())) {
			if (evt.getSource() == TransactionDateDate) {

				CashBankAccount source = (CashBankAccount) accountComp().getObject();
				CashBankAccount receiving = (CashBankAccount) rcvAccountComp().getObject();
				if (source != null) {
					if (source.getCurrency().getIndex()!=baseCurrency.getIndex())
						setDefaultExchangeRate(source.getCurrency());
					else {
						if (receiving != null) {
							if (receiving.getCurrency().getIndex()!=baseCurrency.getIndex())
								setDefaultExchangeRate(receiving.getCurrency());
						}
					}
				}
			}
		}
	}

	private void changeReceivingAmount() {
		Number sourceAmountNbr = (Number) AmountText.getValue();
		Number exchangeRateNbr = (Number) ExchRateText.getValue();

		CashBankAccount source = null;
		if (accountComp().getObject() instanceof BankAccount)
			source = (BankAccount) accountComp().getObject();
		else
			source = (CashAccount) accountComp().getObject();

		CashBankAccount receiving = null;
		if (rcvAccountComp().getObject() instanceof BankAccount)
			receiving = (BankAccount) rcvAccountComp().getObject();
		else
			receiving = (CashAccount) rcvAccountComp().getObject();

		if ((source != null) && (receiving != null)) {
			if (source.getCurrency().getIndex() == receiving.getCurrency()
					.getIndex()) {
				AmountText2.setValue(AmountText.getValue());
			} else {
				double amt = 0;
				if (source.getCurrency().getIndex() == baseCurrency.getIndex()) {
					// Rp ke $
					amt = sourceAmountNbr.doubleValue()
							/ exchangeRateNbr.doubleValue();
				} else {
					// $ ke Rp
					amt = sourceAmountNbr.doubleValue()
							* exchangeRateNbr.doubleValue();
				}
				AmountText2.setValue(new Double(amt));
			}
		} else {
			if ((sourceAmountNbr != null) && (exchangeRateNbr != null)) {
				double amt = sourceAmountNbr.doubleValue()
						* exchangeRateNbr.doubleValue();
				AmountText2.setValue(new Double(amt));
			} else {
				AmountText2.setValue(AmountText.getValue());
			}
		}
	}

	private void changeExchangeRate() {
		CashBankAccount source = null;
		if (accountComp().getObject() instanceof BankAccount)
			source = (BankAccount) accountComp().getObject();
		else
			source = (CashAccount) accountComp().getObject();

		boolean isNotSourceBaseCurrency = false;
		if (source != null) {
			if (source.getCurrency().getIndex() != baseCurrency.getIndex())
				isNotSourceBaseCurrency = true;
		}

		CashBankAccount receiving = null;
		if (rcvAccountComp().getObject() instanceof BankAccount)
			receiving = (BankAccount) rcvAccountComp().getObject();
		else
			receiving = (CashAccount) rcvAccountComp().getObject();

		boolean isNotReceivingBaseCurrency = false;
		if (receiving != null) {
			if (receiving.getCurrency().getIndex() != baseCurrency.getIndex())
				isNotReceivingBaseCurrency = true;
		}

		if (isNotSourceBaseCurrency || isNotReceivingBaseCurrency){
			ExchRateText.setEditable(true);
			if (isNotSourceBaseCurrency)
				setDefaultExchangeRate(source.getCurrency());
			else {
				if (isNotReceivingBaseCurrency)
					setDefaultExchangeRate(receiving.getCurrency());
			}
		}
		else {
			ExchRateText.setEditable(false);
			ExchRateText.setValue(new Double(1.0));
		}
	}
	
	private void enableExchangeRate() {
		CashBankAccount source = null;
		if (accountComp().getObject() instanceof BankAccount)
			source = (BankAccount) accountComp().getObject();
		else
			source = (CashAccount) accountComp().getObject();

		boolean isNotSourceBaseCurrency = false;
		if (source != null) {
			if (source.getCurrency().getIndex() != baseCurrency.getIndex())
				isNotSourceBaseCurrency = true;
		}

		CashBankAccount receiving = null;
		if (rcvAccountComp().getObject() instanceof BankAccount)
			receiving = (BankAccount) rcvAccountComp().getObject();
		else
			receiving = (CashAccount) rcvAccountComp().getObject();

		boolean isNotReceivingBaseCurrency = false;
		if (receiving != null) {
			if (receiving.getCurrency().getIndex() != baseCurrency.getIndex())
				isNotReceivingBaseCurrency = true;
		}

		if (isNotSourceBaseCurrency || isNotReceivingBaseCurrency){
			ExchRateText.setEditable(true);
		}
		else {
			ExchRateText.setEditable(false);
			ExchRateText.setValue(new Double(1.0));
		}
	}

	private void toBaseCurrency() {
		Number amount = (Number) AmountText.getValue();
		Number xchRate = (Number) ExchRateText.getValue();

		CashBankAccount source = null;
		if (accountComp().getObject() instanceof BankAccount)
			source = (BankAccount) accountComp().getObject();
		else
			source = (CashAccount) accountComp().getObject();

		if ((amount != null) && (xchRate != null)) {
			double amt = ((Number) AmountText.getValue()).doubleValue();
			double exch = ((Number) ExchRateText.getValue()).doubleValue();

			if (source == null) {
				AmountBaseCurrentText.setValue(new Double(amt));
			} else {
				if (source.getCurrency().getIndex() == baseCurrency.getIndex()) {
					AmountBaseCurrentText.setValue(new Double(amt));
				} else {
					double baseVal = new Double(exch).doubleValue()
							* new Double(amt).doubleValue();
					AmountBaseCurrentText.setValue(new Double(baseVal));
				}
			}
		} else {
			AmountBaseCurrentText.setValue(null);
		}
	}

	private void toRcvBaseCurrency() {
		Number amount = (Number) AmountText2.getValue();
		Number xchRate = (Number) ExchRateText2.getValue();

		CashBankAccount receiving = null;
		if (rcvAccountComp().getObject() instanceof BankAccount)
			receiving = (BankAccount) rcvAccountComp().getObject();
		else
			receiving = (CashAccount) rcvAccountComp().getObject();

		if ((amount != null) && (xchRate != null)) {
			double amt = ((Number) AmountText2.getValue()).doubleValue();
			double exch = ((Number) ExchRateText2.getValue()).doubleValue();

			if (receiving == null) {
				AmountBaseCurrentText2.setValue(new Double(amt));
			} else {
				if (receiving.getCurrency().getIndex() == baseCurrency
						.getIndex()) {
					AmountBaseCurrentText2.setValue(new Double(amt));
				} else {
					double baseVal = new Double(exch).doubleValue()
							* new Double(amt).doubleValue();
					AmountBaseCurrentText2.setValue(new Double(baseVal));
				}
			}
		} else {
			AmountBaseCurrentText2.setValue(null);
		}
	}

	protected void enableEditMode() {
		this.PaymentSourceCombo.setEnabled(true);
		// this.AccountComp.setEnabled(true);
		this.AmountBaseCurrentText.setEditable(false);
		this.ChequeNoText.setEditable(true);
		this.ChequeDueDateDate.setEditable(true);
		this.PayToCombo.setEnabled(true);
		// this.RcvAccountComp.setEnabled(true);
		this.AmountText.setEditable(true);
		this.TransactionDateDate.setEditable(true);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);

		accountComp().setEnabled(true);
		rcvAccountComp().setEnabled(true);
		enableExchangeRate();
	}

	protected void disableEditMode() {
		this.PaymentSourceCombo.setEnabled(false);
		// this.AccountComp.setEnabled(false);
		this.AmountBaseCurrentText.setEditable(false);
		this.ChequeNoText.setEditable(false);
		this.ChequeDueDateDate.setEditable(false);
		this.PayToCombo.setEnabled(false);
		// this.RcvAccountComp.setEnabled(false);
		this.AmountText.setEditable(false);
		this.TransactionDateDate.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);

		accountComp().setEnabled(false);
		rcvAccountComp().setEnabled(false);
		this.ExchRateText.setEditable(false);
	}

	ArrayList validityMsgs = new ArrayList();

	protected boolean cekValidity() {
		validityMsgs.clear();
		if (TransactionDateDate.getDate() == null)
			addInvalid("Transaction date must be selected");
		if (AccountComp.getObject() == null)
			addInvalid("Source account must be selected");
		if (RcvAccountComp.getObject() == null)
			addInvalid("Receiving account must be selected");
		if (DescriptionTextArea.getText().equals(""))
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

	private void setRcvAccountComp(LookupPicker rcvAccountComp) {
		LookupPicker old = RcvAccountComp;
		if (old != null) {
			old.removePropertyChangeListener("object", this);
		}
		RcvAccountComp = rcvAccountComp;
		RcvAccountComp.addPropertyChangeListener("object", this);
	}

	private LookupPicker rcvAccountComp() {
		return RcvAccountComp;
	}

	protected void doSave() {
		if (!cekValidity())
			return;
		if (!checkBalance())
			return;
		super.doSave();
		/*AccountingSQLSAP sql = new AccountingSQLSAP();
		try {
			long index = sql.getMaxIndex(
					IDBConstants.TABLE_PMT_UNIT_BANKCASH_TRNS, m_conn);
			entity().setIndex(index);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}

	private boolean checkBalance() {
		gui2entity();
		currentEntity().isetBaseCurrency(baseCurrency);
		if (!entity().simulationSubmit(this.m_sessionid, this.m_conn)) {
			JOptionPane
					.showMessageDialog(
							this,
							"It probably results in an unbalance transaction. Please check transaction amount and exchange rate!");
			return false;
		}
		return true;
	}

	private void isiDefaultAssignPanel() {
		OriginatorComp.m_jobTextField
				.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField
				.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField
				.setText(getEmployeeJobTitle(defaultReceived));
	}

	private void enableAwal() {
		setenableEditPanel(jPanel1_2_1, false);
		setenableEditPanel(jPanel1_2_1_1, false);
		setenableEditPanel(jPanel1_2_2_1, false);
		setenableEditPanel(jPanel1_2_2_2_1, false);
		CurrText.setEditable(false);
		ExchRateText.setEditable(false);
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

}
