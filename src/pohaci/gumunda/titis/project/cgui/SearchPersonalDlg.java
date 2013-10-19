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

public class SearchPersonalDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  PersonalListPanel m_panel;
  SearchTable m_table;
  JRadioButton m_andRadioBt, m_orRadioBt;
  JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
  JButton m_findBt, m_closeBt, m_clearBt;

  Connection m_conn = null;
  long m_sessionid = -1;

  public SearchPersonalDlg(JFrame owner, PersonalListPanel panel,
                           Connection conn, long sessionid) {
    super(owner, "Search Personal", false);
    setSize(350, 400);
    m_mainframe = owner;
    m_panel = panel;
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

    scrollPane.setPreferredSize(new Dimension(100, 177));
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
      m_panel.reset(logic.getPersonalByCriteria(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, query));
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

      model.addRow(new Object[]{"Code", ""});
      model.addRow(new Object[]{"Name", ""});
      model.addRow(new Object[]{"Business Address", ""});
      model.addRow(new Object[]{"City", ""});
      model.addRow(new Object[]{"Post Code", ""});
      model.addRow(new Object[]{"Province", ""});
      model.addRow(new Object[]{"Country", ""});
      model.addRow(new Object[]{"Company", ""});
      model.addRow(new Object[]{"Job Title", ""});
      model.addRow(new Object[]{"Department", ""});

      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
      getColumnModel().getColumn(1).setCellEditor(new BaseTableCellEditor());
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
      String operator, equality, criteria = "";
      int row = 0;
      String queryselect = "SELECT * ";
      String querytable = "FROM " + IDBConstants.TABLE_PERSONAL;
      String addquery = "";

      if(m_andRadioBt.isSelected())
        operator = " AND ";
      else
        operator = " OR ";

      if(m_containsRadioBt.isSelected())
        equality = " LIKE ";
      else
        equality = " = ";

      String value = (String)getValueAt(row++, 1);
      if(!value.equals("")) {
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";
        criteria = "WHERE " + "UPPER(" + IDBConstants.ATTR_CODE + ")" + equality + "'" + value.toUpperCase() + "'";
      }

      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(criteria.equals(""))
          criteria = "WHERE " + "UPPER(" + IDBConstants.ATTR_FIRST_NAME + "& ' ' &" + IDBConstants.ATTR_LAST_NAME + ")" + equality + "'" + value.toUpperCase() + "'";
        else
          criteria += operator + "UPPER(" + IDBConstants.ATTR_FIRST_NAME + "& ' ' &" + IDBConstants.ATTR_LAST_NAME + ")" + equality + "'" + value.toUpperCase() + "'";
      }

      value = (String)getValueAt(row++, 1);
       if(!value.equals("")){
         if(m_containsRadioBt.isSelected())
           value = "%" + value + "%";

         if(addquery.equals("")) {
           addquery = "," + IDBConstants.TABLE_PERSONAL_BUSINESS;
           queryselect = "SELECT DISTINCT " + IDBConstants.TABLE_PERSONAL + ".* ";
           querytable += addquery;
         }

         if(criteria.equals("")) {
           criteria = "WHERE " + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX + operator +
                      "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_ADDRESS + ")" + equality + "'" + value.toUpperCase() + "'";
         }
         else {
           if(addquery.equals("")) {
             criteria += operator + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX + operator +
                         "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_ADDRESS + ")" + equality + "'" + value.toUpperCase() + "'";
           }
           else {
             criteria += operator + "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_ADDRESS + ")" + equality + "'" + value.toUpperCase() + "'";
           }
         }
      }

      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(criteria.equals(""))
          criteria = "WHERE " + "UPPER(" + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_CITY + ")" + equality + "'" + value.toUpperCase() + "'";
        else
          criteria += operator + "UPPER(" + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_CITY + ")" + equality + "'" + value.toUpperCase() + "'";
      }

      int postcode = 0;
      try {
        String strpostcode = (String)getValueAt(row++, 1);
        if(!(strpostcode).equals(""))
          postcode = Integer.parseInt(strpostcode);
      }
      catch(Exception ex) {
        throw new Exception("Post code have to fill in numeric");
      }

      if(postcode > 0) {
        if(criteria.equals(""))
          criteria = "WHERE " +  IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_POSTALCODE + " = " + postcode;
        else
          criteria += operator + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_POSTALCODE + " = " + postcode;
      }

      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(criteria.equals(""))
          criteria = "WHERE " + "UPPER(" + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_PROVINCE + ")" + equality + "'" + value.toUpperCase() + "'";
        else
          criteria += operator + "UPPER(" + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_PROVINCE + ")" + equality + "'" + value.toUpperCase() + "'";
      }

      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(criteria.equals(""))
          criteria = "WHERE " + "UPPER(" + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_COUNTRY + ")" + equality + "'" + value.toUpperCase() + "'";
        else
          criteria += operator + "UPPER(" + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_COUNTRY + ")" + equality + "'" + value.toUpperCase() + "'";
      }

      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(addquery.equals("")) {
          addquery = "," + IDBConstants.TABLE_PERSONAL_BUSINESS;
          queryselect = "SELECT DISTINCT " + IDBConstants.TABLE_PERSONAL + ".* ";
          querytable += addquery;
        }

        if(criteria.equals("")) {
          criteria = "WHERE " + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX + operator +
                     "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_COMPANY + ")" + equality + "'" + value.toUpperCase() + "'";
        }
        else {
          if(addquery.equals("")) {
            criteria += operator + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX + operator +
                        "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_COMPANY + ")" + equality + "'" + value.toUpperCase() + "'";
          }
          else {
            criteria += operator + "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_COMPANY + ")" + equality + "'" + value.toUpperCase() + "'";
          }
        }
      }

      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(addquery.equals("")) {
          addquery = "," + IDBConstants.TABLE_PERSONAL_BUSINESS;
          queryselect = "SELECT DISTINCT " + IDBConstants.TABLE_PERSONAL + ".* ";
          querytable += addquery;
        }

        if(criteria.equals("")) {
          criteria = "WHERE " + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX + operator +
                     "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_JOB_TITLE + ")" + equality + "'" + value.toUpperCase() + "'";
        }
        else {
          if(addquery.equals("")) {
            criteria += operator + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX + operator +
                        "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_JOB_TITLE + ")" + equality + "'" + value.toUpperCase() + "'";
          }
          else {
            criteria += operator + "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_JOB_TITLE + ")" + equality + "'" + value.toUpperCase() + "'";
          }
        }
      }

      value = (String)getValueAt(row++, 1);
      if(!value.equals("")){
        if(m_containsRadioBt.isSelected())
          value = "%" + value + "%";

        if(addquery.equals("")) {
          addquery = "," + IDBConstants.TABLE_PERSONAL_BUSINESS;
          queryselect = "SELECT DISTINCT " + IDBConstants.TABLE_PERSONAL + ".* ";
          querytable += addquery;
        }

        if(criteria.equals("")) {
          criteria = "WHERE " + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX + operator +
                     "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_DEPARTEMENT + ")" + equality + "'" + value.toUpperCase() + "'";
        }
        else {
          if(addquery.equals("")) {
            criteria += operator + IDBConstants.TABLE_PERSONAL + "." + IDBConstants.ATTR_AUTOINDEX + "=" + IDBConstants.ATTR_PERSONAL_INDEX + operator +
                        "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_DEPARTEMENT + ")" + equality + "'" + value.toUpperCase() + "'";
          }
          else {
            criteria += operator + "UPPER(" + IDBConstants.TABLE_PERSONAL_BUSINESS + "." + IDBConstants.ATTR_DEPARTEMENT + ")" + equality + "'" + value.toUpperCase() + "'";
          }
        }
      }
      System.err.println("query :" + queryselect + querytable + " " + criteria);
      return queryselect + querytable + " " + criteria;
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