package pohaci.gumunda.titis.accounting.entity;

public class PayrollPmtTax21HoPay {
	/*
	 "PAYROLLPMTTAX21HO"               Fixed (38,0),
	"TAX21PAYABLE"               Fixed (38,0),
	FOREIGN KEY "PAYROLLPMTTAX21HO_PAYROLLPMTTAX2"	("PAYROLLPMTTAX21HO") REFERENCES "SAMPURNAUSER"."PAYROLLPMTTAX21HO" ("AUTOINDEX") ON DELETE  RESTRICT,
	FOREIGN KEY "TRANSACTIONPOSTED_PAYROLLPMTTAX2"	("TAX21PAYABLE") REFERENCES "SAMPURNAUSER"."TRANSACTIONPOSTED" ("AUTOINDEX") ON DELETE  RESTRICT
	 */
	PayrollPmtTax21Ho m_payrollPmtTax21Ho;
	// class TRANSACTIONPOSTED tanya keirwan
	public PayrollPmtTax21HoPay(){
		
	}
	public PayrollPmtTax21Ho getPayrollPmtTax21Ho() {
		return m_payrollPmtTax21Ho;
	}
	public void setPayrollPmtTax21Ho(PayrollPmtTax21Ho payrollPmtTax21Ho) {
		m_payrollPmtTax21Ho = payrollPmtTax21Ho;
	}
}
