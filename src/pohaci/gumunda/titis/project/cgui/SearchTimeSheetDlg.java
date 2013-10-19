package pohaci.gumunda.titis.project.cgui;

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
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class SearchTimeSheetDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  TimeSheetDetailPanel m_panel;
  SearchTable m_table;
  JRadioButton m_andRadioBt, m_orRadioBt;
  JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
  JButton m_findBt, m_closeBt, m_clearBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  ProjectData m_project = null;

  public SearchTimeSheetDlg(JFrame owner, TimeSheetDetailPanel panel, ProjectData project,
                          Connection conn, long sessionid) {
    super(owner, "Search Time Sheet", false);
    setSize(350, 400);
    m_mainframe = owner;
    m_panel = panel;
    m_project = project;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
  }

  JPanel criteriaPanel() {
    m_table = new SearchTable();
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

    scrollPane.setPreferredSize(new Dimension(100, 113));
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
    try {
      query = m_table.getCriterion();      
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }
    System.err.println(query);
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_panel.reset(logic.getTimeSheetByCriteria(m_sessionid,
          IDBConstants.MODUL_PROJECT_MANAGEMENT, query));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void clear() {
    m_table.clear();
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_findBt) {
      find();
    }
    else if(e.getSource() == m_closeBt) {
      m_panel.m_show = false;
      dispose();
    }
    else if(e.getSource() == m_clearBt) {
      clear();
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

      model.addRow(new Object[]{"Entry Date", ""});
      model.addRow(new Object[]{"Prepared By", ""});
      model.addRow(new Object[]{"Prepared Date", ""});
      model.addRow(new Object[]{"Check By", ""});
      model.addRow(new Object[]{"Check Date", ""});
      model.addRow(new Object[]{"Work Description", ""});
      //model.addRow(new Object[]{"Start Date (>=)", ""});
      //model.addRow(new Object[]{"End Date (<=)", ""});

      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    }

    public TableCellEditor getCellEditor(int row, int col) {
    	if(col==1){
    		if(row==0 || row==2 || row==4)
    			return new DateCellEditor(GumundaMainFrame.getMainFrame());
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

    /*
    public String getCriterion() throws Exception {
      stopCellEditing();
      String operator, equality, criteria = "";
      int row = 0;
      boolean m_find = false;
      boolean m_preparedby = false;
      String andProject = " AND " + IDBConstants.ATTR_PROJECT + "=" + m_project.getIndex();
      String queryselect = "SELECT DISTINCT * ";
      String querytable = "FROM " + IDBConstants.TABLE_TIME_SHEET;
      String addquery = "";      

      if(m_andRadioBt.isSelected())
        operator = " AND ";
      else
        operator = " OR ";

      if(m_containsRadioBt.isSelected())
        equality = " LIKE ";
      else
        equality = " = ";

      //criteria = "WHERE " + IDBConstants.ATTR_PROJECT + "=" + m_project.getIndex();

      java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd-MM-yyyy"),
      sf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");

      java.util.Date date = null;
      String value = (String)getValueAt(row++, 1);
      try {        
        if(!value.equals("")) {          
          m_find = true;
          date = sf.parse(value);
          value = sf2.format(date);
        }
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }

      //entrydate
      if(!value.equals("")){        
        m_find = true;
        if(criteria.equals(""))
          criteria = " WHERE (" + IDBConstants.ATTR_ENTRY_DATE + "='" + value + "'";
        else
          criteria += operator + IDBConstants.ATTR_ENTRY_DATE + "='" + value + "'";
      }

      //preparedby
      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){    
        m_preparedby = true;
        m_find = true;
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(addquery.equals("")) {
          addquery = "," + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE;
          queryselect = "SELECT DISTINCT " + IDBConstants.TABLE_TIME_SHEET + ".* ";
          querytable += addquery;
        }

        if(criteria.equals("")) {
          criteria = " WHERE (UPPER(" + IDBConstants.ATTR_FIRST_NAME + "& ' ' &" + IDBConstants.ATTR_MIDLE_NAME + "& ' ' &" + IDBConstants.ATTR_LAST_NAME +  " ) " + equality + "'" + value.toUpperCase() + "'";
        }
        else {
          criteria += operator + "UPPER(" + IDBConstants.ATTR_FIRST_NAME + "& ' ' &" + IDBConstants.ATTR_MIDLE_NAME + "& ' ' &" + IDBConstants.ATTR_LAST_NAME +  ")" + equality + "'" + value.toUpperCase() + "'";
        }
      }
      
      //prepared date
      value = (String)getValueAt(row++, 1);
      try {        
        if(!value.equals("")) {               
          m_find = true;
          date = sf.parse(value);
          value = sf2.format(date);
        }
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }

      if(!value.equals("")){
        m_preparedby = true;
        m_find = true;
        if(criteria.equals(""))
          criteria = " WHERE (" + IDBConstants.ATTR_PREPARED_DATE + "='" + value + "'";
        else
          criteria += operator + IDBConstants.ATTR_PREPARED_DATE + "='" + value + "'";
      }

      //checkedby
      value = (String)getValueAt(row++, 1);      
      if(!value.equals("")){        
        m_find = true;        
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(addquery.equals("")) {
          addquery = "," + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE;
          queryselect = "SELECT DISTINCT timesheet.*,timesheet.*,employee.firstname,employee.midlename,employee.lastname  ";
          querytable += addquery;
        }

        if(criteria.equals("")) {
          m_find = true;
          criteria = " WHERE (UPPER(" + IDBConstants.ATTR_FIRST_NAME + "& ' ' &" + IDBConstants.ATTR_MIDLE_NAME + "& ' ' &" + IDBConstants.ATTR_LAST_NAME +  ")" + equality + "'" + value.toUpperCase() + "'";
        }
        else {
          criteria += operator + "UPPER(" + IDBConstants.ATTR_FIRST_NAME + "& ' ' &" + IDBConstants.ATTR_MIDLE_NAME + "& ' ' &" + IDBConstants.ATTR_LAST_NAME +  ")" + equality + "'" + value.toUpperCase() + "'"  + andProject;
        }
      }

      //checkeddate
      value = (String)getValueAt(row++, 1);
      try {        
        if(!value.equals("")) {
          m_find = true;
          date = sf.parse(value);
          value = sf2.format(date);
        }
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }

      if(!value.equals("")){
        m_find = true;
        if(criteria.equals(""))
          criteria = " WHERE (" + IDBConstants.ATTR_CHEKED_DATE + "='" + value + "'";
        else
          criteria += operator + IDBConstants.ATTR_CHEKED_DATE + "='" + value + "'";
      }

      //work description
      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        m_find = true;
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(criteria.equals(""))
          criteria = " WHERE (" + "UPPER(workdescription)" + equality + "'" + value.toUpperCase() + "'";
        else
          criteria += operator + "UPPER(workdescription)" + equality + "'" + value.toUpperCase() + "'";
      }

      /*String strstart = "", strend = "";
      strstart = (String)getValueAt(row++, 1);
      strend = (String)getValueAt(row++, 1);
      value = "";
      try {
        if(!strstart.equals("")) {
          date = sf.parse(strstart);
          strstart = sf2.format(date);
          value += strstart;
        }

        if(!strend.equals("")) {
          date = sf.parse(strend);
          strend = sf2.format(date);
          value += strend;
        }
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }


      if(!value.equals("")){
        if(addquery.equals("")) {
          addquery = "," + IDBConstants.TABLE_TIME_SHEET_DETAIL;
          queryselect = "SELECT " + IDBConstants.TABLE_TIME_SHEET + ".* ";
          querytable += addquery;
        }
        else {
          addquery = "," + IDBConstants.TABLE_TIME_SHEET_DETAIL;
          querytable += addquery;
        }

        if(!strstart.equals("")) {
          if(criteria.equals("")) {
            criteria = "WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_TIME_SHEET + operator +
                       IDBConstants.ATTR_START_DATE + ">=" + "'" + strstart + "'";
          }
          else {
            criteria += operator + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_TIME_SHEET + operator +
                        IDBConstants.ATTR_START_DATE + ">=" + "'" + strstart + "'";
          }
        }

        if(!strend.equals("")) {
          if(criteria.equals("")) {
            criteria = "WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_TIME_SHEET + operator +
                       IDBConstants.ATTR_FINISH_DATE + "<=" + "'" + strend + "'";
          }
          else {
            criteria += operator + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_TIME_SHEET + operator +
                        IDBConstants.ATTR_FINISH_DATE + "<=" + "'" + strend + "'";
          }
        }
      }*/

/*      
      if (m_find && !m_preparedby)
        return queryselect + querytable + criteria + ")" + andProject;
      else if (m_preparedby)
        return queryselect + querytable + criteria + ")" + " AND " + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PREPARED_BY  + andProject;  
      else
        return queryselect + querytable + " " + "WHERE " + IDBConstants.ATTR_PROJECT + "=" + m_project.getIndex();
    }*/
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
        //String equality;
        int row = 0;
        
        String queryselect = "SELECT * FROM (" +
        		"SELECT ts.*, " +
        		"(e1." + IDBConstants.ATTR_FIRST_NAME + " & ' ' & e1." + 
        		IDBConstants.ATTR_MIDLE_NAME + " & ' ' & e1." + IDBConstants.ATTR_LAST_NAME + ") preparedbyname, " +
        		"(e2." + IDBConstants.ATTR_FIRST_NAME + " & ' ' & e2." + 
        		IDBConstants.ATTR_MIDLE_NAME + " & ' ' & e2." + IDBConstants.ATTR_LAST_NAME + ") checkedbyname " +
        		"FROM " + IDBConstants.TABLE_TIME_SHEET + " ts " +
        		"LEFT JOIN " + IDBConstants.TABLE_EMPLOYEE + " e1 " +
        		"ON ts." + IDBConstants.ATTR_PREPARED_BY + "=e1." + IDBConstants.ATTR_AUTOINDEX + " " +
        		"LEFT JOIN " + IDBConstants.TABLE_EMPLOYEE + " e2 " +
        		"ON ts." + IDBConstants.ATTR_CHEKED_BY + "=e2." + IDBConstants.ATTR_AUTOINDEX + " " +
        		") ";
        
        String addProject = IDBConstants.ATTR_PROJECT + "=" + m_project.getIndex();
        
        if(m_andRadioBt.isSelected())
            operator = " AND ";
          else
            operator = " OR ";

/*        
          if(m_containsRadioBt.isSelected())
            equality = " LIKE ";
          else
            equality = " = ";
*/          
        
        Date date = null;
        String value = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy"),
        	sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String str = "";
        
        // entry date
        str = (String) getValueAt(row++, 1);
        if(!str.equals("")){
	        date = sdf1.parse(str);
	        value = sdf2.format(date);
	        if(!value.equals("")){
	        	criteria = "WHERE " + IDBConstants.ATTR_ENTRY_DATE + "='" + value + "'";
	        }
        }
        
        // prepared by
        value = (String) getValueAt(row++, 1);
        if(!value.equals("")){
        	if(criteria.equals(""))
        		criteria = "WHERE " + getCriteria("preparedbyname", value);
        	else 
        		criteria += operator + getCriteria("preparedbyname", value);
        }
        
        // prepared date
        str = (String) getValueAt(row++, 1);
        if(!str.equals("")){
	        date = sdf1.parse(str);
	        value = sdf2.format(date);
	        if(!value.equals("")){
	        	if(criteria.equals(""))
	        		criteria = "WHERE " + IDBConstants.ATTR_PREPARED_DATE + "='" + value + "'";
	        	else
	        		criteria += operator + IDBConstants.ATTR_PREPARED_DATE + "='" + value + "'";
	        }
        }
        
        // checked by
        value = (String) getValueAt(row++, 1);
        if(!value.equals("")){
        	if(criteria.equals(""))
        		criteria = "WHERE " + getCriteria("checkedbyname", value);
        	else 
        		criteria += operator + getCriteria("checkedbyname", value);
        }
        
        // checked date
        str = (String) getValueAt(row++, 1);
        if(!str.equals("")){
	        date = sdf1.parse(str);
	        value = sdf2.format(date);
	        if(!value.equals("")){
	        	if(criteria.equals(""))
	        		criteria = "WHERE " + IDBConstants.ATTR_CHEKED_DATE + "='" + value + "'";
	        	else
	        		criteria += operator + IDBConstants.ATTR_CHEKED_DATE + "='" + value + "'";
	        }
        }
        
        // work description
        value = (String) getValueAt(row++, 1);
        if(!value.equals("")){
        	if(criteria.equals(""))
        		criteria = "WHERE " + getCriteria(IDBConstants.ATTR_WORK_DESCRIPTION, value);
        	else 
        		criteria += operator + getCriteria(IDBConstants.ATTR_WORK_DESCRIPTION, value);
        }
        
        String query = "";
        if(criteria.equals("")){
        	query = queryselect + "WHERE " + addProject;
        } else {
        	query = queryselect + criteria + " AND " + addProject;
        }
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
  }
}