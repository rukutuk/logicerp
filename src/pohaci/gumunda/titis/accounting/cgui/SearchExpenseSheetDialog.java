package pohaci.gumunda.titis.accounting.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;

import pohaci.gumunda.titis.accounting.cgui.LookupPicker.DefaultTableModelAdapter;
import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.entity.GeneralExpeseSheet;
import pohaci.gumunda.titis.accounting.entity.SheetLoader;
import pohaci.gumunda.titis.hrm.cgui.EmployeeCellEditor;
import pohaci.gumunda.titis.project.cgui.ProjectDataCellEditor;

public class SearchExpenseSheetDialog extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SearchTable m_searchTable;
	ExpenseSheetListTable m_expensesheetListTable;
	JButton m_selectBt;
	int m_iResponse = JOptionPane.NO_OPTION;
	JFrame m_mainframe;
	SearchTable m_table;
	JRadioButton m_andRadioBt, m_orRadioBt;
	JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
	JButton m_findBt, m_closeBt, m_clearBt;

	Connection m_conn = null;
	Object[] m_receiverList;
	long m_sessionid = -1;
	private JTextField filterField = new JTextField(10);
	SheetLoader loader;	
	private JComboBox m_statusComboBox;
	private String m_type;

	public SearchExpenseSheetDialog(JFrame owner, String title, Connection conn, long sessionid ,SheetLoader loader,String type,String a){
		super(owner, ( title == null ) ? "Search Expense Sheet" : title, true);
		this.loader = loader;
		m_mainframe = owner;
		this.m_type=type;
		m_conn = conn;
		m_sessionid = sessionid;
		setSize(800, 600);
		kriteria=a;
		constructComponent();
		find();
	}

	void constructComponent() {
		m_statusComboBox = new JComboBox(new Object[]{"Not Submitted","Submitted","Posted","All"});
		m_expensesheetListTable = new ExpenseSheetListTable();
		m_expensesheetListTable.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					onSelect();	
				}
			}
		});JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		m_selectBt = new JButton("Select");
		m_selectBt.addActionListener(this);
		buttonPanel.add(m_selectBt);

		centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Expense Sheet List",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
	
		Box vertBox  = Box.createVerticalBox();
		vertBox.add(filterField);
		vertBox.add(new JScrollPane(m_expensesheetListTable));
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

		scrollPane.setPreferredSize(new Dimension(100, 146));
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
	String kriteria;
	void find() {
		String criterion;	  
		String operator=" and ";
		if(m_orRadioBt.isSelected()){
			operator =" or ";
		}
		criterion = loader.getCriterion(m_searchTable,operator);		
		criterion=criterion+" and "+kriteria;
		
		Object[] obj = loader.find(criterion);
		List list = Arrays.asList(obj);
		List newList = new ArrayList();
		
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			ExpenseSheet es = (ExpenseSheet)iterator.next();
			
			if(this.m_type==IConstants.EXPENSE_SHEET_PROJECT){
				if(es.getPmtCaProject()!=null){
					newList.add(es);
				}else if(es.getPmtCaIouProjectSettled()!=null){
					newList.add(es);
				}else{
					if (es.getBeginningBalance()!=null)
						if(es.getBeginningBalance().getProject()!=null){
							newList.add(es);
						}
				}
			}else{
				if(es.getPmtCaOthers()!=null){
					newList.add(es);
				}else if(es.getPmtCaIouOthersSettled()!=null){
					newList.add(es);
				}else{
					if (es.getBeginningBalance()!=null)
						if(es.getBeginningBalance().getProject()==null){
							newList.add(es);
						}
				}
			}
		}
		
		m_receiverList = (Object[]) newList.toArray(new Object[newList.size()]);
		m_expensesheetListTable.setReceiptList(m_receiverList);
	}
	
	void clear() {
		m_searchTable.clear();
		m_andRadioBt.setSelected(true);
		find();
		//m_expensesheetListTable.clear();
	}

	Object selectedObj = null;
	void onSelect(){
		m_iResponse = JOptionPane.OK_OPTION;
		int selectedRow = m_expensesheetListTable.getSelectedRow();
		if (selectedRow > -1)
			selectedObj = m_expensesheetListTable.getRow(selectedRow);		
		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_selectBt) {
			onSelect();
		}
		else if(e.getSource() == m_findBt) {
			find();
		}
		else if(e.getSource() == m_clearBt) {
			clear();
		}
		else if(e.getSource() == m_closeBt) {
			dispose();
		}
	}

	public class SearchTable extends JTable {	
	
		private static final long serialVersionUID = 1L;
		SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
		public SearchTable() {
			SearchTableModel model = new SearchTableModel();
			model.addColumn("Attribute");
			model.addColumn("Description");
			model.addRow(new Object[]{"Expense Sheet Type",m_type});
			model.addRow(new Object[]{"Expense Sheet  No", ""});
			model.addRow(new Object[]{"Expense Sheet Date", dateformat.format(new Date())});
			model.addRow(new Object[]{"Project Code", ""});						
			model.addRow(new Object[]{"Originator", ""});
			model.addRow(new Object[]{"Approved By", ""});
			model.addRow(new Object[]{"Status", "All"});
			model.addRow(new Object[]{"Submitted Date", ""});

			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(125);
			getColumnModel().getColumn(0).setMaxWidth(150);
			getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
		}

		public TableCellEditor getCellEditor(int row, int col) {
			if((row == 2 || row == 7) && col == 1){
				return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
			}else if(row == 3 && col == 1){
				return new ProjectDataCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
			}else if((row == 4 || row == 5) && col == 1){
				return new EmployeeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),m_conn,m_sessionid);
			}else if (row == 6 && col == 1){
				return new DefaultCellEditor(m_statusComboBox);
			}

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
			for(int i = 1; i < row; i ++)
				if (i==2)
					setValueAt(dateformat.format(new Date()), i, 1);
				else if (i==6)
					setValueAt("All", i, 1);
				else
					setValueAt("", i, 1);
		}
	}

	class SearchTableModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2442832863677748891L;

		public boolean isCellEditable(int row, int col) {
			if(col == 0)
				return false;
			return true;
		}
	}

	class ExpenseSheetListTable extends JTable {
		private static final long serialVersionUID = 1L;
		DefaultTableModelAdapter model =new DefaultTableModelAdapter();
		public ExpenseSheetListTable() {

			model.addColumn("No");
			model.addColumn("Expense Sheet Type");
			model.addColumn("Expense Sheet  No");
			model.addColumn("Expense Sheet Date");
			model.addColumn("Project Code");			
			model.addColumn("Advance Amount");
			model.addColumn("Total Exp");
			model.addColumn("Originator");
			model.addColumn("Approved By");
			model.addColumn("Status");
			model.addColumn("Submitted Date");
			setModel(model.buildTableModel(filterField));

			getColumnModel().getColumn(0).setPreferredWidth(50);
			getColumnModel().getColumn(0).setMaxWidth(50);
		}

		Object[] objRows;
		public Object getRow(int rowIdx){
			return objRows[((Integer)getValueAt(rowIdx,0)).intValue()-1];
		}

		void setReceiptList(Object[] object) {
			model.clearRows();
			objRows = object;
			for(int i = 0; i < object.length; i ++) {
				GeneralExpeseSheet Sheet = (GeneralExpeseSheet)object[i];
				model.addRow(new Object[]{
						new Integer(i + 1),
						Sheet.vgetESType(),
						Sheet.vgetESNo(),
						Sheet.vgetESDate(),
						Sheet.vgetProjectCode(),
						Sheet.vgetAdvanceAmount(),
						Sheet.vgetTotalExp(),
						Sheet.vgetOriginator(),
						Sheet.vgetApprovedBy(),
						Sheet.vgetStatus(),
						Sheet.vgetSubmittedDate()
				});
			}
		}		

		void clear() {
			DefaultTableModel model = (DefaultTableModel)getModel();
			model.setRowCount(0);
		}
	}
}