package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.Hashtable;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import pohaci.gumunda.titis.accounting.entity.PmtCAIOUOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAOthers;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class ExpenseSheetList extends JList {
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	  int objnya;
	  long m_sessionid = -1;
	  Employee[] m_employee = new Employee[0];
	  public ExpenseSheetList(Connection conn, long sessionid,int i) {
	    m_conn = conn;
	    m_sessionid = sessionid;
	    objnya=i;
	    setModel(new DefaultListModel());
	    initData(i);
	  }
	
	  void initData(int j) {
		  Hashtable myhash=new Hashtable(); 
		  GenericMapper mapnya1,mapnya2;
		  mapnya1=null;
		  mapnya2=null;
		  Object[] listData;
		  if (j==1){//Ini berati unutk project
		  mapnya1=MasterMap.obtainMapperFor(PmtCAIOUProject.class);
		  mapnya2=MasterMap.obtainMapperFor(PmtCAProject.class);
		  }
		  else{
			  mapnya1=MasterMap.obtainMapperFor(PmtCAIOUOthers.class);
			  mapnya2=MasterMap.obtainMapperFor(PmtCAOthers.class);
		
		  }
		  mapnya1.setActiveConn(m_conn);
		  mapnya2.setActiveConn(m_conn);
		  listData=mapnya1.doSelectAll().toArray();
	      DefaultListModel model = (DefaultListModel)getModel();
	      model.clear();
	    for(int i = 0; i < listData.length; i ++){
	     	if (j==1)
	    	{PmtCAIOUProject data=(PmtCAIOUProject)listData[i];
	    	  if ((myhash.get(data.getPayTo().getEmployeeNo())==null)&&(data.getStatus()==3))
	    	  {myhash.put(data.getPayTo().getEmployeeNo(),data.getPayTo());
	    	   model.addElement(data.getPayTo());}}
	    	else
	    	{PmtCAIOUOthers data=(PmtCAIOUOthers)listData[i];
	    	 if ((myhash.get(data.getPayTo().getEmployeeNo())==null)&&(data.getStatus()==3))
	    	  {myhash.put(data.getPayTo().getEmployeeNo(),data.getPayTo());
	    	 model.addElement(data.getPayTo());}
	    	}
	    	
		  }
	     listData=mapnya2.doSelectAll().toArray();
	    for(int i = 0; i < listData.length; i ++){
	    	if (j==1){
	    		PmtCAProject data=(PmtCAProject)listData[i];
	    		 if ((myhash.get(data.getPayTo().getEmployeeNo())==null)&&(data.getStatus()==3))
		    	  {myhash.put(data.getPayTo().getEmployeeNo(),data.getPayTo());
		    	
	    	model.addElement(data.getPayTo());}}
	    	else
	    	{PmtCAOthers data=(PmtCAOthers)listData[i];
	    	 if ((myhash.get(data.getPayTo().getEmployeeNo())==null)&&(data.getStatus()==3))
	    	  {myhash.put(data.getPayTo().getEmployeeNo(),data.getPayTo());
	    	model.addElement(data.getPayTo());	}
	    	}
	    	
		  }

	  }
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
	  
	  //Ini baru untuk refresh Project
	  public void refresh(int i){
		  initData(i);
	  }

	}

