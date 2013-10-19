/**
 * 
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.entity.PmtUnitBankCashTrans;
import pohaci.gumunda.titis.accounting.entity.RcvUnitBankCashTrns;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class PaymentUnitBankCashTransferBusinessLogic extends
		TransactionBusinessLogic {

	public PaymentUnitBankCashTransferBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(PmtUnitBankCashTrans.class);
		entityMapper.setActiveConn(connection);
	}

	public List getOutstandingList() {
		
		// get all
		String clause = "STATUS=3";
		List paymentList = entityMapper.doSelectWhere(clause);
		
		// prepare another mapper
		GenericMapper rcvMapper = MasterMap.obtainMapperFor(RcvUnitBankCashTrns.class);
		rcvMapper.setActiveConn(connection);
		
		Iterator iterator = paymentList.iterator();
		List resultList = new ArrayList();
		while(iterator.hasNext()){
			PmtUnitBankCashTrans trans = (PmtUnitBankCashTrans) iterator.next();
			
			// only with different unit of source and target
			CashBankAccount source = null;
			CashBankAccount target = null;
			if(trans.isSourceBank())
				source = trans.getBankAccount();
			else
				source = trans.getCashAccount();
			if(trans.isReceivingBank())
				target = trans.getRcvBankAccount();
			else
				target = trans.getRcvCashAccount();
			
			if(source.getUnit().getIndex()!=target.getUnit().getIndex()){
				// only outstanding payment
				String rcvClause = "TRANSFERFROM=" + trans.getIndex() + " AND STATUS=3";
				List receivedList = rcvMapper.doSelectWhere(rcvClause);
				
				if(receivedList.size()==0)
					resultList.add(trans);
			}
		}
		
		return resultList;
	}
}
