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
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class SearchOfficeHourPermitionDlg extends JDialog implements ActionListener {
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

  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  SimpleDateFormat m_formatdate = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat m_timeformat = new SimpleDateFormat("HH:mm");
  
  Employee m_employee = null;
  EmployeeOfficeHourPermition m_reason = null;
  int m_iResponse = JOptionPane.NO_OPTION;

  public SearchOfficeHourPermitionDlg(JFrame owner, Connection conn, long sessionid, Employee employee) {
    super(owner, "Search Office Hour Permission", true);
    setSize(650, 600);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    m_employee = employee;
    constructComponent();
    find();
  }

  JPanel criteriaPanel() {
    m_searchtable = new SearchTable();
    m_resulttable = new ResultTable();
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

    scrollPane.setPreferredSize(new Dimension(300, 145));
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

    //bantai
//    JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//    northPanel.setPreferredSize(new Dimension(300, 300));
//    northPanel.add(criteriaPanel);

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
      m_resulttable.setEmployeeOfficeHourPermition(logic.getAllEmployeeOfficeHourPermition(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, query));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void select() {
    int row = m_resulttable.getSelectedRow();
    m_reason = (EmployeeOfficeHourPermition)m_resulttable.getValueAt(row, 5);
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public EmployeeOfficeHourPermition getEmployeeOfficeHourPermition() {
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

      model.addRow(new Object[]{"Propose Date", ""});
      model.addRow(new Object[]{"Permission Date", ""});
      model.addRow(new Object[]{"Start Time (<=)", ""});
      model.addRow(new Object[]{"End Time (>=)", ""});
      model.addRow(new Object[]{"Days #", ""});
      model.addRow(new Object[]{"Reason", ""});
      model.addRow(new Object[]{"Checked By", ""});
      model.addRow(new Object[]{"Approved By", ""});

      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    }

    public TableCellEditor getCellEditor(int row, int col) {
    	if(col==1){
    		if(row == 0 || row == 1){
    			return new DateCellEditor(GumundaMainFrame.getMainFrame());
    		} 
    		else if(row == 5){
    	        return new OfficeHourPermitionCellEditor(GumundaMainFrame.getMainFrame(),
    	            "Office Hour Permition Type", m_conn, m_sessionid);
    		}
    		else if(row == 6 || row == 7 ){
    			return new EmployeeCellEditor(GumundaMainFrame.getMainFrame(), m_conn, m_sessionid);
    		}
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
      String querytable = "FROM " + IDBConstants.TABLE_EMPLOYEE_OFFICE_PERMITION;
//      String addquery = "";

      if(m_andRadioBt.isSelected())
        operator = " AND ";
      else
        operator = " OR ";

      String value = "";
      criteria = "WHERE " + IDBConstants.ATTR_EMPLOYEE + "=" + m_employee.getIndex();

      //proposed date
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

      //permission
      strdate = (String)getValueAt(row++, 1);
      if(!strdate.equals("")) {
        date = m_dateformat.parse(strdate);
        value = m_formatdate.format(date);

        if(criteria.equals(""))
          criteria = "WHERE " + IDBConstants.ATTR_PERMISSION_DATE + "='" + value + "'";
        else
          criteria += operator + IDBConstants.ATTR_PERMISSION_DATE + "='" + value + "'";
      }

      row++;

      Object obj = getValueAt(row++, 1);
      if(obj instanceof OfficeHourPermition) {
        if(criteria.equals(""))
          criteria = "WHERE " + IDBConstants.ATTR_REASON + " = " + ((OfficeHourPermition)obj).getIndex();
        else
          criteria += operator + IDBConstants.ATTR_REASON + " = " + ((OfficeHourPermition)obj).getIndex();
      }

      row++;
      row++;

      String query = queryselect + querytable + " " + criteria;
      System.out.println(query);
      return query;
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

    public void setValueAt(Object obj, int row, int col) {
      if(obj instanceof OfficeHourPermition)
        super.setValueAt(new OfficeHourPermition((OfficeHourPermition)obj, OfficeHourPermition.DESCRIPTION), row, col);
      else
        super.setValueAt(obj, row, col);
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
      model.addColumn("Propose Date");
      model.addColumn("Permission Date");
      model.addColumn("From");
      model.addColumn("To");
      model.addColumn("Reason");
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

    void setEmployeeOfficeHourPermition(EmployeeOfficeHourPermition reason[]) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      for(int i = 0; i < reason.length; i ++) {
        model.addRow(new Object[]{
        		String.valueOf(i + 1),
		        m_dateformat.format(reason[i].getPropose()), 
		        m_dateformat.format(reason[i].getPermissionDate()),
		        m_timeformat.format(reason[i].getFrom()),
		        m_timeformat.format(reason[i].getTo()), 
		        reason[i],
		        reason[i].getChecked(), 
		        reason[i].getApproved(), 
		        reason[i].getDesciption()
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