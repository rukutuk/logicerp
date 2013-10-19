package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountPayable;
import pohaci.gumunda.titis.accounting.cgui.BaseCurrency;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.VariableAccountSetting;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class BeginningAccountPayableBusinessLogic extends BeginningBalanceBusinessLogic{
	
	public BeginningAccountPayableBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);		
		entityMapper = MasterMap
		.obtainMapperFor(BeginningAccountPayable.class);
		entityMapper.setActiveConn(connection);
	}
	public List getOutstanding() {
		VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(this.connection, 
				this.sessionId,IConstants.ATTR_VARS_AP);
		List resultList = new ArrayList();
		List list = entityMapper.doSelectWhere("account="
				+ vas.getAccount().getIndex());
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			BeginningAccountPayable bb = (BeginningAccountPayable) iterator.next();
			Transaction trans = findTransaction(bb.getProject().getUnit());
			if (trans != null) {
				bb.setTrans(trans);
				bb.showReferenceNo(true);
				if (isOutstanding(bb)){
					resultList.add(bb);
				}
			}
		}
		return resultList;
	}
	
	public boolean isOutstanding(BeginningAccountPayable bb) {
		double balance = getAccountPayableBalance(bb);
		return (balance > 0);
	}
	
	public double getAccountPayableBalance(BeginningAccountPayable bb) {
		double totalReceived = getAccumulatedPayable(bb);
		double ar = bb.getAccValue();		
		double balance = ar - totalReceived;
		return balance;
	}
	
	public double getAccumulatedPayable(BeginningAccountPayable bb) {
		GenericMapper mapper = MasterMap.obtainMapperFor(PurchaseApPmt.class);
		mapper.setActiveConn(connection);
		
		Currency receiptCurrency = bb.getCurrency();
		Currency baseCurrency = BaseCurrency.createBaseCurrency(this.connection, this.sessionId);
		
		String strWhere ="BEGINNINGBALANCE=" + bb.getIndex() + " AND STATUS=3";
		System.err.println(strWhere);
		List list = mapper.doSelectWhere(strWhere);		
		Iterator iter = list.iterator();
		double amt = 0;
		while (iter.hasNext()) {
			PurchaseApPmt pmt = (PurchaseApPmt) iter.next();			
			
			double payment = pmt.getAppMtAmount();
    		Currency paymentCurrency = pmt.getAppMtCurr();
    		
    		if(!paymentCurrency.getSymbol().equals(receiptCurrency.getSymbol())){
    			if(paymentCurrency.getSymbol().equals(baseCurrency.getSymbol())){
    				// payment = Rp; receipt = $;
    				double receiptExchangeRate = bb.getExchangeRate();
    				payment = payment / receiptExchangeRate;
    			}else{
    				// payment = $; receipt = Rp;
    				double paymentExchangeRate = pmt.getAppMtExchRate();
    				payment = payment * paymentExchangeRate;
    			}
    		}
    		
    		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
			payment = nr.round(payment);
    		amt += payment;
		}		
		return amt;
	}
	
}
