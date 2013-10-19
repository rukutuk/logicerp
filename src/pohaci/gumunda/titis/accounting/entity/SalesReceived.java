package pohaci.gumunda.titis.accounting.entity;

import java.util.ArrayList;
import java.util.List;
import pohaci.gumunda.titis.accounting.beginningbalance.BeginningAccountReceivable;
import pohaci.gumunda.titis.accounting.cgui.BaseCurrency;
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

public class SalesReceived extends TransactionTemplateEntity implements
		GeneralReceipt {
	long index;

	// Transaction trans;

	SalesInvoice invoice;

	BeginningAccountReceivable beginningBalance;

	String customerStatus;

	CashAccount cashAccount;

	BankAccount bankAccount;

	Currency salesArCurr;

	Currency tax23Curr;

	Currency vatCurr;

	Currency bankChargesCurr;

	Currency retentionCurr;

	Currency translationCurr;

	// Employee empOriginator;
	// Employee empApproved;
	// Employee empReceived;
	double salesArexChRate;

	double salesAramount;

	double tax23Percent;

	double tax23Exchrate;

	double vatExchrate;

	double tax23Amount;

	double vatAmount;

	double bankChargesAmount;

	double bankChargesExchRate;

	double retentionAmount;

	double translationAmount;

	String receiveTo;

	public String getReceiveTo() {
		return receiveTo;
	}

	public void setReceiveTo(String receiveTo) {
		this.receiveTo = receiveTo;
	}

	public SalesReceived() {
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
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

	/*
	 * public Transaction getTrans() { return trans; } public void
	 * setTrans(Transaction trans) { this.trans = trans; }
	 */
	public String getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

	/*
	 * public void setDateApproved(Date dateApproved) { this.dateApproved =
	 * dateApproved; }
	 *
	 * public void setDateReceived(Date dateReceived) { this.dateReceived =
	 * dateReceived; }
	 */
	public SalesInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(SalesInvoice invoice) {
		this.invoice = invoice;
	}

	/*
	 * public String getReferenceno() { return referenceNo; } public void
	 * setReferenceno(String referenceno) { this.referenceNo = referenceno; }
	 */
	public double getSalesARAmount() {
		return salesAramount;
	}

	public void setSalesARAmount(double salesAramount) {
		this.salesAramount = salesAramount;
	}

	public Currency getSalesARCurr() {
		return salesArCurr;
	}

	public void setSalesARCurr(Currency salesArCurr) {
		this.salesArCurr = salesArCurr;
	}

	public double getSalesARExchRate() {
		return salesArexChRate;
	}

	public void setSalesARExchRate(double salesArexChRate) {
		this.salesArexChRate = salesArexChRate;
	}

	public double getTax23Amount() {
		return tax23Amount;
	}

	public void setTax23Amount(double tax23Amount) {
		this.tax23Amount = tax23Amount;
	}

	public double getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}

	public double getTax23ExchRate() {
		return tax23Exchrate;
	}

	public void setTax23ExchRate(double tax23Exchrate) {
		this.tax23Exchrate = tax23Exchrate;
	}

	public double getVatExchRate() {
		return vatExchrate;
	}

	public void setVatExchRate(double vatExchrate) {
		this.vatExchrate = vatExchrate;
	}

	public double getTax23Percent() {
		return tax23Percent;
	}

	public void setTax23Percent(double tax23Percent) {
		this.tax23Percent = tax23Percent;
	}

	/*
	 * public Employee getEmpapproved() { return empApproved; } public void
	 * setEmpapproved(Employee empapproved) { this.empApproved = empapproved; }
	 * public Employee getEmporiginator() { return empOriginator; } public void
	 * setEmporiginator(Employee emporiginator) { this.empOriginator =
	 * emporiginator; } public Employee getEmpreceived() { return empReceived; }
	 * public void setEmpreceived(Employee empreceived) { this.empReceived =
	 * empreceived; }
	 */
	public Currency getTax23Curr() {
		return tax23Curr;
	}

	public void setTax23Curr(Currency tax23Curr) {
		this.tax23Curr = tax23Curr;
	}

	public Currency getVatCurr() {
		return vatCurr;
	}

	public void setVatCurr(Currency vatCurr) {
		this.vatCurr = vatCurr;
	}

	public Object vgetReceiveType() {
		return "Account Receivable Received";
	}

	public Object vgetReceiptNo() {
		return this.referenceNo;
	}

	public Object vgetReceiptDate() {
		return this.transactionDate;
	}

	public Object vgetReceiveBy() {
		return this.empReceived;
	}

	public Object vgetReceiveAccount() {
		return this.bankAccount;
	}

	public Object vgetUnitCode() {
		if (unit != null)
			return this.unit.toString();
		return "";
	}

	public Object vgetStatus() {
		if (this.status == 0)
			return "Not Submitted";
		else if (this.status == 1)
			return "Submitted";
		else if (this.status == 2)
			return "Submitted";
		else if (this.status == 3)
			return "Posted";
		return "";
	}

	public Object vgetSubmittedDate() {
		return this.submitDate;
	}

	public Object vgetReceiveFrom() {
		return this.empReceived;
	}

	public void submit(long sessionId, java.sql.Connection conn)
			throws Exception {
		// CREATE THE TRANSACTION 1 : PENJUALAN
		Currency baseCurrency = BaseCurrency
				.createBaseCurrency(conn, sessionId);
		baseCurrency.setIsBase(true);

		ArrayList detailList = new ArrayList();
		VariableAccountSetting vas = null;

		// siapkan variable-nya
		double sales = getSalesARAmount();
		double tax = getTax23Amount();
		double vat = getVatAmount();
		double retention = getRetentionAmount();
		double translation = getTranslationAmount();
		double ar = sales + vat - retention;

		if (allowNumberRounding) {
			NumberRounding nr = new NumberRounding(
					NumberRounding.NUMBERROUNDING_ROUND, 2);
			ar = nr.round(ar);
			tax = nr.round(tax);
		}

		double exchRate  = getSalesARExchRate();

		// selisih kurs

		if (translation > 0) {
			vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_TRANSLATION_GAIN);
		} else if (translation < 0) {
			vas = VariableAccountSetting.createVariableAccountSetting(conn, sessionId, IConstants.ATTR_VARS_TRANSLATION_LOSS);
		}

		SalesInvoice inv = getInvoice();
		BeginningAccountReceivable bar = getBeginningBalance();
		
		if (translation != 0) {
			// selisih kurs
			TransactionDetail translationDetail = createNewTransDet(vas
					.getAccount(), translation, baseCurrency,
					1);
			detailList.add(translationDetail);


			// perubahan di piutang usaha
			vas = VariableAccountSetting.createVariableAccountSetting(conn,
					sessionId, IConstants.ATTR_VARS_AR);
			if (inv!=null) {
				TransactionDetail translationDetail2 = createNewTransDet(vas
						.getAccount(), -translation, baseCurrency,
						1, inv.getProject().getCustomer().getIndex());
				detailList.add(translationDetail2);
			} else {
				TransactionDetail translationDetail2 = createNewTransDet(vas
						.getAccount(), -translation, baseCurrency,
						1, bar.getProject().getCustomer().getIndex());
				detailList.add(translationDetail2);
			}
		}

		// piutang usaha
		vas = VariableAccountSetting.createVariableAccountSetting(conn,
				sessionId, IConstants.ATTR_VARS_AR);
		
		
		//double vatPercent = 0;

		if (inv != null) {
			exchRate = inv.getSalesExchRate();
			if (inv.getSalesCurr().getIndex() != getSalesARCurr().getIndex()) {
				if (getSalesARCurr().getIndex() == baseCurrency.getIndex()) {
					// rcv Rp, inv $
					exchRate = 1;
				} else {
					// rcv $, inv Rp
					exchRate = getSalesARExchRate();
				}
			}

			TransactionDetail arDetail = createNewTransDet(vas.getAccount(),
					ar, getSalesARCurr(), exchRate, inv.getProject()
							.getCustomer().getIndex());
			detailList.add(arDetail);

			//vatPercent = inv.getVatPercent();
		} else {
			exchRate = bar.getExchangeRate();
			if (bar.getCurrency().getIndex() != getSalesARCurr().getIndex()) {
				if (getSalesARCurr().getIndex() == baseCurrency.getIndex())
					exchRate = 1;
				else
					exchRate = getSalesARExchRate();
			}

			TransactionDetail arDetail = createNewTransDet(vas.getAccount(),
					ar, getSalesARCurr(), exchRate, bar.getProject()
							.getCustomer().getIndex());
			detailList.add(arDetail);

			//vatPercent = 10; // terpaksa dipaksa
		}

		String attr = "";


		// ppn keluaran atau ppn perhitungan, untuk wapu
		if (getCustomerStatus().equals("WAPU")) {

			/*
			 * double sales = ar / (1 + (vatPercent / 100)); vat = sales *
			 * (vatPercent / 100);
			 */
			vat = getVatAmount();
			if (allowNumberRounding) {
				NumberRounding nr = new NumberRounding(
						NumberRounding.NUMBERROUNDING_ROUND, 2);
				vat = nr.round(vat);
			}

			vas = VariableAccountSetting.createVariableAccountSetting(conn,
					sessionId, IConstants.ATTR_VARS_OUT_VAT);

			double er = 1;
			if (inv != null) {
				exchRate = inv.getVatExchRate();
				if (inv.getSalesCurr().getIndex() != getSalesARCurr()
						.getIndex()) {
					if (getSalesARCurr().getIndex() == baseCurrency.getIndex())
						// inv = $, rcv = Rp
						exchRate = 1;
					else
						// inv = Rp, rcv = $
						exchRate = getSalesARExchRate();
				}

			} else {
				exchRate = bar.getExchangeRate();
				if (bar.getCurrency().getIndex() != getSalesARCurr().getIndex()) {
					if (getSalesARCurr().getIndex() == baseCurrency.getIndex())
						exchRate = 1;
					else
						exchRate = getSalesARExchRate();
				}
			}

			// dulunya aku pake inv.getVatCurr()
			// sekarang aku pake getVatCurr()
			TransactionDetail vatDetail = createNewTransDet(vas.getAccount(),
					vat * exchRate, getVatCurr(), er);
			detailList.add(vatDetail);

			if (getReceiveTo().equals("Cash"))
				attr = IConstants.ATTR_AR_RCV_CASH_WAPU;
			else
				attr = IConstants.ATTR_AR_RCV_BANK_WAPU;
		} else {
			if (getReceiveTo().equals("Cash"))
				attr = IConstants.ATTR_AR_RCV_CASH_NONWAPU;
			else
				attr = IConstants.ATTR_AR_RCV_BANK_NONWAPU;
		}

		// tax art 22/23
		double er = 1;
		exchRate = getTax23ExchRate();
		if (inv != null) {
			//exchRate = inv.getSalesExchRate();
			if (inv.getSalesCurr().getIndex() != getSalesARCurr().getIndex()) {
				if (getSalesARCurr().getIndex() == baseCurrency.getIndex())
					// inv = $, rcv = Rp
					exchRate = 1;
				else
					// inv = Rp, rcv = $
					exchRate = getSalesARExchRate();
			}
		} else {
			//exchRate = bar.getExchangeRate();
			if (bar.getCurrency().getIndex() != getSalesARCurr().getIndex()) {
				if (getSalesARCurr().getIndex() == baseCurrency.getIndex())
					exchRate = 1;
				else
					exchRate = getSalesARExchRate();
			}
		}

		vas = VariableAccountSetting.createVariableAccountSetting(conn,
				sessionId, IConstants.ATTR_VARS_TAX23);
		TransactionDetail taxDetail = createNewTransDet(vas.getAccount(), tax
				* exchRate, getTax23Curr(), er);
		detailList.add(taxDetail);

		// bank charges
		// tambahan per 14/04/08
		double bankCharges = getBankChargesAmount();

		// untuk semua
			// exchange rate selalu yang current kan?
			exchRate = getBankChargesExchRate();

			vas = VariableAccountSetting.createVariableAccountSetting(conn,
					sessionId, IConstants.ATTR_VARS_BANK_CHARGES);
			TransactionDetail bankChargesDetail = createNewTransDet(vas.getAccount(), bankCharges
					, getSalesARCurr(), exchRate);
			detailList.add(bankChargesDetail);




		// terima di bank

		double rcv = 0;
		if (getCustomerStatus().equals("WAPU"))
			rcv = sales - retention - tax - bankCharges;
		else
			rcv = ar - tax - bankCharges;

		CashBankAccount cba = null;
		String type = "";
		String code = "";
		if (getReceiveTo().equals("Cash")){
			cba = getCashAccount();
			type = ReferenceNoGeneratorHelper.CASH_IN;
			code = ((CashAccount)cba).getCode();
		}else{
			cba = getBankAccount();
			type = ReferenceNoGeneratorHelper.BANK_IN;
			code = ((BankAccount)cba).getCode();
		}

		// tambahan untuk selisih kurs
		/*if (getSalesARCurr().getIndex() != baseCurrency.getIndex()) {
			double exch = getSalesARExchRate();

			translation /= er;

			NumberRounding nr = new NumberRounding(NumberRounding.NUMBERROUNDING_ROUND, 2);
			translation = nr.round(translation);
		}

		rcv += translation;*/

		if (allowNumberRounding) {
			NumberRounding nr = new NumberRounding(
					NumberRounding.NUMBERROUNDING_ROUND, 2);
			rcv = nr.round(rcv);
		}

		TransactionDetail rcvDetail = createNewTransDet(cba.getAccount(), rcv,
				cba.getCurrency(), getSalesARExchRate(), cba.getIndex());
		detailList.add(rcvDetail);

		JournalStandardSettingPickerHelper helper = new JournalStandardSettingPickerHelper(
				conn, sessionId, IDBConstants.MODUL_ACCOUNTING);

		List journalStdList = helper.getJournalStandardSettingWithAccount(
				IConstants.SALES_AR_RECEIVE, attr);

		// harusnya dapat satu, yang lain abaikan aja... berarti user salah
		// milih hehehehehe
		JournalStandardSetting setting = (JournalStandardSetting) journalStdList
				.get(0);
		JournalStandard journal = setting.getJournalStandard();

		// get the reference no lah
		ReferenceNoGeneratorHelper noGenerator = new ReferenceNoGeneratorHelper(
				conn, sessionId, IDBConstants.MODUL_ACCOUNTING,
				getReferenceNo());

		setReferenceNo(noGenerator.createCashOrBankReferenceNo(
				type, code,
				getTransactionDate()));

		// this is waktunya...
		Transaction trans = createNewTrans(journal);
		TransactionDetail[] details = (TransactionDetail[]) detailList
				.toArray(new TransactionDetail[detailList.size()]);

		TransactionDetailPreparingHelper transHelper = new TransactionDetailPreparingHelper();
		details = transHelper.prepareJournalStandardTransactionDetail(journal
				.getJournalStandardAccount(), details, getUnit(),
				igetBaseCurrency(), 1, false);

		trans.setTransactionDetail(details);

		// save lah
		AccountingBusinessLogic logic = new AccountingBusinessLogic(conn);
		trans = logic.createTransactionData(sessionId,
				IDBConstants.MODUL_ACCOUNTING, trans, trans
						.getTransactionDetail());

		// get everything related
		setStatus((short) StateTemplateEntity.State.VERIFIED);
		setSubmitDate(trans.getVerifyDate());
		setTrans(trans);

		System.out.println("DONE");
	}

	public BeginningAccountReceivable getBeginningBalance() {
		return beginningBalance;
	}

	public void setBeginningBalance(BeginningAccountReceivable beginningBalance) {
		this.beginningBalance = beginningBalance;
	}

	public double getBankChargesAmount() {
		return bankChargesAmount;
	}

	public void setBankChargesAmount(double bankChargesAmount) {
		this.bankChargesAmount = bankChargesAmount;
	}

	public Currency getBankChargesCurr() {
		return bankChargesCurr;
	}

	public void setBankChargesCurr(Currency bankChargesCurr) {
		this.bankChargesCurr = bankChargesCurr;
	}

	public double getBankChargesExchRate() {
		return bankChargesExchRate;
	}

	public void setBankChargesExchRate(double bankChargesExchRate) {
		this.bankChargesExchRate = bankChargesExchRate;
	}

	/**
	 * @return the translationCurr
	 */
	public Currency getTranslationCurr() {
		return translationCurr;
	}

	/**
	 * @param translationCurr the translationCurr to set
	 */
	public void setTranslationCurr(Currency translationCurr) {
		this.translationCurr = translationCurr;
	}

	/**
	 * @return the translationAmount
	 */
	public double getTranslationAmount() {
		return translationAmount;
	}

	/**
	 * @param translationAmount the translationAmount to set
	 */
	public void setTranslationAmount(double translationAmount) {
		this.translationAmount = translationAmount;
	}

	/**
	 * @return Returns the retentionAmount.
	 */
	public double getRetentionAmount() {
		return retentionAmount;
	}

	/**
	 * @param retentionAmount The retentionAmount to set.
	 */
	public void setRetentionAmount(double retentionAmount) {
		this.retentionAmount = retentionAmount;
	}

	/**
	 * @return Returns the retentionCurr.
	 */
	public Currency getRetentionCurr() {
		return retentionCurr;
	}

	/**
	 * @param retentionCurr The retentionCurr to set.
	 */
	public void setRetentionCurr(Currency retentionCurr) {
		this.retentionCurr = retentionCurr;
	}
}
