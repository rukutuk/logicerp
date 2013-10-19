package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.cgui.LookupPicker.DefaultTableModelAdapter;
import pohaci.gumunda.titis.accounting.entity.ReceiverLoader;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.SalesReceived;
import pohaci.gumunda.titis.hrm.cgui.EmployeeCellEditor;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class SearchSalesArReceiptDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	SearchTableArReceive m_searchTable;
	ReceiptListTable m_receiptListTable;
	JButton m_selectBt;
	int m_iResponse = JOptionPane.NO_OPTION;
	JFrame m_mainframe;
	SearchTableArReceive m_table;
	JRadioButton m_andRadioBt, m_orRadioBt;
	JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
	JButton m_findBt, m_closeBt, m_clearBt;
	private JTextField filterField = new JTextField(6);
	Object[] m_receiverList;
	
	Connection m_conn = null;
	long m_sessionid = -1;
	ReceiverLoader loader;
	private JComboBox m_statusComboBox,m_comboCashBank;
	private String m_type;  
	private String[] m_kolom;
	private String m_orderby;
	private DecimalFormat m_desimalFormat;
	
	public SearchSalesArReceiptDialog(JFrame owner, String title, Connection conn, long sessionid,String[] kolom,String orderby,ReceiverLoader loader,String type){
		super(owner, ( title == null ) ? "Search Receipt" : title, true);
		this.loader = loader;
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		m_kolom = kolom;
		m_orderby = orderby;
		this.m_type=type;
		setSize(650, 650);
		m_desimalFormat = new DecimalFormat("#,##0.00");
		constructComponent();
		find();
	}
	
	void constructComponent() {
		m_receiptListTable = new ReceiptListTable();
		m_receiptListTable.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					onAdd();	
				}
			}
		});
		m_statusComboBox = new JComboBox(new Object[]{"Not Submitted","Submitted","Posted","All"});//0,1,3,*
		m_comboCashBank = new JComboBox(new Object[]{"Cash/Bank","Bank","Cash"});
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		m_selectBt = new JButton("Select");
		buttonPanel.add(m_selectBt);
		m_selectBt.addActionListener(this);
		
		centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Receipt List",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
		Box vertBox  = Box.createVerticalBox();
		vertBox.add(filterField);
		vertBox.add(new JScrollPane(m_receiptListTable));
		centerPanel.add(vertBox, BorderLayout.CENTER);
		centerPanel.add(buttonPanel, BorderLayout.NORTH);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(criteriaPanel(), BorderLayout.NORTH);
		getContentPane().add(centerPanel, BorderLayout.CENTER);
	}
	
	private void setBorder(JPanel panel,String theme){
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), theme ,
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
	}
	
	JPanel criteriaPanel() {
		m_searchTable = new SearchTableArReceive();
		m_andRadioBt = new JRadioButton("and");
		m_andRadioBt.setSelected(true);
		m_orRadioBt = new JRadioButton("or");
		
		m_containsRadioBt = new JRadioButton("Text Contains Criteria");
		m_containsRadioBt.setSelected(true);
		m_matchRadioBt = new JRadioButton("Match Case");
		m_wholeRadioBt = new JRadioButton("Find Whole Words only");
		m_findBt = new JButton("Find");
		m_findBt.addActionListener(this);
		m_closeBt = new JButton("Cancel");
		m_closeBt.addActionListener(this);
		m_clearBt = new JButton("Clear");
		m_clearBt.addActionListener(this);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(m_andRadioBt);
		bg.add(m_orRadioBt);
		
		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(m_containsRadioBt);
		bg2.add(m_matchRadioBt);
		bg2.add(m_wholeRadioBt);
		
		JPanel centerPanel = new JPanel();
		JPanel criteriaPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel operatorPanel = new JPanel();
		JPanel optionPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gridBagConstraints;
		setBorder(operatorPanel,"Search Operator");
		setBorder(optionPanel,"Option");		
		
		scrollPane.setPreferredSize(new Dimension(100, 145));
		scrollPane.getViewport().add(m_searchTable);
		
		operatorPanel.setLayout(new GridLayout(2, 1));
		operatorPanel.add(m_andRadioBt);
		operatorPanel.add(m_orRadioBt);
		
		optionPanel.setLayout(new GridLayout(3, 1));
		optionPanel.add(m_containsRadioBt);
		optionPanel.add(m_matchRadioBt);
		optionPanel.add(m_wholeRadioBt);
		
		criteriaPanel.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		gridBagConstraints.weightx = 1.0;
		
		setBorder(criteriaPanel,"Search Criteria");
		
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(1, 0, 5, 0);
		criteriaPanel.add(scrollPane, gridBagConstraints);
		
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new Insets(1, 0, 5, 0);
		criteriaPanel.add(operatorPanel, gridBagConstraints);
		
		gridBagConstraints.gridy = 4;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		
		gridBagConstraints.gridy = 5;
		criteriaPanel.add(optionPanel, gridBagConstraints);
		
		buttonPanel.setLayout(new GridLayout(4, 1));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(m_findBt);
		buttonPanel.add(m_clearBt);
		buttonPanel.add(m_closeBt);
		gridBagConstraints.gridy = 6;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		criteriaPanel.add(buttonPanel, gridBagConstraints);
		
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(criteriaPanel, BorderLayout.NORTH);
		return centerPanel;
	}
	
	public void setVisible( boolean flag ){
		Rectangle rc = m_mainframe.getBounds();
		Rectangle rcthis = getBounds();
		setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
				(int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
				(int)rcthis.getWidth(), (int)rcthis.getHeight());
		super.setVisible(flag);
	}
	
	public int getResponse() {
		return m_iResponse;
	}
	
	void find() {
		String criterion;	  
		String operator=" and ";
		if(m_orRadioBt.isSelected()){
			operator =" or ";
		}
		criterion = loader.getCriterion(m_searchTable,m_kolom,operator) + m_orderby;
		m_receiverList = loader.find(criterion);		
		m_receiptListTable.setReceiptList(m_receiverList);
	}
	
	void clear() {
		m_searchTable.clear();
		m_andRadioBt.setSelected(true);
		find();
	}
	
	Object selectedObj;
	void onAdd(){
		m_iResponse = JOptionPane.OK_OPTION;
		int selectedRow = m_receiptListTable.getSelectedRow();
		if (selectedRow > -1){
			selectedObj = m_receiptListTable.getRow(selectedRow);
		}
		dispose();
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_selectBt) {
			onAdd();
		}else if (e.getSource()==m_findBt){
			find();
		}else if (e.getSource()==m_clearBt){
			clear();
		}else if (e.getSource()==m_closeBt){
			dispose();
		}
	}
	
	public class SearchTableArReceive extends JTable {	
		private static final long serialVersionUID = 1L;
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		public SearchTableArReceive() {						
			SearchTableModel model = new SearchTableModel();
			model.addColumn("Attribute");
			model.addColumn("Description");
			model.addRow(new Object[]{"Receive Type", m_type});
			model.addRow(new Object[]{"Receipt No", ""});
			model.addRow(new Object[]{"Receipt Date", dateformat.format(new Date())});
			model.addRow(new Object[]{"Receive by", ""});
			model.addRow(new Object[]{"Receive Account", "Bank/Cash"});
			model.addRow(new Object[]{"Receive Unit Code", ""});
			model.addRow(new Object[]{"Status", "All"});
			model.addRow(new Object[]{"Submitted Date", ""});
			
			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(100);
			getColumnModel().getColumn(0).setMaxWidth(100);
			getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
		}
		
		public TableCellEditor getCellEditor(int row, int col) {		
			if(row == 2 && col == 1){
				return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
			}
			else if(row == 3 && col == 1){
				return new EmployeeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
						m_conn, m_sessionid);			  
			}
			else if (row ==4 && col ==1){
				return new DefaultCellEditor(m_comboCashBank);
			}
			else if(row == 5 && col == 1){
				return new UnitCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
						"Unit Code",m_conn, m_sessionid);			  
			}
			else if(row == 7 && col == 1){
				return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());			  
			}
			else if(row == 6 )
				return new DefaultCellEditor(m_statusComboBox);
			return new BaseTableCellEditor();
		}
		
		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}
		public boolean isCellEditable(int row, int column) {
			if(row==0&&column==1)return false;			
			return super.isCellEditable(row, column);
		}
				
		public void clear() {
			stopCellEditing();
			int row = getRowCount();
			for(int i = 0; i < row; i ++)
				if (i==0)
					setValueAt(m_type, i, 1);
				else if(i==2)
					setValueAt(dateformat.format(new Date()), i, 1);
				else if (i==4)
					setValueAt("Bank/Cash", i, 1);					
				else if (i==6)
					setValueAt("All", i, 1);
				else
					setValueAt("", i, 1);			
		}
	}

	class SearchTableModel extends DefaultTableModel {		
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int col) {
			if(col == 0)
				return false;
			return true;
		}
	}
	
	public class ReceiptListTable extends JTable {
		private static final long serialVersionUID = 1L;
		DefaultTableModelAdapter model =new DefaultTableModelAdapter();
		public ReceiptListTable() {      
			
			model.addColumn("No");			
			model.addColumn("Receive Type");
			model.addColumn("Customer");
			model.addColumn("Total Receive");
			model.addColumn("Receipt No.");
			model.addColumn("Receipt Date");
			model.addColumn("Receipt by");
			model.addColumn("Bank Account");
			model.addColumn("Unit Code");
			model.addColumn("Status");
			model.addColumn("Submitted Date");
			setModel(model.buildTableModel(filterField));
			getColumnModel().getColumn(0).setPreferredWidth(40);
			getColumnModel().getColumn(0).setMaxWidth(40);
		}
		
		Object[] m_objRows;  
		public Object getRow(int rowIdx){
			Integer val = (Integer)getValueAt(rowIdx,0);
			int intVal = val.intValue()-1;
			return m_receiverList[intVal];
		}
		void setReceiptList(Object[] object) {
			model.clearRows();
			m_objRows = object;
			for(int i = 0; i < object.length; i ++) {   
				SalesReceived rec = (SalesReceived)object[i];
				String strCustomer = getCustomer(rec);
				double total = getTotalRec(rec);
				model.addRow(new Object[]{
						new Integer(i + 1),
						m_type,
						strCustomer,
						m_desimalFormat.format(total),
						rec.vgetReceiptNo(),
						rec.vgetReceiptDate(),
						rec.vgetReceiveFrom(),
						rec.vgetReceiveAccount(),
						rec.vgetUnitCode(),
						rec.vgetStatus(),
						rec.vgetSubmittedDate()    					
				});
			}    
		}
		
		public TableCellEditor getCellEditor(int row, int col) {
			return cellEditor;
		}
		void clear() {
			DefaultTableModel model = (DefaultTableModel)getModel();
			model.setRowCount(0);
		}
	}
	
	class ReceiptListTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {
			if(col == 0)
				return false;
			return true;
		}
	}
	public double getTotalRec(SalesReceived rec) {
		double totalrec = 0;
		if (rec.getCustomerStatus().equals("WAPU")){
			double sales = rec.getSalesARAmount()*rec.getSalesARExchRate();
			double retention = rec.getRetentionAmount();
			double tax = rec.getTax23Amount() * rec.getTax23ExchRate();
			double bankcharges = rec.getBankChargesAmount()* rec.getBankChargesExchRate();
			totalrec = sales - retention-tax - bankcharges;
		}else if (rec.getCustomerStatus().equals("NONWAPU")){
			double sales = rec.getSalesARAmount()*rec.getSalesARExchRate();
			double vat = rec.getVatAmount()*rec.getVatExchRate();
			double retention = rec.getRetentionAmount();
			double tax = rec.getTax23Amount() * rec.getTax23ExchRate();
			double bankcharges = rec.getBankChargesAmount()* rec.getBankChargesExchRate();
			totalrec = sales+vat - retention-tax - bankcharges;
		}
		return totalrec;
	}
	
	public String getCustomer(SalesReceived rec) {
		String strCustomer = "";				
		if (rec.getInvoice()!=null){
			strCustomer = getValueCustomer(rec.getInvoice());
		}
		else if (rec.getBeginningBalance()!=null){
			strCustomer = getValueCustomer(rec.getBeginningBalance());
		}
		return strCustomer;
	}
	
	public String getValueCustomer(Object obj){
		String strcustomer = "";	
		ProjectData project = null;
		if (obj instanceof SalesInvoice){
			project = ((SalesInvoice) obj).getProject();
			if (project.getCustomer()!=null)
				strcustomer = project.getCustomer().toString();
		}
		else if (obj instanceof BeginningAccountReceivable){
			project = ((BeginningAccountReceivable) obj).getProject();
			if (project.getCustomer()!=null)
				strcustomer = project.getCustomer().toString();
		}
		return strcustomer;
	}
}
