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
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.titis.project.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.Misc;

public class PersonalPanel extends JPanel implements ActionListener, ListSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
PersonalListPanel m_list;
  PersonalTable m_table;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  PersonalHomePanel m_homepanel;
  PersonalBusinessPanel m_businesspanel;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;

  Personal m_personal = null;

  public PersonalPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {
    System.out.println("personal komponen");
    
    // deklarasikan splitpane dari jsplitpane
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JTabbedPane tabPane = new JTabbedPane();
    JPanel listPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel button1Panel = new JPanel();
    JPanel button2Panel = new JPanel();

    
    m_list = new PersonalListPanel(m_conn, m_sessionid);
    m_list.getList().addListSelectionListener(this);
    m_table = new PersonalTable(m_conn, m_sessionid);

    m_homepanel = new PersonalHomePanel();
    m_businesspanel = new PersonalBusinessPanel();
    
    // definisikan button
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    button1Panel.add(m_addBt);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    button1Panel.add(m_editBt);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    button1Panel.add(m_deleteBt);
    m_saveBt = new JButton("Save");
    m_saveBt.addActionListener(this);
    button2Panel.add(m_saveBt);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);
    button2Panel.add(m_cancelBt);

    // set enabled/disabled button
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(false);

    // set jtoolbar list personal dan buttonpanel pada listpanel
    listPanel.setLayout(new BorderLayout());
    listPanel.add(m_list, BorderLayout.CENTER);
    listPanel.add(button1Panel, BorderLayout.SOUTH);

    // set tab pada panel
    tabPane.setBackground(Color.white);
    tabPane.setForeground(Color.blue.darker().darker());
    tabPane.addTab("Profile", new JScrollPane(m_table));
    tabPane.addTab("Home", m_homepanel);
    tabPane.addTab("Business", m_businesspanel);

    // set tab dan button2 panel pada centerpanel
    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(tabPane, BorderLayout.CENTER);
    centerPanel.add(button2Panel, BorderLayout.SOUTH);
    
    // set listpanel pada splitpanel sebelah kiri
    splitPane.setLeftComponent(listPanel);
    
    // set centerpanel pada splitpanel sebelah kanan
    splitPane.setRightComponent(centerPanel);
    splitPane.setDividerLocation(200);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
  }

  void onAdd() {
    m_new = true;
    m_table.m_editable = true;
    m_table.clearTable();
    m_homepanel.setEnabled(true);
    m_homepanel.clear();
    m_businesspanel.setEnabled(true);
    m_businesspanel.clear();

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_table.m_editable = true;
    m_homepanel.setEnabled(true);
    m_businesspanel.setEnabled(true);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onSave() {
    Personal personal = null;
    PersonalHome[] home = new PersonalHome[0];
    PersonalBusiness[] business = new PersonalBusiness[0];

    try {
      personal = m_table.getPersonal();

      home = m_homepanel.getPersonalHome();
      personal.setPersonalHome(home);

      business = m_businesspanel.getPersonalBusiness();
      personal.setPersonalBusiness(business);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if(m_new) {
      try {
        m_new = false;
        createPersonal(personal);
      }
      catch(Exception ex) {
        ex.printStackTrace();
        m_new = true;
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                      JOptionPane.WARNING_MESSAGE);
      }
    }
    else {
      try {
        updatePersonal(new Personal(m_personal.getIndex(), personal));
      }
      catch(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Perhatian",
                                      JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  void onCancel() {
    m_table.stopCellEditing();

    if(!Misc.getConfirmation())
      return;

    if(m_personal != null)
      setSelectedObject(m_personal);
    else
      objectViewNoSelection();
  }

  void onDelete() {
    deletePartner();
  }

  void setSelectedObject(Personal personal) {
    m_personal = personal;
    m_table.setPersonal(personal);
    setPersonalHome(personal);
    setPersonalBusiness(personal);

    m_new = false;
    m_edit = false;
    m_table.m_editable = false;
    m_homepanel.setEnabled(false);
    m_businesspanel.setEnabled(false);

    m_addBt.setEnabled(true);
    m_editBt.setEnabled(true);
    m_saveBt.setEnabled(false);
    m_deleteBt.setEnabled(true);
    m_cancelBt.setEnabled(false);
  }

  void objectViewNoSelection(){
    m_new = false;
    m_edit = false;
    m_table.m_editable = false;
    m_homepanel.setEnabled(false);
    m_businesspanel.setEnabled(false);

    m_table.clearTable();
    m_homepanel.clear();
    m_businesspanel.clear();
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void createPersonal(Personal personal) throws Exception {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
    Personal object = logic.createPersonal(m_sessionid, IDBConstants.MODUL_MASTER_DATA, personal);

    model.addElement(object);
    m_list.getList().setSelectedValue(object, true);
  }

  void updatePersonal(Personal personal) throws Exception {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
    Personal object = logic.updatePersonal(m_sessionid, IDBConstants.MODUL_MASTER_DATA, personal.getIndex(), personal);

    model.setElementAt(object, m_editedIndex);
    setSelectedObject(object);
  }

  void deletePartner() {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    Personal personal = (Personal)m_list.getList().getSelectedValue();

    if(!Misc.getConfirmation())
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deletePersonal(m_sessionid, IDBConstants.MODUL_MASTER_DATA, personal.getIndex());
      model.removeElementAt(m_editedIndex);
      m_personal = null;
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void setPersonalHome(Personal personal) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      PersonalHome[] home = logic.getPersonalHome(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, personal.getIndex());

      m_homepanel.setPersonalHome(home);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setPersonalBusiness(Personal personal) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      PersonalBusiness[] business = logic.getPersonalBusiness(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, personal.getIndex());

      m_businesspanel.setPersonalBusiness(business);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }


  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_addBt) {
      onAdd();
    }
    else if(e.getSource() == m_editBt) {
      onEdit();
    }
    else if(e.getSource() == m_saveBt) {
      onSave();
    }
    else if(e.getSource() == m_cancelBt) {
      onCancel();
    }
    else if(e.getSource() == m_deleteBt) {
      onDelete();
    }
  }

  public void valueChanged(ListSelectionEvent e) {
    if(!e.getValueIsAdjusting()){
      int iRowIndex = m_list.getList().getMinSelectionIndex();

      if(m_new || m_edit){
        if(iRowIndex != m_editedIndex){
          int count = m_list.getList().getModel().getSize() + 1;
          if(m_editedIndex == -1)
            m_list.getList().getSelectionModel().setSelectionInterval(m_editedIndex + count,
                m_editedIndex + count);
          else
            m_list.getList().getSelectionModel().setSelectionInterval(m_editedIndex, m_editedIndex);
        }
      }
      else{
        if(iRowIndex != -1){
          Personal personal = (Personal)m_list.getList().getSelectedValue();
          setSelectedObject(personal);
          m_editedIndex = iRowIndex;
        }
        else
          objectViewNoSelection();

      }
    }
  }
}