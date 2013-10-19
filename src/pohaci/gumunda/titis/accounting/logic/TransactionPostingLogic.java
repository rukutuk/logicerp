package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.aas.logic.AuthorizationBusinessLogic;
import pohaci.gumunda.aas.logic.AuthorizationException;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.StateTemplateEntity;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.TransactionPosted;
import pohaci.gumunda.titis.accounting.entity.TransactionPostedDetail;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * 
 * @author dark-knight
 *
 */
public class TransactionPostingLogic {
	Connection connection = null;
	
	public TransactionPostingLogic(Connection conn) {
		connection = conn;
	}
	
	public void post(long sessionid, String modul, Transaction transaction, Date postedDate) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				connection);
		
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
				throw new AuthorizationException(
						"Authorization write of module " + modul + " denied");
			}
			
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			// get the transaction posted
			TransactionPosted posted = transaction.createTransactionPosted(postedDate);
			posted.setStatus(Transaction.POSTED);
			
			// save
			GenericMapper mapper = MasterMap.obtainMapperFor(TransactionPosted.class);
			mapper.setActiveConn(connection);
			mapper.doInsert(posted);
			
			AccountingSQLSAP isql = new AccountingSQLSAP();
			long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION_POSTED ,connection);
			
			// save the details
			TransactionPostedDetail[] details = posted.getTransactionPostedDetail();
			mapper = MasterMap.obtainMapperFor(TransactionPostedDetail.class);
			mapper.setActiveConn(connection);
			if(details.length>0){
				for(int i=0; i<details.length; i++){
					TransactionPostedDetail detail = 
						new TransactionPostedDetail(index, details[i]);
					mapper.doInsert(detail);
				}
			}
			
			BeginningBalanceBusinessLogic logic = new BeginningBalanceBusinessLogic(connection, sessionid);
			JournalStandard bbJS = logic.findJournalStandard();
			if (bbJS!=null){
				if(transaction.getJournalStandard().getIndex()!=bbJS.getIndex())
					updateStatus(sessionid, modul, transaction, postedDate);
			}else{
				updateStatus(sessionid, modul, transaction, postedDate);
			}
					
			
			transaction.post(postedDate);
			mapper = MasterMap.obtainMapperFor(Transaction.class);
			mapper.setActiveConn(connection);
			mapper.doUpdate(transaction);
			
			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
				connection.setAutoCommit(true);
				connection.setTransactionIsolation(trans);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		}
	}
	
	public void postOnly(long sessionid, String modul, Transaction transaction, Date postedDate) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				connection);
		
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
				throw new AuthorizationException(
						"Authorization write of module " + modul + " denied");
			}
			
			connection.setAutoCommit(false);
			trans = connection.getTransactionIsolation();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			// get the transaction posted
			TransactionPosted posted = transaction.createTransactionPosted(postedDate);
			posted.setStatus(Transaction.POSTED);
			
			// save
			GenericMapper mapper = MasterMap.obtainMapperFor(TransactionPosted.class);
			mapper.setActiveConn(connection);
			mapper.doInsert(posted);
			
			AccountingSQLSAP isql = new AccountingSQLSAP();
			long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION_POSTED ,connection);
			
			// save the details
			TransactionPostedDetail[] details = posted.getTransactionPostedDetail();
			mapper = MasterMap.obtainMapperFor(TransactionPostedDetail.class);
			mapper.setActiveConn(connection);
			if(details.length>0){
				for(int i=0; i<details.length; i++){
					TransactionPostedDetail detail = 
						new TransactionPostedDetail(index, details[i]);
					mapper.doInsert(detail);
				}
			}
			
			transaction.post(postedDate);
			mapper = MasterMap.obtainMapperFor(Transaction.class);
			mapper.setActiveConn(connection);
			mapper.doUpdate(transaction);
			
			connection.commit();
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(trans);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
				connection.setAutoCommit(true);
				connection.setTransactionIsolation(trans);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		}
	}

	private void updateStatus(long sessionid, String modul, 
			Transaction transaction, Date postedDate) throws Exception {
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
						orig.setStatus(Transaction.POSTED);
						mapper.doUpdate(orig);
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
		
		// update the status
		transaction.post(postedDate);
		mapper = MasterMap.obtainMapperFor(Transaction.class);
		mapper.setActiveConn(connection);
		mapper.doUpdate(transaction);
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
