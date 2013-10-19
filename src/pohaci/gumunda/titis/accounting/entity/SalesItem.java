package pohaci.gumunda.titis.accounting.entity;

public class SalesItem {
	long index;
	long salesInvoice;
	String m_number;
	String m_description;
	String m_specification;
	double m_qty;
	double m_unitPrice;
	double m_amount;
	double m_personAmount;
	String m_personDesc;
	
	SalesItemDetail[] salesItemDetails; 
	                
	public SalesItem(){		
	}
	public double getAmount() {
		return m_amount;
	}
	public void setAmount(double amount) {
		m_amount = amount;
	}
	public String getDescription() {
		return m_description;
	}
	public void setDescription(String description) {
		m_description = description;
	}
	public double getQty() {
		return m_qty;
	}
	public void setQty(double qty) {
		m_qty = qty;
	}
	
	public double getUnitPrice() {
		return m_unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		m_unitPrice = unitPrice;
	}
	public String getSpecification() {
		return m_specification;
	}
	public void setSpecification(String m_specification) {
		this.m_specification = m_specification;
	}
	public SalesItemDetail[] igetSalesItemDetails() {
		return salesItemDetails;
	}
	public void isetSalesItemDetails(SalesItemDetail[] salesItemDetails) {
		this.salesItemDetails = salesItemDetails;
	}
	
	public long getSalesInvoice() {
		return salesInvoice;
	}
	public void setSalesInvoice(long salesInvoice) {
		this.salesInvoice = salesInvoice;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public String getNumber() {
		return m_number;
	}
	public void setNumber(String number) {
		this.m_number = number;
	}
	public double getPersonAmount() {
		return m_personAmount;
	}
	public void setPersonAmount(double personAmount) {
		this.m_personAmount = personAmount;
	}
	public String getPersonDesc() {
		return m_personDesc;
	}
	public void setPersonDesc(String personDesc) {
		this.m_personDesc = personDesc;
	}
}
