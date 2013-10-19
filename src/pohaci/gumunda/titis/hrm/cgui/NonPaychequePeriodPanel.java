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
import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.*;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;

public class NonPaychequePeriodPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
PeriodTable m_table;
  JButton m_editBt, m_saveBt, m_cancelBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  NonPaychequePeriod m_period = null;
  boolean m_editable = false;

  public NonPaychequePeriodPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    initData();
  }

  void constructComponent() {
    JScrollPane scrollPane = new JScrollPane();
    JPanel buttonPanel = new JPanel();

    m_table = new PeriodTable();
    scrollPane.getViewport().add(m_table);
    scrollPane.setPreferredSize(new Dimension(100, 40));

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

    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);

    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  void initData() {
    try {
     HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
     m_period = logic.getNonPaychequePeriod(m_sessionid, IDBConstants.MODUL_MASTER_DATA);
     if(m_period != null)
       m_table.setNonPaychequePeriod(m_period);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warnng", JOptionPane.WARNING_MESSAGE);
    }
  }

  void edit() {
    m_editable = true;
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
  }

  void save() {
    NonPaychequePeriod period = null;
    try {
      period = m_table.getNonPaychequePeriod();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage());
      return;
    }

    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      logic.createNonPaychequePeriod(m_sessionid, IDBConstants.MODUL_MASTER_DATA, period);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    m_period = period;
    m_editable = false;
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void cancel() {
    if(m_period != null)
      m_table.setNonPaychequePeriod(m_period);
    else {
      m_table.stopCellEditing();
      m_table.clear();
    }

    m_editable = false;
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_editBt) {
      edit();
    }
    else if(e.getSource() == m_saveBt) {
      save();
    }
    else if(e.getSource() == m_cancelBt) {
      cancel();
    }
  }

  /**
   *
   */
  class PeriodTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PeriodTable() {
      PeriodTableModel model = new PeriodTableModel();
      model.addColumn("  ");
      model.addColumn("From");
      model.addColumn("To");
      model.addRow(new Object[]{"Period 1", "", ""});
      model.addRow(new Object[]{"Period 2", "", ""});
      setModel(model);

      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col > 0)
        return new IntegerCellEditor();
      return super.getCellEditor(row, col);
    }

    public void stopCellEditing() {
      TableCellEditor editor = null;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();

    }

    public NonPaychequePeriod getNonPaychequePeriod() throws Exception {
      short from1 = 0, to1 = 0, from2 = 0, to2 = 0;
      stopCellEditing();
      from1 = ((Integer)getValueAt(0, 1)).shortValue();
      to1 = ((Integer)getValueAt(0, 2)).shortValue();
      from2 = ((Integer)getValueAt(1, 1)).shortValue();
      to2 = ((Integer)getValueAt(1, 2)).shortValue();

      return new NonPaychequePeriod(from1, to1, from2, to2);
    }

    void setNonPaychequePeriod(NonPaychequePeriod period) {
      setValueAt(new Integer(period.getFrom1()), 0, 1);
      setValueAt(new Integer(period.getTo1()), 0, 2);
      setValueAt(new Integer(period.getFrom2()), 1, 1);
      setValueAt(new Integer(period.getTo2()), 1, 2);
    }

    void clear() {
      setValueAt(new Integer(0), 0, 1);
      setValueAt(new Integer(0), 0, 2);
      setValueAt(new Integer(0), 1, 1);
      setValueAt(new Integer(0), 1, 2);
    }
  }

  /**
   *
   */
  class PeriodTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;
      if(m_editable)
        return true;
      return false;
    }
  }
}