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
import javax.swing.event.*;
import javax.swing.table.*;

import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class ProjectPartnerPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
PartnerTable m_table;
  JButton m_addBt, m_editBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  ProjectData m_project = null;

  public ProjectPartnerPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {
    m_table = new PartnerTable();

    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);

    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(buttonPanel, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }

  public void setEditable(boolean editable) {
    m_addBt.setEnabled(editable);
    m_editBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
  }

  public void setProjectData(ProjectData project) {    
    m_project = project;
  }

  public void setProjectPartner(ProjectData project) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_table.setProjectPartner(logic.getProjectPartner(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, project.getIndex()));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void add() {        
    ProjectPartnerDlg dlg = new ProjectPartnerDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, m_project);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      m_table.addProjectPartner(dlg.getProjectPartner());
    }
    int m_addIndex = m_table.getRowCount()-1;
    m_table.getSelectionModel().setSelectionInterval(m_addIndex , m_addIndex );
  }

  void edit() {
    int row = m_table.getSelectedRow();
    ProjectPartner projectpartner = (ProjectPartner)m_table.getValueAt(row, 3);
    
    ProjectPartnerDlg dlg = new ProjectPartnerDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, projectpartner);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION){
      m_table.updateProjectPartner(dlg.getProjectPartner(), row);
    }
    m_table.getSelectionModel().setSelectionInterval(row , row);
     m_table.updateNumber(0);
  }

  void delete() {
    int row = m_table.getSelectedRow();
    ProjectPartner projectpartner = (ProjectPartner)m_table.getValueAt(row, 3);

    if(!pohaci.gumunda.titis.application.Misc.getConfirmation())
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteProjectPartner(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, projectpartner.getIndex());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.removeRow(row);
    m_table.updateNumber(0);
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
  }

  /**
   *
   */
  class PartnerTable extends JTable {
        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public PartnerTable() {
      PartnerTableModel model = new PartnerTableModel();
      model.addColumn("No");
      model.addColumn("Code");
      model.addColumn("Group");
      model.addColumn("Name");
      model.addColumn("Address");
      model.addColumn("City");
      model.addColumn("Postcode");
      model.addColumn("Country");
      model.addColumn("Phone1");
      model.addColumn("Phone2");
      model.addColumn("Fax");
      model.addColumn("Email");
      model.addColumn("Website");

      setModel(model);
      getSelectionModel().addListSelectionListener(model);

      addMouseListener(new MouseAdapter() {
       public void mouseClicked( MouseEvent e ) {
         if(e.getClickCount() >= 2) {
           edit();
         }
       }
      });
    }

    void addProjectPartner(ProjectPartner projectpartner) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      Partner partner = projectpartner.getPartner();
      String postcode = "";
      if(partner.getPostCode() > 0)
        postcode = String.valueOf(partner.getPostCode());

      model.addRow(new Object[]{
        String.valueOf(getRowCount() + 1),
        partner.getCode(), partner.getCompanyToString(),
        projectpartner,
        partner.getAddress(), partner.getCity(), postcode,
        partner.getPhone1(), partner.getPhone2(), partner.getFax1(),
        partner.getEmail(), partner.getWebsite()
      });
    }

    void updateProjectPartner(ProjectPartner projectpartner, int row) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);

      Partner partner = projectpartner.getPartner();
      String postcode = "";
      if(partner.getPostCode() > 0)
        postcode = String.valueOf(partner.getPostCode());

      model.insertRow(row, new Object[]{
        String.valueOf(row + 1),
        partner.getCode(), partner.getCompanyToString(),
        projectpartner,
        partner.getAddress(), partner.getCity(), postcode,
        partner.getPhone1(), partner.getPhone2(), partner.getFax1(),
        partner.getEmail(), partner.getWebsite()
      });      
    }

    void setProjectPartner(ProjectPartner[] projectpartner) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      for(int i = 0; i < projectpartner.length; i ++) {
        Partner partner = projectpartner[i].getPartner();
        String postcode = "";
        if(partner.getPostCode() > 0)
          postcode = String.valueOf(partner.getPostCode());

        model.addRow(new Object[]{
          String.valueOf(i + 1),
          partner.getCode(), partner.getCompanyToString(),
          projectpartner[i],
          partner.getAddress(), partner.getCity(), postcode,
          partner.getPhone1(), partner.getPhone2(), partner.getFax1(),
          partner.getEmail(), partner.getWebsite()
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
  class PartnerTableModel extends DefaultTableModel implements ListSelectionListener {
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

        if(iRowIndex != -1){
          m_addBt.setEnabled(true);
          m_editBt.setEnabled(true);
          m_deleteBt.setEnabled(true);
        }
        else {
          m_addBt.setEnabled(true);
          m_editBt.setEnabled(false);
          m_deleteBt.setEnabled(false);
        }
      }
    }
  }
}