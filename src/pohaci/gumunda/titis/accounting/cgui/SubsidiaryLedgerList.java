package pohaci.gumunda.titis.accounting.cgui;

import java.sql.Connection;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import pohaci.gumunda.titis.accounting.entity.BankAccount;
import pohaci.gumunda.titis.accounting.entity.CashAccount;
import pohaci.gumunda.titis.accounting.entity.SubsidiaryAccountSetting;
import pohaci.gumunda.titis.hrm.cgui.Employee;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class SubsidiaryLedgerList extends JList {
	
	private static final long serialVersionUID = 1L;
	Connection m_conn = null;
	long m_sessionid = -1;
	SubsidiaryAccountSetting m_subsidiariAcc;
	Object m_obj;
	
	public SubsidiaryLedgerList(Connection conn, long sessionid) {
		m_conn = conn;
		m_sessionid = sessionid;
		setModel(new DefaultListModel());
		initData();
	}
	
	public SubsidiaryLedgerList(Connection conn, long sessionid,Object obj) {		
		m_conn = conn;
		m_sessionid = sessionid;
		m_obj = obj;
		setModel(new DefaultListModel());
		initData1();
	}
	

	void initData() {
		DefaultListModel model = (DefaultListModel)getModel();
		model.removeAllElements();		
			model.addElement("");
	}
	
	void initData1() {		
		DefaultListModel model = (DefaultListModel)getModel();
		model.removeAllElements();
		if (m_obj instanceof Employee[]) {
			Employee[] emp = (Employee[]) m_obj;
			for(int i = 0; i < emp.length; i ++){				
				model.addElement(emp[i]);
			}
		}
		else if (m_obj instanceof BankAccount[]) {
			BankAccount[] bankAcc = (BankAccount[]) m_obj;
			for(int i = 0; i < bankAcc.length; i ++){				
				model.addElement(bankAcc[i]);
			}
		}
		else if (m_obj instanceof CashAccount[]) {
			CashAccount[] cashAcc = (CashAccount[]) m_obj;
			for(int i = 0; i < cashAcc.length; i ++){				
				model.addElement(cashAcc[i]);
			}
		}
		else if (m_obj instanceof Customer[]) {
			Customer[] cust = (Customer[]) m_obj;
			for(int i = 0; i < cust.length; i ++){				
				model.addElement(cust[i]);
			}
		}
		else if (m_obj instanceof CompanyLoan[]) {
			CompanyLoan[] loan = (CompanyLoan[]) m_obj;
			for(int i = 0; i < loan.length; i ++){				
				model.addElement(loan[i]);
			}
		}
		else if (m_obj instanceof Partner[]) {
			Partner[] partner = (Partner[]) m_obj;
			for(int i = 0; i < partner.length; i ++){				
				model.addElement(partner[i]);
			}
		}
		else if (m_obj instanceof ProjectData[]) {
			ProjectData[] projects = (ProjectData[]) m_obj;
			for(int i = 0; i < projects.length; i ++){				
				model.addElement(projects[i]);
			}
		}
		
	}	
}