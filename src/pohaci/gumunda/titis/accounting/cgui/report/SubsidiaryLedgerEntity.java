/**
 *
 */
package pohaci.gumunda.titis.accounting.cgui.report;

import java.util.Date;

/**
 * @author dark-knight
 *
 */
public class SubsidiaryLedgerEntity implements Comparable {
	private Date transactionDate;
	private Date postedDate;
	private String description;
	private String referenceNo;
	private double value;
	private double exchangeRate;

	/**
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}
	/**
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the referenceNo
	 */
	public String getReferenceNo() {
		return referenceNo;
	}
	/**
	 * @param referenceNo the referenceNo to set
	 */
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	/**
	 * @return the exchangeRate
	 */
	public double getExchangeRate() {
		return exchangeRate;
	}
	/**
	 * @param exchangeRate the exchangeRate to set
	 */
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg) {
		if (arg instanceof SubsidiaryLedgerEntity) {
			SubsidiaryLedgerEntity compared = (SubsidiaryLedgerEntity) arg;

			int val = getTransactionDate().compareTo(compared.getTransactionDate());
			if (val!=0)
				return val;
			else {
				val = getPostedDate().compareTo(compared.getPostedDate());
				if (val!=0)
					return val;
				else {
					val = getReferenceNo().compareTo(compared.getReferenceNo());
					return val;
				}
			}
		}
		return -1;
	}
	/**
	 * @return the postedDate
	 */
	public Date getPostedDate() {
		return postedDate;
	}
	/**
	 * @param postedDate the postedDate to set
	 */
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}


}
