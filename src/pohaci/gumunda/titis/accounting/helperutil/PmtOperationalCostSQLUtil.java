/**
 *
 */
package pohaci.gumunda.titis.accounting.helperutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardAccount;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCost;
import pohaci.gumunda.titis.accounting.entity.PmtOperationalCostDetail;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class PmtOperationalCostSQLUtil {
	private Connection connection;



	/**
	 * @param connection
	 */
	public PmtOperationalCostSQLUtil(Connection connection) {
		super();
		this.connection = connection;
	}



	public boolean ifTotalColumnExists() throws SQLException {
		Statement statement = connection.createStatement();

		String query =
			"select * from sys.all_tab_columns " +
			"where table_name='" +
			IDBConstants.TABLE_PMT_OPERATIONAL_COST.toUpperCase() + "' " +
			"and column_name='" +
			IDBConstants.ATTR_TOTAL.toUpperCase() + "'";

		ResultSet resultSet = statement.executeQuery(query);

		return resultSet.next();
	}

	public void addTotalColumn() throws SQLException {
		Statement statement = connection.createStatement();

		String query =
			"alter table " +
			IDBConstants.TABLE_PMT_OPERATIONAL_COST +
			" add (" +
			IDBConstants.ATTR_TOTAL +
			" fixed(38,2))";

		statement.executeUpdate(query);
	}

	public List findNoTotalRecords()  {
		List list = new ArrayList();

		GenericMapper mapper = MasterMap.obtainMapperFor(PmtOperationalCost.class);
		mapper.setActiveConn(connection);

		String clause = IDBConstants.ATTR_TOTAL + " is null order by " + IDBConstants.ATTR_TRANSACTION_DATE;
		list = mapper.doSelectWhere(clause);

		return list;
	}

	public List countTotal(List noTotalList) {
		List list = noTotalList;

		GenericMapper mapper = MasterMap.obtainMapperFor(PmtOperationalCostDetail.class);
		mapper.setActiveConn(connection);

		String clause =
			IDBConstants.ATTR_PMT_OPERASIONAL_COST +
			" in (select autoindex from " +
			IDBConstants.TABLE_PMT_OPERATIONAL_COST +
			" where " +
			IDBConstants.ATTR_TOTAL +
			" is null)";

		List detaiList = mapper.doSelectWhere(clause);

		HashMap map = new HashMap();

		Iterator iterator = detaiList.iterator();
		while(iterator.hasNext()) {
			PmtOperationalCostDetail detail = (PmtOperationalCostDetail) iterator.next();

			Long key = new Long(detail.getPmtOperationalCost().getIndex());
			if (map.containsKey(key)) {
				List dets = (List) map.get(key);
				dets.add(detail);
				map.put(key, dets);
			} else {
				List dets = new ArrayList();
				dets.add(detail);
				map.put(key, dets);
			}
		}

		String attr = "";

		HashMap jsMap = prepareJournalStandardAccountMap();

		iterator = list.iterator();
		while(iterator.hasNext()) {
			PmtOperationalCost payment = (PmtOperationalCost) iterator.next();
			Long key = new Long(payment.getIndex());

			if (map.containsKey(key)) {
				List dets = (List) map.get(key);
				Iterator iter = dets.iterator();
				double total = 0;

				if (payment.vgetCashAccount()!=null)
					attr = IConstants.ATTR_PMT_CASH;
				else if (payment.vgetBankAccount()!=null)
					attr = IConstants.ATTR_PMT_BANK;
				else {
					if (payment.igetPaymentSource().equalsIgnoreCase("CASH"))
						attr = IConstants.ATTR_PMT_CASH;
					else
						attr = IConstants.ATTR_PMT_BANK;
				}

				JournalStandardAccount[] jsas = (JournalStandardAccount[]) jsMap.get(attr);

				while(iter.hasNext()) {
					PmtOperationalCostDetail det = (PmtOperationalCostDetail) iter.next();

					for (int i=0; i<jsas.length; i++) {
						if (det.getAccount().getIndex() == jsas[i].getAccount().getIndex()) {
							if (!jsas[i].isCalculate() && !jsas[i].isHidden()) {
								if (jsas[i].getBalance()==0)
									total += det.getAccValue();
								else
									total -= det.getAccValue();
							}
						}
					}
				}
				payment.setTotal(total);
			}
		}

		return list;
	}



	/**
	 * @return
	 */
	private HashMap prepareJournalStandardAccountMap() {
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(connection, -1,
					IDBConstants.MODUL_ACCOUNTING);

		List journalStdList = helper
				.getJournalStandardSettingWithAccount(IConstants.PAYMENT_OPERASIONAL_COST);

		HashMap journalStandardAccountsMap = new HashMap();
		Iterator iterator = journalStdList.iterator();
		while(iterator.hasNext()) {
			JournalStandardSetting setting = (JournalStandardSetting) iterator.next();

			String key = setting.getAttribute();
			if (journalStandardAccountsMap.containsKey(key)) {

			} else {
				JournalStandard journal = setting.getJournalStandard();
				JournalStandardAccount[] jsAcc = journal.getJournalStandardAccount();

				journalStandardAccountsMap.put(key, jsAcc);
			}
		}
		return journalStandardAccountsMap;
	}

	public void updateTotal(List list) {
		GenericMapper mapper = MasterMap.obtainMapperFor(PmtOperationalCost.class);
		mapper.setActiveConn(connection);

		Iterator iterator = list.iterator();
		while(iterator.hasNext()) {
			PmtOperationalCost payment = (PmtOperationalCost) iterator.next();

			mapper.doUpdate(payment);
		}
	}
}
