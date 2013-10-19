package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.entity.RcvESDiff;
import pohaci.gumunda.titis.accounting.logic.BeginningExpensheetDifferenceBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.ExpenseSheetBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class LookupExpRcvOwner extends LookupPicker {
	private static final long serialVersionUID = 1L;

	GenericMapper mapperPmtEsDiff = null;

	public LookupExpRcvOwner(Connection conn, long sessionid) {
		super(conn, sessionid, "Expense Sheet Receive Owner");
		setSize(400, 300);
		mapperPmtEsDiff = MasterMap.obtainMapperFor(RcvESDiff.class);
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
				m_conn, m_sessionid, ExpenseSheetBusinessLogic.RECEIVE);
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
				BeginningExpensheetDifferenceBusinessLogic.RECEIVE);
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
		 maper.setActiveConn(m_conn);
		 List list=maper.doSelectWhere(IDBConstants.ATTR_VALUE + ">=0");
		 int jum = list.size();
		 Hashtable tbl=new Hashtable(); 	
		 for(int i=0;i<jum;i++){
		 BeginningEsDiff bb=(BeginningEsDiff)list.get(i);
		 if (cekPayBeginningBalance(bb)){
		 if (!tbl.containsKey(new Long(bb.getEmployee().getIndex()))){
		 tbl.put(new Long(bb.getEmployee().getIndex()),bb.getEmployee());	
		 }
		 }	
		 }
		 
		 GenericMapper mapnya1 =MasterMap.obtainMapperFor(ExpenseSheet.class);
		 mapnya1.setActiveConn(m_conn);
		 Object[] listData=mapnya1.doSelectAll().toArray();		
		 for(int i = 0; i < listData.length; i ++){
		 ExpenseSheet es=(ExpenseSheet)listData[i];				
		 PmtCAProject pmtCaProject=null;
		 PmtCAIOUProjectSettled pmt2=null;
		 PmtCAOthers pmtCaOthers=null;
		 PmtCAIOUOthersSettled pmtCAIOUOthersSettled=null;
		 double amount1=0;
		 double amount2=0;
		 amount2=es.getAmount();
		 String a=es.getEsProjectType();
		 if (a.equalsIgnoreCase("General")){
		 if (es.getPmtCaProject()!=null){
		 pmtCaProject=es.getPmtCaProject();
		 amount1=pmtCaProject.getAmount();
		 }else if (es.getPmtCaOthers()!=null){
		 pmtCaOthers=es.getPmtCaOthers();
		 if(pmtCaOthers!=null)
		 amount1=pmtCaOthers.getAmount();
		 }
		 }else if (a.equalsIgnoreCase("IOU")){  
		 if (es.getPmtCaIouProjectSettled()!=null)	{
		 pmt2=es.getPmtCaIouProjectSettled();
		 amount1=pmt2.getAmount();	
		 }
		 else if (es.getPmtCaIouOthersSettled()!=null){
		 pmtCAIOUOthersSettled=es.getPmtCaIouOthersSettled();
		 amount1=pmtCAIOUOthersSettled.getAmount();
		 }					
		 }				
		 if (amount1>amount2){					
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
		 ex.printStackTrace();
		 JOptionPane.showMessageDialog(this, "Ini","Warning", JOptionPane.WARNING_MESSAGE);
		 }*/
	}

	/*public boolean cekPayBeginningBalance(BeginningEsDiff bb) {
		boolean cek = false;
		String strWhere = IDBConstants.ATTR_BEGINNING_BALANCE + "="
				+ bb.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		List list = mapperPmtEsDiff.doSelectWhere(strWhere);

		BeginningExpensheetDifferenceBusinessLogic bbLogic = new BeginningExpensheetDifferenceBusinessLogic(
				m_conn, m_sessionid, bb.getEmployee().getIndex(), 1);
		List bbList = bbLogic.getOutstanding();

		if (list.size() == 0 && bbList.size() > 0)
			cek = true;
		return cek;
	}*/

	/*public boolean cekPayExpensheSheet(ExpenseSheet es) {
		boolean cek = false;
		String strWhere = IDBConstants.ATTR_ES_NO + "=" + es.getIndex()
				+ " AND " + IDBConstants.ATTR_STATUS + "=3";
		System.err.println(strWhere);
		List list = mapperPmtEsDiff.doSelectWhere(strWhere);
		if (list.size() == 0)
			cek = true;
		return cek;
	}*/

	public void select() {
		int rowindex = m_table.getSelectedRow();
		if (rowindex != -1) {
			Object obj = getModel().getValueAt(rowindex, 1);
			setObject(obj);
		}
	}

	protected String getCaption(Object obj) {
		if (obj instanceof Employee) {
			Employee emp = (Employee) obj;
			return emp.getFirstName() + " " + emp.getMidleName() + " "
					+ emp.getLastName();

		} else
			return null;
		//Employee emp = (Employee)obj;

		/*if (obj instanceof ExpenseSheet) {
		 ExpenseSheet emp = (ExpenseSheet) obj;
		 return emp.getEsOwner().getFirstName()+" "+emp.getEsOwner().getLastName();
		 
		 }
		 else if (obj instanceof BeginningEsDiff) {
		 BeginningEsDiff emp = (BeginningEsDiff) obj;
		 return emp.getEmployee().getFirstName()+" "+emp.getEmployee().getLastName();
		 }
		 return "";*/
	}
}
