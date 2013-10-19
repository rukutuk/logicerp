package pohaci.gumunda.titis.accounting.entity;

public class PayrollPmtEmpInsPay {
	/*"PAYROLLPMTEMPINS"               Fixed (38,0),
	"TAX21PAYABLE"               Fixed (38,0),
	FOREIGN KEY "PAYROLLPMTEMPINSURANCE_PAYROLLPM"	("PAYROLLPMTEMPINS") REFERENCES "SAMPURNAUSER"."PAYROLLPMTEMPINSURANCE" ("AUTOINDEX") ON DELETE  RESTRICT,
	FOREIGN KEY "TRANSACTIONPOSTED_PAYROLLPMTEMPI"	("TAX21PAYABLE") REFERENCES "SAMPURNAUSER"."TRANSACTIONPOSTED" ("AUTOINDEX") ON DELETE  RESTRICT*/
	PayrollPmtEmpInsurance	m_payrollPmtEmpIns;
	// TRANSACTIONPOSTED tanyakan ke irwan
	public PayrollPmtEmpInsPay(){
		
	}
	public PayrollPmtEmpInsurance getPayrollPmtEmpIns() {
		return m_payrollPmtEmpIns;
	}
	public void setPayrollPmtEmpIns(PayrollPmtEmpInsurance payrollPmtEmpIns) {
		m_payrollPmtEmpIns = payrollPmtEmpIns;
	}
}
