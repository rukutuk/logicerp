/**
 *
 */
package pohaci.gumunda.titis.accounting.helperutil;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.dbapi.HRMSQLSAP;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.ProjectData;
import pohaci.gumunda.titis.project.dbapi.ProjectSQLSAP;

/**
 * @author dark-knight
 *
 */
public class BeginningBalanceTransferLogic {

	public List generateList(Connection connection, Date date) {
		AccountingSQLSAP sql = new AccountingSQLSAP();

		// siapkan daftar subsidiary
		Map subMap = new HashMap();
		try {
			SubsidiaryAccountSetting[] subs = sql.getAllSubsidiaryAccountSetting(connection);

			for(int i=0; i<subs.length; i++) {
				subMap.put(new Long(subs[i].getAccount().getIndex()), subs[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List list = new ArrayList();

		// daftar akun

		List acctList = sql.getLeafAccount(connection);

		double[] total = {0, 0};

		Iterator iterator = acctList.iterator();
		while(iterator.hasNext()) {
			Account account = (Account) iterator.next();

			BeginningBalanceTransferEntity entity = new BeginningBalanceTransferEntity();
			entity.setCode(account.getCode());
			entity.setDesc(account.getName());

			// saldo
			double balanceIDR = countBalanceIDR(connection, account, date);
			if (balanceIDR >= 0) {
				entity.setDebitrp(new Double(balanceIDR));
				entity.setCreditrp(new Double(0));
				total[0] += balanceIDR;
			} else {
				entity.setDebitrp(new Double(0));
				entity.setCreditrp(new Double(-balanceIDR));
				total[1] += (-balanceIDR);
			}


			// saldo dalam rp dan $
			Currency[] currencies = null;
			try {
				currencies = sql.getAllCurrency(connection);

			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			HashMap balance = countBalance(connection, account, date);
			int c = 0;

			for (int i=0; i<currencies.length; i++) {
				if (balance.containsKey(new Long(currencies[i].getIndex()))) {
					if (c > 0) {
						entity = new BeginningBalanceTransferEntity();
						entity.setCode("");
						entity.setDesc("");
					}
					Double valbal = (Double) balance.get(new Long(currencies[i].getIndex()));
					entity.setCurr(currencies[i].getSymbol());
					if (valbal.doubleValue() >= 0) {
						entity.setDebit(valbal);
						entity.setCredit(new Double(0));
					} else {
						entity.setDebit(new Double(0));
						entity.setCredit(new Double(-valbal.doubleValue()));
					}
					c++;
					list.add(entity);
				}
			}
			if (c==0) {
				list.add(entity);
			}

			// subsidiary
			if (subMap.containsKey(new Long(account.getIndex()))) {
				SubsidiaryAccountSetting sub = (SubsidiaryAccountSetting) subMap.get(new Long(account.getIndex()));

				HashMap subValues = getSubValues(connection, account, date);

				Set keys = subValues.keySet();
				Iterator iter = keys.iterator();
				while(iter.hasNext()) {
					Long key = (Long) iter.next();

					BeginningBalanceTransferEntity subChild = new BeginningBalanceTransferEntity();

					Double val = (Double) subValues.get(key);

					if (val.doubleValue() != 0) {
						subChild.setCode("");
						if (val.doubleValue() >= 0) {
							subChild.setDebitrp(val);
							subChild.setCreditrp(new Double(0));
						} else {
							subChild.setDebitrp(new Double(0));
							subChild.setCreditrp(new Double(-val.doubleValue()));
						}

						AccountingSQLSAP sqlsap = new AccountingSQLSAP();
						try {
							if (sub.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.CASH)){
								CashAccount cashAccount = sqlsap.getCashAccountByIndex(key.longValue(), connection);
								if (cashAccount != null)
									subChild.setDesc(cashAccount.getCode() + " " + cashAccount.getName());
							} else if (sub.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.BANK)){
								BankAccount bankAccount = sqlsap.getBankAccountByIndex(key.longValue(), connection);
								if (bankAccount != null)
									subChild.setDesc(bankAccount.getCode() + " " + bankAccount.getName());
							} else if (sub.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.LOAN)){
								CompanyLoan loan = sqlsap.getCompanyLoan(key.longValue(), connection);
								if (loan != null)
									subChild.setDesc(loan.getCode() + " " + loan.getName());
							} else if (sub.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.EMPLOYEE)){
								HRMSQLSAP hrm = new HRMSQLSAP();
								Employee emp = hrm.getEmployeeByIndex(key.longValue(), connection);
								if (emp != null)
									subChild.setDesc(emp.getEmployeeNo() + " " + emp.getFirstName() + " " + emp.getMidleName() + " " + emp.getLastName());
							} else if (sub.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.PARTNER)){
								ProjectSQLSAP pro = new ProjectSQLSAP();
								Partner partner = pro.getPartnerByIndex(key.longValue(), connection);
								if (partner != null)
									subChild.setDesc(partner.getCode() + " " + partner.getName());
							} else if (sub.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.CUSTOMER)){
								ProjectSQLSAP pro = new ProjectSQLSAP();
								Customer cust = pro.getCustomerByIndex(key.longValue(), connection);
								if (cust != null)
									subChild.setDesc(cust.getCode() + " " + cust.getName());
							} else if (sub.getSubsidiaryAccount().equals(SubsidiaryAccountSetting.PROJECT)){
								ProjectSQLSAP pro = new ProjectSQLSAP();
								ProjectData proj = pro.getProjectDataByIndex(key.longValue(), connection);
								if (proj != null)
									subChild.setDesc(proj.getCode() + " " + proj.getIPCNo());
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}

						// hitung currencynya
						HashMap valsubBals = getSubBalByCurr(connection, account, date, key.longValue());
						int d = 0;

						for (int i=0; i<currencies.length; i++) {
							if (valsubBals.containsKey(new Long(currencies[i].getIndex()))) {
								if (d > 0) {
									subChild = new BeginningBalanceTransferEntity();
									subChild.setCode("");
									subChild.setDesc("");
								}
								Double valsubBal = (Double) valsubBals.get(new Long(currencies[i].getIndex()));
								subChild.setCurr(currencies[i].getSymbol());
								if (valsubBal.doubleValue() >= 0) {
									subChild.setDebit(valsubBal);
									subChild.setCredit(new Double(0));
								} else {
									subChild.setDebit(new Double(0));
									subChild.setCredit(new Double(-valsubBal.doubleValue()));
								}
								d++;
								list.add(subChild);
							}
						}
						if (d==0) {
							list.add(subChild);
						}
					}
				}
			}



		}
		BeginningBalanceTransferEntity bal = new BeginningBalanceTransferEntity();
		bal.setCode("");
		bal.setDesc("TOTAL");
		bal.setDebitrp(new Double(total[0]));
		bal.setCreditrp(new Double(total[1]));
		list.add(bal);

		return list;
	}

	/**
	 * @param connection
	 * @param account
	 * @param date
	 * @param l
	 * @return
	 */
	private HashMap getSubBalByCurr(Connection connection, Account account, Date date, long index) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HashMap map = new HashMap();

		String sql =
			"SELECT tv.currency, SUM(accvalue*(1-2*balancecode)) debitvalue " +
			"FROM transvalueposted tv, account ac, transactionposted tp " +
			"WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted " +
			"AND ac.treepath LIKE '" + account.getTreePath() + "%' " +
			"AND tp.TRANSACTIONDATE <= '" +  dateFormat.format(date) + "' AND " +
			"tv.subsidiaryaccount = " + index + " " +
			"GROUP BY tv.currency";

		AccountingSQLSAP sqlsap = new AccountingSQLSAP();

		try {
			map = sqlsap.getGroupedDebitValue(connection, sql, "CURRENCY");

			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * @param connection
	 * @param account
	 * @param date
	 * @return
	 */
	private HashMap countBalance(Connection connection, Account account, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HashMap map = new HashMap();

		String sql =
			"SELECT tv.currency, SUM(accvalue*(1-2*balancecode)) debitvalue " +
			"FROM transvalueposted tv, account ac, transactionposted tp " +
			"WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted " +
			"AND ac.treepath LIKE '" + account.getTreePath() + "%' " +
			"AND tp.TRANSACTIONDATE <= '" +  dateFormat.format(date) + "' " +
			"GROUP BY tv.currency";

		AccountingSQLSAP sqlsap = new AccountingSQLSAP();

		try {
			map = sqlsap.getGroupedDebitValue(connection, sql, "CURRENCY");

			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * @param connection
	 * @param account
	 * @param date
	 * @return
	 */
	private HashMap getSubValues(Connection connection, Account account, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HashMap map = new HashMap();

		String sql =
			"SELECT tv.subsidiaryaccount, SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue " +
			"FROM transvalueposted tv, account ac, transactionposted tp " +
			"WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted " +
			"AND ac.treepath LIKE '" + account.getTreePath() + "%' " +
			"AND tp.TRANSACTIONDATE <= '" +  dateFormat.format(date) + "' " +
			"GROUP BY tv.subsidiaryaccount";

		AccountingSQLSAP sqlsap = new AccountingSQLSAP();

		try {
			map = sqlsap.getGroupedDebitValue(connection, sql, "SUBSIDIARYACCOUNT");

			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * @param account
	 * @return
	 */
	private double countBalanceIDR(Connection connection, Account account, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String sql =
			"SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue " +
			"FROM transvalueposted tv, account ac, transactionposted tp " +
			"WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted " +
			"AND ac.treepath LIKE '" + account.getTreePath() + "%' " +
			"AND tp.TRANSACTIONDATE <= '" +  dateFormat.format(date) + "'";

		AccountingSQLSAP sqlsap = new AccountingSQLSAP();

		try {
			double debitValue = sqlsap.getDebitValue(connection, sql);

			return debitValue;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
}
