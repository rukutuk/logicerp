/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayroll;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Submit;

/**
 * @author dark-knight
 *
 */
public class TransactionStateReversalLogic {

	Connection connection;
	
	public TransactionStateReversalLogic(Connection connection) {
		this.connection = connection;
	}

	public void revertState(Transaction transaction){
		if(transaction.getStatus()==3)
			return;
		
		// find the standard journal setting
		GenericMapper mapper = MasterMap.obtainMapperFor(JournalStandardSetting.class);
		mapper.setActiveConn(connection);
		List list = 
			mapper.doSelectWhere("JOURNAL=" + transaction.getJournalStandard().getIndex());
	
		// get the related transaction
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			JournalStandardSetting setting = (JournalStandardSetting) iterator.next();
			
			String appName = setting.getApplication();
			List classList = getClassList(appName);
			
			Iterator iterator2 = classList.iterator();
			while(iterator2.hasNext()){
				Class clazz = (Class) iterator2.next();
				mapper = MasterMap.obtainMapperFor(clazz);
				mapper.setActiveConn(connection);
				List objectList = (List) mapper.doSelectWhere(IDBConstants.ATTR_TRANSACTION + "=" + transaction.getIndex());
				
				// you find the data, update the state
				Iterator iterator3 = objectList.iterator();
				while(iterator3.hasNext()){
					TransactionTemplateEntity orig = (TransactionTemplateEntity) iterator3.next();
					orig.setStatus(Transaction.NOT_SUBMITTED);
					//orig.setReferenceNo(null);
					orig.setTrans(null);
					orig.setSubmitDate(null);
					
					mapper.doUpdate(orig);
					
					// special for employee payroll
					if (orig instanceof EmployeePayroll){
						mapper.doDelete(orig);
					} else if (orig instanceof TaxArt21Submit) {
						mapper.doDelete(orig);
					}
				}
			}
		}
		
		mapper = MasterMap.obtainMapperFor(TransactionDetail.class);
		mapper.setActiveConn(connection);
		
		mapper.doDeleteByColumn("TRANS", String.valueOf(transaction.getIndex()));

		
		// delete transaction
		mapper = MasterMap.obtainMapperFor(Transaction.class);
		mapper.setActiveConn(connection);
		mapper.doDelete(transaction);
	}

	private List getClassList(String application) {
		ArrayList list = new ArrayList();
		Iterator iter = TransactionMapper.getList().iterator();
		while(iter.hasNext()){
			List myList = (List) iter.next();
			if(((String)myList.get(0)).equals(application))
				list.add((Class)myList.get(1));
		}
		return list;
	}
}
