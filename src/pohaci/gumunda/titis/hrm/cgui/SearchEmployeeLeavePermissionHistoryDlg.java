package pohaci.gumunda.titis.hrm.cgui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.DateCellEditor;
import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class SearchEmployeeLeavePermissionHistoryDlg extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame m_mainframe;
	EmployeeLeavePermissionHistoryPanel m_panel;
	
	SearchTable m_table;
	JComboBox m_typeComboBox = null;
	
	JRadioButton m_andRadioBt, m_orRadioBt;
	JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
	JButton m_findBt, m_closeBt, m_clearBt;
	
	EmployeeLeavePermissionHistory[] m_history = null;
	
	Connection m_conn;
	long m_sessionid = -1;
	
	public SearchEmployeeLeavePermissionHistoryDlg(JFrame owner, 
			EmployeeLeavePermissionHistoryPanel panel, Connection conn, long sessionid) {
		super(owner, "Search Leave & Permission", false);
		setSize(350, 320);
		m_mainframe = owner;
		m_panel = panel;
		m_conn = conn;
		m_sessionid = sessionid;
	
		constructComponent();
	}
	
	private void constructComponent() {
		m_table = new SearchTable();
		m_typeComboBox = new JComboBox(new Object[]{"All", "Leave", "Permission"});
		m_typeComboBox.setSelectedIndex(0);
		m_andRadioBt = new JRadioButton("and");
	    m_andRadioBt.setSelected(true);
	    m_orRadioBt = new JRadioButton("or");

	    m_containsRadioBt = new JRadioButton("Text Contains Criteria");
	    m_containsRadioBt.setSelected(true);
	    m_matchRadioBt = new JRadioButton("Match Case");
	    m_wholeRadioBt = new JRadioButton("Find Whole Words only");
	    m_findBt = new JButton("Find");
	    m_findBt.addActionListener(this);
	    m_closeBt = new JButton("Close");
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

	    scrollPane.setPreferredSize(new Dimension(100, 81));
	    scrollPane.getViewport().add(m_table);

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
	    criteriaPanel.add(new JLabel("Search Criterion"), gridBagConstraints);

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
	    buttonPanel.add(m_closeBt);
	    buttonPanel.add(m_clearBt);

	    centerPanel.setLayout(new BorderLayout());
	    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
	    centerPanel.add(criteriaPanel, BorderLayout.NORTH);
	    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

	    getContentPane().add(centerPanel, BorderLayout.CENTER);
	    
	    addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	dispose();
	        }
	    });
	}
	public void setVisible( boolean flag ){
	    Rectangle rc = m_mainframe.getBounds();
	    Rectangle rcthis = getBounds();
	    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
	              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
	              (int)rcthis.getWidth(), (int)rcthis.getHeight());
	              
	    super.setVisible(flag);
	 }
	

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==m_findBt){
			find();
		}else if(e.getSource()==m_closeBt){
			dispose();
		}else if(e.getSource()==m_clearBt){
			clear();
		}
	}

	private void clear() {
		m_table.clear();
	}

	private void find() {
		String query = "";
		try {
			query = m_table.getCriterion();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		    return;
		}
		
		try {
			HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
			m_history = logic.getEmployeeLeavePermissionHistoryByQuery(m_sessionid, IDBConstants.MODUL_MASTER_DATA, query);
			m_panel.viewData(m_history);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
		}
		
	}

	/**
	 * 
	 * @author dark-knight
	 *
	 */
	class SearchTable extends JTable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SearchTable() {
			SearchTableModel model = new SearchTableModel();
			model.addColumn("Attribute");
		    model.addColumn("Description");

		    model.addRow(new Object[]{"Type", ""});
		    model.addRow(new Object[]{"Reason", ""});
		    model.addRow(new Object[]{"Proposed Date >=", ""});
		    model.addRow(new Object[]{"Proposed Date <=", ""});
		    
		    model.setValueAt("All", 0, 1);
		    
		    setModel(model);
		    getColumnModel().getColumn(0).setPreferredWidth(100);
		    getColumnModel().getColumn(0).setMaxWidth(100);
		    getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
		}
		
		public void clear() {
			stopCellEditing();
			int row = getRowCount();
		    for(int i = 0; i < row; i ++){
		    	if(i==0)
		    		setValueAt("All", i, 1);
		    	else
		    		setValueAt("", i, 1);		
		    }
		}

		public TableCellEditor getCellEditor(int row, int col) {
			if(col==1){
				if(row==0){
					return new DefaultCellEditor(m_typeComboBox);
				} else if(row==1){
					return new LeavePermissionTypeCellEditor(GumundaMainFrame.getMainFrame(),
							"Leave & Permission Type", m_conn, m_sessionid, 
							(short)(m_typeComboBox.getSelectedIndex()-1));
				} else if(row==2 || row==3) {
					return new DateCellEditor(GumundaMainFrame.getMainFrame());
				}
			}
			return new BaseTableCellEditor();
		}
		
		public void stopCellEditing() {
		    TableCellEditor editor;
		     if((editor = getCellEditor()) != null)
		        editor.stopCellEditing();
		}
		
		public String getCriteria(String attribute, String value) {
			String criteria = "";
	        if(m_containsRadioBt.isSelected())
	        	criteria = Misc.getCriteria(attribute, value, Misc.CONTAINTS_CRITERIA);
	        else if(m_matchRadioBt.isSelected())
	        	criteria = Misc.getCriteria(attribute, value, Misc.MATCH_CASE);
	        else
	        	criteria = Misc.getCriteria(attribute, value, Misc.FIND_WHOLE_WORDS_ONLY);
	        return criteria;
		}
		
		public String getCriterion() throws Exception {
			stopCellEditing();
			
			Date date = new GregorianCalendar().getTime();
			DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
			
			String operator = "";
			
			int row = 0;
			
			String query = 
				"select autoindex, type, reason, " 
				+ "proposedate, attrfrom, attrto, days, "
				+ "replaced, checked, checkeddate, "
				+ "approved, approveddate, deduction from ("
				+ "select l.autoindex, l.employee, 'Leave' type, "
				+ "lt.description reason, "
				+ "l.proposedate, l.attrfrom, l.attrto, l.days, "
				+ "ep.firstname & ' ' & ep.midlename & ' ' & ep.lastname replaced, "
				+ "ec.firstname & ' ' & ec.midlename & ' ' & ec.lastname checked, "
				+ "l.checkeddate, "
				+ "ea.firstname & ' ' & ea.midlename & ' ' & ea.lastname approved, "
				+ "l.approveddate, lt.deduction deduction from employeeleave l "
				+ "left join leavetype lt on l.reason=lt.autoindex "
				+ "left join employee ep on l.replaced=ep.autoindex "
				+ "left join employee ec on l.checked=ec.autoindex "
				+ "left join employee ea on l.approved=ea.autoindex "
				+ "union all "
				+ "select p.autoindex, p.employee, 'Permission' type, "
				+ "pt.description reason, "
				+ "p.proposedate, p.attrfrom, p.attrto, p.days, "
				+ "ep.firstname & ' ' & ep.midlename & ' ' & ep.lastname replaced, "
				+ "ec.firstname & ' ' & ec.midlename & ' ' & ec.lastname checked, "
				+ "p.checkeddate, "
				+ "ea.firstname & ' ' & ea.midlename & ' ' & ea.lastname approved, "
				+ "p.approveddate, pt.deduction deduction from employepermition p "
				+ "left join permitiontype pt on p.reason=pt.autoindex "
				+ "left join employee ep on p.replaced=ep.autoindex "
				+ "left join employee ec on p.checked=ec.autoindex "
				+ "left join employee ea on p.approved=ea.autoindex "
				+ ") ";
			
			String whereClausa = "where employee=" + m_panel.m_employee.getIndex();
			
			if (m_andRadioBt.isSelected())
				operator = " AND ";
			else
				operator = " OR ";

			// type
			String value = (String) getValueAt(row++, 1);
			if(!value.equals("All")){
				whereClausa += operator + "type='" + value + "'";
			}
			
			// reason
			//value = (String) getValueAt(row++, 1);
			value = "";
			Object obj = getValueAt(row++, 1);
			if(obj instanceof LeavePermissionType){
				value = ((LeavePermissionType)obj).getDescription(); 
				short type = ((LeavePermissionType)obj).getTypeAsShort();
				if ((m_typeComboBox.getSelectedIndex() - 1) > -1) {
					if (type != (m_typeComboBox.getSelectedIndex() - 1))
						throw new Exception("Types choosen is not matched");
				}
			}
			
			if(!value.equals("")){
				whereClausa += operator + getCriteria("reason", value);
			}
			
			// permission date >=
			value = (String) getValueAt(row++, 1);
			if(!value.equals("")){
				try {
					date = dateFormat2.parse(value);
					value = dateFormat1.format(date);
				} catch (ParseException e) {
					throw new Exception(e.getMessage());
				}
			}
			if(!value.equals("")){
				whereClausa += operator + "proposedate>='" + value + "'";
			}
			
			//	permission date <=
			value = (String) getValueAt(row++, 1);
			if(!value.equals("")){
				try {
					date = dateFormat2.parse(value);
					value = dateFormat1.format(date);
				} catch (ParseException e) {
					throw new Exception(e.getMessage());
				}
			}
			if(!value.equals("")){
				whereClausa += operator + "proposedate<='" + value + "'";	
			}
			
			System.out.println(query + whereClausa);
			return (query + whereClausa);
		}
		
	}
	
	class SearchTableModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int col) {
			if(col==0)
				return false;
			return true;
		}
	}

}
