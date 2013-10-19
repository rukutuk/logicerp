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

public class PersonalHomeDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  HomeTable m_table;
  JButton m_okBt, m_cancelBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  PersonalHome m_home = null;
  boolean m_editable = true;

  public PersonalHomeDlg(JFrame owner) {
    super(owner, "Home", true);
    setSize(350, 350);

    m_mainframe = owner;
    constructComponent();
  }

  public PersonalHomeDlg(JFrame owner, PersonalHome home, boolean editable) {
    super(owner, "Home", true);
    setSize(350, 350);

    m_mainframe = owner;
    m_home = home;
    m_editable = editable;
    constructComponent();
    initData();
  }

  void constructComponent() {
    m_table = new HomeTable();
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
    m_table.setPersonalHome(m_home);
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

  public PersonalHome getPersonalHome() {
    return m_home;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_okBt) {
      try {
        m_home = m_table.getPersonalHome();
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
  class HomeTable extends JTable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HomeTable() {
      HomeTableModel model = new HomeTableModel();
      model.addColumn("Attribute");
      model.addColumn("Description");

      model.addRow(new Object[]{"Address", ""});
      model.addRow(new Object[]{"City", ""});
      model.addRow(new Object[]{"Post Code", ""});
      model.addRow(new Object[]{"Province", ""});
      model.addRow(new Object[]{"Country", ""});
      model.addRow(new Object[]{"Website", ""});
      model.addRow(new Object[]{"Phone1", ""});
      model.addRow(new Object[]{"Phone2", ""});
      model.addRow(new Object[]{"Mobile1", ""});
      model.addRow(new Object[]{"Mobile2", ""});
      model.addRow(new Object[]{"Fax", ""});

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

    public PersonalHome getPersonalHome() throws Exception {
      String address = "", city = "";
      int postcode = 0;
      String province = "", country = "", website = "", phone1 = "",
      phone2 = "", mobile1 = "", mobile2 = "", fax = "";

      stopCellEditing();
      int row = 0;

      address = (String)getValueAt(row++, 1);
      if(address == null || address.equals(""))
        throw new Exception("Address have to fill");

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
      website = (String)getValueAt(row++, 1);
      phone1 = (String)getValueAt(row++, 1);
      phone2 = (String)getValueAt(row++, 1);
      mobile1 = (String)getValueAt(row++, 1);
      mobile2 = (String)getValueAt(row++, 1);
      fax = (String)getValueAt(row++, 1);

      return new PersonalHome(address, city, postcode, province, country,
                             website, phone1, phone2, mobile1, mobile2, fax);
    }

    public void setPersonalHome(PersonalHome home) {
      int row = 0;
      setValueAt(home.getAddress(), row++, 1);
      setValueAt(home.getCity(), row++, 1);
      if(home.getPostCode() > 0)
        setValueAt(String.valueOf(home.getPostCode()), row++, 1);
      else
        setValueAt("", row++, 1);
      setValueAt(home.getProvince(), row++, 1);
      setValueAt(home.getCountry(), row++, 1);
      setValueAt(home.getWebsie(), row++, 1);
      setValueAt(home.getPhone1(), row++, 1);
      setValueAt(home.getPhone2(), row++, 1);
      setValueAt(home.getMobile1(), row++, 1);
      setValueAt(home.getMobile2(), row++, 1);
      setValueAt(home.getFax(), row++, 1);
    }
  }

  /**
   *
   */
  class HomeTableModel extends DefaultTableModel {
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