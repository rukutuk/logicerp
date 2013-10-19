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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class SearchProjectDetailDlg extends SearchProjectDlg {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
ProjectTable m_detailtable;
  JButton m_selectBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  ProjectData[] m_project = new ProjectData[0];  
  protected int rowSync = 0;

  public SearchProjectDetailDlg(JFrame owner, Connection conn, long sessionid) {     
    super(owner, null, conn, sessionid);
    setModal(true);
    setSize(450, 600);
    find();
  }

  void constructComponent() {
    m_detailtable = new ProjectTable();
    m_selectBt = new JButton("Select");
    m_selectBt.addActionListener(this);

    JPanel centerPanel = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_selectBt);

    centerPanel.add(new JScrollPane(m_detailtable), BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().add(criteriaPanel(), BorderLayout.NORTH);
    getContentPane().add(centerPanel, BorderLayout.CENTER);
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
      m_project = logic.getProjectDataByCriteria(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, query);      
      //Tambahan dari cok gung 22 mei 2007
        m_detailtable.setProjectData(m_project);        
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void clear() {
    super.clear();
    m_detailtable.clear();
  }

  public int getResponse() {
    return m_iResponse;
  }

  public ProjectData[] getProjectData() {
    return m_project;
  }
  

  void onSelect() {
    int row[] = m_detailtable.getSelectedRows();    
    java.util.Vector vresult = new java.util.Vector();    
    ProjectData project = (ProjectData)m_project[row[0]];      
    //Tambahan dari cok gung 22 Mei 2007
    try{
    project.setUnit(new AccountingSQLSAP().getUnitByIndex(project.getIndexunit(),m_conn));//.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
    project.setActivity(new AccountingSQLSAP().getActivityByIndex(project.getIndexact(), m_conn));//.getActivityByIndex(rs.getLong(IDBConstants.ATTR_ACTIVITY), conn),
    project.setDepartment(new HRMSQLSAP().getOrganizationByIndex(project.getIndexdept(), m_conn));
    }
    catch(Exception exc) {
        
      }

 
    vresult.addElement(project);     
    m_project = new ProjectData[vresult.size()];
    vresult.copyInto(m_project);
    m_iResponse = JOptionPane.OK_OPTION;
    dispose();
  }

  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if(e.getSource() == m_selectBt) {
      onSelect();
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
      model.addColumn("OR No.");
      model.addColumn("IPC No.");
      model.addColumn("Po No.");
      setModel(model);

      getColumnModel().getColumn(0).setPreferredWidth(50);
      getColumnModel().getColumn(0).setMaxWidth(50);
      
      addMouseListener(new MouseAdapter() {
          public void mouseClicked( MouseEvent e ) {
            if(e.getClickCount() >= 2) {
              onSelect();
            }
          }
        });
    }

    void setProjectData(ProjectData[] project) {        
      DefaultTableModel model = (DefaultTableModel)getModel(); 
      clear();      
      for(int i = 0; i < project.length; i ++) {
        rowSync = i;        
        model.addRow(new Object[]{            
            String.valueOf((i + 1)), project[i].getCode(),
            project[i].getCustname(),project[i].getORNo(),project[i].getIPCNo(),project[i].getPONo()
        }
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
  class ProjectTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}