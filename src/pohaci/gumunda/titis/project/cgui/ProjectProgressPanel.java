package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.cgui.report.*;

public class ProjectProgressPanel extends JPanel implements ActionListener{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;

  JButton m_excellBt, m_filterBt, m_refreshBt;
  JButton m_addBt, m_editBt, m_deleteBt;
  ProgressTable m_table;

  ProjectData m_project = null;
  ProjectProgress[] m_progress = null;
  boolean m_show = false;

  public ProjectProgressPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    constructComponent();
    setEditable(false);
  }

  void constructComponent() {
    m_table = new ProgressTable();
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
    setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    add(northbar, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }

  public void setProjectData(ProjectData project) {
    m_project = project;
  }
  
  public void setEditable(boolean editable) {
	    m_addBt.setEnabled(editable);
	    m_editBt.setEnabled(false);
	    m_deleteBt.setEnabled(false);

	    m_excellBt.setEnabled(editable);
	    m_filterBt.setEnabled(editable);
	    m_refreshBt.setEnabled(editable);
 }

  public void setProgress(ProjectData project) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_progress = logic.getProjectProgress(m_sessionid,
          IDBConstants.MODUL_PROJECT_MANAGEMENT, project.getIndex());      
      m_table.setProgress(m_progress);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }
  
  public void setM_progress(){
	  try {
	      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
	      m_progress = logic.getProjectProgress(m_sessionid,
	          IDBConstants.MODUL_PROJECT_MANAGEMENT, m_project.getIndex());
	    }
	  catch(Exception ex) {
	      JOptionPane.showMessageDialog(this, ex.getMessage(),
	                                    "Warning", JOptionPane.WARNING_MESSAGE);
	    }
  }

  void refresh() {
	  setProgress(m_project); 
  }
  
  public void reset(ProjectProgress[] progress) {    
	m_progress = progress;
	m_table.setProgress(m_progress);
  }
  
  void onAdd() {
    if(m_project == null)
      return;

    ProjectProgressDlg dlg = new ProjectProgressDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, m_project);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      ProjectProgress progress = dlg.getProgress();      
      m_table.addProgress(progress);
      setM_progress();
    }
  }

  void onEdit() {
    int row = m_table.getSelectedRow();
    System.err.println("row sebelom :" + row);
    ProjectProgress progress = (ProjectProgress)m_table.getValueAt(row, 1);
    ProjectProgressDlg dlg = new ProjectProgressDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, progress);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {      
      progress = dlg.getProgress();           
      m_table.updateProgress(progress, row);
      setM_progress();
    }    
    m_table.updateNumber(0);
  }

  void onDelete() {
    int row = m_table.getSelectedRow();
    ProjectProgress progress = (ProjectProgress)m_table.getValueAt(row, 1);

    if(!pohaci.gumunda.titis.application.Misc.getConfirmation())
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteProjectProgress(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, progress.getIndex());
      setM_progress();
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    ((DefaultTableModel)m_table.getModel()).removeRow(row);
    m_table.updateNumber(0);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_excellBt) {      
        new ProjectMonitoringProgressJasper(m_conn,m_progress,true);       
    }
    else if(e.getSource() == m_filterBt) {
        if(!m_show) {
            m_show = true;
            SearchProjectProgressDlg dlg = new SearchProjectProgressDlg(
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
      onAdd();
    }
    else if(e.getSource() == m_editBt) {
      onEdit();
    }
    else if(e.getSource() == m_deleteBt) {
      onDelete();
    }
  }

  /**
   *
   */
  class ProgressTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProgressTable() {
      ProgressTableModel model = new ProgressTableModel();
      model.addColumn("No.");
      model.addColumn("Date");
      model.addColumn("Description");
      model.addColumn("% Complete");
      model.addColumn("Prepared By");
      model.addColumn("Approved By");
      model.addColumn("Remark");
      setModel(model);
      getSelectionModel().addListSelectionListener(model);

      addMouseListener(new MouseAdapter() {
        public void mouseClicked( MouseEvent e ) {
          if(e.getClickCount() >= 2) {
            onEdit();
          }
        }
      });
    }

    public void addProgress(ProjectProgress progress) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.addRow(new Object[]{
        String.valueOf(getRowCount() + 1), progress, progress.getDescription(),
        new Float(progress.getCompletion()), progress.getPreparedBy(),
        progress.getApprover(), progress.getRemark()
      });
    }

    public void updateProgress(ProjectProgress progress, int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row,new Object[]{
        String.valueOf(getRowCount() + 1), progress, progress.getDescription(),
        new Float(progress.getCompletion()), progress.getPreparedBy(),
        progress.getApprover(), progress.getRemark()
      });
      this.getSelectionModel().setSelectionInterval(row, row);
      updateNumber(0);
    }

    public void setProgress(ProjectProgress[] progress) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < progress.length; i ++) {
        model.addRow(new Object[]{
        String.valueOf(getRowCount() + 1), progress[i], progress[i].getDescription(),
        new Float(progress[i].getCompletion()), progress[i].getPreparedBy(),
        progress[i].getApprover(), progress[i].getRemark()
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
  class ProgressTableModel extends DefaultTableModel implements ListSelectionListener {
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
