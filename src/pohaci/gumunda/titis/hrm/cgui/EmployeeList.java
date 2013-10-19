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
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import pohaci.gumunda.titis.accounting.entity.PmtEmpReceivable;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeeList extends JList {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Connection m_conn = null;
  long m_sessionid = -1;
  Employee[] m_employee = new Employee[0];

  public EmployeeList(Connection conn, long sessionid) {
    m_conn = conn;
    m_sessionid = sessionid;

    setModel(new DefaultListModel());
    initData();
  }
  //Tambahan cok gung
  boolean EmpPay=false;
  public EmployeeList(Connection conn, long sessionid,int i) {
	    m_conn = conn;
	    m_sessionid = sessionid;
	    EmpPay=true;
	    setModel(new DefaultListModel());
	    initData1();
	  }

  void initData() {
    DefaultListModel model = (DefaultListModel)getModel();
    model.removeAllElements();    
    try {
      HRMBusinessLogic logic = new HRMBusinessLogic(m_conn);
      m_employee = logic.getAllEmployee(m_sessionid, IDBConstants.MODUL_MASTER_DATA);

      for(int i = 0; i < m_employee.length; i ++)
        model.addElement(m_employee[i]);
    }
    catch(Exception ex) {
      JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

  public void initData1()
  { Hashtable myhash=new Hashtable(); 
	GenericMapper mapnya = MasterMap.obtainMapperFor(PmtEmpReceivable.class);
    mapnya.setActiveConn(this.m_conn);
	//UnitBankCashTransferLoader loader= new  UnitBankCashTransferLoader(m_conn,PmtEmpReceivable.class);
    Object[] listData=mapnya.doSelectAll().toArray();
    DefaultListModel model = (DefaultListModel)getModel();
  //  AccountingSQLSAP isql = new AccountingSQLSAP();
    model.clear();
    for(int i = 0; i < listData.length; i ++){
    	PmtEmpReceivable data=(PmtEmpReceivable)listData[i];
    	//if (data.getTrans()!=null)
    //	System.out.println("Woi woi"+data.statusInString()+" "+data.getStatus());
    	if ((myhash.get(data.getPayTo().getEmployeeNo())==null)&&(data.getStatus()==3))
    	{myhash.put(data.getPayTo().getEmployeeNo(),data.getPayTo());
     	model.addElement((Employee)data.getPayTo());}

    }
}
  // tambahan method dari nunung
  public Employee[] getEmployee(){	  
	  return m_employee;
  }
  
  public void reset(Employee[] employee) { // dapat employee hasil search
    DefaultListModel model = (DefaultListModel)getModel();
    model.removeAllElements();    
    for(int i = 0; i < employee.length; i ++)
      model.addElement(employee[i]);
    m_employee = employee; // tambahan koding dari nunung
  }

  public void refresh() { // dapat employee total
    if (EmpPay==false)
	  initData();
    else
    	initData1();
  }
  
  
  
 /* public Object getSelectedValue(){
	  
	return this.getSelectedValue(); 
  }*/
  

}