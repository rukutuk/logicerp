/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.cgui.StateTemplateEntity;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.TransactionPosted;
import pohaci.gumunda.titis.accounting.entity.TransactionPostedDetail;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayroll;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Submit;

/**
 * @author dark-knight
 * 
 */
public class TransactionVoidLogic {
	Connection connection;

	String modul;

	long sessionId;

	public TransactionVoidLogic(Connection connection, String modul,
			long sessionId) {
		this.connection = connection;
		this.modul = modul;
		this.sessionId = sessionId;
	}

	public void voidTransaction(Transaction transaction, String description) throws Exception {
		if (transaction.getStatus() < 3)
			return;

		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			// create Void Transaction
			Transaction voidTransaction = createVoidTransaction(transaction, description);
			
			// create posted void transaction
			TransactionPosted postedVoidTransaction = voidTransaction.createTransactionPosted(new Date());

			// save Void Transaction
			saveTransaction(voidTransaction);
			
			// save posted void transaction
			saveTransactionPosted(postedVoidTransaction);
			
			AccountingSQLSAP sql = new AccountingSQLSAP();
			BeginningBalance begBal = sql.getBeginningBalance((short)1, this.connection);
			JournalStandard js = begBal.getJournalStandard();
			long idx = 0;
			if (js!=null)
				idx = js.getIndex();
			
			if (idx>0){
				if (idx!=transaction.getJournalStandard().getIndex())
					updateStatus(sessionId, IDBConstants.MODUL_ACCOUNTING, transaction);
			}else 
				updateStatus(sessionId, IDBConstants.MODUL_ACCOUNTING, transaction);
			
			//	update the status
			transaction.isetVoid(true);
			GenericMapper mapper;
			mapper = MasterMap.obtainMapperFor(Transaction.class);
			mapper.setActiveConn(connection);
			mapper.doUpdate(transaction);

			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);
		} catch (Exception e) {
			e.printStackTrace();

			connection.rollback();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);

			throw new Exception(e);
		}

	}

	private Transaction createVoidTransaction(Transaction transaction, String description) {
		JournalStandard journalStandard = getVoidJournalStandard();
		
		ReferenceNoGeneratorHelper noHelper = new ReferenceNoGeneratorHelper(
				connection, sessionId, IDBConstants.MODUL_ACCOUNTING,
				transaction.getReference());
		
		String no = noHelper.createVoidReferenceNo(journalStandard.getCode());

		Transaction voidTransaction = transaction.createVoidTransaction(
				transaction.getTransDate(), journalStandard, no, description);
		return voidTransaction;
	}

	public JournalStandard getVoidJournalStandard() {
		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				connection, sessionId, IDBConstants.MODUL_ACCOUNTING);
		List journalStandardSettingList = helper
				.getJournalStandardSetting(IConstants.VOID_TRANSACTION);

		JournalStandardSetting journalStandardSetting = (JournalStandardSetting) journalStandardSettingList
				.get(0);
		JournalStandard journalStandard = journalStandardSetting
				.getJournalStandard();
		return journalStandard;
	}

	/*private Unit getBaseUnit() throws Exception {
		Unit baseUnit = DefaultUnit.createDefaultUnit(connection, sessionId);

		return baseUnit;
	}*/
	


	private void saveTransaction(Transaction voidTransaction) throws Exception {
		// save
		AccountingBusinessLogic logic = new AccountingBusinessLogic(connection);
		logic.createTransactionData(sessionId, IDBConstants.MODUL_ACCOUNTING,
				voidTransaction);
	}
	
	private void saveTransactionPosted(TransactionPosted postedVoidTransaction) throws Exception {
		// save
		GenericMapper mapper = MasterMap.obtainMapperFor(TransactionPosted.class);
		mapper.setActiveConn(connection);
		mapper.doInsert(postedVoidTransaction);
		
		AccountingSQLSAP isql = new AccountingSQLSAP();
		long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION_POSTED, connection);
		
		// save the details
		TransactionPostedDetail[] details = postedVoidTransaction.getTransactionPostedDetail();
		mapper = MasterMap.obtainMapperFor(TransactionPostedDetail.class);
		mapper.setActiveConn(connection);
		if(details.length>0){
			for(int i=0; i<details.length; i++){
				TransactionPostedDetail detail = 
					new TransactionPostedDetail(index, details[i]);
				mapper.doInsert(detail);
			}
		}
	}
	
	private void updateStatus(long sessionid, String modul, 
			Transaction transaction) throws Exception {
		GenericMapper mapper;
		
		// update the original table
		// but, get the table first
		
		// 1st, get the standard journal setting
		mapper = MasterMap.obtainMapperFor(JournalStandardSetting.class);
		mapper.setActiveConn(connection);
		List list = 
			mapper.doSelectWhere("JOURNAL=" + transaction.getJournalStandard().getIndex());
		
		if(list.size()==0)
			throw new Exception("There is no journal standard setting related to this transaction");
			
		// 2nd, get the table list based on standard journal setting list
		Iterator iter = list.iterator();
		int counter = 0;
		try {
			while (iter.hasNext()) {
				JournalStandardSetting journalStandardSetting = (JournalStandardSetting) iter
						.next();

				String appName = journalStandardSetting.getApplication();
				List classList = getClassList(appName);

				// 3rd, using the table list, do select based on transaction Id,
				// and you get the data
				Iterator iterator = classList.iterator();
				while (iterator.hasNext()) {
					Class clazz = (Class) iterator.next();
					mapper = MasterMap.obtainMapperFor(clazz);
					mapper.setActiveConn(connection);
					List objectList = (List) mapper
							.doSelectWhere(IDBConstants.ATTR_TRANSACTION + "="
									+ transaction.getIndex());

					// 4th, okay, it's time to update
					Iterator iterator1 = objectList.iterator();
					while (iterator1.hasNext()) {
						StateTemplateEntity orig = (StateTemplateEntity) iterator1
								.next();
						orig.setStatus(Transaction.NOT_SUBMITTED);
						mapper.doUpdate(orig);
						
						// special for employee payroll
						if (orig instanceof EmployeePayroll){
							mapper.doDelete(orig);
						} else if (orig instanceof TaxArt21Submit) {
							mapper.doDelete(orig);
						}
						
						counter++;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception(e);
		}
		
		if(counter==0)
			throw new Exception("Unable to update the transaction status");
		
		
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
