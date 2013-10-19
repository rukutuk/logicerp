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
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class SearchProjectNoteDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  ProjectNotesPanel m_panel;
  SearchTable m_table;
  JRadioButton m_andRadioBt, m_orRadioBt;
  JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
  JButton m_findBt, m_closeBt, m_clearBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  ProjectData m_project = null;

  public SearchProjectNoteDlg(JFrame owner, ProjectNotesPanel panel, ProjectData project,
                          Connection conn, long sessionid) {
    super(owner, "Search Project Note", false);
    setSize(350, 350);
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

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_panel.reset(logic.getProjectNotesByCriteria(m_sessionid,
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

      model.addRow(new Object[]{"Date", ""});
      model.addRow(new Object[]{"Description", ""});
      model.addRow(new Object[]{"Action", ""});
      model.addRow(new Object[]{"Responbility", ""});
      model.addRow(new Object[]{"Prepared By", ""});
      model.addRow(new Object[]{"Aprover", ""});

      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    }

	public TableCellEditor getCellEditor(int row, int col) {
    	if( col == 1){
    		if(row == 0)
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

    public String getCriterion() throws Exception {
        stopCellEditing();
        String operator, equality, criteria = "",temp="";
        int row = 0;      
        boolean first = true;
        String queryselect = "SELECT projectnotes.* ";
        String querytable = "FROM " + IDBConstants.TABLE_PROJECT_NOTES + ",employee " +   
        					"WHERE " + IDBConstants.ATTR_PROJECT + "=" + m_project.getIndex() + 
        					" AND projectnotes.project=employee.autoindex";
        					//" AND (";
        //String addquery = "";

        if(m_andRadioBt.isSelected())
          operator = " AND ";
        else
          operator = " OR ";

        if(m_containsRadioBt.isSelected())
          equality = " LIKE ";
        else
          equality = " = ";


        java.util.Date date = null;
        String value = (String)getValueAt(row++, 1);
        java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("dd-MM-yyyy"),
        sf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");

        try {
          if(!value.equals("")) {
            date = sf.parse(value);
            value = sf2.format(date);
          }
        }
        catch(Exception ex) {
          throw new Exception(ex.getMessage());
        }
        
        // date      
        if(!value.equals("")){    	  
      	  if (first){       	  
            	temp = "";
            	first = false;
            }else {
            	temp = operator;        	
            }
            criteria += temp + IDBConstants.ATTR_NOTES_DATE + "='" + value + "'";
        }
        
        //Description
        value = (String)getValueAt(row++, 1);
        if(!value.equals("")){
          if(m_containsRadioBt.isSelected())
        	  value = "%" + value + "%";
          if (first){       	  
          	temp = "";
          	first = false;
          }else {
          	temp = operator;        	
          }            
            criteria += temp + "UPPER(" + IDBConstants.ATTR_DESCRIPTION + ")" + equality + "'" + value.toUpperCase() + "'";
        }

        // Action
        value = (String)getValueAt(row++, 1);
        if(!value.equals("")){
          if(m_containsRadioBt.isSelected())
            value = "%" + value + "%";
          if (first){       	  
          	temp = "";
          	first = false;
          }else {
          	temp = operator;        	
          }
            criteria += temp + "UPPER(" + IDBConstants.ATTR_ACTION + ")" + equality + "'" + value.toUpperCase() + "'";
        }

        // Responsibility
        value = (String)getValueAt(row++, 1);
        if(!value.equals("")){
          if(m_containsRadioBt.isSelected())
            value = "%" + value + "%";
          
          querytable = "FROM " + IDBConstants.TABLE_PROJECT_NOTES + ",employee " +   
  			"WHERE " + IDBConstants.ATTR_PROJECT + "=" + m_project.getIndex();
          
          if (first){       	  
          	temp = "";
          	first = false;
          }else {
          	temp = operator;        	
          }
            criteria += temp + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_RESPONSIBILITY + " AND " +
                        "UPPER(" + IDBConstants.ATTR_FIRST_NAME + "& ' ' &" + IDBConstants.ATTR_MIDLE_NAME + "& ' ' &" + IDBConstants.ATTR_LAST_NAME +  ")" + equality + "'" + value.toUpperCase() + "'";
        }

        // Prepared By      
        value = (String)getValueAt(row++, 1);
        if(!value.equals("")){
          if(m_containsRadioBt.isSelected())
            value = "%" + value + "%";
        
          querytable = "FROM " + IDBConstants.TABLE_PROJECT_NOTES + ",employee " +   
  		"WHERE " + IDBConstants.ATTR_PROJECT + "=" + m_project.getIndex();
          
          if (first){       	  
          	temp = "";
          	first = false;
          }else {
          	temp = operator;        	
          }
            criteria += temp + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PREPARED_BY + " AND " +
                        "UPPER(" + IDBConstants.ATTR_FIRST_NAME + "& ' ' &" + IDBConstants.ATTR_MIDLE_NAME + "& ' ' &" + IDBConstants.ATTR_LAST_NAME +  ")" + equality + "'" + value.toUpperCase() + "'";          
        }

        // Approver
        value = (String)getValueAt(row++, 1);
        if(!value.equals("")){
          if(m_containsRadioBt.isSelected())
            value = "%" + value + "%";
          
          querytable = "FROM " + IDBConstants.TABLE_PROJECT_NOTES + ",employee " +   
  		"WHERE " + IDBConstants.ATTR_PROJECT + "=" + m_project.getIndex();
          
          if (first){       	  
          	temp = "";
          	first = false;
          }else {
          	temp = operator;        	
          }
            criteria += temp + pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_APPROVER + " AND " +
                        "UPPER(" + IDBConstants.ATTR_FIRST_NAME + "& ' ' &" + IDBConstants.ATTR_MIDLE_NAME + "& ' ' &" + IDBConstants.ATTR_LAST_NAME +  ")" + equality + "'" + value.toUpperCase() + "'";
          
        }
        
        System.out.println(queryselect + querytable + "  AND ( " + criteria + " ) ");
        if (criteria.equals(""))
      	  return queryselect + querytable;
        else
      	  return queryselect + querytable + " AND ( " + criteria + " ) ";
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