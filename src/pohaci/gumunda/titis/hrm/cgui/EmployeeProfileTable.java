package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.*;

import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeeProfileTable extends JTable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;
  private SexType m_sex = null;
  private SimpleEmployeeAttribute m_marital = null;

  public EmployeeProfileTable(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;
    DetailTableModel model = new DetailTableModel();
    model.addColumn("Attribute");
    model.addColumn("Description");

    model.addRow(new Object[]{"Employee No", ""});
    model.addRow(new Object[]{"Nama", ""});
    model.addRow(new Object[]{"Education", ""});
    model.addRow(new Object[]{"Job Title", ""});
    model.addRow(new Object[]{"Department", ""});
    model.addRow(new Object[]{"Unit Code", ""});
    model.addRow(new Object[]{"Work Agreement", ""});
    model.addRow(new Object[]{"Sex", ""});
    model.addRow(new Object[]{"City", ""});
    model.addRow(new Object[]{"Postcode", ""});
    model.addRow(new Object[]{"Province", ""});
    model.addRow(new Object[]{"Phone", ""});
    model.addRow(new Object[]{"Marital Status", ""});
    model.addRow(new Object[]{"Qualification", ""});
    setModel(model);

    getColumnModel().getColumn(0).setPreferredWidth(150);
    getColumnModel().getColumn(0).setMaxWidth(150);
  }

  public TableCellRenderer getCellRenderer(int row, int col) {
    if(col == 0)
      return new BaseTableCellRenderer();
    return super.getCellRenderer(row, col);
  }

  public void setEmployee(Employee employee) {
	HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
	
    int row = 0;
    String postcode = "";
    if(employee.getPostCode() > 0)
      postcode = String.valueOf(employee.getPostCode());
    setValueAt(employee.getEmployeeNo(), row++, 1);
    setValueAt(employee.getFirstName() + " " + employee.getMidleName() + " " + employee.getLastName(), row++, 1);
    setValueAt("", row++, 1);
    setValueAt("", row++, 1);
    setValueAt("", row++, 1);
    setValueAt("", row++, 1);
    setValueAt("", row++, 1);
    
    try{
        m_sex = logic.getSexType(employee.getNSex());    
        m_marital = logic.getMaritalStatus(employee.getNMarital());        
    }catch(Exception ex){        
    }
    if (m_sex != null)
    	setValueAt(m_sex.getDescription(), row++, 1);  
    
    setValueAt(employee.getCity(), row++, 1);
    setValueAt(postcode, row++, 1);
    setValueAt(employee.getProvince(), row++, 1);
    setValueAt(employee.getPhone(), row++, 1);
    
    if (m_marital!=null)
        setValueAt(m_marital.getDescription(), row++, 1);
    
    setValueAt("", row++, 1);
  }

  public void clearTable() {
    int count = getRowCount();
    for(int i = 0; i < count; i ++)
      setValueAt("", i, 1);
  }

  /**
   *
   */
  class DetailTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      return false;
    }
  }
}