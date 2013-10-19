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

public class PersonalBusinessDlg extends JDialog implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JFrame m_mainframe;
  HomeTable m_table;
  JButton m_okBt, m_cancelBt;

  int m_iResponse = JOptionPane.NO_OPTION;
  PersonalBusiness m_business = null;
  boolean m_editable = true;

  public PersonalBusinessDlg(JFrame owner) {
    super(owner, "Business", true);
    setSize(350, 350);

    m_mainframe = owner;
    constructComponent();
  }

  public PersonalBusinessDlg(JFrame owner, PersonalBusiness business, boolean editable) {
    super(owner, "Business", true);
    setSize(350, 350);

    m_mainframe = owner;
    m_business = business;
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
    m_table.setPersonalBusiness(m_business);
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

  public PersonalBusiness getPersonalBusiness() {
    return m_business;
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == m_okBt) {
      try {
        m_business = m_table.getPersonalBusiness();
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

      model.addRow(new Object[]{"Company", ""});
      model.addRow(new Object[]{"Address", ""});
      model.addRow(new Object[]{"City", ""});
      model.addRow(new Object[]{"Post Code", ""});
      model.addRow(new Object[]{"Province", ""});
      model.addRow(new Object[]{"Country", ""});
      model.addRow(new Object[]{"Website", ""});
      model.addRow(new Object[]{"Phone1", ""});
      model.addRow(new Object[]{"Phone2", ""});
      model.addRow(new Object[]{"Fax", ""});
      model.addRow(new Object[]{"Job Title", ""});
      model.addRow(new Object[]{"Department", ""});
      model.addRow(new Object[]{"Office", ""});

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

    public PersonalBusiness getPersonalBusiness() throws Exception {
      String company, address = "", city = "";
      int postcode = 0;
      String province = "", country = "", website = "", phone1 = "",
      phone2 = "", fax = "",  jobtitle = "", departement = "",  office = "";

      stopCellEditing();
      int row = 0;

      company = (String)getValueAt(row++, 1);
      if(company == null || company.equals(""))
        throw new Exception("Company have to fill");

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
      fax = (String)getValueAt(row++, 1);

      jobtitle = (String)getValueAt(row++, 1);
      if(jobtitle == null || jobtitle.equals(""))
        throw new Exception("Job Title have to fill");

      departement = (String)getValueAt(row++, 1);
      if(departement == null || departement.equals(""))
        throw new Exception("Departement have to fill");

      office = (String)getValueAt(row++, 1);


      return new PersonalBusiness(company, address, city, postcode, province, country,
                             website, phone1, phone2, fax,  jobtitle, departement, office);
    }

    public void setPersonalBusiness(PersonalBusiness business) {
      int row = 0;
      setValueAt(business.getCompany(), row++, 1);
      setValueAt(business.getAddress(), row++, 1);
      setValueAt(business.getCity(), row++, 1);
      if(business.getPostCode() > 0)
        setValueAt(String.valueOf(business.getPostCode()), row++, 1);
      else
        setValueAt("", row++, 1);
      setValueAt(business.getProvince(), row++, 1);
      setValueAt(business.getCountry(), row++, 1);
      setValueAt(business.getWebsie(), row++, 1);
      setValueAt(business.getPhone1(), row++, 1);
      setValueAt(business.getPhone2(), row++, 1);
      setValueAt(business.getFax(), row++, 1);
      setValueAt(business.getJobTitle(), row++, 1);
      setValueAt(business.getDepartement(), row++, 1);
      setValueAt(business.getOffice(), row++, 1);
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