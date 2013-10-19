package pohaci.gumunda.titis.accounting.entity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import pohaci.gumunda.titis.accounting.cgui.CashBankAccount;
import pohaci.gumunda.titis.accounting.cgui.Currency;
import pohaci.gumunda.titis.accounting.cgui.IConstants;
import pohaci.gumunda.titis.accounting.cgui.JournalStandard;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSetting;
import pohaci.gumunda.titis.accounting.cgui.JournalStandardSettingPickerHelper;
import pohaci.gumunda.titis.accounting.cgui.StateTemplateEntity;
import pohaci.gumunda.titis.accounting.cgui.Transaction;
import pohaci.gumunda.titis.accounting.cgui.TransactionDetail;
import pohaci.gumunda.titis.accounting.cgui.TransactionTemplateEntity;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.helper.ReferenceNoGeneratorHelper;
import pohaci.gumunda.titis.accounting.helper.TransactionDetailPreparingHelper;
import pohaci.gumunda.titis.accounting.logic.AccountingBusinessLogic;
import pohaci.gumunda.titis.application.NumberRounding;
import pohaci.gumunda.titis.hrm.cgui.Organization;

public class PmtUnitBankCashTrans extends TransactionTemplateEntity implements GeneralVoucher {
	long index;
	String paymentSource; // kas atau bank
	CashAccount cashAccount;
	BankAccount bankAccount;
	String chequeNo;
	Date chequeDueDate;
	String payTo; 
	CashAccount rcvCashAccount;
	BankAccount rcvBankAccount;

	double amount;
	//Unit unit;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Date getChequeDueDate() {
		return chequeDueDate;
	}
	public void setChequeDueDate(Date chequeDueDate) {
		this.chequeDueDate = chequeDueDate;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public String getPaymentSource() {
		return paymentSource;
	}
	public void setPaymentSource(String paymentSource) {
		this.paymentSource = paymentSource;
	}
	
	public BankAccount getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	public CashAccount getCashAccount() {
		return cashAccount;
	}
	public void setCashAccount(CashAccount cashAccount) {
		this.cashAccount = cashAccount;
	}
	public BankAccount getRcvBankAccount() {
		return rcvBankAccount;
	}
	public void setRcvBankAccount(BankAccount rcvBankAccount) {
		this.rcvBankAccount = rcvBankAccount;
	}
	public CashAccount getRcvCashAccount() {
		return rcvCashAccount;
	}
	public void setRcvCashAccount(CashAccount rcvCashAccount) {
		this.rcvCashAccount = rcvCashAccount;
	}
	// View part - common voucher columns
	public Object vgetPaymentSource() {
		if (isSourceBank())
			return getBankAccount();
		else
			return getCashAccount();
	}

	public Object vgetVoucherType() {
		return "Unit bank/cash transfer";
	}
	public Object vgetVoucherNo() {
		return this.getReferenceNo();
	}
	
	public Object vgetVoucherDate() {
		return getTransactionDate();
	}
	public Object vgetPayTo() {
		boolean isPayToBank="BANK".equalsIgnoreCase(getPayTo());
		if (isPayToBank)
			return getRcvBankAccount();
		else
			return getRcvCashAccount();
	}

	public boolean isSourceBank() {
		return "BANK".equalsIgnoreCase(getPaymentSource());
	}
	
	public Object vgetOriginator() {
		return this.getEmpOriginator();
	}
	public Object vgetApprovedBy() {
		return this.getEmpApproved();
	}
	public Object vgetReceivedBy() {
		return this.getEmpReceived();
	}
	public Object vgetUnitCode() {
		/*if (isBank())
		{
			if (getBankAccount()==null)
				return null;
			return getBankAccount().getUnit();
		}
		else
		{
			if (getCashAccount()==null)
				return null;
			return getCashAccount().getUnit();
		}*/
		return getUnit();
	}
	
	public Object vgetStatus() {
		int status = getStatus();
		switch (status){
		case 0:
			return "Not Submitted";
		case 1:
			return "Submitted";
		case 2:
			return "Submitted";
		case 3:
			return "Posted";
		default:
			return "";
		}			
	}
	public Object vgetSubmitDate() {
		return getSubmitDate();
	}
	
	String n(Object o)
	{
		if (o==null)
			return "";
		else
			return o.toString();
	}
	public String toString()
	{
		return "PmtUnitBankCashTrans: pay to " + n(vgetPayTo()) + " from "+ n(vgetPaymentSource()) +  " vouch no " + n(vgetVoucherNo()) 
		+ " cheq no " + n(getChequeNo());  
	}
	
	public boolean simulationSubmit(long sessionId,java.sql.Connection conn) {
		// CREATE THE TRANSACTION
		
		CashBankAccount sourceAcct = getSourceAccount();
		CashBankAccount receivingAcct = getReceivingAccount();
		
		boolean forceValue = false;
		double sourceExchRate = 1;
		double rcvExchRate = 1;
		
		sourceExchRate = getExchangeRate();
		
		rcvExchRate = getExchangeRate();
		
		double er = sourceExchRate;
		if(sourceAcct.getCurrency().getIndex()==igetBaseCurrency().getIndex())
			er = 1;
		
		TransactionDetail detailSource = createNewTransDet(sourceAcct.getAccount(), getAmount(), sourceAcct.getCurrency(), er);
		// amount di negatifken karena kan keluar duit...
		
		detailSource.setValue(-detailSource.getValue());
		forceValue = true;
		
		ArrayList detailList = new ArrayList();
		detailList.add(detailSource);
		
		// penentuan amount penerima tergantung pada currency-nya
		double amt = getAmount();
		
		if(sourceAcct.getCurrency().getIndex()!=receivingAcct.getCurrency().getIndex()){
			if(sourceAcct.getCurrency().getIndex()==igetBaseCurrency().getIndex()){
				// Rp ke $
				amt = amt / rcvExchRate;
			}else{
				// $ ke Rp
				amt = amt * rcvExchRate;
			}
		}
		
		// satu unit kah?
		boolean sameUnit = false;
		if(sourceAcct.getUnit().getIndex()==receivingAcct.getUnit().getIndex()){
			// satu unit
			er = rcvExchRate;
			if(receivingAcct.getCurrency().getIndex()==igetBaseCurrency().getIndex())
				er = 1;
			TransactionDetail detailReceiving = createNewTransDet(receivingAcct.getAccount(), amt, receivingAcct.getCurrency(), er);
			sameUnit = true;
			detailList.add(detailReceiving);
		} else{
			// beda unit 
			// ntar dihitung di ceking aja....
			// ga jadi ding
			// diitung disini aja kayaknya
			
		}

		// GET THE STANDARD JOURNAL
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, IDBConstants.MODUL_ACCOUNTING);

		String attr = "";
		//String type = "";
		//String code = "";
		if(sourceAcct instanceof BankAccount){
			// dari bank
			//type = ReferenceNoGeneratorHelper.BANK_OUT;
			//code = ((BankAccount)sourceAcct).getCode();
			if(sameUnit)
				attr = IConstants.ATTR_BANK_SAME_UNIT;
			else
				attr = IConstants.ATTR_BANK_OTHER_UNIT;
		} else {
			// dari kas
			//type = ReferenceNoGeneratorHelper.CASH_OUT;
			//code = ((CashAccount)sourceAcct).getUnit().getCode();
			if(sameUnit)
				attr = IConstants.ATTR_CASH_SAME_UNIT;
			else
				attr = IConstants.ATTR_CASH_OTHER_UNIT;
		}
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
					attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		// this is waktunya...
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, forceValue);

		// terlanjur basah ya basah sekalian....
		if(sameUnit)
			return confirmBalance(details);
		else{
			details = ifToAnotherUnit(sessionId,conn);
			return confirmBalance(details);
		}
	}
	
	private TransactionDetail[] ifToAnotherUnit(long sessionId, Connection conn) {
		double rcvExchangeRate = getExchangeRate();
		
		// oke, buat si penerimanya dulu
		CashBankAccount receivingAccount = getReceivingAccount();
		CashBankAccount sourceAccount = getSourceAccount();
		
		double amt = getAmount();
		if (sourceAccount.getCurrency().getIndex() != receivingAccount.getCurrency().getIndex()) {
			// hanya berlaku untuk beda currency aja
			if (receivingAccount.getCurrency().getIndex() == igetBaseCurrency()
					.getIndex()) {
				amt = amt * rcvExchangeRate;
			} else {
				amt = amt / rcvExchangeRate;
			}
		}
		
		double er = rcvExchangeRate;
		if(receivingAccount.getCurrency().getIndex()==igetBaseCurrency().getIndex())
			er = 1;
		
		TransactionDetail receivingDetail = 
			createNewTransDet(receivingAccount.getAccount(), amt, receivingAccount.getCurrency(), er);
		ArrayList detailList = new ArrayList();
		detailList.add(receivingDetail);
		
		// GET THE STANDARD JOURNAL
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, 
					IDBConstants.MODUL_ACCOUNTING);
		
		String attr = "";
		if(receivingAccount instanceof BankAccount){
			// artinya diterima di bank
			attr = IConstants.ATTR_RCV_BANK;
		}else{
			// buat yang cash
			attr = IConstants.ATTR_RCV_CASH;
		}
	
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(
					IConstants.RECEIVE_UNIT_BANK_CASH_TRANSFER,
					attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();		
		
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, true);
		
		return details;
	}
	
	private boolean confirmBalance(TransactionDetail[] details) {
		List list = Arrays.asList(details);		
		double[] total = {0.0, 0.0};		
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			TransactionDetail detail = (TransactionDetail) iter.next();			
			total = getTotalValue(total, detail);
		}		
		if(total[0]==total[1])
			return true;
		
		return false;
	}
	
	private double[] getTotalValue(double[] total, TransactionDetail detail) {
		double amount = getAmount(detail);
		double value = Math.abs(amount);
		if(((detail.getAccount().getBalance()==0)&&(detail.getValue()>=0)) || ((detail.getAccount().getBalance()==1)&&(detail.getValue()<0))) {
			//debet		
			total[0] += value;
		}else{
			//credit
			total[1] += value;
		}
		return total;
	}
	private double getAmount(TransactionDetail detail) {
		return ConvertIntoBaseCurrency(detail.getValue(), detail.getCurrency(), detail.getExchangeRate());
	}

	private double ConvertIntoBaseCurrency(double value, Currency currency, double exchangeRate) {
		double val = 0;
		
		NumberRounding rounding = 
			new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
		value = rounding.round(value);
		
		if(currency.getIndex()==igetBaseCurrency().getIndex()){
			val = value;
		}else{
			val = value * exchangeRate;
		}
		
		return val;
	}
	
	public void submit(long sessionId,java.sql.Connection conn) throws Exception {		
		// CREATE THE TRANSACTION
		
		CashBankAccount sourceAcct = getSourceAccount();
		CashBankAccount receivingAcct = getReceivingAccount();
		
		boolean forceValue = false;
		double sourceExchRate = 1;
		double rcvExchRate = 1;
		
		//if(sourceAcct.getCurrency().getIndex()!=igetBaseCurrency().getIndex())
			sourceExchRate = getExchangeRate();
		
		//if(receivingAcct.getCurrency().getIndex()!=igetBaseCurrency().getIndex())
			rcvExchRate = getExchangeRate();
		
		double exchRateSource = sourceExchRate;
		if(sourceAcct.getCurrency().getIndex()==igetBaseCurrency().getIndex())
			exchRateSource = 1;
		TransactionDetail detailSource = createNewTransDet(sourceAcct.getAccount(), getAmount(), sourceAcct.getCurrency(), exchRateSource, sourceAcct.getIndex());
		// amount di negatifken karena kan keluar duit...
		
		//if((sourceAcct instanceof BankAccount)&&(sourceAcct.getUnit().getIndex()==receivingAcct.getUnit().getIndex())){
			detailSource.setValue(-detailSource.getValue());
			forceValue = true;
		//}
		ArrayList detailList = new ArrayList();
		detailList.add(detailSource);
		
		// penentuan amount penerima tergantung pada currency-nya
		double amt = getAmount();
		
		if(sourceAcct.getCurrency().getIndex()!=receivingAcct.getCurrency().getIndex()){
			if(sourceAcct.getCurrency().getIndex()==igetBaseCurrency().getIndex()){
				// Rp ke $
				amt = amt / rcvExchRate;
			}else{
				// $ ke Rp
				amt = amt * rcvExchRate;
			}
		}
		
		// satu unit kah?
		boolean sameUnit = false;
		if(sourceAcct.getUnit().getIndex()==receivingAcct.getUnit().getIndex()){
			// satu unit
			double exchRateRcv = rcvExchRate;
			if(receivingAcct.getCurrency().getIndex()==igetBaseCurrency().getIndex())
				exchRateRcv = 1;
			TransactionDetail detailReceiving = createNewTransDet(receivingAcct.getAccount(), amt, receivingAcct.getCurrency(), exchRateRcv, receivingAcct.getIndex());
			sameUnit = true;
			detailList.add(detailReceiving);
		} else{
			// beda unit 
			// ntar dihitung di ceking aja....
			// ga jadi ding
			// diitung disini aja kayaknya
			
			/*VariableAccountSetting vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_RC_REL);
			TransactionDetail detailReceiving = createNewTransDet(vas.getAccount(), getAmount());
			sameUnit = false;
			detailList.add(detailReceiving);*/
		}

		// GET THE STANDARD JOURNAL
		
		JournalStandardSettingPickerHelper helper =
			new JournalStandardSettingPickerHelper(conn,sessionId, IDBConstants.MODUL_ACCOUNTING);

		String attr = "";
		String type = "";
		String code = "";
		if(sourceAcct instanceof BankAccount){
			// dari bank
			type = ReferenceNoGeneratorHelper.BANK_OUT;
			code = ((BankAccount)sourceAcct).getCode();
			if(sameUnit)
				attr = IConstants.ATTR_BANK_SAME_UNIT;
			else
				attr = IConstants.ATTR_BANK_OTHER_UNIT;
		} else {
			// dari kas
			type = ReferenceNoGeneratorHelper.CASH_OUT;
			code = ((CashAccount)sourceAcct).getUnit().getCode();
			if(sameUnit)
				attr = IConstants.ATTR_CASH_SAME_UNIT;
			else
				attr = IConstants.ATTR_CASH_OTHER_UNIT;
		}
		
		List journalStdList = 
			helper.getJournalStandardSettingWithAccount(IConstants.PAYMENT_UNIT_BANKCASH_TRANSFER,
					attr);
		
		// harusnya dapat satu, yang lain abaikan aja... berarti user salah milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList.get(0);
		JournalStandard journal = setting.getJournalStandard();
		
		// get the reference no first
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(conn, sessionId, IDBConstants.MODUL_ACCOUNTING, getReferenceNo());
		setReferenceNo(noGenerator.createCashOrBankReferenceNo(type, code, getTransactionDate()));
		
		// this is waktunya...
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList.toArray(new TransactionDetail[detailList.size()]);
		
		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(
				journal.getJournalStandardAccount(), details, getUnit(), igetBaseCurrency(), 1, forceValue);

		trans.setTransactionDetail(details);
		
		// save lah
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,IDBConstants.MODUL_ACCOUNTING,trans,trans.getTransactionDetail());
		
		// get everything related
		setStatus((short)StateTemplateEntity.State.VERIFIED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);

		System.out.println("DONE");
	}
	
	CashBankAccount getSourceAccount() {
		if (getCashAccount()!=null)
			return getCashAccount();
		if (getBankAccount()!=null)
			return getBankAccount();
		return null;
	}
	CashBankAccount getReceivingAccount() {
		if (getRcvCashAccount()!=null)
			return getRcvCashAccount();
		if (getRcvBankAccount()!=null)
			return getRcvBankAccount();
		return null;
	}
	public boolean isReceivingBank() {
		return "BANK".equalsIgnoreCase(getPayTo());
	}
/*	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}*/
	public String getPayTo() {
		return this.payTo;
	}
	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}
	public Organization getDepartment() {
		return null;
	}
	
}
