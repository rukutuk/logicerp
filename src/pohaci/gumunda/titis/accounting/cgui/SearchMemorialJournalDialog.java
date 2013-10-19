package pohaci.gumunda.titis.accounting.cgui;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.DateCellEditor;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.LookupPicker.DefaultTableModelAdapter;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.GeneralJournalStandar;
import pohaci.gumunda.titis.accounting.entity.JournalStandarLoader;
import pohaci.gumunda.titis.hrm.cgui.OrganizationCellEditor;
import pohaci.gumunda.titis.project.cgui.ProjectDataCellEditor;

public class SearchMemorialJournalDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	SearchTable m_searchTable;
	MemorialJournalListTable m_memorialjournalListTable;
	JButton m_selectBt;
	int m_iResponse = JOptionPane.NO_OPTION;
	JFrame m_mainframe;
	SearchTable m_table;
	JRadioButton m_andRadioBt, m_orRadioBt;
	JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
	JButton m_findBt, m_closeBt, m_clearBt;
	private JComboBox m_statusComboBox,m_transactionCode;
	
	Connection m_conn = null;
	long m_sessionid = -1;
	private JTextField filterField = new JTextField(10);
	JournalStandarLoader loader;
	Object[] m_receiverList;
	String m_projNon = "";
	String m_type;
	
	public SearchMemorialJournalDialog(JFrame owner, String title,String type, Connection conn, long sessionid,JournalStandarLoader loader){
		super(owner, ( title == null ) ? "Search Memorial Journal" : title, true);
		this.loader = loader;
		this.m_type = type;
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		setSize(800, 700);
		constructComponent();
		find();
	}
	
	public SearchMemorialJournalDialog(JFrame owner, String title,String type, Connection conn, long sessionid,JournalStandarLoader loader,String projNon){
		super(owner, ( title == null ) ? "Search Memorial Journal" : title, true);
		this.loader = loader;
		this.m_type = type;
		m_mainframe = owner;
		m_conn = conn;
		m_sessionid = sessionid;
		m_projNon = projNon;
		setSize(800, 700);
		constructComponent();
		find();
	}
	
	void constructComponent() {	  
		m_statusComboBox = new JComboBox(new Object[]{"Not Submitted","Submitted","Posted","All"});
		JournalStandardSettingPickerHelper journal = new JournalStandardSettingPickerHelper(m_conn,m_sessionid,"");
		List transCode = journal.getJournalStandardSetting(IConstants.MJ_STANDARD);
		JournalStandardSetting[] jss = 
			(JournalStandardSetting[]) transCode.toArray(new JournalStandardSetting[transCode.size()]);
		m_transactionCode = new JComboBox(jss);
		m_memorialjournalListTable = new MemorialJournalListTable();
		m_memorialjournalListTable.addMouseListener(new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					onSelect();	
				}
			}
		});
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));    
		m_selectBt = new JButton("Select");
		m_selectBt.addActionListener(this);
		buttonPanel.add(m_selectBt);
		
		centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Receipt List",
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
		
		centerPanel.add(new JScrollPane(m_memorialjournalListTable), BorderLayout.CENTER);
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
		
		scrollPane.setPreferredSize(new Dimension(100, 162));
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
	
	
	Object selectedObj = null;
	void onSelect(){
		m_iResponse = JOptionPane.OK_OPTION;
		int selectedRow = m_memorialjournalListTable.getSelectedRow();
		if (selectedRow > -1){
			selectedObj = m_memorialjournalListTable.getRow(selectedRow);
		}
		dispose();
	}
	
	void find() {
		String criterion;	  
		String operator=" and ";
		if(m_orRadioBt.isSelected()){
			operator =" or ";
		}
		criterion = loader.getCriterion(m_searchTable,operator);	
		if (m_projNon.equals(""))
			m_receiverList = loader.find(criterion + " ORDER BY " + 
					IDBConstants.ATTR_TRANSACTION_DATE + " ASC," + IDBConstants.ATTR_REFERENCE_NO + " ASC");
		else{
			if (m_projNon.equals("1"))
				m_receiverList = loader.find( " ( " + criterion + ") and " +IDBConstants.ATTR_PROJECT+" is not null ORDER BY " + 
						IDBConstants.ATTR_TRANSACTION_DATE + " ASC," + IDBConstants.ATTR_REFERENCE_NO + " ASC");
			else
				m_receiverList = loader.find( " ( " + criterion + ") and " +IDBConstants.ATTR_PROJECT+" is null ORDER BY " + 
						IDBConstants.ATTR_TRANSACTION_DATE + " ASC," + IDBConstants.ATTR_REFERENCE_NO + " ASC");
		}		
		m_memorialjournalListTable.setReceiptList(m_receiverList);
	}
	
	void clear() {
		m_searchTable.clear();
		m_andRadioBt.setSelected(true);
		find();
		//m_memorialjournalListTable.clear();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == m_selectBt) {
			onSelect();
		}else if(e.getSource() == m_findBt) {
			find();
		}else if (e.getSource()==m_clearBt){
			clear();
		}else if (e.getSource()==m_closeBt){
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
			
			model.addRow(new Object[]{"Transaction Type",m_type });			
			model.addRow(new Object[]{"Transaction Code", ""});
			model.addRow(new Object[]{"Transaction Date", dateformat.format(new Date())});
			model.addRow(new Object[]{"Memorial Journal No", ""});      
			model.addRow(new Object[]{"Project Code", ""});
			model.addRow(new Object[]{"Unit Code", ""});
			model.addRow(new Object[]{"Department", ""});
			model.addRow(new Object[]{"Status", "All"});
			model.addRow(new Object[]{"Submitted Date", ""});
			
			setModel(model);
			getColumnModel().getColumn(0).setPreferredWidth(125);
			getColumnModel().getColumn(0).setMaxWidth(150);
			getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
		}
		
		public TableCellEditor getCellEditor(int row, int col) {
			if( col == 1){    		
				if (row == 1)
					return new DefaultCellEditor(m_transactionCode);
				if (row == 2 || row ==8)
					return new DateCellEditor(GumundaMainFrame.getMainFrame());
				if(row == 4 )
					return new ProjectDataCellEditor(GumundaMainFrame.getMainFrame(),
							m_conn, m_sessionid);
				if(row == 5 )
					return new UnitCellEditor(GumundaMainFrame.getMainFrame(),
							"Unit Code", m_conn, m_sessionid);
				if(row == 6 )
					return new OrganizationCellEditor(GumundaMainFrame.getMainFrame(),
							m_conn, m_sessionid);
				if(row == 7 )
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
			for(int i = 0; i < row; i ++)
				if (i==0)
					setValueAt(m_type, i, 1);
				else if (i==2)
					setValueAt(dateformat.format(new Date()), i, 1);
				else if (i==7)
					setValueAt("All", i, 1);
				else
					setValueAt("", i, 1);
		}
		
	}
	
	class SearchTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {
			if (col==1){
				if (row==1)
					if (m_type.equals("Non Standard Journal Project") || m_type.equals("Non Standard Journal Non Project"))
						return false;
				if (row ==4 && m_type.equals("Non Standard Journal Non Project"))
					return false;
			}			
			if(col == 0)
				return false;
			return true;
		}
	}
	class MemorialJournalListTable extends JTable {
		private static final long serialVersionUID = 1L;
		DefaultTableModelAdapter model =new DefaultTableModelAdapter();
		public MemorialJournalListTable() {			
			model.addColumn("No");
			model.addColumn("Trans Type");
			model.addColumn("Trans Code");
			model.addColumn("Trans Date");
			model.addColumn("MJ No");      
			model.addColumn("Project Code");
			model.addColumn("Unit Code");
			model.addColumn("Department");
			model.addColumn("Status");
			model.addColumn("Submitted Date");
			setModel(model.buildTableModel(filterField));
			
			getColumnModel().getColumn(0).setPreferredWidth(50);
			getColumnModel().getColumn(0).setMaxWidth(50);
		}
		
		Object[] objRows;
		public Object getRow(int rowIdx)
		{
			return objRows[((Integer)getValueAt(rowIdx,0)).intValue()-1];
		}
		
		void setReceiptList(Object[] object) {
			model.clearRows();
			objRows = object;
			for(int i = 0; i < object.length; i ++) {
				GeneralJournalStandar JournalStandar = (GeneralJournalStandar) object[i];
				model.addRow(new Object[]{
						new Integer(i + 1),
						JournalStandar.vgetTransactionType(),
						JournalStandar.vgetTransactionCode(),
						JournalStandar.vgetTransactionDate(),
						JournalStandar.vgetJournalNo(),
						JournalStandar.vgetProjectCode(),
						JournalStandar.vgetUnitCode(),
						JournalStandar.vgetDepartment(),
						JournalStandar.vgetStatus(),    					
						JournalStandar.vgetSubmitDate()
				});
			}    
			
		}
		
		public TableCellEditor getCellEditor(int row, int col) {					
			return cellEditor;
		}
		
		void clear() {
			model.clearRows();
		}
	}
	class MemorialJournalListTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}
}