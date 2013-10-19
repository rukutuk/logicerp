package pohaci.gumunda.titis.accounting.entity;

public class PayrollPmtSlryHoPay {
	/*"PAYROLLPMTSLRYHO"               Fixed (38,0),
	"PAYROLLCOMPPAY"               Fixed (38,0),
	FOREIGN KEY "PAYROLLPMTSLRYHO_PAYROLLPMTSLRYH"	("PAYROLLPMTSLRYHO") REFERENCES "SAMPURNAUSER"."PAYROLLPMTSLRYHO" ("AUTOINDEX") ON DELETE  RESTRICT,
	FOREIGN KEY "TRANSACTIONPOSTED_PAYROLLPMTSLRY"	("PAYROLLCOMPPAY") REFERENCES "SAMPURNAUSER"."TRANSACTIONPOSTED" ("AUTOINDEX") ON DELETE  RESTRICT*/
	PayrollPmtSlryHo m_payrollPmtSlryHo;
	// FK kelas TRANSACTIONPOSTED tanyakan ke irwan
	public PayrollPmtSlryHoPay(){
		
	}
	public PayrollPmtSlryHo getPayrollPmtSlryHo() {
		return m_payrollPmtSlryHo;
	}
	public void setPayrollPmtSlryHo(PayrollPmtSlryHo payrollPmtSlryHo) {
		m_payrollPmtSlryHo = payrollPmtSlryHo;
	}
}
