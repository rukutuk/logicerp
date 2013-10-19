package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningBalance;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class BeginningBalanceBusinessLogic extends TransactionBusinessLogic {

	public BeginningBalanceBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
	}

	public Transaction findTransaction(Unit unit) {
		GenericMapper mapper;
		List list;
		JournalStandard js = findJournalStandard();

		// now find the transaction based on journal standard and unit
		if (js != null) {
			mapper = MasterMap.obtainMapperFor(Transaction.class);
			String strWhere = "unit=" + unit.getIndex()
			+ " and transactioncode=" + js.getIndex() + " and status=3 and void=false";
			System.err.println(strWhere);
			list = mapper.doSelectWhere(strWhere);
			if (list.size() > 0) {
				Transaction transaction = (Transaction) list.get(0); // should be 1
				return transaction;
			} else
				return null;
		}
		return null;
	}

	public JournalStandard findJournalStandard() {
		// get journal standard
		GenericMapper mapper = MasterMap.obtainMapperFor(BeginningBalance.class);
		mapper.setActiveConn(this.connection);
		List list = mapper.doSelectAll();
		BeginningBalance bb = null;
		if (list.size()>0){
			bb= (BeginningBalance) list.get(0); // dipaksa
			JournalStandard js = bb.getJournalStandard();
			return js;
		}
		return null;
	}
	

}
