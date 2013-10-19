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
import pohaci.gumunda.titis.accounting.entity.PmtProjectCost;
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.SalesItem;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class InvoiceSQLUtil {
	private Connection connection;



	/**
	 * @param connection
	 */
	public InvoiceSQLUtil(Connection connection) {
		super();
		this.connection = connection;
	}



	public boolean ifTotalColumnExists() throws SQLException {
		Statement statement = connection.createStatement();

		String query =
			"select * from sys.all_tab_columns " +
			"where table_name='" +
			IDBConstants.TABLE_SALES_INVOICE.toUpperCase() + "' " +
			"and column_name='" +
			IDBConstants.ATTR_TOTAL.toUpperCase() + "'";

		ResultSet resultSet = statement.executeQuery(query);

		return resultSet.next();
	}

	public void addTotalColumn() throws SQLException {
		Statement statement = connection.createStatement();

		String query =
			"alter table " +
			IDBConstants.TABLE_SALES_INVOICE +
			" add (" +
			IDBConstants.ATTR_TOTAL +
			" fixed(38,2))";

		statement.executeUpdate(query);
	}

	public List findNoTotalRecords()  {
		List list = new ArrayList();

		GenericMapper mapper = MasterMap.obtainMapperFor(SalesInvoice.class);
		mapper.setActiveConn(connection);

		String clause = IDBConstants.ATTR_TOTAL + " is null order by " + IDBConstants.ATTR_TRANSACTION_DATE;
		list = mapper.doSelectWhere(clause);

		return list;
	}

	public List countTotal(List noTotalList) {
		List list = noTotalList;

		GenericMapper mapper = MasterMap.obtainMapperFor(SalesItem.class);
		mapper.setActiveConn(connection);

		String clause =
			IDBConstants.ATTR_SALES_INVOICE +
			" in (select autoindex from " +
			IDBConstants.TABLE_SALES_INVOICE +
			" where " +
			IDBConstants.ATTR_TOTAL +
			" is null)";

		List detaiList = mapper.doSelectWhere(clause);

		HashMap map = new HashMap();

		Iterator iterator = detaiList.iterator();
		while(iterator.hasNext()) {
			SalesItem detail = (SalesItem) iterator.next();

			Long key = new Long(detail.getSalesInvoice());
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

		//String attr = "";

		iterator = list.iterator();
		while(iterator.hasNext()) {
			SalesInvoice salesInvoice = (SalesInvoice) iterator.next();
			Long key = new Long(salesInvoice.getIndex());

			if (map.containsKey(key)) {
				List dets = (List) map.get(key);
				Iterator iter = dets.iterator();
				double total = 0;

				while(iter.hasNext()) {
					SalesItem det = (SalesItem) iter.next();

					total += det.getAmount();
				}
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
				.getJournalStandardSettingWithAccount(IConstants.PAYMENT_PROJECT_COST);

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
		GenericMapper mapper = MasterMap.obtainMapperFor(PmtProjectCost.class);
		mapper.setActiveConn(connection);

		Iterator iterator = list.iterator();
		while(iterator.hasNext()) {
			PmtProjectCost payment = (PmtProjectCost) iterator.next();

			mapper.doUpdate(payment);
		}
	}
}
