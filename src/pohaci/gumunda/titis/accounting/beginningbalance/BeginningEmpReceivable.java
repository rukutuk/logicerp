package pohaci.gumunda.titis.accounting.beginningbalance;

import java.sql.Connection;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.hrm.cgui.Employment;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class BeginningEmpReceivable extends BeginningBalanceSheetDetail {
	protected Employee m_employee;

	public Employee getEmployee() {
		return m_employee;
	}

	public void setEmployee(Employee employee) {
		m_employee = employee;
	}

	public Object subsidiaryStr() {
		if (m_employee==null) return null;
		return m_employee.toString();
	}
    
    public void preload(Connection conn, long sessionid, String modul) {
        Employment e = null;
        try {
            e = new HRMBusinessLogic(conn).getMaxEmployment(sessionid,modul,m_employee.getIndex());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        m_employee.maxEmployment(e);
    }

	protected Unit unit() {
		return m_employee.maxEmployment().getUnit();
	}

	// akibatnya
	protected long indexSubsidiary() {
		if(getEmployee()==null)
			return -1;
		return getEmployee().getIndex();
	}
	
}
