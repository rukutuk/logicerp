package pohaci.gumunda.titis.accounting.beginningbalance;

import pohaci.gumunda.titis.accounting.cgui.CompanyLoan;
import pohaci.gumunda.titis.accounting.entity.Unit;

public class BeginningLoan extends BeginningBalanceSheetDetail {
	protected CompanyLoan m_companyLoan;

	public CompanyLoan getCompanyLoan() {
		return m_companyLoan;
	}

	public void setCompanyLoan(CompanyLoan companyLoan) {
		m_companyLoan = companyLoan;
	}
	public Object subsidiaryStr()
	{
		if (m_companyLoan==null) return null;
		return m_companyLoan.toString();
	}

	protected Unit unit() {
		return m_companyLoan.getUnit();
	}

	protected long indexSubsidiary() {
		if(getCompanyLoan()==null)
			return -1;
		return getCompanyLoan().getIndex();
	}

	
	
}
