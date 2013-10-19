package pohaci.gumunda.titis.hrm.cgui;

/**
 * <p>Title: Project Management</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author dewax4
 * @version 1.0
 */

import javax.swing.*;

import java.awt.event.ActionListener;
import java.sql.Connection;

import pohaci.gumunda.titis.application.*;
import pohaci.gumunda.titis.hrm.dbapi.IDBConstants;
import pohaci.gumunda.titis.hrm.logic.HRMBusinessLogic;

public class EmployeeCellEditor extends ObjectCellEditor implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SearchEmployeeDetailDlg m_empdlg = null;

	public EmployeeCellEditor(JFrame owner, Connection conn, long sessionid) {
	    super(owner, conn, sessionid);
	}

	public void done() {
		m_empdlg = new SearchEmployeeDetailDlg(m_owner, m_conn, m_sessionid);
		m_empdlg.setVisible(true);
		
		if (m_empdlg.getResponse() == JOptionPane.OK_OPTION) {
			Employee[] employee = m_empdlg.getEmployee();
			if (employee.length > 0) 
            {
                HRMBusinessLogic logic = new HRMBusinessLogic(this.m_conn);
                try {
                    employee[0].maxEmployment(logic.getMaxEmployment(this.m_sessionid,
                            IDBConstants.MODUL_MASTER_DATA,employee[0].getIndex()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
				setObject(employee[0]);
			}
		}
		
	}
}
