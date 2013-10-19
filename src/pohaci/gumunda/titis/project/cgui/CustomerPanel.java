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

public class CustomerPanel extends JPanel implements ActionListener, ListSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
CustomerListPanel m_list;
  CustomerTable m_table;
  SpecificAddressPanel m_addresspanel;
  PersonalContactPanel m_contactpanel;
  JButton m_addBt, m_editBt, m_saveBt, m_cancelBt, m_deleteBt;

  Connection m_conn = null;
  long m_sessionid = -1;
  boolean m_new = false, m_edit = false;
  int m_editedIndex = -1;

  Customer m_customer = null;

  public CustomerPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
  }

  void constructComponent() {
    System.out.println("component customer");
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JPanel listPanel = new JPanel();
    JTabbedPane tabPane = new JTabbedPane();
    JPanel centerPanel = new JPanel();
    JPanel button1Panel = new JPanel();
    JPanel button2Panel = new JPanel();    
    
    // get list customer
    m_list = new CustomerListPanel(m_conn, m_sessionid);           
    m_list.getList().addListSelectionListener(this);
    
    // set table customer
    m_table = new CustomerTable(m_conn, m_sessionid);
    
    // set table specific address
    m_addresspanel = new SpecificAddressPanel();
    m_contactpanel = new PersonalContactPanel(m_conn, m_sessionid);

    // set button
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

    // set enable/disable button
    m_addBt.setEnabled(true);
    m_editBt.setEnabled(false);
    m_saveBt.setEnabled(false);
    m_cancelBt.setEnabled(false);
    m_deleteBt.setEnabled(false);

    
    listPanel.setLayout(new BorderLayout());
    listPanel.add(m_list, BorderLayout.CENTER);
    listPanel.add(button1Panel, BorderLayout.SOUTH);

    // set tab
    tabPane.setBackground(Color.white);
    tabPane.setForeground(Color.blue.darker().darker());
    tabPane.addTab("Profile", new JScrollPane(m_table));
    tabPane.addTab("Specific Address", m_addresspanel);
    tabPane.addTab("Contact", m_contactpanel);

    // set tabpane dan button2panel ke centerpanel
    centerPanel.setLayout(new BorderLayout());
    centerPanel.add(tabPane, BorderLayout.CENTER);
    centerPanel.add(button2Panel, BorderLayout.SOUTH);

    // set listpanel pada splitpane kiri
    splitPane.setLeftComponent(listPanel);
    // set centerpanel pada splitpanel kanan
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
    Customer customer = null;

    try {
      customer = m_table.getCustomer();
      customer.setSpecificAddress(m_addresspanel.getSpecificAddress());
      customer.setPersonal(m_contactpanel.getPersonalContact());
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if(m_new) {
      try {
        m_new = false;
        createCustomer(customer);
      }
      catch(Exception ex) {
        m_new = true;
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                      JOptionPane.WARNING_MESSAGE);
      }
    }
    else {
      try {
        updateCustomer(new Customer(m_customer.getIndex(), customer));
      }
      catch(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                      JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  void onCancel() {
    m_table.stopCellEditing();

    if(!Misc.getConfirmation())
      return;

    if(m_customer != null)
      setSelectedObject(m_customer);
    else
      objectViewNoSelection();
  }

  void onDelete() {
    deleteCustomer();
  }

  void setSelectedObject(Customer customer) {
    m_customer = customer;
    setCustomerGroup(m_customer);
    m_table.setCustomer(m_customer);
    setCustomerAddress(customer);
    this.setCustomerContact(customer);

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

  void createCustomer(Customer customer) throws Exception {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
    Customer object = logic.createCustomer(m_sessionid, IDBConstants.MODUL_MASTER_DATA, customer);

    model.addElement(object);
    m_list.getList().setSelectedValue(object, true);
  }

  void updateCustomer(Customer customer) throws Exception {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
    Customer object = logic.updateCustomer(m_sessionid, IDBConstants.MODUL_MASTER_DATA, customer.getIndex(), customer);

    model.setElementAt(object, m_editedIndex);
    setSelectedObject(object);
  }

  void deleteCustomer() {
    DefaultListModel model = (DefaultListModel)m_list.getList().getModel();
    Customer customer = (Customer)m_list.getList().getSelectedValue();

    if(!Misc.getConfirmation())
      return;

    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      logic.deleteCustomer(m_sessionid, IDBConstants.MODUL_MASTER_DATA, customer.getIndex());
      model.removeElementAt(m_editedIndex);
      m_customer = null;
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning",
                                    JOptionPane.WARNING_MESSAGE);
    }
  }

  void setCustomerGroup(Customer customer) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      CompanyGroup[] group = logic.getCustomerGroup(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, customer.getIndex());

      customer.setCompanyGroup(group);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setCustomerAddress(Customer customer) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_addresspanel.setSpecificAddress(logic.getCustomerAddress(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, customer.getIndex()));
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  void setCustomerContact(Customer customer) {
    try {
      ProjectBusinessLogic logic = new ProjectBusinessLogic(m_conn);
      m_contactpanel.setPersonalContact(logic.getCustomerContact(m_sessionid,
          IDBConstants.MODUL_MASTER_DATA, customer.getIndex()));
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
          Customer customer = (Customer)m_list.getList().getSelectedValue();
          setSelectedObject(customer);
          m_editedIndex = iRowIndex;
        }
        else
          objectViewNoSelection();

      }
    }
  }
}