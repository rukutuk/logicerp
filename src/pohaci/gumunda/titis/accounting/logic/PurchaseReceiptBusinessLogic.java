package pohaci.gumunda.titis.accounting.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.BaseCurrency;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.LookupPurchasePicker;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.PurchaseApPmt;
import pohaci.gumunda.titis.accounting.entity.PurchaseReceipt;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

public class PurchaseReceiptBusinessLogic extends TransactionBusinessLogic{

	public PurchaseReceiptBusinessLogic(Connection connection, long sessionId) {
		super(connection, sessionId);
		entityMapper = MasterMap.obtainMapperFor(PurchaseReceipt.class);
		entityMapper.setActiveConn(connection);
	}
	
	public List getOutstandingList(){    
	    List resultList = new ArrayList();		
		List list = entityMapper.doSelectWhere("status=3");		
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			PurchaseReceipt pr = (PurchaseReceipt) iterator.next();    	
			
			if(isOutstanding(pr))
				resultList.add(pr);
			/*if (totAmount<pr.getAmount()){   		
    			resultList.add(pr);
			} */   		
		}			
		return resultList;
	}	

	
	public boolean isOutstanding(PurchaseReceipt pr) {
		double balance = getAccountPayableBalance(pr);
		return balance > 0;
	}

	private double getAccountPayableBalance(PurchaseReceipt pr) {
		double paid = getAccumulatedPayment(pr);
		double purchase = pr.getAmount();
		double vat = pr.getVatAmount();
		double ap = purchase + vat;
		return ap - paid ;
	}

	public double getAccumulatedPayment(PurchaseReceipt pr){
		GenericMapper pmtMapper = MasterMap.obtainMapperFor(PurchaseApPmt.class);
		pmtMapper.setActiveConn(connection);
		
		Currency receiptCurrency = pr.getApCurr();
		Currency baseCurrency = BaseCurrency.createBaseCurrency(this.connection, this.sessionId);
		
		List list = pmtMapper.doSelectWhere(IDBConstants.ATTR_PURCHASE_RECEIPT+"="+pr.getIndex() +
	    		" AND " + IDBConstants.ATTR_STATUS + "=3");
		double totAmount = 0;
	    Iterator iterator = list.iterator();
	    while(iterator.hasNext()){
    		PurchaseApPmt purcAppPmt = (PurchaseApPmt) iterator.next();
    		double payment = purcAppPmt.getAppMtAmount();
    		Currency paymentCurrency = purcAppPmt.getAppMtCurr();
    		
    		if(!paymentCurrency.getSymbol().equals(receiptCurrency.getSymbol())){
    			if(paymentCurrency.getSymbol().equals(baseCurrency.getSymbol())){
    				// payment = Rp; receipt = $;
    				double receiptExchangeRate = pr.getApexChRate();
    				payment = payment / receiptExchangeRate;
    			}else{
    				// payment = $; receipt = Rp;
    				double paymentExchangeRate = purcAppPmt.getAppMtExchRate();
    				payment = payment * paymentExchangeRate;
    			}
    		}
    		NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
			payment = nr.round(payment);
    		totAmount += payment;
	    }
		return totAmount;
	}
	
	public boolean hasAnotherUnpostedTransaction(PurchaseReceipt receipt, PurchaseApPmt payment){
		GenericMapper mapper = MasterMap.obtainMapperFor(PurchaseApPmt.class);
		mapper.setActiveConn(connection);
		String selectWhere = IDBConstants.ATTR_STATUS + "<3 AND " + IDBConstants.ATTR_PURCHASE_RECEIPT + "=" + receipt.getIndex() + " AND " +
		IDBConstants.ATTR_AUTOINDEX + " not in (" +payment.getIndex()+ ")";		
		List rs=mapper.doSelectWhere(selectWhere);
		if (rs.size()>0){
			return true;		 
		}
		return false;
	}
	
	public boolean cekTotalPayment(PurchaseApPmt entity,LookupPurchasePicker purchRcptNoComp,double paymentAmount){
		PurchaseReceipt a = (PurchaseReceipt)purchRcptNoComp.getObject();	
		GenericMapper mapper=MasterMap.obtainMapperFor(PurchaseApPmt.class);
		mapper.setActiveConn(this.connection);
		String selectWhere = IDBConstants.ATTR_STATUS + "=3 AND " + IDBConstants.ATTR_PURCHASE_RECEIPT + "=" + a.getIndex();
		System.err.println(selectWhere);
		List rs=mapper.doSelectWhere(selectWhere);
		double total = 0;
		if (rs.size()>0){
			for (int i=0;i<rs.size();i++){
				PurchaseApPmt b=(PurchaseApPmt)rs.get(i);
				total +=b.getAppMtAmount();
			}			
		}		
		total += paymentAmount;
		if (total > a.getAmount()){
			System.err.println("total :" + total);
			return true;
		}
		return false;		
	}
}
