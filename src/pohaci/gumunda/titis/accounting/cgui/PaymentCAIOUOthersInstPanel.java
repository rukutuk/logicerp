
package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import pohaci.gumunda.titis.accounting.cgui.report.Vcr_IOUInstOthers;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersInstall;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersReceive;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthersSettled;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class PaymentCAIOUOthersInstPanel extends RevTransactionPanel 
implements ActionListener,PropertyChangeListener,DocumentListener{
	private static final long serialVersionUID = 1L;
	private PmtCAIOUOthersInstall m_entity;
	private PmtCAIOUOthers entityparent;
	protected Employee defaultOriginator;
	protected Employee defaultApproved;
	protected Employee defaultReceived;
	JDialog m_owner;
	public PaymentCAIOUOthersInstPanel(JDialog owner,Connection conn, long sessionid,PmtCAIOUOthers obj) {
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
		setEntity(new PmtCAIOUOthersInstall());
		m_entityMapper = MasterMap.obtainMapperFor(PmtCAIOUOthersInstall.class);
		m_entityMapper.setActiveConn(m_conn);
		stateButtonAwal();
	}
	
	CAIOUListPanel caiouListPanel;
	private void initComponents() {
		caiouListPanel = new CAIOUListPanel(m_conn, m_sessionid,entityparent); 
		OriginatorComp = new AssignPanel(m_conn, m_sessionid,"Originator","IOU");
		ApprovedComp = new AssignPanel(m_conn, m_sessionid,"Approved by","IOU");
		ReceivedComp = new AssignPanel(m_conn, m_sessionid,"Received by","IOU");
		m_searchRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter2.gif"));
		m_printViewRefNoBtn = new javax.swing.JButton(new ImageIcon("../images/filter.gif"));
		java.awt.GridBagConstraints gridBagConstraints;
		
		jSplitPane1 = new javax.swing.JSplitPane();
		midlePanel = new javax.swing.JPanel();
		outTopButtonPanel = new javax.swing.JPanel();
		TopButtonPanel = new javax.swing.JPanel();
		outLeftPanel = new javax.swing.JPanel();
		leftPanel = new javax.swing.JPanel();
		outRightPanel = new javax.swing.JPanel();
		rightPanel = new javax.swing.JPanel();
		approvedPanel = new javax.swing.JPanel();
		receivedPanel = new javax.swing.JPanel();
		amountPanel = new javax.swing.JPanel();
		originatorPanel = new javax.swing.JPanel();
		
		m_newBtn = new javax.swing.JButton("New");
		m_editBtn = new javax.swing.JButton("Edit");
		m_saveBtn = new javax.swing.JButton("Save");
		m_deleteBtn = new javax.swing.JButton("Delete");
		m_cancelBtn = new javax.swing.JButton("Cancel");
		m_submitBtn = new javax.swing.JButton("Submit");
		
		StatusLabel = new javax.swing.JLabel("Status");
		SubmittedDateLabel = new javax.swing.JLabel("Submitted Date");
		ReceiveToLabel = new javax.swing.JLabel("Payment Source*");
		AmountReceiveLabel1 = new javax.swing.JLabel("I Owe U Installment");
		VoucherNoLabel = new javax.swing.JLabel("Voucher No");
		VoucherDateLabel = new javax.swing.JLabel("Voucher Date");
		DescriptionLabel = new javax.swing.JLabel("Decription*");
		RemarksLabel = new javax.swing.JLabel("Remarks");
		
		StatusText = new javax.swing.JTextField();
		SubmittedDateText = new javax.swing.JTextField();
		TotalAmountCurrText = new javax.swing.JTextField();		
		RefNoText = new javax.swing.JTextField();
		DescriptionTextArea = new javax.swing.JTextArea();
		
		PaymentSourceCombo = new javax.swing.JComboBox();		
		TotalAmountText = new javax.swing.JFormattedTextField(m_numberFormatter);		
		TransactionDateDate = new pohaci.gumunda.titis.application.DatePicker();		
		DescriptionScrollPane = new javax.swing.JScrollPane();
		RemarksScrollPane = new javax.swing.JScrollPane();
		RemarksTextArea = new javax.swing.JTextArea();		
		
		setLayout(new java.awt.BorderLayout());
		setPreferredSize(new java.awt.Dimension(700, 300));
		jSplitPane1.setDividerLocation(225);
		jSplitPane1.setDividerSize(10);
		jSplitPane1.setContinuousLayout(true);
		jSplitPane1.setOneTouchExpandable(true);
		jSplitPane1.setPreferredSize(new java.awt.Dimension(700, 300));
		jSplitPane1.setLeftComponent(caiouListPanel);
		
		midlePanel.setLayout(new java.awt.BorderLayout());
		midlePanel.setPreferredSize(new java.awt.Dimension(500, 400));
		outTopButtonPanel.setLayout(new java.awt.BorderLayout());
		
		
		m_printViewRefNoBtn.setPreferredSize(new java.awt.Dimension(21, 21));		
		m_newBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_newBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		m_editBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_editBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		m_deleteBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_deleteBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		m_saveBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_saveBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		m_cancelBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_cancelBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		m_submitBtn.setMargin(new java.awt.Insets(2, 2, 2, 2));
		m_submitBtn.setPreferredSize(new java.awt.Dimension(50, 20));
		
		TopButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));
		TopButtonPanel.setPreferredSize(new java.awt.Dimension(400, 35));
		TopButtonPanel.add(m_printViewRefNoBtn);		
		TopButtonPanel.add(m_newBtn);
		TopButtonPanel.add(m_editBtn);
		TopButtonPanel.add(m_deleteBtn);
		TopButtonPanel.add(m_saveBtn);
		TopButtonPanel.add(m_cancelBtn);
		TopButtonPanel.add(m_submitBtn);
		
		StatusLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		SubmittedDateLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		ReceiveToLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		AmountReceiveLabel1.setPreferredSize(new java.awt.Dimension(110, 15));
		RemarksLabel.setPreferredSize(new java.awt.Dimension(110, 15));
		VoucherNoLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		VoucherDateLabel.setPreferredSize(new java.awt.Dimension(95, 15));
		DescriptionLabel.setPreferredSize(new java.awt.Dimension(85, 15));
		
		StatusText.setPreferredSize(new java.awt.Dimension(200, 18));
		SubmittedDateText.setPreferredSize(new java.awt.Dimension(200, 18));
		TotalAmountCurrText.setPreferredSize(new java.awt.Dimension(52, 18));
		TotalAmountText.setPreferredSize(new java.awt.Dimension(145, 18));
		RemarksScrollPane.setPreferredSize(new java.awt.Dimension(200, 40));
		RefNoText.setPreferredSize(new java.awt.Dimension(200, 18));
		TransactionDateDate.setPreferredSize(new java.awt.Dimension(200, 18));
		DescriptionScrollPane.setPreferredSize(new java.awt.Dimension(200, 85));
		
		PaymentSourceCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Cash" }));
		PaymentSourceCombo.setPreferredSize(new java.awt.Dimension(200, 18));
		
		outTopButtonPanel.add(TopButtonPanel, java.awt.BorderLayout.WEST);
		midlePanel.add(outTopButtonPanel, java.awt.BorderLayout.NORTH);
		
		leftPanel.setLayout(new java.awt.GridBagLayout());
		leftPanel.setPreferredSize(new java.awt.Dimension(320, 420));
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(StatusLabel, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(SubmittedDateLabel, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(ReceiveToLabel, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(StatusText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(SubmittedDateText, gridBagConstraints);
	
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(PaymentSourceCombo, gridBagConstraints);
		
		amountPanel.setLayout(new java.awt.GridBagLayout());		
		amountPanel.setPreferredSize(new java.awt.Dimension(320, 25));		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
		amountPanel.add(AmountReceiveLabel1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 3);
		amountPanel.add(TotalAmountCurrText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		amountPanel.add(TotalAmountText, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
		leftPanel.add(amountPanel, gridBagConstraints);
		
		RemarksTextArea.setColumns(20);
		RemarksTextArea.setLineWrap(true);
		RemarksTextArea.setRows(5);
		RemarksScrollPane.setViewportView(RemarksTextArea);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 0);
		leftPanel.add(RemarksScrollPane, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		leftPanel.add(RemarksLabel, gridBagConstraints);
		
		outLeftPanel.setLayout(new java.awt.BorderLayout());
		outLeftPanel.setPreferredSize(new java.awt.Dimension(650, 320));
		outLeftPanel.add(leftPanel, java.awt.BorderLayout.WEST);
		outRightPanel.setLayout(new java.awt.BorderLayout());
		outRightPanel.setPreferredSize(new java.awt.Dimension(325, 305));
		rightPanel.setLayout(new java.awt.GridBagLayout());
		rightPanel.setPreferredSize(new java.awt.Dimension(320, 400));
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(VoucherNoLabel, gridBagConstraints);
		
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(RefNoText, gridBagConstraints);
		
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(VoucherDateLabel, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(TransactionDateDate, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(DescriptionLabel, gridBagConstraints);
		
		DescriptionTextArea.setColumns(20);
		DescriptionTextArea.setLineWrap(true);
		DescriptionTextArea.setRows(5);
		DescriptionScrollPane.setViewportView(DescriptionTextArea);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
		rightPanel.add(DescriptionScrollPane, gridBagConstraints);
		
		outRightPanel.add(rightPanel, java.awt.BorderLayout.WEST);
		outLeftPanel.add(outRightPanel, java.awt.BorderLayout.CENTER);
		
		midlePanel.add(outLeftPanel, java.awt.BorderLayout.CENTER);
		
		originatorPanel.setLayout(new java.awt.BorderLayout());
		originatorPanel.setPreferredSize(new java.awt.Dimension(500, 110));		
		OriginatorComp.setLayout(new java.awt.GridBagLayout());
		OriginatorComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Originator"));
		OriginatorComp.setOpaque(false);
		OriginatorComp.setPreferredSize(new java.awt.Dimension(230,110));
		originatorPanel.add(OriginatorComp, java.awt.BorderLayout.WEST);
		
		approvedPanel.setLayout(new java.awt.BorderLayout());
		ApprovedComp.setLayout(new java.awt.GridBagLayout());
		ApprovedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Approved by"));
		ApprovedComp.setOpaque(false);
		ApprovedComp.setPreferredSize(new java.awt.Dimension(230,110));
		approvedPanel.add(ApprovedComp, java.awt.BorderLayout.WEST);
		
		receivedPanel.setLayout(new java.awt.BorderLayout());		
		ReceivedComp.setLayout(new java.awt.GridBagLayout());
		ReceivedComp.setBorder(javax.swing.BorderFactory.createTitledBorder("Received by"));
		ReceivedComp.setOpaque(false);
		ReceivedComp.setPreferredSize(new java.awt.Dimension(230,110));
		receivedPanel.add(ReceivedComp, java.awt.BorderLayout.WEST);
		
		approvedPanel.add(receivedPanel, java.awt.BorderLayout.CENTER);		
		originatorPanel.add(approvedPanel, java.awt.BorderLayout.CENTER);
		
		midlePanel.add(originatorPanel, java.awt.BorderLayout.SOUTH);
		
		jSplitPane1.setRightComponent(midlePanel);
		add(jSplitPane1, java.awt.BorderLayout.NORTH);
	}
	
	private void addingListener(){
		caiouListPanel.m_list.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					if (caiouListPanel.m_list.getSelectedIndex()!=-1)
					{
						Object temp= caiouListPanel.m_list.getModel().getElementAt(caiouListPanel.m_list.getSelectedIndex());
						loadObject(temp);
					}
				}
			}
		});
		m_printViewRefNoBtn.addActionListener(this);
		m_searchRefNoBtn.addActionListener(this);	
		m_saveBtn.addActionListener(this);
		m_deleteBtn.addActionListener(this);
		m_cancelBtn.addActionListener(this); 
		m_submitBtn.addActionListener(this);
		m_editBtn.addActionListener(this);
		m_newBtn.addActionListener(this);    	
	}
	
	
	
	protected void doNew() {		
		super.doNew();
		clearForm();
		isiDefaultAssignPanel();
	}
	
	protected void doSubmit() {
		validityMsgs.clear();
		if (entityparent.getCashAccount()==null)
			addInvalid("Account source must be selected");
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
		caiouListPanel.refreshData();
		entityparent.setStatus(StateTemplateEntity.State.POSTED);
		try {
			GenericMapper mapnya;
			mapnya= MasterMap.obtainMapperFor(PmtCAIOUOthers.class);
			mapnya.setActiveConn(m_conn);
			mapnya.doUpdate(entityparent);
		}
		catch (Exception ev){    			
		}
	}
	
	protected void doDelete() {
		super.doDelete();
		// i add this
		setParentStatus();
		caiouListPanel.refreshData();
	}
	
	private void setParentStatus() {
		// sebelum diubah status cek apakah yang make dia sudah habis atau tidak
		boolean isAllDeleted = true;
		
		// installment:
		List list = getUsedList(PmtCAIOUOthersInstall.class, entityparent.getIndex());
		isAllDeleted = isAlreadyDeleted(isAllDeleted, list);
		
		// settlement:
		list = getUsedList(PmtCAIOUOthersSettled.class, entityparent.getIndex());
		isAllDeleted = isAlreadyDeleted(isAllDeleted, list);
		
		// received:
		list = getUsedList(PmtCAIOUOthersReceive.class, entityparent.getIndex());
		isAllDeleted = isAlreadyDeleted(isAllDeleted, list);
		
		if (isAllDeleted) {
			entityparent.setStatus(StateTemplateEntity.State.SAVED);
			GenericMapper mapnya = MasterMap.obtainMapperFor(PmtCAIOUOthers.class);
			mapnya.setActiveConn(m_conn);
			mapnya.doUpdate(entityparent);
		}
			
	}

	private boolean isAlreadyDeleted(boolean isAllDeleted, List list) {
		if (list.size()==0) 
			isAllDeleted &= true; // kosong
		else
			isAllDeleted &= false;
		return isAllDeleted;
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == m_printViewRefNoBtn){
			if (m_entity.getIndex()>0)
				new Vcr_IOUInstOthers(m_owner,entityparent,m_entity);
			else{
				JOptionPane.showMessageDialog(this, "Data is empty",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
	}
	
	private void isiDefaultAssignPanel(){
		OriginatorComp.m_jobTextField.setText(getEmployeeJobTitle(defaultOriginator));
		ApprovedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultApproved));
		ReceivedComp.m_jobTextField.setText(getEmployeeJobTitle(defaultReceived));
		OriginatorComp.m_datePicker.setDate(new Date());
		ApprovedComp.m_datePicker.setDate(new Date());
		ReceivedComp.m_datePicker.setDate(new Date());
		TotalAmountCurrText.setText(baseCurrency.getSymbol());
	}
	
	
	protected void gui2entity() {
		String paysource = (String)PaymentSourceCombo.getSelectedItem();
		entity().setPaymentsource(paysource);
		if (entityparent.getCashAccount()!=null){
			entity().setCashAccount(entityparent.getCashAccount());
			entity().setBankAccount(null);
			entity().setUnit(entity().getCashAccount().getUnit());
		}
		entity().transTemplateRead(
				this.OriginatorComp,this.ApprovedComp,
				this.ReceivedComp,this.RefNoText.getText(),
				this.DescriptionTextArea.getText(), this.RemarksTextArea.getText(),
				this.TransactionDateDate.getDate(),new Double(1).doubleValue());		
		if (TotalAmountText.getValue()!=null)
			entity().setAmount(((Number)TotalAmountText.getValue()).doubleValue());
		entity().setPmtcaiouothers(entityparent);
		entity().setCurrency(baseCurrency);
	}
	
	protected void entity2gui() {
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
		StatusText.setText(entity().statusInString());
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		if(entity().getSubmitDate()!=null)
			SubmittedDateText.setText(dateformat.format(entity().getSubmitDate()));
		else
			SubmittedDateText.setText("");		
	}
	
	protected Object createNew(){
		PmtCAIOUOthersInstall a  = new PmtCAIOUOthersInstall();
		a.setEmpOriginator(defaultOriginator);
		a.setEmpApproved(defaultApproved);
		a.setEmpReceived(defaultReceived);
		return a ;
	}
	
	public boolean cekInfoInstallment(){
		double amountSettled = ((Number)TotalAmountText.getValue()).doubleValue();
		double a=caiouListPanel.getAmountInstallment()+amountSettled-entity().getAmount();
		double b=entityparent.getAmount();
		if ((b-a)<0){		
			JOptionPane.showMessageDialog(this, "If you save this installment it will pass the value of Amount in I Owe U!!");
			return false;
		}
		else
			return true;
	}
	
	protected void doSave() {
		if (!cekValidity() ||!cekInfoInstallment()) return;
		super.doSave();
		caiouListPanel.refreshData();
		AccountingSQLSAP sql=new AccountingSQLSAP();
		try {
			if (new Long(entity().getIndex()).toString().equalsIgnoreCase("0")){
				long index = sql.getMaxIndex(IDBConstants.TABLE_PMT_CA_IOU_OTHERS_INSTALLMENTS, m_conn);
				entity().setIndex(index);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	protected StateTemplateEntity currentEntity() {
		return entity();
	}
	
	void setEntity(Object m_entity) {
		PmtCAIOUOthersInstall oldEntity = this.m_entity;
		if (oldEntity!=null)
			oldEntity.removePropertyChangeListener(this);		
		this.m_entity = (PmtCAIOUOthersInstall)m_entity;
		this.m_entity.addPropertyChangeListener(this);
	}
	
	PmtCAIOUOthersInstall entity() {
		return m_entity;
	}
	
	protected void enableEditMode(){		
		this.TotalAmountText.setEditable(true);
		this.TransactionDateDate.setEditable(true);
		this.DescriptionTextArea.setEnabled(true);
		this.RemarksTextArea.setEnabled(true);
		this.OriginatorComp.setEnabled(true);
		this.ApprovedComp.setEnabled(true);
		this.ReceivedComp.setEnabled(true);
	}
	
	protected void disableEditMode(){		
		this.TotalAmountText.setEditable(false);
		this.TransactionDateDate.setEditable(false);
		this.DescriptionTextArea.setEnabled(false);
		this.RemarksTextArea.setEnabled(false);
		this.OriginatorComp.setEnabled(false);
		this.ApprovedComp.setEnabled(false);
		this.ReceivedComp.setEnabled(false);
		
	}
	
	ArrayList validityMsgs = new ArrayList();
	protected boolean cekValidity(){
		validityMsgs.clear();
		if (TotalAmountText.getText().compareTo("")==0)
			addInvalid("Amount must be filled");
		if (TransactionDateDate.getDate() == null)
			addInvalid("Voucher date must be selected");		
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
		if ("state".equals(evt.getPropertyName())){
			readEntityState();
		}		
	}
	
	protected void setDefaultSignature() {
		Signature sign = null;
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_INSTALLMENT, Signature.SIGN_ORIGINATOR);
		if(sign!=null)
			defaultOriginator = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_INSTALLMENT, Signature.SIGN_APPROVED);
		if(sign!=null)
			defaultApproved = sign.getFullEmployee();
		sign = Signature.getSignature(m_conn, IConstants.PAYMENT_CASHADVANCE_IOU_OTHERS_INSTALLMENT, Signature.SIGN_RECEIVED);
		if(sign!=null)
			defaultReceived = sign.getFullEmployee();
	}
	private void enableAwal(){ 	
		setenableEditPanel(leftPanel,false);
		setenableEditPanel(amountPanel,false);
		setenableEditPanel(rightPanel,false);
		PaymentSourceCombo.setEnabled(false);
	}
	
	private double toBaseCurrency() {
		try{
			double amount;
			if (!TotalAmountText.getText().equalsIgnoreCase(""))
				amount=((Double)TotalAmountText.getValue()).doubleValue();
			else
				amount=0;
			return amount;
		}catch (Exception e){
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
	
	protected void clearForm() {
		clearTextField(rightPanel);
		clearTextField(outRightPanel);
		clearTextField(leftPanel);
		clearTextField(leftPanel);
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
		TransactionDateDate.setDate(null);
	}
		
	protected void loadObject(Object temp) {
		doLoad(temp);
		setButton();
		TotalAmountCurrText.setText(entity().getCurrency().getSymbol());
	}
	
	private void setButton() {
		boolean isThere = true;
		List list = getUsedList(PmtCAIOUOthersReceive.class, entityparent.getIndex());
		isThere &= (list.size()>0);
		
		list = getUsedList(PmtCAIOUOthersSettled.class, entityparent.getIndex());
		isThere &= (list.size()>0);
		
		setButtonEnable(m_editBtn, !isThere);
		setButtonEnable(m_deleteBtn, !isThere);
		setButtonEnable(m_saveBtn, !isThere);
		setButtonEnable(m_cancelBtn, !isThere);
		setButtonEnable(m_submitBtn, !isThere);
	}
	
	private void setButtonEnable(JButton btn, boolean val) {
		btn.setEnabled(btn.isEnabled() && val);
	}
	
	private List getUsedList(Class clazz, long parentIndex) {
		GenericMapper mapper = MasterMap.obtainMapperFor(clazz);
		mapper.setActiveConn(m_conn);
		List list = mapper.doSelectWhere(IDBConstants.TABLE_PMT_CA_IOU_OTHERS + "=" + parentIndex);
		return list;
	}

	private javax.swing.JLabel AmountReceiveLabel1;
	private javax.swing.JLabel DescriptionLabel;
	private javax.swing.JScrollPane DescriptionScrollPane;
	private javax.swing.JTextArea DescriptionTextArea;
	private javax.swing.JComboBox PaymentSourceCombo;
	private javax.swing.JLabel ReceiveToLabel;
	private javax.swing.JTextField RefNoText;
	private javax.swing.JLabel RemarksLabel;
	private javax.swing.JScrollPane RemarksScrollPane;
	private javax.swing.JTextArea RemarksTextArea;
	private javax.swing.JLabel StatusLabel;
	private javax.swing.JTextField StatusText;
	private javax.swing.JLabel SubmittedDateLabel;
	private javax.swing.JTextField SubmittedDateText;
	private javax.swing.JFormattedTextField TotalAmountText;
	private javax.swing.JTextField TotalAmountCurrText;
	private pohaci.gumunda.titis.application.DatePicker TransactionDateDate;
	private javax.swing.JLabel VoucherDateLabel;
	private javax.swing.JLabel VoucherNoLabel;
	private javax.swing.JPanel midlePanel;
	private javax.swing.JPanel outTopButtonPanel;
	private javax.swing.JPanel TopButtonPanel;
	private javax.swing.JPanel outLeftPanel;
	private javax.swing.JPanel leftPanel;
	private javax.swing.JPanel amountPanel;
	private javax.swing.JPanel outRightPanel;
	private javax.swing.JPanel rightPanel;
	private javax.swing.JPanel originatorPanel;
	private AssignPanel OriginatorComp;
	private javax.swing.JPanel approvedPanel;
	private AssignPanel ApprovedComp;
	private javax.swing.JPanel receivedPanel;
	private AssignPanel ReceivedComp;
	private javax.swing.JSplitPane jSplitPane1;
}
