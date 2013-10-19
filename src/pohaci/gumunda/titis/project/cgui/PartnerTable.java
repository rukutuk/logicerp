package pohaci.gumunda.titis.project.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;

public class PartnerTable extends JTable  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

boolean m_editable = false;

  Connection m_conn = null;
  long m_sessionid = -1;
  PartnerCompanyGroupCellEditor m_editor = null;
  Partner m_partner = null;

  public PartnerTable(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    SuuplierTableModel model = new SuuplierTableModel();
    model.addColumn("Attribute");
    model.addColumn("Description");
    model.addRow(new Object[]{"Code", ""});
    model.addRow(new Object[]{"Name", ""});
    model.addRow(new Object[]{"Address", ""});
    model.addRow(new Object[]{"City", ""});
    model.addRow(new Object[]{"Post Code", ""});
    model.addRow(new Object[]{"Province", ""});
    model.addRow(new Object[]{"Country", ""});
    model.addRow(new Object[]{"Phone 1", ""});
    model.addRow(new Object[]{"Phone 2", ""});
    model.addRow(new Object[]{"Fax 1", ""});
    model.addRow(new Object[]{"Fax 2", ""});
    model.addRow(new Object[]{"Email", ""});
    model.addRow(new Object[]{"Website", ""});
    model.addRow(new Object[]{"Partner Group", ""});
    setModel(model);

    getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    getColumnModel().getColumn(0).setPreferredWidth(150);
    getColumnModel().getColumn(0).setMaxWidth(150);
  }

  public TableCellEditor getCellEditor(int row, int col) {
    if(row == 13 && col == 1)
      return m_editor =  new PartnerCompanyGroupCellEditor(
        pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
        m_conn, m_sessionid);;
    return new BaseTableCellEditor();
  }

  public void stopCellEditing() {
    TableCellEditor editor;
    if((editor = getCellEditor()) != null)
      editor.stopCellEditing();
  }

  public void clearTable() {
    m_editor = null;
    int count = getRowCount();
    for(int i = 0; i < count; i ++)
      setValueAt("", i, 1);
  }

  public void setPartner(Partner partner) {
    m_partner = partner;
    m_editor = null;
    int row = 0;
    setValueAt(partner.getCode(), row++, 1);
    setValueAt(partner.getName(), row++, 1);
    setValueAt(partner.getAddress(), row++, 1);
    setValueAt(partner.getCity(), row++, 1);
    if(partner.getPostCode() > 0)
      setValueAt(String.valueOf(partner.getPostCode()), row++, 1);
    else
      setValueAt("", row++, 1);
    setValueAt(partner.getProvince(), row++, 1);
    setValueAt(partner.getCountry(), row++, 1);
    setValueAt(partner.getPhone1(), row++, 1);
    setValueAt(partner.getPhone2(), row++, 1);
    setValueAt(partner.getFax1(), row++, 1);
    setValueAt(partner.getFax2(), row++, 1);
    setValueAt(partner.getEmail(), row++, 1);
    setValueAt(partner.getWebsite(), row++, 1);
    setValueAt(partner.getCompanyToString(), row++, 1);
  }

  public Partner getPartner() throws Exception {
    stopCellEditing();
    int row = 0;
    String code = "", name = "", address = "", city = "";
    int postcode = 0;
    String province = "", country = "", phone1 = "",
    phone2 = "", fax1 = "", fax2 = "", email = "", website = "";

    code = (String)getValueAt(row++, 1);
    if(code == null || code.equals(""))
      throw new Exception("Code of partner have to fill");
    name = (String)getValueAt(row++, 1);
    if(name == null || name.equals(""))
      throw new Exception("Name of partner have to fill");
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
    website = (String)getValueAt(row++, 1);

    CompanyGroup[] group = new CompanyGroup[0];
    if(m_editor != null)
     group = m_editor.getCompanyGroup();
    else if(m_partner != null)
      group = m_partner.getCompanyGroup();

    Partner partner = new Partner(code, name, address, city, postcode, province, country,
                        phone1, phone2, fax1, fax2, email, website);
    partner.setCompanyGroup(group);
    return partner;
  }

  /**
   *
   */
  class SuuplierTableModel extends DefaultTableModel {
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