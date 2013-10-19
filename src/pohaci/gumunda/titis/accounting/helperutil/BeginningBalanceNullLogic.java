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

import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;

/**
 * @author dark-knight
 *
 */
public class BeginningBalanceNullLogic {

	public List generateList(Connection connection, Date date) {
		AccountingSQLSAP sql = new AccountingSQLSAP();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


		// siapkan daftar subsidiary
		List subList = new ArrayList();
		try {
			SubsidiaryAccountSetting[] subs = sql.getAllSubsidiaryAccountSetting(connection);

			for(int i=0; i<subs.length; i++) {
				subList.add(subs[i].getAccount());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List list = new ArrayList();

		Iterator iterator = subList.iterator();
		while(iterator.hasNext()) {
			Account account = (Account) iterator.next();

			List nullList = getAllNull(connection, account, date);
			if (!nullList.isEmpty()) {
				double val = getSubValues(connection, account, date);

				BeginningBalanceTransferEntity entity = new BeginningBalanceTransferEntity();
				entity.setCode(account.getCode());
				entity.setDesc(account.getName());
				if (val >= 0) {
					entity.setDebitrp(new Double(val));
					entity.setCreditrp(new Double(0));
				} else {
					entity.setDebitrp(new Double(0));
					entity.setCreditrp(new Double(-val));
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

				// null list
				Iterator iter = nullList.iterator();
				while(iter.hasNext()) {
					HashMap map = (HashMap) iter.next();

					BeginningBalanceTransferEntity ent = new BeginningBalanceTransferEntity();
					ent.setCode((String) map.get("REFERENCENO"));
					ent.setDesc(dateFormat.format((Date) map.get("TRANSACTIONDATE")) +  " " + (String)map.get("DESCRIPTION"));
					ent.setCurr((String) map.get("CURRENCY"));
					Double v = (Double) map.get("DEBITVALUE");

					if (v.doubleValue() >= 0) {
						ent.setDebit(v);
						ent.setCredit(new Double(0));
					} else {
						ent.setDebit(new Double(0));
						ent.setCredit(new Double(-v.doubleValue()));
					}

					list.add(ent);
				}
			}
		}

		return list;
	}

	/**
	 * @param connection
	 * @param account
	 * @param date
	 * @return
	 */
	private List getAllNull(Connection connection, Account account, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List list = new ArrayList();

		String sql =
			"select tp.autoindex, " +
			"tp.transactiondate, tp.description, tp.referenceno, (accvalue * (1-2*balancecode)) debitvalue, tv.currency, " +
			"tv.account " +
			"from transactionposted tp, transvalueposted tv, account ac " +
			"where " +
			"tp.autoindex=tv.transactionposted " +
			"and ac.autoindex=tv.account " +
			"and ac.treepath like '" + account.getTreePath() + "%' " +
			"and tv.subsidiaryaccount=-1 " +
			"and tp.transactiondate <= '" + dateFormat.format(date) + "'";


		AccountingSQLSAP sqlsap = new AccountingSQLSAP();

		try {
			list = sqlsap.getAllNullSubsTransaction(connection, sql);

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * @param connection
	 * @param account
	 * @param date
	 * @param l
	 * @return
	 */
	/*private HashMap getSubBalByCurr(Connection connection, Account account, Date date, long index) {
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
	}*/

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
			"AND tv.subsidiaryaccount = -1 " +
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
	private double getSubValues(Connection connection, Account account, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


		String sql =
			"SELECT SUM(exchangerate*accvalue*(1-2*balancecode)) debitvalue " +
			"FROM transvalueposted tv, account ac, transactionposted tp " +
			"WHERE ac.autoindex = tv.account AND tp.autoindex=transactionposted " +
			"AND ac.treepath LIKE '" + account.getTreePath() + "%' " +
			"AND tp.TRANSACTIONDATE <= '" +  dateFormat.format(date) + "' " +
			"AND tv.subsidiaryaccount = -1";

		AccountingSQLSAP sqlsap = new AccountingSQLSAP();

		try {
			double val = sqlsap.getDebitValue(connection, sql);

			return val;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * @param account
	 * @return
	 */
	/*private double countBalanceIDR(Connection connection, Account account, Date date) {
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
	}*/
}
