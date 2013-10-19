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
import java.text.SimpleDateFormat;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.project.cgui.report.*;

public class ProjectListPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
ProjectTable m_table;
  JButton m_filterBt, m_refreshBt;
  JButton m_addBt, m_editBt, m_deleteBt, m_excellBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  ProjectData[] m_project = new ProjectData[0];
  boolean m_show = false;

  public ProjectListPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    initData();
  }

  void constructComponent() {
    m_table = new ProjectTable();
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

    ButtonGroup bg = new ButtonGroup();
    bg.add(m_excellBt);
    bg.add(m_filterBt);
    bg.add(m_refreshBt);

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

  void initData() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.setRowCount(0);

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_project = logic.getAllProjectData(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT);

      reset(m_project);
    }
    catch(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void reset(ProjectData[] project) {
    m_project = project;
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.setRowCount(0);    
    for(int i = 0; i < m_project.length; i ++) {
        
        String ordate = "";
        if(project[i].getORDate() != null)
            ordate = m_dateformat.format(m_project[i].getORDate());
        
        String podate = "";
        if(m_project[i].getPODate() != null)
            podate = m_dateformat.format(m_project[i].getPODate());
        
        String ipcdate = "";
        if(m_project[i].getIPCDate() != null)
            ipcdate = m_dateformat.format(m_project[i].getIPCDate());
         
        String startdate = "", enddate = "", valdate = "";
        if (m_project[i].getStartdate()!=null)          
            startdate =  m_dateformat.format(m_project[i].getStartdate());
        if(m_project[i].getEnddate() != null)
          enddate = m_dateformat.format(m_project[i].getEnddate());
        if(project[i].getValidation() != null)
          valdate = m_dateformat.format(m_project[i].getValidation());
        
        model.addRow(new Object[]{
        String.valueOf((i + 1)),m_project[i].getCode(),
        m_project[i].getCustname(),m_project[i].getWorkDescription(),
        m_project[i].getAct(),m_project[i].getORNo(),
        ordate,m_project[i].getPONo(),
        podate,
        m_project[i].getIPCNo(),ipcdate,startdate,
        enddate,valdate
      });
    }
     
  }

  public void refresh() {
    initData();
  }

  void onAdd() {
    ProjectDataDlg dlg = new ProjectDataDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
       this,  m_conn, m_sessionid);
    dlg.setVisible(true);
  }

  void onEdit() {      
    int row = m_table.getSelectedRow();
    if(row == -1)
      return;        
    //DefaultTableModel model = (DefaultTableModel)m_table.getModel();          
    ProjectData project = (ProjectData)m_project[row];        
    ProjectDataDlg dlg = new ProjectDataDlg(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
       this,  m_conn, m_sessionid, project);
    dlg.setVisible(true);
  }

  void onDelete() {
    int row = m_table.getSelectedRow();
    if(row == -1)
      return;

    if(!Misc.getConfirmation())
      return;

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    ProjectData project = (ProjectData)m_project[row]; 
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteProjectData(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, project);            
      model.removeRow(row);   
      refresh();      
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning 1", JOptionPane.WARNING_MESSAGE);
    }
  }

  void addProjectData(ProjectData project) {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    String ordate = "";
    if (project !=null){
        if(project.getORDate() != null)
          ordate = m_dateformat.format(project.getORDate());

        String podate = "";
        if(project.getPODate() != null)
          podate = m_dateformat.format(project.getPODate());

        String ipcdate = "";
        if(project.getIPCDate() != null)
          ipcdate = m_dateformat.format(project.getIPCDate());

        model.addRow(new Object[]{
          String.valueOf(model.getRowCount() + 1),
          project,
          project.getCustomer().getName(),
          project.getWorkDescription(),
          project.getActivity(),
          project.getORNo(),
          ordate,
          project.getPONo(),
          podate,
          project.getIPCNo(),
          ipcdate
        });

        m_table.getSelectionModel().setSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
    }
  }

  void editProjectData(ProjectData project) {    
    int row = m_table.getSelectedRow() ;   
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();

    for(int i = 0; i < m_table.getRowCount(); i ++) {
      ProjectData projectcomp = (ProjectData)project;
      if(projectcomp.getIndex() == project.getIndex()) {
        row = i;
        break;
      }
    }

     model.removeRow(row);     

    String ordate = "";
    if(project.getORDate() != null)
      ordate = m_dateformat.format(project.getORDate());

    String podate = "";
    if(project.getPODate() != null)
      podate = m_dateformat.format(project.getPODate());

    String ipcdate = "";
    if(project.getIPCDate() != null)
      ipcdate = m_dateformat.format(project.getIPCDate());

    model.insertRow(row, new Object[]{
      String.valueOf(row + 1),
      project,
      project.getCustomer().getName(),
      project.getWorkDescription(),
      project.getActivity(),
      project.getORNo(),
      ordate,
      project.getPONo(),
      podate,
      project.getIPCNo(),
      ipcdate
      });

    m_table.getSelectionModel().setSelectionInterval(row, row);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      onAdd();
    }
    else if(e.getSource() == m_editBt) {
      onEdit();
    }
    else if(e.getSource() == m_deleteBt) {
      onDelete();
    }
    else if(e.getSource() == m_filterBt) {
      if(!m_show) {
        m_show = true;
        SearchProjectDlg dlg = new SearchProjectDlg(
            pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
            this, m_conn, m_sessionid);
        dlg.setVisible(true);
        m_show = false;
      }
    }
    else if(e.getSource() == m_refreshBt) {
      refresh();
    }
    else if(e.getSource() == m_excellBt) {
          new ProjectlistJasper(m_conn,m_project,true);     
    }
  }

  /**
   *
   */
  class ProjectTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProjectTable() {
      ProjectTableModel model = new ProjectTableModel();      
      model.addColumn("No");
      model.addColumn("Project Code");
      model.addColumn("Customer");
      model.addColumn("Work Description");
      model.addColumn("Activity Code");
      model.addColumn("O.R No.");
      model.addColumn("O.R Date");
      model.addColumn("PO No.");
      model.addColumn("PO Date");
      model.addColumn("IPC No.");
      model.addColumn("IPC Date");
      model.addColumn("Act Start Date");
      model.addColumn("Act End Date");
      model.addColumn("Validation Date");
      setModel(model);

      addMouseListener(new MouseAdapter() {
        public void mouseClicked( MouseEvent e ) {
          if(e.getClickCount() >= 2) {
            onEdit();
          }
        }
      });
    }
  }

  /**
   *
   */
  public class ProjectTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}
