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
import java.text.SimpleDateFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.DateCellEditor;

public class PersonalTable extends JTable  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

boolean m_editable = false;

  Connection m_conn = null;
  long m_sessionid = -1;
  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");

  public PersonalTable(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    PersonalTableModel model = new PersonalTableModel();
    model.addColumn("Attribute");
    model.addColumn("Description");
    model.addRow(new Object[]{"Code", ""});
    model.addRow(new Object[]{"Title", ""});
    model.addRow(new Object[]{"First Name", ""});
    model.addRow(new Object[]{"Last Name", ""});
    model.addRow(new Object[]{"Nick Name", ""});
    model.addRow(new Object[]{"Birth Place", ""});
    model.addRow(new Object[]{"Birth Date", ""});
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
    setModel(model);

    getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    getColumnModel().getColumn(0).setPreferredWidth(150);
    getColumnModel().getColumn(0).setMaxWidth(150);
  }

  public TableCellEditor getCellEditor(int row, int col) {
    if(row == 6 && col == 1)
      return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
    return new BaseTableCellEditor();
  }

  public void stopCellEditing() {
    TableCellEditor editor;
    if((editor = getCellEditor()) != null)
      editor.stopCellEditing();
  }

  public void clearTable() {
    int count = getRowCount();
    for(int i = 0; i < count; i ++)
      setValueAt("", i, 1);
  }

  public void setPersonal(Personal personal) {
    int row = 0;
    setValueAt(personal.getCode(), row++, 1);
    setValueAt(personal.getTitle(), row++, 1);
    setValueAt(personal.getFirstName(), row++, 1);
    setValueAt(personal.getLastName(), row++, 1);
    setValueAt(personal.getNickName(), row++, 1);
    setValueAt(personal.getBirthPlace(), row++, 1);

    if(personal.getBirthDate() != null)
      setValueAt(m_dateformat.format(personal.getBirthDate()), row++, 1);
    else
      setValueAt("", row++, 1);

    setValueAt(personal.getAddress(), row++, 1);
    setValueAt(personal.getCity(), row++, 1);
    if(personal.getPostCode() > 0)
      setValueAt(String.valueOf(personal.getPostCode()), row++, 1);
    else
      setValueAt("", row++, 1);
    setValueAt(personal.getProvince(), row++, 1);
    setValueAt(personal.getCountry(), row++, 1);
    setValueAt(personal.getPhone1(), row++, 1);
    setValueAt(personal.getPhone2(), row++, 1);
    setValueAt(personal.getFax1(), row++, 1);
    setValueAt(personal.getFax2(), row++, 1);
    setValueAt(personal.getEmail(), row++, 1);
    setValueAt(personal.getWebSite(), row++, 1);
  }

  public Personal getPersonal() throws Exception {
    stopCellEditing();

    int row = 0;
    String code = "", title = "", firstname = "", lastname = "", nickname = "", birthplace = "";
    java.util.Date birthdate = null;
    String address = "", city = "";
    int postcode = 0;
    String province = "", country = "", phone1 = "",
    phone2 = "", fax1 = "", fax2 = "", email = "", website = "";

    code = (String)getValueAt(row++, 1);
    if(code == null || code.equals(""))
      throw new Exception("Code of personal have to fill");

    title = (String)getValueAt(row++, 1);

    firstname = (String)getValueAt(row++, 1);
    if(firstname == null || firstname.equals(""))
      throw new Exception("First Name of personal have to fill");

    lastname = (String)getValueAt(row++, 1);
    nickname = (String)getValueAt(row++, 1);
    birthplace = (String)getValueAt(row++, 1);

    String strdate = (String)getValueAt(row++, 1);
    try {
      if(!strdate.equals("")) {
        birthdate = m_dateformat.parse(strdate);
      }
    }
    catch(Exception ex) {
      throw new Exception(ex.getMessage());
    }

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

    return new Personal(code, title, firstname, lastname, nickname, birthplace, birthdate,
                        address, city, postcode, province, country,
                        phone1, phone2, fax1, fax2, email, website);
  }

  /**
   *
   */
  class PersonalTableModel extends DefaultTableModel {
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