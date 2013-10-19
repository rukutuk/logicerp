package pohaci.gumunda.titis.accounting.beginningbalance;

import pohaci.gumunda.titis.accounting.entity.Unit;
import pohaci.gumunda.titis.project.cgui.Partner;
import pohaci.gumunda.titis.project.cgui.ProjectData;

public class BeginningAccountPayable extends BeginningBalanceSheetDetail {
	protected Partner m_partner = Partner.nullObject();
	protected ProjectData m_project = ProjectData.nullObject();
	public Partner getPartner() {
		return m_partner;
	}

	public void setPartner(Partner partner) {
		m_partner = partner;
	}

	public ProjectData getProject() {
		return m_project;
	}

	public void setProject(ProjectData project) {
		m_project = project;
	}

	public Object subsidiaryStr() {
		if (getPartner()==null)
			return null;
		return getPartner().toString();
		
		
	}

	protected Unit unit() {
		return m_project.getUnit();
	}

	// akibatnya
	protected long indexSubsidiary() {
		if (getPartner()==null)
			return -1;
		return getPartner().getIndex();
	}
	
	
}
