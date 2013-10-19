package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.project.cgui.report.TimeSheetJasper;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class TimeSheetDetailPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JButton m_excellBt, m_filterBt, m_refreshBt;
  JButton m_addBt, m_editBt, m_deleteBt;
  TimeSheetTable m_table;

  Connection m_conn = null;
  long m_sessionid = -1;
  ProjectData m_project = null;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  boolean m_show = false;

  TimeSheet[] m_sheet = new TimeSheet[0];

  public TimeSheetDetailPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    setEditable(false);
  }

  void constructComponent() {
    m_table = new TimeSheetTable();
    m_excellBt = new JButton(new ImageIcon("../images/excell.gif"));
    m_excellBt.addActionListener(this);
    m_filterBt = new JButton(new ImageIcon("../images/filter2.gif"));
    m_filterBt.addActionListener(this);
    m_refreshBt = new JButton(new ImageIcon("../images/refresh.gif"));
    m_refreshBt.addActionListener(this);
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);

    JToolBar northbar = new JToolBar();
    JPanel buttonPanel = new JPanel();

    northbar.setFloatable(false);
    northbar.setLayout(new GridBagLayout());
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    northbar.add(m_excellBt, gridBagConstraints);

    gridBagConstraints.gridx = 1;
    northbar.add(m_filterBt, gridBagConstraints);

    gridBagConstraints.gridx = 2;
    northbar.add(m_refreshBt, gridBagConstraints);

    gridBagConstraints.gridx = 3;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    northbar.add(buttonPanel, gridBagConstraints);

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);

    setLayout(new BorderLayout());
    add(northbar, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }

  public void setEditable(boolean editable) {
    m_addBt.setEnabled(editable);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);

    m_excellBt.setEnabled(editable);
    m_filterBt.setEnabled(editable);
    m_refreshBt.setEnabled(editable);
  }

  public void setProjectData(ProjectData project) {
    m_project = project;
  }

  public void setTimeSheet(ProjectData project) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_sheet = logic.getTimeSheet(m_sessionid,
              IDBConstants.MODUL_PROJECT_MANAGEMENT, m_project.getIndex());
      reset(m_sheet);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void refresh() {
    setTimeSheet(m_project);
    //reset(m_sheet);
  }

  public void reset(TimeSheet[] sheet) {
	  m_sheet = sheet;
	  m_table.setTimeSheet(m_sheet);
  }

  void add() {
    TimeSheetDlg dlg = new TimeSheetDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                        m_conn, m_sessionid, m_project);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      m_table.addTimeSheet(dlg.getTimeSheet());
    }
  }

  void edit() {
    int row = m_table.getSelectedRow();
    TimeSheet timesheet = (TimeSheet)m_table.getValueAt(row, 1);
    TimeSheetDlg dlg = new TimeSheetDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                        m_conn, m_sessionid, m_project, timesheet);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      m_table.updateTimeSheet(row, dlg.getTimeSheet());
    }
    m_table.updateNumber(0);
  }

  void delete() {
    int row = m_table.getSelectedRow();
    TimeSheet timesheet = (TimeSheet)m_table.getValueAt(row, 1);

    if(!Misc.getConfirmation())
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteTimeSheet(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, timesheet.getIndex());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    ((DefaultTableModel)m_table.getModel()).removeRow(row);
    m_table.updateNumber(0);
  }

  public void actionPerformed(ActionEvent e){
    if(e.getSource() == m_filterBt) {
      if(!m_show) {
        m_show = true;             
        
        SearchTimeSheetDlg dlg = new SearchTimeSheetDlg(
            pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            this, m_project, m_conn, m_sessionid);
        dlg.setVisible(true);
        m_show = false;
      }
    }
    else if(e.getSource() == m_refreshBt) {
      refresh();
    }
    else if(e.getSource() == m_addBt) {
      add();
    }
    else if(e.getSource() == m_editBt) {
      edit();
    }
    else if(e.getSource() == m_deleteBt) {
      delete();
    }
    else if(e.getSource()==m_excellBt){        
          new TimeSheetJasper(m_conn,m_sheet,true); 
    	
    }
  }

  /**
   *
   */
  class TimeSheetTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimeSheetTable() {
      TimeSheetTableModel model = new TimeSheetTableModel();
      model.addColumn("No.");
      model.addColumn("Entry Date");
      model.addColumn("Prepared By");
      model.addColumn("Prepared Date");
      model.addColumn("Check By");
      model.addColumn("Check Date");
      model.addColumn("Approved By");
      model.addColumn("Approval Date");
      model.addColumn("Description");

      setModel(model);
      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
      getSelectionModel().addListSelectionListener(model);

      addMouseListener(new MouseAdapter() {
        public void mouseClicked( MouseEvent e ) {
          if(e.getClickCount() >= 2) {
            edit();
          }
        }
      });
    }

    void addTimeSheet(TimeSheet timesheet) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{
        String.valueOf(getRowCount() + 1), timesheet,
        timesheet.getPreparedBy(),
        m_dateformat.format(timesheet.getPreparedDate()),
        timesheet.getCheckedBy(),
        m_dateformat.format(timesheet.getCheckedDate()),
        timesheet.getApproved(),
        m_dateformat.format(timesheet.getApprovedDate()),
        timesheet.getDescription()
      });
    }

    void updateTimeSheet(int row, TimeSheet timesheet) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);

      model.insertRow(row, new Object[]{
        String.valueOf(getRowCount() + 1), timesheet,
        timesheet.getPreparedBy(),
        m_dateformat.format(timesheet.getPreparedDate()),
        timesheet.getCheckedBy(),
        m_dateformat.format(timesheet.getCheckedDate()),
        timesheet.getApproved(),
        m_dateformat.format(timesheet.getApprovedDate()),
        timesheet.getDescription()
      });
      this.getSelectionModel().setSelectionInterval(row, row);
      updateNumber(0);
    }

    void setTimeSheet(TimeSheet[] timesheet) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < timesheet.length; i ++) {
        model.addRow(new Object[]{
        String.valueOf(getRowCount() + 1), timesheet[i],
        timesheet[i].getPreparedBy(),
        m_dateformat.format(timesheet[i].getPreparedDate()),
        timesheet[i].getCheckedBy(),
        m_dateformat.format(timesheet[i].getCheckedDate()),
        timesheet[i].getApproved(),
        m_dateformat.format(timesheet[i].getApprovedDate()),
        timesheet[i].getDescription()
      });
      }
    }
    
    void updateNumber(int col) {
        for(int i = 0; i < getRowCount(); i ++)
            setValueAt(String.valueOf(i + 1), i, col); 
    }
  }

  /**
   *
   */
  class TimeSheetTableModel extends DefaultTableModel implements ListSelectionListener {
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

        if(iRowIndex != -1) {
          m_addBt.setEnabled(true);
          m_editBt.setEnabled(true);
          m_deleteBt.setEnabled(true);
        }
        else{
          m_addBt.setEnabled(true);
          m_deleteBt.setEnabled(false);
          m_editBt.setEnabled(false);
        }
      }
    }
  }
}

