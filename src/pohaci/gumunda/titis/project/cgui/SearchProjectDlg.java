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
import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.accounting.cgui.ActivityCellEditor;
import pohaci.gumunda.titis.accounting.cgui.UnitCellEditor;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.OrganizationCellEditor;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class SearchProjectDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  ProjectListPanel m_panel;
  SearchTable m_table;
  JRadioButton m_andRadioBt, m_orRadioBt;
  JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
  JButton m_findBt, m_closeBt, m_clearBt;
  ProjectData[] m_project = null;  
  JComboBox m_statusComboBox;

  Connection m_conn = null;
  long m_sessionid = -1; 

  public SearchProjectDlg(JFrame owner, ProjectListPanel panel,
                          Connection conn, long sessionid) {
    super(owner, "Search Project", false);
    setSize(350, 480);
    m_mainframe = owner;
    m_panel = panel;
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
  }

  public SearchProjectDlg(JFrame owner, Connection conn, long sessionid) {
	  super(owner, "Search Project", false);
	  setSize(350, 480);
	  m_mainframe = owner;	  
	  m_conn = conn;
	  m_sessionid = sessionid;
	  
	  constructComponent();
  }

JPanel criteriaPanel() {
    m_table = new SearchTable();
    m_statusComboBox = new JComboBox(new Object[]{"Active","InActive","All"});
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

    scrollPane.setPreferredSize(new Dimension(100, 209));
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

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_project = logic.getProjectDataByCriteria(m_sessionid,
          IDBConstants.MODUL_PROJECT_MANAGEMENT, query);
      m_panel.reset(m_project);
      
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
      if(m_panel != null)
        m_panel.m_show = false;
      dispose();
    }
    else if(e.getSource() == m_clearBt) {
      //clear();
    	m_table.clear();
    }
  }

  /**
   *
   */
  public class SearchTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SearchTable() {
      SearchTableModel model = new SearchTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");

      model.addRow(new Object[]{"Project Code", ""});
      model.addRow(new Object[]{"Customer", ""});
      model.addRow(new Object[]{"Work Description", ""});
      model.addRow(new Object[]{"Unit Code", ""});
      model.addRow(new Object[]{"O.R No.", ""});
      model.addRow(new Object[]{"PO No.", ""});
      model.addRow(new Object[]{"IPC No.", ""});
      model.addRow(new Object[]{"Start Date (>=)", ""});
      model.addRow(new Object[]{"End Date (<=)", ""});
      model.addRow(new Object[]{"Activity Code", ""});
      model.addRow(new Object[]{"Department", ""});
      model.addRow(new Object[]{"Status", ""});

      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    }

    public TableCellEditor getCellEditor(int row, int col) {
    	if(col==1){
    		if(row==3){
    			return new UnitCellEditor(GumundaMainFrame.getMainFrame(),
        				"Unit Code", m_conn, m_sessionid);
    		}
    		if(row==7 || row==8){
    			return new DateCellEditor(GumundaMainFrame.getMainFrame());
    		}
    		if(row==9){
    			return new ActivityCellEditor(GumundaMainFrame.getMainFrame(),
        				m_conn, m_sessionid);
    		}
    		if(row==10){
    			return new OrganizationCellEditor(GumundaMainFrame.getMainFrame(),
        				m_conn, m_sessionid);
    		}
    		if(row==11){
    			return new DefaultCellEditor(m_statusComboBox);
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
      java.util.Date date = new java.util.GregorianCalendar().getTime();         
      java.text.DateFormat formatDate = new java.text.SimpleDateFormat( "yyyy-MM-dd" );
      String tgl = formatDate.format(date);
      String operator, criteria = "";
      int row = 0;
      String queryselect = "SELECT * FROM (" + 
    	  	"SELECT pd." + IDBConstants.ATTR_AUTOINDEX +
      		", pd." + IDBConstants.ATTR_CODE +
      		", cust." + IDBConstants.ATTR_NAME + " AS CUSTOMERNAME" +
      		", pd." + IDBConstants.ATTR_WORK_DESCRIPTION +
      		", act." + IDBConstants.ATTR_NAME + " AS ACTIVITYNAME" +
      		", pd." + IDBConstants.ATTR_ORNO +
      		", pd." + IDBConstants.ATTR_ORDATE +
      		", pd." + IDBConstants.ATTR_PONO  +
      		", pd." + IDBConstants.ATTR_PODATE +
      		", pd." + IDBConstants.ATTR_IPCNO + 
      		", pd." + IDBConstants.ATTR_IPCDATE +
      		", pc." + IDBConstants.ATTR_ACTUAL_START_DATE +
      		", pc." + IDBConstants.ATTR_ACTUAL_END_DATE +
      		", pc." + IDBConstants.ATTR_VALIDATION +
      		", pd." + IDBConstants.ATTR_REGDATE +
      		", pd." + IDBConstants.ATTR_CUSTOMER +
      		", pd." + IDBConstants.ATTR_UNIT +
      		", pd." + IDBConstants.ATTR_FILE + 
      		", pd." + IDBConstants.ATTR_SHEET +
      		", pd." + IDBConstants.ATTR_ACTIVITY +
      		", pd." + IDBConstants.ATTR_DEPARTMENT + " ";
      
      String querytable = "FROM " + IDBConstants.TABLE_PROJECT_DATA + " pd " +
      		"LEFT JOIN " + IDBConstants.TABLE_CUSTOMER + " cust " +
      		"ON pd." + IDBConstants.ATTR_CUSTOMER + "=cust." + IDBConstants.ATTR_AUTOINDEX + " " +
      		"LEFT JOIN " + IDBConstants.TABLE_ACTIVITY + " act " +
      		"ON pd." + IDBConstants.ATTR_ACTIVITY + "=act." + IDBConstants.ATTR_AUTOINDEX + " " +
      		"LEFT JOIN " + IDBConstants.TABLE_PROJECT_CONTRACT + " pc " +
      		"ON pd." + IDBConstants.ATTR_AUTOINDEX + "=pc." + IDBConstants.ATTR_PROJECT + ") proj";
      
      //String addquery = "";

      if(m_andRadioBt.isSelected())
        operator = " AND ";
      else
        operator = " OR ";

/*      if(m_containsRadioBt.isSelected())
        equality = " LIKE ";
      else
        equality = " = ";
*/
      // project code
      String value = (String)getValueAt(row++, 1);  
      if(!value.equals("")) {
        criteria = " WHERE " + getCriteria(IDBConstants.ATTR_CODE, value);
      }

      // Customer
      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){      
        if(criteria.equals("")) {
        	criteria = " WHERE " + getCriteria("CUSTOMERNAME", value);
        }
        else {
        	criteria += operator + getCriteria("CUSTOMERNAME", value);
        }
      }
      
      // work description
      value = (String)getValueAt(row++, 1); 
      if(!value.equals("")){
        if(criteria.equals(""))
          criteria = " WHERE " + getCriteria(IDBConstants.ATTR_WORK_DESCRIPTION, value);
        else
          criteria += operator + getCriteria(IDBConstants.ATTR_WORK_DESCRIPTION, value);
      }

      //area code
      Object obj = getValueAt(row++, 1);
      if(obj instanceof Unit){
    	  if(criteria.equals("")){
    		  criteria = " WHERE " + IDBConstants.ATTR_UNIT + "=" +
    		  		((Unit)obj).getIndex(); 
    	  }else{
    		  criteria += operator + IDBConstants.ATTR_UNIT + "=" +
		  		((Unit)obj).getIndex(); 
    	  }
      }
      
      // O.R No.
      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(criteria.equals(""))
            criteria = " WHERE " + getCriteria(IDBConstants.ATTR_ORNO, value);
          else
            criteria += operator + getCriteria(IDBConstants.ATTR_ORNO, value);
      }

      // PO No
      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(criteria.equals(""))
            criteria = " WHERE " + getCriteria(IDBConstants.ATTR_PONO, value);
          else
            criteria += operator + getCriteria(IDBConstants.ATTR_PONO, value);
      }

      //IPC No
      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(criteria.equals(""))
            criteria = " WHERE " + getCriteria(IDBConstants.ATTR_PONO, value);
          else
            criteria += operator + getCriteria(IDBConstants.ATTR_PONO, value);
      }
      
      // date
      //java.util.Date date = null;
      String strstart = "", strend = "";
      java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd-MM-yyyy"),
      sf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
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

        if(!strstart.equals("")) {
          if(criteria.equals("")) {
            criteria = "WHERE " + IDBConstants.ATTR_ACTUAL_START_DATE + ">=" + "'" + strstart + "'";
          }
          else {
            criteria += operator + IDBConstants.ATTR_ACTUAL_START_DATE + ">=" + "'" + strstart + "'";
          }
        }

        if(!strend.equals("")) {
          if(criteria.equals("")) {
            criteria = "WHERE " + IDBConstants.ATTR_ACTUAL_END_DATE + "<=" + "'" + strend + "'";
          }
          else {
            criteria += operator + IDBConstants.ATTR_ACTUAL_END_DATE + "<=" + "'" + strend + "'";
          }
        }
      }

      // activity code
      obj = getValueAt(row++, 1);
      if(obj instanceof Activity){
    	  if(criteria.equals("")){
    		  criteria = " WHERE " + IDBConstants.ATTR_ACTIVITY + "=" +
    		  		((Activity)obj).getIndex(); 
    	  }else{
    		  criteria += operator + IDBConstants.ATTR_ACTIVITY + "=" +
		  		((Activity)obj).getIndex(); 
    	  }
      }
      
      // department
      obj = getValueAt(row++, 1);
      if(obj instanceof Organization){
    	  if(criteria.equals("")){
    		  criteria = " WHERE " + IDBConstants.ATTR_DEPARTMENT + "=" +
    		  		((Organization)obj).getIndex(); 
    	  }else{
    		  criteria += operator + IDBConstants.ATTR_DEPARTMENT + "=" +
		  		((Organization)obj).getIndex(); 
    	  }
      }
      // Status
      value = (String)getValueAt(row++, 1);
      if (value.equals("Active")){
    	  System.err.println(tgl);
    	  if(criteria.equals("")){
    		  criteria = "WHERE " + IDBConstants.ATTR_ACTUAL_END_DATE + ">=" + "'" + tgl + "' or " + IDBConstants.ATTR_ACTUAL_END_DATE + " is null ";
    	  }else{
    		  criteria += operator + IDBConstants.ATTR_ACTUAL_END_DATE + ">=" + "'" + tgl + "' or " + IDBConstants.ATTR_ACTUAL_END_DATE + " is null ";
    	  }
      }else if (value.equals("InActive")){
    	  if(criteria.equals("")){
    		  criteria = "WHERE " + IDBConstants.ATTR_ACTUAL_END_DATE + "<" + "'" + tgl + "'";
    	  }else{
    		  criteria += operator + IDBConstants.ATTR_ACTUAL_END_DATE + "<" + "'" + tgl + "'";
    	  }
      }    
      //System.err.println(queryselect + querytable + " " + criteria + "  order by a.autoindex");
      String query = queryselect + querytable + criteria + " ORDER BY " + IDBConstants.ATTR_CODE;
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