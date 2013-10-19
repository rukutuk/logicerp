
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
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import pohaci.gumunda.titis.application.Misc;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;

public class ProjectClientContactPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JButton m_addBt, m_deleteBt, m_saveBt, m_cancelBt;
  ContactTable m_table;
  boolean m_editable = false;

  Connection m_conn = null;
  long m_sessionid = -1;
  ProjectData m_project = null;
  int m_editedIndex = -1;
  boolean m_new = false;

  public ProjectClientContactPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    setEditable(false);
  }

  void constructComponent() {
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    m_table = new ContactTable();

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_deleteBt);
    buttonPanel.add(m_saveBt);
    buttonPanel.add(m_cancelBt);

    setEditable(false);
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(3, 10, 10, 10));
    add(buttonPanel, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }

  public void setEditable(boolean editable) {
    m_editable = editable;
    m_addBt.setEnabled(editable);
    m_deleteBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  public void clear() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.setRowCount(0);
  }

  public void setProjectData(ProjectData project) {
    m_project = project;
  }

  public void setProjectClientContact(ProjectData project) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      ProjectClientContact[] contact = logic.getProjectClientContact(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, project.getIndex());
      m_table.setProjectClientContact(contact);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void add() {
    SearchProjectClientContactDetailDlg dlg = new SearchProjectClientContactDetailDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, m_project);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION){
      Personal[] personal = dlg.getPersonal();
      if(personal.length > 0) {
        m_table.addPersonal(personal[0]);

        m_new = true;
        m_editedIndex = m_table.getRowCount()-1;
        m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
        m_addBt.setEnabled(false);
        m_deleteBt.setEnabled(false);
        m_saveBt.setEnabled(true);
        m_cancelBt.setEnabled(true);
      }
    }
  }

  void save() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    Personal personal = (Personal)model.getValueAt(m_editedIndex, 2);

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      ProjectClientContact contact = logic.createProjectClientContact(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT,
          m_project.getIndex(), personal);

      m_table.updateProjectClientContact(contact, m_editedIndex);
      m_new = false;
      m_addBt.setEnabled(true);
      m_saveBt.setEnabled(false);
      m_cancelBt.setEnabled(false);
      m_deleteBt.setEnabled(true);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }
  }

  void delete() {
    int row = m_table.getSelectedRow();
    if(row == -1)
      return;

    if(!Misc.getConfirmation())
      return;

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    ProjectClientContact contact = (ProjectClientContact)model.getValueAt(row, 2);

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteProjectClientContact(m_sessionid, IDBConstants.MODUL_PROJECT_MANAGEMENT, contact.getIndex());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }

    model.removeRow(row);
    m_table.updateNumber(0);
  }

  void cancel() {
    if(!Misc.getConfirmation())
      return;

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    m_new = false;
    model.removeRow(m_editedIndex);
  }

  void business() {
    ProjectClientContact  contact = (ProjectClientContact)m_table.getValueAt(m_table.getSelectedRow(), 2);
    PersonalBusinessListDlg dlg = new PersonalBusinessListDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, contact.getPersonal());
    dlg.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      add();
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

  /**
   *
   */
  class ContactTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContactTable() {
      ContactTableModel model = new ContactTableModel();
      model.addColumn("No");
      model.addColumn("Code");
      model.addColumn("Name");
      model.addColumn("Address");
      model.addColumn("City");
      model.addColumn("Postcode");
      model.addColumn("Phone");
      model.addColumn("Fax");
      model.addColumn("Email");
      model.addColumn("Website");
      setModel(model);

      getSelectionModel().addListSelectionListener(model);
      addMouseListener(new MouseAdapter() {
        public void mouseClicked( MouseEvent e ) {
          if(e.getClickCount() >= 2) {
            business();
          }
        }
      });
    }

    public void addPersonal(Personal personal) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      String postcode = "";
      if(personal.getPostCode() > 0)
        postcode = String.valueOf(personal.getPostCode());

      model.insertRow(getRowCount(), new Object[]{
        String.valueOf(getRowCount() + 1), personal.getCode(), personal,
        personal.getAddress(), personal.getCity(),
        postcode,
        personal.getPhone1(),
        personal.getFax1(),
        personal.getEmail(),
        personal.getWebSite()
      });
    }

    public void updateProjectClientContact(ProjectClientContact contact, int row) {
      OtherPersonal personal = new OtherPersonal(contact.getPersonal().getIndex(), contact.getPersonal());
      String postcode = "";
      if(personal.getPostCode() > 0)
        postcode = String.valueOf(personal.getPostCode());

      DefaultTableModel model = (DefaultTableModel)getModel();
      model.removeRow(row);
      model.insertRow(row, new Object[]{
        String.valueOf(getRowCount() + 1), contact.getPersonal().getCode(), contact,
        personal.getAddress(), personal.getCity(),
        postcode,
        personal.getPhone1(),
        personal.getFax1(),
        personal.getEmail(),
        personal.getWebSite()
      });
    }

    public void setProjectClientContact(ProjectClientContact[] contact) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < contact.length; i ++) {
        Personal personal = contact[i].getPersonal();
        OtherPersonal other = new OtherPersonal(personal.getIndex(), personal);

        String postcode = "";
        if(other.getPostCode() > 0)
          postcode = String.valueOf(other.getPostCode());

        model.addRow(new Object[]{
          String.valueOf(i + 1), other.getCode(), contact[i],
          other.getAddress(), other.getCity(),
          postcode,
          other.getPhone1(),
          other.getFax1(),
          other.getEmail(),
          other.getWebSite()
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
  class ContactTableModel extends DefaultTableModel implements ListSelectionListener {
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

        if(m_new){
          if(iRowIndex != m_editedIndex)
            m_table.getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
        }
        else{
          if(iRowIndex != -1){
            m_addBt.setEnabled(true);
            m_saveBt.setEnabled(false);
            m_cancelBt.setEnabled(false);
            m_deleteBt.setEnabled(true);
          }
          else{
            m_addBt.setEnabled(true);
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
  class OtherPersonal extends Personal {
    public OtherPersonal(long index, Personal personal) {
      super(index, personal);
    }

    public String toString() {
      String str = m_firstname + " " + m_lastname;
      return str;
    }
  }
}