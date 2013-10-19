package pohaci.gumunda.titis.accounting.entity;

import pohaci.gumunda.titis.hrm.cgui.Organization;
import pohaci.gumunda.titis.hrm.cgui.TaxArt21Submit;

public class TaxArt21DeptValue {

	/**
	 * @param args
	 */
	long index;
	TaxArt21Submit taxArt21Submit;
	Organization organization;
	Account account;
	double accvalue;
	public TaxArt21DeptValue(long index,TaxArt21Submit taxArt21Submit,Organization organization,Account account,double accvalue){		
		this.index = index;
		this.taxArt21Submit=taxArt21Submit;
		this.organization = organization;
		this.account = account;
		this.accvalue = accvalue;
	}
	public TaxArt21DeptValue(){		
	}
	
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public double getAccvalue() {
		return accvalue;
	}
	public void setAccvalue(double accvalue) {
		this.accvalue = accvalue;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public Organization getDepartment() {
		return organization;
	}
	public void setDepartment(Organization organization) {
		this.organization = organization;
	}
	public TaxArt21Submit getTaxArt21Submit() {
		return taxArt21Submit;
	}
	public void setTaxArt21Submit(TaxArt21Submit taxArt21Submit) {
		this.taxArt21Submit = taxArt21Submit;
	}
	

}
