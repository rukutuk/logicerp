package pohaci.gumunda.titis.hrm.cgui;

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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import pohaci.gumunda.titis.application.JYearChooser;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class AnnualLeaveRightPanel extends JPanel implements ActionListener, FocusListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//JSpinField m_fromSpinField, m_thruSpinField;
  JYearChooser m_fromYearChooser, m_thruYearChooser;
	
  //JTextField m_fromTextField, m_thruTextField;
  JTextField m_cloneminTextField, m_clonemaxTextField, m_maxleaveTextField, m_minleaveTextField;
  JTextField m_minTextField, m_maxTextField;
  JButton m_addBt, m_editBt, m_deleteBt, m_saveBt, m_cancelBt;
  LeaveTable m_table;

  Connection m_conn = null;
  long m_sessionid = -1;

  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;
  AnnualLeaveRight m_leave = null;
  
  int year;

  public AnnualLeaveRightPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    setEditableForm(false);
    initData();
  }

  void constructComponent() {
    //m_fromTextField = new JTextField(5);
    //m_thruTextField = new JTextField(5);
    // i change with it  
    Calendar calendar = Calendar.getInstance(Locale.getDefault());
    year = calendar.get(Calendar.YEAR);
        
//    m_fromSpinField = new JSpinField();
//    m_fromSpinField.setMinimum(0);
//    m_fromSpinField.setMaximum(year + 100);
//    m_fromSpinField.setValue(year);
//       
//    m_thruSpinField = new JSpinField();
//    m_thruSpinField.setMinimum(0);
//    m_thruSpinField.setMaximum(year + 100);
//    m_thruSpinField.setValue(0);
    
    m_fromYearChooser = new JYearChooser();
    m_fromYearChooser.setMinimum(0);
    m_fromYearChooser.setMaximum(year + 100);
    m_fromYearChooser.setValue(year);
    
    m_thruYearChooser = new JYearChooser();
    m_thruYearChooser.setMinimum(0);
    m_thruYearChooser.setMaximum(year + 100);
    m_thruYearChooser.setValue(year);
    
    //m_thruTextField.addFocusListener(this);
    m_cloneminTextField = new JTextField(3);
    m_clonemaxTextField = new JTextField(3);

    m_minTextField = new JTextField(3);
    m_minTextField.addFocusListener(this);
    m_maxTextField = new JTextField(3);
    m_maxTextField.addFocusListener(this);
    m_minleaveTextField = new JTextField(3);
    m_maxleaveTextField = new JTextField(3);

    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);

    m_table = new LeaveTable();

    JPanel centerPanel = new JPanel();
    JPanel northPanel = new JPanel();
    JPanel formPanel = new JPanel();
    JPanel validityPanel = new JPanel();
    JPanel leavePanel = new JPanel();
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    JLabel fromLabel = new JLabel("Year From"), toLabel = new JLabel("Year To"),
    minLabel = new JLabel("Min. Working Years"), cloneminLabel = new JLabel("Working Years"),
    maxLabel = new JLabel("Until"), clonemaxLabel = new JLabel("Working Years more than"),
    minleaveLabel = new JLabel(", Annual Leaves Right"), maxleaveLabel = new JLabel(", Annual Leave Right");
    
    validityPanel.setLayout(new GridBagLayout());
    validityPanel.setPreferredSize(new Dimension(400, 60));
    validityPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Rule Validity",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));

    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new Insets(0, 10, 0, 20);
    validityPanel.add(fromLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    //validityPanel.add(m_fromTextField, gridBagConstraints);
    //validityPanel.add(m_fromSpinField, gridBagConstraints);
    validityPanel.add(m_fromYearChooser, gridBagConstraints);
    
    gridBagConstraints.gridx = 2;
    validityPanel.add(toLabel, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    //validityPanel.add(m_thruTextField, gridBagConstraints);
   // validityPanel.add(m_thruSpinField, gridBagConstraints);
    validityPanel.add(m_thruYearChooser, gridBagConstraints);
    
    gridBagConstraints.gridx = 4;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    validityPanel.add(new JPanel(), gridBagConstraints);

    leavePanel.setLayout(new GridBagLayout());
    leavePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Leaves Right Rule",
        javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION,
        new Font("Tahoma", Font.PLAIN, 11), Color.blue.darker().darker()));

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new Insets(0, 10, 10, 10);
    leavePanel.add(minLabel, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    leavePanel.add(m_minTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    leavePanel.add(cloneminLabel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    leavePanel.add(m_cloneminTextField, gridBagConstraints);

    gridBagConstraints.gridx = 2;
    leavePanel.add(maxLabel, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    leavePanel.add(m_maxTextField, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    leavePanel.add(minleaveLabel, gridBagConstraints);

    gridBagConstraints.gridx = 5;
    leavePanel.add(m_minleaveTextField, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    leavePanel.add(clonemaxLabel, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    leavePanel.add(m_clonemaxTextField, gridBagConstraints);

    gridBagConstraints.gridx = 4;
    leavePanel.add(maxleaveLabel, gridBagConstraints);

    gridBagConstraints.gridx = 5;
    leavePanel.add(m_maxleaveTextField, gridBagConstraints);

    formPanel.setLayout(new GridBagLayout());
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    formPanel.add(validityPanel, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
    formPanel.add(new JLabel(" "), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 1.0;
    formPanel.add(leavePanel, gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 1.0;
    formPanel.add(buttonPanel, gridBagConstraints);

    northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    northPanel.add(formPanel);

    centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 8, 10));
    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(northPanel, BorderLayout.NORTH);
    centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);

    setLayout(new BorderLayout());
    add(centerPanel, BorderLayout.CENTER);
  }

  void setEditableForm(boolean editable) {
    //m_fromTextField.setEnabled(editable);
   // m_thruTextField.setEnabled(editable);
//    m_fromSpinField.setEnabled(editable);
//    m_thruSpinField.setEnabled(editable);
	m_fromYearChooser.setEnabled(editable);
	m_thruYearChooser.setEnabled(editable);
    m_minTextField.setEnabled(editable);
    m_cloneminTextField.setEnabled(false);
    m_maxTextField.setEnabled(editable);
    m_clonemaxTextField.setEnabled(false);
    m_minleaveTextField.setEnabled(editable);
    m_maxleaveTextField.setEnabled(editable);
  }

  void initData() {
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      AnnualLeaveRight[] leave = logic.getAllAnnualLeaveRight(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
      m_table.setAnnualeaveRight(leave);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void clear() {
    //m_fromTextField.setText("");
    //m_thruTextField.setText("");
    //m_fromSpinField.setValue(year);
    //m_thruSpinField.setValue(0);
	m_fromYearChooser.setValue(year);
	m_thruYearChooser.setValue(0);
    m_minTextField.setText("");
    m_cloneminTextField.setText("");
    m_maxTextField.setText("");
    m_clonemaxTextField.setText("");
    m_minleaveTextField.setText("");
    m_maxleaveTextField.setText("");
  }

  void add() {
    m_new = true;
    clear();
    setEditableForm(true);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
  }

  void edit() {
    m_edit = true;
    m_editedIndex = m_table.getSelectedRow();
    setEditableForm(true);
    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
  }

  void delete() {
    m_editedIndex = m_table.getSelectedRow();
    if(!Misc.getConfirmation())
      return;

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      logic.deleteAnnualLeaveRight(m_sessionid, IDBConstants.MODUL_MASTER_DATA, m_leave.getIndex());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.removeRow(m_editedIndex);
    clear();
    setEditableForm(false);
  }

  void save() {
    AnnualLeaveRight leave = null;
    try {
      leave = getAnnualLeaveRight();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      if(m_new) {
        leave = logic.createAnnualLeaveRight(m_sessionid, IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT, leave);
        m_table.addAnnualLeaveRight(leave);
      }
      else {
        leave = logic.updateAnnualLeaveRight(m_sessionid, IDBConstants.TABLE_ANNUAL_LEAVE_RIGHT,
            m_leave.getIndex(), leave);
        m_table.updateAnnualLeaveRight(leave, m_editedIndex);
      }
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_new = false; 
    m_edit = false;
    m_table.clearSelection();
    clear();
    setEditableForm(false);
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }
  
  void cancel() {
    if(!Misc.getConfirmation())
      return;

    if(m_new){
      m_new = false;
      m_editBt.setEnabled(false);
      m_deleteBt.setEnabled(false);      
    }
    else if(m_edit){
      m_edit = false;
      m_table.updateAnnualLeaveRight(m_leave, m_editedIndex);
      m_editBt.setEnabled(true);
      m_deleteBt.setEnabled(true);
    }
    
    clear();
    setEditableForm(false);
    m_addBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  AnnualLeaveRight getAnnualLeaveRight() throws Exception {
    short from = 0, thru = 0, minyear = 0, maxyear = 0, minright = 0, maxright = 0;

    ArrayList list = new ArrayList();
    
//    if (m_fromTextField.getText().length()>0) {
//        from = Short.parseShort(m_fromTextField.getText());
//    } else {
//        list.add("Year From");
//    }
//    if (m_thruTextField.getText().length()>0) {
//        thru = Short.parseShort(m_thruTextField.getText());
//    } else {
//        list.add("Year To");
//    }
    //from = new Integer(m_fromSpinField.getValue()).shortValue();
    //thru = new Integer(m_thruSpinField.getValue()).shortValue();
    from = new Integer(m_fromYearChooser.getValue()).shortValue();
    thru = new Integer(m_thruYearChooser.getValue()).shortValue();
    if (m_minTextField.getText().length()>0) {
        minyear = Short.parseShort(m_minTextField.getText());
    } else {
        list.add("Min Working Years");
    }
    if (m_maxTextField.getText().length()>0) {
        maxyear = Short.parseShort(m_maxTextField.getText());
    } else {
        list.add("Upper bound of Group 1");
    }
    if (m_minleaveTextField.getText().length()>0) {
        minright = Short.parseShort(m_minleaveTextField.getText());
    } else {
        list.add("Group 1 Leave Right");
    }
    if (m_maxTextField.getText().length()>0) {
         maxright = Short.parseShort(m_maxleaveTextField.getText());
    } else {
        list.add("Group 2 Leave Right");
    }
    
    String strexc = "Please insert description of :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
        for(int i = 0; i < exception.length; i ++)
            strexc += " - " + exception[i] + "\n";
        throw new Exception(strexc);
    }
    
    return new AnnualLeaveRight(from, thru, minyear, maxyear, minright, maxright);
  }

  void setAnnualLeaveRight(AnnualLeaveRight right) {
    m_leave = right;
    //m_fromTextField.setText(String.valueOf(right.getFrom()));
    //m_thruTextField.setText(String.valueOf(right.getThru()));
    //m_fromSpinField.setValue((int)right.getFrom());
    //m_thruSpinField.setValue((int)right.getThru());
    m_fromYearChooser.setValue((int)right.getFrom());
    m_thruYearChooser.setValue((int)right.getThru());
    m_minTextField.setText(String.valueOf(right.getMinYear()));
    m_cloneminTextField.setText(String.valueOf(right.getMinYear()));
    m_maxTextField.setText(String.valueOf(right.getMaxYear()));
    m_clonemaxTextField.setText(String.valueOf(right.getMaxYear()));
    m_minleaveTextField.setText(String.valueOf(right.getMinRight()));
    m_maxleaveTextField.setText(String.valueOf(right.getMaxRight()));
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      add();
    }
    else if(e.getSource() == m_editBt) {
      edit();
    }
    else if(e.getSource() == m_deleteBt) {
      delete();
    }
    else if(e.getSource() == m_saveBt) {
      save();
    }
    else if(e.getSource() == m_cancelBt) {
      cancel();
    }
  }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
//        if(e.getSource()==m_thruTextField) {
//            if(m_thruTextField.equals("")) {
//                m_thruTextField.setText("0");
//            }
//        }
        if(e.getSource()==m_minTextField){
            m_cloneminTextField.setText(m_minTextField.getText());
        }
        if(e.getSource()==m_maxTextField){
            m_clonemaxTextField.setText(m_maxTextField.getText());
        }
    }

  /**
   *
   */
  class LeaveTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeaveTable() {
      LeaveTableModel model = new LeaveTableModel();
      model.addColumn("Valid From");
      model.addColumn("Valid Until");
      model.addColumn("Min Working Year(s)");
      model.addColumn("Group 1 Years");
      model.addColumn("Group 1 Right");
      model.addColumn("Group 2 Years");
      model.addColumn("Group 2 Right");
      setModel(model);

      getSelectionModel().addListSelectionListener(model);
    }

    public TableCellRenderer getCellRenderer(int row, int col) {
      return new AllignTableCellRenderer();
    }

    void setAnnualeaveRight(AnnualLeaveRight[] leave) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < leave.length; i ++)
        model.addRow(new Object[]{
      leave[i], String.valueOf(leave[i].getThru()), String.valueOf(leave[i].getMinYear()),
      String.valueOf(leave[i].getMinYear()) + " - " + String.valueOf(leave[i].getMaxYear()),
      String.valueOf(leave[i].getMinRight()), " > " + String.valueOf(leave[i].getMaxYear()),
      String.valueOf(leave[i].getMaxRight())
      });
    }

    void addAnnualLeaveRight(AnnualLeaveRight leave) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{
        leave, String.valueOf(leave.getThru()), String.valueOf(leave.getMinYear()),
        String.valueOf(leave.getMinYear()) + " - " + String.valueOf(leave.getMaxYear()),
        String.valueOf(leave.getMinRight()), " > " + String.valueOf(leave.getMaxYear()),
        String.valueOf(leave.getMaxRight())
      });
    }

    void updateAnnualLeaveRight(AnnualLeaveRight leave, int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row,  new Object[]{
        leave, String.valueOf(leave.getThru()), String.valueOf(leave.getMinYear()),
        String.valueOf(leave.getMinYear()) + " - " + String.valueOf(leave.getMaxYear()),
        String.valueOf(leave.getMinRight()), " > " + String.valueOf(leave.getMaxYear()),
        String.valueOf(leave.getMaxRight())
      });
    }
  }

  /**
   *
   */
  class LeaveTableModel extends DefaultTableModel implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }

    public void valueChanged(ListSelectionEvent e) {
      if(!e.getValueIsAdjusting()){
        int iRowIndex = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();

        if(m_new || m_edit){
          if(iRowIndex != m_editedIndex)
            m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
        }
        else{
          if(iRowIndex != -1){
            m_addBt.setEnabled(true);
            m_editBt.setEnabled(true);
            m_saveBt.setEnabled(false);
            m_cancelBt.setEnabled(false);
            m_deleteBt.setEnabled(true);

            AnnualLeaveRight leave = (AnnualLeaveRight)getValueAt(iRowIndex, 0);
            AnnualLeaveRightPanel.this.setAnnualLeaveRight(leave);
          }
          else{
            m_addBt.setEnabled(true);
            m_editBt.setEnabled(false);
            m_saveBt.setEnabled(false);
            m_cancelBt.setEnabled(false);
            m_deleteBt.setEnabled(false);
          }
        }
      }
    }
  }

  /**
   *
   */

  public class AllignTableCellRenderer extends DefaultTableCellRenderer{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,
        Object value,
        boolean isSelected,
        boolean hasFocus,
        int row,
        int column) {

      setHorizontalAlignment(JLabel.CENTER);
      return super.getTableCellRendererComponent(table, value, isSelected,
          hasFocus, row, column);
    }
  }
}