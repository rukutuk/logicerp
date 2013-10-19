
package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.Misc;

public class SearchLeavePermitionDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;
  JFrame m_mainframe;
  SearchTable m_searchtable;
  ResultTable m_resulttable;
  JRadioButton m_andRadioBt, m_orRadioBt;
  JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
  JButton m_findBt, m_closeBt, m_clearBt, m_selectBt;
  JComboBox m_typeComboBox = new JComboBox(EmployeeLeavePermition.m_types);
  

  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  SimpleDateFormat m_formatdate = new SimpleDateFormat("yyyy-MM-dd");
  
  Employee m_employee = null;
  EmployeeLeavePermition m_reason = null;
  int m_iResponse = JOptionPane.NO_OPTION;
  short m_type = -1;

  public SearchLeavePermitionDlg(JFrame owner, Connection conn, long sessionid,
                                 Employee employee, short type) {
    super(owner, "Search Leave & Permission", true);
    setSize(650, 650);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_employee = employee;
    m_type = type;
    constructComponent();

    setType();
    find();
  }

  JPanel criteriaPanel() {
    m_searchtable = new SearchTable();
    m_resulttable = new ResultTable();
    m_andRadioBt = new JRadioButton("and");
    m_andRadioBt.setSelected(true);
    m_orRadioBt = new JRadioButton("or");

    // m_typeComboBox.addActionListener(this); // i add this - ga jadi
    m_typeComboBox.setEnabled(false);
    
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
    m_selectBt = new JButton("Select");
    m_selectBt.addActionListener(this);

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
    JPanel buttonSelectPanel = new JPanel();
    
    JScrollPane scrollPane = new JScrollPane();
    GridBagConstraints gridBagConstraints;

    scrollPane.setPreferredSize(new Dimension(300, 161));
    scrollPane.getViewport().add(m_searchtable);

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
    gridBagConstraints.insets = new Insets(1, 0, 2, 0);
    criteriaPanel.add(new JLabel("Search Criterion"), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(3, 0, 0, 0);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    criteriaPanel.add(new JSeparator(JSeparator.HORIZONTAL), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(1, 0, 5, 0);
    criteriaPanel.add(scrollPane, gridBagConstraints);

    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new Insets(1, 0, 2, 0);
    criteriaPanel.add(new JLabel("Search Operator"), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(3, 0, 0, 0);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    criteriaPanel.add(new JSeparator(JSeparator.HORIZONTAL), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new Insets(1, 0, 5, 0);
    criteriaPanel.add(operatorPanel, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new Insets(1, 0, 2, 0);
    criteriaPanel.add(new JLabel("Option"), gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(3, -48, 0, 0);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    criteriaPanel.add(new JSeparator(JSeparator.HORIZONTAL), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.insets = new Insets(1, 0, 5, 0);
    criteriaPanel.add(optionPanel, gridBagConstraints);

    // i change this
//    JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//    northPanel.setPreferredSize(new Dimension(300, 320));
//    northPanel.add(criteriaPanel);
    
    // aslinya disini
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_findBt);
    buttonPanel.add(m_closeBt);
    buttonPanel.add(m_clearBt);
    
    buttonSelectPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonSelectPanel.add(m_selectBt);
      
    JPanel northPanel = new JPanel();
    northPanel.setLayout(new BorderLayout());
    northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    northPanel.add(criteriaPanel, BorderLayout.NORTH);
    northPanel.add(buttonPanel, BorderLayout.SOUTH);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    centerPanel.add(northPanel, BorderLayout.NORTH);
    centerPanel.add(new JScrollPane(m_resulttable), BorderLayout.CENTER);
    centerPanel.add(buttonSelectPanel, BorderLayout.SOUTH);

    return centerPanel;
  }

  void constructComponent() {
    getContentPane().add(criteriaPanel(), BorderLayout.CENTER);
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

  void setType() {
    if(EmployeeLeavePermition.getTypeAsString(m_type).equals(EmployeeLeavePermition.STR_TYPE1))
      m_searchtable.setValueAt("Leave", 0, 1);
    else
      m_searchtable.setValueAt("Permission", 0, 1);
  }

  void find() {
    String query = "";
    m_resulttable.clear();
    try {
      query = m_searchtable.getCriterion();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      m_resulttable.setEmployeeLeavePermition(logic.getAllEmployeeLeavePermition(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, query, m_type));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void select() {
    int row = m_resulttable.getSelectedRow();
    m_reason = (EmployeeLeavePermition)m_resulttable.getValueAt(row, 1);
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public EmployeeLeavePermition getEmployeeLeavePermition() {
    return m_reason;
  }

  public int getResponse() {
    return m_iResponse;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_findBt) {
      find();
    }
    else if(e.getSource() == m_closeBt) {
      dispose();
    }
    else if(e.getSource() == m_clearBt) {
      m_searchtable.clear();
    } 
    else if(e.getSource() == m_selectBt) {
      select();
    }
//    else if(e.getSource() == m_typeComboBox) {
//        m_type = new Integer(m_typeComboBox.getSelectedIndex()).shortValue();
//    }
  }

  /**
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
      model.addRow(new Object[]{"Propose Date", ""});
      model.addRow(new Object[]{"Start Date (<=)", ""});
      model.addRow(new Object[]{"End Date (>=)", ""});
      model.addRow(new Object[]{"Days #", ""});
      model.addRow(new Object[]{"Reason", ""});
      model.addRow(new Object[]{"Replaced By", ""});
      model.addRow(new Object[]{"Checked By", ""});
      model.addRow(new Object[]{"Approved By", ""});

      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    }

    public TableCellEditor getCellEditor(int row, int col) {
    	if(col==1){
    		if(row == 0){
    			//return new DefaultCellEditor(m_typeComboBox); // i frustate :<
    		}
    		else if(row == 1 || row == 2 || row == 3 ){
    			return new DateCellEditor(GumundaMainFrame.getMainFrame());
    		}
    		else if(row == 5) {
    			if(EmployeeLeavePermition.getTypeAsString(m_type).equals(EmployeeLeavePermition.STR_TYPE1))
    				return new LeaveTypeCellEditor(GumundaMainFrame.getMainFrame(),
    						"Leave Type", m_conn, m_sessionid);
    			else
    				return new PermitionTypeCellEditor(GumundaMainFrame.getMainFrame(),
    						"Permition Type", m_conn, m_sessionid);
    		}
    		else if(row == 6 || row == 7 || row == 8){
    			return new EmployeeCellEditor(GumundaMainFrame.getMainFrame(), m_conn, m_sessionid);
    		}
    	}

      return new BaseTableCellEditor();
    }

    
    
    public boolean isCellEditable(int row, int column) {
    	if(row == 0 && column == 1){
    		return false;
    	}else
		return super.isCellEditable(row, column);
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
        setValueAt("", i, 1);
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
      String operator, criteria = "";
//      String equality = " = ";
      int row = 0;
      String queryselect = "SELECT * ";
      String querytable = "";
      //if(EmployeeLeavePermition.getTypeAsString(m_type).equals(EmployeeLeavePermition.STR_TYPE1))
      if(EmployeeLeavePermition.getTypeAsString(m_type).equals(EmployeeLeavePermition.STR_TYPE1))
        querytable = "FROM " + IDBConstants.TABLE_EMPLOYEE_LEAVE;
      else
        querytable = "FROM " + IDBConstants.TABLE_EMPLOYEE_PERMITION;

      if(m_andRadioBt.isSelected())
        operator = " AND ";
      else
        operator = " OR ";

      String value = "";
      criteria = "WHERE " + IDBConstants.ATTR_EMPLOYEE + "=" + m_employee.getIndex();

      row++;

      // proposed date
      String strdate = (String)getValueAt(row++, 1);
      java.util.Date date = null;
      if(!strdate.equals("")) {
        date = m_dateformat.parse(strdate);
        value = m_formatdate.format(date);

        if(criteria.equals(""))
          criteria = "WHERE " + IDBConstants.ATTR_PROPOSE_DATE + "='" + value + "'";
        else
          criteria += operator + IDBConstants.ATTR_PROPOSE_DATE + "='" + value + "'";
      }
      
      // from date
      strdate = (String)getValueAt(row++, 1);
      if(!strdate.equals("")) {
        date = m_dateformat.parse(strdate);
        value = m_formatdate.format(date);

        if(criteria.equals(""))
          criteria = "WHERE " + IDBConstants.ATTR_FROM + "<='" + value + "'";
        else
          criteria += operator + IDBConstants.ATTR_FROM + "<='" + value + "'";
      }

      // to date
      strdate = (String)getValueAt(row++, 1);
      if(!strdate.equals("")) {
        date = m_dateformat.parse(strdate);
        value = m_formatdate.format(date);

        if(criteria.equals(""))
          criteria = "WHERE " + IDBConstants.ATTR_TO + ">='" + value + "'";
        else
          criteria += operator + IDBConstants.ATTR_TO + ">='" + value + "'";
      }

      //row++;
      // days
      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
    	  if(!criteria.equals(""))
    		  criteria = "WHERE " + IDBConstants.ATTR_DAYS + "=" + value;
          else
              criteria += operator + IDBConstants.ATTR_DAYS + "=" + value;
      }
    	  
      
      
      // reason
      Object obj = getValueAt(row++, 1);
      if(obj instanceof LeaveType) {
        if(criteria.equals(""))
          criteria = "WHERE " + IDBConstants.ATTR_REASON + " = " + ((LeaveType)obj).getIndex();
        else
          criteria += operator + IDBConstants.ATTR_REASON + " = " + ((LeaveType)obj).getIndex();
      }
      else if(obj instanceof PermitionType){
        if(criteria.equals(""))
          criteria = "WHERE " + IDBConstants.ATTR_REASON + " = " + ((PermitionType)obj).getIndex();
        else
          criteria += operator + IDBConstants.ATTR_REASON + " = " + ((PermitionType)obj).getIndex();
      }

      //replaced by
      obj = getValueAt(row++, 1);
      if(obj instanceof Employee) {
    	  if(criteria.equals(""))
    		  criteria = "WHERE " + IDBConstants.ATTR_REPLACED + " = " + ((Employee)obj).getIndex();
          else
            criteria += operator + IDBConstants.ATTR_REPLACED + " = " + ((Employee)obj).getIndex(); 
      }
      
//    checked by
      obj = getValueAt(row++, 1);
      if(obj instanceof Employee) {
    	  if(criteria.equals(""))
    		  criteria = "WHERE " + IDBConstants.ATTR_CHECKED + " = " + ((Employee)obj).getIndex();
          else
            criteria += operator + IDBConstants.ATTR_CHECKED + " = " + ((Employee)obj).getIndex(); 
      }
      
//    approved by
      obj = getValueAt(row++, 1);
      if(obj instanceof Employee) {
    	  if(criteria.equals(""))
    		  criteria = "WHERE " + IDBConstants.ATTR_APPROVED + " = " + ((Employee)obj).getIndex();
          else
            criteria += operator + IDBConstants.ATTR_APPROVED + " = " + ((Employee)obj).getIndex(); 
      }

      String queryall = queryselect + querytable + " " + criteria;
      System.out.println(queryall);
      return queryall;
    }
  }

  /**
   *
   */
  class SearchTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;
      return true;
    }
  }

  /**
   *
   */
  class ResultTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResultTable() {
      ResultTableModel model = new ResultTableModel();
      model.addColumn("No");
      model.addColumn("Type");
      model.addColumn("Propose Date");
      model.addColumn("From");
      model.addColumn("To");
      model.addColumn("Reason");
      model.addColumn("Replaced By");
      model.addColumn("Checked By");
      model.addColumn("Approved By");
      model.addColumn("Description");
      setModel(model);

      addMouseListener(new MouseAdapter() {
        public void mouseClicked( MouseEvent e ) {
          if(e.getClickCount() >= 2) {
            select();
          }
        }
      });
    }

    void clear() {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
    }

    void setEmployeeLeavePermition(EmployeeLeavePermition reason[]) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      for(int i = 0; i < reason.length; i ++) {
        model.addRow(new Object[]{String.valueOf(i + 1),
        reason[i],
        m_dateformat.format(reason[i].getPropose()), 
        m_dateformat.format(reason[i].getFrom()),
        m_dateformat.format(reason[i].getTo()), 
        reason[i].getReasonAsObject(m_sessionid, m_conn).toString(), reason[i].getReplacedEmployee(m_sessionid, m_conn),
        reason[i].getCheckedEmployee(m_sessionid, m_conn), 
        reason[i].getApprovedEmployee(m_sessionid, m_conn), reason[i].getDescription()
      });
      }
    }
  }

  /**
   *
   */
  class ResultTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}