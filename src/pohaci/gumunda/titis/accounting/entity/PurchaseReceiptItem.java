package pohaci.gumunda.titis.accounting.entity;

public class PurchaseReceiptItem  {
	PurchaseReceipt m_purchaseReceipt;
	String m_description;
	String m_specification;
	double m_qty;
	double m_unitPrice;
	double m_amount;
	//Unit m_unit;
	
	public PurchaseReceiptItem(){		
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
	public PurchaseReceipt getPurchaseReceipt() {
		return m_purchaseReceipt;
	}
	public void setPurchaseReceipt(PurchaseReceipt purchaseReceipt) {
		m_purchaseReceipt = purchaseReceipt;
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
	/*public Unit getUnit() {
		return m_unit;
	}
	public void setUnit(Unit unit) {
		m_unit = unit;
	}*/


	public String getSpecification() {
		return m_specification;
	}


	public void setSpecification(String specification) {
		this.m_specification = specification;
	}

	
}
