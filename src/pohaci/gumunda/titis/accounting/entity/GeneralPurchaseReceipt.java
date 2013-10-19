package pohaci.gumunda.titis.accounting.entity;

public interface GeneralPurchaseReceipt {
	Object vgetReceiptNo();
	Object vgetReceiptDate();
	Object vgetProjectCode();
	Object vgetInvoiceNo();
	Object vgetInvoiceDate();
	Object vgetSupplier();
	Object vgetSubmittedDate();
	Object vgetStatus();	
	long  vgetIndex();
	String vgetSymbolCurrency();
}
