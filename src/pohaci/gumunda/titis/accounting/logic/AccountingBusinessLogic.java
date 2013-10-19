package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pohaci.gumunda.aas.logic.AuthorizationBusinessLogic;
import pohaci.gumunda.aas.logic.AuthorizationException;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountPayable;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetDetail;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetEntry;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBankDetail;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashAdvance;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashDetail;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEmpReceivable;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningEsDiff;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningLoan;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningWorkInProgress;
import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.accounting.cgui.CreditorList;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.DefaultUnit;
import pohaci.gumunda.titis.accounting.cgui.Journal;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.Report;
import pohaci.gumunda.titis.accounting.cgui.Signature;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.cgui.TransactionPeriod;
import pohaci.gumunda.titis.accounting.cgui.Worksheet;
import pohaci.gumunda.titis.accounting.cgui.WorksheetColumn;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.dbapi.IAccountingSQL;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.AccountPayable;
import pohaci.gumunda.titis.accounting.entity.AccountReceivable;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.ExchangeRate;
import pohaci.gumunda.titis.accounting.entity.PayrollDeptValue;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.entity.TaxArt21DeptValue;
import pohaci.gumunda.titis.accounting.entity.TransactionPosted;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.EmployeePayrollSubmit;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Submit;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;

public class AccountingBusinessLogic {
	Connection m_conn = null;

	public AccountingBusinessLogic(Connection conn) {
		m_conn = conn;
	}
	public Transaction createTransactionData(long sessionid, String modul,Transaction transaction,
			TransactionDetail[] transactionDetail) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
				throw new AuthorizationException(
						"Authorization write of module " + modul + " denied");
			}
			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			IAccountingSQL isql = new AccountingSQLSAP();
			//long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION ,m_conn);
			//MoneyTalk
			isql.createTransaction(m_conn,transaction);
			long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION ,m_conn);
			transaction.setIndex(index);

			for(int i=0;i<transactionDetail.length;i++){
				isql.createTransactionDetail(index,m_conn,transactionDetail[i]);
			}

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return transaction;
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		} finally {
			m_conn.setAutoCommit(true);
		}
	}

	public Transaction createTransactionData(long sessionid, String modul, Transaction transaction) throws Exception {
		// use it if transaction including its details
		return createTransactionData(sessionid, modul, transaction, transaction.getTransactionDetail());
	}

	public Transaction createTransactionPostedData(long sessionid, String modul,Transaction transaction,
			TransactionDetail[] transactionDetail) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
				throw new AuthorizationException(
						"Authorization write of module " + modul + " denied");
			}
			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createTransactionPosted(m_conn,transaction);
			long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION_POSTED ,m_conn);
			transaction.setIndex(index);

			for(int i=0;i<transactionDetail.length;i++){
				isql.createTransactionValuePosted(index,m_conn,transactionDetail[i]);
			}

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);
			return transaction;
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		} finally {
			m_conn.setAutoCommit(true);
		}
	}

	public Account createAccount(long sessionid, String modul, Account account) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createAccount(account, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_ACCOUNT, m_conn);
			String path = "";
			if (account.getParent()==null)
				path = Long.toString(index);
			else
				path = account.getParent().getTreePath() +  index;
			path += "/";
			account.setTreePath(path);
			isql.updateAccount(index,account,m_conn);

			if(account.getParent() != null) {
				isql.createAccountStructure(account.getParent().getIndex(), index, m_conn);
			}

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new Account(index, account);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Account updateAccount(long sessionid, String modul, long index,  Account account) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			String path = "";
			if (account.getParent()==null)
				path = Long.toString(index);
			else
				path = account.getParent().getTreePath() + index;
			path += "/";
			account.setTreePath(path);
			isql.updateAccount(index, account, m_conn);
			return new Account(index, account);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Account[] getSuperAccount(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getSuperAccount(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	//Perubahan Cok Gung
	public Account[] getAllAccount(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllAccount(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	public Account getAccount(long index,long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAccount(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}


	public Account[] getSubAccount(long sessionid, String modul, long superindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getSubAccount(superindex, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteAccount(long sessionid, String modul, Account account) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteAccount(account.getIndex(), m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Activity createActivity(long sessionid, String modul, Activity activity) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createActivity(activity, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_ACTIVITY, m_conn);

			if(activity.getParent() != null)
				isql.createActivityStructure(activity.getParent().getIndex(), index, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new Activity(index, activity.getCode(), activity.getName(), activity.getDescription());
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Activity updateActivity(long sessionid, String modul, long index, Activity activity) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateActivity(index, activity, m_conn);
			return new Activity(index, activity.getCode(), activity.getName(), activity.getDescription());
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Activity[] getSuperActivity(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getSuperActivity(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Activity[] getSubActivity(long sessionid, String modul, long superindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getSubActivity(superindex, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteActivity(long sessionid, String modul, Activity activity) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteActivity(activity.getIndex(), m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Unit createUnit(long sessionid, String modul, Unit unit) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createUnit(unit, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_UNIT, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new Unit(index, unit.getCode(), unit.getDescription());
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Unit updateUnit(long sessionid, String modul, long index, Unit unit) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateUnit(index, unit, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			return new Unit(index, unit.getCode(), unit.getDescription());
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Unit[] getAllUnit(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllUnit(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteUnit(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteUnit(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Journal createJournal(long sessionid, String modul, Journal journal) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createJournal(journal, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_JOURNAL, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new Journal(index, journal.getName());
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Journal updateJournal(long sessionid, String modul, long index, Journal journal) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateJournal(index, journal, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			return new Journal(index, journal.getName());
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Journal[] getAllJournal(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllJournal(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteJournal(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteJournal(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public JournalStandard createJournalStandard(long sessionid, String modul, JournalStandard journal) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createJournalStandard(journal, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_JOURNAL_STANDARD, m_conn);

			if(journal.getParent() != null)
				isql.createJournalStandardStructure(journal.getParent().getIndex(), index, m_conn);

			JournalStandardAccount[] account = journal.getJournalStandardAccount();
			for(int i = 0; i < account.length; i ++)
				isql.createJournalStandardAccount(index, account[i], m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new  JournalStandard(index, journal);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public JournalStandard updateJournalStandard(long sessionid, String modul, long index, JournalStandard journal) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateJournalStandard(index, journal, m_conn);

			JournalStandardAccount[] account = journal.getJournalStandardAccount();
			isql.deleteJournalStandardAccount(index, m_conn);
			for(int i = 0; i < account.length; i ++)
				isql.createJournalStandardAccount(index, account[i], m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);

			return new  JournalStandard(index, journal);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public JournalStandard[] getSuperJournalStandard(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getSuperJournalStandard(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public JournalStandard[] getSubJournalStandard(long sessionid, String modul, long superindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getSubJournalStandard(superindex, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}


	public void deleteJournalStandard(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteJournalStandard(index, m_conn);

		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public JournalStandardAccount[] getJournalStandardAccount(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getJournalStandardAccount(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public JournalStandardAccount[] getJournalStandardAccountNonCalHidd(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getJournalStandardAccountNonCalHidd(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Currency createCurrency(long sessionid, String modul, Currency currency) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createCurrency(currency, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_CURRENCY, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new Currency(index, currency);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Currency updateCurrency(long sessionid, String modul, long index, Currency currency) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateCurrency(index, currency, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			return new Currency(index, currency);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Currency[] getAllCurrency(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllCurrency(m_conn);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public void deleteCurrency(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteCurrency(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public BankAccount createBankAccount(long sessionid, String modul, BankAccount bank) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createBankAccount(bank, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_BANK_ACCOUNT, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new BankAccount(index, bank);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public BankAccount updateBankAccount(long sessionid, String modul, long index, BankAccount bank) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateBankAccount(index, bank, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			return new BankAccount(index, bank);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public BankAccount[] getAllBankAccount(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllBankAccount(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public BankAccount[] getListAllBankAccount(long sessionid, String modul,String query) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getListAllBankAccount(m_conn,query);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public BankAccount getBankAccountByIndex(long sessionid, String modul,long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getBankAccountByIndex(index,m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteBankAccount(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteBankAccount(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public CashAccount getCashAccountByIndex(long sessionid, String modul,long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getCashAccountByIndex(index,m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public CreditorList createCreditorList(long sessionid, String modul, CreditorList bank) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createCreditorList(bank, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_CREDITOR_LIST, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new CreditorList(index, bank);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public CreditorList updateCreditorList(long sessionid, String modul, long index, CreditorList bank) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateCreditorList(index, bank, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			return new CreditorList(index, bank);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public CreditorList[] getAllCreditorList(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllCreditorList(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteCreditorList(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteCreditorList(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public SubsidiaryAccountSetting[] getAllSubsidiaryAccountSetting(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getAllSubsidiaryAccountSetting(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	public SubsidiaryAccountSetting getSubsidiaryAccountSettingByIndex(long sessionid, String modul,long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getSubsidiaryAccountSettingByIndex(m_conn,index);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createSubsidiaryAccountSetting(long sessionid, String modul,SubsidiaryAccountSetting accSetting) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}
			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createSubsidiaryAccountSetting(m_conn,accSetting);
			m_conn.commit();
			m_conn.setAutoCommit(true);

		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void updateSubsidiaryAccountSetting(long sessionid, String modul,SubsidiaryAccountSetting accSetting) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateSubsidiaryAccountSetting(m_conn,accSetting);

			m_conn.commit();
			m_conn.setAutoCommit(true);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteSubsidiaryAccountSetting(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteSubsidiaryAccountSetting(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}



	public CompanyLoan createBankLoan(long sessionid, String modul, CompanyLoan loan) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createCompanyLoan(loan, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_COMPANY_LOAN, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new CompanyLoan(index, loan);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}



	public CompanyLoan updateCompanyLoan(long sessionid, String modul, long index, CompanyLoan loan) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateCompanyLoan(index, loan, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			return new CompanyLoan(index, loan);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public CompanyLoan[] getAllCompanyLoan(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getAllCompanyLoan(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public CompanyLoan[] getListAllCompanyLoan(long sessionid, String modul,String query) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getListAllCompanyLoan(m_conn,query);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public CompanyLoan getCompanyLoan(long sessionid, String modul,long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getCompanyLoan(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteCompanyLoan(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteCompanyLoan(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public CashAccount createCashAccount(long sessionid, String modul, CashAccount cash) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createCashAccount(cash, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_CASH_ACCOUNT, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new CashAccount(index, cash);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public CashAccount updateCashAccount(long sessionid, String modul, long index, CashAccount cash) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateCashAccount(index, cash, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			return new CashAccount(index, cash);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public CashAccount[] getAllCashAccount(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getAllCashAccount(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public CashAccount[] getListAllCashAccount(long sessionid, String modul,String query) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getListAllCashAccount(m_conn,query);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteCashAccount(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteCashAccount(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public PmtCAIOUProject[] getAllPmtCAIOUProject(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllPmtCAIOUProject(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public PmtCAProject[] getAllPmtCAProject(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllPmtCAProject(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public PurchaseReceipt[] getAllPurchaseReceipt(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllPurchaseReceipt(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public PurchaseApPmt[] getAllPurchaseApPmtByReceipt(long sessionid, String modul,String query) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllPurchaseApPmtByReceipt(m_conn,query);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public ExchangeRate createExchangeRate(long sessionid, String modul, ExchangeRate exchange) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createExchangeRate(exchange, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_EXCHANGE_RATE, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new ExchangeRate(index, exchange);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public ExchangeRate updateExchangeRate(long sessionid, String modul, long index, ExchangeRate exchange) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateExchangeRate(index, exchange, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			return new ExchangeRate(index, exchange);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public ExchangeRate[] getAllExchangeRate(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllExchangeRate(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteExchangeRate(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteExchangeRate(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public List createTransactionBlock(long sessionid, String modul, List transactionList) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			AccountingSQLSAP isql = new AccountingSQLSAP();
			int j;
			for (j=0; j<transactionList.size(); j++)
			{
				Transaction transaction = (Transaction) transactionList.get(j);
				isql.createTransaction(m_conn,transaction);
				long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION, m_conn);
				transaction.setIndex(index); // i add this

				TransactionDetail[] detail = transaction.getTransactionDetail();
				for(int i = 0; i < detail.length; i ++)
					isql.createTransactionDetail(index, m_conn, detail[i]);
			}
			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return transactionList;
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}

	}

	public void deleteTransaction(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteTransaction(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	// setting
	public Worksheet createWorksheet(long sessionid, String modul, Worksheet worksheet) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createWorksheet(worksheet, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_WORKSHEET, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new Worksheet(index, worksheet.getName());
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Worksheet[] getAllWorksheet(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllWorksheet(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteWorksheet(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteWorksheet(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public WorksheetColumn createWorksheetColumn(long sessionid, String modul, long worksheetindex, WorksheetColumn column) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createWorksheetColumn(worksheetindex, column, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_WORKSHEET_COLUMN, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new WorksheetColumn(index, column.getName());
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public WorksheetColumn[] getAllWorksheetColumn(long sessionid, String modul, long worksheetindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllWorksheetColumn(worksheetindex, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteWorksheetColumn(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteWorksheetColumn(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createWorksheetJournal(long sessionid, String modul, long columnindex, long journalindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createWorksheetJournal(columnindex, journalindex, m_conn);

		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Journal[] getAllWorksheetJournal(long sessionid, String modul, long columnindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllWorksheetJournal(columnindex, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteWorksheetJournal(long sessionid, String modul, long columnindex, long journalindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteWorksheetJournal(columnindex, journalindex, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Report createBalanceSheetReport(long sessionid, String modul, Report report) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createBalanceSheetReport(report, m_conn);
			long index = isql.getMaxIndex(IDBConstants.TABLE_BALANCE_SHEET_REPORT, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);

			return new Report(index, report.getName());
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Report[] getAllBalanceSheetReport(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllBalanceSheetReport(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteBalanceSheetReport(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteBalanceSheetReport(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createAccountReceivable(long sessionid, String modul, long customerindex, long accountindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createAccountReceivable(customerindex, accountindex, m_conn);

		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public AccountReceivable[] getAllAccountReceivable(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllAccountReceivable(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void updateAccountReceivable(long sessionid, String modul, long customerindex, long accountindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateAccountReceivable(customerindex, accountindex, m_conn);

		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteAccountReceivable(long sessionid, String modul, long customerindex, long accountindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteAccountReceivable(customerindex, accountindex, m_conn);

		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createAccountReceivableDefault(long sessionid, String modul, Account account) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteAccountReceivableDefault(m_conn);
			isql.createAccountReceivableDefault(account.getIndex(), m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Account getAccountReceivableDefault(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAccountReceivableDefault(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createAccountPayable(long sessionid, String modul, AccountPayable payable) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createAccountPayable(payable, m_conn);

		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public AccountPayable[] getAllAccountPayable(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllAccountPayable(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void updateAccountPayable(long sessionid, String modul, AccountPayable payable) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateAccountPayable(payable, m_conn);

		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void deleteAccountPayable(long sessionid, String modul, long customerindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteAccountPayable(customerindex, m_conn);

		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createAccountPayableDefault(long sessionid, String modul, Account account) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteAccountPayableDefault(m_conn);
			isql.createAccountPayableDefault(account.getIndex(), m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Account getAccountPayableDefault(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAccountPayableDefault(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createBaseCurrency(long sessionid, String modul, Currency currency) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteBaseCurrency(m_conn);
			isql.createBaseCurrency(currency.getIndex(), m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
		}
		catch (Exception ex) {
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Currency getBaseCurrency(long sessionid, String modul) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getBaseCurrency(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createSignature(long sessionid, String modul, String app, Signature[] signature) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteSignature(app, m_conn);
			for(int i = 0; i < signature.length; i++)
				isql.createSignature(app, signature[i], m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public Signature[] getSignature(long sessionid, String modul, String app) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getSignature(app,m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createJournalStandardSetting(long sessionid, String modul, String app, JournalStandardSetting[] journal) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.deleteJournalStandardSetting(app, m_conn);
			for(int i = 0; i < journal.length; i++)
				isql.createJournalStandardSetting(app, journal[i], m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	public JournalStandardSetting[] getJournalStandardSetting(long sessionid, String modul, String app,String cashBank) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getJournalStandardSetting(app, m_conn,cashBank);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	// tambahan nunung

	public JournalStandardSetting[] getJournalStandardSetting(long sessionid, String modul, String app) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getJournalStandardSetting(app, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Unit getUnitByIndex(long index) throws Exception{
		try{
			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getUnitByIndex(index,m_conn);
		}
		catch(Exception ex){
			throw new Exception(ex.getMessage());
		}
	}

	public Unit getUnitByDescription(String description) throws Exception {
		try{
			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getUnitByDescription(description,m_conn);
		}
		catch(Exception ex){
			throw new Exception(ex.getMessage());
		}
	}


	public Activity getActivityByIndex(long index)throws Exception{
		try{
			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getActivityByIndex(index,m_conn);
		}
		catch(Exception ex){
			throw new Exception(ex.getMessage());
		}
	}

	public Currency getCurrencyByIndex(long sessionid, String modul, long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getCurrency(index,m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	public BeginningBalanceSheetEntry[] getAllBeginningBalanceSheetEntries(long sessionid, String modul) throws Exception
	{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}
			GenericMapper mapper = getBalanceSheetEntryMapper();
			Class childClass = null;
			m_conn.setAutoCommit(false);
			mapper.setActiveConn(m_conn);
			List list = mapper.doSelectAll();
			BeginningBalanceSheetEntry[] result = new BeginningBalanceSheetEntry[0];
			result = (BeginningBalanceSheetEntry[]) list.toArray(result);
			int i;
			for (i=0; i<result.length; i++)
			{
				childClass = result[i].typeCodeToClass();
				if (childClass!=null) {
					GenericMapper childMapper = MasterMap.obtainMapperFor(childClass);
					childMapper.setActiveConn(m_conn);
					List childList = childMapper.doSelectChildsOf(result[i]);
					BeginningBalanceSheetDetail[] childArray = new BeginningBalanceSheetDetail[0];
					childArray = (BeginningBalanceSheetDetail[]) childList.toArray(childArray);
					int id;
					for (id=0; id<childArray.length; id++)
						childArray[id].preload(this.m_conn,sessionid,modul);
					result[i].setDetails(childArray);
				}
			}
			return result;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		finally {
			m_conn.setAutoCommit(true);

		}
	}
	public BeginningBalanceSheetEntry createBeginningBalanceSheetEntry(long sessionid, String modul, BeginningBalanceSheetEntry balanceSheet) throws Exception
	{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}
			GenericMapper mapper = getBalanceSheetEntryMapper();
			Class childClass = null;
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			m_conn.setAutoCommit(false);
			mapper.setActiveConn(m_conn);
			balanceSheet.beforeSave();
			mapper.doInsert(balanceSheet);
			childClass = balanceSheet.typeCodeToClass();
			if (childClass!= null)
			{
				GenericMapper childMapper = MasterMap.obtainMapperFor(childClass);
				childMapper.setActiveConn(m_conn);
				childMapper.doDeleteChildsOf(balanceSheet);
				int i;
				BeginningBalanceSheetDetail[] details = balanceSheet.getDetails();
				for (i=0; i< details.length; i++)
				{
					childMapper.doInsert(details[i],balanceSheet);
				}
			}
			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex);
		}

		return balanceSheet;
	}
	private GenericMapper getBalanceSheetEntryMapper() {
		GenericMapper mapper = MasterMap.obtainMapperFor(BeginningBalanceSheetEntry.class);
		return mapper;
	}

	public BeginningBalance createBeginningBalance(long sessionid, String modul, BeginningBalance balance) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createBeginningBalance(balance, m_conn);
			m_conn.commit();
			m_conn.setTransactionIsolation(trans);
			return balance;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());

		}

	}
	public void deleteBeginningBalanceSheetEntry(long sessionid, String modul, long index, BeginningBalanceSheetEntry balanceSheet)
	{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}
			GenericMapper mapper = getBalanceSheetEntryMapper();
			Class childClass = null;
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			m_conn.setAutoCommit(false);
			mapper.setActiveConn(m_conn);
			childClass = balanceSheet.typeCodeToClass();
			if (childClass!= null)
			{
				GenericMapper childMapper = MasterMap.obtainMapperFor(childClass);
				childMapper.setActiveConn(m_conn);
				childMapper.doDeleteChildsOf(balanceSheet);
			}
			mapper.doDelete(balanceSheet);
			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new RuntimeException(ex);
		}



	}
	public BeginningBalanceSheetEntry updateBeginningBalanceSheetEntry(long sessionid, String modul, long index, BeginningBalanceSheetEntry balanceSheetEntry)
	throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}
			GenericMapper mapper = getBalanceSheetEntryMapper();
			Class childClass = null;
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			m_conn.setAutoCommit(false);
			mapper.setActiveConn(m_conn);
			balanceSheetEntry.beforeSave();
			mapper.doUpdate(balanceSheetEntry);
			childClass = balanceSheetEntry.typeCodeToClass();
			if (childClass!= null){
				GenericMapper childMapper = MasterMap.obtainMapperFor(childClass);
				childMapper.setActiveConn(m_conn);
				childMapper.doDeleteChildsOf(balanceSheetEntry);
				int i;
				BeginningBalanceSheetDetail[] details = balanceSheetEntry.getDetails();
				for (i=0; i< details.length; i++)
					childMapper.doInsert(details[i],balanceSheetEntry);
			}
			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new RuntimeException(ex);
		}

		return balanceSheetEntry;

	}
	public BeginningBalance updateBeginningBalance(long sessionid, String modul, long index, BeginningBalance balance) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_UPDATE)){
				throw new AuthorizationException("Authorization update of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateBeginningBalance(index, balance, m_conn);
			return balance;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}
	public BeginningBalance getBeginningBalance(long sessionid, String modul, short type) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			BeginningBalance beginningBalance = isql.getBeginningBalance(type, m_conn);
			return beginningBalance;
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public BeginningBalanceSheetEntry[] getBeginningBalanceSheetView(long sessionid, String modul, long index, short category) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getBeginningBalanceSheetView(index, category, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public TransactionDetail[] getBeginningBalanceSheet(long sessionid, String modul, long index, short category) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getBeginningBalanceSheet(index, category, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void submitBeginningBalance(long sessionid, String modul, java.util.Date transdate,
			Journal journal, BeginningBalance balance) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)){
				throw new AuthorizationException("Authorization write of module " + modul + " denied");
			}

			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			IAccountingSQL isql = new AccountingSQLSAP();

			Transaction newtrans =
				new Transaction(BeginningBalanceSheetDetail.BALANCE_SHEET,
						transdate, null, null, "", journal, null, (short)0, null);
			isql.createTransaction(m_conn,newtrans);
			long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION, m_conn);

			isql.deleteTransactionDetail(index, m_conn);
			balance.setTrans(newtrans);
			isql.updateBeginningBalance(balance.getIndex(), balance, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try{
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			}
			catch(Exception e){}
			throw new Exception(ex.getMessage());
		}
	}

	private List selectChildOfBeginningBalance(GenericMapper m,long parentIndex){
		return
		m.doSelectWhere(" " + IDBConstants.ATTR_BEGINNING_BALANCE + " = ? ",new Object[] { new Long(parentIndex) });
	}

	public BeginningAccountPayable[] getBeginningAccountPayableView(long sessionid, String modul, long parentIndex)	{
		GenericMapper genericMapper =
			MasterMap.obtainMapperFor(BeginningAccountPayable.class);
		List list=selectChildOfBeginningBalance(genericMapper,parentIndex);
		BeginningAccountPayable[] accountPayables =
			new BeginningAccountPayable[list.size()];
		return (BeginningAccountPayable[]) list.toArray(accountPayables);
	}

	public BeginningAccountReceivable[] getBeginningAccountReceivableView(long sessionid, String modul, long parentIndex){
		GenericMapper genericMapper =
			MasterMap.obtainMapperFor(BeginningAccountReceivable.class);
		List list=selectChildOfBeginningBalance(genericMapper,parentIndex);
		BeginningAccountReceivable[] accountReceivables =
			new BeginningAccountReceivable[list.size()];
		return (BeginningAccountReceivable[]) list.toArray(accountReceivables);
	}

	public BeginningEmpReceivable[] getBeginningEmpReceivableView(long sessionid, String modul, long parentIndex){
		GenericMapper genericMapper =
			MasterMap.obtainMapperFor(BeginningEmpReceivable.class);
		List list= selectChildOfBeginningBalance(genericMapper,parentIndex);
		BeginningEmpReceivable[] accountEmpReceivables =
			new BeginningEmpReceivable[list.size()];
		return (BeginningEmpReceivable[]) list.toArray(accountEmpReceivables);
	}

	public BeginningCashAdvance[] getBeginningCashInAdvanceView(long sessionid, String modul, long parentIndex){
		GenericMapper genericMapper =
			MasterMap.obtainMapperFor(BeginningCashAdvance.class);
		List list=selectChildOfBeginningBalance(genericMapper,parentIndex);
		BeginningCashAdvance[] cashInAdvances =
			new BeginningCashAdvance[list.size()];
		return (BeginningCashAdvance[]) list.toArray(cashInAdvances);
	}

	public BeginningBankDetail[] getBeginningBankView(long sessionid, String modul, long index, long unitindex) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}
			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getBeginningBankView(index, unitindex, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}


	public Transaction getTransaction(long sessionid, String modul,long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}
			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getTransaction(m_conn, index);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public Transaction[] getTransactionByUnit(long sessionid, String modul,long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}
			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getTransactionByUnit(m_conn, index);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

	public TransactionPosted[] getTransactionCriteria(long sessionid, String modul,String query) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}
			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getTransactionCriteria(m_conn, query);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		}
	}

	public TransactionDetail[] getTransactionDetail(long sessionid, String modul,long index) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getTransactionDetail(index,m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getLastReferenceNo(long sessionid, String modul, String likeClausa) throws Exception {
		try {
			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getLastReferenceNo(m_conn, likeClausa);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public String getLastReferenceNoByYear(long sessionid, String modul,
			int year, String likeClausa) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getLastReferenceNoByYear(m_conn, year, likeClausa);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public double getDebitValue(long sessionid, String modul,String query) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getDebitValue(m_conn,query);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createDefaultUnit(long sessionid, String modul,DefaultUnit dunit) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createDefaultUnit(dunit, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	public DefaultUnit[] getAllDefaultUnit(long sessionid, String modul) throws Exception{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getAllDefaultUnit(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	public DefaultUnit getDefaultUnit(long sessionid, String modul) throws Exception{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getDefaultUnit(m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	public void updateDefaultUnit(int index, DefaultUnit dUnit, long sessionid, String modul) throws Exception{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.updateDefaultUnit(index, dUnit, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createTransactionPeriod(long sessionid, String modul,TransactionPeriod tPeriod) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createTransactionPeriod(tPeriod, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createTransaction(Transaction trans, String modul, long sessionid)throws Exception{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createTransaction(m_conn, trans);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	public void createTransactionPosted(Transaction trans, String modul, long sessionid)throws Exception{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createTransactionPosted(m_conn, trans);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	public void createTransactionDetail(long index,TransactionDetail detail, String modul,
			long sessionid)throws Exception{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createTransactionDetail(index, m_conn, detail);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}

	}

	public void createTransactionValuePosted(long index,TransactionDetail detail, String modul,
			long sessionid)throws Exception{
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createTransactionValuePosted(index, m_conn, detail);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}

	}

	public Journal getJournal(long index, String modul, long sessionid) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.getJournal(index, m_conn);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public boolean findReferenceNo(long sessionId, String modul, String refNo) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionId, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			return isql.findReferenceNo(m_conn, refNo);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}
	public CompanyLoan[] getCompanyLoanByAccount(long sessionid, String modul, Account account) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);

		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			AccountingSQLSAP isql = new AccountingSQLSAP();
			return isql.getCompanyLoanByAccount(m_conn, account);
		}
		catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public void createPayrollDeptValue(long sessionid, String modul, PayrollDeptValue payrollDeptValue){
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createPayrollDeptValue(m_conn,payrollDeptValue);
		}
		catch (Exception ex) {
			try {
				throw new Exception(ex.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void createTaxArtDeptValue(long sessionid, String modul, TaxArt21DeptValue taxArt21DeptValue){
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(m_conn);
		try {
			if(!autho.isAuthorized(sessionid, modul, pohaci.gumunda.aas.dbapi.IDBConstants.ATT_READ)){
				throw new AuthorizationException("Authorization read of module " + modul + " denied");
			}

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createTaxtArtDeptValue(m_conn,taxArt21DeptValue);
		}
		catch (Exception ex) {
			try {
				throw new Exception(ex.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @param sessionid
	 * @param modulAccounting
	 * @param index
	 * @param detail
	 */
	public void updateBeginningBalanceSheetDetail(long sessionid,
			String modulAccounting, long index,
			BeginningBalanceSheetDetail detail) {

		AccountingSQLSAP sql = new AccountingSQLSAP();

		String table;
		if(detail.getClass()==BeginningCashDetail.class)
			table = IDBConstants.TABLE_BEGINNING_CASH_DETAIL;
		else if (detail.getClass()==BeginningBankDetail.class)
			table = IDBConstants.TABLE_BEGINNING_BANK_DETAIL;
		else if (detail.getClass()==BeginningAccountPayable.class)
			table = IDBConstants.TABLE_BEGINNING_ACCOUNTPAYABLE;
		else if (detail.getClass()==BeginningAccountReceivable.class)
			table = IDBConstants.TABLE_BEGINNING_ACCOUNT_RECEIVABLE;
		else if (detail.getClass()==BeginningCashAdvance.class)
			table = IDBConstants.TABLE_BEGINNING_CASHADVANCE;
		else if (detail.getClass()==BeginningEmpReceivable.class)
			table = IDBConstants.TABLE_BEGINNING_EMPRECV;
		else if (detail.getClass()==BeginningEsDiff.class)
			table = IDBConstants.TABLE_BEGINNING_ESDIFF;
		else if (detail.getClass()==BeginningLoan.class)
			table = IDBConstants.TABLE_BEGINNING_LOAN;
		else if (detail.getClass()==BeginningWorkInProgress.class)
			table = IDBConstants.TABLE_BEGINNING_WORK_IN_PROGRESS;
		else
			table = "";

		try {
			if (!table.equals(""))
				sql.updateBeginningBalanceSheetDetail(m_conn, table, index, detail);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param sessionid
	 * @param modulAccounting
	 * @param index
	 * @param entry
	 */
	public void updateBeginningBalanceSheetEntryTrans(long sessionid,
			String modulAccounting, long index, BeginningBalanceSheetEntry entry) {

		AccountingSQLSAP sql = new AccountingSQLSAP();
		try {
			sql.updateBeginningBalanceSheetDetail(m_conn, index, entry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void createPayrollTransactionData(long sessionid,
			String modul, Transaction transaction,
			TransactionDetail[] transactionDetail,
			EmployeePayrollSubmit employeePayrollSubmit) throws Exception {

		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
				throw new AuthorizationException(
						"Authorization write of module " + modul + " denied");
			}
			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createTransaction(m_conn,transaction);
			long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION ,m_conn);
			transaction.setIndex(index);

			for(int i=0;i<transactionDetail.length;i++){
				isql.createTransactionDetail(index,m_conn,transactionDetail[i]);
			}

			HRMSQLSAP hsql = new HRMSQLSAP();
			hsql.updateEmployeePayrollVerified(index, employeePayrollSubmit, m_conn);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		} finally {
			m_conn.setAutoCommit(true);
		}
	}
	public void createTaxTransactionData(long sessionid,
			String modul, Transaction transaction,
			TransactionDetail[] transactionDetail, TaxArt21Submit taxSubmitted) throws Exception {
		AuthorizationBusinessLogic autho = new AuthorizationBusinessLogic(
				m_conn);
		int trans = Connection.TRANSACTION_READ_COMMITTED;
		try {
			if (!autho.isAuthorized(sessionid, modul,
					pohaci.gumunda.aas.dbapi.IDBConstants.ATT_CREATE)) {
				throw new AuthorizationException(
						"Authorization write of module " + modul + " denied");
			}
			m_conn.setAutoCommit(false);
			trans = m_conn.getTransactionIsolation();
			m_conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			IAccountingSQL isql = new AccountingSQLSAP();
			isql.createTransaction(m_conn,transaction);
			long index = isql.getMaxIndex(IDBConstants.TABLE_TRANSACTION ,m_conn);
			transaction.setIndex(index);

			for(int i=0;i<transactionDetail.length;i++){
				isql.createTransactionDetail(index,m_conn,transactionDetail[i]);
			}

			HRMSQLSAP hsql = new HRMSQLSAP();
			hsql.updateTaxArt21Submit(m_conn, transaction, taxSubmitted);

			m_conn.commit();
			m_conn.setAutoCommit(true);
			m_conn.setTransactionIsolation(trans);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				m_conn.rollback();
				m_conn.setAutoCommit(true);
				m_conn.setTransactionIsolation(trans);
			} catch (Exception e) {
			}
			throw new Exception(ex.getMessage());
		} finally {
			m_conn.setAutoCommit(true);
		}
	}



}