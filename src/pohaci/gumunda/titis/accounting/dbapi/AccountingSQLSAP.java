package pohaci.gumunda.titis.accounting.dbapi;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetDetail;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalanceSheetEntry;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBankDetail;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningCashDetail;
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
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.AccountPayable;
import pohaci.gumunda.titis.accounting.entity.AccountReceivable;
import pohaci.gumunda.titis.accounting.entity.Activity;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.ExchangeRate;
import pohaci.gumunda.titis.accounting.entity.PayrollDeptValue;
import pohaci.gumunda.titis.accounting.entity.TaxArt21DeptValue;
import pohaci.gumunda.titis.accounting.entity.PmtCAIOUProject;
import pohaci.gumunda.titis.accounting.entity.PmtCAProject;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.accounting.entity.TransactionPosted;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.Formula;
import pohaci.gumunda.titis.application.FormulaEntity;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.Formula.TokenParser;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;
import pohaci.gumunda.titis.hrm.cgui.SimpleEmployee;
import pohaci.gumunda.titis.hrm.cgui.TimeSheetSummary;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL;
import pohaci.gumunda.titis.hrm.dbapi.IHRMSQL.IFormulaParser;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;
import pohaci.gumunda.titis.hrm.logic.OrganizationLogic;
import pohaci.gumunda.titis.hrm.logic.PayrollTokenParser;
import pohaci.gumunda.titis.project.dbapi.ProjectSQLSAP;
import pohaci.gumunda.titis.project.logic.ProjectBusinessLogic;
import pohaci.gumunda.util.OtherSQLException;

public class AccountingSQLSAP implements IAccountingSQL {
    Calendar oneCalendar = Calendar.getInstance();
    public BigDecimal calcIncomeStmt(Account acct, java.util.Date fromDate, java.util.Date toDate, String journalList, Connection conn)
    {
    	NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
        PreparedStatement stm = null;
        Date fromSqlDate = utilToSqlDate(fromDate);
        Date toSqlDate = utilToSqlDate(toDate);
        String query = "SELECT SUM("
            + IDBConstants.ATTR_EXCHANGE_RATE + "*"
            + IDBConstants.ATTR_VALUE + "*(1-2*" + IDBConstants.ATTR_BALANCE_CODE
            + ")) debitvalue FROM " + IDBConstants.TABLE_TRANSACTION_VALUE_POSTED
            + " tv, " + IDBConstants.TABLE_ACCOUNT + " ac, "
            + IDBConstants.TABLE_TRANSACTION_POSTED  + " tp WHERE ac.autoindex = tv."
            +  IDBConstants.ATTR_ACCOUNT + " AND tp.autoindex=" +
            IDBConstants.ATTR_TRANSACTION_POSTED +
            " AND tp.JOURNAL IN (" + journalList + ")" +
            " AND ac.TREEPATH LIKE ? AND tp.TRANSACTIONDATE BETWEEN ? AND ?";
//      POSTED DATE atau TRANSACTION DATE nih?
        System.err.print(query);

        try {
            stm = conn.prepareStatement(query);
            stm.setString(1,acct.getTreePath() + "%");
            stm.setDate(2,(java.sql.Date) fromSqlDate);
            stm.setDate(3,(java.sql.Date) toSqlDate);
            ResultSet rs = stm.executeQuery();
            if (rs.next())
            {
                BigDecimal num = rs.getBigDecimal(1);
                if (num!=null) {
                	num = nr.round(num);
                    return num;
                }
            }

            return BigDecimal.valueOf(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.valueOf(0);
    }
	/**
	 * Calculates the balance of an account (and all other sub accounts in CoA tree)
	 * on some point of time (onDate). Positive if the resulting balance is debit side.
	 * @param acct account in question
	 * @param onDate point of time
	 * @param journals
	 * @param conn sql Connection used in the query
	 * @return positive BigDecimal if the resulting balance is in debit side, negative if it
	 * is in credit side
	 */
	public BigDecimal calcBalance(Account acct, java.util.Date onDate, String journals, Connection conn)
	{
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		PreparedStatement stm = null;
		oneCalendar.setTime(onDate);
		java.sql.Date sqlDate = utilToSqlDate(onDate);
		String query = "SELECT SUM("
			+ IDBConstants.ATTR_EXCHANGE_RATE + "*"
			+ IDBConstants.ATTR_VALUE + "*(1-2*" + IDBConstants.ATTR_BALANCE_CODE
			+ ")) debitvalue FROM " + IDBConstants.TABLE_TRANSACTION_VALUE_POSTED
			+ " tv, " + IDBConstants.TABLE_ACCOUNT + " ac, "
			+ IDBConstants.TABLE_TRANSACTION_POSTED  + " tp WHERE ac.autoindex = tv."
			+  IDBConstants.ATTR_ACCOUNT + " AND tp.autoindex=" +
			IDBConstants.ATTR_TRANSACTION_POSTED +
			" AND tp.JOURNAL IN (" + journals + ")" +
			" AND ac.TREEPATH LIKE ? AND tp.TRANSACTIONDATE <= ?";
		// POSTED DATE atau TRANSACTION DATE nih?
		try {
			stm = conn.prepareStatement(query);
			stm.setString(1, acct.getTreePath() + "%");
			stm.setDate(2, sqlDate, oneCalendar);
			ResultSet rs = stm.executeQuery();
            if (rs.next())
            {
                BigDecimal num = rs.getBigDecimal(1);
                if (num!=null) {
                	num = nr.round(num);
                    return num;
                }
            }

            return BigDecimal.valueOf(0);
		}
		catch (SQLException x)
		{
			x.printStackTrace();
			return BigDecimal.valueOf(0);
		}
	}
	/**
	 * Calculates the balance of an account (and all other sub accounts in CoA tree)
	 * on some point of time (onDate). Positive if the resulting balance is debit side.
	 * @param acct account in question
	 * @param onDate point of time
	 * @param journals
	 * @param conn sql Connection used in the query
	 * @return positive BigDecimal if the resulting balance is in debit side, negative if it
	 * is in credit side
	 */
	public BigDecimal calcStartAltBalance(Account acct, java.util.Date onDate, String journals, Connection conn)
	{
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		PreparedStatement stm = null;
		oneCalendar.setTime(onDate);
		java.sql.Date sqlDate = utilToSqlDate(onDate);
		String query = "SELECT SUM("
			+ IDBConstants.ATTR_EXCHANGE_RATE + "*"
			+ IDBConstants.ATTR_VALUE + "*(1-2*" + IDBConstants.ATTR_BALANCE_CODE
			+ ")) debitvalue FROM " + IDBConstants.TABLE_TRANSACTION_VALUE_POSTED
			+ " tv, " + IDBConstants.TABLE_ACCOUNT + " ac, "
			+ IDBConstants.TABLE_TRANSACTION_POSTED  + " tp WHERE ac.autoindex = tv."
			+  IDBConstants.ATTR_ACCOUNT + " AND tp.autoindex=" +
			IDBConstants.ATTR_TRANSACTION_POSTED +
			" AND tp.JOURNAL IN (" + journals + ")" +
			" AND ac.TREEPATH LIKE ? AND tp.TRANSACTIONDATE < ?";
		// POSTED DATE atau TRANSACTION DATE nih?
		try {
			stm = conn.prepareStatement(query);
			stm.setString(1, acct.getTreePath() + "%");
			stm.setDate(2, sqlDate, oneCalendar);
			ResultSet rs = stm.executeQuery();
            if (rs.next())
            {
                BigDecimal num = rs.getBigDecimal(1);
                if (num!=null) {
                	num = nr.round(num);
                    return num;
                }
            }

            return new BigDecimal(0);
		}
		catch (SQLException x)
		{
			x.printStackTrace();
			return new BigDecimal(0);
		}
	}
    private java.sql.Date utilToSqlDate(java.util.Date onDate) {
        java.sql.Date sqlDate = new java.sql.Date(onDate.getTime());
        return sqlDate;
    }
	public long getMaxIndex(String table, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			String query = "SELECT MAX(" + IDBConstants.ATTR_AUTOINDEX +
			") as maxindex FROM " + table;
			ResultSet rs = stm.executeQuery(query);
			rs.next();
			return rs.getLong("maxindex");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}


	public void createTransaction(Connection conn,
			Transaction transaction) throws SQLException {
		PreparedStatement stm = null;
		String query="INSERT INTO "
			+IDBConstants.TABLE_TRANSACTION + "("
			+IDBConstants.ATTR_TRANSACTION_CODE  + "," //1
			+IDBConstants.ATTR_DESCRIPTION  + "," //2
			+IDBConstants.ATTR_TRANSACTION_DATE  + "," //3
			+IDBConstants.ATTR_VERIFICATION_DATE  + "," //4
			+IDBConstants.ATTR_POSTED_DATE  + "," //5
			+IDBConstants.ATTR_REFERENCE_NO  + "," //6
			+IDBConstants.ATTR_JOURNAL  + "," //7
			+IDBConstants.ATTR_STATUS + ","//8
			+IDBConstants.ATTR_UNIT + ","//9
			+IDBConstants.ATTR_ISVOID //10
			+")" + " values (? , ? , ? , ? , ? , ? , ? , ?, ?, ?)";

		try {
			stm = conn.prepareStatement(query);

			stm.setLong(1, transaction.getJournalStandard().getIndex());
			stm.setString(2, transaction.getDescription());
			stm.setDate(3, new java.sql.Date(transaction.getTransDate().getTime()));
			stm.setDate(4,  new java.sql.Date(transaction.getVerifyDate().getTime()));
			if(transaction.getPostedDate()!=null)
				stm.setDate(5, new java.sql.Date(transaction.getPostedDate().getTime()));
			else
				stm.setNull(5, Types.LONGVARCHAR);
			stm.setString(6, transaction.getReference());
			//if(transaction.getJournal()!=null)
			stm.setLong(7, transaction.getJournal().getIndex());
			//else
			//	stm.setNull(7,Types.LONGVARCHAR);
			stm.setShort(8, transaction.getStatus());
			stm.setLong(9, transaction.getUnit().getIndex());
			stm.setBoolean(10, transaction.isVoid());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTransactionPosted(Connection conn,
			Transaction transaction) throws SQLException {
		PreparedStatement stm = null;
		String query="INSERT INTO "
			+IDBConstants.TABLE_TRANSACTION_POSTED + "("
			+IDBConstants.ATTR_TRANSACTION_CODE  + "," //1
			+IDBConstants.ATTR_DESCRIPTION  + "," //2
			+IDBConstants.ATTR_TRANSACTION_DATE  + "," //3
			+IDBConstants.ATTR_VERIFICATION_DATE  + "," //4
			+IDBConstants.ATTR_POSTED_DATE  + "," //5
			+IDBConstants.ATTR_REFERENCE_NO  + "," //6
			+IDBConstants.ATTR_JOURNAL  + "," //7
			+IDBConstants.ATTR_STATUS + ","//8
			+IDBConstants.ATTR_UNIT + ","//9
			+IDBConstants.ATTR_ISVOID //10
			+")" + " values (? , ? , ? , ? , ? , ? , ? , ?, ?, ?)";

		try {
			stm = conn.prepareStatement(query);

			stm.setLong(1, transaction.getJournalStandard().getIndex());
			stm.setString(2, transaction.getDescription());
			stm.setDate(3, new java.sql.Date(transaction.getTransDate().getTime()));
			stm.setDate(4,  new java.sql.Date(transaction.getVerifyDate().getTime()));
			if(transaction.getPostedDate()!=null)
				stm.setDate(5, new java.sql.Date(transaction.getPostedDate().getTime()));
			else
				stm.setNull(5, Types.LONGVARCHAR);
			stm.setString(6, transaction.getReference());
			//if(transaction.getJournal()!=null)
			stm.setLong(7, transaction.getJournal().getIndex());
			//else
			//	stm.setNull(7,Types.LONGVARCHAR);
			stm.setShort(8, transaction.getStatus());
			stm.setLong(9, transaction.getUnit().getIndex());
			stm.setBoolean(10, transaction.isVoid());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTransactionDetail(long index,Connection conn,
			TransactionDetail transactionDetail) throws SQLException {
		PreparedStatement stm = null;

		try {
			String strQuery = "INSERT INTO "
				+IDBConstants.TABLE_TRANSACTION_VALUE + "("
				+IDBConstants.ATTR_TRANSACTION  + "," //1
				+IDBConstants.ATTR_ACCOUNT  + "," //2
				+IDBConstants.ATTR_VALUE  + "," //3
				+IDBConstants.ATTR_CURRENCY + "," //4
				+IDBConstants.ATTR_EXCHANGE_RATE + ","//5
				+IDBConstants.ATTR_UNIT + "," //6
				+IDBConstants.ATTR_SUBSIDIARY_ACCOUNT //7
				+")" + " values (?, ?, ?, ?, ?, ?, ?)";
			stm = conn.prepareStatement(strQuery);

			stm.setLong(1, index);
			stm.setLong(2, transactionDetail.getAccount().getIndex());
			stm.setDouble(3, transactionDetail.getValue() );
			//stm.setLong(4,1);// test
			stm.setLong(4,transactionDetail.getCurrency().getIndex());
			stm.setDouble(5, transactionDetail.getExchangeRate());
			stm.setLong(6, transactionDetail.getUnit().getIndex());
			stm.setLong(7, transactionDetail.getSubsidiaryAccount());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTransactionValuePosted(long index,Connection conn,
			TransactionDetail transactionDetail) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO "
					+IDBConstants.TABLE_TRANSACTION_VALUE_POSTED + "("
					+IDBConstants.ATTR_TRANSACTION_POSTED  + "," //1
					+IDBConstants.ATTR_ACCOUNT  + "," //2
					+IDBConstants.ATTR_VALUE  + "," //3
					+IDBConstants.ATTR_CURRENCY + "," //4
					+IDBConstants.ATTR_EXCHANGE_RATE + ","//5
					+IDBConstants.ATTR_UNIT + "," //6
					+IDBConstants.ATTR_SUBSIDIARY_ACCOUNT //7
					+")" + " values (?, ?, ?, ?, ?, ?, ?)");

			stm.setLong(1, index);
			stm.setLong(2, transactionDetail.getAccount().getIndex());
			stm.setDouble(3, transactionDetail.getValue() );
			//stm.setLong(4,1);// test
			stm.setLong(4,transactionDetail.getCurrency().getIndex());
			stm.setDouble(5, transactionDetail.getExchangeRate());
			stm.setLong(6, transactionDetail.getUnit().getIndex());
			stm.setLong(7, transactionDetail.getSubsidiaryAccount());
			stm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createAccount(Account account, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_ACCOUNT + "(" +
					IDBConstants.ATTR_ACCOUNT_CODE + "," +
					IDBConstants.ATTR_ACCOUNT_NAME + "," +
					IDBConstants.ATTR_CATEGORY + "," +
					IDBConstants.ATTR_IS_GROUP + "," +
					IDBConstants.ATTR_BALANCE_CODE + "," +
					IDBConstants.ATTR_NOTE + "," +
                    IDBConstants.ATTR_PATH +
                    ")" +
			" values (?, ?, ?, ?, ?, ?, ?)");

			int col = 1;
			stm.setString(col++, account.getCode());
			stm.setString(col++, account.getName());
			stm.setShort(col++, account.getCategory());
			stm.setBoolean(col++, account.isGroup());
			stm.setShort(col++, account.getBalance());
			stm.setString(col++, account.getNote());
			stm.setString(col++, account.getTreePath());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateAccount(long index, Account account, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_ACCOUNT + " SET " +
					IDBConstants.ATTR_ACCOUNT_CODE + "=?," +
					IDBConstants.ATTR_ACCOUNT_NAME + "=?," +
					IDBConstants.ATTR_CATEGORY + "=?," +
					IDBConstants.ATTR_IS_GROUP + "=?," +
					IDBConstants.ATTR_BALANCE_CODE + "=?," +
					IDBConstants.ATTR_NOTE + "=?," +
                    IDBConstants.ATTR_PATH + "=?" +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			int col = 1;
			stm.setString(col++, account.getCode());
			stm.setString(col++, account.getName());
			stm.setShort(col++, account.getCategory());
			stm.setBoolean(col++, account.isGroup());
			stm.setShort(col++, account.getBalance());
			stm.setString(col++, account.getNote());
			stm.setString(col++, account.getTreePath());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteAccount(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_ACCOUNT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Account[] getAllAccount(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_ACCOUNT);

			while(rs.next()) {
				vresult.addElement(new Account(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_ACCOUNT_CODE),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NAME),
						rs.getShort(IDBConstants.ATTR_CATEGORY),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE),
						rs.getString(IDBConstants.ATTR_NOTE),
                        rs.getString(IDBConstants.ATTR_PATH)));
			}

			Account[] result = new Account[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Account getAccount(long index,Connection conn) throws SQLException {
		Statement stm = null;
		//Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_ACCOUNT+
					" WHERE "+IDBConstants.ATTR_AUTOINDEX+ " = "+index);

			if(rs.next()) {
				return new Account(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_ACCOUNT_CODE),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NAME),
						rs.getShort(IDBConstants.ATTR_CATEGORY),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE),
						rs.getString(IDBConstants.ATTR_NOTE),
                        rs.getString(IDBConstants.ATTR_PATH)
                        );
			}
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createJournal(Journal journal, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_JOURNAL +
					"(" + IDBConstants.ATTR_NAME + ") values ('" + journal.getName() + "')" );
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Journal[] getAllJournal(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_JOURNAL);

			while(rs.next()) {
				vresult.addElement(new Journal(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME)));
			}

			Journal[] result = new Journal[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Journal getJournal(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_JOURNAL +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			if(rs.next())
				return new Journal(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME));
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateJournal(long index, Journal journal, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("UPDATE " + IDBConstants.TABLE_JOURNAL + " SET " +
					IDBConstants.ATTR_NAME + "='" + journal.getName() + "'" +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteJournal(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_JOURNAL +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createJournalStandard(JournalStandard journal, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_JOURNAL_STANDARD + "(" +
					IDBConstants.ATTR_CODE + "," +
					IDBConstants.ATTR_DESCRIPTION + "," +
					IDBConstants.ATTR_IS_GROUP + "," +
					IDBConstants.ATTR_JOURNAL + ")" +
			" values (?, ?, ?, ?)");
			stm.setString(1, journal.getCode());
			stm.setString(2, journal.getDescription());
			stm.setBoolean(3, journal.isGroup());
			if(journal.getJournal() != null)
				stm.setLong(4, journal.getJournal().getIndex());
			else
				stm.setNull(4, Types.LONGVARCHAR);
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createJournalStandardStructure(long superjournal, long subjournal, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_JOURNAL_STANDARD_STRUCTURE +
					" values(" + superjournal + "," + subjournal + ")");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	/*public JournalStandard[] getAllJournalStandard(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT s.*, j." + IDBConstants.ATTR_NAME + " FROM " + IDBConstants.TABLE_JOURNAL_STANDARD  + " s LEFT JOIN " +
		IDBConstants.TABLE_JOURNAL + " j ON s." + IDBConstants.ATTR_JOURNAL + "=j." + IDBConstants.ATTR_AUTOINDEX +
		" where s."+IDBConstants.ATTR_CODE+" like upper('MJ%') and j." + IDBConstants.ATTR_NAME + "='General Journal' order by j." + IDBConstants.ATTR_AUTOINDEX;
		System.err.println(query);
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				Journal journal = null;
				if(rs.getString(IDBConstants.ATTR_NAME) != null)
					journal = new Journal(rs.getLong(IDBConstants.ATTR_JOURNAL), rs.getString(IDBConstants.ATTR_NAME));
				vresult.addElement(new JournalStandard(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE), rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP), journal));
			}

			JournalStandard[] result = new JournalStandard[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}*/

	public JournalStandard getJournalStandardByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try{
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT s.*, j." + IDBConstants.ATTR_NAME + " FROM " + IDBConstants.TABLE_JOURNAL_STANDARD + " s LEFT JOIN " +
					IDBConstants.TABLE_JOURNAL + " j ON s." + IDBConstants.ATTR_JOURNAL + "=j." + IDBConstants.ATTR_AUTOINDEX +
					" WHERE s." + IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if(rs.next()) {
				Journal journal = null;
				if(rs.getString(IDBConstants.ATTR_NAME) != null)
					journal = new Journal(rs.getLong(IDBConstants.ATTR_JOURNAL), rs.getString(IDBConstants.ATTR_NAME));
				return new JournalStandard(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
						journal);
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally{
			if(stm != null) {
				stm.close();
			}
		}
	}

	/*public JournalStandardAccount[] getJournalStandardAccountByCalculate(long index, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "select * from journalstandardaccount where journal = " + index + " and calculate=true";
		try{
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				vresult.addElement(new JournalStandardAccount(
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT),conn),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE),
						rs.getBoolean(IDBConstants.ATTR_HIDDEN),
						rs.getBoolean(IDBConstants.ATTR_CALCULATE)));
			}

			JournalStandardAccount[] result = new JournalStandardAccount[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}*/

	public JournalStandard[] getSuperJournalStandard(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT s.*, j." + IDBConstants.ATTR_NAME + " FROM " + IDBConstants.TABLE_JOURNAL_STANDARD  + " s LEFT JOIN " +
					IDBConstants.TABLE_JOURNAL + " j ON s." + IDBConstants.ATTR_JOURNAL + "=j." + IDBConstants.ATTR_AUTOINDEX +
					" WHERE s." + IDBConstants.ATTR_AUTOINDEX +
					" NOT IN (SELECT st." + IDBConstants.ATTR_SUB_JOURNAL +
					" FROM " + IDBConstants.TABLE_JOURNAL_STANDARD_STRUCTURE + " st" +
					") ORDER BY s." + IDBConstants.ATTR_CODE);

			while(rs.next()) {
				Journal journal = null;
				if(rs.getString(IDBConstants.ATTR_NAME) != null)
					journal = new Journal(rs.getLong(IDBConstants.ATTR_JOURNAL), rs.getString(IDBConstants.ATTR_NAME));
				vresult.addElement(new JournalStandard(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE), rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP), journal));
			}

			JournalStandard[] result = new JournalStandard[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public JournalStandard[] getSubJournalStandard(long superindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			String que = "SELECT s.*, j." + IDBConstants.ATTR_NAME + " FROM " + IDBConstants.TABLE_JOURNAL_STANDARD + " s LEFT JOIN " + IDBConstants.TABLE_JOURNAL + " j" +
			" ON s." + IDBConstants.ATTR_JOURNAL + "=j." + IDBConstants.ATTR_AUTOINDEX + "," + IDBConstants.TABLE_JOURNAL_STANDARD_STRUCTURE + " st" +
			" WHERE st." + IDBConstants.ATTR_SUPER_JOURNAL + "=" + superindex +
			" AND st." + IDBConstants.ATTR_SUB_JOURNAL + "=s." + IDBConstants.ATTR_AUTOINDEX +
			" ORDER BY s." + IDBConstants.ATTR_CODE;
			System.err.println(que);

			ResultSet rs = stm.executeQuery(que);


			while(rs.next()) {
				Journal journal = null;
				if(rs.getString(IDBConstants.ATTR_NAME) != null)
					journal = new Journal(rs.getLong(IDBConstants.ATTR_JOURNAL), rs.getString(IDBConstants.ATTR_NAME));
				vresult.addElement(new JournalStandard(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE), rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP), journal));
			}

			JournalStandard[] result = new JournalStandard[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateJournalStandard(long index, JournalStandard journal, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_JOURNAL_STANDARD + " SET " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_DESCRIPTION + "=?, " +
					IDBConstants.ATTR_IS_GROUP + "=?, " +
					IDBConstants.ATTR_JOURNAL + "=?" +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setString(1, journal.getCode());
			stm.setString(2, journal.getDescription());
			stm.setBoolean(3, journal.isGroup());
			if(journal.getJournal() != null)
				stm.setLong(4, journal.getJournal().getIndex());
			else
				stm.setNull(4, Types.LONGVARCHAR);
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteJournalStandard(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_JOURNAL_STANDARD +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createJournalStandardAccount(long index, JournalStandardAccount account, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT + "(" +
					IDBConstants.ATTR_JOURNAL + "," +
					IDBConstants.ATTR_ACCOUNT + "," +
					IDBConstants.ATTR_BALANCE_CODE + "," +
					IDBConstants.ATTR_HIDDEN + "," +
					IDBConstants.ATTR_CALCULATE + ")" +
			" values(?, ?, ?, ?, ?)");

			int col = 1;
			stm.setLong(col++, index);
			stm.setLong(col++, account.getAccount().getIndex());
			stm.setShort(col++, account.getBalance());
			stm.setBoolean(col++, account.isHidden());
			stm.setBoolean(col++, account.isCalculate());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public JournalStandardAccount[] getJournalStandardAccount(long index, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String Query = "SELECT * FROM " + IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT +
		" WHERE " + IDBConstants.ATTR_JOURNAL + "=" + index;// +
		System.err.println(Query);

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(Query);
			while(rs.next()) {
				vresult.addElement(new JournalStandardAccount(this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE), rs.getBoolean(IDBConstants.ATTR_HIDDEN),
						rs.getBoolean(IDBConstants.ATTR_CALCULATE)));
			}

			JournalStandardAccount[] result = new JournalStandardAccount[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public JournalStandardAccount[] getJournalStandardAccountNonCalHidd(long index, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String Query = "SELECT * FROM " + IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT +
		" WHERE " + IDBConstants.ATTR_JOURNAL + "=" + index +
		" AND ("+IDBConstants.ATTR_HIDDEN+"=false AND "+IDBConstants.ATTR_CALCULATE+"=false)";
		System.err.println(Query);

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(Query);
			while(rs.next()) {
				vresult.addElement(new JournalStandardAccount(this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE), rs.getBoolean(IDBConstants.ATTR_HIDDEN),
						rs.getBoolean(IDBConstants.ATTR_CALCULATE)));
			}

			JournalStandardAccount[] result = new JournalStandardAccount[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteJournalStandardAccount(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_JOURNAL_STANDARD_ACCOUNT +
					" WHERE " + IDBConstants.ATTR_JOURNAL + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Account[] getSuperAccount(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM " + IDBConstants.TABLE_ACCOUNT +
		" WHERE " + IDBConstants.ATTR_AUTOINDEX +
		" NOT IN (SELECT " + IDBConstants.ATTR_SUB_ACCOUNT +
		" FROM " + IDBConstants.TABLE_ACCOUNT_STRUCTURE +
		") ORDER BY " + IDBConstants.ATTR_ACCOUNT_CODE;
		System.err.println(query);

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new Account(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_ACCOUNT_CODE),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NAME),
						rs.getShort(IDBConstants.ATTR_CATEGORY),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE),
						rs.getString(IDBConstants.ATTR_NOTE),
                        rs.getString(IDBConstants.ATTR_PATH)));
			}

			Account[] result = new Account[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Account[] getSubAccount(long superindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT " + IDBConstants.TABLE_ACCOUNT + ".*  FROM " +
			IDBConstants.TABLE_ACCOUNT + ", " + IDBConstants.TABLE_ACCOUNT_STRUCTURE +
			" WHERE " + IDBConstants.ATTR_SUPER_ACCOUNT + "=" + superindex +
			" AND " + IDBConstants.ATTR_SUB_ACCOUNT + "=" + IDBConstants.ATTR_AUTOINDEX +
			" ORDER BY " + IDBConstants.TABLE_ACCOUNT + "." + IDBConstants.ATTR_ACCOUNT_CODE;
		//System.err.println(query);
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new Account(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_ACCOUNT_CODE),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NAME),
						rs.getShort(IDBConstants.ATTR_CATEGORY),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE),
						rs.getString(IDBConstants.ATTR_NOTE),
                        rs.getString(IDBConstants.ATTR_PATH)
                        ));
			}

			Account[] result = new Account[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createAccountStructure(long superindex, long subindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_ACCOUNT_STRUCTURE +
					" values(" + superindex + "," + subindex + ")");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createActivity(Activity activity, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_ACTIVITY + "(" +
					IDBConstants.ATTR_CODE + "," +
					IDBConstants.ATTR_NAME + "," +
					IDBConstants.ATTR_DESCRIPTION + ")" +
			" values (?, ?, ?)");

			stm.setString(1, activity.getCode());
			stm.setString(2, activity.getName());
			stm.setString(3, activity.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createActivityStructure(long superactivity, long subactivity, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_ACTIVITY_STRUCTURE +
					" values(" + superactivity + "," + subactivity + ")");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Activity getActivityByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try{
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_ACTIVITY +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
			if(rs.next()) {
				return new Activity(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally{
			if(stm != null) {
				stm.close();
			}
		}
	}

	public Activity[] getSuperActivity(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_ACTIVITY +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX +
					" NOT IN (SELECT " + IDBConstants.ATTR_SUB_ACTIVITY +
					" FROM " + IDBConstants.TABLE_ACTIVITY_STRUCTURE +
					") ORDER BY " + IDBConstants.ATTR_CODE);
			while(rs.next()) {
				vresult.addElement(new Activity(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}

			Activity[] activity = new Activity[vresult.size()];
			vresult.copyInto(activity);
			return activity;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Activity[] getSubActivity(long superindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT " + IDBConstants.TABLE_ACTIVITY + ".*  FROM " +
					IDBConstants.TABLE_ACTIVITY + ", " + IDBConstants.TABLE_ACTIVITY_STRUCTURE +
					" WHERE " + IDBConstants.ATTR_SUPER_ACTIVITY + "=" + superindex +
					" AND " + IDBConstants.ATTR_SUB_ACTIVITY + "=" + IDBConstants.ATTR_AUTOINDEX +
					" ORDER BY " + IDBConstants.TABLE_ACTIVITY + "." + IDBConstants.ATTR_CODE);
			while(rs.next()) {
				vresult.addElement(new Activity(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}

			Activity[] activity = new Activity[vresult.size()];
			vresult.copyInto(activity);
			return activity;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateActivity(long index, Activity activity, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_ACTIVITY + " SET " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_NAME + "=?, " +
					IDBConstants.ATTR_DESCRIPTION + "=?" +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setString(1, activity.getCode());
			stm.setString(2, activity.getName());
			stm.setString(3, activity.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteActivity(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_ACTIVITY +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createUnit(Unit unit, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_UNIT + "(" +
					IDBConstants.ATTR_CODE + "," +
					IDBConstants.ATTR_DESCRIPTION + ")" +
			" values (?, ?)");

			stm.setString(1, unit.getCode());
			stm.setString(2, unit.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Unit[] getAllUnit(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_UNIT);

			while(rs.next()) {
				vresult.addElement(new Unit(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE), rs.getString(IDBConstants.ATTR_DESCRIPTION)));
			}

			Unit[] unit = new Unit[vresult.size()];
			vresult.copyInto(unit);
			return unit;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Unit getUnitByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;
		String query ="SELECT * FROM " + IDBConstants.TABLE_UNIT +
		" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			if(rs.next()) {
				return new Unit(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE), rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Unit getUnitByDescription(String description, Connection conn) throws SQLException {
		Statement stm = null;
		String query ="SELECT * FROM " + IDBConstants.TABLE_UNIT +
		" WHERE " + IDBConstants.ATTR_DESCRIPTION+ " = '" + description + "'";
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			if(rs.next()) {
				return new Unit(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE), rs.getString(IDBConstants.ATTR_DESCRIPTION));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateUnit(long index, Unit unit, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_UNIT + " SET " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_DESCRIPTION + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setString(1, unit.getCode());
			stm.setString(2, unit.getDescription());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteUnit(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_UNIT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

		public void createCurrency(Currency currency, Connection conn) throws SQLException {
			PreparedStatement stm = null;
			
			try {
				stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_CURRENCY + "(" +
						IDBConstants.ATTR_SYMBOL + "," +
						IDBConstants.ATTR_CODE + "," +
						IDBConstants.ATTR_DESCRIPTION + "," +	
						IDBConstants.ATTR_SAY + "," +
						IDBConstants.ATTR_CENT_WORDS + "," +
						IDBConstants.ATTR_LANGUAGE + ")" +
				" values (?, ?, ?, ?,?,?)");
				
				stm.setString(1, currency.getSymbol());
				stm.setString(2, currency.getCode());
				if(currency.getDescription() != null)
					stm.setString(3, currency.getDescription());
				else
					stm.setNull(3, Types.VARCHAR);
				stm.setString(4, currency.getSay());
				stm.setString(5, currency.getCent());
				stm.setString(6, currency.getLanguage());
				stm.executeUpdate();
			}
			catch(Exception ex) {
				throw new SQLException(OtherSQLException.getMessage(ex));
			}
	
			finally {
				if(stm != null)
					stm.close();
			}
		}

	public Currency[] getAllCurrency(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CURRENCY);

			while(rs.next()) {
				vresult.addElement(new Currency(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_SYMBOL),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getString(IDBConstants.ATTR_SAY),
						rs.getString(IDBConstants.ATTR_CENT_WORDS),
						rs.getString(IDBConstants.ATTR_LANGUAGE)));
			}

			Currency[] result = new Currency[vresult.size()];
			Currency currencybase= getBaseCurrency(conn);
			vresult.copyInto(result);
			if (currencybase != null)
			{
				int i;
				for (i=0; i<result.length;i++)
					if (currencybase.getCode().compareTo(result[i].getCode())==0)
						result[i].setIsBase(true);
					else
						result[i].setIsBase(false);

			}
			return result;
		}
		catch(Exception ex) {
			//throw new SQLException(OtherSQLException.getMessage(ex));
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Currency getCurrency(long index, Connection conn) throws SQLException {
		Statement stm = null;
		String query = "SELECT * FROM " + IDBConstants.TABLE_CURRENCY +
		" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			if(rs.next()) {
				Currency curr = new Currency(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_SYMBOL),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getString(IDBConstants.ATTR_SAY),
						rs.getString(IDBConstants.ATTR_CENT_WORDS),
						rs.getString(IDBConstants.ATTR_LANGUAGE));
				Currency currencybase= getBaseCurrency(conn);
				if (currencybase != null)
					if (currencybase.getCode().compareTo(curr.getCode())==0)
						curr.setIsBase(true);
				return curr;
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateCurrency(long index, Currency currency, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_CURRENCY + " SET " +
					IDBConstants.ATTR_SYMBOL + "=?, " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_DESCRIPTION + "=?, " +
					IDBConstants.ATTR_SAY + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setString(1, currency.getSymbol());
			stm.setString(2, currency.getCode());
			if(currency.getDescription() != null)
				stm.setString(3, currency.getDescription());
			else
				stm.setNull(3, Types.VARCHAR);
			stm.setString(4, currency.getSay());

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteCurrency(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_CURRENCY +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createBankAccount(BankAccount bank, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_BANK_ACCOUNT + "(" +
					IDBConstants.ATTR_CODE + "," +
					IDBConstants.ATTR_NAME + "," +
					IDBConstants.ATTR_ACCOUNT_NO + "," +
					IDBConstants.ATTR_CURRENCY + "," +
					IDBConstants.ATTR_ADDRESS + "," +
					IDBConstants.ATTR_CONTACT + "," +
					IDBConstants.ATTR_PHONE + "," +
					IDBConstants.ATTR_UNIT + "," +
					IDBConstants.ATTR_ACCOUNT + ")" +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stm.setString(1, bank.getCode());
			stm.setString(2, bank.getName());
			stm.setString(3, bank.getAccountNo());
			if(bank.getCurrency() != null)
				stm.setLong(4, bank.getCurrency().getIndex());
			else
				stm.setNull(4, Types.LONGVARCHAR);
			if(bank.getAddress() != null)
				stm.setString(5, bank.getAddress());
			else
				stm.setNull(5, Types.VARCHAR);
			if(bank.getContact() != null)
				stm.setString(6, bank.getContact());
			else
				stm.setNull(6, Types.VARCHAR);
			if(bank.getPhone() != null)
				stm.setString(7, bank.getPhone());
			else
				stm.setNull(7, Types.VARCHAR);
			if(bank.getUnit() != null)
				stm.setLong(8, bank.getUnit().getIndex());
			else
				stm.setNull(8, Types.LONGVARCHAR);
			if(bank.getAccount() != null)
				stm.setLong(9, bank.getAccount().getIndex());
			else
				stm.setNull(9, Types.LONGVARCHAR);

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public BankAccount[] getAllBankAccount(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM " + IDBConstants.TABLE_BANK_ACCOUNT;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new BankAccount(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NO),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CONTACT),
						rs.getString(IDBConstants.ATTR_PHONE),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn)));
			}

			BankAccount[] result = new BankAccount[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public BankAccount[] getListAllBankAccount(Connection conn,String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new BankAccount(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NO),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CONTACT),
						rs.getString(IDBConstants.ATTR_PHONE),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn),true));
			}

			BankAccount[] result = new BankAccount[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public BankAccount getBankAccountByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_BANK_ACCOUNT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			if(rs.next()) {
				return new BankAccount(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NO),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_CONTACT),
						rs.getString(IDBConstants.ATTR_PHONE),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						this.getAccount(rs.getLong(IDBConstants.ATTR_UNIT), conn));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateBankAccount(long index, BankAccount bank, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_BANK_ACCOUNT + " SET " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_NAME + "=?, " +
					IDBConstants.ATTR_ACCOUNT_NO + "=?, " +
					IDBConstants.ATTR_CURRENCY + "=?, " +
					IDBConstants.ATTR_ADDRESS + "=?, " +
					IDBConstants.ATTR_CONTACT + "=?, " +
					IDBConstants.ATTR_PHONE + "=?, " +
					IDBConstants.ATTR_UNIT + "=?," +
					IDBConstants.ATTR_ACCOUNT + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setString(1, bank.getCode());
			stm.setString(2, bank.getName());
			stm.setString(3, bank.getAccountNo());
			if(bank.getCurrency() != null)
				stm.setLong(4, bank.getCurrency().getIndex());
			else
				stm.setNull(4, Types.LONGVARCHAR);
			if(bank.getAddress() != null)
				stm.setString(5, bank.getAddress());
			else
				stm.setNull(5, Types.VARCHAR);
			if(bank.getContact() != null)
				stm.setString(6, bank.getContact());
			else
				stm.setNull(6, Types.VARCHAR);
			if(bank.getPhone() != null)
				stm.setString(7, bank.getPhone());
			else
				stm.setNull(7, Types.VARCHAR);
			if(bank.getUnit() != null)
				stm.setLong(8, bank.getUnit().getIndex());
			else
				stm.setNull(8, Types.LONGVARCHAR);
			if(bank.getAccount() != null)
				stm.setLong(9, bank.getAccount().getIndex());
			else
				stm.setNull(9, Types.LONGVARCHAR);

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteBankAccount(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_BANK_ACCOUNT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createCreditorList(CreditorList bank, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_CREDITOR_LIST + "(" +
					IDBConstants.ATTR_CODE + "," +
					IDBConstants.ATTR_NAME + "," +
					IDBConstants.ATTR_ADDRESS + "," +
					IDBConstants.ATTR_PROVINCE + "," +
					IDBConstants.ATTR_COUNTRY + "," +
					IDBConstants.ATTR_CONTACT + "," +
					IDBConstants.ATTR_PHONE + "," +
					IDBConstants.ATTR_BANK_ACCOUNT + "," +
					IDBConstants.ATTR_BANK_NAME + "," +
					IDBConstants.ATTR_CURRENCY + "," +
					IDBConstants.ATTR_BANK_ADDRESS + "," +
					IDBConstants.ATTR_CREDITOR_TYPE + ")" +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			stm.setString(1, bank.getCode());
			stm.setString(2, bank.getName());

			if(bank.getAddress() != null)
				stm.setString(3, bank.getAddress());
			else
				stm.setNull(3, Types.VARCHAR);

			if(bank.getProvince() != null)
				stm.setString(4, bank.getProvince());
			else
				stm.setNull(4, Types.VARCHAR);

			if(bank.getCountry() != null)
				stm.setString(5, bank.getCountry());
			else
				stm.setNull(5, Types.VARCHAR);

			if(bank.getContact() != null)
				stm.setString(6, bank.getContact());
			else
				stm.setNull(6, Types.VARCHAR);

			if(bank.getPhone() != null)
				stm.setString(7, bank.getPhone());
			else
				stm.setNull(7, Types.VARCHAR);

			if(bank.getBankAccount() != null)
				stm.setString(8, bank.getBankAccount());
			else
				stm.setNull(8, Types.VARCHAR);

			if(bank.getBankName() != null)
				stm.setString(9, bank.getBankName());
			else
				stm.setNull(9, Types.VARCHAR);

			if(bank.getCurrency() != null)
				stm.setLong(10, bank.getCurrency().getIndex());
			else
				stm.setNull(10, Types.LONGVARBINARY);

			if(bank.getBankAddress() != null)
				stm.setString(11, bank.getBankAddress());
			else
				stm.setNull(11, Types.VARCHAR);

			if(bank.getCreditorType() != null)
				stm.setString(12, bank.getCreditorType());
			else
				stm.setNull(12, Types.VARCHAR);

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public CreditorList[] getAllCreditorList(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CREDITOR_LIST);

			while(rs.next()) {
				vresult.addElement(new CreditorList(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_CONTACT),
						rs.getString(IDBConstants.ATTR_PHONE),
						rs.getString(IDBConstants.ATTR_BANK_ACCOUNT),
						rs.getString(IDBConstants.ATTR_BANK_NAME),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						rs.getString(IDBConstants.ATTR_BANK_ADDRESS),
						rs.getString(IDBConstants.ATTR_CREDITOR_TYPE)));
			}

			CreditorList[] result = new CreditorList[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateCreditorList(long index, CreditorList bank, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_CREDITOR_LIST + " SET " +
					IDBConstants.ATTR_CODE + "=?," +
					IDBConstants.ATTR_NAME + "=?," +
					IDBConstants.ATTR_ADDRESS + "=?," +
					IDBConstants.ATTR_PROVINCE + "=?," +
					IDBConstants.ATTR_COUNTRY + "=?," +
					IDBConstants.ATTR_CONTACT + "=?," +
					IDBConstants.ATTR_PHONE + "=?," +
					IDBConstants.ATTR_BANK_ACCOUNT + "=?," +
					IDBConstants.ATTR_BANK_NAME + "=?," +
					IDBConstants.ATTR_CURRENCY + "=?," +
					IDBConstants.ATTR_BANK_ADDRESS + "=?, " +
					IDBConstants.ATTR_CREDITOR_TYPE + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setString(1, bank.getCode());
			stm.setString(2, bank.getName());

			if(bank.getAddress() != null)
				stm.setString(3, bank.getAddress());
			else
				stm.setNull(3, Types.VARCHAR);

			if(bank.getProvince() != null)
				stm.setString(4, bank.getProvince());
			else
				stm.setNull(4, Types.VARCHAR);

			if(bank.getCountry() != null)
				stm.setString(5, bank.getCountry());
			else
				stm.setNull(5, Types.VARCHAR);

			if(bank.getContact() != null)
				stm.setString(6, bank.getContact());
			else
				stm.setNull(6, Types.VARCHAR);

			if(bank.getPhone() != null)
				stm.setString(7, bank.getPhone());
			else
				stm.setNull(7, Types.VARCHAR);

			if(bank.getBankAccount() != null)
				stm.setString(8, bank.getBankAccount());
			else
				stm.setNull(8, Types.VARCHAR);

			if(bank.getBankName() != null)
				stm.setString(9, bank.getBankName());
			else
				stm.setNull(9, Types.VARCHAR);

			if(bank.getCurrency() != null)
				stm.setLong(10, bank.getCurrency().getIndex());
			else
				stm.setNull(10, Types.LONGVARBINARY);

			if(bank.getBankAddress() != null)
				stm.setString(11, bank.getBankAddress());
			else
				stm.setNull(11, Types.VARCHAR);

			if(bank.getCreditorType() != null)
				stm.setString(12, bank.getCreditorType());
			else
				stm.setNull(12, Types.VARCHAR);

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteCreditorList(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_CREDITOR_LIST +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createSubsidiaryAccountSetting(Connection conn,SubsidiaryAccountSetting accSetting) throws SQLException {
		PreparedStatement stm = null;
		String query = "INSERT INTO " + IDBConstants.TABLE_SUBSIDIARY_ACCOUNT_SETTING + "(" +
		IDBConstants.ATTR_ACCOUNT + "," +
		IDBConstants.ATTR_SUBSIDIARY_ACCOUNT + "," + IDBConstants.ATTR_TRANSACTION_CODE + ")" +
		" values (?, ?, ?)";
		try {
			stm = conn.prepareStatement(query);
			stm.setLong(1, accSetting.getAccount().getIndex());
			stm.setString(2,accSetting.getSubsidiaryAccount());
			stm.setInt(3,accSetting.getTranscode());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public SubsidiaryAccountSetting[] getAllSubsidiaryAccountSetting(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_SUBSIDIARY_ACCOUNT_SETTING);

			while(rs.next()) {
				SubsidiaryAccountSetting theClass = new SubsidiaryAccountSetting();
				theClass.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				theClass.setAccount(this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT),conn));
				theClass.setSubsidiaryAccount(rs.getString(IDBConstants.ATTR_SUBSIDIARY_ACCOUNT));
				theClass.setTranscode(rs.getInt(IDBConstants.ATTR_TRANSACTION_CODE));
				vresult.addElement(theClass);
			}

			SubsidiaryAccountSetting[] result = new SubsidiaryAccountSetting[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public SubsidiaryAccountSetting getSubsidiaryAccountSettingByIndex(Connection conn,long index) throws SQLException {
		Statement stm = null;
		String query = "SELECT * FROM " + IDBConstants.TABLE_SUBSIDIARY_ACCOUNT_SETTING
			+ " where account=" + index;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);


			if (rs.next()) {
				SubsidiaryAccountSetting theClass = new SubsidiaryAccountSetting();
				theClass.setAutoindex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				theClass.setAccount(this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT),conn));
				theClass.setSubsidiaryAccount(rs.getString(IDBConstants.ATTR_SUBSIDIARY_ACCOUNT));
				theClass.setTranscode(rs.getInt(IDBConstants.ATTR_TRANSACTION_CODE));
				return theClass;
			}
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}

	}

	public void updateSubsidiaryAccountSetting(Connection conn,SubsidiaryAccountSetting accSetting) throws SQLException {
		PreparedStatement stm = null;
		String query = "UPDATE " + IDBConstants.TABLE_SUBSIDIARY_ACCOUNT_SETTING + " SET " +
					IDBConstants.ATTR_ACCOUNT + "=?," +
					IDBConstants.ATTR_SUBSIDIARY_ACCOUNT + "=?," +
					IDBConstants.ATTR_TRANSACTION_CODE + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + accSetting.getAutoindex();
		try {
			stm = conn.prepareStatement(query);
			stm.setLong(1, accSetting.getAccount().getIndex());
			stm.setString(2, accSetting.getSubsidiaryAccount());
			stm.setInt(3,accSetting.getTranscode());
				stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteSubsidiaryAccountSetting(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_SUBSIDIARY_ACCOUNT_SETTING +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createCompanyLoan(CompanyLoan loan, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_COMPANY_LOAN + "(" +
					IDBConstants.ATTR_CREDITOR + "," +
					IDBConstants.ATTR_CODE + "," +
					IDBConstants.ATTR_NAME + "," +
					IDBConstants.ATTR_CURRENCY + "," +
					IDBConstants.ATTR_INITIAL + "," +
					IDBConstants.ATTR_ACCOUNT + "," +
					IDBConstants.ATTR_UNIT + "," +
					IDBConstants.ATTR_REMARKS + ")" +
			" values (?, ?, ?, ?, ?, ?, ?, ?)");

			stm.setLong(1, loan.getCreditorList().getIndex());
			stm.setString(2, loan.getCode());
			stm.setString(3, loan.getName());

			if(loan.getCurrency() != null)
				stm.setLong(4, loan.getCurrency().getIndex());
			else
				stm.setNull(4, Types.LONGVARCHAR);

			stm.setDouble(5, loan.getInitial());

			if(loan.getAccount() != null)
				stm.setLong(6, loan.getAccount().getIndex());
			else
				stm.setNull(6, Types.LONGVARCHAR);

			if(loan.getUnit() != null)
				stm.setLong(7, loan.getUnit().getIndex());
			else
				stm.setNull(7, Types.LONGVARCHAR);

			if(loan.getRemarks() != null)
				stm.setString(8, loan.getRemarks());
			else
				stm.setNull(8, Types.VARCHAR);

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}


	public CompanyLoan[] getAllCompanyLoan(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_COMPANY_LOAN);

			while(rs.next()) {
				vresult.addElement(new CompanyLoan(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						this.getCreditorList(rs.getLong(IDBConstants.ATTR_CREDITOR), conn),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						rs.getDouble(IDBConstants.ATTR_INITIAL),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						rs.getString(IDBConstants.ATTR_REMARKS)));
			}

			CompanyLoan[] result = new CompanyLoan[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public CompanyLoan[] getListAllCompanyLoan(Connection conn,String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new CompanyLoan(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						this.getCreditorList(rs.getLong(IDBConstants.ATTR_CREDITOR), conn),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						rs.getDouble(IDBConstants.ATTR_INITIAL),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						rs.getString(IDBConstants.ATTR_REMARKS)));
			}

			CompanyLoan[] result = new CompanyLoan[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public CompanyLoan getCompanyLoan(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_COMPANY_LOAN +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			if(rs.next()) {
				return new CompanyLoan(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						this.getCreditorList(rs.getLong(IDBConstants.ATTR_CREDITOR), conn),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						rs.getDouble(IDBConstants.ATTR_INITIAL),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						rs.getString(IDBConstants.ATTR_REMARKS));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public double getRcvLoanDetail(CompanyLoan companyLoan,Connection conn) throws SQLException {
		Statement stm = null;
		String Query="select * from "+IDBConstants.TABLE_RCV_LOAN+" where companyloan="+ companyLoan.getIndex() +" and status=3";

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(Query);
			double accumulatedAmount=0;
			while(rs.next()) {
				accumulatedAmount=accumulatedAmount+rs.getDouble(IDBConstants.ATTR_AMOUNT);
			}
			return accumulatedAmount;

		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}


	public void updateCompanyLoan(long index, CompanyLoan loan, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_COMPANY_LOAN + " SET " +
					IDBConstants.ATTR_CREDITOR + "=?," +
					IDBConstants.ATTR_CODE + "=?," +
					IDBConstants.ATTR_NAME + "=?," +
					IDBConstants.ATTR_CURRENCY + "=?," +
					IDBConstants.ATTR_INITIAL + "=?," +
					IDBConstants.ATTR_ACCOUNT + "=?," +
					IDBConstants.ATTR_UNIT + "=?," +
					IDBConstants.ATTR_REMARKS + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);


			stm.setLong(1, loan.getCreditorList().getIndex());
			stm.setString(2, loan.getCode());
			stm.setString(3, loan.getName());

			if(loan.getCurrency() != null)
				stm.setLong(4, loan.getCurrency().getIndex());
			else
				stm.setNull(4, Types.LONGVARCHAR);

			stm.setDouble(5, loan.getInitial());

			if(loan.getAccount() != null)
				stm.setLong(6, loan.getAccount().getIndex());
			else
				stm.setNull(6, Types.LONGVARCHAR);

			if(loan.getUnit() != null)
				stm.setLong(7, loan.getUnit().getIndex());
			else
				stm.setNull(7, Types.LONGVARCHAR);

			if(loan.getRemarks() != null)
				stm.setString(8, loan.getRemarks());
			else
				stm.setNull(8, Types.VARCHAR);

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteCompanyLoan(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_COMPANY_LOAN +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createCashAccount(CashAccount cash, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_CASH_ACCOUNT + "(" +
					IDBConstants.ATTR_CODE + "," +
					IDBConstants.ATTR_NAME + "," +
					IDBConstants.ATTR_CURRENCY + "," +
					IDBConstants.ATTR_UNIT + "," +
					IDBConstants.ATTR_ACCOUNT + ")" +
			" values (?, ?, ?, ?, ?)");

			stm.setString(1, cash.getCode());
			stm.setString(2, cash.getName());
			if(cash.getCurrency() != null)
				stm.setLong(3, cash.getCurrency().getIndex());
			else
				stm.setNull(3, Types.LONGVARCHAR);
			if(cash.getUnit() != null)
				stm.setLong(4, cash.getUnit().getIndex());
			else
				stm.setNull(4, Types.LONGVARCHAR);
			if(cash.getAccount() != null)
				stm.setLong(5, cash.getAccount().getIndex());
			else
				stm.setNull(5, Types.LONGVARCHAR);

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public CashAccount[] getAllCashAccount(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM " + IDBConstants.TABLE_CASH_ACCOUNT;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new CashAccount(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn)));
			}

			CashAccount[] result = new CashAccount[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public CashAccount[] getListAllCashAccount(Connection conn,String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				vresult.addElement(new CashAccount(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn),true));
			}

			CashAccount[] result = new CashAccount[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public CashAccount getCashAccountByIndex(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CASH_ACCOUNT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			if(rs.next()) {
				return new CashAccount(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn));
			}
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public PmtCAIOUProject[] getAllPmtCAIOUProject(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM " + IDBConstants.TABLE_PMT_CA_IOU_PROJECT;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				ProjectBusinessLogic projectLogic = new ProjectBusinessLogic(conn);
				OrganizationLogic orgLogic = new OrganizationLogic();
				PmtCAIOUProject PmtCAIOUProject = new PmtCAIOUProject();
				PmtCAIOUProject.setDescription(rs.getString("description"));
				PmtCAIOUProject.setReferenceNo(rs.getString("referenceNo"));
				PmtCAIOUProject.setProject(projectLogic.getProjectDataByIndex(rs.getLong("project")));
				PmtCAIOUProject.setUnit(this.getUnitByIndex(rs.getLong("unit"),conn));
				PmtCAIOUProject.setAmount(rs.getDouble("amount"));
				PmtCAIOUProject.setCurrency(this.getCurrency(rs.getLong("currency"),conn));
				PmtCAIOUProject.setDepartment(orgLogic.getOrganizationByIndex(rs.getLong("department"),conn));
				vresult.addElement(PmtCAIOUProject);
			}

			PmtCAIOUProject[] result = new PmtCAIOUProject[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public PmtCAProject[] getAllPmtCAProject(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM " + IDBConstants.TABLE_PMT_CA_PROJECT;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				ProjectBusinessLogic projectLogic = new ProjectBusinessLogic(conn);
				//OrganizationLogic orgLogic = new OrganizationLogic();
				PmtCAProject PmtCAProject = new PmtCAProject();
				PmtCAProject.setReferenceNo(rs.getString("referenceno"));
				PmtCAProject.setCashAccount(this.getCashAccountByIndex(rs.getLong("cashaccount"),conn));
				PmtCAProject.setBankAccount(this.getBankAccountByIndex(rs.getLong("bankaccount"),conn));
				PmtCAProject.setProject(projectLogic.getProjectDataByIndex(rs.getLong("project")));
				PmtCAProject.setAmount(rs.getDouble("amount"));
				PmtCAProject.setExchangeRate(rs.getDouble("exchangerate"));
				PmtCAProject.setUnit(this.getUnitByIndex(rs.getLong("unit"),conn));
				PmtCAProject.setCurrency(this.getCurrency(rs.getLong("currency"),conn));
				vresult.addElement(PmtCAProject);
			}

			PmtCAProject[] result = new PmtCAProject[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public PurchaseReceipt[] getAllPurchaseReceipt(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT * FROM " + IDBConstants.TABLE_PURCHASE_RECEIPT;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				ProjectBusinessLogic projectLogic = new ProjectBusinessLogic(conn);
				HRMBusinessLogic hrmlogic = new HRMBusinessLogic(conn);
				PurchaseReceipt purchaseReceipt = new PurchaseReceipt();
				purchaseReceipt.setIndex(rs.getLong("autoindex"));
				purchaseReceipt.setTransactionDate(rs.getDate("transactiondate"));
				purchaseReceipt.setReferenceNo(rs.getString("referenceno"));
				purchaseReceipt.setSupplier(projectLogic.getPartnerByIndex(rs.getLong("supplier")));
				purchaseReceipt.setBankAccount(rs.getString("bankaccount"));
				purchaseReceipt.setProject(projectLogic.getProjectDataByIndex(rs.getLong("project")));
				purchaseReceipt.setInvoice(rs.getString("invoice"));
				purchaseReceipt.setInvoiceDate(rs.getDate("invoicedate"));
				purchaseReceipt.setApCurr(this.getCurrency(rs.getLong("apcurr"), conn));
				purchaseReceipt.setAmount(rs.getDouble("amount"));
				purchaseReceipt.setApexChRate(rs.getDouble("apexchrate"));
				purchaseReceipt.setVatCurr(this.getCurrency(rs.getLong("vatcurr"),conn));
				purchaseReceipt.setVatAmount(rs.getDouble("vatamount"));
				purchaseReceipt.setEmpApproved(hrmlogic.getEmployeeByIndex1(rs.getLong("empapproved")));
				purchaseReceipt.setStatus(rs.getShort("status"));
				vresult.addElement(purchaseReceipt);
			}

			PurchaseReceipt[] result = new PurchaseReceipt[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public PurchaseReceipt getAllPurchaseReceiptByIndex(Connection conn,long index) throws SQLException {
		Statement stm = null;
		String query = "SELECT * FROM " + IDBConstants.TABLE_PURCHASE_RECEIPT + " where autoindex=" + index;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			if(rs.next()) {
				ProjectBusinessLogic projectLogic = new ProjectBusinessLogic(conn);
				HRMBusinessLogic hrmlogic = new HRMBusinessLogic(conn);
				PurchaseReceipt purchaseReceipt = new PurchaseReceipt();
				purchaseReceipt.setIndex(rs.getLong(IDBConstants.ATTR_AUTOINDEX));
				purchaseReceipt.setTransactionDate(rs.getDate("transactiondate"));
				purchaseReceipt.setReferenceNo(rs.getString("referenceno"));
				purchaseReceipt.setSupplier(projectLogic.getPartnerByIndex(rs.getLong("supplier")));
				purchaseReceipt.setBankAccount(rs.getString("bankaccount"));
				purchaseReceipt.setProject(projectLogic.getProjectDataByIndex(rs.getLong("project")));
				purchaseReceipt.setInvoice(rs.getString("invoice"));
				purchaseReceipt.setInvoiceDate(rs.getDate("invoicedate"));
				purchaseReceipt.setApCurr(this.getCurrency(rs.getLong("apcurr"), conn));
				purchaseReceipt.setAmount(rs.getDouble("amount"));
				purchaseReceipt.setApexChRate(rs.getDouble("apexchrate"));
				purchaseReceipt.setVatCurr(this.getCurrency(rs.getLong("vatcurr"),conn));
				purchaseReceipt.setVatAmount(rs.getDouble("vatamount"));
				purchaseReceipt.setEmpApproved(hrmlogic.getEmployeeByIndex1(rs.getLong("empapproved")));
				purchaseReceipt.setStatus(rs.getShort("status"));
				return purchaseReceipt;
			}
			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public PurchaseApPmt[] getAllPurchaseApPmtByReceipt(Connection conn,String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()) {
				PurchaseApPmt purchaseApPmt = new PurchaseApPmt();
				purchaseApPmt.setAppMtAmount(rs.getDouble(IDBConstants.ATTR_AP_PMT_AMOUNT));
				purchaseApPmt.setReferenceNo(rs.getString(IDBConstants.ATTR_REFERENCE_NO));
				if (rs.getLong(IDBConstants.ATTR_PURCHASE_RECEIPT)>0)
					purchaseApPmt.setPurchaseReceipt(this.getAllPurchaseReceiptByIndex(conn,rs.getLong(IDBConstants.ATTR_PURCHASE_RECEIPT)));
				vresult.addElement(purchaseApPmt);
			}

			PurchaseApPmt[] result = new PurchaseApPmt[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}


	public void updateCashAccount(long index, CashAccount cash, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_CASH_ACCOUNT + " SET " +
					IDBConstants.ATTR_CODE + "=?, " +
					IDBConstants.ATTR_NAME + "=?, " +
					IDBConstants.ATTR_CURRENCY + "=?, " +
					IDBConstants.ATTR_UNIT + "=?, " +
					IDBConstants.ATTR_ACCOUNT + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setString(1, cash.getCode());
			stm.setString(2, cash.getName());
			if(cash.getCurrency() != null)
				stm.setLong(3, cash.getCurrency().getIndex());
			else
				stm.setNull(3, Types.LONGVARCHAR);
			if(cash.getUnit() != null)
				stm.setLong(4, cash.getUnit().getIndex());
			else
				stm.setNull(4, Types.LONGVARCHAR);
			if(cash.getAccount() != null)
				stm.setLong(5, cash.getAccount().getIndex());
			else
				stm.setNull(5, Types.LONGVARCHAR);

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteCashAccount(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_CASH_ACCOUNT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}


	public void createExchangeRate(ExchangeRate exchange, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_EXCHANGE_RATE + "(" +
					IDBConstants.ATTR_REFERENCE_CURRENCY + "," +
					IDBConstants.ATTR_BASE_CURRENCY + "," +
					IDBConstants.ATTR_EXCHANGE_RATE + "," +
					IDBConstants.ATTR_VALID_FROM + "," +
					IDBConstants.ATTR_VALID_TO + ")" +
			" values (?, ?, ?, ?, ?)");

			stm.setLong(1, exchange.getReferenceCurrency().getIndex());
			stm.setLong(2, exchange.getBaseCurrency().getIndex());
			stm.setDouble(3, exchange.getExchangeRate());
			stm.setDate(4, new java.sql.Date(exchange.getValidFrom().getTime()));
			stm.setDate(5, new java.sql.Date(exchange.getValidTo().getTime()));
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public ExchangeRate[] getAllExchangeRate(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_EXCHANGE_RATE);

			while(rs.next()) {
				vresult.addElement(new ExchangeRate(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_REFERENCE_CURRENCY), conn),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_BASE_CURRENCY), conn),
						rs.getDouble(IDBConstants.ATTR_EXCHANGE_RATE),
						rs.getDate(IDBConstants.ATTR_VALID_FROM),
						rs.getDate(IDBConstants.ATTR_VALID_TO)));
			}

			ExchangeRate[] result = new ExchangeRate[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public CreditorList getCreditorList(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CREDITOR_LIST +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			if(rs.next()) {
				return new CreditorList(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						rs.getString(IDBConstants.ATTR_ADDRESS),
						rs.getString(IDBConstants.ATTR_PROVINCE),
						rs.getString(IDBConstants.ATTR_COUNTRY),
						rs.getString(IDBConstants.ATTR_CONTACT),
						rs.getString(IDBConstants.ATTR_PHONE),
						rs.getString(IDBConstants.ATTR_BANK_ACCOUNT),
						rs.getString(IDBConstants.ATTR_BANK_NAME),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						rs.getString(IDBConstants.ATTR_BANK_ADDRESS),
						rs.getString(IDBConstants.ATTR_CREDITOR_TYPE));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}
	public ExchangeRate getExchangeRate(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_EXCHANGE_RATE +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			if(rs.next()) {
				return new ExchangeRate(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_REFERENCE_CURRENCY), conn),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_BASE_CURRENCY), conn),
						rs.getDouble(IDBConstants.ATTR_EXCHANGE_RATE),
						rs.getDate(IDBConstants.ATTR_VALID_FROM),
						rs.getDate(IDBConstants.ATTR_VALID_TO));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateExchangeRate(long index, ExchangeRate exchange, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_EXCHANGE_RATE + " SET " +
					IDBConstants.ATTR_REFERENCE_CURRENCY + "=?, " +
					IDBConstants.ATTR_BASE_CURRENCY + "=?, " +
					IDBConstants.ATTR_EXCHANGE_RATE + "=?, " +
					IDBConstants.ATTR_VALID_FROM + "=?, " +
					IDBConstants.ATTR_VALID_TO + "=? " +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			stm.setLong(1, exchange.getReferenceCurrency().getIndex());
			stm.setLong(2, exchange.getBaseCurrency().getIndex());
			stm.setDouble(3, exchange.getExchangeRate());
			stm.setDate(4, new java.sql.Date(exchange.getValidFrom().getTime()));
			stm.setDate(5, new java.sql.Date(exchange.getValidTo().getTime()));
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteExchangeRate(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_EXCHANGE_RATE +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createBaseCurrency(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_BASE_CURRENCY +
					"(" + IDBConstants.ATTR_CURRENCY + ") values (" + index + ")");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteBaseCurrency(Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_BASE_CURRENCY);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Currency getBaseCurrency(Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_CURRENCY +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + " IN (SELECT " +
					IDBConstants.ATTR_CURRENCY + " FROM " + IDBConstants.TABLE_BASE_CURRENCY + ")");

			if(rs.next()) {
				return new Currency(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_SYMBOL),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getString(IDBConstants.ATTR_SAY),
						rs.getString(IDBConstants.ATTR_CENT_WORDS),
						rs.getString(IDBConstants.ATTR_LANGUAGE));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createWorksheet(Worksheet worksheet, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_WORKSHEET +
					"(" + IDBConstants.ATTR_NAME + ") values ('" + worksheet.getName() + "')");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Worksheet[] getAllWorksheet(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_WORKSHEET);

			while(rs.next()) {
				vresult.addElement(new Worksheet(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME)));
			}

			Worksheet[] result = new Worksheet[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteWorksheet(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_WORKSHEET +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createWorksheetColumn(long worksheetindex, WorksheetColumn column, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_WORKSHEET_COLUMN +
					"(" + IDBConstants.ATTR_WORKSHEET + "," + IDBConstants.ATTR_COLUMN + ") values (?, ?)");
			stm.setLong(1, worksheetindex);
			stm.setString(2, column.getName());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public WorksheetColumn[] getAllWorksheetColumn(long worksheetindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_WORKSHEET_COLUMN +
					" WHERE " + IDBConstants.ATTR_WORKSHEET + "=" + worksheetindex);

			while(rs.next()) {
				vresult.addElement(new WorksheetColumn(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_COLUMN)));
			}

			WorksheetColumn[] result = new WorksheetColumn[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteWorksheetColumn(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_WORKSHEET_COLUMN +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createWorksheetJournal(long columnindex, long journalindex, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_WORKSHEET_JOURNAL + "(" +
					IDBConstants.ATTR_COLUMN + "," + IDBConstants.ATTR_JOURNAL + ")" +
			" values (?, ?)");
			stm.setLong(1, columnindex);
			stm.setLong(2, journalindex);
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Journal[] getAllWorksheetJournal(long columnindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT " + IDBConstants.TABLE_JOURNAL + ".* FROM " + IDBConstants.TABLE_JOURNAL +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + " IN (SELECT " +
					IDBConstants.ATTR_JOURNAL + " FROM " + IDBConstants.TABLE_WORKSHEET_JOURNAL +
					" WHERE " + IDBConstants.ATTR_COLUMN + "=" + columnindex + ")");

			while(rs.next()) {
				vresult.addElement(new Journal(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME)));
			}

			Journal[] result = new Journal[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteWorksheetJournal(long columnindex, long journalindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_WORKSHEET_JOURNAL +
					" WHERE " + IDBConstants.ATTR_COLUMN + "=" + columnindex + " AND " +
					IDBConstants.ATTR_JOURNAL + "=" + journalindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createBalanceSheetReport(Report report, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_BALANCE_SHEET_REPORT +
					"(" + IDBConstants.ATTR_NAME + ") values ('" + report.getName() + "')");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Report[] getAllBalanceSheetReport(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_BALANCE_SHEET_REPORT);

			while(rs.next()) {
				vresult.addElement(new Report(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_NAME)));
			}

			Report[] result = new Report[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteBalanceSheetReport(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_BALANCE_SHEET_REPORT +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

//	public void createTransaction(Transaction trans, Connection conn) throws SQLException {
//		PreparedStatement stm = null;
//
//		try {
//			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_TRANSACTION + "(" +
//					IDBConstants.ATTR_DESCRIPTION + "," +
//					IDBConstants.ATTR_TRANSACTION_DATE + "," +
//					IDBConstants.ATTR_VERIFICATION_DATE + "," +
//					IDBConstants.ATTR_POSTED_DATE + ", " +
//					IDBConstants.ATTR_REFERENCE_NO + ", " +
//					IDBConstants.ATTR_JOURNAL + ", " +
//					IDBConstants.ATTR_TRANSACTION_CODE + ", " +
//					IDBConstants.ATTR_STATUS + ")" +
//			" values (?, ?, ?, ?, ?, ?, ?, ?)");
//			/*
//			 stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_TRANSACTION + "(" +
//			 IDBConstants.ATTR_DESCRIPTION + "," +
//			 IDBConstants.ATTR_TRANSACTION_DATE + "," +
//			 IDBConstants.ATTR_VERIFICATION_DATE + "," +
//			 IDBConstants.ATTR_POSTED_DATE + ", " +
//			 IDBConstants.ATTR_REFERENCE_NO + ", " +
//			 IDBConstants.ATTR_JOURNAL + ", " +
//			 IDBConstants.ATTR_STANDARD_JOURNAL + ", " +
//			 IDBConstants.ATTR_STATUS + ")" +
//			 " values (?, ?, ?, ?, ?, ?, ?, ?)");*/
//
//			stm.setString(1, trans.getDescription());
//			if(trans.getTransDate() != null)
//				stm.setDate(2, new java.sql.Date(trans.getTransDate().getTime()));
//			else
//				stm.setNull(2, Types.LONGVARCHAR);
//			if(trans.getVerifyDate() != null)
//				stm.setDate(3, new java.sql.Date(trans.getVerifyDate().getTime()));
//			else
//				stm.setNull(3, Types.LONGVARCHAR);
//			if(trans.getPostedDate() != null)
//				stm.setDate(4, new java.sql.Date(trans.getPostedDate().getTime()));
//			else
//				stm.setNull(4, Types.LONGVARCHAR);
//			stm.setString(5, trans.getReference());
//			stm.setLong(6, trans.getJournal().getIndex());
//			if(trans.getJournalStandard() != null)
//				stm.setLong(7, trans.getJournalStandard().getIndex());
//			else
//				stm.setNull(7, Types.LONGVARCHAR);
//			stm.setShort(8, trans.getStatus());
//
//			stm.executeUpdate();
//		}
//		catch(Exception ex) {
//			ex.printStackTrace();
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//
//		finally {
//			if(stm != null)
//				stm.close();
//		}
//	}

	public void updateTransaction(long index, Transaction trans, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {

			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_TRANSACTION + " SET " +
					IDBConstants.ATTR_DESCRIPTION + "=?, " +
					IDBConstants.ATTR_TRANSACTION_DATE + "=?, " +
					IDBConstants.ATTR_VERIFICATION_DATE + "=?, " +
					IDBConstants.ATTR_POSTED_DATE + "=?,  " +
					IDBConstants.ATTR_REFERENCE_NO + "=?, " +
					IDBConstants.ATTR_JOURNAL + "=?, " +
					IDBConstants.ATTR_TRANSACTION_CODE  + "=?, " +
					IDBConstants.ATTR_STATUS + "=?" +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
			/*
			 stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_TRANSACTION + " SET " +
			 IDBConstants.ATTR_DESCRIPTION + "=?, " +
			 IDBConstants.ATTR_TRANSACTION_DATE + "=?, " +
			 IDBConstants.ATTR_VERIFICATION_DATE + "=?, " +
			 IDBConstants.ATTR_POSTED_DATE + "=?,  " +
			 IDBConstants.ATTR_REFERENCE_NO + "=?, " +
			 IDBConstants.ATTR_JOURNAL + "=?, " +
			 IDBConstants.ATTR_STANDARD_JOURNAL + "=?, " +
			 IDBConstants.ATTR_STATUS + "=?" +
			 " WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);*/


			stm.setString(1, trans.getDescription());
			if(trans.getTransDate() != null)
				stm.setDate(2, new java.sql.Date(trans.getTransDate().getTime()));
			else
				stm.setNull(2, Types.LONGVARCHAR);
			if(trans.getVerifyDate() != null)
				stm.setDate(3, new java.sql.Date(trans.getVerifyDate().getTime()));
			else
				stm.setNull(3, Types.LONGVARCHAR);
			if(trans.getPostedDate() != null)
				stm.setDate(4, new java.sql.Date(trans.getPostedDate().getTime()));
			else
				stm.setNull(4, Types.LONGVARCHAR);
			stm.setString(5, trans.getReference());
			stm.setLong(6, trans.getJournal().getIndex());
			if(trans.getJournalStandard() != null)
				stm.setLong(7, trans.getJournalStandard().getIndex());
			else
				stm.setNull(7, Types.LONGVARCHAR);
			stm.setShort(8, trans.getStatus());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteTransaction(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_TRANSACTION +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Transaction[] getAllTransaction(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_TRANSACTION);

			while(rs.next()) {

			}

			Transaction[] result = new Transaction[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

//	public void createTransactionDetail(long transindex, TransactionDetail detail, Connection conn) throws SQLException {
//		PreparedStatement stm = null;
//
//		try {
//			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_TRANSACTION_VALUE + "(" +
//					IDBConstants.ATTR_TRANSACTION + "," +
//					IDBConstants.ATTR_ACCOUNT + "," +
//					IDBConstants.ATTR_VALUE + "," +
//					IDBConstants.ATTR_CURRENCY + ", " +
//					IDBConstants.ATTR_EXCHANGE_RATE + ") values (?, ?, ?, ?, ?)");
//
//			stm.setLong(1, transindex);
//			stm.setLong(2, detail.getAccount().getIndex());
//			stm.setDouble(3, detail.getValue());
//			stm.setLong(4, detail.getCurrency().getIndex());
//			stm.setDouble(5, detail.getExchangeRate());
//			stm.executeUpdate();
//		}
//		catch(Exception ex) {
//			ex.printStackTrace();
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//
//		finally {
//			if(stm != null)
//				stm.close();
//		}
//	}

	public void deleteTransactionDetail(long transindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_TRANSACTION_VALUE +
					" WHERE " + IDBConstants.ATTR_TRANSACTION + "=" + transindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public TransactionDetail[] getTransactionDetail(long transindex, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_TRANSACTION_VALUE +
					" WHERE " + IDBConstants.ATTR_TRANSACTION + "=" + transindex);

			while(rs.next()) {
				vresult.add(new TransactionDetail(
						getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT),conn ),
						rs.getDouble(IDBConstants.ATTR_VALUE),
						getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY),conn ),
						rs.getDouble(IDBConstants.ATTR_EXCHANGE_RATE),
						getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						rs.getLong(IDBConstants.ATTR_SUBSIDIARY_ACCOUNT))
				);
			}

			TransactionDetail[] result = new TransactionDetail[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	// setting
	public void createAccountReceivable(long customerindex, long accountindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE + "(" +
					IDBConstants.ATTR_CUSTOMER + "," + IDBConstants.ATTR_ACCOUNT + ")" +
					" values (" + customerindex + "," + accountindex + ")");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public AccountReceivable[] getAllAccountReceivable(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE);

			while(rs.next()) {
				vresult.addElement(new AccountReceivable(
						new ProjectSQLSAP().getCustomerByIndex(rs.getLong(pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_CUSTOMER), conn),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn)));
			}

			AccountReceivable[] result = new AccountReceivable[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateAccountReceivable(long customerindex, long accountindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("UPDATE " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE +
					" SET " + IDBConstants.ATTR_ACCOUNT + "=" + accountindex +
					" WHERE " + IDBConstants.ATTR_CUSTOMER + "=" + customerindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteAccountReceivable(long customerindex, long accountindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE +
					" WHERE " + pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_CUSTOMER + "=" + customerindex +
					" AND " + IDBConstants.ATTR_ACCOUNT + "=" + accountindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createAccountPayableDefault(long accountindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_ACCOUNT_PAYABLE_DEFAULT +
					" values (" + accountindex + ")");
		}
		catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteAccountPayableDefault(Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_ACCOUNT_PAYABLE_DEFAULT);
		}
		catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Account getAccountPayableDefault(Connection conn) throws SQLException {
		Statement stm = null;
		String query = "SELECT acc.* FROM " + IDBConstants.TABLE_ACCOUNT + " acc INNER JOIN " +
		IDBConstants.TABLE_ACCOUNT_PAYABLE_DEFAULT + " ap ON acc." + IDBConstants.ATTR_AUTOINDEX +
		" = ap." + IDBConstants.ATTR_ACCOUNT;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			if(rs.next()) {
				return new Account(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_ACCOUNT_CODE),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NAME),
						rs.getShort(IDBConstants.ATTR_CATEGORY),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE),
						rs.getString(IDBConstants.ATTR_NOTE),
                        rs.getString(IDBConstants.ATTR_PATH)
                        );
			}

			return null;
		}
		catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createAccountPayable(AccountPayable payable, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_ACCOUNT_PAYABLE + "(" +
					IDBConstants.ATTR_PARTNER + "," + IDBConstants.ATTR_ACCOUNT + ")" +
					" values (" + payable.getPartner().getIndex() + "," + payable.getAccount().getIndex() + ")");
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public AccountPayable[] getAllAccountPayable(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_ACCOUNT_PAYABLE);

			while(rs.next()) {
				vresult.addElement(new AccountPayable(
						new ProjectSQLSAP().getPartnerByIndex(rs.getLong(IDBConstants.ATTR_PARTNER), conn),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn)));
			}

			AccountPayable[] result = new AccountPayable[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public AccountPayable getAccountPayableByPartner(long partnerindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_ACCOUNT_PAYABLE +
					" WHERE " + IDBConstants.ATTR_PARTNER + "=" + partnerindex);

			if(rs.next()) {
				return new AccountPayable(
						new ProjectSQLSAP().getPartnerByIndex(rs.getLong(IDBConstants.ATTR_PARTNER), conn),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateAccountPayable(AccountPayable payable, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("UPDATE " + IDBConstants.TABLE_ACCOUNT_PAYABLE +
					" SET " + IDBConstants.ATTR_ACCOUNT + "=" + payable.getAccount().getIndex() +
					" WHERE " + IDBConstants.ATTR_CUSTOMER + "=" + payable.getPartner().getIndex());
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteAccountPayable(long customerindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE +
					" WHERE " + IDBConstants.ATTR_CUSTOMER + "=" + customerindex);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createAccountReceivableDefault(long accountindex, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("INSERT INTO " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE_DEFAULT +
					" values (" + accountindex + ")");
		}
		catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteAccountReceivableDefault(Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_ACCOUNT_RECEIVABLE_DEFAULT);
		}
		catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Account getAccountReceivableDefault(Connection conn) throws SQLException {
		Statement stm = null;
		String query = "SELECT acc.* FROM " + IDBConstants.TABLE_ACCOUNT + " acc INNER JOIN " +
		IDBConstants.TABLE_ACCOUNT_RECEIVABLE_DEFAULT + " ap ON acc." + IDBConstants.ATTR_AUTOINDEX +
		" = ap." + IDBConstants.ATTR_ACCOUNT;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			if(rs.next()) {
				return new Account(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_ACCOUNT_CODE),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NAME),
						rs.getShort(IDBConstants.ATTR_CATEGORY),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE),
						rs.getString(IDBConstants.ATTR_NOTE),
                        rs.getString(IDBConstants.ATTR_PATH));
			}

			return null;
		}
		catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createSignature(String app, Signature signature, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_SIGNATURE +
			" values (?, ?, ?)");
			stm.setString(1, app);
			stm.setShort(2, signature.getType());
			stm.setLong(3, signature.getEmployee().getIndex());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteSignature(String app, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_SIGNATURE +
					" WHERE " + IDBConstants.ATTR_APPLICATION + "='" + app + "'");
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Signature[] getSignature(String app, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT s.*, e." + IDBConstants.ATTR_FIRST_NAME +
					", e." + IDBConstants.ATTR_MIDLE_NAME + ", e." + IDBConstants.ATTR_LAST_NAME +
					" FROM " + IDBConstants.TABLE_SIGNATURE + " s LEFT JOIN " + IDBConstants.TABLE_EMPLOYEE + " e" +
					" ON s." + IDBConstants.ATTR_EMPLOYEE + "= e." + IDBConstants.ATTR_AUTOINDEX +
					" WHERE s." + IDBConstants.ATTR_APPLICATION + "='" + app + "'");

			while(rs.next()) {
				vresult.addElement(new Signature(rs.getShort(IDBConstants.ATTR_SIGNATURE),
						new SimpleEmployee(rs.getLong(IDBConstants.ATTR_EMPLOYEE), rs.getString(IDBConstants.ATTR_FIRST_NAME),
								rs.getString(IDBConstants.ATTR_MIDLE_NAME), rs.getString(IDBConstants.ATTR_LAST_NAME))));
			}
			Signature[] result = new Signature[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Signature getSignature(String app, short type, Connection conn) throws SQLException {
		Statement stm = null;
		//Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT s.*, e." + IDBConstants.ATTR_FIRST_NAME +
					", e." + IDBConstants.ATTR_MIDLE_NAME + ", e." + IDBConstants.ATTR_LAST_NAME +
					" FROM " + IDBConstants.TABLE_SIGNATURE + " s LEFT JOIN " + IDBConstants.TABLE_EMPLOYEE + " e" +
					" ON s." + IDBConstants.ATTR_EMPLOYEE + "= e." + IDBConstants.ATTR_AUTOINDEX +
					" WHERE s." + IDBConstants.ATTR_APPLICATION + "='" + app + "'" +
					" AND " + IDBConstants.ATTR_SIGNATURE + "=" + type);

			if(rs.next()) {
				return new Signature(rs.getShort(IDBConstants.ATTR_SIGNATURE),
						new SimpleEmployee(rs.getLong(IDBConstants.ATTR_EMPLOYEE), rs.getString(IDBConstants.ATTR_FIRST_NAME),
								rs.getString(IDBConstants.ATTR_MIDLE_NAME), rs.getString(IDBConstants.ATTR_LAST_NAME)));
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createJournalStandardSetting(String app, JournalStandardSetting journal, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_JOURNAL_STANDARD_SETTING +
			" values (?, ?, ?, ?)");
			stm.setString(1, app);
			stm.setShort(2, journal.getNumber());
			stm.setLong(3, journal.getJournalStandard().getIndex());
			if(journal.getAttribute()!=null)
				stm.setString(4, journal.getAttribute());
			else
				stm.setNull(4, Types.VARCHAR);
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteJournalStandardSetting(String app, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_JOURNAL_STANDARD_SETTING +
					" WHERE " + IDBConstants.ATTR_APPLICATION + "='" + app + "'");
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public JournalStandardSetting[] getJournalStandardSetting(String app, Connection conn,String cashBank) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT s.*, j.* FROM " + IDBConstants.TABLE_JOURNAL_STANDARD_SETTING + " s LEFT JOIN " + IDBConstants.TABLE_JOURNAL_STANDARD + " j" +
		" ON s." + IDBConstants.ATTR_JOURNAL + "= j." + IDBConstants.ATTR_AUTOINDEX +
		" WHERE s." + IDBConstants.ATTR_APPLICATION + "='" + app + "' and " + IDBConstants.ATTR_ATTRIBUTE + "='"+cashBank+"'";

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new JournalStandardSetting(rs.getShort(IDBConstants.ATTR_NUMBER),
						new JournalStandard(rs.getLong(IDBConstants.ATTR_AUTOINDEX), rs.getString(IDBConstants.ATTR_CODE),
								rs.getString(IDBConstants.ATTR_DESCRIPTION), rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
								null),
								rs.getString(IDBConstants.ATTR_ATTRIBUTE)));
			}

			JournalStandardSetting[] result = new JournalStandardSetting[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public JournalStandardSetting[] getJournalStandardSetting(String app, Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		String query = "SELECT s.*, j.* FROM " + IDBConstants.TABLE_JOURNAL_STANDARD_SETTING + " s LEFT JOIN " + IDBConstants.TABLE_JOURNAL_STANDARD + " j" +
		" ON s." + IDBConstants.ATTR_JOURNAL + "= j." + IDBConstants.ATTR_AUTOINDEX +
		" WHERE s." + IDBConstants.ATTR_APPLICATION + "='" + app + "'";
		System.err.println(query);
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new JournalStandardSetting(rs.getShort(IDBConstants.ATTR_NUMBER),
						new JournalStandard(rs.getLong(IDBConstants.ATTR_AUTOINDEX), rs.getString(IDBConstants.ATTR_CODE),
								rs.getString(IDBConstants.ATTR_DESCRIPTION), rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
								null),
								rs.getString(IDBConstants.ATTR_ATTRIBUTE)));

			}

			JournalStandardSetting[] result = new JournalStandardSetting[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public JournalStandardSetting getJournalStandardSetting(String app, short number, Connection conn) throws SQLException {
		Statement stm = null;
		//Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT s.*, j.* FROM " + IDBConstants.TABLE_JOURNAL_STANDARD_SETTING + " s LEFT JOIN " + IDBConstants.TABLE_JOURNAL_STANDARD + " j" +
					" ON s." + IDBConstants.ATTR_JOURNAL + "= j." + IDBConstants.ATTR_AUTOINDEX +
					" WHERE s." + IDBConstants.ATTR_APPLICATION + "='" + app + "'" +
					" AND " + IDBConstants.ATTR_NUMBER + "=" + number);

			if(rs.next()) {
				/*return new JournalStandardSetting(rs.getShort(IDBConstants.ATTR_NUMBER),
				 new JournalStandard(rs.getLong(IDBConstants.ATTR_AUTOINDEX), rs.getString(IDBConstants.ATTR_CODE),
				 rs.getString(IDBConstants.ATTR_DESCRIPTION), rs.getBoolean(IDBConstants.ATTR_IS_GROUP), null));*/
			}

			return null;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	// beginning
	public void createBeginningBalance(BeginningBalance balance, Connection conn) throws SQLException {
		GenericMapper m = MasterMap.obtainMapperFor(BeginningBalance.class);
		m.setActiveConn(conn);
		m.doInsert(balance);
	}

	public void updateBeginningBalance(long index, BeginningBalance balance, Connection conn) throws SQLException {
		GenericMapper m = MasterMap.obtainMapperFor(BeginningBalance.class);
		m.setActiveConn(conn);
		m.doUpdate(balance);
	}

	public BeginningBalance getBeginningBalance(short type, Connection conn) {
		GenericMapper m = MasterMap.obtainMapperFor(BeginningBalance.class);
		m.setActiveConn(conn);
		List list = m.doSelectAll();
		if (list.size() > 0)
		{
			BeginningBalance balance = (BeginningBalance) list.get(0);
			return balance;
		}
		return null;
	}

	public void createBeginningBalanceSheetDetail(long index, BeginningBalanceSheetEntry detail, Connection conn) throws SQLException {
//		PreparedStatement stm = null;
//
//		try {
//			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_DETAIL + "(" +
//					IDBConstants.ATTR_BEGINNING_BALANCE + "," +
//					IDBConstants.ATTR_ACCOUNT + "," +
//					IDBConstants.ATTR_VALUE + "," +
//					IDBConstants.ATTR_CURRENCY + ", " +
//					IDBConstants.ATTR_EXCHANGE_RATE + ") values (?, ?, ?, ?, ?)");
//
//			stm.setLong(1, index);
//			stm.setLong(2, detail.getAccountIndex());
//			stm.setDouble(3, detail.getValue());
//			stm.setLong(4, detail.getCurrencyIndex());
//			stm.setDouble(5, detail.getRate());
//			stm.executeUpdate();
//		}
//		catch(Exception ex) {
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//		finally {
//			if(stm != null)
//				stm.close();
//		}
	}

	public void deleteBeginningBalanceDetail(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY +
					" WHERE " + IDBConstants.ATTR_BEGINNING_BALANCE + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public BeginningBalanceSheetEntry[] getBeginningBalanceSheetView(long index, short category, Connection conn) throws SQLException {
//		Statement stm = null;
//		Vector vresult = new Vector();
//
//		String query = "SELECT acc.*, det." + IDBConstants.ATTR_VALUE + ", det." + IDBConstants.ATTR_EXCHANGE_RATE +
//		", det." + IDBConstants.ATTR_CURRENCY + ", det.currencycode" +
//		" FROM " + IDBConstants.TABLE_ACCOUNT + " acc LEFT JOIN " +
//		"(SELECT d." + IDBConstants.ATTR_ACCOUNT + ", d." + IDBConstants.ATTR_VALUE + ", d." + IDBConstants.ATTR_EXCHANGE_RATE +
//		", d." + IDBConstants.ATTR_CURRENCY + ", c." + IDBConstants.ATTR_CODE + " currencycode" +
//		" FROM " + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_DETAIL + " d" +
//		" LEFT JOIN " + IDBConstants.TABLE_BEGINNING_BALANCE + " b ON d." + IDBConstants.ATTR_BEGINNING_BALANCE + " = b." + IDBConstants.ATTR_AUTOINDEX +
//		" LEFT JOIN " + IDBConstants.TABLE_CURRENCY + " c ON d." + IDBConstants.ATTR_CURRENCY + " = c." + IDBConstants.ATTR_AUTOINDEX +
//		" WHERE b." + IDBConstants.ATTR_AUTOINDEX + "=" + index + ") det " +
//		" ON acc." + IDBConstants.ATTR_AUTOINDEX + " = det." + IDBConstants.ATTR_ACCOUNT + "," +
//		IDBConstants.TABLE_ACCOUNT_STRUCTURE + " str" +
//		" WHERE str." + IDBConstants.ATTR_SUB_ACCOUNT + "= acc." + IDBConstants.ATTR_AUTOINDEX +
//		" AND acc." + IDBConstants.ATTR_IS_GROUP + "= false" +
//		" AND acc." + IDBConstants.ATTR_CATEGORY + "=" + category +
//		" ORDER BY acc." + IDBConstants.ATTR_ACCOUNT_CODE;
//
//		try {
//			stm = conn.createStatement();
//			ResultSet rs = stm.executeQuery(query);
//
//			while(rs.next()) {
//				vresult.addElement(new BeginningBalanceSheetEntry(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
//						rs.getString(IDBConstants.ATTR_ACCOUNT_CODE), rs.getString(IDBConstants.ATTR_ACCOUNT_NAME), rs.getLong(IDBConstants.ATTR_CURRENCY),
//						rs.getString("currencycode"), rs.getDouble(IDBConstants.ATTR_VALUE), rs.getDouble(IDBConstants.ATTR_EXCHANGE_RATE)));
//			}
//
//			BeginningBalanceSheetEntry[] result = new BeginningBalanceSheetEntry[vresult.size()];
//			vresult.copyInto(result);
//			return result;
//		}
//		catch(Exception ex) {
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//
//		finally {
//			if(stm != null)
//				stm.close();
//		}
		return null;
	}

	public TransactionDetail[] getBeginningBalanceSheet(long index, short category, Connection conn) throws SQLException {
//		Statement stm = null;
//		Vector vresult = new Vector();
//
//		try {
//			stm = conn.createStatement();
//			ResultSet rs = stm.executeQuery("SELECT acc." + IDBConstants.ATTR_AUTOINDEX + ", det." + IDBConstants.ATTR_VALUE +
//					", det." + IDBConstants.ATTR_CURRENCY + " currency FROM " +
//					IDBConstants.TABLE_ACCOUNT + " acc LEFT JOIN " + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_DETAIL + " det" +
//					" ON acc." + IDBConstants.ATTR_AUTOINDEX + "= det." + IDBConstants.ATTR_ACCOUNT + "," +
//					IDBConstants.TABLE_ACCOUNT_STRUCTURE + " str," +
//					IDBConstants.TABLE_BEGINNING_BALANCE + " bal " +
//					" WHERE str." + IDBConstants.ATTR_SUB_ACCOUNT + "= acc." + IDBConstants.ATTR_AUTOINDEX +
//					" AND det." + IDBConstants.ATTR_BEGINNING_BALANCE + "= bal." + IDBConstants.ATTR_AUTOINDEX +
//					" AND det." + IDBConstants.ATTR_BEGINNING_BALANCE + "=" + index +
//					" AND acc." + IDBConstants.ATTR_IS_GROUP + "= false" +
//					" AND acc." + IDBConstants.ATTR_CATEGORY + "=" + category +
//					" ORDER BY acc." + IDBConstants.ATTR_ACCOUNT_CODE);
//
//			while(rs.next()) {
//				//SimpleAccount account = new SimpleAccount();
//				/*Account account = new Account();
//				 vresult.addElement(new TransactionDetail((Account) account,
//				 rs.getDouble(IDBConstants.ATTR_VALUE), getCurrency(rs.getLong("currency"), conn), 0.0));*/
//			}
//
//			TransactionDetail[] result = new TransactionDetail[vresult.size()];
//			vresult.copyInto(result);
//			return result;
//		}
//		catch(Exception ex) {
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//
//		finally {
//			if(stm != null)
//				stm.close();
//		}
		return null;
	}

	public void createBeginningBankDetail(long index, BeginningBankDetail detail, Connection conn) throws SQLException {
//		PreparedStatement stm = null;
//
//		try {
//			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_BEGINNING_BANK_DETAIL + "(" +
//					IDBConstants.ATTR_BEGINNING_BALANCE + "," +
//					IDBConstants.ATTR_BANK_ACCOUNT + "," +
//					IDBConstants.ATTR_ACCOUNT + "," +
//					IDBConstants.ATTR_VALUE + "," +
//					IDBConstants.ATTR_CURRENCY + ", " +
//					IDBConstants.ATTR_EXCHANGE_RATE + ")" +
//			" values (?, ?, ?, ?, ?, ?)");
//
//			stm.setLong(1, index);
//			stm.setLong(2, detail.getIndex());
//			stm.setLong(3, detail.getAccountIndex());
//			stm.setDouble(4, detail.getValue());
//			stm.setLong(5, detail.getCurrencyIndex());
//			stm.setDouble(6, detail.getRate());
//			stm.executeUpdate();
//		}
//		catch(Exception ex) {
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//		finally {
//			if(stm != null)
//				stm.close();
//		}
	}

	public void deleteBeginningBankDetail(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_BEGINNING_BANK_DETAIL +
					" WHERE " + IDBConstants.ATTR_BEGINNING_BALANCE + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public BeginningBankDetail[] getBeginningBankView(long parentIndex, long unitindex, Connection conn) throws SQLException {
		return null;
	}
//		Statement stm = null;
//		Vector vresult = new Vector();
//		String whereClause = " WHERE bank." + IDBConstants.ATTR_UNIT + "=" + unitindex;
//		if (unitindex==0) whereClause = "";
//		String query = "SELECT bank.*, acc." + IDBConstants.ATTR_ACCOUNT_CODE + ", acc." + IDBConstants.ATTR_ACCOUNT_NAME +
//		", curr." + IDBConstants.ATTR_CODE + " currcode, det." + IDBConstants.ATTR_VALUE + ", det." + IDBConstants.ATTR_EXCHANGE_RATE +
//		" FROM " + IDBConstants.TABLE_BANK_ACCOUNT + " bank " +
//		" LEFT JOIN " + IDBConstants.TABLE_ACCOUNT + " acc ON bank." + IDBConstants.ATTR_ACCOUNT + "= acc." + IDBConstants.ATTR_AUTOINDEX +
//		" LEFT JOIN " + IDBConstants.TABLE_CURRENCY + " curr ON bank." +  IDBConstants.ATTR_CURRENCY + "= curr." + IDBConstants.ATTR_AUTOINDEX +
//		" LEFT JOIN (SELECT d." + IDBConstants.ATTR_BANK_ACCOUNT + ", d." + IDBConstants.ATTR_VALUE + ", d." + IDBConstants.ATTR_EXCHANGE_RATE +
//		" FROM " + IDBConstants.TABLE_BEGINNING_BANK_DETAIL + " d LEFT JOIN " +
//		IDBConstants.TABLE_BEGINNING_BALANCE + " b ON d." + IDBConstants.ATTR_BEGINNING_BALANCE +
//		" = b." + IDBConstants.ATTR_AUTOINDEX + " WHERE b." + IDBConstants.ATTR_AUTOINDEX + "=" + parentIndex + ") det " +
//		" ON bank." + IDBConstants.ATTR_AUTOINDEX + " = det." + IDBConstants.ATTR_BANK_ACCOUNT +
//		whereClause;
//
//		try {
//			stm = conn.createStatement();
//			ResultSet rs = stm.executeQuery(query);
//
//			while(rs.next()) {
//				vresult.addElement(new BeginningBankDetail(rs.getLong(IDBConstants.ATTR_AUTOINDEX), rs.getString(IDBConstants.ATTR_CODE),
//						rs.getString(IDBConstants.ATTR_NAME), rs.getString(IDBConstants.ATTR_ACCOUNT_NO), rs.getLong(IDBConstants.ATTR_ACCOUNT),
//						rs.getString(IDBConstants.ATTR_ACCOUNT_CODE), rs.getString(IDBConstants.ATTR_ACCOUNT_NAME), rs.getLong(IDBConstants.ATTR_CURRENCY),
//						rs.getString("currcode"), rs.getDouble(IDBConstants.ATTR_VALUE), rs.getDouble(IDBConstants.ATTR_EXCHANGE_RATE)));
//			}
//
//			BeginningBankDetail[] result = new BeginningBankDetail[vresult.size()];
//			vresult.copyInto(result);
//			return result;
//		}
//		catch(Exception ex) {
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//
//		finally {
//			if(stm != null)
//				stm.close();
//		}
//	}

	public void createBeginningCashDetail(long index, BeginningCashDetail detail, Connection conn) throws SQLException {
//		PreparedStatement stm = null;
//
//		try {
//			stm = conn.prepareStatement("INSERT INTO " + IDBConstants.TABLE_BEGINNING_CASH_DETAIL + "(" +
//					IDBConstants.ATTR_BEGINNING_BALANCE + "," +
//					IDBConstants.ATTR_CASH_ACCOUNT + "," +
//					IDBConstants.ATTR_ACCOUNT + "," +
//					IDBConstants.ATTR_VALUE + "," +
//					IDBConstants.ATTR_CURRENCY + ", " +
//					IDBConstants.ATTR_EXCHANGE_RATE + ")" +
//			" values (?, ?, ?, ?, ?, ?)");
//
//			stm.setLong(1, index);
//			stm.setLong(2, detail.getIndex());
//			stm.setLong(3, detail.getAccount.());
//			stm.setDouble(4, detail.getValue());
//			stm.setLong(5, detail.getCurrencyIndex());
//			stm.setDouble(6, detail.getRate());
//			stm.executeUpdate();
//		}
//		catch(Exception ex) {
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//		finally {
//			if(stm != null)
//				stm.close();
//		}
	}

	public void deleteBeginningCashDetail(long index, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			stm = conn.createStatement();
			stm.executeUpdate("DELETE FROM " + IDBConstants.TABLE_BEGINNING_CASH_DETAIL +
					" WHERE " + IDBConstants.ATTR_BEGINNING_BALANCE + "=" + index);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Transaction getTransaction(Connection conn,long indexTrans) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			String query="SELECT * FROM " + IDBConstants.TABLE_TRANSACTION
			+" where "+ IDBConstants.ATTR_AUTOINDEX  +"="+indexTrans ;
			System.err.println(query);
			ResultSet rs = stm.executeQuery(query);
			rs.next();
				Transaction transaction=new Transaction(
					indexTrans,
					rs.getString(IDBConstants.ATTR_DESCRIPTION),
					rs.getDate(IDBConstants.ATTR_TRANSACTION_DATE),
					rs.getDate(IDBConstants.ATTR_VERIFICATION_DATE),
					rs.getDate(IDBConstants.ATTR_POSTED_DATE),
					rs.getString(IDBConstants.ATTR_REFERENCE_NO),
					null,
					null,
					(short) -1,
					null,
					false);
			return transaction;
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public Transaction[] getTransactionByUnit(Connection conn,long indexUnit) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			String query="SELECT * FROM " + IDBConstants.TABLE_TRANSACTION
			+" where "+ IDBConstants.ATTR_UNIT  +"="+indexUnit ;
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()){
					vresult.add(new Transaction(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getDate(IDBConstants.ATTR_TRANSACTION_DATE),
						rs.getDate(IDBConstants.ATTR_VERIFICATION_DATE),
						rs.getDate(IDBConstants.ATTR_POSTED_DATE),
						rs.getString(IDBConstants.ATTR_REFERENCE_NO),
						getJournal(rs.getLong(IDBConstants.ATTR_JOURNAL),conn),
						null,
						rs.getShort(IDBConstants.ATTR_STATUS),
						getUnitByIndex(indexUnit,conn), false));
			}
			Transaction[] transaction=new Transaction[vresult.size()];
			vresult.copyInto(transaction);
			return transaction;
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public TransactionPosted[] getTransactionCriteria(Connection conn,String query) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);
			while(rs.next()){
					vresult.add(new TransactionPosted(
						rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_DESCRIPTION),
						rs.getDate(IDBConstants.ATTR_TRANSACTION_DATE),
						rs.getString(IDBConstants.ATTR_REFERENCE_NO),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT),conn)
						));
			}
			TransactionPosted[] transposted=new TransactionPosted[vresult.size()];
			vresult.copyInto(transposted);
			return transposted;
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}

	public String getLastReferenceNo(Connection conn, String likeClausa)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			String query = "select max(" + IDBConstants.ATTR_REFERENCE_NO +
			") as " + IDBConstants.ATTR_REFERENCE_NO +
			" from " + IDBConstants.TABLE_TRANSACTION +
			" where " + IDBConstants.ATTR_REFERENCE_NO +
			" like '" + likeClausa + "%'" +
			" and " + IDBConstants.ATTR_ISVOID + "=false";

			ResultSet rs = stm.executeQuery(query);
			rs.next();

			return rs.getString(IDBConstants.ATTR_REFERENCE_NO);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public String getLastReferenceNoByYear(Connection conn, int year, String likeClausa)
	throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			String query = "select max(" + IDBConstants.ATTR_REFERENCE_NO +
			") as " + IDBConstants.ATTR_REFERENCE_NO +
			" from " + IDBConstants.TABLE_TRANSACTION +
			" where year(" + IDBConstants.ATTR_TRANSACTION_DATE + ")=" +
			String.valueOf(year) +
			" and " + IDBConstants.ATTR_REFERENCE_NO +
			" like '" + likeClausa + "%'" +
			" and " + IDBConstants.ATTR_ISVOID + "=false";

			ResultSet rs = stm.executeQuery(query);
			rs.next();

			return rs.getString(IDBConstants.ATTR_REFERENCE_NO);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public void updateBeginningCashDetail(long index, BeginningCashDetail detail, Connection conn) throws SQLException {
//		PreparedStatement stm = null;
//
//		try {
//			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_BEGINNING_CASH_DETAIL + " SET " +
//					IDBConstants.ATTR_BEGINNING_BALANCE + "=?, " +
//					IDBConstants.ATTR_CASH_ACCOUNT + "=?, " +
//					IDBConstants.ATTR_ACCOUNT + "=?, " +
//					IDBConstants.ATTR_VALUE + "=?, " +
//					IDBConstants.ATTR_CURRENCY + "=?, " +
//					IDBConstants.ATTR_EXCHANGE_RATE + "=? " +
//					" WHERE " +
//					IDBConstants.ATTR_BEGINNING_BALANCE + "=? " +
//					" AND " +
//					IDBConstants.ATTR_CASH_ACCOUNT + "=?");
//
//			stm.setLong(1, index);
//			stm.setLong(2, detail.getIndex());
//			stm.setLong(3, detail.getAccountIndex());
//			stm.setDouble(4, detail.getValue());
//			stm.setLong(5, detail.getCurrencyIndex());
//			stm.setDouble(6, detail.getRate());
//			stm.setLong(7, index);
//			stm.setLong(8, detail.getIndex());
//			stm.executeUpdate();
//		}
//		catch(Exception ex) {
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//		finally {
//			if(stm != null)
//				stm.close();
//		}
	}

	public boolean isBeginningCashDetail(long index, BeginningCashDetail detail, Connection conn) throws SQLException {
		PreparedStatement stm = null;
		try {
			String query = "SELECT * FROM " + IDBConstants.TABLE_BEGINNING_CASH_DETAIL +
			" WHERE " +
			IDBConstants.ATTR_BEGINNING_BALANCE + "=? " +
			" AND " +
			IDBConstants.ATTR_CASH_ACCOUNT + "=?";
			stm = conn.prepareStatement(query);
			stm.setLong(1, index);
			stm.setLong(2, detail.getIndex());

			ResultSet rs = stm.executeQuery();
			if(rs.next())
				return true;
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}



	public void updateBeginningBankDetail(long index, BeginningBankDetail detail, Connection conn) throws SQLException {
//		PreparedStatement stm = null;
//
//		try {
//			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_BEGINNING_BANK_DETAIL + " SET " +
//					IDBConstants.ATTR_BEGINNING_BALANCE + "=?, " +
//					IDBConstants.ATTR_BANK_ACCOUNT + "=?, " +
//					IDBConstants.ATTR_ACCOUNT + "=?, " +
//					IDBConstants.ATTR_VALUE + "=?, " +
//					IDBConstants.ATTR_CURRENCY + "=?, " +
//					IDBConstants.ATTR_EXCHANGE_RATE + "=? " +
//					" WHERE " +
//					IDBConstants.ATTR_BEGINNING_BALANCE + "=? " +
//					" AND " +
//					IDBConstants.ATTR_BANK_ACCOUNT + "=?");
//
//			stm.setLong(1, index);
//			stm.setLong(2, detail.getIndex());
//			stm.setLong(3, detail.getAccountIndex());
//			stm.setDouble(4, detail.getValue());
//			stm.setLong(5, detail.getCurrencyIndex());
//			stm.setDouble(6, detail.getRate());
//			stm.setLong(7, index);
//			stm.setLong(8, detail.getIndex());
//			stm.executeUpdate();
//		}
//		catch(Exception ex) {
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		}
//		finally {
//			if(stm != null)
//				stm.close();
//		}
	}

	public boolean isBeginningBankDetail(long index, BeginningBankDetail detail, Connection conn) throws SQLException {
//		PreparedStatement stm = null;
//		try {
//			String query = "SELECT * FROM " + IDBConstants.TABLE_BEGINNING_BANK_DETAIL +
//			" WHERE " +
//			IDBConstants.ATTR_BEGINNING_BALANCE + "=? " +
//			" AND " +
//			IDBConstants.ATTR_BANK_ACCOUNT + "=?";
//			stm = conn.prepareStatement(query);
//			stm.setLong(1, index);
//			stm.setLong(2, detail.getIndex());
//
//			ResultSet rs = stm.executeQuery();
//			if(rs.next())
//				return true;
//			return false;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			throw new SQLException(OtherSQLException.getMessage(ex));
//		} finally {
//			if (stm != null)
//				stm.close();
//		}
		return true;
	}
    public Account findFirstAccount(String string, Connection conn) throws SQLException {
        PreparedStatement stm = conn.prepareStatement("select " + IDBConstants.ATTR_AUTOINDEX
                + " from " + IDBConstants.TABLE_ACCOUNT + " where "
                + IDBConstants.ATTR_ACCOUNT_NAME + " like ?" );
        stm.setString(1,string);
        ResultSet resultSet = stm.executeQuery();
        if (resultSet.next())
        {
            long firstIdx = resultSet.getLong(1);
            return this.getAccount(firstIdx,conn);
        }
        return null;
    }


	public double getDebitValue(Connection conn,String query) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			if(rs.next()) {
				return rs.getDouble("debitvalue");
			}
			return 0;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}
	public BigDecimal getPeriodicDebitValueByAccount(Connection connection,
			Account account, Date dateFrom, Date dateTo) throws SQLException {

		PreparedStatement stm = null;
		java.sql.Date sqlDateFrom = utilToSqlDate(dateFrom);
		java.sql.Date sqlDateTo = utilToSqlDate(dateTo);

		try {
			String query = "SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue " +
					"FROM transvalueposted tv, account ac, transactionposted tp WHERE " +
					"ac.autoindex = tv.account AND tp.autoindex=transactionposted " +
					"AND ac.TREEPATH LIKE ? AND tp.TRANSACTIONDATE between ? and ?";

			stm = connection.prepareStatement(query);
			stm.setString(1, account.getTreePath() + "%");
			stm.setDate(2, sqlDateFrom);
			stm.setDate(3, sqlDateTo);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				BigDecimal num = rs.getBigDecimal(1);
				if (num != null)
					return num;
			}

			return new BigDecimal(0);
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createDefaultUnit(DefaultUnit dUnit, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			String query = "INSERT INTO " + IDBConstants.TABLE_DEFAULT_UNIT +"( " + IDBConstants.ATTR_AUTOINDEX
			+ ", " +IDBConstants.ATTR_UNIT + ") " + "values (?,?)";
			stm = conn.prepareStatement(query);
			stm.setInt(1, dUnit.getIndex());
			if(dUnit.getUnit()!=null)
				stm.setLong(2, dUnit.getUnit().getIndex());
			else
				stm.setNull(2, Types.LONGVARCHAR);
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteDefaultUnit(DefaultUnit dUnit, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			String query = "DELETE FROM " + IDBConstants.TABLE_DEFAULT_UNIT +
			" WHERE " + IDBConstants.ATTR_UNIT + "=" + dUnit.getUnit() ;

			stm = conn.createStatement();
			stm.executeUpdate(query);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateDefaultUnit(int index, DefaultUnit dUnit, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			String query = "UPDATE " + IDBConstants.TABLE_DEFAULT_UNIT + " SET " +
			IDBConstants.ATTR_UNIT + "=? " +
			" WHERE " + IDBConstants.ATTR_AUTOINDEX + " = " + index + "";
			System.err.println(query);

			stm = conn.prepareStatement(query);
			if(dUnit.getUnit()!=null)
				stm.setLong(1, dUnit.getUnit().getIndex());
			else
				stm.setNull(1, Types.LONGVARCHAR);
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public DefaultUnit[] getAllDefaultUnit(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			String query = "SELECT * FROM " + IDBConstants.TABLE_DEFAULT_UNIT;
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new DefaultUnit(getUnitByIndex(rs.getLong("unit"), conn)));
			}

			DefaultUnit[] dUnit = new DefaultUnit[vresult.size()];
			vresult.copyInto(dUnit);
			return dUnit;

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public DefaultUnit getDefaultUnit(Connection conn) throws SQLException {
		Statement stm = null;

		try {
			String query = "SELECT * FROM " + IDBConstants.TABLE_DEFAULT_UNIT;
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			DefaultUnit dUnit = null;
			if(rs.next()) {
				dUnit = new DefaultUnit(getUnitByIndex(rs.getLong("unit"), conn));
			}

			return dUnit;

		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}


	public void createTransactionPeriod(TransactionPeriod tPeriod, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		java.sql.Date sqlStartDate = utilToSqlDate(tPeriod.getStartDate());
		java.sql.Date sqlEndDate = utilToSqlDate(tPeriod.getEndDate());

		try {
			String query = "insert into " + IDBConstants.TABLE_TRANSACTION_PERIOD + "( " + IDBConstants.ATTR_STARTDATE
			+ ", " +IDBConstants.ATTR_ENDDATE + ", " + IDBConstants.ATTR_STATUS + ") " + "values (?,?,?)";

			stm = conn.prepareStatement(query);
			stm.setDate(1, sqlStartDate );
			stm.setDate(2, sqlEndDate );
			stm.setString(3, tPeriod.getStatus());
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void deleteTransactionPeriod(TransactionPeriod tPeriod, Connection conn) throws SQLException {
		Statement stm = null;

		try {
			String query = "DELETE FROM " + IDBConstants.TABLE_TRANSACTION_PERIOD+
			" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + tPeriod.getIndex();

			stm = conn.createStatement();
			stm.executeUpdate(query);
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public TransactionPeriod[] getAllTransactonPeriod(Connection conn) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			String query = "SELECT * FROM " + IDBConstants.TABLE_TRANSACTION_PERIOD;
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				vresult.addElement(new TransactionPeriod(rs.getInt(IDBConstants.ATTR_AUTOINDEX),
					rs.getDate(IDBConstants.ATTR_STARTDATE), rs.getDate(IDBConstants.ATTR_ENDDATE),
					rs.getString(IDBConstants.ATTR_STATUS)));
			}

			TransactionPeriod[] tPeriod = new TransactionPeriod[vresult.size()];
			vresult.copyInto(tPeriod);
			return tPeriod;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateTransactionPeriod(int index, TransactionPeriod tPeriod, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		java.sql.Date sqlStartDate = utilToSqlDate(tPeriod.getStartDate());
		java.sql.Date sqlEndDate = utilToSqlDate(tPeriod.getEndDate());

		try {
			String query = "UPDATE " + IDBConstants.TABLE_TRANSACTION_PERIOD + " SET " +
			IDBConstants.ATTR_STARTDATE + "=?, " +
			IDBConstants.ATTR_ENDDATE + "=?, " +
			IDBConstants.ATTR_STATUS + "=? " +
			" WHERE " + IDBConstants.ATTR_AUTOINDEX + " = " + index + "";
			System.err.println(query);

			stm = conn.prepareStatement(query);
			stm.setDate(1, sqlStartDate);
			stm.setDate(2, sqlEndDate);
			stm.setString(3, tPeriod.getStatus());

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void updateStatusTransactionPeriod(int index, String status, Connection conn) throws SQLException {
		PreparedStatement stm = null;

		try {
			String query = "UPDATE " + IDBConstants.TABLE_TRANSACTION_PERIOD + " SET " +
			IDBConstants.ATTR_STATUS + "=? " +
			" WHERE " + IDBConstants.ATTR_AUTOINDEX + " = " + index + "";
			System.err.println(query);

			stm = conn.prepareStatement(query);
			stm.setString(1, status);

			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public int getIndexTransactonPeriod(Date starDate, Date endDate, Connection conn) throws SQLException {
		Statement stm = null;
		java.sql.Date sqlStartDate = utilToSqlDate(starDate);
		java.sql.Date sqlEndDate = utilToSqlDate(endDate);

		try {
			String query = "SELECT " + IDBConstants.ATTR_AUTOINDEX + " FROM " + IDBConstants.TABLE_TRANSACTION_PERIOD
				+ " WHERE " + IDBConstants.ATTR_STARTDATE + " = '" + sqlStartDate + "'"
				+ " AND " + IDBConstants.ATTR_ENDDATE + " = '" + sqlEndDate + "'";
			System.err.println(query);
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			if(rs.next()) {
				return (rs.getInt(IDBConstants.ATTR_AUTOINDEX));
			}
			else return 0;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}
	public boolean findReferenceNo(Connection conn, String refNo) throws SQLException {
		Statement stm = null;
		try {
			stm = conn.createStatement();
			String query = "select " + IDBConstants.ATTR_REFERENCE_NO +
			" from " + IDBConstants.TABLE_TRANSACTION +
			" where " + IDBConstants.ATTR_REFERENCE_NO +
			" = '" + refNo + "' and " + IDBConstants.ATTR_ISVOID + "=false";

			ResultSet rs = stm.executeQuery(query);
			if(rs.next())
				return true;
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public List getMonthYearPayrollByQuery(Connection connection, String query) throws SQLException {
		Statement stm = null;
		try {
			stm = connection.createStatement();

			ResultSet rs = stm.executeQuery(query);
			List list = new ArrayList();

			while(rs.next()){
				long[] monthYear = {-1, -1,-1};

				monthYear[0] = rs.getLong(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_MONTH);
				monthYear[1] = rs.getLong(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_YEAR);
				monthYear[2] = rs.getLong(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_TRANSACTION);

				list.add(monthYear);
			}

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public List getDaysAndAreaCode(Connection connection, String query) throws SQLException{
		Statement stm = null;
		try {
			stm = connection.createStatement();

			ResultSet rs = stm.executeQuery(query);
			List list = new ArrayList();

			while (rs.next()) {
				TimeSheetSummary tss = new TimeSheetSummary(
						rs.getLong(pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_PERSONAL),
						rs.getString(pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_AREA_CODE),
						0,
						0,
						rs.getInt(pohaci.gumunda.titis.project.dbapi.IDBConstants.ATTR_DAYS));

				list.add(tss);
			}

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	public FormulaEntity getFormula(Connection connection, long idxEmp, long idxAcct) throws SQLException{
		Statement stm = null;
		try {
			stm = connection.createStatement();

			String query = "select epc.* from employeepayrollcomponent epc, payrollcomponent pc where "
				+ "epc.component=pc.autoindex and pc.account=" + idxAcct + " and epc.employee=" + idxEmp;

			ResultSet rs = stm.executeQuery(query);

			if (rs.next()) {
				IFormulaParser parser = new PayrollFormulaParser(connection);
				return parser.parseToFormula(
						rs.getString(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_FORMULA),
						rs.getShort(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_FORMULA_MONTH),
						new NumberRounding(
								rs.getShort(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_ROUND_VALUE),
								rs.getInt(pohaci.gumunda.titis.hrm.dbapi.IDBConstants.ATTR_PRECISION)));
			}

			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(OtherSQLException.getMessage(ex));
		} finally {
			if (stm != null)
				stm.close();
		}
	}

	class PayrollFormulaParser implements IHRMSQL.IFormulaParser {
		TokenParser itemParser = null;
		Formula mformula = new Formula();
		Connection connection = null;

		public PayrollFormulaParser(Connection connection) {
			this.connection = connection;
		}
		private TokenParser getTokenParser() {
			return new PayrollTokenParser(this.connection);
		}
		public FormulaEntity parseToFormula(String formulaStr) {
			try {
				short whichMonth = 0;
				short roundingMode = -1;
				int precision = 0;
				if (formulaStr == null)
					return null;
				itemParser = getTokenParser();
				mformula.parseFormula(formulaStr, itemParser);
				return mformula.createFormulaEntity(whichMonth,
						new NumberRounding(roundingMode, precision));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public FormulaEntity parseToFormula(String formulaStr,
				short whichMonth, NumberRounding numberRounding) {
			try {
				itemParser = getTokenParser();
				mformula.parseFormula(formulaStr, itemParser);
				return mformula.createFormulaEntity(whichMonth, numberRounding);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public CompanyLoan[] getCompanyLoanByAccount(Connection conn, Account account) throws SQLException {
		Statement stm = null;
		Vector vresult = new Vector();

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM " + IDBConstants.TABLE_COMPANY_LOAN +
					" WHERE " + IDBConstants.ATTR_ACCOUNT + "=" + account.getIndex());

			while(rs.next()) {
				vresult.addElement(new CompanyLoan(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						this.getCreditorList(rs.getLong(IDBConstants.ATTR_CREDITOR), conn),
						rs.getString(IDBConstants.ATTR_CODE),
						rs.getString(IDBConstants.ATTR_NAME),
						this.getCurrency(rs.getLong(IDBConstants.ATTR_CURRENCY), conn),
						rs.getDouble(IDBConstants.ATTR_INITIAL),
						this.getAccount(rs.getLong(IDBConstants.ATTR_ACCOUNT), conn),
						this.getUnitByIndex(rs.getLong(IDBConstants.ATTR_UNIT), conn),
						rs.getString(IDBConstants.ATTR_REMARKS)));
			}

			CompanyLoan[] result = new CompanyLoan[vresult.size()];
			vresult.copyInto(result);
			return result;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

	public void createPayrollDeptValue(Connection conn,
			PayrollDeptValue payrollDeptValue) throws SQLException {
		PreparedStatement stm = null;

		try {
			String query = "INSERT INTO "
				+IDBConstants.TABLE_PAYROLL_DEPARTMENT_VALUE + "("
				+pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_EMPLOYEE_PAYROLL + "," //1
				+IDBConstants.ATTR_DEPARTMENT  + "," //2
				+IDBConstants.ATTR_ACCOUNT + "," //4
				+IDBConstants.ATTR_VALUE //5
				+")" + " values (?, ?, ?, ?)";
			stm = conn.prepareStatement(query);

			stm.setLong(1, payrollDeptValue.getEmployeePayrollSubmit().getEmployeePayroll()  );
			stm.setLong(2, payrollDeptValue.getDepartment().getIndex());
			stm.setLong(3, payrollDeptValue.getAccount().getIndex());
			stm.setDouble(4, payrollDeptValue.getAccvalue());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public void createTaxtArtDeptValue(Connection conn,
			TaxArt21DeptValue payrollTaxArtDeptValue) throws SQLException {
		PreparedStatement stm = null;

		try {
			String query = "INSERT INTO "
				+IDBConstants.TABLE_TAXART21_DEPARTMENT_VALUE + "("
				+pohaci.gumunda.titis.hrm.dbapi.IDBConstants.TABLE_TAX_ART_21_SUBMIT + "," //1
				+IDBConstants.ATTR_DEPARTMENT  + "," //2
				+IDBConstants.ATTR_ACCOUNT + "," //4
				+IDBConstants.ATTR_VALUE //5
				+")" + " values (?, ?, ?, ?)";
			stm = conn.prepareStatement(query);
			stm.setLong(1, payrollTaxArtDeptValue.getTaxArt21Submit().getIndex());
			stm.setLong(2, payrollTaxArtDeptValue.getDepartment().getIndex());
			stm.setLong(3, payrollTaxArtDeptValue.getAccount().getIndex());
			stm.setDouble(4, payrollTaxArtDeptValue.getAccvalue());
			stm.executeUpdate();
		} catch (Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if (stm != null)
				stm.close();
		}
	}

	public List getLeafAccount(Connection connection) {
		Statement stm = null;
		List list = new ArrayList();

		try {
			stm = connection.createStatement();

			String query =
				"Select * from account where isgroup=false order by accountcode";

			ResultSet rs = stm.executeQuery(query);

			while(rs.next()){
				list.add(new Account(rs.getLong(IDBConstants.ATTR_AUTOINDEX),
						rs.getString(IDBConstants.ATTR_ACCOUNT_CODE),
						rs.getString(IDBConstants.ATTR_ACCOUNT_NAME),
						rs.getShort(IDBConstants.ATTR_CATEGORY),
						rs.getBoolean(IDBConstants.ATTR_IS_GROUP),
						rs.getShort(IDBConstants.ATTR_BALANCE_CODE),
						rs.getString(IDBConstants.ATTR_NOTE),
                        rs.getString(IDBConstants.ATTR_PATH)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * @param connection
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public HashMap getGroupedDebitValue(Connection conn, String query, String by) throws SQLException {
		HashMap map = new HashMap();

		Statement stm = null;

		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()){
				map.put(new Long(rs.getLong(by)), new Double(rs.getDouble("DEBITVALUE")));
			}
			return map;
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}
		finally {
			if(stm != null)
				stm.close();
		}
	}
	/**
	 * @param connection
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List getAllNullSubsTransaction(Connection conn, String query) throws SQLException {
		List list = new ArrayList();

		Statement stm = null;


		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(query);

			while(rs.next()) {
				HashMap map = new HashMap();
				map.put("TRANSACTIONDATE", rs.getDate("TRANSACTIONDATE"));
				map.put("DESCRIPTION", rs.getString("DESCRIPTION"));
				map.put("REFERENCENO", rs.getString("REFERENCENO"));
				map.put("DEBITVALUE", new Double(rs.getDouble("DEBITVALUE")));
				map.put("CURRENCY", getCurrency(rs.getLong("CURRENCY"), conn).getSymbol());

				list.add(map);
			}


		} catch (SQLException e) {
			throw new SQLException(OtherSQLException.getMessage(e));
		}


		return list;
	}
	/**
	 * @param m_conn
	 * @param table
	 * @param index
	 * @param detail
	 * @throws SQLException
	 */
	public void updateBeginningBalanceSheetDetail(Connection conn,
			String table, long index, BeginningBalanceSheetDetail detail) throws SQLException {

		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + table + " SET " +
					IDBConstants.ATTR_TRANSACTION + "= " + detail.getTrans().getIndex() +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			//int col = 1;
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}
	/**
	 * @param m_conn
	 * @param index
	 * @param entry
	 * @throws SQLException
	 */
	public void updateBeginningBalanceSheetDetail(Connection conn,
			long index, BeginningBalanceSheetEntry entry) throws SQLException {
		PreparedStatement stm = null;

		try {
			stm = conn.prepareStatement("UPDATE " + IDBConstants.TABLE_BEGINNING_BALANCE_SHEET_ENTRY + " SET " +
					IDBConstants.ATTR_TRANSACTION + "= " + entry.getTrans().getIndex() +
					" WHERE " + IDBConstants.ATTR_AUTOINDEX + "=" + index);

			//int col = 1;
			stm.executeUpdate();
		}
		catch(Exception ex) {
			throw new SQLException(OtherSQLException.getMessage(ex));
		}

		finally {
			if(stm != null)
				stm.close();
		}
	}

}
