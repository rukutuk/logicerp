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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
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

public class SearchProjectDialog extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
SearchTable m_searchTable;
  ProjectListTable m_projectListTable;
  JButton m_selectBt;
  int m_iResponse = JOptionPane.NO_OPTION;
  JFrame m_mainframe;
  SearchTable m_table;
  JRadioButton m_andRadioBt, m_orRadioBt;
  JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
  JButton m_findBt, m_closeBt, m_clearBt;

  Connection m_conn = null;
  long m_sessionid = -1;

  public SearchProjectDialog(JFrame owner, String title, Connection conn, long sessionid){
    super(owner, ( title == null ) ? "Search Project" : title, true);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    setSize(800, 700);
    constructComponent();
    find();
  }

  void constructComponent() {
    m_projectListTable = new ProjectListTable();

    JPanel centerPanel = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    m_selectBt = new JButton("Select");
    buttonPanel.add(m_selectBt);

    centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Project List",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));

    centerPanel.add(new JScrollPane(m_projectListTable), BorderLayout.CENTER);
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

    scrollPane.setPreferredSize(new Dimension(100, 180));
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
  }

  void clear() {
    m_searchTable.clear();
    m_projectListTable.clear();
  }

  void onAdd(){
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_selectBt) {
      onAdd();
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

      model.addRow(new Object[]{"Project Code", ""});
      model.addRow(new Object[]{"Costumer", ""});
      model.addRow(new Object[]{"Unit Code", ""});
      model.addRow(new Object[]{"Activity Code", ""});
      model.addRow(new Object[]{"Department", ""});
      model.addRow(new Object[]{"OR No", ""});
      model.addRow(new Object[]{"SO/PO/Contract No", ""});
      model.addRow(new Object[]{"IPC No", ""});
      model.addRow(new Object[]{"Start Date [ >= ]", ""});
      model.addRow(new Object[]{"End Date [ <= ]", ""});

      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(125);
      getColumnModel().getColumn(0).setMaxWidth(150);
      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    }

    public TableCellEditor getCellEditor(int row, int col) {
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

    public String getCriteria() throws Exception {
      stopCellEditing();
      //String operator, equality, 
      String criteria = "";
      //int row = 0;
      String strquery = "";//"SELECT * FROM " + IDBConstants.TABLE_EMPLOYEE;
      return strquery + " " + criteria;
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
  class ProjectListTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProjectListTable() {
      ProjectListTableModel model = new ProjectListTableModel();

      model.addColumn("No");
      model.addColumn("Project Code");
      model.addColumn("Costumer");
      model.addColumn("Activity Code");
      model.addColumn("OR No");
      model.addColumn("SO/PO/Contract No");
      model.addColumn("IPC No");
      model.addColumn("Work Description");
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
    }

    void setProjectList(Object[] object) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < object.length; i ++) {
        //OtherEmployee emp = new OtherEmployee(employee[i].getIndex(), employee[i], true);
        model.addRow(new Object[]{
      String.valueOf((i + 1)), "", "", "", "", "", "", ""});
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
  class ProjectListTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}