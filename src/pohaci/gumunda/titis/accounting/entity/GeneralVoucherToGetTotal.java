package pohaci.gumunda.titis.accounting.entity;

public interface GeneralVoucherToGetTotal {
	String igetPaymentSource();

	Object vgetVoucherType();

	Object vgetVoucherNo();

	Object vgetVoucherDate();

	Object vgetPayTo();

	Object vgetOriginator();

	Object vgetApprovedBy();

	Object vgetReceivedBy();

	Object vgetUnitCode();

	Object vgetStatus();

	Object vgetSubmitDate();

	long vgetIndex();

	CashAccount vgetCashAccount();

	BankAccount vgetBankAccount();

	String vgetSymbolCurrency();

	String vgetTotal();
}
