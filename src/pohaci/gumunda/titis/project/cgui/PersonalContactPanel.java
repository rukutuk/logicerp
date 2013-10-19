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
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.titis.application.Misc;

public class PersonalContactPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JButton m_searchBt, m_deleteBt;
  ContactTable m_table;
  boolean m_editable = false;

  Connection m_conn = null;
  long m_sessionid = -1;

  public PersonalContactPanel(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    constructComponent();
    setEditable(false);
  }

  void constructComponent() {
    // set button
    m_searchBt = new JButton("Add");
    m_searchBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    m_table = new ContactTable();
    
    // set buttonpanel untuk panel button
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_searchBt);
    buttonPanel.add(m_deleteBt);

    // set buttonpanel dan mtable pada pada 
    setEditable(false);
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(3, 10, 10, 10));
    add(buttonPanel, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }

  public void setEditable(boolean editable) {
    m_editable = editable;
    m_searchBt.setEnabled(editable);
    m_deleteBt.setEnabled(editable);
  }

  public void clear() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.setRowCount(0);
  }

  void onSearch() {
    SearchPersonalDetailDlg dlg = new SearchPersonalDetailDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION){
      Personal[] personal = dlg.getPersonal();
      m_table.addPersonalContact(personal);
    }
  }

  public void setPersonalContact(Personal[] personal) {
    m_table.setPersonalContact(personal);
  }

  public Personal[] getPersonalContact() {
    return m_table.getPersonalContact();
  }

  void onDelete() {
    int row = m_table.getSelectedRow();
    if(row == -1)
      return;
    if(!Misc.getConfirmation())
      return;

    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.removeRow(row);
    m_table.updateNumber(0);
  }

  void business() {
    Personal personal = (Personal)m_table.getValueAt(m_table.getSelectedRow(), 2);
    PersonalBusinessListDlg dlg = new PersonalBusinessListDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid, personal);
    dlg.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_searchBt) {
      onSearch();
    }
    else if(e.getSource() == m_deleteBt) {
      onDelete();
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

      addMouseListener(new MouseAdapter() {
       public void mouseClicked( MouseEvent e ) {
         if(e.getClickCount() >= 2) {
           business();
         }
       }
      });

    }

    public void addPersonalContact(Personal[] personal) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      for(int i = 0; i < personal.length; i ++) {
        String postcode = "";
        if(personal[i].getPostCode() > 0)
          postcode = String.valueOf(personal[i].getPostCode());

        model.insertRow(getRowCount(), new Object[]{
          String.valueOf(getRowCount() + 1), personal[i].getCode(), personal[i],
          personal[i].getAddress(), personal[i].getCity(),
          postcode,
          personal[i].getPhone1(),
          personal[i].getFax1(),
          personal[i].getEmail(),
          personal[i].getWebSite()
        });
      }      
    }

    public void setPersonalContact(Personal[] personal) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);
      for(int i = 0; i < personal.length; i ++) {
        OtherPersonal other = new OtherPersonal(personal[i].getIndex(), personal[i]);
        String postcode = "";
        if(other.getPostCode() > 0)
          postcode = String.valueOf(other.getPostCode());

        model.insertRow(getRowCount(), new Object[]{
          String.valueOf(i + 1), other.getCode(), other,
          other.getAddress(), other.getCity(),
          postcode,
          other.getPhone1(),
          other.getFax1(),
          other.getEmail(),
          other.getWebSite()
        });
      }      
    }

    public Personal[] getPersonalContact() {
      Vector vresult = new Vector();
      int row = getRowCount();
      for(int i = 0; i < row; i ++) {
        vresult.addElement((Personal)getValueAt(i, 2));
      }

      Personal[] contact = new Personal[vresult.size()];
      vresult.copyInto(contact);
      return contact;
    }
    
    void updateNumber(int col) {
        for(int i = 0; i < getRowCount(); i ++)
            setValueAt(String.valueOf(i + 1), i, col); 
    }
    
  }

  /**
   *
   */
  class ContactTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
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