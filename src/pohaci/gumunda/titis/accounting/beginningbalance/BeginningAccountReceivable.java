package pohaci.gumunda.titis.accounting.beginningbalance;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.project.cgui.Customer;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class BeginningAccountReceivable extends BeginningBalanceSheetDetail {
	protected Customer m_customer = Customer.nullObject();
	protected ProjectData m_project = ProjectData.nullObject();

	public Customer getCustomer() {
		return m_customer;
	}

	public void setCustomer(Customer customer) {
		m_customer = customer;
	}
	
	public Object subsidiaryStr() {
		if (m_customer==null)
			return null;
		return m_customer.getName();
	}

	public ProjectData getProject() {
		return m_project;
	}

	public void setProject(ProjectData project) {
		m_project = project;
	}

	protected Unit unit() {
		return m_project.getUnit();
	}

	// akibatnya
	protected long indexSubsidiary() {
		if(getCustomer()==null)
			return -1;
		return getCustomer().getIndex();
	}
}
