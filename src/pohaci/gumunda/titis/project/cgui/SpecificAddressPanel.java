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

public class SpecificAddressPanel extends JPanel implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JButton m_addBt, m_editBt, m_deleteBt;
  AddressTable m_table;
  boolean m_editable = false;

  public SpecificAddressPanel() {
    constructComponent();
  }

  void constructComponent() {    
    // set buttom
    m_addBt = new JButton("Add");
    m_addBt.addActionListener(this);
    m_editBt = new JButton("Edit");
    m_editBt.addActionListener(this);
    m_deleteBt = new JButton("Delete");
    m_deleteBt.addActionListener(this);
    
    // set specific tabel
    m_table = new AddressTable();    
    
    // set buttonpanel buat batel button
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_addBt);
    buttonPanel.add(m_editBt);
    buttonPanel.add(m_deleteBt);
    
    // 
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
    SpecificAddressDlg dlg = new SpecificAddressDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
    dlg.setVisible(true);

    if(dlg.getResponse() == JOptionPane.OK_OPTION) {
      SpecificAddress address = dlg.getSpecificAddress();
      m_table.setSpecificAddress(address);
    }
  }

  void onEdit() {
    int row = m_table.getSelectedRow();
    if(row == -1)
      return;
    SpecificAddress address = (SpecificAddress)m_table.getValueAt(row, 1);
    SpecificAddressDlg dlg = new SpecificAddressDlg(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(), address, m_editable);
    dlg.setVisible(true);

    if(m_editable && dlg.getResponse() == JOptionPane.OK_OPTION) {
      address = dlg.getSpecificAddress();
      m_table.setSpecificAddress(address, row);
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

        for(int i = 0; i < model.getRowCount(); i ++)
          model.setValueAt(String.valueOf(i + 1), i, 0);
      }
  }

  public void setSpecificAddress(SpecificAddress[] address) {
    m_table.setSpecificAddress(address);
  }

  public SpecificAddress[] getSpecificAddress() {
    return m_table.getSpecificAddress();
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
  class AddressTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddressTable() {
      AddressTableModel model = new AddressTableModel();
      model.addColumn("No");
      model.addColumn("Type");
      model.addColumn("Address");
      model.addColumn("Phone");
      model.addColumn("Fax");
      model.addColumn("City");
      model.addColumn("Post Code");
      model.addColumn("Province");
      model.addColumn("Country");
      setModel(model);

      addMouseListener(new MouseAdapter() {
        public void mouseClicked( MouseEvent e ) {
          if(e.getClickCount() >= 2) {
            onEdit();
          }
        }
      });
    }

    public void setSpecificAddress(SpecificAddress address) {
      DefaultTableModel model = (DefaultTableModel)m_table.getModel();
      String postcode = "";
      if(address.getPostCode() > 0)
        postcode = String.valueOf(address.getPostCode());
      model.addRow(new Object[]{
        String.valueOf(getRowCount() + 1),
        address, address.getAddress(), address.getPhone1(), address.getFax1(),
        address.getCity(), postcode, address.getProvince(), address.getCountry()
      });
    }

    public void setSpecificAddress(SpecificAddress address, int row) {
      String postcode = "";
      if(address.getPostCode() > 0)
        postcode = String.valueOf(address.getPostCode());
      setValueAt(String.valueOf(row + 1), row, 0);
      setValueAt(address, row, 1);
      setValueAt(address.getAddress(), row, 2);
      setValueAt(address.getPhone1(), row, 3);
      setValueAt(address.getFax1(), row, 4);
      setValueAt(address.getCity(), row, 5);
      setValueAt(postcode, row, 6);
      setValueAt(address.getProvince(), row, 7);
      setValueAt(address.getCountry(), row, 8);
    }

    public void setSpecificAddress(SpecificAddress[] address) {
      DefaultTableModel model = (DefaultTableModel)getModel();
      model.setRowCount(0);

      for(int i = 0; i < address.length; i ++) {
        String postcode = "";
        if(address[i].getPostCode() > 0)
          postcode = String.valueOf(address[i].getPostCode());
        model.addRow(new Object[]{
        String.valueOf(getRowCount() + 1),
        address[i], address[i].getAddress(), address[i].getPhone1(), address[i].getFax1(),
        address[i].getCity(), postcode, address[i].getProvince(), address[i].getCountry()
      });
      }
    }

    public SpecificAddress[] getSpecificAddress() {
      java.util.Vector vresult = new java.util.Vector();
      int row = getRowCount();

      for(int i = 0; i < row; i ++)
        vresult.addElement((SpecificAddress)getValueAt(i, 1));

      SpecificAddress[] address = new SpecificAddress[vresult.size()];
      vresult.copyInto(address);
      return address;
    }
  }

  /**
   *
   */
  class AddressTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}