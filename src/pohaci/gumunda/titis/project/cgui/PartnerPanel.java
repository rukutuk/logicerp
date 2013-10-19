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
import pohaci.gumunda.titis.application.*;

public class PartnerPanel extends JPanel implements ActionListener, ListSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
PartnerListPanel m_list;
  PartnerTable m_table;
  SpecificAddressPanel m_addresspanel;
  PersonalContactPanel m_contactpanel;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;

  Partner m_partner = null;

  public PartnerPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {
    System.out.println("partner komponent");
    // deklarasikan splitpane 
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JTabbedPane tabPane = new JTabbedPane();
    JPanel listPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel button1Panel = new JPanel();
    JPanel button2Panel = new JPanel();

    m_list = new PartnerListPanel(m_conn, m_sessionid);
    m_list.getList().addListSelectionListener(this);
    m_table = new PartnerTable(m_conn, m_sessionid);

    m_addresspanel = new SpecificAddressPanel();
    m_contactpanel = new PersonalContactPanel(m_conn, m_sessionid);

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

    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(false);

    listPanel.setLayout(new BorderLayout());
    listPanel.add(m_list, BorderLayout.CENTER);
    listPanel.add(button1Panel, BorderLayout.SOUTH);

    tabPane.setBackground(Color.white);
    tabPane.setForeground(Color.blue.darker().darker());
    tabPane.addTab("Profile", new JScrollPane(m_table));
    tabPane.addTab("Specific Address", m_addresspanel);
    tabPane.addTab("Contact", m_contactpanel);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(tabPane, BorderLayout.CENTER);
    centerPanel.add(button2Panel, BorderLayout.SOUTH);

    splitPane.setLeftComponent(listPanel);
    splitPane.setRightComponent(centerPanel);
    splitPane.setDividerLocation(200);

    setLayout(new BorderLayout());
    add(splitPane, BorderLayout.CENTER);
  }

  void onAdd() {
    m_new = true;
    m_table.m_editable = true;
    m_table.clearTable();
    m_addresspanel.setEnabled(true);
    m_addresspanel.clear();
    m_contactpanel.setEditable(true);
    m_contactpanel.clear();

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onEdit() {
    m_edit = true;
    m_table.m_editable = true;
    m_addresspanel.setEnabled(true);
    m_contactpanel.setEditable(true);

    m_addBt.setEnabled(false);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(true);
    m_cancelBt.setEnabled(true);
    m_deleteBt.setEnabled(false);
  }

  void onSave() {
    Partner partner = null;
    //SpecificAddress[] address = new SpecificAddress[0];

    try {
      partner = m_table.getPartner();
      partner.setSpecificAddress(m_addresspanel.getSpecificAddress());
      partner.setPersonal(m_contactpanel.getPersonalContact());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if(m_new) {
      try {
        m_new = false;
        createPartner(partner);
      }
      catch(Exception ex) {
        m_new = true;
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                      JOptionPane.WARNING_MESSAGE);
      }
    }
    else {
      try {
        updatePartner(new Partner(m_partner.getIndex(), partner));
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

    if(m_partner != null)
      setSelectedObject(m_partner);
    else
      objectViewNoSelection();
  }

  void onDelete() {
    deletePartner();
  }

  void setSelectedObject(Partner partner) {
    m_partner = partner;
    setPartnerGroup(m_partner);
    m_table.setPartner(m_partner);
    setPartnerAddress(m_partner);
    setPartnerContact(m_partner);

    m_new = false;
    m_edit = false;
    m_table.m_editable = false;
    m_addresspanel.setEnabled(false);
    m_contactpanel.setEditable(false);

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
    m_addresspanel.setEnabled(false);
    m_contactpanel.setEditable(false);

    m_table.clearTable();
    m_addresspanel.clear();
    m_contactpanel.clear();
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_deleteBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
  }

  void createPartner(Partner partner) throws Exception {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
    Partner object = logic.createPartner(m_sessionid, IDBConstants.MODUL_MASTER_DATA, partner);

    model.addElement(object);
    m_list.getList().setSelectedValue(object, true);
  }

  void updatePartner(Partner partner) throws Exception {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
    Partner object = logic.updatePartner(m_sessionid, IDBConstants.MODUL_MASTER_DATA, partner.getIndex(), partner);

    model.setElementAt(object, m_editedIndex);
    setSelectedObject(object);
  }

  void deletePartner() {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    Partner partner = (Partner)m_list.getList().getSelectedValue();

    if(!Misc.getConfirmation())
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deletePartner(m_sessionid, IDBConstants.MODUL_MASTER_DATA, partner.getIndex());
      model.removeElementAt(m_editedIndex);
      m_partner = null;
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void setPartnerGroup(Partner partner) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      CompanyGroup[] group = logic.getPartnerGroup(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, partner.getIndex());
      partner.setCompanyGroup(group);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setPartnerAddress(Partner partner) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_addresspanel.setSpecificAddress(logic.getPartnerAddress(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, partner.getIndex()));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setPartnerContact(Partner partner) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_contactpanel.setPersonalContact(logic.getPartnerContact(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, partner.getIndex()));
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
          Partner partner = (Partner)m_list.getList().getSelectedValue();
          setSelectedObject(partner);
          m_editedIndex = iRowIndex;
        }
        else
          objectViewNoSelection();

      }
    }
  }
}