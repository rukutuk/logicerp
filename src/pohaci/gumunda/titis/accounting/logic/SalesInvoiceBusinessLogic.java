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
import pohaci.gumunda.titis.accounting.entity.SalesInvoice;
import pohaci.gumunda.titis.accounting.entity.SalesReceived;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class SalesInvoiceBusinessLogic extends TransactionBusinessLogic {
	Currency baseCurrency = null;
	public SalesInvoiceBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		baseCurrency = BaseCurrency.createBaseCurrency(connection, sessionId);
		baseCurrency.setIsBase(true);
		entityMapper = MasterMap.obtainMapperFor(SalesInvoice.class);
		entityMapper.setActiveConn(connection);
	}

	public List getOutstandingList(){
		List resultList = new ArrayList();
		List list = entityMapper.doSelectWhere("status=3");
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			SalesInvoice inv = (SalesInvoice) iterator.next();
    		if(isOutstanding(inv)){
    			resultList.add(inv);
    		}
		}
		return resultList;
	}

	public boolean isOutstanding(Object obj){
		SalesInvoice inv = (SalesInvoice) obj;
		double balance = getAccountReceivableBalance(inv);
		return (balance != 0);
	}

	public double getAccumulatedReceived(SalesInvoice inv) {
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesReceived.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("INVOICE=" + inv.getIndex()
				+ " AND STATUS=3");
		Iterator iter = list.iterator();
		double amt = 0;
		Currency invCurr = inv.getSalesCurr();
		while (iter.hasNext()) {
			SalesReceived rcv = (SalesReceived) iter.next();
			Currency rcvCurr = rcv.getSalesARCurr();
			double receive = rcv.getSalesARAmount() + rcv.getVatAmount() - rcv.getRetentionAmount();
			if(!invCurr.getSymbol().equals(rcvCurr.getSymbol())){
				// hanya yang beda
    			if(rcvCurr.getSymbol().equals(baseCurrency.getSymbol())){
    				// receive = Rp; invoice = $;
    				double invExchRate = inv.getSalesExchRate();
    				receive = receive / invExchRate;
    			}else{
    				// receive = $; invoice = Rp;
    				double rcvExchRate = rcv.getSalesARExchRate();
    				receive = receive * rcvExchRate;
    			}
    		}
    		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
			receive = nr.round(receive);
			amt += receive;
			// translation
			double translation = rcv.getTranslationAmount();
			if (invCurr.getIndex()==baseCurrency.getIndex()) {
				// inv Rp
			} else {
				// inv $
				double invExchRate = inv.getSalesExchRate();
				translation = (translation / invExchRate);
				nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
				translation = nr.round(translation);
			}
			amt -= translation;
		}
		return amt;
	}

	public double getAccountReceivableBalance(SalesInvoice inv){
		double totalReceived = getAccumulatedReceived(inv);
		double sales = inv.getSalesAmount();
		double dp = inv.getDownPaymentAmount();
		sales = sales - dp;
		double vat = inv.getVatAmount();
		double ar = sales + vat;
		double value = ar - totalReceived;
		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		value = nr.round(value);
		return value;
	}

	public List getAccountReceivableList(Object obj, SalesReceived entity){
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesReceived.class);
		mapper.setActiveConn(connection);
		if(obj instanceof SalesInvoice){
			SalesInvoice inv = (SalesInvoice) obj;
			List resultList = mapper.doSelectWhere("INVOICE=" + inv.getIndex() + " AND STATUS=3 AND AUTOINDEX != " + entity.getIndex() );
			return resultList;
		}else if(obj instanceof BeginningAccountReceivable){
			BeginningAccountReceivable bar = (BeginningAccountReceivable) obj;
			List resultList = mapper.doSelectWhere("BEGINNINGBALANCE=" + bar.getIndex() + " AND STATUS=3 AND AUTOINDEX != " + entity.getIndex());
			return resultList;
		}
		return new ArrayList();
	}

	public double getRetention(SalesInvoice inv) {
		GenericMapper mapper = MasterMap.obtainMapperFor(SalesReceived.class);
		mapper.setActiveConn(connection);
		List list = mapper.doSelectWhere("INVOICE=" + inv.getIndex()
				+ " AND STATUS=3");
		Iterator iter = list.iterator();
		double amt = 0;
		Currency invCurr = inv.getSalesCurr();
		while (iter.hasNext()) {
			SalesReceived rcv = (SalesReceived) iter.next();
			Currency rcvCurr = rcv.getSalesARCurr();
			double retention = rcv.getRetentionAmount();
			if(!invCurr.getSymbol().equals(rcvCurr.getSymbol())){
				// hanya yang beda
    			if(rcvCurr.getSymbol().equals(baseCurrency.getSymbol())){
    				// receive = Rp; invoice = $;
    				double invExchRate = inv.getSalesExchRate();
    				retention = retention / invExchRate;
    			}else{
    				// receive = $; invoice = Rp;
    				double rcvExchRate = rcv.getSalesARExchRate();
    				retention = retention * rcvExchRate;
    			}
    		}
    		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
    		retention = nr.round(retention);
			amt += retention;
		}
		return amt;
	}
}
