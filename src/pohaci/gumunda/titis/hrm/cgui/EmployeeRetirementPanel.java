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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.DateCellEditor;
import pohaci.gumunda.titis.application.Misc;

public class EmployeeRetirementPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  RetirementTable m_table;
  JButton m_editBt, m_saveBt, m_cancelBt;
  JComboBox m_statusComboBox = new JComboBox(Retirement.m_statuss);
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");

  boolean m_editable = false;
  boolean m_edit = false;
  int m_editedIndex = -1;
  Retirement m_retirement = null;

  public EmployeeRetirementPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    setEnabled(false);
  }

  void constructComponent() {
    JPanel northPanel = new JPanel();
    JPanel formPanel = new JPanel();
    JScrollPane scrollpane = new JScrollPane();
    JPanel buttonPanel = new JPanel();

    m_table = new RetirementTable();
    scrollpane.getViewport().add(m_table);
    scrollpane.setPreferredSize(new Dimension(350, 97));

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    buttonPanel.add(m_editBt);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    buttonPanel.add(m_saveBt);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    buttonPanel.add(m_cancelBt);

    formPanel.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    formPanel.add(scrollpane, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridheight = 2;
    formPanel.add(new JPanel(), gridBagConstraints);

    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 0.0;
    gridBagConstraints.insets = new Insets(0, 3, 0, -3);
    formPanel.add(buttonPanel, gridBagConstraints);

    northPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    northPanel.add(formPanel);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(15, 10, 8, 10));
    add(northPanel, BorderLayout.CENTER);
  }

  public void setEnabled(boolean editable) {
    if(editable) {
      m_editBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);
    }
    else {
      m_editBt.setEnabled(false);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);
    }
  }

  void onEdit() {
    m_edit = true;
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
  }

  void onSave() {
    Retirement retirement = null;
    try {
      retirement = m_table.getRetirement();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }
    m_retirement = retirement;
    m_edit = false;
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void onCancel() {
    m_table.stopCellEditing();

    if(!Misc.getConfirmation())
      return;
    if(m_retirement != null)
      setRetirement(m_retirement);
    else
      clear();

    m_edit = false;
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  public Retirement getRetirement() {
    return m_retirement;
  }

  public void setRetirement(Retirement retirement) {
    m_retirement = retirement;
    m_edit = false;
    clear();
    if(retirement != null)
      m_table.setRetirement(retirement);
  }

  public void clear() {
    m_retirement = null;
    m_table.clearTable();
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_editBt) {
      onEdit();
    }
    else if(e.getSource() == m_saveBt) {
      onSave();
    }
    else if(e.getSource() == m_cancelBt) {
      onCancel();
    }
  }


  /**
   *
   */
  class RetirementTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RetirementTable() {
      RetirementTableModel model = new RetirementTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");
      model.addRow(new Object[]{"Retirement Reference No", ""});
      model.addRow(new Object[]{"Retirement Reference Date", ""});
      model.addRow(new Object[]{"Reason", ""});
      model.addRow(new Object[]{"Remark", ""});
      model.addRow(new Object[]{"TMT", ""});
      //model.addRow(new Object[]{"Status", ""});
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(150);
      getColumnModel().getColumn(0).setMaxWidth(150);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if((row == 1 || row == 4) && col == 1)
        return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
      //if(row == 5 && col == 1)
        //return new DefaultCellEditor(m_statusComboBox);

      return new BaseTableCellEditor();
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public void clearTable() {
      int count = getRowCount();
      for(int i = 0; i < count; i ++)
        setValueAt("", i, 1);
    }

    public Retirement getRetirement() throws Exception {
      stopCellEditing();

      java.util.ArrayList list = new java.util.ArrayList();
      java.util.Date date = null, tmt = null;
      String reference = "", reason = "", remark = "";

      int row = 0;
      reference = (String)getValueAt(row++, 1);
      if(reference == null || reference.equals("")){
       // list.add("Retirement Reference");
      }

      String strdate = (String)getValueAt(row++, 1);
      if(strdate.equals("")){
        //list.add("Retirement Date");
      }

      reason = (String)getValueAt(row++, 1);
      remark = (String)getValueAt(row++, 1);

      String strtmt = (String)getValueAt(row++, 1);
      if(strtmt.equals("")){
        //list.add("TMT Date");
      }
      
      String strexc = "Please insert description of :\n";
      String[] exception = new String[list.size()];
      list.toArray(exception);
      if(exception.length > 0) {
        for(int i = 0; i < exception.length; i ++)
          strexc += " - " + exception[i] + "\n";
        throw new Exception(strexc);
      }

      try {
    	  if (!strdate.equals(""))
    		  date = m_dateformat.parse(strdate);
    	  if (!strtmt.equals(""))
    		  tmt = m_dateformat.parse(strtmt);
      }
      catch(Exception ex) {
        throw new Exception(ex.getMessage());
      }

      //return new Retirement(reference, date, reason, remark, tmt, status);
      return new Retirement(reference, date, reason, remark, tmt);
    }

    public void setRetirement(Retirement retirement) {
      int row = 0;
      //if (!retirement.toString().equals(""))
    	  setValueAt(retirement.toString(), row++, 1);
      if (retirement.getRetirementDate()!=null)
    	  setValueAt(m_dateformat.format(retirement.getRetirementDate()), row++, 1);
      //if (!retirement.getReason().equals(""))
      setValueAt(retirement.getReason(), row++, 1);
      setValueAt(retirement.getRemarks(), row++, 1);
      if (retirement.getTMT()!=null)
    	  setValueAt(m_dateformat.format(retirement.getTMT()), row++, 1);
     // setValueAt(retirement.getStatusAsString(), row++, 1);
    }
  }

  /**
   *
   */
  class RetirementTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;
      if(m_edit)
        return true;
      return false;
    }
  }
}