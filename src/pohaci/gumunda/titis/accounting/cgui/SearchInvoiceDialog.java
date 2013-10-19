package pohaci.gumunda.titis.accounting.cgui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.cgui.LookupPicker.DefaultTableModelAdapter;
import pohaci.gumunda.titis.accounting.entity.GeneralInvoice;
import pohaci.gumunda.titis.accounting.entity.InvoiceLoader;
import pohaci.gumunda.titis.project.cgui.ProjectDataCellEditor;

public class SearchInvoiceDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	SearchTable m_searchTable;
	InvoiceListTable m_invoiceListTable;
	JButton m_selectBt;
	int m_iResponse = JOptionPane.NO_OPTION;
	JFrame m_mainframe;
	SearchTable m_table;
	JRadioButton m_andRadioBt, m_orRadioBt;
	JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
	JButton m_findBt, m_closeBt, m_clearBt;

	Connection m_conn = null;
	long m_sessionid = -1;
	private JTextField filterField = new JTextField(10);
	InvoiceLoader loader;
	private JComboBox m_statusComboBox;


	public SearchInvoiceDialog(JFrame owner, String title, Connection conn, long sessionid,InvoiceLoader loader){
		super(owner, ( title == null ) ? "Search Invoice" : title, true);
		this.loader = loader;
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		setSize(800, 600);
		constructComponent();
		find();
	}

	void constructComponent() {
		m_statusComboBox = new JComboBox(new Object[]{"Not Submitted","Submitted","Posted","All"});//0,1,3,*
		m_invoiceListTable = new InvoiceListTable();
		m_invoiceListTable.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					onAdd();
				}
			}
		});

		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		m_selectBt = new JButton("Select");
		buttonPanel.add(m_selectBt);
		m_selectBt.addActionListener(this);

		centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Invoice List",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
		Box vertBox  = Box.createVerticalBox();
		vertBox.add(filterField);
		vertBox.add(new JScrollPane(m_invoiceListTable));
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

	JPanel criteriaPanel() {
		m_searchTable = new SearchTable();
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

		scrollPane.setPreferredSize(new Dimension(100, 97));
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
		criteriaPanel.add(new JLabel("Search Criteria"), gridBagConstraints);

		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new Insets(1, 0, 5, 0);
		criteriaPanel.add(scrollPane, gridBagConstraints);

		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		criteriaPanel.add(new JLabel("Search Operator"), gridBagConstraints);

		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new Insets(1, 0, 5, 0);
		criteriaPanel.add(operatorPanel, gridBagConstraints);

		gridBagConstraints.gridy = 4;
		gridBagConstraints.insets = new Insets(1, 0, 2, 0);
		criteriaPanel.add(new JLabel("Option"), gridBagConstraints);

		gridBagConstraints.gridy = 5;
		criteriaPanel.add(optionPanel, gridBagConstraints);

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(m_findBt);
		buttonPanel.add(m_clearBt);
		buttonPanel.add(m_closeBt);

		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
		centerPanel.add(criteriaPanel, BorderLayout.NORTH);
		centerPanel.add(buttonPanel, BorderLayout.SOUTH);

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
		System.out.println(operator);
		criterion = loader.getCriterion(m_searchTable,operator);
		System.err.println(criterion);
		Object[] receiptList = loader.find(criterion);
		m_invoiceListTable.setReceiptList(receiptList);
	}

	void clear() {
		m_searchTable.clear();
		m_andRadioBt.setSelected(true);
		find();
	}

	Object selectedObj;
	void onAdd(){
		m_iResponse = JOptionPane.OK_OPTION;
		int selectedRow = m_invoiceListTable.getSelectedRow();
		System.err.println("sel :" + selectedRow);
		if (selectedRow > -1){
			selectedObj = m_invoiceListTable.getRow(selectedRow);
		}
		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_selectBt) {
			onAdd();
		}
		else if(e.getSource() == m_findBt) {
			find();
		}else if (e.getSource()==m_clearBt){
			clear();
		}else if (e.getSource()==m_closeBt){
			dispose();
		}

	}

	public class SearchTable extends JTable {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		public SearchTable() {

			SearchTableModel model = new SearchTableModel();
			model.addColumn("Attribute");
			model.addColumn("Description");

			model.addRow(new Object[]{"Invoice Date", dateformat.format(new Date())});
			model.addRow(new Object[]{"Invoice No", ""});
			model.addRow(new Object[]{"Project", ""});
			model.addRow(new Object[]{"Status", "All"});
			model.addRow(new Object[]{"Submitted Date", ""});

			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(100);
			getColumnModel().getColumn(0).setMaxWidth(100);
			getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
		}

		public TableCellEditor getCellEditor(int row, int col) {
			if((row == 0 || row==4) && col == 1)
				return new DateCellEditor(GumundaMainFrame.getMainFrame());
			else if(row == 2 && col == 1)
				return new ProjectDataCellEditor(GumundaMainFrame.getMainFrame(),
						m_conn, m_sessionid);
			else if(row == 3 && col == 1)
				return new DefaultCellEditor(m_statusComboBox);
			return new BaseTableCellEditor();
		}

		public void stopCellEditing() {
			TableCellEditor editor;
			if((editor = getCellEditor()) != null)
				editor.stopCellEditing();
		}

		public void clear() {
			stopCellEditing();
			int row = getRowCount();
			for(int i = 0; i < row; i ++)
				if (i==0)
					setValueAt(dateformat.format(new Date()), i, 1);
				else if (i==3)
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

	class InvoiceListTable extends JTable {
		private static final long serialVersionUID = 1L;
		DefaultTableModelAdapter model =new DefaultTableModelAdapter();
		public InvoiceListTable() {
			model.addColumn("No");
			model.addColumn("Invoice Date");
			model.addColumn("Invoice No");
			model.addColumn("Costumer");
			model.addColumn("Unit Code");
			model.addColumn("Activity Code");
			model.addColumn("Department");
			model.addColumn("SO/PO/Contract No");
			model.addColumn("Work Description");
			model.addColumn("Total Amount");
			model.addColumn("Status");
			model.addColumn("Submitted Date");
			setModel(model.buildTableModel(filterField));

			getColumnModel().getColumn(0).setPreferredWidth(50);
			getColumnModel().getColumn(0).setMaxWidth(50);
			getColumnModel().getColumn(9).setCellRenderer(new DoubleCellRenderer(JLabel.RIGHT));
		}

		Object[] m_objRows;
		public Object getRow(int rowIdx){
			String value = ((String)getValueAt(rowIdx,0));
			int nil = Integer.parseInt(value)-1;
			return m_objRows[nil];
		}

		void setReceiptList(Object[] object) {
			model.clearRows();
			m_objRows = object;
			for(int i = 0; i < m_objRows.length; i ++) {
				GeneralInvoice invoice = (GeneralInvoice)object[i];
				model.addRow(new Object[]{
						String.valueOf((i + 1)),
						invoice.vgetInvoiceDate(),
						invoice.vgetInvoiceNo(),
						invoice.vgetCostumer(),
						invoice.vgetUnitCode(),
						invoice.vgetActivityCode(),
						invoice.vgetDepartment(),
						invoice.vgetContractNo(),
						invoice.vgetWorkDesc(),
						invoice.vgetTotAmount(),
						invoice.vgetStatus(),
						invoice.vgetSubmittedDate()
				});
			}
		}

		void clear() {
			DefaultTableModel model = (DefaultTableModel)getModel();
			model.setRowCount(0);
		}
	}

	/**
	 *
	 */
	class VoucherListTableModel extends DefaultTableModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}
}