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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
import pohaci.gumunda.titis.accounting.entity.GeneralIOweU;
import pohaci.gumunda.titis.accounting.entity.IOweULoader;
import pohaci.gumunda.titis.hrm.cgui.EmployeeCellEditor;
import pohaci.gumunda.titis.hrm.cgui.OrganizationCellEditor;

public class SearchIOweYouDialog extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
SearchTable m_searchTable;
  IOweYouListTable m_ioweyouListTable;
  JButton m_selectBt;
  int m_iResponse = JOptionPane.NO_OPTION;
  JFrame m_mainframe;
  SearchTable m_table;
  JRadioButton m_andRadioBt, m_orRadioBt;
  JRadioButton m_containsRadioBt, m_matchRadioBt, m_wholeRadioBt;
  JButton m_findBt, m_closeBt, m_clearBt;
  String type;

  Connection m_conn = null;
  long m_sessionid = -1;
  private IOweULoader loader;
  private JTextField filterField = new JTextField(10);

  public SearchIOweYouDialog(JFrame owner, String title, Connection conn, long sessionid){
    super(owner, ( title == null ) ? "Search I Owe You" : title, true);
    m_mainframe = owner;
    m_conn = conn;
    m_sessionid = sessionid;
    setSize(800, 700);
    constructComponent();
    find();
  }
  
  public SearchIOweYouDialog(JFrame owner, String title, Connection conn, long sessionid, IOweULoader loader,String type){
	  super(owner, ( title == null ) ? "Search Voucher" : title, true);
	  this.loader = loader;
	  m_mainframe = owner;
	  m_conn = conn;
	  m_sessionid = sessionid;
	  this.type=type;
	  setSize(800, 700);
	  constructComponent();
	  find();
  }

  void constructComponent() {
    m_ioweyouListTable = new IOweYouListTable();
    m_ioweyouListTable.addMouseListener(new MouseAdapter()  {
		public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() >= 2) {
              onAdd();
            }
			}
		});
    JPanel centerPanel = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    m_selectBt = new JButton("Select");
    buttonPanel.add(m_selectBt);
    m_selectBt.addActionListener(this);

    centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Receipt List",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
    Box vertBox  = Box.createVerticalBox();
	vertBox.add(filterField);
	vertBox.add(new JScrollPane(m_ioweyouListTable));
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
  
  private void setBorder(JPanel panel,String theme){
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), theme ,
				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));
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

    scrollPane.setPreferredSize(new Dimension(100, 112));
    scrollPane.getViewport().add(m_searchTable);

    operatorPanel.setLayout(new GridLayout(2, 1));
    operatorPanel.add(m_andRadioBt);
    operatorPanel.add(m_orRadioBt);
    setBorder(operatorPanel,"Search Criteria");

    optionPanel.setLayout(new GridLayout(3, 1));
    optionPanel.add(m_containsRadioBt);
    optionPanel.add(m_matchRadioBt);
    optionPanel.add(m_wholeRadioBt);
    setBorder(optionPanel,"Option");

    criteriaPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagConstraints.insets = new Insets(1, 0, 2, 0);
    gridBagConstraints.weightx = 1.0;
   // criteriaPanel.add(new JLabel("Search Criteria"), gridBagConstraints);
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new Insets(1, 0, 5, 0);
    criteriaPanel.add(scrollPane, gridBagConstraints);
    setBorder(criteriaPanel,"Search Criteria");
    
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new Insets(1, 0, 2, 0);
    //criteriaPanel.add(new JLabel("Search Operator"), gridBagConstraints);

    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new Insets(1, 0, 5, 0);
    criteriaPanel.add(operatorPanel, gridBagConstraints);

    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new Insets(1, 0, 2, 0);
    //criteriaPanel.add(new JLabel("Option"), gridBagConstraints);

    gridBagConstraints.gridy = 5;
    criteriaPanel.add(optionPanel, gridBagConstraints);

    gridBagConstraints.gridy = 6;
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_findBt);
    buttonPanel.add(m_clearBt);
    buttonPanel.add(m_closeBt);
    criteriaPanel.add(buttonPanel, gridBagConstraints);

    centerPanel.setLayout(new BorderLayout());
   // centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    centerPanel.add(criteriaPanel, BorderLayout.NORTH);
    //centerPanel.add(buttonPanel, BorderLayout.SOUTH);

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
	  
	  String criterion="";
	  String operator=" and ";
	  if(m_orRadioBt.isSelected()){
		  operator =" or ";
	  }
	  if(m_searchTable!=null)
	  criterion = loader.getCriterion(m_searchTable,operator);
	  
	  Object[] iOweUList = loader.find(criterion);
	  System.out.println(iOweUList.length);
	  m_ioweyouListTable.setProjectList(iOweUList);
  }

  void clear() {
    m_searchTable.clear();
    m_andRadioBt.setSelected(true);
    find();
  }
  
  Object selectedObj = null;
  void onAdd(){
	  m_iResponse = JOptionPane.OK_OPTION;
		int selectedRow = m_ioweyouListTable.getSelectedRow();
		if (selectedRow > -1){
			selectedObj = m_ioweyouListTable.getRow(selectedRow);
		}
		dispose();
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_selectBt) {
      onAdd();
    }else  if(e.getSource() == m_clearBt) {
      clear();
    }else  if(e.getSource() == m_findBt) {
      find();
    }else  if(e.getSource() == m_closeBt) {
      dispose();
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
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
	public SearchTable() {
      SearchTableModel model = new SearchTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");
     
      model.addRow(new Object[]{"I Owe U Type", type});
      model.addRow(new Object[]{"I Owe U No", ""});
      model.addRow(new Object[]{"I Owe U Date", dateformat.format(new Date())});
      model.addRow(new Object[]{"Pay to", ""});
      model.addRow(new Object[]{"Unit Code", ""});
      model.addRow(new Object[]{"Department", ""});
      
      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(125);
      getColumnModel().getColumn(0).setMaxWidth(150);
      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    }

    public TableCellEditor getCellEditor(int row, int col) {
    	if( col == 1){
    		if(row == 4 )
    			return new UnitCellEditor(GumundaMainFrame.getMainFrame(),
    					"Unit Code", m_conn, m_sessionid);
    		if(row==3  )
    			return new EmployeeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
    					m_conn, m_sessionid);
    		if(row == 2 )
    			return new DateCellEditor(GumundaMainFrame.getMainFrame());
    		if (row ==5)
    			return new OrganizationCellEditor(GumundaMainFrame.getMainFrame(),
    					m_conn, m_sessionid);
    	 
    	}
      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }
    public boolean isCellEditable(int row, int column) {
    	// TODO Auto-generated method stub
    	if(row==0&&column==1)return false;
    	return super.isCellEditable(row, column);
    }

    public void clear() {
      stopCellEditing();
      int row = getRowCount();
      for(int i = 1; i < row; i ++){
    	  if(i==2) 
    		  setValueAt(dateformat.format(new Date()), i, 1);
    	  else
    		  setValueAt("", i, 1);
      }
        
    }

    public String getCriteria() throws Exception {
      stopCellEditing();
      String criteria = "";
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
  class IOweYouListTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DefaultTableModelAdapter model =new DefaultTableModelAdapter();
	public IOweYouListTable() {
      model.addColumn("No");
      model.addColumn("I Owe U No");
      model.addColumn("I Owe U D");
      model.addColumn("Pay to");
      model.addColumn("Unit Code");
      model.addColumn("Department");
      setModel(model.buildTableModel(filterField));
      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
    }
	
    Object[] objRows;
    public Object getRow(int rowIdx){
    	return objRows[((Integer)getValueAt(rowIdx,0)).intValue()-1];
    }

    void setProjectList(Object[] object) {
    	model.clearRows();
    	objRows = object;
    	for(int i = 0; i < object.length; i ++) {
    		GeneralIOweU voucher = (GeneralIOweU) object[i];
    		model.addRow(new Object[]{
    				new Integer(i + 1),
    				voucher.igetIOweUNo(),
    				voucher.igetIOweUDate(),
    				voucher.igetPayTo(),
    				voucher.igetUnitCode(),
    				voucher.igetDepartment()}
    		);
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
  class IOweYouListTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
  
  /*public static void main(String[] args){
    Connection m_conn = null;
    long m_sessionid = -1;
    SearchIOweYouDialog iowe = new SearchIOweYouDialog(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            "Search Invoice", m_conn, m_sessionid);
    iowe.setVisible(true);
  }*/
}