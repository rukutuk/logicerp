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
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import pohaci.gumunda.cgui.*;

public class SpecificAddressDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  SpecificAddressTable m_table;
  JButton m_okBt, m_cancelBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  SpecificAddress m_address = null;
  boolean m_editable = true;

  public SpecificAddressDlg(JFrame owner) {
    super(owner, "Specific Address", true);
    setSize(350, 350);

    m_mainframe = owner;
    constructComponent();
  }

  public SpecificAddressDlg(JFrame owner, SpecificAddress address, boolean editable) {
    super(owner, "Specific Address", true);
    setSize(350, 350);

    m_mainframe = owner;
    m_address = address;
    m_editable = editable;
    constructComponent();
    initData();
  }

  void constructComponent() {
    m_table = new SpecificAddressTable();
    m_okBt = new JButton("Save");
    m_okBt.addActionListener(this);
    m_cancelBt = new JButton("Cancel");
    m_cancelBt.addActionListener(this);

    JPanel centerPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(m_okBt);
    buttonPanel.add(m_cancelBt);

    centerPanel.setLayout(new BorderLayout());
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
    centerPanel.add(new JScrollPane(m_table), BorderLayout.CENTER);
    centerPanel.add(buttonPanel, BorderLayout.SOUTH);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(centerPanel, BorderLayout.CENTER);
  }

  void initData() {
    m_table.setSpecificAddress(m_address);
  }

  public void setVisible( boolean flag ){
    Rectangle rc = m_mainframe.getBounds();
    Rectangle rcthis = getBounds();
    setBounds((int)(rc.getWidth() - rcthis.getWidth())/2 + rc.x,
              (int)(rc.getHeight() - rcthis.getHeight())/2 + rc.y,
              (int)rcthis.getWidth(), (int)rcthis.getHeight());

    super.setVisible(flag);
  }

  public int getResponse() {
    return m_iResponse;
  }

  public SpecificAddress getSpecificAddress() {
    return m_address;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_okBt) {
      try {
        m_address = m_table.getSpecificAddress();
      }
      catch(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(),
                                      "Informasi", JOptionPane.INFORMATION_MESSAGE);
        return;
      }

      m_iResponse = JOptionPane.OK_OPTION;
      dispose();
    }
    else if(e.getSource() == m_cancelBt) {
      dispose();
    }
  }

  /**
   *
   */
  class SpecificAddressTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpecificAddressTable() {
      SpecificAddressTableModel model = new SpecificAddressTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");

      model.addRow(new Object[]{"Type", ""});
      model.addRow(new Object[]{"Address", ""});
      model.addRow(new Object[]{"City", ""});
      model.addRow(new Object[]{"Post Code", ""});
      model.addRow(new Object[]{"Province", ""});
      model.addRow(new Object[]{"Country", ""});
      model.addRow(new Object[]{"Phone1", ""});
      model.addRow(new Object[]{"Phone2", ""});
      model.addRow(new Object[]{"Fax1", ""});
      model.addRow(new Object[]{"Fax2", ""});
      model.addRow(new Object[]{"Email", ""});

      setModel(model);
      getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
      getColumnModel().getColumn(0).setPreferredWidth(100);
      getColumnModel().getColumn(0).setMaxWidth(100);
    }

    public TableCellEditor getCellEditor(int row, int col) {
      if(col == 1)
        return new BaseTableCellEditor();
      return super.getCellEditor(row, col);
    }

    public void stopCellEditing() {
      TableCellEditor editor;
      if((editor = getCellEditor()) != null)
        editor.stopCellEditing();
    }

    public SpecificAddress getSpecificAddress() throws Exception {
      String type = "", address = "", city = "";
      int postcode = 0;
      String province = "", country = "", phone1 = "",
      phone2 = "", fax1 = "", fax2 = "", email = "";

      stopCellEditing();
      int row = 0;

      type = (String)getValueAt(row++, 1);
      if(type == null || type.equals(""))
        throw new Exception("Type have to fill");
      address = (String)getValueAt(row++, 1);

      city = (String)getValueAt(row++, 1);

      try {
        String strpostcode = (String)getValueAt(row++, 1);
        if(!(strpostcode).equals(""))
          postcode = Integer.parseInt(strpostcode);
      }
      catch(Exception ex) {
        throw new Exception("Post code have to fill in numeric");
      }

      province = (String)getValueAt(row++, 1);
      country = (String)getValueAt(row++, 1);

      phone1 = (String)getValueAt(row++, 1);
      phone2 = (String)getValueAt(row++, 1);
      fax1 = (String)getValueAt(row++, 1);
      fax2 = (String)getValueAt(row++, 1);
      email = (String)getValueAt(row++, 1);

      return new SpecificAddress(type, address, city, postcode,
                                 province, country, phone1, phone2, fax1, fax2, email);
    }

    public void setSpecificAddress(SpecificAddress address) {
      System.out.println(address);
      int row = 0;
      setValueAt(address.getType(), row++, 1);
      setValueAt(address.getAddress(), row++, 1);
      setValueAt(address.getCity(), row++, 1);
      if(address.getPostCode() > 0)
        setValueAt(String.valueOf(address.getPostCode()), row++, 1);
      else
        setValueAt("", row++, 1);
      setValueAt(address.getProvince(), row++, 1);
      setValueAt(address.getCountry(), row++, 1);

      setValueAt(address.getPhone1(), row++, 1);
      setValueAt(address.getPhone2(), row++, 1);
      setValueAt(address.getFax1(), row++, 1);
      setValueAt(address.getFax2(), row++, 1);
      setValueAt(address.getEmail(), row++, 1);
    }
  }

  /**
   *
   */
  class SpecificAddressTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;

      if(m_editable)
        return true;
      return false;
    }
  }
}