package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.*;

import pohaci.gumunda.titis.accounting.entity.PmtESDiff;
import pohaci.gumunda.titis.accounting.logic.BeginningExpensheetDifferenceBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.ExpenseSheetBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class LookupExpPmtOwner extends LookupPicker {
	private static final long serialVersionUID = 1L;
	GenericMapper mapperPmtEsDiff = null;
	
	public LookupExpPmtOwner(Connection conn, long sessionid){
		super(conn, sessionid, "lookup Expense Sheet");
		setSize(400, 300);
		mapperPmtEsDiff = MasterMap.obtainMapperFor(PmtESDiff.class);
		mapperPmtEsDiff.setActiveConn(m_conn);
		initData();		
	}  
	
	void initData() {
		getModel().addColumn("No.");
		getModel().addColumn("Employee");
		
		int no = 0;

		Hashtable hashtable = new Hashtable();
		List resultList = new ArrayList();

		ExpenseSheetBusinessLogic esLogic = new ExpenseSheetBusinessLogic(
				m_conn, m_sessionid, ExpenseSheetBusinessLogic.PAYMENT);
		List list = esLogic.getESOwner();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Employee employee = (Employee) iterator.next();
			if (!hashtable.containsKey(new Long(employee.getIndex()))) {
				hashtable.put(new Long(employee.getIndex()), employee);
				resultList.add(employee);
			}
		}

		BeginningExpensheetDifferenceBusinessLogic bbLogic = new BeginningExpensheetDifferenceBusinessLogic(
				m_conn, m_sessionid,
				BeginningExpensheetDifferenceBusinessLogic.PAYMENT);
		list = bbLogic.getESOwner();
		iterator = list.iterator();
		while (iterator.hasNext()) {
			Employee employee = (Employee) iterator.next();
			if (!hashtable.containsKey(new Long(employee.getIndex()))) {
				hashtable.put(new Long(employee.getIndex()), employee);
				resultList.add(employee);
			}
		}
		
		iterator = resultList.iterator();
		while(iterator.hasNext()){
			Employee employee = (Employee) iterator.next();
			getModel().addRow(new Object[]{new Integer(++no),employee});
		}
		
		/*try {			
			GenericMapper maper = MasterMap.obtainMapperFor(BeginningEsDiff.class);
			maper.setActiveConn(this.m_conn);
			List list=maper.doSelectWhere(IDBConstants.ATTR_VALUE + "<0");
			
			Hashtable tbl=new Hashtable(); 
			for(int i=0;i<list.size();i++){
				BeginningEsDiff bb=(BeginningEsDiff)list.get(i);
				if (cekPayBeginningBalance(bb))
					if (!tbl.containsKey(new Long(bb.getEmployee().getIndex())))
						tbl.put(new Long(bb.getEmployee().getIndex()),bb.getEmployee());					
			}			
			
			GenericMapper mapnya1;
			mapnya1=null;
			Object[] listData;
			mapnya1=MasterMap.obtainMapperFor(ExpenseSheet.class);
			mapnya1.setActiveConn(m_conn);
			listData=mapnya1.doSelectWhere(IDBConstants.ATTR_STATUS +"=3").toArray();			
			for(int i = 0; i < listData.length; i ++){
				ExpenseSheet es=(ExpenseSheet)listData[i];
				PmtCAProject pmt1=null;
				PmtCAIOUProjectSettled pmt2=null;
				PmtCAOthers pmt3=null;
				PmtCAIOUOthersSettled pmt4=null;
				double amount1 = 0,amount2=0;
				amount2=es.getAmount();
				String a=es.getEsProjectType();		
				if (es.getEsOwner().getIndex() ==12)
					System.err.println(es.getEsOwner().getIndex() +  "=" + es.getEsOwner().getFirstName());
				if (a.equalsIgnoreCase("General")){
					if (es.getPmtCaProject()!=null){
						pmt1=es.getPmtCaProject();
						amount1=pmt1.getAmount();
					}else if (es.getPmtCaOthers()!=null){
						pmt3=es.getPmtCaOthers();
						amount1=pmt3.getAmount();
					}
				}
				else if (a.equalsIgnoreCase("IOU")){
					if (es.getPmtCaIouProjectSettled()!=null){
						pmt2=es.getPmtCaIouProjectSettled();
						amount1=pmt2.getAmount();
					}else{
						pmt4=es.getPmtCaIouOthersSettled();
						amount1=pmt4.getAmount();
					}		    		  
				}		    		  
				if (amount1<amount2){					
					if (cekPayExpensheSheet(es)){
						if (!tbl.containsKey(new Long(es.getEsOwner().getIndex()))){
							tbl.put(new Long(es.getEsOwner().getIndex()),es.getEsOwner());
						}			
					}
				}
			}	
			Enumeration enumeration = tbl.elements();
			int k = 0;
			while (enumeration.hasMoreElements()){
				Object obj = enumeration.nextElement();				
				getModel().addRow(new Object[]{new Integer(++k),(Employee)obj});		
			}
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Ini 1",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}*/
	}	
	
	/*public boolean cekPayBeginningBalance(BeginningEsDiff bb){
		boolean cek = false;
		String strWhere = IDBConstants.ATTR_BEGINNING_BALANCE + "=" + bb.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		System.err.println(strWhere);
		List list = mapperPmtEsDiff.doSelectWhere(strWhere);		
		BeginningExpensheetDifferenceBusinessLogic bbLogic = new BeginningExpensheetDifferenceBusinessLogic(m_conn, m_sessionid,bb.getEmployee().getIndex(),2);
		List bbList = bbLogic.getOutstanding();
		if (list.size()==0 && bbList.size()>0)
			cek = true;
		return cek;
	}
	
	public boolean cekPayExpensheSheet(ExpenseSheet es){
		boolean cek = false;
		String strWhere = IDBConstants.ATTR_ES_NO +"=" + es.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3"; 
		List list = mapperPmtEsDiff.doSelectWhere(strWhere);
		if (list.size()==0)		
			cek = true;
		return cek;
	}	*/
	
	public void select() {
		int rowindex = m_table.getSelectedRow();
		if(rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 1);
			setObject(obj);
		}
	}
	
	protected String getCaption(Object obj){
		if (obj instanceof Employee) {
			Employee emp = (Employee) obj;
			return emp.getFirstName() + " " + emp.getMidleName() + " " + emp.getLastName();
		} else		
			return "";		
	}	
}


