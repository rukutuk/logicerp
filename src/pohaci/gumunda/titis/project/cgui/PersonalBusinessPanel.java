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
import javax.swing.*;
import javax.swing.table.*;

public class PersonalBusinessPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JButton m_addBt, m_editBt, m_deleteBt;
  BusinessTable m_table;
  boolean m_editable = false;

  public PersonalBusinessPanel() {
    constructComponent();
  }

  void constructComponent() {
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    m_table = new BusinessTable();

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);

    setEnabled(false);
    setLayout(new BorderLayout());
    add(buttonPanel, BorderLayout.NORTH);
    add(new JScrollPane(m_table), BorderLayout.CENTER);
  }

  public void setEnabled(boolean editable) {
    m_editable = editable;
    m_addBt.setEnabled(editable);
    m_editBt.setEnabled(editable);
    m_deleteBt.setEnabled(editable);
  }

  public void clear() {
    DefaultTableModel model = (DefaultTableModel)m_table.getModel();
    model.setRowCount(0);
  }

  void onAdd() {
    PersonalBusinessDlg dlg = new PersonalBusinessDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      PersonalBusiness business = dlg.getPersonalBusiness();
      m_table.setPersonalBusiness(business);
    }
  }

  void onEdit() {
    int row = m_table.getSelectedRow();
    if(row == -1)
      return;
    PersonalBusiness business = (PersonalBusiness)m_table.getValueAt(row, 0);
    PersonalBusinessDlg dlg = new PersonalBusinessDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), business, m_editable);
    dlg.setVisible(true);

    if(m_editable && dlg.getResponse() == JOptionPane.OK_OPTION) {
      business = dlg.getPersonalBusiness();
      m_table.setPersonalBusiness(business, row);
    }
  }

  void onDelete() {
    int row = m_table.getSelectedRow();
    if(row != -1) {
      Object[] options = {"Yes", "No"};
      if(JOptionPane.showOptionDialog(this,
                                      "Are you sure ? ","Confirmation",
                                      JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])
                                      == JOptionPane.NO_OPTION)
        return;
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      model.removeRow(row);
    }
  }

  public void setPersonalBusiness(PersonalBusiness[] business) {
    m_table.setPersonalBusiness(business);
  }

  public PersonalBusiness[] getPersonalBusiness() {
    return m_table.getPersonalBusiness();
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
  }

  /**
   *
   */
  class BusinessTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessTable() {
      BusinessTableModel model = new BusinessTableModel();
      model.addColumn("Company");
      model.addColumn("Address");
      model.addColumn("City");
      model.addColumn("Province");
      model.addColumn("Post Code");
      model.addColumn("Country");
      model.addColumn("Website");
      model.addColumn("Job Title");
      model.addColumn("Department");
      model.addColumn("Office");
      model.addColumn("Phone");
      model.addColumn("Fax");
      setModel(model);

      addMouseListener(new MouseAdapter() {
        public void mouseClicked( MouseEvent e ) {
          if(e.getClickCount() >= 2) {
            onEdit();
          }
        }
      });
    }

    public void setPersonalBusiness(PersonalBusiness business) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      String postcode = "";
      if(business.getPostCode() > 0)
        postcode = String.valueOf(business.getPostCode());

      String phone = "";
      if(!business.getPhone1().equals(""))
        phone += business.getPhone1();

      if(!business.getPhone2().equals("")) {
        if(phone.equals(""))
          phone += business.getPhone2();
        else
          phone += ", " + business.getPhone2();
      }

      model.addRow(new Object[]{
        business, business.getAddress(), business.getCity(), business.getProvince(),
        postcode, business.getCountry(), business.getWebsie(),
        business.getJobTitle(), business.getDepartement(), business.getOffice(),
        phone, business.getFax()
      });
    }

    public void setPersonalBusiness(PersonalBusiness business, int row) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      String postcode = "";
      if(business.getPostCode() > 0)
        postcode = String.valueOf(business.getPostCode());

      String phone = "";
      if(!business.getPhone1().equals(""))
        phone += business.getPhone1();

      if(!business.getPhone2().equals("")) {
        if(phone.equals(""))
          phone += business.getPhone2();
        else
          phone += ", " + business.getPhone2();
      }

      model.removeRow(row);
      model.insertRow(row, new Object[]{
        business, business.getAddress(), business.getCity(), business.getProvince(),
        postcode, business.getCountry(), business.getWebsie(),
        business.getJobTitle(), business.getDepartement(), business.getOffice(),
        phone, business.getFax()
      });
    }

    public void setPersonalBusiness(PersonalBusiness[] business) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < business.length; i ++) {
        String postcode = "";
        if(business[i].getPostCode() > 0)
          postcode = String.valueOf(business[i].getPostCode());

        String phone = "";
        if(!business[i].getPhone1().equals(""))
          phone += business[i].getPhone1();

        if(!business[i].getPhone2().equals("")) {
          if(phone.equals(""))
            phone += business[i].getPhone2();
          else
            phone += ", " + business[i].getPhone2();
        }

        model.addRow(new Object[]{
          business[i], business[i].getAddress(), business[i].getCity(), business[i].getProvince(),
          postcode, business[i].getCountry(), business[i].getWebsie(),
          business[i].getJobTitle(), business[i].getDepartement(), business[i].getOffice(),
          phone, business[i].getFax()
        });
      }
    }

    public PersonalBusiness[] getPersonalBusiness() {
      java.util.Vector vresult = new java.util.Vector();
      int row = getRowCount();

      for(int i = 0; i < row; i ++)
        vresult.addElement((PersonalBusiness)getValueAt(i, 0));

      PersonalBusiness[] business = new PersonalBusiness[vresult.size()];
      vresult.copyInto(business);
      return business;
    }
  }

  /**
   *
   */
  class BusinessTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}