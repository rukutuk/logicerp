package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.entity.ExpenseSheet;
import pohaci.gumunda.titis.accounting.logic.BeginningESBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAIOUOthersSettledBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAIOUProjecSettledtBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAOthersBusinessLogic;
import pohaci.gumunda.titis.accounting.logic.CAProjectBusinessLogic;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.Employee;

public class LookupESOwner extends LookupPicker {
	private static final long serialVersionUID = 1L;

	private GenericMapper mapperES = null;
	
	public static int PROJECT = 1;
	public static int OTHERS = 2;

	public LookupESOwner(Connection conn, long sessionid, int a) {
		super(conn, sessionid, "Expense Sheet ES Owner");
		setSize(400, 300);
		mapperES = MasterMap.obtainMapperFor(ExpenseSheet.class);
		mapperES.setActiveConn(m_conn);
		initData(a);
	}

	void initData(int j) {
		getModel().addColumn("No.");
		getModel().addColumn("Employee");

		int no = 0;
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List list3 = new ArrayList();
		//List list4 = new ArrayList();

		List resultList = new ArrayList();
		Hashtable hashtable = new Hashtable();

		if (j == PROJECT) {
			CAProjectBusinessLogic logic1 = new CAProjectBusinessLogic(
					m_conn, m_sessionid);
			list1 = logic1.getESOwner();

			CAIOUProjecSettledtBusinessLogic logic2 = new CAIOUProjecSettledtBusinessLogic(
					m_conn, m_sessionid);
			list2 = logic2.getESOwner();
			
			BeginningESBusinessLogic logic3 = new BeginningESBusinessLogic(m_conn,
					m_sessionid, IConstants.ATTR_VARS_CA_PROJECT);
			list3 = logic3.getESOwner();
			
			/*BeginningESBusinessLogic logic4 = new BeginningESBusinessLogic(m_conn,
					m_sessionid, IConstants.ATTR_VARS_CA_IOU_PROJECT);
			list4 = logic4.getESOwner();*/
		} else if (j==OTHERS){
			CAOthersBusinessLogic logic1 = new CAOthersBusinessLogic(
					m_conn, m_sessionid);
			list1 = logic1.getESOwner();

			CAIOUOthersSettledBusinessLogic logic2 = new CAIOUOthersSettledBusinessLogic(
					m_conn, m_sessionid);
			list2 = logic2.getESOwner();
			
			BeginningESBusinessLogic logic3 = new BeginningESBusinessLogic(m_conn,
					m_sessionid, IConstants.ATTR_VARS_CA_OTHER);
			list3 = logic3.getESOwner();
			
			/*BeginningESBusinessLogic logic4 = new BeginningESBusinessLogic(m_conn,
					m_sessionid, IConstants.ATTR_VARS_CA_IOU_OTHER);
			list4 = logic4.getESOwner();*/
		}

		createResultList(list1, resultList, hashtable);
		createResultList(list2, resultList, hashtable);
		createResultList(list3, resultList, hashtable);
		//createResultList(list4, resultList, hashtable);

		Iterator iterator = resultList.iterator();
		while (iterator.hasNext()) {
			Employee emp = (Employee) iterator.next();
			getModel().addRow(new Object[] { new Integer(++no), emp });
		}

		/*try {
		 Hashtable tbl=new Hashtable(); 
		 GenericMapper mapnya1,mapnya2;
		 mapnya1=null;
		 mapnya2=null;
		 Object[] listData;
		 if (j==1){
		 mapnya1=MasterMap.obtainMapperFor(PmtCAIOUProjectSettled.class);
		 mapnya2=MasterMap.obtainMapperFor(PmtCAProject.class);
		 }
		 else{
		 mapnya1=MasterMap.obtainMapperFor(PmtCAIOUOthersSettled.class);
		 mapnya2=MasterMap.obtainMapperFor(PmtCAOthers.class);			
		 }
		 mapnya1.setActiveConn(m_conn);
		 mapnya2.setActiveConn(m_conn);
		 listData=mapnya1.doSelectWhere(IDBConstants.ATTR_STATUS + "=3").toArray();
		 String strWhere ="";
		 for(int i = 0; i < listData.length; i ++){
		 if (j==1){
		 PmtCAIOUProjectSettled pmtCAIOUProjectSettled=(PmtCAIOUProjectSettled)listData[i];
		 strWhere = IDBConstants.ATTR_PMT_CA_IOU_PROJECT_SETTLED +"=" + pmtCAIOUProjectSettled.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		 if (cekHadPayable(strWhere))
		 if (pmtCAIOUProjectSettled.getPmtcaiouproject()!=null)
		 if (pmtCAIOUProjectSettled.getPmtcaiouproject().getPayTo()!=null)
		 if (!tbl.containsKey(new Long(pmtCAIOUProjectSettled.getPmtcaiouproject().getPayTo().getIndex())))
		 tbl.put(new Long(pmtCAIOUProjectSettled.getPmtcaiouproject().getPayTo().getIndex()),pmtCAIOUProjectSettled.getPmtcaiouproject().getPayTo());
		 
		 }
		 else{
		 PmtCAIOUOthersSettled pmtCAIOUOthersSettled=(PmtCAIOUOthersSettled)listData[i];			
		 strWhere = IDBConstants.ATTR_PMT_CA_IOU_OTHERS_SETTLED +"=" + pmtCAIOUOthersSettled.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		 System.err.println(strWhere);
		 if (cekHadPayable(strWhere)){
		 if (pmtCAIOUOthersSettled.getPmtcaiouothers()!=null){
		 if (pmtCAIOUOthersSettled.getPmtcaiouothers().getPayTo()!=null)
		 if (!tbl.containsKey(new Long(pmtCAIOUOthersSettled.getPmtcaiouothers().getPayTo().getIndex())))
		 tbl.put(new Long(pmtCAIOUOthersSettled.getPmtcaiouothers().getPayTo().getIndex()),pmtCAIOUOthersSettled.getPmtcaiouothers().getPayTo());					
		 }		  
		 }
		 }
		 }
		 listData=mapnya2.doSelectWhere(IDBConstants.ATTR_STATUS + "=3").toArray();
		 for(int i = 0; i < listData.length; i ++){
		 if (j==1){
		 PmtCAProject pmtCAProject=(PmtCAProject)listData[i];
		 strWhere = IDBConstants.ATTR_PMT_CA_PROJECT +"=" + pmtCAProject.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		 if (cekHadPayable(strWhere))
		 if (!tbl.containsKey(new Long(pmtCAProject.getPayTo().getIndex())))
		 tbl.put(new Long(pmtCAProject.getPayTo().getIndex()),pmtCAProject.getPayTo());
		 }
		 else{
		 PmtCAOthers pmtCAOthers=(PmtCAOthers)listData[i];
		 strWhere = IDBConstants.ATTR_PMT_CA_OTHERS +"=" + pmtCAOthers.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		 if (cekHadPayable(strWhere))
		 if (!tbl.containsKey(new Long(pmtCAOthers.getPayTo().getIndex())))
		 tbl.put(new Long(pmtCAOthers.getPayTo().getIndex()),pmtCAOthers.getPayTo());
		 }		    	
		 }			
		 
		 GenericMapper mapnya3=MasterMap.obtainMapperFor(BeginningCashAdvance.class);
		 mapnya3.setActiveConn(m_conn);
		 listData=mapnya3.doSelectWhere(IDBConstants.ATTR_STATUS + "=3").toArray();
		 for(int i = 0; i < listData.length; i ++){
		 BeginningCashAdvance bb = (BeginningCashAdvance)listData[i];
		 strWhere = IDBConstants.ATTR_BEGINNING_BALANCE +"=" + bb.getIndex() + " AND " + IDBConstants.ATTR_STATUS + "=3";
		 if (cekHadPayable(strWhere))
		 if (!tbl.containsKey(new Long(bb.getEmployee().getIndex())))
		 tbl.put(new Long(bb.getEmployee().getIndex()),bb.getEmployee());
		 }
		 
		 Enumeration enumeration = tbl.elements();
		 int no = 0;
		 while (enumeration.hasMoreElements()){
		 Object obj = enumeration.nextElement();				
		 getModel().addRow(new Object[]{new Integer(++no),(Employee)obj});		
		 }			
		 }
		 catch (Exception ex) {
		 JOptionPane.showMessageDialog(this, ex.getMessage(),
		 "Warning", JOptionPane.WARNING_MESSAGE);
		 }*/
	}

	private void createResultList(List list1, List resultList, Hashtable hashtable) {
		Iterator iterator = list1.iterator();
		while (iterator.hasNext()) {
			Employee emp = (Employee) iterator.next();

			if (!hashtable.containsKey(new Long(emp.getIndex()))) {
				hashtable.put(new Long(emp.getIndex()), emp);
				resultList.add(emp);
			}
		}
	}

	public boolean cekHadPayable(String strWhere) {
		boolean cek = false;
		System.err.println(strWhere);
		List list = mapperES.doSelectWhere(strWhere);

		if (list.size() == 0)
			cek = true;
		return cek;
	}

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
			return emp.igetFullName();
		} else
			return null;
	}

}
