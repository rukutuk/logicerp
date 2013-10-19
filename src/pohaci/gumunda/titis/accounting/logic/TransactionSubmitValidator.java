package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.BaseCurrency;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.PostingPanel;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class TransactionSubmitValidator {
	private Transaction transaction;
	private boolean valid = false;
	private Currency baseCurrency;
	private ArrayList errorMessageList;
	private Connection connection;

	/**
	 * @param transaction
	 */
	public TransactionSubmitValidator(Transaction transaction,
			Connection connection, long sessionId) {
		this.transaction = transaction;
		this.connection = connection;

		this.baseCurrency = BaseCurrency.createBaseCurrency(connection,
				sessionId);
		this.errorMessageList = new ArrayList();
	}

	/**
	 * validasi transaksi sebelum disubmit 1. cek balance sesuai yang di
	 * postingPanel 2. simulasi posting
	 *
	 * @return
	 */
	public void validate() {
		if (!checkBalance()) {
			errorMessageList.add("It is unbalance transaction.");
			return;
		}

		if (!simulatePosting()) {
			return;
		}

		valid = true;
	}

	private boolean checkBalance() {
		TransactionDetail[] details = transaction.getTransactionDetail();
		List detailList = Arrays.asList(details);

		Iterator detailIter = detailList.iterator();

		double[] total = { 0, 0 };

		while (detailIter.hasNext()) {
			TransactionDetail detail = (TransactionDetail) detailIter.next();

			total = getTotalValue(total, detail);
		}

		// total
		NumberRounding nr = new NumberRounding(
				NumberRounding.NUMBERROUNDING_ROUND, 2);
		total[0] = nr.round(total[0]);
		total[1] = nr.round(total[1]);

		return (total[0] == total[1]);
	}

	private double[] getTotalValue(double[] total, TransactionDetail detail) {
		double amount = getAmount(detail);
		double value = Math.abs(amount);
		/*NumberRounding nr = new NumberRounding(
				NumberRounding.NUMBERROUNDING_ROUND, 2);*/
		// value = nr.round(value);
		if (((detail.getAccount().getBalance() == 0) && (detail.getValue() >= 0))
				|| ((detail.getAccount().getBalance() == 1) && (detail
						.getValue() < 0))) {
			// debet
			total[0] += value;
		} else {
			// credit
			total[1] += value;
		}
		return total;
	}

	private double getAmount(TransactionDetail detail) {
		double amount;
		if (PostingPanel.SHOW_IN_BASE_CURRENCY)
			amount = convertIntoBaseCurrency(detail.getValue(), detail
					.getCurrency(), detail.getExchangeRate());
		else
			amount = detail.getValue();
		return amount;
	}

	private double convertIntoBaseCurrency(double value, Currency currency,
			double exchangeRate) {
		double val = 0;

		if (currency.getIndex() == baseCurrency.getIndex()) {
			val = value;
		} else {
			val = value * exchangeRate;
		}

		return val;
	}

	/**
	 * Sebenarnya ini hanya untuk mensimulasikan update status posting
	 *
	 * @return
	 */
	private boolean simulatePosting() {
		GenericMapper mapper;

		// update the original table
		// but, get the table first

		// 1st, get the standard journal setting
		mapper = MasterMap.obtainMapperFor(JournalStandardSetting.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("JOURNAL="
				+ transaction.getJournalStandard().getIndex());

		if (list.size() == 0) {
			errorMessageList
					.add("There is no journal standard setting related to this transaction.");
			return false;
		}

		// 2nd, get the table list based on standard journal setting list
		Iterator iter = list.iterator();

		while (iter.hasNext()) {
			JournalStandardSetting journalStandardSetting = (JournalStandardSetting) iter
					.next();

			String appName = journalStandardSetting.getApplication();
			List classList = getClassList(appName);

			if (classList.size() == 0) {
				errorMessageList
						.add("There is no class mapped to this transaction.");
				return false;
			}

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

				if (objectList.size() == 0) {
					errorMessageList
							.add("There is no original object related to this transaction.");
					return false;
				}
			}
		}

		return true;
	}

	private List getClassList(String application) {
		ArrayList list = new ArrayList();
		Iterator iter = TransactionMapper.getList().iterator();
		while (iter.hasNext()) {
			List myList = (List) iter.next();
			if (((String) myList.get(0)).equals(application))
				list.add((Class) myList.get(1));
		}
		return list;
	}

	/**
	 * @return Returns the valid.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @return Returns the errorMessageList.
	 */
	public ArrayList getErrorMessageList() {
		return errorMessageList;
	}

}
