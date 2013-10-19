/**
 *
 */
package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.cgui.BaseCurrency;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.entity.SalesReceived;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class BeginningAccountReceivableBusinessLogic extends
		BeginningBalanceBusinessLogic {
	Currency baseCurrency = null;
	public BeginningAccountReceivableBusinessLogic(Connection connection,
			long sessionId) {
		super(connection, sessionId);
		baseCurrency = BaseCurrency.createBaseCurrency(connection, sessionId);
		baseCurrency.setIsBase(true);
		entityMapper = MasterMap
				.obtainMapperFor(BeginningAccountReceivable.class);
		entityMapper.setActiveConn(connection);
	}

	public List getOutstanding() {
		// specific account - hanya untuk account receivable
		VariableAccountSetting vas = VariableAccountSetting
				.createVariableAccountSetting(this.connection, this.sessionId,
						IConstants.ATTR_VARS_AR);
		List resultList = new ArrayList();
		List list = entityMapper.doSelectWhere("account="
				+ vas.getAccount().getIndex());
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningAccountReceivable bb = (BeginningAccountReceivable) iterator
					.next();
			System.err.println(bb.getProject().getUnit().getDescription());
			Transaction trans = findTransaction(bb.getProject().getUnit());
			if (trans != null) {
				bb.setTrans(trans);
				bb.showReferenceNo(true);
				if (isOutstanding(bb)) {
					resultList.add(bb);
				}
			}
		}
		return resultList;
	}

	public boolean isOutstanding(BeginningAccountReceivable bb) {
		double balance = getAccountReceivableBalance(bb);
		return (balance != 0);
	}

	public double getAccountReceivableBalance(BeginningAccountReceivable bb) {
		double totalReceived = getAccumulatedReceived(bb);
		double ar = bb.getAccValue();
		double val = ar - totalReceived;
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		val = nr.round(val);
		return val;
	}

	public double getAccumulatedReceived(BeginningAccountReceivable bb) {
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesReceived.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("BEGINNINGBALANCE=" + bb.getIndex()
				+ " AND STATUS=3");
		Iterator iter = list.iterator();
		Currency bbCurr = bb.getCurrency();
		double amt = 0;
		while (iter.hasNext()) {
			SalesReceived rcv = (SalesReceived) iter.next();
			Currency rcvCurr = rcv.getSalesARCurr();
			//double receive = rcv.getSalesARAmount();
			double receive = rcv.getSalesARAmount() + rcv.getVatAmount() - rcv.getRetentionAmount();

			if(!bbCurr.getSymbol().equals(rcvCurr.getSymbol())){
				// hanya yang beda
    			if(rcvCurr.getSymbol().equals(baseCurrency.getSymbol())){
    				// receive = Rp; bb = $;
    				double invExchRate = bb.getExchangeRate();
    				receive = receive / invExchRate;
    			}else{
    				// receive = $; bb = Rp;
    				double rcvExchRate = rcv.getSalesARExchRate();
    				receive = receive * rcvExchRate;
    			}
    		}
			NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
			receive = nr.round(receive);
			amt += receive;
			
			// translation

			double translation = rcv.getTranslationAmount();

			if (bbCurr.getIndex()==baseCurrency.getIndex()) {
				// inv Rp

			} else {
				// inv $
				double bbExchRate = bb.getExchangeRate();
				translation = (translation / bbExchRate);
				nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
				translation = nr.round(translation);

			}

			amt -= translation;

		}
		return amt;
	}

	public double getRetention(BeginningAccountReceivable bb) {
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesReceived.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("BEGINNINGBALANCE=" + bb.getIndex()
				+ " AND STATUS=3");
		Iterator iter = list.iterator();
		Currency bbCurr = bb.getCurrency();
		double amt = 0;
		while (iter.hasNext()) {
			SalesReceived rcv = (SalesReceived) iter.next();
			Currency rcvCurr = rcv.getSalesARCurr();
			double retention = rcv.getRetentionAmount();
			if(!bbCurr.getSymbol().equals(rcvCurr.getSymbol())){
				// hanya yang beda
    			if(rcvCurr.getSymbol().equals(baseCurrency.getSymbol())){
    				// receive = Rp; bb = $;
    				double invExchRate = bb.getExchangeRate();
    				retention = retention / invExchRate;
    			}else{
    				// receive = $; bb = Rp;
    				double rcvExchRate = rcv.getSalesARExchRate();
    				retention = retention * rcvExchRate;
    			}
    		}
			NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
			retention = nr.round(retention);
			amt += retention;

			// translation

			double translation = rcv.getTranslationAmount();

			if (bbCurr.getIndex()==baseCurrency.getIndex()) {
				// inv Rp

			} else {
				// inv $
				double bbExchRate = bb.getExchangeRate();
				translation = (translation / bbExchRate);
				nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
				translation = nr.round(translation);

			}

			amt -= translation;
		}
		return amt;
	}
}
