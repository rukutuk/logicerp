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
import java.text.SimpleDateFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pohaci.gumunda.cgui.BaseTableCellEditor;
import pohaci.gumunda.cgui.BaseTableCellRenderer;
import pohaci.gumunda.cgui.DateCellEditor;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
//import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
//import pohaci.gumunda.titis.project.dbapi.IDBConstants;

public class EmployeeTable extends JTable  {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;
  public boolean m_editable = false;

  SimpleDateFormat m_dateformat = new SimpleDateFormat("dd-MM-yyyy");
  QualificationArrayCellEditor m_editor = null;
  Employee m_employee = null;
  private SexType m_sex = null;
  private SimpleEmployeeAttribute m_religion = null;
  private SimpleEmployeeAttribute m_marital = null;
  private PTKP m_ptkp = null;
          
  public EmployeeTable(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    EmployeTableModel model = new EmployeTableModel();
    model.addColumn("Attribute");
    model.addColumn("Description");
    model.addRow(new Object[]{"Employee No*", ""});
    model.addRow(new Object[]{"First Name*", ""});
    model.addRow(new Object[]{"Middle Name", ""});
    model.addRow(new Object[]{"Last Name", ""});
    model.addRow(new Object[]{"Nick Name", ""});
    model.addRow(new Object[]{"Birth Place", ""});
    model.addRow(new Object[]{"Birth Date*", ""});
    model.addRow(new Object[]{"Sex*", ""});

    model.addRow(new Object[]{"Job Title", ""});
    model.addRow(new Object[]{"Department", ""});
    model.addRow(new Object[]{"Unit Code", ""});
    model.addRow(new Object[]{"Education", ""});

    model.addRow(new Object[]{"Qualification", ""});
    model.addRow(new Object[]{"Religion", ""});
    model.addRow(new Object[]{"Nationality", ""});
    model.addRow(new Object[]{"Marital Status", ""});
    model.addRow(new Object[]{"Tax Art 21 Status*", ""});
    model.addRow(new Object[]{"Address", ""});
    model.addRow(new Object[]{"City", ""});
    model.addRow(new Object[]{"Post Code", ""});
    model.addRow(new Object[]{"Province", ""});
    model.addRow(new Object[]{"Country", ""});
    model.addRow(new Object[]{"Phone", ""});
    model.addRow(new Object[]{"Mobile Phone 1", ""});
    model.addRow(new Object[]{"Mobile Phone 2", ""});
    model.addRow(new Object[]{"Fax", ""});
    model.addRow(new Object[]{"Email", ""});
    
    model.addRow(new Object[]{"Status", ""});
    
    setModel(model);

    getColumnModel().getColumn(0).setCellRenderer(new BaseTableCellRenderer());
    getColumnModel().getColumn(0).setPreferredWidth(150);
    getColumnModel().getColumn(0).setMaxWidth(150);
  }

  public TableCellEditor getCellEditor(int row, int col) {
    if(row == 6 && col == 1)
      return new DateCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame());
    else if(row == 7 && col == 1)
      return new SexTypeCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                   "Sex Type", m_conn, m_sessionid);
    else if(row == 12 && col == 1)
      return m_editor = new QualificationArrayCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
          "Qualification", m_conn, m_sessionid);
    else if(row == 13 && col == 1)
      return new ReligionCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                    "Religion", m_conn, m_sessionid);
    else if(row == 15 && col == 1)
      return new MaritalStatusCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
          "Marital Status", m_conn, m_sessionid);
    else if(row == 16 && col == 1)
      return new PTKPCellEditor(pohaci.gumunda.cgui.GumundaMainFrame.getMainFrame(),
                                "PTKP", m_conn, m_sessionid);
    return new BaseTableCellEditor();
  }

  public void stopCellEditing() {
    TableCellEditor editor;
    if((editor = getCellEditor()) != null)
      editor.stopCellEditing();
  }

  public void clearTable() {
    m_editor = null;
    m_employee = null;
    int count = getRowCount();
    for(int i = 0; i < count; i ++)
      setValueAt("", i, 1);
  }

  public void setEmployee(Employee employee) {
    m_employee = employee;
    m_editor = null;
    int row = 0;
    HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
    setValueAt(employee.getEmployeeNo(), row++, 1);
    setValueAt(employee.getFirstName(), row++, 1);
    setValueAt(employee.getMidleName(), row++, 1);
    setValueAt(employee.getLastName(), row++, 1);
    setValueAt(employee.getNickName(), row++, 1);
    setValueAt(employee.getBirthPlace(), row++, 1);

    if(employee.getBirthDate() != null)
      setValueAt(m_dateformat.format(employee.getBirthDate()), row++, 1);
    else
      setValueAt("", row++, 1);
    try{
        m_sex = logic.getSexType(employee.getNSex());
        m_religion = logic.getReligion(employee.getNReligion());
        m_marital = logic.getMaritalStatus(employee.getNMarital());
        m_ptkp = logic.getPTKP(employee.getNArt21());
    }catch(Exception ex){
    	ex.printStackTrace();        
    }
    if (m_sex != null)
        setValueAt(m_sex/*.getDescription()*/, row++, 1);       
    else
    	setValueAt("",row++,1);
    setValueAt("", row++, 1);
    setValueAt("", row++, 1);
    setValueAt("", row++, 1);
    setValueAt("", row++, 1);

    setValueAt(employee.getQualificationToString(), row++, 1);    
    
    if (m_religion != null)
        setValueAt(m_religion/*.getDescription()*/, row++, 1);
    else
		setValueAt("", row++, 1);
    setValueAt(employee.getNationality(), row++, 1);
    
    if (m_marital!=null)
        setValueAt(m_marital/*.getDescription()*/, row++, 1);
    else
		setValueAt("", row++, 1);
    if (m_ptkp!=null)
        setValueAt(m_ptkp/*.getDescription()*/, row++, 1);
    else
		setValueAt("", row++, 1);
    
    setValueAt(employee.getAddress(), row++, 1);
    setValueAt(employee.getCity(), row++, 1);

    if(employee.getPostCode() > 0)
      setValueAt(String.valueOf(employee.getPostCode()), row++, 1);
    else
      setValueAt("", row++, 1);

    setValueAt(employee.getProvince(), row++, 1);
    setValueAt(employee.getCountry(), row++, 1);
    setValueAt(employee.getPhone(), row++, 1);
    setValueAt(employee.getMobilePhone1(), row++, 1);
    setValueAt(employee.getMobilePhone2(), row++, 1);
    setValueAt(employee.getFax(), row++, 1);
    setValueAt(employee.getEmail(), row++, 1);
  }

  public Employee getEmployee() throws Exception {
    stopCellEditing();

    java.util.ArrayList list = new java.util.ArrayList();
    int row = 0;
    String no = "", firstname = "", midlename = "", lastname = "",
    nickname = "", birthplace = "";
    java.util.Date birthdate = null;
    String address = "", city = "";
    int postcode = 0;
    SexType sex = null;
    SimpleEmployeeAttribute religion = null, marital = null;
    PTKP art21 = null;
    String nationality = "", province = "", country = "", phone = "",
    mobphone1 = "", mobphone2 = "", fax = "", email = "";

    no = (String)getValueAt(row++, 1);
    if(no == null || no.equals(""))
      list.add("Employee No");

    firstname = (String)getValueAt(row++, 1);
    if(firstname == null || firstname.equals(""))
      list.add("First Name");

    midlename = (String)getValueAt(row++, 1);
    lastname = (String)getValueAt(row++, 1);
    nickname = (String)getValueAt(row++, 1);
    birthplace = (String)getValueAt(row++, 1);

    String strdate = (String)getValueAt(row++, 1);
    if(strdate == null || strdate.equals(""))
      list.add("Birthdate");

    Object obj = getValueAt(row++, 1);
    if(obj instanceof SexType)
      sex = (SexType)obj;
    else
      list.add("Sex Type");

    row++;
    row++;
    row++;
    row++;
    row++;

    obj = getValueAt(row++, 1);
    if(obj instanceof SimpleEmployeeAttribute)
      religion = (SimpleEmployeeAttribute)obj;

    nationality = (String)getValueAt(row++, 1);

    obj = getValueAt(row++, 1);
    if(obj instanceof SimpleEmployeeAttribute)
      marital = (SimpleEmployeeAttribute)obj;

    obj = getValueAt(row++, 1);
    if(obj instanceof PTKP)
      art21 = (PTKP)obj;
    else
      if(art21 == null)
        list.add("Tax Art 21 Status");

    address = (String)getValueAt(row++, 1);
    city = (String)getValueAt(row++, 1);

    String strexc = "Please insert :\n";
    String[] exception = new String[list.size()];
    list.toArray(exception);
    if(exception.length > 0) {
      for(int i = 0; i < exception.length; i ++)
        strexc += exception[i] + "\n";
      throw new Exception(strexc);
    }

    try {
      birthdate = m_dateformat.parse(strdate);
    }
    catch(Exception ex) {
      throw new Exception(ex.getMessage());
    }

    try {
      String strpostcode = (String)getValueAt(row++, 1);
      if(!(strpostcode).equals(""))
        postcode = Integer.parseInt(strpostcode);
    }
    catch(Exception ex) {
      throw new Exception("Please insert Post Code with numeric");
    }

    province = (String)getValueAt(row++, 1);
    country = (String)getValueAt(row++, 1);
    phone = (String)getValueAt(row++, 1);
    mobphone1 = (String)getValueAt(row++, 1);
    mobphone2 = (String)getValueAt(row++, 1);
    fax = (String)getValueAt(row++, 1);
    email = (String)getValueAt(row++, 1);

    Qualification[] qua = new Qualification[0];
    if(m_editor != null)
      qua = m_editor.getQualification();
    else if(m_employee != null)
      qua = m_employee.getQualification();

    Employee employee =  new Employee(no, firstname, midlename, lastname, nickname, birthplace,
                                      birthdate, sex, religion, nationality, marital,
                                      art21, address, city, postcode, province, country,
                                      phone, mobphone1, mobphone2, fax, email);

    employee.setQualification(qua);
    return employee;
  }

  /**
   *
   */
  class EmployeTableModel extends DefaultTableModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int col) {
      if(col == 0)
        return false;

      if(row == 8 || row == 9 || row == 10 || row == 11 || row == 27)
        return false;

      if(m_editable)
        return true;
      return false;
    }

    public void setValueAt(Object obj, int row, int col) {
      if(obj instanceof SexType)
        super.setValueAt(new SexType((SexType)obj, SexType.DESCRIPTION), row, col);
      else if(obj instanceof Qualification)
        super.setValueAt(new Qualification((Qualification)obj, Qualification.NAME), row, col);
      else
        super.setValueAt(obj, row, col);
    }
  }
}
